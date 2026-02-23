import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class AbgaengerDetailStatistikExport extends JavaObject {

	/**
	 * Satzschlüssel: Die Abgangsart bzw. die Abschlüsse.
	 */
	public abgangsart: string | null = "";

	/**
	 * Satzschlüssel: Das Geburtsjahr der abgehenden Schüler.
	 */
	public geburtsjahr: string | null = "";

	/**
	 * Satzschlüssel: Die Staatsangehörigkeit der abgehenden Schüler.
	 */
	public staatsangehoerigkeit: string | null = "";

	/**
	 * Die abgehenden Schüler zu dieser Abgangsart Zusammen.
	 */
	public abschluesseInsgesamtZusammen: number = 0;

	/**
	 * Die abgehenden Schüler zu dieser Abgangsart Zusammen Weiblich.
	 */
	public abschluesseInsgesamtWeiblich: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.AbgaengerDetailStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.AbgaengerDetailStatistikExport'].includes(name);
	}

	public static readonly class = new Class<AbgaengerDetailStatistikExport>('de.svws_nrw.asd.export.data.AbgaengerDetailStatistikExport');

	public static transpilerFromJSON(json: string): AbgaengerDetailStatistikExport {
		const obj = JSON.parse(json) as Partial<AbgaengerDetailStatistikExport>;
		const result = new AbgaengerDetailStatistikExport();
		result.abgangsart = (obj.abgangsart === undefined) ? null : obj.abgangsart === null ? null : obj.abgangsart;
		result.geburtsjahr = (obj.geburtsjahr === undefined) ? null : obj.geburtsjahr === null ? null : obj.geburtsjahr;
		result.staatsangehoerigkeit = (obj.staatsangehoerigkeit === undefined) ? null : obj.staatsangehoerigkeit === null ? null : obj.staatsangehoerigkeit;
		if (obj.abschluesseInsgesamtZusammen === undefined)
			throw new Error('invalid json format, missing attribute abschluesseInsgesamtZusammen');
		result.abschluesseInsgesamtZusammen = obj.abschluesseInsgesamtZusammen;
		if (obj.abschluesseInsgesamtWeiblich === undefined)
			throw new Error('invalid json format, missing attribute abschluesseInsgesamtWeiblich');
		result.abschluesseInsgesamtWeiblich = obj.abschluesseInsgesamtWeiblich;
		return result;
	}

	public static transpilerToJSON(obj: AbgaengerDetailStatistikExport): string {
		let result = '{';
		result += '"abgangsart" : ' + ((obj.abgangsart === null) ? 'null' : JSON.stringify(obj.abgangsart)) + ',';
		result += '"geburtsjahr" : ' + ((obj.geburtsjahr === null) ? 'null' : JSON.stringify(obj.geburtsjahr)) + ',';
		result += '"staatsangehoerigkeit" : ' + ((obj.staatsangehoerigkeit === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit)) + ',';
		result += '"abschluesseInsgesamtZusammen" : ' + obj.abschluesseInsgesamtZusammen.toString() + ',';
		result += '"abschluesseInsgesamtWeiblich" : ' + obj.abschluesseInsgesamtWeiblich.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<AbgaengerDetailStatistikExport>): string {
		let result = '{';
		if (obj.abgangsart !== undefined) {
			result += '"abgangsart" : ' + ((obj.abgangsart === null) ? 'null' : JSON.stringify(obj.abgangsart)) + ',';
		}
		if (obj.geburtsjahr !== undefined) {
			result += '"geburtsjahr" : ' + ((obj.geburtsjahr === null) ? 'null' : JSON.stringify(obj.geburtsjahr)) + ',';
		}
		if (obj.staatsangehoerigkeit !== undefined) {
			result += '"staatsangehoerigkeit" : ' + ((obj.staatsangehoerigkeit === null) ? 'null' : JSON.stringify(obj.staatsangehoerigkeit)) + ',';
		}
		if (obj.abschluesseInsgesamtZusammen !== undefined) {
			result += '"abschluesseInsgesamtZusammen" : ' + obj.abschluesseInsgesamtZusammen.toString() + ',';
		}
		if (obj.abschluesseInsgesamtWeiblich !== undefined) {
			result += '"abschluesseInsgesamtWeiblich" : ' + obj.abschluesseInsgesamtWeiblich.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_AbgaengerDetailStatistikExport(obj: unknown): AbgaengerDetailStatistikExport {
	return obj as AbgaengerDetailStatistikExport;
}
