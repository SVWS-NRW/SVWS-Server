import { JavaObject } from '../../../java/lang/JavaObject';

export class LehrerPersonaldaten extends JavaObject {

	/**
	 * Die ID des Lehrers.
	 */
	public id : number = 0;

	/**
	 * Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen.
	 */
	public identNrTeil1 : string | null = null;

	/**
	 * Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben.
	 */
	public identNrTeil2SerNr : string | null = null;

	/**
	 * Die Personalaktennummer des Lehrers für den Export zu GPC.
	 */
	public personalaktennummer : string | null = null;

	/**
	 * Die Personalnummer des LBV.
	 */
	public lbvPersonalnummer : string | null = null;

	/**
	 * Der zur Personalnummer gehörige Vergütungsschlüssel.
	 */
	public lbvVerguetungsschluessel : string | null = null;

	/**
	 * Das Datum, wann der Lehrer an die Schule gekommen ist.
	 */
	public zugangsdatum : string | null = null;

	/**
	 * Der Grund für den Zugang des Lehrers - siehe Statistik-Katalog.
	 */
	public zugangsgrund : string | null = null;

	/**
	 * Das Datum, wann der Lehrer an die Schule verlassen hat.
	 */
	public abgangsdatum : string | null = null;

	/**
	 * Der Grund für den Abgang des Lehrers - siehe Statistik-Katalog.
	 */
	public abgangsgrund : string | null = null;

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
	 * Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Statistik-Katalog.
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
		return ['de.nrw.schule.svws.core.data.lehrer.LehrerPersonaldaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): LehrerPersonaldaten {
		const obj = JSON.parse(json);
		const result = new LehrerPersonaldaten();
		if (typeof obj.id === "undefined")
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.identNrTeil1 = typeof obj.identNrTeil1 === "undefined" ? null : obj.identNrTeil1 === null ? null : obj.identNrTeil1;
		result.identNrTeil2SerNr = typeof obj.identNrTeil2SerNr === "undefined" ? null : obj.identNrTeil2SerNr === null ? null : obj.identNrTeil2SerNr;
		result.personalaktennummer = typeof obj.personalaktennummer === "undefined" ? null : obj.personalaktennummer === null ? null : obj.personalaktennummer;
		result.lbvPersonalnummer = typeof obj.lbvPersonalnummer === "undefined" ? null : obj.lbvPersonalnummer === null ? null : obj.lbvPersonalnummer;
		result.lbvVerguetungsschluessel = typeof obj.lbvVerguetungsschluessel === "undefined" ? null : obj.lbvVerguetungsschluessel === null ? null : obj.lbvVerguetungsschluessel;
		result.zugangsdatum = typeof obj.zugangsdatum === "undefined" ? null : obj.zugangsdatum === null ? null : obj.zugangsdatum;
		result.zugangsgrund = typeof obj.zugangsgrund === "undefined" ? null : obj.zugangsgrund === null ? null : obj.zugangsgrund;
		result.abgangsdatum = typeof obj.abgangsdatum === "undefined" ? null : obj.abgangsdatum === null ? null : obj.abgangsdatum;
		result.abgangsgrund = typeof obj.abgangsgrund === "undefined" ? null : obj.abgangsgrund === null ? null : obj.abgangsgrund;
		result.pflichtstundensoll = typeof obj.pflichtstundensoll === "undefined" ? null : obj.pflichtstundensoll === null ? null : obj.pflichtstundensoll;
		result.rechtsverhaeltnis = typeof obj.rechtsverhaeltnis === "undefined" ? null : obj.rechtsverhaeltnis === null ? null : obj.rechtsverhaeltnis;
		result.beschaeftigungsart = typeof obj.beschaeftigungsart === "undefined" ? null : obj.beschaeftigungsart === null ? null : obj.beschaeftigungsart;
		result.einsatzstatus = typeof obj.einsatzstatus === "undefined" ? null : obj.einsatzstatus === null ? null : obj.einsatzstatus;
		result.stammschulnummer = typeof obj.stammschulnummer === "undefined" ? null : obj.stammschulnummer === null ? null : obj.stammschulnummer;
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonaldaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : '"' + obj.identNrTeil1 + '"') + ',';
		result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : '"' + obj.identNrTeil2SerNr + '"') + ',';
		result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : '"' + obj.personalaktennummer + '"') + ',';
		result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : '"' + obj.lbvPersonalnummer + '"') + ',';
		result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : '"' + obj.lbvVerguetungsschluessel + '"') + ',';
		result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : '"' + obj.zugangsdatum + '"') + ',';
		result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : '"' + obj.zugangsgrund + '"') + ',';
		result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : '"' + obj.abgangsdatum + '"') + ',';
		result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : '"' + obj.abgangsgrund + '"') + ',';
		result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : '"' + obj.rechtsverhaeltnis + '"') + ',';
		result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : '"' + obj.beschaeftigungsart + '"') + ',';
		result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : '"' + obj.einsatzstatus + '"') + ',';
		result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : '"' + obj.stammschulnummer + '"') + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<LehrerPersonaldaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + obj.id + ',';
		}
		if (typeof obj.identNrTeil1 !== "undefined") {
			result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : '"' + obj.identNrTeil1 + '"') + ',';
		}
		if (typeof obj.identNrTeil2SerNr !== "undefined") {
			result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : '"' + obj.identNrTeil2SerNr + '"') + ',';
		}
		if (typeof obj.personalaktennummer !== "undefined") {
			result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : '"' + obj.personalaktennummer + '"') + ',';
		}
		if (typeof obj.lbvPersonalnummer !== "undefined") {
			result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : '"' + obj.lbvPersonalnummer + '"') + ',';
		}
		if (typeof obj.lbvVerguetungsschluessel !== "undefined") {
			result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : '"' + obj.lbvVerguetungsschluessel + '"') + ',';
		}
		if (typeof obj.zugangsdatum !== "undefined") {
			result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : '"' + obj.zugangsdatum + '"') + ',';
		}
		if (typeof obj.zugangsgrund !== "undefined") {
			result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : '"' + obj.zugangsgrund + '"') + ',';
		}
		if (typeof obj.abgangsdatum !== "undefined") {
			result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : '"' + obj.abgangsdatum + '"') + ',';
		}
		if (typeof obj.abgangsgrund !== "undefined") {
			result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : '"' + obj.abgangsgrund + '"') + ',';
		}
		if (typeof obj.pflichtstundensoll !== "undefined") {
			result += '"pflichtstundensoll" : ' + ((!obj.pflichtstundensoll) ? 'null' : obj.pflichtstundensoll) + ',';
		}
		if (typeof obj.rechtsverhaeltnis !== "undefined") {
			result += '"rechtsverhaeltnis" : ' + ((!obj.rechtsverhaeltnis) ? 'null' : '"' + obj.rechtsverhaeltnis + '"') + ',';
		}
		if (typeof obj.beschaeftigungsart !== "undefined") {
			result += '"beschaeftigungsart" : ' + ((!obj.beschaeftigungsart) ? 'null' : '"' + obj.beschaeftigungsart + '"') + ',';
		}
		if (typeof obj.einsatzstatus !== "undefined") {
			result += '"einsatzstatus" : ' + ((!obj.einsatzstatus) ? 'null' : '"' + obj.einsatzstatus + '"') + ',';
		}
		if (typeof obj.stammschulnummer !== "undefined") {
			result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : '"' + obj.stammschulnummer + '"') + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_nrw_schule_svws_core_data_lehrer_LehrerPersonaldaten(obj : unknown) : LehrerPersonaldaten {
	return obj as LehrerPersonaldaten;
}
