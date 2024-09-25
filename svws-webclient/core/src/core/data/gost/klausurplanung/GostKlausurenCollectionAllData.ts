import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostKlausurenCollectionRaumData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionRaumData';
import { GostKursklausur } from '../../../../core/data/gost/klausurplanung/GostKursklausur';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { GostKlausurvorgabe } from '../../../../core/data/gost/klausurplanung/GostKlausurvorgabe';
import { GostKlausurenCollectionMetaData } from '../../../../core/data/gost/klausurplanung/GostKlausurenCollectionMetaData';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostSchuelerklausur } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausur';
import { GostSchuelerklausurTermin } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTermin';
import { GostKlausurtermin } from '../../../../core/data/gost/klausurplanung/GostKlausurtermin';
import { HashSet } from '../../../../java/util/HashSet';

export class GostKlausurenCollectionAllData extends JavaObject {

	/**
	 * Ein Array mit den Klausurvorgaben.
	 */
	public vorgaben : List<GostKlausurvorgabe> = new ArrayList<GostKlausurvorgabe>();

	/**
	 * Ein Array mit den Kursklausuren.
	 */
	public kursklausuren : List<GostKursklausur> = new ArrayList<GostKursklausur>();

	/**
	 * Ein Array mit den Schülerklausuren.
	 */
	public schuelerklausuren : List<GostSchuelerklausur> = new ArrayList<GostSchuelerklausur>();

	/**
	 * Ein Array mit den Schülerklausurterminen.
	 */
	public schuelerklausurtermine : List<GostSchuelerklausurTermin> = new ArrayList<GostSchuelerklausurTermin>();

	/**
	 * Ein Array mit den Klausurterminen.
	 */
	public termine : List<GostKlausurtermin> = new ArrayList<GostKlausurtermin>();

	/**
	 * Die zu den Klausurdaten gehörenden Meta-Informationen wie Fachdaten, Kursdaten, Lehrerdaten, Schülerdaten.
	 */
	public metadata : GostKlausurenCollectionMetaData = new GostKlausurenCollectionMetaData();

	/**
	 * Die zu den Klausurdaten gehörenden Raumdaten.
	 */
	public raumdata : GostKlausurenCollectionRaumData = new GostKlausurenCollectionRaumData();


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Fügt den Datensammlungen in dieser Klasse die im übergebenen Objekt enthaltenen Daten hinzu.
	 *
	 * @param addData die Daten, die hinzugefügt werden sollen
	 */
	public addAll(addData : GostKlausurenCollectionAllData) : void {
		this.vorgaben.addAll(addData.vorgaben);
		this.kursklausuren.addAll(addData.kursklausuren);
		this.schuelerklausuren.addAll(addData.schuelerklausuren);
		this.schuelerklausurtermine.addAll(addData.schuelerklausurtermine);
		const terminMenge : JavaSet<GostKlausurtermin> | null = new HashSet<GostKlausurtermin>(this.termine);
		terminMenge.addAll(addData.termine);
		this.termine = new ArrayList(terminMenge);
		this.metadata.addAll(addData.metadata);
		this.raumdata.addAll(addData.raumdata);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData'].includes(name);
	}

	public static class = new Class<GostKlausurenCollectionAllData>('de.svws_nrw.core.data.gost.klausurplanung.GostKlausurenCollectionAllData');

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
