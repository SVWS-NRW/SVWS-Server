package de.svws_nrw.module.reporting.types.fach;

import de.svws_nrw.module.reporting.filterung.FilterRegistry;
import de.svws_nrw.module.reporting.filterung.ReportingFilterung;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingFach}: hält die Filterkonfiguration des Reporting-Typs (Registry).
 * Die fertige {@link ReportingFilterung}-Instanz wird über {@link ReportingFach#FILTER} verwendet.
 */
public final class ReportingFachFilter {

	/** Die Filterkonfiguration für {@link ReportingFach}. */
	public static final ReportingFilterung<ReportingFach> FILTER =
			ReportingFilterung.<ReportingFach>builder()
					.registry(buildRegistry())
					.build();

	private ReportingFachFilter() {
		throw new IllegalStateException("Begleit-Klasse zur Filterung von ReportingFach. Initialisierung nicht möglich.");
	}

	private static FilterRegistry<ReportingFach> buildRegistry() {
		final FilterRegistry<ReportingFach> reg = new FilterRegistry<>();

		// Grundlegende Attribute
		reg.registriereAttribut(ReportingFach::id);
		reg.registriereAttribut(ReportingFach::kuerzel);
		reg.registriereAttribut(ReportingFach::bezeichnung);
		reg.registriereAttribut(ReportingFach::bezeichnungZeugnis);
		reg.registriereAttribut(ReportingFach::bezeichnungUeberweisungszeugnis);
		reg.registriereAttribut(ReportingFach::sortierungEintrag);

		// Fachspezifische Attribute
		reg.registriereAttribut(ReportingFach::aufgabenfeld);
		reg.registriereAttribut(ReportingFach::bilingualeSprache);
		reg.registriereAttribut(ReportingFach::aufZeugnis);
		reg.registriereAttribut(ReportingTypesUtils.methodeToString(ReportingFach::fachgruppe), f -> (f.fachgruppe() == null) ? null : f.fachgruppe().name());
		reg.registriereAttribut(ReportingFach::istPruefungsordnungsRelevant);
		reg.registriereAttribut(ReportingFach::istGostFach);
		reg.registriereAttribut(ReportingFach::istSichtbar);
		reg.registriereAttribut(ReportingFach::istFremdsprache);
		reg.registriereAttribut(ReportingFach::istFremdSpracheNeuEinsetzend);
		reg.registriereAttribut(ReportingFach::istNachpruefungErlaubt);
		reg.registriereAttribut(ReportingFach::istSchriftlichBA);
		reg.registriereAttribut(ReportingFach::istSchriftlichZK);

		// Attribut für das Statistik-Fach (ASD-Kürzel)
		reg.registriereAttribut("statistikKuerzel", f -> (f.statistikfach() == null) ? null : f.statistikfach().kuerzelASD());

		return reg;
	}
}
