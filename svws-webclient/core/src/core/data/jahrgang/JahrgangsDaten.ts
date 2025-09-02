import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class JahrgangsDaten extends JavaObject {

	/**
	 * Die ID des Jahrgangs.
	 */
	public id : number = 0;

	/**
	 * Das schulinterne Kürzel des Jahrgangs.
	 */
	public kuerzel : string | null = null;

	/**
	 * Die schulinterne Kurzbezeichnung
	 */
	public kurzbezeichnung : string | null = null;

	/**
	 * Das dem Jahrgang zugeordnete Statistik-Kürzel.
	 */
	public kuerzelStatistik : string | null = null;

	/**
	 * Die dem Jahrgang zugeordnete schulinterne Bezeichnung.
	 */
	public bezeichnung : string = "";

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

	/**
	 * Gibt an, ob der Jahrgang in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen : boolean | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.jahrgang.JahrgangsDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.jahrgang.JahrgangsDaten'].includes(name);
	}

	public static class = new Class<JahrgangsDaten>('de.svws_nrw.core.data.jahrgang.JahrgangsDaten');

	public static transpilerFromJSON(json : string): JahrgangsDaten {
		const obj = JSON.parse(json) as Partial<JahrgangsDaten>;
		const result = new JahrgangsDaten();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.kuerzel = (obj.kuerzel === undefined) ? null : obj.kuerzel === null ? null : obj.kuerzel;
		result.kurzbezeichnung = (obj.kurzbezeichnung === undefined) ? null : obj.kurzbezeichnung === null ? null : obj.kurzbezeichnung;
		result.kuerzelStatistik = (obj.kuerzelStatistik === undefined) ? null : obj.kuerzelStatistik === null ? null : obj.kuerzelStatistik;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		result.kuerzelSchulgliederung = (obj.kuerzelSchulgliederung === undefined) ? null : obj.kuerzelSchulgliederung === null ? null : obj.kuerzelSchulgliederung;
		result.idFolgejahrgang = (obj.idFolgejahrgang === undefined) ? null : obj.idFolgejahrgang === null ? null : obj.idFolgejahrgang;
		result.anzahlRestabschnitte = (obj.anzahlRestabschnitte === undefined) ? null : obj.anzahlRestabschnitte === null ? null : obj.anzahlRestabschnitte;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		result.gueltigVon = (obj.gueltigVon === undefined) ? null : obj.gueltigVon === null ? null : obj.gueltigVon;
		result.gueltigBis = (obj.gueltigBis === undefined) ? null : obj.gueltigBis === null ? null : obj.gueltigBis;
		result.referenziertInAnderenTabellen = (obj.referenziertInAnderenTabellen === undefined) ? null : obj.referenziertInAnderenTabellen === null ? null : obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj : JahrgangsDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		result += '"kuerzelStatistik" : ' + ((obj.kuerzelStatistik === null) ? 'null' : JSON.stringify(obj.kuerzelStatistik)) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"kuerzelSchulgliederung" : ' + ((obj.kuerzelSchulgliederung === null) ? 'null' : JSON.stringify(obj.kuerzelSchulgliederung)) + ',';
		result += '"idFolgejahrgang" : ' + ((obj.idFolgejahrgang === null) ? 'null' : obj.idFolgejahrgang.toString()) + ',';
		result += '"anzahlRestabschnitte" : ' + ((obj.anzahlRestabschnitte === null) ? 'null' : obj.anzahlRestabschnitte.toString()) + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<JahrgangsDaten>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + ((obj.kuerzel === null) ? 'null' : JSON.stringify(obj.kuerzel)) + ',';
		}
		if (obj.kurzbezeichnung !== undefined) {
			result += '"kurzbezeichnung" : ' + ((obj.kurzbezeichnung === null) ? 'null' : JSON.stringify(obj.kurzbezeichnung)) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + ((obj.kuerzelStatistik === null) ? 'null' : JSON.stringify(obj.kuerzelStatistik)) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.kuerzelSchulgliederung !== undefined) {
			result += '"kuerzelSchulgliederung" : ' + ((obj.kuerzelSchulgliederung === null) ? 'null' : JSON.stringify(obj.kuerzelSchulgliederung)) + ',';
		}
		if (obj.idFolgejahrgang !== undefined) {
			result += '"idFolgejahrgang" : ' + ((obj.idFolgejahrgang === null) ? 'null' : obj.idFolgejahrgang.toString()) + ',';
		}
		if (obj.anzahlRestabschnitte !== undefined) {
			result += '"anzahlRestabschnitte" : ' + ((obj.anzahlRestabschnitte === null) ? 'null' : obj.anzahlRestabschnitte.toString()) + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.gueltigVon !== undefined) {
			result += '"gueltigVon" : ' + ((obj.gueltigVon === null) ? 'null' : obj.gueltigVon.toString()) + ',';
		}
		if (obj.gueltigBis !== undefined) {
			result += '"gueltigBis" : ' + ((obj.gueltigBis === null) ? 'null' : obj.gueltigBis.toString()) + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_jahrgang_JahrgangsDaten(obj : unknown) : JahrgangsDaten {
	return obj as JahrgangsDaten;
}
