package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;


public class Configuration {

	private static final String PROPERTY_FILE = "app.properties";
	private static final String sep = java.io.File.separator;
	
	private static HashMap<String, String> propiedades = new LinkedHashMap<String, String>();
	
	private static Configuration instance;
	
	private String direccionAppFolder;
	private String direccionHome;
	private String standaloneFile;
	
	private String empresasFile;
	private String persistenceFile;
	private String scriptsFolder;
	private String xsdFolder;
	private String keystoreFile;
	private String printResourcesFolder;
	private boolean isLinux;
	private boolean isLinuxDB;
	private String wildflyFolder;
	
	private String dbUrl = "";
	private String dbUser = "";
	private String dbPass = "";
	
	private String auxEmpresasXml;
	private String auxPersistenceXml;
	
	
	private Configuration() {
		Properties props = new Properties();
        InputStream is = Configuration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        
		try {
			props.load(is);
			//
			direccionAppFolder = props.getProperty( "home.app.base" );
			direccionAppFolder = direccionAppFolder.endsWith( sep ) ? direccionAppFolder : direccionAppFolder + sep;
			direccionHome = props.getProperty( "home.base" );
			direccionHome = direccionHome.endsWith( sep ) ? direccionHome : direccionHome + sep;
			//
			standaloneFile = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "configuration" +sep+ "standalone.xml";
			// aux
			wildflyFolder = props.getProperty( "wildfly.base" );
			auxEmpresasXml = props.getProperty( "server.empresas" );
			auxPersistenceXml = props.getProperty( "server.persistence" );
			//
			scriptsFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetGestionEmpresas.war" +sep+ "scripts";
			xsdFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetGestionEmpresas.war" +sep+ "resources" +sep+ "xsd";
			keystoreFile = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetGestionEmpresas.war" +sep+ "resources" +sep+ "otros" +sep+ "vacio.keystore";
			printResourcesFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetGestionEmpresas.war" +sep+ "resources" +sep+ "PrintResources";
			//
			dbUrl = props.getProperty( "database.url" );
			dbUser = props.getProperty( "database.user" );
			dbPass = props.getProperty( "database.pass" );
			//
			isLinux = props.getProperty( "os" ).equals( "linux" );
			isLinux = props.getProperty( "database.linux" ) != null ? props.getProperty( "database.linux" ).equals("true") : false; 
		} catch (IOException e) {
			throw new RuntimeException( "No se pudo cargar el archivo de configuracion o alguno de sus parametros" );
		}
	}

	public static Configuration getInstance() {
		if ( instance == null ) {
			instance = new Configuration();
		}
		return instance;
	}

	public String getDireccionAppFolder() {
		return direccionAppFolder;
	}

	public String getDireccionHome() {
		return direccionHome;
	}

	public String getStandaloneFile() {
		return standaloneFile;
	}

	public static HashMap<String, String> getPropiedades() {
		return propiedades;
	}


	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public String getScriptsFolder() {
		return scriptsFolder;
	}

	public String getXsdFolder() {
		return xsdFolder;
	}

	public String getPrintResourcesFolder() {
		return printResourcesFolder;
	}

	public String getKeystoreFile() {
		return keystoreFile;
	}

	public boolean isLinux() {
		return isLinux;
	}

	public boolean isLinuxDB() {
		return isLinuxDB;
	}

	public String getEmpresasFile( String serverWarName ) {
		empresasFile = wildflyFolder +sep+ "standalone" +sep+ "deployments" +sep+ serverWarName + auxEmpresasXml;
		return empresasFile;
	}

	public String getPersistenceFile( String serverWarName ) {
		persistenceFile = wildflyFolder +sep+ "standalone" +sep+ "deployments" +sep+ serverWarName + auxPersistenceXml;
		return persistenceFile;
	}

	public String getServerWarDirectory() {
		return wildflyFolder +sep+ "standalone" +sep+ "deployments" +sep;
	}

	public String getWildflyFolder() {
		return wildflyFolder;
	}

	
	
}
