package au.edu.unsw.soacourse.mds;

import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MDSInInterceptor extends AbstractSoapInterceptor {

	public MDSInInterceptor() {
		super(Phase.USER_PROTOCOL);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		System.out.println("A message received with the following headers:");
		List<Header> headers = message.getHeaders();
		for (Header header : headers) {
			System.out.println(header.getName());
			Element root = (Element) header.getObject();
			NodeList nodeList = root.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (1 == node.getNodeType())
					System.out.println(node.getNamespaceURI() + ":" + node.getLocalName() //
							+ " has " + node.getTextContent());
			}
		}
	}
}
