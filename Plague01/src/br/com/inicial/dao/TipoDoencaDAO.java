package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.inicial.modelo.TipoDoenca;

public class TipoDoencaDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(TipoDoenca tipoDoenca) {
		this.session.save(tipoDoenca);		
	}

	public void atualizar(TipoDoenca tipoDoenca) {
		this.session.merge(tipoDoenca);
	}

	public void excluir(TipoDoenca tipoDoenca) {
		this.session.delete(tipoDoenca);
	}

	public TipoDoenca carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o TipoDoenca
		// no parametro, tem que ser diretamente a chave (integer)
		return (TipoDoenca) this.session.get(TipoDoenca.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<TipoDoenca> listar() {
		return this.session.createCriteria(TipoDoenca.class).list();
		
	}
	
	public List<TipoDoenca> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from TipoDoenca c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<TipoDoenca>) consulta.list();
	}
	
	public TipoDoenca buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from TipoDoenca c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (TipoDoenca) consulta.uniqueResult();
	}
}
