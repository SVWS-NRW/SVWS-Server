import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiManager } from '../../../schuldatei/v1/utils/SchuldateiManager';
import { SchuldateiOrganisationseinheitAdresse } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitAdresse';
import { SchuldateiOrganisationseinheitManager } from '../../../schuldatei/v1/utils/SchuldateiOrganisationseinheitManager';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class SchuldateiOrganisationseinheitAdressManager extends JavaObject {

	/**
	 * Die Referenz auf den Manager für die Schuldatei
	 */
	private readonly _managerSchuldatei : SchuldateiManager;

	/**
	 * Die Referenz auf den Manager der Organisationseinheit
	 */
	private readonly _managerOrganisationseinheit : SchuldateiOrganisationseinheitManager;

	/**
	 * Das Datenobjekt für die Adresse aus der Organisationseinheit
	 */
	private readonly _adresse : SchuldateiOrganisationseinheitAdresse;

	/**
	 * Die Art der Adresse
	 */
	private readonly _artDerAdresse : string;

	/**
	 * Gibt an, ob es sich um den Hauptstandort der Organisationseinheit handelt oder nicht
	 */
	private readonly _istHauptstandort : boolean;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Adresse und
	 * den beiden zugehörigen Managern für die Schuldatei sowie die Organisationseinheit.
	 *
	 * @param managerSchuldatei             der Manager für die Schuldatei
	 * @param managerOrganisationseinheit   der Manager für die Organisationseinheit
	 * @param adresse                       die Adresse der Organisationseinheit
	 */
	public constructor(managerSchuldatei : SchuldateiManager, managerOrganisationseinheit : SchuldateiOrganisationseinheitManager, adresse : SchuldateiOrganisationseinheitAdresse) {
		super();
		this._managerSchuldatei = managerSchuldatei;
		this._managerOrganisationseinheit = managerOrganisationseinheit;
		this._adresse = adresse;
		if (this._managerOrganisationseinheit.getSchulnummer() !== this._adresse.schulnummer)
			throw new IllegalArgumentException("Die Schulnummer " + adresse.schulnummer + " bei der Adresse mit der ID " + adresse.id + " passt nicht zu der Schulnummer der Organisationseinheit " + this._managerOrganisationseinheit.getSchulnummer() + ".")
		this._artDerAdresse = (adresse.adresstypeid === null) ? "" : "" + adresse.adresstypeid;
		if (!this._managerSchuldatei.katalogAddressarten.hatEintrag(this._artDerAdresse))
			throw new IllegalArgumentException("Die Art der Adresse '" + this._artDerAdresse + "' bei der Adresse mit der ID " + adresse.id + " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer() + " ist im zugehörigen Katalog nicht vorhanden.")
		this._istHauptstandort = JavaObject.equalsTranspiler("1", (adresse.hauptstandortadresse));
	}

	/**
	 * Gibt die ID der Adresse zurück.
	 *
	 * @return die ID der Adresse
	 */
	public getID() : number {
		return this._adresse.id;
	}

	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück, zu der die Adresse gehört.
	 *
	 * @return die Schulnummer der Organisationseinheit
	 */
	public getSchulnummer() : number {
		return this._adresse.schulnummer;
	}

	/**
	 * Gibt die Nummer der Liegenschaft der Organisationseinheit zurück, zu der die Adresse gehört.
	 *
	 * @return die Nummer der Liegenschaft der Organisationseinheit
	 */
	public getLiegenschaftnummer() : number {
		return this._adresse.liegenschaft;
	}

	/**
	 * Gibt die Straße zurück.
	 *
	 * @return die Straße
	 */
	public getStrasse() : string {
		return this._adresse.strasse;
	}

	/**
	 * Gibt die Postleitzahl zurück.
	 *
	 * @return die Postleitzahl
	 */
	public getPostleitzahl() : string {
		return this._adresse.postleitzahl;
	}

	/**
	 * Gibt den Ort zurück.
	 *
	 * @return der Ort
	 */
	public getOrt() : string {
		return this._adresse.ort;
	}

	/**
	 * Gibt den Regionalschlüssel zurück.
	 *
	 * @return der Regionalschlüssel
	 */
	public getRegionalschluessel() : string {
		return this._adresse.regionalschluessel;
	}

	/**
	 * Gibt die Qualität der Verortung (siehe zugehöriger Katalog) zurück.
	 *
	 * @return die Qualität der Verortung
	 */
	public getQualitaetVerortung() : number {
		return this._adresse.qualitaetverortung;
	}

	/**
	 * Gibt den Koordinatenrechtswert zurück.
	 *
	 * @return der Koordinatenrechtswert
	 */
	public getKoordinatenrechtswert() : number {
		return this._adresse.koordinaterechtswert;
	}

	/**
	 * Gibt den Koordinatenhochwert zurück.
	 *
	 * @return der Koordinatenhochwert
	 */
	public getKoordinatenhochwert() : number {
		return this._adresse.koordinatehochwert;
	}

	/**
	 * Gibt die Art der Adresse (siehe zugehöriger Katalog) zurück.
	 *
	 * @return die Art der Adresse
	 */
	public getArtDerAdresse() : string {
		return this._artDerAdresse;
	}

	/**
	 * Gibt das Standortkennzeichen des Teilstandorts zurück.
	 *
	 * @return das Standortkennzeichen des Teilstandorts
	 */
	public getStandortkennzeichen() : number {
		return this._adresse.standortkennzeichen;
	}

	/**
	 * Gibt das Adresskennzeichen zurück.
	 *
	 * @return das Adresskennzeichen
	 */
	public getAdresskennzeichen() : string {
		return this._adresse.adresskennzeichen;
	}

	/**
	 * Gibt die Information zur Hauptstandortadresse (siehe zugehöriger Katalog)
	 * zurück.
	 *
	 * @return die Information zur Hauptstandortadresse
	 */
	public getHauptstandortadresse() : string {
		return this._adresse.hauptstandortadresse;
	}

	/**
	 * Gibt zurück, ob es sich um den Hauptstandort handelt.
	 *
	 * @return true, wenn es sich um den Hauptstandort handelt, und ansonsten false
	 */
	public istHauptstandort() : boolean {
		return this._istHauptstandort;
	}

	/**
	 * Gibt das Datum zurück, ab dem diese Adresse gültig ist.
	 *
	 * @return das Datum, ab dem diese Adresse gültig ist.
	 */
	public getGueltigAb() : string | null {
		return this._adresse.gueltigab;
	}

	/**
	 * Gibt das Datum zurück, bis zu welchem diese Adresse gültig ist.
	 *
	 * @return das Datum, bis zu welchem diese Adresse gültig ist.
	 */
	public getGueltigBis() : string | null {
		return this._adresse.gueltigbis;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitAdressManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitAdressManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schuldatei_v1_utils_SchuldateiOrganisationseinheitAdressManager(obj : unknown) : SchuldateiOrganisationseinheitAdressManager {
	return obj as SchuldateiOrganisationseinheitAdressManager;
}
