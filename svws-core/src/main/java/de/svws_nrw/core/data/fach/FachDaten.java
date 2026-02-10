package de.svws_nrw.core.data.fach;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Fachs.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Fachs.")
@TranspilerDTO
public class FachDaten {

	/** Die ID des Fachs. */
	@Schema(description = "die ID des Fachs", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das eindeutige Kürzel des Fachs */
	@Schema(description = "das eindeutige Kürzel des Fachs.", example = "M")
	public @NotNull String kuerzel = "";

	/** Das Statistik-Kürzel des Fachs */
	@Schema(description = "das Statistik-Kürzel des Fachs.", example = "M")
	public @NotNull String kuerzelStatistik = "";

	/** Die Bezeichnung des Fachs */
	@Schema(description = "die Bezeichnung des Fachs.", example = "Mathematik")
	public @NotNull String bezeichnung = "";

	/** Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört. */
	@Schema(description = "Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört.", example = "Aufgabenfeld I")
	public String aufgabenfeld;

	/** Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt. */
	@Schema(description = "Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.", example = "Englisch")
	public String bilingualeSprache;

	/** Gibt an, ob das Fach auf einem Zeugnis erscheinen soll. */
	@Schema(description = "Gibt an, ob das Fach auf einem Zeugnis erscheinen soll.", example = "true")
	public boolean aufZeugnis;

	/** Die Bezeichnung des Fachs auf allgemeinen Zeugnissen. */
	@Schema(description = "Die Bezeichnung des Fachs auf allgemeinen Zeugnissen.", example = "Mathematik")
	public String bezeichnungZeugnis;

	/** Die Bezeichnung des Fachs auf Überweisungs-Zeugnissen. */
	@Schema(description = "Die Bezeichnung des Fachs auf Überweisungs-Zeugnissen.", example = "Mathematik")
	public String bezeichnungUeberweisungszeugnis;

	/** Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht. */
	@Schema(description = "Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.", example = "true")
	public boolean istOberstufenFach;

	/** Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen). */
	@Schema(description = "Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).",
			example = "true")
	public boolean istPruefungsordnungsRelevant;

	/** Gibt an, ob es sich um ein Fremdsprachen-Fach handelt. */
	@Schema(description = "Gibt an, ob es sich um ein Fremdsprachen-Fach handelt.", example = "true")
	public boolean istFremdsprache;

	/** Gibt an, ob es sich um ein Fremdsprachen-Fach handelt, welches in der Sekundarstufe II neu einsetzbar ist. */
	@Schema(description = "Gibt an, ob es sich um ein Fremdsprachen-Fach handelt, welches in der Sekundarstufe II neu einsetzbar ist.", example = "true")
	public boolean istMoeglichAlsNeueFremdspracheInSekII;

	/** Gibt an, ob eine Nachprüfung in diesem Fach möglich ist. */
	@Schema(description = "Gibt an, ob eine Nachprüfung in diesem Fach möglich ist.", example = "true")
	public boolean istNachpruefungErlaubt;

	/** Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht. */
	@Schema(description = "Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht.", example = "true")
	public boolean istSchriftlichZK;

	/** Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertest wird (Berufskolleg). */
	@Schema(description = "Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertet wird (Berufskolleg).", example = "true")
	public boolean istSchriftlichBA;

	/** Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg). */
	@Schema(description = "Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg).", example = "true")
	public boolean istFHRFach;

	/** Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussbrechnung berücksichtigt wird,
	 * sofern es im aktuellen Abschnitt nicht belegt wurde. */
	@Schema(description = "Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussbrechnung "
			+ "berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.",
			example = "true")
	public boolean holeAusAltenLernabschnitten;

	/** Gibt die maximale Anzahl an Zeichen an, die in Fachbemerkungen genutzt werden dürfen. */
	@Schema(description = "Gibt die maximale Anzahl an Zeichen an, die in Fachbemerkungen genutzt werden dürfen.", example = "100000")
	public Integer maxZeichenInFachbemerkungen;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.", example = "true")
	public boolean istSichtbar;

	/** Die Sortierreihenfolge des Fächerlisten-Eintrags. */
	@Schema(description = "Die Sortierreihenfolge des Fächerlisten-Eintrags.", example = "1")
	public int sortierung;

	/** Gibt an, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

}
