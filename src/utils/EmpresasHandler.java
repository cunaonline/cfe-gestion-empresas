package utils;

import org.w3c.dom.Element;

public class EmpresasHandler extends XmlHandler {

	public EmpresasHandler( boolean isDesarrollo ) throws Exception {
		super( Configuration.getInstance().getEmpresasFile( isDesarrollo ));
	}
	
	private EmpresasHandler( String fileName ) throws Exception {
		super( fileName );
	}
	
	/**
	 * Crea una empresa en el archivo empresas.xml. Usando los valores por defecto para db, publicacion e historico.
	 * @param alias
	 * @param rut
	 * @throws Exception 
	 */
	public void agregarUnaEmpresa( String alias, String rut ) throws Exception {
		// creamos el node datasource
		Element elem = document.createElement( "empresa" );
		elem.setAttribute( "rut", rut );
		elem.setAttribute( "alias", alias );
		elem.setAttribute( "visible", "true" );
		//
		elem.appendChild( crearNodoConValor( document, "db", alias + "PU" ));
		elem.appendChild( crearNodoConValor( document, "db-publicacion", alias + "PublicadorPU" ));
		elem.appendChild( crearNodoConValor( document, "historico", alias + "_historico" ));
		//
		// agregamos el elemento
		agregarNodo( "empresas", elem );
	}
	
	public static void main(String[] args) throws Exception {
		EmpresasHandler eh = new EmpresasHandler( "C:\\CFERondanetServer\\wildfly-10.1.0.Final\\standalone\\deployments\\CFERondanetServer.war\\WEB-INF\\classes\\META-INF\\empresas.xml" );
		eh.agregarUnaEmpresa( "gs1", "212032250017" );
	}
}
