package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Estado;

public class EstadoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Estado objeto) {
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

	public void atualizar(Estado objeto) {
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

	public void excluir(Estado objeto) {
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

	public Estado carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Estado
		// no parametro, tem que ser diretamente a chave (integer)
		return (Estado) this.session.get(Estado.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Estado> listar() {
		return this.session.createCriteria(Estado.class).list();
		
	}
	
	public Estado buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Estado c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
		return (Estado) consulta.uniqueResult();
	}
	
	public List<Estado> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Estado c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
		return (List<Estado>) consulta.list();
	}
}
