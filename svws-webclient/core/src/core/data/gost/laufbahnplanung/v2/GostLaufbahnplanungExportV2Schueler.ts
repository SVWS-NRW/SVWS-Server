import { JavaObject } from '../../../../../java/lang/JavaObject';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostLaufbahnplanungExportV2SchuelerSprachpruefung } from '../../../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerSprachpruefung';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostLaufbahnplanungExportV2SchuelerFachbelegung } from '../../../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerFachbelegung';
import { GostLaufbahnplanungExportV2SchuelerSprachbelegung } from '../../../../../core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2SchuelerSprachbelegung';

export class GostLaufbahnplanungExportV2Schueler extends JavaObject {

	/**
	 * Die eindeutige ID des Schülers
	 */
	public id: number = 0;

	/**
	 * Die ID des Schüler verschlüsselt
	 */
	public idEnc: string = "";

	/**
	 * Das Jahr, in welchem der Schüler in die Gymnasiale Oberstufe in Bezug auf die Prüfungsordnung eingetreten ist
	 */
	public schuljahrEintrittOberstufe: number = -1;

	/**
	 * Der Vorname des Schülers
	 */
	public vorname: string = "";

	/**
	 * Der Nachname des Schülers
	 */
	public nachname: string = "";

	/**
	 * Das Geschlecht des Schülers
	 */
	public geschlecht: string = "";

	/**
	 * Gibt an, ob es sich um einen Export für einen Schüler in einem G8-Jahrgang handelt oder nicht.
	 */
	public istG8: boolean = false;

	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 */
	public bilingualeSprache: string | null = null;

	/**
	 * Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 */
	public readonly bewertetesHalbjahr: Array<boolean> = Array(6).fill(false);

	/**
	 * Die Informationen zu den Wahlen der Gleichwertigen komplexen Lernleistungen (ID oder null), jeweils für die Aufgabenfelder 1-3 in der EF (Index 0-2) und der Q-Phase (Index 3-5).
	 */
	public readonly gkl: Array<number | null> = Array(6).fill(null);

	/**
	 * Ein Array mit den Fachbelegungen in der Oberstufe.
	 */
	public readonly fachbelegungen: List<GostLaufbahnplanungExportV2SchuelerFachbelegung> = new ArrayList<GostLaufbahnplanungExportV2SchuelerFachbelegung>();

	/**
	 * Die Liste der Sprachbelegungen.
	 */
	public sprachbelegungen: List<GostLaufbahnplanungExportV2SchuelerSprachbelegung> = new ArrayList<GostLaufbahnplanungExportV2SchuelerSprachbelegung>();

	/**
	 * Die Liste der Sprachprüfungen.
	 */
	public sprachpruefungen: List<GostLaufbahnplanungExportV2SchuelerSprachpruefung> = new ArrayList<GostLaufbahnplanungExportV2SchuelerSprachpruefung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Schueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Schueler'].includes(name);
	}

	public static readonly class = new Class<GostLaufbahnplanungExportV2Schueler>('de.svws_nrw.core.data.gost.laufbahnplanung.v2.GostLaufbahnplanungExportV2Schueler');

	public static transpilerFromJSON(json: string): GostLaufbahnplanungExportV2Schueler {
		const obj = JSON.parse(json) as Partial<GostLaufbahnplanungExportV2Schueler>;
		const result = new GostLaufbahnplanungExportV2Schueler();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.idEnc === undefined)
			throw new Error('invalid json format, missing attribute idEnc');
		result.idEnc = obj.idEnc;
		if (obj.schuljahrEintrittOberstufe === undefined)
			throw new Error('invalid json format, missing attribute schuljahrEintrittOberstufe');
		result.schuljahrEintrittOberstufe = obj.schuljahrEintrittOberstufe;
		if (obj.vorname === undefined)
			throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (obj.nachname === undefined)
			throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (obj.geschlecht === undefined)
			throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		if (obj.istG8 === undefined)
			throw new Error('invalid json format, missing attribute istG8');
		result.istG8 = obj.istG8;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.bewertetesHalbjahr !== undefined) {
			for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
				result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
			}
		}
		if (obj.gkl !== undefined) {
			for (let i = 0; i < obj.gkl.length; i++) {
				result.gkl[i] = obj.gkl[i] === null ? null : obj.gkl[i];
			}
		}
		if (obj.fachbelegungen !== undefined) {
			for (const elem of obj.fachbelegungen) {
				result.fachbelegungen.add(GostLaufbahnplanungExportV2SchuelerFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sprachbelegungen !== undefined) {
			for (const elem of obj.sprachbelegungen) {
				result.sprachbelegungen.add(GostLaufbahnplanungExportV2SchuelerSprachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.sprachpruefungen !== undefined) {
			for (const elem of obj.sprachpruefungen) {
				result.sprachpruefungen.add(GostLaufbahnplanungExportV2SchuelerSprachpruefung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: GostLaufbahnplanungExportV2Schueler): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"idEnc" : ' + JSON.stringify(obj.idEnc) + ',';
		result += '"schuljahrEintrittOberstufe" : ' + obj.schuljahrEintrittOberstufe.toString() + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht) + ',';
		result += '"istG8" : ' + obj.istG8.toString() + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"bewertetesHalbjahr" : [ ';
		for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
			const elem = obj.bewertetesHalbjahr[i];
			result += JSON.stringify(elem);
			if (i < obj.bewertetesHalbjahr.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gkl" : [ ';
		for (let i = 0; i < obj.gkl.length; i++) {
			const elem = obj.gkl[i];
			result += (elem === null) ? null : elem.toString();
			if (i < obj.gkl.length - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"fachbelegungen" : [ ';
		for (let i = 0; i < obj.fachbelegungen.size(); i++) {
			const elem = obj.fachbelegungen.get(i);
			result += GostLaufbahnplanungExportV2SchuelerFachbelegung.transpilerToJSON(elem);
			if (i < obj.fachbelegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sprachbelegungen" : [ ';
		for (let i = 0; i < obj.sprachbelegungen.size(); i++) {
			const elem = obj.sprachbelegungen.get(i);
			result += GostLaufbahnplanungExportV2SchuelerSprachbelegung.transpilerToJSON(elem);
			if (i < obj.sprachbelegungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"sprachpruefungen" : [ ';
		for (let i = 0; i < obj.sprachpruefungen.size(); i++) {
			const elem = obj.sprachpruefungen.get(i);
			result += GostLaufbahnplanungExportV2SchuelerSprachpruefung.transpilerToJSON(elem);
			if (i < obj.sprachpruefungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<GostLaufbahnplanungExportV2Schueler>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.idEnc !== undefined) {
			result += '"idEnc" : ' + JSON.stringify(obj.idEnc) + ',';
		}
		if (obj.schuljahrEintrittOberstufe !== undefined) {
			result += '"schuljahrEintrittOberstufe" : ' + obj.schuljahrEintrittOberstufe.toString() + ',';
		}
		if (obj.vorname !== undefined) {
			result += '"vorname" : ' + JSON.stringify(obj.vorname) + ',';
		}
		if (obj.nachname !== undefined) {
			result += '"nachname" : ' + JSON.stringify(obj.nachname) + ',';
		}
		if (obj.geschlecht !== undefined) {
			result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht) + ',';
		}
		if (obj.istG8 !== undefined) {
			result += '"istG8" : ' + obj.istG8.toString() + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.bewertetesHalbjahr !== undefined) {
			const a = obj.bewertetesHalbjahr;
			result += '"bewertetesHalbjahr" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += JSON.stringify(elem);
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gkl !== undefined) {
			const a = obj.gkl;
			result += '"gkl" : [ ';
			for (let i = 0; i < a.length; i++) {
				const elem = a[i];
				result += (elem === null) ? null : elem.toString();
				if (i < a.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.fachbelegungen !== undefined) {
			result += '"fachbelegungen" : [ ';
			for (let i = 0; i < obj.fachbelegungen.size(); i++) {
				const elem = obj.fachbelegungen.get(i);
				result += GostLaufbahnplanungExportV2SchuelerFachbelegung.transpilerToJSON(elem);
				if (i < obj.fachbelegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sprachbelegungen !== undefined) {
			result += '"sprachbelegungen" : [ ';
			for (let i = 0; i < obj.sprachbelegungen.size(); i++) {
				const elem = obj.sprachbelegungen.get(i);
				result += GostLaufbahnplanungExportV2SchuelerSprachbelegung.transpilerToJSON(elem);
				if (i < obj.sprachbelegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.sprachpruefungen !== undefined) {
			result += '"sprachpruefungen" : [ ';
			for (let i = 0; i < obj.sprachpruefungen.size(); i++) {
				const elem = obj.sprachpruefungen.get(i);
				result += GostLaufbahnplanungExportV2SchuelerSprachpruefung.transpilerToJSON(elem);
				if (i < obj.sprachpruefungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_laufbahnplanung_v2_GostLaufbahnplanungExportV2Schueler(obj: unknown): GostLaufbahnplanungExportV2Schueler {
	return obj as GostLaufbahnplanungExportV2Schueler;
}
