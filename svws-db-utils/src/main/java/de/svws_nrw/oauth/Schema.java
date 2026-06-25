package de.svws_nrw.oauth;

import org.apache.commons.lang3.StringUtils;

/**
 * Identifiziert ein DB-Schema / einen Mandanten.
 *
 * @param name Name des DB-Schemas
 */
public record Schema(String name) {

	/**
	 * Konstruktor
	 * @param value value
	 */
	public Schema {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("schema must not be blank");
		}
	}
}
