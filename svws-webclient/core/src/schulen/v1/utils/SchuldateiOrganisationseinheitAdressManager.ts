import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiManager } from '../../../schulen/v1/utils/SchuldateiManager';
import { SchuldateiOrganisationseinheitAdresse } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitAdresse';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheitManager } from '../../../schulen/v1/utils/SchuldateiOrganisationseinheitManager';
import type { List } from '../../../java/util/List';
import { JavaString } from '../../../java/lang/JavaString';
import { SchuldateiOrganisationseinheitErreichbarkeit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitErreichbarkeit';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { SchuldateiUtils } from '../../../schulen/v1/utils/SchuldateiUtils';

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
	 * Die Festnetznummern (codekey=02) zu der Adresse
	 */
	private readonly _festnetzNummern : List<string>;

	/**
	 * Die Mobilnummern (codekey=03) zu der Adresse
	 */
	private readonly _mobilNummern : List<string>;

	/**
	 * Die Faxnummern (codekey=04) zu der Adresse
	 */
	private readonly _faxNummern : List<string>;

	/**
	 * Die Emails (codekey=01) zu der Adresse
	 */
	private readonly _emailAdressen : List<string>;

	/**
	 * Die URLs (codekey=09) zu der Adresse
	 */
	private readonly _webAdressen : List<string>;

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
	 * @param erreichbarkeiten				die Erreichbarkeiten der Organisationseinheit
	 */
	public constructor(managerSchuldatei : SchuldateiManager, managerOrganisationseinheit : SchuldateiOrganisationseinheitManager, adresse : SchuldateiOrganisationseinheitAdresse, erreichbarkeiten : List<SchuldateiOrganisationseinheitErreichbarkeit>) {
		super();
		this._managerSchuldatei = managerSchuldatei;
		this._managerOrganisationseinheit = managerOrganisationseinheit;
		this._adresse = adresse;
		this._festnetzNummern = new ArrayList();
		this._mobilNummern = new ArrayList();
		this._faxNummern = new ArrayList();
		this._emailAdressen = new ArrayList();
		this._webAdressen = new ArrayList();
		if (this._managerOrganisationseinheit.getSchulnummer() !== this._adresse.schulnummer)
			throw new IllegalArgumentException("Die Schulnummer " + this._adresse.schulnummer + " bei der Adresse mit der ID " + this._adresse.id + " passt nicht zu der Schulnummer der Organisationseinheit " + this._managerOrganisationseinheit.getSchulnummer() + ".")
		if (!this._managerSchuldatei.katalogQualitaetenVerortung.hatEintrag(this._adresse.qualitaetverortung))
			throw new IllegalArgumentException("Der Wert von QualitätVerortung '" + this._adresse.qualitaetverortung + "' bei der Adresse mit der ID " + this._adresse.id + " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer() + " ist im zugehörigen Katalog nicht vorhanden.")
		this._artDerAdresse = (this._adresse.adresstypeid === null) ? "" : ("" + this._adresse.adresstypeid);
		if (!this._managerSchuldatei.katalogAddressarten.hatEintrag(this._artDerAdresse))
			throw new IllegalArgumentException("Die Art der Adresse '" + this._artDerAdresse + "' bei der Adresse mit der ID " + this._adresse.id + " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer() + " ist im zugehörigen Katalog nicht vorhanden.")
		if (!this._managerSchuldatei.katalogHauptstandort.hatEintrag(this._adresse.hauptstandortadresse))
			throw new IllegalArgumentException("Der Wert von Hauptstandortadresse '" + this._adresse.hauptstandortadresse + "' bei der Adresse mit der ID " + this._adresse.id + " der Organisationseinheit mit der Schulnummer " + this._managerOrganisationseinheit.getSchulnummer() + " ist im zugehörigen Katalog nicht vorhanden.")
		this._istHauptstandort = JavaObject.equalsTranspiler("1", (adresse.hauptstandortadresse));
		for (const erreichbarkeit of erreichbarkeiten) {
			if (((erreichbarkeit.liegenschaft === 0) || (erreichbarkeit.liegenschaft === adresse.liegenschaft)) && (erreichbarkeit.codekey !== null) && (SchuldateiUtils.pruefeUeberlappung(erreichbarkeit, adresse))) {
				if (JavaString.compareTo(erreichbarkeit.codekey, "01") === 0)
					this._emailAdressen.add(erreichbarkeit.codewert);
				else
					if (JavaString.compareTo(erreichbarkeit.codekey, "02") === 0)
						this._festnetzNummern.add(erreichbarkeit.codewert);
					else
						if (JavaString.compareTo(erreichbarkeit.codekey, "03") === 0)
							this._mobilNummern.add(erreichbarkeit.codewert);
						else
							if (JavaString.compareTo(erreichbarkeit.codekey, "04") === 0)
								this._faxNummern.add(erreichbarkeit.codewert);
							else
								if (JavaString.compareTo(erreichbarkeit.codekey, "09") === 0)
									this._webAdressen.add(erreichbarkeit.codewert);
			}
		}
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

	/**
	 * Gibt die Festnetznummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Festnetznummern
	 */
	public getFestnetznummern() : List<string> {
		return this._festnetzNummern;
	}

	/**
	 * Gibt die Mobilnummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Mobilnummern
	 */
	public getMobilnummern() : List<string> {
		return this._mobilNummern;
	}

	/**
	 * Gibt die Faxnummern zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Faxnummern
	 */
	public getFaxnummern() : List<string> {
		return this._faxNummern;
	}

	/**
	 * Gibt die Emailadressen zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Emailadressen
	 */
	public getEmailadressen() : List<string> {
		return this._emailAdressen;
	}

	/**
	 * Gibt die Webadressen zu dieser Adresse zurück
	 *
	 * @return die Liste der entsprechenden Webadressen
	 */
	public getWebadressen() : List<string> {
		return this._webAdressen;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitAdressManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitAdressManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiOrganisationseinheitAdressManager(obj : unknown) : SchuldateiOrganisationseinheitAdressManager {
	return obj as SchuldateiOrganisationseinheitAdressManager;
}
