package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import br.com.inicial.modelo.Zona;

public class ZonaDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Zona zona) {
		this.session.save(zona);		
	}

	public void atualizar(Zona zona) {
		this.session.merge(zona);
	}

	public void excluir(Zona zona) {
		this.session.delete(zona);
	}

	public Zona carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Zona
		// no parametro, tem que ser diretamente a chave (integer)
		return (Zona) this.session.get(Zona.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Zona> listar() {
		return this.session.createCriteria(Zona.class).list();
	}
	
	public List<Zona> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Zona c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Zona>) consulta.list();
	}
}
