import { JavaObject } from '../../../java/lang/JavaObject';
import { StundenplanPausenaufsicht } from '../../../core/data/stundenplan/StundenplanPausenaufsicht';
import { StundenplanUnterrichtsverteilung } from '../../../core/data/stundenplan/StundenplanUnterrichtsverteilung';
import { ArrayList } from '../../../java/util/ArrayList';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import type { List } from '../../../java/util/List';
import { Stundenplan, cast_de_svws_nrw_core_data_stundenplan_Stundenplan } from '../../../core/data/stundenplan/Stundenplan';

export class StundenplanKomplett extends JavaObject {

	/**
	 * Die Grunddaten des Stundenplans.
	 */
	public daten : Stundenplan = new Stundenplan();

	/**
	 * Die Unterrichtsdaten des Stundenplans.
	 */
	public unterrichte : List<StundenplanUnterricht> = new ArrayList<StundenplanUnterricht>();

	/**
	 * Die Informationen zu den Pausenaufsichten.
	 */
	public pausenaufsichten : List<StundenplanPausenaufsicht> = new ArrayList<StundenplanPausenaufsicht>();

	/**
	 * Die Zusatzinformationen zu der Unterrichtsverteilung.
	 */
	public unterrichtsverteilung : StundenplanUnterrichtsverteilung = new StundenplanUnterrichtsverteilung();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanKomplett';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKomplett'].includes(name);
	}

	public static transpilerFromJSON(json : string): StundenplanKomplett {
		const obj = JSON.parse(json) as Partial<StundenplanKomplett>;
		const result = new StundenplanKomplett();
		if (obj.daten === undefined)
			throw new Error('invalid json format, missing attribute daten');
		result.daten = Stundenplan.transpilerFromJSON(JSON.stringify(obj.daten));
		if (obj.unterrichte !== undefined) {
			for (const elem of obj.unterrichte) {
				result.unterrichte.add(StundenplanUnterricht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.pausenaufsichten !== undefined) {
			for (const elem of obj.pausenaufsichten) {
				result.pausenaufsichten.add(StundenplanPausenaufsicht.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.unterrichtsverteilung === undefined)
			throw new Error('invalid json format, missing attribute unterrichtsverteilung');
		result.unterrichtsverteilung = StundenplanUnterrichtsverteilung.transpilerFromJSON(JSON.stringify(obj.unterrichtsverteilung));
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKomplett) : string {
		let result = '{';
		result += '"daten" : ' + Stundenplan.transpilerToJSON(obj.daten) + ',';
		result += '"unterrichte" : [ ';
		for (let i = 0; i < obj.unterrichte.size(); i++) {
			const elem = obj.unterrichte.get(i);
			result += StundenplanUnterricht.transpilerToJSON(elem);
			if (i < obj.unterrichte.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"pausenaufsichten" : [ ';
		for (let i = 0; i < obj.pausenaufsichten.size(); i++) {
			const elem = obj.pausenaufsichten.get(i);
			result += StundenplanPausenaufsicht.transpilerToJSON(elem);
			if (i < obj.pausenaufsichten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"unterrichtsverteilung" : ' + StundenplanUnterrichtsverteilung.transpilerToJSON(obj.unterrichtsverteilung) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKomplett>) : string {
		let result = '{';
		if (obj.daten !== undefined) {
			result += '"daten" : ' + Stundenplan.transpilerToJSON(obj.daten) + ',';
		}
		if (obj.unterrichte !== undefined) {
			result += '"unterrichte" : [ ';
			for (let i = 0; i < obj.unterrichte.size(); i++) {
				const elem = obj.unterrichte.get(i);
				result += StundenplanUnterricht.transpilerToJSON(elem);
				if (i < obj.unterrichte.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.pausenaufsichten !== undefined) {
			result += '"pausenaufsichten" : [ ';
			for (let i = 0; i < obj.pausenaufsichten.size(); i++) {
				const elem = obj.pausenaufsichten.get(i);
				result += StundenplanPausenaufsicht.transpilerToJSON(elem);
				if (i < obj.pausenaufsichten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.unterrichtsverteilung !== undefined) {
			result += '"unterrichtsverteilung" : ' + StundenplanUnterrichtsverteilung.transpilerToJSON(obj.unterrichtsverteilung) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKomplett(obj : unknown) : StundenplanKomplett {
	return obj as StundenplanKomplett;
}
