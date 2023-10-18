import { JavaObject } from '../../../java/lang/JavaObject';

export class DruckGostKursplanungKursSchueler extends JavaObject {

	/**
	 * ID des Schülers.
	 */
	public Id : number = -1;

	/**
	 * Nachname des Schülers.
	 */
	public Nachname : string = "";

	/**
	 * Vorname des Schülers.
	 */
	public Vorname : string = "";

	/**
	 * Geschlecht des Schülers.
	 */
	public Geschlecht : string | null = "";

	/**
	 * Geburtsdatum des Schülers.
	 */
	public Geburtsdatum : string | null = "";

	/**
	 * Externe Schulnummer des Schülers, wenn er den Status extern hat.
	 */
	public ExterneSchulnummer : string | null = "";

	/**
	 * Kursbelegung des Schülers, d. h. schriftlich oder mündlich.
	 */
	public Belegung : string | null = "";

	/**
	 * Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist.
	 */
	public Abiturfach : string | null = "";


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.druck.DruckGostKursplanungKursSchueler'].includes(name);
	}

	public static transpilerFromJSON(json : string): DruckGostKursplanungKursSchueler {
		const obj = JSON.parse(json);
		const result = new DruckGostKursplanungKursSchueler();
		if (typeof obj.Id === "undefined")
			 throw new Error('invalid json format, missing attribute Id');
		result.Id = obj.Id;
		if (typeof obj.Nachname === "undefined")
			 throw new Error('invalid json format, missing attribute Nachname');
		result.Nachname = obj.Nachname;
		if (typeof obj.Vorname === "undefined")
			 throw new Error('invalid json format, missing attribute Vorname');
		result.Vorname = obj.Vorname;
		result.Geschlecht = typeof obj.Geschlecht === "undefined" ? null : obj.Geschlecht === null ? null : obj.Geschlecht;
		result.Geburtsdatum = typeof obj.Geburtsdatum === "undefined" ? null : obj.Geburtsdatum === null ? null : obj.Geburtsdatum;
		result.ExterneSchulnummer = typeof obj.ExterneSchulnummer === "undefined" ? null : obj.ExterneSchulnummer === null ? null : obj.ExterneSchulnummer;
		result.Belegung = typeof obj.Belegung === "undefined" ? null : obj.Belegung === null ? null : obj.Belegung;
		result.Abiturfach = typeof obj.Abiturfach === "undefined" ? null : obj.Abiturfach === null ? null : obj.Abiturfach;
		return result;
	}

	public static transpilerToJSON(obj : DruckGostKursplanungKursSchueler) : string {
		let result = '{';
		result += '"Id" : ' + obj.Id + ',';
		result += '"Nachname" : ' + JSON.stringify(obj.Nachname!) + ',';
		result += '"Vorname" : ' + JSON.stringify(obj.Vorname!) + ',';
		result += '"Geschlecht" : ' + ((!obj.Geschlecht) ? 'null' : JSON.stringify(obj.Geschlecht)) + ',';
		result += '"Geburtsdatum" : ' + ((!obj.Geburtsdatum) ? 'null' : JSON.stringify(obj.Geburtsdatum)) + ',';
		result += '"ExterneSchulnummer" : ' + ((!obj.ExterneSchulnummer) ? 'null' : JSON.stringify(obj.ExterneSchulnummer)) + ',';
		result += '"Belegung" : ' + ((!obj.Belegung) ? 'null' : JSON.stringify(obj.Belegung)) + ',';
		result += '"Abiturfach" : ' + ((!obj.Abiturfach) ? 'null' : JSON.stringify(obj.Abiturfach)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<DruckGostKursplanungKursSchueler>) : string {
		let result = '{';
		if (typeof obj.Id !== "undefined") {
			result += '"Id" : ' + obj.Id + ',';
		}
		if (typeof obj.Nachname !== "undefined") {
			result += '"Nachname" : ' + JSON.stringify(obj.Nachname!) + ',';
		}
		if (typeof obj.Vorname !== "undefined") {
			result += '"Vorname" : ' + JSON.stringify(obj.Vorname!) + ',';
		}
		if (typeof obj.Geschlecht !== "undefined") {
			result += '"Geschlecht" : ' + ((!obj.Geschlecht) ? 'null' : JSON.stringify(obj.Geschlecht)) + ',';
		}
		if (typeof obj.Geburtsdatum !== "undefined") {
			result += '"Geburtsdatum" : ' + ((!obj.Geburtsdatum) ? 'null' : JSON.stringify(obj.Geburtsdatum)) + ',';
		}
		if (typeof obj.ExterneSchulnummer !== "undefined") {
			result += '"ExterneSchulnummer" : ' + ((!obj.ExterneSchulnummer) ? 'null' : JSON.stringify(obj.ExterneSchulnummer)) + ',';
		}
		if (typeof obj.Belegung !== "undefined") {
			result += '"Belegung" : ' + ((!obj.Belegung) ? 'null' : JSON.stringify(obj.Belegung)) + ',';
		}
		if (typeof obj.Abiturfach !== "undefined") {
			result += '"Abiturfach" : ' + ((!obj.Abiturfach) ? 'null' : JSON.stringify(obj.Abiturfach)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_druck_DruckGostKursplanungKursSchueler(obj : unknown) : DruckGostKursplanungKursSchueler {
	return obj as DruckGostKursplanungKursSchueler;
}
