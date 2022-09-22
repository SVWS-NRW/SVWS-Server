import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { KursblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKurs } from '../../../core/data/kursblockung/KursblockungInputKurs';
import { KursblockungInputSchueler, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputSchueler } from '../../../core/data/kursblockung/KursblockungInputSchueler';
import { KursblockungInputRegel, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputRegel } from '../../../core/data/kursblockung/KursblockungInputRegel';
import { KursblockungInputKursart, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputKursart } from '../../../core/data/kursblockung/KursblockungInputKursart';
import { KursblockungInputFach, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputFach } from '../../../core/data/kursblockung/KursblockungInputFach';
import { KursblockungInputFachwahl, cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInputFachwahl } from '../../../core/data/kursblockung/KursblockungInputFachwahl';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';

export class KursblockungInput extends JavaObject {

	public seed : number = 0;

	public input : number = 0;

	public maxTimeMillis : number = 0;

	public maxSchienen : number = 0;

	public regeln : Vector<KursblockungInputRegel> = new Vector();

	public faecher : Vector<KursblockungInputFach> = new Vector();

	public kursarten : Vector<KursblockungInputKursart> = new Vector();

	public kurse : Vector<KursblockungInputKurs> = new Vector();

	public schueler : Vector<KursblockungInputSchueler> = new Vector();

	public fachwahlen : Vector<KursblockungInputFachwahl> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.kursblockung.KursblockungInput'].includes(name);
	}

	public static transpilerFromJSON(json : string): KursblockungInput {
		const obj = JSON.parse(json);
		const result = new KursblockungInput();
		if (typeof obj.seed === "undefined")
			 throw new Error('invalid json format, missing attribute seed');
		result.seed = obj.seed;
		if (typeof obj.input === "undefined")
			 throw new Error('invalid json format, missing attribute input');
		result.input = obj.input;
		if (typeof obj.maxTimeMillis === "undefined")
			 throw new Error('invalid json format, missing attribute maxTimeMillis');
		result.maxTimeMillis = obj.maxTimeMillis;
		if (typeof obj.maxSchienen === "undefined")
			 throw new Error('invalid json format, missing attribute maxSchienen');
		result.maxSchienen = obj.maxSchienen;
		if (!!obj.regeln) {
			for (let elem of obj.regeln) {
				result.regeln?.add(KursblockungInputRegel.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.faecher) {
			for (let elem of obj.faecher) {
				result.faecher?.add(KursblockungInputFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kursarten) {
			for (let elem of obj.kursarten) {
				result.kursarten?.add(KursblockungInputKursart.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(KursblockungInputKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.schueler) {
			for (let elem of obj.schueler) {
				result.schueler?.add(KursblockungInputSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.fachwahlen) {
			for (let elem of obj.fachwahlen) {
				result.fachwahlen?.add(KursblockungInputFachwahl.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : KursblockungInput) : string {
		let result = '{';
		result += '"seed" : ' + obj.seed + ',';
		result += '"input" : ' + obj.input + ',';
		result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		result += '"maxSchienen" : ' + obj.maxSchienen + ',';
		if (!obj.regeln) {
			result += '"regeln" : []';
		} else {
			result += '"regeln" : [ ';
			for (let i : number = 0; i < obj.regeln.size(); i++) {
				let elem = obj.regeln.get(i);
				result += KursblockungInputRegel.transpilerToJSON(elem);
				if (i < obj.regeln.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.faecher) {
			result += '"faecher" : []';
		} else {
			result += '"faecher" : [ ';
			for (let i : number = 0; i < obj.faecher.size(); i++) {
				let elem = obj.faecher.get(i);
				result += KursblockungInputFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kursarten) {
			result += '"kursarten" : []';
		} else {
			result += '"kursarten" : [ ';
			for (let i : number = 0; i < obj.kursarten.size(); i++) {
				let elem = obj.kursarten.get(i);
				result += KursblockungInputKursart.transpilerToJSON(elem);
				if (i < obj.kursarten.size() - 1)
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
				result += KursblockungInputKurs.transpilerToJSON(elem);
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
				result += KursblockungInputSchueler.transpilerToJSON(elem);
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
				result += KursblockungInputFachwahl.transpilerToJSON(elem);
				if (i < obj.fachwahlen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<KursblockungInput>) : string {
		let result = '{';
		if (typeof obj.seed !== "undefined") {
			result += '"seed" : ' + obj.seed + ',';
		}
		if (typeof obj.input !== "undefined") {
			result += '"input" : ' + obj.input + ',';
		}
		if (typeof obj.maxTimeMillis !== "undefined") {
			result += '"maxTimeMillis" : ' + obj.maxTimeMillis + ',';
		}
		if (typeof obj.maxSchienen !== "undefined") {
			result += '"maxSchienen" : ' + obj.maxSchienen + ',';
		}
		if (typeof obj.regeln !== "undefined") {
			if (!obj.regeln) {
				result += '"regeln" : []';
			} else {
				result += '"regeln" : [ ';
				for (let i : number = 0; i < obj.regeln.size(); i++) {
					let elem = obj.regeln.get(i);
					result += KursblockungInputRegel.transpilerToJSON(elem);
					if (i < obj.regeln.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.faecher !== "undefined") {
			if (!obj.faecher) {
				result += '"faecher" : []';
			} else {
				result += '"faecher" : [ ';
				for (let i : number = 0; i < obj.faecher.size(); i++) {
					let elem = obj.faecher.get(i);
					result += KursblockungInputFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kursarten !== "undefined") {
			if (!obj.kursarten) {
				result += '"kursarten" : []';
			} else {
				result += '"kursarten" : [ ';
				for (let i : number = 0; i < obj.kursarten.size(); i++) {
					let elem = obj.kursarten.get(i);
					result += KursblockungInputKursart.transpilerToJSON(elem);
					if (i < obj.kursarten.size() - 1)
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
					result += KursblockungInputKurs.transpilerToJSON(elem);
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
					result += KursblockungInputSchueler.transpilerToJSON(elem);
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
					result += KursblockungInputFachwahl.transpilerToJSON(elem);
					if (i < obj.fachwahlen.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_kursblockung_KursblockungInput(obj : unknown) : KursblockungInput {
	return obj as KursblockungInput;
}
