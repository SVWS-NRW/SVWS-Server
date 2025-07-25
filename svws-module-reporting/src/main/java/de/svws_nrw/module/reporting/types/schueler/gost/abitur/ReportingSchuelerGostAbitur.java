package de.svws_nrw.module.reporting.types.schueler.gost.abitur;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.fach.Fachgruppe;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.gost.abitur.ReportingGostAbiturFachbelegung;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostAbiturdaten.
 */
public class ReportingSchuelerGostAbitur extends ReportingBaseType {

	/** Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	protected int abiturjahr;

	/** Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	protected int abiturSchuljahr;

	/** Der Schuljahresabschnitt, in dem der Schüler sein Abitur abgelegt hat. */
	protected ReportingSchuljahresabschnitt abiturSchuljahresabschnitt;

	/** Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern). */
	protected String besondereLernleistungArt;

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



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturjahr Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 * @param abiturSchuljahr Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 * @param abiturSchuljahresabschnitt Der Schuljahresabschnitt, in dem der Schüler sein Abitur abgelegt hat.
	 * @param besondereLernleistungArt Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern).
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
	 */
	public ReportingSchuelerGostAbitur(final int abiturjahr, final int abiturSchuljahr, final ReportingSchuljahresabschnitt abiturSchuljahresabschnitt,
			final String besondereLernleistungArt, final Note besondereLernleistungNote, final String besondereLernleistungThema,
			final boolean[] bewertetesHalbjahr, final String bilingualeSprache, final Integer block1AnzahlKurse, final Integer block1DefiziteGesamt,
			final Integer block1DefiziteLK, final long block1FehlstundenGesamt, final long block1FehlstundenUnentschuldigt,
			final Double block1NotenpunkteDurchschnitt, final Integer block1PunktSummeGK, final Integer block1PunktSummeLK,
			final Integer block1PunktSummeNormiert, final Boolean block1Zulassung, final Integer block2DefiziteGesamt, final Integer block2DefiziteLK,
			final Integer block2PunktSumme, final List<ReportingGostAbiturFachbelegung> fachbelegungen, final boolean freiwilligerRuecktritt,
			final Integer gesamtPunkte, final Integer gesamtPunkteVerbesserung, final Integer gesamtPunkteVerschlechterung, final String note,
			final String projektkursThema, final Boolean pruefungBestanden) {
		this.abiturjahr = abiturjahr;
		this.abiturSchuljahr = abiturSchuljahr;
		this.abiturSchuljahresabschnitt = abiturSchuljahresabschnitt;
		this.besondereLernleistungArt = besondereLernleistungArt;
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

		this.fachbelegungen().sort(ReportingGostAbiturFachbelegung::compareToGost);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Ermittelt und liefert eine Liste von Abiturfachbelegungen basierend auf den Fachbelegungen,
	 * wobei nur nicht-null Einträge berücksichtigt werden, deren zugehöriges Abiturfach nicht null ist.
	 * Die resultierende Liste wird nach den Abiturfächern sortiert.
	 *
	 * @return eine sortierte Liste der Abiturfachbelegungen
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAbiturfaecher() {
		return fachbelegungen.stream()
				.filter(Objects::nonNull)
				.filter(f -> f.abiturFach() != null)
				.sorted(Comparator.comparing(ReportingGostAbiturFachbelegung::abiturFach))
				.toList();
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 1 mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppen sind Deutsch (FG_D), Fremdsprachen (FG_FS), Musik (FG_ME) und Kunst (FG_MS).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB1() {
		return getFachbelegungen(Fachgruppe.FG_D, Fachgruppe.FG_FS, Fachgruppe.FG_ME, Fachgruppe.FG_MS);
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 1 mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppen sind Deutsch (FG_D), Fremdsprachen (FG_FS), Musik (FG_ME) und Kunst (FG_MS).
	 *
	 @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB1Zeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_D, Fachgruppe.FG_FS, Fachgruppe.FG_ME, Fachgruppe.FG_MS);
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 2 mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppen sind Gesellschaftswissenschaften (FG_GE) und Philosophie (FG_PL).
	 *
	 @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB2() {
		return getFachbelegungen(Fachgruppe.FG_GS, Fachgruppe.FG_PL);
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 2 mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppen sind Gesellschaftswissenschaften (FG_GE) und Philosophie (FG_PL).
	 *
	 @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB2Zeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_GS, Fachgruppe.FG_PL);
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 3 mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppen sind Mathematik (FG_M), Naturwissenschaften (FG_NW) oder Wahlpflichtfächer Naturwissenschaften (FG_WN).
	 *
	 @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB3() {
		return getFachbelegungen(Fachgruppe.FG_M, Fachgruppe.FG_NW, Fachgruppe.FG_WN);
	}

	/**
	 * Diese Methode filtert die Fachbelegungen des Aufgabenfeldes 3 mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppen sind Mathematik (FG_M), Naturwissenschaften (FG_NW) oder Wahlpflichtfächer Naturwissenschaften (FG_WN).
	 *
	 @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Fachbelegungen, die den oben genannten Fachgruppen zugeordnet sind.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenAB3Zeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_M, Fachgruppe.FG_NW, Fachgruppe.FG_WN);
	}

	/**
	 * Diese Methode filtert die Religionsfachbelegungen mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppe ist Religion (FG_RE).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Religionsfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenReligion() {
		return getFachbelegungen(Fachgruppe.FG_RE);
	}

	/**
	 * Diese Methode filtert die Religionsfachbelegungen mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppe ist Religion (FG_RE).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Religionsfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenReligionZeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_RE);
	}

	/**
	 * Diese Methode filtert die Sportfachbelegungen mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppe ist Sport (FG_SP).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Sportfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenSport() {
		return getFachbelegungen(Fachgruppe.FG_SP);
	}

	/**
	 * Diese Methode filtert die Sportfachbelegungen mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppe ist Sport (FG_SP).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Sportfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenSportZeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_SP);
	}

	/**
	 * Diese Methode filtert die Projektkursbelegungen mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppe ist die Gruppe Projektkurs (FG_PX).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Projektkursbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenProjektkurse() {
		return getFachbelegungen(Fachgruppe.FG_PX);
	}

	/**
	 * Diese Methode filtert die Projektkursbelegungen mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppe ist die Gruppe Projektkurs (FG_PX).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Projektkursbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenProjektkurseZeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_PX);
	}

	/**
	 * Diese Methode filtert die Vertiefungsfachbelegungen mit Relevanz für die Prüfungsordnung.
	 * Eingeschlossene Fachgruppe ist die Gruppe Vertiefungskurs (FG_VX).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Vertiefungsfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenVertiefungskurse() {
		return getFachbelegungen(Fachgruppe.FG_VX);
	}

	/**
	 * Diese Methode filtert die Vertiefungsfachbelegungen mit Relevanz für die Prüfungsordnung und Zeugnisse.
	 * Eingeschlossene Fachgruppe ist die Gruppe Vertiefungskurs (FG_VX).
	 *
	 * @return Eine Liste von {@code ReportingGostAbiturFachbelegung} mit Vertiefungsfachbelegungen.
	 */
	public List<ReportingGostAbiturFachbelegung> fachbelegungenVertiefungskurseZeugnisrelevant() {
		return getFachbelegungenZeugnisrelevant(Fachgruppe.FG_VX);
	}

	/**
	 * Private Hilfsmethode zum Filtern der Fachbelegungen nach Prüfungsordnungsrelevanz und Fachgruppen.
	 *
	 * @param fachgruppen Die Fachgruppen, deren Fachbelegungen berücksichtigt werden sollen.
	 *
	 * @return Eine Liste der Fachbelegungen zu den übergebenen Fachgruppen.
	 */
	private List<ReportingGostAbiturFachbelegung> getFachbelegungen(final Fachgruppe... fachgruppen) {
		return fachbelegungen.stream()
				.filter(Objects::nonNull)
				.filter(f -> f.fach().istPruefungsordnungsRelevant())
				.filter(f -> Arrays.asList(fachgruppen).contains(f.fach().fachgruppe()))
				.toList();
	}

	/**
	 * Private Hilfsmethode zum Filtern der Fachbelegungen nach Prüfungsordnungsrelevanz, Zeugnisrelevanz und Fachgruppen.
	 *
	 * @param fachgruppen Die Fachgruppen, deren Fachbelegungen berücksichtigt werden sollen.
	 *
	 * @return Eine Liste der Fachbelegungen zu den übergebenen Fachgruppen.
	 */
	private List<ReportingGostAbiturFachbelegung> getFachbelegungenZeugnisrelevant(final Fachgruppe... fachgruppen) {
		return getFachbelegungen(fachgruppen).stream()
				.filter(f -> f.fach().aufZeugnis())
				.toList();
	}

	/**
	 * Gibt die Abiturendnote in Worten aus, oder '---', wenn keine Note vorhanden ist.
	 * @return Die Abiturnote im Format 'Zahl Komma Zahl' oder '---'
	 */
	public String noteInWorten() {
		final List<String> zahlworte = Arrays.asList("Null", "Eins", "Zwei", "Drei", "Vier", "Fünf", "Sechs", "Sieben", "Acht", "Neun");

		final String durchschnittsnote = note();
		if (durchschnittsnote.isEmpty() || "---".equals(durchschnittsnote))
			return "---";

		final List<String> durchschnittsnoteZiffern = new ArrayList<>(Arrays.stream(durchschnittsnote.split(",")).toList());

		if (durchschnittsnoteZiffern.size() != 2)
			return "---";

		return zahlworte.get(Integer.parseInt(durchschnittsnoteZiffern.get(0))) + " Komma "
				+ zahlworte.get(Integer.parseInt(durchschnittsnoteZiffern.get(1)));
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Projektkurs belegt wurde.
	 *
	 * @return Projektkursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean projektkursVorhanden() {
		return !fachbelegungenProjektkurseZeugnisrelevant().isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Religionskurs belegt wurde.
	 *
	 * @return Religionskursbelegung vorhanden.
	 */
	@JsonIgnore
	public boolean religionVorhanden() {
		return !fachbelegungenReligionZeugnisrelevant().isEmpty();
	}

	/**
	 * Gibt die Information zurück, ob in der Q-Phase ein Vertiefungskurs belegt wurde.
	 *
	 * @return Vertiefungskursbelegung in Q-Phase vorhanden.
	 */
	@JsonIgnore
	public boolean vertiefungskursVorhanden() {
		final List<ReportingGostAbiturFachbelegung> listVertiefungskurse = fachbelegungenVertiefungskurseZeugnisrelevant();
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
	 * Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.
	 *
	 * @return Inhalt des Feldes abiturSchuljahr
	 */
	public int abiturSchuljahr() {
		return abiturSchuljahr;
	}

	/**
	 * Der Schuljahresabschnitt, in dem der Schüler sein Abitur abgelegt hat.
	 *
	 * @return Inhalt des Feldes abiturSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt abiturSchuljahresabschnitt() {
		return abiturSchuljahresabschnitt;
	}

	/**
	 * Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern).
	 *
	 * @return Inhalt des Feldes besondereLernleistung
	 */
	public String besondereLernleistungArt() {
		return besondereLernleistungArt;
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
		if ((note == null) || note.trim().isEmpty())
			return "---";

		String checkNote = note.trim();
		if (!checkNote.contains(","))
			checkNote += ",0";

		if (checkNote.length() != 3)
			return "---";

		final List<String> erlaubteZiffern = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		final List<String> checkNoteZiffern = new ArrayList<>(Arrays.stream(checkNote.split(",")).toList());

		if (checkNoteZiffern.size() != 2)
			return "---";

		if (!erlaubteZiffern.contains(checkNoteZiffern.get(0)) || !erlaubteZiffern.contains(checkNoteZiffern.get(1)))
			return "---";

		return checkNote;
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

}
