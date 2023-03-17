import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { JavaLong, cast_java_lang_Long } from '../../../../java/lang/JavaLong';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../../java/util/Vector';

export class GostKursklausur extends JavaObject {

	/**
	 * Die ID der Kursklausur. 
	 */
	public id : number = -1;

	/**
	 * Die ID der Klausur-Vorgabe. 
	 */
	public idVorgabe : number = -1;

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird. 
	 */
	public abijahr : number = -1;

	/**
	 * Das Gost-Halbjahr, in dem die Klausurg geschrieben wird. 
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur gechrieben wird. 
	 */
	public quartal : number = -1;

	/**
	 * Die ID des Faches. 
	 */
	public idFach : number = -1;

	/**
	 * Das Kürzel einer verallgemeinerten Kursart. 
	 */
	public kursart : string = "";

	/**
	 * Die Dauer der Klausur in Minuten. 
	 */
	public dauer : number = -1;

	/**
	 * Die Auswahlzeit in Minuten, sofern vorhanden. 
	 */
	public auswahlzeit : number = -1;

	/**
	 * Die Information, ob es sich um eine mündliche Prüfung handelt. 
	 */
	public istMdlPruefung : boolean = false;

	/**
	 * Die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen. 
	 */
	public istAudioNotwendig : boolean = false;

	/**
	 * Die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse. 
	 */
	public istVideoNotwendig : boolean = false;

	/**
	 * Die textuelle Bemerkung zur Klausurvorgabe, sofern vorhanden. 
	 */
	public bemerkungVorgabe : string | null = null;

	/**
	 * Die ID des Klausurkurses. 
	 */
	public idKurs : number = -1;

	/**
	 * Die Kurzbezeichnung des Klausurkurses. 
	 */
	public kursKurzbezeichnung : string | null = "";

	/**
	 * Die Schiene des Kurses. 
	 */
	public kursSchiene : Array<number> = [];

	/**
	 * Die ID des Kurslehrers. 
	 */
	public idLehrer : number = -1;

	/**
	 * Die ID des Klausurtermins, sofern schon gesetzt. 
	 */
	public idTermin : number | null = null;

	/**
	 * Die Startzeit der Klausur, sofern abweichend von Startzeit des gesamten Termins. 
	 */
	public startzeit : string | null = null;

	/**
	 * Die Liste der IDs der zugehörigen Schüler. 
	 */
	public schuelerIds : List<number> = new Vector();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKursklausur {
		const obj = JSON.parse(json);
		const result = new GostKursklausur();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idVorgabe === "undefined")
			 throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (typeof obj.abijahr === "undefined")
			 throw new Error('invalid json format, missing attribute abijahr');
		result.abijahr = obj.abijahr;
		if (typeof obj.halbjahr === "undefined")
			 throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (typeof obj.quartal === "undefined")
			 throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		if (typeof obj.idFach === "undefined")
			 throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.dauer === "undefined")
			 throw new Error('invalid json format, missing attribute dauer');
		result.dauer = obj.dauer;
		if (typeof obj.auswahlzeit === "undefined")
			 throw new Error('invalid json format, missing attribute auswahlzeit');
		result.auswahlzeit = obj.auswahlzeit;
		if (typeof obj.istMdlPruefung === "undefined")
			 throw new Error('invalid json format, missing attribute istMdlPruefung');
		result.istMdlPruefung = obj.istMdlPruefung;
		if (typeof obj.istAudioNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istAudioNotwendig');
		result.istAudioNotwendig = obj.istAudioNotwendig;
		if (typeof obj.istVideoNotwendig === "undefined")
			 throw new Error('invalid json format, missing attribute istVideoNotwendig');
		result.istVideoNotwendig = obj.istVideoNotwendig;
		result.bemerkungVorgabe = typeof obj.bemerkungVorgabe === "undefined" ? null : obj.bemerkungVorgabe === null ? null : obj.bemerkungVorgabe;
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		result.kursKurzbezeichnung = typeof obj.kursKurzbezeichnung === "undefined" ? null : obj.kursKurzbezeichnung === null ? null : obj.kursKurzbezeichnung;
		for (let i : number = 0; i < obj.kursSchiene.length; i++) {
			result.kursSchiene[i] = obj.kursSchiene[i];
		}
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		result.idTermin = typeof obj.idTermin === "undefined" ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		if (!!obj.schuelerIds) {
			for (let elem of obj.schuelerIds) {
				result.schuelerIds?.add(elem);
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostKursklausur) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		result += '"abijahr" : ' + obj.abijahr + ',';
		result += '"halbjahr" : ' + obj.halbjahr + ',';
		result += '"quartal" : ' + obj.quartal + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"kursart" : ' + '"' + obj.kursart! + '"' + ',';
		result += '"dauer" : ' + obj.dauer + ',';
		result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : '"' + obj.bemerkungVorgabe + '"') + ',';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : '"' + obj.kursKurzbezeichnung + '"') + ',';
		if (!obj.kursSchiene) {
			result += '"kursSchiene" : []';
		} else {
			result += '"kursSchiene" : [ ';
			for (let i : number = 0; i < obj.kursSchiene.length; i++) {
				let elem = obj.kursSchiene[i];
				result += JSON.stringify(elem);
				if (i < obj.kursSchiene.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		if (!obj.schuelerIds) {
			result += '"schuelerIds" : []';
		} else {
			result += '"schuelerIds" : [ ';
			for (let i : number = 0; i < obj.schuelerIds.size(); i++) {
				let elem = obj.schuelerIds.get(i);
				result += elem;
				if (i < obj.schuelerIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKursklausur>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idVorgabe !== "undefined") {
			result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		}
		if (typeof obj.abijahr !== "undefined") {
			result += '"abijahr" : ' + obj.abijahr + ',';
		}
		if (typeof obj.halbjahr !== "undefined") {
			result += '"halbjahr" : ' + obj.halbjahr + ',';
		}
		if (typeof obj.quartal !== "undefined") {
			result += '"quartal" : ' + obj.quartal + ',';
		}
		if (typeof obj.idFach !== "undefined") {
			result += '"idFach" : ' + obj.idFach + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + '"' + obj.kursart + '"' + ',';
		}
		if (typeof obj.dauer !== "undefined") {
			result += '"dauer" : ' + obj.dauer + ',';
		}
		if (typeof obj.auswahlzeit !== "undefined") {
			result += '"auswahlzeit" : ' + obj.auswahlzeit + ',';
		}
		if (typeof obj.istMdlPruefung !== "undefined") {
			result += '"istMdlPruefung" : ' + obj.istMdlPruefung + ',';
		}
		if (typeof obj.istAudioNotwendig !== "undefined") {
			result += '"istAudioNotwendig" : ' + obj.istAudioNotwendig + ',';
		}
		if (typeof obj.istVideoNotwendig !== "undefined") {
			result += '"istVideoNotwendig" : ' + obj.istVideoNotwendig + ',';
		}
		if (typeof obj.bemerkungVorgabe !== "undefined") {
			result += '"bemerkungVorgabe" : ' + ((!obj.bemerkungVorgabe) ? 'null' : '"' + obj.bemerkungVorgabe + '"') + ',';
		}
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.kursKurzbezeichnung !== "undefined") {
			result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : '"' + obj.kursKurzbezeichnung + '"') + ',';
		}
		if (typeof obj.kursSchiene !== "undefined") {
			let a = obj.kursSchiene;
			if (!a) {
				result += '"kursSchiene" : []';
			} else {
				result += '"kursSchiene" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.idTermin !== "undefined") {
			result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		}
		if (typeof obj.startzeit !== "undefined") {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : '"' + obj.startzeit + '"') + ',';
		}
		if (typeof obj.schuelerIds !== "undefined") {
			if (!obj.schuelerIds) {
				result += '"schuelerIds" : []';
			} else {
				result += '"schuelerIds" : [ ';
				for (let i : number = 0; i < obj.schuelerIds.size(); i++) {
					let elem = obj.schuelerIds.get(i);
					result += elem;
					if (i < obj.schuelerIds.size() - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_klausuren_GostKursklausur(obj : unknown) : GostKursklausur {
	return obj as GostKursklausur;
}
