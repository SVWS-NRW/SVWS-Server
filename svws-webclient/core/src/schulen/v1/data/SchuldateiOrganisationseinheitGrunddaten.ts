import { SchuldateiOrganisationseinheitSchulform } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitSchulform';
import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';

export class SchuldateiOrganisationseinheitGrunddaten extends SchuldateiEintrag {

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
	public rechtsstatus : number = 0;

	/**
	 * Schulträgernummer der Organisationseinheit
	 */
	public schultraegernummer : number = 0;

	/**
	 * Art der Trägerschaft der Schule
	 */
	public artdertraegerschaft : number = 0;

	/**
	 * Betriebsschlüssel der Schule
	 */
	public schulbetriebsschluessel : number = 0;

	/**
	 * Kapitel der Schule
	 */
	public kapitel : number = 0;

	/**
	 * Obere Schulaufsicht der Schule
	 */
	public obereschulaufsicht : number = 0;

	/**
	 * Untere Schulaufsicht der Schule
	 */
	public untereschulaufsicht : number = 0;

	/**
	 * Zentrum für schulpraktische Lehrerausbildung ZFSL
	 */
	public zfsl : number = 0;

	/**
	 * Dienststellenschlüssel bzw. Personalbereich der Organisationseinheit
	 */
	public dienststellenschluessel : number = 0;

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

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGrunddaten {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitGrunddaten();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
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
		result.ptb = (obj.ptb === undefined) ? null : obj.ptb === null ? null : obj.ptb;
		result.internatsbetrieb = (obj.internatsbetrieb === undefined) ? null : obj.internatsbetrieb === null ? null : obj.internatsbetrieb;
		result.internatsplaetze = (obj.internatsplaetze === undefined) ? null : obj.internatsplaetze === null ? null : obj.internatsplaetze;
		if ((obj.schulform !== undefined) && (obj.schulform !== null)) {
			for (const elem of obj.schulform) {
				result.schulform?.add(SchuldateiOrganisationseinheitSchulform.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGrunddaten) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer + ',';
		result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung!) + ',';
		result += '"rechtsstatus" : ' + obj.rechtsstatus + ',';
		result += '"schultraegernummer" : ' + obj.schultraegernummer + ',';
		result += '"artdertraegerschaft" : ' + obj.artdertraegerschaft + ',';
		result += '"schulbetriebsschluessel" : ' + obj.schulbetriebsschluessel + ',';
		result += '"kapitel" : ' + obj.kapitel + ',';
		result += '"obereschulaufsicht" : ' + obj.obereschulaufsicht + ',';
		result += '"untereschulaufsicht" : ' + obj.untereschulaufsicht + ',';
		result += '"zfsl" : ' + obj.zfsl + ',';
		result += '"dienststellenschluessel" : ' + obj.dienststellenschluessel + ',';
		result += '"ptb" : ' + ((!obj.ptb) ? 'null' : JSON.stringify(obj.ptb)) + ',';
		result += '"internatsbetrieb" : ' + ((!obj.internatsbetrieb) ? 'null' : JSON.stringify(obj.internatsbetrieb)) + ',';
		result += '"internatsplaetze" : ' + ((!obj.internatsplaetze) ? 'null' : obj.internatsplaetze) + ',';
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
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (obj.kurzbezeichnung !== undefined) {
			result += '"kurzbezeichnung" : ' + JSON.stringify(obj.kurzbezeichnung!) + ',';
		}
		if (obj.rechtsstatus !== undefined) {
			result += '"rechtsstatus" : ' + obj.rechtsstatus + ',';
		}
		if (obj.schultraegernummer !== undefined) {
			result += '"schultraegernummer" : ' + obj.schultraegernummer + ',';
		}
		if (obj.artdertraegerschaft !== undefined) {
			result += '"artdertraegerschaft" : ' + obj.artdertraegerschaft + ',';
		}
		if (obj.schulbetriebsschluessel !== undefined) {
			result += '"schulbetriebsschluessel" : ' + obj.schulbetriebsschluessel + ',';
		}
		if (obj.kapitel !== undefined) {
			result += '"kapitel" : ' + obj.kapitel + ',';
		}
		if (obj.obereschulaufsicht !== undefined) {
			result += '"obereschulaufsicht" : ' + obj.obereschulaufsicht + ',';
		}
		if (obj.untereschulaufsicht !== undefined) {
			result += '"untereschulaufsicht" : ' + obj.untereschulaufsicht + ',';
		}
		if (obj.zfsl !== undefined) {
			result += '"zfsl" : ' + obj.zfsl + ',';
		}
		if (obj.dienststellenschluessel !== undefined) {
			result += '"dienststellenschluessel" : ' + obj.dienststellenschluessel + ',';
		}
		if (obj.ptb !== undefined) {
			result += '"ptb" : ' + ((!obj.ptb) ? 'null' : JSON.stringify(obj.ptb)) + ',';
		}
		if (obj.internatsbetrieb !== undefined) {
			result += '"internatsbetrieb" : ' + ((!obj.internatsbetrieb) ? 'null' : JSON.stringify(obj.internatsbetrieb)) + ',';
		}
		if (obj.internatsplaetze !== undefined) {
			result += '"internatsplaetze" : ' + ((!obj.internatsplaetze) ? 'null' : obj.internatsplaetze) + ',';
		}
		if (obj.schulform !== undefined) {
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

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitGrunddaten(obj : unknown) : SchuldateiOrganisationseinheitGrunddaten {
	return obj as SchuldateiOrganisationseinheitGrunddaten;
}
