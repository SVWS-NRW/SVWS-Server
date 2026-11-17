package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten der Zusatzerhebungen zur amtlichen Schulstatistik für einen Schuljahresabschnitt.
 */
@XmlRootElement
@Schema(description = "Die Daten der Zusatzerhebungen zur amtlichen Schulstatistik für einen Schuljahresabschnitt.")
@TranspilerDTO
public class ZusatzerhebungenStatistikGesamt {

	/** Die ID des Schuljahresabschnitts, zu welchem diese Daten gehören */
	@Schema(description = "die ID des Schuljahresabschnitts, zu welchem diese Daten gehören", example = "1")
	public long idSchuljahresabschnitt;

	/** Name des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Name des Statistik-Ansprechpartners für IT.NRW", example = "Max Mustermann")
	public String statistikAnsprechpartnerName = null;

	/** Telefonnummer des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Telefonnummer des Statistik-Ansprechpartners für IT.NRW", example = "0211-980500")
	public String statistikAnsprechpartnerTelefon = null;

	/** Email des Statistik-Ansprechpartners für IT.NRW */
	@Schema(description = "Email des Statistik-Ansprechpartners für IT.NRW", example = "Max.Mustermann@musterschule.de")
	public String statistikAnsprechpartnerEmail = null;

	/** Gibt an, ob die Schule internationale Kontakte pflegt */
	@Schema(description = "Gibt an, ob die Schule internationale Kontakte pflegt", example = "false")
	public boolean hatInternationaleKontakte;

	/** Die Anzahl der Schulanmeldungen nach § 35 SchulG */
	@Schema(description = "die Anzahl der Schulanmeldungen nach § 35 SchulG", example = "113")
	public Integer anzahlSchulanmeldungen = null;

	/** Die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen */
	@Schema(description = "die Anzahl der Zurückstellungsverfahren im Zuge der Anmeldungen", example = "17")
	public Integer anzahlZurueckstellungsverfahren = null;

	/** Die Anzahl der tatsächlichen Zurückstellungen insgesamt */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen insgesamt", example = "15")
	public Integer anzahlZurueckstellungenInsgesamt = null;

	/** Die Anzahl der tatsächlichen Zurückstellungen weiblich */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen weiblich", example = "8")
	public Integer anzahlZurueckstellungenWeiblich = null;

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen Ausländer zusammen", example = "5")
	public Integer anzahlZurueckstellungenAuslaenderZusammen = null;

	/** Die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich */
	@Schema(description = "die Anzahl der tatsächlichen Zurückstellungen Ausländer weiblich", example = "3")
	public Integer anzahlZurueckstellungenAuslaenderWeiblich = null;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Erhebungsvorjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 4. Quartal des Vorjahres 6 Jahre alt geworden sind", example = "2")
	public Integer anzahlZurueckstellungenInsgesamtQuartal4 = null;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 1. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "3")
	public Integer anzahlZurueckstellungenInsgesamtQuartal1 = null;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 2. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "4")
	public Integer anzahlZurueckstellungenInsgesamtQuartal2 = null;

	/** Die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind */
	@Schema(description = "die Anzahl der tatsächlich zurückgestellten Kinder, die im 3. Quartal des Erhebungsjahres 6 Jahre alt geworden sind", example = "6")
	public Integer anzahlZurueckstellungenInsgesamtQuartal3 = null;

	/** Die Art des Internats */
	@Schema(description = "die Art des Internats", example = "3")
	public Long idInternatArt = null;

	/** Die Anzahl der möglichen Internatsplätze für Jungen */
	@Schema(description = "die Anzahl der möglichen Internatsplätze für Jungen", example = "32")
	public Integer anzahlInternatplaetzeJungenMoeglich = null;

	/** Die Anzahl der belegten Internatsplätze für Jungen */
	@Schema(description = "die Anzahl der belegten Internatsplätze für Jungen", example = "30")
	public Integer anzahlInternatplaetzeJungenBelegt = null;

	/** Die Anzahl der möglichen Internatsplätze für Mädchen */
	@Schema(description = "die Anzahl der möglichen Internatsplätze für Mädchen", example = "24")
	public Integer anzahlInternatplaetzeMaedchenMoeglich = null;

	/** Die Anzahl der belegten Internatsplätze für Mädchen */
	@Schema(description = "die Anzahl der belegten Internatsplätze für Mädchen", example = "23")
	public Integer anzahlInternatplaetzeMaedchenBelegt = null;

	/** Die Anzahl der möglichen geschlechtsneutralen Internatsplätze */
	@Schema(description = "die Anzahl der möglichen geschlechtsneutralen Internatsplätze", example = "84")
	public Integer anzahlInternatplaetzeNeutralMoeglich = null;

	/** Die Anzahl der belegten geschlechtsneutralen Internatsplätze */
	@Schema(description = "die Anzahl der belegten geschlechtsneutralen Internatsplätze", example = "79")
	public Integer anzahlInternatplaetzeNeutralBelegt = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend insgesamt", example = "199.5")
	public Double durchschnittKlinikschuleAllgemeinbildendInsgesamt = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - allgemeinbildend mit Schwerstbehinderung", example = "49.8")
	public Double durchschnittKlinikschuleAllgemeinbildendSchwerstbehinderte = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit insgesamt", example = "15.7")
	public Double durchschnittKlinikschuleBerufsbildendTeilzeitInsgesamt = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Teilzeit mit Schwerstbehinderung", example = "2.1")
	public Double durchschnittKlinikschuleBerufsbildendTeilzeitSchwerstbehinderte = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit insgesamt", example = "20.8")
	public Double durchschnittKlinikschuleBerufsbildendVollzeitInsgesamt = null;

	/** Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung */
	@Schema(description = "Klinikschule - Die durchschnittliche Anzahl der Schüler im letzten Schuljahr - berufsbildend in Vollzeit mit Schwerstbehinderung", example = "3.5")
	public Double durchschnittKlinikschuleBerufsbildendVollzeitSchwerstbehinderte = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ZusatzerhebungenStatistikGesamt() {
		// leer
	}

}
