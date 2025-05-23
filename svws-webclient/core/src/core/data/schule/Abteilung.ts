import { JavaObject } from '../../../java/lang/JavaObject';
import { AbteilungKlassenzuordnung } from '../../../core/data/schule/AbteilungKlassenzuordnung';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Abteilung extends JavaObject {

	/**
	 * Die ID des Eintrags für die Abteilung
	 */
	public id : number = -1;

	/**
	 * Die Bezeichnung der Abteilung (max. 50 Zeichen)
	 */
	public bezeichnung : string = "";

	/**
	 * Die ID des Schuljahresabschnitts für den die Abteilung definiert ist.
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat.
	 */
	public idAbteilungsleiter : number | null = null;

	/**
	 * Die Bezeichnung des Raums des Abteilungsleiters (z.B. für Briefköpfe, max. 20 Zeichen)
	 */
	public raum : string | null = null;

	/**
	 * Die eMail-Adresse des Abteilungsleiters (max. 100 Zeichen)
	 */
	public email : string | null = null;

	/**
	 * Die interne telefonische Durchwahl des Abteilungsleiters
	 */
	public durchwahl : string | null = null;

	/**
	 * Gibt einen Wert für die Sortierung der Abteilungen an.
	 */
	public sortierung : number = 32000;

	/**
	 * Die Zuordnung der Klassen zu der Abteilung.
	 */
	public readonly klassenzuordnungen : List<AbteilungKlassenzuordnung> = new ArrayList<AbteilungKlassenzuordnung>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schule.Abteilung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schule.Abteilung'].includes(name);
	}

	public static class = new Class<Abteilung>('de.svws_nrw.core.data.schule.Abteilung');

	public static transpilerFromJSON(json : string): Abteilung {
		const obj = JSON.parse(json) as Partial<Abteilung>;
		const result = new Abteilung();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.idSchuljahresabschnitt === undefined)
			throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.idAbteilungsleiter = (obj.idAbteilungsleiter === undefined) ? null : obj.idAbteilungsleiter === null ? null : obj.idAbteilungsleiter;
		result.raum = (obj.raum === undefined) ? null : obj.raum === null ? null : obj.raum;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.durchwahl = (obj.durchwahl === undefined) ? null : obj.durchwahl === null ? null : obj.durchwahl;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.klassenzuordnungen !== undefined) {
			for (const elem of obj.klassenzuordnungen) {
				result.klassenzuordnungen.add(AbteilungKlassenzuordnung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : Abteilung) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		result += '"idAbteilungsleiter" : ' + ((obj.idAbteilungsleiter === null) ? 'null' : obj.idAbteilungsleiter.toString()) + ',';
		result += '"raum" : ' + ((obj.raum === null) ? 'null' : JSON.stringify(obj.raum)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"durchwahl" : ' + ((obj.durchwahl === null) ? 'null' : JSON.stringify(obj.durchwahl)) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"klassenzuordnungen" : [ ';
		for (let i = 0; i < obj.klassenzuordnungen.size(); i++) {
			const elem = obj.klassenzuordnungen.get(i);
			result += AbteilungKlassenzuordnung.transpilerToJSON(elem);
			if (i < obj.klassenzuordnungen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<Abteilung>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.idSchuljahresabschnitt !== undefined) {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt.toString() + ',';
		}
		if (obj.idAbteilungsleiter !== undefined) {
			result += '"idAbteilungsleiter" : ' + ((obj.idAbteilungsleiter === null) ? 'null' : obj.idAbteilungsleiter.toString()) + ',';
		}
		if (obj.raum !== undefined) {
			result += '"raum" : ' + ((obj.raum === null) ? 'null' : JSON.stringify(obj.raum)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.durchwahl !== undefined) {
			result += '"durchwahl" : ' + ((obj.durchwahl === null) ? 'null' : JSON.stringify(obj.durchwahl)) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.klassenzuordnungen !== undefined) {
			result += '"klassenzuordnungen" : [ ';
			for (let i = 0; i < obj.klassenzuordnungen.size(); i++) {
				const elem = obj.klassenzuordnungen.get(i);
				result += AbteilungKlassenzuordnung.transpilerToJSON(elem);
				if (i < obj.klassenzuordnungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schule_Abteilung(obj : unknown) : Abteilung {
	return obj as Abteilung;
}
