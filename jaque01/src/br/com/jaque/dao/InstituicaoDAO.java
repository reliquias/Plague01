package br.com.jaque.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.jaque.modelo.Instituicao;

public class InstituicaoDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(Instituicao instituicao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.save(instituicao);
	}
	
	public void salvarOutAtualizar(Instituicao instituicao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.saveOrUpdate(instituicao);
	}

	public void atualizar(Instituicao instituicao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.merge(instituicao);
	}

	public void excluir(Instituicao instituicao) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.delete(instituicao);
	}

	public Instituicao carregar(Integer id) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (Instituicao) this.session.get(Instituicao.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Instituicao> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Instituicao.class).list();
	}
}
