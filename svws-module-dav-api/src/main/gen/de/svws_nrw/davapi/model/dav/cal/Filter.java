package de.svws_nrw.davapi.model.dav.cal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "filter", namespace = "urn:ietf:params:xml:ns:caldav")
public class Filter {

	@XmlElement(name = "comp-filter")
	protected CompFilter compFilter;

	public CompFilter getCompFilter() {
		return compFilter;
	}

	public void setCompFilter(CompFilter compFilter) {
		this.compFilter = compFilter;
	}

}
