package br.com.inicial.controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.primefaces.event.FileUploadEvent;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.TalhaoDAO;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.Talhao;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;

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
	File fileTalhao;

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
		facesContext.getExternalContext().getSessionMap().put("talhao", talhao);
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
			
			firebaseRef.child("id").setValue(Utils.retornarStringVazioQuanoNulo(tahao.getId()));
			firebaseRef.child("nome").setValue(Utils.retornarStringVazioQuanoNulo(tahao.getNome()));
			firebaseRef.child("area").setValue(Utils.retornarStringVazioQuanoNulo(tahao.getArea()));
			firebaseRef.child("fazendaId").setValue(Utils.retornarStringVazioQuanoNulo(tahao.getFazenda().getId()));
			firebaseRef.child("fazendaDescricao").setValue(Utils.retornarStringVazioQuanoNulo(tahao.getFazenda().getNome()));
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
	
	private void carregarShape()throws IOException {
		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoAn.shp");
//		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoRe.shp");
		Map<String, Serializable> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource source = dataStore.getFeatureSource(typeName);

		FeatureCollection collection = source.getFeatures();
		FeatureIterator<SimpleFeature> results = collection.features();
		try {
			while (results.hasNext()) {
				SimpleFeature feature = (SimpleFeature) results.next();
				MultiLineString mult = (MultiLineString) feature.getAttribute(0);
				Coordinate[] coordenadas = mult.getCoordinates();
				for (Coordinate coordinate : coordenadas) {
					System.out.println(coordinate.y);
					System.out.println(coordinate.x);
					System.out.println(coordinate.z);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			results.close();
			dataStore.dispose();
		}
	}
	
	public void uploadTalhao(FileUploadEvent event) {
		try {
			FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			InputStream input = event.getFile().getInputstream();
			
			Path folder = Paths.get(System.getenv("temp"));
			String filename = FilenameUtils.getBaseName(event.getFile().getFileName()); 
			String extension = FilenameUtils.getExtension(event.getFile().getFileName());
			Path file = Files.createTempFile(folder, filename + "-", "." + extension);
			Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
			fileTalhao = new File(file.toString());
			processarArquivo(fileTalhao);
			talhoesModel = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void processarArquivo(File file) throws IOException {
		if (file != null) {
			Map<String, Serializable> map = new HashMap<>();
			map.put("url", file.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];
			FeatureSource source = dataStore.getFeatureSource(typeName);
			org.geotools.feature.FeatureCollection collection = source.getFeatures();
			FeatureIterator<SimpleFeature> results = collection.features();
			try {
				int x = 1;
				while (results.hasNext()) {
					String area = "";
					SimpleFeature feature = (SimpleFeature) results.next();
					MultiLineString mult = (MultiLineString) feature.getAttribute(0);
					com.vividsolutions.jts.geom.Coordinate[] coordenadas = mult.getCoordinates();
					for (com.vividsolutions.jts.geom.Coordinate coordinate : coordenadas) {
						area = area + coordinate.y + " " + coordinate.x + ";";
					}
					if(x==1){
						talhaoDAO.deletarTalhao(fazenda.getId());
					}
					Talhao talhao = new Talhao();
					talhao.setNome(x <= 9 ? "0" + x : "" + x);
					talhao.setFazenda(fazenda);
					talhao.setArea(area);
					talhaoDAO.salvar(talhao);
					talhaoFirebase(talhao);
					x++;
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				results.close();
				dataStore.dispose();
				JsfUtil.addSuccessMessage("Upload dos talhão ok!");
			}
		} else {
			JsfUtil.addSuccessMessage("Erro, arquivo invalido!");
		}
	}
}