/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.

Oracle and Java are registered trademarks of Oracle and/or its affiliates.
Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package enterprise.messageboard.resources;

import enterprise.messageboard.ejbs.SessionContext;
import enterprise.messageboard.entities.Item;
import enterprise.messageboard.entities.ItemPK;
import enterprise.messageboard.entities.Message;
import enterprise.messageboard.entities.Products;
import enterprise.messageboard.entities.Users;
import enterprise.messageboard.sessions.CredsFacade;
import enterprise.messageboard.sessions.ItemFacade;
import enterprise.messageboard.sessions.ProductsFacade;
import enterprise.messageboard.sessions.UsersFacade;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

@Singleton
public class MessageHolderSingletonBean {
    @EJB
    private ProductsFacade prodOp;
    
    @EJB
    private UsersFacade userOp;
    
    @EJB
    private ItemFacade itemOp;
    
    @EJB
    private CredsFacade credOp;
    
    private LinkedList<Message> list = new LinkedList<Message>();
    private int maxMessages = 10;
    int currentId = 0;

    public MessageHolderSingletonBean() {
        // initial content
        addMessage("msg0", new Date(0));
        addMessage("msg1", new Date(1000));
        addMessage("msg2", new Date(2000));
    }
    
    
      public List<Message> getMessages() {
        List<Message> l = new LinkedList<Message>();

        int index = 0;

        while (index < list.size() && index < maxMessages) {
            l.add(list.get(index));
            index++;
        }

        return l;
    }

    private synchronized int getNewId() {
        return currentId++;
    }

    public synchronized Message addMessage(String msg) {
        return addMessage(msg, new Date());
    }

    private synchronized Message addMessage(String msg, Date date) {
        Message m = new Message(date, msg, getNewId());
       
        list.addFirst(m);

        return m;
    }

    public Message getMessage(int uniqueId) {
        int index = 0;
        Message m;

        while (index < list.size()) {
            if ((m = list.get(index)).getUniqueId() == uniqueId) {
                return m;
            }
            index++;
        }

        return null;
    }

    public synchronized boolean deleteMessage(int uniqueId) {
        int index = 0;

        while (index < list.size()) {
            if (list.get(index).getUniqueId() == uniqueId) {
                list.remove(index);
                return true;
            }
            index++;
        }

        return false;
    }
       
   //retrieve list of products
    public String retrieveProducts(){
        List<Products> products = prodOp.findAll();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for(int i = 0; i < products.size(); i++){
            Products product = products.get(i);
            jsonObjectBuilder.add("id", product.getId()).
                    add("name", product.getProductName()).
                    add("image", product.getProductImage()).
                    add("price", product.getProductPrice());        
            jsonArrayBuilder.add(jsonObjectBuilder);
        }
         return jsonArrayBuilder.build().toString();
    }
    
    //ShoppingCartGet
    public String getItemsByUserId(int user_id){
        float amount = (float) 0;
        List<Item> items = itemOp.findItemByUserId(user_id);
        
        if (items != null) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            
            for(int i = 0; i < items.size(); i++){
                Item item = items.get(i);
                ItemPK itemPK = item.getItemPK();
                
                Products product = prodOp.findProductById(itemPK.getIdProduct());
                
                float itemPrice = item.getQuantity()*product.getProductPrice().floatValue();
                amount += itemPrice;
                
                jsonObjectBuilder.add("userId",itemPK.getIdUser()).
                        add("productId", itemPK.getIdProduct()).
                        add("quantity", item.getQuantity()).
                        add("price", product.getProductPrice().floatValue()).
                        add("amount", itemPrice);
                jsonArrayBuilder.add(jsonObjectBuilder);
            }
            
            jsonArrayBuilder.add(Json.createObjectBuilder().add("amount", amount));
            return jsonArrayBuilder.build().toString();
            
        } else {
            return "Sorry, Object null!!!";
        }        
    }
      
    //register user
    public String addUser(String newUserJson) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        JsonObject jsonUser = convertStringToJson(newUserJson);
               
        if(userOp.findUserByUsername(jsonUser.getString("username")) != null){
            System.out.println("USUÁRIO EXISTE!");
            return "Usuário já existe!!!";
        } else {
            Users user = new Users();
            user.setId(currentId);
            user.setName(jsonUser.getString("name"));
            user.setUsername(jsonUser.getString("username"));
            user.setPassword(convertStringToSha2(jsonUser.getString("password")));
            Date date = new Date();
            user.setRegisterDt(date);


            System.out.println("ID: " + user.getId() +
                    " Name: " + user.getName() +
                    " Username: " + user.getUsername() +
                    " Password: " + user.getPassword() +
                    " Timestamp: " + user.getRegisterDt().toString());

            userOp.create(user);

           return "Usuário Criado: " + user.getName() + " json: " + jsonUser.toString();            
        }      
    }
    
    //login and open session
    public String doLogin(String loginJson) {
        JsonObject json = convertStringToJson(loginJson);
        
       Users user = userOp.findUserByUsername(json.getString("username"));
       
       if(user != null){
           if(convertStringToSha2(json.getString("password")).equals(user.getPassword())){
               try{
                   // SessionContext.getInstance().setAttribute("user", user);
               }catch(Exception e){
                   e.printStackTrace();
               }
               System.out.println("USUÁRIO LOGADO!");
               return "Usuário Logado";
           }
       }  
        
        System.out.println("USUÁRIO NÃO EXISTE. Username: "
                + json.getString("username") + " senha: " 
                + json.getString("password"));
       return "Username or Password don´t match!!!";        
    }
    
    //logout and close session
    public String doLogout(){
        //TODO: Close session
        return "";
    }
    
    /*
    //add Item to the shopping cart
    public String addItem(String itemJson){
        JsonObject itemObj = convertStringToJson(itemJson);
        ItemPK itemPK = new ItemPK();
        itemPK.setIdProduct(itemObj.getInt("productId"));
        itemPK.setIdUser(itemObj.getInt("userId")); //TODO: Pegar da Sessão
        Item item = new Item();
        item.setItemPK(itemPK);
        item.setQuantity(itemObj.getInt("quantity"));       
        itemOp.create(item);
        return "Usuário Criado: " + item.toString();
    }
    */
    
    //Add an item to the cart (TODO: Eliminate userId parameter)
    public String addItemToCart(int idProduct, int quantity, int userId){
        Products product = prodOp.findProductById(idProduct);
        
        ItemPK itemPK = new ItemPK();
        itemPK.setIdProduct(product.getId());
        itemPK.setIdUser(userId); //TODO: Pegar da Sessão
        Item item = new Item();
        item.setItemPK(itemPK);
        item.setQuantity(quantity);       
        itemOp.create(item);
        
        return "Product added to the cart: " + item.toString();
    }
    
    //Delete item from the cart (TODO: remove both parameter, instead itemId
    public String deleteItem(int productId, int userId){
       
        Item item = itemOp.findItemById(productId, userId);
        itemOp.remove(item);
        
        return "";
    }
            
   private String convertStringToSha2(String valor) {
               MessageDigest mDigest;
               try { 
                      //Instanciamos o nosso HASH MD5, poderíamos usar outro como
                      //SHA, por exemplo, mas optamos por MD5.
                      mDigest = MessageDigest.getInstance("SHA-256");
                      
                      //Convert a String valor para um array de bytes em MD5
                      byte[] valorSHA2 = mDigest.digest(valor.getBytes("UTF-8"));
                      
                      //Convertemos os bytes para hexadecimal, assim podemos salvar
                      //no banco para posterior comparação se senhas
                      StringBuffer sb = new StringBuffer();
                      for (byte b : valorSHA2){
                             sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
                      }
   
                      return sb.toString();
                      
               } catch (NoSuchAlgorithmException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return null;
               } catch (UnsupportedEncodingException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                      return null;
               }
    }

    private JsonObject convertStringToJson(String string) {
        JsonReader jsonReader = Json.createReader(new StringReader(string));
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        return json;
    }
        
}
