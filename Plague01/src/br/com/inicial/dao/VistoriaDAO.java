package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Vistoria;

public class VistoriaDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Vistoria objeto) {
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

	public void atualizar(Vistoria objeto) {
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

	public void excluir(Vistoria objeto) {
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

	public Vistoria carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Vistoria
		// no parametro, tem que ser diretamente a chave (integer)
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (Vistoria) this.session.get(Vistoria.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Vistoria> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Vistoria.class).list();
	}
	
	public List<Vistoria> buscarListaPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Vistoria c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Vistoria>) consulta.list();
	}
}
