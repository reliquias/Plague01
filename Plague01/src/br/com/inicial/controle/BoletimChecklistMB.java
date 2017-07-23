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
		boletimChecklist = buscarPorCampo("boletimDiario.id", boletimDiario.getId()); 
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
		if (boletimChecklist.getId() == "0" || boletimChecklist.getId() == null) {
			try {
				boletimChecklist.setBoletimDiario(boletimDiario);
				boletimChecklistDAO.salvar(boletimChecklist);
				JsfUtil.addSuccessMessage("BoletimChecklist salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				boletimChecklistDAO.atualizar(boletimChecklist);
				JsfUtil.addSuccessMessage("BoletimChecklist salvo com Sucesso");
				boletimChecklistFirebase(boletimChecklist);
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

	public BoletimChecklist buscarPorCampo(String campo, Object valor) {
		return this.boletimChecklistDAO.buscarPorCampo(campo, valor);
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
	
	public List buscarChecklistPorBoletimDiario(Integer idBoletimDiario) {
    	return this.boletimChecklistDAO.buscarListaPorCampo("boletimDiario.id", idBoletimDiario);
    }

	public List buscarChecklistPorBoletimDiario(String idBoletimDiario) {
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
			boletimChecklists = buscarChecklistPorBoletimDiario(boletimDiario.getId());
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
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/bdos/"+boletimDiario.getId()+"/checklist/");
			Firebase chaeckListSnapshot = firebase.child(boletimChecklist.getId());
			
			chaeckListSnapshot.child("adesivosSeguranca").setValue(boletimChecklist.isAdesivosSeguranca());
			chaeckListSnapshot.child("alarmesPainelInstrumentos").setValue(boletimChecklist.isAlarmesPainelInstrumentos());
			chaeckListSnapshot.child("assentoOperacao").setValue(boletimChecklist.isAssentoOperacao());
			chaeckListSnapshot.child("chaveGeral").setValue(boletimChecklist.isChaveGeral());
			chaeckListSnapshot.child("condicoesPneus").setValue(boletimChecklist.isCondicoesPneus());
			chaeckListSnapshot.child("conjuntoCorteHARVESTER").setValue(boletimChecklist.isConjuntoCorteHARVESTER());
			chaeckListSnapshot.child("discoEDentesCorteFELLER").setValue(boletimChecklist.isDiscoEDentesCorteFELLER());
			chaeckListSnapshot.child("escadasOuEstribosAcesso").setValue(boletimChecklist.isEscadasOuEstribosAcesso());
			chaeckListSnapshot.child("faroisTrabalho").setValue(boletimChecklist.isFaroisTrabalho());
			chaeckListSnapshot.child("freioEmergencia").setValue(boletimChecklist.isFreioEmergencia());
			chaeckListSnapshot.child("freioServicos").setValue(boletimChecklist.isFreioServicos());
			chaeckListSnapshot.child("inspecaoExtintores").setValue(boletimChecklist.isInspecaoExtintores());
			chaeckListSnapshot.child("janelasLaterais").setValue(boletimChecklist.isJanelasLaterais());
			chaeckListSnapshot.child("kitContencaoVazamentos").setValue(boletimChecklist.isKitContencaoVazamentos());
			chaeckListSnapshot.child("limpezaInternaCabine").setValue(boletimChecklist.isLimpezaInternaCabine());
			chaeckListSnapshot.child("limpezaRadiador").setValue(boletimChecklist.isLimpezaRadiador());
			chaeckListSnapshot.child("lubrificacaoGeral").setValue(boletimChecklist.isLubrificacaoGeral());
			chaeckListSnapshot.child("nivelLiquidoRefrigerante").setValue(boletimChecklist.isNivelLiquidoRefrigerante());
			chaeckListSnapshot.child("nivelOleoHidraulico").setValue(boletimChecklist.isNivelOleoHidraulico());
			chaeckListSnapshot.child("nivelOleoMotor").setValue(boletimChecklist.isNivelOleoMotor());
			chaeckListSnapshot.child("parabrisaFrontal").setValue(boletimChecklist.isParabrisaFrontal());
			chaeckListSnapshot.child("paradaCombinada").setValue(boletimChecklist.isParadaCombinada());
			chaeckListSnapshot.child("paradaImediata").setValue(boletimChecklist.isParadaImediata());
			chaeckListSnapshot.child("pinosBielasFacasRolosECilindros").setValue(boletimChecklist.isPinosBielasFacasRolosECilindros());
			chaeckListSnapshot.child("presencaGeralTrincasAcessorio").setValue(boletimChecklist.isPresencaGeralTrincasAcessorio());
			chaeckListSnapshot.child("presencaGeralTrincasMaquinaBase").setValue(boletimChecklist.isPresencaGeralTrincasMaquinaBase());
			chaeckListSnapshot.child("protecaoFrontalRadiador").setValue(boletimChecklist.isProtecaoFrontalRadiador());
			chaeckListSnapshot.child("radioComunicacao").setValue(boletimChecklist.isRadioComunicacao());
			chaeckListSnapshot.child("roletesInferiores").setValue(boletimChecklist.isRoletesInferiores());
			chaeckListSnapshot.child("roletesSuperiores").setValue(boletimChecklist.isRoletesSuperiores());
			chaeckListSnapshot.child("rotator").setValue(boletimChecklist.isRotator());
			chaeckListSnapshot.child("sapatasEsteira").setValue(boletimChecklist.isSapatasEsteira());
			chaeckListSnapshot.child("sinalizacaoReEMovimento").setValue(boletimChecklist.isSinalizacaoReEMovimento());
			chaeckListSnapshot.child("tampasProtecao").setValue(boletimChecklist.isTampasProtecao());
			chaeckListSnapshot.child("tensaoEsteiras").setValue(boletimChecklist.isTensaoEsteiras());
			chaeckListSnapshot.child("travaCabineFORWARDER").setValue(boletimChecklist.isTravaCabineFORWARDER());
			chaeckListSnapshot.child("vazamentoMangueirasECilindros").setValue(boletimChecklist.isVazamentoMangueirasECilindros());
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