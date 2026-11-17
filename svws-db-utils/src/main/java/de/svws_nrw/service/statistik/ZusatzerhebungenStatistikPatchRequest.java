package de.svws_nrw.service.statistik;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Der Patch-Request für {@link ZusatzerhebungenStatistikGesamt}
 */
public class ZusatzerhebungenStatistikPatchRequest {

	/** Name des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Name des Statistik-Ansprechpartners für IT.NRW", example = "Max Mustermann")
	public JsonNullable<@Size(max = 255) String> statistikAnsprechpartnerName = JsonNullable.undefined();

	/** Telefonnummer des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Telefonnummer des Statistik-Ansprechpartners für IT.NRW", example = "0211-980500")
	public JsonNullable<@Size(max = 20) String> statistikAnsprechpartnerTelefon = JsonNullable.undefined();

	/** Email des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Email des Statistik-Ansprechpartners für IT.NRW", example = "Max.Mustermann@musterschule.de")
	public JsonNullable<@Size(max = 100) String> statistikAnsprechpartnerEmail = JsonNullable.undefined();

	/** Gibt an, ob die Schule internationale Kontakte pflegt */
	@Schema(description = "Gibt an, ob die Schule internationale Kontakte pflegt", example = "false")
	public JsonNullable<@NotNull Boolean> hatInternationaleKontakte = JsonNullable.undefined();

	/** Die Anzahl der Schulanmeldungen nach § 35 SchulG */
	@Schema(description = "die Anzahl der Schulanmeldungen nach § 35 SchulG", example = "113")
	public JsonNullable<@PositiveOrZero Integer> anzahlSchulanmeldungen = JsonNullable.undefined();

	/** Die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen */
	@Schema(description = "die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen", example = "17")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungsverfahren = JsonNullable.undefined();

	/** Die Anzahl der tatsächlichen Zurückstellungen insgesamt */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen insgesamt", example = "15")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenInsgesamt = JsonNullable.undefined();

	/** Die Anzahl der tatsächlichen Zurückstellungen weiblich */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen weiblich", example = "8")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenWeiblich = JsonNullable.undefined();

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen", example = "5")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenAuslaenderZusammen = JsonNullable.undefined();

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich", example = "3")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenAuslaenderWeiblich = JsonNullable.undefined();

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Erhebungsvorjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Vorjahres 6 Jahre alt geworden sind", example = "2")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenInsgesamtQuartal4 = JsonNullable.undefined();

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "3")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenInsgesamtQuartal1 = JsonNullable.undefined();

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "4")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenInsgesamtQuartal2 = JsonNullable.undefined();

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "6")
	public JsonNullable<@PositiveOrZero Integer> anzahlZurueckstellungenInsgesamtQuartal3 = JsonNullable.undefined();

	/** Die Art des Internats */
	@Schema(description = "die Art des Internats", example = "3")
	public JsonNullable<Long> idInternatArt = JsonNullable.undefined();

	/** Die Anzahl der möglichen Internatsplätze für Jungen */
	@Schema(description = "die Anzahl der möglichen Internatsplätze für Jungen", example = "32")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeJungenMoeglich = JsonNullable.undefined();

	/** Die Anzahl der belegten Internatsplätze für Jungen */
	@Schema(description = "die Anzahl der belegten Internatsplätze für Jungen", example = "30")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeJungenBelegt = JsonNullable.undefined();

	/** Die Anzahl der möglichen Internatsplätze für Mädchen */
	@Schema(description = "die Anzahl der möglichen Internatsplätze für Mädchen", example = "24")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeMaedchenMoeglich = JsonNullable.undefined();

	/** Die Anzahl der belegten Internatsplätze für Mädchen */
	@Schema(description = "die Anzahl der belegten Internatsplätze für Mädchen", example = "23")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeMaedchenBelegt = JsonNullable.undefined();

	/** Die Anzahl der möglichen geschlechtsneutralen Internatsplätze */
	@Schema(description = "die Anzahl der möglichen geschlechtsneutralen Internatsplätze", example = "84")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeNeutralMoeglich = JsonNullable.undefined();

	/** Die Anzahl der belegten geschlechtsneutralen Internatsplätze */
	@Schema(description = "die Anzahl der belegten geschlechtsneutralen Internatsplätze", example = "79")
	public JsonNullable<@PositiveOrZero Integer> anzahlInternatplaetzeNeutralBelegt = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt", example = "199.5")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleAllgemeinbildendInsgesamt = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung", example = "49.8")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt", example = "15.7")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung", example = "2.1")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt", example = "20.8")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = JsonNullable.undefined();

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung", example = "3.5")
	public JsonNullable<@PositiveOrZero Double> durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = JsonNullable.undefined();

}
