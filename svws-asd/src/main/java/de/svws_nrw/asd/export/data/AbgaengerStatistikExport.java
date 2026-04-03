package de.svws_nrw.asd.export.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Summe der Abgänger (V51).
 *
 */
@XmlRootElement
@Schema(description = "Die Summe der Abgänger (V51).")
@TranspilerDTO
public class AbgaengerStatistikExport {

	/** Satzschlüssel: Der Jahrgang der Schüler an Allgemeinbildenden Schulen. */
	@Schema(description = "satzschlüssel: der Jahrgang der Schüler an Allgemeinbildenden Schulen", example = "09")
	public String jahrgang = "";

	/** Satzschlüssel: Das Bildungsgangkennzeichen der Schüler. */
	@Schema(description = "satzschlüssel: das Bildungsgangkennzeichen der Schüler", example = "A")
	public String bildungsgangkennzeichen = "";

	/** Satzschlüssel: Die Schulgliederung der Schüler. */
	@Schema(description = "satzschlüssel: die Schulgliederung der Schüler", example = "H")
	public String schulgliederung = "";

	/** Satzschlüssel: Die Fachklasse der Schüler. */
	@Schema(description = "satzschlüssel: die Fachklasse der Schüler", example = "50200")
	public String fachklasse = "";

	/** Satzschlüssel: Die Klassenart der Schüler. */
	@Schema(description = "satzschlüssel: die Klassenart der Schüler", example = "RK")
	public String klassenart = "";

	/** Satzschlüssel: Der Jahrgang der Schüler an Berufskollegs. */
	@Schema(description = "satzschlüssel: der Jahrgang der Schüler an Berufskollegs", example = "02")
	public String jahrgangBK = "";

	/** Satzschlüssel: Der erste Förderschwerpunkt der Schüler. */
	@Schema(description = "satzschlüssel: der erste Förderschwerpunkt der Schüler", example = "SB")
	public String foerderschwerpunkt1 = "";

	/** Satzschlüssel: Der zweite Förderschwerpunkt der Schüler. */
	@Schema(description = "satzschlüssel: der zweite Förderschwerpunkt der Schüler", example = "GB")
	public String foerderschwerpunkt2 = "";

	/** Satzschlüssel: Der Schüler hat einen Schwerbehinderungsnachweis. */
	@Schema(description = "satzschlüssel: der Schüler hat einen Schwerbehinderungsnachweis", example = "true")
	public boolean hatSchwerbehinderungsNachweis = false;

	/** Die Summe der abgehenden Schüler insgesamt. */
	@Schema(description = "die Summe der abgehenden Schüler insgesamt", example = "51")
	public long abgaengeInsgesamtZusammen = 0;

	/** Die Summe der abgehenden Schüler insgesamt Weiblich. */
	@Schema(description = "die Summe der abgehenden Schüler insgesamt Weiblich", example = "25")
	public long abgaengeInsgesamtWeiblich = 0;

	/** Die Summe der abgehenden ausländischen Schüler zusammen. */
	@Schema(description = "die Summe der abgehenden ausländischen Schüler zusammen", example = "15")
	public long abgaengeAuslaenderZusammen = 0;

	/** Die Summe der abgehenden ausländischen Schüler  Weiblich. */
	@Schema(description = "die Summe der abgehenden ausländischen Schüler  Weiblich", example = "8")
	public long abgaengeAuslaenderWeiblich = 0;

	/** Die Summe der Vorjahresschüler insgesamt. */
	@Schema(description = "die Summe der Vorjahresschüler insgesamt", example = "51")
	public long vorjahresSchuelerInsgesamtZusammen = 0;

	/** Die Summe der Vorjahresschüler insgesamt Weiblich. */
	@Schema(description = "die Summe der Vorjahresschüler insgesamt Weiblich", example = "25")
	public long vorjahresSchuelerInsgesamtWeiblich = 0;

	/** Die Summe der ausländischen Vorjahresschüler zusammen. */
	@Schema(description = "die Summe der ausländischen Vorjahresschüler zusammen", example = "15")
	public long vorjahresSchuelerAuslaenderZusammen = 0;

	/** Die Summe der ausländischen Vorjahresschüler Weiblich. */
	@Schema(description = "die Summe der ausländischen Vorjahresschüler Weiblich", example = "8")
	public long vorjahresSchuelerAuslaenderWeiblich = 0;

	/** Die Bestätigung, dass für diesen Satz keine Abgänger vorliegen. */
	@Schema(description = "die Bestätigung, dass für diesen Satz keine Abgänger vorliegen", example = "true")
	public boolean bestaetigungKeineAbgaenger = false;

	/** Das Datumsstempel der Daten zu den Vorjahresschülern. */
	@Schema(description = "das Datumsstempel der Daten zu den Vorjahresschülern", example = "2026")
	public String datumStempelVorjahresSchueler = "";

	/** Kennzeichnung Vorjahresschülerdatensatz. */
	@Schema(description = "Kennzeichnung Vorjahresschülerdatensatz", example = "true")
	public boolean istVorgabedatensatz = false;

	/** Die Summen der Abgänger im Detail (V54). */
	@ArraySchema(schema = @Schema(implementation = AbgaengerDetailStatistikExport.class,
			description = "die Summen der Abgänger im Detail (V54)"))
	public @NotNull List<AbgaengerDetailStatistikExport> abgaengerDetailStatistikExport = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AbgaengerStatistikExport() {
		// leer
	}

}
