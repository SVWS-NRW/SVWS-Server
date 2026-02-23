import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class KlassenHerkunftAnrechungenStatistikExport extends JavaObject {

	/**
	 * Die Schüler ohne Anrechung.
	 */
	public anrechnungOhne: number = 0;

	/**
	 * Die Schüler mit 6 Monate Anrechung.
	 */
	public anrechnung6Monate: number = 0;

	/**
	 * Die Schüler mit 12 Monate Anrechung.
	 */
	public anrechnung12Monate: number = 0;

	/**
	 * Die Schüler mit 18 Monate Anrechung.
	 */
	public anrechnung18Monate: number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.export.data.KlassenHerkunftAnrechungenStatistikExport';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.export.data.KlassenHerkunftAnrechungenStatistikExport'].includes(name);
	}

	public static readonly class = new Class<KlassenHerkunftAnrechungenStatistikExport>('de.svws_nrw.asd.export.data.KlassenHerkunftAnrechungenStatistikExport');

	public static transpilerFromJSON(json: string): KlassenHerkunftAnrechungenStatistikExport {
		const obj = JSON.parse(json) as Partial<KlassenHerkunftAnrechungenStatistikExport>;
		const result = new KlassenHerkunftAnrechungenStatistikExport();
		if (obj.anrechnungOhne === undefined)
			throw new Error('invalid json format, missing attribute anrechnungOhne');
		result.anrechnungOhne = obj.anrechnungOhne;
		if (obj.anrechnung6Monate === undefined)
			throw new Error('invalid json format, missing attribute anrechnung6Monate');
		result.anrechnung6Monate = obj.anrechnung6Monate;
		if (obj.anrechnung12Monate === undefined)
			throw new Error('invalid json format, missing attribute anrechnung12Monate');
		result.anrechnung12Monate = obj.anrechnung12Monate;
		if (obj.anrechnung18Monate === undefined)
			throw new Error('invalid json format, missing attribute anrechnung18Monate');
		result.anrechnung18Monate = obj.anrechnung18Monate;
		return result;
	}

	public static transpilerToJSON(obj: KlassenHerkunftAnrechungenStatistikExport): string {
		let result = '{';
		result += '"anrechnungOhne" : ' + obj.anrechnungOhne.toString() + ',';
		result += '"anrechnung6Monate" : ' + obj.anrechnung6Monate.toString() + ',';
		result += '"anrechnung12Monate" : ' + obj.anrechnung12Monate.toString() + ',';
		result += '"anrechnung18Monate" : ' + obj.anrechnung18Monate.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KlassenHerkunftAnrechungenStatistikExport>): string {
		let result = '{';
		if (obj.anrechnungOhne !== undefined) {
			result += '"anrechnungOhne" : ' + obj.anrechnungOhne.toString() + ',';
		}
		if (obj.anrechnung6Monate !== undefined) {
			result += '"anrechnung6Monate" : ' + obj.anrechnung6Monate.toString() + ',';
		}
		if (obj.anrechnung12Monate !== undefined) {
			result += '"anrechnung12Monate" : ' + obj.anrechnung12Monate.toString() + ',';
		}
		if (obj.anrechnung18Monate !== undefined) {
			result += '"anrechnung18Monate" : ' + obj.anrechnung18Monate.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_export_data_KlassenHerkunftAnrechungenStatistikExport(obj: unknown): KlassenHerkunftAnrechungenStatistikExport {
	return obj as KlassenHerkunftAnrechungenStatistikExport;
}
