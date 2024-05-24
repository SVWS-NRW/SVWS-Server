import { SchuldateiEintrag } from '../../../schuldatei/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitSchulform extends SchuldateiEintrag {

	/**
	 * Die ID des Schulform-Eintrages.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Schulformcode
	 */
	public schulformcode : string = "";

	/**
	 * Schulformwert
	 */
	public schulformwert : string = "";


	/**
	 * Erstellt eine neue Schulform für eine Organiationseinheit der Schuldatei
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitSchulform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag', 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitSchulform'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitSchulform {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitSchulform();
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : obj.id;
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (typeof obj.schulformcode === "undefined")
			 throw new Error('invalid json format, missing attribute schulformcode');
		result.schulformcode = obj.schulformcode;
		if (typeof obj.schulformwert === "undefined")
			 throw new Error('invalid json format, missing attribute schulformwert');
		result.schulformwert = obj.schulformwert;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitSchulform) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"schulformcode" : ' + JSON.stringify(obj.schulformcode!) + ',';
		result += '"schulformwert" : ' + JSON.stringify(obj.schulformwert!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitSchulform>) : string {
		let result = '{';
		if (typeof obj.gueltigab !== "undefined") {
			result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		}
		if (typeof obj.gueltigbis !== "undefined") {
			result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		}
		if (typeof obj.geaendertam !== "undefined") {
			result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		}
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (typeof obj.schulformcode !== "undefined") {
			result += '"schulformcode" : ' + JSON.stringify(obj.schulformcode!) + ',';
		}
		if (typeof obj.schulformwert !== "undefined") {
			result += '"schulformwert" : ' + JSON.stringify(obj.schulformwert!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitSchulform(obj : unknown) : SchuldateiOrganisationseinheitSchulform {
	return obj as SchuldateiOrganisationseinheitSchulform;
}
