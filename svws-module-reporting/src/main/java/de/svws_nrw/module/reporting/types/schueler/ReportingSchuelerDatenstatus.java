package de.svws_nrw.module.reporting.types.schueler;

/**
 * Eine Enum, um den Status der aus der DB geladenen Daten eine ReportingSchuelers zu speichern und schnell darauf zuzugreifen.
 */
public enum ReportingSchuelerDatenstatus {

	/** Erzieherdaten wurden bereits geladen. */
	ERZIEHER(1),

	/** Lernabschnittsdaten wurden bereits geladen. */
	LERNABSCHNITTE(2),

	/** Leistungsdaten wurden bereits geladen. */
	LEISTUNGSDATEN(4),

	/** Leistungsdaten wurden bereits geladen. */
	SPRACHBELEGUNGEN(8);

	private final int state;

	ReportingSchuelerDatenstatus(final int state) {
		this.state = state;
	}
}
