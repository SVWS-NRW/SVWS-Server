package de.nrw.schule.svws.base.kurs42;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** 
 * Diese Klasse dient als DTO für die CSV-Tabelle {@code Faecher.txt} eines Kurs42-Textdatei-Exportes.
 * 
 * @author Thomas Bachran 
 */
@JsonPropertyOrder({ "IDNr", "Krz", "Bezeichnung", "StatistikKrz", "Sortierung", "Fachgr_ID", "IstSprache" })
public class Kurs42DataFaecher {

	/** Die ID der Schiene innerhalb von Kurs 42. Entspricht vermutlich der Schienen-Nummer (hier 1-indiziert) */
	public long IDNr;

	/** Das Fachkürzel. */
	public String Krz;

	/** Die Bezeichnung des Faches. */
	public String Bezeichnung;

	/** Das Statistik-Kürzel des Faches */
	public String StatistikKrz;

	/** Eine Nummer für die Sortierreihenfolge der Fächer */
	public int Sortierung;

	/** Die ID der Fachgruppe, der das Fach angehört */
	public long Fachgr_ID;
	
	/** Gibt an, ob es sich bei dem Fach um eine Sprache handelt (1) oder nicht (0) */
	public int IstSprache;
}