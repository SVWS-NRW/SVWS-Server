package de.svws_nrw.asd.export.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Daten einer (Teil-) Klasse (K81)
 */
@XmlRootElement
@Schema(description = "Die Daten einer (Teil-) Klasse (K81)")
@TranspilerDTO
public class KlassenStatistikExport {

	/** Satzschlüssel: Der Jahrgang. */
	@Schema(description = "satzschlüssel: der Jahrgang.", example = "05")
	public @NotNull String jahrgang = "";

	/** Satzschlüssel: 1. Stelle Parallelität / Das Bildungsgangkennzeichen. */
	@Schema(description = "satzschlüssel: die 1. Stelle Parallelität / das Bildungsgangkennzeichen.", example = "A")
	public @NotNull String bildungsgangkennzeichen = "";

	/** Satzschlüssel: 2. Stelle Parallelität. */
	@Schema(description = "satzschlüssel: die 2. Stelle Parallelität.", example = "A")
	public @NotNull String parallelitaet2 = "";

	/** Satzschlüssel: Die Teilklasse. */
	@Schema(description = "satzschlüssel: die Teilklasse.", example = "02")
	public @NotNull String teilklasse = "";

	/** Die schulinterne Bezeichnung. */
	@Schema(description = "die schulinterne Bezeichnung.", example = "11H12G")
	public @NotNull String schulinterneBezeichnung = "";

	/** Die Schulgliederung bzw. der Bildungsgang. */
	@Schema(description = "die Schulgliederung bzw. der Bildungsgang", example = "B09")
	public String schulgliederung = "";

	/** Die Fachklasse. */
	@Schema(description = "die Fachklasse", example = "50300")
	public String fachklasse = "";

	/** Die Klassenart. */
	@Schema(description = "die Klassenart.", example = "RK")
	public String klassenart = "";

	/** Der Jahrgang der Teilklasse. */
	@Schema(description = "der Jahrgang der Teilklasse.", example = "05")
	public @NotNull String jahrgangTeilklasse = "";

	/** Die Organisationsform. */
	@Schema(description = "die Organisationsform.", example = "1")
	public String organisationsform = "";

	/** Der erste Förderschwerpunkt. */
	@Schema(description = "der erste Förderschwerpunkt.", example = "SB")
	public String foerderschwerpunkt1 = "";

	/** Der zweite Förderschwerpunkt. */
	@Schema(description = "der zweite Förderschwerpunkt.", example = "GB")
	public String foerderschwerpunkt2 = "";

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht */
	@Schema(description = "gibt an, ob eine Schwerbehinderung nachgwiesen ist oder nicht", example = "false")
	public boolean hatSchwerbehinderungsNachweis = false;

	/** Der Bildungsbereich. */
	@Schema(description = "der Bildungsbereich.", example = "A")
	public String bildungsbereich = "";

	/** Ist JVA-Klasse. */
	@Schema(description = "ist JVA-Klasse.", example = "0")
	public String jvaKlasse = "";

	/** Die Art der Reformpädagogik. */
	@Schema(description = "die Art der Reformpädagogik.", example = "M")
	public String reformpaedagogik = "";

	/** Das Kürzel des Klassenlehrers. */
	@Schema(description = "das Kürzel des Klassenlehrers.", example = "MUS")
	public @NotNull String kuerzelKlassenlehrer = "";

	/** Die Schüler der Teilklasse insgesamt. */
	@Schema(description = "die Schüler der Teilklasse insgesamt.", example = "35")
	public int schuelerInsgesamt = 0;

	/** Die Schüler der Teilklasse weiblich. */
	@Schema(description = "die Schüler der Teilklasse weiblich.", example = "32")
	public int schuelerWeiblich = 0;

	/** Die ausländischen Schüler der Teilklasse zusammen. */
	@Schema(description = "die ausländischen Schüler der Teilklasse zusammen.", example = "29")
	public int schuelerAuslaendischZusammen = 0;

	/** Die ausländischen Schüler der Teilklasse weiblich. */
	@Schema(description = "die ausländischen Schüler der Teilklasse weiblich.", example = "27")
	public int schuelerAuslaendischWeiblich = 0;

	/** Das Adresskennzeichen. */
	@Schema(description = "das Adresskennzeichen", example = "A")
	public String adresskennzeichen = "";

	/** Hat Verkürzung halbjährlich. */
	@Schema(description = "hat Verkürzung halbjährlich", example = "false")
	public boolean verkuerzungHalbjaehrlich = false;

	/** Die Daten zur schulischen Herkunft der (Teil-) Klasse (K82). */
	@ArraySchema(schema = @Schema(implementation = KlassenHerkunftStatistikExport.class,
			description = "ein Array mit den Daten zur schulischen Herkunft der (Teil-) Klasse (K82)"))
	public @NotNull List<KlassenHerkunftStatistikExport> klassenHerkunftStatistikExport = new ArrayList<>();

	/** Die Daten zu den Staatsangehörigkeiten der (Teil-) Klasse (K83). */
	@ArraySchema(schema = @Schema(implementation = KlassenNationalitaetenStatistikExport.class,
			description = "ein Array mit den Daten zu den Staatsangehörigkeiten der (Teil-) Klasse (K83)"))
	public @NotNull List<KlassenNationalitaetenStatistikExport> klassenNationalitaetenStatistikExport = new ArrayList<>();

	/** Die Daten der Ausbildungsorte der (Teil-) Klasse (K85). */
	@Schema(description = "Die Daten der Ausbildungsorte der (Teil-) Klasse (K85)")
	public @NotNull KlassenAusbildungsortsartStatistikExport klassenAusbildungsortsartStatistikExport = new KlassenAusbildungsortsartStatistikExport();

	/** Die Daten zur Betreuung der (Teil-) Klasse (K87). */
	@ArraySchema(schema = @Schema(implementation = KlassenBetreuungStatistikExport.class,
			description = "ein Array mit den Daten zur Betreuung der (Teil-) Klasse (K87)"))
	public @NotNull List<KlassenBetreuungStatistikExport> klassenBetreuungStatistikExport = new ArrayList<>();

	/** Die Daten zur regionalen Herkunft der Schüler nach dem Wohnort in der (Teil-) Klasse (X94). */
	@ArraySchema(schema = @Schema(implementation = KlassenWohnorteStatistikExport.class,
			description = "ein Array mit den Daten zur regionalen Herkunft der Schüler nach dem Wohnort in der (Teil-) Klasse (X94)"))
	public @NotNull List<KlassenWohnorteStatistikExport> klassenWohnorteStatistikExport = new ArrayList<>();

	/** Die Daten zur Altersstruktur der Schüler in der (Teil-) Klasse (X95). */
	@ArraySchema(schema = @Schema(implementation = KlassenAltersstrukturStatistikExport.class,
			description = "ein Array mit den Daten zur Altersstruktur der Schüler in der (Teil-) Klasse (X95)"))
	public @NotNull List<KlassenAltersstrukturStatistikExport> klassenAltersstrukturStatistikExport = new ArrayList<>();

	/** Die Daten zur regionalen Lage des Ausbildungsortes der Schüler in der (Teil-) Klasse (X96). */
	@ArraySchema(schema = @Schema(implementation = KlassenAusbildungsorteStatistikExport.class,
			description = "ein Array mit den Daten zur regionalen Lage des Ausbildungsortes der Schüler in der (Teil-) Klasse (X96)"))
	public @NotNull List<KlassenAusbildungsorteStatistikExport> klassenAusbildungsorteStatistikExport = new ArrayList<>();

	/** Die Daten zur Zuwanderungsgeschichte der Schüler in der (Teil-) Klasse (X98). */
	@Schema(description = "ein Array mit den Daten zur Zuwanderungsgeschichte der Schüler in der (Teil-) Klasse (X98)")
	public @NotNull KlassenZuwanderungsgeschichteStatistikExport klassenZuwanderungsgeschichteStatistikExport =
			new KlassenZuwanderungsgeschichteStatistikExport();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public KlassenStatistikExport() {
	}

}
