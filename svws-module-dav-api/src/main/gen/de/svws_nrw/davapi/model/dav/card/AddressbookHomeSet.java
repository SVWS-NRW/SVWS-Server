package de.svws_nrw.davapi.model.dav.card;

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
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "href"
})
@XmlRootElement(name = "addressbook-home-set", namespace = "urn:ietf:params:xml:ns:carddav")
public class AddressbookHomeSet {
	@XmlElement(required = true, namespace = "DAV:")
    protected List<String> href;

    /**
	 * Leerer Standardkonstruktor.
	 */
	public AddressbookHomeSet() {
		// leer
	}

	/**
     * Gets the value of the href property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the href property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHref().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getHref() {
        if (href == null) {
            href = new ArrayList<>();
        }
        return this.href;
    }

}
