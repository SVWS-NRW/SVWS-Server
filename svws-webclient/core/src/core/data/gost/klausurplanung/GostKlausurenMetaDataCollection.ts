import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { KursListeEintrag } from '../../../../core/data/kurse/KursListeEintrag';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostKlausurenDataCollection } from '../../../../core/data/gost/klausurplanung/GostKlausurenDataCollection';

export class GostKlausurenMetaDataCollection extends JavaObject {

	/**
	 * Die Liste der Klausurvorgaben.
	 */
	public faecher : List<GostFach> = new ArrayList();

	/**
	 * Die Liste der Kursklausuren.
	 */
	public schueler : List<SchuelerListeEintrag> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public lehrer : List<LehrerListeEintrag> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public kurse : List<KursListeEintrag> = new ArrayList();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public klausurdata : GostKlausurenDataCollection = new GostKlausurenDataCollection();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenMetaDataCollection';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenMetaDataCollection'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenMetaDataCollection {
		const obj = JSON.parse(json);
		const result = new GostKlausurenMetaDataCollection();
		if ((obj.faecher !== undefined) && (obj.faecher !== null)) {
			for (const elem of obj.faecher) {
				result.faecher?.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.schueler !== undefined) && (obj.schueler !== null)) {
			for (const elem of obj.schueler) {
				result.schueler?.add(SchuelerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.lehrer !== undefined) && (obj.lehrer !== null)) {
			for (const elem of obj.lehrer) {
				result.lehrer?.add(LehrerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.kurse !== undefined) && (obj.kurse !== null)) {
			for (const elem of obj.kurse) {
				result.kurse?.add(KursListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.klausurdata === "undefined")
			 throw new Error('invalid json format, missing attribute klausurdata');
		result.klausurdata = GostKlausurenDataCollection.transpilerFromJSON(JSON.stringify(obj.klausurdata));
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenMetaDataCollection) : string {
		let result = '{';
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += SchuelerListeEintrag.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.lehrer) {
			result += '"lehrer" : []';
		} else {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LehrerListeEintrag.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += KursListeEintrag.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"klausurdata" : ' + GostKlausurenDataCollection.transpilerToJSON(obj.klausurdata) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenMetaDataCollection>) : string {
		let result = '{';
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i = 0; i < obj.faecher.size(); i++) {
					const elem = obj.faecher.get(i);
					result += GostFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.schueler !== "undefined") {
			if (!obj.schueler) {
				result += '"schueler" : []';
			} else {
				result += '"schueler" : [ ';
				for (let i = 0; i < obj.schueler.size(); i++) {
					const elem = obj.schueler.get(i);
					result += SchuelerListeEintrag.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lehrer !== "undefined") {
			if (!obj.lehrer) {
				result += '"lehrer" : []';
			} else {
				result += '"lehrer" : [ ';
				for (let i = 0; i < obj.lehrer.size(); i++) {
					const elem = obj.lehrer.get(i);
					result += LehrerListeEintrag.transpilerToJSON(elem);
					if (i < obj.lehrer.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kurse !== "undefined") {
			if (!obj.kurse) {
				result += '"kurse" : []';
			} else {
				result += '"kurse" : [ ';
				for (let i = 0; i < obj.kurse.size(); i++) {
					const elem = obj.kurse.get(i);
					result += KursListeEintrag.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.klausurdata !== "undefined") {
			result += '"klausurdata" : ' + GostKlausurenDataCollection.transpilerToJSON(obj.klausurdata) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenMetaDataCollection(obj : unknown) : GostKlausurenMetaDataCollection {
	return obj as GostKlausurenMetaDataCollection;
}
