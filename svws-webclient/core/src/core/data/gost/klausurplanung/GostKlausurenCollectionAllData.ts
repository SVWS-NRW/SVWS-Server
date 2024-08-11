import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurenCollectionRaumData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionRaumData';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostKlausurenCollectionMetaData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionMetaData';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';

export class GostKlausurenCollectionAllData extends JavaObject {

	/**
	 * Die Liste der Klausurvorgaben.
	 */
	public vorgaben : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();

	/**
	 * Die Liste der Kursklausuren.
	 */
	public kursklausuren : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public schuelerklausuren : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public termine : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public metadata : GostKlausurenCollectionMetaData = new GostKlausurenCollectionMetaData();

	/**
	 * Die Liste der Schülerklausuren.
	 */
	public raumdata : GostKlausurenCollectionRaumData = new GostKlausurenCollectionRaumData();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKlausurenCollectionAllData {
		const obj = JSON.parse(json) as Partial<GostKlausurenCollectionAllData>;
		const result = new GostKlausurenCollectionAllData();
		if (obj.vorgaben !== undefined) {
			for (const elem of obj.vorgaben) {
				result.vorgaben.add(GostKlausurvorgabe.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kursklausuren !== undefined) {
			for (const elem of obj.kursklausuren) {
				result.kursklausuren.add(GostKursklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerklausuren !== undefined) {
			for (const elem of obj.schuelerklausuren) {
				result.schuelerklausuren.add(GostSchuelerklausur.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schuelerklausurtermine !== undefined) {
			for (const elem of obj.schuelerklausurtermine) {
				result.schuelerklausurtermine.add(GostSchuelerklausurTermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.termine !== undefined) {
			for (const elem of obj.termine) {
				result.termine.add(GostKlausurtermin.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.metadata === undefined)
			throw new Error('invalid json format, missing attribute metadata');
		result.metadata = GostKlausurenCollectionMetaData.transpilerFromJSON(JSON.stringify(obj.metadata));
		if (obj.raumdata === undefined)
			throw new Error('invalid json format, missing attribute raumdata');
		result.raumdata = GostKlausurenCollectionRaumData.transpilerFromJSON(JSON.stringify(obj.raumdata));
		return result;
	}

	public static transpilerToJSON(obj : GostKlausurenCollectionAllData) : string {
		let result = '{';
		result += '"vorgaben" : [ ';
		for (let i = 0; i < obj.vorgaben.size(); i++) {
			const elem = obj.vorgaben.get(i);
			result += GostKlausurvorgabe.transpilerToJSON(elem);
			if (i < obj.vorgaben.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kursklausuren" : [ ';
		for (let i = 0; i < obj.kursklausuren.size(); i++) {
			const elem = obj.kursklausuren.get(i);
			result += GostKursklausur.transpilerToJSON(elem);
			if (i < obj.kursklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerklausuren" : [ ';
		for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
			const elem = obj.schuelerklausuren.get(i);
			result += GostSchuelerklausur.transpilerToJSON(elem);
			if (i < obj.schuelerklausuren.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schuelerklausurtermine" : [ ';
		for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
			const elem = obj.schuelerklausurtermine.get(i);
			result += GostSchuelerklausurTermin.transpilerToJSON(elem);
			if (i < obj.schuelerklausurtermine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"termine" : [ ';
		for (let i = 0; i < obj.termine.size(); i++) {
			const elem = obj.termine.get(i);
			result += GostKlausurtermin.transpilerToJSON(elem);
			if (i < obj.termine.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"metadata" : ' + GostKlausurenCollectionMetaData.transpilerToJSON(obj.metadata) + ',';
		result += '"raumdata" : ' + GostKlausurenCollectionRaumData.transpilerToJSON(obj.raumdata) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKlausurenCollectionAllData>) : string {
		let result = '{';
		if (obj.vorgaben !== undefined) {
			result += '"vorgaben" : [ ';
			for (let i = 0; i < obj.vorgaben.size(); i++) {
				const elem = obj.vorgaben.get(i);
				result += GostKlausurvorgabe.transpilerToJSON(elem);
				if (i < obj.vorgaben.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kursklausuren !== undefined) {
			result += '"kursklausuren" : [ ';
			for (let i = 0; i < obj.kursklausuren.size(); i++) {
				const elem = obj.kursklausuren.get(i);
				result += GostKursklausur.transpilerToJSON(elem);
				if (i < obj.kursklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerklausuren !== undefined) {
			result += '"schuelerklausuren" : [ ';
			for (let i = 0; i < obj.schuelerklausuren.size(); i++) {
				const elem = obj.schuelerklausuren.get(i);
				result += GostSchuelerklausur.transpilerToJSON(elem);
				if (i < obj.schuelerklausuren.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schuelerklausurtermine !== undefined) {
			result += '"schuelerklausurtermine" : [ ';
			for (let i = 0; i < obj.schuelerklausurtermine.size(); i++) {
				const elem = obj.schuelerklausurtermine.get(i);
				result += GostSchuelerklausurTermin.transpilerToJSON(elem);
				if (i < obj.schuelerklausurtermine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.termine !== undefined) {
			result += '"termine" : [ ';
			for (let i = 0; i < obj.termine.size(); i++) {
				const elem = obj.termine.get(i);
				result += GostKlausurtermin.transpilerToJSON(elem);
				if (i < obj.termine.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.metadata !== undefined) {
			result += '"metadata" : ' + GostKlausurenCollectionMetaData.transpilerToJSON(obj.metadata) + ',';
		}
		if (obj.raumdata !== undefined) {
			result += '"raumdata" : ' + GostKlausurenCollectionRaumData.transpilerToJSON(obj.raumdata) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKlausurenCollectionAllData(obj : unknown) : GostKlausurenCollectionAllData {
	return obj as GostKlausurenCollectionAllData;
}
