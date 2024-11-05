package de.svws_nrw.module.reporting.types.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;

import java.util.List;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturdaten.
 */
public class ReportingSchuelerGostAbitur {

	/** Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	protected int abiturjahr;

	/** Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern). */
	protected String besondereLernleistung;

	/** Ggf. die Note einer externen besonderen Lernleistung. */
	protected Note besondereLernleistungNote;

	/** Das Thema der besonderen Lernleistung. */
	protected String besondereLernleistungThema;

	/** Gibt für die einzelnen GostHalbjahr-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	protected boolean[] bewertetesHalbjahr;

	/** Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt. */
	protected String bilingualeSprache;

	/** Die Anzahl der Kurse in der Qualifikationsphase. */
	protected Integer block1AnzahlKurse;

	/** Die Anzahl der Gesamtdefizite in der Qualifikationsphase. */
	protected Integer block1DefiziteGesamt;

	/** Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase. */
	protected Integer block1DefiziteLK;

	/** Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase. */
	protected long block1FehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase. */
	protected long block1FehlstundenUnentschuldigt;

	/** Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase. */
	protected Double block1NotenpunkteDurchschnitt;

	/** Die Punktsumme aller Grundkurse in der Qualifikationsphase. */
	protected Integer block1PunktSummeGK;

	/** Die Punktsumme aller Leistungskurse in der Qualifikationsphase. */
	protected Integer block1PunktSummeLK;

	/** Die normierte Punktsumme aller Kurse in der Qualifikationsphase. */
	protected Integer block1PunktSummeNormiert;

	/** Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde. */
	protected Boolean block1Zulassung;

	/** Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an. */
	protected Integer block2DefiziteGesamt;

	/** Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II). */
	protected Integer block2DefiziteLK;

	/** Die Punktsumme im Abiturbereich (Block II). */
	protected Integer block2PunktSumme;

	/** Die Liste mit den Fachbelegungen in der Oberstufe. */
	protected List<ReportingGostAbiturFachbelegung> fachbelegungen;

	/** Die Angabe, ob freiwillig von der Abiturprüfung zurückgetreten wurde. */
	protected boolean freiwilligerRuecktritt;

	/** Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II). */
	protected Integer gesamtPunkte;

	/** Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde. */
	protected Integer gesamtPunkteVerbesserung;

	/** Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde. */
	protected Integer gesamtPunkteVerschlechterung;

	/** Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	protected String note;

	/** Das Projektkursthema, sofern ein Projektkurs belegt wurde. */
	protected String projektkursThema;

	/** Die Angabe, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	protected Boolean pruefungBestanden;

	/** Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	protected int schuljahrAbitur;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 * @param besondereLernleistung Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern).
	 * @param besondereLernleistungNote Ggf. die Note einer externen besonderen Lernleistung.
	 * @param besondereLernleistungThema Das Thema der besonderen Lernleistung.
	 * @param bewertetesHalbjahr Gibt für die einzelnen GostHalbjahr-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 * @param bilingualeSprache Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 * @param block1AnzahlKurse Die Anzahl der Kurse in der Qualifikationsphase.
	 * @param block1DefiziteGesamt Die Anzahl der Gesamtdefizite in der Qualifikationsphase.
	 * @param block1DefiziteLK Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase.
	 * @param block1FehlstundenGesamt Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase.
	 * @param block1FehlstundenUnentschuldigt Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase.
	 * @param block1NotenpunkteDurchschnitt Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase.
	 * @param block1PunktSummeGK Die Punktsumme aller Grundkurse in der Qualifikationsphase.
	 * @param block1PunktSummeLK Die Punktsumme aller Leistungskurse in der Qualifikationsphase.
	 * @param block1PunktSummeNormiert Die normierte Punktsumme aller Kurse in der Qualifikationsphase.
	 * @param block1Zulassung Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde.
	 * @param block2DefiziteGesamt Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an.
	 * @param block2DefiziteLK Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II).
	 * @param block2PunktSumme Die Punktsumme im Abiturbereich (Block II).
	 * @param fachbelegungen Die Liste mit den Fachbelegungen in der Oberstufe.
	 * @param freiwilligerRuecktritt Die Angabe, ob freiwillig von der Abiturprüfung zurückgetreten wurde.
	 * @param gesamtPunkte Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II).
	 * @param gesamtPunkteVerbesserung Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde.
	 * @param gesamtPunkteVerschlechterung Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde.
	 * @param note Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 * @param projektkursThema Das Projektkurs thema, sofern ein Projektkurs belegt wurde.
	 * @param pruefungBestanden Die Angabe, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 * @param schuljahrAbitur Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 */
	public ReportingSchuelerGostAbitur(final int abiturjahr, final String besondereLernleistung, final Note besondereLernleistungNote,
			final String besondereLernleistungThema, final boolean[] bewertetesHalbjahr, final String bilingualeSprache, final Integer block1AnzahlKurse,
			final Integer block1DefiziteGesamt, final Integer block1DefiziteLK, final long block1FehlstundenGesamt, final long block1FehlstundenUnentschuldigt,
			final Double block1NotenpunkteDurchschnitt, final Integer block1PunktSummeGK, final Integer block1PunktSummeLK,
			final Integer block1PunktSummeNormiert, final Boolean block1Zulassung, final Integer block2DefiziteGesamt, final Integer block2DefiziteLK,
			final Integer block2PunktSumme, final List<ReportingGostAbiturFachbelegung> fachbelegungen, final boolean freiwilligerRuecktritt,
			final Integer gesamtPunkte, final Integer gesamtPunkteVerbesserung, final Integer gesamtPunkteVerschlechterung, final String note,
			final String projektkursThema, final Boolean pruefungBestanden, final int schuljahrAbitur) {
		this.abiturjahr = abiturjahr;
		this.besondereLernleistung = besondereLernleistung;
		this.besondereLernleistungNote = besondereLernleistungNote;
		this.besondereLernleistungThema = besondereLernleistungThema;
		this.bewertetesHalbjahr = bewertetesHalbjahr;
		this.bilingualeSprache = bilingualeSprache;
		this.block1AnzahlKurse = block1AnzahlKurse;
		this.block1DefiziteGesamt = block1DefiziteGesamt;
		this.block1DefiziteLK = block1DefiziteLK;
		this.block1FehlstundenGesamt = block1FehlstundenGesamt;
		this.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;
		this.block1NotenpunkteDurchschnitt = block1NotenpunkteDurchschnitt;
		this.block1PunktSummeGK = block1PunktSummeGK;
		this.block1PunktSummeLK = block1PunktSummeLK;
		this.block1PunktSummeNormiert = block1PunktSummeNormiert;
		this.block1Zulassung = block1Zulassung;
		this.block2DefiziteGesamt = block2DefiziteGesamt;
		this.block2DefiziteLK = block2DefiziteLK;
		this.block2PunktSumme = block2PunktSumme;
		this.fachbelegungen = fachbelegungen;
		this.freiwilligerRuecktritt = freiwilligerRuecktritt;
		this.gesamtPunkte = gesamtPunkte;
		this.gesamtPunkteVerbesserung = gesamtPunkteVerbesserung;
		this.gesamtPunkteVerschlechterung = gesamtPunkteVerschlechterung;
		this.note = note;
		this.projektkursThema = projektkursThema;
		this.pruefungBestanden = pruefungBestanden;
		this.schuljahrAbitur = schuljahrAbitur;

		this.fachbelegungen().sort(ReportingGostAbiturFachbelegung::compareToGost);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Projektkurs belegt wurde.
	 *
	 * @return Projektkursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean projektkursVorhanden() {
		return !this.fachbelegungen.stream()
				.filter(f -> ((f != null) && (Fachgruppe.FG_PX == f.fach().fachgruppe())))
				.toList()
				.isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Religionskurs belegt wurde.
	 *
	 * @return Religionskursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean religionVorhanden() {
		return !this.fachbelegungen.stream()
				.filter(f -> ((f != null) && (Fachgruppe.FG_RE == f.fach().fachgruppe())))
				.toList()
				.isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Vertiefungskurs belegt wurde.
	 *
	 * @return Vertiefungskursbelegung in Q-Phase vorhanden.
	 */
	@JsonIgnore
	public boolean vertiefungskursVorhanden() {
		final List<ReportingGostAbiturFachbelegung> listVertiefungskurse =
				this.fachbelegungen.stream().filter(f -> (Fachgruppe.FG_VX == f.fach().fachgruppe())).toList();
		if (listVertiefungskurse.isEmpty())
			return false;

		for (final ReportingGostAbiturFachbelegung vertiefung : listVertiefungskurse) {
			for (int i = 0; i < 6; i++) {
				if ((vertiefung.halbjahresbelegungen()[i] != null) && ('Q' == vertiefung.halbjahresbelegungen()[i].halbjahrKuerzel().charAt(0)))
					return true;
			}
		}

		return false;
	}



	// ##### Getter #####

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 *
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern).
	 *
	 * @return Inhalt des Feldes besondereLernleistung
	 */
	public String besondereLernleistung() {
		return besondereLernleistung;
	}

	/**
	 * Ggf. Note einer externen besonderen Lernleistung.
	 *
	 * @return Inhalt des Feldes besondereLernleistungNote
	 */
	public Note besondereLernleistungNote() {
		return besondereLernleistungNote;
	}

	/**
	 * Das Thema der besonderen Lernleistung.
	 *
	 * @return Inhalt des Feldes besondereLernleistungThema
	 */
	public String besondereLernleistungThema() {
		return besondereLernleistungThema;
	}

	/**
	 * Für die einzelnen GostHalbjahr wird angegeben, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 *
	 * @return Inhalt des Feldes bewertetesHalbjahr
	 */
	public final boolean[] bewertetesHalbjahr() {
		return bewertetesHalbjahr;
	}

	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 *
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Die Anzahl der Kurse in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1AnzahlKurse
	 */
	public Integer block1AnzahlKurse() {
		return block1AnzahlKurse;
	}

	/**
	 * Die Anzahl der Gesamtdefizite in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1DefiziteGesamt
	 */
	public Integer block1DefiziteGesamt() {
		return block1DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1DefiziteLK
	 */
	public Integer block1DefiziteLK() {
		return block1DefiziteLK;
	}

	/**
	 * Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1FehlstundenGesamt
	 */
	public long block1FehlstundenGesamt() {
		return block1FehlstundenGesamt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1FehlstundenUnentschuldigt
	 */
	public long block1FehlstundenUnentschuldigt() {
		return block1FehlstundenUnentschuldigt;
	}

	/**
	 * Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1NotenpunkteDurchschnitt
	 */
	public Double block1NotenpunkteDurchschnitt() {
		return block1NotenpunkteDurchschnitt;
	}

	/**
	 * Die Punktsumme aller Grundkurse in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1PunktSummeGK
	 */
	public Integer block1PunktSummeGK() {
		return block1PunktSummeGK;
	}

	/**
	 * Die Punktsumme aller Leistungskurse in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1PunktSummeLK
	 */
	public Integer block1PunktSummeLK() {
		return block1PunktSummeLK;
	}

	/**
	 * Die normierte Punktsumme aller Kurse in der Qualifikationsphase.
	 *
	 * @return Inhalt des Feldes block1PunktSummeNormiert
	 */
	public Integer block1PunktSummeNormiert() {
		return block1PunktSummeNormiert;
	}

	/**
	 * Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde.
	 *
	 * @return Inhalt des Feldes block1Zulassung
	 */
	public Boolean block1Zulassung() {
		return block1Zulassung;
	}

	/**
	 * Die Anzahl der Gesamtdefizite im Abiturbereich (Block II).
	 *
	 * @return Inhalt des Feldes block2DefiziteGesamt
	 */
	public Integer block2DefiziteGesamt() {
		return block2DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II).
	 *
	 * @return Inhalt des Feldes block2DefiziteLK
	 */
	public Integer block2DefiziteLK() {
		return block2DefiziteLK;
	}

	/**
	 * Die Punktsumme im Abiturbereich (Block II).
	 *
	 * @return Inhalt des Feldes block2PunktSumme
	 */
	public Integer block2PunktSumme() {
		return block2PunktSumme;
	}

	/**
	 * Die Liste mit den Fachbelegungen in der Oberstufe.
	 *
	 * @return Inhalt des Feldes fachbelegungen
	 */
	public final List<ReportingGostAbiturFachbelegung> fachbelegungen() {
		return fachbelegungen;
	}

	/**
	 * Gibt an, ob freiwillig von der Abiturprüfung zurückgetreten wurde.
	 *
	 * @return Inhalt des Feldes freiwilligerRuecktritt = false
	 */
	public boolean freiwilligerRuecktritt() {
		return freiwilligerRuecktritt;
	}

	/**
	 * Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II).
	 *
	 * @return Inhalt des Feldes gesamtPunkte
	 */
	public Integer gesamtPunkte() {
		return gesamtPunkte;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde
	 *
	 * @return Inhalt des Feldes gesamtPunkteVerbesserung
	 */
	public Integer gesamtPunkteVerbesserung() {
		return gesamtPunkteVerbesserung;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde.
	 *
	 * @return Inhalt des Feldes gesamtPunkteVerschlechterung
	 */
	public Integer gesamtPunkteVerschlechterung() {
		return gesamtPunkteVerschlechterung;
	}

	/**
	 * Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 *
	 * @return Inhalt des Feldes note
	 */
	public String note() {
		return note;
	}

	/**
	 * Das Projektkursthema, sofern ein Projektkurs belegt wurde.
	 *
	 * @return Inhalt des Feldes projektkursThema
	 */
	public String projektkursThema() {
		return projektkursThema;
	}

	/**
	 * Gibt an, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 *
	 * @return Inhalt des Feldes pruefungBestanden
	 */
	public Boolean pruefungBestanden() {
		return pruefungBestanden;
	}

	/**
	 * Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 *
	 * @return Inhalt des Feldes schuljahrAbitur
	 */
	public int schuljahrAbitur() {
		return schuljahrAbitur;
	}

}
