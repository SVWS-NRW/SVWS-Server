package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.module.reporting.types.ankreuzkompetenz.ReportingAnkreuzkompetenz;

/**
 * Proxy-Klasse im Rahmen des Reportings für eine Schüler-Ankreuzkompetenz und erweitert die Klasse {@link ReportingSchuelerAnkreuzkompetenz}.
 */
public class ProxyReportingSchuelerAnkreuzkompetenz extends ReportingSchuelerAnkreuzkompetenz {

	/** Die ID des zugehörigen Ankreuzkompetenz-Katalog-Eintrags (für Lazy-Loading). */
	@JsonIgnore
	private final long idFloskel;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingSchuelerAnkreuzkompetenz}.
	 *
	 * @param dto           Der Datensatz der Schüler-Ankreuzkompetenz aus der Datenbank.
	 * @param lernabschnitt Der Lernabschnitt, dem die Belegung zugeordnet ist.
	 */
	public ProxyReportingSchuelerAnkreuzkompetenz(final DTOSchuelerAnkreuzfloskeln dto, final ReportingSchuelerLernabschnitt lernabschnitt) {
		super(null, dto.ID, lernabschnitt, dto.Stufe1, dto.Stufe2, dto.Stufe3, dto.Stufe4, dto.Stufe5);
		this.idFloskel = dto.Floskel_ID;
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
	 * Stellt die zugehörige Ankreuzkompetenz (Katalog-Eintrag) zur Verfügung. Beim ersten Zugriff wird sie lazy aus dem Schuljahresabschnitt
	 * des Lernabschnitts aufgelöst.
	 *
	 * @return Die zugehörige Ankreuzkompetenz oder null, falls sie nicht aufgelöst werden konnte.
	 */
	@Override
	public ReportingAnkreuzkompetenz ankreuzkompetenz() {
		if ((super.ankreuzkompetenz == null) && (super.lernabschnitt != null) && (super.lernabschnitt.schuljahresabschnitt() != null)) {
			super.ankreuzkompetenz = super.lernabschnitt.schuljahresabschnitt().ankreuzkompetenz(this.idFloskel);
		}
		return super.ankreuzkompetenz;
	}

}
