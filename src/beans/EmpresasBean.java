package beans;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

@ManagedBean(name = "empresaBean")
@SessionScoped
public class EmpresasBean implements Serializable {

	private static final long serialVersionUID = 7765876811740798583L;
	private static final String FORM = "loginForm";
	private static final String SEP = java.io.File.separator;

	private static boolean empresaCreadaOk;
	private String mensajeErrorAltaEmpresa = "";

	private boolean dbCreada = false;
	private boolean appInstalada = false;
	private boolean appParametrizada = false;
	private boolean homeFolderCreado = false;
	private boolean homeAppFolderCreado = false;
	private boolean agregadoStandalone = false;
	private boolean agregadaEmpresaPersistence = false;
	private boolean agregadaEmpresa = false;

	private String nombre = null;
	private String nombreComercial = null;
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
	private Integer maxConnections = 10;
	private Integer minConnections = 3;
	//
	private String version;
	private List<SelectItem> versionesDisponibles;
	//
	private String war;
	private List<SelectItem> warsDisponibles;
	private ControladorEmpresa ctrlEmpresa;

	private HashMap<String, String> emisores = new HashMap<String, String>();
	private String emisorSeleccionado;

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
		this.emisores = ctrlEmpresa.getEmisores();
		actualizarCtrl();

	}

	public void actualizarCtrl() {
		Empresa empresa = new Empresa(nombre, nombreComercial, rut, passwordRNC, homeFolder, homeAppFolder, produccion,
				razonSocial, keystoreFile, logo, telefono1, telefono2, ciudad, departamento, maxConnections,
				minConnections, version, war,
				(this.emisorSeleccionado == null || this.emisorSeleccionado.equals("-1")) ? null
						: this.emisorSeleccionado);
		this.ctrlEmpresa.setEmpresa(empresa);
	}

	public String getNombre() {
		return nombre;
	}

	public String getNombreComercial() {
		return nombreComercial;
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

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
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
		errores.clear();
		actualizarCtrl();
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
		context.addMessage(FORM + ":bAceptar",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Datos Validados Correctamente", ""));
		System.out.println(nombre + "-" + rut + "-" + homeFolder + "-" + razonSocial);
		return "dummie";
	}

	private boolean parametrosOK = false;

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

	private List<Mensaje> errores = new ArrayList<Mensaje>();

	public void isConfirmadoOk() {
		empresaCreadaOk = false;
		ctrlEmpresa.limpiarMensajes();
		errores.clear();
		if (this.login().toUpperCase().equals("ERROR")) {
			errores.add(new Mensaje("ERROR", "Validación de datos fallida. Por favor revise los datos nuevamente."));
			return;
		}

		dbCreada = this.ctrlEmpresa.crearDB();
		appInstalada = false;
		appParametrizada = false;
		homeFolderCreado = false;
		homeAppFolderCreado = false;
		agregadoStandalone = false;
		agregadaEmpresaPersistence = false;
		agregadaEmpresa = false;

		if (dbCreada) {
			appInstalada = this.ctrlEmpresa.ejecutarScriptInstalacion();
			appParametrizada = this.ctrlEmpresa.parametrizarBD();
			homeFolderCreado = this.ctrlEmpresa.crearHomeFolder();
			homeAppFolderCreado = this.ctrlEmpresa.crearHomeAppFolder();
			agregadoStandalone = this.ctrlEmpresa.agregarStandalone();
			agregadaEmpresaPersistence = this.ctrlEmpresa.agregarEmpresaPersistence();
			agregadaEmpresa = this.ctrlEmpresa.agregarEmpresa();
		}

		if (dbCreada && appInstalada && appParametrizada && homeFolderCreado && homeAppFolderCreado && agregadaEmpresa
				&& agregadaEmpresaPersistence && agregadoStandalone) {
			limpiarDatos();
			empresaCreadaOk = true;
		}

		List<String> tmpErrores = this.ctrlEmpresa.getMensajes().get("ERROR");
		for (Iterator iterator = tmpErrores.iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			errores.add(new Mensaje("ERROR", item));
		}
		tmpErrores = this.ctrlEmpresa.getMensajes().get("INFO");
		for (Iterator iterator = tmpErrores.iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			errores.add(new Mensaje("INFO", item));
		}
		loguearDatosEjecución();
	}

	public void actualizarRutas() {
		actualizarCtrl();
		this.homeAppFolder = Configuration.getInstance().getDireccionAppFolder() + ctrlEmpresa.nombreAmbiente();
		this.homeFolder = Configuration.getInstance().getDireccionHome() + ctrlEmpresa.nombreAmbiente();

		this.logo = ctrlEmpresa.getEmpresa().getNombre() + EmpresasBean.SEP + "logo"
				+ ctrlEmpresa.getEmpresa().getNombre() + ".jpg";
	}

	private void limpiarDatos() {
		nombre = null;
		nombreComercial = null;
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
		this.errores.clear();
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

	public List<Mensaje> getErrores() {
		return errores;
	}

	public void setErrores(List<Mensaje> errores) {
		this.errores = errores;
	}

	private void loguearDatosEjecución() {
		try {
			String nombreArchivo = !empresaCreadaOk ? "ERROR-" : "EXITO-";
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String dateString = format.format(new Date());

			nombreArchivo = nombreArchivo + ctrlEmpresa.nombreAmbiente() + dateString.toString() + ".log";
			File fout = new File(Configuration.getInstance().getLogsFolder() + nombreArchivo);
			FileOutputStream fos = new FileOutputStream(fout);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (Iterator iterator = errores.iterator(); iterator.hasNext();) {
				Mensaje mensaje = (Mensaje) iterator.next();
				bw.write(mensaje.toString());
				bw.newLine();
			}

			bw.close();
			errores.add(new Mensaje("INFO",
					"Log guardado en: " + Configuration.getInstance().getLogsFolder() + nombreArchivo));
		} catch (Exception e) {
			errores.add(new Mensaje("ERROR",
					"No se pudo loguear la ejecución. Existe la ruta configurada para los logs?. Error: "
							+ e.getMessage()));
		}
	}

	public HashMap<String, String> getEmisores() {
		return emisores;
	}

	public void setEmisores(HashMap<String, String> emisores) {
		this.emisores = emisores;
	}

	public String getEmisorSeleccionado() {
		return emisorSeleccionado;
	}

	public void setEmisorSeleccionado(String emisorSeleccionado) {
		this.emisorSeleccionado = emisorSeleccionado;
	}

}
