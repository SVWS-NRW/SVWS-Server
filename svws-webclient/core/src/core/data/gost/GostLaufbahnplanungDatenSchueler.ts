import { JavaObject } from '../../../java/lang/JavaObject';
import { GostLaufbahnplanungDatenFachbelegung } from '../../../core/data/gost/GostLaufbahnplanungDatenFachbelegung';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';

export class GostLaufbahnplanungDatenSchueler extends JavaObject {

	/**
	 * Die eindeutige ID des Schülers
	 */
	public id : number = 0;

	/**
	 * Die ID des Schüler verschlüsselt
	 */
	public idEnc : string = "";

	/**
	 * Der Vorname des Schülers
	 */
	public vorname : string = "";

	/**
	 * Der Nachname des Schülers
	 */
	public nachname : string = "";

	/**
	 * Das Geschlecht des Schülers
	 */
	public geschlecht : string = "";

	/**
	 * Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt.
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt.
	 */
	public readonly bewertetesHalbjahr : Array<boolean> = Array(6).fill(false);

	/**
	 * Ein Array mit den Fachbelegungen in der Oberstufe.
	 */
	public readonly fachbelegungen : List<GostLaufbahnplanungDatenFachbelegung> = new ArrayList();

	/**
	 * Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen.
	 */
	public sprachendaten : Sprachendaten = new Sprachendaten();


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenSchueler';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.gost.GostLaufbahnplanungDatenSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): GostLaufbahnplanungDatenSchueler {
		const obj = JSON.parse(json);
		const result = new GostLaufbahnplanungDatenSchueler();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idEnc === "undefined")
			 throw new Error('invalid json format, missing attribute idEnc');
		result.idEnc = obj.idEnc;
		if (typeof obj.vorname === "undefined")
			 throw new Error('invalid json format, missing attribute vorname');
		result.vorname = obj.vorname;
		if (typeof obj.nachname === "undefined")
			 throw new Error('invalid json format, missing attribute nachname');
		result.nachname = obj.nachname;
		if (typeof obj.geschlecht === "undefined")
			 throw new Error('invalid json format, missing attribute geschlecht');
		result.geschlecht = obj.geschlecht;
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
			result.bewertetesHalbjahr[i] = obj.bewertetesHalbjahr[i];
		}
		if ((obj.fachbelegungen !== undefined) && (obj.fachbelegungen !== null)) {
			for (const elem of obj.fachbelegungen) {
				result.fachbelegungen?.add(GostLaufbahnplanungDatenFachbelegung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (typeof obj.sprachendaten === "undefined")
			 throw new Error('invalid json format, missing attribute sprachendaten');
		result.sprachendaten = Sprachendaten.transpilerFromJSON(JSON.stringify(obj.sprachendaten));
		return result;
	}

	public static transpilerToJSON(obj : GostLaufbahnplanungDatenSchueler) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idEnc" : ' + JSON.stringify(obj.idEnc!) + ',';
		result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht!) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		if (!obj.bewertetesHalbjahr) {
			result += '"bewertetesHalbjahr" : []';
		} else {
			result += '"bewertetesHalbjahr" : [ ';
			for (let i = 0; i < obj.bewertetesHalbjahr.length; i++) {
				const elem = obj.bewertetesHalbjahr[i];
				result += JSON.stringify(elem);
				if (i < obj.bewertetesHalbjahr.length - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachbelegungen) {
			result += '"fachbelegungen" : []';
		} else {
			result += '"fachbelegungen" : [ ';
			for (let i = 0; i < obj.fachbelegungen.size(); i++) {
				const elem = obj.fachbelegungen.get(i);
				result += GostLaufbahnplanungDatenFachbelegung.transpilerToJSON(elem);
				if (i < obj.fachbelegungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<GostLaufbahnplanungDatenSchueler>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idEnc !== "undefined") {
			result += '"idEnc" : ' + JSON.stringify(obj.idEnc!) + ',';
		}
		if (typeof obj.vorname !== "undefined") {
			result += '"vorname" : ' + JSON.stringify(obj.vorname!) + ',';
		}
		if (typeof obj.nachname !== "undefined") {
			result += '"nachname" : ' + JSON.stringify(obj.nachname!) + ',';
		}
		if (typeof obj.geschlecht !== "undefined") {
			result += '"geschlecht" : ' + JSON.stringify(obj.geschlecht!) + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (typeof obj.bewertetesHalbjahr !== "undefined") {
			const a = obj.bewertetesHalbjahr;
			if (!a) {
				result += '"bewertetesHalbjahr" : []';
			} else {
				result += '"bewertetesHalbjahr" : [ ';
				for (let i = 0; i < a.length; i++) {
					const elem = a[i];
					result += JSON.stringify(elem);
					if (i < a.length - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachbelegungen !== "undefined") {
			if (!obj.fachbelegungen) {
				result += '"fachbelegungen" : []';
			} else {
				result += '"fachbelegungen" : [ ';
				for (let i = 0; i < obj.fachbelegungen.size(); i++) {
					const elem = obj.fachbelegungen.get(i);
					result += GostLaufbahnplanungDatenFachbelegung.transpilerToJSON(elem);
					if (i < obj.fachbelegungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.sprachendaten !== "undefined") {
			result += '"sprachendaten" : ' + Sprachendaten.transpilerToJSON(obj.sprachendaten) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_gost_GostLaufbahnplanungDatenSchueler(obj : unknown) : GostLaufbahnplanungDatenSchueler {
	return obj as GostLaufbahnplanungDatenSchueler;
}
