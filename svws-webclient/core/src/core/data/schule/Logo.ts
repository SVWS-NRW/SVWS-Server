import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class Logo extends JavaObject {

	/**
	 * Die ID des Eintrags.
	 */
	public id: number = -1;

	/**
	 * Die Kennung des Logos.
	 */
	public kennung: string = "";

	/**
	 * Die Bezeichnung des Logos.
	 */
	public bezeichnung: string = "";

	/**
	 * Die Beschreibung des Logos.
	 */
	public beschreibung: string = "";

	/**
	 * Das Logo als Bild im Base64-Format.
	 */
	public logoBase64: string = "";

	/**
	 * Datum, wann das Logo hinzugefügt wurde.
	 */
	public hinzugefuegtAm: string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Logo';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schule.Logo'].includes(name);
	}

	public static readonly class = new Class<Logo>('de.svws_nrw.core.data.schule.Logo');

	public static transpilerFromJSON(json: string): Logo {
		const obj = JSON.parse(json) as Partial<Logo>;
		const result = new Logo();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kennung === undefined)
			throw new Error('invalid json format, missing attribute kennung');
		result.kennung = obj.kennung;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.beschreibung === undefined)
			throw new Error('invalid json format, missing attribute beschreibung');
		result.beschreibung = obj.beschreibung;
		if (obj.logoBase64 === undefined)
			throw new Error('invalid json format, missing attribute logoBase64');
		result.logoBase64 = obj.logoBase64;
		if (obj.hinzugefuegtAm === undefined)
			throw new Error('invalid json format, missing attribute hinzugefuegtAm');
		result.hinzugefuegtAm = obj.hinzugefuegtAm;
		return result;
	}

	public static transpilerToJSON(obj: Logo): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kennung" : ' + JSON.stringify(obj.kennung) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		result += '"logoBase64" : ' + JSON.stringify(obj.logoBase64) + ',';
		result += '"hinzugefuegtAm" : ' + JSON.stringify(obj.hinzugefuegtAm) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<Logo>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kennung !== undefined) {
			result += '"kennung" : ' + JSON.stringify(obj.kennung) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.beschreibung !== undefined) {
			result += '"beschreibung" : ' + JSON.stringify(obj.beschreibung) + ',';
		}
		if (obj.logoBase64 !== undefined) {
			result += '"logoBase64" : ' + JSON.stringify(obj.logoBase64) + ',';
		}
		if (obj.hinzugefuegtAm !== undefined) {
			result += '"hinzugefuegtAm" : ' + JSON.stringify(obj.hinzugefuegtAm) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Logo(obj: unknown): Logo {
	return obj as Logo;
}
