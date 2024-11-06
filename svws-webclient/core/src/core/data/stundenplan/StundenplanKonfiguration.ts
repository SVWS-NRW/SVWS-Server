import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class StundenplanKonfiguration extends JavaObject {

	/**
	 * Der Default-Wert für den Unterrichtsbeginn.
	 */
	public defaultUnterrichtsbeginn : number = 8 * 60;

	/**
	 * Der Default-Wert für die Dauer einer Unterrichtsstunde.
	 */
	public defaultStundendauer : number = 45;

	/**
	 * Der Default-Wert für die Zeit zwischen zwei Unterrichtsstunden die für Raumwechsel gilt.
	 */
	public defaultPausenzeitFuerRaumwechsel : number = 5;

	/**
	 * Der Default-Wert des Beginns der 1. Vormittagspause.
	 */
	public defaultVormittagspause1Nach : number = 2;

	/**
	 * Der Default-Wert der Dauer der 1. Vormittagspause.
	 */
	public defaultVormittagspause1Dauer : number = 25;

	/**
	 * Der Default-Wert des Beginns der 2. Vormittagspause.
	 */
	public defaultVormittagspause2Nach : number = 4;

	/**
	 * Der Default-Wert der Dauer der 2. Vormittagspause.
	 */
	public defaultVormittagspause2Dauer : number = 25;

	/**
	 * Der Default-Wert des Beginns der Mittagspause.
	 */
	public defaultMittagspauseNach : number = 6;

	/**
	 * Der Default-Wert der Dauer der Mittagspause.
	 */
	public defaultMittagspauseDauer : number = 60;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.stundenplan.StundenplanKonfiguration';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.stundenplan.StundenplanKonfiguration'].includes(name);
	}

	public static class = new Class<StundenplanKonfiguration>('de.svws_nrw.core.data.stundenplan.StundenplanKonfiguration');

	public static transpilerFromJSON(json : string): StundenplanKonfiguration {
		const obj = JSON.parse(json) as Partial<StundenplanKonfiguration>;
		const result = new StundenplanKonfiguration();
		if (obj.defaultUnterrichtsbeginn === undefined)
			throw new Error('invalid json format, missing attribute defaultUnterrichtsbeginn');
		result.defaultUnterrichtsbeginn = obj.defaultUnterrichtsbeginn;
		if (obj.defaultStundendauer === undefined)
			throw new Error('invalid json format, missing attribute defaultStundendauer');
		result.defaultStundendauer = obj.defaultStundendauer;
		if (obj.defaultPausenzeitFuerRaumwechsel === undefined)
			throw new Error('invalid json format, missing attribute defaultPausenzeitFuerRaumwechsel');
		result.defaultPausenzeitFuerRaumwechsel = obj.defaultPausenzeitFuerRaumwechsel;
		if (obj.defaultVormittagspause1Nach === undefined)
			throw new Error('invalid json format, missing attribute defaultVormittagspause1Nach');
		result.defaultVormittagspause1Nach = obj.defaultVormittagspause1Nach;
		if (obj.defaultVormittagspause1Dauer === undefined)
			throw new Error('invalid json format, missing attribute defaultVormittagspause1Dauer');
		result.defaultVormittagspause1Dauer = obj.defaultVormittagspause1Dauer;
		if (obj.defaultVormittagspause2Nach === undefined)
			throw new Error('invalid json format, missing attribute defaultVormittagspause2Nach');
		result.defaultVormittagspause2Nach = obj.defaultVormittagspause2Nach;
		if (obj.defaultVormittagspause2Dauer === undefined)
			throw new Error('invalid json format, missing attribute defaultVormittagspause2Dauer');
		result.defaultVormittagspause2Dauer = obj.defaultVormittagspause2Dauer;
		if (obj.defaultMittagspauseNach === undefined)
			throw new Error('invalid json format, missing attribute defaultMittagspauseNach');
		result.defaultMittagspauseNach = obj.defaultMittagspauseNach;
		if (obj.defaultMittagspauseDauer === undefined)
			throw new Error('invalid json format, missing attribute defaultMittagspauseDauer');
		result.defaultMittagspauseDauer = obj.defaultMittagspauseDauer;
		return result;
	}

	public static transpilerToJSON(obj : StundenplanKonfiguration) : string {
		let result = '{';
		result += '"defaultUnterrichtsbeginn" : ' + obj.defaultUnterrichtsbeginn.toString() + ',';
		result += '"defaultStundendauer" : ' + obj.defaultStundendauer.toString() + ',';
		result += '"defaultPausenzeitFuerRaumwechsel" : ' + obj.defaultPausenzeitFuerRaumwechsel.toString() + ',';
		result += '"defaultVormittagspause1Nach" : ' + obj.defaultVormittagspause1Nach.toString() + ',';
		result += '"defaultVormittagspause1Dauer" : ' + obj.defaultVormittagspause1Dauer.toString() + ',';
		result += '"defaultVormittagspause2Nach" : ' + obj.defaultVormittagspause2Nach.toString() + ',';
		result += '"defaultVormittagspause2Dauer" : ' + obj.defaultVormittagspause2Dauer.toString() + ',';
		result += '"defaultMittagspauseNach" : ' + obj.defaultMittagspauseNach.toString() + ',';
		result += '"defaultMittagspauseDauer" : ' + obj.defaultMittagspauseDauer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<StundenplanKonfiguration>) : string {
		let result = '{';
		if (obj.defaultUnterrichtsbeginn !== undefined) {
			result += '"defaultUnterrichtsbeginn" : ' + obj.defaultUnterrichtsbeginn.toString() + ',';
		}
		if (obj.defaultStundendauer !== undefined) {
			result += '"defaultStundendauer" : ' + obj.defaultStundendauer.toString() + ',';
		}
		if (obj.defaultPausenzeitFuerRaumwechsel !== undefined) {
			result += '"defaultPausenzeitFuerRaumwechsel" : ' + obj.defaultPausenzeitFuerRaumwechsel.toString() + ',';
		}
		if (obj.defaultVormittagspause1Nach !== undefined) {
			result += '"defaultVormittagspause1Nach" : ' + obj.defaultVormittagspause1Nach.toString() + ',';
		}
		if (obj.defaultVormittagspause1Dauer !== undefined) {
			result += '"defaultVormittagspause1Dauer" : ' + obj.defaultVormittagspause1Dauer.toString() + ',';
		}
		if (obj.defaultVormittagspause2Nach !== undefined) {
			result += '"defaultVormittagspause2Nach" : ' + obj.defaultVormittagspause2Nach.toString() + ',';
		}
		if (obj.defaultVormittagspause2Dauer !== undefined) {
			result += '"defaultVormittagspause2Dauer" : ' + obj.defaultVormittagspause2Dauer.toString() + ',';
		}
		if (obj.defaultMittagspauseNach !== undefined) {
			result += '"defaultMittagspauseNach" : ' + obj.defaultMittagspauseNach.toString() + ',';
		}
		if (obj.defaultMittagspauseDauer !== undefined) {
			result += '"defaultMittagspauseDauer" : ' + obj.defaultMittagspauseDauer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_stundenplan_StundenplanKonfiguration(obj : unknown) : StundenplanKonfiguration {
	return obj as StundenplanKonfiguration;
}
