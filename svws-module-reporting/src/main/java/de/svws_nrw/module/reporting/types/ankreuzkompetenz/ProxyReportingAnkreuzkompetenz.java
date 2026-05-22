package de.svws_nrw.module.reporting.types.ankreuzkompetenz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Ankreuzkompetenz und erweitert die Klasse {@link ReportingAnkreuzkompetenz}.
 */
public class ProxyReportingAnkreuzkompetenz extends ReportingAnkreuzkompetenz {

	/** Die ID des Faches, dem die Ankreuzkompetenz zugeordnet ist (für Lazy-Loading), oder null. */
	@JsonIgnore
	private final Long idFach;

	/** Die IDs der Jahrgänge, denen die Ankreuzkompetenz zugeordnet ist (für Lazy-Loading). */
	@JsonIgnore
	private final List<Long> idsJahrgaenge;

	/** Der Schuljahresabschnitt, in dessen Kontext Fach und Jahrgänge aufgelöst werden. */
	@JsonIgnore
	private final ReportingSchuljahresabschnitt schuljahresabschnitt;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingAnkreuzkompetenz}.
	 *
	 * @param ankreuzkompetenz		Die Daten der Ankreuzkompetenz aus dem Core-DTO.
	 * @param schuljahresabschnitt	Der Schuljahresabschnitt, in dessen Kontext Fach und Jahrgänge aufgelöst werden.
	 */
	public ProxyReportingAnkreuzkompetenz(final Ankreuzkompetenz ankreuzkompetenz, final ReportingSchuljahresabschnitt schuljahresabschnitt) {
		super(ankreuzkompetenz.abschnitt,
				null,
				ankreuzkompetenz.fachSortierung,
				ersetzeNullBlankTrim(ankreuzkompetenz.floskelText),
				ankreuzkompetenz.id,
				ankreuzkompetenz.istAktiv,
				ankreuzkompetenz.istASV,
				ankreuzkompetenz.istSichtbar,
				new HashMap<>(),
				ersetzeNullBlankTrim(ankreuzkompetenz.schulgliederung),
				ankreuzkompetenz.sortierung);

		this.idFach = ankreuzkompetenz.idFach;
		this.idsJahrgaenge = ankreuzkompetenz.jahrgaengezuordnung.stream().map(z -> z.idJahrgang).toList();
		this.schuljahresabschnitt = schuljahresabschnitt;
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
	 * Stellt das zugeordnete Fach zur Verfügung. Beim ersten Zugriff wird das Fach lazy aus dem Schuljahresabschnitt aufgelöst.
	 *
	 * @return Das zugeordnete Fach oder null, falls die Ankreuzkompetenz dem Arbeits- und Sozialverhalten zugeordnet ist oder kein Fach gefunden wurde.
	 */
	@Override
	public ReportingFach fach() {
		if ((super.fach == null) && (this.idFach != null) && (this.schuljahresabschnitt != null)) {
			super.fach = this.schuljahresabschnitt.fach(this.idFach);
		}
		return super.fach;
	}

	/**
	 * Stellt die Map der zugeordneten Jahrgänge zur Verfügung. Beim ersten Zugriff werden die Jahrgänge lazy aus dem Schuljahresabschnitt aufgelöst.
	 *
	 * @return Map der zugeordneten Jahrgänge, indiziert nach Jahrgangs-ID.
	 */
	@Override
	public Map<Long, ReportingJahrgang> jahrgaenge() {
		if (super.jahrgaenge.isEmpty() && !this.idsJahrgaenge.isEmpty() && (this.schuljahresabschnitt != null)) {
			for (final Long idJahrgang : this.idsJahrgaenge) {
				if (idJahrgang == null) {
					continue;
				}
				final ReportingJahrgang jahrgang = this.schuljahresabschnitt.jahrgang(idJahrgang);
				if (jahrgang != null) {
					super.jahrgaenge.put(jahrgang.id(), jahrgang);
				}
			}
		}
		return super.jahrgaenge;
	}

}
