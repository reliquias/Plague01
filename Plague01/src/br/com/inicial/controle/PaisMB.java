package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.PaisDAO;
import br.com.inicial.modelo.Pais;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="paisMB")
@RequestScoped
public class PaisMB {

	private Pais pais = new Pais();
	private PaisDAO	paisDAO;
	private List<Object>	paiss = new ArrayList<Object>();
	private XLazyModel paissModel;
	private String	       destinoSalvar;

	public PaisMB() {
		this.paisDAO = DAOFactory.criarPaisDAO();
		paiss = lista();
		paissModel = new XLazyModel(paiss);
		if(paissModel.getPageSize() == 0){
			paissModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.pais = new Pais();
		return "pais";
	}

	public String editar() {
//		this.confirmarSenha = this.pais.getSenha();
		return "pais";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.pais.getSenha();
	}
	
	public String listar() {
		paissModel = null;
    	return "paisLista";
    }
	
	public void adicionaOuAtualiza() {
		if (pais.getId() == 0 || pais.getId() == null) {
			try {
				paisDAO.salvar(pais);
				pais = new Pais();
				JsfUtil.addSuccessMessage("Pais salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				paisDAO.atualizar(pais);
				JsfUtil.addSuccessMessage("Pais salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "paisLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.paisDAO.salvar(this.pais);

		return "/pais/paisLista";
	}

	public String ativar() {
		this.paisDAO.salvar(this.pais);
		return null;
	}

	public Pais carregar(Integer codigo) {
		return this.paisDAO.carregar(codigo);
	}

	public String excluir() {
		this.paisDAO.excluir(this.pais);
		this.paiss = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		paisDAO.excluir(pais);
		paissModel = null;
    }

	public List lista() {
		return this.paisDAO.listar();
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getPaissModel() {
		if (paissModel == null) {
			paiss = lista();
			paissModel = new XLazyModel(paiss);
			if (paissModel.getPageSize() == 0) {
				paissModel.setPageSize(1);
			}
		}
		return paissModel;
	}

	public void setPaissModel(XLazyModel paissModel) {
		this.paissModel = paissModel;
	}
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(paisDAO.listar(), true);
    }
}