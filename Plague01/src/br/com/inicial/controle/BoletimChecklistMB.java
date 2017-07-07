package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.BoletimChecklistDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.BoletimChecklist;
import br.com.inicial.modelo.BoletimDiario;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.Firebase;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="boletimChecklistMB")
@RequestScoped
public class BoletimChecklistMB {

	private BoletimChecklist boletimChecklist = new BoletimChecklist();
	private BoletimChecklistDAO	boletimChecklistDAO;
	private BoletimDiario boletimDiario;
	private List<Object>	boletimChecklists = new ArrayList<Object>();
	private XLazyModel boletimChecklistsModel;
	private String	       destinoSalvar;
	FacesContext facesContext = FacesContext.getCurrentInstance();

	public BoletimChecklistMB() {
		this.boletimChecklistDAO = DAOFactory.criarBoletimChecklistDAO();
		boletimDiario = (BoletimDiario) facesContext.getExternalContext().getSessionMap().get("boletimDiario");
	}
	
	/*public String novo() {
		this.boletimChecklist = new BoletimChecklist();
		return "boletimChecklist";
	}*/
	
	public void novo(ActionEvent actionEvent) {
		this.boletimChecklist = new BoletimChecklist();
    }

	public String editar() {
		return "boletimChecklist";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.boletimChecklist.getSenha();
	}
	
	public String listar() {
		boletimChecklistsModel = null;
    	return "boletimChecklistLista";
    }
	
	public void adicionaOuAtualiza() {
		if (boletimChecklist.getId() == 0 || boletimChecklist.getId() == null) {
			try {
				boletimChecklist.setBoletimDiario(boletimDiario);
				boletimChecklistDAO.salvar(boletimChecklist);
				boletimChecklistFirebase(boletimChecklist);
				JsfUtil.addSuccessMessage("BoletimChecklist salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				boletimChecklistDAO.atualizar(boletimChecklist);
				JsfUtil.addSuccessMessage("BoletimChecklist salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		boletimChecklistsModel = null;
//		return "boletimChecklistLista";
	}
	
	public String salvar() {
		this.boletimChecklistDAO.salvar(this.boletimChecklist);
		return "/boletimChecklist/boletimChecklistLista";
	}

	public String ativar() {
		this.boletimChecklistDAO.salvar(this.boletimChecklist);
		return null;
	}

	public BoletimChecklist carregar(Integer codigo) {
		return this.boletimChecklistDAO.carregar(codigo);
	}

	public String excluir() {
		this.boletimChecklistDAO.excluir(this.boletimChecklist);
		this.boletimChecklists = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		boletimChecklistDAO.excluir(boletimChecklist);
		boletimChecklistsModel = null;
    }

	public List lista() {
		return this.boletimChecklistDAO.listar();
	}
	
	public List buscarPlantaPorBoletimDiario(Integer idBoletimDiario) {
    	return this.boletimChecklistDAO.buscarListaPorCampo("boletimDiario.id", idBoletimDiario);
    }

	public BoletimChecklist getBoletimChecklist() {
		return boletimChecklist;
	}

	public void setBoletimChecklist(BoletimChecklist boletimChecklist) {
		this.boletimChecklist = boletimChecklist;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getBoletimChecklistsModel() {
		if (boletimDiario != null) {
			boletimChecklists = buscarPlantaPorBoletimDiario(boletimDiario.getId());
		}
		if (boletimChecklistsModel == null) {
			boletimChecklistsModel = new XLazyModel(boletimChecklists);
			if (boletimChecklistsModel.getPageSize() == 0) {
				boletimChecklistsModel.setPageSize(1);
			}
		}
		return boletimChecklistsModel;
	}

	public void setBoletimChecklistsModel(XLazyModel boletimChecklistsModel) {
		this.boletimChecklistsModel = boletimChecklistsModel;
	}
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(boletimChecklistDAO.listar(), true);
    }
	
	private void boletimChecklistFirebase(BoletimChecklist boletimChecklist){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/boletimChecklist/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(Utils.retornarStringVazioQuanoNulo(boletimChecklist.getId()));
		}
	}

	public BoletimDiario getBoletimDiario() {
		return boletimDiario;
	}

	public void setBoletimDiario(BoletimDiario boletimDiario) {
		this.boletimDiario = boletimDiario;
	}

	public List<Object> getBoletimChecklists() {
		return boletimChecklists;
	}

	public void setBoletimChecklists(List<Object> boletimChecklists) {
		this.boletimChecklists = boletimChecklists;
	}
}