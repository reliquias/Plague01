package br.com.jaque.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.jaque.modelo.Produto;

public class ProdutoDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(Produto produto) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.save(produto);
	}
	
	public void salvarOutAtualizar(Produto produto) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.saveOrUpdate(produto);
	}

	public void atualizar(Produto produto) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.merge(produto);
	}

	public void excluir(Produto produto) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.delete(produto);
	}

	public Produto carregar(Integer id) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (Produto) this.session.get(Produto.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Produto> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Produto.class).list();
	}
}
