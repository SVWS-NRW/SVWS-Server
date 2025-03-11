package de.svws_nrw.module.reporting.proxytypes.schueler.lernabschnitte;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;


/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Kurs und erweitert die Klasse {@link ReportingSchuelerLeistungsdaten}.
 */
public class ProxyReportingSchuelerLeistungsdaten extends ReportingSchuelerLeistungsdaten {

	/** Collator für die deutsche Sortierung von Einträgen */
	@JsonIgnore
	private final Collator colGerman = Collator.getInstance(Locale.GERMAN);

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerLeistungsdaten}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param reportingSchuelerLernabschnitt Der Lernabschnitt, dem die Leistungsdaten zugeordneten werden sollen. Dieser muss mit dem Lernabschnitt aus den
	 * übergebenen Leistungsdaten übereinstellen, andernfalls ergibt sich Fehler.
	 * @param schuelerLeistungsdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuelerLeistungsdaten(final ReportingRepository reportingRepository,
			final ReportingSchuelerLernabschnitt reportingSchuelerLernabschnitt, final SchuelerLeistungsdaten schuelerLeistungsdaten) {
		super(schuelerLeistungsdaten.abifach,
				schuelerLeistungsdaten.aufZeugnis,
				null,
				null,
				schuelerLeistungsdaten.fehlstundenGesamt,
				schuelerLeistungsdaten.fehlstundenUnentschuldigt,
				ersetzeNullBlankTrim(schuelerLeistungsdaten.geholtJahrgangAbgeschlossen),
				schuelerLeistungsdaten.gewichtungAllgemeinbildend,
				schuelerLeistungsdaten.id,
				schuelerLeistungsdaten.istEpochal,
				schuelerLeistungsdaten.istGemahnt,
				schuelerLeistungsdaten.istZP10oderZKEF,
				null,
				ersetzeNullBlankTrim(schuelerLeistungsdaten.kursart),
				reportingSchuelerLernabschnitt,
				ersetzeNullBlankTrim(schuelerLeistungsdaten.mahndatum),
				Note.fromKuerzel(schuelerLeistungsdaten.note),
				Note.fromKuerzel(schuelerLeistungsdaten.noteBerufsabschluss),
				Note.fromKuerzel(schuelerLeistungsdaten.noteQuartal),
				schuelerLeistungsdaten.koopSchule,
				ersetzeNullBlankTrim(schuelerLeistungsdaten.textFachbezogeneLernentwicklung),
				ersetzeNullBlankTrim(schuelerLeistungsdaten.umfangLernstandsbericht),
				new HashMap<>(),
				(double) schuelerLeistungsdaten.wochenstunden,
				new ArrayList<>());

		this.reportingRepository = reportingRepository;

		// Lernabschnitt und damit den Schuljahresabschnitt ermitteln, um die an den Abschnitt gebundenen Daten wie Fächer zu gelangen.
		if ((reportingSchuelerLernabschnitt == null) || (this.lernabschnitt().id() != schuelerLeistungsdaten.lernabschnittID)) {
			// Wenn der Lernabschnitt nicht in den Maps gefunden werden kann, dann bleiben die Leistungsdaten an zentraler Stelle null.
			super.lernabschnitt = null;
			return;
		}

		// Fach setzen, dafür und für weitere Daten wird der Schuljahresabschnitt des Lernabschnitts zu den Leistungsdaten benötigt.
		final ReportingSchuljahresabschnitt schuljahresabschnitt =
				this.reportingRepository.schuljahresabschnitt(reportingSchuelerLernabschnitt.idSchuljahresabschnitt());
		super.fach = schuljahresabschnitt.fach(schuelerLeistungsdaten.fachID);

		// Es wird zwischen Klassen- und Kursunterricht unterschieden. In den Leistungsdaten können aber bei Kursunterrichten trotzdem vom Kurs abweichende Eintragungen vorgenommen werden.
		// Vorgabe ist, dass die Angaben in den Leistungsdaten vorrang vor dem Kurs haben. Setze daher diese verbindlich und ergänze später nur evtl. zusätzliche Angaben.

		// Fachlehrkraft setzen. Dabei wird die eingetragene Fachlehrkraft immer genommen, auch wenn sie von einer evtl. Kursleitung abweicht.
		if (schuelerLeistungsdaten.lehrerID != null) {
			super.fachLehrkraft = new ProxyReportingLehrer(this.reportingRepository,
					this.reportingRepository.mapLehrerStammdaten().get(schuelerLeistungsdaten.lehrerID));
			super.wochenstundenLehrkraefte.put(schuelerLeistungsdaten.lehrerID, (double) schuelerLeistungsdaten.wochenstunden);
		} else {
			super.fachLehrkraft = null;
		}
		// Zusätzliche Lehrkräfte setzen
		if (schuelerLeistungsdaten.zusatzkraftID != null) {
			super.zusatzLehrkraefte.add(new ProxyReportingLehrer(this.reportingRepository,
					this.reportingRepository.mapLehrerStammdaten().get(schuelerLeistungsdaten.zusatzkraftID)));
			super.wochenstundenLehrkraefte.put(schuelerLeistungsdaten.zusatzkraftID, (double) schuelerLeistungsdaten.zusatzkraftWochenstunden);
		} else
			super.zusatzLehrkraefte = new ArrayList<>();

		if (schuelerLeistungsdaten.kursID != null) {
			// Es liegt Kursunterricht vor. Ergänze alle Angaben entsprechend.
			super.kurs = schuljahresabschnitt.kurs(schuelerLeistungsdaten.kursID);
			if (super.fachLehrkraft == null)
				super.fachLehrkraft = kurs.kursleitung();
			super.zusatzLehrkraefte.addAll(kurs.zusatzLehrkraefte());
			super.wochenstundenLehrkraefte.putAll(kurs.wochenstundenLehrkraefte());
		}
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

}
