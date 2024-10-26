package de.svws_nrw.davapi.model.dav.cal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "comp-filter", namespace = "urn:ietf:params:xml:ns:caldav")
public class CompFilter {

	@XmlAttribute(name = "name")
	private String name;

	@XmlElement(name = "time-range")
	private TimeRange timeRange;

	@XmlElement(name = "comp-filter")
	private CompFilter compFilter;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CompFilter() {
		// leer
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TimeRange getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(final TimeRange timeRange) {
		this.timeRange = timeRange;
	}

	public CompFilter getCompFilter() {
		return compFilter;
	}

	public void setCompFilter(final CompFilter compFilter) {
		this.compFilter = compFilter;
	}

}
