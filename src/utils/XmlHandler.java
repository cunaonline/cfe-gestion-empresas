package utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class XmlHandler {
	
	protected String filePath;
	protected Document document;

	public XmlHandler(String filePath) throws Exception {
		super();
		this.filePath = filePath;
		//
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.parse( filePath );
	}

	public void agregarNodo( String nodoPadre, Node nodoAgregar ) throws Exception {
		//
		Node nodeParent = document.getElementsByTagName( nodoPadre ).item( 0 );
		nodeParent.appendChild( nodoAgregar );
		//
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource( document );
		StreamResult result = new StreamResult( new File( filePath ));
		transformer.transform( source, result );
	}
	
	/**
	 * Auxiliar para crear nodos.
	 * @param doc
	 * @param nodoName
	 * @param valorNodo
	 * @return
	 */
	protected Element crearNodoConValor( Document doc, String nodoName, String valorNodo ) {
		Element ret = doc.createElement( nodoName );
		ret.setTextContent( valorNodo );
		return ret;
	}
}
