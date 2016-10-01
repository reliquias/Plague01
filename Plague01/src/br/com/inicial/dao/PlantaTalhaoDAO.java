package br.com.inicial.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.PlantaTalhao;

public class PlantaTalhaoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(PlantaTalhao objeto) {
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

	public void atualizar(PlantaTalhao objeto) {
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

	public void excluir(PlantaTalhao objeto) {
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

	public PlantaTalhao carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o PlantaTalhao
		// no parametro, tem que ser diretamente a chave (integer)
		return (PlantaTalhao) this.session.get(PlantaTalhao.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<PlantaTalhao> listar() {
		List<PlantaTalhao> lista = new ArrayList<PlantaTalhao>();
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    lista = this.session.createCriteria(PlantaTalhao.class).list();
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		return lista;
	}
	
	public List<PlantaTalhao> buscarListaPorCampo(String campo, Object valor) {
		List<PlantaTalhao> lista = new ArrayList<PlantaTalhao>();
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from PlantaTalhao c where c." + campo +" = " + valor;
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    Query consulta =this.session.createQuery(hql);
			lista  = (List<PlantaTalhao>) consulta.list();
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		return lista;
	}
	
	public PlantaTalhao buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from PlantaTalhao c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (PlantaTalhao) consulta.uniqueResult();
	}
}
