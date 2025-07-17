import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit extends JavaObject {

	/**
	 * mögliche Fächer für das 3. Abiturfach
	 */
	public abifach3 : List<string> = new ArrayList<string>();

	/**
	 * mögliche Fächer für das 4. Abiturfach
	 */
	public abifach4 : List<string> = new ArrayList<string>();

	/**
	 * mögliche Fächer für das 5. Abiturfach
	 */
	public abifach5 : List<string> = new ArrayList<string>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit'].includes(name);
	}

	public static class = new Class<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>('de.svws_nrw.asd.data.schule.BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit');

	public static transpilerFromJSON(json : string): BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit {
		const obj = JSON.parse(json) as Partial<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>;
		const result = new BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit();
		if (obj.abifach3 !== undefined) {
			for (const elem of obj.abifach3) {
				result.abifach3.add(elem);
			}
		}
		if (obj.abifach4 !== undefined) {
			for (const elem of obj.abifach4) {
				result.abifach4.add(elem);
			}
		}
		if (obj.abifach5 !== undefined) {
			for (const elem of obj.abifach5) {
				result.abifach5.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit) : string {
		let result = '{';
		result += '"abifach3" : [ ';
		for (let i = 0; i < obj.abifach3.size(); i++) {
			const elem = obj.abifach3.get(i);
			result += '"' + elem + '"';
			if (i < obj.abifach3.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"abifach4" : [ ';
		for (let i = 0; i < obj.abifach4.size(); i++) {
			const elem = obj.abifach4.get(i);
			result += '"' + elem + '"';
			if (i < obj.abifach4.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"abifach5" : [ ';
		for (let i = 0; i < obj.abifach5.size(); i++) {
			const elem = obj.abifach5.get(i);
			result += '"' + elem + '"';
			if (i < obj.abifach5.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit>) : string {
		let result = '{';
		if (obj.abifach3 !== undefined) {
			result += '"abifach3" : [ ';
			for (let i = 0; i < obj.abifach3.size(); i++) {
				const elem = obj.abifach3.get(i);
				result += '"' + elem + '"';
				if (i < obj.abifach3.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.abifach4 !== undefined) {
			result += '"abifach4" : [ ';
			for (let i = 0; i < obj.abifach4.size(); i++) {
				const elem = obj.abifach4.get(i);
				result += '"' + elem + '"';
				if (i < obj.abifach4.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.abifach5 !== undefined) {
			result += '"abifach5" : [ ';
			for (let i = 0; i < obj.abifach5.size(); i++) {
				const elem = obj.abifach5.get(i);
				result += '"' + elem + '"';
				if (i < obj.abifach5.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_schule_BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit(obj : unknown) : BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit {
	return obj as BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit;
}
