package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Cidade;

public class CidadeDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Cidade objeto) {
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

	public void atualizar(Cidade objeto) {
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

	public void excluir(Cidade objeto) {
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

	public Cidade carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Cidade
		// no parametro, tem que ser diretamente a chave (integer)
		return (Cidade) this.session.get(Cidade.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(Cidade.class).list();
		
	}
	
	public Cidade buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Cidade c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
		return (Cidade) consulta.uniqueResult();
	}
	
	public List<Cidade> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Cidade c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
		return (List<Cidade>) consulta.list();
	}
}
