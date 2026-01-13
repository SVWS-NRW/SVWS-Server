package de.svws_nrw.core.data.fach;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Faches.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Faches.")
@TranspilerDTO
public class FachDaten {

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das eindeutige Kürzel des Faches */
	@Schema(description = "das eindeutige Kürzel des Faches", example = "M")
	public @NotNull String kuerzel = "";

	/** Das Statistik-Kürzel des Faches */
	@Schema(description = "das Statistik-Kürzel des Faches", example = "M")
	public @NotNull String kuerzelStatistik = "";

	/** Die Bezeichnung des Faches */
	@Schema(description = "die Bezeichnung des Faches", example = "Mathematik")
	public @NotNull String bezeichnung = "";

	/** Die Sortierreihenfolge des Fächerlisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Fächerlisten-Eintrags", example = "1")
	public int sortierung = 32000;

	/** Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht", example = "true")
	public boolean istOberstufenFach = false;

	/** Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen). */
	@Schema(description = "gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).",
			example = "true")
	public boolean istPruefungsordnungsRelevant = false;

	/** Gibt an, ob es sich um ein Fremdsprachen-Fach handelt */
	@Schema(description = "gibt an, ob es sich um ein Fremdsprachen-Fach handelt", example = "true")
	public boolean istFremdsprache = false;

	/** Gibt an, ob es sich um ein Fremdsprachen-Fach handelt, welches in der Sekundarstufe II neu einsetzen ist. */
	@Schema(description = "gibt an, ob es sich um ein Fremdsprachen-Fach handelt, welches in der Sekundarstufe II neu einsetzen ist", example = "true")
	public boolean istMoeglichAlsNeueFremdspracheInSekII = false;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = false;

	/** Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört */
	@Schema(description = "das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört", example = "null")
	public String aufgabenfeld = null;

	/** Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachefach handelt. */
	@Schema(description = "die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachefach handelt", example = "null")
	public String bilingualeSprache = null;

	/** Gibt an, ob eine Nachprüfung in diesem Fach möglich ist. */
	@Schema(description = "gibt an, ob eine Nachprüfung in diesem Fach möglich ist", example = "true")
	public boolean istNachpruefungErlaubt = false;

	/** Gibt an, ob das Fach auf einem Zeugnis erscheinen soll. */
	@Schema(description = "gibt an, ob das Fach auf einem Zeugnis erscheinen soll", example = "true")
	public boolean aufZeugnis = false;

	/** Die Bezeichnung des Faches auf allgemeinen Zeugnissen */
	@Schema(description = "die Bezeichnung des Faches auf allgemeinen Zeugnissen", example = "Mathematik")
	public @NotNull String bezeichnungZeugnis = "";

	/** Die Bezeichnung des Faches auf Überweisungs-Zeugnissen */
	@Schema(description = "die Bezeichnung des Faches auf Überweisungs-Zeugnissen", example = "Mathematik")
	public @NotNull String bezeichnungUeberweisungszeugnis = "";

	/** Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen. */
	@Schema(description = "gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen", example = "100000")
	public int maxZeichenInFachbemerkungen = Integer.MAX_VALUE;

	/** Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht. */
	@Schema(description = "gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht", example = "true")
	public boolean istSchriftlichZK = false;

	/** Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertest wird (Berufskolleg). */
	@Schema(description = "gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg)", example = "true")
	public boolean istSchriftlichBA = false;

	/** Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg). */
	@Schema(description = "gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg)", example = "true")
	public boolean istFHRFach = false;

	/** Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussbrechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde. */
	@Schema(description = "gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussbrechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.",
			example = "true")
	public boolean holeAusAltenLernabschnitten = false;

	/** Gibt an, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public Boolean referenziertInAnderenTabellen = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public FachDaten() {
		// leer
	}

}
