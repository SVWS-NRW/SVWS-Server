package de.nrw.schule.svws.core.data.enm;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Schülern
 * in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Schülern in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMSchueler {

	/** Die ID des Schülers in der SVWS-DB */
	@Schema(required = true, description = "Die ID des Schülers in der SVWS-DB", example="12345")
	public long id; 

	/** Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet */
	@Schema(required = true, description = "Die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet", example="13")
	public long jahrgangID;

	/** Die ID der aktuellen Klasse, in der sich der Schüler befindet */
	@Schema(required = true, description = "Die ID der aktuellen Klasse, in der sich der Schüler befindet", example="42")
	public long klasseID;

	/** Der Nachname des Schülers (z.B. Mustermann) */
	@Schema(required = true, description = "Der Nachname des Schülers", example="Mustermann")
	public String nachname;

	/** Der Vorname des Schülers (z.B. Max) */
	@Schema(required = true, description = "Der Vorname des Schülers", example="Max")
	public String vorname;
	
	/** Das Geschlecht des Schülers (m,w,d,x) */
	@Schema(required = true, description = "Das Geschlecht des Schülers (m - männlich, w - weiblich, d - divers, x - ohne Angabe im Geburtenregister)", example="d")
	public String geschlecht;

	/** Gibt an, ob sich der Schüler aktuell im bilingualen Bildungsgang befindet (wenn ja, z.B. F) oder nicht (null) */
	@Schema(required = true, description = "Gibt an, ob sich der Schüler aktuell im bilingualen Bildungsgang befindet "
			+ "(wenn ja, z.B. F) oder nicht (null)", example="null")
	public String bilingualeSprache;

	/** Gibt an, ob der Schüler Ziel-different unterrichtet wird */
	@Schema(required = true, description = "Gibt an, ob der Schüler Ziel-different unterrichtet wird", example="false")
	public boolean istZieldifferent;
	
	/** Gibt an, ob der Schüler Deutsch-Förderung mit Deutsch als Zweitsprache (DaZ) bekommt (Seiteneinsteiger, z.B. Flüchtlingskinder) */
	@Schema(required = true, description = "Gibt an, ob der Schüler Deutsch-Förderung mit Deutsch als Zweitsprache (DaZ) "
			+ "bekommt (Seiteneinsteiger, z.B. Flüchtlingskinder)", example="false")
	public boolean istDaZFoerderung;
	
	/** Die Sprachenfolge des Schülers */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMSprachenfolge.class, description = "Ein Array mit den Informationen zu der Sprachenfolge des Schülers."))
	public @NotNull Vector<@NotNull ENMSprachenfolge> sprachenfolge = new Vector<>();

	/** Informationen zum Lernabschnitt des Schülers in der Notendatei */
	@Schema(required = true, description = "Informationen zum Lernabschnitt des Schülers in der Notendatei.", example="2")
	public @NotNull ENMLernabschnitt lernabschnitt = new ENMLernabschnitt();
	
	/** Die Leistungsdaten des Schülers in dem Lernabschnitt der Notendatei */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMLeistung.class, description = "Ein Array mit den Informationen "
			+ "der Leistungsdaten des Schülers in dem Lernabschnitt der Notendatei."))
	public final @NotNull Vector<@NotNull ENMLeistung> leistungsdaten = new Vector<>();

	/** Die Bemerkungen bei dem Schüler in Bezug auf den Lernabschnitt der Notendatei */
	@Schema(required = true, description = "Die Bemerkungen bei dem Schüler in Bezug auf den Lernabschnitt der Notendatei.", example="Bemerkungstext.")
	public ENMLeistungBemerkungen bemerkungen = new ENMLeistungBemerkungen();

	/** Die Informationen zu den Zentralen Prüfungen Klasse 10, sofern vorhanden - ansonsten null */
	@Schema(required = false, description = "Die Informationen zu den Zentralen Prüfungen Klasse 10, sofern vorhanden - ansonsten null.", example="null")
	public ENMZP10 zp10; 

	/** Die Informationen zu den Abschlüssen am Berufskolleg, sofern vorhanden - ansonsten null */
	@Schema(required = false, description = "Die Informationen zu den Abschlüssen am Berufskolleg, sofern vorhanden - ansonsten null.", example="null")
	public ENMBKAbschluss bkabschluss;
	

}

