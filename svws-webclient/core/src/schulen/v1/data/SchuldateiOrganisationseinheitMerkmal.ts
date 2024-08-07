import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitMerkmal extends SchuldateiEintrag {

	/**
	 * Die ID des Merkmal-Eintrags.
	 */
	public id : number | null = null;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Nummer der Liegenschaft der Organisationseinheit
	 */
	public liegenschaft : number = 0;

	/**
	 * Das Merkmal
	 */
	public merkmal : string = "";

	/**
	 * Die Merkmalsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)
	 */
	public merkmalgruppe : string = "";

	/**
	 * Das Attribut
	 */
	public attribut : string | null = null;

	/**
	 * Die Attributsgruppe (hat zur Zeit 25.07.24 keine Bedeutung)
	 */
	public attributgruppe : string | null = null;

	/**
	 * Der Wert
	 */
	public wert : string | null = null;


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
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitMerkmal();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		result.id = (obj.id === undefined) ? null : obj.id === null ? null : obj.id;
		if (obj.schulnummer === undefined)
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.liegenschaft === undefined)
			 throw new Error('invalid json format, missing attribute liegenschaft');
		result.liegenschaft = obj.liegenschaft;
		if (obj.merkmal === undefined)
			 throw new Error('invalid json format, missing attribute merkmal');
		result.merkmal = obj.merkmal;
		if (obj.merkmalgruppe === undefined)
			 throw new Error('invalid json format, missing attribute merkmalgruppe');
		result.merkmalgruppe = obj.merkmalgruppe;
		result.attribut = (obj.attribut === undefined) ? null : obj.attribut === null ? null : obj.attribut;
		result.attributgruppe = (obj.attributgruppe === undefined) ? null : obj.attributgruppe === null ? null : obj.attributgruppe;
		result.wert = (obj.wert === undefined) ? null : obj.wert === null ? null : obj.wert;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitMerkmal) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"liegenschaft" : ' + obj.liegenschaft! + ',';
		result += '"merkmal" : ' + JSON.stringify(obj.merkmal!) + ',';
		result += '"merkmalgruppe" : ' + JSON.stringify(obj.merkmalgruppe!) + ',';
		result += '"attribut" : ' + ((!obj.attribut) ? 'null' : JSON.stringify(obj.attribut)) + ',';
		result += '"attributgruppe" : ' + ((!obj.attributgruppe) ? 'null' : JSON.stringify(obj.attributgruppe)) + ',';
		result += '"wert" : ' + ((!obj.wert) ? 'null' : JSON.stringify(obj.wert)) + ',';
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
		if (obj.id !== undefined) {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (obj.liegenschaft !== undefined) {
			result += '"liegenschaft" : ' + obj.liegenschaft + ',';
		}
		if (obj.merkmal !== undefined) {
			result += '"merkmal" : ' + JSON.stringify(obj.merkmal!) + ',';
		}
		if (obj.merkmalgruppe !== undefined) {
			result += '"merkmalgruppe" : ' + JSON.stringify(obj.merkmalgruppe!) + ',';
		}
		if (obj.attribut !== undefined) {
			result += '"attribut" : ' + ((!obj.attribut) ? 'null' : JSON.stringify(obj.attribut)) + ',';
		}
		if (obj.attributgruppe !== undefined) {
			result += '"attributgruppe" : ' + ((!obj.attributgruppe) ? 'null' : JSON.stringify(obj.attributgruppe)) + ',';
		}
		if (obj.wert !== undefined) {
			result += '"wert" : ' + ((!obj.wert) ? 'null' : JSON.stringify(obj.wert)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schulen_v1_data_SchuldateiOrganisationseinheitMerkmal(obj : unknown) : SchuldateiOrganisationseinheitMerkmal {
	return obj as SchuldateiOrganisationseinheitMerkmal;
}
