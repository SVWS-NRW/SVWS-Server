package de.svws_nrw.module.reporting.types.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturdaten.</p>
 * <p>Sie enthält die Daten eines Schüler zum ABitur in der gymnasialen Oberstufe.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 *  einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingSchuelerGostAbitur {

	/** Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	private int abiturjahr;

	/** Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern). */
	private String besondereLernleistung;

	/** Ggf. die Note einer externen besonderen Lernleistung. */
	private Note besondereLernleistungNote;

	/** Das Thema der besonderen Lernleistung. */
	private String besondereLernleistungThema;

	/** Gibt für die einzelnen GostHalbjahr-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	private boolean[] bewertetesHalbjahr;

	/** Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt. */
	private String bilingualeSprache;

	/** Die Anzahl der Kurse in der Qualifikationsphase. */
	private Integer block1AnzahlKurse;

	/** Die Anzahl der Gesamtdefizite in der Qualifikationsphase. */
	private Integer block1DefiziteGesamt;

	/** Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase. */
	private Integer block1DefiziteLK;

	/** Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase. */
	private long block1FehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase. */
	private long block1FehlstundenUnentschuldigt;

	/** Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase. */
	private Double block1NotenpunkteDurchschnitt;

	/** Die Punktsumme aller Grundkurse in der Qualifikationsphase. */
	private Integer block1PunktSummeGK;

	/** Die Punktsumme aller Leistungskurse in der Qualifikationsphase. */
	private Integer block1PunktSummeLK;

	/** Die normierte Punktsumme aller Kurse in der Qualifikationsphase. */
	private Integer block1PunktSummeNormiert;

	/** Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde. */
	private Boolean block1Zulassung;

	/** Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an. */
	private Integer block2DefiziteGesamt;

	/** Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II). */
	private Integer block2DefiziteLK;

	/** Die Punktsumme im Abiturbereich (Block II). */
	private Integer block2PunktSumme;

	/** Die Liste mit den Fachbelegungen in der Oberstufe. */
	private List<ReportingGostAbiturFachbelegung> fachbelegungen = new ArrayList<>();

	/** Die Angabe, ob freiwillig von der Abiturprüfung zurückgetreten wurde. */
	private boolean freiwilligerRuecktritt;

	/** Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II). */
	private Integer gesamtPunkte;

	/** Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde. */
	private Integer gesamtPunkteVerbesserung;

	/** Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde. */
	private Integer gesamtPunkteVerschlechterung;

	/** Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	private String note;

	/** Das Projektkursthema, sofern ein Projektkurs belegt wurde. */
	private String projektkursThema;

	/** Die Angabe, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	private Boolean pruefungBestanden;

	/** Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	private int schuljahrAbitur;


//	/** Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen. */
//	private Sprachendaten sprachendaten = new Sprachendaten();
//
//	/** Gibt an, ob das Graecum erworben wurde. */
//	private boolean graecum = false;
//
//	/** Gibt an, ob das Hebraicum erworben wurde. */
//	private boolean hebraicum = false;
//
//	/** Gibt an, ob das kleine Latinum erworben wurde. */
//	private boolean kleinesLatinum = false;
//
//	/** Gibt an, ob das große Latinum erworben wurde. */
//	private boolean latinum = false;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
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
	public ReportingSchuelerGostAbitur(final int abiturjahr, final String besondereLernleistung, final Note besondereLernleistungNote, final String besondereLernleistungThema, final boolean[] bewertetesHalbjahr, final String bilingualeSprache, final Integer block1AnzahlKurse, final Integer block1DefiziteGesamt, final Integer block1DefiziteLK, final long block1FehlstundenGesamt, final long block1FehlstundenUnentschuldigt, final Double block1NotenpunkteDurchschnitt, final Integer block1PunktSummeGK, final Integer block1PunktSummeLK, final Integer block1PunktSummeNormiert, final Boolean block1Zulassung, final Integer block2DefiziteGesamt, final Integer block2DefiziteLK, final Integer block2PunktSumme, final List<ReportingGostAbiturFachbelegung> fachbelegungen, final boolean freiwilligerRuecktritt, final Integer gesamtPunkte, final Integer gesamtPunkteVerbesserung, final Integer gesamtPunkteVerschlechterung, final String note, final String projektkursThema, final Boolean pruefungBestanden, final int schuljahrAbitur) {
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


	// ##### Berechnete Methodem #####

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Projektkurs belegt wurde.
	 * @return Projektkursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean projektkursVorhanden() {
		return !this.fachbelegungen.stream()
			.filter(f -> (f != null && "PX".equals(f.fach().fachgruppe().daten.kuerzel)))
			.toList()
			.isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Religionskurs belegt wurde.
	 * @return Religionskursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean religionVorhanden() {
		return !this.fachbelegungen.stream()
			.filter(f -> (f != null && "RE".equals(f.fach().fachgruppe().daten.kuerzel)))
			.toList()
			.isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Vertiefungskurs belegt wurde.
	 * @return Vertiefungskursbelegung in Q-Phase vorhanden.
	 */
	@JsonIgnore
	public boolean vertiefungskursVorhanden() {
		final List<ReportingGostAbiturFachbelegung> listVertiefungskurse = this.fachbelegungen.stream().filter(f -> "VX".equals(f.fach().fachgruppe().daten.kuerzel)).toList();
		if (listVertiefungskurse.isEmpty())
			return false;

		for (final ReportingGostAbiturFachbelegung vertiefung : listVertiefungskurse) {
			for (int i = 0; i < 6; i++) {
				if (vertiefung.halbjahresbelegungen()[i] != null && 'Q' == vertiefung.halbjahresbelegungen()[i].halbjahrKuerzel().charAt(0))
					return true;
			}
		}

		return false;
	}



	// ##### Getter und Setter #####

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 * @return Inhalt des Feldes abiturjahr
	 */
	public int abiturjahr() {
		return abiturjahr;
	}

	/**
	 * Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird, wird gesetzt.
	 * @param abiturjahr Neuer Wert für das Feld abiturjahr
	 */
	public void setAbiturjahr(final int abiturjahr) {
		this.abiturjahr = abiturjahr;
	}

	/**
	 * Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern).
	 * @return Inhalt des Feldes besondereLernleistung
	 */
	public String besondereLernleistung() {
		return besondereLernleistung;
	}

	/**
	 * Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern) wird gesetzt.
	 * @param besondereLernleistung Neuer Wert für das Feld besondereLernleistung
	 */
	public void setBesondereLernleistung(final String besondereLernleistung) {
		this.besondereLernleistung = besondereLernleistung;
	}

	/**
	 * Ggf. Note einer externen besonderen Lernleistung.
	 * @return Inhalt des Feldes besondereLernleistungNote
	 */
	public Note besondereLernleistungNote() {
		return besondereLernleistungNote;
	}

	/**
	 * Ggf. Note einer externen besonderen Lernleistung wird gesetzt.
	 * @param besondereLernleistungNote Neuer Wert für das Feld besondereLernleistungNote
	 */
	public void setBesondereLernleistungNote(final Note besondereLernleistungNote) {
		this.besondereLernleistungNote = besondereLernleistungNote;
	}

	/**
	 * Das Thema der besonderen Lernleistung.
	 * @return Inhalt des Feldes besondereLernleistungThema
	 */
	public String besondereLernleistungThema() {
		return besondereLernleistungThema;
	}

	/**
	 * Das Thema der besonderen Lernleistung wird gesetzt.
	 * @param besondereLernleistungThema Neuer Wert für das Feld besondereLernleistungThema
	 */
	public void setBesondereLernleistungThema(final String besondereLernleistungThema) {
		this.besondereLernleistungThema = besondereLernleistungThema;
	}

	/**
	 * Für die einzelnen GostHalbjahr wird angegeben, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 * @return Inhalt des Feldes bewertetesHalbjahr
	 */
	public final boolean[] bewertetesHalbjahr() {
		return bewertetesHalbjahr;
	}

	/**
	 * Für die einzelnen GostHalbjahr wird angegeben, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 * @param bewertetesHalbjahr Neuer Wert für das Feld bewertetesHalbjahr
	 */
	public void setBewertetesHalbjahr(final boolean[] bewertetesHalbjahr) {

		this.bewertetesHalbjahr = bewertetesHalbjahr;
	}
	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 * @return Inhalt des Feldes bilingualeSprache
	 */
	public String bilingualeSprache() {
		return bilingualeSprache;
	}

	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt, wird gesetzt.
	 * @param bilingualeSprache Neuer Wert für das Feld bilingualeSprache
	 */
	public void setBilingualeSprache(final String bilingualeSprache) {
		this.bilingualeSprache = bilingualeSprache;
	}

	/**
	 * Die Anzahl der Kurse in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1AnzahlKurse
	 */
	public Integer block1AnzahlKurse() {
		return block1AnzahlKurse;
	}

	/**
	 * Die Anzahl der Kurse in der Qualifikationsphase wird gesetzt.
	 * @param block1AnzahlKurse Neuer Wert für das Feld block1AnzahlKurse
	 */
	public void setBlock1AnzahlKurse(final Integer block1AnzahlKurse) {
		this.block1AnzahlKurse = block1AnzahlKurse;
	}

	/**
	 * Die Anzahl der Gesamtdefizite in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1DefiziteGesamt
	 */
	public Integer block1DefiziteGesamt() {
		return block1DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Gesamtdefizite in der Qualifikationsphase wird gesetzt.
	 * @param block1DefiziteGesamt Neuer Wert für das Feld block1DefiziteGesamt
	 */
	public void setBlock1DefiziteGesamt(final Integer block1DefiziteGesamt) {
		this.block1DefiziteGesamt = block1DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1DefiziteLK
	 */
	public Integer block1DefiziteLK() {
		return block1DefiziteLK;
	}

	/**
	 * Die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase wird gesetzt.
	 * @param block1DefiziteLK Neuer Wert für das Feld block1DefiziteLK
	 */
	public void setBlock1DefiziteLK(final Integer block1DefiziteLK) {
		this.block1DefiziteLK = block1DefiziteLK;
	}

	/**
	 * Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase.
	 * @return Inhalt des Feldes block1FehlstundenGesamt
	 */
	public long block1FehlstundenGesamt() {
		return block1FehlstundenGesamt;
	}

	/**
	 * Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase wird gesetzt.
	 * @param block1FehlstundenGesamt Neuer Wert für das Feld block1FehlstundenGesamt
	 */
	public void setBlock1FehlstundenGesamt(final long block1FehlstundenGesamt) {
		this.block1FehlstundenGesamt = block1FehlstundenGesamt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase.
	 * @return Inhalt des Feldes block1FehlstundenUnentschuldigt
	 */
	public long block1FehlstundenUnentschuldigt() {
		return block1FehlstundenUnentschuldigt;
	}

	/**
	 * Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase wird gesetzt.
	 * @param block1FehlstundenUnentschuldigt Neuer Wert für das Feld block1FehlstundenUnentschuldigt
	 */
	public void setBlock1FehlstundenUnentschuldigt(final long block1FehlstundenUnentschuldigt) {
		this.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;
	}

	/**
	 * Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase.
	 * @return Inhalt des Feldes block1NotenpunkteDurchschnitt
	 */
	public Double block1NotenpunkteDurchschnitt() {
		return block1NotenpunkteDurchschnitt;
	}

	/**
	 * Der Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase wird gesetzt.
	 * @param block1NotenpunkteDurchschnitt Neuer Wert für das Feld block1NotenpunkteDurchschnitt
	 */
	public void setBlock1NotenpunkteDurchschnitt(final Double block1NotenpunkteDurchschnitt) {
		this.block1NotenpunkteDurchschnitt = block1NotenpunkteDurchschnitt;
	}

	/**
	 * Die Punktsumme aller Grundkurse in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1PunktSummeGK
	 */
	public Integer block1PunktSummeGK() {
		return block1PunktSummeGK;
	}

	/**
	 * Die Punktsumme aller Grundkurse in der Qualifikationsphase wird gesetzt.
	 * @param block1PunktSummeGK Neuer Wert für das Feld block1PunktSummeGK
	 */
	public void setBlock1PunktSummeGK(final Integer block1PunktSummeGK) {
		this.block1PunktSummeGK = block1PunktSummeGK;
	}

	/**
	 * Die Punktsumme aller Leistungskurse in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1PunktSummeLK
	 */
	public Integer block1PunktSummeLK() {
		return block1PunktSummeLK;
	}

	/**
	 * Die Punktsumme aller Leistungskurse in der Qualifikationsphase wird gesetzt.
	 * @param block1PunktSummeLK Neuer Wert für das Feld block1PunktSummeLK
	 */
	public void setBlock1PunktSummeLK(final Integer block1PunktSummeLK) {
		this.block1PunktSummeLK = block1PunktSummeLK;
	}

	/**
	 * Die normierte Punktsumme aller Kurse in der Qualifikationsphase.
	 * @return Inhalt des Feldes block1PunktSummeNormiert
	 */
	public Integer block1PunktSummeNormiert() {
		return block1PunktSummeNormiert;
	}

	/**
	 * Die normierte Punktsumme aller Kurse in der Qualifikationsphase wird gesetzt.
	 * @param block1PunktSummeNormiert Neuer Wert für das Feld block1PunktSummeNormiert
	 */
	public void setBlock1PunktSummeNormiert(final Integer block1PunktSummeNormiert) {
		this.block1PunktSummeNormiert = block1PunktSummeNormiert;
	}

	/**
	 * Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde.
	 * @return Inhalt des Feldes block1Zulassung
	 */
	public Boolean block1Zulassung() {
		return block1Zulassung;
	}

	/**
	 * Die Angabe, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde - wird gesetzt.
	 * @param block1Zulassung Neuer Wert für das Feld block1Zulassung
	 */
	public void setBlock1Zulassung(final Boolean block1Zulassung) {
		this.block1Zulassung = block1Zulassung;
	}

	/**
	 * Die Anzahl der Gesamtdefizite im Abiturbereich (Block II).
	 * @return Inhalt des Feldes block2DefiziteGesamt
	 */
	public Integer block2DefiziteGesamt() {
		return block2DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Gesamtdefizite im Abiturbereich (Block II) wird gesetzt.
	 * @param block2DefiziteGesamt Neuer Wert für das Feld block2DefiziteGesamt
	 */
	public void setBlock2DefiziteGesamt(final Integer block2DefiziteGesamt) {
		this.block2DefiziteGesamt = block2DefiziteGesamt;
	}

	/**
	 * Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II).
	 * @return Inhalt des Feldes block2DefiziteLK
	 */
	public Integer block2DefiziteLK() {
		return block2DefiziteLK;
	}

	/**
	 * Die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II) wird gesetzt.
	 * @param block2DefiziteLK Neuer Wert für das Feld block2DefiziteLK
	 */
	public void setBlock2DefiziteLK(final Integer block2DefiziteLK) {
		this.block2DefiziteLK = block2DefiziteLK;
	}

	/**
	 * Die Punktsumme im Abiturbereich (Block II).
	 * @return Inhalt des Feldes block2PunktSumme
	 */
	public Integer block2PunktSumme() {
		return block2PunktSumme;
	}

	/**
	 * Die Punktsumme im Abiturbereich (Block II) wird gesetzt.
	 * @param block2PunktSumme Neuer Wert für das Feld block2PunktSumme
	 */
	public void setBlock2PunktSumme(final Integer block2PunktSumme) {
		this.block2PunktSumme = block2PunktSumme;
	}

	/**
	 * Die Liste mit den Fachbelegungen in der Oberstufe.
	 * @return Inhalt des Feldes fachbelegungen
	 */
	public final List<ReportingGostAbiturFachbelegung> fachbelegungen() {
		return fachbelegungen;
	}

	/**
	 * Die Liste mit den Fachbelegungen in der Oberstufe wird gesetzt.
	 * @param fachbelegungen Neuer Wert für das Feld fachbelegungen
	 */
	public void setFachbelegungen(final List<ReportingGostAbiturFachbelegung> fachbelegungen) {
		this.fachbelegungen = fachbelegungen;
	}

	/**
	 * Gibt an, ob freiwillig von der Abiturprüfung zurückgetreten wurde.
	 * @return Inhalt des Feldes freiwilligerRuecktritt = false
	 */
	public boolean freiwilligerRuecktritt() {
		return freiwilligerRuecktritt;
	}

	/**
	 * Die Angabe, ob freiwillig von der Abiturprüfung zurückgetreten wurde, wird gesetzt.
	 * @param freiwilligerRuecktritt Neuer Wert für das Feld freiwilligerRuecktritt = false
	 */
	public void setFreiwilligerRuecktritt(final boolean freiwilligerRuecktritt) {
		this.freiwilligerRuecktritt = freiwilligerRuecktritt;
	}

	/**
	 * Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II).
	 * @return Inhalt des Feldes gesamtPunkte
	 */
	public Integer gesamtPunkte() {
		return gesamtPunkte;
	}

	/**
	 * Die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II) wird gesetzt.
	 * @param gesamtPunkte Neuer Wert für das Feld gesamtPunkte
	 */
	public void setGesamtPunkte(final Integer gesamtPunkte) {
		this.gesamtPunkte = gesamtPunkte;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde
	 * @return Inhalt des Feldes gesamtPunkteVerbesserung
	 */
	public Integer gesamtPunkteVerbesserung() {
		return gesamtPunkteVerbesserung;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verbessern würde, wird gesetzt.
	 * @param gesamtPunkteVerbesserung Neuer Wert für das Feld gesamtPunkteVerbesserung
	 */
	public void setGesamtPunkteVerbesserung(final Integer gesamtPunkteVerbesserung) {
		this.gesamtPunkteVerbesserung = gesamtPunkteVerbesserung;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde.
	 * @return Inhalt des Feldes gesamtPunkteVerschlechterung
	 */
	public Integer gesamtPunkteVerschlechterung() {
		return gesamtPunkteVerschlechterung;
	}

	/**
	 * Die Gesamtpunktzahl, ab der sich die Abiturnote verschlechtern würde, wird gesetzt.
	 * @param gesamtPunkteVerschlechterung Neuer Wert für das Feld gesamtPunkteVerschlechterung
	 */
	public void setGesamtPunkteVerschlechterung(final Integer gesamtPunkteVerschlechterung) {
		this.gesamtPunkteVerschlechterung = gesamtPunkteVerschlechterung;
	}

	/**
	 * Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 * @return Inhalt des Feldes note
	 */
	public String note() {
		return note;
	}

	/**
	 * Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde - wird gesetzt.
	 * @param note Neuer Wert für das Feld note
	 */
	public void setNote(final String note) {
		this.note = note;
	}

	/**
	 * Das Projektkursthema, sofern ein Projektkurs belegt wurde.
	 * @return Inhalt des Feldes projektkursThema
	 */
	public String projektkursThema() {
		return projektkursThema;
	}

	/**
	 * Das Projektkursthema, sofern ein Projektkurs belegt wurde, wird gesetzt.
	 * @param projektkursThema Neuer Wert für das Feld projektkursThema
	 */
	public void setProjektkursThema(final String projektkursThema) {
		this.projektkursThema = projektkursThema;
	}

	/**
	 * Gibt an, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde.
	 * @return Inhalt des Feldes pruefungBestanden
	 */
	public Boolean pruefungBestanden() {
		return pruefungBestanden;
	}

	/**
	 * Die Angabe, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde, wird gesetzt.
	 * @param pruefungBestanden Neuer Wert für das Feld pruefungBestanden
	 */
	public void setPruefungBestanden(final Boolean pruefungBestanden) {
		this.pruefungBestanden = pruefungBestanden;
	}

	/**
	 * Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 * @return Inhalt des Feldes schuljahrAbitur
	 */
	public int schuljahrAbitur() {
		return schuljahrAbitur;
	}

	/**
	 * Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird, wird gesetzt.
	 * @param schuljahrAbitur Neuer Wert für das Feld schuljahrAbitur
	 */
	public void setSchuljahrAbitur(final int schuljahrAbitur) {
		this.schuljahrAbitur = schuljahrAbitur;
	}
}
