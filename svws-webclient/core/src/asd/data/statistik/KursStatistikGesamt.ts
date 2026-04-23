import { JavaObject } from '../../../java/lang/JavaObject';
import { KursLehrer } from '../../../asd/data/kurse/KursLehrer';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class KursStatistikGesamt extends JavaObject {

	/**
	 * Die ID des Kurses.
	 */
	public id: number = 0;

	/**
	 * Das Kürzel des Kurses.
	 */
	public kuerzel: string = "";

	/**
	 * Die IDs der Jahrgänge, denen der Kurs zugeordnet ist
	 */
	public idJahrgaenge: List<number> = new ArrayList<number>();

	/**
	 * Die ID des Faches, dem der Kurs zugeordnet ist
	 */
	public idFach: number = 0;

	/**
	 * Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird.
	 */
	public kursartAllg: string = "";

	/**
	 * Die Wochenstunden des Kurses.
	 */
	public wochenstunden: number = -1;

	/**
	 * Die ID des Kurslehrers.
	 */
	public lehrer: number | null = null;

	/**
	 * Die Wochenstunden des Kurslehrers in dem Kurs.
	 */
	public wochenstundenLehrer: number = -1;

	/**
	 * Die Liste der zusätzlichen Lehrkräfte eines Kurses.
	 */
	public weitereLehrer: List<KursLehrer> = new ArrayList<KursLehrer>();

	/**
	 * Die Schulnummer des Kurses, falls der Kurs an einer anderen Schule stattfindet (z.B. im Rahmen einer Kooperation).
	 */
	public schulnummer: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.statistik.KursStatistikGesamt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.data.statistik.KursStatistikGesamt'].includes(name);
	}

	public static readonly class = new Class<KursStatistikGesamt>('de.svws_nrw.asd.data.statistik.KursStatistikGesamt');

	public static transpilerFromJSON(json: string): KursStatistikGesamt {
		const obj = JSON.parse(json) as Partial<KursStatistikGesamt>;
		const result = new KursStatistikGesamt();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.idJahrgaenge !== undefined) {
			for (const elem of obj.idJahrgaenge) {
				result.idJahrgaenge.add(elem);
			}
		}
		if (obj.idFach === undefined)
			throw new Error('invalid json format, missing attribute idFach');
		result.idFach = obj.idFach;
		if (obj.kursartAllg === undefined)
			throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = obj.kursartAllg;
		if (obj.wochenstunden === undefined)
			throw new Error('invalid json format, missing attribute wochenstunden');
		result.wochenstunden = obj.wochenstunden;
		result.lehrer = (obj.lehrer === undefined) ? null : obj.lehrer === null ? null : obj.lehrer;
		if (obj.wochenstundenLehrer === undefined)
			throw new Error('invalid json format, missing attribute wochenstundenLehrer');
		result.wochenstundenLehrer = obj.wochenstundenLehrer;
		if (obj.weitereLehrer !== undefined) {
			for (const elem of obj.weitereLehrer) {
				result.weitereLehrer.add(KursLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.schulnummer = (obj.schulnummer === undefined) ? null : obj.schulnummer === null ? null : obj.schulnummer;
		return result;
	}

	public static transpilerToJSON(obj: KursStatistikGesamt): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"idJahrgaenge" : [ ';
		for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
			const elem = obj.idJahrgaenge.get(i);
			result += elem.toString();
			if (i < obj.idJahrgaenge.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"idFach" : ' + obj.idFach.toString() + ',';
		result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg) + ',';
		result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		result += '"weitereLehrer" : [ ';
		for (let i = 0; i < obj.weitereLehrer.size(); i++) {
			const elem = obj.weitereLehrer.get(i);
			result += KursLehrer.transpilerToJSON(elem);
			if (i < obj.weitereLehrer.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : obj.schulnummer.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<KursStatistikGesamt>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.idJahrgaenge !== undefined) {
			result += '"idJahrgaenge" : [ ';
			for (let i = 0; i < obj.idJahrgaenge.size(); i++) {
				const elem = obj.idJahrgaenge.get(i);
				result += elem.toString();
				if (i < obj.idJahrgaenge.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.idFach !== undefined) {
			result += '"idFach" : ' + obj.idFach.toString() + ',';
		}
		if (obj.kursartAllg !== undefined) {
			result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg) + ',';
		}
		if (obj.wochenstunden !== undefined) {
			result += '"wochenstunden" : ' + obj.wochenstunden.toString() + ',';
		}
		if (obj.lehrer !== undefined) {
			result += '"lehrer" : ' + ((obj.lehrer === null) ? 'null' : obj.lehrer.toString()) + ',';
		}
		if (obj.wochenstundenLehrer !== undefined) {
			result += '"wochenstundenLehrer" : ' + obj.wochenstundenLehrer.toString() + ',';
		}
		if (obj.weitereLehrer !== undefined) {
			result += '"weitereLehrer" : [ ';
			for (let i = 0; i < obj.weitereLehrer.size(); i++) {
				const elem = obj.weitereLehrer.get(i);
				result += KursLehrer.transpilerToJSON(elem);
				if (i < obj.weitereLehrer.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : obj.schulnummer.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_statistik_KursStatistikGesamt(obj: unknown): KursStatistikGesamt {
	return obj as KursStatistikGesamt;
}
