package beans;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import utils.Configuration;
import utils.DataBaseManager;
import utils.EmpresasHandler;
import utils.StandaloneHandler;
import utils.FileUtils;
import utils.PersistenceHandler;


@ManagedBean(name = "empresaBean")
@ViewScoped
public class EmpresasBean implements Serializable {

	private static final long serialVersionUID = 7765876811740798583L;
	private static final String FORM = "loginForm";
	private static final String SEP = java.io.File.separator;
	

	private String nombre;
	private String rut;
	private String passwordRNC;
	private String homeFolder;
	private String homeAppFolder;
	private boolean produccion;
	private String razonSocial;
	private String keystoreFile;
	private String logo;
	private String telefono1;
	private String telefono2;
	private String ciudad = "Montevideo";
	private String departamento = "Montevideo";
	private String version;

	public EmpresasBean() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = req.getRequestURL().toString();
		url = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
		System.out.println( url );
	}

	// geters

	public String getNombre() {
		return nombre;
	}
	public String getRut() {
		return rut;
	}
	public String getPasswordRNC() {
		return passwordRNC;
	}
	public String getHomeFolder() {
		return homeFolder;
	}
	public String getHomeAppFolder() {
		return homeAppFolder;
	}

	// setters

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public void setPasswordRNC(String passwordRNC) {
		this.passwordRNC = passwordRNC;
	}
	public void setHomeFolder(String homeFolder) {
		this.homeFolder = homeFolder;
	}
	public void setHomeAppFolder(String homeAppFolder) {
		this.homeAppFolder = homeAppFolder;
	}

	public boolean isProduccion() {
		return produccion;
	}

	public void setProduccion(boolean produccion) {
		this.produccion = produccion;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	




	// metodos

	public void capitalizeText() {
		String pod = produccion ? "_prod" : "_test";
		this.setHomeFolder( Configuration.getInstance().getDireccionHome() + nombre + pod );
		// this.logo = Configuration.getInstance().getDireccionAppFolder() + nombre +SEP+ "PrintResources" +SEP;
		this.setLogo( nombre + "/" + "logo"+nombre+".jpg" );
		this.setHomeAppFolder( Configuration.getInstance().getDireccionAppFolder() + nombre );
	}
	
	
	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println( "login" );
		this.parametrosOK = true;
		this.mnsg = "";
		List<String> errores = new ArrayList<>();
		if (( nombre == null || nombre.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro Nombre es vacio"; errores.add( mnsg ); }
		if (( razonSocial == null || razonSocial.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Razon Social' es vacio"; errores.add( mnsg ); }
		if (( rut == null || rut.length() == 0 )) { parametrosOK = false; mnsg = "Error: el parametro RUT es vacio"; errores.add( mnsg ); }
		if (( passwordRNC == null || passwordRNC.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Password RNC' es vacio"; errores.add( mnsg ); }
		if (( homeFolder == null || homeFolder.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Home Folder' es vacio"; errores.add( mnsg ); }
		if (( homeAppFolder == null || homeAppFolder.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Home App Folder' es vacio"; errores.add( mnsg ); }
		// vacio.keystore: if (( keystoreFile == null || keystoreFile.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Archivo Keystore' es vacio"; errores.add( mnsg ); }
		// vacio.keystore: if (( keystorePass == null || keystorePass.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Password de la Keystore' es vacio"; errores.add( mnsg ); }
		if (( logo == null || logo.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Logo' es vacio"; errores.add( mnsg ); }
		// telefono puede ser vacio: if (( telefono1 == null || telefono1.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro 'Telefono emisor' es vacio"; errores.add( mnsg ); }
		if (( ciudad == null || ciudad.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro Ciudad es vacio"; errores.add( mnsg ); }
		if (( departamento == null || departamento.length() == 0  )) { parametrosOK = false; mnsg = "Error: el parametro Departamento es vacio"; errores.add( mnsg ); }
		try { BigInteger.valueOf(Long.valueOf(rut)); } catch (Exception e) { parametrosOK = false; mnsg = "Error: el parametro RUT no es numerico"; errores.add( mnsg ); }
		if ( !parametrosOK ) {
			for ( Iterator<String> iterator = errores.iterator(); iterator.hasNext(); ) {
				String unError = (String) iterator.next();
				context.addMessage( FORM + ":bAceptar", new FacesMessage( FacesMessage.SEVERITY_ERROR, unError, "" ));	
			}
			return "ERROR";
		}
		System.out.println( nombre +"-"+ rut +"-"+ homeFolder +"-"+ razonSocial );
		parametrosOK = true;
		return "OK";
	}
	private String mnsg = "";
	private boolean parametrosOK = false;
	public boolean isParametrosOK() {
		System.out.println( "isparametrosOK" );
		return parametrosOK;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	

	@SuppressWarnings("unused")
	private boolean empresaCreadaOk = false;
	public void isConfirmadoOk() {
		System.out.println( "entra" );
		if ( parametrosOK ) {
			parametrosOK = false;
			empresaCreadaOk = false;
			DataBaseManager db = null;
			try {
				System.out.println( "creando" );
				db = new DataBaseManager();
				this.keystoreFile = nombre + ".keystore";
				//
				// conectamos con la generica (postgres) y creamos la base de datos
				db.conectar();
				String nombreBaseDatos = nombre + ( produccion ? "_prod" : "_test" ); 
				db.crearBase( nombreBaseDatos );
				System.out.println( "base creada" ); 
				db.cerrarConexion();
				// conectamos con la recien creada y corremos los scripts
				db.conectar( "/" + nombreBaseDatos );
				db.ejecutarStriptsBase( version, produccion );
				System.out.println( "tablas creadas" );
				// actualizamos los parametros
				db.actualizarParametro( RAZON_SOCIALL_PARAM_NAME, this.razonSocial );
				db.actualizarParametro( CIUDADL_PARAM_NAME, this.ciudad );
				db.actualizarParametro( DEPARTAMENTO_PARAM_NAME, this.departamento );
				db.actualizarParametro( TELEFONO1_EMISOR_PARAM_NAME, this.telefono1 != null ? this.telefono1 : ""  );
				db.actualizarParametro( TELEFONO2_EMISOR_PARAM_NAME, this.telefono2 != null ? this.telefono2 : "" );
				db.actualizarParametro( NOMBRE_EMPRESA_PARAM_NAME, this.nombre.toUpperCase() );
				db.actualizarParametro( RUT_EMISOR_PARAM_NAME, this.rut );
				//
				db.actualizarParametro( HOME_FOLDER_PARAM_NAME, this.homeFolder );
				db.actualizarParametro( HOME_APP_FOLDER_PARAM_NAME, this.homeAppFolder );
				db.actualizarParametro( LOGO_PATH, this.logo );
				// db.actualizarParametro( LOGO_PATH, this.logo.replaceAll( "\\", SEP).replaceAll( "/", SEP ));
				//
				db.actualizarParametro( KEYSTORE_FILENAME_PARAM_NAME, this.keystoreFile );
				// db.actualizarParametro( KEYSTORE_PASSWORD_PARAM_NAME, this.keystorePass ); // creamos sin password
				//
				db.actualizarParametro( PASSWORD_RNC_PARAM_NAME, this.passwordRNC );
				//db.actualizarParametro( SERVICIOS_EXTERNOS_USUARIO_PARAM_NAME, this.homeFolder );
				//db.actualizarParametro( SERVICIOS_EXTERNOS_PASSWORD_PARAM_NAME, this.homeFolder );
				//
				db.actualizarParametro( TIPO_APLICACION, "SAAS" );
				//
				db.actualizarParametro( ACTUALIZACION_USER_FTPS_PARAM_NAME, "cfeactualizador" + ( produccion ? "" : "_test" ) + "@gs1uy.org" );
				db.actualizarParametro( ACTUALIZACION_PASS_FTPS_PARAM_NAME, ( produccion ? "" : "" ) );
				//
				// parametros para des y prod
				String dirUrlCentralBase = "https://cfe.rondanet.com" + ( produccion ? "" : ":5542" )+ "/cgi-bin/Receptor.cgi/";
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_ACUSES_PARAM_NAME, dirUrlCentralBase + "Envio" );
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES_PARAM_NAME, dirUrlCentralBase + "ConfRecepcionResp" );
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_ERRORES_PARAM_NAME, dirUrlCentralBase + "Error" );
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP_PARAM_NAME, dirUrlCentralBase + "Envio" );
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP_PARAM_NAME, dirUrlCentralBase + "Envio" );
				////// db.actualizarParametro( URL_RNCENTRAL_ENVIO_REPORTES_DIARIOS_PARAM_NAME, dirUrlCentralBase + "" );
				db.actualizarParametro( URL_RNCENTRAL_ENVIO_STATUS_PARAM_NAME, dirUrlCentralBase + "EnvioInforme" );
				db.actualizarParametro( URL_RNCENTRAL_RECEPCION_SOBRES_PARAM_NAME, dirUrlCentralBase + "BajarSobres" );
				db.actualizarParametro( URL_RNCENTRAL_RECEPCION_ACUSES_PARAM_NAME, dirUrlCentralBase + "BajarRespuestas" );
				db.actualizarParametro( URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI_PARAM_NAME, dirUrlCentralBase + "BajarRechazosDGI" );
				db.actualizarParametro( URL_RNCENTRAL_CONF_RECHAZOS_DGI_PARAM_NAME, dirUrlCentralBase + "ConfRecepcionRech" );
				db.actualizarParametro( URL_RNCENTRAL_RECEPCION_USUARIOS_PARAM_NAME, dirUrlCentralBase + "BajarUsuarios" );
				db.actualizarParametro( URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS_PARAM_NAME, dirUrlCentralBase + "BajarEmpresas" );
				db.actualizarParametro( URL_RNCENTRAL_MONITOREO_PARAM_NAME, dirUrlCentralBase + "EnvioInforme" );
				db.actualizarParametro( URL_RNCENTRAL_VALORES_UI_PARAM_NAME, dirUrlCentralBase + "BajarValoresUI" );
				//
				if ( produccion ) {
					db.actualizarParametro( URL_WS_CONSULTA_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba" );
					db.actualizarParametro( URL_WS_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba" );
				} else {
					db.actualizarParametro( URL_WS_CONSULTA_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba" );
					db.actualizarParametro( URL_WS_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba" );	
				}
				System.out.println( "parametros actualizados" );
				// cerramos la conexion
				db.cerrarConexion();
				//
				// creamos la estructura de carpetas
				String auxFolder = null;
				String auxDir = null;
				//
				auxDir = homeFolder + java.io.File.separator;
				//
				auxFolder = auxDir + "AckInternos"; new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CARPETA_ACTUALIZACIONES", "actualizaciones" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CARPETA_AEDITAR", "AEditar" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CARPETA_BORRADORES", "AEditar/Borradores" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CARPETA_PLANTILLAS", "AEditar/Plantillas" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "HIGH_PRIORITY_FOLDER", "AEnviar" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "LOW_PRIORITY_FOLDER", "AEnviarTKMenores" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CAEs_TO_CREATE_FOLDER", "CAEsACargar" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CAEs_CREATED_FOLDER", "CAEsCargados" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CAEs_NOT_CREATED_FOLDER", "CAEsNoCargados" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "CONTROL_FOLDER", "Control" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "SENT_FOLDER", "Enviados" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "PRINT_OUTPUT_FOLDER", "Impresion" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "LOG_FOLDER", "Logs" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "NOT_SENT_FOLDER", "NoEnviados" ); new File( auxFolder ).mkdirs();
				// auxFolder = auxDir + db.obtenerParametro( "PAQUETES_MENSUALES_NOEXISTE", "PaquetesMensuales" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "ASIGNACION_RANGO_CAE", "RangoCAEEntregados" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "SOLICITUD_RANGO_NO_ASIGNADO", "RangoCAENoAsignados" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "SOLICITUD_RANGO_CAE", "RangoCAEPedidos" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "RECEIVED_FOLDER", "Recibidos" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "BACKUP_FOLDER", "Respaldos" ); new File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro( "TEMP_FOLDER", "Temp" ); new File( auxFolder ).mkdirs();
				//
				auxDir = homeAppFolder + java.io.File.separator;
				//
				auxFolder = auxDir + db.obtenerParametro( "PRINT_TEMPLATES_FOLDER", "PrintResources" ); new File( auxFolder ).mkdirs();
				FileUtils.copyFolder( new File( Configuration.getInstance().getPrintResourcesFolder() ), new File ( auxFolder ));
				//
				FileUtils.copyFolder( new File( Configuration.getInstance().getXsdFolder() ), new File ( this.homeAppFolder +SEP+ "xsd" ));
				System.out.println( "Carpetas creadas correctamente" );
				//
				FileUtils.copyFile( new File( Configuration.getInstance().getKeystoreFile() ), keystoreFile, new File ( auxFolder ));
				System.out.println( "keystore copiado correctamente" );
				// db.obtenerParametro( ,  );
				//
				// creamos la entrada en el standalone.
				StandaloneHandler sh = new StandaloneHandler();
				String nombreDS = nombre + "DS";
				sh.agregarAppAStandalone( nombreBaseDatos, nombreDS );
				System.out.println( "standalone.xml actualizado" );
				//
				// creamos la entrada en empresas
				EmpresasHandler eh = new EmpresasHandler( !produccion );
				eh.agregarUnaEmpresa( nombre, String.valueOf( rut ));
				System.out.println( "empresas.xml actualizado" );
				// creamos la entrada en el persistence:
				PersistenceHandler ph = new PersistenceHandler( !produccion );
				ph.agregarUnaEmpresa( nombre );
				System.out.println( "persistence.xml actualizado" );
				//
				System.out.println( "Finalizando" );
				empresaCreadaOk = true;
			} catch (Exception e) {
				System.out.println( "ERROR: " + e.getLocalizedMessage() );
				if ( db != null ) db.cerrarConexion();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public void cbModificado() {
		System.out.println( "modificado" );
	}
	
	
	public static final String RAZON_SOCIALL_PARAM_NAME = "RAZON_SOCIAL";
	public static final String CIUDADL_PARAM_NAME = "CIUDAD";
	public static final String DEPARTAMENTO_PARAM_NAME = "DEPARTAMENTO";
	public static final String TELEFONO1_EMISOR_PARAM_NAME = "TELEFONO1_EMISOR";
	public static final String TELEFONO2_EMISOR_PARAM_NAME = "TELEFONO2_EMISOR";
	public static final String NOMBRE_EMPRESA_PARAM_NAME = "NOMBRE_EMPRESA";
	public static final String RUT_EMISOR_PARAM_NAME = "RUT_EMISOR";
	
	public static final String HOME_FOLDER_PARAM_NAME = "HOME_FOLDER";
	public static final String HOME_APP_FOLDER_PARAM_NAME = "HOME_APP_FOLDER";
	public static final String LOGO_PATH = "LOGO";
	
	public static final String KEYSTORE_FILENAME_PARAM_NAME = "KEYSTORE_FILENAME";
	public static final String KEYSTORE_PASSWORD_PARAM_NAME = "KEYSTORE_PASSWORD";
	public static final String LICENCIA_PARAM_NAME = "LICENCIA"; // ?
	
	public static final String PASSWORD_RNC_PARAM_NAME = "PASSWORD_RNC";
	public static final String SERVICIOS_EXTERNOS_USUARIO_PARAM_NAME = "SERVICIOS_EXTERNOS_USUARIO"; 
	public static final String SERVICIOS_EXTERNOS_PASSWORD_PARAM_NAME = "SERVICIOS_EXTERNOS_PASSWORD";
	
	/* */
	public static final String TIPO_APLICACION = "TIPO_APLICACION";
	
	public static final String ACTUALIZACION_USER_FTPS_PARAM_NAME = "ACTUALIZACION_USER_FTPS";
	public static final String ACTUALIZACION_PASS_FTPS_PARAM_NAME = "ACTUALIZACION_PASS_FTPS";
	
	public static final String URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP_PARAM_NAME = "URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP";	
	public static final String URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP_PARAM_NAME = "URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP";
	public static final String URL_RNCENTRAL_ENVIO_REPORTES_DIARIOS_PARAM_NAME = "URL_RNCENTRAL_ENVIO_REPORTES_DIARIOS";
	public static final String URL_RNCENTRAL_ENVIO_ACUSES_PARAM_NAME = "URL_RNCENTRAL_ENVIO_ACUSES";
	public static final String URL_RNCENTRAL_ENVIO_ERRORES_PARAM_NAME = "URL_RNCENTRAL_ENVIO_ERRORES";
	public static final String URL_RNCENTRAL_ENVIO_STATUS_PARAM_NAME = "URL_RNCENTRAL_ENVIO_STATUS";
	public static final String URL_RNCENTRAL_RECEPCION_SOBRES_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_SOBRES";
	public static final String URL_RNCENTRAL_RECEPCION_ACUSES_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_ACUSES";
	public static final String URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES_PARAM_NAME = "URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES";
	public static final String URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI";
	public static final String URL_RNCENTRAL_CONF_RECHAZOS_DGI_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_CONF_RECHAZOS_DGI";	
	public static final String URL_RNCENTRAL_RECEPCION_USUARIOS_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_USUARIOS";
	public static final String URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS_PARAM_NAME = "URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS";
	public static final String URL_RNCENTRAL_MONITOREO_PARAM_NAME = "URL_RNCENTRAL_MONITOREO";
	public static final String URL_RNCENTRAL_VALORES_UI_PARAM_NAME = "URL_RNCENTRAL_VALORES_UI";	

	public static final String URL_WS_DGI_PARAM_NAME = "URL_WS_DGI";
	public static final String URL_WS_CONSULTA_DGI_PARAM_NAME = "URL_WS_CONSULTA_DGI";


	
}
