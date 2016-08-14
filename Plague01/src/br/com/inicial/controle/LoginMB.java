package br.com.inicial.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.UsuarioDAO;
import br.com.inicial.modelo.Usuario;
import br.com.inicial.util.JsfUtil;

/**
 *
 * @author Windows Vista
 */
@ManagedBean(name="loginMB")
@SessionScoped
public class LoginMB{

	private String login;
	private String senha;
    private Usuario usuario = new Usuario();
    
    public LoginMB() {
    }

    public String verificaLogin(){
    	UsuarioDAO usuarioDao = DAOFactory.criarUsuarioDAO(); 
    	Usuario usuario = usuarioDao.buscarPorLogin(login);
    	if(usuario!=null && usuario.getId()!=null){
			if(usuario.getSenha().equalsIgnoreCase(senha)){
				return "/restrito/principal";
			}
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
}
