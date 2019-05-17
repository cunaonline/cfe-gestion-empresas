package beans;

import static ctes.Ctes.ACTUALIZACION_PASS_FTPS_PARAM_NAME;
import static ctes.Ctes.ACTUALIZACION_USER_FTPS_PARAM_NAME;
import static ctes.Ctes.CIUDADL_PARAM_NAME;
import static ctes.Ctes.DEPARTAMENTO_PARAM_NAME;
import static ctes.Ctes.HABILITAR_CFE_ENTRY_PARAM_NAME;
import static ctes.Ctes.HOME_APP_FOLDER_PARAM_NAME;
import static ctes.Ctes.HOME_FOLDER_PARAM_NAME;
import static ctes.Ctes.KEYSTORE_FILENAME_PARAM_NAME;
import static ctes.Ctes.LOGO_PATH;
import static ctes.Ctes.NOMBRE_EMPRESA_PARAM_NAME;
import static ctes.Ctes.PASSWORD_RNC_PARAM_NAME;
import static ctes.Ctes.RAZON_SOCIALL_PARAM_NAME;
import static ctes.Ctes.RUT_EMISOR_PARAM_NAME;
import static ctes.Ctes.TELEFONO1_EMISOR_PARAM_NAME;
import static ctes.Ctes.TELEFONO2_EMISOR_PARAM_NAME;
import static ctes.Ctes.TIPO_APLICACION;
import static ctes.Ctes.URL_RNCENTRAL_CONF_RECHAZOS_DGI_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_ACUSES_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_ERRORES_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_ENVIO_STATUS_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_MONITOREO_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_RECEPCION_ACUSES_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_RECEPCION_SOBRES_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_RECEPCION_USUARIOS_PARAM_NAME;
import static ctes.Ctes.URL_RNCENTRAL_VALORES_UI_PARAM_NAME;
import static ctes.Ctes.URL_WS_CONSULTA_DGI_PARAM_NAME;
import static ctes.Ctes.URL_WS_DGI_PARAM_NAME;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import ctrl.ControladorEmpresa;
import domain.Empresa;
import utils.Configuration;
import utils.DataBaseManager;
import utils.EmpresasHandler;
import utils.FileUtils;
import utils.PersistenceHandler;
import utils.RNEncrypter;
import utils.StandaloneHandler;

@ManagedBean(name = "empresaBean")
@SessionScoped
public class EmpresasBean implements Serializable {

	private static final long serialVersionUID = 7765876811740798583L;
	private static final String FORM = "loginForm";
	private static final String SEP = java.io.File.separator;

	private String nombre = null;
	private String rut = null;
	private String passwordRNC = null;
	private String passwordRNC2 = null;
	private String homeFolder = null;
	private String homeAppFolder = null;
	private boolean produccion = false;
	private String razonSocial = null;
	private String keystoreFile = "valor_defecto.keystore";
	private String logo = null;
	private String telefono1 = null;
	private String telefono2 = null;
	private String ciudad = "Montevideo";
	private String departamento = "Montevideo";
	private Integer maxConnections = 100;
	private Integer minConnections = 10;
	//
	private String version;
	private List<SelectItem> versionesDisponibles;
	//
	private String war;
	private List<SelectItem> warsDisponibles;
	private ControladorEmpresa ctrlEmpresa;

	public EmpresasBean() {
		ctrlEmpresa = new ControladorEmpresa();

		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String url = req.getRequestURL().toString();
		url = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
		System.out.println(url);

		versionesDisponibles = new LinkedList<>();
		versionesDisponibles.add(new SelectItem("", "Elegir version del Server"));
		List<String> versiones = ctrlEmpresa.versionesWarDisponibles();
		for (String item : versiones) {
			versionesDisponibles.add(new SelectItem(item, item));
		}

		warsDisponibles = new LinkedList<>();
		String[] directorios = ctrlEmpresa.warsDisponibles();
		warsDisponibles.add(new SelectItem("", "Elegir War"));
		for (String auxWarName : directorios) {
			warsDisponibles.add(new SelectItem(auxWarName, auxWarName.substring(0, auxWarName.length() - 4)));
		}
	}

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

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}

	public Integer getMinConnections() {
		return minConnections;
	}

	public void setMinConnections(Integer minConnections) {
		this.minConnections = minConnections;
	}

	public String login() {
		Empresa empresa = new Empresa(nombre, rut, passwordRNC, homeFolder, homeAppFolder, produccion, razonSocial,
				keystoreFile, logo, telefono1, telefono2, ciudad, departamento, maxConnections, minConnections, version,
				war);
		this.ctrlEmpresa.setEmpresa(empresa);
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("login");

		Set<ConstraintViolation<Empresa>> listaErrores = ctrlEmpresa.validarDatos();
		this.parametrosOK = listaErrores.size() == 0;

		if (!this.parametrosOK) {
			for (Iterator<ConstraintViolation<Empresa>> iterator = listaErrores.iterator(); iterator.hasNext();) {
				ConstraintViolation<Empresa> unError = iterator.next();
				context.addMessage(FORM + ":bAceptar",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, unError.getMessage(), ""));
			}

			return "ERROR";
		}
		System.out.println(nombre + "-" + rut + "-" + homeFolder + "-" + razonSocial);
		return "dummie";
	}

	private boolean parametrosOK;

	public boolean isParametrosOK() {
		System.out.println("isparametrosOK: " + parametrosOK);
		return parametrosOK;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private static boolean empresaCreadaOk;
	private String mensajeErrorAltaEmpresa = "";

	public void isConfirmadoOk() {
		System.out.println("entra");
		boolean baseCreadaOK = false;
		if (parametrosOK) {
			//
			empresaCreadaOk = false;
			parametrosOK = false;
			// variables afuera para poder elimninar todo si hay algun error.
			String wildflyFolder = Configuration.getInstance().getWildflyFolder();
			String dbURL = Configuration.getInstance().getDbUrl();
			String dbUser = Configuration.getInstance().getDbUser();
			String dbPass;
			try {
				dbPass = RNEncrypter.decrypt(Configuration.getInstance().getDbPass());
			} catch (Exception e) {
				dbPass = "";
			}
			this.nombre = this.nombre != null ? this.nombre : this.razonSocial;
			String nombreBaseDatos = this.nombre.replaceAll(" ", "_") + (produccion ? "_prod" : "_test");

			DataBaseManager db = new DataBaseManager();
			EmpresasHandler eh = null;
			PersistenceHandler ph = null;
			try {
				System.out.println("creando");
				// conectamos con la generica (postgres) y creamos la base de
				// datos
				db.conectar();
				db.crearBase(nombreBaseDatos, !Configuration.getInstance().isLinuxDB());
				baseCreadaOK = true;
				System.out.println("base creada");
				db.cerrarConexion();
				// conectamos con la recien creada y corremos los scripts
				db.conectar("/" + nombreBaseDatos);
				db.ejecutarStriptsBase(version, produccion);
				System.out.println("tablas creadas");
				// actualizamos los parametros
				db.actualizarParametro(RAZON_SOCIALL_PARAM_NAME, this.razonSocial);
				db.actualizarParametro(CIUDADL_PARAM_NAME, this.ciudad);
				db.actualizarParametro(DEPARTAMENTO_PARAM_NAME, this.departamento);
				db.actualizarParametro(TELEFONO1_EMISOR_PARAM_NAME, this.telefono1 != null ? this.telefono1 : "");
				db.actualizarParametro(TELEFONO2_EMISOR_PARAM_NAME, this.telefono2 != null ? this.telefono2 : "");
				db.actualizarParametro(NOMBRE_EMPRESA_PARAM_NAME, this.nombre);
				db.actualizarParametro(RUT_EMISOR_PARAM_NAME, this.rut);
				//
				db.actualizarParametro(HOME_FOLDER_PARAM_NAME, this.homeFolder);
				db.actualizarParametro(HOME_APP_FOLDER_PARAM_NAME, this.homeAppFolder);
				db.actualizarParametro(LOGO_PATH, this.logo);

				this.keystoreFile = nombre + ".keystore";
				db.actualizarParametro(KEYSTORE_FILENAME_PARAM_NAME, this.keystoreFile);

				db.actualizarParametroPassword(PASSWORD_RNC_PARAM_NAME, this.passwordRNC);
				db.actualizarParametro(TIPO_APLICACION, "SAAS");
				//
				db.actualizarParametro(ACTUALIZACION_USER_FTPS_PARAM_NAME,
						"cfeactualizador" + (produccion ? "" : "_test") + "@gs1uy.org");
				db.actualizarParametroPassword(ACTUALIZACION_PASS_FTPS_PARAM_NAME, (produccion ? "" : ""));
				//
				// parametros para des y prod
				String dirUrlCentralBase = "https://cfe.rondanet.com" + (produccion ? "" : ":5542")
						+ "/cgi-bin/Receptor.cgi/";
				db.actualizarParametro(URL_RNCENTRAL_ENVIO_ACUSES_PARAM_NAME, dirUrlCentralBase + "Envio");
				db.actualizarParametro(URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES_PARAM_NAME,
						dirUrlCentralBase + "ConfRecepcionResp");
				db.actualizarParametro(URL_RNCENTRAL_ENVIO_ERRORES_PARAM_NAME, dirUrlCentralBase + "Error");
				db.actualizarParametro(URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP_PARAM_NAME, dirUrlCentralBase + "Envio");
				db.actualizarParametro(URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP_PARAM_NAME, dirUrlCentralBase + "Envio");

				db.actualizarParametro(URL_RNCENTRAL_ENVIO_STATUS_PARAM_NAME, dirUrlCentralBase + "EnvioInforme");
				db.actualizarParametro(URL_RNCENTRAL_RECEPCION_SOBRES_PARAM_NAME, dirUrlCentralBase + "BajarSobres");
				db.actualizarParametro(URL_RNCENTRAL_RECEPCION_ACUSES_PARAM_NAME,
						dirUrlCentralBase + "BajarRespuestas");
				db.actualizarParametro(URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI_PARAM_NAME,
						dirUrlCentralBase + "BajarRechazosDGI");
				db.actualizarParametro(URL_RNCENTRAL_CONF_RECHAZOS_DGI_PARAM_NAME,
						dirUrlCentralBase + "ConfRecepcionRech");
				db.actualizarParametro(URL_RNCENTRAL_RECEPCION_USUARIOS_PARAM_NAME,
						dirUrlCentralBase + "BajarUsuarios");
				db.actualizarParametro(URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS_PARAM_NAME,
						dirUrlCentralBase + "BajarEmpresas");
				db.actualizarParametro(URL_RNCENTRAL_MONITOREO_PARAM_NAME, dirUrlCentralBase + "EnvioInforme");
				db.actualizarParametro(URL_RNCENTRAL_VALORES_UI_PARAM_NAME, dirUrlCentralBase + "BajarValoresUI");

				// activamos por defecto el data entry
				db.actualizarParametro(HABILITAR_CFE_ENTRY_PARAM_NAME, true);
				//
				if (produccion) {
					db.actualizarParametro(URL_WS_CONSULTA_DGI_PARAM_NAME,
							"https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba");
					db.actualizarParametro(URL_WS_DGI_PARAM_NAME,
							"https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba");
				} else {
					db.actualizarParametro(URL_WS_CONSULTA_DGI_PARAM_NAME,
							"https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba");
					db.actualizarParametro(URL_WS_DGI_PARAM_NAME,
							"https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba");
				}
				System.out.println("parametros actualizados");
				//
				// creamos la estructura de carpetas
				String auxFolder = null;
				String auxDir = null;
				//
				auxDir = homeFolder + java.io.File.separator;
				//
				auxFolder = auxDir + "AckInternos";
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CARPETA_ACTUALIZACIONES", "actualizaciones");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CARPETA_AEDITAR", "AEditar");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CARPETA_BORRADORES", "AEditar/Borradores");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CARPETA_PLANTILLAS", "AEditar/Plantillas");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("HIGH_PRIORITY_FOLDER", "AEnviar");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("LOW_PRIORITY_FOLDER", "AEnviarTKMenores");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CAEs_TO_CREATE_FOLDER", "CAEsACargar");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CAEs_CREATED_FOLDER", "CAEsCargados");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CAEs_NOT_CREATED_FOLDER", "CAEsNoCargados");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("CONTROL_FOLDER", "Control");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("SENT_FOLDER", "Enviados");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("PRINT_OUTPUT_FOLDER", "Impresion");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("LOG_FOLDER", "Logs");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("NOT_SENT_FOLDER", "NoEnviados");
				new File(auxFolder).mkdirs();
				// auxFolder = auxDir + db.obtenerParametro(
				// "PAQUETES_MENSUALES_NOEXISTE", "PaquetesMensuales" ); new
				// File( auxFolder ).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("ASIGNACION_RANGO_CAE", "RangoCAEEntregados");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("SOLICITUD_RANGO_NO_ASIGNADO", "RangoCAENoAsignados");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("SOLICITUD_RANGO_CAE", "RangoCAEPedidos");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("RECEIVED_FOLDER", "Recibidos");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("BACKUP_FOLDER", "Respaldos");
				new File(auxFolder).mkdirs();
				auxFolder = auxDir + db.obtenerParametro("TEMP_FOLDER", "Temp");
				new File(auxFolder).mkdirs();
				//
				auxDir = homeAppFolder + java.io.File.separator;
				// los print resources
				auxFolder = auxDir + db.obtenerParametro("PRINT_TEMPLATES_FOLDER", "PrintResources");
				new File(auxFolder).mkdirs();
				FileUtils.copyFolder(new File(Configuration.getInstance().getPrintResourcesFolder()),
						new File(auxFolder));
				// los xsds
				FileUtils.copyFolder(new File(Configuration.getInstance().getXsdFolder()),
						new File(this.homeAppFolder + SEP + "xsd"));
				ctrlEmpresa.cambiarPermisos(auxDir, "gs1uy", "gs1uy");
				System.out.println("Carpetas creadas correctamente");
				// el nuevo keystore
				FileUtils.copyFile(new File(Configuration.getInstance().getKeystoreFile()), keystoreFile,
						new File(auxDir));
				System.out.println("Keystore copiado correctamente");
				// db.obtenerParametro( , );
				//
				// creamos la entrada en el standalone.
				StandaloneHandler sh = new StandaloneHandler();
				String nombreDS = nombre + "DS";

				sh.agregarAppAStandalone(wildflyFolder, dbURL, dbUser, dbPass, nombreBaseDatos, nombreDS,
						maxConnections.toString(), minConnections.toString());

				System.out.println("Standalone.xml actualizado");
				//
				// creamos la entrada en empresas
				eh = new EmpresasHandler(war);
				eh.agregarUnaEmpresa(nombre, String.valueOf(rut));
				System.out.println("Empresas.xml actualizado");
				// creamos la entrada en el persistence:
				ph = new PersistenceHandler(war);
				ph.agregarUnaEmpresa(nombre);
				System.out.println("persistence.xml actualizado");
				//
				System.out.println("Finalizando");
				empresaCreadaOk = true;
				limpiarDatos();
			} catch (Exception e) {
				//
				empresaCreadaOk = false;
				// guardamos flag y mensaje de error
				mensajeErrorAltaEmpresa = e.getMessage();
				// cerramos la conexion con la base actual, porque no se puede
				// eliminar si esta activa. (solo la elimnamos si la creamos
				// nosotros)
				db.cerrarConexion();
				if (baseCreadaOK) {
					try {
						db.conectar();
						// eliminamos la base de datos y cerramos la conexion
						db.eliminarBase(nombreBaseDatos);
					} catch (Exception e1) {
						// No hacemos nada
					}
					db.cerrarConexion();
				}
				//
				org.apache.commons.io.FileUtils.deleteQuietly(new File(homeFolder + java.io.File.separator));
				org.apache.commons.io.FileUtils.deleteQuietly(new File(homeAppFolder + java.io.File.separator));
				// eliminamos los archivos
				if (eh != null)
					eh.eliminarUnaEmpresa(rut);
				if (ph != null)
					ph.eliminarUnaEmpresa(nombre);
				// imprimimos en el log
				e.printStackTrace();
				System.out.println("ERROR: " + mensajeErrorAltaEmpresa);
				// cerramos la conexion
				if (db != null)
					db.cerrarConexion();
			}
		}
	}

	private void limpiarDatos() {
		nombre = null;
		rut = null;
		passwordRNC = null;
		passwordRNC2 = null;
		homeFolder = null;
		homeAppFolder = null;
		produccion = false;
		razonSocial = null;
		keystoreFile = "valor_defecto.keystore";
		logo = null;
		telefono1 = null;
		telefono2 = null;
		ciudad = "Montevideo";
		departamento = "Montevideo";
		maxConnections = 100;
	}

	public void cbModificado() {
		System.out.println("modificado");
	}

	public boolean isRutValido() {
		return rutValido;
	}

	public void setRutValido(boolean rutValido) {
		this.rutValido = rutValido;
	}

	public String getPasswordRNC2() {
		return passwordRNC2;
	}

	public void setPasswordRNC2(String passwordRNC2) {
		this.passwordRNC2 = passwordRNC2;
	}

	public String getWar() {
		return war;
	}

	public void setWar(String war) {
		this.war = war;
	}

	public List<SelectItem> getWarsDisponibles() {
		return warsDisponibles;
	}

	public void setWarsDisponibles(List<SelectItem> warsDisponibles) {
		this.warsDisponibles = warsDisponibles;
	}

	public void verficarRut() {
		rutValido = rut == null || rut.length() == 0 || utils.Utils.validarRUT(rut);
		if (!rutValido)
			FacesContext.getCurrentInstance().addMessage(FORM + ":itRut",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "El rut no es válido", "El rut no es válido"));
	}

	private boolean rutValido = false;

	public void validarPasswordIguales() {
		if (!passwordRNC.equals(passwordRNC2))
			FacesContext.getCurrentInstance().addMessage(FORM + ":isPwd2",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los passwords no coinciden", ""));
	}

	public List<SelectItem> getVersionesDisponibles() {
		return versionesDisponibles;
	}

	public boolean isEmpresaCreadaOk() {
		return empresaCreadaOk;
	}

	public String getMensajeErrorAltaEmpresa() {
		return mensajeErrorAltaEmpresa;
	}

}
