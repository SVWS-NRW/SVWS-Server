package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;
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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "current-user-privilege-set", namespace = "DAV:")
public class CurrentUserPrivilegeSet {

	private List<Privilege> privilege;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CurrentUserPrivilegeSet() {
		// leer
	}

	/**
	 * Liefert eine Liste der {@link Privilege}-Objekte.
	 * Falls noch keine Liste existiert, wird eine neue leere Liste erstellt.
	 *
	 * @return eine Liste der {@link Privilege}-Objekte, nie null.
	 */
	public List<Privilege> getPrivilege() {
		if (privilege == null) {
			privilege = new ArrayList<>();
		}
		return privilege;
	}

	/**
	 * Setzt die Liste der {@link Privilege}-Objekte.
	 *
	 * @param privileges   die neue Liste von {@link Privilege}-Objekten, die gesetzt werden soll.
	 */
	public void setPrivilege(final List<Privilege> privileges) {
		this.privilege = privileges;
	}

}
