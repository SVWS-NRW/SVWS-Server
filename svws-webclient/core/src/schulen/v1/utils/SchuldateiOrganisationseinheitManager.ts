import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitGrunddaten } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitGrunddaten';
import { SchuldateiKatalogeintrag } from '../../../schulen/v1/data/SchuldateiKatalogeintrag';
import { HashMap } from '../../../java/util/HashMap';
import { SchuldateiOrganisationseinheitAdressManager } from '../../../schulen/v1/utils/SchuldateiOrganisationseinheitAdressManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { SchuldateiManager } from '../../../schulen/v1/utils/SchuldateiManager';
import { SchuldateiOrganisationseinheit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { SchuldateiUtils } from '../../../schulen/v1/utils/SchuldateiUtils';

export class SchuldateiOrganisationseinheitManager extends JavaObject {

	/**
	 * Die Referenz auf den Manager für die Schuldatei
	 */
	private readonly _managerSchuldatei : SchuldateiManager;

	/**
	 * Das Datenobjekt für die Schuldatei
	 */
	private readonly _organisationseinheit : SchuldateiOrganisationseinheit;

	/**
	 * Die Manager für die Adressenn anhand ihrer ID
	 */
	private readonly _mapAdressManagerByID : JavaMap<number, SchuldateiOrganisationseinheitAdressManager> = new HashMap<number, SchuldateiOrganisationseinheitAdressManager>();

	/**
	 * Cache: Eine Map der Grunddaten anhand des Schuljahres
	 */
	private readonly _mapGrunddatenBySchuljahr : JavaMap<number, SchuldateiOrganisationseinheitGrunddaten> = new HashMap<number, SchuldateiOrganisationseinheitGrunddaten>();

	/**
	 * Cache: Eine Map der Schulform anhand des Schuljahres
	 */
	private readonly _mapSchulformBySchuljahr : JavaMap<number, string> = new HashMap<number, string>();

	/**
	 * Cache: Eine Map der SchulformASD anhand des Schuljahres
	 */
	private readonly _mapSchulformASDBySchuljahr : JavaMap<number, string> = new HashMap<number, string>();

	/**
	 * Cache: Eine Map der Schulart anhand des Schuljahres
	 */
	private readonly _mapSchulartBySchuljahr : JavaMap<number, string> = new HashMap<number, string>();

	/**
	 * Cache: Eine Map der Adress-Manager anhand des Schuljahres
	 */
	private readonly _mapAdressenBySchuljahr : JavaMap<number, List<SchuldateiOrganisationseinheitAdressManager>> = new HashMap<number, List<SchuldateiOrganisationseinheitAdressManager>>();

	/**
	 * Cache: Eine Map des Hauptstandortes anhand des Schuljahres
	 */
	private readonly _mapHauptstandortBySchuljahr : JavaMap<number, SchuldateiOrganisationseinheitAdressManager> = new HashMap<number, SchuldateiOrganisationseinheitAdressManager>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit der übergebenen Organisationseinheit und
	 * dem zugehörigen Manager für die Schuldatei.
	 *
	 * @param managerSchuldatei      der Manager für die Schuldatei
	 * @param organisationseinheit   die Organisationseinheit aus der Schuldatei
	 */
	public constructor(managerSchuldatei : SchuldateiManager, organisationseinheit : SchuldateiOrganisationseinheit) {
		super();
		this._managerSchuldatei = managerSchuldatei;
		this._organisationseinheit = organisationseinheit;
		this.validate();
	}

	/**
	 * Validiere die Daten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validate() : void {
		if ((this._organisationseinheit.oeart !== null) && (!this._managerSchuldatei.katalogOrganisationseinheitarten.hatEintrag(this._organisationseinheit.oeart)))
			throw new IllegalArgumentException(JavaString.format("Die Art %s der Organisationseinheit mit der Schulnummer %d hat keinen zugehörigen Katalog-Eintrag.", this._organisationseinheit.oeart, this._organisationseinheit.schulnummer))
		this.validateGrunddaten();
		this.validateAdressen();
		this.validateMerkmale();
		this.validateErreichbarkeiten();
		this.validateEigenschaften();
		this.validateGliederungen();
	}

	/**
	 * Validiere die Grunddaten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateGrunddaten() : void {
		for (const grunddaten of this._organisationseinheit.grunddaten) {
			if (this._organisationseinheit.schulnummer !== grunddaten.schulnummer)
				throw new IllegalArgumentException("Die Schulnummer " + grunddaten.schulnummer + " bei den Grunddaten passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogRechtsstatus.hatEintrag(grunddaten.rechtsstatus))
				throw new IllegalArgumentException("Der Rechtstatus " + grunddaten.rechtsstatus + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if ((grunddaten.artdertraegerschaft !== 0) && (!this._managerSchuldatei.katalogArtDerTraegerschaft.hatEintrag(grunddaten.artdertraegerschaft)))
				throw new IllegalArgumentException("Die Art der Trägerschaft " + grunddaten.artdertraegerschaft + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if (!this._managerSchuldatei.katalogSchulbetriebsschluessel.hatEintrag(grunddaten.schulbetriebsschluessel))
				throw new IllegalArgumentException("Der Schulbetriebsschlüssel " + grunddaten.schulbetriebsschluessel + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			this.validateSchulform(grunddaten);
		}
	}

	/**
	 * Bestimmt die Informationen zu der Schulform aus den Grunddaten. Diese bestehen aus
	 * drei Werte:
	 * <ol>
	 *   <li>die allgemeine Schulform</li>
	 *   <li>die speziellere Schulform (auch SchulformASD)</li>
	 *   <li>die Schulart, mit weiteren Information zu der Schule</li>
	 * </ol>
	 *
	 * @param grunddaten   die Grunddaten
	 *
	 * @return ein Array mit den drei Werten zur Schulform
	 */
	private static getSchulformInfo(grunddaten : SchuldateiOrganisationseinheitGrunddaten) : Array<string> {
		const sf : Array<string> = ["", "", ""];
		for (const schulform of grunddaten.schulform) {
			if (JavaObject.equalsTranspiler("Schulform", (schulform.schulformcode))) {
				sf[0] = schulform.schulformwert;
			} else
				if (JavaObject.equalsTranspiler("SchulformASD", (schulform.schulformcode))) {
					sf[1] = schulform.schulformwert;
				} else
					if (JavaObject.equalsTranspiler("Schulart", (schulform.schulformcode))) {
						sf[2] = schulform.schulformwert;
					}
		}
		return sf;
	}

	/**
	 * Validiere die Schulform der übergebenen Grunddaten der Organisationseinheit
	 *
	 * @param grunddaten   die Grundaten der Organisationseinheit.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateSchulform(grunddaten : SchuldateiOrganisationseinheitGrunddaten) : void {
		const sf : Array<string> = SchuldateiOrganisationseinheitManager.getSchulformInfo(grunddaten);
		if (JavaString.isBlank(sf[0]) || JavaString.isBlank(sf[1]))
			throw new IllegalArgumentException("Die Schulform ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht gesetzt.")
		if (this._managerSchuldatei.katalogSchulformen.getEintraege(sf[0]).isEmpty())
			throw new IllegalArgumentException("Die Schulform '" + sf[0] + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht im Katalog enthalten.")
		if (!this._managerSchuldatei.katalogSchulformen.hatEintrag(sf[1]))
			throw new IllegalArgumentException("Die SchulformASD '" + sf[1] + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht im Katalog enthalten.")
		if (!this._managerSchuldatei.katalogSchularten.hatEintrag(sf[2]))
			throw new IllegalArgumentException("Die Schulart '" + sf[2] + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht im Katalog enthalten.")
	}

	/**
	 * Validiere die Adressen der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateAdressen() : void {
		for (const adresse of this._organisationseinheit.adressen) {
			if (this._mapAdressManagerByID.containsKey(adresse.id))
				throw new IllegalArgumentException(JavaString.format("Die Addressen bei der Organisationseinheit mit der Schulnummer %d hat Duplikate.", this._organisationseinheit.schulnummer))
			this._mapAdressManagerByID.put(adresse.id, new SchuldateiOrganisationseinheitAdressManager(this._managerSchuldatei, this, adresse));
		}
	}

	/**
	 * Validiere die Merkmale der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateMerkmale() : void {
		for (const merkmal of this._organisationseinheit.merkmal) {
			// empty block
		}
	}

	/**
	 * Validiere die Erreichbarkeiten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateErreichbarkeiten() : void {
		for (const erreichbarkeit of this._organisationseinheit.erreichbarkeiten) {
			// empty block
		}
	}

	/**
	 * Validiere die Eigenschaften der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateEigenschaften() : void {
		for (const eigenschaft of this._organisationseinheit.oe_eigenschaften) {
			// empty block
		}
	}

	/**
	 * Validiere die Gliederungen der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateGliederungen() : void {
		for (const gliederung of this._organisationseinheit.gliederung) {
			// empty block
		}
	}

	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück.
	 *
	 * @return die Schulnummer
	 */
	public getSchulnummer() : number {
		return this._organisationseinheit.schulnummer;
	}

	/**
	 * Gib die Bundeslandkennung (NRW) der Organisationseinheit zurück.
	 *
	 * @return die Bundeslandkennung
	 */
	public getBundeslandkennung() : string | null {
		return this._organisationseinheit.bundeslandkennung;
	}

	/**
	 * Gibt die eindeutige Identifier für das XSCHULE-Format zurück.
	 *
	 * @return der Identifier
	 */
	public getXscid() : string | null {
		return this._organisationseinheit.xscid;
	}

	/**
	 * Gibt die Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Art der Organisationseinheit
	 */
	public getArt() : string | null {
		return this._organisationseinheit.oeart;
	}

	/**
	 * Gibt die Bezeichnung der Art der Organisationseinheit aus dem Katalog der Schuldatei zurück.
	 *
	 * @return die Bezeichnung der Art der Organisationseinheit
	 */
	public getArtBezeichnung() : string | null {
		const eintrag : SchuldateiKatalogeintrag | null = this._managerSchuldatei.katalogOrganisationseinheitarten.getEintrag(this._organisationseinheit.oeart);
		return (eintrag === null) ? "" : eintrag.bezeichnung;
	}

	/**
	 * Gibt die amtliche Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public getAmtlicheBezeichnung() : string {
		return this._organisationseinheit.amtsbez;
	}

	/**
	 * Gibt das Datum der Errichtung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Errichtung der Organisationseinheit
	 */
	public getDatumErrichtung() : string | null {
		return this._organisationseinheit.errichtung;
	}

	/**
	 * Gibt das Datum der Auflösung der Organisationseinheit zurück.
	 *
	 * @return das Datum der Auflösung
	 */
	public getDatumAufloesung() : string | null {
		return this._organisationseinheit.aufloesung;
	}

	/**
	 * Bestimmt die Grunddaten für das Schuljahr anhand der vorhandenen Grunddaten-Einträge und
	 * erzeugt einen Cache für den schnellen Zugriff auf diese Grunddaten.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Grunddaten für das Schuljahr
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	private getGrunddaten(schuljahr : number) : SchuldateiOrganisationseinheitGrunddaten {
		const daten : SchuldateiOrganisationseinheitGrunddaten | null = this._mapGrunddatenBySchuljahr.get(schuljahr);
		if (daten !== null)
			return daten;
		const grunddaten : List<SchuldateiOrganisationseinheitGrunddaten> = new ArrayList<SchuldateiOrganisationseinheitGrunddaten>();
		for (const eintrag of this._organisationseinheit.grunddaten)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				grunddaten.add(eintrag);
		if (grunddaten.isEmpty())
			throw new IllegalArgumentException("Es konnten keine Grunddaten für das Schuljahr " + schuljahr + "/" + (schuljahr + 1) + " gefunden werden.")
		let eintrag : SchuldateiOrganisationseinheitGrunddaten = grunddaten.get(0);
		for (let i : number = 1; i < grunddaten.size(); i++) {
			const other : SchuldateiOrganisationseinheitGrunddaten = grunddaten.get(0);
			if (SchuldateiUtils.istFrueher(eintrag.gueltigbis, other.gueltigbis))
				eintrag = other;
		}
		this._mapGrunddatenBySchuljahr.put(schuljahr, eintrag);
		const sf : Array<string> = SchuldateiOrganisationseinheitManager.getSchulformInfo(eintrag);
		this._mapSchulformBySchuljahr.put(schuljahr, sf[0]);
		this._mapSchulformASDBySchuljahr.put(schuljahr, sf[1]);
		this._mapSchulartBySchuljahr.put(schuljahr, sf[2]);
		return eintrag;
	}

	/**
	 * Gibt die Kurzbezeichnung der Organisationseinheit zurück, die in dem
	 * angegebenen Schuljahr gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Kurzbezeichnung der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getKurzbezeichnung(schuljahr : number) : string {
		return this.getGrunddaten(schuljahr).kurzbezeichnung;
	}

	/**
	 * Gibt den Rechtsstatus der Organisationseinheit zurück ("1" für öffentlich und "2" für privat),
	 * der in dem angegebenen Schuljahr gültig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Rechtsstatus der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getRechtsstatus(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).rechtsstatus;
	}

	/**
	 * Gibt die Schulträgernummer der Organisationseinheit zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulträgernummer der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getSchultraegernummer(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).schultraegernummer;
	}

	/**
	 * Gibt die Organisationseinheit des Schulträgers dieser Organisationseinheit zurück,
	 * sofern ein Schulträger in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit des Schulträgers
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für den Schulträger in der Schuldatei keine Daten enthalten sind.
	 */
	public getSchultraeger(schuljahr : number) : SchuldateiOrganisationseinheitManager {
		const nummer : number = this.getGrunddaten(schuljahr).schultraegernummer;
		const schultraeger : SchuldateiOrganisationseinheitManager | null = this._managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schultraeger === null)
			throw new IllegalArgumentException("Der Schulträger " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
		return schultraeger;
	}

	/**
	 * Gibt die Art der Trägerschaft der Organisationseinheit zurück,
	 * welche in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Art der Trägerschaft der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getArtDerTraegerschaft(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).artdertraegerschaft;
	}

	/**
	 * Gibt den Betriebsschlüssel der Schule zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Betriebsschlüssel der Schule
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getSchulbetriebsschluessel(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).schulbetriebsschluessel;
	}

	/**
	 * Gibt das Kapitel der Schule zurück, welches in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return das Kapitel der Schule
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getKapitel(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).kapitel;
	}

	/**
	 * Gibt die Schulnummer der oberen Schulaufsichtsbehörde zurück, welche in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getObereSchulaufsichtNummer(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).obereschulaufsicht;
	}

	/**
	 * Gibt die Organisationseinheit der oberen Schulaufsichtsbehörde dieser Organisationseinheit zurück,
	 * sofern eine obere Schulaufsichtsbehörde in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit der oberen Schulaufsichtsbehörde
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für obere Schulaufsichtsbehörde in der Schuldatei keine Daten enthalten sind.
	 */
	public getObereSchulaufsicht(schuljahr : number) : SchuldateiOrganisationseinheitManager {
		const nummer : number = this.getGrunddaten(schuljahr).obereschulaufsicht;
		const schulaufsicht : SchuldateiOrganisationseinheitManager | null = this._managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schulaufsicht === null)
			throw new IllegalArgumentException("Die obere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
		return schulaufsicht;
	}

	/**
	 * Gibt die Schulnummer der unteren Schulaufsichtsbehörde zurück, welche in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getUntererSchulaufsichtNummer(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).untereschulaufsicht;
	}

	/**
	 * Gibt die Organisationseinheit der unteren Schulaufsichtsbehörde dieser Organisationseinheit zurück,
	 * sofern eine untere Schulaufsichtsbehörde in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit der unteren Schulaufsichtsbehörde
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für untere Schulaufsichtsbehörde in der Schuldatei keine Daten enthalten sind.
	 */
	public getUntereSchulaufsicht(schuljahr : number) : SchuldateiOrganisationseinheitManager {
		const nummer : number = this.getGrunddaten(schuljahr).untereschulaufsicht;
		const schulaufsicht : SchuldateiOrganisationseinheitManager | null = this._managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (schulaufsicht === null)
			throw new IllegalArgumentException("Die untere Schulfaufsicht " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
		return schulaufsicht;
	}

	/**
	 * Gibt die Schulnummer des ZfsL zurück, welches in dem angegebenen
	 * Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulnummer
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getZfsLNummer(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).zfsl;
	}

	/**
	 * Gibt die Organisationseinheit des ZfsL dieser Organisationseinheit zurück,
	 * sofern ein ZfsL in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Organisationseinheit des ZfsL
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 *   oder für das ZfsL in der Schuldatei keine Daten enthalten sind.
	 */
	public getZfsL(schuljahr : number) : SchuldateiOrganisationseinheitManager {
		const nummer : number = this.getGrunddaten(schuljahr).zfsl;
		const zfsl : SchuldateiOrganisationseinheitManager | null = this._managerSchuldatei.getOrganisationsheinheitManager(nummer);
		if (zfsl === null)
			throw new IllegalArgumentException("Das ZfsL " + nummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
		return zfsl;
	}

	/**
	 * Gibt den Dienststellenschlüssel/Personalbereich der Organisationseinheit zurück,
	 * welcher in dem angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Dienststellenschlüssel der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getDienststellenschluessel(schuljahr : number) : number {
		return this.getGrunddaten(schuljahr).dienststellenschluessel;
	}

	/**
	 * Gibt den Personalteilbereich der Organisationseinheit an, sofern einer in dem
	 * angegebenen Schuljahr zugeordnet ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Personalteilbereich der Organisationseinheit
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getPersonalteilbereich(schuljahr : number) : string | null {
		return this.getGrunddaten(schuljahr).ptb;
	}

	/**
	 * Gibt die Art des Internatsbetriebs an, sofern einer in dem
	 * angegebenen Schuljahr vorhanden ist.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Art des Internatsbetriebs
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getInternatsbetrieb(schuljahr : number) : string | null {
		return this.getGrunddaten(schuljahr).internatsbetrieb;
	}

	/**
	 * Gibt die Anzahl der Internatsplätze zurück, sofern ein Internatsbetrieb
	 * vorliegt und ansonsten 0;
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Anzahl der Internatsplätze
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getInternatsplaetze(schuljahr : number) : number {
		const grunddaten : SchuldateiOrganisationseinheitGrunddaten | null = this.getGrunddaten(schuljahr);
		return (grunddaten.internatsplaetze === null) ? 0 : grunddaten.internatsplaetze;
	}

	/**
	 * Gibt die Schulform der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulform
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getSchulform(schuljahr : number) : string {
		if (!this._mapSchulformBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		const result : string | null = this._mapSchulformBySchuljahr.get(schuljahr);
		if (result === null)
			throw new IllegalArgumentException("Es konnte keine Schulform für die Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " in diesem Schuljahr gefunden werden.")
		return result;
	}

	/**
	 * Gibt die SchulformASD der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die SchulformASD
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getSchulformASD(schuljahr : number) : string {
		if (!this._mapSchulformASDBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		const result : string | null = this._mapSchulformASDBySchuljahr.get(schuljahr);
		if (result === null)
			throw new IllegalArgumentException("Es konnte keine SchulformASD für die Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " in diesem Schuljahr gefunden werden.")
		return result;
	}

	/**
	 * Gibt die Schulart der Organisationseinheit zurück, sofern diese in dem
	 * angegebenen Schuljahr existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Schulart
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getSchulart(schuljahr : number) : string {
		if (!this._mapSchulartBySchuljahr.containsKey(schuljahr))
			this.getGrunddaten(schuljahr);
		const result : string | null = this._mapSchulartBySchuljahr.get(schuljahr);
		return (result === null) ? "" : result;
	}

	/**
	 * Bestimmt die Liste der Adress-Manager für das Schuljahr anhand der vorhandenen Adress-Einträge und
	 * erzeugt einen Cache für den schnellen Zugriff auf diese Adressen.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return die Adress-Manager für die Adressen des Schuljahres
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getAdressManager(schuljahr : number) : List<SchuldateiOrganisationseinheitAdressManager> {
		let listManager : List<SchuldateiOrganisationseinheitAdressManager> | null = this._mapAdressenBySchuljahr.get(schuljahr);
		if (listManager !== null)
			return listManager;
		listManager = new ArrayList();
		for (const eintrag of this._organisationseinheit.adressen)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listManager.add(this._mapAdressManagerByID.get(eintrag.id));
		if (listManager.isEmpty())
			throw new IllegalArgumentException("Es konnten keine Adressen für das Schuljahr " + schuljahr + "/" + (schuljahr + 1) + " gefunden werden.")
		this._mapAdressenBySchuljahr.put(schuljahr, listManager);
		let managerHauptstandort : SchuldateiOrganisationseinheitAdressManager | null = null;
		for (const managerOther of listManager) {
			if ((managerOther.istHauptstandort()) && ((managerHauptstandort === null) || SchuldateiUtils.istFrueher(managerHauptstandort.getGueltigBis(), managerOther.getGueltigBis())))
				managerHauptstandort = managerOther;
		}
		if (managerHauptstandort !== null)
			this._mapHauptstandortBySchuljahr.put(schuljahr, managerHauptstandort);
		return listManager;
	}

	/**
	 * Gibt den Adress-Manager für den Hauptstandort der Organisationseinheit in
	 * dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return der Adress-Manager für den Hauptstandort
	 *
	 * @throws IllegalArgumentException wenn für das Schuljahr keine Daten vorhanden sind
	 */
	public getHauptstandort(schuljahr : number) : SchuldateiOrganisationseinheitAdressManager {
		if (!this._mapHauptstandortBySchuljahr.containsKey(schuljahr))
			this.getAdressManager(schuljahr);
		const result : SchuldateiOrganisationseinheitAdressManager | null = this._mapHauptstandortBySchuljahr.get(schuljahr);
		if (result === null)
			throw new IllegalArgumentException("Es konnte kein Hauptstandort für die Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " in diesem Schuljahr gefunden werden.")
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiOrganisationseinheitManager(obj : unknown) : SchuldateiOrganisationseinheitManager {
	return obj as SchuldateiOrganisationseinheitManager;
}
