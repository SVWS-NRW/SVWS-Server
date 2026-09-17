import { JavaObject } from '../../../../java/lang/JavaObject';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';

export class ReportingReportvorlageSchulformen extends JavaObject {

	/**
	 * Die Schulformen mit gymnasialer Oberstufe. Ein Test gleicht die Liste mit den Katalogeinträgen ab, für die {@code hatGymOb} gilt.
	 */
	public static readonly GOST: List<Schulform> = ArrayList.of(Schulform.GY, Schulform.GE, Schulform.SG, Schulform.FW, Schulform.WF);


	private constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageSchulformen';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageSchulformen'].includes(name);
	}

	public static readonly class = new Class<ReportingReportvorlageSchulformen>('de.svws_nrw.core.types.reporting.reportvorlagekonfiguration.ReportingReportvorlageSchulformen');

}

export function cast_de_svws_nrw_core_types_reporting_reportvorlagekonfiguration_ReportingReportvorlageSchulformen(obj: unknown): ReportingReportvorlageSchulformen {
	return obj as ReportingReportvorlageSchulformen;
}
