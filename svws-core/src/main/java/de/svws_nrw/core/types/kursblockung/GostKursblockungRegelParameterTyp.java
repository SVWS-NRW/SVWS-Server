package de.svws_nrw.core.types.kursblockung;

import de.svws_nrw.core.data.gost.GostBlockungRegel;

/**
 * Diese Klasse definiert die unterschiedlichen Typen von Regel-Parametern,
 * die im Rahmen der Kursblockung eingesetzt werden bei {@link GostBlockungRegel}.
 */
public enum GostKursblockungRegelParameterTyp {

	/** Der Parameter-Typ Kursart */
	KURSART,

	/** Der Parameter-Typ Schienennummer*/
	SCHIENEN_NR,

	/** Der Parameter-Typ Kurs-ID */
	KURS_ID,

	/** Der Parameter Typ Schüler-ID */
	SCHUELER_ID,

	/** Der Parameter Typ für eine Ja=1/Nein=0 Entscheidung. */
	BOOLEAN;

}
