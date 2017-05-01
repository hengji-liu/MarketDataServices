package au.edu.unsw.soacourse.mds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarketDataServicesImpl implements MarketDataServices {
	// DEBUG C:\Users\Hengj\AppData\Local\Temp\
	private final String tmpDir = System.getProperty("java.io.tmpdir");

	@Override
	public ImportMarketDataResp importMarketData(ImportMarketDataReq parameters)
			throws ProgramErrorMsg, InvalidSECCodeMsg, InvalidDatesMsg, InvalidURLMsg {
		final String MSG = "Import Market Data Error";
		DateFormat dfUser = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat dfCSV = new SimpleDateFormat("dd-MMM-yyyy");

		// validate sec
		Pattern psec = Pattern.compile("[A-Z]{3,4}");
		Matcher msec = psec.matcher(parameters.sec);
		if (!msec.matches())
			throw ErrorUtils.InvalidSECCodeMsg(MSG, ErrorUtils.SEC_FORMAT);

		// validate startDate endDate
		Pattern pdate = Pattern
				.compile("(((0[1-9]|[12][0-9]|3[01])-((0[13578]|1[02]))|((0[1-9]|[12][0-9]|30)-(0[469]|11))|" //
						+ "(0[1-9]|[1][0-9]|2[0-8])-(02))-([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}))|" //
						+ "(29-02-(([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00)))");
		Matcher msd = pdate.matcher(parameters.startDate);
		Matcher med = pdate.matcher(parameters.endDate);
		if (!msd.matches() || !med.matches())
			throw ErrorUtils.InvalidDatesMsg(MSG, ErrorUtils.DATE_FORMAT);

		Date sDate = null;
		Date eDate = null;
		try {
			sDate = dfUser.parse(parameters.startDate);
			eDate = dfUser.parse(parameters.endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw ErrorUtils.InvalidDatesMsg(MSG, ErrorUtils.DATE_FORMAT);
		}
		if (eDate.before(sDate))
			throw ErrorUtils.InvalidDatesMsg(MSG, ErrorUtils.DATE_BEFORE);

		// get content from url
		BufferedReader reader = null;
		try {
			URL url = new URL(parameters.dataSourceURL);
			reader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.InvalidURLMsg(MSG, ErrorUtils.URL_CONNECT);
		}

		// create a tmpFile
		String uuid = UUID.randomUUID().toString();
		BufferedWriter writer = null;
		File tmpFile = null;
		try {
			tmpFile = new File(tmpDir + uuid);
			writer = new BufferedWriter(new FileWriter(tmpFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_CREATE);
		}

		// filter by sec and dates
		boolean fileEmpty = true;
		try {
			String line = null;

			line = reader.readLine(); // title line
			try {
				writer.write(line);
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_WRITE);
			}

			while ((line = reader.readLine()) != null) {
				String field[] = line.split(",", -1);
				String s = field[0];
				Date d = dfCSV.parse(field[1]);
				if (s.equals(parameters.sec) //
						&& (d.before(eDate) || d.equals(eDate)) //
						&& (d.after(sDate) || d.equals(sDate)) //
				) {
					// write temp file
					try {
						writer.write(line);
						writer.newLine();
					} catch (IOException e) {
						e.printStackTrace();
						throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_WRITE);
					}

					if (fileEmpty)
						fileEmpty = false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw ErrorUtils.InvalidDatesMsg(MSG, ErrorUtils.SOURCE_DATE_PARSE);
		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.URL_READ);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.URL_CLOSE);
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_CLOSE);
			}
		}

		// check file not empty, delete empty file
		if (fileEmpty) {
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.OUTPUT_EMPTY);
		}

		// response
		ImportMarketDataResp resp = new ImportMarketDataResp();
		resp.setEventSetId(uuid);
		return resp;
	}

	@Override
	public DownloadFileResp downloadFile(DownloadFileReq parameters)
			throws ProgramErrorMsg, InvalidFileTypeMsg, InvalidEventSetIdMsg {
		final String MSG = "Download File Error";

		// validate type CSV, XML, HTML
		Pattern ptype = Pattern.compile("CSV|XML|HTML");
		Matcher mtype = ptype.matcher(parameters.fileType);
		if (!mtype.matches())
			throw ErrorUtils.InvalidFileTypeMsg(MSG, ErrorUtils.FILETYPE_FORMAT);

		// validate id
		Pattern pid = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
		Matcher mid = pid.matcher(parameters.eventSetId);
		if (!mid.matches())
			throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_FORMAT);

		String asIs = null;

		if ("XML".equals(parameters.fileType))
			asIs = csv2xml(tmpDir + parameters.eventSetId, MSG);
		if ("CSV".equals(parameters.fileType)) {
			File file = new File(tmpDir + parameters.eventSetId);
			byte[] encoded = null;
			try {
				encoded = Files.readAllBytes(Paths.get(file.toURI()));
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_NONEXIST);
			}
			asIs = new String(encoded);
		}
		if ("HTML".equals(parameters.fileType))
			asIs = csv2html(tmpDir + parameters.eventSetId, MSG);

		// response
		DownloadFileResp resp = new DownloadFileResp();
		resp.setData(asIs);
		return resp;
	}

	@Override
	public ConvertMarketDataResp convertMarketData(ConvertMarketDataReq parameters)
			throws ProgramErrorMsg, InvalidCurrencyCodeMsg, InvalidEventSetIdMsg {
		final String MSG = "Convert Market Data Error";
		String targetCurrency = parameters.targetCurrency;
		String eventSetId = parameters.eventSetId;

		// validate currency code
		Pattern ptc = Pattern.compile("^[A-Z]{3}$");
		Matcher mtc = ptc.matcher(targetCurrency);
		if (!mtc.matches())
			throw ErrorUtils.InvalidCurrencyCodeMsg(MSG, ErrorUtils.CURRENCY_FORMAT);

		// validate id
		Pattern pid = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");
		Matcher mid = pid.matcher(eventSetId);
		if (!mid.matches())
			throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_FORMAT);

		// get rate from yahoo
		String rate = callYahoo("AUD", targetCurrency, MSG)[0];

		// create temp file
		String uuid = UUID.randomUUID().toString();
		BufferedWriter writer = null;
		File tmpFile = null;
		try {
			tmpFile = new File(tmpDir + uuid);
			writer = new BufferedWriter(new FileWriter(tmpFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_CREATE);
		}

		// open given file
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(tmpDir + eventSetId)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_CLOSE);
			}
			throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_NONEXIST);
		}

		// convert data
		try {
			String line = null;
			line = reader.readLine(); // title line
			try {
				writer.write(line);
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_WRITE);
			}

			while ((line = reader.readLine()) != null) {
				String field[] = line.split(",", -1);

				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMaximumFractionDigits(2);
				nf.setRoundingMode(RoundingMode.UP);

				for (int i = 0; i < 3; i++) {
					if (field.length >= i * 2 + 6 && !"".equals(field[i * 2 + 5])) {
						// field[7] - bid price
						// field[9] - ask price
						if (!Character.isDigit(field[i * 2 + 5].charAt(0))) {
							throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.SOURCE_CONVERTED);
						}
						field[i * 2 + 5] = targetCurrency
								+ nf.format(Double.parseDouble(field[i * 2 + 5]) * Double.parseDouble(rate));
					}
				}

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < field.length; i++) {
					sb.append(field[i]);
					sb.append(",");
				}

				// write into tmpFile
				try {
					writer.write(sb.substring(0, sb.length() - 1));
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
					throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_WRITE);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.URL_READ);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.URL_CLOSE);
			}
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.TEMP_CLOSE);
			}
		}

		// response
		ConvertMarketDataResp resp = new ConvertMarketDataResp();
		resp.setEventSetId(uuid);
		return resp;
	}

	@Override
	public YahooExchangeRateResp yahooExchangeRate(YahooExchangeRateReq parameters)
			throws ProgramErrorMsg, InvalidCurrencyCodeMsg {
		final String MSG = "Yahoo Exchange Rate Error";

		// validate currency code
		Pattern pcc = Pattern.compile("^[A-Z]{3}$");
		Matcher mBase = pcc.matcher(parameters.baseCurrencyCode);
		Matcher mTarget = pcc.matcher(parameters.targetCurrencyCode);
		if (!mBase.matches() || !mTarget.matches())
			throw ErrorUtils.InvalidCurrencyCodeMsg(MSG, ErrorUtils.CURRENCY_FORMAT);

		// get content from yahoo
		String[] rateAndDate = callYahoo(parameters.baseCurrencyCode, parameters.targetCurrencyCode, MSG);

		// response
		YahooExchangeRateResp resp = new YahooExchangeRateResp();
		resp.setRate(rateAndDate[0]);
		resp.setAsAt(rateAndDate[1]);
		return resp;
	}

	private String[] callYahoo(String base, String target, String MSG) throws ProgramErrorMsg, InvalidCurrencyCodeMsg {
		// get rate from yahoo
		StringBuffer sBuffer = new StringBuffer("http://finance.yahoo.com/d/quotes.csv?e=.csv&f=sl1d1t1&s=");
		sBuffer.append(base) //
				.append(target) //
				.append("=X");

		BufferedReader reader = null;
		String string = null;
		try {
			URL url = new URL(sBuffer.toString());
			reader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
			string = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.YAHOO_CONNECT);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.YAHOO_CLOSE);
			}
		}

		// "USDAUD=X",1.3100,"3/24/2017","10:50pm"
		// or
		// "USDAUX=X",N/A,N/A,N/A

		// validate extract data from yahoo
		String field[] = string.split(",", -1);
		String rate = field[1];
		if ("N/A".equals(rate))
			throw ErrorUtils.InvalidCurrencyCodeMsg(MSG, ErrorUtils.CURRENCY_INVALID);

		DateFormat dfInput = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat dfOutput = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = dfInput.parse(field[2].substring(1, field[2].length() - 1));
		} catch (ParseException e) {
			e.printStackTrace();
			throw ErrorUtils.ProgramErrorMsg(MSG, ErrorUtils.YAHOO_DATE_PARSE);
		}

		String rateAndDate[] = { rate, dfOutput.format(date) };
		return rateAndDate;
	}

	private String csv2xml(String file, String MSG) throws ProgramErrorMsg, InvalidEventSetIdMsg {
		StringBuffer sb = new StringBuffer("<Data>\n");

		Scanner s = null;
		try {
			s = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_NONEXIST);
		}

		// header
		ArrayList<String> header = null;
		if (s.hasNextLine()) {
			String line = s.nextLine();
			String item[] = line.split(",", -1);
			for (int i = 0; i < item.length; i++) // xml invalid characters
				item[i] = item[i].replaceAll("[#\\[\\] ]", "");
			header = new ArrayList<>(Arrays.asList(item));
		}

		// data
		while (s.hasNextLine()) {
			sb.append("  <Row>\n");
			String line = s.nextLine();
			String[] field = line.split(",", -1);
			for (int i = 0; i < field.length; i++) {
				if (!"".equals(field[i])) {
					sb.append("    <" + header.get(i) + ">");
					sb.append(field[i]);
					sb.append("</" + header.get(i) + ">\n");
				}
			}
			sb.append("  </Row>\n");
		}

		sb.append("</Data>");
		s.close();
		return sb.toString();
	}

	private String csv2html(String file, String MSG) throws InvalidEventSetIdMsg {
		StringBuffer sb = new StringBuffer("<table border=\"1\">\n");

		Scanner s = null;
		try {
			s = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw ErrorUtils.InvalidEventSetIdMsg(MSG, ErrorUtils.ID_NONEXIST);
		}

		if (s.hasNextLine()) {
			sb.append("  <tr>\n");
			String line = s.nextLine();
			String[] field = line.split(",", -1);
			for (int i = 0; i < field.length; i++) {
				sb.append("    <th>" + field[i] + "</th>\n");
			}
			sb.append("  </tr>\n");
		}

		while (s.hasNextLine()) {
			sb.append("  <tr>\n");
			String line = s.nextLine();
			String[] field = line.split(",", -1);
			for (int i = 0; i < field.length; i++) {
				sb.append("    <td>" + field[i] + "</td>\n");
			}
			sb.append("  </tr>\n");
		}

		sb.append("</table>");
		s.close();
		return sb.toString();
	}
}
