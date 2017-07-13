package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.BoletimDiario;

public class BoletimDiarioDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(BoletimDiario objeto) {
		Transaction tx = null;
		try {
			if(!session.isOpen()){
				session = HibernateUtil.getSessionFactory().getCurrentSession();
			}
		    tx = session.beginTransaction();
		    this.session.save(objeto);
		    tx.commit();
		}
		catch (Exception e) {
		    if (tx != null) tx.rollback();
		    throw e;
		}
/*		finally {
		    session.close();
		}*/		
	}
	
	public void flush(){
		if(!session.isOpen()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		session.flush();
	}
	
	public void evict(BoletimDiario objeto){
		if(!session.isOpen()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		session.evict(objeto);
	}
	
	public void saveOrUpdate(BoletimDiario objeto) {
		Transaction tx = null;
		try {
			if(!session.isOpen()){
				session = HibernateUtil.getSessionFactory().getCurrentSession();
			}
			tx = session.beginTransaction();
			this.session.saveOrUpdate(objeto);
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
	}

	public void atualizar(BoletimDiario objeto) {
		Transaction tx = null;
		try {
			if(!session.isConnected()){
				session = HibernateUtil.getSessionFactory().getCurrentSession();
			}
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

	public void excluir(BoletimDiario objeto) {
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

	public BoletimDiario carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o BoletimDiario
		// no parametro, tem que ser diretamente a chave (integer)
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (BoletimDiario) this.session.get(BoletimDiario.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<BoletimDiario> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(BoletimDiario.class).list();
	}
	
	public List<BoletimDiario> buscarListaPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimDiario c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<BoletimDiario>) consulta.list();
	}

	public BoletimDiario buscarPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimDiario c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (BoletimDiario) consulta.uniqueResult();
	}
	
	public BoletimDiario buscarBoletimDoisCampo(String campo, Object valor, String campo2, Object valor2) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimDiario c where c." + campo +" = " + valor + " and " + "c."+campo2+" = "+valor2;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (BoletimDiario) consulta.uniqueResult();
	}
	
	public void deletarBoletimDiario(Integer idFazenda){
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		String hql = "delete from BoletimDiario c where c.fazenda.id= " + idFazenda;
		this.session.createQuery(hql).executeUpdate();
	}
	
	
}
