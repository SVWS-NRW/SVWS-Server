import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Schueler, cast_de_nrw_schule_svws_core_data_schueler_Schueler } from '../../../core/data/schueler/Schueler';
import { GostBlockungsergebnisListeneintrag, cast_de_nrw_schule_svws_core_data_gost_GostBlockungsergebnisListeneintrag } from '../../../core/data/gost/GostBlockungsergebnisListeneintrag';
import { GostBlockungSchiene, cast_de_nrw_schule_svws_core_data_gost_GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostBlockungRegel, cast_de_nrw_schule_svws_core_data_gost_GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { GostBlockungKurs, cast_de_nrw_schule_svws_core_data_gost_GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';

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
	 * Gibt an, ob diese Blockung aktiviert wurde, d.h. bereits in die Leistungsdaten übertragen wurde.
	 */
	public istAktiv : boolean = false;

	/**
	 * Die Definition der Schienen
	 */
	public schienen : List<GostBlockungSchiene> = new Vector();

	/**
	 * Die Definition der Regeln
	 */
	public regeln : List<GostBlockungRegel> = new Vector();

	/**
	 * Die für die Blockung angelegten Kurse
	 */
	public kurse : List<GostBlockungKurs> = new Vector();

	/**
	 * Die SchülerInnen für die Blockung.
	 */
	public schueler : List<Schueler> = new Vector();

	/**
	 * Die Fachwahlen für die Blockung
	 */
	public fachwahlen : List<GostFachwahl> = new Vector();

	/**
	 * Eine Liste der Ergebnisse, die der Blockungsdefinition zugeordnet sind.
	 */
	public readonly ergebnisse : List<GostBlockungsergebnisListeneintrag> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.GostBlockungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostBlockungsdaten {
		const obj = JSON.parse(json);
		const result = new GostBlockungsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.name === "undefined")
			 throw new Error('invalid json format, missing attribute name');
		result.name = obj.name;
		if (typeof obj.abijahrgang === "undefined")
			 throw new Error('invalid json format, missing attribute abijahrgang');
		result.abijahrgang = obj.abijahrgang;
		if (typeof obj.gostHalbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute gostHalbjahr');
		result.gostHalbjahr = obj.gostHalbjahr;
		if (typeof obj.istAktiv === "undefined")
			 throw new Error('invalid json format, missing attribute istAktiv');
		result.istAktiv = obj.istAktiv;
		if (!!obj.schienen) {
			for (let elem of obj.schienen) {
				result.schienen?.add(GostBlockungSchiene.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.regeln) {
			for (let elem of obj.regeln) {
				result.regeln?.add(GostBlockungRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(GostBlockungKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.schueler) {
			for (let elem of obj.schueler) {
				result.schueler?.add(Schueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.fachwahlen) {
			for (let elem of obj.fachwahlen) {
				result.fachwahlen?.add(GostFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.ergebnisse) {
			for (let elem of obj.ergebnisse) {
				result.ergebnisse?.add(GostBlockungsergebnisListeneintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostBlockungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"name" : ' + '"' + obj.name! + '"' + ',';
		result += '"abijahrgang" : ' + obj.abijahrgang + ',';
		result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		result += '"istAktiv" : ' + obj.istAktiv + ',';
		if (!obj.schienen) {
			result += '"schienen" : []';
		} else {
			result += '"schienen" : [ ';
			for (let i : number = 0; i < obj.schienen.size(); i++) {
				let elem = obj.schienen.get(i);
				result += GostBlockungSchiene.transpilerToJSON(elem);
				if (i < obj.schienen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.regeln) {
			result += '"regeln" : []';
		} else {
			result += '"regeln" : [ ';
			for (let i : number = 0; i < obj.regeln.size(); i++) {
				let elem = obj.regeln.get(i);
				result += GostBlockungRegel.transpilerToJSON(elem);
				if (i < obj.regeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kurse) {
			result += '"kurse" : []';
		} else {
			result += '"kurse" : [ ';
			for (let i : number = 0; i < obj.kurse.size(); i++) {
				let elem = obj.kurse.get(i);
				result += GostBlockungKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.schueler) {
			result += '"schueler" : []';
		} else {
			result += '"schueler" : [ ';
			for (let i : number = 0; i < obj.schueler.size(); i++) {
				let elem = obj.schueler.get(i);
				result += Schueler.transpilerToJSON(elem);
				if (i < obj.schueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachwahlen) {
			result += '"fachwahlen" : []';
		} else {
			result += '"fachwahlen" : [ ';
			for (let i : number = 0; i < obj.fachwahlen.size(); i++) {
				let elem = obj.fachwahlen.get(i);
				result += GostFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.ergebnisse) {
			result += '"ergebnisse" : []';
		} else {
			result += '"ergebnisse" : [ ';
			for (let i : number = 0; i < obj.ergebnisse.size(); i++) {
				let elem = obj.ergebnisse.get(i);
				result += GostBlockungsergebnisListeneintrag.transpilerToJSON(elem);
				if (i < obj.ergebnisse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostBlockungsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.name !== "undefined") {
			result += '"name" : ' + '"' + obj.name + '"' + ',';
		}
		if (typeof obj.abijahrgang !== "undefined") {
			result += '"abijahrgang" : ' + obj.abijahrgang + ',';
		}
		if (typeof obj.gostHalbjahr !== "undefined") {
			result += '"gostHalbjahr" : ' + obj.gostHalbjahr + ',';
		}
		if (typeof obj.istAktiv !== "undefined") {
			result += '"istAktiv" : ' + obj.istAktiv + ',';
		}
		if (typeof obj.schienen !== "undefined") {
			if (!obj.schienen) {
				result += '"schienen" : []';
			} else {
				result += '"schienen" : [ ';
				for (let i : number = 0; i < obj.schienen.size(); i++) {
					let elem = obj.schienen.get(i);
					result += GostBlockungSchiene.transpilerToJSON(elem);
					if (i < obj.schienen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.regeln !== "undefined") {
			if (!obj.regeln) {
				result += '"regeln" : []';
			} else {
				result += '"regeln" : [ ';
				for (let i : number = 0; i < obj.regeln.size(); i++) {
					let elem = obj.regeln.get(i);
					result += GostBlockungRegel.transpilerToJSON(elem);
					if (i < obj.regeln.size() - 1)
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
				for (let i : number = 0; i < obj.kurse.size(); i++) {
					let elem = obj.kurse.get(i);
					result += GostBlockungKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
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
				for (let i : number = 0; i < obj.schueler.size(); i++) {
					let elem = obj.schueler.get(i);
					result += Schueler.transpilerToJSON(elem);
					if (i < obj.schueler.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachwahlen !== "undefined") {
			if (!obj.fachwahlen) {
				result += '"fachwahlen" : []';
			} else {
				result += '"fachwahlen" : [ ';
				for (let i : number = 0; i < obj.fachwahlen.size(); i++) {
					let elem = obj.fachwahlen.get(i);
					result += GostFachwahl.transpilerToJSON(elem);
					if (i < obj.fachwahlen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.ergebnisse !== "undefined") {
			if (!obj.ergebnisse) {
				result += '"ergebnisse" : []';
			} else {
				result += '"ergebnisse" : [ ';
				for (let i : number = 0; i < obj.ergebnisse.size(); i++) {
					let elem = obj.ergebnisse.get(i);
					result += GostBlockungsergebnisListeneintrag.transpilerToJSON(elem);
					if (i < obj.ergebnisse.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_GostBlockungsdaten(obj : unknown) : GostBlockungsdaten {
	return obj as GostBlockungsdaten;
}
