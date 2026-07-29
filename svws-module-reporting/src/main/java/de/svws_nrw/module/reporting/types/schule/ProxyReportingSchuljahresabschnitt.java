package de.svws_nrw.module.reporting.types.schule;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ProxyReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.fach.ProxyReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Schuljahresabschnitt und erweitert die Klasse {@link ReportingSchuljahresabschnitt}.
 */
public class ProxyReportingSchuljahresabschnitt extends ReportingSchuljahresabschnitt {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuljahresabschnitt}.
	 *
	 * @param reportingContext 	Repository für das Reporting.
	 * @param schuljahresabschnitt	Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingContext reportingContext, final Schuljahresabschnitt schuljahresabschnitt) {
		super(schuljahresabschnitt.id,
				schuljahresabschnitt.schuljahr,
				schuljahresabschnitt.abschnitt,
				schuljahresabschnitt.idFolgeAbschnitt,
				schuljahresabschnitt.idVorigerAbschnitt,
				null,
				null,
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>(),
				new HashMap<>());

		this.reportingContext = reportingContext;
	}

	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für einen virtuellen {@link ReportingSchuljahresabschnitt}, also für
	 * einen Abschnitt, der in der Datenbank der Schule (noch) nicht existiert. Zum fachlichen Hintergrund und zu den
	 * Einschränkungen eines solchen Abschnitts siehe {@link ReportingSchuljahresabschnitt#istVirtuell()}.
	 *
	 * <p>Der Abschnitt erhält die übergebene negative Pseudo-ID; Vorgänger und Nachfolger bleiben unbesetzt, da sich
	 * ein virtueller Abschnitt nicht in die Abschnittskette der Schule einreiht.</p>
	 *
	 * @param reportingContext 	Repository für das Reporting.
	 * @param id				Die negative Pseudo-ID des virtuellen Abschnitts, vergeben von
	 *                          {@link de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule}.
	 * @param schuljahr			Das Schuljahr, in welchem der Schuljahresabschnitt liegt.
	 * @param abschnitt			Die Nummer des Abschnitts im Schuljahr.
	 */
	public ProxyReportingSchuljahresabschnitt(final ReportingContext reportingContext, final long id, final int schuljahr, final int abschnitt) {
		super(id, schuljahr, abschnitt, null, null, null, null,
				new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());

		this.reportingContext = reportingContext;
	}


	/**
	 * Gibt den folgenden Schuljahresabschnitt zurück (lazy-loading).
	 *
	 * @return Der folgende Schuljahresabschnitt oder null.
	 */
	@Override
	public ReportingSchuljahresabschnitt folgenderAbschnitt() {
		if ((super.folgenderAbschnitt == null) && (super.idFolgenderAbschnitt != null)) {
			super.folgenderAbschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.idFolgenderAbschnitt);
		}
		return super.folgenderAbschnitt;
	}

	/**
	 * Gibt den vorherigen Schuljahresabschnitt zurück (lazy-loading).
	 *
	 * @return Der vorherige Schuljahresabschnitt oder null.
	 */
	@Override
	public ReportingSchuljahresabschnitt vorherigerAbschnitt() {
		if ((super.vorherigerAbschnitt == null) && (super.idVorherigerAbschnitt != null)) {
			super.vorherigerAbschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(super.idVorherigerAbschnitt);
		}
		return super.vorherigerAbschnitt;
	}


	/**
	 * Gibt die Map der Fächer dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Fächer, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingFach> faecher() {
		if ((super.faecher == null) || super.faecher.isEmpty()) {
			super.faecher = new HashMap<>();
			this.reportingContext.repositoryKataloge().faecher()
					.forEach(fach -> super.faecher.put(fach.ID, new ProxyReportingFach(fach, this.schuljahr)));
		}
		return super.faecher;
	}

	/**
	 * Gibt die Map der Jahrgänge dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Jahrgänge, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingJahrgang> jahrgaenge() {
		if ((super.jahrgaenge == null) || super.jahrgaenge.isEmpty()) {
			super.jahrgaenge = new HashMap<>();
			this.reportingContext.repositoryKataloge().jahrgaenge().forEach(jahrgang -> super.jahrgaenge.put(jahrgang.id,
					new ProxyReportingJahrgang(this.reportingContext, jahrgang, this)));
		}
		return super.jahrgaenge;
	}

	/**
	 * Gibt die Map der Klassen dieses Schuljahresabschnitts zurück.
	 *
	 * <p>Bei einem virtuellen Abschnitt bleibt die Map leer, da Klassen an einen Abschnitt in der Datenbank gebunden
	 * sind und für einen nicht existierenden Abschnitt fachlich nicht vorhanden sein können. Die Abfrage wird deshalb
	 * gar nicht erst an die Datenbank gestellt — siehe {@link ReportingSchuljahresabschnitt#istVirtuell()}.</p>
	 *
	 * @return Map der Klassen, die in diesem Schuljahresabschnitt gültig sind; bei einem virtuellen Abschnitt leer.
	 */
	@Override
	public Map<Long, ReportingKlasse> klassen() {
		if (istVirtuell()) {
			return super.klassen;
		}
		if ((super.klassen == null) || super.klassen.isEmpty()) {
			super.klassen = new HashMap<>();
			for (final ReportingKlasse klasse : this.reportingContext.repositoryLerngruppen().klassen(this.id())) {
				super.klassen.put(klasse.id(), klasse);
			}
		}
		return super.klassen;
	}

	/**
	 * Gibt die Map der Kurse dieses Schuljahresabschnitts zurück.
	 *
	 * <p>Bei einem virtuellen Abschnitt bleibt die Map leer, da Kurse an einen Abschnitt in der Datenbank gebunden sind
	 * und für einen nicht existierenden Abschnitt fachlich nicht vorhanden sein können. Die Abfrage wird deshalb gar
	 * nicht erst an die Datenbank gestellt — siehe {@link ReportingSchuljahresabschnitt#istVirtuell()}. Die Kurse einer
	 * GOSt-Blockung sind davon unberührt; sie stammen aus dem Blockungsergebnis und nicht aus dem Abschnitt.</p>
	 *
	 * @return Map der Kurse, die in diesem Schuljahresabschnitt gültig sind; bei einem virtuellen Abschnitt leer.
	 */
	@Override
	public Map<Long, ReportingKurs> kurse() {
		if (istVirtuell()) {
			return super.kurse;
		}
		if ((super.kurse == null) || super.kurse.isEmpty()) {
			super.kurse = new HashMap<>();
			for (final ReportingKurs kurs : this.reportingContext.repositoryLerngruppen().kurse(this.id())) {
				super.kurse.put(kurs.id(), kurs);
			}
		}
		return super.kurse;
	}

	/**
	 * Gibt die Map der Ankreuzkompetenzen dieses Schuljahresabschnitts zurück.
	 *
	 * @return Map der Ankreuzkompetenzen, die in diesem Schuljahresabschnitt gültig sind.
	 */
	@Override
	public Map<Long, ReportingAnkreuzkompetenz> ankreuzkompetenzen() {
		if ((super.ankreuzkompetenzen == null) || super.ankreuzkompetenzen.isEmpty()) {
			super.ankreuzkompetenzen = new HashMap<>();
			this.reportingContext.repositoryKataloge().ankreuzkompetenzen()
					.forEach(ankreuzkompetenz -> {
						if ((ankreuzkompetenz.abschnitt == 0) || (ankreuzkompetenz.abschnitt == super.abschnitt)) {
							super.ankreuzkompetenzen.put(ankreuzkompetenz.id, new ProxyReportingAnkreuzkompetenz(ankreuzkompetenz, this));
						}
					});
		}
		return super.ankreuzkompetenzen;
	}
}
