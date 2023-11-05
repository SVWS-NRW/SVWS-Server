import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerPersonalabschnittsdaten } from '../../../core/data/lehrer/LehrerPersonalabschnittsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

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
	 * Die Schulnummer der Stammschule, sofern diese abweicht.
	 */
	public stammschulnummer : string | null = null;

	/**
	 * Die Abschnittsdaten des Lehrers.
	 */
	public readonly abschnittsdaten : List<LehrerPersonalabschnittsdaten> = new ArrayList();


	public constructor() {
		super();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.data.lehrer.LehrerPersonaldaten'].includes(name);
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
		result.stammschulnummer = typeof obj.stammschulnummer === "undefined" ? null : obj.stammschulnummer === null ? null : obj.stammschulnummer;
		if ((obj.abschnittsdaten !== undefined) && (obj.abschnittsdaten !== null)) {
			for (const elem of obj.abschnittsdaten) {
				result.abschnittsdaten?.add(LehrerPersonalabschnittsdaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : LehrerPersonaldaten) : string {
		let result = '{';
		result += '"id" : ' + obj.id + ',';
		result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : JSON.stringify(obj.identNrTeil1)) + ',';
		result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : JSON.stringify(obj.identNrTeil2SerNr)) + ',';
		result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : JSON.stringify(obj.personalaktennummer)) + ',';
		result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : JSON.stringify(obj.lbvPersonalnummer)) + ',';
		result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : JSON.stringify(obj.lbvVerguetungsschluessel)) + ',';
		result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : JSON.stringify(obj.zugangsdatum)) + ',';
		result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : JSON.stringify(obj.zugangsgrund)) + ',';
		result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : JSON.stringify(obj.abgangsdatum)) + ',';
		result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : JSON.stringify(obj.abgangsgrund)) + ',';
		result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		if (!obj.abschnittsdaten) {
			result += '"abschnittsdaten" : []';
		} else {
			result += '"abschnittsdaten" : [ ';
			for (let i = 0; i < obj.abschnittsdaten.size(); i++) {
				const elem = obj.abschnittsdaten.get(i);
				result += LehrerPersonalabschnittsdaten.transpilerToJSON(elem);
				if (i < obj.abschnittsdaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
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
			result += '"identNrTeil1" : ' + ((!obj.identNrTeil1) ? 'null' : JSON.stringify(obj.identNrTeil1)) + ',';
		}
		if (typeof obj.identNrTeil2SerNr !== "undefined") {
			result += '"identNrTeil2SerNr" : ' + ((!obj.identNrTeil2SerNr) ? 'null' : JSON.stringify(obj.identNrTeil2SerNr)) + ',';
		}
		if (typeof obj.personalaktennummer !== "undefined") {
			result += '"personalaktennummer" : ' + ((!obj.personalaktennummer) ? 'null' : JSON.stringify(obj.personalaktennummer)) + ',';
		}
		if (typeof obj.lbvPersonalnummer !== "undefined") {
			result += '"lbvPersonalnummer" : ' + ((!obj.lbvPersonalnummer) ? 'null' : JSON.stringify(obj.lbvPersonalnummer)) + ',';
		}
		if (typeof obj.lbvVerguetungsschluessel !== "undefined") {
			result += '"lbvVerguetungsschluessel" : ' + ((!obj.lbvVerguetungsschluessel) ? 'null' : JSON.stringify(obj.lbvVerguetungsschluessel)) + ',';
		}
		if (typeof obj.zugangsdatum !== "undefined") {
			result += '"zugangsdatum" : ' + ((!obj.zugangsdatum) ? 'null' : JSON.stringify(obj.zugangsdatum)) + ',';
		}
		if (typeof obj.zugangsgrund !== "undefined") {
			result += '"zugangsgrund" : ' + ((!obj.zugangsgrund) ? 'null' : JSON.stringify(obj.zugangsgrund)) + ',';
		}
		if (typeof obj.abgangsdatum !== "undefined") {
			result += '"abgangsdatum" : ' + ((!obj.abgangsdatum) ? 'null' : JSON.stringify(obj.abgangsdatum)) + ',';
		}
		if (typeof obj.abgangsgrund !== "undefined") {
			result += '"abgangsgrund" : ' + ((!obj.abgangsgrund) ? 'null' : JSON.stringify(obj.abgangsgrund)) + ',';
		}
		if (typeof obj.stammschulnummer !== "undefined") {
			result += '"stammschulnummer" : ' + ((!obj.stammschulnummer) ? 'null' : JSON.stringify(obj.stammschulnummer)) + ',';
		}
		if (typeof obj.abschnittsdaten !== "undefined") {
			if (!obj.abschnittsdaten) {
				result += '"abschnittsdaten" : []';
			} else {
				result += '"abschnittsdaten" : [ ';
				for (let i = 0; i < obj.abschnittsdaten.size(); i++) {
					const elem = obj.abschnittsdaten.get(i);
					result += LehrerPersonalabschnittsdaten.transpilerToJSON(elem);
					if (i < obj.abschnittsdaten.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_lehrer_LehrerPersonaldaten(obj : unknown) : LehrerPersonaldaten {
	return obj as LehrerPersonaldaten;
}
