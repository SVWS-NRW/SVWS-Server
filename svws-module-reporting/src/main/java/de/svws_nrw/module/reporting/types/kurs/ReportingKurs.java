package de.svws_nrw.module.reporting.types.kurs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;


/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Kurs.
 */
public class ReportingKurs extends ReportingBaseType {

	/** Ggf. die Zeugnisbezeichnung des Kurses. */
	protected String bezeichnungZeugnis;

	/** Das Fach, das dem Kurs zugeordnet ist. */
	protected ReportingFach fach;

	/** Die ID des Kurses. */
	protected long id;

	/** Gibt an, ob der Kurs zu einem epochalen Unterricht gehört. */
	protected boolean istEpochalunterricht;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Die Jahrgänge, denen der Kurs zugeordnet ist. */
	protected List<ReportingJahrgang> jahrgaenge;

	/** Das Kürzel des Kurses. */
	protected String kuerzel;

	/** Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird. */
	protected String kursartAllg;

	/** Die Lehrkraft, die den Kurs unterrichtet und verantwortlich leite. */
	protected ReportingLehrer kursleitung;

	/** Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde */
	protected List<Integer> schienen;

	/** Die IDs der Schüler des Kurses. */
	protected List<Long> idsSchueler;

	/** Die Schüler des Kurses. */
	protected List<ReportingSchueler> schueler;

	/** Der Schuljahresabschnitt des Kurses. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet. */
	protected Integer schulnummer;

	/** Die Sortierreihenfolge des Listen-Eintrags. */
	protected int sortierung;

	/** Die Wochenstunden des Kurses für die Schüler. */
	protected int wochenstunden;

	/** Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID. */
	protected Map<Long, Double> wochenstundenLehrkraefte;

	/** Die Lehrkräfte, die den Kurs neben der Kursleitung unterrichten. */
	protected List<ReportingLehrer> zusatzLehrkraefte;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param bezeichnungZeugnis 		Ggf. die Zeugnisbezeichnung des Kurses.
	 * @param fach 						Das Fach, das dem Kurs zugeordnet ist.
	 * @param id 						Die ID des Kurses.
	 * @param istEpochalunterricht 		Gibt an, ob der Kurs zu einem epochalen Unterricht gehört.
	 * @param istSichtbar 				Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 * @param jahrgaenge 				Die Jahrgänge, denen der Kurs zugeordnet ist.
	 * @param kuerzel 					Das Kürzel des Kurses.
	 * @param kursartAllg 				Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 * @param kursleitung 				Der Lehrer, der den Kurs unterrichtet und verantwortlich leite.
	 * @param schienen 					Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde.
	 * @param schueler 					Die Schüler des Kurses.
	 * @param idsSchueler 				Die Schüler des Kurses als Liste ihrer IDs.
	 * @param schuljahresabschnitt 		Der Schuljahresabschnitt des Kurses.
	 * @param schulnummer 				Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet.
	 * @param sortierung 				Die Sortierreihenfolge des Listen-Eintrags.
	 * @param wochenstunden 			Die Wochenstunden des Kurses für die Schüler.
	 * @param wochenstundenLehrkraefte 	Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 * @param zusatzLehrkraefte 		Die Lehrkräfte, die den Kurs neben der Kursleitung (Verantwortlichen) unterrichten.
	 */
	public ReportingKurs(final String bezeichnungZeugnis, final ReportingFach fach, final long id, final boolean istEpochalunterricht,
			final boolean istSichtbar, final List<ReportingJahrgang> jahrgaenge, final String kuerzel, final String kursartAllg,
			final List<Integer> schienen, final List<Long> idsSchueler, final List<ReportingSchueler> schueler, final ReportingLehrer kursleitung,
			final ReportingSchuljahresabschnitt schuljahresabschnitt, final Integer schulnummer, final int sortierung,
			final int wochenstunden, final Map<Long, Double> wochenstundenLehrkraefte, final List<ReportingLehrer> zusatzLehrkraefte) {
		this.bezeichnungZeugnis = bezeichnungZeugnis;
		this.fach = fach;
		this.id = id;
		this.istEpochalunterricht = istEpochalunterricht;
		this.istSichtbar = istSichtbar;
		this.jahrgaenge = jahrgaenge;
		this.kuerzel = kuerzel;
		this.kursartAllg = kursartAllg;
		this.kursleitung = kursleitung;
		this.schienen = schienen;
		this.idsSchueler = idsSchueler;
		this.schueler = schueler;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.schulnummer = schulnummer;
		this.sortierung = sortierung;
		this.wochenstunden = wochenstunden;
		this.wochenstundenLehrkraefte = wochenstundenLehrkraefte;
		this.zusatzLehrkraefte = zusatzLehrkraefte;
	}


	// ##### Berechnete Methoden #####
	/**
	 * Gibt eine Liste aller Lehrkräfte des Kurses aus, wobei die erste die Kursleitung ist.
	 *
	 * @return		Die Liste der Lehrkräfte im Kurs, beginnend mit der Kursleitung.
	 */
	public List<ReportingLehrer> lehrkraefte() {
		final List<ReportingLehrer> listeLehrkraefte = new ArrayList<>();
		if (kursleitung != null)
			listeLehrkraefte.add(kursleitung);
		if ((zusatzLehrkraefte != null) && !zusatzLehrkraefte.isEmpty())
			listeLehrkraefte.addAll(zusatzLehrkraefte);
		return listeLehrkraefte;
	}

	/**
	 * Auflistung der Lehrkräfte des Kurses als kommaseparierte Liste der Kürzel.
	 *
	 * @return		Kommaseparierte Liste der Lehrkräfte, beginnend mit der Kursleitung.
	 */
	public String auflistungLehrkraefte() {
		if (lehrkraefte().isEmpty())
			return "";
		return this.lehrkraefte().stream().map(ReportingLehrer::kuerzel).collect(Collectors.joining(","));
	}

	/**
	 * Gibt die Wochenstunden zur ID einer Lehrkraft zurück.
	 *
	 * @param id	Die ID der Lehrkraft.
	 *
	 * @return		Die Wochenstunden der Lehrkraft in diesem Kurs.
	 */
	public double wochenstundenLehrerZurID(final Long id) {
		if ((id == null) || !wochenstundenLehrkraefte.containsKey(id))
			return 0;
		return wochenstundenLehrkraefte.get(id);
	}


	// ##### Getter #####

	/**
	 * Ggf. die Zeugnisbezeichnung des Kurses.
	 *
	 * @return Inhalt des Feldes bezeichnungZeugnis
	 */
	public String bezeichnungZeugnis() {
		return bezeichnungZeugnis;
	}

	/**
	 * Das Fach, das dem Kurs zugeordnet ist.
	 *
	 * @return Inhalt des Feldes fach
	 */
	public ReportingFach fach() {
		return fach;
	}

	/**
	 * Die ID des Kurses.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob der Kurs zu einem epochalen Unterricht gehört.
	 *
	 * @return Inhalt des Feldes istEpochalunterricht
	 */
	public boolean istEpochalunterricht() {
		return istEpochalunterricht;
	}

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 *
	 * @return Inhalt des Feldes istSichtbar
	 */
	public boolean istSichtbar() {
		return istSichtbar;
	}

	/**
	 * Die Jahrgänge, denen der Kurs zugeordnet ist.
	 *
	 * @return Inhalt des Feldes jahrgaenge
	 */
	public List<ReportingJahrgang> jahrgaenge() {
		return jahrgaenge;
	}

	/**
	 * Das Kürzel des Kurses.
	 *
	 * @return Inhalt des Feldes kuerzel
	 */
	public String kuerzel() {
		return kuerzel;
	}

	/**
	 * Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 *
	 * @return Inhalt des Feldes kursartAllg
	 */
	public String kursartAllg() {
		return kursartAllg;
	}

	/**
	 * Die Lehrkraft, die den Kurs unterrichtet und verantwortlich leite.
	 *
	 * @return Inhalt des Feldes lehrer
	 */
	public ReportingLehrer kursleitung() {
		return kursleitung;
	}

	/**
	 * Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde.
	 *
	 * @return Inhalt des Feldes schienen
	 */
	public List<Integer> schienen() {
		return schienen;
	}

	/**
	 * Die Schüler des Kurses.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public List<ReportingSchueler> schueler() {
		return schueler;
	}

	/**
	 * Die Schüler des Kurses als Liste ihrer IDs.
	 *
	 * @return Inhalt des Feldes idsSchueler
	 */
	public List<Long> idsSchueler() {
		return idsSchueler;
	}


	/**
	 * Der Schuljahresabschnitt des Kurses.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet.
	 *
	 * @return Inhalt des Feldes schulnummer
	 */
	public Integer schulnummer() {
		return schulnummer;
	}

	/**
	 * Die Sortierreihenfolge des Listen-Eintrags.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Die Wochenstunden des Kurses für die Schüler.
	 *
	 * @return Inhalt des Feldes wochenstunden
	 */
	public int wochenstunden() {
		return wochenstunden;
	}

	/**
	 * Eine Map mit den Wochenstunden der Lehrkräfte zu deren ID.
	 *
	 * @return Inhalt des Feldes wochenstundenLehrkraefte
	 */
	public Map<Long, Double> wochenstundenLehrkraefte() {
		return wochenstundenLehrkraefte;
	}

	/**
	 * Die Lehrkräfte, die den Kurs neben der Kursleitung (Verantwortlichen) unterrichten.
	 *
	 * @return Inhalt des Feldes zusatzLehrkraefte
	 */
	public List<ReportingLehrer> zusatzLehrkraefte() {
		return zusatzLehrkraefte;
	}

}
