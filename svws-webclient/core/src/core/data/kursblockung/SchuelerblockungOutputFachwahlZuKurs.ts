import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchuelerblockungOutputFachwahlZuKurs extends JavaObject {

	/**
	 * Die ID des Faches (des Schülers).
	 */
	public fachID : number = 0;

	/**
	 * Die ID der Kursart (des Schülers).
	 */
	public kursartID : number = 0;

	/**
	 * Die ID des zugeordneten Kurses zur Fachwahl (des Schülers). Ein Wert von {@code -1} bedeutet, dass das Fach
	 *  nicht zugeordnet werden konnte, was als <b>Nicht-Wahl</b> bezeichnet wird.
	 */
	public kursID : number = 0;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs'].includes(name);
	}

	public static class = new Class<SchuelerblockungOutputFachwahlZuKurs>('de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs');

	public static transpilerFromJSON(json : string): SchuelerblockungOutputFachwahlZuKurs {
		const obj = JSON.parse(json) as Partial<SchuelerblockungOutputFachwahlZuKurs>;
		const result = new SchuelerblockungOutputFachwahlZuKurs();
		if (obj.fachID === undefined)
			throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (obj.kursartID === undefined)
			throw new Error('invalid json format, missing attribute kursartID');
		result.kursartID = obj.kursartID;
		if (obj.kursID === undefined)
			throw new Error('invalid json format, missing attribute kursID');
		result.kursID = obj.kursID;
		return result;
	}

	public static transpilerToJSON(obj : SchuelerblockungOutputFachwahlZuKurs) : string {
		let result = '{';
		result += '"fachID" : ' + obj.fachID.toString() + ',';
		result += '"kursartID" : ' + obj.kursartID.toString() + ',';
		result += '"kursID" : ' + obj.kursID.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuelerblockungOutputFachwahlZuKurs>) : string {
		let result = '{';
		if (obj.fachID !== undefined) {
			result += '"fachID" : ' + obj.fachID.toString() + ',';
		}
		if (obj.kursartID !== undefined) {
			result += '"kursartID" : ' + obj.kursartID.toString() + ',';
		}
		if (obj.kursID !== undefined) {
			result += '"kursID" : ' + obj.kursID.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_kursblockung_SchuelerblockungOutputFachwahlZuKurs(obj : unknown) : SchuelerblockungOutputFachwahlZuKurs {
	return obj as SchuelerblockungOutputFachwahlZuKurs;
}
