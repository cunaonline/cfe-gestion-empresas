package utils;

/**
 * Se encarga de actulizar el standalone.xml
 */
public class StandaloneHandler {

	// private static final String PASSWORD = "${VAULT::vb::db-pass::1}";
	private static final String PASSWORD = "cf0_r1nd2n3t_gs1";
	private static final String USUARIO = "postgres";

	private static final String CMD_WIN = "cmd.exe";
	private static final String CMD_LINUX = "bash";

	public StandaloneHandler() throws Exception {
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

	/* tested OK windows */
	public static void main(String[] args) throws Exception {
		/*
		 * StandaloneHandler sh = new StandaloneHandler();
		 * sh.agregarAppAStandalone( "nombreBase", "sdsDC" );
		 */
		String nombreDataSource = "sdsDC";
		String nombreBase = "basesql";
		String comandoWindows = "cd " + "FOLDER" + "/bin && " + "sh jboss-cli.sh " + "--connect "
				+ "/subsystem=datasources/data-source=\"" + nombreDataSource
				+ "\":add(jndi-name=\"java:jboss/datasources/" + nombreDataSource + "\","
				+ "max-pool-size=100,min-pool-size=10,driver-name=\"postgresql\",enabled=\"true\","
				+ "connection-url=\"jdbc:postgresql://127.0.0.1:5432/" + nombreBase + "\",user-name=\"" + USUARIO
				+ "\",password=\"" + PASSWORD + "\")";
		System.out.println(comandoWindows);
	}
	/* */
}
