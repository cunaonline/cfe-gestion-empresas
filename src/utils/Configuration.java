package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;


public class Configuration {

	private static final String PROPERTY_FILE = "app.properties";
	private static HashMap<String, String> propiedades = new LinkedHashMap<String, String>();
	
	private static Configuration instance;
	
	private String direccionAppFolder;
	private String direccionHome;
	private String standaloneFile;
	private String serverWarNameTest;
	private String serverWarNameProd;
	private String empresasFileTest;
	private String persistenceFileTest;
	private String empresasFileProd;
	private String persistenceFileProd;
	private String scriptsFolder;
	private String xsdFolder;
	private String keystoreFile;
	private String printResourcesFolder;
	private boolean isLinux;
	
	private String dbUrl = "";
	private String dbUser = "";
	private String dbPass = "";
	
	private Configuration() {
		Properties props = new Properties();
        InputStream is = Configuration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        String sep = java.io.File.separator;
		try {
			props.load(is);
			direccionAppFolder = props.getProperty( "home.app.base" );
			direccionHome = props.getProperty( "home.base" );
			standaloneFile = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "configuration" +sep+ "standalone.xml";
			//
			serverWarNameTest = props.getProperty( "server.war.name.test" );
			empresasFileTest = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ serverWarNameTest + props.getProperty( "server.empresas" );
			persistenceFileTest = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ serverWarNameTest + props.getProperty( "server.persistence" );
			//
			serverWarNameProd = props.getProperty( "server.war.name.prod" );
			empresasFileProd = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ serverWarNameProd + props.getProperty( "server.empresas" );
			persistenceFileProd = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ serverWarNameProd + props.getProperty( "server.persistence" );
			//
			scriptsFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "scripts";
			xsdFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "resources" +sep+ "xsd";
			keystoreFile = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "resources" +sep+ "otros" +sep+ "vacio.keystore";
			printResourcesFolder = props.getProperty( "wildfly.base" ) +sep+ "standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "resources" +sep+ "PrintResources";
			//
			dbUrl = props.getProperty( "database.url" );
			dbUser = props.getProperty( "database.user" );
			dbPass = props.getProperty( "database.pass" );
			//
			isLinux = props.getProperty( "database.os" ).equals( "linux" );
			//
		} catch (IOException e) {
			direccionAppFolder = "/usr/bin/CFERondanetServer";
			direccionHome = "/usr/CFERondanetServer";
			standaloneFile = "/usr/bin/CFERondanetServer/wildfly-10.1.0.Final/standalone/configuration/standalone.xml";
			serverWarNameTest = "CFERondanetServer.war";
			empresasFileTest = serverWarNameTest + "/WEB-INF/classes/META-INF/empresas.xml";
			persistenceFileTest = serverWarNameTest + "/WEB-INF/classes/META-INF/persistence.xml";
			scriptsFolder = "/usr/bin/CFERondanetServer/wildfly-10.1.0.Final/standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "scripts";
			xsdFolder = "/usr/bin/CFERondanetServer/wildfly-10.1.0.Final/standalone" +sep+ "deployments" +sep+ "CFERondanetAltaEmpresas.war" +sep+ "xsd";
			keystoreFile = null;
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

	public String getServerWarNameTest() {
		return serverWarNameTest;
	}

	public String getEmpresasFile( boolean isDesarrollo ) {
		return isDesarrollo ? empresasFileTest : empresasFileProd;
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

	public String getPersistenceFile( boolean isDesarrollo ) {
		return isDesarrollo ? persistenceFileTest : persistenceFileProd;
	}

	public String getServerWarNameProd() {
		return serverWarNameProd;
	}

	public String getKeystoreFile() {
		return keystoreFile;
	}

	public boolean isLinux() {
		return isLinux;
	}
	
	
}
