package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.inicial.modelo.Pais;

public class PaisDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Pais pais) {
		this.session.save(pais);		
	}

	public void atualizar(Pais pais) {
		this.session.update(pais);
	}

	public void excluir(Pais pais) {
		this.session.delete(pais);
	}

	public Pais carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Pais
		// no parametro, tem que ser diretamente a chave (integer)
		return (Pais) this.session.get(Pais.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Pais> listar() {
		return this.session.createCriteria(Pais.class).list();
		
	}
	
	public List<Pais> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Pais c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Pais>) consulta.list();
	}
	
	public Pais buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Pais c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (Pais) consulta.uniqueResult();
	}
}
