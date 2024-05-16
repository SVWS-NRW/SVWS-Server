import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuldateiOrganisationseinheitGliederung extends JavaObject {

	/**
	 * Die ID des Gliederungs-Eintrages.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Gliederung
	 */
	public gliederung : string | null = null;

	/**
	 * Der Förderschwerpunkt
	 */
	public foerderschwerpunkt : string | null = null;

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
	 * Erstellt eine neue Gliederung für eine Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGliederung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitGliederung'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitGliederung {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitGliederung();
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : obj.id;
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.gliederung = typeof obj.gliederung === "undefined" ? null : obj.gliederung === null ? null : obj.gliederung;
		result.foerderschwerpunkt = typeof obj.foerderschwerpunkt === "undefined" ? null : obj.foerderschwerpunkt === null ? null : obj.foerderschwerpunkt;
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitGliederung) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		result += '"foerderschwerpunkt" : ' + ((!obj.foerderschwerpunkt) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitGliederung>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (typeof obj.gliederung !== "undefined") {
			result += '"gliederung" : ' + ((!obj.gliederung) ? 'null' : JSON.stringify(obj.gliederung)) + ',';
		}
		if (typeof obj.foerderschwerpunkt !== "undefined") {
			result += '"foerderschwerpunkt" : ' + ((!obj.foerderschwerpunkt) ? 'null' : JSON.stringify(obj.foerderschwerpunkt)) + ',';
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
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitGliederung(obj : unknown) : SchuldateiOrganisationseinheitGliederung {
	return obj as SchuldateiOrganisationseinheitGliederung;
}
