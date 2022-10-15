import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragPruefungsordnung extends JavaObject {

	public PO_Schulform : String | null = null;

	public PO_Krz : String | null = null;

	public PO_Name : String | null = null;

	public PO_SGL : String | null = null;

	public PO_MinJahrgang : Number | null = null;

	public PO_MaxJahrgang : Number | null = null;

	public PO_Jahrgaenge : String | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragPruefungsordnung'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragPruefungsordnung {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragPruefungsordnung();
		result.PO_Schulform = typeof obj.PO_Schulform === "undefined" ? null : obj.PO_Schulform;
		result.PO_Krz = typeof obj.PO_Krz === "undefined" ? null : obj.PO_Krz;
		result.PO_Name = typeof obj.PO_Name === "undefined" ? null : obj.PO_Name;
		result.PO_SGL = typeof obj.PO_SGL === "undefined" ? null : obj.PO_SGL;
		result.PO_MinJahrgang = typeof obj.PO_MinJahrgang === "undefined" ? null : obj.PO_MinJahrgang;
		result.PO_MaxJahrgang = typeof obj.PO_MaxJahrgang === "undefined" ? null : obj.PO_MaxJahrgang;
		result.PO_Jahrgaenge = typeof obj.PO_Jahrgaenge === "undefined" ? null : obj.PO_Jahrgaenge;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragPruefungsordnung) : string {
		let result = '{';
		result += '"PO_Schulform" : ' + ((!obj.PO_Schulform) ? 'null' : '"' + obj.PO_Schulform.valueOf() + '"') + ',';
		result += '"PO_Krz" : ' + ((!obj.PO_Krz) ? 'null' : '"' + obj.PO_Krz.valueOf() + '"') + ',';
		result += '"PO_Name" : ' + ((!obj.PO_Name) ? 'null' : '"' + obj.PO_Name.valueOf() + '"') + ',';
		result += '"PO_SGL" : ' + ((!obj.PO_SGL) ? 'null' : '"' + obj.PO_SGL.valueOf() + '"') + ',';
		result += '"PO_MinJahrgang" : ' + ((!obj.PO_MinJahrgang) ? 'null' : obj.PO_MinJahrgang.valueOf()) + ',';
		result += '"PO_MaxJahrgang" : ' + ((!obj.PO_MaxJahrgang) ? 'null' : obj.PO_MaxJahrgang.valueOf()) + ',';
		result += '"PO_Jahrgaenge" : ' + ((!obj.PO_Jahrgaenge) ? 'null' : '"' + obj.PO_Jahrgaenge.valueOf() + '"') + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragPruefungsordnung>) : string {
		let result = '{';
		if (typeof obj.PO_Schulform !== "undefined") {
			result += '"PO_Schulform" : ' + ((!obj.PO_Schulform) ? 'null' : '"' + obj.PO_Schulform.valueOf() + '"') + ',';
		}
		if (typeof obj.PO_Krz !== "undefined") {
			result += '"PO_Krz" : ' + ((!obj.PO_Krz) ? 'null' : '"' + obj.PO_Krz.valueOf() + '"') + ',';
		}
		if (typeof obj.PO_Name !== "undefined") {
			result += '"PO_Name" : ' + ((!obj.PO_Name) ? 'null' : '"' + obj.PO_Name.valueOf() + '"') + ',';
		}
		if (typeof obj.PO_SGL !== "undefined") {
			result += '"PO_SGL" : ' + ((!obj.PO_SGL) ? 'null' : '"' + obj.PO_SGL.valueOf() + '"') + ',';
		}
		if (typeof obj.PO_MinJahrgang !== "undefined") {
			result += '"PO_MinJahrgang" : ' + ((!obj.PO_MinJahrgang) ? 'null' : obj.PO_MinJahrgang.valueOf()) + ',';
		}
		if (typeof obj.PO_MaxJahrgang !== "undefined") {
			result += '"PO_MaxJahrgang" : ' + ((!obj.PO_MaxJahrgang) ? 'null' : obj.PO_MaxJahrgang.valueOf()) + ',';
		}
		if (typeof obj.PO_Jahrgaenge !== "undefined") {
			result += '"PO_Jahrgaenge" : ' + ((!obj.PO_Jahrgaenge) ? 'null' : '"' + obj.PO_Jahrgaenge.valueOf() + '"') + ',';
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragPruefungsordnung(obj : unknown) : Schild3KatalogEintragPruefungsordnung {
	return obj as Schild3KatalogEintragPruefungsordnung;
}
