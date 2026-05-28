package de.svws_nrw.module.reporting.types.lehrer;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingLehrer}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingLehrer#FILTER} verwendet.
 */
public final class ReportingLehrerFilter {

	/** Die Filterkonfiguration für {@link ReportingLehrer}. */
	public static final ReportingFilterung<ReportingLehrer> FILTER =
			ReportingFilterung.<ReportingLehrer>builder()
					.registry(buildRegistry())
					.build();

	private ReportingLehrerFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingLehrer. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingLehrer> buildRegistry() {
		final FilterRegistry<ReportingLehrer> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingLehrer::id);
		reg.registriereAttribut(ReportingLehrer::kuerzel);
		reg.registriereAttribut(ReportingLehrer::nachname);
		reg.registriereAttribut(ReportingLehrer::vorname);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingLehrer::geschlecht),
				l -> (l.geschlecht() == null) ? null : l.geschlecht().name());

		// Lehrerspezifische Attribute
		reg.registriereAttribut(ReportingLehrer::amtsbezeichnung);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingLehrer::personalTyp),
				l -> (l.personalTyp() == null) ? null : l.personalTyp().name());

		return reg;
	}
}
