/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.ejbs;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author caiqu
 */
public class PageFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        System.out.println("AQUI NO DO FILTER");
        
         HttpSession sess = ((HttpServletRequest) request).getSession(true);
   
         String newCurrentPage = ((HttpServletRequest) request).getServletPath();
   
         if (sess.getAttribute("currentPage") == null) {
             sess.setAttribute("lastPage", newCurrentPage);
             sess.setAttribute("currentPage", newCurrentPage);
         } else {
   
             String oldCurrentPage = sess.getAttribute("currentPage").toString();
             if (!oldCurrentPage.equals(newCurrentPage)) {
               sess.setAttribute("lastPage", oldCurrentPage);
               sess.setAttribute("currentPage", newCurrentPage);
             }
         }
   
         chain.doFilter(request, response);  
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
