package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.TipoDoencaDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.TipoDoenca;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.Firebase;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="tipoDoencaMB")
@RequestScoped
public class TipoDoencaMB {

	private TipoDoenca tipoDoenca = new TipoDoenca();
	private TipoDoencaDAO	tipoDoencaDAO;
	private List<Object>	tipoDoencas = new ArrayList<Object>();
	private XLazyModel tipoDoencasModel;
	private String	       destinoSalvar;

	public TipoDoencaMB() {
		this.tipoDoencaDAO = DAOFactory.criarTipoDoencaDAO();
		tipoDoencas = lista();
		tipoDoencasModel = new XLazyModel(tipoDoencas);
		if(tipoDoencasModel.getPageSize() == 0){
			tipoDoencasModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.tipoDoenca = new TipoDoenca();
		return "tipoDoenca";
	}

	public String editar() {
		return "tipoDoenca";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.tipoDoenca.getSenha();
	}
	
	public String listar() {
		tipoDoencasModel = null;
    	return "tipoDoencaLista";
    }
	
	public void adicionaOuAtualiza() {
		if (tipoDoenca.getId() == 0 || tipoDoenca.getId() == null) {
			try {
				tipoDoencaDAO.salvar(tipoDoenca);
				tipoDoencaFirebase(tipoDoenca);
				JsfUtil.addSuccessMessage("TipoDoenca salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				tipoDoencaDAO.atualizar(tipoDoenca);
				JsfUtil.addSuccessMessage("TipoDoenca salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
//		return "tipoDoencaLista";
	}
	
	public String salvar() {
		this.tipoDoencaDAO.salvar(this.tipoDoenca);
		return "/tipoDoenca/tipoDoencaLista";
	}

	public String ativar() {
		this.tipoDoencaDAO.salvar(this.tipoDoenca);
		return null;
	}

	public TipoDoenca carregar(Integer codigo) {
		return this.tipoDoencaDAO.carregar(codigo);
	}

	public String excluir() {
		this.tipoDoencaDAO.excluir(this.tipoDoenca);
		this.tipoDoencas = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
		tipoDoencaDAO.excluir(tipoDoenca);
		tipoDoencasModel = null;
    }

	public List lista() {
		return this.tipoDoencaDAO.listar();
	}

	public TipoDoenca getTipoDoenca() {
		return tipoDoenca;
	}

	public void setTipoDoenca(TipoDoenca tipoDoenca) {
		this.tipoDoenca = tipoDoenca;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getTipoDoencasModel() {
		if (tipoDoencasModel == null) {
			tipoDoencas = lista();
			tipoDoencasModel = new XLazyModel(tipoDoencas);
			if (tipoDoencasModel.getPageSize() == 0) {
				tipoDoencasModel.setPageSize(1);
			}
		}
		return tipoDoencasModel;
	}

	public void setTipoDoencasModel(XLazyModel tipoDoencasModel) {
		this.tipoDoencasModel = tipoDoencasModel;
	}
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(tipoDoencaDAO.listar(), true);
    }
	
	private void tipoDoencaFirebase(TipoDoenca tipoDoenca){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/tipoDoenca/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(Utils.retornarStringVazioQuanoNulo(tipoDoenca.getId()));
			firebaseRef.child("descricao").setValue(Utils.retornarStringVazioQuanoNulo(tipoDoenca.getDescricao()));
			firebaseRef.child("doenca").setValue(Utils.retornarStringVazioQuanoNulo(tipoDoenca.getDoenca()));
		}
	}
}