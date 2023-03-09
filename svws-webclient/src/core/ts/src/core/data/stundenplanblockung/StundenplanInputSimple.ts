import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { StundenplanInputSimpleKurs, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleKurs } from './StundenplanInputSimpleKurs';
import { StundenplanInputSimpleLehrkraft, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleLehrkraft } from './StundenplanInputSimpleLehrkraft';
import { StundenplanInputSimpleFach, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleFach } from './StundenplanInputSimpleFach';
import { StundenplanInputSimpleRaum, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleRaum } from './StundenplanInputSimpleRaum';
import { StundenplanInputSimpleKopplung, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleKopplung } from './StundenplanInputSimpleKopplung';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { StundenplanInputSimpleKlasse, cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimpleKlasse } from './StundenplanInputSimpleKlasse';

export class StundenplanInputSimple extends JavaObject {

	/**
	 * Alle Lehrkräfte. 
	 */
	public lehrkraefte : Vector<StundenplanInputSimpleLehrkraft> = new Vector();

	/**
	 * Alle Klassen. 
	 */
	public klassen : Vector<StundenplanInputSimpleKlasse> = new Vector();

	/**
	 * Alle Fächer. 
	 */
	public faecher : Vector<StundenplanInputSimpleFach> = new Vector();

	/**
	 * Alle Räume. 
	 */
	public raeume : Vector<StundenplanInputSimpleRaum> = new Vector();

	/**
	 * Alle Kopplungen. 
	 */
	public kopplungen : Vector<StundenplanInputSimpleKopplung> = new Vector();

	/**
	 * Alle Kurse. 
	 */
	public kurse : Vector<StundenplanInputSimpleKurs> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanInputSimple'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanInputSimple {
		const obj = JSON.parse(json);
		const result = new StundenplanInputSimple();
		if (!!obj.lehrkraefte) {
			for (let elem of obj.lehrkraefte) {
				result.lehrkraefte?.add(StundenplanInputSimpleLehrkraft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.klassen) {
			for (let elem of obj.klassen) {
				result.klassen?.add(StundenplanInputSimpleKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.faecher) {
			for (let elem of obj.faecher) {
				result.faecher?.add(StundenplanInputSimpleFach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.raeume) {
			for (let elem of obj.raeume) {
				result.raeume?.add(StundenplanInputSimpleRaum.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kopplungen) {
			for (let elem of obj.kopplungen) {
				result.kopplungen?.add(StundenplanInputSimpleKopplung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (!!obj.kurse) {
			for (let elem of obj.kurse) {
				result.kurse?.add(StundenplanInputSimpleKurs.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : StundenplanInputSimple) : string {
		let result = '{';
		if (!obj.lehrkraefte) {
			result += '"lehrkraefte" : []';
		} else {
			result += '"lehrkraefte" : [ ';
			for (let i : number = 0; i < obj.lehrkraefte.size(); i++) {
				let elem = obj.lehrkraefte.get(i);
				result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
				if (i < obj.lehrkraefte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.klassen) {
			result += '"klassen" : []';
		} else {
			result += '"klassen" : [ ';
			for (let i : number = 0; i < obj.klassen.size(); i++) {
				let elem = obj.klassen.get(i);
				result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
				if (i < obj.klassen.size() - 1)
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
				result += StundenplanInputSimpleFach.transpilerToJSON(elem);
				if (i < obj.faecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.raeume) {
			result += '"raeume" : []';
		} else {
			result += '"raeume" : [ ';
			for (let i : number = 0; i < obj.raeume.size(); i++) {
				let elem = obj.raeume.get(i);
				result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
				if (i < obj.raeume.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.kopplungen) {
			result += '"kopplungen" : []';
		} else {
			result += '"kopplungen" : [ ';
			for (let i : number = 0; i < obj.kopplungen.size(); i++) {
				let elem = obj.kopplungen.get(i);
				result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
				if (i < obj.kopplungen.size() - 1)
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
				result += StundenplanInputSimpleKurs.transpilerToJSON(elem);
				if (i < obj.kurse.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanInputSimple>) : string {
		let result = '{';
		if (typeof obj.lehrkraefte !== "undefined") {
			if (!obj.lehrkraefte) {
				result += '"lehrkraefte" : []';
			} else {
				result += '"lehrkraefte" : [ ';
				for (let i : number = 0; i < obj.lehrkraefte.size(); i++) {
					let elem = obj.lehrkraefte.get(i);
					result += StundenplanInputSimpleLehrkraft.transpilerToJSON(elem);
					if (i < obj.lehrkraefte.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.klassen !== "undefined") {
			if (!obj.klassen) {
				result += '"klassen" : []';
			} else {
				result += '"klassen" : [ ';
				for (let i : number = 0; i < obj.klassen.size(); i++) {
					let elem = obj.klassen.get(i);
					result += StundenplanInputSimpleKlasse.transpilerToJSON(elem);
					if (i < obj.klassen.size() - 1)
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
					result += StundenplanInputSimpleFach.transpilerToJSON(elem);
					if (i < obj.faecher.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.raeume !== "undefined") {
			if (!obj.raeume) {
				result += '"raeume" : []';
			} else {
				result += '"raeume" : [ ';
				for (let i : number = 0; i < obj.raeume.size(); i++) {
					let elem = obj.raeume.get(i);
					result += StundenplanInputSimpleRaum.transpilerToJSON(elem);
					if (i < obj.raeume.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.kopplungen !== "undefined") {
			if (!obj.kopplungen) {
				result += '"kopplungen" : []';
			} else {
				result += '"kopplungen" : [ ';
				for (let i : number = 0; i < obj.kopplungen.size(); i++) {
					let elem = obj.kopplungen.get(i);
					result += StundenplanInputSimpleKopplung.transpilerToJSON(elem);
					if (i < obj.kopplungen.size() - 1)
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
					result += StundenplanInputSimpleKurs.transpilerToJSON(elem);
					if (i < obj.kurse.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_stundenplanblockung_StundenplanInputSimple(obj : unknown) : StundenplanInputSimple {
	return obj as StundenplanInputSimple;
}
