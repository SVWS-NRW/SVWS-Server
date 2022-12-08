package de.nrw.schule.svws.core.types.kursblockung;

/** 
 * Diese Klasse definiert die unterschiedlichen Typen von Regel-Parametern, 
 * die im Rahmen der Kursblockung eingesetzt werden. 
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
