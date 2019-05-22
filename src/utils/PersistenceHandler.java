package utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PersistenceHandler extends XmlHandler {

	public PersistenceHandler(String serverWarName) throws Exception {
		super(Configuration.getInstance().getPersistenceFile(serverWarName));
	}

	/**
	 * Crea una empresa en el archivo empresas.xml. Usando los valores por
	 * defecto para db, publicacion e historico.
	 * 
	 * @param alias
	 * @param rut
	 * @throws Exception
	 */
	public void agregarUnaEmpresa(String name, String archivo) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document1 = docBuilder.parse(archivo);

		NodeList children = document1.getChildNodes();
		Node nodeTemplatePU = null;
		for (int i = 0, in = children.getLength(); i < in; i++) {
			Node child = children.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (child.getNodeName().equals("persistence-unit"))
				nodeTemplatePU = child;
		}

		Element elemTemplate = (Element) nodeTemplatePU;
		elemTemplate.setAttribute("name", name + "PU");
		elemTemplate.getElementsByTagName("non-jta-data-source").item(0).getFirstChild()
				.setNodeValue("java:jboss/datasources/" + name + "DS");
		Node nodeToImport = document.importNode(elemTemplate, true);
		agregarNodo("persistence", nodeToImport);
	}

	public void eliminarUnaEmpresa(String nombre) {
		super.eliminarNodo("//persistence/persistence-unit[@name=" + nombre + "PU]");
	}

	/* */
	public static void main(String[] args) throws Exception {
		PersistenceHandler eh = new PersistenceHandler(
				"C:\\nuevoserver\\wildfly-10.1.0.Final\\standalone\\deployments\\CFERondanetServer.OK.war\\WEB-INF\\classes\\META-INF\\persistence.xml",
				true);
		eh.eliminarUnaEmpresa("empresatest15");
	}

	// constructor solo para testing
	private PersistenceHandler(String fileName, boolean dummie) throws Exception {
		super(fileName);
	}
	/* */
}
