import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class LehrerFachrichtungenStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Eine Fachrichtung eines Lehrers.
	 */
	public fachrichtung: string = "";

	/**
	 * Die Qualifikation zu der Fachrichtung.
	 */
	public qualifikation: string = "";


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.LehrerFachrichtungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.LehrerFachrichtungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<LehrerFachrichtungenStatistikExport>('de.svws_nrw.asd.export.data.LehrerFachrichtungenStatistikExport');

	public static transpilerFromJSON(json: string): LehrerFachrichtungenStatistikExport {
		const obj = JSON.parse(json) as Partial<LehrerFachrichtungenStatistikExport>;
		const result = new LehrerFachrichtungenStatistikExport();
		if (obj.fachrichtung === undefined)
			throw new Error('invalid json format, missing attribute fachrichtung');
		result.fachrichtung = obj.fachrichtung;
		if (obj.qualifikation === undefined)
			throw new Error('invalid json format, missing attribute qualifikation');
		result.qualifikation = obj.qualifikation;
		return result;
	}

	public static transpilerToJSON(obj: LehrerFachrichtungenStatistikExport): string {
		let result = '{';
		result += '"fachrichtung" : ' + JSON.stringify(obj.fachrichtung) + ',';
		result += '"qualifikation" : ' + JSON.stringify(obj.qualifikation) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<LehrerFachrichtungenStatistikExport>): string {
		let result = '{';
		if (obj.fachrichtung !== undefined) {
			result += '"fachrichtung" : ' + JSON.stringify(obj.fachrichtung) + ',';
		}
		if (obj.qualifikation !== undefined) {
			result += '"qualifikation" : ' + JSON.stringify(obj.qualifikation) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_LehrerFachrichtungenStatistikExport(obj: unknown): LehrerFachrichtungenStatistikExport {
	return obj as LehrerFachrichtungenStatistikExport;
}
