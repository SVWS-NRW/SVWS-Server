import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class InternatsplaetzeStatistikExport extends JavaObject {

	/**
	 * Die Art des Internatsbetriebs.
	 */
	public internatsart: string | null = "";

	/**
	 * Die Internatsplätze für Jungen.
	 */
	public internatsplaetzeJungen: number = 0;

	/**
	 * Die belegten Internatsplätze für Jungen.
	 */
	public internatsplaetzeJungenBelegt: number = 0;

	/**
	 * Die Internatsplätze für Mädchen.
	 */
	public internatsplaetzeMaedchen: number = 0;

	/**
	 * Die belegten Internatsplätze für Mädchen.
	 */
	public internatsplaetzeMaedchenBelegt: number = 0;

	/**
	 * Die Internatsplätze geschlechtsneutral.
	 */
	public internatsplaetzeGeschlechtsneutral: number = 0;

	/**
	 * Die belegten Internatsplätze geschlechtsneutral.
	 */
	public internatsplaetzeGeschlechtsneutralBelegt: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.InternatsplaetzeStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.InternatsplaetzeStatistikExport'].includes(name);
	}

	public static readonly class = new Class<InternatsplaetzeStatistikExport>('de.svws_nrw.asd.export.data.InternatsplaetzeStatistikExport');

	public static transpilerFromJSON(json: string): InternatsplaetzeStatistikExport {
		const obj = JSON.parse(json) as Partial<InternatsplaetzeStatistikExport>;
		const result = new InternatsplaetzeStatistikExport();
		result.internatsart = (obj.internatsart === undefined) ? null : obj.internatsart === null ? null : obj.internatsart;
		if (obj.internatsplaetzeJungen === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeJungen');
		result.internatsplaetzeJungen = obj.internatsplaetzeJungen;
		if (obj.internatsplaetzeJungenBelegt === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeJungenBelegt');
		result.internatsplaetzeJungenBelegt = obj.internatsplaetzeJungenBelegt;
		if (obj.internatsplaetzeMaedchen === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeMaedchen');
		result.internatsplaetzeMaedchen = obj.internatsplaetzeMaedchen;
		if (obj.internatsplaetzeMaedchenBelegt === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeMaedchenBelegt');
		result.internatsplaetzeMaedchenBelegt = obj.internatsplaetzeMaedchenBelegt;
		if (obj.internatsplaetzeGeschlechtsneutral === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeGeschlechtsneutral');
		result.internatsplaetzeGeschlechtsneutral = obj.internatsplaetzeGeschlechtsneutral;
		if (obj.internatsplaetzeGeschlechtsneutralBelegt === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetzeGeschlechtsneutralBelegt');
		result.internatsplaetzeGeschlechtsneutralBelegt = obj.internatsplaetzeGeschlechtsneutralBelegt;
		return result;
	}

	public static transpilerToJSON(obj: InternatsplaetzeStatistikExport): string {
		let result = '{';
		result += '"internatsart" : ' + ((obj.internatsart === null) ? 'null' : JSON.stringify(obj.internatsart)) + ',';
		result += '"internatsplaetzeJungen" : ' + obj.internatsplaetzeJungen.toString() + ',';
		result += '"internatsplaetzeJungenBelegt" : ' + obj.internatsplaetzeJungenBelegt.toString() + ',';
		result += '"internatsplaetzeMaedchen" : ' + obj.internatsplaetzeMaedchen.toString() + ',';
		result += '"internatsplaetzeMaedchenBelegt" : ' + obj.internatsplaetzeMaedchenBelegt.toString() + ',';
		result += '"internatsplaetzeGeschlechtsneutral" : ' + obj.internatsplaetzeGeschlechtsneutral.toString() + ',';
		result += '"internatsplaetzeGeschlechtsneutralBelegt" : ' + obj.internatsplaetzeGeschlechtsneutralBelegt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<InternatsplaetzeStatistikExport>): string {
		let result = '{';
		if (obj.internatsart !== undefined) {
			result += '"internatsart" : ' + ((obj.internatsart === null) ? 'null' : JSON.stringify(obj.internatsart)) + ',';
		}
		if (obj.internatsplaetzeJungen !== undefined) {
			result += '"internatsplaetzeJungen" : ' + obj.internatsplaetzeJungen.toString() + ',';
		}
		if (obj.internatsplaetzeJungenBelegt !== undefined) {
			result += '"internatsplaetzeJungenBelegt" : ' + obj.internatsplaetzeJungenBelegt.toString() + ',';
		}
		if (obj.internatsplaetzeMaedchen !== undefined) {
			result += '"internatsplaetzeMaedchen" : ' + obj.internatsplaetzeMaedchen.toString() + ',';
		}
		if (obj.internatsplaetzeMaedchenBelegt !== undefined) {
			result += '"internatsplaetzeMaedchenBelegt" : ' + obj.internatsplaetzeMaedchenBelegt.toString() + ',';
		}
		if (obj.internatsplaetzeGeschlechtsneutral !== undefined) {
			result += '"internatsplaetzeGeschlechtsneutral" : ' + obj.internatsplaetzeGeschlechtsneutral.toString() + ',';
		}
		if (obj.internatsplaetzeGeschlechtsneutralBelegt !== undefined) {
			result += '"internatsplaetzeGeschlechtsneutralBelegt" : ' + obj.internatsplaetzeGeschlechtsneutralBelegt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_InternatsplaetzeStatistikExport(obj: unknown): InternatsplaetzeStatistikExport {
	return obj as InternatsplaetzeStatistikExport;
}
