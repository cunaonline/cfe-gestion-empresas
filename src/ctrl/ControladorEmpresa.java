package ctrl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class ControladorEmpresa {

	private static final String CMD_WIN = "cmd.exe";
	private static final String CMD_LINUX = "bash";

	private Validator validator = null;
	private Empresa empresa = null;

	public ControladorEmpresa() {
		super();
		this.iniciarValidador();
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
			} catch (IOException e) {
				e.printStackTrace();
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

}
