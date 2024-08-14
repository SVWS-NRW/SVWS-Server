import { JavaObject } from '../../../java/lang/JavaObject';
import { GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import type { List } from '../../../java/util/List';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

export class GostBlockungsdaten extends JavaObject {

	/**
	 * Die ID der Blockung
	 */
	public id : number = -1;

	/**
	 * Der Name der Blockung
	 */
	public name : string = "Neue Blockung";

	/**
	 * Der Abiturjahrgang, dem die Kursblockung zugeordnet ist
	 */
	public abijahrgang : number = -1;

	/**
	 * Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2)
	 */
	public gostHalbjahr : number = GostHalbjahr.EF1.id;

	/**
	 * Gibt an, ob diese Blockung als aktiv markiert wurde.
	 */
	public istAktiv : boolean = false;

	/**
	 * Die Definition der Schienen
	 */
	public schienen : List<GostBlockungSchiene> = new ArrayList<GostBlockungSchiene>();

	/**
	 * Die Definition der Regeln
	 */
	public regeln : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>();

	/**
	 * Die für die Blockung angelegten Kurse
	 */
	public kurse : List<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();

	/**
	 * Die Schüler für die Blockung.
	 */
	public schueler : List<Schueler> = new ArrayList<Schueler>();

	/**
	 * Die Fachwahlen für die Blockung
	 */
	public fachwahlen : List<GostFachwahl> = new ArrayList<GostFachwahl>();

	/**
	 * Eine Liste der Ergebnisse, die der Blockungsdefinition zugeordnet sind.
	 */
	public readonly ergebnisse : List<GostBlockungsergebnis> = new ArrayList<GostBlockungsergebnis>();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostBlockungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostBlockungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsdaten {
		const obj = JSON.parse(json) as Partial<GostBlockungsdaten>;
		const result = new GostBlockungsdaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.name === undefined)
			throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (obj.abijahrgang === undefined)
			throw new Error('invalid json format, missing attribute abijahrgang');
		result.abijahrgang = obj.abijahrgang;
		if (obj.gostHalbjahr === undefined)
			throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (obj.istAktiv === undefined)
			throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (obj.schienen !== undefined) {
			for (const elem of obj.schienen) {
				result.schienen.add(GostBlockungSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.regeln !== undefined) {
			for (const elem of obj.regeln) {
				result.regeln.add(GostBlockungRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.kurse !== undefined) {
			for (const elem of obj.kurse) {
				result.kurse.add(GostBlockungKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.schueler !== undefined) {
			for (const elem of obj.schueler) {
				result.schueler.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.fachwahlen !== undefined) {
			for (const elem of obj.fachwahlen) {
				result.fachwahlen.add(GostFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.ergebnisse !== undefined) {
			for (const elem of obj.ergebnisse) {
				result.ergebnisse.add(GostBlockungsergebnis.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"name" : ' + JSON.stringify(obj.name) + ',';
		result += '"abijahrgang" : ' + obj.abijahrgang.toString() + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr.toString() + ',';
		result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		result += '"schienen" : [ ';
		for (let i = 0; i < obj.schienen.size(); i++) {
			const elem = obj.schienen.get(i);
			result += GostBlockungSchiene.transpilerToJSON(elem);
			if (i < obj.schienen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"regeln" : [ ';
		for (let i = 0; i < obj.regeln.size(); i++) {
			const elem = obj.regeln.get(i);
			result += GostBlockungRegel.transpilerToJSON(elem);
			if (i < obj.regeln.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"kurse" : [ ';
		for (let i = 0; i < obj.kurse.size(); i++) {
			const elem = obj.kurse.get(i);
			result += GostBlockungKurs.transpilerToJSON(elem);
			if (i < obj.kurse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schueler" : [ ';
		for (let i = 0; i < obj.schueler.size(); i++) {
			const elem = obj.schueler.get(i);
			result += Schueler.transpilerToJSON(elem);
			if (i < obj.schueler.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachwahlen" : [ ';
		for (let i = 0; i < obj.fachwahlen.size(); i++) {
			const elem = obj.fachwahlen.get(i);
			result += GostFachwahl.transpilerToJSON(elem);
			if (i < obj.fachwahlen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"ergebnisse" : [ ';
		for (let i = 0; i < obj.ergebnisse.size(); i++) {
			const elem = obj.ergebnisse.get(i);
			result += GostBlockungsergebnis.transpilerToJSON(elem);
			if (i < obj.ergebnisse.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsdaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.name !== undefined) {
			result += '"name" : ' + JSON.stringify(obj.name) + ',';
		}
		if (obj.abijahrgang !== undefined) {
			result += '"abijahrgang" : ' + obj.abijahrgang.toString() + ',';
		}
		if (obj.gostHalbjahr !== undefined) {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr.toString() + ',';
		}
		if (obj.istAktiv !== undefined) {
			result += '"istAktiv" : ' + obj.istAktiv.toString() + ',';
		}
		if (obj.schienen !== undefined) {
			result += '"schienen" : [ ';
			for (let i = 0; i < obj.schienen.size(); i++) {
				const elem = obj.schienen.get(i);
				result += GostBlockungSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.regeln !== undefined) {
			result += '"regeln" : [ ';
			for (let i = 0; i < obj.regeln.size(); i++) {
				const elem = obj.regeln.get(i);
				result += GostBlockungRegel.transpilerToJSON(elem);
				if (i < obj.regeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.kurse !== undefined) {
			result += '"kurse" : [ ';
			for (let i = 0; i < obj.kurse.size(); i++) {
				const elem = obj.kurse.get(i);
				result += GostBlockungKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schueler !== undefined) {
			result += '"schueler" : [ ';
			for (let i = 0; i < obj.schueler.size(); i++) {
				const elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachwahlen !== undefined) {
			result += '"fachwahlen" : [ ';
			for (let i = 0; i < obj.fachwahlen.size(); i++) {
				const elem = obj.fachwahlen.get(i);
				result += GostFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.ergebnisse !== undefined) {
			result += '"ergebnisse" : [ ';
			for (let i = 0; i < obj.ergebnisse.size(); i++) {
				const elem = obj.ergebnisse.get(i);
				result += GostBlockungsergebnis.transpilerToJSON(elem);
				if (i < obj.ergebnisse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostBlockungsdaten(obj : unknown) : GostBlockungsdaten {
	return obj as GostBlockungsdaten;
}
