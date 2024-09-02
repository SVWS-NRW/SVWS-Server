import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitGrunddaten } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitGrunddaten';
import { SchuldateiOrganisationseinheitEigenschaft } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitEigenschaft';
import { SchuldateiOrganisationseinheitMerkmal } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitMerkmal';
import { SchuldateiOrganisationseinheitAdresse } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitAdresse';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheitGliederung } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitGliederung';
import type { List } from '../../../java/util/List';
import { SchuldateiOrganisationseinheitErreichbarkeit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitErreichbarkeit';

export class SchuldateiOrganisationseinheit extends JavaObject {

	/**
	 * Die Schulnummer der Organisationseinheit.
	 */
	public schulnummer : string = "";

	/**
	 * Die Bundeslandkennung (NRW)
	 */
	public bundeslandkennung : string = "";

	/**
	 * Die eindeutige Identifier für das XSCHULE-Format
	 */
	public xscid : string = "";

	/**
	 * Die Art der Organisationseinheit
	 */
	public oeart : string = "";

	/**
	 * Die Amtsbezeichnung der Organisationseinheit
	 */
	public amtsbez1 : string = "";

	/**
	 * Die Amtsbezeichnung der Organisationseinheit
	 */
	public amtsbez2 : string = "";

	/**
	 * Die Amtsbezeichnung der Organisationseinheit
	 */
	public amtsbez3 : string = "";

	/**
	 * Das Errichtungsdatum der Schule.
	 */
	public errichtung : string = "";

	/**
	 * Das Aufloesungsdatum der Schule.
	 */
	public aufloesung : string = "";

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
	 * Die Gliederungen der Organisationseinheit-Schule (zeitl. Verlaufsliste)
	 */
	public readonly gliederung : List<SchuldateiOrganisationseinheitGliederung> = new ArrayList<SchuldateiOrganisationseinheitGliederung>();

	/**
	 * Die Eigenschaften der Organisationseinheit
	 */
	public readonly oeEigenschaften : List<SchuldateiOrganisationseinheitEigenschaft> = new ArrayList<SchuldateiOrganisationseinheitEigenschaft>();


	/**
	 * Erstellt eine neue Organiationseinheit für die Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheit'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheit {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheit>;
		const result = new SchuldateiOrganisationseinheit();
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.bundeslandkennung === undefined)
			throw new Error('invalid json format, missing attribute bundeslandkennung');
		result.bundeslandkennung = obj.bundeslandkennung;
		if (obj.xscid === undefined)
			throw new Error('invalid json format, missing attribute xscid');
		result.xscid = obj.xscid;
		if (obj.oeart === undefined)
			throw new Error('invalid json format, missing attribute oeart');
		result.oeart = obj.oeart;
		if (obj.amtsbez1 === undefined)
			throw new Error('invalid json format, missing attribute amtsbez1');
		result.amtsbez1 = obj.amtsbez1;
		if (obj.amtsbez2 === undefined)
			throw new Error('invalid json format, missing attribute amtsbez2');
		result.amtsbez2 = obj.amtsbez2;
		if (obj.amtsbez3 === undefined)
			throw new Error('invalid json format, missing attribute amtsbez3');
		result.amtsbez3 = obj.amtsbez3;
		if (obj.errichtung === undefined)
			throw new Error('invalid json format, missing attribute errichtung');
		result.errichtung = obj.errichtung;
		if (obj.aufloesung === undefined)
			throw new Error('invalid json format, missing attribute aufloesung');
		result.aufloesung = obj.aufloesung;
		if (obj.grunddaten !== undefined) {
			for (const elem of obj.grunddaten) {
				result.grunddaten.add(SchuldateiOrganisationseinheitGrunddaten.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.adressen !== undefined) {
			for (const elem of obj.adressen) {
				result.adressen.add(SchuldateiOrganisationseinheitAdresse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.merkmal !== undefined) {
			for (const elem of obj.merkmal) {
				result.merkmal.add(SchuldateiOrganisationseinheitMerkmal.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.erreichbarkeiten !== undefined) {
			for (const elem of obj.erreichbarkeiten) {
				result.erreichbarkeiten.add(SchuldateiOrganisationseinheitErreichbarkeit.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.gliederung !== undefined) {
			for (const elem of obj.gliederung) {
				result.gliederung.add(SchuldateiOrganisationseinheitGliederung.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.oeEigenschaften !== undefined) {
			for (const elem of obj.oeEigenschaften) {
				result.oeEigenschaften.add(SchuldateiOrganisationseinheitEigenschaft.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheit) : string {
		let result = '{';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"bundeslandkennung" : ' + JSON.stringify(obj.bundeslandkennung) + ',';
		result += '"xscid" : ' + JSON.stringify(obj.xscid) + ',';
		result += '"oeart" : ' + JSON.stringify(obj.oeart) + ',';
		result += '"amtsbez1" : ' + JSON.stringify(obj.amtsbez1) + ',';
		result += '"amtsbez2" : ' + JSON.stringify(obj.amtsbez2) + ',';
		result += '"amtsbez3" : ' + JSON.stringify(obj.amtsbez3) + ',';
		result += '"errichtung" : ' + JSON.stringify(obj.errichtung) + ',';
		result += '"aufloesung" : ' + JSON.stringify(obj.aufloesung) + ',';
		result += '"grunddaten" : [ ';
		for (let i = 0; i < obj.grunddaten.size(); i++) {
			const elem = obj.grunddaten.get(i);
			result += SchuldateiOrganisationseinheitGrunddaten.transpilerToJSON(elem);
			if (i < obj.grunddaten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"adressen" : [ ';
		for (let i = 0; i < obj.adressen.size(); i++) {
			const elem = obj.adressen.get(i);
			result += SchuldateiOrganisationseinheitAdresse.transpilerToJSON(elem);
			if (i < obj.adressen.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"merkmal" : [ ';
		for (let i = 0; i < obj.merkmal.size(); i++) {
			const elem = obj.merkmal.get(i);
			result += SchuldateiOrganisationseinheitMerkmal.transpilerToJSON(elem);
			if (i < obj.merkmal.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"erreichbarkeiten" : [ ';
		for (let i = 0; i < obj.erreichbarkeiten.size(); i++) {
			const elem = obj.erreichbarkeiten.get(i);
			result += SchuldateiOrganisationseinheitErreichbarkeit.transpilerToJSON(elem);
			if (i < obj.erreichbarkeiten.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"gliederung" : [ ';
		for (let i = 0; i < obj.gliederung.size(); i++) {
			const elem = obj.gliederung.get(i);
			result += SchuldateiOrganisationseinheitGliederung.transpilerToJSON(elem);
			if (i < obj.gliederung.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"oeEigenschaften" : [ ';
		for (let i = 0; i < obj.oeEigenschaften.size(); i++) {
			const elem = obj.oeEigenschaften.get(i);
			result += SchuldateiOrganisationseinheitEigenschaft.transpilerToJSON(elem);
			if (i < obj.oeEigenschaften.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheit>) : string {
		let result = '{';
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.bundeslandkennung !== undefined) {
			result += '"bundeslandkennung" : ' + JSON.stringify(obj.bundeslandkennung) + ',';
		}
		if (obj.xscid !== undefined) {
			result += '"xscid" : ' + JSON.stringify(obj.xscid) + ',';
		}
		if (obj.oeart !== undefined) {
			result += '"oeart" : ' + JSON.stringify(obj.oeart) + ',';
		}
		if (obj.amtsbez1 !== undefined) {
			result += '"amtsbez1" : ' + JSON.stringify(obj.amtsbez1) + ',';
		}
		if (obj.amtsbez2 !== undefined) {
			result += '"amtsbez2" : ' + JSON.stringify(obj.amtsbez2) + ',';
		}
		if (obj.amtsbez3 !== undefined) {
			result += '"amtsbez3" : ' + JSON.stringify(obj.amtsbez3) + ',';
		}
		if (obj.errichtung !== undefined) {
			result += '"errichtung" : ' + JSON.stringify(obj.errichtung) + ',';
		}
		if (obj.aufloesung !== undefined) {
			result += '"aufloesung" : ' + JSON.stringify(obj.aufloesung) + ',';
		}
		if (obj.grunddaten !== undefined) {
			result += '"grunddaten" : [ ';
			for (let i = 0; i < obj.grunddaten.size(); i++) {
				const elem = obj.grunddaten.get(i);
				result += SchuldateiOrganisationseinheitGrunddaten.transpilerToJSON(elem);
				if (i < obj.grunddaten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.adressen !== undefined) {
			result += '"adressen" : [ ';
			for (let i = 0; i < obj.adressen.size(); i++) {
				const elem = obj.adressen.get(i);
				result += SchuldateiOrganisationseinheitAdresse.transpilerToJSON(elem);
				if (i < obj.adressen.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.merkmal !== undefined) {
			result += '"merkmal" : [ ';
			for (let i = 0; i < obj.merkmal.size(); i++) {
				const elem = obj.merkmal.get(i);
				result += SchuldateiOrganisationseinheitMerkmal.transpilerToJSON(elem);
				if (i < obj.merkmal.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.erreichbarkeiten !== undefined) {
			result += '"erreichbarkeiten" : [ ';
			for (let i = 0; i < obj.erreichbarkeiten.size(); i++) {
				const elem = obj.erreichbarkeiten.get(i);
				result += SchuldateiOrganisationseinheitErreichbarkeit.transpilerToJSON(elem);
				if (i < obj.erreichbarkeiten.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.gliederung !== undefined) {
			result += '"gliederung" : [ ';
			for (let i = 0; i < obj.gliederung.size(); i++) {
				const elem = obj.gliederung.get(i);
				result += SchuldateiOrganisationseinheitGliederung.transpilerToJSON(elem);
				if (i < obj.gliederung.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.oeEigenschaften !== undefined) {
			result += '"oeEigenschaften" : [ ';
			for (let i = 0; i < obj.oeEigenschaften.size(); i++) {
				const elem = obj.oeEigenschaften.get(i);
				result += SchuldateiOrganisationseinheitEigenschaft.transpilerToJSON(elem);
				if (i < obj.oeEigenschaften.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheit(obj : unknown) : SchuldateiOrganisationseinheit {
	return obj as SchuldateiOrganisationseinheit;
}
