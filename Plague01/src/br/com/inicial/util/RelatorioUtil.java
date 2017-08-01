package br.com.inicial.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.inicial.modelo.Empresa;


public class RelatorioUtil {

	public static final int	RELATORIO_PDF							= 1;
	public static final int	RELATORIO_EXCEL						= 2;
	public static final int	RELATORIO_HTML							= 3;
	public static final int	RELATORIO_PLANILHA_OPEN_OFFICE	= 4;
        private static Connection con;

	public StreamedContent geraRelatorio(HashMap parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio, Empresa empresa) throws UtilException {
		StreamedContent arquivoRetorno = null;
//http://davidbuzatto.com.br/tag/subrelatorio/
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Connection conexao = this.getConexao2(empresa);
			String caminhoRelatorio = context.getExternalContext().getRealPath("relatorios");
			String caminhoArquivoJasper = caminhoRelatorio + File.separator + nomeRelatorioJasper + ".jasper";
			String caminhoArquivoRelatorio = null;
			parametrosRelatorio.put("SUBREPORT_DIR", caminhoRelatorio);
//			paramentrosRelatorio.put("SUBREPORT_DIR", "C:\\Fontes Java\\BgiWeb\\WebContent\\relatorios\\");

			JasperReport relatorioJasper = (JasperReport) JRLoader.loadObject(caminhoArquivoJasper);
			JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, conexao);
			JRExporter tipoArquivoExportado = null;
			String extensaoArquivoExportado = "";
			File arquivoGerado = null;

			switch (tipoRelatorio) {
				case RelatorioUtil.RELATORIO_PDF :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
				case RelatorioUtil.RELATORIO_HTML :
					tipoArquivoExportado = new JRHtmlExporter();
					extensaoArquivoExportado = "html";
					break;
				case RelatorioUtil.RELATORIO_EXCEL :
					tipoArquivoExportado = new JRXlsExporter();
					extensaoArquivoExportado = "xls";
					break;
				case RelatorioUtil.RELATORIO_PLANILHA_OPEN_OFFICE :
					tipoArquivoExportado = new JROdtExporter();
					extensaoArquivoExportado = "ods";
					break;
				default :
					tipoArquivoExportado = new JRPdfExporter();
					extensaoArquivoExportado = "pdf";
					break;
			}
			caminhoArquivoRelatorio = caminhoRelatorio + File.separator + nomeRelatorioSaida + "." + extensaoArquivoExportado;
			arquivoGerado = new java.io.File(caminhoArquivoRelatorio);
			tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
			tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
			tipoArquivoExportado.exportReport();
			arquivoGerado.deleteOnExit();

			InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
			arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado, nomeRelatorioSaida + "." + extensaoArquivoExportado);
		} catch (JRException e) {
			throw new UtilException("Nao foi possivel gerar o relatorio.", e);
		} catch (FileNotFoundException e) {
			throw new UtilException("Nao foi possivel gerar o relatorio.", e);
		}
		return arquivoRetorno;
	}

	private Connection getConexao() throws UtilException {
		java.sql.Connection conexao = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env/");
			javax.sql.DataSource ds = (javax.sql.DataSource) envContext.lookup("jdbc/BgiWebBd");
			conexao = (java.sql.Connection) ds.getConnection();
		} catch (NamingException e) {
			throw new UtilException("Nï¿½o foi possï¿½vel encontrar o nome da conexï¿½o do banco.", e);
		} catch (SQLException e) {
			throw new UtilException("Ocorreu um erro de SQL.", e);
		}
		return conexao;
	}


	private  Connection getConexao2(Empresa empresa) throws UtilException {
		String driver = empresa.getDriverClass();//"com.mysql.jdbc.Driver";
		String url = empresa.getURLConexao();//"jdbc:mysql://localhost:3306/myplague02";
		String login = empresa.getUsername();//"root";
		String senha = empresa.getPassword();//"asenna";
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, login, senha);
		} catch (ClassNotFoundException e) {
			throw new UtilException("Driver nÃ£o encontrado: " + e.getMessage());
		} catch (SQLException e) {
			throw new UtilException("Erro abrindo conexÃ£o: " + e.getMessage());
		}
		return con;
	}

        //Eu coloquei esse codigo que encontrei no javafree
    public StreamedContent gerarRelatoriosClientes(HashMap parametrosRelatorio, String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio, Empresa empresa) throws Exception {
        StreamedContent rel = null;
//rel e um nome para demostra o relatorio.
//relatorio que traz todas informaÃ§Ãµes.
        try {
            Connection con = getConexao2(empresa);//e para conexaÃ§Ã£o di banco estatico tem que ser criado pra chama o //dados do banco
//            HashMap map = new HashMap();
//            String arquivoJasper = "relatorioclientes2.jasper";//nome do arquivo de relatorio que devera esta na pasta //principal do projeto api de java...
            rel = (StreamedContent) JasperFillManager.fillReport(nomeRelatorioJasper, parametrosRelatorio, con);//coloque os nomes que voce demonina emcima
        } catch (JRException e) {
            e.printStackTrace();
        }
        return rel;//returna seu relatorio
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        RelatorioUtil.con = con;
    }
}
