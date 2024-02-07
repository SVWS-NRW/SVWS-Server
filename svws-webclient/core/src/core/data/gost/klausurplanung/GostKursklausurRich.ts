import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { JavaObject } from '../../../../java/lang/JavaObject';

export class GostKursklausurRich extends JavaObject {

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
	 * Das Gost-Halbjahr, in dem die Klausur geschrieben wird.
	 */
	public halbjahr : number = -1;

	/**
	 * Das Quartal, in welchem die Klausur geschrieben wird.
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
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number | null = null;

	/**
	 * Die Liste der IDs der zugehörigen Schüler.
	 */
	public schuelerIds : List<number> = new ArrayList();

	/**
	 * Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das akutelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte indentisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return another !== null && ((another instanceof JavaObject) && ((another as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich'))) && this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKursklausurRich(another)).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	public hashCode() : number {
		return JavaLong.hashCode((this.id));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostKursklausurRich {
		const obj = JSON.parse(json);
		const result = new GostKursklausurRich();
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
		if (typeof obj.idKurs === "undefined")
			 throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		result.kursKurzbezeichnung = typeof obj.kursKurzbezeichnung === "undefined" ? null : obj.kursKurzbezeichnung === null ? null : obj.kursKurzbezeichnung;
		for (let i = 0; i < obj.kursSchiene.length; i++) {
			result.kursSchiene[i] = obj.kursSchiene[i];
		}
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		result.idTermin = typeof obj.idTermin === "undefined" ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = typeof obj.startzeit === "undefined" ? null : obj.startzeit === null ? null : obj.startzeit;
		if ((obj.schuelerIds !== undefined) && (obj.schuelerIds !== null)) {
			for (const elem of obj.schuelerIds) {
				result.schuelerIds?.add(elem);
			}
		}
		result.bemerkung = typeof obj.bemerkung === "undefined" ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostKursklausurRich) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idVorgabe" : ' + obj.idVorgabe + ',';
		result += '"abijahr" : ' + obj.abijahr + ',';
		result += '"halbjahr" : ' + obj.halbjahr + ',';
		result += '"quartal" : ' + obj.quartal + ',';
		result += '"idFach" : ' + obj.idFach + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		result += '"idKurs" : ' + obj.idKurs + ',';
		result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : JSON.stringify(obj.kursKurzbezeichnung)) + ',';
		if (!obj.kursSchiene) {
			result += '"kursSchiene" : []';
		} else {
			result += '"kursSchiene" : [ ';
			for (let i = 0; i < obj.kursSchiene.length; i++) {
				const elem = obj.kursSchiene[i];
				result += JSON.stringify(elem);
				if (i < obj.kursSchiene.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		if (!obj.schuelerIds) {
			result += '"schuelerIds" : []';
		} else {
			result += '"schuelerIds" : [ ';
			for (let i = 0; i < obj.schuelerIds.size(); i++) {
				const elem = obj.schuelerIds.get(i);
				result += elem;
				if (i < obj.schuelerIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKursklausurRich>) : string {
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
			result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		}
		if (typeof obj.idKurs !== "undefined") {
			result += '"idKurs" : ' + obj.idKurs + ',';
		}
		if (typeof obj.kursKurzbezeichnung !== "undefined") {
			result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : JSON.stringify(obj.kursKurzbezeichnung)) + ',';
		}
		if (typeof obj.kursSchiene !== "undefined") {
			const a = obj.kursSchiene;
			if (!a) {
				result += '"kursSchiene" : []';
			} else {
				result += '"kursSchiene" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
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
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit) + ',';
		}
		if (typeof obj.schuelerIds !== "undefined") {
			if (!obj.schuelerIds) {
				result += '"schuelerIds" : []';
			} else {
				result += '"schuelerIds" : [ ';
				for (let i = 0; i < obj.schuelerIds.size(); i++) {
					const elem = obj.schuelerIds.get(i);
					result += elem;
					if (i < obj.schuelerIds.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.bemerkung !== "undefined") {
			result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_klausurplanung_GostKursklausurRich(obj : unknown) : GostKursklausurRich {
	return obj as GostKursklausurRich;
}
