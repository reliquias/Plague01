package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.CidadeDAO;
import br.com.inicial.dao.DAOFactory;
import br.com.inicial.modelo.Cidade;
import br.com.inicial.modelo.Estado;
import br.com.inicial.modelo.Pais;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="cidadeMB")
@SessionScoped
public class CidadeMB {

	private Cidade cidade = new Cidade();
	private CidadeDAO	cidadeDAO;
	private List<Object>	cidades = new ArrayList<Object>();
	private XLazyModel cidadesModel;
	private String	       destinoSalvar;
	private List<Estado> estados = new ArrayList<Estado>();

	public CidadeMB() {
		this.cidadeDAO = DAOFactory.criarCidadeDAO();
		cidades = lista();
		cidadesModel = new XLazyModel(cidades);
		if(cidadesModel.getPageSize() == 0){
			cidadesModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.cidade = new Cidade();
		this.estados = new ArrayList<Estado>();
		return "cidade";
	}

	public String editar() {
//		this.confirmarSenha = this.cidade.getSenha();
		this.estados = new ArrayList<Estado>();
		return "cidade";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.cidade.getSenha();
	}
	
	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadeMB");
		cidadesModel = null;
    	return "cidadeLista";
    }
	
	public void adicionaOuAtualiza() {
		if (cidade.getId() == 0 || cidade.getId() == null) {
			try {
				cidadeDAO.salvar(cidade);
				JsfUtil.addSuccessMessage("Cidade salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				cidadeDAO.atualizar(cidade);
				JsfUtil.addSuccessMessage("Cidade salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
//		return "cidadeLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.cidadeDAO.salvar(this.cidade);

		return "/cidade/cidadeLista";
	}

	public String ativar() {
		this.cidadeDAO.salvar(this.cidade);
		return null;
	}

	public Cidade carregar(Integer codigo) {
		return this.cidadeDAO.carregar(codigo);
	}

	public void excluirAjax(ActionEvent actionEvent) {
		cidadeDAO.excluir(cidade);
		cidadesModel = null;
    }

	public List lista() {
		return this.cidadeDAO.listar();
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}

	public XLazyModel getCidadesModel() {
		if (cidadesModel == null) {
			cidades = lista();
			cidadesModel = new XLazyModel(cidades);
			if (cidadesModel.getPageSize() == 0) {
				cidadesModel.setPageSize(1);
			}
		}
		return cidadesModel;
	}

	public void setCidadesModel(XLazyModel cidadesModel) {
		this.cidadesModel = cidadesModel;
	}
	
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(cidadeDAO.listar(), true);
    }
	
	/**trecho que faz os trabalhos javascript*/
	public void cmbEstadoChange(AjaxBehaviorEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		String idPais = event.getComponent().getAttributes().get("value").toString();
		Pais pais = DAOFactory.criarPaisDAO().buscarPorCampo("sigla", idPais);
		this.estados = DAOFactory.criarEstadoDAO().buscarListaPorCampo("pais.id", pais.getId());
	}
	
	public SelectItem[] getItemsAvailableSelectOneEstado() {
		if(this.estados.size() == 0 && cidade!=null && cidade.getPais()!=null){
			this.estados = DAOFactory.criarEstadoDAO().buscarListaPorCampo("pais.id", cidade.getPais().getId());
		}
        return JsfUtil.getSelectItems(this.estados, true);
    }

	
	
}