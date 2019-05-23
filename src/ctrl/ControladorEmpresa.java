package ctrl;

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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import domain.Empresa;
import utils.Configuration;
import utils.DataBaseManager;
import utils.EmpresasHandler;
import utils.FileUtils;
import utils.PersistenceHandler;
import utils.RNEncrypter;
import utils.StandaloneHandler;

public class ControladorEmpresa {

	private static final String CMD_WIN = "cmd.exe";
	private static final String CMD_LINUX = "bash";

	private Validator validator = null;
	private Empresa empresa = null;
	HashMap<String, ArrayList<String>> mensajes = new HashMap<String, ArrayList<String>>();

	public ControladorEmpresa() {
		super();
		this.iniciarValidador();
		mensajes.put("ERROR", new ArrayList<String>());
		mensajes.put("INFO", new ArrayList<String>());
	}

	public void limpiarMensajes() {
		mensajes.put("ERROR", new ArrayList<String>());
		mensajes.put("INFO", new ArrayList<String>());
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public HashMap<String, ArrayList<String>> getMensajes() {
		return mensajes;
	}

	public void setMensajes(HashMap<String, ArrayList<String>> mensajes) {
		this.mensajes = mensajes;
	}

	private void iniciarValidador() {
		if (this.validator == null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			this.validator = factory.getValidator();
		}
	}

	public Set<ConstraintViolation<Empresa>> validarDatos() {
		return validator.validate(this.empresa);
	}

	public List<String> versionesWarDisponibles() {
		ArrayList<String> versionesDisponibles = new ArrayList();
		File auxDirScripts = new File(Configuration.getInstance().getScriptsFolder());
		String[] listVers = auxDirScripts.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("db\\s+([\\d\\.]+)\\.sql");
			}
		});
		Pattern p = Pattern.compile("db\\s+([\\d\\.]+)\\.sql");
		for (String fileVersion : listVers) {
			Matcher matcher = p.matcher(fileVersion);
			if (matcher.matches()) {
				String auxVersion = matcher.group(1);
				versionesDisponibles.add(auxVersion);
			}
		}

		Collections.sort(versionesDisponibles);

		return versionesDisponibles;
	}

	public String[] warsDisponibles() {
		File dirWars = new File(Configuration.getInstance().getServerWarDirectory());
		String[] directorios = dirWars.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains("CFERondanetServer") && name.toLowerCase().endsWith("war");
			}
		});

		Arrays.sort(directorios);

		return directorios;
	}

	public boolean cambiarPermisos(String ruta, String grupo, String usuario) {
		if (Configuration.getInstance().isLinux()) {
			String comandoLinux = "chown -R gs1:gs1uy " + ruta + "/*";
			ProcessBuilder builder = new ProcessBuilder(CMD_LINUX, "-c", comandoLinux);
			builder.redirectErrorStream(true);
			try {
				builder.start();
				mensajes.get("INFO").add("Se cambiaron los permisos al Grupo: " + grupo + ", Usuario: " + usuario
						+ " en la ruta: " + ruta);
			} catch (IOException e) {
				e.printStackTrace();
				mensajes.get("ERROR").add("No se pudieron cambiar los permisos al Grupo: " + grupo + ", Usuario: "
						+ usuario + " en la ruta: " + ruta);
				return false;
			}
		} else {
			System.out.println("Modificando permisos en Windows");
		}
		return true;
	}

	public void agregarAppAStandalone(String wildflyFolder, String url, String usuario, String pass, String nombreBase,
			String nombreDataSource, String maxConnections, String minConnections) throws Exception {

		if (Configuration.getInstance().isLinux()) {
			String comandoLinux = "cd " + wildflyFolder + "/bin && " + "sh jboss-cli.sh " + "--connect "
					+ "'/subsystem=datasources/data-source=\"" + nombreDataSource
					+ "\":add(jndi-name=\"java:jboss/datasources/" + nombreDataSource + "\"," + "max-pool-size="
					+ maxConnections + ",min-pool-size=" + minConnections
					+ ",driver-name=\"postgresql\",enabled=\"true\"," + "connection-url=\"" + url + "/" + nombreBase
					+ "\",user-name=\"" + usuario + "\",password=\"" + pass + "\")'";
			System.out.println(comandoLinux);
			ProcessBuilder builder = new ProcessBuilder(CMD_LINUX, "-c", comandoLinux);
			builder.redirectErrorStream(true);
			builder.start();
		} else {
			String comandoWindows = "cd " + wildflyFolder + "\\bin && " + "jboss-cli.bat " + "--connect "
					+ "/subsystem=datasources/data-source=\"" + nombreDataSource
					+ "\":add(jndi-name=\"java:jboss/datasources/" + nombreDataSource + "\"," + "max-pool-size="
					+ maxConnections + ",min-pool-size=" + minConnections
					+ ",driver-name=\"postgresql\",enabled=\"true\"," + "connection-url=\"" + url + "/" + nombreBase
					+ "\",user-name=\"" + usuario + "\",password=\"" + pass + "\")";
			System.out.println(comandoWindows);
			ProcessBuilder builder = new ProcessBuilder(CMD_WIN, "/c", comandoWindows);
			builder.redirectErrorStream(true);
			builder.start();
		}
	}

	public String nombreAmbiente() {
		String nombre = this.empresa.getNombre() != null ? this.empresa.getNombre() : this.empresa.getRazonSocial();
		String nombreBaseDatos = nombre.replaceAll(" ", "_") + (this.empresa.isProduccion() ? "_prod" : "_test");
		return nombreBaseDatos.toLowerCase();
	}

	public String nombreSinAmbiente() {
		String nombre = this.empresa.getNombre() != null ? this.empresa.getNombre() : this.empresa.getRazonSocial();
		String nombreBaseDatos = nombre.replaceAll(" ", "_");
		return nombreBaseDatos.toLowerCase();
	}

	public boolean crearDB() {
		boolean bdCreada = false;
		String nombreBaseDatos = this.nombreAmbiente();
		DataBaseManager db = new DataBaseManager();
		try {
			db.conectar();
			db.crearBase(nombreBaseDatos, !Configuration.getInstance().isLinuxDB());
			mensajes.get("INFO").add("Base de Datos " + nombreBaseDatos + " creada correctamente.");
			db.cerrarConexion();
			bdCreada = true;
		} catch (Exception e) {
			try {
				db.cerrarConexion();
			} catch (Exception e1) {
				// TODO: handle exception
			}
			mensajes.get("ERROR")
					.add("Base de Datos " + nombreBaseDatos + " no se pudo crear. Mensaje: " + e.getMessage());
		}

		return bdCreada;
	}

	public boolean ejecutarScriptInstalacion() {
		boolean scriptsEjecutados = false;
		DataBaseManager db = new DataBaseManager();
		String nombreBaseDatos = this.nombreAmbiente();
		try {
			db.conectar("/" + nombreBaseDatos);
			db.ejecutarStriptsBase(this.empresa.getVersion(), this.empresa.isProduccion());
			mensajes.get("INFO").add("Scripts para versión " + this.empresa.getVersion() + " en ambiente "
					+ this.nombreAmbiente() + " ejecutado correctamente.");
			db.cerrarConexion();
			scriptsEjecutados = true;
		} catch (Exception e) {
			try {
				db.cerrarConexion();
			} catch (Exception e1) {
				// TODO: handle exception
			}
			mensajes.get("ERROR").add("Scripts para versión " + this.empresa.getVersion() + " en ambiente "
					+ this.nombreAmbiente() + " no fué ejecutado.");
		}
		return scriptsEjecutados;
	}

	public boolean parametrizarBD() {
		boolean parametizacioCorrecta = false;
		DataBaseManager db = new DataBaseManager();
		try {
			db.conectar("/" + this.nombreAmbiente());
			db.actualizarParametro(RAZON_SOCIALL_PARAM_NAME, this.empresa.getRazonSocial());
			db.actualizarParametro(CIUDADL_PARAM_NAME, this.empresa.getCiudad());
			db.actualizarParametro(DEPARTAMENTO_PARAM_NAME, this.empresa.getDepartamento());
			db.actualizarParametro(TELEFONO1_EMISOR_PARAM_NAME,
					this.empresa.getTelefono1() != null ? this.empresa.getTelefono1() : "");
			db.actualizarParametro(TELEFONO2_EMISOR_PARAM_NAME,
					this.empresa.getTelefono2() != null ? this.empresa.getTelefono2() : "");
			db.actualizarParametro(NOMBRE_EMPRESA_PARAM_NAME, this.empresa.getNombre());
			db.actualizarParametro(RUT_EMISOR_PARAM_NAME, this.empresa.getRut());
			//
			db.actualizarParametro(HOME_FOLDER_PARAM_NAME, this.empresa.getHomeFolder());
			db.actualizarParametro(HOME_APP_FOLDER_PARAM_NAME, this.empresa.getHomeAppFolder());
			db.actualizarParametro(LOGO_PATH, this.empresa.getLogo());

			this.empresa.setKeystoreFile(this.nombreSinAmbiente() + ".keystore");
			db.actualizarParametro(KEYSTORE_FILENAME_PARAM_NAME, this.empresa.getKeystoreFile());

			db.actualizarParametroPassword(PASSWORD_RNC_PARAM_NAME, this.empresa.getPasswordRNC());
			db.actualizarParametro(TIPO_APLICACION, "SAAS");
			//
			db.actualizarParametro(ACTUALIZACION_USER_FTPS_PARAM_NAME,
					"cfeactualizador" + (this.empresa.isProduccion() ? "" : "_test") + "@gs1uy.org");
			db.actualizarParametroPassword(ACTUALIZACION_PASS_FTPS_PARAM_NAME, (this.empresa.isProduccion() ? "" : ""));
			//
			// parametros para des y prod
			String dirUrlCentralBase = "https://cfe.rondanet.com" + (this.empresa.isProduccion() ? "" : ":5542")
					+ "/cgi-bin/Receptor.cgi/";
			db.actualizarParametro(URL_RNCENTRAL_ENVIO_ACUSES_PARAM_NAME, dirUrlCentralBase + "Envio");
			db.actualizarParametro(URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES_PARAM_NAME,
					dirUrlCentralBase + "ConfRecepcionResp");
			db.actualizarParametro(URL_RNCENTRAL_ENVIO_ERRORES_PARAM_NAME, dirUrlCentralBase + "Error");
			db.actualizarParametro(URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP_PARAM_NAME, dirUrlCentralBase + "Envio");
			db.actualizarParametro(URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP_PARAM_NAME, dirUrlCentralBase + "Envio");

			db.actualizarParametro(URL_RNCENTRAL_ENVIO_STATUS_PARAM_NAME, dirUrlCentralBase + "EnvioInforme");
			db.actualizarParametro(URL_RNCENTRAL_RECEPCION_SOBRES_PARAM_NAME, dirUrlCentralBase + "BajarSobres");
			db.actualizarParametro(URL_RNCENTRAL_RECEPCION_ACUSES_PARAM_NAME, dirUrlCentralBase + "BajarRespuestas");
			db.actualizarParametro(URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI_PARAM_NAME,
					dirUrlCentralBase + "BajarRechazosDGI");
			db.actualizarParametro(URL_RNCENTRAL_CONF_RECHAZOS_DGI_PARAM_NAME, dirUrlCentralBase + "ConfRecepcionRech");
			db.actualizarParametro(URL_RNCENTRAL_RECEPCION_USUARIOS_PARAM_NAME, dirUrlCentralBase + "BajarUsuarios");
			db.actualizarParametro(URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS_PARAM_NAME,
					dirUrlCentralBase + "BajarEmpresas");
			db.actualizarParametro(URL_RNCENTRAL_MONITOREO_PARAM_NAME, dirUrlCentralBase + "EnvioInforme");
			db.actualizarParametro(URL_RNCENTRAL_VALORES_UI_PARAM_NAME, dirUrlCentralBase + "BajarValoresUI");

			// activamos por defecto el data entry
			db.actualizarParametro(HABILITAR_CFE_ENTRY_PARAM_NAME, true);
			//
			if (this.empresa.isProduccion()) {
				db.actualizarParametro(URL_WS_CONSULTA_DGI_PARAM_NAME,
						"https://efactura.dgi.gub.uy:6440/efactura/ws_consultas");
				db.actualizarParametro(URL_WS_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy/efactura/ws_efactura");
			} else {
				db.actualizarParametro(URL_WS_CONSULTA_DGI_PARAM_NAME,
						"https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba");
				db.actualizarParametro(URL_WS_DGI_PARAM_NAME, "https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba");
			}
			db.cerrarConexion();
			mensajes.get("INFO").add("Se parametrizó la Empresa  " + this.nombreAmbiente() + " correctamente.");
			parametizacioCorrecta = true;
		} catch (Exception e) {
			try {
				db.cerrarConexion();
			} catch (Exception e1) {
				// TODO: handle exception
			}
			mensajes.get("ERROR").add("No se parametrizó la Empresa  " + this.nombreAmbiente() + " correctamente.");
		}
		return parametizacioCorrecta;
	}

	public boolean crearHomeAppFolder() {
		boolean homeAppFolderCreada = false;
		DataBaseManager db = new DataBaseManager();
		String auxDir = null;
		auxDir = this.empresa.getHomeAppFolder() + java.io.File.separator;
		try {
			File tmp = new File(auxDir);
			if (!tmp.exists()) {
				tmp.mkdirs();
				if (!tmp.canWrite())
					throw new Exception("No existe el directorio " + auxDir + " y no se puede crear");
			}
			db.conectar("/" + this.nombreAmbiente());
			// los print resources
			String auxFolder = null;
			auxFolder = auxDir + db.obtenerParametro("PRINT_TEMPLATES_FOLDER", "PrintResources");
			File tmpFile = new File(auxFolder);
			if (!tmpFile.exists()) {
				tmpFile.mkdirs();
				File resourceFolder = new File(auxFolder);
				FileUtils.copyFolder(new File(Configuration.getInstance().getPrintResourcesFolder()), resourceFolder);
				mensajes.get("INFO").add("Se crea el directorio: " + auxFolder + ".");
			} else {
				mensajes.get("INFO")
						.add("Ya existía el directorio: " + auxFolder + " . No se copian los PrintResources.");
			}
			// los xsds
			File xsdFolder = new File(auxDir + "xsd");
			if (!xsdFolder.exists()) {
				FileUtils.copyFolder(new File(Configuration.getInstance().getXsdFolder()), xsdFolder);
			} else {
				mensajes.get("INFO").add("Ya existía el directorio: " + xsdFolder + " . No se copian los XSD.");
			}

			// el nuevo keystore
			File keyStoreFile = new File(auxDir + java.io.File.separator + this.empresa.getKeystoreFile());
			if (!keyStoreFile.exists()) {
				FileUtils.copyFile(new File(Configuration.getInstance().getKeystoreFile()),
						this.empresa.getKeystoreFile(), new File(auxDir));
			} else {
				mensajes.get("INFO").add("Ya existía el archivo: " + keyStoreFile + " . No se copia el KeyStore.");
			}
			this.cambiarPermisos(auxDir, "gs1uy", "gs1uy");
			db.cerrarConexion();
			homeAppFolderCreada = true;
		} catch (Exception e) {
			try {
				db.cerrarConexion();
			} catch (Exception e1) {
				// TODO: handle exception
			}
			mensajes.get("ERROR").add("Error creando Carpeta " + auxDir + " Mensaje: " + e.getMessage());
		}
		return homeAppFolderCreada;
	}

	public boolean crearHomeFolder() {
		boolean homeFolderCreada = false;
		DataBaseManager db = new DataBaseManager();
		String auxDir = null;
		auxDir = this.empresa.getHomeFolder() + java.io.File.separator;

		try {
			db.conectar("/" + this.nombreAmbiente());
			// creamos la estructura de carpetas
			String auxFolder = null;

			File tmp = new File(auxDir);
			if (!tmp.exists()) {
				tmp.mkdirs();
				if (!tmp.canWrite())
					throw new Exception("No existe el directorio " + auxDir + " y no se puede crear");
			}

			auxFolder = auxDir + db.obtenerParametro("HIGH_PRIORITY_FOLDER", "AEnviar");
			File tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + "AckInternos";
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CARPETA_ACTUALIZACIONES", "actualizaciones");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CARPETA_AEDITAR", "AEditar");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CARPETA_BORRADORES", "AEditar/Borradores");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CARPETA_PLANTILLAS", "AEditar/Plantillas");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("LOW_PRIORITY_FOLDER", "AEnviarTKMenores");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CAEs_TO_CREATE_FOLDER", "CAEsACargar");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CAEs_CREATED_FOLDER", "CAEsCargados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CAEs_NOT_CREATED_FOLDER", "CAEsNoCargados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("CONTROL_FOLDER", "Control");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("SENT_FOLDER", "Enviados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("PRINT_OUTPUT_FOLDER", "Impresion");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("LOG_FOLDER", "Logs");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("NOT_SENT_FOLDER", "NoEnviados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("ASIGNACION_RANGO_CAE", "RangoCAEEntregados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("SOLICITUD_RANGO_NO_ASIGNADO", "RangoCAENoAsignados");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("SOLICITUD_RANGO_CAE", "RangoCAEPedidos");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("RECEIVED_FOLDER", "Recibidos");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("BACKUP_FOLDER", "Respaldos");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			auxFolder = auxDir + db.obtenerParametro("TEMP_FOLDER", "Temp");
			tmpFile = new File(auxFolder);
			if (!tmpFile.exists())
				tmpFile.mkdirs();
			else
				mensajes.get("INFO").add("Ya existía el directorio: " + auxFolder);
			db.cerrarConexion();
			mensajes.get("INFO").add("Se creó la Carpeta Home en " + auxDir);
			homeFolderCreada = true;
		} catch (Exception e) {
			try {
				db.cerrarConexion();
			} catch (Exception e1) {
				// TODO: handle exception
			}
			mensajes.get("ERROR").add("Ocurrió un error creando el Home en " + auxDir + ". " + e.getMessage());
		}

		return homeFolderCreada;

	}

	public boolean agregarStandalone() {
		boolean agregadoStandalone = false;
		String nombreDS = this.nombreAmbiente() + "DS";
		String wildflyFolder = Configuration.getInstance().getWildflyFolder();
		String dbURL = Configuration.getInstance().getDbUrl();
		String dbUser = Configuration.getInstance().getDbUser();
		String dbPass;
		try {
			dbPass = RNEncrypter.decrypt(Configuration.getInstance().getDbPass());
		} catch (Exception e) {
			dbPass = "";
		}

		try {
			StandaloneHandler sh = new StandaloneHandler();
			sh.agregarAppAStandalone(wildflyFolder, dbURL, dbUser, dbPass, this.nombreAmbiente(), nombreDS,
					this.empresa.getMaxConnections().toString(), this.empresa.getMinConnections().toString());
			mensajes.get("INFO").add("Conexión agregada al Standalone. Nombre DataSource:  " + nombreDS);
			agregadoStandalone = true;
		} catch (Exception e) {
			mensajes.get("ERROR").add("No se pudo agregar conexión al Standalone. Error: " + e.getMessage());
		}

		return agregadoStandalone;
	}

	public boolean agregarEmpresa() {
		boolean empresaAgregada = false;
		try {
			EmpresasHandler eh = new EmpresasHandler(this.empresa.getWar());
			eh.agregarUnaEmpresa(this.nombreAmbiente(), String.valueOf(this.empresa.getRut()));
			mensajes.get("INFO").add("Empresa agregada. Nombre Empresa:  " + this.nombreAmbiente());
			empresaAgregada = true;
		} catch (Exception e) {
			mensajes.get("ERROR").add("No se pudo agregar Empresa. Error: " + e.getMessage());
		}

		return empresaAgregada;
	}

	public boolean agregarEmpresaPersistence() {
		boolean empresaAgregadaPersistence = false;
		try {
			PersistenceHandler ph = new PersistenceHandler(this.empresa.getWar());
			ph.agregarUnaEmpresa(this.nombreAmbiente(), Configuration.getInstance().getPersistenceFolder()
					+ java.io.File.separator + this.getEmpresa().getVersion() + ".persistence.xml");
			mensajes.get("INFO").add("Empresa agregada al Persistence Unit. Nombre Empresa:  " + this.nombreAmbiente());
			empresaAgregadaPersistence = true;
		} catch (Exception e) {
			e.printStackTrace();
			mensajes.get("ERROR").add("No se pudo agregar Empresa al Persistence Unit. Error: " + e.getMessage());
		}

		return empresaAgregadaPersistence;
	}

}
