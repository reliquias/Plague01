package br.com.inicial.controle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.inicial.dao.BoletimDiarioDAO;
import br.com.inicial.dao.DAOFactory;
import br.com.inicial.modelo.BoletimChecklist;
import br.com.inicial.modelo.BoletimDiario;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.PlantaTalhao;
import br.com.inicial.modelo.TipoDoenca;
import br.com.inicial.modelo.Vistoria;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="boletimDiarioMB")
@RequestScoped
public class BoletimDiarioMB {

	private BoletimDiario boletimDiario = new BoletimDiario();
	private BoletimDiarioDAO	boletimDiarioDAO;
	private List<Object>	boletins = new ArrayList<Object>();
	private XLazyModel boletinsModel;
	private String	       destinoSalvar;
	private FazendaMB fazendaMB;
	private Fazenda fazenda;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	File fileBoletimDiario;
	int xCont;

	public BoletimDiarioMB() {
		this.boletimDiarioDAO = DAOFactory.criarBoletimDiarioDAO();
		facesContext = FacesContext.getCurrentInstance();
		fazendaMB =  (FazendaMB) facesContext.getExternalContext().getSessionMap().get("fazendaMB");
		fazenda = fazendaMB.getFazenda();
		ouvinteFirebase();
		boletins = buscarBoletinsPorFazenda(fazenda.getId());
		boletinsModel = new XLazyModel(boletins);
		if(boletinsModel.getPageSize() == 0){
			boletinsModel.setPageSize(1);
		}
	}
	
	public String novo() {
		this.boletimDiario = new BoletimDiario();
		return "boletimDiario";
	}

	public String editar() {
		facesContext.getExternalContext().getSessionMap().put("boletimDiario", boletimDiario);
		return "boletimDiario";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("boletimDiario");
		boletinsModel = null;
		return "boletimDiarioLista";
    }
	
	public void adicionaOuAtualiza() {
		try {
			boletimDiario.setFazenda(fazenda);
			if (boletimDiario.getId() == 0 || boletimDiario.getId() == null) {
				boletimDiarioDAO.salvar(boletimDiario);
				JsfUtil.addSuccessMessage("BoletimDiario salvo com Sucesso");
			} else {
				boletimDiarioDAO.atualizar(boletimDiario);
				JsfUtil.addSuccessMessage("BoletimDiario salvo com Sucesso");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		facesContext.getExternalContext().getSessionMap().put("boletimDiario", boletimDiario);
		// return "boletimDiarioLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.boletimDiarioDAO.salvar(this.boletimDiario);
		return "/boletimDiario/boletimDiarioLista";
	}
	
	public void saveOrUpdate(BoletimDiario boletimDiario) {
		try {
			boletimDiarioDAO.saveOrUpdate(boletimDiario);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

	}

	public String ativar() {
		this.boletimDiarioDAO.salvar(this.boletimDiario);
		return null;
	}

	public BoletimDiario carregar(Integer codigo) {
		return this.boletimDiarioDAO.carregar(codigo);
	}

	public String excluir() {
		this.boletimDiarioDAO.excluir(this.boletimDiario);
		this.boletins = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        boletimDiarioDAO.excluir(boletimDiario);
        boletinsModel = null;
    }

	public List getLista() {
		return this.boletimDiarioDAO.listar();
	}

	public List getListaPorCampo(String campo, Object valor) {
		return this.boletimDiarioDAO.buscarListaPorCampo(campo, valor);
	}

	public BoletimDiario getBoletimDiario() {
		return boletimDiario;
	}

	public void setBoletimDiario(BoletimDiario boletimDiario) {
		this.boletimDiario = boletimDiario;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getBoletinsModel() {
		if(boletinsModel == null){
			/*facesContext = FacesContext.getCurrentInstance();
			fazenda =  (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");*/
			boletins = buscarBoletinsPorFazenda(fazenda.getId());
        	boletinsModel = new XLazyModel(boletins);
        	if(boletinsModel.getPageSize() == 0){
        		boletinsModel.setPageSize(1);
        	}
    	}
		return boletinsModel;
	}

	public void setBoletinsModel(XLazyModel boletinsModel) {
		this.boletinsModel = boletinsModel;
	}
	
	public List buscarBoletinsPorFazenda(Integer idFazenda) {
    	return boletimDiarioDAO.buscarListaPorCampo("fazenda.id", idFazenda);
    }
	
	public String voltarFazenda() {
		return "/faces/restrito/public/fazenda/fazenda";
	}

	public FazendaMB getFazendaMB() {
		return fazendaMB;
	}

	public void setFazendaMB(FazendaMB fazendaMB) {
		this.fazendaMB = fazendaMB;
	}

	public Fazenda getFazenda() {
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		this.fazenda = fazenda;
	}
	
	private void ouvinteFirebase(){
		final List<BoletimDiario> listBoletins = new ArrayList<BoletimDiario>();
		final String[] executou = {null};
		final int id;
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/bdos/");
		firebase.addValueEventListener(new ValueEventListener() {

		            @Override
		            public void onDataChange(DataSnapshot ds) {
		            	
		            	for (DataSnapshot boletimSnapshot: ds.getChildren()) {
		                	BoletimDiario boletim = new BoletimDiario();
		                    	boletim.setHorimetro(boletimSnapshot.child("horimetro").getValue().toString());
//		                    	boletim.setId(Integer.parseInt(boletimSnapshot.child("horimetro").getValue().toString()));
		                    	//Ver com Rogerio
		                    	//boletim.setIdFirebase(boletimSnapshot.child("id").getValue().toString());
		                    	boletim.setMaquinaBase(boletimSnapshot.child("maquinaBaseHV_FB_FW").getValue().toString());
		                    	boletim.setMatriculaResponsavel(boletimSnapshot.child("matriculaResponsavel").getValue().toString());
		                    	boletim.setNomeResponsavel(boletimSnapshot.child("nomeResponsavel").getValue().toString());
		                    	boletim.setOdometro(boletimSnapshot.child("odometro").getValue().toString());
		                    	boletim.setProjeto(boletimSnapshot.child("projeto").getValue().toString());
		                    	boletim.setTurno(boletimSnapshot.child("turno").getValue().toString());
		                    	boletim.setIdFazenda(Integer.parseInt(boletimSnapshot.child("fazendaId").getValue().toString()));
		                    	/*for (DataSnapshot boletimSnapshot2 : boletimSnapshot.child("boletins").getChildren()) {
									System.out.println(boletimSnapshot2.child("nomeAtividade").getValue().toString());
								}*/
								for (DataSnapshot boletimSnapshot2 : boletimSnapshot.child("checklist").getChildren()) {
									BoletimChecklist checkList = new BoletimChecklist();
//									checkList.setBoletimDiario(boletim);
									checkList.setAdesivosSeguranca((boolean) boletimSnapshot2.child("adesivosSeguranca").getValue());
									checkList.setAlarmesPainelInstrumentos((boolean)boletimSnapshot2.child("alarmesPainelInstrumentos").getValue());
									checkList.setAssentoOperacao((boolean)boletimSnapshot2.child("assentoOperacao").getValue());
									checkList.setChaveGeral((boolean)boletimSnapshot2.child("chaveGeral").getValue());
									checkList.setCondicoesPneus((boolean)boletimSnapshot2.child("condicoesPneus").getValue());
									checkList.setConjuntoCorteHARVESTER((boolean)boletimSnapshot2.child("conjuntoCorteHARVESTER").getValue());
									checkList.setDiscoEDentesCorteFELLER((boolean)boletimSnapshot2.child("discoEDentesCorteFELLER").getValue());
									checkList.setEscadasOuEstribosAcesso((boolean)boletimSnapshot2.child("escadasOuEstribosAcesso").getValue());
									checkList.setFaroisTrabalho((boolean)boletimSnapshot2.child("faroisTrabalho").getValue());
									checkList.setFreioEmergencia((boolean)boletimSnapshot2.child("freioEmergencia").getValue());
									checkList.setFreioServicos((boolean)boletimSnapshot2.child("freioServicos").getValue());
									checkList.setInspecaoExtintores((boolean)boletimSnapshot2.child("inspecaoExtintores").getValue());
									checkList.setJanelasLaterais((boolean)boletimSnapshot2.child("janelasLaterais").getValue());
									checkList.setKitContencaoVazamentos((boolean)boletimSnapshot2.child("kitContencaoVazamentos").getValue());
									checkList.setLimpezaEsteiras((boolean)boletimSnapshot2.child("limpezaEsteiras").getValue());
									checkList.setLimpezaInternaCabine((boolean)boletimSnapshot2.child("limpezaInternaCabine").getValue());
									checkList.setLimpezaRadiador((boolean)boletimSnapshot2.child("limpezaRadiador").getValue());
									checkList.setLubrificacaoGeral((boolean)boletimSnapshot2.child("lubrificacaoGeral").getValue());
									checkList.setNivelLiquidoRefrigerante((boolean)boletimSnapshot2.child("nivelLiquidoRefrigerante").getValue());
									checkList.setNivelOleoHidraulico((boolean)boletimSnapshot2.child("nivelOleoHidraulico").getValue());
									checkList.setNivelOleoMotor((boolean)boletimSnapshot2.child("nivelOleoMotor").getValue());
									checkList.setParabrisaFrontal((boolean)boletimSnapshot2.child("parabrisaFrontal").getValue());
									checkList.setParadaCombinada((boolean)boletimSnapshot2.child("paradaCombinada").getValue());
									checkList.setParadaImediata((boolean)boletimSnapshot2.child("paradaImediata").getValue());
									checkList.setPinosBielasFacasRolosECilindros((boolean)boletimSnapshot2.child("pinosBielasFacasRolosECilindros").getValue());
									checkList.setPresencaGeralTrincasAcessorio((boolean)boletimSnapshot2.child("presencaGeralTrincasAcessorio").getValue());
									checkList.setPresencaGeralTrincasMaquinaBase((boolean)boletimSnapshot2.child("presencaGeralTrincasMaquinaBase").getValue());
									checkList.setProtecaoFrontalRadiador((boolean)boletimSnapshot2.child("protecaoFrontalRadiador").getValue());
									checkList.setRadioComunicacao((boolean)boletimSnapshot2.child("radioComunicacao").getValue());
									checkList.setRoletesInferiores((boolean)boletimSnapshot2.child("roletesInferiores").getValue());
									checkList.setRoletesSuperiores((boolean)boletimSnapshot2.child("roletesSuperiores").getValue());
									checkList.setRotator((boolean)boletimSnapshot2.child("rotator").getValue());
									checkList.setSapatasEsteira((boolean)boletimSnapshot2.child("sapatasEsteira").getValue());
									checkList.setSinalizacaoReEMovimento((boolean)boletimSnapshot2.child("sinalizacaoReEMovimento").getValue());
									checkList.setTampasProtecao((boolean)boletimSnapshot2.child("tampasProtecao").getValue());
									checkList.setTensaoEsteiras((boolean)boletimSnapshot2.child("tensaoEsteiras").getValue());
									checkList.setTravaCabineFORWARDER((boolean)boletimSnapshot2.child("travaCabineFORWARDER").getValue());
									checkList.setVazamentoMangueirasECilindros((boolean)boletimSnapshot2.child("vazamentoMangueirasECilindros").getValue());
									boletim.setBoletimCheckList(checkList);
								}
		                    	listBoletins.add(boletim);
		                    	System.out.println("Entrou");
		                }
		            	executou[0] = "true";
		            }

		            @Override
		            public void onCancelled(FirebaseError fe) {
		                
		            }
		        });
		
		while(executou[0] == null){
			System.out.println("Dentro do while");
		}
		
		for (BoletimDiario boletimDiario : listBoletins) {
			Fazenda fazendaBoletim = DAOFactory.criarFazendaDAO().carregar(boletimDiario.getIdFazenda());
			boletimDiario.setFazenda(fazendaBoletim);
			saveOrUpdate(boletimDiario);
		}
		if(fazenda!=null){
			this.boletins = buscarBoletinsPorFazenda(fazenda.getId());
		}
		System.out.println("Eai");
	}
}