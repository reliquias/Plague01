/*
 * Utils.java
 *
 * Created on 25 de Junho de 2007, 17:24
 *
 */
package br.com.jaque.util;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Classe de metodos utilitarios que nao envolvam acesso a banco de dados.
 */
/**
 * @author usuario_pc10
 *
 */
public class Utils {

	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

	/**
	 * Testa se o objeto passado é uma String vazia ou null no caso de objeto do
	 * tipo String caso contrário apenas se é null.
	 * 
	 * @param objeto
	 *            Objeto a ser verificado.
	 * @return true Caso o objeto passado seja null ou branco.
	 */
	public static boolean isBlankOrNull(Object objeto) {
		if (objeto instanceof String) {
			if ((String) objeto == null || ((String) objeto).trim().equals("")) {
				return true;
			}
		} else {
			if (objeto == null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isZeroOrNull(BigDecimal objeto) {
		if (objeto == null || objeto.compareTo(new BigDecimal(0)) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isZeroOrNull(Integer objeto) {
		if (objeto == null || objeto.compareTo(new Integer(0)) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isZeroOrNull(Short objeto) {
		if (objeto == null || objeto.compareTo(Short.valueOf("0")) == 0) {
			return true;
		}
		return false;

	}

	public static String toString(Calendar c) {
		if (c == null) {
			return "";
		}
		return format.format(c.getTime());
	}

	public static String toString(Calendar c, String mascara) {
		if (c == null) {
			return "";
		}
		return new SimpleDateFormat(mascara).format(c.getTime());
	}

	public static String toString(Object[] state) {

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < state.length; i++) {
			if (state[i] instanceof Calendar) {
				builder.append(Utils.toString((Calendar) state[i]));
			} else {
				builder.append(state[i] + ",");
			}
		}

		if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	public static String toString(Object state) {

		StringBuilder builder = new StringBuilder();

		if (state instanceof Calendar) {
			builder.append(Utils.toString((Calendar) state));
		} else {
			builder.append(state);
		}

		return builder.toString();
	}

	/**
	 * Torna a 1a. letra da string minuscula.
	 * 
	 * @param string
	 *            String que seseja tornar a 1a. letra minuscula.
	 * @return String
	 */
	public static String decapitaliza(String string) {
		String letra = string.substring(0, 1);
		return letra.toLowerCase() + string.substring(1);
	}

	/**
	 * Torna a 1a. letra da string maiscula.
	 * 
	 * @param string
	 *            String que seseja tornar a 1a. letra maiscula.
	 * @return String
	 */
	public static String capitaliza(String string) {
		String letra = string.substring(0, 1);
		return letra.toUpperCase() + string.substring(1);
	}

	/**
	 * Retorna o nome do atributo dos metodos getters/setters.
	 * 
	 * @param nomeMetodo
	 *            Nome do metodo getter/setter que deseja o nome do atributo.
	 * @return Nome do atributo.
	 */
	public static String extractAttributeFromMethodName(String nomeMetodo) {
		String atributo = nomeMetodo.substring(3);
		return Utils.decapitaliza(atributo);
	}

	/**
	 * Testa se o nome do metodo é um metodo do tipo getter.
	 * 
	 * @param nomeMetodo
	 *            Nome do metodo que deseja verificar
	 * @return true se o metodo é do tipo getter.
	 */
	public static boolean isGetter(String nomeMetodo) {
		return nomeMetodo.startsWith("get") && !"getClass".equals(nomeMetodo);
	}

	/**
	 * Testa se a string passada começa com o fragmento.
	 * 
	 * @param string
	 *            String que deseja verificar.
	 * @param fragmento
	 *            Porção de texto que deseja verificar se existe no inicio da
	 *            string.
	 * @return true se a string começa com o fragmento.
	 */
	public static boolean comecaCom(String string, String fragmento) {
		return string.startsWith(fragmento);
	}

	/**
	 * Verifica se o atributo padrão da classe esta vazio ou nulo.
	 * 
	 * @param obj
	 *            Objeto da classe que deseja verificar o campo padrão.
	 * @return true Se o campo padrão esta branco ou nulo.
	 */
	
	// Metodo original do sistema de importação antigo
	public static Image renderImage(byte[] figura) {
		int height = 110;
		int width = 370;

		if (figura != null) {
			MediaTracker traker = new MediaTracker(new Panel());

			Image img = Toolkit.getDefaultToolkit().createImage(figura);

			traker.addImage(img, 0);
			try {
				traker.waitForID(0);
			} catch (Exception e) {
				img = null;
			}

			Image scaledImage = img.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			traker.addImage(scaledImage, 1);
			try {
				traker.waitForID(1);
			} catch (Exception e) {
				scaledImage = null;
			}

			Toolkit.getDefaultToolkit().prepareImage(scaledImage, -1, -1, null);
			traker.addImage(scaledImage, 2);
			try {
				traker.waitForID(2);
			} catch (Exception e) {
				scaledImage = null;
			}
			return scaledImage;
		} else {
			return null;
		}
	}

	/*
	 * Verifica se o objeto é nulo ou é zero em notação científica e retorna
	 * BigDecimal.ZERO para não dar pau na página (quando 0-7E formato notação)
	 * ou em qualquer classe quando null
	 */
	public static BigDecimal verificaNuloOuNotacao(BigDecimal obj) {
		if (obj == null || obj.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return obj;
		}
	}

	public static String verificaNuloOuVazio(String obj) {
		if (obj == null || obj.isEmpty()) {
			return null;
		} else {
			return obj;
		}
	}

	// ROTINA UTILIZADA NA MIGRACAO DE DADOS PARA RETORNAR QUALQUER OBJETO PASSADO
	public static Object verificaObjetoNuloOuVazio(Object obj) {
		if (obj == null || obj.equals("")) {
			return null;
		} else {
			return obj;
		}
	}
	
	/*
	 * Verifica se o objeto é nulo e retorna zero para não dar pau na página ou
	 * em qualquer classe quando null
	 */
	public static Short substituiNuloPorZero(Short obj) {
		if (obj == null) {
			return Short.valueOf("0");
		} else {
			return obj;
		}
	}

	/*
	 * Verifica se o objeto é nulo e retorna zero para não dar pau na página ou
	 * em qualquer classe quando null
	 */
	public static BigDecimal substituiNuloPorZero(BigDecimal obj) {
		if (obj == null || obj.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return obj;
		}
	}
	
	/*
	 * Verifica se o objeto é nulo e retorna zero para não dar pau na página ou
	 * em qualquer classe quando null
	 */
	public static Long substituiNuloPorZero(Long obj) {
		if (obj == null) {
			return Long.valueOf("0");
		} else {
			return obj;
		}
	}

	/*
	 * Verifica se o objeto é nulo e retorna zero para não dar pau na página ou
	 * em qualquer classe quando null
	 */
	public static Integer substituiNuloPorZero(Integer obj) {
		if (obj == null) {
			return Integer.valueOf("0");
		} else {
			return obj;
		}
	}

	/*
	 * Verifica se o objeto é nulo e retorna string vazia para não dar pau na
	 * página ou em qualquer classe quando null
	 */
	public static String substituiNuloPorString(String obj) {
		if (obj == null) {
			return "";
		} else {
			return obj;
		}
	}

	/*
	 * Verifica se o objeto é nulo e retorna string vazia para não dar pau na
	 * página ou em qualquer classe quando null
	 */
	public static Object substituiNuloPorObject(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj;
		}
	}

	/*
	 * Verifica se o objeto é nulo e retorna false para não dar pau na página ou
	 * em qualquer classe quando null
	 */
	public static Boolean substituiNuloPorFalse(Boolean obj) {
		if (obj == null) {
			return Boolean.FALSE;
		} else {
			return obj;
		}
	}

	public static Date stringToDate(String formato, String data) throws ParseException {
		SimpleDateFormat formatDate = new SimpleDateFormat(formato);

		return formatDate.parse(data);
	}

	public static String dateToString(String formato, Date data) {
		SimpleDateFormat formatDate = new SimpleDateFormat(formato);

		return formatDate.format(data);
	}
	
	public static Calendar stringSemBarraToCalendar(String data){
		try {
			String[] pedacos = new String[3];
			pedacos[0] = data.substring(0, 2);
			pedacos[1] = data.substring(2, 4);
			pedacos[2] = data.substring(4, 8);
			
			data = pedacos[0] + "/" + pedacos[1] + "/" + pedacos[2];
			
			Calendar calendar = calendarioEmBranco();
			calendar.setTime(stringToDate("dd/MM/yyyy", data));
			return calendar;
		} catch (Exception e){
			return null;
		}
	}

	public static String completaComCaracter(String conteudo, char complemento,
			int quantidade, Boolean inicio) {
		String adicional = "";
		for (int i = 0; i < quantidade; i++) {
			adicional = adicional + complemento;
		}
		if (inicio) {
			return adicional + conteudo;
		} else {
			return conteudo + adicional;
		}
	}

	public static boolean comparaObjetos(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		} else if ((obj1 == null && obj2 != null)) {
			return false;
		} else if ((obj1 != null && obj2 == null)) {
			return false;
		} else if (obj1.toString() == null && obj2.toString() == null) {
			return true;
		} else if (obj1 instanceof String || obj1 instanceof String) {
			return ((String) obj1).equals((String) obj2);
		} else if (obj1 instanceof BigDecimal || obj1 instanceof BigDecimal) {
			return ((BigDecimal) obj1).compareTo((BigDecimal) obj2) == 0;
		} else if (obj1 instanceof Short || obj1 instanceof Short) {
			return ((Short) obj1).compareTo((Short) obj2) == 0;
		} else if (obj1 instanceof Integer || obj1 instanceof Integer) {
			return ((Integer) obj1).compareTo((Integer) obj2) == 0;
		} else if (obj1 instanceof Calendar || obj1 instanceof Calendar) {
			return ((Calendar) obj1).compareTo((Calendar) obj2) == 0;
		} else if (obj1 instanceof Boolean || obj1 instanceof Boolean) {
			return ((Boolean) obj1).equals((Boolean) obj2);
		} else {
			return obj1.toString().equals(obj2.toString());
		}
	}

	public static Boolean validaCNPJ(String valor) {
		return Utils.validaIntegerDeString(valor, 14);
	}

	public static Boolean validaIntegerDeString(String valor, Integer tamanho) {
		if (valor == null) {
			return Boolean.FALSE;
		} else {
			return Utils.extraiNumericos(valor).length() == tamanho;
		}
	}

	public static String extraiNumericos(String valor) {
		if (valor != null) {
		  return valor.replaceAll("[^0-9]", "");
		} else {
			return "";
		}
	}

	public static void geraArquivo(String nome, String conteudo, HttpServletResponse response) throws IOException {      
		response.setContentType("inline/download");
		String arq = "attachment;filename=" + nome;
		response.setHeader("Content-Disposition", arq);

		ServletOutputStream os = response.getOutputStream();
		os.write(conteudo.getBytes());
		os.flush();
		os.close();		
	}
	
	public static String removeAcentos(String conteudo) {
		conteudo = conteudo.replaceAll("[ÂÀÁÄÃ]", "A");
		conteudo = conteudo.replaceAll("[âãàáä]", "a");
		conteudo = conteudo.replaceAll("[ÊÈÉË]", "E");
		conteudo = conteudo.replaceAll("[êèéë]", "e");
		conteudo = conteudo.replaceAll("[ÎÍÌÏ]", "I");
		conteudo = conteudo.replaceAll("[îíìï]", "i");
		conteudo = conteudo.replaceAll("[ÔÕÒÓÖ]", "O");
		conteudo = conteudo.replaceAll("[ôõòóö]", "o");
		conteudo = conteudo.replaceAll("[ÛÙÚÜ]", "U");
		conteudo = conteudo.replaceAll("[ûúùü]", "u");
		conteudo = conteudo.replaceAll("Ç", "C");
		conteudo = conteudo.replaceAll("ç", "c");
		conteudo = conteudo.replaceAll("[ýÿ]", "y");
		conteudo = conteudo.replaceAll("Ý", "Y");
		conteudo = conteudo.replaceAll("ñ", "n");
		conteudo = conteudo.replaceAll("Ñ", "N");
		return conteudo;		

	}

    //Tratamento de String's
    /**
     * Forma um texto colocando zeros a esquerda
     * e retirando valores maiores que o campo desejado:
     * Por exemplo:
     * formatText9("5", 4) retorna 0005
     * formatText9("16", 3) retorna 016
     * formatText9("00005", 4) retorna 0005
     */
    public static String formatText9(String value, int tamanhoCampo) {
    	StringBuffer retorno = new StringBuffer(value);
    	while (retorno.length() < tamanhoCampo) {
    		retorno.insert(0, "0");
    	}
    	while (retorno.length() > tamanhoCampo) {
    		retorno.deleteCharAt(0);
    	}
    	return retorno.toString();
    }
    
    public static String cortaPosicoes(String string, int tamanho) {
    	if (string.length() > tamanho) {
    		String retorno = string.substring(0, (tamanho));
    		return retorno;
    	} else {
    		return string;
    	}
    }
    
	public static Calendar calendarioEmBranco() {
		//Calendar.getInstance() seta valores de milisegundo que não possibilitam que o compareTo() retorne o resultado igual (0)
		Calendar calendar =  Calendar.getInstance();
		calendar.clear(); 
		return calendar;
	}
	
	public static BigDecimal stringToBigDecimal(String numero, int precisao, int escala) {
		
/*		 ESCALA > 0 (número decimal)
		 =========================
		 100.200,100 >> 100200.100 (precisao = 9, escala = 3)
 	 		   1,100 >> 	 1.100 (precisao = 4, escala = 3)
 	 		   
		 100,200.100 >> 100200.100 
		       1.100 >> 	 1.100
		
		 Formatos decimais digitados incorretamente: 
		 100.100.10 >> 100100.10
		 100,100,10 >> 100100.10

		 ESCALA = 0 (número inteiro)
		 ========================
		 100.100.100 >> 100100100
		 100,100,100 >> 100100100
*/		
		//número decimal
		if (escala > 0) {
			int ultimoPonto = numero.lastIndexOf('.');
			int ultimaVirgula = numero.lastIndexOf(',');
			boolean formatoBrasileiro = (ultimaVirgula > ultimoPonto);
			
			if (formatoBrasileiro) {
				numero = numero.replace(".", "").replace(',', '.');
			} else {
				numero = numero.replace(",", "");
			}
			
			//se ponto decimal digitado incorretamente manter somente o último
			if (numero.indexOf('.') != numero.lastIndexOf('.')) {
				
				int posicaoDecimal = numero.lastIndexOf('.');
				String inteiro = numero.substring(0, posicaoDecimal).replace(".", "");
				String decimal = numero.substring(posicaoDecimal + 1);
				
				numero = inteiro + "." + decimal;
			}
		}
		//número inteiro
		else {
			numero = numero.replace(".", "").replace(",", "");
		}
		
		try {
			BigDecimal num = new BigDecimal(numero.trim()).setScale(escala, RoundingMode.HALF_UP);
			if (num.precision() > precisao) {
				return BigDecimal.ZERO;
			} else {
				return num;
			}
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	public static short stringToShort(String numero) {
		try {
			return Short.valueOf(numero.trim());
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Calendar stringToCalendar(String dataString) {
		try {
			Calendar calendar = calendarioEmBranco();
			calendar.setTime(stringToDate("dd/MM/yyyy", dataString));
			return calendar;
		} catch (Exception e){
			return null;
		}
	}
	
	public static boolean stringToBoolean(String booleanString) {
		try {
			return Boolean.valueOf(booleanString.toLowerCase().trim());
		} catch (Exception e){
			return Boolean.FALSE;
		}
	}

	public static boolean stringVFToBoolean(String booleanString) {
		if(booleanString.equalsIgnoreCase("T")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean stringSNToBoolean(String booleanString) {
		if(booleanString.equalsIgnoreCase("S")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean stringENToBoolean(String booleanString) {
		if(booleanString.equalsIgnoreCase("E")){
			return true;
		}else{
			return false;
		}
	}
	
	public static int contarCaracteres(String expressao, Character simbolo ) {
		int contador = 0;
		for (Character character : expressao.toCharArray()) {
			if (character.equals(simbolo)){
				contador += 1;
			}
		}
		return contador;
	}
	
	public static boolean isNumeric(String expressao) {
		try {
			Double.parseDouble(expressao);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
    public static void downloadArquivo (ServletContext context, HttpServletResponse response, String caminhoArquivo) throws IOException {
    	//ex: caminho arquivo = "/help/planilhas/Cliente.xls"
    	
    	File file = new File(context.getRealPath(caminhoArquivo));
    	FileInputStream fileInputStream = new FileInputStream(file);
    	
    	byte[] bytes = new byte[(int) file.length()];
    	
    	fileInputStream.read(bytes);
    	
    	Utils.geraArquivo(file.getName(), new String(bytes), response);

    }
    
	public static void criarCookie(HttpServletResponse response, String valor, int tempoVida) {
		String assinatura = "SoftLeasing";
		String cookieName = "SLAcesso" ;

		Cookie cookie = new Cookie(cookieName, assinatura);
		cookie.setValue(valor);
		cookie.setMaxAge(tempoVida); // em segundos
		response.addCookie(cookie);
	}
	
	public static void limparCookie(HttpServletRequest request, HttpServletResponse response, String valor) {
		String cookieName = "SLAcesso" ;
		
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName) && cookie.getValue().equals(valor)) {
					//maxAge = 0 o cookie é destruído
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					return;
			}
		}
	}
	
	public static boolean obterCookie(HttpServletRequest request, String valor) {
		String cookieName = "SLAcesso" ;
		
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName) && cookie.getValue().equals(valor)) {
					return true;
			}
		}
		
		return false;
	}

	public static Collection ordenarNatural(List lista, Class classe) {
		for (Class interf: classe.getInterfaces()) {
    		if (interf.equals(Comparable.class) ) {
    			Collections.sort(lista);
    		}
    	}
		return lista;
	}
	
	public static String converterStringParaUmaLinha(String stringMultiLinhas){
		if(stringMultiLinhas == null){
			return "";
		}
		return stringMultiLinhas.replace("\n", " ").replace("\r", "");
	}
	
	public static String gerarSelectOption(String value, String label, boolean selecionado) {
		return "<option value='"+ value +"' " + (selecionado ? " selected " : "") + " >"+ label +"</option>";
	}
	
	/** 
     * Converte uma String para um objeto Date. Caso a String seja vazia ou nula,  
     * retorna null - para facilitar em casos onde formulários podem ter campos 
     * de datas vazios. 
     * @param data String no formato dd/MM/yyyy a ser formatada 
     * @return Date Objeto Date ou null caso receba uma String vazia ou nula 
     * @throws Exception Caso a String esteja no formato errado 
     */  
    public static java.sql.Date formataData(String data) throws Exception {   
        if (data == null || data.equals(""))  
            return null;  
          
        java.sql.Date date = null;  
        try {  
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
            date = new java.sql.Date( ((java.util.Date)formatter.parse(data)).getTime() );  
        } catch (ParseException e) {              
            throw e;  
        }  
        return date;  
    }
    
    public static String inserindo(String sb, int num, int num2, char c){  
        return sb.substring(0,num)+c+sb.substring(num,sb.length()).substring(0, num2)+c+sb.substring(num2, sb.length());  
      } 
    
    public static String removeCodigoTotal(String select){
		String[] retorno;
		retorno = select.toLowerCase().split("where");
		String sele = "";
		int cont = 0;
		for (String string : retorno) {
			if(cont == 0){
				sele = sele.concat(select.substring(0, string.length()).replace("#", "").replace("-", ""));
			}else{
				sele = sele.concat(string.replace("#", ""));
			}
			sele = sele.concat(" where ");
			cont++;
		}
		
		return sele.substring(0, sele.length() - 6);
		
	}
    
    public static List<String> camposTotais (String select){
    	//Para o total funcionar os simbolo deve # deve estar antes do nome da coluna (que sera apresentado)
    	//e o @ depois do nome da coluna (que sera apresentada). 
    	//Ex.1 = ,valor_incoterm [#Valor Incoterm@],
    	//Ex.2 = ,#VALOR_UNITARIO@,
		String[] selects = select.split("#");
		List<String> camposTotais = new ArrayList<String>();
		for (String string : selects) {
			String[] picado;
			picado = string.split("@");
			camposTotais.add(picado[0].replace(",", ""));
		}
		camposTotais.remove(0);
		return camposTotais;
    }
    
    public static String formulaExcelMaisUm(String formula, int col, int linha){
    	List<String> formCelulaList = new ArrayList<String>();
		String form = formula.substring(15);
		String form2 = null;
		String formulaSaida = null;
		String operador = null;
		
		List<String> operadores = new ArrayList<String>();
		operadores.add("*");
		operadores.add(":");
		operadores.add("/");
		operadores.add("-");
		String[] duas = null;
		for (String string : operadores) {
			if(form.contains(string)){
				operador = string;
				if(form.contains("*")){
					form2 = form.replace("*", ";");
					string = ";";
				}
				duas = form2.substring(1).split(string);
			}
		}
		for (String string : duas) {
			formCelulaList.add((string));
		}
		
		Integer mais1 = Integer.parseInt(formCelulaList.get(0).substring(1));
		Integer mais2 = Integer.parseInt(formCelulaList.get(1).substring(1));
		
		//vai ser aq
		mais1 = (linha-mais1) + mais1;
		mais2 = (linha-mais2) + mais2;
		
		mais1 = mais1 + col;
		mais2 = mais2 + col;
		
		formulaSaida = form.substring(5, 6);
		formulaSaida = formula.substring(16, 17)+ mais1 + operador + formulaSaida + mais2;
		return formulaSaida;
    	
    }
    
	public static Calendar diaPrimeiro(Calendar date){
		   int mes = date.get(Calendar.MONTH);
		   mes = mes+1;
		   Calendar gc = date;//assume a data e hora atuais
//		   gc.setTime(date);
		   //Ajustar campos da data:  
//		   gc.set(GregorianCalendar.YEAR, 2008); //muda o ano  
		   gc.set(Calendar.MONTH, mes); //muda o mês -> Atenção: 0 = janeiro, 1 = fevereiro, etc.  
		   gc.set(Calendar.DAY_OF_MONTH, 1); //muda o dia do mês  
		return gc;
		   
	   }
	
	
	/**
	 * 
	 * @descrição 
	 * Recebe duas datas e retorna o número de dias entre a primeira e a segunda
	 * @param data1 String no formato dd/MM/yyyy, esta data tem que ser anterior à data2
	 * @param data2 String no formato dd/MM/yyyy, esta data tem que ser posterior à data1
	 * @return número de dias entre a data1 e data2
	 */
	public static int numeroDias(String data1, String data2)
	{
		
		//utilizando o método valida:
		if(!valida(data1)||!valida(data2))
		{
			System.out.println("Data invalida!");
			System.exit(1);
		}
		
		//atribuindo o dia, mês e ano as variáveis:
		int dia1=Integer.parseInt(data1.substring(0,2));
		int mes1=Integer.parseInt(data1.substring(3,5));
		int ano1=Integer.parseInt(data1.substring(6,10));
		
		int dia2=Integer.parseInt(data2.substring(0,2));
		int mes2=Integer.parseInt(data2.substring(3,5));
		int ano2=Integer.parseInt(data2.substring(6,10));
		
		//utilizando o método bissexto e se o dia e o mês e o ano são válidos:
		if(!bissexto(dia1,mes1,ano1)||!bissexto(dia2,mes2,ano2))
		{
			System.out.println("Data invalida!");
			System.exit(1);
		}
		
		//verificando se uma data é maior que a outra:
		if(ano2<ano1)
		{
			System.out.println("A data inicial digitada deve ser menor que a final!");
			System.exit(1);
		}
		else
		if((ano2==ano1)&&(mes2<mes1))
		{
			System.out.println("A data inicial digitada deve ser menor que a final!");
			System.exit(1);
		}
		else
		if(dia2<dia1)
		{
			System.out.println("A data inicial digitada deve ser menor que a final!");
			System.exit(1);
		}
		
		//calculando a diferença de dias entre as datas:
		GregorianCalendar d1= new GregorianCalendar();
		d1.set(GregorianCalendar.DATE, dia1);
		d1.set(GregorianCalendar.MONTH, mes1);
		d1.set(GregorianCalendar.YEAR, ano1);
		d1.getTime();
		
		GregorianCalendar d2= new GregorianCalendar();
		d2.set(GregorianCalendar.DATE, dia2);
		d2.set(GregorianCalendar.MONTH, mes2);
		d2.set(GregorianCalendar.YEAR, ano2);
		d2.getTime();
		return diferencaEmDias(d1,d2);

	}
	
	//criando o método valida:
	//Validador do método numeroDias
	private static boolean valida(String str)
	{
		//verificando se a data possui somente os caracteres 0123456789/
		for(int i=0;i<str.length();i++)
		{
			char ch=str.charAt(i);//verificando se nos índices data[5] e data[2] estão as barras "/"
			if((i==2)||(i==5))
			{
				if("/".indexOf(ch)==-1)
				{
					return(false);
				}
			}
			else
			if("0123456789".indexOf(ch)==-1)//verificando se nos outros índices estão os números "0123456789"
			{
				return(false);
			}
		}
		return(true);		
	}
	
	//método q verifica se o ano é bissexto:
	//Validador do método numeroDias
	private static boolean bissexto(int dia, int mes, int ano)
	{
		//verificando se o dia e o mês e o ano são válidos:
		if((dia<1)||(dia>31)||(mes<1)||(mes>12)||(ano<1000))
		{
			return(false);
		}
		if(ano%400==0 || (ano%100!=0 && ano%4==0))
		{
			if((mes==2)&&(dia>29))
			{
				return(false);
			}
		}
		else
		if((mes==2)&&(dia>28))
		{
				return(false);
		}
		return(true);
	}
	
	//Validador do método numeroDias
	public static int diferencaEmDias(Calendar c1, Calendar c2)
	{
		long m1 = c1.getTimeInMillis();
		long m2 = c2.getTimeInMillis();
		return (int) ((m2 - m1) / (24*60*60*1000));
	}
	
    public static String substituiNuloPorZero(String obj) {
        if (obj == null) {
            return "0";
        } else {
            return obj;
        }
    }

    public static String substituiVazioPorZero(String obj) {
    	if (obj == "") {
    		return "0";
    	} else {
    		return obj;
    	}
    }
    
	public static double stringToDouble(String arg, String separador) throws ParseException {
		NumberFormat nf;
		if(separador.equals(",")){
			nf = NumberFormat.getNumberInstance(Locale.GERMANY);
		}else{
			nf = NumberFormat.getNumberInstance(Locale.US);
		}
		// obtem um NumberFormat para o Locale default (BR)
		
//		nf = NumberFormat.getNumberInstance();
		
		// converte um número com vírgulas ex: 2,56 para double
		double number = nf.parse(arg).doubleValue();
		return number;
	}
}
