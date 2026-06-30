package de.svws_nrw.oauth;

public enum OAuthScope {

	/** DEFAULT */
	DEFAULT("default");

	private final String text;

	OAuthScope(final String text) {
		this.text = text;
	}

	/**
	 * Liefert die Text-Repräsentation.
	 *
	 * @return die Text-Repräsentation
	 */
	public String text() {
		return text;
	}
}
