/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author caiqu
 */
@Entity
@Table(name = "creds")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Creds.findAll", query = "SELECT c FROM Creds c")
    , @NamedQuery(name = "Creds.findById", query = "SELECT c FROM Creds c WHERE c.id = :id")
    , @NamedQuery(name = "Creds.findByUsername", query = "SELECT c FROM Creds c WHERE c.username = :username")
    , @NamedQuery(name = "Creds.findByPassword", query = "SELECT c FROM Creds c WHERE c.password = :password")
    , @NamedQuery(name = "Creds.findByName", query = "SELECT c FROM Creds c WHERE c.name = :name")
    , @NamedQuery(name = "Creds.findByDate", query = "SELECT c FROM Creds c WHERE c.date = :date")})
public class Creds implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Creds() {
    }

    public Creds(Integer id) {
        this.id = id;
    }

    public Creds(Integer id, String username, String password, String name, Date date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Creds)) {
            return false;
        }
        Creds other = (Creds) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enterprise.messageboard.entities.Creds[ id=" + id + " ]";
    }
    
}
