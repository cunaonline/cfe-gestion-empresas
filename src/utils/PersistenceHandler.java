package utils;

import org.w3c.dom.Element;

public class PersistenceHandler extends XmlHandler {

	public PersistenceHandler( String serverWarName ) throws Exception {
		super( Configuration.getInstance().getPersistenceFile( serverWarName ) );
	}
	
	
	/**
	 * Crea una empresa en el archivo empresas.xml. Usando los valores por defecto para db, publicacion e historico.
	 * @param alias
	 * @param rut
	 * @throws Exception 
	 */
	public void agregarUnaEmpresa( String name ) throws Exception {
		// creamos el node datasource
		Element elem = document.createElement( "persistence-unit" );
		elem.setAttribute( "name", name + "PU" );
		elem.setAttribute( "transaction-type", "RESOURCE_LOCAL" );
		//
		elem.appendChild( crearNodoConValor( document, "provider", "org.hibernate.ejb.HibernatePersistence" ));
		elem.appendChild( crearNodoConValor( document, "non-jta-data-source", "java:jboss/datasources/" + name + "DS" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Parametro" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Acuse" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Cae" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Alarma" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.CaeAsignado" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.CaeAsignadoXML" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.PDFComprobante" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Comprobante" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.DetalleAcuse" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.EmisorControlRd" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Impresora" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Lote" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.NumeroAsignado" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.RangoComprobantesAnulados" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ReceptorCae" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ReceptorElectronico" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ReporteDiario" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Sobre" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.TipoComprobante" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.UserLog" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Usuario" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.RetencionPercepcion" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.RetPerPK" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.TipoAlarma" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ValorUI" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.publicacion.ComprobantePublicado" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Cliente" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.Sucursal" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ProductoServicio" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.PrecioProducto" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.ListaPrecios" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.CondicionEnvio" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.CondicionPago" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.PaqueteMensual" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.StockMinimoReceptor" ));
		elem.appendChild( crearNodoConValor( document, "class", "org.gs1uy.rondanetweb.domain.MedioDePago" ));
		//
		// el show_sql = false
		Element elemProperties = document.createElement( "properties" );
		Element elemPropertiesProperty = document.createElement( "property" );
		elemPropertiesProperty.setAttribute( "name", "hibernate.show_sql" );
		elemPropertiesProperty.setAttribute( "value", "false" );
		elemProperties.appendChild( elemPropertiesProperty );
		elem.appendChild( elemProperties );
		//
		// agregamos el elemento al padre principal
		agregarNodo( "persistence", elem );
	}
	
	public void eliminarUnaEmpresa( String nombre ) {
		super.eliminarNodo( "//persistence/persistence-unit[@name=" +nombre+ "PU]");
	}
	
	/* */
	public static void main(String[] args) throws Exception {
		PersistenceHandler eh = new PersistenceHandler( "C:\\nuevoserver\\wildfly-10.1.0.Final\\standalone\\deployments\\CFERondanetServer.OK.war\\WEB-INF\\classes\\META-INF\\persistence.xml", true );
		eh.eliminarUnaEmpresa( "empresatest15" );
	}
	// constructor solo para testing
	private PersistenceHandler( String fileName, boolean dummie ) throws Exception {
		super( fileName );
	}
	/* */
}
