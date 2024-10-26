package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
     "href"
})
@XmlRootElement(name = "inherited")
public class Inherited {

	private String href;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Inherited() {
		// leer
	}

	public String getHref() {
		return href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

}
