import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerPersonalabschnittsdaten extends JavaObject {

	/**
	 * Die ID des Abschnitts für den Lehrer in der Datenbank.
	 */
	public id : number = 0;

	/**
	 * Die ID des Lehrers.
	 */
	public idLehrer : number = 0;

	/**
	 * Die ID des Schuljahresabschnitts, zu welchem diese Abschnittdaten gehören.
	 */
	public idSchuljahresabschnitt : number = 0;

	/**
	 * Das Pflichtstundensoll des Lehrers.
	 */
	public pflichtstundensoll : number | null = null;

	/**
	 * Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.
	 */
	public rechtsverhaeltnis : string | null = null;

	/**
	 * Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.
	 */
	public beschaeftigungsart : string | null = null;

	/**
	 * [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Core-Type {@link LehrerEinsatzstatus}
	 */
	public einsatzstatus : string | null = null;

	/**
	 * Die Schulnummer der Stammschule, sofern diese abweicht.
	 */
	public stammschulnummer : string | null = null;


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonalabschnittsdaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonalabschnittsdaten {
		const obj = JSON.parse(json);
		const result = new LehrerPersonalabschnittsdaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (typeof obj.idLehrer === "undefined")
			 throw new Error('invalid json format, missing attribute idLehrer');
		result.idLehrer = obj.idLehrer;
		if (typeof obj.idSchuljahresabschnitt === "undefined")
			 throw new Error('invalid json format, missing attribute idSchuljahresabschnitt');
		result.idSchuljahresabschnitt = obj.idSchuljahresabschnitt;
		result.pflichtstundensoll = typeof obj.pflichtstundensoll === "undefined" ? null : obj.pflichtstundensoll === null ? null : obj.pflichtstundensoll;
		result.rechtsverhaeltnis = typeof obj.rechtsverhaeltnis === "undefined" ? null : obj.rechtsverhaeltnis === null ? null : obj.rechtsverhaeltnis;
		result.beschaeftigungsart = typeof obj.beschaeftigungsart === "undefined" ? null : obj.beschaeftigungsart === null ? null : obj.beschaeftigungsart;
		result.einsatzstatus = typeof obj.einsatzstatus === "undefined" ? null : obj.einsatzstatus === null ? null : obj.einsatzstatus;
		result.stammschulnummer = typeof obj.stammschulnummer === "undefined" ? null : obj.stammschulnummer === null ? null : obj.stammschulnummer;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonalabschnittsdaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"idLehrer" : ' + obj.idLehrer + ',';
		result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonalabschnittsdaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.idLehrer !== "undefined") {
			result += '"idLehrer" : ' + obj.idLehrer + ',';
		}
		if (typeof obj.idSchuljahresabschnitt !== "undefined") {
			result += '"idSchuljahresabschnitt" : ' + obj.idSchuljahresabschnitt + ',';
		}
		if (typeof obj.pflichtstundensoll !== "undefined") {
			result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		}
		if (typeof obj.rechtsverhaeltnis !== "undefined") {
			result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : JSON.stringify(obj.rechtsverhaeltnis)) + ',';
		}
		if (typeof obj.beschaeftigungsart !== "undefined") {
			result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : JSON.stringify(obj.beschaeftigungsart)) + ',';
		}
		if (typeof obj.einsatzstatus !== "undefined") {
			result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : JSON.stringify(obj.einsatzstatus)) + ',';
		}
		if (typeof obj.stammschulnummer !== "undefined") {
			result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonalabschnittsdaten(obj : unknown) : LehrerPersonalabschnittsdaten {
	return obj as LehrerPersonalabschnittsdaten;
}
