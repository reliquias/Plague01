package br.com.inicial.controle;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.inicial.dao.BoletimDiarioDAO;
import br.com.inicial.dao.DAOFactory;
import br.com.inicial.modelo.BoletimApontamento;
import br.com.inicial.modelo.BoletimChecklist;
import br.com.inicial.modelo.BoletimDiario;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Fazenda;
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
	private BoletimChecklist boletimCheckList = new BoletimChecklist();
	private String checklistId;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	File fileBoletimDiario;
	int xCont;

	public BoletimDiarioMB() {
		this.boletimDiarioDAO = DAOFactory.criarBoletimDiarioDAO();
		facesContext = FacesContext.getCurrentInstance();
		fazendaMB =  (FazendaMB) facesContext.getExternalContext().getSessionMap().get("fazendaMB");
		fazenda = fazendaMB.getFazenda();
		if( facesContext.getExternalContext().getSessionMap().get("ouvinteFirebase") == null || facesContext.getExternalContext().getSessionMap().get("ouvinteFirebase").equals("")){
			ouvinteFirebase();	
		}
		facesContext.getExternalContext().getSessionMap().put("ouvinteFirebase", "true");
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
		facesContext.getExternalContext().getSessionMap().put("boletimCheckList", boletimDiario.getBoletimCheckList());
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
			boletimCheckList =  (BoletimChecklist) facesContext.getExternalContext().getSessionMap().get("boletimCheckList");
			boletimDiario.setBoletimCheckList(boletimCheckList);
			boletimDiario.setFazenda(fazenda);
			if (boletimDiario.getId() == "0" || boletimDiario.getId() == null) {
				boletimDiarioDAO.salvar(boletimDiario);
				JsfUtil.addSuccessMessage("BoletimDiario salvo com Sucesso");
			} else {
				boletimDiarioDAO.atualizar(boletimDiario);
				JsfUtil.addSuccessMessage("BoletimDiario salvo com Sucesso");
				boletimDiarioFirebase(boletimDiario);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		facesContext.getExternalContext().getSessionMap().put("boletimDiario", boletimDiario);
		// return "boletimDiarioLista";
	}
	
	private void boletimDiarioFirebase(BoletimDiario boletimDiario){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/bdos/");
			Firebase firebaseRef = firebase.child(boletimDiario.getId());
			
			firebaseRef.child("horimetro").setValue(boletimDiario.getHorimetro());
			firebaseRef.child("horimetro").setValue(boletimDiario.getHorimetro());
			firebaseRef.child("maquinaBaseHV_FB_FW").setValue(boletimDiario.getMaquinaBase());
			firebaseRef.child("matriculaResponsavel").setValue(boletimDiario.getMatriculaResponsavel());
			firebaseRef.child("dataHoraInicio").setValue(boletimDiario.getDataHoraInicio());
			firebaseRef.child("nomeResponsavel").setValue(boletimDiario.getNomeResponsavel());
			firebaseRef.child("odometro").setValue(boletimDiario.getOdometro());
			firebaseRef.child("projeto").setValue(boletimDiario.getProjeto());
			firebaseRef.child("turno").setValue(boletimDiario.getTurno());
		}
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
		int id = 0;
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/bdos/");
		firebase.addValueEventListener(new ValueEventListener() {

		            @Override
		            public void onDataChange(DataSnapshot ds) {
		            	
		            	for (DataSnapshot boletimSnapshot: ds.getChildren()) {
		            		try {
								if(boletimSnapshot.getKey()!=null){
								String dataHora = boletimSnapshot.child("dataHoraInicio").getValue().toString();
								String nomeResponsavel = boletimSnapshot.child("nomeResponsavel").getValue().toString();
								BoletimDiario boletim = new BoletimDiario();
									boletim.setHorimetro(boletimSnapshot.child("horimetro").getValue().toString());
									boletim.setId(boletimSnapshot.getKey());
									System.out.println(boletimSnapshot.getKey());
									//Ver com Rogerio
									boletim.setIdFirebase(boletimSnapshot.getKey());
									boletim.setMaquinaBase(boletimSnapshot.child("maquinaBaseHV_FB_FW").getValue().toString());
									boletim.setMatriculaResponsavel(boletimSnapshot.child("matriculaResponsavel").getValue().toString());
									boletim.setNomeResponsavel(nomeResponsavel);
									boletim.setOdometro(boletimSnapshot.child("odometro").getValue().toString());
									boletim.setProjeto(boletimSnapshot.child("projeto").getValue().toString());
									boletim.setTurno(boletimSnapshot.child("turno").getValue().toString());
									boletim.setDataHoraInicio(dataHora);
									boletim.setIdFazenda(Integer.parseInt(boletimSnapshot.child("fazendaId").getValue().toString()));
									
									List<BoletimApontamento> apontamentoList = new ArrayList<BoletimApontamento>();
									for (DataSnapshot apontamentoSnapshot : boletimSnapshot.child("boletins").getChildren()) {
										//if(apontamentoSnapshot.child("dataProcessamento").getValue() == null){
										long IdFirebase = (long)apontamentoSnapshot.child("id").getValue();
										BoletimApontamento apontamento = new BoletimApontamento();
										apontamento.setId(apontamentoSnapshot.getKey());
										apontamento.setBoletimDiario(boletim);
										apontamento.setCheckListId((long) apontamentoSnapshot.child("checkListId").getValue());
										apontamento.setCodigoAtividade((long)apontamentoSnapshot.child("codigoAtividade").getValue());
										apontamento.setIdFirebase(IdFirebase);
										apontamento.setMecanicoId((long)apontamentoSnapshot.child("mecanicoId").getValue());
										apontamento.setCto(apontamentoSnapshot.child("cto").getValue().toString());
										apontamento.setDataHoraCadastro(apontamentoSnapshot.child("dataHoraCadastro").getValue().toString());
										apontamento.setHorimetro(apontamentoSnapshot.child("horimetro").getValue().toString());
										apontamento.setNomeAtividade(apontamentoSnapshot.child("nomeAtividade").getValue().toString());
										apontamento.setNumeroLaudo((long)apontamentoSnapshot.child("numeroLaudo").getValue());
										apontamento.setOdometro(apontamentoSnapshot.child("odometro").getValue().toString());
										apontamento.setProducao(apontamentoSnapshot.child("producao").getValue().toString());
										apontamento.setResponsavel(apontamentoSnapshot.child("responsavel").getValue().toString());
										apontamento.setTalhao((long)apontamentoSnapshot.child("talhao").getValue());
										apontamentoList.add(apontamento);
										//}
									}
									if(apontamentoList.size()>0){
										boletim.setApontamentoList(apontamentoList);
									}
									for (DataSnapshot chaeckListSnapshot : boletimSnapshot.child("checklist").getChildren()) {
										long IdFirebase = (long)chaeckListSnapshot.child("id").getValue();
										BoletimChecklist checkList = new BoletimChecklist();
										
										checkList.setId(chaeckListSnapshot.getKey());
										checkList.setAdesivosSeguranca((boolean) chaeckListSnapshot.child("adesivosSeguranca").getValue());
										checkList.setAlarmesPainelInstrumentos((boolean)chaeckListSnapshot.child("alarmesPainelInstrumentos").getValue());
										checkList.setAssentoOperacao((boolean)chaeckListSnapshot.child("assentoOperacao").getValue());
										checkList.setChaveGeral((boolean)chaeckListSnapshot.child("chaveGeral").getValue());
										checkList.setCondicoesPneus((boolean)chaeckListSnapshot.child("condicoesPneus").getValue());
										checkList.setConjuntoCorteHARVESTER((boolean)chaeckListSnapshot.child("conjuntoCorteHARVESTER").getValue());
										checkList.setDiscoEDentesCorteFELLER((boolean)chaeckListSnapshot.child("discoEDentesCorteFELLER").getValue());
										checkList.setEscadasOuEstribosAcesso((boolean)chaeckListSnapshot.child("escadasOuEstribosAcesso").getValue());
										checkList.setFaroisTrabalho((boolean)chaeckListSnapshot.child("faroisTrabalho").getValue());
										checkList.setFreioEmergencia((boolean)chaeckListSnapshot.child("freioEmergencia").getValue());
										checkList.setFreioServicos((boolean)chaeckListSnapshot.child("freioServicos").getValue());
										checkList.setInspecaoExtintores((boolean)chaeckListSnapshot.child("inspecaoExtintores").getValue());
										checkList.setJanelasLaterais((boolean)chaeckListSnapshot.child("janelasLaterais").getValue());
										checkList.setKitContencaoVazamentos((boolean)chaeckListSnapshot.child("kitContencaoVazamentos").getValue());
										checkList.setLimpezaEsteiras((boolean)chaeckListSnapshot.child("limpezaEsteiras").getValue());
										checkList.setLimpezaInternaCabine((boolean)chaeckListSnapshot.child("limpezaInternaCabine").getValue());
										checkList.setLimpezaRadiador((boolean)chaeckListSnapshot.child("limpezaRadiador").getValue());
										checkList.setLubrificacaoGeral((boolean)chaeckListSnapshot.child("lubrificacaoGeral").getValue());
										checkList.setNivelLiquidoRefrigerante((boolean)chaeckListSnapshot.child("nivelLiquidoRefrigerante").getValue());
										checkList.setNivelOleoHidraulico((boolean)chaeckListSnapshot.child("nivelOleoHidraulico").getValue());
										checkList.setNivelOleoMotor((boolean)chaeckListSnapshot.child("nivelOleoMotor").getValue());
										checkList.setParabrisaFrontal((boolean)chaeckListSnapshot.child("parabrisaFrontal").getValue());
										checkList.setParadaCombinada((boolean)chaeckListSnapshot.child("paradaCombinada").getValue());
										checkList.setParadaImediata((boolean)chaeckListSnapshot.child("paradaImediata").getValue());
										checkList.setPinosBielasFacasRolosECilindros((boolean)chaeckListSnapshot.child("pinosBielasFacasRolosECilindros").getValue());
										checkList.setPresencaGeralTrincasAcessorio((boolean)chaeckListSnapshot.child("presencaGeralTrincasAcessorio").getValue());
										checkList.setPresencaGeralTrincasMaquinaBase((boolean)chaeckListSnapshot.child("presencaGeralTrincasMaquinaBase").getValue());
										checkList.setProtecaoFrontalRadiador((boolean)chaeckListSnapshot.child("protecaoFrontalRadiador").getValue());
										checkList.setRadioComunicacao((boolean)chaeckListSnapshot.child("radioComunicacao").getValue());
										checkList.setRoletesInferiores((boolean)chaeckListSnapshot.child("roletesInferiores").getValue());
										checkList.setRoletesSuperiores((boolean)chaeckListSnapshot.child("roletesSuperiores").getValue());
										checkList.setRotator((boolean)chaeckListSnapshot.child("rotator").getValue());
										checkList.setSapatasEsteira((boolean)chaeckListSnapshot.child("sapatasEsteira").getValue());
										checkList.setSinalizacaoReEMovimento((boolean)chaeckListSnapshot.child("sinalizacaoReEMovimento").getValue());
										checkList.setTampasProtecao((boolean)chaeckListSnapshot.child("tampasProtecao").getValue());
										checkList.setTensaoEsteiras((boolean)chaeckListSnapshot.child("tensaoEsteiras").getValue());
										checkList.setTravaCabineFORWARDER((boolean)chaeckListSnapshot.child("travaCabineFORWARDER").getValue());
										checkList.setVazamentoMangueirasECilindros((boolean)chaeckListSnapshot.child("vazamentoMangueirasECilindros").getValue());
										boletim.setBoletimCheckList(checkList);
									}
									listBoletins.add(boletim);
									System.out.println("Entrou");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                }
		            	executou[0] = "true";
		            }

		            @Override
		            public void onCancelled(FirebaseError fe) {
		                
		            }
		        });
		
		System.out.println("Entrou do while");
		while(executou[0] == null){
//			System.out.println("Dentro do while");
			id++;
		}
		System.out.println("Saiu do while");
		for (BoletimDiario boletimDiario : listBoletins) {
			Fazenda fazendaBoletim = DAOFactory.criarFazendaDAO().carregar(boletimDiario.getIdFazenda());
			boletimDiario.setFazenda(fazendaBoletim);
			saveOrUpdate(boletimDiario);
		}
		if(fazenda!=null){
			this.boletins = buscarBoletinsPorFazenda(fazenda.getId());
			for (Object bolet : this.boletins) {
				BoletimDiario boletim = (BoletimDiario) bolet;
				for (BoletimApontamento apontamento : boletim.getApontamentoList()) {
					/*Firebase firebase2 = new Firebase("https://baseagro-f1859.firebaseio.com/"+ empresa.getCnpj() + "/bdos/"+ boletimDiario.getIdFirebase()+"/boletins/"+apontamento.getIdFirebase());
					firebase2.child("dataProcessamento").setValue(Utils.toString(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss"));*/	
				}
			}
		}
		System.out.println("Eai");
	}
	
	public String voltarBoletimDiario() {
		this.boletimDiario = (BoletimDiario) facesContext.getExternalContext().getSessionMap().get("boletimDiario"); 
		return "/faces/restrito/public/boletimDiario/boletimDiario";
	}

	public BoletimChecklist getBoletimCheckList() {
		if(boletimDiario!=null && boletimDiario.getId()!=null && boletimDiario.getBoletimCheckList()!=null){
			boletimCheckList = boletimDiario.getBoletimCheckList();
			checklistId = boletimDiario.getBoletimCheckList().getId();
		}
		return boletimCheckList;
	}

	public void setBoletimCheckList(BoletimChecklist boletimCheckList) {
		this.boletimCheckList = boletimCheckList;
	}

	public String getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(String checklistId) {
		this.checklistId = checklistId;
	}


	
	
	
}