package br.com.inicial.filtros;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.SessionFactory;

import br.com.inicial.dao.HibernateUtil;
import br.com.inicial.modelo.Empresa;

public class ConexaoHibernateFilter implements Filter {

	private SessionFactory	sf;
	private FacesContext facesContext = FacesContext.getCurrentInstance();
	
	public void init(FilterConfig config) throws ServletException {
		this.sf = HibernateUtil.getSessionFactory();
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException {

		try {
			/*if(this.sf.getAllClassMetadata().size()==1 && LoginMB.empresa2!=null && LoginMB.empresa2.getId()!=null){
				Empresa empresa = LoginMB.empresa2;
				this.sf = HibernateUtil.getSessionFactory(empresa);
				LoginMB.empresa2 = null;
			}*/
//			if(facesContext.getCurrentInstance()!=null){
				this.sf = HibernateUtil.getSessionFactory();
//			}
			this.sf.getCurrentSession().beginTransaction();
			chain.doFilter(servletRequest, servletResponse);

			this.sf.getCurrentSession().getTransaction().commit();
			this.sf.getCurrentSession().close();

		} catch (Throwable ex) {
			try {
				if (this.sf.getCurrentSession().getTransaction().isActive()) {
					this.sf.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			throw new ServletException(ex);
		}
	}
	
	public void destroy() {
	}


}
