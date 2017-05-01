
package au.edu.unsw.soacourse.mds;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2017-04-13T13:23:39.689+10:00
 * Generated source version: 3.0.4
 */

@WebFault(name = "InvalidEventSetId", targetNamespace = "http://mds.soacourse.unsw.edu.au")
public class InvalidEventSetIdMsg extends Exception {
    
    private au.edu.unsw.soacourse.mds.ServiceFaultType invalidEventSetId;

    public InvalidEventSetIdMsg() {
        super();
    }
    
    public InvalidEventSetIdMsg(String message) {
        super(message);
    }
    
    public InvalidEventSetIdMsg(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEventSetIdMsg(String message, au.edu.unsw.soacourse.mds.ServiceFaultType invalidEventSetId) {
        super(message);
        this.invalidEventSetId = invalidEventSetId;
    }

    public InvalidEventSetIdMsg(String message, au.edu.unsw.soacourse.mds.ServiceFaultType invalidEventSetId, Throwable cause) {
        super(message, cause);
        this.invalidEventSetId = invalidEventSetId;
    }

    public au.edu.unsw.soacourse.mds.ServiceFaultType getFaultInfo() {
        return this.invalidEventSetId;
    }
}
