/*
 * copyright(c) 2014 SAS Institute, Cary NC 27513 Created on Oct 23, 2014
 */
package de.svws_nrw.davapi.model.dav;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 * <p>Java class for anonymous complex type.</p>
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>
    &lt;element name="searchrequest"&gt;
        &lt;complexType&gt;
            &lt;any processContents="skip" namespace="##other" minOccurs="1" maxOccurs="1" /&gt;
        &lt;/complexType&gt;
    &lt;/element&gt;
 * </pre>
 */
@XmlType(name = "")
@XmlRootElement(name = "searchrequest")
public class SearchRequest {

	private String language;
	private String query;

	/**
	 * Default-Konstruktor setzt:
	 * <br> das Attribut {@link #language} auf 'davbasic'
	 * <br> das Attribut {@link #query} auf ''
	 */
	public SearchRequest() {
		this.language = "davbasic";
		this.query = "";
	}

	/**
	 * Konstruktor zum setzen der Attribute {@link #language} und {@link #query}.
	 *
	 * @param language   das neue language-Attribut.
	 * @param query      das neue query-Attribut.
	 */
	public SearchRequest(final String language, final String query) {
		this.language = language;
		this.query = query;
	}

	/**
	 * Liefert den String des language-Attributes.
	 *
	 * @return den String des language-Attributes.
	 */
	public final String getLanguage() {
		return language;
	}

	/**
	 * Setzt das language-Attribut.
	 *
	 * @param language   das neue language-Attribut.
	 */
	@XmlTransient
	public void setLanguage(final String language) {
		this.language = language;
	}

	/**
	 * Liefert den String des query-Attributes.
	 *
	 * @return den String des query-Attributes.
	 */
	public final String getQuery() {
		return query;
	}

	/**
	 * Setzt das query-Attribut.
	 *
	 * @param query   das neue query-Attribut.
	 */
	@XmlTransient
	public void setQuery(final String query) {
		this.query = query;
	}

	/**
	 * Liefert ein erzeugtes {@link JAXBElement}-Element definiert durch die Attribute language und query.
	 *
	 * @return ein erzeugtes {@link JAXBElement}-Element definiert durch die Attribute language und query.
	 */
	@XmlAnyElement
	public JAXBElement<String> getElement() {
		return new JAXBElement<>(new QName("DAV:", language), String.class, query);
	}
}
