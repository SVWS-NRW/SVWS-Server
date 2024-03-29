import { JavaObject } from '../../../../java/lang/JavaObject';

export class SchildReportingSchuelerLeistungsdaten extends JavaObject {

	/**
	 * Die ID der Leistungsdaten
	 */
	public id : number = -1;

	/**
	 * Die ID des Schüler-Lernabschnitts, zu dem die Leistungsdaten gehören.
	 */
	public abschnittID : number = -1;

	/**
	 * Die ID des Faches der Leistungsdaten
	 */
	public fachID : number = -1;

	/**
	 * Das Kürzel des Faches
	 */
	public fachKuerzel : string = "";

	/**
	 * Die Bezeichnung des Faches
	 */
	public fach : string = "";

	/**
	 * Die ID des Fachlehrers
	 */
	public lehrerID : number | null = null;

	/**
	 * Das Kürzel des Fachlehrers
	 */
	public lehrerKuerzel : string | null = null;

	/**
	 * Die ID des Kurses, sofern vorhanden
	 */
	public kursID : number | null = null;

	/**
	 * Die Bezeichnung des Kurses, sofern vorhanden, sonst leer
	 */
	public kurs : string = "";

	/**
	 * Die spezielle Kursart
	 */
	public kursart : string = "";

	/**
	 * Die allgemeine Kursart
	 */
	public kursartAllg : string = "";

	/**
	 * Die Bezeichnung der Note
	 */
	public note : string = "";

	/**
	 * Das Notenkürzel
	 */
	public noteKuerzel : string = "";

	/**
	 * Die Notenpunkte, sofern eine Note gesetzt ist
	 */
	public notePunkte : number | null = null;

	/**
	 * Wert für allgemeine Sortierung der Fächer. Wird mit aufgenommen, um in Reports eine benutzerdefinierte Sortierung zu ermöglichen
	 */
	public sortierungAllg : number | null = null;

	/**
	 * Wert für Sortierung der Fächer in der Sek-II. Wird mit aufgenommen, um in Reports eine benutzerdefinierte Sortierung zu ermöglichen
	 */
	public sortierungSekII : number | null = null;


	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLeistungsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.schild3.reporting.SchildReportingSchuelerLeistungsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchildReportingSchuelerLeistungsdaten {
		const obj = JSON.parse(json);
		const result = new SchildReportingSchuelerLeistungsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.abschnittID === "undefined")
			 throw new Error('invalid json format, missing attribute abschnittID');
		result.abschnittID = obj.abschnittID;
		if (typeof obj.fachID === "undefined")
			 throw new Error('invalid json format, missing attribute fachID');
		result.fachID = obj.fachID;
		if (typeof obj.fachKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute fachKuerzel');
		result.fachKuerzel = obj.fachKuerzel;
		if (typeof obj.fach === "undefined")
			 throw new Error('invalid json format, missing attribute fach');
		result.fach = obj.fach;
		result.lehrerID = typeof obj.lehrerID === "undefined" ? null : obj.lehrerID === null ? null : obj.lehrerID;
		result.lehrerKuerzel = typeof obj.lehrerKuerzel === "undefined" ? null : obj.lehrerKuerzel === null ? null : obj.lehrerKuerzel;
		result.kursID = typeof obj.kursID === "undefined" ? null : obj.kursID === null ? null : obj.kursID;
		if (typeof obj.kurs === "undefined")
			 throw new Error('invalid json format, missing attribute kurs');
		result.kurs = obj.kurs;
		if (typeof obj.kursart === "undefined")
			 throw new Error('invalid json format, missing attribute kursart');
		result.kursart = obj.kursart;
		if (typeof obj.kursartAllg === "undefined")
			 throw new Error('invalid json format, missing attribute kursartAllg');
		result.kursartAllg = obj.kursartAllg;
		if (typeof obj.note === "undefined")
			 throw new Error('invalid json format, missing attribute note');
		result.note = obj.note;
		if (typeof obj.noteKuerzel === "undefined")
			 throw new Error('invalid json format, missing attribute noteKuerzel');
		result.noteKuerzel = obj.noteKuerzel;
		result.notePunkte = typeof obj.notePunkte === "undefined" ? null : obj.notePunkte === null ? null : obj.notePunkte;
		result.sortierungAllg = typeof obj.sortierungAllg === "undefined" ? null : obj.sortierungAllg === null ? null : obj.sortierungAllg;
		result.sortierungSekII = typeof obj.sortierungSekII === "undefined" ? null : obj.sortierungSekII === null ? null : obj.sortierungSekII;
		return result;
	}

	public static transpilerToJSON(obj : SchildReportingSchuelerLeistungsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"abschnittID" : ' + obj.abschnittID + ',';
		result += '"fachID" : ' + obj.fachID + ',';
		result += '"fachKuerzel" : ' + JSON.stringify(obj.fachKuerzel!) + ',';
		result += '"fach" : ' + JSON.stringify(obj.fach!) + ',';
		result += '"lehrerID" : ' + ((!obj.lehrerID) ? 'null' : obj.lehrerID) + ',';
		result += '"lehrerKuerzel" : ' + ((!obj.lehrerKuerzel) ? 'null' : JSON.stringify(obj.lehrerKuerzel)) + ',';
		result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID) + ',';
		result += '"kurs" : ' + JSON.stringify(obj.kurs!) + ',';
		result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg!) + ',';
		result += '"note" : ' + JSON.stringify(obj.note!) + ',';
		result += '"noteKuerzel" : ' + JSON.stringify(obj.noteKuerzel!) + ',';
		result += '"notePunkte" : ' + ((!obj.notePunkte) ? 'null' : obj.notePunkte) + ',';
		result += '"sortierungAllg" : ' + ((!obj.sortierungAllg) ? 'null' : obj.sortierungAllg) + ',';
		result += '"sortierungSekII" : ' + ((!obj.sortierungSekII) ? 'null' : obj.sortierungSekII) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchildReportingSchuelerLeistungsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.abschnittID !== "undefined") {
			result += '"abschnittID" : ' + obj.abschnittID + ',';
		}
		if (typeof obj.fachID !== "undefined") {
			result += '"fachID" : ' + obj.fachID + ',';
		}
		if (typeof obj.fachKuerzel !== "undefined") {
			result += '"fachKuerzel" : ' + JSON.stringify(obj.fachKuerzel!) + ',';
		}
		if (typeof obj.fach !== "undefined") {
			result += '"fach" : ' + JSON.stringify(obj.fach!) + ',';
		}
		if (typeof obj.lehrerID !== "undefined") {
			result += '"lehrerID" : ' + ((!obj.lehrerID) ? 'null' : obj.lehrerID) + ',';
		}
		if (typeof obj.lehrerKuerzel !== "undefined") {
			result += '"lehrerKuerzel" : ' + ((!obj.lehrerKuerzel) ? 'null' : JSON.stringify(obj.lehrerKuerzel)) + ',';
		}
		if (typeof obj.kursID !== "undefined") {
			result += '"kursID" : ' + ((!obj.kursID) ? 'null' : obj.kursID) + ',';
		}
		if (typeof obj.kurs !== "undefined") {
			result += '"kurs" : ' + JSON.stringify(obj.kurs!) + ',';
		}
		if (typeof obj.kursart !== "undefined") {
			result += '"kursart" : ' + JSON.stringify(obj.kursart!) + ',';
		}
		if (typeof obj.kursartAllg !== "undefined") {
			result += '"kursartAllg" : ' + JSON.stringify(obj.kursartAllg!) + ',';
		}
		if (typeof obj.note !== "undefined") {
			result += '"note" : ' + JSON.stringify(obj.note!) + ',';
		}
		if (typeof obj.noteKuerzel !== "undefined") {
			result += '"noteKuerzel" : ' + JSON.stringify(obj.noteKuerzel!) + ',';
		}
		if (typeof obj.notePunkte !== "undefined") {
			result += '"notePunkte" : ' + ((!obj.notePunkte) ? 'null' : obj.notePunkte) + ',';
		}
		if (typeof obj.sortierungAllg !== "undefined") {
			result += '"sortierungAllg" : ' + ((!obj.sortierungAllg) ? 'null' : obj.sortierungAllg) + ',';
		}
		if (typeof obj.sortierungSekII !== "undefined") {
			result += '"sortierungSekII" : ' + ((!obj.sortierungSekII) ? 'null' : obj.sortierungSekII) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schild3_reporting_SchildReportingSchuelerLeistungsdaten(obj : unknown) : SchildReportingSchuelerLeistungsdaten {
	return obj as SchildReportingSchuelerLeistungsdaten;
}
