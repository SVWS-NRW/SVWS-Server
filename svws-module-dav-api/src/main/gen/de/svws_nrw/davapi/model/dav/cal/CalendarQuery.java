package de.svws_nrw.davapi.model.dav.cal;

import de.svws_nrw.davapi.model.dav.Prop;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Diese Klasse repräsentiert eine Kalenderabfrage im CalDAV-Protokoll.
 * Sie enthält Eigenschaften zur Definition der Abfrage und der Filterkriterien.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"prop",
		"filter"
})
@XmlRootElement(name = "calendar-query", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarQuery {

	/**
	 * Die Prop-Eigenschaft dieser CalendarQuery.
	 */
	@XmlElement(required = true, namespace = "DAV:")
	protected Prop prop;

	/**
	 * Die Filter-Eigenschaft dieser CalendarQuery.
	 */
	@XmlElement(required = true)
	protected Filter filter;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CalendarQuery() {
		// leer
	}

	/**
	* Liefert das {@link Prop}-Objekt.
	*
	* @return das {@link Prop}-Objekt.
	*/
	public Prop getProp() {
		return prop;
	}

	/**
	 * Setzt das {@link Prop}-Objekt.
	 *
	 * @param value   das zu setzende {@link Prop}-Objekt.
	 */
	public void setProp(final Prop value) {
		this.prop = value;
	}

	/**
	* Liefert das {@link Filter}-Objekt.
	*
	* @return das {@link Filter}-Objekt.
	*/
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Setzt das {@link Filter}-Objekt.
	 *
	 * @param filter   das zu setzende {@link Filter}-Objekt.
	 */
	public void setFilter(final Filter filter) {
		this.filter = filter;
	}

}
