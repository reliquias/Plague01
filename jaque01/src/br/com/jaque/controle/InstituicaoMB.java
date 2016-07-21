package br.com.jaque.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.com.jaque.dao.DAOFactory;
import br.com.jaque.dao.InstituicaoDAO;
import br.com.jaque.modelo.Instituicao;
import br.com.jaque.util.JsfUtil;
import br.com.jaque.util.Perfil;
import br.com.jaque.util.Regiao;
import br.com.jaque.util.XLazyModel;

/**
 *
 * @author wReliquias
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@ManagedBean(name="instituicaoMB")
@RequestScoped
public class InstituicaoMB{

    private Instituicao instituicao =  new Instituicao();
    private InstituicaoDAO instituicaoDAO;
    private List<Object> instituicaos = new ArrayList<Object>();
    private XLazyModel instituicaosModel;
    
    
    public InstituicaoMB() {
    	this.instituicaoDAO = DAOFactory.criarInstituicaoDAO();
    	instituicaos = getListarTodas();
    	instituicaosModel = new XLazyModel(instituicaos);
    	if(instituicaosModel.getPageSize() == 0){
    		instituicaosModel.setPageSize(1);
    	}
    }

   public void adicionaOuAtualiza(ActionEvent actionEvent) {
        if (instituicao.getId() == 0) {
            try {
                instituicaoDAO.salvar(instituicao);
                JsfUtil.addSuccessMessage("Instituicao salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        } else {
            try {
                instituicaoDAO.atualizar(instituicao);
                JsfUtil.addSuccessMessage("Instituicao salvo com Sucesso");
            } catch (Exception e) {
            	JsfUtil.addErrorMessage(e, "Vixi!!!!");
            }
        }
    }

    public void excluirAjax(ActionEvent actionEvent) {
        instituicaoDAO.excluir(instituicao);
        instituicaosModel = null;
    }

    public String excluir() {
    	try {
			instituicaoDAO.excluir(instituicao);
			JsfUtil.addSuccessMessage("Instituicao excluida com Sucesso");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, "Vixi!!!!");
		}
    	return "ListInstituicao";
    }

    public String novo() {
        instituicao = new Instituicao();
        return "CadastrarInstituicao";
    }
    
    public String listar() {
    	instituicaosModel = null;
    	return "ListInstituicao";
    }

    public String preparaEditar() {
    	return "CadastrarInstituicao";
    }

    public List getListarTodas() {
        return instituicaoDAO.listar();
    }

    public Perfil[] getPerfil() {
        return Perfil.values();
    }

    public Regiao[] getRegiao() {
    	return Regiao.values();
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

   public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(instituicaoDAO.listar(), true);
    }

    public InstituicaoDAO getInstituicaoDAO() {
        return instituicaoDAO;
    }

    public void setInstituicaoDAO(InstituicaoDAO instituicaoDAO) {
        this.instituicaoDAO = instituicaoDAO;
    }

    public List getInstituicaos() {
        return instituicaos;
    }

    public void setInstituicaos(List instituicaos) {
        this.instituicaos = instituicaos;
    }
    
    public XLazyModel getInstituicaosModel() {
    	if(instituicaosModel == null){
    		instituicaos = getListarTodas();
        	instituicaosModel = new XLazyModel(instituicaos);
        	if(instituicaosModel.getPageSize() == 0){
        		instituicaosModel.setPageSize(1);
        	}
    	}
		return instituicaosModel;
	}

	public void setInstituicaosModel(XLazyModel instituicaosModel) {
		this.instituicaosModel = instituicaosModel;
	}
}
