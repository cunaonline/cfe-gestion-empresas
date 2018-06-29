package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DataBaseManager {
	
	private Connection conn;
	private Configuration conf;
	
	public DataBaseManager() {
		conf = Configuration.getInstance();
	    try {
	        Class.forName( "org.postgresql.Driver" );
	    } catch (ClassNotFoundException e) {
	        System.err.println("Where is your PostgreSQL JDBC Driver? "
	                + "Include in your library path!");
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
	

	/**
	 * Abre una conexion generica<br>
	 * <b>Se usa para crear la base de datos</b>.
	 * @throws Exception
	 */
	public void conectar() throws Exception { 
		this.conectar( "/postgres" );
	}
	
	/**
	 * Abre una conexion a la base de datos db.<br>
	 * se
	 * @param db el nombre de la base de datos
	 * @throws Exception
	 */
	public void conectar( String db )  throws Exception {
		conn = DriverManager.getConnection( conf.getDbUrl() + db, conf.getDbUser(), RNEncrypter.decrypt( conf.getDbPass() ));
	}

	/**
	 * Crea la base de datos.
	 * @param nombreBase
	 * @return
	 */
	public boolean crearBase( String nombreBase ) {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(true);
			// Crea la base de datos
			stmt = conn.prepareStatement( "CREATE DATABASE " + nombreBase +
							"  WITH OWNER = rnlocal " + 
							"       ENCODING = 'UTF8' "	+ 
							"       TABLESPACE = pg_default " + 
							"       LC_COLLATE = 'Spanish_Spain.1252' " +
							"       LC_CTYPE = 'Spanish_Spain.1252' " + 
							"       CONNECTION LIMIT = -1");
			// stmt.setString(1, nombreBase);
			stmt.executeUpdate();
			// 
			stmt.close();
			//
			stmt = conn.prepareStatement( "GRANT ALL ON DATABASE " + nombreBase + " TO rnlocal" );
		} catch ( Exception e ) {
			e.printStackTrace(); 
			try {
				if ( stmt != null && !stmt.isClosed() )
					stmt.close();
			} catch ( SQLException e1 ) {
				// do nothing
				e.printStackTrace(); 
			}
			return false;
		}
		return true;
	}
	
	public void ejecutarStriptsBase( String versionServer, boolean isProduccion ) throws Exception {
		String script = Configuration.getInstance().getScriptsFolder() +java.io.File.separator+ "db " + versionServer + ".sql";
		// String scriptTestAProd = Configuration.getInstance().getScriptsFolder() +java.io.File.separator+ "testing_a_produccion" + versionServer + ".sql";
		String scriptTestAProd = Configuration.getInstance().getScriptsFolder() +java.io.File.separator+ "testing_a_produccion" + ".dummie" + ".sql"; // cambiar desp
		String[] scriptsEjecutar = isProduccion ? new String[] { script, scriptTestAProd } : new String[] { script };
		// declaramos las varibles afuera para poder cerrarlas si hay error 
		Statement stmt = null;
		BufferedReader br = null;
		String consulta = "";
		//
		try {
			stmt = conn.createStatement();
			String aux = null;
			for ( String scritpFile : scriptsEjecutar ) { 
				br = new BufferedReader( new FileReader( new File( scritpFile )));
				boolean beginEncontrado = false;
				while(( aux = br.readLine()) != null ) {
					aux = aux.trim();
					if ( aux.length() == 0 || aux.startsWith( "--" )) continue;
					// apendeamos la consulta por si es de mas de una linea
					consulta += aux + " ";
					//
					if ( aux.contains( "BEGIN" ))
						beginEncontrado = true;
					// verificamos si ejecutar o leera la siguiente linea
					if ( aux.endsWith( ";" ) && !beginEncontrado ) {
						// descartamos algunas consultas
						if ( consulta.toUpperCase().startsWith( "CREATE ROLE" ) || consulta.toUpperCase().startsWith( "GRANT ALL ON DATABASE" ))
							consulta = "";
						// verificamos si es una consulta select, o de insercion.
						if ( consulta.toUpperCase().startsWith( "SELECT") )
							stmt.execute( consulta );
						else
							stmt.executeUpdate( consulta );
						// reiniciamos
						consulta = "";
					}
					// verificamos si encontramos el final
					if ( aux.contains( "END" ))
						beginEncontrado = false; // cambiar a un entero, por si hay mas de un begin
				}
				br.close();
			}
			stmt.close();
		} catch ( Exception e ) {
			System.out.println( consulta ); 
			if ( stmt != null && !stmt.isClosed() ) stmt.close();
			if ( br != null ) br.close();
			throw e;
		}
	}
	
	


	public void cerrarConexion() {
		try { conn.close(); } catch ( Exception e ) { /* no hacemos nada */ }
	}
	
	
	
	public void actualizarParametro( String nomParam, String valParm ) {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(true);
			// Crea la base de datos
			stmt = conn.prepareStatement( "Update parametros " + 
							"  set valor_string = ? " + 
							"  where id_parametro = ? ");
			stmt.setString( 1, valParm );
			stmt.setString( 2, nomParam );
			stmt.executeUpdate();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void actualizarParametro( String nomParam, Integer valParm ) {
		actualizarParametro( nomParam, String.valueOf( valParm ));
	}
	
	
	public void actualizarParametro( String nomParam, boolean valParam ) {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(true);
			// Crea la base de datos
			stmt = conn.prepareStatement( "Update parametros " + 
							"  set valor_string = ? " + 
							"  where id_parametro = ? ");
			stmt.setBoolean( 1, valParam );
			stmt.setString( 2, nomParam );
			stmt.executeUpdate();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actualizarParametro( String nomParam, int valParam ) {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(true);
			// Crea la base de datos
			stmt = conn.prepareStatement( "Update parametros " + 
							"  set valor_integer = ? " + 
							"  where id_parametro = ? ");
			stmt.setInt( 1, valParam );
			stmt.setString( 2, nomParam );
			stmt.executeUpdate();
		} catch ( SQLException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actualizarParametroPassword( String nomParam, String passwordSinEncriptar ) {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(true);
			// Crea la base de datos
			stmt = conn.prepareStatement( "Update parametros " + 
							"  set valor_password = ? " + 
							"  where id_parametro = ? ");
			stmt.setString( 1, RNEncrypter.encrypt( passwordSinEncriptar ));
			stmt.setString( 2, nomParam );
			stmt.executeUpdate();
		} catch ( SQLException | InvalidKeyException | InvalidKeySpecException | 
				  NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | 
				  BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String obtenerParametro( String nomParam, String valorDefecto ) {
		PreparedStatement stmt = null;
		try {
			// Crea la base de datos
			stmt = conn.prepareStatement( "Select valor_string parametros where id_parametro = ? ");
			stmt.setString( 1, valorDefecto );
			ResultSet rs = stmt.executeQuery();
			if ( rs.next() ) {
				return rs.getString( 0 );
			} else {
				return valorDefecto;
			}
		} catch ( SQLException e ) {
			return valorDefecto;
		}
	}

	
	public static void main( String[] args ) throws Exception {
		/* *
		DataBaseManager db = new DataBaseManager();
		db.conectar();
		db.crearBase( "test_automatico_v520" );
		System.out.println( "base creada" ); 
		db.cerrarConexion();
		db.conectar( "/test_automatico_v520" );
		db.ejecutarStriptsBase( "5.2.0" );
		System.out.println( "tablas creadas" ); 
		db.cerrarConexion();
		/* */
		String SEP = java.io.File.separator;
		String logo = "rondanet\\algo\\logo.png";
		logo.replaceAll( "[/\\u005c]", SEP );
		System.out.println( logo );
		logo = "rondanet\\algo\\logo.png";
		logo.replaceAll( "[/\\\\]", SEP );
		System.out.println( logo );
	}
}

