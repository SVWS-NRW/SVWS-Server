package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt eine Liste von Objekten (der Privilegien) zur Verfügung.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"content"
})
@XmlRootElement(name = "privilege")
public class Privilege {

	@XmlMixed
	@XmlAnyElement(lax = true)
	private List<Object> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Privilege() {
		// leer
	}

	/**
	 * Liefert eine Liste der Objekte (der Privilegien).
	 *
	 * @return eine Liste der Objekte (der Privilegien).
	 */
	public List<Object> getContent() {
		if (content == null)
			content = new ArrayList<>();
		return content;
	}

	/**
	 * Setzt die Liste Objekte (der Privilegien).
	 *
	 * @param content   die zu setzende Liste der Objekte (der Privilegien).
	 */
	public void setContent(final List<Object> content) {
		this.content = content;
	}

}
