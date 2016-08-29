package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.modelo.Usuario;

public class UsuarioDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Usuario objeto) {
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

	public void atualizar(Usuario objeto) {
		/*if(usuario.getPermissao() == null || usuario.getPermissao().size() == 0){
			Usuario usuarioPermissao = this.carregar(usuario.getId());
			usuario.setPermissao(usuarioPermissao.getPermissao());
			this.session.evict(usuarioPermissao);
		}*/
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

	public void excluir(Usuario objeto) {
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

	public Usuario carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Usuario
		// no parametro, tem que ser diretamente a chave (integer)
		return (Usuario) this.session.get(Usuario.class, codigo);
	}

	public Usuario buscarPorLogin(String login) {
		if(!session.isConnected()){
			session = HibernateUtil.getSessionFactory().getCurrentSession();
		}
		String hql = "select u from Usuario u where u.login = :login";
		Query consulta = this.session.createQuery(hql);
		consulta.setString("login", login);

		//TODO mostrar primeiramente com o list e depois apresentar o uniqueResult
		return (Usuario) consulta.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		return this.session.createCriteria(Usuario.class).list();
		
	}
}
