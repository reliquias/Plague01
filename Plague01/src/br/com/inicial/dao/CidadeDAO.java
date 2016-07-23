package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.inicial.modelo.Cidade;
import br.com.inicial.modelo.Estado;

public class CidadeDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Cidade cidade) {
		this.session.save(cidade);		
	}

	public void atualizar(Cidade cidade) {
		this.session.update(cidade);
	}

	public void excluir(Cidade cidade) {
		this.session.delete(cidade);
	}

	public Cidade carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Cidade
		// no parametro, tem que ser diretamente a chave (integer)
		return (Cidade) this.session.get(Cidade.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> listar() {
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
