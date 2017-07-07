package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.HibernateUtil;
import br.com.inicial.dao.UsuarioDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Usuario;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.Firebase;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="usuarioMB")
@RequestScoped
public class UsuarioMB {

	private Usuario usuario = new Usuario();
	private UsuarioDAO	usuarioDAO;
	private String	       confirmarSenha;
	private List<Object>	usuarios = new ArrayList<Object>();
	private XLazyModel usuariosModel;
	private String	       destinoSalvar;

	public UsuarioMB() {
		/*Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		Session session = HibernateUtil.getSession(empresa);
		DAOFactory daoFachada = new DAOFactory(session);
		this.usuarioDAO = daoFachada.getUsuarioDAO();*/
		this.usuarioDAO = DAOFactory.criarUsuarioDAO();
		usuarios = getLista();
		usuariosModel = new XLazyModel(usuarios);
		if(usuariosModel.getPageSize() == 0){
			usuariosModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		return "usuario";
	}

	public String editar() {
		this.confirmarSenha = this.usuario.getSenha();
		return "usuario";
	}

	public void preparaEditar(ActionEvent actionEvent) {
		this.confirmarSenha = this.usuario.getSenha();
	}
	
	public String listar() {
		usuariosModel = null;
    	return "usuarioLista";
    }
	
	public void adicionaOuAtualiza() {
		FacesContext context = FacesContext.getCurrentInstance();
		String senha = this.usuario.getSenha();
		if (usuario.getId() == 0 || usuario.getId() == null) {
			if (!senha.equals(this.confirmarSenha)) {
				FacesMessage facesMessage = new FacesMessage("A senha não foi confirmada corretamente");
				context.addMessage(null, facesMessage);
			} else {
				Integer id = usuario.getId();
				if(id==null||id==0){
					usuario.setAtivo(true);
				}
				try {
					usuarioDAO.salvar(usuario);
					firebaseUsuario(usuario);
					JsfUtil.addSuccessMessage("Usuario salvo com Sucesso");
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		} else {
			try {
				usuarioDAO.atualizar(usuario);
				JsfUtil.addSuccessMessage("Usuario salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		/*Integer id = usuario.getId();
		if(id==null||id==0){
			usuario.getPermissao().add("ROLE_USUARIO");
		}*/

		/*String senha = this.usuario.getSenha();
		if (!senha.equals(this.confirmarSenha)) {
			FacesMessage facesMessage = new FacesMessage("A senha não foi confirmada corretamente");
			context.addMessage(null, facesMessage);
			return null;
		}*/

		this.usuarioDAO.salvar(this.usuario);

		return "/usuario/usuarioLista";
	}

	public String ativar() {
		if (this.usuario.isAtivo()){
			this.usuario.setAtivo(false);
		}
		else{
			this.usuario.setAtivo(true);
		}
		this.usuarioDAO.salvar(this.usuario);
		return null;
	}

	public Usuario carregar(Integer codigo) {
		return this.usuarioDAO.carregar(codigo);
	}

	public Usuario buscarPorLogin(String login) {
		return this.usuarioDAO.buscarPorLogin(login);
	}

	public String excluir() {
		this.usuarioDAO.excluir(this.usuario);
		this.usuarios = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		usuarioDAO.excluir(usuario);
		usuariosModel = null;
    }
	

	public List getLista() {
		return this.usuarioDAO.listar();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getUsuariosModel() {
		if(usuariosModel == null){
    		usuarios = getLista();
        	usuariosModel = new XLazyModel(usuarios);
        	if(usuariosModel.getPageSize() == 0){
        		usuariosModel.setPageSize(1);
        	}
    	}
		return usuariosModel;
	}

	public void setUsuariosModel(XLazyModel usuariosModel) {
		this.usuariosModel = usuariosModel;
	}
	
	private void firebaseUsuario(Usuario usuario){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/usuario/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(Utils.retornarStringVazioQuanoNulo(usuario.getId()));
			firebaseRef.child("nome").setValue(Utils.retornarStringVazioQuanoNulo(usuario.getNome()));
			firebaseRef.child("cpf").setValue(Utils.retornarStringVazioQuanoNulo(usuario.getCpf()));
			firebaseRef.child("matricula").setValue(Utils.retornarStringVazioQuanoNulo(usuario.getMatricula()));
		}
	}
}