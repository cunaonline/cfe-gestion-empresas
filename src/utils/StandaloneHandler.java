package utils;

/**
 * Se encarga de actulizar el standalone.xml
 */
public class StandaloneHandler {

	private static final String PASSWORD = "${VAULT::vb::db-pass::1}";
	private static final String USUARIO = "postgres";

	private static final String CMD = "cmd.exe";
	

	public StandaloneHandler() throws Exception {
	}

	public void agregarAppAStandalone( String nombreBase, String nombreDataSource ) throws Exception {
		
		if ( Configuration.getInstance().isLinux() ) {
			String comandoLinux = "cd /usr/bin/CFERondanetServer/wildfly-10.1.0.Final/bin && "
					+ "jboss-cli.sh "
					+ "--connect "
					+ "/subsystem=datasources/data-source=\"" + nombreDataSource + "\":add(jndi-name=\"java:jboss/datasources/" +nombreDataSource + "\","
							+ "max-pool-size=100,min-pool-size=10,driver-name=\"postgresql\",enabled=\"true\","
							+ "connection-url=\"jdbc:postgresql://127.0.0.1:5432/" +nombreBase+ "\",user-name=\"" +USUARIO+ "\",password=\"" +PASSWORD+ "\")";
			System.out.println( comandoLinux );
			ProcessBuilder builder = new ProcessBuilder( CMD, "/c", comandoLinux );
			builder.redirectErrorStream(true);
			builder.start();
		} else {
			String comandoWindows = "cd C:\\nuevoserver\\wildfly-10.1.0.Final\\bin && "
					+ "jboss-cli.bat "
					+ "--connect "
					+ "/subsystem=datasources/data-source=\"" + nombreDataSource + "\":add(jndi-name=\"java:jboss/datasources/" +nombreDataSource + "\","
							+ "max-pool-size=100,min-pool-size=10,driver-name=\"postgresql\",enabled=\"true\","
							+ "connection-url=\"jdbc:postgresql://127.0.0.1:5432/" +nombreBase+ "\",user-name=\"" +USUARIO+ "\",password=\"" +PASSWORD+ "\")";
			System.out.println( comandoWindows );
			ProcessBuilder builder = new ProcessBuilder( CMD, "/c", comandoWindows );
			builder.redirectErrorStream(true);
			builder.start();	
		}
	}

	/* tested OK *
	public static void main(String[] args) throws Exception {
		StandaloneHandler sh = new StandaloneHandler();
		sh.agregarAppAStandalone( "nombreBase", "sdsDC" );
	}
	/* */
}
