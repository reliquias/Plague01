package br.com.inicial.controle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.FazendaDAO;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Container;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="fazendaMB")
@SessionScoped
public class FazendaMB {

	private Fazenda fazenda = new Fazenda();
	private FazendaDAO	fazendaDAO;
	private List<Object>	fazendas = new ArrayList<Object>();
	private XLazyModel fazendasModel;
	private String	       destinoSalvar;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	private MapModel polygonModel;
	
	private List<Coordinate> coordinates;

	public FazendaMB() {
		this.fazendaDAO = DAOFactory.criarFazendaDAO();
		fazendas = getLista();
		fazendasModel = new XLazyModel(fazendas);
		if(fazendasModel.getPageSize() == 0){
			fazendasModel.setPageSize(1);
		}
		getPolygonModel();
	}
	
	public String novo() {
//		facesContext.getExternalContext().getSessionMap().remove("fazenda");
		this.fazenda = new Fazenda();
		return "fazenda";
	}

	public String editar() {
		return "fazenda";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
//		facesContext.getExternalContext().getSessionMap().remove("fazenda");
		fazendasModel = null;
    	return "fazendaLista";
    }
	
	public void adicionaOuAtualiza() {
		if (fazenda.getId() == 0 || fazenda.getId() == null) {
			try {
				fazendaDAO.salvar(fazenda);
				JsfUtil.addSuccessMessage("Fazenda salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				fazendaDAO.atualizar(fazenda);
				JsfUtil.addSuccessMessage("Fazenda salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "fazendaLista";
	}
	
	public String salvar() {
//		FacesContext context = FacesContext.getCurrentInstance();
		this.fazendaDAO.salvar(this.fazenda);

		return "/fazenda/fazendaLista";
	}

	public String ativar() {
		this.fazendaDAO.salvar(this.fazenda);
		return null;
	}

	public Fazenda carregar(Integer codigo) {
		return this.fazendaDAO.carregar(codigo);
	}

	public String excluir() {
		this.fazendaDAO.excluir(this.fazenda);
		this.fazendas = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        fazendaDAO.excluir(fazenda);
        fazendasModel = null;
    }

	public List getLista() {
		return this.fazendaDAO.listar();
	}

	public Fazenda getFazenda() {
		/*if(facesContext.getExternalContext().getSessionMap().get("fazenda") !=null){
			fazenda = (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");
    	}*/
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		this.fazenda = fazenda;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getFazendasModel() {
		if(fazendasModel == null){
    		fazendas = getLista();
        	fazendasModel = new XLazyModel(fazendas);
        	if(fazendasModel.getPageSize() == 0){
        		fazendasModel.setPageSize(1);
        	}
    	}
		return fazendasModel;
	}

	public void setFazendasModel(XLazyModel fazendasModel) {
		this.fazendasModel = fazendasModel;
	}
	
	public String zonasForm() {
//		facesContext.getExternalContext().getSessionMap().put("fazenda", fazenda);
		return "/faces/public/zona/zonaLista.xhtml";
    }
	
	public void handleFileUpload(FileUploadEvent event) {
        try {
        	FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            byte[] kmlFile = event.getFile().getContents();
			fazenda.setKmlFile(kmlFile);
			fazenda.setNomeArquivo(event.getFile().getFileName());
			//            File f = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Doricardo II.kml");
			Kml kml = getKml(event.getFile().getInputstream());
			Feature feature = kml.getFeature();
		    parseFeature(feature);
		    for (Coordinate c : coordinates) {
				fazenda.setArea(c.getLatitude() + " " + c.getLongitude());
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void carregarKmlFile(){
//		File is = new File("C:\\Fontes Java\\Plague01Docs\\FazendaDoricardoII.kml");
//		InputStream is = getClass().getClassLoader().getResourceAsStream("C:\\Fontes Java\\Plague01Docs\\Fazenda Doricardo II.kml");
//	    Kml kml = Kml.unmarshal(is);
		if(fazenda.getKmlFile()!=null){
			InputStream is = new ByteArrayInputStream(fazenda.getKmlFile());
			Kml kml = Kml.unmarshal(is);
			Feature feature = kml.getFeature();
		    parseFeature(feature);
		}
	}
	
	public static Kml getKml(InputStream is) throws Exception {
	    String str = IOUtils.toString( is );
	    IOUtils.closeQuietly( is );
	    str = StringUtils.replace( str, "xmlns=\"http://earth.google.com/kml/2.2\"", "xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\"" );
	    ByteArrayInputStream bais = new ByteArrayInputStream( str.getBytes( "UTF-8" ) );
	    return Kml.unmarshal(bais);
	}
	
	/*public void parseKml() {
	    String src = "misctests/stackoverflow/kml/labasa.kml";
	    InputStream is = getClass().getClassLoader().getResourceAsStream(src);
	    Assert.assertNotNull(is);
	    Kml kml = Kml.unmarshal(is);
	    Feature feature = kml.getFeature();
	    parseFeature(feature);
	}*/

	private void parseFeature(Feature feature) {
	    if(feature != null) {
	        if(feature instanceof Document) {
	            Document document = (Document) feature;
	            List<Feature> featureList = document.getFeature();
	            for(Feature documentFeature : featureList) {
	            	List<Placemark> list = getPlacemarks(documentFeature);
	            	for (Placemark placemark : list) {
	            		Geometry geometry = placemark.getGeometry();
	                    parseGeometry(geometry);
					}
	            }
	        }
	    }
	}

	private void parseGeometry(Geometry geometry) {
	    if(geometry != null) {
	        if(geometry instanceof Polygon) {
	            Polygon polygon = (Polygon) geometry;
	            Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
	            if(outerBoundaryIs != null) {
	                LinearRing linearRing = outerBoundaryIs.getLinearRing();
	                if(linearRing != null) {
	                    coordinates = linearRing.getCoordinates();
	                }
	            }
	        }
	    }
	}
	
	public void onPolygonSelect(OverlaySelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Polygon Selected", null));
    }

	private void parseCoordinate(Coordinate coordinate) {
	    if(coordinate != null) {
	        System.out.println("Longitude: " +  coordinate.getLongitude());
	        System.out.println("Latitude : " +  coordinate.getLatitude());
	        System.out.println("Altitude : " +  coordinate.getAltitude());
	        System.out.println("");
	    }
	}
	
	public ArrayList<Placemark> getPlacemarks(Feature root) { 
        ArrayList<Placemark> Placemarks = new ArrayList<Placemark>(); 

        if (root instanceof Container) { 
                if (root instanceof Document) { 
                        for (Feature feature : ((Document) root).getFeature()) { 
                                if (feature instanceof Placemark) { 
                                        Placemarks.add((Placemark) feature); 
                                } else if ((feature instanceof Document)|| (feature instanceof Folder)) { 
                                        Placemarks.addAll(getPlacemarks(feature)); 
                                } 
                        } 
                } else if (root instanceof Folder) { 
                        for (Feature feature : ((Folder) root).getFeature()) { 
                                if (feature instanceof Placemark) { 
                                        Placemarks.add((Placemark) feature); 
                                } else if ((feature instanceof Document)|| (feature instanceof Folder)) { 
                                        Placemarks.addAll(getPlacemarks(feature)); 
                                } 
                        } 
                } 
        } else { 
                if (root instanceof Placemark) { 
                        Placemarks.add((Placemark) root); 
                } 
        } 
        return Placemarks; 
	}
	
/*	public MapModel getPolygonModel() {
		return polygonModel;
	}*/
	
//	public MapModel montarPolygonModel(List<Coordinate> coordinates) {
	
	public MapModel getPolygonModel() {
		carregarKmlFile();
		if(coordinates!=null){
			polygonModel = new DefaultMapModel();
			org.primefaces.model.map.Polygon polygon = new org.primefaces.model.map.Polygon();
			int x = 0;
			for (Coordinate coordinate : coordinates) {
				/*if(x==0){
					x++;
					fazenda.setArea(coordinate.getLatitude() + " " + coordinate.getLongitude());
					System.out.println(fazenda.getArea());
				}*/
				LatLng coord = new LatLng(coordinate.getLatitude(),coordinate.getLongitude());
				polygon.getPaths().add(coord);
			}
			  
	        polygon.setStrokeColor("#FF9900");
	        polygon.setFillColor("#FF9900");
	        polygon.setStrokeOpacity(0.7);
	        polygon.setFillOpacity(0.7);
	          
	        polygonModel.addOverlay(polygon);
		}
		return polygonModel;
    }
	
	public MapModel getPolygonModel2() {
        polygonModel = new DefaultMapModel();
		
        		double lat = Double.parseDouble("-23.563556");
				double lon = Double.parseDouble("-46.633307");
				
				LatLng coord3 = new LatLng(lat+0.000784, lon+0.000378);
		        LatLng coord2 = new LatLng(lat, lon);
		        LatLng coord1 = new LatLng(lat+0.000735, lon+0.001242);
		        
		        System.out.println((lat+0.000784) + " * " + (lon+0.000378));
		        System.out.println((lat+0.000735) + " * " + (lon+0.001242));

		        org.primefaces.model.map.Polygon polygon = new org.primefaces.model.map.Polygon();
		        polygon.getPaths().add(coord1);
		        polygon.getPaths().add(coord2);
		        polygon.getPaths().add(coord3);
		  
		        polygon.setStrokeColor("#FF9900");
		        polygon.setFillColor("#FF9900");
		        polygon.setStrokeOpacity(0.7);
		        polygon.setFillOpacity(0.7);
		          
		        polygonModel.addOverlay(polygon);
		return polygonModel;
    }
	
	/**
	 * vaivaivaivaiavai
	 */
}