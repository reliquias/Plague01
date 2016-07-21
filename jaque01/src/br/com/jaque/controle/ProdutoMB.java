package br.com.jaque.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.jaque.dao.DAOFactory;
import br.com.jaque.dao.ProdutoDAO;
import br.com.jaque.modelo.Produto;
import br.com.jaque.util.JsfUtil;
import br.com.jaque.util.Perfil;
import br.com.jaque.util.Regiao;
import br.com.jaque.util.XLazyModel;

/**
 *
 * @author wReliquias
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="produtoMB")
@RequestScoped
public class ProdutoMB{

    private Produto produto =  new Produto();
    private ProdutoDAO produtoDAO;
    private List<Object> produtos = new ArrayList<Object>();
    private XLazyModel produtosModel;
    
    
    public ProdutoMB() {
    	this.produtoDAO = DAOFactory.criarProdutoDAO();
    	produtos = getListarTodas();
    	produtosModel = new XLazyModel(produtos);
    	if(produtosModel.getPageSize() == 0){
    		produtosModel.setPageSize(1);
    	}
    }

   public void adicionaOuAtualiza(ActionEvent actionEvent) {
        if (produto.getId() == 0) {
            try {
                produtoDAO.salvar(produto);
                JsfUtil.addSuccessMessage("Produto salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        } else {
            try {
                produtoDAO.atualizar(produto);
                JsfUtil.addSuccessMessage("Produto salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        }
    }

    public void excluirAjax(ActionEvent actionEvent) {
        produtoDAO.excluir(produto);
        produtosModel = null;
    }

    public String excluir() {
    	try {
			produtoDAO.excluir(produto);
			JsfUtil.addSuccessMessage("Produto excluida com Sucesso");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, "Vixi!!!!");
		}
    	return "ListProduto";
    }

    public String novo() {
        produto = new Produto();
        return "CadastrarProduto";
    }
    
    public String listar() {
    	produtosModel = null;
    	return "ListProduto";
    }

    public String preparaEditar() {
    	return "CadastrarProduto";
    }

    public List getListarTodas() {
        return produtoDAO.listar();
    }

    public Perfil[] getPerfil() {
        return Perfil.values();
    }

    public Regiao[] getRegiao() {
    	return Regiao.values();
    }

    public Produto getProduto() {
        return produto;
    }

   public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(produtoDAO.listar(), true);
    }

    public ProdutoDAO getProdutoDAO() {
        return produtoDAO;
    }

    public void setProdutoDAO(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public List getProdutos() {
        return produtos;
    }

    public void setProdutos(List produtos) {
        this.produtos = produtos;
    }
    
    public XLazyModel getProdutosModel() {
    	if(produtosModel == null){
    		produtos = getListarTodas();
        	produtosModel = new XLazyModel(produtos);
        	if(produtosModel.getPageSize() == 0){
        		produtosModel.setPageSize(1);
        	}
    	}
		return produtosModel;
	}

	public void setProdutosModel(XLazyModel produtosModel) {
		this.produtosModel = produtosModel;
	}
}
