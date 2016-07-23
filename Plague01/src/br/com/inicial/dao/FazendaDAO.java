package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.inicial.modelo.Fazenda;

public class FazendaDAO {

	private Session	session;

	public void setSession(Session session) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session = session;
	}

	public void salvar(Fazenda fazenda) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.save(fazenda);		
	}

	public void atualizar(Fazenda fazenda) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.update(fazenda);
	}

	public void excluir(Fazenda fazenda) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		this.session.delete(fazenda);
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
