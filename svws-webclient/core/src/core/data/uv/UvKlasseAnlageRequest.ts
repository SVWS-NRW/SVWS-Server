import { JavaObject } from '../../../java/lang/JavaObject';
import { UvKlasse, cast_de_svws_nrw_core_data_uv_UvKlasse } from '../../../core/data/uv/UvKlasse';
import { Class } from '../../../java/lang/Class';
import { UvSchuelergruppeCreateRequest } from '../../../core/data/uv/UvSchuelergruppeCreateRequest';

export class UvKlasseAnlageRequest extends JavaObject {

	/**
	 * Die anzulegende Klasse.
	 */
	public klasse: UvKlasse = new UvKlasse();

	/**
	 * Die neue Schülergruppe; null verwendet die Gruppen-ID der Klasse.
	 */
	public schuelergruppe: UvSchuelergruppeCreateRequest | null = null;


	/**
	 *Erstellt eine leere Anlageanfrage.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.uv.UvKlasseAnlageRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.uv.UvKlasseAnlageRequest'].includes(name);
	}

	public static readonly class = new Class<UvKlasseAnlageRequest>('de.svws_nrw.core.data.uv.UvKlasseAnlageRequest');

	public static transpilerFromJSON(json: string): UvKlasseAnlageRequest {
		const obj = JSON.parse(json) as Partial<UvKlasseAnlageRequest>;
		const result = new UvKlasseAnlageRequest();
		if (obj.klasse === undefined)
			throw new Error('invalid json format, missing attribute klasse');
		result.klasse = UvKlasse.transpilerFromJSON(JSON.stringify(obj.klasse));
		result.schuelergruppe = ((obj.schuelergruppe === undefined) || (obj.schuelergruppe === null)) ? null : UvSchuelergruppeCreateRequest.transpilerFromJSON(JSON.stringify(obj.schuelergruppe));
		return result;
	}

	public static transpilerToJSON(obj: UvKlasseAnlageRequest): string {
		let result = '{';
		result += '"klasse" : ' + UvKlasse.transpilerToJSON(obj.klasse) + ',';
		result += '"schuelergruppe" : ' + ((obj.schuelergruppe === null) ? 'null' : UvSchuelergruppeCreateRequest.transpilerToJSON(obj.schuelergruppe)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<UvKlasseAnlageRequest>): string {
		let result = '{';
		if (obj.klasse !== undefined) {
			result += '"klasse" : ' + UvKlasse.transpilerToJSON(obj.klasse) + ',';
		}
		if (obj.schuelergruppe !== undefined) {
			result += '"schuelergruppe" : ' + ((obj.schuelergruppe === null) ? 'null' : UvSchuelergruppeCreateRequest.transpilerToJSON(obj.schuelergruppe)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_uv_UvKlasseAnlageRequest(obj: unknown): UvKlasseAnlageRequest {
	return obj as UvKlasseAnlageRequest;
}
