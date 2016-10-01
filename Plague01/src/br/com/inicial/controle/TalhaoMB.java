package br.com.inicial.controle;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.TalhaoDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.Talhao;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="talhaoMB")
@RequestScoped
public class TalhaoMB {

	private Talhao talhao = new Talhao();
	private TalhaoDAO	talhaoDAO;
	private List<Object>	talhoes = new ArrayList<Object>();
	private XLazyModel talhoesModel;
	private String	       destinoSalvar;
	private FazendaMB fazendaMB;
	private Fazenda fazenda;
	FacesContext facesContext = FacesContext.getCurrentInstance();

	public TalhaoMB() {
		this.talhaoDAO = DAOFactory.criarTalhaoDAO();
		facesContext = FacesContext.getCurrentInstance();
		fazendaMB =  (FazendaMB) facesContext.getExternalContext().getSessionMap().get("fazendaMB");
		fazenda = fazendaMB.getFazenda();
		talhoes = buscarTalhoesPorFazenda(fazenda.getId());
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
		facesContext.getExternalContext().getSessionMap().put("talhao", talhao);
		return "talhao";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("talhao");
		talhoesModel = null;
		return "talhaoLista";
    }
	
	public void adicionaOuAtualiza() {
		try {
//			InputStream f = this.getClass().getResourceAsStream("C:\\Fontes Java\\Plague01Docs\\talhoes.properties");
			Properties prop = new Properties();
			prop.load(new FileInputStream("C:\\Fontes Java\\Plague01Docs\\talhoes.properties"));
			talhao.setArea(prop.getProperty(talhao.getNome()));
			talhao.setFazenda(fazenda);
			if (talhao.getId() == 0 || talhao.getId() == null) {
				talhaoDAO.salvar(talhao);
				JsfUtil.addSuccessMessage("Talhao salvo com Sucesso");
				talhaoFirebase(talhao);
			} else {
				talhaoDAO.atualizar(talhao);
				JsfUtil.addSuccessMessage("Talhao salvo com Sucesso");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// return "talhaoLista";
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
			/*facesContext = FacesContext.getCurrentInstance();
			fazenda =  (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");*/
			talhoes = buscarTalhoesPorFazenda(fazenda.getId());
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
	
	public List buscarTalhoesPorFazenda(Integer idFazenda) {
    	return talhaoDAO.buscarListaPorCampo("fazenda.id", idFazenda);
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
	
	private void talhaoFirebase(Talhao tahao){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/talhao");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(tahao.getId());
			firebaseRef.child("nome").setValue(tahao.getNome());
			firebaseRef.child("area").setValue(tahao.getArea());
			firebaseRef.child("fazendaId").setValue(tahao.getFazenda().getId());
			firebaseRef.child("fazendaDescricao").setValue(tahao.getFazenda().getNome());
		}
	}
	
	private void ouvinteFirebase(){
		
		final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/cliente01/fazenda/");
		final String[] areaFazenda = {null};
		final String[] areaInicialFazenda = {null};
		areaInicialFazenda.toString().equals("");
		firebase.addValueEventListener(new ValueEventListener() {

		            @Override
		            public void onDataChange(DataSnapshot ds) {
		                
		                for (DataSnapshot fazendaSnapshot: ds.getChildren()) {
		                    if (fazendaSnapshot.child("nome").getValue()!=null) {
		                        String nomeFazenda = fazendaSnapshot.child("nome").getValue().toString();
		                        if (nomeFazenda.equals("DoRicardo")) {
		                            areaFazenda[0] = fazendaSnapshot.child("area").getValue().toString();
		                            areaInicialFazenda[0] = fazendaSnapshot.child("areaInicial").getValue().toString();
		                        }
		                    }
		                }
//		                System.out.println("Area Inicial = " + areaInicialFazenda[0]);
		                
		            }

		            @Override
		            public void onCancelled(FirebaseError fe) {
		                
		            }
		        });
		
		while(areaInicialFazenda[0] == null){
			System.out.println("Dentro do while");
		}
		String areaInicial = areaInicialFazenda[0];
		System.out.println("Wanderson: "+areaInicial);
		
	}
}