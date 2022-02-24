package utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static boolean validarRUT(String rut) {

		if (rut.length() != 12) {
			return false;
		}

		int[] factores = { 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

		int probableCheck;

		List<Integer> listaNumeros = new ArrayList<Integer>();

		for (int i = 0; i < rut.length(); i++) {
			listaNumeros.add(Integer.parseInt("" + rut.charAt(i)));
		}
		// CASOS DE ERROR:
		// Primeros 2 d�gitos menores a 01 o mayores a 21
		if (listaNumeros.get(0) * 10 + listaNumeros.get(1) < 1 || listaNumeros.get(0) * 10 + listaNumeros.get(1) > 21) {
			return false;
		}

		// 3,4,5,6,7,8 cifras iguales a 0
		if (rut.substring(2, 7).equals("000000")) {
			return false;
		}

		// 9� y 10� d�gitos distintos de 0
		if (listaNumeros.get(8) != 0 || listaNumeros.get(9) != 0) {
			return false;
		}

		int suma = 0;
		for (int i = 0; i < listaNumeros.size() - 1; i++) {
			suma += listaNumeros.get(i) * factores[i];
		}

		int resto = suma % 11;

		probableCheck = 11 - resto;

		// CALCULO DEL DIGITO VERIFICADOR
		// Si es menor a 10 el probable check entonces queda ese
		if (probableCheck < 10 && probableCheck == listaNumeros.get(11)) {
			return true;
		}

		// Si es 11 el probable check entonces el check digit es 0
		if (probableCheck == 11 && listaNumeros.get(11) == 0) {
			return true;
		}

		// Si es 0 no se adjudica ninguno
		if (probableCheck == 10) {
			return false;
		}

		return false;
	}
	
}
