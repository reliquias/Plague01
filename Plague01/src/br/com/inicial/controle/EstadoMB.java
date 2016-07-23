package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.EstadoDAO;
import br.com.inicial.modelo.Estado;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="estadoMB")
@RequestScoped
public class EstadoMB {

	private Estado estado = new Estado();
	private EstadoDAO	estadoDAO;
	private List<Object>	estados = new ArrayList<Object>();
	private XLazyModel estadosModel;
	private String	       destinoSalvar;

	public EstadoMB() {
		this.estadoDAO = DAOFactory.criarEstadoDAO();
		estados = lista();
		estadosModel = new XLazyModel(estados);
		if(estadosModel.getPageSize() == 0){
			estadosModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.estado = new Estado();
		return "estado";
	}

	public String editar() {
//		this.confirmarSenha = this.estado.getSenha();
		return "estado";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.estado.getSenha();
	}
	
	public String listar() {
		estadosModel = null;
    	return "estadoLista";
    }
	
	public void adicionaOuAtualiza() {
		if (estado.getId() == 0 || estado.getId() == null) {
			try {
				estadoDAO.salvar(estado);
				estado = new Estado();
				JsfUtil.addSuccessMessage("Estado salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				estadoDAO.atualizar(estado);
				JsfUtil.addSuccessMessage("Estado salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "estadoLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.estadoDAO.salvar(this.estado);

		return "/estado/estadoLista";
	}

	public String ativar() {
		this.estadoDAO.salvar(this.estado);
		return null;
	}

	public Estado carregar(Integer codigo) {
		return this.estadoDAO.carregar(codigo);
	}

	public void excluirAjax(ActionEvent actionEvent) {
		estadoDAO.excluir(estado);
		estadosModel = null;
    }

	public List lista() {
		return this.estadoDAO.listar();
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getEstadosModel() {
		if (estadosModel == null) {
			estados = lista();
			estadosModel = new XLazyModel(estados);
			if (estadosModel.getPageSize() == 0) {
				estadosModel.setPageSize(1);
			}
		}
		return estadosModel;
	}

	public void setEstadosModel(XLazyModel estadosModel) {
		this.estadosModel = estadosModel;
	}
	
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(estadoDAO.listar(), true);
    }
}