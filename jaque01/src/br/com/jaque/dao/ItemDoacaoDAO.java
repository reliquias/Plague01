package br.com.jaque.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.jaque.modelo.ItemDoacao;

public class ItemDoacaoDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(ItemDoacao itemDoacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.save(itemDoacao);
	}
	
	public void salvarOutAtualizar(ItemDoacao itemDoacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.saveOrUpdate(itemDoacao);
	}

	public void atualizar(ItemDoacao itemDoacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.merge(itemDoacao);
	}

	public void excluir(ItemDoacao itemDoacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.delete(itemDoacao);
	}

	public ItemDoacao carregar(Integer id) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (ItemDoacao) this.session.get(ItemDoacao.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ItemDoacao> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(ItemDoacao.class).list();
	}
}
