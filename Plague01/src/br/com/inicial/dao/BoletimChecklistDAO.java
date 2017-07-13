package br.com.inicial.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.BoletimApontamento;
import br.com.inicial.modelo.BoletimChecklist;

public class BoletimChecklistDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(BoletimChecklist objeto) {
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

	public void atualizar(BoletimChecklist objeto) {
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

	public void excluir(BoletimChecklist objeto) {
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

	public BoletimChecklist carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o BoletimChecklist
		// no parametro, tem que ser diretamente a chave (integer)
		return (BoletimChecklist) this.session.get(BoletimChecklist.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<BoletimChecklist> listar() {
		List<BoletimChecklist> lista = new ArrayList<BoletimChecklist>();
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    lista = this.session.createCriteria(BoletimChecklist.class).list();
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		return lista;
	}
	
	public List<BoletimChecklist> buscarListaPorCampo(String campo, Object valor) {
		List<BoletimChecklist> lista = new ArrayList<BoletimChecklist>();
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimChecklist c where c." + campo +" = " + valor;
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		Transaction tx = null;
		try {
		    tx = session.beginTransaction();
		    Query consulta =this.session.createQuery(hql);
			lista  = (List<BoletimChecklist>) consulta.list();
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
		return lista;
	}
	
	public BoletimChecklist buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimChecklist c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (BoletimChecklist) consulta.uniqueResult();
	}
	
	public List<BoletimChecklist> buscarListaPorCampos(String[] campo, Object[] valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		
		String hql = "select c from BoletimChecklist c where ";
				
		for (int nCampos = 0; nCampos < campo.length; nCampos++) {
			Object v = valor[nCampos];
			if(valor[nCampos] instanceof String){
				v = "'" + v + "'";
			};
			hql = hql + "c." + campo[nCampos] +" = " + v + " and ";
		}
		
		Query consulta = this.session.createQuery(hql);
		return (List<BoletimChecklist>) consulta.list();
	}

	public BoletimChecklist buscarPorCampos(String[] campo, Object[] valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		
		String hql = "select c from BoletimChecklist c where ";
		
		for (int nCampos = 0; nCampos < campo.length; nCampos++) {
			Object v = valor[nCampos];
			if(valor[nCampos] instanceof String){
				v = "'" + v + "'";
			};
			hql = hql + "c." + campo[nCampos] +" = " + v + " and ";	
		}
		
		Query consulta = this.session.createQuery(hql.substring(0, hql.length()-5));
		return (BoletimChecklist) consulta.uniqueResult();
	}
	
	public BoletimChecklist buscarPorPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimChecklist c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (BoletimChecklist) consulta.uniqueResult();
	}
}
