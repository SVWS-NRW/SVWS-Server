package de.nrw.schule.svws.transpiler.typescript;

import jakarta.ws.rs.core.MediaType;

import de.nrw.schule.svws.transpiler.TranspilerException;

/**
 * Diese Aufzählung beinhaltet alle Mime-Types, die derzeit vom
 * Transpiler für die OpenAPI unterstützt werden.
 */
public enum ApiMimeType {

	/** Plain Text*/
	TEXT_PLAIN("MediaType.TEXT_PLAIN", "text/plain"),
	
	/** JSON */
	APPLICATION_JSON("MediaType.APPLICATION_JSON", "application/json"),
	
	/** Multipart Form Data */
	MULTIPART_FORM_DATA("MediaType.MULTIPART_FORM_DATA", "multipart/form-data"),
	
	/** Binary Data */
	APPLICATION_OCTET_STREAM("MediaType.APPLICATION_OCTET_STREAM", "application/octet-stream"),
	
	/** SQlite-Database */
	SQLITE("", "application/vnd.sqlite3"),
	
	/** PDF */
	PDF("", "application/pdf");
	
	private final String mediaType;
	private final String mimetype; 
	
	private ApiMimeType(String mediaType, String mimetype) {
		this.mediaType = mediaType;
		this.mimetype = mimetype;
	}
	
	/**
	 * Ermittelt anhand der übergebenen Zeichenkette den Mime-Type
	 * aus dieser Aufzählung. Hierbei kann sowohl der MimeType übergeben 
	 * werden oder auch die Konstante der Klasse {@link MediaType}.
	 * 
	 * @param str   die Zeichenkette mit dem Mime-Type
	 * 
	 * @return das Element dieser Aufzählung
	 */
	public static ApiMimeType get(String str) {
		for (ApiMimeType t : ApiMimeType.values()) {
			if (t.mediaType.equals(str) || t.mimetype.equals(str))
				return t;
		}
		throw new TranspilerException("Transpiler Error: Unsupported mime type.");
	}

	@Override
	public String toString() {
		return this.mimetype;
	}

}
