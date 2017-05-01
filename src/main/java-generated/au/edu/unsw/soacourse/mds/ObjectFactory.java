
package au.edu.unsw.soacourse.mds;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the au.edu.unsw.soacourse.mds package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InvalidURL_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidURL");
    private final static QName _InvalidEventSetId_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidEventSetId");
    private final static QName _InvalidSECCode_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidSECCode");
    private final static QName _InvalidDates_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidDates");
    private final static QName _InvalidFileType_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidFileType");
    private final static QName _InvalidCurrencyCode_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "InvalidCurrencyCode");
    private final static QName _ProgramError_QNAME = new QName("http://mds.soacourse.unsw.edu.au", "ProgramError");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: au.edu.unsw.soacourse.mds
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportMarketDataReq }
     * 
     */
    public ImportMarketDataReq createImportMarketDataReq() {
        return new ImportMarketDataReq();
    }

    /**
     * Create an instance of {@link ImportMarketDataResp }
     * 
     */
    public ImportMarketDataResp createImportMarketDataResp() {
        return new ImportMarketDataResp();
    }

    /**
     * Create an instance of {@link DownloadFileReq }
     * 
     */
    public DownloadFileReq createDownloadFileReq() {
        return new DownloadFileReq();
    }

    /**
     * Create an instance of {@link DownloadFileResp }
     * 
     */
    public DownloadFileResp createDownloadFileResp() {
        return new DownloadFileResp();
    }

    /**
     * Create an instance of {@link ConvertMarketDataReq }
     * 
     */
    public ConvertMarketDataReq createConvertMarketDataReq() {
        return new ConvertMarketDataReq();
    }

    /**
     * Create an instance of {@link ConvertMarketDataResp }
     * 
     */
    public ConvertMarketDataResp createConvertMarketDataResp() {
        return new ConvertMarketDataResp();
    }

    /**
     * Create an instance of {@link YahooExchangeRateReq }
     * 
     */
    public YahooExchangeRateReq createYahooExchangeRateReq() {
        return new YahooExchangeRateReq();
    }

    /**
     * Create an instance of {@link YahooExchangeRateResp }
     * 
     */
    public YahooExchangeRateResp createYahooExchangeRateResp() {
        return new YahooExchangeRateResp();
    }

    /**
     * Create an instance of {@link ServiceFaultType }
     * 
     */
    public ServiceFaultType createServiceFaultType() {
        return new ServiceFaultType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidURL")
    public JAXBElement<ServiceFaultType> createInvalidURL(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidURL_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidEventSetId")
    public JAXBElement<ServiceFaultType> createInvalidEventSetId(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidEventSetId_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidSECCode")
    public JAXBElement<ServiceFaultType> createInvalidSECCode(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidSECCode_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidDates")
    public JAXBElement<ServiceFaultType> createInvalidDates(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidDates_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidFileType")
    public JAXBElement<ServiceFaultType> createInvalidFileType(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidFileType_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "InvalidCurrencyCode")
    public JAXBElement<ServiceFaultType> createInvalidCurrencyCode(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_InvalidCurrencyCode_QNAME, ServiceFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://mds.soacourse.unsw.edu.au", name = "ProgramError")
    public JAXBElement<ServiceFaultType> createProgramError(ServiceFaultType value) {
        return new JAXBElement<ServiceFaultType>(_ProgramError_QNAME, ServiceFaultType.class, null, value);
    }

}
