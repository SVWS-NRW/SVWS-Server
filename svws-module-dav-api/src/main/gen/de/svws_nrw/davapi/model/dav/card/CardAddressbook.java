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
		"content"
})
@XmlRootElement(name = "addressbook", namespace = "urn:ietf:params:xml:ns:carddav")
public class CardAddressbook {

	/**
	 * Liste der Inhalte des Adressbuchs.
	 * Diese Liste enthält gemischte XML-Inhalte, die als Strings repräsentiert werden.
	 */
	@XmlMixed
	private List<String> content;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CardAddressbook() {
		// leer
	}

	/**
	 * Gibt die Liste der Inhalte des Adressbuchs zurück.
	 *
	 * @return Eine Live-Referenz auf die Liste der Inhalte. Wenn die Liste noch nicht existiert,
	 *         wird sie initialisiert und zurückgegeben.
	 *
	 * Hinweis: Diese Methode liefert eine direkte Referenz auf die interne Liste.
	 * Änderungen an der zurückgegebenen Liste wirken sich direkt auf das JAXB-Objekt aus.
	 *
	 * Beispiel zur Hinzufügung eines neuen Elements:
	 * <pre>
	 *    getContent().add(neuesElement);
	 * </pre>
	 *
	 * Die Liste kann nur Objekte vom Typ {@link String} enthalten.
	 */
	public List<String> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

}
