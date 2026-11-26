package de.svws_nrw.oauth.config;

import de.svws_nrw.core.types.ServerMode;

/**
 * Die Umgebungen der externen OAuth-Systeme (Test- bzw. Produktivumgebung).
 * Das Enum {@link ServerMode} wird dafür bewusst nicht genutzt.
 */
public enum OAuthEnvironment {

	/** Die Testumgebung der externen Systeme. */
	TEST,

	/** Die Produktivumgebung der externen Systeme. */
	PROD;

	/**
	 * Bildet den Modus, in welchem der Server betrieben wird, auf die Umgebung der externen Systeme ab:
	 * STABLE wird auf PROD abgebildet, alle übrigen Modi (BETA, ALPHA und DEV) auf TEST.
	 *
	 * @param serverMode   der Modus, in welchem der Server betrieben wird (die SVWS-Konfiguration liefert immer einen Modus)
	 *
	 * @return die zugehörige Umgebung der externen Systeme
	 */
	public static OAuthEnvironment of(final ServerMode serverMode) {
		if (serverMode == ServerMode.STABLE) {
			return OAuthEnvironment.PROD;
		}
		return OAuthEnvironment.TEST;
	}

}
