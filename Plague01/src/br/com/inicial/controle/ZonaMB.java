package br.com.inicial.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.ZonaDAO;
import br.com.inicial.modelo.Fazenda;
import br.com.inicial.modelo.Zona;
import br.com.inicial.util.JsfUtil;
import br.com.inicial.util.XLazyModel;

@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="zonaMB")
@RequestScoped
public class ZonaMB {

	private Zona zona = new Zona();
	private ZonaDAO	zonaDAO;
	private List<Object>	zonas = new ArrayList<Object>();
	private XLazyModel zonasModel;
	private String	       destinoSalvar;
	private Fazenda fazenda;
	FacesContext facesContext = FacesContext.getCurrentInstance();
	
	private MapModel polygonModel;

	public ZonaMB() {
		this.zonaDAO = DAOFactory.criarZonaDAO();
		facesContext = FacesContext.getCurrentInstance();
		fazenda =  (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");
		zonas = buscarZonasPorFazenda(fazenda.getId());
		zonasModel = new XLazyModel(zonas);
		if(zonasModel.getPageSize() == 0){
			zonasModel.setPageSize(1);
		}
		
		getPolygonModel();
		
		/*circleModel = new DefaultMapModel();
	  	  
        //Shared coordinates
        LatLng coord1 = new LatLng(-23.563556, -46.633307);
        LatLng coord4 = new LatLng(-23.583556, -46.933307);
        
        //23.563556,-46.633307
 
        //Circle
        Circle circle1 = new Circle(coord1, 500);
        circle1.setStrokeColor("#d93c3c");
        circle1.setFillColor("#d93c3c");
        circle1.setFillOpacity(0.5);
 
        Circle circle2 = new Circle(coord4, 300);
        circle2.setStrokeColor("#00ff00");
        circle2.setFillColor("#00ff00");
        circle2.setStrokeOpacity(0.7);
        circle2.setFillOpacity(0.7);
 
        circleModel.addOverlay(circle1);
        circleModel.addOverlay(circle2);*/
	}
	
	public String novo() {
		facesContext.getExternalContext().getSessionMap().remove("zona");
		this.zona = new Zona();
		return "zona";
	}

	public String editar() {
		getPolygonModel();
		return "zona";
	}

	public void preparaEditar(ActionEvent actionEvent) {
	}
	
	public String listar() {
		zonasModel = null;
    	return "zonaLista";
    }
	
	public void adicionaOuAtualiza() {
		fazenda =  (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");
		zona.setFazenda(fazenda);
		if (zona.getId() == 0 || zona.getId() == null) {
			try {
				zonaDAO.salvar(zona);
				zona = new Zona();
				JsfUtil.addSuccessMessage("Zona salvo com Sucesso");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
			try {
				zonaDAO.atualizar(zona);
				JsfUtil.addSuccessMessage("Zona salvo com Sucesso");
			} catch (Exception e) {
			}
		}
//		return "zonaLista";
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		this.zonaDAO.salvar(this.zona);
		return "/zona/zonaLista";
	}

	public String ativar() {
		this.zonaDAO.salvar(this.zona);
		return null;
	}

	public Zona carregar(Integer codigo) {
		return this.zonaDAO.carregar(codigo);
	}

	public String excluir() {
		this.zonaDAO.excluir(this.zona);
		this.zonas = null;
		return null;
	}
	
	public void excluirAjax(ActionEvent actionEvent) {
        zonaDAO.excluir(zona);
        zonasModel = null;
    }

	public List getLista() {
		return this.zonaDAO.listar();
	}

	public Zona getZona() {
		if(facesContext.getExternalContext().getSessionMap().get("zona") !=null){
			zona = (Zona) facesContext.getExternalContext().getSessionMap().get("zona");
		}
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	public XLazyModel getZonasModel() {
		if(zonasModel == null){
			facesContext = FacesContext.getCurrentInstance();
			fazenda =  (Fazenda) facesContext.getExternalContext().getSessionMap().get("fazenda");
			zonas = buscarZonasPorFazenda(fazenda.getId());
        	zonasModel = new XLazyModel(zonas);
        	if(zonasModel.getPageSize() == 0){
        		zonasModel.setPageSize(1);
        	}
    	}
		return zonasModel;
	}

	public void setZonasModel(XLazyModel zonasModel) {
		this.zonasModel = zonasModel;
	}
	
	public List buscarZonasPorFazenda(Integer idFazenda) {
    	return zonaDAO.buscarListaPorCampo("fazenda.id", idFazenda);
    }
	
	public String talhaoForm() {
		facesContext.getExternalContext().getSessionMap().put("zona", zona);
    	return "/faces/public/talhao/talhaoLista.xhtml";
    }
	
	public String voltarFazenda() {
		return "/faces/public/fazenda/fazenda";
	}
	public MapModel getPolygonModel() {
        polygonModel = new DefaultMapModel();
		
		if(this.zona!=null &&this.zona.getId()!= null){
			
			String[] locale = zona.getZona().split(" ");
			if(locale!=null && locale.length>1){
				double lat = Double.parseDouble(zona.getZona().split(" ")[0]);
				double lon = Double.parseDouble(zona.getZona().split(" ")[1]);
				double dif1 = 0.002000;
				double dif2 = 0.003000;
				
				/*LatLng coord1 = new LatLng(-23.563556, -46.633307);
				 *                             0.020000,   0.300000
		          LatLng coord4 = new LatLng(-23.583556, -46.933307);*/
		        
		        LatLng coord3 = new LatLng(lat+0.000784, lon+0.000378);
		        LatLng coord2 = new LatLng(lat, lon);
		        LatLng coord1 = new LatLng(lat+0.000735, lon+0.001242);
		        
		        System.out.println((lat+0.000784) + " * " + (lon+0.000378));
		        System.out.println((lat+0.000735) + " * " + (lon+0.001242));
		        
		        
		        
		        //23.563556,-46.633307
	
		        //Polygon
		        Polygon polygon = new Polygon();
		        polygon.getPaths().add(coord1);
		        polygon.getPaths().add(coord2);
		        polygon.getPaths().add(coord3);
		  
		        polygon.setStrokeColor("#FF9900");
		        polygon.setFillColor("#FF9900");
		        polygon.setStrokeOpacity(0.7);
		        polygon.setFillOpacity(0.7);
		          
		        polygonModel.addOverlay(polygon);
			}
		}
		return polygonModel;
    }
	
	public void onCircleSelect(OverlaySelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Circle Selected", null));
    }
	
	public void onPolygonSelect(OverlaySelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Polygon Selected", null));
    }
}