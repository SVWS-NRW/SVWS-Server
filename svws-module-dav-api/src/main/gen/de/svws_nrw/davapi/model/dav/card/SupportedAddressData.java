package de.svws_nrw.davapi.model.dav.card;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert die unterstützten Adressdaten im
 * CardDAV-Protokoll. Sie enthält eine Liste von
 * {@link CardAddressDataType}, die die verschiedenen Typen
 * von Adressdaten beschreibt, die unterstützt werden.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "supported-address-data", namespace = "urn:ietf:params:xml:ns:carddav")
public class SupportedAddressData {

	/** Eine Liste von unterstützten Adressdatentypen. */
	@XmlElement(required = true)
	protected List<CardAddressDataType> addressDataTypes;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SupportedAddressData() {
		// leer
	}

	/**
	 * Gibt die Liste der unterstützten Adressdatentypen zurück.
	 * Wenn die Liste noch nicht initialisiert wurde, wird sie automatisch erstellt.
	 *
	 * @return Eine Liste von {@link CardAddressDataType},
	 *         die die unterstützten Adressdatentypen enthält.
	 */
	public List<CardAddressDataType> getAddressDataTypes() {
		if (addressDataTypes == null) {
			addressDataTypes = new ArrayList<>();
		}
		return this.addressDataTypes;
	}
}
