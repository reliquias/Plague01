package br.com.jaque.controle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.jaque.dao.DAOFactory;
import br.com.jaque.dao.ItemDoacaoDAO;
import br.com.jaque.modelo.Doacao;
import br.com.jaque.modelo.ItemDoacao;
import br.com.jaque.util.JsfUtil;
import br.com.jaque.util.XLazyModel;

/**
 *
 * @author Windows Vista
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean
@RequestScoped
public class ItemDoacaoMB{

    private ItemDoacao consumoDoacao =  new ItemDoacao();
    private ItemDoacaoDAO consumoDoacaoDAO;
    private List<Object> itemDoacoes = new ArrayList<Object>();
    private XLazyModel itemDoacoesModel;
//    @ManagedProperty(value="#{doacaoMB}")
    private Doacao doacao;
    FacesContext facesContext;
    private Integer idDoacao;
    
    public ItemDoacaoMB() {
    	this.consumoDoacaoDAO = DAOFactory.criarItemDoacaoDAO();
    	facesContext = FacesContext.getCurrentInstance();
    	doacao =  (Doacao) facesContext.getExternalContext().getSessionMap().get("doacao");
    	if(doacao == null || doacao.getId() == null){
//    		doacao = DAOFactory.criarDoacaoDAO().carregar(DoacaoMB.idDoacao);
    	}
    	itemDoacoesModel = null;
    }

	public void adicionaOuAtualiza(ActionEvent actionEvent) {
        if (consumoDoacao.getId() == 0) {
            try {
            	consumoDoacao.setDoacao(this.doacao);
                consumoDoacaoDAO.salvar(consumoDoacao);
                consumoDoacao = new ItemDoacao();
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        } else {
            try {
            	consumoDoacaoDAO.atualizar(consumoDoacao);
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        }
        itemDoacoesModel = null;
    }

    public void excluir(ActionEvent actionEvent) {
        consumoDoacaoDAO.excluir(consumoDoacao);
        itemDoacoesModel = null;
    }

    public String listar() {
    	return "ListItemDoacao";
    }
    
    public void preparaEditar(ActionEvent actionEvent) {
    }

    public ItemDoacao getItemDoacao() {
        return consumoDoacao;
    }

   public void setItemDoacao(ItemDoacao consumoDoacao) {
        this.consumoDoacao = consumoDoacao;
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(consumoDoacaoDAO.listar(), true);
    }

    public ItemDoacaoDAO getItemDoacaoDAO() {
        return consumoDoacaoDAO;
    }

    public void setItemDoacaoDAO(ItemDoacaoDAO consumoDoacaoDAO) {
        this.consumoDoacaoDAO = consumoDoacaoDAO;
    }

/*    public Doacao getDoacao() {
    	doacao =  (Doacao) facesContext.getExternalContext().getSessionMap().get("doacao");
		return doacao;
	}*/

    public void setItemDoacoesModel(XLazyModel itemDoacoesModel) {
		this.itemDoacoesModel = itemDoacoesModel;
	}

	public Integer getIdDoacao() {
		if(idDoacao == null){
			idDoacao = doacao.getId();
		}
		return idDoacao;
	}

	public void setIdDoacao(Integer idDoacao) {
		this.idDoacao = idDoacao;
	}
	public String doacaoForm() {
    	return "/faces/restrito/doacao/ListDoacao";
    }
}
