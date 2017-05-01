package au.edu.unsw.soacourse.mds;

public class ErrorUtils {
	// FORMAT
	private static final String FMT = "FMT";
	protected static final String[] ID_FORMAT = { FMT + "_0", "The given event set id is not in a correct format" };
	protected static final String[] SEC_FORMAT = { FMT + "_1",
			"A SEC code should be in uppercase, alphabetics only and 3-4 characters in length" };
	protected static final String[] DATE_FORMAT = { FMT + "_2", "Wrong Date format, sample: 18-07-2001" };
	protected static final String[] FILETYPE_FORMAT = { FMT + "_3",
			"Only provide CSV, XML or HTML, please put in uppercase" };
	protected static final String[] CURRENCY_FORMAT = { FMT + "_4",
			"Currency code is composed of 3 uppercase letters, sample: USD, AUD, CNY" };

	// PROTOCOL INCONSISTENTCY
	private static final String PTL = "PTL";
	protected static final String[] DATE_PARSE = { PTL + "_0", "Failed to parse startDate/endDate" };
	protected static final String[] SOURCE_DATE_PARSE = { PTL + "_1", "A date in the given file is not valid" };
	protected static final String[] YAHOO_DATE_PARSE = { PTL + "_2", "Failed to parse Yahoo's date in the response" };

	// INPUT LOGIC
	private static final String LGC = "LGC";
	protected static final String[] OUTPUT_EMPTY = { LGC + "_0",
			"Cannot match any data entries, please valid SEC code or shift date window" };
	protected static final String[] ID_NONEXIST = { LGC + "_1", "The given event set id does not exist" };
	protected static final String[] DATE_BEFORE = { LGC + "_2", "endDate cannot be before startDate" };
	protected static final String[] SOURCE_CONVERTED = { LGC + "_3", "This file has been converted before" };
	protected static final String[] CURRENCY_INVALID = { LGC + "_4",
			"Failed to get the exchange rate between these two currencies" };

	// SYSTEM IO CREATE/READ/WRITE/CLOSE
	private static final String SIO = "SIO";
	protected static final String[] TEMP_CREATE = { SIO + "_0", "Failed to creat temp file" };
	protected static final String[] TEMP_WRITE = { SIO + "_1", "Failed to write into temp file" };
	protected static final String[] TEMP_CLOSE = { SIO + "_2", "Failed to close temp file" };
	protected static final String[] URL_CONNECT = { SIO + "_3", "Failed to connect to the given source URL" };
	protected static final String[] URL_READ = { SIO + "_4", "Failed to read from the given source URL" };
	protected static final String[] URL_CLOSE = { SIO + "_5", "Failed to close connection with the given source URL" };
	protected static final String[] YAHOO_CONNECT = { SIO + "_6", "Failed to connect to Yahoo" };
	protected static final String[] YAHOO_CLOSE = { SIO + "_7", "Failed to close Yahoo connection" };

	private static ServiceFaultType serviceFaultTypeMaker(String textPrefix, String[] err) {
		ServiceFaultType fault = new ServiceFaultType();
		fault.setErrcode(err[0]);
		fault.setErrtext(textPrefix + ": " + err[1]);
		return fault;
	}

	protected static InvalidEventSetIdMsg InvalidEventSetIdMsg(String message, String[] err) {
		return new InvalidEventSetIdMsg(message, serviceFaultTypeMaker("Invalid Event Set Id", err));
	}

	protected static InvalidURLMsg InvalidURLMsg(String message, String[] err) {
		return new InvalidURLMsg(message, serviceFaultTypeMaker("Invalid URL", err));
	}

	protected static InvalidSECCodeMsg InvalidSECCodeMsg(String message, String[] err) {
		return new InvalidSECCodeMsg(message, serviceFaultTypeMaker("Invalid SEC CodeMsg", err));
	}

	protected static InvalidDatesMsg InvalidDatesMsg(String message, String[] err) {
		return new InvalidDatesMsg(message, serviceFaultTypeMaker("Invalid Dates", err));
	}

	protected static InvalidFileTypeMsg InvalidFileTypeMsg(String message, String[] err) {
		return new InvalidFileTypeMsg(message, serviceFaultTypeMaker("Invalid File Type", err));
	}

	protected static InvalidCurrencyCodeMsg InvalidCurrencyCodeMsg(String message, String[] err) {
		return new InvalidCurrencyCodeMsg(message, serviceFaultTypeMaker("Invalid Currency Code", err));
	}

	protected static ProgramErrorMsg ProgramErrorMsg(String message, String[] err) {
		return new ProgramErrorMsg(message, serviceFaultTypeMaker("Program Error", err));
	}

}
