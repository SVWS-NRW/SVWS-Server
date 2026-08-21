package de.svws_nrw.mapper;

import java.net.URI;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public class UriMapper {

	/**
	 * Mapped String auf URI
	 *
	 * @param value String Repräsentation
	 *
	 * @return URI
	 */
	@Named("toUri")
	public URI toUri(final String value) {
		return value == null ? null : URI.create(value);
	}

	/**
	 * Mapped URI auf String
	 *
	 * @param value URI
	 *
	 * @return String Repräsentation
	 */
	@Named("fromUri")
	public String fromUri(final URI value) {
		return value == null ? null : value.toString();
	}
}
