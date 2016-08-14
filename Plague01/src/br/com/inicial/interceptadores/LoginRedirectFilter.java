package br.com.inicial.interceptadores;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.inicial.controle.LoginMB;
  
public class LoginRedirectFilter implements Filter  
{  
    @Override  
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,   ServletException  
    {         
        //arg2.doFilter(arg0, arg1);  
          
      HttpServletRequest servletRequest = (HttpServletRequest)arg0;  
      @SuppressWarnings("unused")  
      HttpServletResponse servletResponse = (HttpServletResponse)arg1;  
      HttpSession session = servletRequest.getSession();  
      String uri = ((HttpServletRequest) arg0).getRequestURI();  
      LoginMB loginTmp = (LoginMB)session.getAttribute("loginMB");  
      String userTmp = null;
      if(loginTmp != null){
  			userTmp = loginTmp.getLogin();
  		}
        try  
        {  
            if((uri.indexOf("restrito") > 0) && (userTmp == null || userTmp.equals(""))){  
//           	if(userTmp == null) {  
                String url = "/faces/login/Login.xhtml";  
                arg0.getRequestDispatcher(url).forward(arg0, arg1);  
                return;  
            }  
            else  
                arg2.doFilter(arg0, arg1);   
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
            arg2.doFilter(arg0, arg1);  
        }  
    }  
  
    @Override  
    public void init(FilterConfig arg0) throws ServletException  
    {  
    }  
  
    public void destroy()  
    {  
    }  
}  
