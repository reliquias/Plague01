package br.com.jaque.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.jaque.dao.DAOFactory;
import br.com.jaque.dao.UsuarioDAO;
import br.com.jaque.modelo.Usuario;
import br.com.jaque.util.JsfUtil;
import br.com.jaque.util.XLazyModel;

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
//		this.confirmarSenha = this.usuario.getSenha();
		return "usuario";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.usuario.getSenha();
	}
	
	public String listar() {
    	return "usuarioLista";
    }
	
	public void adicionaOuAtualiza() {
		if (usuario.getId() == 0 || usuario.getId() == null) {
			try {
				usuarioDAO.salvar(usuario);
				usuario = new Usuario();
				JsfUtil.addSuccessMessage("Usuario salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				usuarioDAO.atualizar(usuario);
				JsfUtil.addSuccessMessage("Usuario salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "usuarioLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		Integer id = usuario.getId();
		if(id==null||id==0){
			usuario.getPermissao().add("ROLE_USUARIO");
		}

		/*String senha = this.usuario.getSenha();
		if (!senha.equals(this.confirmarSenha)) {
			FacesMessage facesMessage = new FacesMessage("A senha n�o foi confirmada corretamente");
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

	public List getLista() {
		if (this.usuarios == null || this.usuarios.isEmpty()) {
			return this.usuarioDAO.listar();
		}
		return this.usuarios;
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
		return usuariosModel;
	}

	public void setUsuariosModel(XLazyModel usuariosModel) {
		this.usuariosModel = usuariosModel;
	}
}