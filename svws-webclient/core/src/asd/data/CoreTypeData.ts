import { JavaObject } from '../../java/lang/JavaObject';
import { Class } from '../../java/lang/Class';

export class CoreTypeData extends JavaObject {

	/**
	 * Die ID des Core-Type-Daten-Eintrags und der neue Schlüssel für die ASD
	 */
	public id : number = -1;

	/**
	 *  Ein Schlüssel als Zeichenkette, welcher sich auf den Schlüssel eines externen Katalogs bezieht. Als fremder Katalog können
	 *  hier auch Schlüsselwerte aus der früheren ASD-Statistik angegeben. Diese müssen für ein Jahr der Historie über den
	 *  Katalog eindeutig sein, jedoch nicht im Verlauf der Jahre. In der Historie eines Bezeichners können diese allerdings abweichen.
	 */
	public schluessel : string = "";

	/**
	 * Das Kürzel, welches als Kurztext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden.
	 */
	public kuerzel : string = "";

	/**
	 * Die Bezeichnung, welche als Langtext zu Visualisierung verwendet wird. Sollte nicht als identifizierendes Merkmal verwendet werden.
	 */
	public text : string = "";

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt die Daten als String zurück.
	 *
	 * @return die Daten als String
	 */
	public toString() : string | null {
		return "CoreTypeData [id=" + this.id + ", schluessel=" + this.schluessel + ", kuerzel=" + this.kuerzel + ", text=" + this.text + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + "]";
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.CoreTypeData';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<CoreTypeData>('de.svws_nrw.asd.data.CoreTypeData');

	public static transpilerFromJSON(json : string): CoreTypeData {
		const obj = JSON.parse(json) as Partial<CoreTypeData>;
		const result = new CoreTypeData();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schluessel === undefined)
			throw new Error('invalid json format, missing attribute schluessel');
		result.schluessel = obj.schluessel;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.text === undefined)
			throw new Error('invalid json format, missing attribute text');
		result.text = obj.text;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : CoreTypeData) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<CoreTypeData>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.schluessel !== undefined) {
			result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.text !== undefined) {
			result += '"text" : ' + JSON.stringify(obj.text) + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_CoreTypeData(obj : unknown) : CoreTypeData {
	return obj as CoreTypeData;
}
