package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.PlantaDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Planta;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.Firebase;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="plantaMB")
@RequestScoped
public class PlantaMB {

	private Planta planta = new Planta();
	private PlantaDAO	plantaDAO;
	private List<Object>	plantas = new ArrayList<Object>();
	private XLazyModel plantasModel;
	private String	       destinoSalvar;

	public PlantaMB() {
		this.plantaDAO = DAOFactory.criarPlantaDAO();
		plantas = lista();
		plantasModel = new XLazyModel(plantas);
		if(plantasModel.getPageSize() == 0){
			plantasModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.planta = new Planta();
		return "planta";
	}

	public String editar() {
		return "planta";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.planta.getSenha();
	}
	
	public String listar() {
		plantasModel = null;
    	return "plantaLista";
    }
	
	public void adicionaOuAtualiza() {
		if (planta.getId() == 0 || planta.getId() == null) {
			try {
				plantaDAO.salvar(planta);
				plantaFirebase(planta);
				JsfUtil.addSuccessMessage("Planta salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				plantaDAO.atualizar(planta);
				JsfUtil.addSuccessMessage("Planta salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "plantaLista";
	}
	
	public String salvar() {
		this.plantaDAO.salvar(this.planta);
		return "/planta/plantaLista";
	}

	public String ativar() {
		this.plantaDAO.salvar(this.planta);
		return null;
	}

	public Planta carregar(Integer codigo) {
		return this.plantaDAO.carregar(codigo);
	}

	public String excluir() {
		this.plantaDAO.excluir(this.planta);
		this.plantas = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		plantaDAO.excluir(planta);
		plantasModel = null;
    }

	public List lista() {
		return this.plantaDAO.listar();
	}

	public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getPlantasModel() {
		if (plantasModel == null) {
			plantas = lista();
			plantasModel = new XLazyModel(plantas);
			if (plantasModel.getPageSize() == 0) {
				plantasModel.setPageSize(1);
			}
		}
		return plantasModel;
	}

	public void setPlantasModel(XLazyModel plantasModel) {
		this.plantasModel = plantasModel;
	}
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(plantaDAO.listar(), true);
    }
	
	private void plantaFirebase(Planta planta){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/planta/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(planta.getId());
			firebaseRef.child("nome").setValue(planta.getNome());
			firebaseRef.child("descricao").setValue(planta.getDescricao());
		}
	}
}