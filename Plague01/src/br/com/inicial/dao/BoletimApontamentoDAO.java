package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.Transaction;

import br.com.inicial.modelo.BoletimApontamento;

public class BoletimApontamentoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(BoletimApontamento objeto) {
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
		finally {
		    session.close();
		}		
	}

	public void atualizar(BoletimApontamento objeto) {
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

	public void excluir(BoletimApontamento objeto) {
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

	public BoletimApontamento carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o BoletimApontamento
		// no parametro, tem que ser diretamente a chave (integer)
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return (BoletimApontamento) this.session.get(BoletimApontamento.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<BoletimApontamento> listar() {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		return this.session.createCriteria(BoletimApontamento.class).list();
	}
	
	public List<BoletimApontamento> buscarListaPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimApontamento c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<BoletimApontamento>) consulta.list();
	}

	public BoletimApontamento buscarPorPorCampo(String campo, Object valor) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from BoletimApontamento c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (BoletimApontamento) consulta.uniqueResult();
	}
	
	public void deletarBoletimApontamento(Integer idBoletimDiario){
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		String hql = "delete from BoletimApontamento c where c.boletimDiario.id= " + idBoletimDiario;
		this.session.createQuery(hql).executeUpdate();
	}
}
