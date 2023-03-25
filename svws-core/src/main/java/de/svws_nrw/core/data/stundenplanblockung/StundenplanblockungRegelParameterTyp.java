package de.svws_nrw.core.data.stundenplanblockung;

/** 
 * Diese Klasse definiert die unterschiedlichen Typen von Regel-Parametern, 
 * die im Rahmen der Stundenplanblockung eingesetzt werden bei {@link StundenplanblockungRegel}. 
 */
public enum StundenplanblockungRegelParameterTyp {

	/** Der Parameter-Typ der Lehrkraft. */
	LEHRKRAFT_ID,
	
	/** Der Parameter-Typ der Klasse. */
	KLASSE_ID,
	
	/** Der Parameter-Typ des Faches. */
	FACH_ID,
	
	/** Der Parameter-Typ des Raumes. */
	RAUM_ID,
	
	/** Der Parameter-Typ der Kopplung. */
	KOPPLUNG_ID,
	
	/** Der Parameter-Typ der Lerngruppe. */
	LERNGRUPPE_ID,
	
	/** Der Parameter-Typ für JA=1 und NEIN=0 Werte. */
	WERT_BOOLEAN,
	
	/** Der Parameter-Typ für Integer-Werte. */
	WERT_INTEGER,
	
}
