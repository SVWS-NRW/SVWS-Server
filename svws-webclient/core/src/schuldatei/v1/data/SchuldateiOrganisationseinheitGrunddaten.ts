import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitSchulform } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitSchulform';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuldateiOrganisationseinheitGrunddaten extends JavaObject {

	/**
	 * Die ID der Grunddaten.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Kurzbezeichnung der Organisationseinheit
	 */
	public kurzbezeichnung : string = "";

	/**
	 * Der Rechtsstatus der Organisationseinheit 1=öffentlich, 2=privat
	 */
	public rechtsstatus : string | null = null;

	/**
	 * Schulträgernummer der Organisationseinheit
	 */
	public schultraegernummer : string | null = null;

	/**
	 * Art der Trägerschaft der Schule
	 */
	public artdertraegerschaft : string | null = null;

	/**
	 * Betriebsschlüssel der Schule
	 */
	public schulbetriebsschluessel : string | null = null;

	/**
	 * Kapitel der Schule
	 */
	public kapitel : string | null = null;

	/**
	 * Obere Schulaufsicht der Schule
	 */
	public obereschulaufsicht : string | null = null;

	/**
	 * Untere Schulaufsicht der Schule
	 */
	public untereschulaufsicht : string | null = null;

	/**
	 * Zentrum für schulpraktische Lehrerausbildung ZFSL
	 */
	public zfsl : string | null = null;

	/**
	 * Dienststellenschlüssel der Organisationseinheit
	 */
	public dienststellenschluessel : string | null = null;

	/**
	 * Personalteilbereich der Organisationseinheit
	 */
	public ptb : string | null = null;

	/**
	 * Gibt an ob die Schule Internatsbetrieb hat
	 */
	public internatsbetrieb : string | null = null;

	/**
	 * Anzahl der Internatsplätze
	 */
	public internatsplaetze : number | null = null;

	/**
	 * Gibt die Gültigkeit ab welchem Schuljahr an
	 */
	public gueltigab : string | null = null;

	/**
	 * Gibt die Gültigkeit bis zu welchem Schuljahr an
	 */
	public gueltigbis : string | null = null;

	/**
	 * Gibt das Änderungsdatum des Eintrags an
	 */
	public geaendertam : string | null = null;

	/**
	 * Die Schulformen der Organisationseinheit:Schule (zeitl. Verlaufsliste)
	 */
	public readonly schulform : List<SchuldateiOrganisationseinheitSchulform> = new ArrayList<SchuldateiOrganisationseinheitSchulform>();


	/**
	 * Erstellt neue Grunddaten für eine Organiationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGrunddaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGrunddaten'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGrunddaten {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitGrunddaten();
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : obj.id;
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (typeof obj.kurzbezeichnung === "undefined")
			 throw new Error('invalid json format, missing attribute kurzbezeichnung');
		result.kurzbezeichnung = obj.kurzbezeichnung;
		result.rechtsstatus = typeof obj.rechtsstatus === "undefined" ? null : obj.rechtsstatus === null ? null : obj.rechtsstatus;
		result.schultraegernummer = typeof obj.schultraegernummer === "undefined" ? null : obj.schultraegernummer === null ? null : obj.schultraegernummer;
		result.artdertraegerschaft = typeof obj.artdertraegerschaft === "undefined" ? null : obj.artdertraegerschaft === null ? null : obj.artdertraegerschaft;
		result.schulbetriebsschluessel = typeof obj.schulbetriebsschluessel === "undefined" ? null : obj.schulbetriebsschluessel === null ? null : obj.schulbetriebsschluessel;
		result.kapitel = typeof obj.kapitel === "undefined" ? null : obj.kapitel === null ? null : obj.kapitel;
		result.obereschulaufsicht = typeof obj.obereschulaufsicht === "undefined" ? null : obj.obereschulaufsicht === null ? null : obj.obereschulaufsicht;
		result.untereschulaufsicht = typeof obj.untereschulaufsicht === "undefined" ? null : obj.untereschulaufsicht === null ? null : obj.untereschulaufsicht;
		result.zfsl = typeof obj.zfsl === "undefined" ? null : obj.zfsl === null ? null : obj.zfsl;
		result.dienststellenschluessel = typeof obj.dienststellenschluessel === "undefined" ? null : obj.dienststellenschluessel === null ? null : obj.dienststellenschluessel;
		result.ptb = typeof obj.ptb === "undefined" ? null : obj.ptb === null ? null : obj.ptb;
		result.internatsbetrieb = typeof obj.internatsbetrieb === "undefined" ? null : obj.internatsbetrieb === null ? null : obj.internatsbetrieb;
		result.internatsplaetze = typeof obj.internatsplaetze === "undefined" ? null : obj.internatsplaetze === null ? null : obj.internatsplaetze;
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if ((obj.schulform !== undefined) && (obj.schulform !== null)) {
			for (const elem of obj.schulform) {
				result.schulform?.add(SchuldateiOrganisationseinheitSchulform.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGrunddaten) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung!) + ',';
		result += '"rechtsstatus" : ' + ((!obj.rechtsstatus) ? 'null' : JSON.stringify(obj.rechtsstatus)) + ',';
		result += '"schultraegernummer" : ' + ((!obj.schultraegernummer) ? 'null' : JSON.stringify(obj.schultraegernummer)) + ',';
		result += '"artdertraegerschaft" : ' + ((!obj.artdertraegerschaft) ? 'null' : JSON.stringify(obj.artdertraegerschaft)) + ',';
		result += '"schulbetriebsschluessel" : ' + ((!obj.schulbetriebsschluessel) ? 'null' : JSON.stringify(obj.schulbetriebsschluessel)) + ',';
		result += '"kapitel" : ' + ((!obj.kapitel) ? 'null' : JSON.stringify(obj.kapitel)) + ',';
		result += '"obereschulaufsicht" : ' + ((!obj.obereschulaufsicht) ? 'null' : JSON.stringify(obj.obereschulaufsicht)) + ',';
		result += '"untereschulaufsicht" : ' + ((!obj.untereschulaufsicht) ? 'null' : JSON.stringify(obj.untereschulaufsicht)) + ',';
		result += '"zfsl" : ' + ((!obj.zfsl) ? 'null' : JSON.stringify(obj.zfsl)) + ',';
		result += '"dienststellenschluessel" : ' + ((!obj.dienststellenschluessel) ? 'null' : JSON.stringify(obj.dienststellenschluessel)) + ',';
		result += '"ptb" : ' + ((!obj.ptb) ? 'null' : JSON.stringify(obj.ptb)) + ',';
		result += '"internatsbetrieb" : ' + ((!obj.internatsbetrieb) ? 'null' : JSON.stringify(obj.internatsbetrieb)) + ',';
		result += '"internatsplaetze" : ' + ((!obj.internatsplaetze) ? 'null' : obj.internatsplaetze) + ',';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		if (!obj.schulform) {
			result += '"schulform" : []';
		} else {
			result += '"schulform" : [ ';
			for (let i = 0; i < obj.schulform.size(); i++) {
				const elem = obj.schulform.get(i);
				result += SchuldateiOrganisationseinheitSchulform.transpilerToJSON(elem);
				if (i < obj.schulform.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitGrunddaten>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (typeof obj.kurzbezeichnung !== "undefined") {
			result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung!) + ',';
		}
		if (typeof obj.rechtsstatus !== "undefined") {
			result += '"rechtsstatus" : ' + ((!obj.rechtsstatus) ? 'null' : JSON.stringify(obj.rechtsstatus)) + ',';
		}
		if (typeof obj.schultraegernummer !== "undefined") {
			result += '"schultraegernummer" : ' + ((!obj.schultraegernummer) ? 'null' : JSON.stringify(obj.schultraegernummer)) + ',';
		}
		if (typeof obj.artdertraegerschaft !== "undefined") {
			result += '"artdertraegerschaft" : ' + ((!obj.artdertraegerschaft) ? 'null' : JSON.stringify(obj.artdertraegerschaft)) + ',';
		}
		if (typeof obj.schulbetriebsschluessel !== "undefined") {
			result += '"schulbetriebsschluessel" : ' + ((!obj.schulbetriebsschluessel) ? 'null' : JSON.stringify(obj.schulbetriebsschluessel)) + ',';
		}
		if (typeof obj.kapitel !== "undefined") {
			result += '"kapitel" : ' + ((!obj.kapitel) ? 'null' : JSON.stringify(obj.kapitel)) + ',';
		}
		if (typeof obj.obereschulaufsicht !== "undefined") {
			result += '"obereschulaufsicht" : ' + ((!obj.obereschulaufsicht) ? 'null' : JSON.stringify(obj.obereschulaufsicht)) + ',';
		}
		if (typeof obj.untereschulaufsicht !== "undefined") {
			result += '"untereschulaufsicht" : ' + ((!obj.untereschulaufsicht) ? 'null' : JSON.stringify(obj.untereschulaufsicht)) + ',';
		}
		if (typeof obj.zfsl !== "undefined") {
			result += '"zfsl" : ' + ((!obj.zfsl) ? 'null' : JSON.stringify(obj.zfsl)) + ',';
		}
		if (typeof obj.dienststellenschluessel !== "undefined") {
			result += '"dienststellenschluessel" : ' + ((!obj.dienststellenschluessel) ? 'null' : JSON.stringify(obj.dienststellenschluessel)) + ',';
		}
		if (typeof obj.ptb !== "undefined") {
			result += '"ptb" : ' + ((!obj.ptb) ? 'null' : JSON.stringify(obj.ptb)) + ',';
		}
		if (typeof obj.internatsbetrieb !== "undefined") {
			result += '"internatsbetrieb" : ' + ((!obj.internatsbetrieb) ? 'null' : JSON.stringify(obj.internatsbetrieb)) + ',';
		}
		if (typeof obj.internatsplaetze !== "undefined") {
			result += '"internatsplaetze" : ' + ((!obj.internatsplaetze) ? 'null' : obj.internatsplaetze) + ',';
		}
		if (typeof obj.gueltigab !== "undefined") {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (typeof obj.gueltigbis !== "undefined") {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (typeof obj.geaendertam !== "undefined") {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (typeof obj.schulform !== "undefined") {
			if (!obj.schulform) {
				result += '"schulform" : []';
			} else {
				result += '"schulform" : [ ';
				for (let i = 0; i < obj.schulform.size(); i++) {
					const elem = obj.schulform.get(i);
					result += SchuldateiOrganisationseinheitSchulform.transpilerToJSON(elem);
					if (i < obj.schulform.size() - 1)
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

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitGrunddaten(obj : unknown) : SchuldateiOrganisationseinheitGrunddaten {
	return obj as SchuldateiOrganisationseinheitGrunddaten;
}
