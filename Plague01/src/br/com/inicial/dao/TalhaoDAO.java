package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import br.com.inicial.modelo.Talhao;

public class TalhaoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Talhao talhao) {
		this.session.save(talhao);		
	}

	public void atualizar(Talhao talhao) {
		this.session.update(talhao);
	}

	public void excluir(Talhao talhao) {
		this.session.delete(talhao);
	}

	public Talhao carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Talhao
		// no parametro, tem que ser diretamente a chave (integer)
		return (Talhao) this.session.get(Talhao.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Talhao> listar() {
		return this.session.createCriteria(Talhao.class).list();
	}
	
	public List<Talhao> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Talhao c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Talhao>) consulta.list();
	}
}
