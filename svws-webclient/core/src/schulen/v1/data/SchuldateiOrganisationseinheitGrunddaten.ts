import { SchuldateiOrganisationseinheitSchulform } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitSchulform';
import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchuldateiOrganisationseinheitGrunddaten extends SchuldateiEintrag {

	/**
	 * Die ID der Grunddaten.
	 */
	public id : string = "";

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Die Kurzbezeichnung der Organisationseinheit
	 */
	public kurzbezeichnung : string = "";

	/**
	 * Der Rechtsstatus der Organisationseinheit 1=öffentlich, 2=privat
	 */
	public rechtsstatus : string = "";

	/**
	 * Schulträgernummer der Organisationseinheit
	 */
	public schultraegernummer : string = "";

	/**
	 * Art der Trägerschaft der Schule
	 */
	public artdertraegerschaft : string = "";

	/**
	 * Betriebsschlüssel der Schule
	 */
	public schulbetriebsschluessel : string = "";

	/**
	 * Kapitel der Schule
	 */
	public kapitel : string = "";

	/**
	 * Obere Schulaufsicht der Schule
	 */
	public obereschulaufsicht : string = "";

	/**
	 * Untere Schulaufsicht der Schule
	 */
	public untereschulaufsicht : string = "";

	/**
	 * Zentrum für schulpraktische Lehrerausbildung ZFSL
	 */
	public zfsl : string = "";

	/**
	 * Dienststellenschlüssel bzw. Personalbereich der Organisationseinheit
	 */
	public dienststellenschluessel : string = "";

	/**
	 * Personalteilbereich der Organisationseinheit
	 */
	public ptb : string = "";

	/**
	 * Gibt an ob die Schule Internatsbetrieb hat
	 */
	public internatsbetrieb : string = "";

	/**
	 * Anzahl der Internatsplätze
	 */
	public internatsplaetze : number = 0;

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
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGrunddaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiEintrag', 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGrunddaten'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitGrunddaten>('de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitGrunddaten');

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGrunddaten {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitGrunddaten>;
		const result = new SchuldateiOrganisationseinheitGrunddaten();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.kurzbezeichnung === undefined)
			throw new Error('invalid json format, missing attribute kurzbezeichnung');
		result.kurzbezeichnung = obj.kurzbezeichnung;
		if (obj.rechtsstatus === undefined)
			throw new Error('invalid json format, missing attribute rechtsstatus');
		result.rechtsstatus = obj.rechtsstatus;
		if (obj.schultraegernummer === undefined)
			throw new Error('invalid json format, missing attribute schultraegernummer');
		result.schultraegernummer = obj.schultraegernummer;
		if (obj.artdertraegerschaft === undefined)
			throw new Error('invalid json format, missing attribute artdertraegerschaft');
		result.artdertraegerschaft = obj.artdertraegerschaft;
		if (obj.schulbetriebsschluessel === undefined)
			throw new Error('invalid json format, missing attribute schulbetriebsschluessel');
		result.schulbetriebsschluessel = obj.schulbetriebsschluessel;
		if (obj.kapitel === undefined)
			throw new Error('invalid json format, missing attribute kapitel');
		result.kapitel = obj.kapitel;
		if (obj.obereschulaufsicht === undefined)
			throw new Error('invalid json format, missing attribute obereschulaufsicht');
		result.obereschulaufsicht = obj.obereschulaufsicht;
		if (obj.untereschulaufsicht === undefined)
			throw new Error('invalid json format, missing attribute untereschulaufsicht');
		result.untereschulaufsicht = obj.untereschulaufsicht;
		if (obj.zfsl === undefined)
			throw new Error('invalid json format, missing attribute zfsl');
		result.zfsl = obj.zfsl;
		if (obj.dienststellenschluessel === undefined)
			throw new Error('invalid json format, missing attribute dienststellenschluessel');
		result.dienststellenschluessel = obj.dienststellenschluessel;
		if (obj.ptb === undefined)
			throw new Error('invalid json format, missing attribute ptb');
		result.ptb = obj.ptb;
		if (obj.internatsbetrieb === undefined)
			throw new Error('invalid json format, missing attribute internatsbetrieb');
		result.internatsbetrieb = obj.internatsbetrieb;
		if (obj.internatsplaetze === undefined)
			throw new Error('invalid json format, missing attribute internatsplaetze');
		result.internatsplaetze = obj.internatsplaetze;
		if (obj.schulform !== undefined) {
			for (const elem of obj.schulform) {
				result.schulform.add(SchuldateiOrganisationseinheitSchulform.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGrunddaten) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + JSON.stringify(obj.id) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung) + ',';
		result += '"rechtsstatus" : ' + JSON.stringify(obj.rechtsstatus) + ',';
		result += '"schultraegernummer" : ' + JSON.stringify(obj.schultraegernummer) + ',';
		result += '"artdertraegerschaft" : ' + JSON.stringify(obj.artdertraegerschaft) + ',';
		result += '"schulbetriebsschluessel" : ' + JSON.stringify(obj.schulbetriebsschluessel) + ',';
		result += '"kapitel" : ' + JSON.stringify(obj.kapitel) + ',';
		result += '"obereschulaufsicht" : ' + JSON.stringify(obj.obereschulaufsicht) + ',';
		result += '"untereschulaufsicht" : ' + JSON.stringify(obj.untereschulaufsicht) + ',';
		result += '"zfsl" : ' + JSON.stringify(obj.zfsl) + ',';
		result += '"dienststellenschluessel" : ' + JSON.stringify(obj.dienststellenschluessel) + ',';
		result += '"ptb" : ' + JSON.stringify(obj.ptb) + ',';
		result += '"internatsbetrieb" : ' + JSON.stringify(obj.internatsbetrieb) + ',';
		result += '"internatsplaetze" : ' + obj.internatsplaetze.toString() + ',';
		result += '"schulform" : [ ';
		for (let i = 0; i < obj.schulform.size(); i++) {
			const elem = obj.schulform.get(i);
			result += SchuldateiOrganisationseinheitSchulform.transpilerToJSON(elem);
			if (i < obj.schulform.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitGrunddaten>) : string {
		let result = '{';
		if (obj.gueltigab !== undefined) {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (obj.gueltigbis !== undefined) {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (obj.geaendertam !== undefined) {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (obj.id !== undefined) {
			result += '"id" : ' + JSON.stringify(obj.id) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.kurzbezeichnung !== undefined) {
			result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung) + ',';
		}
		if (obj.rechtsstatus !== undefined) {
			result += '"rechtsstatus" : ' + JSON.stringify(obj.rechtsstatus) + ',';
		}
		if (obj.schultraegernummer !== undefined) {
			result += '"schultraegernummer" : ' + JSON.stringify(obj.schultraegernummer) + ',';
		}
		if (obj.artdertraegerschaft !== undefined) {
			result += '"artdertraegerschaft" : ' + JSON.stringify(obj.artdertraegerschaft) + ',';
		}
		if (obj.schulbetriebsschluessel !== undefined) {
			result += '"schulbetriebsschluessel" : ' + JSON.stringify(obj.schulbetriebsschluessel) + ',';
		}
		if (obj.kapitel !== undefined) {
			result += '"kapitel" : ' + JSON.stringify(obj.kapitel) + ',';
		}
		if (obj.obereschulaufsicht !== undefined) {
			result += '"obereschulaufsicht" : ' + JSON.stringify(obj.obereschulaufsicht) + ',';
		}
		if (obj.untereschulaufsicht !== undefined) {
			result += '"untereschulaufsicht" : ' + JSON.stringify(obj.untereschulaufsicht) + ',';
		}
		if (obj.zfsl !== undefined) {
			result += '"zfsl" : ' + JSON.stringify(obj.zfsl) + ',';
		}
		if (obj.dienststellenschluessel !== undefined) {
			result += '"dienststellenschluessel" : ' + JSON.stringify(obj.dienststellenschluessel) + ',';
		}
		if (obj.ptb !== undefined) {
			result += '"ptb" : ' + JSON.stringify(obj.ptb) + ',';
		}
		if (obj.internatsbetrieb !== undefined) {
			result += '"internatsbetrieb" : ' + JSON.stringify(obj.internatsbetrieb) + ',';
		}
		if (obj.internatsplaetze !== undefined) {
			result += '"internatsplaetze" : ' + obj.internatsplaetze.toString() + ',';
		}
		if (obj.schulform !== undefined) {
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

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitGrunddaten(obj : unknown) : SchuldateiOrganisationseinheitGrunddaten {
	return obj as SchuldateiOrganisationseinheitGrunddaten;
}
