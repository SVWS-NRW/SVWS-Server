import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaLong, cast_java_lang_Long } from '../../../java/lang/JavaLong';
import { AbiturFachbelegungHalbjahr, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { JavaBoolean, cast_java_lang_Boolean } from '../../../java/lang/JavaBoolean';
import { JavaDouble, cast_java_lang_Double } from '../../../java/lang/JavaDouble';

export class AbiturFachbelegung extends JavaObject {

	public fachID : number = -1;

	public letzteKursart : String | null = null;

	public abiturFach : Number | null = null;

	public istFSNeu : boolean = false;

	public block1PunktSumme : Number | null = null;

	public block1NotenpunkteDurchschnitt : Number | null = null;

	public block2NotenKuerzelPruefung : String | null = null;

	public block2PunkteZwischenstand : Number | null = null;

	public block2MuendlichePruefungAbweichung : Boolean | null = null;

	public block2MuendlichePruefungBestehen : Boolean | null = null;

	public block2MuendlichePruefungFreiwillig : Boolean | null = null;

	public block2MuendlichePruefungReihenfolge : Number | null = null;

	public block2MuendlichePruefungNotenKuerzel : String | null = null;

	public block2Punkte : Number | null = null;

	public block2Pruefer : Number | null = null;

	public readonly belegungen : Array<AbiturFachbelegungHalbjahr | null> = Array(GostHalbjahr.maxHalbjahre).fill(null);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.gost.AbiturFachbelegung'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbiturFachbelegung {
		const obj = JSON.parse(json);
		const result = new AbiturFachbelegung();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.letzteKursart = typeof obj.letzteKursart === "undefined" ? null : obj.letzteKursart === null ? null : String(obj.letzteKursart);
		result.abiturFach = typeof obj.abiturFach === "undefined" ? null : obj.abiturFach === null ? null : Number(obj.abiturFach);
		if (typeof obj.istFSNeu === "undefined")
			 throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		result.block1PunktSumme = typeof obj.block1PunktSumme === "undefined" ? null : obj.block1PunktSumme === null ? null : Number(obj.block1PunktSumme);
		result.block1NotenpunkteDurchschnitt = typeof obj.block1NotenpunkteDurchschnitt === "undefined" ? null : obj.block1NotenpunkteDurchschnitt === null ? null : Number(obj.block1NotenpunkteDurchschnitt);
		result.block2NotenKuerzelPruefung = typeof obj.block2NotenKuerzelPruefung === "undefined" ? null : obj.block2NotenKuerzelPruefung === null ? null : String(obj.block2NotenKuerzelPruefung);
		result.block2PunkteZwischenstand = typeof obj.block2PunkteZwischenstand === "undefined" ? null : obj.block2PunkteZwischenstand === null ? null : Number(obj.block2PunkteZwischenstand);
		result.block2MuendlichePruefungAbweichung = typeof obj.block2MuendlichePruefungAbweichung === "undefined" ? null : obj.block2MuendlichePruefungAbweichung === null ? null : Boolean(obj.block2MuendlichePruefungAbweichung);
		result.block2MuendlichePruefungBestehen = typeof obj.block2MuendlichePruefungBestehen === "undefined" ? null : obj.block2MuendlichePruefungBestehen === null ? null : Boolean(obj.block2MuendlichePruefungBestehen);
		result.block2MuendlichePruefungFreiwillig = typeof obj.block2MuendlichePruefungFreiwillig === "undefined" ? null : obj.block2MuendlichePruefungFreiwillig === null ? null : Boolean(obj.block2MuendlichePruefungFreiwillig);
		result.block2MuendlichePruefungReihenfolge = typeof obj.block2MuendlichePruefungReihenfolge === "undefined" ? null : obj.block2MuendlichePruefungReihenfolge === null ? null : Number(obj.block2MuendlichePruefungReihenfolge);
		result.block2MuendlichePruefungNotenKuerzel = typeof obj.block2MuendlichePruefungNotenKuerzel === "undefined" ? null : obj.block2MuendlichePruefungNotenKuerzel === null ? null : String(obj.block2MuendlichePruefungNotenKuerzel);
		result.block2Punkte = typeof obj.block2Punkte === "undefined" ? null : obj.block2Punkte === null ? null : Number(obj.block2Punkte);
		result.block2Pruefer = typeof obj.block2Pruefer === "undefined" ? null : obj.block2Pruefer === null ? null : Number(obj.block2Pruefer);
		for (let i : number = 0; i < obj.belegungen.length; i++) {
			result.belegungen[i] = obj.belegungen[i] == null ? null : (AbiturFachbelegungHalbjahr.transpilerFromJSON(JSON.stringify(obj.belegungen[i])));
		}
		return result;
	}

	public static transpilerToJSON(obj : AbiturFachbelegung) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"letzteKursart" : ' + ((!obj.letzteKursart) ? 'null' : '"' + obj.letzteKursart.valueOf() + '"') + ',';
		result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach.valueOf()) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		result += '"block1PunktSumme" : ' + ((!obj.block1PunktSumme) ? 'null' : obj.block1PunktSumme.valueOf()) + ',';
		result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt.valueOf()) + ',';
		result += '"block2NotenKuerzelPruefung" : ' + ((!obj.block2NotenKuerzelPruefung) ? 'null' : '"' + obj.block2NotenKuerzelPruefung.valueOf() + '"') + ',';
		result += '"block2PunkteZwischenstand" : ' + ((!obj.block2PunkteZwischenstand) ? 'null' : obj.block2PunkteZwischenstand.valueOf()) + ',';
		result += '"block2MuendlichePruefungAbweichung" : ' + ((!obj.block2MuendlichePruefungAbweichung) ? 'null' : obj.block2MuendlichePruefungAbweichung.valueOf()) + ',';
		result += '"block2MuendlichePruefungBestehen" : ' + ((!obj.block2MuendlichePruefungBestehen) ? 'null' : obj.block2MuendlichePruefungBestehen.valueOf()) + ',';
		result += '"block2MuendlichePruefungFreiwillig" : ' + ((!obj.block2MuendlichePruefungFreiwillig) ? 'null' : obj.block2MuendlichePruefungFreiwillig.valueOf()) + ',';
		result += '"block2MuendlichePruefungReihenfolge" : ' + ((!obj.block2MuendlichePruefungReihenfolge) ? 'null' : obj.block2MuendlichePruefungReihenfolge.valueOf()) + ',';
		result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((!obj.block2MuendlichePruefungNotenKuerzel) ? 'null' : '"' + obj.block2MuendlichePruefungNotenKuerzel.valueOf() + '"') + ',';
		result += '"block2Punkte" : ' + ((!obj.block2Punkte) ? 'null' : obj.block2Punkte.valueOf()) + ',';
		result += '"block2Pruefer" : ' + ((!obj.block2Pruefer) ? 'null' : obj.block2Pruefer.valueOf()) + ',';
		if (!obj.belegungen) {
			result += '"belegungen" : []';
		} else {
			result += '"belegungen" : [ ';
			for (let i : number = 0; i < obj.belegungen.length; i++) {
				let elem = obj.belegungen[i];
				result += (elem == null) ? null : AbiturFachbelegungHalbjahr.transpilerToJSON(elem);
				if (i < obj.belegungen.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<AbiturFachbelegung>) : string {
		let result = '{';
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.letzteKursart !== "undefined") {
			result += '"letzteKursart" : ' + ((!obj.letzteKursart) ? 'null' : '"' + obj.letzteKursart.valueOf() + '"') + ',';
		}
		if (typeof obj.abiturFach !== "undefined") {
			result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach.valueOf()) + ',';
		}
		if (typeof obj.istFSNeu !== "undefined") {
			result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		}
		if (typeof obj.block1PunktSumme !== "undefined") {
			result += '"block1PunktSumme" : ' + ((!obj.block1PunktSumme) ? 'null' : obj.block1PunktSumme.valueOf()) + ',';
		}
		if (typeof obj.block1NotenpunkteDurchschnitt !== "undefined") {
			result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt.valueOf()) + ',';
		}
		if (typeof obj.block2NotenKuerzelPruefung !== "undefined") {
			result += '"block2NotenKuerzelPruefung" : ' + ((!obj.block2NotenKuerzelPruefung) ? 'null' : '"' + obj.block2NotenKuerzelPruefung.valueOf() + '"') + ',';
		}
		if (typeof obj.block2PunkteZwischenstand !== "undefined") {
			result += '"block2PunkteZwischenstand" : ' + ((!obj.block2PunkteZwischenstand) ? 'null' : obj.block2PunkteZwischenstand.valueOf()) + ',';
		}
		if (typeof obj.block2MuendlichePruefungAbweichung !== "undefined") {
			result += '"block2MuendlichePruefungAbweichung" : ' + ((!obj.block2MuendlichePruefungAbweichung) ? 'null' : obj.block2MuendlichePruefungAbweichung.valueOf()) + ',';
		}
		if (typeof obj.block2MuendlichePruefungBestehen !== "undefined") {
			result += '"block2MuendlichePruefungBestehen" : ' + ((!obj.block2MuendlichePruefungBestehen) ? 'null' : obj.block2MuendlichePruefungBestehen.valueOf()) + ',';
		}
		if (typeof obj.block2MuendlichePruefungFreiwillig !== "undefined") {
			result += '"block2MuendlichePruefungFreiwillig" : ' + ((!obj.block2MuendlichePruefungFreiwillig) ? 'null' : obj.block2MuendlichePruefungFreiwillig.valueOf()) + ',';
		}
		if (typeof obj.block2MuendlichePruefungReihenfolge !== "undefined") {
			result += '"block2MuendlichePruefungReihenfolge" : ' + ((!obj.block2MuendlichePruefungReihenfolge) ? 'null' : obj.block2MuendlichePruefungReihenfolge.valueOf()) + ',';
		}
		if (typeof obj.block2MuendlichePruefungNotenKuerzel !== "undefined") {
			result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((!obj.block2MuendlichePruefungNotenKuerzel) ? 'null' : '"' + obj.block2MuendlichePruefungNotenKuerzel.valueOf() + '"') + ',';
		}
		if (typeof obj.block2Punkte !== "undefined") {
			result += '"block2Punkte" : ' + ((!obj.block2Punkte) ? 'null' : obj.block2Punkte.valueOf()) + ',';
		}
		if (typeof obj.block2Pruefer !== "undefined") {
			result += '"block2Pruefer" : ' + ((!obj.block2Pruefer) ? 'null' : obj.block2Pruefer.valueOf()) + ',';
		}
		if (typeof obj.belegungen !== "undefined") {
			let a = obj.belegungen;
			if (!a) {
				result += '"belegungen" : []';
			} else {
				result += '"belegungen" : [ ';
				for (let i : number = 0; i < a.length; i++) {
					let elem = a[i];
					result += (elem == null) ? null : AbiturFachbelegungHalbjahr.transpilerToJSON(elem);
					if (i < a.length - 1)
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

export function cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung(obj : unknown) : AbiturFachbelegung {
	return obj as AbiturFachbelegung;
}
