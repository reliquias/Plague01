package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Fazenda;

public class FazendaDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(Fazenda objeto) {
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    this.session.save(objeto);
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		finally {
		    session.close();
		}		
	}

	public void atualizar(Fazenda objeto) {
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    this.session.merge(objeto);
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		finally {
		    session.close();
		}
	}

	public void excluir(Fazenda objeto) {
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    this.session.delete(objeto);
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
	}

	public Fazenda carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Fazenda
		// no parametro, tem que ser diretamente a chave (integer)
		return (Fazenda) this.session.get(Fazenda.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Fazenda> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Fazenda.class).list();
		
	}
}
