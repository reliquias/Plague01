package br.com.inicial.dao;

import javax.faces.context.FacesContext;

import org.hibernate.Session;

import br.com.inicial.modelo.Empresa;


public class DAOFactory {
	
	 private final Session session;  
	   
	public DAOFactory() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
//		session = HibernateUtil.getSession();
	}  
	
	public DAOFactory(Session session) {
        this.session = session;
    }

	public static UsuarioDAO criarUsuarioDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.setSession(HibernateUtil.getSession(empresa));
		return usuarioDAO;
	}

	public UsuarioDAO getUsuarioDAO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.setSession(this.session);
		return usuarioDAO;
	}
	
	public static FazendaDAO criarFazendaDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		FazendaDAO fazendaDAO = new FazendaDAO();
		fazendaDAO.setSession(HibernateUtil.getSession(empresa));
		return fazendaDAO;
	}
	
	public PaisDAO getPaisDAO() {
		PaisDAO paisDAO = new PaisDAO();
		paisDAO.setSession(this.session);
		return paisDAO;
	}
	
	public static PaisDAO criarPaisDAO() {
		PaisDAO paisDAO = new PaisDAO();
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		paisDAO.setSession(HibernateUtil.getSession(empresa));
		return paisDAO;
	}
	
	public static EstadoDAO criarEstadoDAO() {
		EstadoDAO estadoDAO = new EstadoDAO();
//		estadoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		estadoDAO.setSession(HibernateUtil.getSession(empresa));
		return estadoDAO;
	}
	
	public static CidadeDAO criarCidadeDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		CidadeDAO cidadeDAO = new CidadeDAO();
		cidadeDAO.setSession(HibernateUtil.getSession(empresa));
		return cidadeDAO;
	}
	
	public static ZonaDAO criarZonaDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		ZonaDAO zonaDAO = new ZonaDAO();
		zonaDAO.setSession(HibernateUtil.getSession(empresa));
		return zonaDAO;
	}

	public static TalhaoDAO criarTalhaoDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		TalhaoDAO talhaoDAO = new TalhaoDAO();
		talhaoDAO.setSession(HibernateUtil.getSession(empresa));
		return talhaoDAO;
	}
	
	public static TipoDoencaDAO criarTipoDoencaDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		TipoDoencaDAO tipoDoencaDAO = new TipoDoencaDAO();
		tipoDoencaDAO.setSession(HibernateUtil.getSession(empresa));
		return tipoDoencaDAO;
	}

	public static PlantaDAO criarPlantaDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		PlantaDAO plantaDAO = new PlantaDAO();
		plantaDAO.setSession(HibernateUtil.getSession(empresa));
		return plantaDAO;
	}

	public static PlantaTalhaoDAO criarPlantaTalhaoDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		PlantaTalhaoDAO plantaTalhaoDAO = new PlantaTalhaoDAO();
		plantaTalhaoDAO.setSession(HibernateUtil.getSession(empresa));
		return plantaTalhaoDAO;
	}

	public static VistoriaDAO criarVistoriaDAO() {
		Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
		VistoriaDAO vistoriaDAO = new VistoriaDAO();
		vistoriaDAO.setSession(HibernateUtil.getSession(empresa));
		return vistoriaDAO;
	}


	public Session getSession() {
		return session;
	}
	
	public EmpresaDAO getEmpresaDao() {
        return new EmpresaDAO(this.session);
    }
}