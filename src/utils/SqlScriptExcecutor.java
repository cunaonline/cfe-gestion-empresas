package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SqlScriptExcecutor {

	public void ejecutarScript( String username, String dbname, String password, String scriptFile ) {
		
	    try {
	        String line;
	        Process p = Runtime.getRuntime().exec( "psql -U " +username+ " -d " +dbname+ " -h serverhost -f " + scriptFile );
	        BufferedReader input = new BufferedReader( new InputStreamReader( p.getInputStream() ));
	        while ((line = input.readLine()) != null) {
	          System.out.println(line);
	        }
	        input.close();
	      }
	      catch (Exception err) {
	        err.printStackTrace();
	      }
	}
	
	public static void main(String[] args) {
		
	}
}
