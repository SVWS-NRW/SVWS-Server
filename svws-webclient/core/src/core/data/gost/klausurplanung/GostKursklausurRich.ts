import { JavaLong } from '../../../../java/lang/JavaLong';
import { ArrayList } from '../../../../java/util/ArrayList';
import type { List } from '../../../../java/util/List';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

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
	public idLehrer : number | null = null;

	/**
	 * Die ID des Klausurtermins, sofern schon gesetzt.
	 */
	public idTermin : number | null = null;

	/**
	 * Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins.
	 */
	public startzeit : number | null = null;

	/**
	 * Ein Array mit den IDs der zugehörigen Schüler.
	 */
	public schuelerIds : List<number> = new ArrayList<number>();

	/**
	 * Die textuelle Bemerkung zur Kursklausur, sofern vorhanden.
	 */
	public bemerkung : string | null = null;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	public equals(another : unknown | null) : boolean {
		return (another !== null) && (((another instanceof JavaObject) && (another.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich')))) && (this.id === (cast_de_svws_nrw_core_data_gost_klausurplanung_GostKursklausurRich(another)).id);
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

	public static class = new Class<GostKursklausurRich>('de.svws_nrw.core.data.gost.klausurplanung.GostKursklausurRich');

	public static transpilerFromJSON(json : string): GostKursklausurRich {
		const obj = JSON.parse(json) as Partial<GostKursklausurRich>;
		const result = new GostKursklausurRich();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idVorgabe === undefined)
			throw new Error('invalid json format, missing attribute idVorgabe');
		result.idVorgabe = obj.idVorgabe;
		if (obj.abijahr === undefined)
			throw new Error('invalid json format, missing attribute abijahr');
		result.abijahr = obj.abijahr;
		if (obj.halbjahr === undefined)
			throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		if (obj.quartal === undefined)
			throw new Error('invalid json format, missing attribute quartal');
		result.quartal = obj.quartal;
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.idKurs === undefined)
			throw new Error('invalid json format, missing attribute idKurs');
		result.idKurs = obj.idKurs;
		result.kursKurzbezeichnung = (obj.kursKurzbezeichnung === undefined) ? null : obj.kursKurzbezeichnung === null ? null : obj.kursKurzbezeichnung;
		if (obj.kursSchiene !== undefined) {
			for (let i = 0; i < obj.kursSchiene.length; i++) {
				result.kursSchiene[i] = obj.kursSchiene[i];
			}
		}
		result.idLehrer = (obj.idLehrer === undefined) ? null : obj.idLehrer === null ? null : obj.idLehrer;
		result.idTermin = (obj.idTermin === undefined) ? null : obj.idTermin === null ? null : obj.idTermin;
		result.startzeit = (obj.startzeit === undefined) ? null : obj.startzeit === null ? null : obj.startzeit;
		if (obj.schuelerIds !== undefined) {
			for (const elem of obj.schuelerIds) {
				result.schuelerIds.add(elem);
			}
		}
		result.bemerkung = (obj.bemerkung === undefined) ? null : obj.bemerkung === null ? null : obj.bemerkung;
		return result;
	}

	public static transpilerToJSON(obj : GostKursklausurRich) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idVorgabe" : ' + obj.idVorgabe.toString() + ',';
		result += '"abijahr" : ' + obj.abijahr.toString() + ',';
		result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		result += '"quartal" : ' + obj.quartal.toString() + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : JSON.stringify(obj.kursKurzbezeichnung)) + ',';
		result += '"kursSchiene" : [ ';
		for (let i = 0; i < obj.kursSchiene.length; i++) {
			const elem = obj.kursSchiene[i];
			result += JSON.stringify(elem);
			if (i < obj.kursSchiene.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idLehrer" : ' + ((!obj.idLehrer) ? 'null' : obj.idLehrer.toString()) + ',';
		result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin.toString()) + ',';
		result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit.toString()) + ',';
		result += '"schuelerIds" : [ ';
		for (let i = 0; i < obj.schuelerIds.size(); i++) {
			const elem = obj.schuelerIds.get(i);
			result += elem.toString();
			if (i < obj.schuelerIds.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"bemerkung" : ' + ((!obj.bemerkung) ? 'null' : JSON.stringify(obj.bemerkung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostKursklausurRich>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idVorgabe !== undefined) {
			result += '"idVorgabe" : ' + obj.idVorgabe.toString() + ',';
		}
		if (obj.abijahr !== undefined) {
			result += '"abijahr" : ' + obj.abijahr.toString() + ',';
		}
		if (obj.halbjahr !== undefined) {
			result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		}
		if (obj.quartal !== undefined) {
			result += '"quartal" : ' + obj.quartal.toString() + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + JSON.stringify(obj.kursart) + ',';
		}
		if (obj.idKurs !== undefined) {
			result += '"idKurs" : ' + obj.idKurs.toString() + ',';
		}
		if (obj.kursKurzbezeichnung !== undefined) {
			result += '"kursKurzbezeichnung" : ' + ((!obj.kursKurzbezeichnung) ? 'null' : JSON.stringify(obj.kursKurzbezeichnung)) + ',';
		}
		if (obj.kursSchiene !== undefined) {
			const a = obj.kursSchiene;
			result += '"kursSchiene" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idLehrer !== undefined) {
			result += '"idLehrer" : ' + ((!obj.idLehrer) ? 'null' : obj.idLehrer.toString()) + ',';
		}
		if (obj.idTermin !== undefined) {
			result += '"idTermin" : ' + ((!obj.idTermin) ? 'null' : obj.idTermin.toString()) + ',';
		}
		if (obj.startzeit !== undefined) {
			result += '"startzeit" : ' + ((!obj.startzeit) ? 'null' : obj.startzeit.toString()) + ',';
		}
		if (obj.schuelerIds !== undefined) {
			result += '"schuelerIds" : [ ';
			for (let i = 0; i < obj.schuelerIds.size(); i++) {
				const elem = obj.schuelerIds.get(i);
				result += elem.toString();
				if (i < obj.schuelerIds.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.bemerkung !== undefined) {
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
