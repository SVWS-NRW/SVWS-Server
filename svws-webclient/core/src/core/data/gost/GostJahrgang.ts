import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class GostJahrgang extends JavaObject {

	/**
	 * Das Jahr, in welchem der Jahrgang Abitur machen wird. -1 bei der Vorlage für neue Abiturjahrgänge.
	 */
	public abiturjahr : number = -1;

	/**
	 * Die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist.
	 */
	public jahrgang : string | null = null;

	/**
	 * Das aktuelle Halbjahr, in dem sich der Jahrgang laut Schuljahrsabschnitt der Schule befindet.
	 */
	public halbjahr : number = 0;

	/**
	 * Die textuelle Bezeichnung für den Abiturjahrgang
	 */
	public bezeichnung : string | null = "Allgemein / Vorlage";

	/**
	 * Gibt an, ob das Abitur für diesen Jahrgang bereits abgeschlossen ist, d.h. die Schule sich bereits in einem späteren Schuljahr befindet.
	 */
	public istAbgeschlossen : boolean = false;

	/**
	 * Gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits eine Blockung in den Leistungsdaten persistiert wurde (0=EF.1, 1=EF.2, ...)
	 */
	public istBlockungFestgelegt : Array<boolean> = Array(6).fill(false);

	/**
	 * Gibt an, ob für die jeweiligen Halbjahre der Oberstufe bereits (Quartals-)Noten in den Leistungsdaten vorhanden sind (0=EF.1, 1=EF.2, ...)
	 */
	public existierenNotenInLeistungsdaten : Array<boolean> = Array(6).fill(false);


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostJahrgang';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostJahrgang'].includes(name);
	}

	public static class = new Class<GostJahrgang>('de.svws_nrw.core.data.gost.GostJahrgang');

	public static transpilerFromJSON(json : string): GostJahrgang {
		const obj = JSON.parse(json) as Partial<GostJahrgang>;
		const result = new GostJahrgang();
		if (obj.abiturjahr === undefined)
			throw new Error('invalid json format, missing attribute abiturjahr');
		result.abiturjahr = obj.abiturjahr;
		result.jahrgang = (obj.jahrgang === undefined) ? null : obj.jahrgang === null ? null : obj.jahrgang;
		if (obj.halbjahr === undefined)
			throw new Error('invalid json format, missing attribute halbjahr');
		result.halbjahr = obj.halbjahr;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.istAbgeschlossen === undefined)
			throw new Error('invalid json format, missing attribute istAbgeschlossen');
		result.istAbgeschlossen = obj.istAbgeschlossen;
		if (obj.istBlockungFestgelegt !== undefined) {
			for (let i = 0; i < obj.istBlockungFestgelegt.length; i++) {
				result.istBlockungFestgelegt[i] = obj.istBlockungFestgelegt[i];
			}
		}
		if (obj.existierenNotenInLeistungsdaten !== undefined) {
			for (let i = 0; i < obj.existierenNotenInLeistungsdaten.length; i++) {
				result.existierenNotenInLeistungsdaten[i] = obj.existierenNotenInLeistungsdaten[i];
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : GostJahrgang) : string {
		let result = '{';
		result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen.toString() + ',';
		result += '"istBlockungFestgelegt" : [ ';
		for (let i = 0; i < obj.istBlockungFestgelegt.length; i++) {
			const elem = obj.istBlockungFestgelegt[i];
			result += JSON.stringify(elem);
			if (i < obj.istBlockungFestgelegt.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"existierenNotenInLeistungsdaten" : [ ';
		for (let i = 0; i < obj.existierenNotenInLeistungsdaten.length; i++) {
			const elem = obj.existierenNotenInLeistungsdaten[i];
			result += JSON.stringify(elem);
			if (i < obj.existierenNotenInLeistungsdaten.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostJahrgang>) : string {
		let result = '{';
		if (obj.abiturjahr !== undefined) {
			result += '"abiturjahr" : ' + obj.abiturjahr.toString() + ',';
		}
		if (obj.jahrgang !== undefined) {
			result += '"jahrgang" : ' + ((obj.jahrgang === null) ? 'null' : JSON.stringify(obj.jahrgang)) + ',';
		}
		if (obj.halbjahr !== undefined) {
			result += '"halbjahr" : ' + obj.halbjahr.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.istAbgeschlossen !== undefined) {
			result += '"istAbgeschlossen" : ' + obj.istAbgeschlossen.toString() + ',';
		}
		if (obj.istBlockungFestgelegt !== undefined) {
			const a = obj.istBlockungFestgelegt;
			result += '"istBlockungFestgelegt" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.existierenNotenInLeistungsdaten !== undefined) {
			const a = obj.existierenNotenInLeistungsdaten;
			result += '"existierenNotenInLeistungsdaten" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
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

export function cast_de_svws_nrw_core_data_gost_GostJahrgang(obj : unknown) : GostJahrgang {
	return obj as GostJahrgang;
}
