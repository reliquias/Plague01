package br.com.inicial.testes;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import br.com.inicial.util.Utils;

public class TestesGeral01 {

	public static void main(String[] args) throws IOException, ParseException {
		/*String dataFirebase = "sáb, 07/01/2017, 18:31:35";
		String data = dataFirebase.substring(5, 15);
		String hora = dataFirebase.substring(17);
		String horaC = data + " " +hora;
		Calendar x = Utils.stringToCalendar("dd/MM/yyyy HH:mm:ss", horaC);
		System.out.println(Utils.toString(x));*/
		
		validaData();
	}
	
	private static Boolean validaData(){
//		String dataLimite = "28/01/2017";
		Calendar dataLimite = Calendar.getInstance();
		dataLimite.set(2017, 05, 28);
		Calendar dataHoje = Calendar.getInstance();
		System.out.println(Utils.toString(dataLimite));
		if(dataLimite.compareTo(dataHoje) <=0){
			System.out.println("Venceu");
			return false;
		}else{
			System.out.println("Pode usar vai");
			return true;
		}
	}
}
