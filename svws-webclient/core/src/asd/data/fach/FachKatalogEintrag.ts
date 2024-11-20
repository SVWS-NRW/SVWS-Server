import { SchulformSchulgliederung } from '../../../asd/data/schule/SchulformSchulgliederung';
import { CoreTypeDataNurSchulformenUndSchulgliederungen } from '../../../asd/data/CoreTypeDataNurSchulformenUndSchulgliederungen';
import { Class } from '../../../java/lang/Class';

export class FachKatalogEintrag extends CoreTypeDataNurSchulformenUndSchulgliederungen {

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
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.data.fach.FachKatalogEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.data.CoreTypeDataNurSchulformenUndSchulgliederungen', 'de.svws_nrw.asd.data.fach.FachKatalogEintrag', 'de.svws_nrw.asd.data.CoreTypeData'].includes(name);
	}

	public static class = new Class<FachKatalogEintrag>('de.svws_nrw.asd.data.fach.FachKatalogEintrag');

	public static transpilerFromJSON(json : string): FachKatalogEintrag {
		const obj = JSON.parse(json) as Partial<FachKatalogEintrag>;
		const result = new FachKatalogEintrag();
		if (obj.zulaessig !== undefined) {
			for (const elem of obj.zulaessig) {
				result.zulaessig.add(SchulformSchulgliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
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
		result.aufgabenfeld = (obj.aufgabenfeld === undefined) ? null : obj.aufgabenfeld === null ? null : obj.aufgabenfeld;
		result.fachgruppe = (obj.fachgruppe === undefined) ? null : obj.fachgruppe === null ? null : obj.fachgruppe;
		result.abJahrgang = (obj.abJahrgang === undefined) ? null : obj.abJahrgang === null ? null : obj.abJahrgang;
		if (obj.istFremdsprache === undefined)
			throw new Error('invalid json format, missing attribute istFremdsprache');
		result.istFremdsprache = obj.istFremdsprache;
		if (obj.istHKFS === undefined)
			throw new Error('invalid json format, missing attribute istHKFS');
		result.istHKFS = obj.istHKFS;
		if (obj.istAusRegUFach === undefined)
			throw new Error('invalid json format, missing attribute istAusRegUFach');
		result.istAusRegUFach = obj.istAusRegUFach;
		if (obj.istErsatzPflichtFS === undefined)
			throw new Error('invalid json format, missing attribute istErsatzPflichtFS');
		result.istErsatzPflichtFS = obj.istErsatzPflichtFS;
		if (obj.istKonfKoop === undefined)
			throw new Error('invalid json format, missing attribute istKonfKoop');
		result.istKonfKoop = obj.istKonfKoop;
		if (obj.nurSII === undefined)
			throw new Error('invalid json format, missing attribute nurSII');
		result.nurSII = obj.nurSII;
		if (obj.exportASD === undefined)
			throw new Error('invalid json format, missing attribute exportASD');
		result.exportASD = obj.exportASD;
		return result;
	}

	public static transpilerToJSON(obj : FachKatalogEintrag) : string {
		let result = '{';
		result += '"zulaessig" : [ ';
		for (let i = 0; i < obj.zulaessig.size(); i++) {
			const elem = obj.zulaessig.get(i);
			result += SchulformSchulgliederung.transpilerToJSON(elem);
			if (i < obj.zulaessig.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"schluessel" : ' + JSON.stringify(obj.schluessel) + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"text" : ' + JSON.stringify(obj.text) + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"aufgabenfeld" : ' + ((obj.aufgabenfeld === null) ? 'null' : obj.aufgabenfeld.toString()) + ',';
		result += '"fachgruppe" : ' + ((obj.fachgruppe === null) ? 'null' : JSON.stringify(obj.fachgruppe)) + ',';
		result += '"abJahrgang" : ' + ((obj.abJahrgang === null) ? 'null' : JSON.stringify(obj.abJahrgang)) + ',';
		result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		result += '"istHKFS" : ' + obj.istHKFS.toString() + ',';
		result += '"istAusRegUFach" : ' + obj.istAusRegUFach.toString() + ',';
		result += '"istErsatzPflichtFS" : ' + obj.istErsatzPflichtFS.toString() + ',';
		result += '"istKonfKoop" : ' + obj.istKonfKoop.toString() + ',';
		result += '"nurSII" : ' + obj.nurSII.toString() + ',';
		result += '"exportASD" : ' + obj.exportASD.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachKatalogEintrag>) : string {
		let result = '{';
		if (obj.zulaessig !== undefined) {
			result += '"zulaessig" : [ ';
			for (let i = 0; i < obj.zulaessig.size(); i++) {
				const elem = obj.zulaessig.get(i);
				result += SchulformSchulgliederung.transpilerToJSON(elem);
				if (i < obj.zulaessig.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
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
		if (obj.aufgabenfeld !== undefined) {
			result += '"aufgabenfeld" : ' + ((obj.aufgabenfeld === null) ? 'null' : obj.aufgabenfeld.toString()) + ',';
		}
		if (obj.fachgruppe !== undefined) {
			result += '"fachgruppe" : ' + ((obj.fachgruppe === null) ? 'null' : JSON.stringify(obj.fachgruppe)) + ',';
		}
		if (obj.abJahrgang !== undefined) {
			result += '"abJahrgang" : ' + ((obj.abJahrgang === null) ? 'null' : JSON.stringify(obj.abJahrgang)) + ',';
		}
		if (obj.istFremdsprache !== undefined) {
			result += '"istFremdsprache" : ' + obj.istFremdsprache.toString() + ',';
		}
		if (obj.istHKFS !== undefined) {
			result += '"istHKFS" : ' + obj.istHKFS.toString() + ',';
		}
		if (obj.istAusRegUFach !== undefined) {
			result += '"istAusRegUFach" : ' + obj.istAusRegUFach.toString() + ',';
		}
		if (obj.istErsatzPflichtFS !== undefined) {
			result += '"istErsatzPflichtFS" : ' + obj.istErsatzPflichtFS.toString() + ',';
		}
		if (obj.istKonfKoop !== undefined) {
			result += '"istKonfKoop" : ' + obj.istKonfKoop.toString() + ',';
		}
		if (obj.nurSII !== undefined) {
			result += '"nurSII" : ' + obj.nurSII.toString() + ',';
		}
		if (obj.exportASD !== undefined) {
			result += '"exportASD" : ' + obj.exportASD.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_asd_data_fach_FachKatalogEintrag(obj : unknown) : FachKatalogEintrag {
	return obj as FachKatalogEintrag;
}
