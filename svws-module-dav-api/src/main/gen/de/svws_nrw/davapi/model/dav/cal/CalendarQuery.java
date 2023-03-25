package de.svws_nrw.davapi.model.dav.cal;

import de.svws_nrw.davapi.model.dav.Prop;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "prop",
    "filter"
})
@XmlRootElement(name = "calendar-query", namespace = "urn:ietf:params:xml:ns:caldav")
public class CalendarQuery {

    @XmlElement(required = true, namespace = "DAV:")
    protected Prop prop;

	@XmlElement(required = true)
	protected Filter filter;

    /**
     * Gets the value of the prop property.
     *
     * @return
     *     possible object is
     *     {@link Prop }
     *
     */
    public Prop getProp() {
        return prop;
    }

    /**
     * Sets the value of the prop property.
     *
     * @param value
     *     allowed object is
     *     {@link Prop }
     *
     */
    public void setProp(Prop value) {
        this.prop = value;
    }

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

}
