package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingSchuelerLernabschnitt}: hält die Sortierkonfiguration des Reporting-Typs
 * (Registry + Standardsortierung). Die fertige {@link ReportingSortierung}-Instanz wird über
 * {@link ReportingSchuelerLernabschnitt#SORTIERUNG} verwendet.
 */
public final class ReportingSchuelerLernabschnittSortierung {

	/** Schlüssel der Sortierung nach dem Schuljahr des Schuljahresabschnitts. */
	public static final String SCHULJAHRESABSCHNITT_SCHULJAHR = "schuljahresabschnitt.schuljahr";

	/** Schlüssel der Sortierung nach dem Abschnitt des Schuljahresabschnitts. */
	public static final String SCHULJAHRESABSCHNITT_ABSCHNITT = "schuljahresabschnitt.abschnitt";

	/** Die Sortierkonfiguration für {@link ReportingSchuelerLernabschnitt}. */
	public static final ReportingSortierung<ReportingSchuelerLernabschnitt> SORTIERUNG =
			ReportingSortierung.<ReportingSchuelerLernabschnitt>builder()
					.registry(buildRegistry())
					.standard(List.of(
							SCHULJAHRESABSCHNITT_SCHULJAHR,
							SCHULJAHRESABSCHNITT_ABSCHNITT,
							ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::wechselNr),
							ReportingTypesUtils.methodeToString(ReportingSchuelerLernabschnitt::id)))
					.build();

	private ReportingSchuelerLernabschnittSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingSchuelerLernabschnitt. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingSchuelerLernabschnitt> buildRegistry() {
		final SortierungRegistry<ReportingSchuelerLernabschnitt> reg = new SortierungRegistry<>();

		reg.registiereComparable(ReportingSchuelerLernabschnitt::id);
		reg.registiereComparable(ReportingSchuelerLernabschnitt::wechselNr);
		reg.registiereString(ReportingSchuelerLernabschnitt::datumAnfang);
		reg.registiereString(ReportingSchuelerLernabschnitt::datumEnde);

		// Verschachtelte Attribute des Schuljahresabschnitts
		reg.registiereComparable(SCHULJAHRESABSCHNITT_SCHULJAHR,
				la -> (la.schuljahresabschnitt() == null) ? null : la.schuljahresabschnitt().schuljahr());
		reg.registiereComparable(SCHULJAHRESABSCHNITT_ABSCHNITT,
				la -> (la.schuljahresabschnitt() == null) ? null : la.schuljahresabschnitt().abschnitt());

		return reg;
	}
}
