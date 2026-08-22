package de.svws_nrw.module.reporting.signing;

/**
 * Der Zustand der QR-Codes einer Schulbescheinigung. Er ist die einzige Information, aus der die Vorlage die Darstellung ableitet: entweder beide QR-Codes
 * oder zwei identische Fehlerbilder mit einem festen Text je Fehlerart. Technische Fehlermeldungen gehören nicht in die Vorlagendaten; sie stehen im
 * Server-Log der Meldefassade.
 */
public enum SchulbescheinigungSignaturzustand {

	/** Beide QR-Codes liegen vor; die Bescheinigung ist digital signiert. */
	SIGNIERT,

	/** Das XSchule-XML konnte wegen fehlender oder fehlerhafter Daten des Schülers nicht erzeugt werden; es gibt keine QR-Codes. */
	DATENFEHLER,

	/** Signierung oder QR-Darstellung sind gescheitert; es gibt keine QR-Codes. */
	SIGNIERFEHLER

}
