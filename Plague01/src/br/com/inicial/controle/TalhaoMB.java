package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.TalhaoDAO;
import br.com.inicial.modelo.Zona;
import br.com.inicial.modelo.Talhao;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="talhaoMB")
@RequestScoped
public class TalhaoMB {

	private Talhao talhao = new Talhao();
	private TalhaoDAO	talhaoDAO;
	private List<Object>	talhoes = new ArrayList<Object>();
	private XLazyModel talhoesModel;
	private String	       destinoSalvar;
	private Zona zona;
	FacesContext facesContext = FacesContext.getCurrentInstance();

	public TalhaoMB() {
		this.talhaoDAO = DAOFactory.criarTalhaoDAO();
		facesContext = FacesContext.getCurrentInstance();
		zona =  (Zona) facesContext.getExternalContext().getSessionMap().get("zona");
		talhoes = buscarTalhoesPorZona(zona.getId());
		talhoesModel = new XLazyModel(talhoes);
		if(talhoesModel.getPageSize() == 0){
			talhoesModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.talhao = new Talhao();
		return "talhao";
	}

	public String editar() {
		return "talhao";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		talhoesModel = null;
    	return "talhaoLista";
    }
	
	public void adicionaOuAtualiza() {
		zona =  (Zona) facesContext.getExternalContext().getSessionMap().get("zona");
		talhao.setZona(zona);
		if (talhao.getId() == 0 || talhao.getId() == null) {
			try {
				talhaoDAO.salvar(talhao);
				talhao = new Talhao();
				JsfUtil.addSuccessMessage("Talhao salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				talhaoDAO.atualizar(talhao);
				JsfUtil.addSuccessMessage("Talhao salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "talhaoLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.talhaoDAO.salvar(this.talhao);
		return "/talhao/talhaoLista";
	}

	public String ativar() {
		this.talhaoDAO.salvar(this.talhao);
		return null;
	}

	public Talhao carregar(Integer codigo) {
		return this.talhaoDAO.carregar(codigo);
	}

	public String excluir() {
		this.talhaoDAO.excluir(this.talhao);
		this.talhoes = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        talhaoDAO.excluir(talhao);
        talhoesModel = null;
    }

	public List getLista() {
		return this.talhaoDAO.listar();
	}

	public Talhao getTalhao() {
		return talhao;
	}

	public void setTalhao(Talhao talhao) {
		this.talhao = talhao;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getTalhoesModel() {
		if(talhoesModel == null){
			facesContext = FacesContext.getCurrentInstance();
			zona =  (Zona) facesContext.getExternalContext().getSessionMap().get("zona");
			talhoes = buscarTalhoesPorZona(zona.getId());
        	talhoesModel = new XLazyModel(talhoes);
        	if(talhoesModel.getPageSize() == 0){
        		talhoesModel.setPageSize(1);
        	}
    	}
		return talhoesModel;
	}

	public void setTalhoesModel(XLazyModel talhoesModel) {
		this.talhoesModel = talhoesModel;
	}
	
	public List buscarTalhoesPorZona(Integer idZona) {
    	return talhaoDAO.buscarListaPorCampo("zona.id", idZona);
    }
	
	public String voltarZona() {
		return "/faces/public/zona/zona";
	}
}