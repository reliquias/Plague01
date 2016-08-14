package br.com.inicial.dao;

import org.hibernate.Session;


public class DAOFactory {
	
	 private final Session session;  
	   
	public DAOFactory() {
		session = HibernateUtil.getSessionFactory().getCurrentSession();
	}  
	
	public DAOFactory(Session session) {
        this.session = session;
    }

	public static UsuarioDAO criarUsuarioDAO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return usuarioDAO;
	}
	
	public static FazendaDAO criarFazendaDAO() {
		FazendaDAO fazendaDAO = new FazendaDAO();
		fazendaDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return fazendaDAO;
	}
	
	public static PaisDAO criarPaisDAO() {
		PaisDAO paisDAO = new PaisDAO();
		paisDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return paisDAO;
	}
	
	public static EstadoDAO criarEstadoDAO() {
		EstadoDAO estadoDAO = new EstadoDAO();
		estadoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return estadoDAO;
	}
	
	public static CidadeDAO criarCidadeDAO() {
		CidadeDAO cidadeDAO = new CidadeDAO();
		cidadeDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return cidadeDAO;
	}
	
	public static ZonaDAO criarZonaDAO() {
		ZonaDAO zonaDAO = new ZonaDAO();
		zonaDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return zonaDAO;
	}

	public static TalhaoDAO criarTalhaoDAO() {
		TalhaoDAO talhaoDAO = new TalhaoDAO();
		talhaoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return talhaoDAO;
	}
	
	public static TipoDoencaDAO criarTipoDoencaDAO() {
		TipoDoencaDAO tipoDoencaDAO = new TipoDoencaDAO();
		tipoDoencaDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return tipoDoencaDAO;
	}

	public static PlantaDAO criarPlantaDAO() {
		PlantaDAO plantaDAO = new PlantaDAO();
		plantaDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return plantaDAO;
	}


	public Session getSession() {
		return session;
	}
}