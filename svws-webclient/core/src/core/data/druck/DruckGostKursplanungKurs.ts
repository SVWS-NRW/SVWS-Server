import { JavaObject } from '../../../java/lang/JavaObject';
import { DruckGostKursplanungKursSchueler } from '../../../core/data/druck/DruckGostKursplanungKursSchueler';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class DruckGostKursplanungKurs extends JavaObject {

	/**
	 * ID des Kurses
	 */
	public Id : number = -1;

	/**
	 * Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis.
	 */
	public GostHalbjahr : string | null = "";

	/**
	 * Bezeichnung des Kurses.
	 */
	public Bezeichnung : string = "";

	/**
	 * Kommaseparierte Liste der Lehrkräfte des Kurses.
	 */
	public Lehrkraefte : string | null = "";

	/**
	 * Kursart des Kurses.
	 */
	public Kursart : string | null = "";

	/**
	 * Anzahl der Schülerinnen und Schüler im Kurs.
	 */
	public AnzahlTeilnehmer : number = -1;

	/**
	 * Anzahl der Schülerinnen und Schüler mit Status extern.
	 */
	public AnzahlExterneTeilnehmer : number = -1;

	/**
	 * Anzahl der Klausurschreiber.
	 */
	public AnzahlKlausurteilnehmer : number = -1;

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist.
	 */
	public AnzahlAB12 : number = -1;

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist.
	 */
	public AnzahlAB3 : number = -1;

	/**
	 * Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist.
	 */
	public AnzahlAB4 : number = -1;

	/**
	 * Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält.
	 */
	public Kursschueler : List<DruckGostKursplanungKursSchueler> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.druck.DruckGostKursplanungKurs'].includes(name);
	}

	public static transpilerFromJSON(json : string): DruckGostKursplanungKurs {
		const obj = JSON.parse(json);
		const result = new DruckGostKursplanungKurs();
		if (typeof obj.Id === "undefined")
			 throw new Error('invalid json format, missing attribute Id');
		result.Id = obj.Id;
		result.GostHalbjahr = typeof obj.GostHalbjahr === "undefined" ? null : obj.GostHalbjahr === null ? null : obj.GostHalbjahr;
		if (typeof obj.Bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute Bezeichnung');
		result.Bezeichnung = obj.Bezeichnung;
		result.Lehrkraefte = typeof obj.Lehrkraefte === "undefined" ? null : obj.Lehrkraefte === null ? null : obj.Lehrkraefte;
		result.Kursart = typeof obj.Kursart === "undefined" ? null : obj.Kursart === null ? null : obj.Kursart;
		if (typeof obj.AnzahlTeilnehmer === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlTeilnehmer');
		result.AnzahlTeilnehmer = obj.AnzahlTeilnehmer;
		if (typeof obj.AnzahlExterneTeilnehmer === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlExterneTeilnehmer');
		result.AnzahlExterneTeilnehmer = obj.AnzahlExterneTeilnehmer;
		if (typeof obj.AnzahlKlausurteilnehmer === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlKlausurteilnehmer');
		result.AnzahlKlausurteilnehmer = obj.AnzahlKlausurteilnehmer;
		if (typeof obj.AnzahlAB12 === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlAB12');
		result.AnzahlAB12 = obj.AnzahlAB12;
		if (typeof obj.AnzahlAB3 === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlAB3');
		result.AnzahlAB3 = obj.AnzahlAB3;
		if (typeof obj.AnzahlAB4 === "undefined")
			 throw new Error('invalid json format, missing attribute AnzahlAB4');
		result.AnzahlAB4 = obj.AnzahlAB4;
		if ((obj.Kursschueler !== undefined) && (obj.Kursschueler !== null)) {
			for (const elem of obj.Kursschueler) {
				result.Kursschueler?.add(DruckGostKursplanungKursSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : DruckGostKursplanungKurs) : string {
		let result = '{';
		result += '"Id" : ' + obj.Id + ',';
		result += '"GostHalbjahr" : ' + ((!obj.GostHalbjahr) ? 'null' : JSON.stringify(obj.GostHalbjahr)) + ',';
		result += '"Bezeichnung" : ' + JSON.stringify(obj.Bezeichnung!) + ',';
		result += '"Lehrkraefte" : ' + ((!obj.Lehrkraefte) ? 'null' : JSON.stringify(obj.Lehrkraefte)) + ',';
		result += '"Kursart" : ' + ((!obj.Kursart) ? 'null' : JSON.stringify(obj.Kursart)) + ',';
		result += '"AnzahlTeilnehmer" : ' + obj.AnzahlTeilnehmer + ',';
		result += '"AnzahlExterneTeilnehmer" : ' + obj.AnzahlExterneTeilnehmer + ',';
		result += '"AnzahlKlausurteilnehmer" : ' + obj.AnzahlKlausurteilnehmer + ',';
		result += '"AnzahlAB12" : ' + obj.AnzahlAB12 + ',';
		result += '"AnzahlAB3" : ' + obj.AnzahlAB3 + ',';
		result += '"AnzahlAB4" : ' + obj.AnzahlAB4 + ',';
		if (!obj.Kursschueler) {
			result += '"Kursschueler" : []';
		} else {
			result += '"Kursschueler" : [ ';
			for (let i = 0; i < obj.Kursschueler.size(); i++) {
				const elem = obj.Kursschueler.get(i);
				result += DruckGostKursplanungKursSchueler.transpilerToJSON(elem);
				if (i < obj.Kursschueler.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DruckGostKursplanungKurs>) : string {
		let result = '{';
		if (typeof obj.Id !== "undefined") {
			result += '"Id" : ' + obj.Id + ',';
		}
		if (typeof obj.GostHalbjahr !== "undefined") {
			result += '"GostHalbjahr" : ' + ((!obj.GostHalbjahr) ? 'null' : JSON.stringify(obj.GostHalbjahr)) + ',';
		}
		if (typeof obj.Bezeichnung !== "undefined") {
			result += '"Bezeichnung" : ' + JSON.stringify(obj.Bezeichnung!) + ',';
		}
		if (typeof obj.Lehrkraefte !== "undefined") {
			result += '"Lehrkraefte" : ' + ((!obj.Lehrkraefte) ? 'null' : JSON.stringify(obj.Lehrkraefte)) + ',';
		}
		if (typeof obj.Kursart !== "undefined") {
			result += '"Kursart" : ' + ((!obj.Kursart) ? 'null' : JSON.stringify(obj.Kursart)) + ',';
		}
		if (typeof obj.AnzahlTeilnehmer !== "undefined") {
			result += '"AnzahlTeilnehmer" : ' + obj.AnzahlTeilnehmer + ',';
		}
		if (typeof obj.AnzahlExterneTeilnehmer !== "undefined") {
			result += '"AnzahlExterneTeilnehmer" : ' + obj.AnzahlExterneTeilnehmer + ',';
		}
		if (typeof obj.AnzahlKlausurteilnehmer !== "undefined") {
			result += '"AnzahlKlausurteilnehmer" : ' + obj.AnzahlKlausurteilnehmer + ',';
		}
		if (typeof obj.AnzahlAB12 !== "undefined") {
			result += '"AnzahlAB12" : ' + obj.AnzahlAB12 + ',';
		}
		if (typeof obj.AnzahlAB3 !== "undefined") {
			result += '"AnzahlAB3" : ' + obj.AnzahlAB3 + ',';
		}
		if (typeof obj.AnzahlAB4 !== "undefined") {
			result += '"AnzahlAB4" : ' + obj.AnzahlAB4 + ',';
		}
		if (typeof obj.Kursschueler !== "undefined") {
			if (!obj.Kursschueler) {
				result += '"Kursschueler" : []';
			} else {
				result += '"Kursschueler" : [ ';
				for (let i = 0; i < obj.Kursschueler.size(); i++) {
					const elem = obj.Kursschueler.get(i);
					result += DruckGostKursplanungKursSchueler.transpilerToJSON(elem);
					if (i < obj.Kursschueler.size() - 1)
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

export function cast_de_svws_nrw_core_data_druck_DruckGostKursplanungKurs(obj : unknown) : DruckGostKursplanungKurs {
	return obj as DruckGostKursplanungKurs;
}
