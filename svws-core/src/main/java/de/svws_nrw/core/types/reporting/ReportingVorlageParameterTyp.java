package de.svws_nrw.core.types.reporting;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Enum-Klasse repräsentiert die Typen von Parametern, die für eine Reporting-Vorlage verwendet werden können.
 */
public enum ReportingVorlageParameterTyp {
	/** Vorlage-Parameter wurde vom Typ her noch nicht festgelegt. */
	UNDEFINED(0),

	/** Vorlage-Parameter des Typs BOOLEAN */
	BOOLEAN(1),

	/** Vorlage-Parameter des Typs String */
	STRING(2),

	/** Vorlage-Parameter des Typs LONG */
	LONG(3),

	/** Vorlage-Parameter des Typs INTEGER */
	INTEGER(4),

	/** Vorlage-Parameter des Typs DECIMAL */
	DECIMAL(5);


	/** Die ID des Vorlage-Parameter-Typs */
	private final int id;


	/**
	 * Erstellt einen neuen ReportingDVorlageParameterTyp
	 *
	 * @param id Die ID des ReportingVorlageParameterTyp
	 */
	ReportingVorlageParameterTyp(final int id) {
		this.id = id;
	}

	/**
	 * Gibt die ID dieses ReportingDVorlageParameterTyp zurück
	 *
	 * @return Die ID dieses ReportingDVorlageParameterTyp
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Diese Methode ermittelt den ReportingDVorlageParameterTyp anhand der übergebenen ID.
	 *
	 * @param id   	Die ID des gesuchten Vorlage-Parameters
	 *
	 * @return 		Der ReportingDVorlageParameterTyp
	 */
	public static @NotNull ReportingVorlageParameterTyp getByID(final int id) {
		for (final ReportingVorlageParameterTyp dp : ReportingVorlageParameterTyp.values()) {
			if (dp.id == id) {
				return dp;
			}
		}
		return UNDEFINED;
	}


}
