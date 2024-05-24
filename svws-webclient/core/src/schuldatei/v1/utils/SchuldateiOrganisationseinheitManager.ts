import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitGrunddaten } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheitGrunddaten';
import { SchuldateiKatalogeintrag } from '../../../schuldatei/v1/data/SchuldateiKatalogeintrag';
import { SchuldateiManager } from '../../../schuldatei/v1/utils/SchuldateiManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { SchuldateiUtils } from '../../../schuldatei/v1/utils/SchuldateiUtils';

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
	 * Cache: Eine Map der Grunddaten anhand des Schuljahres
	 */
	private readonly _mapGrunddatenBySchuljahr : JavaMap<number, SchuldateiOrganisationseinheitGrunddaten> = new HashMap<number, SchuldateiOrganisationseinheitGrunddaten>();


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
		if ((organisationseinheit.oeart !== null) && (!this._managerSchuldatei.katalogOrganisationseinheitarten.hatEintrag(organisationseinheit.oeart)))
			throw new IllegalArgumentException("Die Art " + organisationseinheit.oeart + " der Organisationseinheit mit der Schulnummer " + organisationseinheit.schulnummer + " hat keinen zugehörigen Katalog-Eintrag.")
		for (const grunddaten of this._organisationseinheit.grunddaten) {
			if (this._organisationseinheit.schulnummer! !== grunddaten.schulnummer)
				throw new IllegalArgumentException("Die Schulnummer " + grunddaten.schulnummer + " bei den Grunddaten passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogRechtsstatus.hatEintrag(grunddaten.rechtsstatus))
				throw new IllegalArgumentException("Der Rechtstatus " + grunddaten.rechtsstatus + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if ((grunddaten.artdertraegerschaft !== 0) && (!this._managerSchuldatei.katalogArtDerTraegerschaft.hatEintrag(grunddaten.artdertraegerschaft)))
				throw new IllegalArgumentException("Die Art der Trägerschaft " + grunddaten.artdertraegerschaft + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if (!this._managerSchuldatei.katalogSchulbetriebsschluessel.hatEintrag(grunddaten.schulbetriebsschluessel))
				throw new IllegalArgumentException("Der Schulbetriebsschlüssel " + grunddaten.schulbetriebsschluessel + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
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
	 * Gibt den Dienststellenschlüssel der Organisationseinheit zurück,
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
		return grunddaten.internatsplaetze === null ? 0 : grunddaten.internatsplaetze;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.utils.SchuldateiOrganisationseinheitManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schuldatei_v1_utils_SchuldateiOrganisationseinheitManager(obj : unknown) : SchuldateiOrganisationseinheitManager {
	return obj as SchuldateiOrganisationseinheitManager;
}
