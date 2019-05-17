package domain;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Empresa {
	@NotBlank(message = "El nombre no debe ser vacío")
	private String nombre;
	@NotBlank(message = "El RUT no debe ser vacío")
	@Digits(message = "El RUT debe estar compuesto dígitos", fraction = 0, integer = 12)
	private String rut;
	@NotBlank(message = "El password no debe ser vacío")
	@Length(min = 3, message = "La contraseña debe tener mínimo 3 caracteres")
	private String passwordRNC;
	@NotBlank(message = "La carpeta Home no debe ser vacía")
	private String homeFolder;
	@NotBlank(message = "La carpeta HomeApp no debe ser vacía")
	private String homeAppFolder;
	private boolean produccion;
	@NotBlank(message = "La Razón Social no debe estar vacía")
	private String razonSocial;
	@NotBlank(message = "El keystore no debe estar vacío")
	private String keystoreFile;
	@NotBlank(message = "El logo no debe estar vacío")
	private String logo;
	private String telefono1;
	private String telefono2;
	private String ciudad = "Montevideo";
	private String departamento = "Montevideo";
	private Integer maxConnections = 100;
	private Integer minConnections = 10;
	@NotBlank(message = "La versión debe estar definida")
	private String version;
	@NotBlank(message = "El war a usar debe estar definido")
	private String war;

	public Empresa(String nombre, String rut, String passwordRNC, String homeFolder, String homeAppFolder,
			boolean produccion, String razonSocial, String keystoreFile, String logo, String telefono1,
			String telefono2, String ciudad, String departamento, Integer maxConnections, Integer minConnections,
			String version, String war) {
		super();
		this.nombre = nombre;
		this.rut = rut;
		this.passwordRNC = passwordRNC;
		this.homeFolder = homeFolder;
		this.homeAppFolder = homeAppFolder;
		this.produccion = produccion;
		this.razonSocial = razonSocial;
		this.keystoreFile = keystoreFile;
		this.logo = logo;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.ciudad = ciudad;
		this.departamento = departamento;
		this.maxConnections = maxConnections;
		this.minConnections = minConnections;
		this.version = version;
		this.war = war;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public String getPasswordRNC() {
		return passwordRNC;
	}

	public void setPasswordRNC(String passwordRNC) {
		this.passwordRNC = passwordRNC;
	}

	public String getHomeFolder() {
		return homeFolder;
	}

	public void setHomeFolder(String homeFolder) {
		this.homeFolder = homeFolder;
	}

	public String getHomeAppFolder() {
		return homeAppFolder;
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

	public String getKeystoreFile() {
		return keystoreFile;
	}

	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWar() {
		return war;
	}

	public void setWar(String war) {
		this.war = war;
	}

}
