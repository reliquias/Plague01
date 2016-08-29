package br.com.inicial.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import br.com.inicial.modelo.Empresa;

@SuppressWarnings("deprecation")
public class HibernateUtil {

	private static SessionFactory	sessionFactory;
//	private static SessionFactory	sessionFactory;
	private static Map sessionFactoryMap =null;
	private static Configuration cfg = null;
	
	/*private static final SessionFactory	sessionFactory;
	private static final Map sessionFactoryMap;
	private static final Configuration cfg;*/
	
	static {
        cfg = new AnnotationConfiguration();
        cfg.configure("hibernate.cfg.EMPRESAS.xml");
        
        sessionFactory = cfg.buildSessionFactory();
        sessionFactoryMap = new HashMap();
    }
	

	private static SessionFactory buildSessionFactory() {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure("hibernate.cfg.EMPRESAS.xml");
			return cfg.buildSessionFactory();
		} catch (Throwable e) {
			System.out.println("Criação inicial do objeto SessionFactory falhou. Erro: " + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
     * Cria novo session factory baseado na empresa passada como argumento
     * @param empresa
     * @return
     */
    private static SessionFactory createSessionFactory( Empresa empresa ) {
        Configuration cfg = new AnnotationConfiguration();
        cfg.configure( empresa.getConfigFileName().trim() );
     
        if(empresa.getConfigFileName().trim().equalsIgnoreCase("hibernate.cfg.Default.xml")){
	        try {
				String sqlDialect = empresa.getSqlDialect();
				String URLConexao = empresa.getURLConexao();
				String driver_class	=	empresa.getDriverClass();
				String username	=	empresa.getUsername();
				String password	=	empresa.getPassword();

				cfg.setProperty("hibernate.dialect", sqlDialect);
				cfg.setProperty("hibernate.connection.url", URLConexao);
				cfg.setProperty("hibernate.connection.driver_class", driver_class);
				cfg.setProperty("hibernate.connection.username", username);
				cfg.setProperty("hibernate.connection.password", password);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
//        sessionFactory = cfg.buildSessionFactory();
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        sessionFactoryMap.put( empresa.getCnpj() , sessionFactory);
        return sessionFactory;
    }
    
    public static Session getSession(Empresa empresa) {
        SessionFactory _sessionFactory = getSessionFactory( empresa );
        //System.out.println("HibernateUtil-> Abrindo factory: " + sessionFactory); Aqui
        return _sessionFactory.openSession();
    }
    
    public static void closeFactory() {
        sessionFactory.close();

        Set sessionSet = sessionFactoryMap.entrySet();
        for (Iterator it = sessionSet.iterator(); it.hasNext();) {
            SessionFactory sf = (SessionFactory)it.next();
            sf.close();
        }
    }
    
    
    public static SessionFactory getSessionFactory( Empresa empresa ) {
        //recupera factory padrao que conecta no banco de empresas
        if( empresa == null || empresa.getCnpj() == null ) {
            return sessionFactory;
        }

        Object empresaSessionFactory = sessionFactoryMap.get( empresa.getCnpj() );
        if( empresaSessionFactory == null ) { //Ainda não existe factory para essa empresa
            empresaSessionFactory = createSessionFactory( empresa );
        }
        return (SessionFactory) empresaSessionFactory;
    }
    
    public static AnnotationConfiguration createHibernateXML(Empresa empresa) {
    	AnnotationConfiguration cfg = new AnnotationConfiguration();
    	cfg.configure( empresa.getConfigFileName().trim() );
    
    	if(empresa.getConfigFileName().trim().equalsIgnoreCase("hibernate.cfg.Default.xml")){
    		try {
    			String sqlDialect = empresa.getSqlDialect();
    			String URLConexao = empresa.getURLConexao();
    			String driver_class	=	empresa.getDriverClass();
    			String username	=	empresa.getUsername();
    			String password	=	empresa.getPassword();
    			
    			cfg.setProperty("hibernate.dialect", sqlDialect);
    			cfg.setProperty("hibernate.connection.url", URLConexao);
    			cfg.setProperty("hibernate.connection.driver_class", driver_class);
    			cfg.setProperty("hibernate.connection.username", username);
    			cfg.setProperty("hibernate.connection.password", password);
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	return cfg;
    }

	public static SessionFactory getSessionFactory() {
		/*if(sessionFactory == null){
			return sessionFactory;
		}*/
		if(FacesContext.getCurrentInstance()!=null ){
			Empresa empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
			if(empresa != null){
//				sessionFactory = null;
				return createSessionFactory(empresa);
			}
		}
		return sessionFactory;
	}
	
	public static Session getSession() {
		if(sessionFactory == null){
			sessionFactory.openSession();
		}
		return sessionFactory.openSession();
    }

	public static SessionFactory getSessionFactory2() {
		return sessionFactory;
	}
	
	
}
