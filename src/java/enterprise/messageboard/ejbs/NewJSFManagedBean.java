/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.ejbs;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 *
 * @author caiqu
 */
@Named(value = "newJSFManagedBean")
@Dependent
public class NewJSFManagedBean {

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    public NewJSFManagedBean() {
        
    }
    
    @GET
    @Produces({"application/json", "text/html"})
    public String getThat(){
        return "Eu estou aqui garota!";
    }
    
    @GET
    @Produces({"application/json", "text/html"})
    public String getThis(){
        return "Eu estou aqui garota!";
    }
    
    
    
}
