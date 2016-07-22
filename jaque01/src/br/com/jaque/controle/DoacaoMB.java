package br.com.jaque.controle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

import br.com.jaque.dao.DAOFactory;
import br.com.jaque.dao.DoacaoDAO;
import br.com.jaque.modelo.Doacao;
import br.com.jaque.util.JsfUtil;
import br.com.jaque.util.Perfil;
import br.com.jaque.util.Regiao;
import br.com.jaque.util.XLazyModel;

/**
 *
 * @author wReliquias
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="doacaoMB")
@RequestScoped
public class DoacaoMB{

    private Doacao doacao =  new Doacao();
    private DoacaoDAO doacaoDAO;
    private List<Object> doacoes = new ArrayList<Object>();
    private XLazyModel doacoesModel;
    
    private MapModel circleModel;
    
    public DoacaoMB() {
    	this.doacaoDAO = DAOFactory.criarDoacaoDAO();
    	doacoes = getListarTodas();
    	doacoesModel = new XLazyModel(doacoes);
    	if(doacoesModel.getPageSize() == 0){
    		doacoesModel.setPageSize(1);
    	}
    	circleModel = new DefaultMapModel();
    	  
        //Shared coordinates
        LatLng coord1 = new LatLng(36.879466, 30.667648);
        LatLng coord4 = new LatLng(36.885233, 30.702323);
 
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
        circleModel.addOverlay(circle2);
    }

   public void adicionaOuAtualiza(ActionEvent actionEvent) {
        if (doacao.getId() == 0) {
            try {
                doacaoDAO.salvar(doacao);
                JsfUtil.addSuccessMessage("Doacao salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        } else {
            try {
                doacaoDAO.atualizar(doacao);
                JsfUtil.addSuccessMessage("Doacao salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        }
    }

    public void excluirAjax(ActionEvent actionEvent) {
        doacaoDAO.excluir(doacao);
        doacoesModel = null;
    }
    
    //fdp

    public String excluir() {
    	try {
			doacaoDAO.excluir(doacao);
			JsfUtil.addSuccessMessage("Doacao excluida com Sucesso");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, "Vixi!!!!");
		}
    	return "ListDoacao";
    }

    public String novo() {
        doacao = new Doacao();
        return "CadastrarDoacao";
    }
    
    public String listar() {
    	doacoesModel = null;
    	return "ListDoacao";
    }

    public String preparaEditar() {
    	return "CadastrarDoacao";
    }

    public List getListarTodas() {
        return doacaoDAO.listar();
    }

    public Perfil[] getPerfil() {
        return Perfil.values();
    }

    public Regiao[] getRegiao() {
    	return Regiao.values();
    }

    public Doacao getDoacao() {
        return doacao;
    }

   public void setDoacao(Doacao doacao) {
        this.doacao = doacao;
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(doacaoDAO.listar(), true);
    }

    public DoacaoDAO getDoacaoDAO() {
        return doacaoDAO;
    }

    public void setDoacaoDAO(DoacaoDAO doacaoDAO) {
        this.doacaoDAO = doacaoDAO;
    }

    public List getDoacaos() {
        return doacoes;
    }

    public void setDoacaos(List doacoes) {
        this.doacoes = doacoes;
    }
    
    public XLazyModel getDoacoesModel() {
    	if(doacoesModel == null){
    		doacoes = getListarTodas();
        	doacoesModel = new XLazyModel(doacoes);
        	if(doacoesModel.getPageSize() == 0){
        		doacoesModel.setPageSize(1);
        	}
    	}
		return doacoesModel;
	}
    
    public void setDoacoesModel(XLazyModel doacoesModel) {
		this.doacoesModel = doacoesModel;
	}
	
	public MapModel getCircleModel() {
		circleModel = new DefaultMapModel();
  	  
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
        circleModel.addOverlay(circle2);
        return circleModel;
    }
  
    public void onCircleSelect(OverlaySelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Circle Selected", null));
    }
}
