package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.inicial.modelo.Estado;

public class EstadoDAO {

	private Session	session;

	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Estado estado) {
		this.session.save(estado);		
	}

	public void atualizar(Estado estado) {
		this.session.update(estado);
	}

	public void excluir(Estado estado) {
		this.session.delete(estado);
	}

	public Estado carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Estado
		// no parametro, tem que ser diretamente a chave (integer)
		return (Estado) this.session.get(Estado.class, codigo);
	}

	@SuppressWarnings("unchecked")
	public List<Estado> listar() {
		return this.session.createCriteria(Estado.class).list();
		
	}
}
