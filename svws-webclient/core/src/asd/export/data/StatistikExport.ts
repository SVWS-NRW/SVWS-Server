import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StatistikExport extends JavaObject {


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.StatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.StatistikExport'].includes(name);
	}

	public static readonly class = new Class<StatistikExport>('de.svws_nrw.asd.export.data.StatistikExport');

}

export function cast_de_svws_nrw_asd_export_data_StatistikExport(obj: unknown): StatistikExport {
	return obj as StatistikExport;
}
