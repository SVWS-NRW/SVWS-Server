package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.</p>
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>
 *   &lt;D:owner&gt;
 *        &lt;D:href&gt;http://www.example.com/acl/users/gstein&lt;/D:href&gt;
 *      &lt;/D:owner&gt;
 * </pre>
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "principal", "grant", "deny", "inherited", "protected1" })
@XmlRootElement(name = "ace")
public class Ace {
	private Principal principal;
	private Grant grant;
	private Deny deny;
	private Inherited inherited;

	@XmlElement(name = "protected")
	private Protected protected1;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Ace() {
		// leer
	}

	/**
	* Gibt das Principal-Objekt zurück.
	* @return Das Principal-Objekt
	*/
	public Principal getPrincipal() {
		return principal;
	}

	/**
	 * Setzt das Principal-Objekt.
	 * @param principal Das zu setzende Principal-Objekt
	 */
	public void setPrincipal(final Principal principal) {
		this.principal = principal;
	}

	/**
	* Gibt das Grant-Objekt zurück.
	* @return Das Grant-Objekt
	*/
	public Grant getGrant() {
		return grant;
	}

	/**
	 * Setzt das Grant-Objekt.
	 * @param grant Das zu setzende Grant-Objekt
	 */
	public void setGrant(final Grant grant) {
		this.grant = grant;
	}

	/**
	* Gibt das Deny-Objekt zurück.
	* @return Das Deny-Objekt
	*/
	public Deny getDeny() {
		return deny;
	}

	/**
	* Setzt das Deny-Objekt.
	* @param deny Das zu setzende Deny-Objekt
	*/
	public void setDeny(final Deny deny) {
		this.deny = deny;
	}

	/**
	 * Gibt das Inherited-Objekt zurück.
	 * @return Das Inherited-Objekt
	 */
	public Inherited getInherited() {
		return inherited;
	}

	/**
	 * Setzt das Inherited-Objekt.
	 * @param inherited Das zu setzende Inherited-Objekt
	 */
	public void setInherited(final Inherited inherited) {
		this.inherited = inherited;
	}

	/**
	* Gibt das Protected-Objekt zurück.
	* @return Das Protected-Objekt
	*/
	public Protected getProtected() {
		return protected1;
	}

	/**
	* Setzt das Protected-Objekt.
	* @param protected1 Das zu setzende Protected-Objekt
	*/
	public void setProtected(final Protected protected1) {
		this.protected1 = protected1;
	}

}
