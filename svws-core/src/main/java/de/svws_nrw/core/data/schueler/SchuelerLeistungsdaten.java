package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse liefert die Leistungsdaten eines Lernabschnitts eines Schülers zurück.  
 */
@XmlRootElement
@Schema(description = "Die Leistungsdaten eines Lernabschnitts eines Schülers.")
@TranspilerDTO
public class SchuelerLeistungsdaten {

	/** Die ID der Leistungsdaten in der Datenbank. */
	@Schema(description = "die ID der Leistungsdaten in der Datenbank", example = "126784")
	public long id = -1;
	
	/** Die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören. */
	@Schema(description = "die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören", example = "4785")
	public long lernabschnittID = -1;

	// TODO Version 1.x: sind bei Kursen identisch zur Kurstabelle, für Klassen kann man eine Tabelle Klassenunterricht einführen, so dass die Daten hier dann redundant wären
	/** Die ID des Faches, auf welches sich die Leistungsdaten beziehen. */
	@Schema(description = "die ID des Faches, auf welches sich die Leistungsdaten beziehen", example = "46")
	public long fachID = -1;

	/** Die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassen unterricht NULL. */
	@Schema(description = "die ID des Kurses, auf welches sich die Leistungsdaten beziehen - bei Klassen unterricht NULL", example = "7732")
	public Long kursID = null;

	/** Die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt. */
	@Schema(description = "die spezielle Kursart des Schülers, sofern Kursunterricht vorliegt", example = "LK1")
	public String kursart = null;

	/** Gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt und wenn ja, um welches (NULL, 1, 2, 3, 4) */
	@Schema(description = "gibt an, ob es sich bei der Fachbelegung um ein Abiturfach des Schülers handelt (NULL, 1, 2, 3, 4)", example = "1")
	public String abifach = null;

	/** Gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren 10 (G8) */
	@Schema(description = "gibt an, ob es sich um ein Fach der Zentralen Prüfungen 10 handelt oder um ein Fach der Zentralen Klausuren 10 (G8)", example = "false")
	public boolean istZP10oderZK10 = false;
	
	// --- Informationen zum Fachlehrer

	/** Die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL */
	@Schema(description = "die Schulnummer, sofern es sich um Unterricht an einer kooperierenden Schule handelt, ansonsten NULL", example = "null")
	public Integer koopSchule = null;
	
	// TODO Version 1.x: sind bei Kursen identisch zum eingetragenen Fachlehrer, für Klassen kann man eine Tabelle Klassenunterricht einführen, so dass die Daten hier redundant wären	
	/** Die ID des zugehörigen Fach-Lehrers. */
	@Schema(description = "die ID des Lernabschnitts des Schülers, zu dem diese Leistungsdaten gehören", example = "23")
	public Long lehrerID = null;

	/** Die Anzahl der Wochenstunden, welche das Fach unterrichtet wird. */
	@Schema(description = "die Anzahl der Wochenstunden, welche das Fach unterrichtet wird", example = "3")
	public int wochenstunden = 0;

	// TODO Version 1.x: (Zusatz?-)Lehrkräfte und Wochenstunden sollten flexibler verwaltet werden und hier in Form eines Arrays übergeben werden
	
	/** Die ID der Zusatzkraft. */
	@Schema(description = "die ID der Zusatzkraft", example = "23")
	public Long zusatzkraftID = null;

	/** Die Anzahl der Wochenstunden der Zusatzkraft. */
	@Schema(description = "die Anzahl der Wochenstunden der Zusatzkraft", example = "3")
	public int zusatzkraftWochenstunden = 0;


	// --- Informationen zur Notengebung
	
	/** Gibt an, on das Fach auf dem Zeugnis erscheint oder nicht. */
	@Schema(description = "gibt an, on das Fach auf dem Zeugnis erscheint oder nicht", example = "3")
	public boolean aufZeugnis = true;

	/** Das Kürzel der erteilten Note - es können auch Pseudonoten eingetragen werden (z.B. AT). */
	@Schema(description = "das Kürzel der erteilten Note - es können auch Pseudonoten eingetragen werden (z.B. AT)", example = "3-")
	public String note = null;

	/** Gibt an, ob die Leistung gemahnt wurde oder nicht. */
	@Schema(description = "gibt an, ob die Leistung gemahnt wurde oder nicht", example = "false")
	public boolean istGemahnt = false;
	
	/** Das Datum, wann die Leistung gemahnt wurde oder null. */
	@Schema(description = "das Datum, wann die Leistung gemahnt wurde oder null", example = "null")
	public String Mahndatum = null;

	/** Gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich um ein epochal unterrichtetes Fach handelt oder nicht", example = "false")
	public boolean istEpochal = false; 

	// TODO in einem anderen Format bereitstellen:
	/** Gibt an, ob es sich um eine Leistung handelt, welche über das "Holen von abgeschlossenen Fächern" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben aus welchem die Daten geholt wurden */
	@Schema(description = "gibt an, ob es sich um eine Leistung handelt, welche über das \"Holen von abgeschlossenen Fächern\" in diesem Leistungsabschnitt bereitstehen. Wenn ja, dann ist hier der Jahrgang angegeben aus welchem die Daten geholt wurden", example = "06")	
	public String geholtJahrgangAbgeschlossen = null;
	
	/** Die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg) */
	@Schema(description = "die Gewichtung für den allgemeinbildenden Teil (am Berufskolleg)", example = "1")	
	public int gewichtungAllgemeinbildend = 1;

	// TODO Deprecated ???
	/** Die Berufsabschlussnote am Berufskolleg */
	@Schema(description = "die Berufsabschlussnote am Berufskolleg ", example = "2")	
	public String noteBerufsabschluss = null; 


	/** Der Text für die fachbezogene Lernentwicklung des Schülers */
	@Schema(description = "der Text für die fachbezogene Lernentwicklung des Schülers", example = "")		
	public @NotNull String textFachbezogeneLernentwicklung = "";

	/** Die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang) */
	@Schema(description = "die Facheigenschaft für die Lernstandberichte an Grundschulen (V = voller Umfang, R = reduzierter Umfang)", example = "V")		
	public @NotNull String umfangLernstandsbericht = "";


	/** Die Gesamt-Anzahl der Fehlstunden für dieses Fach */
	@Schema(description = "die Gesamt-Anzahl der Fehlstunden für dieses Fach", example = "2")		
	public int fehlstundenGesamt = 0;

	/** Die Anzahl der unentschuldigten Fehlstunden für dieses Fach */
	@Schema(description = "die Anzahl der unentschuldigten Fehlstunden für dieses Fach", example = "2")		
	public int fehlstundenUnentschuldigt = 0;

}
