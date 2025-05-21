import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { BKGymAbiturFachbelegungHalbjahr } from '../../../../core/abschluss/bk/d/BKGymAbiturFachbelegungHalbjahr';
import { Class } from '../../../../java/lang/Class';

export class BKGymAbiturFachbelegung extends JavaObject {

	/**
	 * Die ID des Faches des beruflichen Gymnasiums, welches belegt wurde.
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
	public readonly belegungen : Array<BKGymAbiturFachbelegungHalbjahr | null> = Array(GostHalbjahr.maxHalbjahre).fill(null);


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymAbiturFachbelegung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymAbiturFachbelegung'].includes(name);
	}

	public static class = new Class<BKGymAbiturFachbelegung>('de.svws_nrw.core.abschluss.bk.d.BKGymAbiturFachbelegung');

	public static transpilerFromJSON(json : string): BKGymAbiturFachbelegung {
		const obj = JSON.parse(json) as Partial<BKGymAbiturFachbelegung>;
		const result = new BKGymAbiturFachbelegung();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		result.letzteKursart = (obj.letzteKursart === undefined) ? null : obj.letzteKursart === null ? null : obj.letzteKursart;
		result.abiturFach = (obj.abiturFach === undefined) ? null : obj.abiturFach === null ? null : obj.abiturFach;
		if (obj.istFSNeu === undefined)
			throw new Error('invalid json format, missing attribute istFSNeu');
		result.istFSNeu = obj.istFSNeu;
		result.block1PunktSumme = (obj.block1PunktSumme === undefined) ? null : obj.block1PunktSumme === null ? null : obj.block1PunktSumme;
		result.block1NotenpunkteDurchschnitt = (obj.block1NotenpunkteDurchschnitt === undefined) ? null : obj.block1NotenpunkteDurchschnitt === null ? null : obj.block1NotenpunkteDurchschnitt;
		result.block2NotenKuerzelPruefung = (obj.block2NotenKuerzelPruefung === undefined) ? null : obj.block2NotenKuerzelPruefung === null ? null : obj.block2NotenKuerzelPruefung;
		result.block2PunkteZwischenstand = (obj.block2PunkteZwischenstand === undefined) ? null : obj.block2PunkteZwischenstand === null ? null : obj.block2PunkteZwischenstand;
		result.block2MuendlichePruefungAbweichung = (obj.block2MuendlichePruefungAbweichung === undefined) ? null : obj.block2MuendlichePruefungAbweichung === null ? null : obj.block2MuendlichePruefungAbweichung;
		result.block2MuendlichePruefungBestehen = (obj.block2MuendlichePruefungBestehen === undefined) ? null : obj.block2MuendlichePruefungBestehen === null ? null : obj.block2MuendlichePruefungBestehen;
		result.block2MuendlichePruefungFreiwillig = (obj.block2MuendlichePruefungFreiwillig === undefined) ? null : obj.block2MuendlichePruefungFreiwillig === null ? null : obj.block2MuendlichePruefungFreiwillig;
		result.block2MuendlichePruefungReihenfolge = (obj.block2MuendlichePruefungReihenfolge === undefined) ? null : obj.block2MuendlichePruefungReihenfolge === null ? null : obj.block2MuendlichePruefungReihenfolge;
		result.block2MuendlichePruefungNotenKuerzel = (obj.block2MuendlichePruefungNotenKuerzel === undefined) ? null : obj.block2MuendlichePruefungNotenKuerzel === null ? null : obj.block2MuendlichePruefungNotenKuerzel;
		result.block2Punkte = (obj.block2Punkte === undefined) ? null : obj.block2Punkte === null ? null : obj.block2Punkte;
		result.block2Pruefer = (obj.block2Pruefer === undefined) ? null : obj.block2Pruefer === null ? null : obj.block2Pruefer;
		if (obj.belegungen !== undefined) {
			for (let i = 0; i < obj.belegungen.length; i++) {
				result.belegungen[i] = obj.belegungen[i] == null ? null : (BKGymAbiturFachbelegungHalbjahr.transpilerFromJSON(JSON.stringify(obj.belegungen[i])));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : BKGymAbiturFachbelegung) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"letzteKursart" : ' + ((obj.letzteKursart === null) ? 'null' : JSON.stringify(obj.letzteKursart)) + ',';
		result += '"abiturFach" : ' + ((obj.abiturFach === null) ? 'null' : obj.abiturFach.toString()) + ',';
		result += '"istFSNeu" : ' + obj.istFSNeu.toString() + ',';
		result += '"block1PunktSumme" : ' + ((obj.block1PunktSumme === null) ? 'null' : obj.block1PunktSumme.toString()) + ',';
		result += '"block1NotenpunkteDurchschnitt" : ' + ((obj.block1NotenpunkteDurchschnitt === null) ? 'null' : obj.block1NotenpunkteDurchschnitt.toString()) + ',';
		result += '"block2NotenKuerzelPruefung" : ' + ((obj.block2NotenKuerzelPruefung === null) ? 'null' : JSON.stringify(obj.block2NotenKuerzelPruefung)) + ',';
		result += '"block2PunkteZwischenstand" : ' + ((obj.block2PunkteZwischenstand === null) ? 'null' : obj.block2PunkteZwischenstand.toString()) + ',';
		result += '"block2MuendlichePruefungAbweichung" : ' + ((obj.block2MuendlichePruefungAbweichung === null) ? 'null' : obj.block2MuendlichePruefungAbweichung.toString()) + ',';
		result += '"block2MuendlichePruefungBestehen" : ' + ((obj.block2MuendlichePruefungBestehen === null) ? 'null' : obj.block2MuendlichePruefungBestehen.toString()) + ',';
		result += '"block2MuendlichePruefungFreiwillig" : ' + ((obj.block2MuendlichePruefungFreiwillig === null) ? 'null' : obj.block2MuendlichePruefungFreiwillig.toString()) + ',';
		result += '"block2MuendlichePruefungReihenfolge" : ' + ((obj.block2MuendlichePruefungReihenfolge === null) ? 'null' : obj.block2MuendlichePruefungReihenfolge.toString()) + ',';
		result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((obj.block2MuendlichePruefungNotenKuerzel === null) ? 'null' : JSON.stringify(obj.block2MuendlichePruefungNotenKuerzel)) + ',';
		result += '"block2Punkte" : ' + ((obj.block2Punkte === null) ? 'null' : obj.block2Punkte.toString()) + ',';
		result += '"block2Pruefer" : ' + ((obj.block2Pruefer === null) ? 'null' : obj.block2Pruefer.toString()) + ',';
		result += '"belegungen" : [ ';
		for (let i = 0; i < obj.belegungen.length; i++) {
			const elem = obj.belegungen[i];
			result += (elem === null) ? null : BKGymAbiturFachbelegungHalbjahr.transpilerToJSON(elem);
			if (i < obj.belegungen.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<BKGymAbiturFachbelegung>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.letzteKursart !== undefined) {
			result += '"letzteKursart" : ' + ((obj.letzteKursart === null) ? 'null' : JSON.stringify(obj.letzteKursart)) + ',';
		}
		if (obj.abiturFach !== undefined) {
			result += '"abiturFach" : ' + ((obj.abiturFach === null) ? 'null' : obj.abiturFach.toString()) + ',';
		}
		if (obj.istFSNeu !== undefined) {
			result += '"istFSNeu" : ' + obj.istFSNeu.toString() + ',';
		}
		if (obj.block1PunktSumme !== undefined) {
			result += '"block1PunktSumme" : ' + ((obj.block1PunktSumme === null) ? 'null' : obj.block1PunktSumme.toString()) + ',';
		}
		if (obj.block1NotenpunkteDurchschnitt !== undefined) {
			result += '"block1NotenpunkteDurchschnitt" : ' + ((obj.block1NotenpunkteDurchschnitt === null) ? 'null' : obj.block1NotenpunkteDurchschnitt.toString()) + ',';
		}
		if (obj.block2NotenKuerzelPruefung !== undefined) {
			result += '"block2NotenKuerzelPruefung" : ' + ((obj.block2NotenKuerzelPruefung === null) ? 'null' : JSON.stringify(obj.block2NotenKuerzelPruefung)) + ',';
		}
		if (obj.block2PunkteZwischenstand !== undefined) {
			result += '"block2PunkteZwischenstand" : ' + ((obj.block2PunkteZwischenstand === null) ? 'null' : obj.block2PunkteZwischenstand.toString()) + ',';
		}
		if (obj.block2MuendlichePruefungAbweichung !== undefined) {
			result += '"block2MuendlichePruefungAbweichung" : ' + ((obj.block2MuendlichePruefungAbweichung === null) ? 'null' : obj.block2MuendlichePruefungAbweichung.toString()) + ',';
		}
		if (obj.block2MuendlichePruefungBestehen !== undefined) {
			result += '"block2MuendlichePruefungBestehen" : ' + ((obj.block2MuendlichePruefungBestehen === null) ? 'null' : obj.block2MuendlichePruefungBestehen.toString()) + ',';
		}
		if (obj.block2MuendlichePruefungFreiwillig !== undefined) {
			result += '"block2MuendlichePruefungFreiwillig" : ' + ((obj.block2MuendlichePruefungFreiwillig === null) ? 'null' : obj.block2MuendlichePruefungFreiwillig.toString()) + ',';
		}
		if (obj.block2MuendlichePruefungReihenfolge !== undefined) {
			result += '"block2MuendlichePruefungReihenfolge" : ' + ((obj.block2MuendlichePruefungReihenfolge === null) ? 'null' : obj.block2MuendlichePruefungReihenfolge.toString()) + ',';
		}
		if (obj.block2MuendlichePruefungNotenKuerzel !== undefined) {
			result += '"block2MuendlichePruefungNotenKuerzel" : ' + ((obj.block2MuendlichePruefungNotenKuerzel === null) ? 'null' : JSON.stringify(obj.block2MuendlichePruefungNotenKuerzel)) + ',';
		}
		if (obj.block2Punkte !== undefined) {
			result += '"block2Punkte" : ' + ((obj.block2Punkte === null) ? 'null' : obj.block2Punkte.toString()) + ',';
		}
		if (obj.block2Pruefer !== undefined) {
			result += '"block2Pruefer" : ' + ((obj.block2Pruefer === null) ? 'null' : obj.block2Pruefer.toString()) + ',';
		}
		if (obj.belegungen !== undefined) {
			const a = obj.belegungen;
			result += '"belegungen" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += (elem === null) ? null : BKGymAbiturFachbelegungHalbjahr.transpilerToJSON(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymAbiturFachbelegung(obj : unknown) : BKGymAbiturFachbelegung {
	return obj as BKGymAbiturFachbelegung;
}
