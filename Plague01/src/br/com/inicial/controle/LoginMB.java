package br.com.inicial.controle;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Session;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.EmpresaDAO;
import br.com.inicial.dao.HibernateUtil;
import br.com.inicial.dao.UsuarioDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Usuario;
import br.com.inicial.util.JsfUtil;

/**
 *
 * @author Windows Vista
 */
@ManagedBean(name="loginMB")
@SessionScoped
public class LoginMB{

	private Empresa empresa= new Empresa();
	public static Empresa empresa2= new Empresa();
	private String cnpjEmpresa;
	private String login;
	private String senha;
    private Usuario usuario = new Usuario();
    
    public LoginMB() {
    }
    
    private DAOFactory getApplicationDao() {
		Session session = HibernateUtil.getSession(this.empresa);
		DAOFactory daoFachada = new DAOFactory(session);
		return daoFachada;
	}
    
    private DAOFactory getEmpresaDaoFachada() {
		Empresa empresa = null;
		Session session = HibernateUtil.getSession(empresa);
		DAOFactory daoFachada = new DAOFactory(session);
		return daoFachada;
	}

    public String verificaLogin(){
    	try {
			/*Class.forName("org.hibernate.dialect.MySQLDialect");
			Connection con = DriverManager.getConnection("jdbc:mysql://wreliquiasNot/CPRG_EMPRESA", "root","asenna");
			Statement stm = con.createStatement();
			String select = "select id,cnpj,config_file_name,driver_class,PASSWORD,sql_dialect,url_conexao,user_name from prg_empresa where cnpj = '" +cnpjEmpresa +"'";
			ResultSet result = stm.executeQuery(select);
			while (result.next()) {
	     		this.empresa = new Empresa();
	     		this.empresa.setId((Integer) result.getObject(1));
	     		this.empresa.setCnpj(result.getObject(2).toString());
	     		this.empresa.setConfigFileName(result.getObject(3).toString());
	     		this.empresa.setDriverClass(result.getObject(4).toString());
	     		this.empresa.setPassword(result.getObject(5).toString());
	     		this.empresa.setSqlDialect(result.getObject(6).toString());
	     		this.empresa.setURLConexao(result.getObject(7).toString());
	     		this.empresa.setUsername(result.getObject(8).toString());
	         }
			result.close();
			stm.close();
			con.close();*/
    		
    		if(licencaVencida()){
    			JsfUtil.addErrorMessage("Licença Vencida, procure o fornecedor do sistema");
    	    	return null;
    		}
			
	    	EmpresaDAO empresaDao = getEmpresaDaoFachada().getEmpresaDao();
	    	this.empresa = new Empresa();
	    	this.empresa.setCnpj(cnpjEmpresa);
	    	this.empresa = empresaDao.buscarPorCnpj(cnpjEmpresa);
	    	
	    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("empresa", this.empresa);
	    	LoginMB.empresa2 = this.empresa;
	    	
	    	DAOFactory daoFachada = getApplicationDao();
	    	UsuarioDAO usuarioDao = daoFachada.getUsuarioDAO();
	    	Usuario usuario = usuarioDao.buscarPorLogin(login);
//	    	Usuario usuario = DAOFactory.criarUsuarioDAO().buscarPorLogin(login);
	    	if(usuario!=null && usuario.getId()!=null){
				if(usuario.getSenha().equalsIgnoreCase(senha)){
					
					return "/restrito/principal";
				}
			}
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JsfUtil.addErrorMessage("Credenciais inválidas");
    	return null;
    }
    
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getCnpjEmpresa() {
		return cnpjEmpresa;
	}

	public void setCnpjEmpresa(String cnpjEmpresa) {
		this.cnpjEmpresa = cnpjEmpresa;
	}
	
	private Boolean licencaVencida(){
//		String dataLimite = "28/01/2017";
		Calendar dataLimite = Calendar.getInstance();
		dataLimite.set(2017, 12, 28);
		Calendar dataHoje = Calendar.getInstance();
		if(dataLimite.compareTo(dataHoje) <=0){
			System.out.println("Venceu");
			return true;
		}else{
			System.out.println("Pode usar vai");
			return false;
		}
	}
}
