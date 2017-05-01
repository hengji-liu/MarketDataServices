
package au.edu.unsw.soacourse.mds;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2017-04-13T13:23:39.711+10:00
 * Generated source version: 3.0.4
 */

@WebFault(name = "InvalidSECCode", targetNamespace = "http://mds.soacourse.unsw.edu.au")
public class InvalidSECCodeMsg extends Exception {
    
    private au.edu.unsw.soacourse.mds.ServiceFaultType invalidSECCode;

    public InvalidSECCodeMsg() {
        super();
    }
    
    public InvalidSECCodeMsg(String message) {
        super(message);
    }
    
    public InvalidSECCodeMsg(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSECCodeMsg(String message, au.edu.unsw.soacourse.mds.ServiceFaultType invalidSECCode) {
        super(message);
        this.invalidSECCode = invalidSECCode;
    }

    public InvalidSECCodeMsg(String message, au.edu.unsw.soacourse.mds.ServiceFaultType invalidSECCode, Throwable cause) {
        super(message, cause);
        this.invalidSECCode = invalidSECCode;
    }

    public au.edu.unsw.soacourse.mds.ServiceFaultType getFaultInfo() {
        return this.invalidSECCode;
    }
}
