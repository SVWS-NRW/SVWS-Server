package de.svws_nrw.core.data.bk.abi;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt für die einzelnen Leistungen des Schülers zu einem Fach in einem Halbjahr des beruflichen Gymnasiums zur Verfügung.
 */
@XmlRootElement
@Schema(description = "Informationen zu den einzelnen Leistungen des Schülers zu einem Fach in einem Halbjahr des beruflichen Gymnasiums.")
@TranspilerDTO
public class BKGymLeistungenFachHalbjahr {

	/** Die ID des Datensatzes */
	public long id;

	/** Das Schuljahr der Fachbelegung */
	public int schuljahr;

	/** Das Kürzel des Halbjahres der Fachbelegung */
	public String halbjahrKuerzel;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt. */
	public boolean abschnittGewertet;

	/** Gibt den Jahrgang, an dem die Belegung zugeordnet ist */
	public String jahrgang;

	/** Die ID des Kurses */
	public Long idKurs;

	/** Gibt die ID des Fachlehrers an, bei dem der zur Fachbelegung gehörige Kurs belegt wurde. */
	public Long idFachlehrer;

	/** Gibt die Note als Zeichenkette und mit Tendenz an */
	public String notenKuerzel;

	/** Das Kürzel der Kursart der gymnasialen Oberstufe des belegten Kurses */
	public String kursartKuerzel;

	/** Gibt an, ob der Kurs schriftlich belegt wurde. */
	public boolean istSchriftlich;

	/** Gibt die Sprache als einstelliges Kürzel an, sofern der Kurs bilingual unterrichtet wurde. */
	public String bilingualeSprache;

	/** Gibt die Anzahl der Wochenstunden für den Kurs an. */
	public int wochenstunden;

	/** Gibt die Anzahl der Fehlstunden in dem Halbjahr an. */
	public int fehlstundenGesamt;

	/** Gibt die Anzahl der unentschuldigten Fehlstunden in dem Halbjahr an. */
	public int fehlstundenUnentschuldigt;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BKGymLeistungenFachHalbjahr() {
		// leer
	}

}
