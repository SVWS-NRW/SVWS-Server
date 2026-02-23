import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class VomSchulbesuchZurueckgestelltStatistikExport extends JavaObject {

	/**
	 * Die Anzahl der Zurückgestellten Kinder insgesamt.
	 */
	public zurueckgestelltInsgesamtZusammen: number = 0;

	/**
	 * Die Anzahl der Zurückgestellten Kinder insgesamt Weiblich.
	 */
	public zurueckgestelltInsgesamtWeiblich: number = 0;

	/**
	 * Die Anzahl der Zurückgestellten ausländischen Kinder zusammen.
	 */
	public zurueckgestelltAuslaenderZusammen: number = 0;

	/**
	 * Die Anzahl der Zurückgestellten ausländischen Kinder Weiblich.
	 */
	public zurueckgestelltAuslaenderWeiblich: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.VomSchulbesuchZurueckgestelltStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.VomSchulbesuchZurueckgestelltStatistikExport'].includes(name);
	}

	public static readonly class = new Class<VomSchulbesuchZurueckgestelltStatistikExport>('de.svws_nrw.asd.export.data.VomSchulbesuchZurueckgestelltStatistikExport');

	public static transpilerFromJSON(json: string): VomSchulbesuchZurueckgestelltStatistikExport {
		const obj = JSON.parse(json) as Partial<VomSchulbesuchZurueckgestelltStatistikExport>;
		const result = new VomSchulbesuchZurueckgestelltStatistikExport();
		if (obj.zurueckgestelltInsgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute zurueckgestelltInsgesamtZusammen');
		result.zurueckgestelltInsgesamtZusammen = obj.zurueckgestelltInsgesamtZusammen;
		if (obj.zurueckgestelltInsgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute zurueckgestelltInsgesamtWeiblich');
		result.zurueckgestelltInsgesamtWeiblich = obj.zurueckgestelltInsgesamtWeiblich;
		if (obj.zurueckgestelltAuslaenderZusammen === undefined)
			throw new Error('invalid json format, missing attribute zurueckgestelltAuslaenderZusammen');
		result.zurueckgestelltAuslaenderZusammen = obj.zurueckgestelltAuslaenderZusammen;
		if (obj.zurueckgestelltAuslaenderWeiblich === undefined)
			throw new Error('invalid json format, missing attribute zurueckgestelltAuslaenderWeiblich');
		result.zurueckgestelltAuslaenderWeiblich = obj.zurueckgestelltAuslaenderWeiblich;
		return result;
	}

	public static transpilerToJSON(obj: VomSchulbesuchZurueckgestelltStatistikExport): string {
		let result = '{';
		result += '"zurueckgestelltInsgesamtZusammen" : ' + obj.zurueckgestelltInsgesamtZusammen.toString() + ',';
		result += '"zurueckgestelltInsgesamtWeiblich" : ' + obj.zurueckgestelltInsgesamtWeiblich.toString() + ',';
		result += '"zurueckgestelltAuslaenderZusammen" : ' + obj.zurueckgestelltAuslaenderZusammen.toString() + ',';
		result += '"zurueckgestelltAuslaenderWeiblich" : ' + obj.zurueckgestelltAuslaenderWeiblich.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<VomSchulbesuchZurueckgestelltStatistikExport>): string {
		let result = '{';
		if (obj.zurueckgestelltInsgesamtZusammen !== undefined) {
			result += '"zurueckgestelltInsgesamtZusammen" : ' + obj.zurueckgestelltInsgesamtZusammen.toString() + ',';
		}
		if (obj.zurueckgestelltInsgesamtWeiblich !== undefined) {
			result += '"zurueckgestelltInsgesamtWeiblich" : ' + obj.zurueckgestelltInsgesamtWeiblich.toString() + ',';
		}
		if (obj.zurueckgestelltAuslaenderZusammen !== undefined) {
			result += '"zurueckgestelltAuslaenderZusammen" : ' + obj.zurueckgestelltAuslaenderZusammen.toString() + ',';
		}
		if (obj.zurueckgestelltAuslaenderWeiblich !== undefined) {
			result += '"zurueckgestelltAuslaenderWeiblich" : ' + obj.zurueckgestelltAuslaenderWeiblich.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_VomSchulbesuchZurueckgestelltStatistikExport(obj: unknown): VomSchulbesuchZurueckgestelltStatistikExport {
	return obj as VomSchulbesuchZurueckgestelltStatistikExport;
}
