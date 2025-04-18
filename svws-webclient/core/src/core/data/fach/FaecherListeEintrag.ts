import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { Class } from '../../../java/lang/Class';

export class FaecherListeEintrag extends JavaObject {

	/**
	 * Die ID des Faches.
	 */
	public id : number = -1;

	/**
	 * Das eindeutige Kürzel des Faches
	 */
	public kuerzel : string = "";

	/**
	 * Das Statistik-Kürzel des Faches
	 */
	public kuerzelStatistik : string = "";

	/**
	 * Die Bezeichnung des Faches
	 */
	public bezeichnung : string = "";

	/**
	 * Die Sortierreihenfolge des Fächerlisten-Eintrags.
	 */
	public sortierung : number = 32000;

	/**
	 * Gibt an, ob es sich um ein Fach der Oberstufe handelt oder nicht.
	 */
	public istOberstufenFach : boolean = false;

	/**
	 * Gibt an, ob es sich um ein Fach handelt, welches relevant für die Prüfungsordnung ist oder nicht (z.B. bei Belegprüfungen).
	 */
	public istPruefungsordnungsRelevant : boolean = false;

	/**
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört
	 */
	public aufgabenfeld : string | null = null;

	/**
	 * Die Sprache, in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachfach handelt.
	 */
	public bilingualeSprache : string | null = null;

	/**
	 * Gibt an, ob eine Nachprüfung in diesem Fach möglich ist.
	 */
	public istNachpruefungErlaubt : boolean = false;

	/**
	 * Gibt an, ob das Fach auf einem Zeugnis erscheinen soll.
	 */
	public aufZeugnis : boolean = false;

	/**
	 * Die Bezeichnung des Faches auf allgemeinen Zeugnissen
	 */
	public bezeichnungZeugnis : string = "";

	/**
	 * Die Bezeichnung des Faches auf Überweisungs-Zeugnissen
	 */
	public bezeichnungUeberweisungszeugnis : string = "";

	/**
	 * Gibt die maximale Anzahl an Zeichen an, die in Fachbemerkungen genutzt werden dürfen.
	 */
	public maxZeichenInFachbemerkungen : number = JavaInteger.MAX_VALUE;

	/**
	 * Gibt an, ob das Fach ein schriftliches Fach für die zentralen Klausuren ist oder nicht.
	 */
	public istSchriftlichZK : boolean = false;

	/**
	 * Gibt an, ob das Fach als schriftliches Fach für den Berufsabschluss gewertest wird (Berufskolleg).
	 */
	public istSchriftlichBA : boolean = false;

	/**
	 * Gibt an, ob das Fach bei der Berechnung der FHR berücksichtigt wird oder nicht (Berufskolleg).
	 */
	public istFHRFach : boolean = false;

	/**
	 * Gibt an, ob das Fach ggf. bei der Aggregation von Leistungen aus früheren Lernabschnitten/Jahrgängen für eine Abschlussbrechnung berücksichtigt wird, sofern es im aktuellen Abschnitt nicht belegt wurde.
	 */
	public holeAusAltenLernabschnitten : boolean = false;

	/**
	 * Gibt an, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht.
	 */
	public referenziertInAnderenTabellen : boolean | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.fach.FaecherListeEintrag';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.fach.FaecherListeEintrag'].includes(name);
	}

	public static class = new Class<FaecherListeEintrag>('de.svws_nrw.core.data.fach.FaecherListeEintrag');

	public static transpilerFromJSON(json : string): FaecherListeEintrag {
		const obj = JSON.parse(json) as Partial<FaecherListeEintrag>;
		const result = new FaecherListeEintrag();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.kuerzel === undefined)
			throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (obj.kuerzelStatistik === undefined)
			throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (obj.bezeichnung === undefined)
			throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (obj.sortierung === undefined)
			throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (obj.istOberstufenFach === undefined)
			throw new Error('invalid json format, missing attribute istOberstufenFach');
		result.istOberstufenFach = obj.istOberstufenFach;
		if (obj.istPruefungsordnungsRelevant === undefined)
			throw new Error('invalid json format, missing attribute istPruefungsordnungsRelevant');
		result.istPruefungsordnungsRelevant = obj.istPruefungsordnungsRelevant;
		if (obj.istSichtbar === undefined)
			throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		result.aufgabenfeld = (obj.aufgabenfeld === undefined) ? null : obj.aufgabenfeld === null ? null : obj.aufgabenfeld;
		result.bilingualeSprache = (obj.bilingualeSprache === undefined) ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (obj.istNachpruefungErlaubt === undefined)
			throw new Error('invalid json format, missing attribute istNachpruefungErlaubt');
		result.istNachpruefungErlaubt = obj.istNachpruefungErlaubt;
		if (obj.aufZeugnis === undefined)
			throw new Error('invalid json format, missing attribute aufZeugnis');
		result.aufZeugnis = obj.aufZeugnis;
		if (obj.bezeichnungZeugnis === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungZeugnis');
		result.bezeichnungZeugnis = obj.bezeichnungZeugnis;
		if (obj.bezeichnungUeberweisungszeugnis === undefined)
			throw new Error('invalid json format, missing attribute bezeichnungUeberweisungszeugnis');
		result.bezeichnungUeberweisungszeugnis = obj.bezeichnungUeberweisungszeugnis;
		if (obj.maxZeichenInFachbemerkungen === undefined)
			throw new Error('invalid json format, missing attribute maxZeichenInFachbemerkungen');
		result.maxZeichenInFachbemerkungen = obj.maxZeichenInFachbemerkungen;
		if (obj.istSchriftlichZK === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlichZK');
		result.istSchriftlichZK = obj.istSchriftlichZK;
		if (obj.istSchriftlichBA === undefined)
			throw new Error('invalid json format, missing attribute istSchriftlichBA');
		result.istSchriftlichBA = obj.istSchriftlichBA;
		if (obj.istFHRFach === undefined)
			throw new Error('invalid json format, missing attribute istFHRFach');
		result.istFHRFach = obj.istFHRFach;
		if (obj.holeAusAltenLernabschnitten === undefined)
			throw new Error('invalid json format, missing attribute holeAusAltenLernabschnitten');
		result.holeAusAltenLernabschnitten = obj.holeAusAltenLernabschnitten;
		result.referenziertInAnderenTabellen = (obj.referenziertInAnderenTabellen === undefined) ? null : obj.referenziertInAnderenTabellen === null ? null : obj.referenziertInAnderenTabellen;
		return result;
	}

	public static transpilerToJSON(obj : FaecherListeEintrag) : string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		result += '"istOberstufenFach" : ' + obj.istOberstufenFach.toString() + ',';
		result += '"istPruefungsordnungsRelevant" : ' + obj.istPruefungsordnungsRelevant.toString() + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		result += '"aufgabenfeld" : ' + ((obj.aufgabenfeld === null) ? 'null' : JSON.stringify(obj.aufgabenfeld)) + ',';
		result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"istNachpruefungErlaubt" : ' + obj.istNachpruefungErlaubt.toString() + ',';
		result += '"aufZeugnis" : ' + obj.aufZeugnis.toString() + ',';
		result += '"bezeichnungZeugnis" : ' + JSON.stringify(obj.bezeichnungZeugnis) + ',';
		result += '"bezeichnungUeberweisungszeugnis" : ' + JSON.stringify(obj.bezeichnungUeberweisungszeugnis) + ',';
		result += '"maxZeichenInFachbemerkungen" : ' + obj.maxZeichenInFachbemerkungen.toString() + ',';
		result += '"istSchriftlichZK" : ' + obj.istSchriftlichZK.toString() + ',';
		result += '"istSchriftlichBA" : ' + obj.istSchriftlichBA.toString() + ',';
		result += '"istFHRFach" : ' + obj.istFHRFach.toString() + ',';
		result += '"holeAusAltenLernabschnitten" : ' + obj.holeAusAltenLernabschnitten.toString() + ',';
		result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FaecherListeEintrag>) : string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.kuerzel !== undefined) {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel) + ',';
		}
		if (obj.kuerzelStatistik !== undefined) {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung) + ',';
		}
		if (obj.sortierung !== undefined) {
			result += '"sortierung" : ' + obj.sortierung.toString() + ',';
		}
		if (obj.istOberstufenFach !== undefined) {
			result += '"istOberstufenFach" : ' + obj.istOberstufenFach.toString() + ',';
		}
		if (obj.istPruefungsordnungsRelevant !== undefined) {
			result += '"istPruefungsordnungsRelevant" : ' + obj.istPruefungsordnungsRelevant.toString() + ',';
		}
		if (obj.istSichtbar !== undefined) {
			result += '"istSichtbar" : ' + obj.istSichtbar.toString() + ',';
		}
		if (obj.aufgabenfeld !== undefined) {
			result += '"aufgabenfeld" : ' + ((obj.aufgabenfeld === null) ? 'null' : JSON.stringify(obj.aufgabenfeld)) + ',';
		}
		if (obj.bilingualeSprache !== undefined) {
			result += '"bilingualeSprache" : ' + ((obj.bilingualeSprache === null) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (obj.istNachpruefungErlaubt !== undefined) {
			result += '"istNachpruefungErlaubt" : ' + obj.istNachpruefungErlaubt.toString() + ',';
		}
		if (obj.aufZeugnis !== undefined) {
			result += '"aufZeugnis" : ' + obj.aufZeugnis.toString() + ',';
		}
		if (obj.bezeichnungZeugnis !== undefined) {
			result += '"bezeichnungZeugnis" : ' + JSON.stringify(obj.bezeichnungZeugnis) + ',';
		}
		if (obj.bezeichnungUeberweisungszeugnis !== undefined) {
			result += '"bezeichnungUeberweisungszeugnis" : ' + JSON.stringify(obj.bezeichnungUeberweisungszeugnis) + ',';
		}
		if (obj.maxZeichenInFachbemerkungen !== undefined) {
			result += '"maxZeichenInFachbemerkungen" : ' + obj.maxZeichenInFachbemerkungen.toString() + ',';
		}
		if (obj.istSchriftlichZK !== undefined) {
			result += '"istSchriftlichZK" : ' + obj.istSchriftlichZK.toString() + ',';
		}
		if (obj.istSchriftlichBA !== undefined) {
			result += '"istSchriftlichBA" : ' + obj.istSchriftlichBA.toString() + ',';
		}
		if (obj.istFHRFach !== undefined) {
			result += '"istFHRFach" : ' + obj.istFHRFach.toString() + ',';
		}
		if (obj.holeAusAltenLernabschnitten !== undefined) {
			result += '"holeAusAltenLernabschnitten" : ' + obj.holeAusAltenLernabschnitten.toString() + ',';
		}
		if (obj.referenziertInAnderenTabellen !== undefined) {
			result += '"referenziertInAnderenTabellen" : ' + ((obj.referenziertInAnderenTabellen === null) ? 'null' : obj.referenziertInAnderenTabellen.toString()) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_fach_FaecherListeEintrag(obj : unknown) : FaecherListeEintrag {
	return obj as FaecherListeEintrag;
}
