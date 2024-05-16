import { JavaObject } from '../../../java/lang/JavaObject';

export class SchuldateiOrganisationseinheitAdresse extends JavaObject {

	/**
	 * Die ID des Adress-Eintrags.
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
	 * Straße der Adresse der Organisationseinheit
	 */
	public strasse : string | null = null;

	/**
	 * Postleitzahl der Schule
	 */
	public postleitzahl : string | null = null;

	/**
	 * Ort der Schule
	 */
	public ort : string | null = null;

	/**
	 * Regionalschlüssel der Schule
	 */
	public regionalschluessel : string | null = null;

	/**
	 * Qualität der Verortung
	 */
	public qualitaetverortung : number | null = null;

	/**
	 * Koordinatenrechtswert der Adresse
	 */
	public koordinaterechtswert : number | null = null;

	/**
	 * Koordinatenhochwert der Adresse
	 */
	public koordinatehochwert : number | null = null;

	/**
	 * Der Adresstyp
	 */
	public adresstypeid : string | null = null;

	/**
	 * Das Standortkennzeichen
	 */
	public standortkennzeichen : string | null = null;

	/**
	 * Das Adresskennzeichnen des Teilstandorts (ein Buchstabe)
	 */
	public adresskennzeichen : string = "";

	/**
	 * Hauptstandortadresse
	 */
	public hauptstandortadresse : string = "";

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
	 * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
	 */
	public constructor() {
		super();
		// empty block
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitAdresse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.data.SchuldateiOrganisationseinheitAdresse'].includes(name);
	}

	public static transpilerFromJSON(json : string): SchuldateiOrganisationseinheitAdresse {
		const obj = JSON.parse(json);
		const result = new SchuldateiOrganisationseinheitAdresse();
		result.id = typeof obj.id === "undefined" ? null : obj.id === null ? null : obj.id;
		if (typeof obj.schulnummer === "undefined")
			 throw new Error('invalid json format, missing attribute schulnummer');
		result.schulnummer = obj.schulnummer;
		if (typeof obj.liegenschaft === "undefined")
			 throw new Error('invalid json format, missing attribute liegenschaft');
		result.liegenschaft = obj.liegenschaft;
		result.strasse = typeof obj.strasse === "undefined" ? null : obj.strasse === null ? null : obj.strasse;
		result.postleitzahl = typeof obj.postleitzahl === "undefined" ? null : obj.postleitzahl === null ? null : obj.postleitzahl;
		result.ort = typeof obj.ort === "undefined" ? null : obj.ort === null ? null : obj.ort;
		result.regionalschluessel = typeof obj.regionalschluessel === "undefined" ? null : obj.regionalschluessel === null ? null : obj.regionalschluessel;
		result.qualitaetverortung = typeof obj.qualitaetverortung === "undefined" ? null : obj.qualitaetverortung === null ? null : obj.qualitaetverortung;
		result.koordinaterechtswert = typeof obj.koordinaterechtswert === "undefined" ? null : obj.koordinaterechtswert === null ? null : obj.koordinaterechtswert;
		result.koordinatehochwert = typeof obj.koordinatehochwert === "undefined" ? null : obj.koordinatehochwert === null ? null : obj.koordinatehochwert;
		result.adresstypeid = typeof obj.adresstypeid === "undefined" ? null : obj.adresstypeid === null ? null : obj.adresstypeid;
		result.standortkennzeichen = typeof obj.standortkennzeichen === "undefined" ? null : obj.standortkennzeichen === null ? null : obj.standortkennzeichen;
		if (typeof obj.adresskennzeichen === "undefined")
			 throw new Error('invalid json format, missing attribute adresskennzeichen');
		result.adresskennzeichen = obj.adresskennzeichen;
		if (typeof obj.hauptstandortadresse === "undefined")
			 throw new Error('invalid json format, missing attribute hauptstandortadresse');
		result.hauptstandortadresse = obj.hauptstandortadresse;
		result.gueltigab = typeof obj.gueltigab === "undefined" ? null : obj.gueltigab === null ? null : obj.gueltigab;
		result.gueltigbis = typeof obj.gueltigbis === "undefined" ? null : obj.gueltigbis === null ? null : obj.gueltigbis;
		result.geaendertam = typeof obj.geaendertam === "undefined" ? null : obj.geaendertam === null ? null : obj.geaendertam;
		return result;
	}

	public static transpilerToJSON(obj : SchuldateiOrganisationseinheitAdresse) : string {
		let result = '{';
		result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		result += '"schulnummer" : ' + obj.schulnummer! + ',';
		result += '"liegenschaft" : ' + obj.liegenschaft! + ',';
		result += '"strasse" : ' + ((!obj.strasse) ? 'null' : JSON.stringify(obj.strasse)) + ',';
		result += '"postleitzahl" : ' + ((!obj.postleitzahl) ? 'null' : JSON.stringify(obj.postleitzahl)) + ',';
		result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		result += '"regionalschluessel" : ' + ((!obj.regionalschluessel) ? 'null' : JSON.stringify(obj.regionalschluessel)) + ',';
		result += '"qualitaetverortung" : ' + ((!obj.qualitaetverortung) ? 'null' : obj.qualitaetverortung) + ',';
		result += '"koordinaterechtswert" : ' + ((!obj.koordinaterechtswert) ? 'null' : obj.koordinaterechtswert) + ',';
		result += '"koordinatehochwert" : ' + ((!obj.koordinatehochwert) ? 'null' : obj.koordinatehochwert) + ',';
		result += '"adresstypeid" : ' + ((!obj.adresstypeid) ? 'null' : JSON.stringify(obj.adresstypeid)) + ',';
		result += '"standortkennzeichen" : ' + ((!obj.standortkennzeichen) ? 'null' : JSON.stringify(obj.standortkennzeichen)) + ',';
		result += '"adresskennzeichen" : ' + JSON.stringify(obj.adresskennzeichen!) + ',';
		result += '"hauptstandortadresse" : ' + JSON.stringify(obj.hauptstandortadresse!) + ',';
		result += '"gueltigab" : ' + ((!obj.gueltigab) ? 'null' : JSON.stringify(obj.gueltigab)) + ',';
		result += '"gueltigbis" : ' + ((!obj.gueltigbis) ? 'null' : JSON.stringify(obj.gueltigbis)) + ',';
		result += '"geaendertam" : ' + ((!obj.geaendertam) ? 'null' : JSON.stringify(obj.geaendertam)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj : Partial<SchuldateiOrganisationseinheitAdresse>) : string {
		let result = '{';
		if (typeof obj.id !== "undefined") {
			result += '"id" : ' + ((!obj.id) ? 'null' : obj.id) + ',';
		}
		if (typeof obj.schulnummer !== "undefined") {
			result += '"schulnummer" : ' + obj.schulnummer + ',';
		}
		if (typeof obj.liegenschaft !== "undefined") {
			result += '"liegenschaft" : ' + obj.liegenschaft + ',';
		}
		if (typeof obj.strasse !== "undefined") {
			result += '"strasse" : ' + ((!obj.strasse) ? 'null' : JSON.stringify(obj.strasse)) + ',';
		}
		if (typeof obj.postleitzahl !== "undefined") {
			result += '"postleitzahl" : ' + ((!obj.postleitzahl) ? 'null' : JSON.stringify(obj.postleitzahl)) + ',';
		}
		if (typeof obj.ort !== "undefined") {
			result += '"ort" : ' + ((!obj.ort) ? 'null' : JSON.stringify(obj.ort)) + ',';
		}
		if (typeof obj.regionalschluessel !== "undefined") {
			result += '"regionalschluessel" : ' + ((!obj.regionalschluessel) ? 'null' : JSON.stringify(obj.regionalschluessel)) + ',';
		}
		if (typeof obj.qualitaetverortung !== "undefined") {
			result += '"qualitaetverortung" : ' + ((!obj.qualitaetverortung) ? 'null' : obj.qualitaetverortung) + ',';
		}
		if (typeof obj.koordinaterechtswert !== "undefined") {
			result += '"koordinaterechtswert" : ' + ((!obj.koordinaterechtswert) ? 'null' : obj.koordinaterechtswert) + ',';
		}
		if (typeof obj.koordinatehochwert !== "undefined") {
			result += '"koordinatehochwert" : ' + ((!obj.koordinatehochwert) ? 'null' : obj.koordinatehochwert) + ',';
		}
		if (typeof obj.adresstypeid !== "undefined") {
			result += '"adresstypeid" : ' + ((!obj.adresstypeid) ? 'null' : JSON.stringify(obj.adresstypeid)) + ',';
		}
		if (typeof obj.standortkennzeichen !== "undefined") {
			result += '"standortkennzeichen" : ' + ((!obj.standortkennzeichen) ? 'null' : JSON.stringify(obj.standortkennzeichen)) + ',';
		}
		if (typeof obj.adresskennzeichen !== "undefined") {
			result += '"adresskennzeichen" : ' + JSON.stringify(obj.adresskennzeichen!) + ',';
		}
		if (typeof obj.hauptstandortadresse !== "undefined") {
			result += '"hauptstandortadresse" : ' + JSON.stringify(obj.hauptstandortadresse!) + ',';
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

export function cast_de_svws_nrw_schuldatei_v1_data_SchuldateiOrganisationseinheitAdresse(obj : unknown) : SchuldateiOrganisationseinheitAdresse {
	return obj as SchuldateiOrganisationseinheitAdresse;
}
