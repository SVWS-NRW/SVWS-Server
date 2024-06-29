import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitEigenschaft extends SchuldateiEintrag {

	/**
	 * Die ID des Eigenschafts-Eintrags.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Eigenschaftsnummer
	 */
	public eigenschaft : string = "";

	/**
	 * Beschreibung
	 */
	public Beschreibung : string | null = null;

	/**
	 * Detail
	 */
	public detail : string | null = null;


	/**
	 * Erstellt eine neue weitere Eigenschaft einer Organiationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiEintrag', 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitEigenschaft'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitEigenschaft {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitEigenschaft();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.schulnummer === undefined)
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.eigenschaft === undefined)
			 throw new Error('invalid json format, missing attribute eigenschaft');
		result.eigenschaft = obj.eigenschaft;
		result.Beschreibung = (obj.Beschreibung === undefined) ? null : obj.Beschreibung === null ? null : obj.Beschreibung;
		result.detail = (obj.detail === undefined) ? null : obj.detail === null ? null : obj.detail;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitEigenschaft) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"eigenschaft" : ' + JSON.stringify(obj.eigenschaft!) + ',';
		result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : JSON.stringify(obj.Beschreibung)) + ',';
		result += '"detail" : ' + ((!obj.detail) ? 'null' : JSON.stringify(obj.detail)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitEigenschaft>) : string {
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
		if (obj.eigenschaft !== undefined) {
			result += '"eigenschaft" : ' + JSON.stringify(obj.eigenschaft!) + ',';
		}
		if (obj.Beschreibung !== undefined) {
			result += '"Beschreibung" : ' + ((!obj.Beschreibung) ? 'null' : JSON.stringify(obj.Beschreibung)) + ',';
		}
		if (obj.detail !== undefined) {
			result += '"detail" : ' + ((!obj.detail) ? 'null' : JSON.stringify(obj.detail)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitEigenschaft(obj : unknown) : SchuldateiOrganisationseinheitEigenschaft {
	return obj as SchuldateiOrganisationseinheitEigenschaft;
}
