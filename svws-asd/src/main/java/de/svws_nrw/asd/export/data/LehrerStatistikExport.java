package de.svws_nrw.asd.export.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Stammdaten eines Lehrer-Eintrags (L61)
 */
@XmlRootElement
@Schema(description = "Die Stammdaten eines Lehrer-Eintrags (L61)")
@TranspilerDTO
public class LehrerStatistikExport {

	//**** Nicht Abschnittsbezogene Daten

	/** Satzschlüssel: Das Kürzel des Lehrers. */
	@Schema(description = "satzschlüssel: das Kürzel des Lehrers.", example = "MUS")
	public @NotNull String kuerzel = "";

	/** Der Nachname des Lehrers. */
	@Schema(description = "der Nachname des Lehrers.", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Der Vorname des Lehrers. */
	@Schema(description = "der Vorname des Lehrers.", example = "Max")
	public @NotNull String vorname = "";

	/** Der Tag des Geburtsdatums des Lehrers. */
	@Schema(description = "der Tag des Geburtsdatums des Lehrers.", example = "30")
	public String geburtsdatumTag = "";

	/** Der Monat des Geburtsdatums des Lehrers. */
	@Schema(description = "der Monat des Geburtsdatums des Lehrers.", example = "11")
	public String geburtsdatumMonat = "";

	/** Das Jahr des Geburtsdatums des Lehrers. */
	@Schema(description = "das Jahr des Geburtsdatums des Lehrers.", example = "1980")
	public String geburtsdatumJahr = "";

	/** Das Geschlecht das Lehrers. */
	@Schema(description = "das Geschlecht das Lehrers", example = "3")
	public int geschlecht = 7; // 7 - Ohne Angabe

	/** Die Staatsangehörigkeit des Lehrers. */
	@Schema(description = "die Staatsangehörigkeit des Lehrers (Deutsch:'   ' -> Leer(3))", example = "123")
	public Long staatsangehoerigkeit = 0L;

	/** Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit). */
	@Schema(description = "das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit).",
			example = "L")
	public String rechtsverhaeltnis = "";

	/** Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.). */
	@Schema(description = "die Art der Beschäftigung (Vollzeit, Teilzeit, etc.).", example = "T")
	public String beschaeftigungsart = "";

	/** [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) */
	@Schema(description = "[ASD] der Einsatzstatus (z.B. Stammschule, nur hier tätig). Ein leerer Eintrag wird als DEFAULT interpretiert, und bedeutet \"Nur an Stammschule tätig.\"",
			example = "A")
	public String einsatzstatus = "";

	/** Das Pflichtstundensoll des Lehrers. */
	@Schema(description = "das Pflichtstundensoll des Lehrers", example = "18.5")
	public double pflichtstundensoll = 0.0;

	/** Der zu erteilende Unterricht des Lehrers. */
	@Schema(description = "der zu erteilende Unterricht des Lehrers", example = "15.5")
	public double zuErteilenderUnterricht = 0.0;

	/** Erteilter Unterricht des Lehrers. */
	@Schema(description = "erteilter Unterricht des Lehrers", example = "15.5")
	public double erteilerUnterricht = 0.0;

	/** Die Daten zu den Lehrämtern (L62). */
	@ArraySchema(schema = @Schema(implementation = LehrerLehraemterStatistikExport.class,
			description = "ein Array Daten zu den Lehrämtern (L62)"))
	public @NotNull List<LehrerLehraemterStatistikExport> lehraemterStatistikExport = new ArrayList<>();

	/** Die Daten zu den Fachrichtungen (L63). */
	@ArraySchema(schema = @Schema(implementation = LehrerFachrichtungenStatistikExport.class,
			description = "ein Array mit den Daten zu den Fachrichtungen (L63)"))
	public @NotNull List<LehrerFachrichtungenStatistikExport> fachrichtungenStatistikExport = new ArrayList<>();

	/** Die Daten zu der Lehrbefähigungen (L64). */
	@ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungenStatistikExport.class,
			description = "ein Array mit Daten zu der Lehrbefähigungen (L64)"))
	public @NotNull List<LehrerLehrbefaehigungenStatistikExport> lehrbefaehigungenStatistikExport = new ArrayList<>();

	/** Die nicht unterrichtlichen Tätigkeiten / Anrechungen (L65). */
	@ArraySchema(schema = @Schema(implementation = LehrerAnrechungenStatistikExport.class,
			description = "ein Array nicht unterrichtlichen Tätigkeiten / Anrechungen (L65)"))
	public @NotNull List<LehrerAnrechungenStatistikExport> anrechungenStatistikExport = new ArrayList<>();

	/** Die Mehrleistungen (L66). */
	@ArraySchema(schema = @Schema(implementation = LehrerMehrleistungenStatistikExport.class,
			description = "ein Array mit Mehrleitsungen (L66)"))
	public @NotNull List<LehrerMehrleistungenStatistikExport> mehrleistungenStatistikExport = new ArrayList<>();

	/** Die Minderleistungen (L67). */
	@ArraySchema(schema = @Schema(implementation = LehrerMinderleistungenStatistikExport.class,
			description = "ein Array mit Minderleistungen (L67)"))
	public @NotNull List<LehrerMinderleistungenStatistikExport> minderleistungenStatistikExport = new ArrayList<>();

	/** Die erteilten Stunden nach Bildungsbereich (nur FW) (L68). */
	@ArraySchema(schema = @Schema(implementation = LehrerErteilteStundenStatistikExport.class,
			description = "ein Array mit erteilten Stunden nach Bildungsbereich (nur FW) (L68)"))
	public @NotNull List<LehrerErteilteStundenStatistikExport> erteilteStundenStatistikExport = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerStatistikExport() {
		// leer
	}

}
