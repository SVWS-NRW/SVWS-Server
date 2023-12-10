import { JavaObject } from '../../../java/lang/JavaObject';

export class FachDaten extends JavaObject {

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
	 * Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 */
	public istSichtbar : boolean = false;

	/**
	 * Das Aufgabenfeld am Berufskolleg, zu welchem das Fach gehört
	 */
	public aufgabenfeld : string | null = null;

	/**
	 * Die Sprache in der das Fach unterrichtet wird, sofern es sich um ein bilinguales Sachefach handelt.
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
	 * Gibt die maximale Anzahl an Zeichen an, doe in Fachbemerkungen genutzt werden dürfen.
	 */
	public maxZeichenInFachbemerkungen : number = -1;

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


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.fach.FachDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.fach.FachDaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): FachDaten {
		const obj = JSON.parse(json);
		const result = new FachDaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.kuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzel');
		result.kuerzel = obj.kuerzel;
		if (typeof obj.kuerzelStatistik === "undefined")
			 throw new Error('invalid json format, missing attribute kuerzelStatistik');
		result.kuerzelStatistik = obj.kuerzelStatistik;
		if (typeof obj.bezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnung');
		result.bezeichnung = obj.bezeichnung;
		if (typeof obj.sortierung === "undefined")
			 throw new Error('invalid json format, missing attribute sortierung');
		result.sortierung = obj.sortierung;
		if (typeof obj.istOberstufenFach === "undefined")
			 throw new Error('invalid json format, missing attribute istOberstufenFach');
		result.istOberstufenFach = obj.istOberstufenFach;
		if (typeof obj.istSichtbar === "undefined")
			 throw new Error('invalid json format, missing attribute istSichtbar');
		result.istSichtbar = obj.istSichtbar;
		result.aufgabenfeld = typeof obj.aufgabenfeld === "undefined" ? null : obj.aufgabenfeld === null ? null : obj.aufgabenfeld;
		result.bilingualeSprache = typeof obj.bilingualeSprache === "undefined" ? null : obj.bilingualeSprache === null ? null : obj.bilingualeSprache;
		if (typeof obj.istNachpruefungErlaubt === "undefined")
			 throw new Error('invalid json format, missing attribute istNachpruefungErlaubt');
		result.istNachpruefungErlaubt = obj.istNachpruefungErlaubt;
		if (typeof obj.aufZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute aufZeugnis');
		result.aufZeugnis = obj.aufZeugnis;
		if (typeof obj.bezeichnungZeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungZeugnis');
		result.bezeichnungZeugnis = obj.bezeichnungZeugnis;
		if (typeof obj.bezeichnungUeberweisungszeugnis === "undefined")
			 throw new Error('invalid json format, missing attribute bezeichnungUeberweisungszeugnis');
		result.bezeichnungUeberweisungszeugnis = obj.bezeichnungUeberweisungszeugnis;
		if (typeof obj.maxZeichenInFachbemerkungen === "undefined")
			 throw new Error('invalid json format, missing attribute maxZeichenInFachbemerkungen');
		result.maxZeichenInFachbemerkungen = obj.maxZeichenInFachbemerkungen;
		if (typeof obj.istSchriftlichZK === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlichZK');
		result.istSchriftlichZK = obj.istSchriftlichZK;
		if (typeof obj.istSchriftlichBA === "undefined")
			 throw new Error('invalid json format, missing attribute istSchriftlichBA');
		result.istSchriftlichBA = obj.istSchriftlichBA;
		if (typeof obj.istFHRFach === "undefined")
			 throw new Error('invalid json format, missing attribute istFHRFach');
		result.istFHRFach = obj.istFHRFach;
		if (typeof obj.holeAusAltenLernabschnitten === "undefined")
			 throw new Error('invalid json format, missing attribute holeAusAltenLernabschnitten');
		result.holeAusAltenLernabschnitten = obj.holeAusAltenLernabschnitten;
		return result;
	}

	public static transpilerToJSON(obj : FachDaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		result += '"sortierung" : ' + obj.sortierung + ',';
		result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		result += '"aufgabenfeld" : ' + ((!obj.aufgabenfeld) ? 'null' : JSON.stringify(obj.aufgabenfeld)) + ',';
		result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		result += '"istNachpruefungErlaubt" : ' + obj.istNachpruefungErlaubt + ',';
		result += '"aufZeugnis" : ' + obj.aufZeugnis + ',';
		result += '"bezeichnungZeugnis" : ' + JSON.stringify(obj.bezeichnungZeugnis!) + ',';
		result += '"bezeichnungUeberweisungszeugnis" : ' + JSON.stringify(obj.bezeichnungUeberweisungszeugnis!) + ',';
		result += '"maxZeichenInFachbemerkungen" : ' + obj.maxZeichenInFachbemerkungen + ',';
		result += '"istSchriftlichZK" : ' + obj.istSchriftlichZK + ',';
		result += '"istSchriftlichBA" : ' + obj.istSchriftlichBA + ',';
		result += '"istFHRFach" : ' + obj.istFHRFach + ',';
		result += '"holeAusAltenLernabschnitten" : ' + obj.holeAusAltenLernabschnitten + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<FachDaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.kuerzel !== "undefined") {
			result += '"kuerzel" : ' + JSON.stringify(obj.kuerzel!) + ',';
		}
		if (typeof obj.kuerzelStatistik !== "undefined") {
			result += '"kuerzelStatistik" : ' + JSON.stringify(obj.kuerzelStatistik!) + ',';
		}
		if (typeof obj.bezeichnung !== "undefined") {
			result += '"bezeichnung" : ' + JSON.stringify(obj.bezeichnung!) + ',';
		}
		if (typeof obj.sortierung !== "undefined") {
			result += '"sortierung" : ' + obj.sortierung + ',';
		}
		if (typeof obj.istOberstufenFach !== "undefined") {
			result += '"istOberstufenFach" : ' + obj.istOberstufenFach + ',';
		}
		if (typeof obj.istSichtbar !== "undefined") {
			result += '"istSichtbar" : ' + obj.istSichtbar + ',';
		}
		if (typeof obj.aufgabenfeld !== "undefined") {
			result += '"aufgabenfeld" : ' + ((!obj.aufgabenfeld) ? 'null' : JSON.stringify(obj.aufgabenfeld)) + ',';
		}
		if (typeof obj.bilingualeSprache !== "undefined") {
			result += '"bilingualeSprache" : ' + ((!obj.bilingualeSprache) ? 'null' : JSON.stringify(obj.bilingualeSprache)) + ',';
		}
		if (typeof obj.istNachpruefungErlaubt !== "undefined") {
			result += '"istNachpruefungErlaubt" : ' + obj.istNachpruefungErlaubt + ',';
		}
		if (typeof obj.aufZeugnis !== "undefined") {
			result += '"aufZeugnis" : ' + obj.aufZeugnis + ',';
		}
		if (typeof obj.bezeichnungZeugnis !== "undefined") {
			result += '"bezeichnungZeugnis" : ' + JSON.stringify(obj.bezeichnungZeugnis!) + ',';
		}
		if (typeof obj.bezeichnungUeberweisungszeugnis !== "undefined") {
			result += '"bezeichnungUeberweisungszeugnis" : ' + JSON.stringify(obj.bezeichnungUeberweisungszeugnis!) + ',';
		}
		if (typeof obj.maxZeichenInFachbemerkungen !== "undefined") {
			result += '"maxZeichenInFachbemerkungen" : ' + obj.maxZeichenInFachbemerkungen + ',';
		}
		if (typeof obj.istSchriftlichZK !== "undefined") {
			result += '"istSchriftlichZK" : ' + obj.istSchriftlichZK + ',';
		}
		if (typeof obj.istSchriftlichBA !== "undefined") {
			result += '"istSchriftlichBA" : ' + obj.istSchriftlichBA + ',';
		}
		if (typeof obj.istFHRFach !== "undefined") {
			result += '"istFHRFach" : ' + obj.istFHRFach + ',';
		}
		if (typeof obj.holeAusAltenLernabschnitten !== "undefined") {
			result += '"holeAusAltenLernabschnitten" : ' + obj.holeAusAltenLernabschnitten + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_fach_FachDaten(obj : unknown) : FachDaten {
	return obj as FachDaten;
}
