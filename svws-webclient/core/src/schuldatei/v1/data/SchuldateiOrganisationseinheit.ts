import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitGrunddaten } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitGrunddaten';
import { SchuldateiOrganisationseinheitEigenschaft } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitEigenschaft';
import { SchuldateiOrganisationseinheitMerkmal } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitMerkmal';
import { SchuldateiOrganisationseinheitAdresse } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitAdresse';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheitGliederung } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitGliederung';
import type { List } from '../../../java/util/List';
import { SchuldateiOrganisationseinheitErreichbarkeit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitErreichbarkeit';

export class SchuldateiOrganisationseinheit extends JavaObject {

	/**
	 * Die Schulnummer der Organisationseinheit.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Bundeslandkennung (NRW)
	 */
	public bundeslandkennung : string | null = null;

	/**
	 * Die eindeutige Identifier für das XSCHULE-Format
	 */
	public xscid : string | null = null;

	/**
	 * Die Art der Organisationseinheit
	 */
	public oeart : string | null = null;

	/**
	 * Die Amtsbezeichnung der Organisationseinheit
	 */
	public amtsbez : string = "";

	/**
	 * Das Errichtungsdatum der Schule.
	 */
	public errichtung : string | null = null;

	/**
	 * Das Aufloesungsdatum der Schule.
	 */
	public aufloesung : string | null = null;

	/**
	 * Die Grunddaten der Organisationseinheit (zeitl. Verlaufsliste)
	 */
	public readonly grunddaten : List<SchuldateiOrganisationseinheitGrunddaten> = new ArrayList<SchuldateiOrganisationseinheitGrunddaten>();

	/**
	 * Die Adressen der Organisationseinheit (zeitl. Verlaufsliste)
	 */
	public readonly adressen : List<SchuldateiOrganisationseinheitAdresse> = new ArrayList<SchuldateiOrganisationseinheitAdresse>();

	/**
	 * Die Merkmale der Organisationseinheit (zeitl. Verlaufsliste)
	 */
	public readonly merkmal : List<SchuldateiOrganisationseinheitMerkmal> = new ArrayList<SchuldateiOrganisationseinheitMerkmal>();

	/**
	 * Die Erreichbarkeiten der Organisationseinheit (zeitl. Verlaufsliste)
	 */
	public readonly erreichbarkeiten : List<SchuldateiOrganisationseinheitErreichbarkeit> = new ArrayList<SchuldateiOrganisationseinheitErreichbarkeit>();

	/**
	 * Die Eigenschaften der Organisationseinheit
	 */
	public readonly oe_eigenschaften : List<SchuldateiOrganisationseinheitEigenschaft> = new ArrayList<SchuldateiOrganisationseinheitEigenschaft>();

	/**
	 * Die Gliederungen der Organisationseinheit-Schule (zeitl. Verlaufsliste)
	 */
	public readonly gliederung : List<SchuldateiOrganisationseinheitGliederung> = new ArrayList<SchuldateiOrganisationseinheitGliederung>();


	/**
	 * Erstellt eine neue Organiationseinheit für die Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheit'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheit {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheit();
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.bundeslandkennung = typeof obj.bundeslandkennung === "undefined" ? null : obj.bundeslandkennung === null ? null : obj.bundeslandkennung;
		result.xscid = typeof obj.xscid === "undefined" ? null : obj.xscid === null ? null : obj.xscid;
		result.oeart = typeof obj.oeart === "undefined" ? null : obj.oeart === null ? null : obj.oeart;
		if (typeof obj.amtsbez === "undefined")
			 throw new Error('invalid json format, missing attribute amtsbez');
		result.amtsbez = obj.amtsbez;
		result.errichtung = typeof obj.errichtung === "undefined" ? null : obj.errichtung === null ? null : obj.errichtung;
		result.aufloesung = typeof obj.aufloesung === "undefined" ? null : obj.aufloesung === null ? null : obj.aufloesung;
		if ((obj.grunddaten !== undefined) && (obj.grunddaten !== null)) {
			for (const elem of obj.grunddaten) {
				result.grunddaten?.add(SchuldateiOrganisationseinheitGrunddaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.adressen !== undefined) && (obj.adressen !== null)) {
			for (const elem of obj.adressen) {
				result.adressen?.add(SchuldateiOrganisationseinheitAdresse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.merkmal !== undefined) && (obj.merkmal !== null)) {
			for (const elem of obj.merkmal) {
				result.merkmal?.add(SchuldateiOrganisationseinheitMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.erreichbarkeiten !== undefined) && (obj.erreichbarkeiten !== null)) {
			for (const elem of obj.erreichbarkeiten) {
				result.erreichbarkeiten?.add(SchuldateiOrganisationseinheitErreichbarkeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.oe_eigenschaften !== undefined) && (obj.oe_eigenschaften !== null)) {
			for (const elem of obj.oe_eigenschaften) {
				result.oe_eigenschaften?.add(SchuldateiOrganisationseinheitEigenschaft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if ((obj.gliederung !== undefined) && (obj.gliederung !== null)) {
			for (const elem of obj.gliederung) {
				result.gliederung?.add(SchuldateiOrganisationseinheitGliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheit) : string {
		let result = '{';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"bundeslandkennung" : ' + ((!obj.bundeslandkennung) ? 'null' : JSON.stringify(obj.bundeslandkennung)) + ',';
		result += '"xscid" : ' + ((!obj.xscid) ? 'null' : JSON.stringify(obj.xscid)) + ',';
		result += '"oeart" : ' + ((!obj.oeart) ? 'null' : JSON.stringify(obj.oeart)) + ',';
		result += '"amtsbez" : ' + JSON.stringify(obj.amtsbez!) + ',';
		result += '"errichtung" : ' + ((!obj.errichtung) ? 'null' : JSON.stringify(obj.errichtung)) + ',';
		result += '"aufloesung" : ' + ((!obj.aufloesung) ? 'null' : JSON.stringify(obj.aufloesung)) + ',';
		if (!obj.grunddaten) {
			result += '"grunddaten" : []';
		} else {
			result += '"grunddaten" : [ ';
			for (let i = 0; i < obj.grunddaten.size(); i++) {
				const elem = obj.grunddaten.get(i);
				result += SchuldateiOrganisationseinheitGrunddaten.transpilerToJSON(elem);
				if (i < obj.grunddaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.adressen) {
			result += '"adressen" : []';
		} else {
			result += '"adressen" : [ ';
			for (let i = 0; i < obj.adressen.size(); i++) {
				const elem = obj.adressen.get(i);
				result += SchuldateiOrganisationseinheitAdresse.transpilerToJSON(elem);
				if (i < obj.adressen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.merkmal) {
			result += '"merkmal" : []';
		} else {
			result += '"merkmal" : [ ';
			for (let i = 0; i < obj.merkmal.size(); i++) {
				const elem = obj.merkmal.get(i);
				result += SchuldateiOrganisationseinheitMerkmal.transpilerToJSON(elem);
				if (i < obj.merkmal.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.erreichbarkeiten) {
			result += '"erreichbarkeiten" : []';
		} else {
			result += '"erreichbarkeiten" : [ ';
			for (let i = 0; i < obj.erreichbarkeiten.size(); i++) {
				const elem = obj.erreichbarkeiten.get(i);
				result += SchuldateiOrganisationseinheitErreichbarkeit.transpilerToJSON(elem);
				if (i < obj.erreichbarkeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.oe_eigenschaften) {
			result += '"oe_eigenschaften" : []';
		} else {
			result += '"oe_eigenschaften" : [ ';
			for (let i = 0; i < obj.oe_eigenschaften.size(); i++) {
				const elem = obj.oe_eigenschaften.get(i);
				result += SchuldateiOrganisationseinheitEigenschaft.transpilerToJSON(elem);
				if (i < obj.oe_eigenschaften.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (!obj.gliederung) {
			result += '"gliederung" : []';
		} else {
			result += '"gliederung" : [ ';
			for (let i = 0; i < obj.gliederung.size(); i++) {
				const elem = obj.gliederung.get(i);
				result += SchuldateiOrganisationseinheitGliederung.transpilerToJSON(elem);
				if (i < obj.gliederung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheit>) : string {
		let result = '{';
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (typeof obj.bundeslandkennung !== "undefined") {
			result += '"bundeslandkennung" : ' + ((!obj.bundeslandkennung) ? 'null' : JSON.stringify(obj.bundeslandkennung)) + ',';
		}
		if (typeof obj.xscid !== "undefined") {
			result += '"xscid" : ' + ((!obj.xscid) ? 'null' : JSON.stringify(obj.xscid)) + ',';
		}
		if (typeof obj.oeart !== "undefined") {
			result += '"oeart" : ' + ((!obj.oeart) ? 'null' : JSON.stringify(obj.oeart)) + ',';
		}
		if (typeof obj.amtsbez !== "undefined") {
			result += '"amtsbez" : ' + JSON.stringify(obj.amtsbez!) + ',';
		}
		if (typeof obj.errichtung !== "undefined") {
			result += '"errichtung" : ' + ((!obj.errichtung) ? 'null' : JSON.stringify(obj.errichtung)) + ',';
		}
		if (typeof obj.aufloesung !== "undefined") {
			result += '"aufloesung" : ' + ((!obj.aufloesung) ? 'null' : JSON.stringify(obj.aufloesung)) + ',';
		}
		if (typeof obj.grunddaten !== "undefined") {
			if (!obj.grunddaten) {
				result += '"grunddaten" : []';
			} else {
				result += '"grunddaten" : [ ';
				for (let i = 0; i < obj.grunddaten.size(); i++) {
					const elem = obj.grunddaten.get(i);
					result += SchuldateiOrganisationseinheitGrunddaten.transpilerToJSON(elem);
					if (i < obj.grunddaten.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.adressen !== "undefined") {
			if (!obj.adressen) {
				result += '"adressen" : []';
			} else {
				result += '"adressen" : [ ';
				for (let i = 0; i < obj.adressen.size(); i++) {
					const elem = obj.adressen.get(i);
					result += SchuldateiOrganisationseinheitAdresse.transpilerToJSON(elem);
					if (i < obj.adressen.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.merkmal !== "undefined") {
			if (!obj.merkmal) {
				result += '"merkmal" : []';
			} else {
				result += '"merkmal" : [ ';
				for (let i = 0; i < obj.merkmal.size(); i++) {
					const elem = obj.merkmal.get(i);
					result += SchuldateiOrganisationseinheitMerkmal.transpilerToJSON(elem);
					if (i < obj.merkmal.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.erreichbarkeiten !== "undefined") {
			if (!obj.erreichbarkeiten) {
				result += '"erreichbarkeiten" : []';
			} else {
				result += '"erreichbarkeiten" : [ ';
				for (let i = 0; i < obj.erreichbarkeiten.size(); i++) {
					const elem = obj.erreichbarkeiten.get(i);
					result += SchuldateiOrganisationseinheitErreichbarkeit.transpilerToJSON(elem);
					if (i < obj.erreichbarkeiten.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.oe_eigenschaften !== "undefined") {
			if (!obj.oe_eigenschaften) {
				result += '"oe_eigenschaften" : []';
			} else {
				result += '"oe_eigenschaften" : [ ';
				for (let i = 0; i < obj.oe_eigenschaften.size(); i++) {
					const elem = obj.oe_eigenschaften.get(i);
					result += SchuldateiOrganisationseinheitEigenschaft.transpilerToJSON(elem);
					if (i < obj.oe_eigenschaften.size() - 1)
						result += ',';
				}
				result += ' ]' + ',';
			}
		}
		if (typeof obj.gliederung !== "undefined") {
			if (!obj.gliederung) {
				result += '"gliederung" : []';
			} else {
				result += '"gliederung" : [ ';
				for (let i = 0; i < obj.gliederung.size(); i++) {
					const elem = obj.gliederung.get(i);
					result += SchuldateiOrganisationseinheitGliederung.transpilerToJSON(elem);
					if (i < obj.gliederung.size() - 1)
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

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheit(obj : unknown) : SchuldateiOrganisationseinheit {
	return obj as SchuldateiOrganisationseinheit;
}
