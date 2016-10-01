package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.PlantaTalhaoDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.PlantaTalhao;
import br.com.inicial.modelo.Talhao;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.Firebase;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="plantaTalhaoMB")
@RequestScoped
public class PlantaTalhaoMB {

	private PlantaTalhao plantaTalhao = new PlantaTalhao();
	private PlantaTalhaoDAO	plantaTalhaoDAO;
	private Talhao talhao;
	private List<Object>	plantaTalhaos = new ArrayList<Object>();
	private XLazyModel plantaTalhaosModel;
	private String	       destinoSalvar;
	FacesContext facesContext = FacesContext.getCurrentInstance();

	public PlantaTalhaoMB() {
		this.plantaTalhaoDAO = DAOFactory.criarPlantaTalhaoDAO();
		talhao = (Talhao) facesContext.getExternalContext().getSessionMap().get("talhao");
		plantaTalhaos = buscarPlantaPorTalhao(talhao.getId());
		plantaTalhaosModel = new XLazyModel(plantaTalhaos);
		if(plantaTalhaosModel.getPageSize() == 0){
			plantaTalhaosModel.setPageSize(1);
		}
	}
	
	/*public String novo() {
		this.plantaTalhao = new PlantaTalhao();
		return "plantaTalhao";
	}*/
	
	public void novo(ActionEvent actionEvent) {
		this.plantaTalhao = new PlantaTalhao();
    }

	public String editar() {
		return "plantaTalhao";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.plantaTalhao.getSenha();
	}
	
	public String listar() {
		plantaTalhaosModel = null;
    	return "plantaTalhaoLista";
    }
	
	public void adicionaOuAtualiza() {
		if (plantaTalhao.getId() == 0 || plantaTalhao.getId() == null) {
			try {
				plantaTalhao.setTalhao(talhao);
				plantaTalhaoDAO.salvar(plantaTalhao);
				plantaTalhaoFirebase(plantaTalhao);
				JsfUtil.addSuccessMessage("PlantaTalhao salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				plantaTalhaoDAO.atualizar(plantaTalhao);
				JsfUtil.addSuccessMessage("PlantaTalhao salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		plantaTalhaosModel = null;
//		return "plantaTalhaoLista";
	}
	
	public String salvar() {
		this.plantaTalhaoDAO.salvar(this.plantaTalhao);
		return "/plantaTalhao/plantaTalhaoLista";
	}

	public String ativar() {
		this.plantaTalhaoDAO.salvar(this.plantaTalhao);
		return null;
	}

	public PlantaTalhao carregar(Integer codigo) {
		return this.plantaTalhaoDAO.carregar(codigo);
	}

	public String excluir() {
		this.plantaTalhaoDAO.excluir(this.plantaTalhao);
		this.plantaTalhaos = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		plantaTalhaoDAO.excluir(plantaTalhao);
		plantaTalhaosModel = null;
    }

	public List lista() {
		return this.plantaTalhaoDAO.listar();
	}
	
	public List buscarPlantaPorTalhao(Integer idTalhao) {
    	return this.plantaTalhaoDAO.buscarListaPorCampo("talhao.id", idTalhao);
    }

	public PlantaTalhao getPlantaTalhao() {
		return plantaTalhao;
	}

	public void setPlantaTalhao(PlantaTalhao plantaTalhao) {
		this.plantaTalhao = plantaTalhao;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getPlantaTalhaosModel() {
		if (plantaTalhaosModel == null) {
			plantaTalhaos = buscarPlantaPorTalhao(talhao.getId());
			plantaTalhaosModel = new XLazyModel(plantaTalhaos);
			if (plantaTalhaosModel.getPageSize() == 0) {
				plantaTalhaosModel.setPageSize(1);
			}
		}
		return plantaTalhaosModel;
	}

	public void setPlantaTalhaosModel(XLazyModel plantaTalhaosModel) {
		this.plantaTalhaosModel = plantaTalhaosModel;
	}
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(plantaTalhaoDAO.listar(), true);
    }
	
	private void plantaTalhaoFirebase(PlantaTalhao plantaTalhao){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/plantaTalhao/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(plantaTalhao.getId());
			firebaseRef.child("idPlanta").setValue(plantaTalhao.getPlanta().getId());
			firebaseRef.child("plantaNome").setValue(plantaTalhao.getPlanta().getNome());
			firebaseRef.child("plantaDescricao").setValue(plantaTalhao.getPlanta().getDescricao());
			firebaseRef.child("idTalhao").setValue(plantaTalhao.getTalhao().getId());
			firebaseRef.child("talhaoDescricao").setValue(plantaTalhao.getTalhao().getNome());
			firebaseRef.child("coordenadas").setValue(plantaTalhao.getCoordenadas());
			
		}
	}

	public Talhao getTalhao() {
		return talhao;
	}

	public void setTalhao(Talhao talhao) {
		this.talhao = talhao;
	}

	public List<Object> getPlantaTalhaos() {
		return plantaTalhaos;
	}

	public void setPlantaTalhaos(List<Object> plantaTalhaos) {
		this.plantaTalhaos = plantaTalhaos;
	}
}