import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuleStammdaten } from '../../../asd/data/schule/SchuleStammdaten';
import { Class } from '../../../java/lang/Class';

export class SchuleStatistikdatenGesamt extends JavaObject {

	/**
	 * Die Stammdaten der Schule
	 */
	public stammdaten : SchuleStammdaten = new SchuleStammdaten();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt'].includes(name);
	}

	public static class = new Class<SchuleStatistikdatenGesamt>('de.svws_nrw.asd.data.schule.SchuleStatistikdatenGesamt');

	public static transpilerFromJSON(json : string): SchuleStatistikdatenGesamt {
		const obj = JSON.parse(json) as Partial<SchuleStatistikdatenGesamt>;
		const result = new SchuleStatistikdatenGesamt();
		if (obj.stammdaten === undefined)
			throw new Error('invalid json format, missing attribute stammdaten');
		result.stammdaten = SchuleStammdaten.transpilerFromJSON(JSON.stringify(obj.stammdaten));
		return result;
	}

	public static transpilerToJSON(obj : SchuleStatistikdatenGesamt) : string {
		let result = '{';
		result += '"stammdaten" : ' + SchuleStammdaten.transpilerToJSON(obj.stammdaten) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuleStatistikdatenGesamt>) : string {
		let result = '{';
		if (obj.stammdaten !== undefined) {
			result += '"stammdaten" : ' + SchuleStammdaten.transpilerToJSON(obj.stammdaten) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_SchuleStatistikdatenGesamt(obj : unknown) : SchuleStatistikdatenGesamt {
	return obj as SchuleStatistikdatenGesamt;
}
