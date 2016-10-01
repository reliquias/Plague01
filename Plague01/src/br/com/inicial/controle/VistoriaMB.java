package br.com.inicial.controle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.inicial.dao.VistoriaDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.Vistoria;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="vistoriaMB")
@SessionScoped
public class VistoriaMB {

	private Vistoria vistoria = new Vistoria();
	private VistoriaDAO	vistoriaDAO;
	private List<Vistoria>	vistorias = new ArrayList<Vistoria>();
	private XLazyModel vistoriasModel;
	private String	       destinoSalvar;
	private FazendaMB fazendaMB;
	private Fazenda fazenda;
	FacesContext facesContext = FacesContext.getCurrentInstance();

	public VistoriaMB() {
		System.out.println("----------------Inicio---------------");
		ouvinteFirebase();
		System.out.println("------------------Fim---------------");
	}
	
	public String editar() {
		facesContext.getExternalContext().getSessionMap().put("vistoria", vistoria);
		return "vistoria";
	}

	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vistoria");
		vistoriasModel = null;
		return "vistoriaLista";
    }
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.vistoriaDAO.salvar(this.vistoria);
		return "/vistoria/vistoriaLista";
	}

	public String ativar() {
		this.vistoriaDAO.salvar(this.vistoria);
		return null;
	}

	public Vistoria carregar(Integer codigo) {
		return this.vistoriaDAO.carregar(codigo);
	}

	public String excluir() {
		this.vistoriaDAO.excluir(this.vistoria);
		this.vistorias = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        vistoriaDAO.excluir(vistoria);
        vistoriasModel = null;
    }

	public List getLista() {
		return this.vistoriaDAO.listar();
	}

	public Vistoria getVistoria() {
		return vistoria;
	}

	public void setVistoria(Vistoria vistoria) {
		this.vistoria = vistoria;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	/*public XLazyModel getTalhoesModel() {
		if(vistoriasModel == null){
			vistorias = buscarVistoriasPorFazenda(fazenda.getId());
        	vistoriasModel = new XLazyModel(vistorias);
        	if(vistoriasModel.getPageSize() == 0){
        		vistoriasModel.setPageSize(1);
        	}
    	}
		return vistoriasModel;
	}*/

	public void setTalhoesModel(XLazyModel vistoriasModel) {
		this.vistoriasModel = vistoriasModel;
	}
	
	public List buscarVistoriasPorFazenda(Integer idFazenda) {
    	return vistoriaDAO.buscarListaPorCampo("fazenda.id", idFazenda);
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
	
	private void ouvinteFirebase2(){
		final Fazenda fazendaFire = new Fazenda();
		final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/cliente01/vistorias/");
		final String[] areaFazenda = {null};
		final String[] areaInicialFazenda = {null};
		areaInicialFazenda.toString().equals("");
		firebase.addValueEventListener(new ValueEventListener() {

		            @Override
		            public void onDataChange(DataSnapshot ds) {
		                
		                for (DataSnapshot fazendaSnapshot: ds.getChildren()) {
//		                    if (fazendaSnapshot.child("nome").getValue()!=null) {
		                            areaFazenda[0] = fazendaSnapshot.child("armazenamento").getValue().toString();
		                            areaInicialFazenda[0] = fazendaSnapshot.child("local").getValue().toString();
//		                            Pais pais = DAOFactory.criarPaisDAO().carregar(Integer.parseInt(fazendaSnapshot.child("paisId").getValue().toString()));
//		                    }
		                }
		            }

		            @Override
		            public void onCancelled(FirebaseError fe) {
		                
		            }
		        });
		int cont =1;
		while(areaInicialFazenda[0] == null){
			System.out.println("Dentro do while: " + cont);
			cont++;
		}
		String areaInicial = areaInicialFazenda[0];
		System.out.println("Wanderson: "+ fazendaFire.getArea());
		
	}
	
	private void ouvinteFirebase(){
		if(vistorias!=null && vistorias.size() == 0){
			final List<Vistoria> listVistorias = new ArrayList<Vistoria>();
			final String[] executou = {null};
			Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
			final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/vistorias/");
			firebase.addValueEventListener(new ValueEventListener() {
	
			            @Override
			            public void onDataChange(DataSnapshot ds) {
			            	
			            	executou[0] = "true";
			                
			                for (DataSnapshot vitoriaSnapshot: ds.getChildren()) {
			                	Vistoria vist = new Vistoria();
			                    vist.setId(new BigDecimal(Math.random()).intValue());
			                    vist.setArmazenamento(vitoriaSnapshot.child("armazenamento").getValue().toString());
			                    vist.setCoordenadas(vitoriaSnapshot.child("coordenadas").getValue().toString());
			                    vist.setStatus(vitoriaSnapshot.child("status").getValue().toString());
			                    vist.setResponsavel(vitoriaSnapshot.child("responsavel").getValue().toString());
			                    listVistorias.add(vist);
			                }
			            }
	
			            @Override
			            public void onCancelled(FirebaseError fe) {
			                
			            }
			        });
			
			while(executou[0] == null){
				System.out.println("Dentro do while");
			}
			
			vistorias = listVistorias;
			for (Vistoria vistoria : listVistorias) {
				System.out.println("Vistoria: "+vistoria.getResponsavel());
			}
		}
	}

	public List<Vistoria> getVistorias() {
		ouvinteFirebase();
		return vistorias;
	}

	public void setVistorias(List<Vistoria> vistorias) {
		this.vistorias = vistorias;
	}
	
	public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getVistorias(), true);
    }
	
}