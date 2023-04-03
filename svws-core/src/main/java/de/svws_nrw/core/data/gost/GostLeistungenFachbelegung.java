package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt die Core-Types für
 * Fachbelegungen innerhalb der Leistungsdaten für die Abiturberechnung zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Fachbelegungen innerhalb der Leistungsdaten von einem Schüler der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLeistungenFachbelegung {

	/** Die ID des Datensatzes */
	public long id;

	/** Das Schuljahr der Fachbelegung */
	public int schuljahr;

	/** Das Kürzel des Halbjahres der Fachbelegung */
	public String halbjahrKuerzel;

	/** Der Abschnitt, dem die Fachbelegung zugeordnet ist - unterscheidet sich z.B. im Quartalsbetrieb vom Halbjahr */
	public int abschnitt;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt. */
	public boolean abschnittGewertet;

	/** Gibt den Jahrgang, an dem die Belegung zugeordnet ist */
	public String jahrgang;

	/** Gibt die ID des Fachlehrers an, bei dem der zur Fachbelegung gehörige Kurs belegt wurde. */
	public Long lehrer;

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

}
