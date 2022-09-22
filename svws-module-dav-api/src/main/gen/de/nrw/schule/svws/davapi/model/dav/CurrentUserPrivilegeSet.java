package de.nrw.schule.svws.davapi.model.dav;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.</p>
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "current-user-privilege-set", namespace = "DAV:")
public class CurrentUserPrivilegeSet {

    private List<Privilege> privilege;

    public List<Privilege> getPrivilege() {
        if(privilege == null){
            privilege = new ArrayList<>();
        }
        return privilege;
    }

    public void setPrivilege(List<Privilege> privileges) {
        this.privilege = privileges;
    }

}
