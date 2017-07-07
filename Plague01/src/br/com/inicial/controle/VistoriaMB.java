package br.com.inicial.controle;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.VistoriaDAO;
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
	private Integer vistoriaId;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	List<String> images = new ArrayList<String>();
	boolean vai = false;
	
	

	public VistoriaMB() throws InterruptedException {
		this.vistoriaDAO = DAOFactory.criarVistoriaDAO();
		System.out.println("----------------Inicio---------------");
		ouvinteFirebase();
		/*while(!vai){
			Thread.sleep(10000);
		}*/
		System.out.println("------------------Fim---------------");
	}
	
	public String editar() {
		System.out.println("Meu" + vistoriaId);
		vistoria = vistoriaDAO.carregar(vistoriaId);
		return "/faces/restrito/public/vistoria/vistoria.xhtml";
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
	
	public void ouvinteFirebase(){
		if(vistorias!=null && vistorias.size() == 0){
			final List<Vistoria> listVistorias = new ArrayList<Vistoria>();
			final String[] executou = {null};
			Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
			final Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/vistorias/");
			firebase.addValueEventListener(new ValueEventListener() {
	
			            @Override
			            public void onDataChange(DataSnapshot ds) {
			            	
			            	for (DataSnapshot vitoriaSnapshot: ds.getChildren()) {
			                	Vistoria vist = new Vistoria();
			                    vist.setId(Integer.parseInt(vitoriaSnapshot.child("id").getValue().toString()));
			                    vist.setArmazenamento(vitoriaSnapshot.child("armazenamento").getValue().toString());
			                    vist.setCoordenadas(vitoriaSnapshot.child("coordenadas").getValue().toString());
			                    vist.setStatus(vitoriaSnapshot.child("status").getValue().toString());
			                    vist.setResponsavel(vitoriaSnapshot.child("responsavel").getValue().toString());
			                    vist.setDataVistoria(Utils.firebaseToCalendar(vitoriaSnapshot.child("dataHoraCadastro").getValue().toString()));
			                    
			                    /*campos Transientes*/
			                    vist.setIdDoenca(Integer.parseInt(vitoriaSnapshot.child("praga").getValue().toString()));
			                    vist.setIdFazenda(Integer.parseInt(vitoriaSnapshot.child("fazenda").getValue().toString()));
			                    vist.setIdPlanta(Integer.parseInt(vitoriaSnapshot.child("planta").getValue().toString()));
			                    vist.setIdTalhao(Integer.parseInt(vitoriaSnapshot.child("talhao").getValue().toString()));
			                    
			                    
			                    int nImagens = (int) vitoriaSnapshot.child("imagesList").getChildrenCount();
			                    List<String> urlImage = new ArrayList<String>();
								if(nImagens > 0){
									for (DataSnapshot vistoriaImg : vitoriaSnapshot.child("imagesList").getChildren()){
										if(!urlImage.contains(vistoriaImg.getValue().toString())){
											urlImage.add(vistoriaImg.getValue().toString());
										}
									}
									vist.setUrlImage(urlImage);
								}
								listVistorias.add(vist);
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
//			vai = true;
			for (Vistoria vistoria : listVistorias) {
				PlantaTalhao plantaTalhao =DAOFactory.criarPlantaTalhaoDAO().carregar(vistoria.getIdPlanta());
				vistoria.setPlantaTalhao(plantaTalhao);		
				TipoDoenca doenca = DAOFactory.criarTipoDoencaDAO().carregar(vistoria.getIdDoenca());
				vistoria.setPraga(doenca);
				adicionaOuAtualiza2(vistoria);
			}
			this.vistorias = DAOFactory.criarVistoriaDAO().listar();
//			this.vistorias = listVistorias;
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
	
	public void adicionaOuAtualiza(Vistoria vistoria) {
		if (vistoria.getId() == 0 || vistoria.getId() == null) {
			try {
				vistoriaDAO.salvar(vistoria);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getStackTrace());
			}

		} else {
			try {
				vistoriaDAO.atualizar(vistoria);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void adicionaOuAtualiza2(Vistoria vistoria) {
		try {
			/*if(vistoria.getUrlImage1() != null){
				vistoria.setImagem1(Utils.urlToByte(vistoria.getUrlImage1()).toByteArray());
			}*/
			vistoriaDAO.saveOrUpdate(vistoria);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getStackTrace());
		}

	}

	public Integer getVistoriaId() {
		return vistoriaId;
	}

	public void setVistoriaId(Integer vistoriaId) {
		this.vistoriaId = vistoriaId;
	}
	
	public StreamedContent getImagem() {
		InputStream dbStream = new ByteArrayInputStream(vistoria.getImagem1());
    	return new DefaultStreamedContent(dbStream, "image/jpeg");
	}
	
	public List<String> getImagesLink() {
		List<String> images = new ArrayList<>();
		if(vistoria.getImagens()!=null){
			String[] imagens = vistoria.getImagens().split(";");
			for (String string : imagens) {
				images.add(string);
			}
		}
        return images;
    }
	
	public List<String> getImages() throws IOException{
        images.clear();
        Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/temp");
        String path = System.getProperty("java.io.tmpdir");
        String unico = empresa.getCnpj()+vistoria.getResponsavel()+vistoria.getPlantaTalhao();
        unico = unico.replace(",", "").replace(" ", "");
        String caminho = path + "\\" +unico;
        
        if(vistoria.getImagem1() != null){
        	FileOutputStream fos = new FileOutputStream(caminho+ "image1.jpg");
            fos.write(vistoria.getImagem1());
            fos.close();
            images.add(caminho+  "image1.jpg");
        }
        if(vistoria.getImagem2() != null){
        	FileOutputStream fos = new FileOutputStream(caminho+ "image2.jpg");
        	fos.write(vistoria.getImagem2());
        	fos.close();
        	images.add(caminho+ "image2.jpg");
        }
        if(vistoria.getImagem3() != null){
        	FileOutputStream fos = new FileOutputStream(caminho+ "image3.jpg");
        	fos.write(vistoria.getImagem3());
        	fos.close();
        	images.add(caminho+ "image3.jpg");
        }
        if(vistoria.getImagem4() != null){
        	FileOutputStream fos = new FileOutputStream(caminho+ "image4.jpg");
        	fos.write(vistoria.getImagem4());
        	fos.close();
        	images.add(caminho+ "image4.jpg");
        }
        if(vistoria.getImagem5() != null){
        	FileOutputStream fos = new FileOutputStream(caminho+"image5.jpg");
        	fos.write(vistoria.getImagem5());
        	fos.close();
        	images.add(caminho+ "image5.jpg");
        }
        return images;
   }
	
}