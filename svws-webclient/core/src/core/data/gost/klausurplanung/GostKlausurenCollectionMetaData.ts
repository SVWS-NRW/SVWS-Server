import { JavaObject } from '../../../../java/lang/JavaObject';
import { KursDaten } from '../../../../core/data/kurse/KursDaten';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { SchuelerListeEintrag } from '../../../../core/data/schueler/SchuelerListeEintrag';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { HashSet } from '../../../../java/util/HashSet';

export class GostKlausurenCollectionMetaData extends JavaObject {

	/**
	 * Die Liste der Klausurvorgaben.
	 */
	public faecher : List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Die Liste der Kursklausuren.
	 */
	public schueler : List<SchuelerListeEintrag> = new ArrayList<SchuelerListeEintrag>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public lehrer : List<LehrerListeEintrag> = new ArrayList<LehrerListeEintrag>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public kurse : List<KursDaten> = new ArrayList<KursDaten>();


	public constructor() {
		super();
	}

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param addData dd
	 */
	public addAll(addData : GostKlausurenCollectionMetaData) : void {
		this.faecher.addAll(addData.faecher);
		const schuelerMenge : JavaSet<SchuelerListeEintrag> | null = new HashSet<SchuelerListeEintrag>(this.schueler);
		schuelerMenge.addAll(addData.schueler);
		this.schueler = new ArrayList(schuelerMenge);
		this.lehrer.addAll(addData.lehrer);
		this.kurse.addAll(addData.kurse);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionMetaData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionMetaData'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionMetaData {
		const obj = JSON.parse(json) as Partial<GostKlausurenCollectionMetaData>;
		const result = new GostKlausurenCollectionMetaData();
		if (obj.faecher !== undefined) {
			for (const elem of obj.faecher) {
				result.faecher.add(GostFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(SchuelerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.lehrer !== undefined) {
			for (const elem of obj.lehrer) {
				result.lehrer.add(LehrerListeEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(KursDaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionMetaData) : string {
		let result = '{';
		result += '"faecher" : [ ';
		for (let i = 0; i < obj.faecher.size(); i++) {
			const elem = obj.faecher.get(i);
			result += GostFach.transpilerToJSON(elem);
			if (i < obj.faecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += SchuelerListeEintrag.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"lehrer" : [ ';
		for (let i = 0; i < obj.lehrer.size(); i++) {
			const elem = obj.lehrer.get(i);
			result += LehrerListeEintrag.transpilerToJSON(elem);
			if (i < obj.lehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += KursDaten.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionMetaData>) : string {
		let result = '{';
		if (obj.faecher !== undefined) {
			result += '"faecher" : [ ';
			for (let i = 0; i < obj.faecher.size(); i++) {
				const elem = obj.faecher.get(i);
				result += GostFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += SchuelerListeEintrag.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : [ ';
			for (let i = 0; i < obj.lehrer.size(); i++) {
				const elem = obj.lehrer.get(i);
				result += LehrerListeEintrag.transpilerToJSON(elem);
				if (i < obj.lehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += KursDaten.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionMetaData(obj : unknown) : GostKlausurenCollectionMetaData {
	return obj as GostKlausurenCollectionMetaData;
}
