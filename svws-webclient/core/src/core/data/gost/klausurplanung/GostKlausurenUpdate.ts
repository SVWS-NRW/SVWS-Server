import { JavaObject } from '../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';

export class GostKlausurenUpdate extends JavaObject {

	/**
	 * Die zu entfernenden Regeln
	 */
	public listSchuelerklausurTermineRemoveIdTermin : List<number> = new ArrayList();

	/**
	 * Die hinzuzufügenden Regeln
	 */
	public listKlausurtermineNachschreiberZugelassenFalse : List<number> = new ArrayList();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenUpdate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenUpdate'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenUpdate {
		const obj = JSON.parse(json);
		const result = new GostKlausurenUpdate();
		if ((obj.listSchuelerklausurTermineRemoveIdTermin !== undefined) && (obj.listSchuelerklausurTermineRemoveIdTermin !== null)) {
			for (const elem of obj.listSchuelerklausurTermineRemoveIdTermin) {
				result.listSchuelerklausurTermineRemoveIdTermin?.add(elem);
			}
		}
		if ((obj.listKlausurtermineNachschreiberZugelassenFalse !== undefined) && (obj.listKlausurtermineNachschreiberZugelassenFalse !== null)) {
			for (const elem of obj.listKlausurtermineNachschreiberZugelassenFalse) {
				result.listKlausurtermineNachschreiberZugelassenFalse?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenUpdate) : string {
		let result = '{';
		if (!obj.listSchuelerklausurTermineRemoveIdTermin) {
			result += '"listSchuelerklausurTermineRemoveIdTermin" : []';
		} else {
			result += '"listSchuelerklausurTermineRemoveIdTermin" : [ ';
			for (let i = 0; i < obj.listSchuelerklausurTermineRemoveIdTermin.size(); i++) {
				const elem = obj.listSchuelerklausurTermineRemoveIdTermin.get(i);
				result += elem;
				if (i < obj.listSchuelerklausurTermineRemoveIdTermin.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.listKlausurtermineNachschreiberZugelassenFalse) {
			result += '"listKlausurtermineNachschreiberZugelassenFalse" : []';
		} else {
			result += '"listKlausurtermineNachschreiberZugelassenFalse" : [ ';
			for (let i = 0; i < obj.listKlausurtermineNachschreiberZugelassenFalse.size(); i++) {
				const elem = obj.listKlausurtermineNachschreiberZugelassenFalse.get(i);
				result += elem;
				if (i < obj.listKlausurtermineNachschreiberZugelassenFalse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenUpdate>) : string {
		let result = '{';
		if (typeof obj.listSchuelerklausurTermineRemoveIdTermin !== "undefined") {
			if (!obj.listSchuelerklausurTermineRemoveIdTermin) {
				result += '"listSchuelerklausurTermineRemoveIdTermin" : []';
			} else {
				result += '"listSchuelerklausurTermineRemoveIdTermin" : [ ';
				for (let i = 0; i < obj.listSchuelerklausurTermineRemoveIdTermin.size(); i++) {
					const elem = obj.listSchuelerklausurTermineRemoveIdTermin.get(i);
					result += elem;
					if (i < obj.listSchuelerklausurTermineRemoveIdTermin.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.listKlausurtermineNachschreiberZugelassenFalse !== "undefined") {
			if (!obj.listKlausurtermineNachschreiberZugelassenFalse) {
				result += '"listKlausurtermineNachschreiberZugelassenFalse" : []';
			} else {
				result += '"listKlausurtermineNachschreiberZugelassenFalse" : [ ';
				for (let i = 0; i < obj.listKlausurtermineNachschreiberZugelassenFalse.size(); i++) {
					const elem = obj.listKlausurtermineNachschreiberZugelassenFalse.get(i);
					result += elem;
					if (i < obj.listKlausurtermineNachschreiberZugelassenFalse.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenUpdate(obj : unknown) : GostKlausurenUpdate {
	return obj as GostKlausurenUpdate;
}
