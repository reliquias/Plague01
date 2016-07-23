package br.com.inicial.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.inicial.dao.DAOFactory;
import br.com.inicial.dao.HibernateUtil;
import br.com.inicial.dao.PaisDAO;
import br.com.inicial.modelo.Pais;

@WebService
public class PragaWS {
	
	/*@WebMethod
	public String adicionarFazenda (@WebParam(name="fazenda") Fazenda fazenda){
		FazendaMB fazendaMb = new FazendaMB();
		fazendaMb.setFazenda(fazenda);
		fazendaMb.adicionaOuAtualiza();
		return "ok";
	}
	
	@WebMethod
	public String adicionarPais (@WebParam(name="pais") Pais pais){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		DAOFactory daof = new DAOFactory(session);
		PaisDAO paisDAO = daof.criarPaisDAO();
//		paisDAO.setSession(HibernateUtil.getSessionFactory().getCurrentSession());
		session.beginTransaction();
		paisDAO.salvar(pais);
		session.getTransaction().commit();
		return "ok";
	}*/
	
	@WebMethod
	public String digaOla (@WebParam(name="nome") String nome){
		String ola = "Ola " + nome;
		return ola;
	}
	
	
	@WebMethod(operationName="qualPais")
	public String qualPais(@WebParam(name="sigla") String sigla){
		return buscarPais(sigla);
	}
	
	private String buscar(String sigla) {
		String hql = "select c from Pais c where c.sigla= " + sigla;
		Session ss = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = ss.beginTransaction();
		String pais = "Vazio";
		try {
			// ss.save(pais);
			pais = (String) ss.createQuery(hql).uniqueResult();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			return e.getLocalizedMessage().toString();
		}
		
		return pais;
	}
	
	private String buscarPais(String sigla){
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			DAOFactory daof = new DAOFactory(session);
			PaisDAO paisDAO = daof.criarPaisDAO();
//			session.beginTransaction();
			System.out.println("Passou");
			Pais pais = paisDAO.buscarPorCampo("sigla", sigla);
//			session.getTransaction().commit();
			if(pais != null && pais.getId()!=null){
				return "Pais: " + pais.getSigla() + " descricao: " + pais.getDescricao();
			}else{
				return "Esse pais não existe";
			}
		} catch (HibernateException e) {
			System.out.println(e.getLocalizedMessage().toString());
			return e.getLocalizedMessage().toString();
		}
		
		
	}
	

}
