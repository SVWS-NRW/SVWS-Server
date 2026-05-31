package de.svws_nrw.module.reporting.types.gost.abitur;

import java.util.List;

import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistry;
import de.svws_nrw.module.reporting.utils.ReportingTypesUtils;

/**
 * Begleit-Datei zu {@link ReportingGostAbiturFachbelegung}: hält die Sortierkonfiguration des Reporting-Typs
 * (Registry + Standardsortierung). Die fertige {@link ReportingSortierung}-Instanz wird über
 * {@link ReportingGostAbiturFachbelegung#SORTIERUNG} verwendet.
 *
 * <p>Die Standardsortierung folgt der GOSt-Fachreihenfolge (analog zu
 * {@link ReportingGostAbiturFachbelegung#compareToGost}) und nutzt dafür den Registry-Eintrag
 * {@link #GOST_SORTIERUNG}, der auf {@link GostFachbereich#getSortierung(Fach)} delegiert.
 */
public final class ReportingGostAbiturFachbelegungSortierung {

	/** Schlüssel der GOSt-spezifischen Fachreihenfolge. */
	public static final String GOST_SORTIERUNG = "gostSortierung";

	/** Die Sortierkonfiguration für {@link ReportingGostAbiturFachbelegung}. */
	public static final ReportingSortierung<ReportingGostAbiturFachbelegung> SORTIERUNG =
			ReportingSortierung.<ReportingGostAbiturFachbelegung>builder()
					.registry(buildRegistry())
					.standard(List.of(
							GOST_SORTIERUNG,
							ReportingTypesUtils.methodeToString(ReportingGostAbiturFachbelegung::abiturFach)))
					.build();

	private ReportingGostAbiturFachbelegungSortierung() {
		throw new IllegalStateException("Begleit-Klasse zur Sortierung von ReportingGostAbiturFachbelegung. Initialisierung nicht möglich.");
	}

	private static SortierungRegistry<ReportingGostAbiturFachbelegung> buildRegistry() {
		final SortierungRegistry<ReportingGostAbiturFachbelegung> reg = new SortierungRegistry<>();

		reg.registiereComparable(ReportingGostAbiturFachbelegung::abiturFach);
		reg.registiereString(ReportingGostAbiturFachbelegung::letzteKursart);

		// GOSt-spezifische Fachreihenfolge, semantisch identisch zu ReportingFach.compareToGost.
		reg.registiereComparable(GOST_SORTIERUNG, fb -> {
			if ((fb.fach() == null) || (fb.fach().statistikfach() == null)) {
				return Integer.MAX_VALUE;
			}
			return GostFachbereich.getSortierung(Fach.getBySchluesselOrDefault(fb.fach().statistikfach().kuerzelASD()));
		});

		return reg;
	}
}
