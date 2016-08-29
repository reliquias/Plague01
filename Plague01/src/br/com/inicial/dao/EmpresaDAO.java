package br.com.inicial.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.inicial.modelo.Empresa;

public class EmpresaDAO {

	private Session	session;

	public EmpresaDAO(Session session) {
		this.session = session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Empresa empresa) {
		this.session.save(empresa);		
	}

	public void excluir(Empresa empresa) {
		this.session.delete(empresa);
	}

	public Empresa carregar(Integer codigo) {
		//TODO o hibernate nao conseguira fazer a carga caso seja passado o Empresa
		// no parametro, tem que ser diretamente a chave (integer)
		return (Empresa) this.session.get(Empresa.class, codigo);
	}

	public Empresa buscarPorCnpj(String cnpj) {
		String hql = "select u from Empresa u where u.cnpj = :cnpj";
		Query consulta = this.session.createQuery(hql);
		consulta.setString("cnpj", cnpj);
		return (Empresa) consulta.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Empresa> listar() {
		return this.session.createCriteria(Empresa.class).list();
		
	}
}
