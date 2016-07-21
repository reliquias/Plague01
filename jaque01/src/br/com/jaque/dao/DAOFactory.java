package br.com.jaque.dao;

import org.hibernate.Session;


public class DAOFactory {
	
	 private final Session session;  
	   
	     public DAOFactory() {  
	         session = HibernateUtil.getSessionFactory().getCurrentSession();  
	     }  

	public static UsuarioDAO criarUsuarioDAO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return usuarioDAO;
	}

	public static InstituicaoDAO criarInstituicaoDAO() {
		InstituicaoDAO instituicaoDAO = new InstituicaoDAO();
		instituicaoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return instituicaoDAO;
	}

	public static ProdutoDAO criarProdutoDAO() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return produtoDAO;
	}

	public static DoacaoDAO criarDoacaoDAO() {
		DoacaoDAO doacaoDAO = new DoacaoDAO();
		doacaoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return doacaoDAO;
	}
	
	public static ItemDoacaoDAO criarItemDoacaoDAO() {
		ItemDoacaoDAO itemDoacaoDAO = new ItemDoacaoDAO();
		itemDoacaoDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return itemDoacaoDAO;
	}
	
	public Session getSession() {
		return session;
	}
}