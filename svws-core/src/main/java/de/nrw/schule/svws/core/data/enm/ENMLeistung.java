package de.nrw.schule.svws.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.util.Vector;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu den Leistungsdaten
 * eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu den Leistungsdaten eines Schüler in Bezug auf den Lernabschnitt für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMLeistung {

	/** Die ID der Leistungsdaten des Schülers in der SVWS-DB (z.B. 307956) */
	@Schema(required = true, description = "Die ID der Leistungsdaten des Schülers in der SVWS-DB.", example="307956")	
	public long id;

	/** Die eindeutige ID der Lerngruppe, der der Schüler zugeordnet ist. (Klasse oder Kurs wird erst 
	 * in der Lerngruppe unterschieden!) */
	@Schema(required = true, description = "Die eindeutige ID der Lerngruppe, der der Schüler zugeordnet ist. (Klasse oder Kurs wird erst."
			+ "in der Lerngruppe unterschieden!)", example="12345")
	public long lerngruppenID;

	/** Das Kürzel der Note, die vergeben wurde. */
	@Schema(required = true, description = "Das Kürzel der Note, die vergeben wurde.", example="3+")
	public String note;

	/** Gibt bei Oberstufenkursen an, ob das Fach schriftlich belegt wurde oder nicht. */
	@Schema(required = false, description = "Gibt bei Oberstufenkursen an, ob das Fach schriftlich belegt wurde oder nicht.", example="true")
    public Boolean istSchriftlich;

    /** Gibt an, ob es sich um ein Abiturfach handelt (1,2,3 oder 4) oder nicht (null) */
	@Schema(required = true, description = "Gibt an, ob es sich um ein Abitufach handelt (1,2,3 oder 4) oder nicht (null).", example="2")
    public Integer abiturfach;

    /** Gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittel werden.", example="23")
    public Integer fehlstundenGesamt;

    /** Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittel werden.", example="0")
    public Integer fehlstundenUnentschuldigt;

    /** Die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen */
	@Schema(required = false, description = "Die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen.", example="Text zum Fach")
    public String fachbezogeneBemerkungen;

    /** Die Kurszuweisung, die auf dem Zeugnis erscheinen soll für den nächsten Kursabschnitt (z.B. E oder G-Kurs, z.B. an der Gesamtschule) */
	@Schema(required = false, description = "Die Kurszuweisung, die auf dem Zeugnis erscheinen soll für den nächsten "
			+ "Kursabschnitt (z.B. E oder G-Kurs, z.B. an der Gesamtschule).", example="E")
    // TODO Core Type
    public String neueZuweisungKursart;
	
	/** Gibt an, ob ein Fach gemahnt wurde oder nicht. */
	@Schema(required = false, description = "Gibt an, ob ein Fach gemahnt wurde oder nicht.", example="true")
    public Boolean istGemahnt;

	/** Das Mahndatum bei erfolgter Mahnung. */
	@Schema(required = true, description = "Das Mahndatum bei erfolgter Mahnung.", example="11.11.1911")
	public String mahndatum;	
    
    /** Die Teilleistungen, sofern welche vordefiniert sind. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMTeilleistung.class, description = "Ein Array mit den Informationen zu den Teilleistungen, "
			+ "sofern welche vordefiniert sind."))
    public @NotNull Vector<@NotNull ENMTeilleistung> teilleistungen = new Vector<>();

}
