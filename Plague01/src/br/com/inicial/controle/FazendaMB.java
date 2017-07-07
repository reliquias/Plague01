package br.com.inicial.controle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.primefaces.component.gmap.GMap;
import org.primefaces.component.gmap.GMapInfoWindow;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.EstadoDAO;
import br.com.inicial.dao.FazendaDAO;
import br.com.inicial.modelo.Cidade;
import br.com.inicial.modelo.Empresa;
import br.com.inicial.modelo.Estado;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.Pais;
import br.com.inicial.modelo.Talhao;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.Utils;
import br.com.inicial.util.XLazyModel;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.vividsolutions.jts.geom.MultiLineString;

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
	private List<Estado> estados = new ArrayList<Estado>();
	private List<Cidade> cidades = new ArrayList<Cidade>();
	
	private List<Coordinate> coordinates;

	public FazendaMB() {
//		ouvinteFirebase();
		this.fazendaDAO = DAOFactory.criarFazendaDAO();
		fazendas = getLista();
		fazendasModel = new XLazyModel(fazendas);
		if(fazendasModel.getPageSize() == 0){
			fazendasModel.setPageSize(1);
		}
		getPolygonModel();
	}
	
	public String novo() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fazendaMB");
		this.fazenda = new Fazenda();
		this.estados = new ArrayList<Estado>();
		this.cidades = new ArrayList<Cidade>();
		return "fazenda";
	}

	public String editar() {
		this.estados = new ArrayList<Estado>();
		this.cidades = new ArrayList<Cidade>();
		return "fazenda";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fazendaMB");
		fazendasModel = null;
    	return "fazendaLista";
    }
	
	public void adicionaOuAtualiza() {
		if (fazenda.getId() == 0 || fazenda.getId() == null) {
			try {
				fazendaDAO.salvar(fazenda);
				JsfUtil.addSuccessMessage("Fazenda salvo com Sucesso");
				fazendaFirebase(fazenda);
//				ouvinteFirebase();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				fazendaDAO.atualizar(fazenda);
				JsfUtil.addSuccessMessage("Fazenda salvo com Sucesso");
//				fazendaFirebase(fazenda);
			} catch (Exception e) {
				System.out.println(e.getMessage());
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
	
	public String talhoesForm() {
//		facesContext.getExternalContext().getSessionMap().put("fazenda", fazenda);
		return "/faces/restrito/public/talhao/talhaoLista";
    }

	public String boletinsForm() {
		return "/faces/restrito/public/boletimDiario/boletimDiarioLista";
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        try {
        	FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            byte[] kmlFile = event.getFile().getContents();
			fazenda.setKmlFile(kmlFile);
			fazenda.setNomeArquivo(event.getFile().getFileName());
			//            File f = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Doricardo II.kml");
			Kml kml = kmlFile(event.getFile().getInputstream());
			Feature feature = kml.getFeature();
		    parseFeature(feature);
		    String coordenadas = "";
		    int x = 0;
		    int y = coordinates.size()/4;
		    for (Coordinate c : coordinates) {
		    	coordenadas = coordenadas + c.getLatitude() + " " + c.getLongitude() + ";";
		    	if(x == y){
		    		fazenda.setAreaInicial(c.getLatitude() + " " + c.getLongitude());
		    	}
		    	x++;
		    }
		    fazenda.setArea(coordenadas);
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
	
	public static Kml kmlFile(InputStream is) throws Exception {
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
	
	public void onPointSelect(org.primefaces.event.map.PointSelectEvent event) {
		System.out.println("oint");
        LatLng latlng = event.getLatLng();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Circle Selected", null));
//        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
    }
	
	public void onPolygonSelect(org.primefaces.event.map.OverlaySelectEvent event) {
		System.out.println("Overlay");
		Overlay latlng = event.getOverlay();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Double latitude = Double.parseDouble(params.get("latitude"));
        Double longitude = Double.parseDouble(params.get("longitude"));
    }
	
	public void onGeocode(GeocodeEvent event) {
		List<GeocodeResult> results = event.getResults();
		System.out.println("teste");
	}
	
	public void onMarkerSelect(OverlaySelectEvent event) {
		System.out.println("Ts");
		Marker selectedMarker = (Marker) event.getOverlay();

		 }
	
	public void onCircleSelect(OverlaySelectEvent event) {
		System.out.println("Circle");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Circle Selected", null));
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
	
	public MapModel getPolygonModel() {
		if(fazenda.getArea()!=null && !fazenda.getArea().equalsIgnoreCase("")){
			polygonModel = new DefaultMapModel();
			org.primefaces.model.map.Polygon polygon = new org.primefaces.model.map.Polygon();
			String[] areas = fazenda.getArea().split(";");
			for (String longLat : areas) {
				double lat = Double.parseDouble(longLat.split(" ")[0]);
				double lon = Double.parseDouble(longLat.split(" ")[1]);
				LatLng coord = new LatLng(lat,lon);
				polygon.getPaths().add(coord);
			}
			  
	        polygon.setStrokeColor("#00EE76");
	        polygon.setFillColor("#00EE76");
	        polygon.setStrokeOpacity(0.7);
	        polygon.setFillOpacity(0.7);
	        polygonModel.addOverlay(polygon);
	        
	        List<Talhao> talhoes = DAOFactory.criarTalhaoDAO().buscarListaPorCampo("fazenda.id", fazenda.getId());
	        
	        for (Talhao talhao: talhoes) {
	        	org.primefaces.model.map.Polygon talhaoPolygon = new org.primefaces.model.map.Polygon();
	        	String[] areasTalhao = talhao.getArea().split(";");
	        	for (String longLat : areasTalhao) {
	        		double lat = Double.parseDouble(longLat.split(" ")[0]);
					double lon = Double.parseDouble(longLat.split(" ")[1]);
					LatLng coord = new LatLng(lat,lon);
					talhaoPolygon.getPaths().add(coord);
				}
	        	talhaoPolygon.setStrokeColor("#d10a21");
	        	talhaoPolygon.setFillColor("#d10a21");
	        	talhaoPolygon.setStrokeOpacity(0.7);
	        	talhaoPolygon.setFillOpacity(0.7);
	        	talhaoPolygon.setId(talhao.getId().toString());
		        polygonModel.addOverlay(talhaoPolygon);
			}
	        
/*	        try {
				talhaoShape();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		return polygonModel;
    }
	
	private void talhaoShape() throws IOException{
		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoAn.shp");
//		File file = new File("C:\\Fontes Java\\Plague01Docs\\Fazenda Santo Antonio\\Shapes\\Shapes\\FazStoRe.shp");
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
				System.out.println("Coodenadas=" +x);
				x++;
				org.primefaces.model.map.Polygon talhaoPolygon = new org.primefaces.model.map.Polygon();
				SimpleFeature feature = (SimpleFeature) results.next();
				MultiLineString mult = (MultiLineString) feature.getAttribute(0);
				com.vividsolutions.jts.geom.Coordinate[] coordenadas = mult.getCoordinates();
				for (com.vividsolutions.jts.geom.Coordinate coordinate : coordenadas) {
					double lat = coordinate.y;
					double lon = coordinate.x;
					LatLng coord = new LatLng(lat,lon);
					talhaoPolygon.getPaths().add(coord);
					System.out.println(coordinate.y);
					System.out.println(coordinate.x);
				}
				talhaoPolygon.setStrokeColor("#d10a21");
	        	talhaoPolygon.setFillColor("#d10a21");
	        	talhaoPolygon.setStrokeOpacity(0.7);
	        	talhaoPolygon.setFillOpacity(0.7);
		        polygonModel.addOverlay(talhaoPolygon);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			results.close();
			dataStore.dispose();
		}
	}
	
	/*public MapModel getPolygonModelDoFile() {
		carregarKmlFile();
		if(coordinates!=null){
			polygonModel = new DefaultMapModel();
			org.primefaces.model.map.Polygon polygon = new org.primefaces.model.map.Polygon();
			int x = 0;
			for (Coordinate coordinate : coordinates) {
				if(x==0){
					x++;
					fazenda.setArea(coordinate.getLatitude() + " " + coordinate.getLongitude());
					System.out.println(fazenda.getArea());
				}
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
	
	/**trecho que faz os trabalhos javascript*/
	public void cmbEstadoChange(AjaxBehaviorEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		String idPais = event.getComponent().getAttributes().get("value").toString();
		Pais pais = DAOFactory.criarPaisDAO().buscarPorCampo("sigla", idPais);
		this.estados = DAOFactory.criarEstadoDAO().buscarListaPorCampo("pais.id", pais.getId());
	}
	
	public SelectItem[] getItemsAvailableSelectOneEstado() {
		if(this.estados.size() == 0 && fazenda!=null && fazenda.getPais()!=null){
			this.estados = DAOFactory.criarEstadoDAO().buscarListaPorCampo("pais.id", fazenda.getPais().getId());
		}
        return JsfUtil.getSelectItems(this.estados, true);
    }

	public void cmbCidadeChange(AjaxBehaviorEvent event) {
		String idCidade = event.getComponent().getAttributes().get("value").toString();
		EstadoDAO estadoDao = DAOFactory.criarEstadoDAO();
		Estado estado = estadoDao.buscarPorCampo("nome", idCidade);
		this.cidades = DAOFactory.criarCidadeDAO().buscarListaPorCampo("estado.id", estado.getId());
	}
	
	public SelectItem[] getItemsAvailableSelectOneCidade() {
		if(this.cidades.size() == 0 && fazenda!=null && fazenda.getEstado()!=null){
			this.cidades = DAOFactory.criarCidadeDAO().buscarListaPorCampo("estado.id", fazenda.getEstado().getId());
		}
		return JsfUtil.getSelectItems(this.cidades, true);
	}
	
	private void ouvinteFirebase(){
		final Fazenda fazendaFire = new Fazenda();
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
		                            fazendaFire.setArea(fazendaSnapshot.child("area").getValue().toString());
		                            fazendaFire.setAreaInicial(fazendaSnapshot.child("areaInicial").getValue().toString());
		                            fazendaFire.setNome(fazendaSnapshot.child("nome").getValue().toString());
//		                            Pais pais = DAOFactory.criarPaisDAO().carregar(Integer.parseInt(fazendaSnapshot.child("paisId").getValue().toString()));
		                        }
		                    }
		                }
//		                System.out.println("Area Inicial = " + areaInicialFazenda[0]);
		                
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
	
	private void fazendaFirebase(Fazenda fazenda){
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		if(empresa!=null){
			Firebase firebase = new Firebase("https://baseagro-f1859.firebaseio.com/"+empresa.getCnpj()+"/fazenda/");
			Firebase firebaseRef = firebase.push();
			
			firebaseRef.child("id").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getId()));
			firebaseRef.child("nome").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getNome()));
			firebaseRef.child("areaInicial").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getAreaInicial()));
			firebaseRef.child("area").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getArea()));
			firebaseRef.child("paisId").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getPais().getId()));
			firebaseRef.child("paisDescricao").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getPais().getDescricao()));
			firebaseRef.child("estadoId").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getEstado().getId()));
			firebaseRef.child("estadoNome").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getEstado().getSigla()));
			firebaseRef.child("cidadeId").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getCidade().getId()));
			firebaseRef.child("cidadeNome").setValue(Utils.retornarStringVazioQuanoNulo(fazenda.getCidade().getNome()));
		}
	}
	
	public void onDataChange(DataSnapshot snapshot) {
	    GenericTypeIndicator<Map<String,String>> mapType = new GenericTypeIndicator<Map<String, String>>() { };
	    Map<String,String>  namelist =  snapshot.child("Driver name").getValue(mapType);
	    Map<String,String> carlist =  snapshot.child("Drivers car").getValue(mapType);
	    if (namelist!=null & carlist!=null) {
	        for (String name: namelist.values()) {
	            System.out.println(name);
	        }
	    }
	}
	
	private void testeShape(){
		File file = new File("mayshapefile.shp");
/*
		try {
		  Map connect = new HashMap();
		  connect.put("url", file.toURL());

		  DataStore dataStore = DataStoreFinder.getDataStore(connect);
		  String[] typeNames = dataStore.getTypeNames();
		  String typeName = typeNames[0];

		  System.out.println("Reading content " + typeName);

		  FeatureSource featureSource = dataStore.getFeatureSource(typeName);
		  FeatureCollection collection = featureSource.getFeatures();
		  FeatureIterator iterator = collection.features();


		  try {
		    while (iterator.hasNext()) {
		      Feature feature = iterator.next();
		      Geometry sourceGeometry = feature.getDefaultGeometry();
		    }
		  } finally {
		    iterator.close();
		  }

		} catch (Throwable e) {}
		*/
	}
	
	public String getCentroMapa(){
		String centro = "";
		if(fazenda!=null && fazenda.getAreaInicial() !=null && !fazenda.getAreaInicial().equals("")){
			centro = fazenda.getAreaInicial().split(" ")[0] +", " + fazenda.getAreaInicial().split(" ")[1];
		}
		return centro;
	}
	
	protected void encodeOverlays(FacesContext context, GMap map) throws IOException {
		MapModel model = map.getModel();
		ResponseWriter writer = context.getResponseWriter();
		
		//Overlays
		if(model != null) {
			if(!model.getMarkers().isEmpty()) { 
//				encodeMarkers(context, map);
			}
			if(!model.getPolylines().isEmpty()){ 
//				encodePolylines(context, map);
			if(!model.getPolygons().isEmpty()){ 
//				encodePolygons(context, map);
			}
			if(!model.getCircles().isEmpty()){ 
//				encodeCircles(context, map);
			}
			if(!model.getRectangles().isEmpty()){ 
//				encodeRectangles(context, map);
			}
		}
	       
	       GMapInfoWindow infoWindow = map.getInfoWindow();

	       if(infoWindow != null) {
	           writer.write(",infoWindow: new google.maps.InfoWindow({");
	           writer.write("id:'" + infoWindow.getClientId(context) + "'");
	           writer.write("})");
	       }
	}
	
	}
	
}