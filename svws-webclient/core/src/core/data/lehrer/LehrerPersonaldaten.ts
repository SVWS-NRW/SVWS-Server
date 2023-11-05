import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../core/data/lehrer/LehrerLehramtEintrag';
import { LehrerPersonalabschnittsdaten } from '../../../core/data/lehrer/LehrerPersonalabschnittsdaten';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigungEintrag } from '../../../core/data/lehrer/LehrerLehrbefaehigungEintrag';
import { LehrerFachrichtungEintrag } from '../../../core/data/lehrer/LehrerFachrichtungEintrag';

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

	/**
	 * Die Lehrämter des Lehrers.
	 */
	public readonly lehraemter : List<LehrerLehramtEintrag> = new ArrayList();

	/**
	 * Die Fachrichtungen des Lehrers.
	 */
	public readonly fachrichtungen : List<LehrerFachrichtungEintrag> = new ArrayList();

	/**
	 * Die Lehrbefähigungen des Lehrers.
	 */
	public readonly lehrbefaehigungen : List<LehrerLehrbefaehigungEintrag> = new ArrayList();


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
		if ((obj.lehraemter !== undefined) && (obj.lehraemter !== null)) {
			for (const elem of obj.lehraemter) {
				result.lehraemter?.add(LehrerLehramtEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.fachrichtungen !== undefined) && (obj.fachrichtungen !== null)) {
			for (const elem of obj.fachrichtungen) {
				result.fachrichtungen?.add(LehrerFachrichtungEintrag.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.lehrbefaehigungen !== undefined) && (obj.lehrbefaehigungen !== null)) {
			for (const elem of obj.lehrbefaehigungen) {
				result.lehrbefaehigungen?.add(LehrerLehrbefaehigungEintrag.transpilerFromJSON(JSON.stringify(elem)));
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
		if (!obj.lehraemter) {
			result += '"lehraemter" : []';
		} else {
			result += '"lehraemter" : [ ';
			for (let i = 0; i < obj.lehraemter.size(); i++) {
				const elem = obj.lehraemter.get(i);
				result += LehrerLehramtEintrag.transpilerToJSON(elem);
				if (i < obj.lehraemter.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.fachrichtungen) {
			result += '"fachrichtungen" : []';
		} else {
			result += '"fachrichtungen" : [ ';
			for (let i = 0; i < obj.fachrichtungen.size(); i++) {
				const elem = obj.fachrichtungen.get(i);
				result += LehrerFachrichtungEintrag.transpilerToJSON(elem);
				if (i < obj.fachrichtungen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.lehrbefaehigungen) {
			result += '"lehrbefaehigungen" : []';
		} else {
			result += '"lehrbefaehigungen" : [ ';
			for (let i = 0; i < obj.lehrbefaehigungen.size(); i++) {
				const elem = obj.lehrbefaehigungen.get(i);
				result += LehrerLehrbefaehigungEintrag.transpilerToJSON(elem);
				if (i < obj.lehrbefaehigungen.size() - 1)
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
		if (typeof obj.lehraemter !== "undefined") {
			if (!obj.lehraemter) {
				result += '"lehraemter" : []';
			} else {
				result += '"lehraemter" : [ ';
				for (let i = 0; i < obj.lehraemter.size(); i++) {
					const elem = obj.lehraemter.get(i);
					result += LehrerLehramtEintrag.transpilerToJSON(elem);
					if (i < obj.lehraemter.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.fachrichtungen !== "undefined") {
			if (!obj.fachrichtungen) {
				result += '"fachrichtungen" : []';
			} else {
				result += '"fachrichtungen" : [ ';
				for (let i = 0; i < obj.fachrichtungen.size(); i++) {
					const elem = obj.fachrichtungen.get(i);
					result += LehrerFachrichtungEintrag.transpilerToJSON(elem);
					if (i < obj.fachrichtungen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.lehrbefaehigungen !== "undefined") {
			if (!obj.lehrbefaehigungen) {
				result += '"lehrbefaehigungen" : []';
			} else {
				result += '"lehrbefaehigungen" : [ ';
				for (let i = 0; i < obj.lehrbefaehigungen.size(); i++) {
					const elem = obj.lehrbefaehigungen.get(i);
					result += LehrerLehrbefaehigungEintrag.transpilerToJSON(elem);
					if (i < obj.lehrbefaehigungen.size() - 1)
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
