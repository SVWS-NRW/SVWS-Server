import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitMerkmal extends SchuldateiEintrag {

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : string = "";

	/**
	 * Die ID des Merkmal-Eintrags.
	 */
	public id : number | null = null;

	/**
	 * Die Nummer der Liegenschaft der Organisationseinheit
	 */
	public liegenschaft : number = 0;

	/**
	 * Das Merkmal
	 */
	public merkmal : number = 0;

	/**
	 * Die Merkmalsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)
	 */
	public merkmalgruppe : number = 0;

	/**
	 * Das Attribut
	 */
	public attribut : number = 0;

	/**
	 * Die Attributsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)
	 */
	public attributgruppe : number = 0;

	/**
	 * Der Wert
	 */
	public wert : string = "";


	/**
	 * Erstellt ein neues Merkmal für eine Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitMerkmal';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.data.SchuldateiOrganisationseinheitMerkmal', 'de.svws_nrw.schulen.v1.data.SchuldateiEintrag'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitMerkmal {
		const obj = JSON.parse(json) as Partial<SchuldateiOrganisationseinheitMerkmal>;
		const result = new SchuldateiOrganisationseinheitMerkmal();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.schulnummer === undefined)
			throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.liegenschaft === undefined)
			throw new Error('invalid json format, missing attribute liegenschaft');
		result.liegenschaft = obj.liegenschaft;
		if (obj.merkmal === undefined)
			throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = obj.merkmal;
		if (obj.merkmalgruppe === undefined)
			throw new Error('invalid json format, missing attribute merkmalgruppe');
		result.merkmalgruppe = obj.merkmalgruppe;
		if (obj.attribut === undefined)
			throw new Error('invalid json format, missing attribute attribut');
		result.attribut = obj.attribut;
		if (obj.attributgruppe === undefined)
			throw new Error('invalid json format, missing attribute attributgruppe');
		result.attributgruppe = obj.attributgruppe;
		if (obj.wert === undefined)
			throw new Error('invalid json format, missing attribute wert');
		result.wert = obj.wert;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitMerkmal) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.toString()) + ',';
		result += '"liegenschaft" : ' + obj.liegenschaft.toString() + ',';
		result += '"merkmal" : ' + obj.merkmal.toString() + ',';
		result += '"merkmalgruppe" : ' + obj.merkmalgruppe.toString() + ',';
		result += '"attribut" : ' + obj.attribut.toString() + ',';
		result += '"attributgruppe" : ' + obj.attributgruppe.toString() + ',';
		result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitMerkmal>) : string {
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
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + JSON.stringify(obj.schulnummer) + ',';
		}
		if (obj.id !== undefined) {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id.toString()) + ',';
		}
		if (obj.liegenschaft !== undefined) {
			result += '"liegenschaft" : ' + obj.liegenschaft.toString() + ',';
		}
		if (obj.merkmal !== undefined) {
			result += '"merkmal" : ' + obj.merkmal.toString() + ',';
		}
		if (obj.merkmalgruppe !== undefined) {
			result += '"merkmalgruppe" : ' + obj.merkmalgruppe.toString() + ',';
		}
		if (obj.attribut !== undefined) {
			result += '"attribut" : ' + obj.attribut.toString() + ',';
		}
		if (obj.attributgruppe !== undefined) {
			result += '"attributgruppe" : ' + obj.attributgruppe.toString() + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + JSON.stringify(obj.wert) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitMerkmal(obj : unknown) : SchuldateiOrganisationseinheitMerkmal {
	return obj as SchuldateiOrganisationseinheitMerkmal;
}
