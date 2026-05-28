package de.svws_nrw.module.reporting.types.lehrer;

import java.util.List;

import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingLehrer}: hält die Sortierkonfiguration des Reporting-Typs (Registry + Standardsortierung).
 * Die fertige {@link ReportingSortierung}-Instanz wird über {@link ReportingLehrer#SORTIERUNG} verwendet.
 */
public final class ReportingLehrerSortierung {

	/** Die Sortierkonfiguration für {@link ReportingLehrer}. */
	public static final ReportingSortierung<ReportingLehrer> SORTIERUNG =
			ReportingSortierung.<ReportingLehrer>builder()
					.registry(buildRegistry())
					.standard(List.of(
							ReportingTypesUtils.methodeToString(ReportingLehrer::nachname),
							ReportingTypesUtils.methodeToString(ReportingLehrer::vorname),
							ReportingTypesUtils.methodeToString(ReportingLehrer::vornamen),
							ReportingTypesUtils.methodeToString(ReportingLehrer::geburtsdatum),
							ReportingTypesUtils.methodeToString(ReportingLehrer::id)))
					.build();

	private ReportingLehrerSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingLehrer. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingLehrer> buildRegistry() {
		final SortierungRegistry<ReportingLehrer> reg = new SortierungRegistry<>();

		// Personenattribute (von ReportingPerson geerbt)
		reg.registiereString(ReportingLehrer::nachname);
		reg.registiereString(ReportingLehrer::vorname);
		reg.registiereString(ReportingLehrer::vornamen);
		reg.registiereString(ReportingLehrer::anrede);
		reg.registiereString(ReportingLehrer::titel);
		reg.registiereString(ReportingLehrer::geburtsdatum);
		reg.registiereString(ReportingLehrer::geburtsname);
		reg.registiereString(ReportingLehrer::geburtsort);
		reg.registiereComparable(ReportingLehrer::geschlecht);
		reg.registiereString(ReportingLehrer::emailPrivat);
		reg.registiereString(ReportingLehrer::emailSchule);
		reg.registiereString(ReportingLehrer::telefonPrivat);
		reg.registiereString(ReportingLehrer::telefonPrivatMobil);
		reg.registiereString(ReportingLehrer::strassenname);
		reg.registiereString(ReportingLehrer::hausnummer);
		reg.registiereString(ReportingLehrer::hausnummerZusatz);
		reg.registiereString(ReportingLehrer::wohnortname);
		reg.registiereString(ReportingLehrer::wohnortsteilname);

		// Lehrerspezifische Attribute
		reg.registiereComparable(ReportingLehrer::id);
		reg.registiereString(ReportingLehrer::kuerzel);
		reg.registiereString(ReportingLehrer::amtsbezeichnung);
		reg.registiereComparable(ReportingLehrer::personalTyp);

		return reg;
	}
}
