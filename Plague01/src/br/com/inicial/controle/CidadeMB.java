package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.CidadeDAO;
import br.com.inicial.dao.DAOFactory;
import br.com.inicial.modelo.Cidade;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="cidadeMB")
@RequestScoped
public class CidadeMB {

	private Cidade cidade = new Cidade();
	private CidadeDAO	cidadeDAO;
	private List<Object>	cidades = new ArrayList<Object>();
	private XLazyModel cidadesModel;
	private String	       destinoSalvar;

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
		return "cidade";
	}

	public String editar() {
//		this.confirmarSenha = this.cidade.getSenha();
		return "cidade";
	}

	public void preparaEditar(ActionEvent actionEvent) {
//		this.confirmarSenha = this.cidade.getSenha();
	}
	
	public String listar() {
		cidadesModel = null;
    	return "cidadeLista";
    }
	
	public void adicionaOuAtualiza() {
		if (cidade.getId() == 0 || cidade.getId() == null) {
			try {
				cidadeDAO.salvar(cidade);
				cidade = new Cidade();
				JsfUtil.addSuccessMessage("Cidade salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				cidadeDAO.atualizar(cidade);
				JsfUtil.addSuccessMessage("Cidade salvo com Sucesso");
			} catch (Exception e) {
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
}