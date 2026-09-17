package de.svws_nrw.core.types.reporting.reportvorlagekonfiguration;

import java.util.List;

import de.svws_nrw.asd.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Die Schulform-Listen, auf die sich Report-Vorlagen beschränken. Die Listen nennen ihre Schulformen im Quelltext, weil die Einträge der Enum
 * {@code ReportingReportvorlage} beim Laden der Klasse entstehen und der Schulform-Katalog dann noch nicht geladen ist.
 */
public final class ReportingReportvorlageSchulformen {

	/** Die Schulformen mit gymnasialer Oberstufe. Ein Test gleicht die Liste mit den Katalogeinträgen ab, für die {@code hatGymOb} gilt. */
	public static final @NotNull List<Schulform> GOST = List.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF);

	private ReportingReportvorlageSchulformen() {
	}

}
