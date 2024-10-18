import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerblockungInputFachwahl extends JavaObject {

	/**
	 * Die ID der Fachwahl.
	 */
	public id : number = 0;

	/**
	 * Die ID des Faches. Beispielsweise des Faches 'D'.
	 */
	public fach : number = 0;

	/**
	 * Die ID der Kursart. Beispielsweise der Kursart 'LK'.
	 */
	public kursart : number = 0;

	/**
	 * Eine String-Darstellung der Fachwahl, damit bei Warnungen oder Fehlern dem Benutzer diese angezeigt werden kann,
	 *  beispielsweise 'Mareike Musterfrau hat D;LK'.
	 */
	public representation : string = "";


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kursblockung.SchuelerblockungInputFachwahl';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kursblockung.SchuelerblockungInputFachwahl'].includes(name);
	}

	public static class = new Class<SchuelerblockungInputFachwahl>('de.svws_nrw.core.data.kursblockung.SchuelerblockungInputFachwahl');

	public static transpilerFromJSON(json : string): SchuelerblockungInputFachwahl {
		const obj = JSON.parse(json) as Partial<SchuelerblockungInputFachwahl>;
		const result = new SchuelerblockungInputFachwahl();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.fach === undefined)
			throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		if (obj.kursart === undefined)
			throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (obj.representation === undefined)
			throw new Error('invalid json format, missing attribute representation');
		result.representation = obj.representation;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungInputFachwahl) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"fach" : ' + obj.fach.toString() + ',';
		result += '"kursart" : ' + obj.kursart.toString() + ',';
		result += '"representation" : ' + JSON.stringify(obj.representation) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungInputFachwahl>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.fach !== undefined) {
			result += '"fach" : ' + obj.fach.toString() + ',';
		}
		if (obj.kursart !== undefined) {
			result += '"kursart" : ' + obj.kursart.toString() + ',';
		}
		if (obj.representation !== undefined) {
			result += '"representation" : ' + JSON.stringify(obj.representation) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kursblockung_SchuelerblockungInputFachwahl(obj : unknown) : SchuelerblockungInputFachwahl {
	return obj as SchuelerblockungInputFachwahl;
}
