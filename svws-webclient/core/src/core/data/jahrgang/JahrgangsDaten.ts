import { JavaObject } from '../../../java/lang/JavaObject';

export class JahrgangsDaten extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public id : number = 0;

	/**
	 * Das Kürzel des Jahrgangs.
	 */
	public kuerzel : string | null = null;

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 */
	public kuerzelStatistik : string = "";

	/**
	 * Der Name / die Bezeichnung des Jahrgangs.
	 */
	public bezeichnung : string | null = null;

	/**
	 * Die Sortierreihenfolge des Jahrgangslisten-Eintrags.
	 */
	public sortierung : number = 0;

	/**
	 * Die ID der Schulgliederung, der der Eintrag zugeordnet ist.
	 */
	public kuerzelSchulgliederung : string | null = null;

	/**
	 * Die ID des Folgejahrgangs, sofern einer definiert ist, ansonsten null
	 */
	public idFolgejahrgang : number | null = null;

	/**
	 * Gibt die Anzahl der Restabschnitte bis zum Abschluss bei der Schulform an
	 */
	public anzahlRestabschnitte : number | null = null;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet von dem ersten Abschnitt an
	 */
	public gueltigVon : number | null = null;

	/**
	 * Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), null bedeutet bis zum letzten Abschnitt, Ende offen
	 */
	public gueltigBis : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.jahrgang.JahrgangsDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.jahrgang.JahrgangsDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): JahrgangsDaten {
		const obj = JSON.parse(json);
		const result = new JahrgangsDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = typeof obj.kuerzel === "undefined" ? null : obj.kuerzel === null ? null : obj.kuerzel;
		if (typeof obj.kuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		result.bezeichnung = typeof obj.bezeichnung === "undefined" ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.kuerzelSchulgliederung = typeof obj.kuerzelSchulgliederung === "undefined" ? null : obj.kuerzelSchulgliederung === null ? null : obj.kuerzelSchulgliederung;
		result.idFolgejahrgang = typeof obj.idFolgejahrgang === "undefined" ? null : obj.idFolgejahrgang === null ? null : obj.idFolgejahrgang;
		result.anzahlRestabschnitte = typeof obj.anzahlRestabschnitte === "undefined" ? null : obj.anzahlRestabschnitte === null ? null : obj.anzahlRestabschnitte;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		result.gueltigVon = typeof obj.gueltigVon === "undefined" ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = typeof obj.gueltigBis === "undefined" ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"kuerzelSchulgliederung" : ' + ((!obj.kuerzelSchulgliederung) ? 'null' : JSON.stringify(obj.kuerzelSchulgliederung)) + ',';
		result += '"idFolgejahrgang" : ' + ((!obj.idFolgejahrgang) ? 'null' : obj.idFolgejahrgang) + ',';
		result += '"anzahlRestabschnitte" : ' + ((!obj.anzahlRestabschnitte) ? 'null' : obj.anzahlRestabschnitte) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"gueltigVon" : ' + ((!obj.gueltigVon) ? 'null' : obj.gueltigVon) + ',';
		result += '"gueltigBis" : ' + ((!obj.gueltigBis) ? 'null' : obj.gueltigBis) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + ((!obj.kuerzel) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + ((!obj.bezeichnung) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.kuerzelSchulgliederung !== "undefined") {
			result += '"kuerzelSchulgliederung" : ' + ((!obj.kuerzelSchulgliederung) ? 'null' : JSON.stringify(obj.kuerzelSchulgliederung)) + ',';
		}
		if (typeof obj.idFolgejahrgang !== "undefined") {
			result += '"idFolgejahrgang" : ' + ((!obj.idFolgejahrgang) ? 'null' : obj.idFolgejahrgang) + ',';
		}
		if (typeof obj.anzahlRestabschnitte !== "undefined") {
			result += '"anzahlRestabschnitte" : ' + ((!obj.anzahlRestabschnitte) ? 'null' : obj.anzahlRestabschnitte) + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
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

export function cast_de_svws_nrw_core_data_jahrgang_JahrgangsDaten(obj : unknown) : JahrgangsDaten {
	return obj as JahrgangsDaten;
}
