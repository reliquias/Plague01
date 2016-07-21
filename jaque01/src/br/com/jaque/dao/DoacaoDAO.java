package br.com.jaque.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.jaque.modelo.Doacao;

public class DoacaoDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(Doacao doacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.save(doacao);
	}
	
	public void salvarOutAtualizar(Doacao doacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.saveOrUpdate(doacao);
	}

	public void atualizar(Doacao doacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.merge(doacao);
	}

	public void excluir(Doacao doacao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.delete(doacao);
	}

	public Doacao carregar(Integer id) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (Doacao) this.session.get(Doacao.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Doacao> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Doacao.class).list();
	}
}
