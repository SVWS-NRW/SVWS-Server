import { JavaObject } from '../../../java/lang/JavaObject';
import { GostZulassungSimulationNote } from '../../../core/data/gost/GostZulassungSimulationNote';
import { GostZulassungSimulationAbiturfach } from '../../../core/data/gost/GostZulassungSimulationAbiturfach';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class GostZulassungSimulation extends JavaObject {

	/**
	 * Die eingegebenen Notenpunkte der Fächer in den Halbjahren der Qualifikationsphase
	 */
	public readonly noten: List<GostZulassungSimulationNote> = new ArrayList<GostZulassungSimulationNote>();

	/**
	 * Die eingegebenen Prüfungsergebnisse der Abiturfächer
	 */
	public readonly abiturfaecher: List<GostZulassungSimulationAbiturfach> = new ArrayList<GostZulassungSimulationAbiturfach>();

	/**
	 * Die Art der besonderen Lernleistung (K - keine, P - in einem Projektkurs, E - extern)
	 */
	public besondereLernleistung: string = "K";

	/**
	 * Die Notenpunkte der besonderen Lernleistung oder null, wenn nichts eingegeben wurde
	 */
	public besondereLernleistungNotenpunkte: number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostZulassungSimulation';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.GostZulassungSimulation'].includes(name);
	}

	public static readonly class = new Class<GostZulassungSimulation>('de.svws_nrw.core.data.gost.GostZulassungSimulation');

	public static transpilerFromJSON(json: string): GostZulassungSimulation {
		const obj = JSON.parse(json) as Partial<GostZulassungSimulation>;
		const result = new GostZulassungSimulation();
		if (obj.noten !== undefined) {
			for (const elem of obj.noten) {
				result.noten.add(GostZulassungSimulationNote.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.abiturfaecher !== undefined) {
			for (const elem of obj.abiturfaecher) {
				result.abiturfaecher.add(GostZulassungSimulationAbiturfach.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.besondereLernleistung === undefined)
			throw new Error('invalid json format, missing attribute besondereLernleistung');
		result.besondereLernleistung = obj.besondereLernleistung;
		result.besondereLernleistungNotenpunkte = (obj.besondereLernleistungNotenpunkte === undefined) ? null : obj.besondereLernleistungNotenpunkte === null ? null : obj.besondereLernleistungNotenpunkte;
		return result;
	}

	public static transpilerToJSON(obj: GostZulassungSimulation): string {
		let result = '{';
		result += '"noten" : [ ';
		for (let i = 0; i < obj.noten.size(); i++) {
			const elem = obj.noten.get(i);
			result += GostZulassungSimulationNote.transpilerToJSON(elem);
			if (i < obj.noten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"abiturfaecher" : [ ';
		for (let i = 0; i < obj.abiturfaecher.size(); i++) {
			const elem = obj.abiturfaecher.get(i);
			result += GostZulassungSimulationAbiturfach.transpilerToJSON(elem);
			if (i < obj.abiturfaecher.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"besondereLernleistung" : ' + JSON.stringify(obj.besondereLernleistung) + ',';
		result += '"besondereLernleistungNotenpunkte" : ' + ((obj.besondereLernleistungNotenpunkte === null) ? 'null' : obj.besondereLernleistungNotenpunkte.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostZulassungSimulation>): string {
		let result = '{';
		if (obj.noten !== undefined) {
			result += '"noten" : [ ';
			for (let i = 0; i < obj.noten.size(); i++) {
				const elem = obj.noten.get(i);
				result += GostZulassungSimulationNote.transpilerToJSON(elem);
				if (i < obj.noten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.abiturfaecher !== undefined) {
			result += '"abiturfaecher" : [ ';
			for (let i = 0; i < obj.abiturfaecher.size(); i++) {
				const elem = obj.abiturfaecher.get(i);
				result += GostZulassungSimulationAbiturfach.transpilerToJSON(elem);
				if (i < obj.abiturfaecher.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.besondereLernleistung !== undefined) {
			result += '"besondereLernleistung" : ' + JSON.stringify(obj.besondereLernleistung) + ',';
		}
		if (obj.besondereLernleistungNotenpunkte !== undefined) {
			result += '"besondereLernleistungNotenpunkte" : ' + ((obj.besondereLernleistungNotenpunkte === null) ? 'null' : obj.besondereLernleistungNotenpunkte.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostZulassungSimulation(obj: unknown): GostZulassungSimulation {
	return obj as GostZulassungSimulation;
}
