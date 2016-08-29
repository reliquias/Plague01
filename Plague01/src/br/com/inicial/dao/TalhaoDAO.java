package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Talhao;

public class TalhaoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Talhao objeto) {
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

	public void atualizar(Talhao objeto) {
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

	public void excluir(Talhao objeto) {
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

	public Talhao carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Talhao
		// no parametro, tem que ser diretamente a chave (integer)
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (Talhao) this.session.get(Talhao.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Talhao> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Talhao.class).list();
	}
	
	public List<Talhao> buscarListaPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Talhao c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Talhao>) consulta.list();
	}
}
