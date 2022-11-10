import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class Schild3KatalogEintragPruefungsordnungOption extends JavaObject {

	public OP_Schulformen : String | null = null;

	public OP_POKrz : String | null = null;

	public OP_Krz : String | null = null;

	public OP_Abgangsart_B : String | null = null;

	public OP_Abgangsart_NB : String | null = null;

	public OP_Art : String | null = null;

	public OP_Typ : String | null = null;

	public OP_Bildungsgang : String | null = null;

	public OP_Name : String | null = null;

	public OP_Kommentar : String | null = null;

	public OP_Jahrgaenge : String | null = null;

	public OP_BKIndex : String | null = null;

	public OP_BKAnl_Typ : String | null = null;

	public OP_Reihenfolge : Number | null = null;

	public gueltigVon : Number | null = null;

	public gueltigBis : Number | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.schild3.Schild3KatalogEintragPruefungsordnungOption'].includes(name);
	}

	public static transpilerFromJSON(json : string): Schild3KatalogEintragPruefungsordnungOption {
		const obj = JSON.parse(json);
		const result = new Schild3KatalogEintragPruefungsordnungOption();
		result.OP_Schulformen = typeof obj.OP_Schulformen === "undefined" ? null : obj.OP_Schulformen === null ? null : String(obj.OP_Schulformen);
		result.OP_POKrz = typeof obj.OP_POKrz === "undefined" ? null : obj.OP_POKrz === null ? null : String(obj.OP_POKrz);
		result.OP_Krz = typeof obj.OP_Krz === "undefined" ? null : obj.OP_Krz === null ? null : String(obj.OP_Krz);
		result.OP_Abgangsart_B = typeof obj.OP_Abgangsart_B === "undefined" ? null : obj.OP_Abgangsart_B === null ? null : String(obj.OP_Abgangsart_B);
		result.OP_Abgangsart_NB = typeof obj.OP_Abgangsart_NB === "undefined" ? null : obj.OP_Abgangsart_NB === null ? null : String(obj.OP_Abgangsart_NB);
		result.OP_Art = typeof obj.OP_Art === "undefined" ? null : obj.OP_Art === null ? null : String(obj.OP_Art);
		result.OP_Typ = typeof obj.OP_Typ === "undefined" ? null : obj.OP_Typ === null ? null : String(obj.OP_Typ);
		result.OP_Bildungsgang = typeof obj.OP_Bildungsgang === "undefined" ? null : obj.OP_Bildungsgang === null ? null : String(obj.OP_Bildungsgang);
		result.OP_Name = typeof obj.OP_Name === "undefined" ? null : obj.OP_Name === null ? null : String(obj.OP_Name);
		result.OP_Kommentar = typeof obj.OP_Kommentar === "undefined" ? null : obj.OP_Kommentar === null ? null : String(obj.OP_Kommentar);
		result.OP_Jahrgaenge = typeof obj.OP_Jahrgaenge === "undefined" ? null : obj.OP_Jahrgaenge === null ? null : String(obj.OP_Jahrgaenge);
		result.OP_BKIndex = typeof obj.OP_BKIndex === "undefined" ? null : obj.OP_BKIndex === null ? null : String(obj.OP_BKIndex);
		result.OP_BKAnl_Typ = typeof obj.OP_BKAnl_Typ === "undefined" ? null : obj.OP_BKAnl_Typ === null ? null : String(obj.OP_BKAnl_Typ);
		result.OP_Reihenfolge = typeof obj.OP_Reihenfolge === "undefined" ? null : obj.OP_Reihenfolge === null ? null : Number(obj.OP_Reihenfolge);
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : Number(obj.gueltigVon);
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : Number(obj.gueltigBis);
		return result;
	}

	public static transpilerToJSON(obj : Schild3KatalogEintragPruefungsordnungOption) : string {
		let result = '{';
		result += '"OP_Schulformen" : ' + ((!obj.OP_Schulformen) ? 'null' : '"' + obj.OP_Schulformen.valueOf() + '"') + ',';
		result += '"OP_POKrz" : ' + ((!obj.OP_POKrz) ? 'null' : '"' + obj.OP_POKrz.valueOf() + '"') + ',';
		result += '"OP_Krz" : ' + ((!obj.OP_Krz) ? 'null' : '"' + obj.OP_Krz.valueOf() + '"') + ',';
		result += '"OP_Abgangsart_B" : ' + ((!obj.OP_Abgangsart_B) ? 'null' : '"' + obj.OP_Abgangsart_B.valueOf() + '"') + ',';
		result += '"OP_Abgangsart_NB" : ' + ((!obj.OP_Abgangsart_NB) ? 'null' : '"' + obj.OP_Abgangsart_NB.valueOf() + '"') + ',';
		result += '"OP_Art" : ' + ((!obj.OP_Art) ? 'null' : '"' + obj.OP_Art.valueOf() + '"') + ',';
		result += '"OP_Typ" : ' + ((!obj.OP_Typ) ? 'null' : '"' + obj.OP_Typ.valueOf() + '"') + ',';
		result += '"OP_Bildungsgang" : ' + ((!obj.OP_Bildungsgang) ? 'null' : '"' + obj.OP_Bildungsgang.valueOf() + '"') + ',';
		result += '"OP_Name" : ' + ((!obj.OP_Name) ? 'null' : '"' + obj.OP_Name.valueOf() + '"') + ',';
		result += '"OP_Kommentar" : ' + ((!obj.OP_Kommentar) ? 'null' : '"' + obj.OP_Kommentar.valueOf() + '"') + ',';
		result += '"OP_Jahrgaenge" : ' + ((!obj.OP_Jahrgaenge) ? 'null' : '"' + obj.OP_Jahrgaenge.valueOf() + '"') + ',';
		result += '"OP_BKIndex" : ' + ((!obj.OP_BKIndex) ? 'null' : '"' + obj.OP_BKIndex.valueOf() + '"') + ',';
		result += '"OP_BKAnl_Typ" : ' + ((!obj.OP_BKAnl_Typ) ? 'null' : '"' + obj.OP_BKAnl_Typ.valueOf() + '"') + ',';
		result += '"OP_Reihenfolge" : ' + ((!obj.OP_Reihenfolge) ? 'null' : obj.OP_Reihenfolge.valueOf()) + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon.valueOf()) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis.valueOf()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Schild3KatalogEintragPruefungsordnungOption>) : string {
		let result = '{';
		if (typeof obj.OP_Schulformen !== "undefined") {
			result += '"OP_Schulformen" : ' + ((!obj.OP_Schulformen) ? 'null' : '"' + obj.OP_Schulformen.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_POKrz !== "undefined") {
			result += '"OP_POKrz" : ' + ((!obj.OP_POKrz) ? 'null' : '"' + obj.OP_POKrz.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Krz !== "undefined") {
			result += '"OP_Krz" : ' + ((!obj.OP_Krz) ? 'null' : '"' + obj.OP_Krz.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Abgangsart_B !== "undefined") {
			result += '"OP_Abgangsart_B" : ' + ((!obj.OP_Abgangsart_B) ? 'null' : '"' + obj.OP_Abgangsart_B.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Abgangsart_NB !== "undefined") {
			result += '"OP_Abgangsart_NB" : ' + ((!obj.OP_Abgangsart_NB) ? 'null' : '"' + obj.OP_Abgangsart_NB.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Art !== "undefined") {
			result += '"OP_Art" : ' + ((!obj.OP_Art) ? 'null' : '"' + obj.OP_Art.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Typ !== "undefined") {
			result += '"OP_Typ" : ' + ((!obj.OP_Typ) ? 'null' : '"' + obj.OP_Typ.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Bildungsgang !== "undefined") {
			result += '"OP_Bildungsgang" : ' + ((!obj.OP_Bildungsgang) ? 'null' : '"' + obj.OP_Bildungsgang.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Name !== "undefined") {
			result += '"OP_Name" : ' + ((!obj.OP_Name) ? 'null' : '"' + obj.OP_Name.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Kommentar !== "undefined") {
			result += '"OP_Kommentar" : ' + ((!obj.OP_Kommentar) ? 'null' : '"' + obj.OP_Kommentar.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Jahrgaenge !== "undefined") {
			result += '"OP_Jahrgaenge" : ' + ((!obj.OP_Jahrgaenge) ? 'null' : '"' + obj.OP_Jahrgaenge.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_BKIndex !== "undefined") {
			result += '"OP_BKIndex" : ' + ((!obj.OP_BKIndex) ? 'null' : '"' + obj.OP_BKIndex.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_BKAnl_Typ !== "undefined") {
			result += '"OP_BKAnl_Typ" : ' + ((!obj.OP_BKAnl_Typ) ? 'null' : '"' + obj.OP_BKAnl_Typ.valueOf() + '"') + ',';
		}
		if (typeof obj.OP_Reihenfolge !== "undefined") {
			result += '"OP_Reihenfolge" : ' + ((!obj.OP_Reihenfolge) ? 'null' : obj.OP_Reihenfolge.valueOf()) + ',';
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

export function cast_de_nrw_schule_svws_core_data_schild3_Schild3KatalogEintragPruefungsordnungOption(obj : unknown) : Schild3KatalogEintragPruefungsordnungOption {
	return obj as Schild3KatalogEintragPruefungsordnungOption;
}
