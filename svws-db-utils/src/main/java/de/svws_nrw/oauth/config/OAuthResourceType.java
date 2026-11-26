package de.svws_nrw.oauth.config;

/**
 * Die Arten von Resource Servern, die über OAuth angebunden werden.
 */
public enum OAuthResourceType {

	/** Der Signierdienst von IT.NRW */
	IT_NRW_SIGNIERDIENST,

	/** Der Abruf von Daten (z.B. erfolgreiche Bewerbungen) bei schulbewerbung.de */
	SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN

}
