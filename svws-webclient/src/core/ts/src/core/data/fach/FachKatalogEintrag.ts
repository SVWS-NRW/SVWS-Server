import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { SchulformSchulgliederung, cast_de_nrw_schule_svws_core_data_schule_SchulformSchulgliederung } from '../../../core/data/schule/SchulformSchulgliederung';
import { Jahrgaenge, cast_de_nrw_schule_svws_core_types_jahrgang_Jahrgaenge } from '../../../core/types/jahrgang/Jahrgaenge';
import { Fachgruppe, cast_de_nrw_schule_svws_core_types_fach_Fachgruppe } from '../../../core/types/fach/Fachgruppe';
import { Schulform, cast_de_nrw_schule_svws_core_types_schule_Schulform } from '../../../core/types/schule/Schulform';
import { Schulgliederung, cast_de_nrw_schule_svws_core_types_schule_Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { List, cast_java_util_List } from '../../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../../java/util/Vector';
import { Pair, cast_de_nrw_schule_svws_core_adt_Pair } from '../../../core/adt/Pair';

export class FachKatalogEintrag extends JavaObject {

	/**
	 * Die ID des Katalog-Eintrags.
	 */
	public id : number = -1;

	/**
	 * Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 */
	public kuerzelASD : string = "";

	/**
	 * Die texttuelle Beschreibung des Faches
	 */
	public bezeichnung : string = "";

	/**
	 * Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik
	 */
	public kuerzel : string = "";

	/**
	 * Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 */
	public aufgabenfeld : number | null = -1;

	/**
	 * Das Kürzel der zugeordneten Fachgruppe
	 */
	public fachgruppe : string | null = "";

	/**
	 * Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 */
	public abJahrgang : string | null = "";

	/**
	 * Gibt an, ob es sich um eine Fremdsprache handelt
	 */
	public istFremdsprache : boolean = false;

	/**
	 * Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache)
	 */
	public istHKFS : boolean = false;

	/**
	 * Gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird.
	 */
	public istAusRegUFach : boolean = false;

	/**
	 * Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS)
	 */
	public istErsatzPflichtFS : boolean = false;

	/**
	 * Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik
	 */
	public istKonfKoop : boolean = false;

	/**
	 * Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird.
	 */
	public nurSII : boolean = false;

	/**
	 * Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht.
	 */
	public exportASD : boolean = false;

	/**
	 * Die Informationen zu Schulformen und -gliederungen, wo das Fach zulässig ist.
	 */
	public zulaessig : List<SchulformSchulgliederung> = new Vector();

	/**
	 * Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	public gueltigBis : number | null = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public constructor();

	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id                    die ID
	 * @param kuerzelASD            das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik
	 * @param bezeichnung           die texttuelle Beschreibung des Faches
	 * @param kuerzel               das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel)
	 *                              - Teil des Kürzels für die amtliche Schulstatistik
	 * @param aufgabenfeld          das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3)
	 * @param fachgruppe            das Kürzel der zugeordneten Fachgruppe
	 * @param abJahrgang            der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen)
	 * @param istFremdsprache       gibt an, ob es sich um eine Fremdsprache handelt
	 * @param istHKFS               gibt an, ob es sich um ein Fach der Herkuntftsprache handelt
	 *                              (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer
	 *                              Pflichtfremdsprache)
	 * @param istAusRegUFach        gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird
	 * @param istErsatzPflichtFS    gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt
	 *                              (siehe auch istHKFS)
	 * @param istKonfKoop           gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht
	 *                              - Teil des Kürzels für die amtliche Schulstatistik
	 * @param nurSII                gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird
	 * @param exportASD             gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden
	 *                              soll oder nicht
	 * @param zulaessig             die Informationen zu Schulformen und -gliederungen, wo das Fach zulässig ist
	 * @param gueltigVon            das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und
	 *                              "schon immer gültig war"
	 * @param gueltigBis            das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public constructor(id : number, kuerzelASD : string, bezeichnung : string, kuerzel : string, aufgabenfeld : number | null, fachgruppe : Fachgruppe | null, abJahrgang : Jahrgaenge | null, istFremdsprache : boolean, istHKFS : boolean, istAusRegUFach : boolean, istErsatzPflichtFS : boolean, istKonfKoop : boolean, nurSII : boolean, exportASD : boolean, zulaessig : List<Pair<Schulform, Schulgliederung | null>>, gueltigVon : number | null, gueltigBis : number | null);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : number, __param1? : string, __param2? : string, __param3? : string, __param4? : null | number, __param5? : Fachgruppe | null, __param6? : Jahrgaenge | null, __param7? : boolean, __param8? : boolean, __param9? : boolean, __param10? : boolean, __param11? : boolean, __param12? : boolean, __param13? : boolean, __param14? : List<Pair<Schulform, Schulgliederung | null>>, __param15? : null | number, __param16? : null | number) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined") && (typeof __param2 === "undefined") && (typeof __param3 === "undefined") && (typeof __param4 === "undefined") && (typeof __param5 === "undefined") && (typeof __param6 === "undefined") && (typeof __param7 === "undefined") && (typeof __param8 === "undefined") && (typeof __param9 === "undefined") && (typeof __param10 === "undefined") && (typeof __param11 === "undefined") && (typeof __param12 === "undefined") && (typeof __param13 === "undefined") && (typeof __param14 === "undefined") && (typeof __param15 === "undefined") && (typeof __param16 === "undefined")) {
			// empty method body
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "number") && ((typeof __param1 !== "undefined") && (typeof __param1 === "string")) && ((typeof __param2 !== "undefined") && (typeof __param2 === "string")) && ((typeof __param3 !== "undefined") && (typeof __param3 === "string")) && ((typeof __param4 !== "undefined") && (typeof __param4 === "number") || (__param4 === null)) && ((typeof __param5 !== "undefined") && ((__param5 instanceof JavaObject) && (__param5.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.fach.Fachgruppe'))) || (__param5 === null)) && ((typeof __param6 !== "undefined") && ((__param6 instanceof JavaObject) && (__param6.isTranspiledInstanceOf('de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge'))) || (__param6 === null)) && ((typeof __param7 !== "undefined") && typeof __param7 === "boolean") && ((typeof __param8 !== "undefined") && typeof __param8 === "boolean") && ((typeof __param9 !== "undefined") && typeof __param9 === "boolean") && ((typeof __param10 !== "undefined") && typeof __param10 === "boolean") && ((typeof __param11 !== "undefined") && typeof __param11 === "boolean") && ((typeof __param12 !== "undefined") && typeof __param12 === "boolean") && ((typeof __param13 !== "undefined") && typeof __param13 === "boolean") && ((typeof __param14 !== "undefined") && ((__param14 instanceof JavaObject) && (__param14.isTranspiledInstanceOf('java.util.List'))) || (__param14 === null)) && ((typeof __param15 !== "undefined") && (typeof __param15 === "number") || (__param15 === null)) && ((typeof __param16 !== "undefined") && (typeof __param16 === "number") || (__param16 === null))) {
			let id : number = __param0 as number;
			let kuerzelASD : string = __param1;
			let bezeichnung : string = __param2;
			let kuerzel : string = __param3;
			let aufgabenfeld : number | null = __param4;
			let fachgruppe : Fachgruppe | null = cast_de_nrw_schule_svws_core_types_fach_Fachgruppe(__param5);
			let abJahrgang : Jahrgaenge | null = cast_de_nrw_schule_svws_core_types_jahrgang_Jahrgaenge(__param6);
			let istFremdsprache : boolean = __param7 as boolean;
			let istHKFS : boolean = __param8 as boolean;
			let istAusRegUFach : boolean = __param9 as boolean;
			let istErsatzPflichtFS : boolean = __param10 as boolean;
			let istKonfKoop : boolean = __param11 as boolean;
			let nurSII : boolean = __param12 as boolean;
			let exportASD : boolean = __param13 as boolean;
			let zulaessig : List<Pair<Schulform, Schulgliederung | null>> = cast_java_util_List(__param14);
			let gueltigVon : number | null = __param15;
			let gueltigBis : number | null = __param16;
			this.id = id;
			this.kuerzelASD = kuerzelASD;
			this.bezeichnung = bezeichnung;
			this.kuerzel = kuerzel;
			this.aufgabenfeld = aufgabenfeld;
			this.fachgruppe = (fachgruppe === null) ? null : fachgruppe.daten.kuerzel;
			this.abJahrgang = (abJahrgang === null) ? null : abJahrgang.daten.kuerzel;
			this.istFremdsprache = istFremdsprache;
			this.istHKFS = istHKFS;
			this.istAusRegUFach = istAusRegUFach;
			this.istErsatzPflichtFS = istErsatzPflichtFS;
			this.istKonfKoop = istKonfKoop;
			this.nurSII = nurSII;
			this.exportASD = exportASD;
			for (let zul of zulaessig) {
				let sfsgl : SchulformSchulgliederung | null = new SchulformSchulgliederung();
				let sf : Schulform = zul.a;
				if (sf.daten === null)
					continue;
				sfsgl.schulform = sf.daten.kuerzel;
				let sgl : Schulgliederung | null = zul.b;
				sfsgl.gliederung = ((sgl === null) || (sgl.daten === null)) ? null : sgl.daten.kuerzel;
				this.zulaessig.add(sfsgl);
			}
			this.gueltigVon = gueltigVon;
			this.gueltigBis = gueltigBis;
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.data.fach.FachKatalogEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): FachKatalogEintrag {
		const obj = JSON.parse(json);
		const result = new FachKatalogEintrag();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzelASD === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelASD');
		result.kuerzelASD = obj.kuerzelASD;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		result.aufgabenfeld = typeof obj.aufgabenfeld === "undefined" ? null : obj.aufgabenfeld === null ? null : obj.aufgabenfeld;
		result.fachgruppe = typeof obj.fachgruppe === "undefined" ? null : obj.fachgruppe === null ? null : obj.fachgruppe;
		result.abJahrgang = typeof obj.abJahrgang === "undefined" ? null : obj.abJahrgang === null ? null : obj.abJahrgang;
		if (typeof obj.istFremdsprache === "undefined")
			 throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (typeof obj.istHKFS === "undefined")
			 throw new Error('invalid json format, missing attribute istHKFS');
		result.istHKFS = obj.istHKFS;
		if (typeof obj.istAusRegUFach === "undefined")
			 throw new Error('invalid json format, missing attribute istAusRegUFach');
		result.istAusRegUFach = obj.istAusRegUFach;
		if (typeof obj.istErsatzPflichtFS === "undefined")
			 throw new Error('invalid json format, missing attribute istErsatzPflichtFS');
		result.istErsatzPflichtFS = obj.istErsatzPflichtFS;
		if (typeof obj.istKonfKoop === "undefined")
			 throw new Error('invalid json format, missing attribute istKonfKoop');
		result.istKonfKoop = obj.istKonfKoop;
		if (typeof obj.nurSII === "undefined")
			 throw new Error('invalid json format, missing attribute nurSII');
		result.nurSII = obj.nurSII;
		if (typeof obj.exportASD === "undefined")
			 throw new Error('invalid json format, missing attribute exportASD');
		result.exportASD = obj.exportASD;
		if ((obj.zulaessig !== undefined) && (obj.zulaessig !== null)) {
			for (const elem of obj.zulaessig) {
				result.zulaessig?.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : FachKatalogEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzelASD" : ' + '"' + obj.kuerzelASD! + '"' + ',';
		result += '"bezeichnung" : ' + '"' + obj.bezeichnung! + '"' + ',';
		result += '"kuerzel" : ' + '"' + obj.kuerzel! + '"' + ',';
		result += '"aufgabenfeld" : ' + ((!obj.aufgabenfeld) ? 'null' : obj.aufgabenfeld) + ',';
		result += '"fachgruppe" : ' + ((!obj.fachgruppe) ? 'null' : '"' + obj.fachgruppe + '"') + ',';
		result += '"abJahrgang" : ' + ((!obj.abJahrgang) ? 'null' : '"' + obj.abJahrgang + '"') + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		result += '"istHKFS" : ' + obj.istHKFS + ',';
		result += '"istAusRegUFach" : ' + obj.istAusRegUFach + ',';
		result += '"istErsatzPflichtFS" : ' + obj.istErsatzPflichtFS + ',';
		result += '"istKonfKoop" : ' + obj.istKonfKoop + ',';
		result += '"nurSII" : ' + obj.nurSII + ',';
		result += '"exportASD" : ' + obj.exportASD + ',';
		if (!obj.zulaessig) {
			result += '"zulaessig" : []';
		} else {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachKatalogEintrag>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzelASD !== "undefined") {
			result += '"kuerzelASD" : ' + '"' + obj.kuerzelASD + '"' + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + '"' + obj.bezeichnung + '"' + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + '"' + obj.kuerzel + '"' + ',';
		}
		if (typeof obj.aufgabenfeld !== "undefined") {
			result += '"aufgabenfeld" : ' + ((!obj.aufgabenfeld) ? 'null' : obj.aufgabenfeld) + ',';
		}
		if (typeof obj.fachgruppe !== "undefined") {
			result += '"fachgruppe" : ' + ((!obj.fachgruppe) ? 'null' : '"' + obj.fachgruppe + '"') + ',';
		}
		if (typeof obj.abJahrgang !== "undefined") {
			result += '"abJahrgang" : ' + ((!obj.abJahrgang) ? 'null' : '"' + obj.abJahrgang + '"') + ',';
		}
		if (typeof obj.istFremdsprache !== "undefined") {
			result += '"istFremdsprache" : ' + obj.istFremdsprache + ',';
		}
		if (typeof obj.istHKFS !== "undefined") {
			result += '"istHKFS" : ' + obj.istHKFS + ',';
		}
		if (typeof obj.istAusRegUFach !== "undefined") {
			result += '"istAusRegUFach" : ' + obj.istAusRegUFach + ',';
		}
		if (typeof obj.istErsatzPflichtFS !== "undefined") {
			result += '"istErsatzPflichtFS" : ' + obj.istErsatzPflichtFS + ',';
		}
		if (typeof obj.istKonfKoop !== "undefined") {
			result += '"istKonfKoop" : ' + obj.istKonfKoop + ',';
		}
		if (typeof obj.nurSII !== "undefined") {
			result += '"nurSII" : ' + obj.nurSII + ',';
		}
		if (typeof obj.exportASD !== "undefined") {
			result += '"exportASD" : ' + obj.exportASD + ',';
		}
		if (typeof obj.zulaessig !== "undefined") {
			if (!obj.zulaessig) {
				result += '"zulaessig" : []';
			} else {
				result += '"zulaessig" : [ ';
				for (let i = 0; i < obj.zulaessig.size(); i++) {
					const elem = obj.zulaessig.get(i);
					result += SchulformSchulgliederung.transpilerToJSON(elem);
					if (i < obj.zulaessig.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.gueltigVon !== "undefined") {
			result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		}
		if (typeof obj.gueltigBis !== "undefined") {
			result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_fach_FachKatalogEintrag(obj : unknown) : FachKatalogEintrag {
	return obj as FachKatalogEintrag;
}
