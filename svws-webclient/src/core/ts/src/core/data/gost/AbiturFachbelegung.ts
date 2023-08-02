import { JavaObject } from '../../../java/lang/JavaObject';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';

export class AbiturFachbelegung extends JavaObject {

	/**
	 * Die ID des Faches der Gymnasialen Oberstufe, welches belegt wurde.
	 */
	public fachID : number = -1;

	/**
	 * Die letzte Kursart der Gymnasialen Oberstufe (LK, GK, ZK, PJK, VTF), mit welcher das Fach belegt wurde
	 */
	public letzteKursart : string | null = null;

	/**
	 * Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null)
	 */
	public abiturFach : number | null = null;

	/**
	 * Gibt an, ob es sich um die Belegung einer neuen Fremdsprache handelt
	 */
	public istFSNeu : boolean = false;

	/**
	 * Die Punktsumme im Block I des Abiturs für die Fachbelegung
	 */
	public block1PunktSumme : number | null = null;

	/**
	 * Der Durchschnitt der Notenpunkte im Block I des Abiturs für die Fachbelegung
	 */
	public block1NotenpunkteDurchschnitt : number | null = null;

	/**
	 * Das Notenkürzel der Abiturprüfungsnote, sofern dies die Belegung eines Abiturfaches ist.
	 */
	public block2NotenKuerzelPruefung : string | null = null;

	/**
	 * Der Zwischenstand der erreichten Punkte im Abitur vor einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 */
	public block2PunkteZwischenstand : number | null = null;

	/**
	 * Gibt an, ob eine mündliche Abweichungsprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist (nur bis Abiturjahrgang 2019, ab 2020 gibt es keine Abweichungsprüfungen mehr).
	 */
	public block2MuendlichePruefungAbweichung : boolean | null = null;

	/**
	 * Gibt an, ob eine mündliche Bestehensprüfung angesetzt werden muss, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 */
	public block2MuendlichePruefungBestehen : boolean | null = null;

	/**
	 * Gibt an, ob eine freiwillige mündliche Prüfung angesetzt wurde, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 */
	public block2MuendlichePruefungFreiwillig : boolean | null = null;

	/**
	 * Gibt die Reihenfolge bei den angesetzten mündlichen Prüfungen an, sofern dies die Belegung eines schriftlichen Abiturfaches ist.
	 */
	public block2MuendlichePruefungReihenfolge : number | null = null;

	/**
	 * Das Notenkürzel der mündlichen Abiturprüfung, sofern es sich um eine mündliche Prüfung bei einer Belegung eines schriftlichen Abiturfaches handelt.
	 */
	public block2MuendlichePruefungNotenKuerzel : string | null = null;

	/**
	 * Die erreichten Punkte im Abitur nach einer evtl. mündlichen Prüfung, sofern dies die Belegung eines Abiturfaches ist.
	 */
	public block2Punkte : number | null = null;

	/**
	 * Die Lehrer-ID des Prüfers im Rahmen der Abiturprüfung.
	 */
	public block2Pruefer : number | null = null;

	/**
	 * Die Einzelbelegungen des Faches in den einzelnen Halbjahren im Block I des Abiturs
	 */
	public readonly belegungen : Array<AbiturFachbelegungHalbjahr | null> = Array(GostHalbjahr.maxHalbjahre).fill(null);


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.AbiturFachbelegung'].includes(name);
	}

	public static transpilerFromJSON(json : string): AbiturFachbelegung {
		const obj = JSON.parse(json);
		const result = new AbiturFachbelegung();
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.letzteKursart = typeof obj.letzteKursart === "undefined" ? null : obj.letzteKursart === null ? null : obj.letzteKursart;
		result.abiturFach = typeof obj.abiturFach === "undefined" ? null : obj.abiturFach === null ? null : obj.abiturFach;
		if (typeof obj.istFSNeu === "undefined")
			 throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		result.block1PunktSumme = typeof obj.block1PunktSumme === "undefined" ? null : obj.block1PunktSumme === null ? null : obj.block1PunktSumme;
		result.block1NotenpunkteDurchschnitt = typeof obj.block1NotenpunkteDurchschnitt === "undefined" ? null : obj.block1NotenpunkteDurchschnitt === null ? null : obj.block1NotenpunkteDurchschnitt;
		result.block2NotenKuerzelPruefung = typeof obj.block2NotenKuerzelPruefung === "undefined" ? null : obj.block2NotenKuerzelPruefung === null ? null : obj.block2NotenKuerzelPruefung;
		result.block2PunkteZwischenstand = typeof obj.block2PunkteZwischenstand === "undefined" ? null : obj.block2PunkteZwischenstand === null ? null : obj.block2PunkteZwischenstand;
		result.block2MuendlichePruefungAbweichung = typeof obj.block2MuendlichePruefungAbweichung === "undefined" ? null : obj.block2MuendlichePruefungAbweichung === null ? null : obj.block2MuendlichePruefungAbweichung;
		result.block2MuendlichePruefungBestehen = typeof obj.block2MuendlichePruefungBestehen === "undefined" ? null : obj.block2MuendlichePruefungBestehen === null ? null : obj.block2MuendlichePruefungBestehen;
		result.block2MuendlichePruefungFreiwillig = typeof obj.block2MuendlichePruefungFreiwillig === "undefined" ? null : obj.block2MuendlichePruefungFreiwillig === null ? null : obj.block2MuendlichePruefungFreiwillig;
		result.block2MuendlichePruefungReihenfolge = typeof obj.block2MuendlichePruefungReihenfolge === "undefined" ? null : obj.block2MuendlichePruefungReihenfolge === null ? null : obj.block2MuendlichePruefungReihenfolge;
		result.block2MuendlichePruefungNotenKuerzel = typeof obj.block2MuendlichePruefungNotenKuerzel === "undefined" ? null : obj.block2MuendlichePruefungNotenKuerzel === null ? null : obj.block2MuendlichePruefungNotenKuerzel;
		result.block2Punkte = typeof obj.block2Punkte === "undefined" ? null : obj.block2Punkte === null ? null : obj.block2Punkte;
		result.block2Pruefer = typeof obj.block2Pruefer === "undefined" ? null : obj.block2Pruefer === null ? null : obj.block2Pruefer;
		for (let i = 0; i < obj.belegungen.length; i++) {
			result.belegungen[i] = obj.belegungen[i] == null ? null : (AbiturFachbelegungHalbjahr.transpilerFromJSON(JSON.stringify(obj.belegungen[i])));
		}
		return result;
	}

	public static transpilerToJSON(obj : AbiturFachbelegung) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"letzteKursart" : ' + ((!obj.letzteKursart) ? 'null' : JSON.stringify(obj.letzteKursart)) + ',';
		result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		result += '"block1PunktSumme" : ' + ((!obj.block1PunktSumme) ? 'null' : obj.block1PunktSumme) + ',';
		result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt) + ',';
		result += '"block2NotenKuerzelPruefung" : ' + ((!obj.block2NotenKuerzelPruefung) ? 'null' : JSON.stringify(obj.block2NotenKuerzelPruefung)) + ',';
		result += '"block2PunkteZwischenstand" : ' + ((!obj.block2PunkteZwischenstand) ? 'null' : obj.block2PunkteZwischenstand) + ',';
		result += '"block2MuendlichePruefungAbweichung" : ' + ((!obj.block2MuendlichePruefungAbweichung) ? 'null' : obj.block2MuendlichePruefungAbweichung) + ',';
		result += '"block2MuendlichePruefungBestehen" : ' + ((!obj.block2MuendlichePruefungBestehen) ? 'null' : obj.block2MuendlichePruefungBestehen) + ',';
		result += '"block2MuendlichePruefungFreiwillig" : ' + ((!obj.block2MuendlichePruefungFreiwillig) ? 'null' : obj.block2MuendlichePruefungFreiwillig) + ',';
		result += '"block2MuendlichePruefungReihenfolge" : ' + ((!obj.block2MuendlichePruefungReihenfolge) ? 'null' : obj.block2MuendlichePruefungReihenfolge) + ',';
		result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((!obj.block2MuendlichePruefungNotenKuerzel) ? 'null' : JSON.stringify(obj.block2MuendlichePruefungNotenKuerzel)) + ',';
		result += '"block2Punkte" : ' + ((!obj.block2Punkte) ? 'null' : obj.block2Punkte) + ',';
		result += '"block2Pruefer" : ' + ((!obj.block2Pruefer) ? 'null' : obj.block2Pruefer) + ',';
		if (!obj.belegungen) {
			result += '"belegungen" : []';
		} else {
			result += '"belegungen" : [ ';
			for (let i = 0; i < obj.belegungen.length; i++) {
				const elem = obj.belegungen[i];
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
			result += '"letzteKursart" : ' + ((!obj.letzteKursart) ? 'null' : JSON.stringify(obj.letzteKursart)) + ',';
		}
		if (typeof obj.abiturFach !== "undefined") {
			result += '"abiturFach" : ' + ((!obj.abiturFach) ? 'null' : obj.abiturFach) + ',';
		}
		if (typeof obj.istFSNeu !== "undefined") {
			result += '"istFSNeu" : ' + obj.istFSNeu + ',';
		}
		if (typeof obj.block1PunktSumme !== "undefined") {
			result += '"block1PunktSumme" : ' + ((!obj.block1PunktSumme) ? 'null' : obj.block1PunktSumme) + ',';
		}
		if (typeof obj.block1NotenpunkteDurchschnitt !== "undefined") {
			result += '"block1NotenpunkteDurchschnitt" : ' + ((!obj.block1NotenpunkteDurchschnitt) ? 'null' : obj.block1NotenpunkteDurchschnitt) + ',';
		}
		if (typeof obj.block2NotenKuerzelPruefung !== "undefined") {
			result += '"block2NotenKuerzelPruefung" : ' + ((!obj.block2NotenKuerzelPruefung) ? 'null' : JSON.stringify(obj.block2NotenKuerzelPruefung)) + ',';
		}
		if (typeof obj.block2PunkteZwischenstand !== "undefined") {
			result += '"block2PunkteZwischenstand" : ' + ((!obj.block2PunkteZwischenstand) ? 'null' : obj.block2PunkteZwischenstand) + ',';
		}
		if (typeof obj.block2MuendlichePruefungAbweichung !== "undefined") {
			result += '"block2MuendlichePruefungAbweichung" : ' + ((!obj.block2MuendlichePruefungAbweichung) ? 'null' : obj.block2MuendlichePruefungAbweichung) + ',';
		}
		if (typeof obj.block2MuendlichePruefungBestehen !== "undefined") {
			result += '"block2MuendlichePruefungBestehen" : ' + ((!obj.block2MuendlichePruefungBestehen) ? 'null' : obj.block2MuendlichePruefungBestehen) + ',';
		}
		if (typeof obj.block2MuendlichePruefungFreiwillig !== "undefined") {
			result += '"block2MuendlichePruefungFreiwillig" : ' + ((!obj.block2MuendlichePruefungFreiwillig) ? 'null' : obj.block2MuendlichePruefungFreiwillig) + ',';
		}
		if (typeof obj.block2MuendlichePruefungReihenfolge !== "undefined") {
			result += '"block2MuendlichePruefungReihenfolge" : ' + ((!obj.block2MuendlichePruefungReihenfolge) ? 'null' : obj.block2MuendlichePruefungReihenfolge) + ',';
		}
		if (typeof obj.block2MuendlichePruefungNotenKuerzel !== "undefined") {
			result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((!obj.block2MuendlichePruefungNotenKuerzel) ? 'null' : JSON.stringify(obj.block2MuendlichePruefungNotenKuerzel)) + ',';
		}
		if (typeof obj.block2Punkte !== "undefined") {
			result += '"block2Punkte" : ' + ((!obj.block2Punkte) ? 'null' : obj.block2Punkte) + ',';
		}
		if (typeof obj.block2Pruefer !== "undefined") {
			result += '"block2Pruefer" : ' + ((!obj.block2Pruefer) ? 'null' : obj.block2Pruefer) + ',';
		}
		if (typeof obj.belegungen !== "undefined") {
			const a = obj.belegungen;
			if (!a) {
				result += '"belegungen" : []';
			} else {
				result += '"belegungen" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
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

export function cast_de_svws_nrw_core_data_gost_AbiturFachbelegung(obj : unknown) : AbiturFachbelegung {
	return obj as AbiturFachbelegung;
}
