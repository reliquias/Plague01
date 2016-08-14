package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.inicial.modelo.Planta;

public class PlantaDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Planta planta) {
		this.session.save(planta);		
	}

	public void atualizar(Planta planta) {
		this.session.merge(planta);
	}

	public void excluir(Planta planta) {
		this.session.delete(planta);
	}

	public Planta carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Planta
		// no parametro, tem que ser diretamente a chave (integer)
		return (Planta) this.session.get(Planta.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Planta> listar() {
		return this.session.createCriteria(Planta.class).list();
		
	}
	
	public List<Planta> buscarListaPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Planta c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (List<Planta>) consulta.list();
	}
	
	public Planta buscarPorCampo(String campo, Object valor) {
		if(valor instanceof String){
			valor = "'" + valor + "'";
		}
		String hql = "select c from Planta c where c." + campo +" = " + valor;
		Query consulta = this.session.createQuery(hql);
//		consulta.setInteger("idFicha", idFicha);
		return (Planta) consulta.uniqueResult();
	}
}
