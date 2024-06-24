import { SchuldateiEintrag } from '../../../schuldatei/v1/data/SchuldateiEintrag';

export class SchuldateiOrganisationseinheitAdresse extends SchuldateiEintrag {

	/**
	 * Die ID des Adress-Eintrags.
	 */
	public id : number = 0;

	/**
	 * Die Schulnummer.
	 */
	public schulnummer : number = 0;

	/**
	 * Die Nummer der Liegenschaft der Organisationseinheit
	 */
	public liegenschaft : number = 0;

	/**
	 * Straße der Adresse der Organisationseinheit
	 */
	public strasse : string = "";

	/**
	 * Postleitzahl der Schule
	 */
	public postleitzahl : string = "";

	/**
	 * Ort der Schule
	 */
	public ort : string = "";

	/**
	 * Regionalschlüssel der Schule
	 */
	public regionalschluessel : string = "";

	/**
	 * Qualität der Verortung
	 */
	public qualitaetverortung : number = 0;

	/**
	 * Koordinatenrechtswert der Adresse
	 */
	public koordinaterechtswert : number = 0;

	/**
	 * Koordinatenhochwert der Adresse
	 */
	public koordinatehochwert : number = 0;

	/**
	 * Der Adresstyp
	 */
	public adresstypeid : number | null = null;

	/**
	 * Das Standortkennzeichen
	 */
	public standortkennzeichen : number = 0;

	/**
	 * Das Adresskennzeichnen des Teilstandorts (ein Buchstabe)
	 */
	public adresskennzeichen : string = "";

	/**
	 * Hauptstandortadresse
	 */
	public hauptstandortadresse : string = "";


	/**
	 * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitAdresse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiEintrag', 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitAdresse'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitAdresse {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitAdresse();
		result.gueltigab = (obj.gueltigab === undefined) ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = (obj.gueltigbis === undefined) ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = (obj.geaendertam === undefined) ? null : obj.geaendertam === null ? null : obj.geaendertam;
		if (obj.id === undefined)
			 throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		if (obj.schulnummer === undefined)
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (obj.liegenschaft === undefined)
			 throw new Error('invalid json format, missing attribute liegenschaft');
		result.liegenschaft = obj.liegenschaft;
		if (obj.strasse === undefined)
			 throw new Error('invalid json format, missing attribute strasse');
		result.strasse = obj.strasse;
		if (obj.postleitzahl === undefined)
			 throw new Error('invalid json format, missing attribute postleitzahl');
		result.postleitzahl = obj.postleitzahl;
		if (obj.ort === undefined)
			 throw new Error('invalid json format, missing attribute ort');
		result.ort = obj.ort;
		if (obj.regionalschluessel === undefined)
			 throw new Error('invalid json format, missing attribute regionalschluessel');
		result.regionalschluessel = obj.regionalschluessel;
		if (obj.qualitaetverortung === undefined)
			 throw new Error('invalid json format, missing attribute qualitaetverortung');
		result.qualitaetverortung = obj.qualitaetverortung;
		if (obj.koordinaterechtswert === undefined)
			 throw new Error('invalid json format, missing attribute koordinaterechtswert');
		result.koordinaterechtswert = obj.koordinaterechtswert;
		if (obj.koordinatehochwert === undefined)
			 throw new Error('invalid json format, missing attribute koordinatehochwert');
		result.koordinatehochwert = obj.koordinatehochwert;
		result.adresstypeid = (obj.adresstypeid === undefined) ? null : obj.adresstypeid === null ? null : obj.adresstypeid;
		if (obj.standortkennzeichen === undefined)
			 throw new Error('invalid json format, missing attribute standortkennzeichen');
		result.standortkennzeichen = obj.standortkennzeichen;
		if (obj.adresskennzeichen === undefined)
			 throw new Error('invalid json format, missing attribute adresskennzeichen');
		result.adresskennzeichen = obj.adresskennzeichen;
		if (obj.hauptstandortadresse === undefined)
			 throw new Error('invalid json format, missing attribute hauptstandortadresse');
		result.hauptstandortadresse = obj.hauptstandortadresse;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitAdresse) : string {
		let result = '{';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result += '"id" : ' + obj.id + ',';
		result += '"schulnummer" : ' + obj.schulnummer + ',';
		result += '"liegenschaft" : ' + obj.liegenschaft + ',';
		result += '"strasse" : ' + JSON.stringify(obj.strasse!) + ',';
		result += '"postleitzahl" : ' + JSON.stringify(obj.postleitzahl!) + ',';
		result += '"ort" : ' + JSON.stringify(obj.ort!) + ',';
		result += '"regionalschluessel" : ' + JSON.stringify(obj.regionalschluessel!) + ',';
		result += '"qualitaetverortung" : ' + obj.qualitaetverortung + ',';
		result += '"koordinaterechtswert" : ' + obj.koordinaterechtswert + ',';
		result += '"koordinatehochwert" : ' + obj.koordinatehochwert + ',';
		result += '"adresstypeid" : ' + ((!obj.adresstypeid) ? 'null' : obj.adresstypeid) + ',';
		result += '"standortkennzeichen" : ' + obj.standortkennzeichen + ',';
		result += '"adresskennzeichen" : ' + JSON.stringify(obj.adresskennzeichen!) + ',';
		result += '"hauptstandortadresse" : ' + JSON.stringify(obj.hauptstandortadresse!) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitAdresse>) : string {
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
			result += '"id" : ' + obj.id + ',';
		}
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (obj.liegenschaft !== undefined) {
			result += '"liegenschaft" : ' + obj.liegenschaft + ',';
		}
		if (obj.strasse !== undefined) {
			result += '"strasse" : ' + JSON.stringify(obj.strasse!) + ',';
		}
		if (obj.postleitzahl !== undefined) {
			result += '"postleitzahl" : ' + JSON.stringify(obj.postleitzahl!) + ',';
		}
		if (obj.ort !== undefined) {
			result += '"ort" : ' + JSON.stringify(obj.ort!) + ',';
		}
		if (obj.regionalschluessel !== undefined) {
			result += '"regionalschluessel" : ' + JSON.stringify(obj.regionalschluessel!) + ',';
		}
		if (obj.qualitaetverortung !== undefined) {
			result += '"qualitaetverortung" : ' + obj.qualitaetverortung + ',';
		}
		if (obj.koordinaterechtswert !== undefined) {
			result += '"koordinaterechtswert" : ' + obj.koordinaterechtswert + ',';
		}
		if (obj.koordinatehochwert !== undefined) {
			result += '"koordinatehochwert" : ' + obj.koordinatehochwert + ',';
		}
		if (obj.adresstypeid !== undefined) {
			result += '"adresstypeid" : ' + ((!obj.adresstypeid) ? 'null' : obj.adresstypeid) + ',';
		}
		if (obj.standortkennzeichen !== undefined) {
			result += '"standortkennzeichen" : ' + obj.standortkennzeichen + ',';
		}
		if (obj.adresskennzeichen !== undefined) {
			result += '"adresskennzeichen" : ' + JSON.stringify(obj.adresskennzeichen!) + ',';
		}
		if (obj.hauptstandortadresse !== undefined) {
			result += '"hauptstandortadresse" : ' + JSON.stringify(obj.hauptstandortadresse!) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitAdresse(obj : unknown) : SchuldateiOrganisationseinheitAdresse {
	return obj as SchuldateiOrganisationseinheitAdresse;
}
