package br.com.inicial.controle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.inicial.dao.BoletimApontamentoDAO;
import br.com.inicial.dao.DAOFactory;
import br.com.inicial.modelo.BoletimApontamento;
import br.com.inicial.modelo.BoletimDiario;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="boletimApontamentoMB")
@RequestScoped
public class BoletimApontamentoMB {

	private BoletimApontamento boletimApontamento = new BoletimApontamento();
	private BoletimApontamentoDAO	boletimApontamentoDAO;
	private List<Object>	apontamentos = new ArrayList<Object>();
	private XLazyModel apontamentosModel;
	private String	       destinoSalvar;
	private BoletimDiarioMB boletimDiarioMB;
	private BoletimDiario boletimDiario;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	File fileBoletimApontamento;

	public BoletimApontamentoMB() {
		this.boletimApontamentoDAO = DAOFactory.criarBoletimApontamentoDAO();
		facesContext = FacesContext.getCurrentInstance();
		boletimDiario =  (BoletimDiario) facesContext.getExternalContext().getSessionMap().get("boletimDiario");
		apontamentos = buscarApontamentosPorBoletimDiario(boletimDiario.getId());
		apontamentosModel = new XLazyModel(apontamentos);
		if(apontamentosModel.getPageSize() == 0){
			apontamentosModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.boletimApontamento = new BoletimApontamento();
		return "boletimApontamento";
	}

	public String editar() {
		facesContext.getExternalContext().getSessionMap().put("boletimApontamento", boletimApontamento);
		return "boletimApontamento";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("boletimApontamento");
		apontamentosModel = null;
		return "boletimApontamentoLista";
    }
	
	public void adicionaOuAtualiza() {
		try {
			boletimApontamento.setBoletimDiario(boletimDiario);
			if (boletimApontamento.getId() == "0" || boletimApontamento.getId() == null) {
				boletimApontamentoDAO.salvar(boletimApontamento);
				JsfUtil.addSuccessMessage("BoletimApontamento salvo com Sucesso");
			} else {
				boletimApontamentoDAO.atualizar(boletimApontamento);
				JsfUtil.addSuccessMessage("BoletimApontamento salvo com Sucesso");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		facesContext.getExternalContext().getSessionMap().put("boletimApontamento", boletimApontamento);
		// return "boletimApontamentoLista";
	}
	
	public String salvar() {
		this.boletimApontamentoDAO.salvar(this.boletimApontamento);
		return "/boletimApontamento/boletimApontamentoLista";
	}

	public String ativar() {
		this.boletimApontamentoDAO.salvar(this.boletimApontamento);
		return null;
	}

	public BoletimApontamento carregar(Integer codigo) {
		return this.boletimApontamentoDAO.carregar(codigo);
	}

	public String excluir() {
		this.boletimApontamentoDAO.excluir(this.boletimApontamento);
		this.apontamentos = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        boletimApontamentoDAO.excluir(boletimApontamento);
        apontamentosModel = null;
    }

	public List getLista() {
		return this.boletimApontamentoDAO.listar();
	}

	public BoletimApontamento getBoletimApontamento() {
		return boletimApontamento;
	}

	public void setBoletimApontamento(BoletimApontamento boletimApontamento) {
		this.boletimApontamento = boletimApontamento;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getApontamentosModel() {
		if(apontamentosModel == null){
			apontamentos = buscarApontamentosPorBoletimDiario(boletimDiario.getId());
        	apontamentosModel = new XLazyModel(apontamentos);
        	if(apontamentosModel.getPageSize() == 0){
        		apontamentosModel.setPageSize(1);
        	}
    	}
		return apontamentosModel;
	}

	public void setApontamentosModel(XLazyModel apontamentosModel) {
		this.apontamentosModel = apontamentosModel;
	}
	
	public List buscarApontamentosPorBoletimDiario(Integer idBoletimDiario) {
    	return boletimApontamentoDAO.buscarListaPorCampo("boletimDiario.id", idBoletimDiario);
    }

	public List buscarApontamentosPorBoletimDiario(String idBoletimDiario) {
		return boletimApontamentoDAO.buscarListaPorCampo("boletimDiario.id", idBoletimDiario);
	}
	
	public String voltarBoletimDiario() {
		return "/faces/restrito/public/boletimDiario/boletimDiario";
	}

	public BoletimDiarioMB getBoletimDiarioMB() {
		return boletimDiarioMB;
	}

	public void setBoletimDiarioMB(BoletimDiarioMB boletimDiarioMB) {
		this.boletimDiarioMB = boletimDiarioMB;
	}

	public BoletimDiario getBoletimDiario() {
		return boletimDiario;
	}

	public void setBoletimDiario(BoletimDiario boletimDiario) {
		this.boletimDiario = boletimDiario;
	}
}