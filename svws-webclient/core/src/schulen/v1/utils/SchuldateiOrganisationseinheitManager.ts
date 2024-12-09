import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiOrganisationseinheitGrunddaten } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitGrunddaten';
import { SchuldateiKatalogeintrag } from '../../../schulen/v1/data/SchuldateiKatalogeintrag';
import { SchuldateiOrganisationseinheitEigenschaft } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitEigenschaft';
import { HashMap } from '../../../java/util/HashMap';
import { SchuldateiOrganisationseinheitAdressManager } from '../../../schulen/v1/utils/SchuldateiOrganisationseinheitAdressManager';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { SchuldateiManager } from '../../../schulen/v1/utils/SchuldateiManager';
import { SchuldateiOrganisationseinheitMerkmal } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitMerkmal';
import { SchuldateiOrganisationseinheit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheit';
import { SchuldateiOrganisationseinheitGliederung } from '../../../schulen/v1/data/SchuldateiOrganisationseinheitGliederung';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
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
	 * Die Manager für die Adressen anhand ihrer ID
	 */
	private readonly _mapAdressManagerByID : JavaMap<number, SchuldateiOrganisationseinheitAdressManager> = new HashMap<number, SchuldateiOrganisationseinheitAdressManager>();

	/**
	 * Cache: Eine Map der Grunddaten anhand des Schuljahres
	 */
	private readonly _mapGrunddatenBySchuljahr : JavaMap<number, SchuldateiOrganisationseinheitGrunddaten> = new HashMap<number, SchuldateiOrganisationseinheitGrunddaten>();

	/**
	 * Cache: Eine Map der Gliederung anhand des Schuljahres
	 */
	private readonly _mapGliederungenBySchuljahr : JavaMap<number, List<SchuldateiOrganisationseinheitGliederung>> = new HashMap<number, List<SchuldateiOrganisationseinheitGliederung>>();

	/**
	 * Cache: Eine Map der Eigenschaften anhand des Schuljahres
	 */
	private readonly _mapEigenschaftenBySchuljahr : JavaMap<number, List<SchuldateiOrganisationseinheitEigenschaft>> = new HashMap<number, List<SchuldateiOrganisationseinheitEigenschaft>>();

	/**
	 * Cache: Eine Map der Merkmale anhand des Schuljahres
	 */
	private readonly _mapMerkmaleBySchuljahr : JavaMap<number, List<SchuldateiOrganisationseinheitMerkmal>> = new HashMap<number, List<SchuldateiOrganisationseinheitMerkmal>>();

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
		if ((this._organisationseinheit.oeart !== null) && (!this._managerSchuldatei.katalogOrganisationseinheitarten.hasEintrag(this._organisationseinheit.oeart)))
			throw new IllegalArgumentException(JavaString.format("Die Art %s der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", this._organisationseinheit.oeart, this._organisationseinheit.schulnummer))
		this.validateGrunddaten();
		this.validateAdressen();
		this.validateMerkmale();
		this.validateErreichbarkeiten();
		this.validateEigenschaften();
		this.validateGliederungen();
	}

	/**
	 * Validiere die Referenzenzen auf Organisationseinheiten von dieser Organisationseinheit
	 * Die Prüfung kann erst erfolgen, wenn alle Organisationseinheiten eingelesen wurden, weswegen
	 * diese Prüfung separat erfolgt.
	 * Folgender Referenzen sind vorhanden und werden geprüft:
	 *  - grunddaten.schultraegernummer
	 *  - grunddaten.obereschulaufsicht
	 *  - grunddaten.untereschulaufsicht
	 *  - grunddaten.zfsl
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht widerspruchsfrei sind
	 */
	public validateOeReferenzen() : void {
		for (const grunddaten of this._organisationseinheit.grunddaten) {
			if ((!JavaObject.equalsTranspiler(grunddaten.schultraegernummer, ("0"))) && (!JavaString.isEmpty(grunddaten.schultraegernummer)) && (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.schultraegernummer) === null))
				throw new IllegalArgumentException("Der Schulträger " + grunddaten.schultraegernummer + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
			if ((!JavaObject.equalsTranspiler(grunddaten.obereschulaufsicht, ("0"))) && (!JavaString.isEmpty(grunddaten.obereschulaufsicht)) && (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.obereschulaufsicht) === null))
				throw new IllegalArgumentException("Die obere Schulaufsicht " + grunddaten.obereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
			if ((!JavaObject.equalsTranspiler(grunddaten.untereschulaufsicht, ("0"))) && (!JavaString.isEmpty(grunddaten.untereschulaufsicht)) && (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.untereschulaufsicht) === null))
				throw new IllegalArgumentException("Die untere Schulaufsicht " + grunddaten.untereschulaufsicht + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
			if ((!JavaObject.equalsTranspiler(grunddaten.zfsl, ("0"))) && (!JavaString.isEmpty(grunddaten.zfsl)) && (this._managerSchuldatei.getOrganisationsheinheitManager(grunddaten.zfsl) === null))
				throw new IllegalArgumentException("Das ZfsL " + grunddaten.zfsl + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " konnte nicht als Organisationseinheit gefunden werden.")
		}
	}

	/**
	 * Validiere die Grunddaten der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateGrunddaten() : void {
		for (const grunddaten of this._organisationseinheit.grunddaten) {
			if (!JavaObject.equalsTranspiler(this._organisationseinheit.schulnummer, (grunddaten.schulnummer)))
				throw new IllegalArgumentException("Die Schulnummer " + grunddaten.schulnummer + " bei den Grunddaten passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogRechtsstatus.hasEintragInZeitraum(grunddaten, grunddaten.rechtsstatus))
				throw new IllegalArgumentException("Der Rechtstatus " + grunddaten.rechtsstatus + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if ((!JavaString.isEmpty(grunddaten.artdertraegerschaft)) && (!this._managerSchuldatei.katalogArtDerTraegerschaft.hasEintragInZeitraum(grunddaten, grunddaten.artdertraegerschaft)))
				throw new IllegalArgumentException("Die Art der Trägerschaft " + grunddaten.artdertraegerschaft + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if (!this._managerSchuldatei.katalogSchulbetriebsschluessel.hasEintragInZeitraum(grunddaten, grunddaten.schulbetriebsschluessel))
				throw new IllegalArgumentException("Der Schulbetriebsschlüssel " + grunddaten.schulbetriebsschluessel + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
			if ((grunddaten.internatsbetrieb !== null) && (!JavaObject.equalsTranspiler(grunddaten.internatsbetrieb, ("0")))) {
				if (!this._managerSchuldatei.katalogHeimInternat.hasEintragInZeitraum(grunddaten, grunddaten.internatsbetrieb))
					throw new IllegalArgumentException("Der Schlüssel für den Internatsbetrieb " + grunddaten.internatsbetrieb + " bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ist im zugehörigen Katalog nicht vorhanden.")
				else
					if (grunddaten.internatsplaetze === 0)
						throw new IllegalArgumentException("Die Internatsplätze haben einen Wert von 0 bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ,obwohl Internatsbetrieb vorliegt.")
			} else {
				if (grunddaten.internatsplaetze > 0)
					throw new IllegalArgumentException("Die Internatsplätze haben einen Wert > 0 bei den Grunddaten der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + " ,obwohl kein Internatsbetrieb vorliegt.")
			}
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
	 * @param schuljahr    das schuljahr
	 * @param grunddaten   die Grunddaten
	 *
	 * @return ein Array mit den drei Werten zur Schulform
	 */
	private static getSchulformInfo(schuljahr : number, grunddaten : SchuldateiOrganisationseinheitGrunddaten) : Array<string> {
		const sf : Array<string> = ["", "", ""];
		for (const schulform of grunddaten.schulform) {
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, schulform)) {
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
		}
		return sf;
	}

	/**
	 * Validiere die Schulform der übergebenen Grunddaten der Organisationseinheit
	 * Die Schulformcodes "Schulform" und "SchulformASD" müssen immer zusammen auftreten und der
	 * Schlüsselwert des Katalogeintrags von SchulformASD muss dem Wert von Schulform entsprechen!
	 *
	 * @param grunddaten   die Grundaten der Organisationseinheit.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateSchulform(grunddaten : SchuldateiOrganisationseinheitGrunddaten) : void {
		let isSchulform : boolean = false;
		let isSchulformASD : boolean = false;
		let schulformWert : string = "";
		let schulformAsdWert : string = "";
		let schuljahr : number = SchuldateiUtils._immerGueltigBis;
		for (const schulform of grunddaten.schulform) {
			if (JavaObject.equalsTranspiler("Schulform", (schulform.schulformcode))) {
				schulformWert = schulform.schulformwert;
				isSchulform = true;
			} else
				if (JavaObject.equalsTranspiler("SchulformASD", (schulform.schulformcode))) {
					if (!this._managerSchuldatei.katalogSchulformen.hasEintragInZeitraum(schulform, schulform.schulformwert))
						throw new IllegalArgumentException("Die SchulformASD '" + schulform.schulformwert + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht im Katalog enthalten.")
					schulformAsdWert = schulform.schulformwert;
					schuljahr = (schulform.gueltigbis === null ? SchuldateiUtils._immerGueltigBis : SchuldateiUtils.schuljahrAusDatum(schulform.gueltigbis));
					isSchulformASD = true;
				} else
					if (JavaObject.equalsTranspiler("Schulart", (schulform.schulformcode))) {
						if (!this._managerSchuldatei.katalogSchularten.hasEintragInZeitraum(schulform, schulform.schulformwert))
							throw new IllegalArgumentException("Die Schulart '" + schulform.schulformwert + "' ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht im Katalog enthalten.")
					}
		}
		if (!isSchulform || !isSchulformASD)
			throw new IllegalArgumentException("Die Schulform ist bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + " nicht gesetzt.")
		const eintrag : SchuldateiKatalogeintrag | null = this._managerSchuldatei.katalogSchulformen.getEintragBySchuljahrAndWert(schuljahr, schulformAsdWert);
		if (eintrag === null || !JavaObject.equalsTranspiler(eintrag.schluessel, (schulformWert)))
			throw new IllegalArgumentException("Die Schulformen Schulform und SchulformASD passen nicht zusammen bei der Organisationseinheit mit der Schulnummer " + grunddaten.schulnummer + ".")
	}

	/**
	 * Validiere die Adressen der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateAdressen() : void {
		for (const adresse of this._organisationseinheit.adressen) {
			if (this._mapAdressManagerByID.containsKey(adresse.id))
				throw new IllegalArgumentException(JavaString.format("Die Addressen bei der Organisationseinheit mit der Schulnummer %s hat Duplikate.", this._organisationseinheit.schulnummer))
			this._mapAdressManagerByID.put(adresse.id, new SchuldateiOrganisationseinheitAdressManager(this._managerSchuldatei, this, adresse, this._organisationseinheit.erreichbarkeiten));
		}
	}

	/**
	 * Validiere die Merkmale der Organisationseinheit
	 * Validiert werden die
	 *  - Die Schulnummer muss identisch sein mit der Nummer dieser Organisationseinheit
	 *  - Nummer der Liegenschaft :
	 *       == 0  für alle Liegenschaften immer ok
	 *        > 0  die Liegenschaftsnummer muss in den Adressen existieren
	 *  - Das Merkmal muss im Katalog der Merkmale existieren
	 *  - Das Attribut muss im Katalog der Attribute existieren
	 *  Die Properties Merkmalgruppe, Attributgruppe und Wert sollen laut Thomas Heyn nicht berücksichtigt werden,
	 *  da die Felder nur zur Differenzierung in der Schulaufsicht benötigt werden.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateMerkmale() : void {
		for (const merkmal of this._organisationseinheit.merkmal) {
			if (!JavaObject.equalsTranspiler(this._organisationseinheit.schulnummer, (merkmal.schulnummer)))
				throw new IllegalArgumentException("Die Schulnummer " + merkmal.schulnummer + " bei dem Merkmal mit der ID " + merkmal.id + " passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if ((merkmal.liegenschaft !== 0) && (!this.existsLiegenschaftInAdressen(merkmal.liegenschaft)))
				throw new IllegalArgumentException("Für die Liegenschaftsnummer " + merkmal.liegenschaft + " bei dem Merkmal mit der ID " + merkmal.id + " existiert keine Adresse mit der gleichen Liegenschaftsnummer bei der Organisationseinheit mit der Schulnummer " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogMerkmale.hasEintragInZeitraum(merkmal, merkmal.merkmal))
				throw new IllegalArgumentException("Das Merkmal " + merkmal.merkmal + JavaString.format(" der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", this._organisationseinheit.schulnummer))
			if (!this._managerSchuldatei.katalogAttribute.hasEintragInZeitraum(merkmal, merkmal.attribut))
				throw new IllegalArgumentException("Das Attribut " + merkmal.attribut + JavaString.format(" der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", this._organisationseinheit.schulnummer))
		}
	}

	/**
	 * Validiere die Erreichbarkeiten der Organisationseinheit
	 * Erreichbarkeiten referenzieren über die Eigenschaft liegenschaft die entsprechende Adresse über die
	 * gleiche Eigenschaft.
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateErreichbarkeiten() : void {
		for (const erreichbarkeit of this._organisationseinheit.erreichbarkeiten) {
			if (!JavaObject.equalsTranspiler(this._organisationseinheit.schulnummer, (erreichbarkeit.schulnummer)))
				throw new IllegalArgumentException("Die Schulnummer " + erreichbarkeit.schulnummer + " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if ((erreichbarkeit.liegenschaft !== 0) && !this.existsLiegenschaftInAdressen(erreichbarkeit.liegenschaft))
				throw new IllegalArgumentException("Für die Liegenschaftsnummer " + erreichbarkeit.liegenschaft + " existiert keine Adresse mit der gleichen Liegenschaftsnummer bei der Organisationseinheit mit Schulnummer " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogErreichbarkeiten.hasEintragInZeitraum(erreichbarkeit, erreichbarkeit.codekey))
				throw new IllegalArgumentException(JavaString.format("Der Typ (codekey) der Erreichbarkeit mit der Id %s in der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", erreichbarkeit.id, erreichbarkeit.schulnummer))
			if (!this._managerSchuldatei.katalogKommunikationsgruppen.hasEintragInZeitraum(erreichbarkeit, erreichbarkeit.kommgruppe))
				throw new IllegalArgumentException(JavaString.format("Die Kommunikationsgruppe >%s< der Erreichbarkeit mit der Id %s in der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", erreichbarkeit.kommgruppe, erreichbarkeit.id, erreichbarkeit.schulnummer))
		}
	}

	/**
	 * Validiere die Eigenschaften der Organisationseinheit
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateEigenschaften() : void {
		for (const eigenschaft of this._organisationseinheit.oeEigenschaften) {
			if (!JavaObject.equalsTranspiler(this._organisationseinheit.schulnummer, (eigenschaft.schulnummer)))
				throw new IllegalArgumentException("Die Schulnummer " + eigenschaft.schulnummer + " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if (!this._managerSchuldatei.katalogOergangisationseinheitEigenschaften.hasEintragInZeitraum(eigenschaft, eigenschaft.eigenschaft))
				throw new IllegalArgumentException("Die Eigenschaft " + eigenschaft.eigenschaft + JavaString.format(" mit der Id %d in der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", eigenschaft.id, this._organisationseinheit.schulnummer))
		}
	}

	/**
	 * Validiere die Gliederungen der Organisationseinheit
	 * (Der Bereich Gliederungen wird in Zukunft wegfallen, da diese Informationen dann unter Merkmale
	 *  eingestellt werden)
	 *
	 * @throws IllegalArgumentException falls die Daten der Schuldatei nicht fehlerfrei eingelesen werden können
	 */
	private validateGliederungen() : void {
		for (const gliederung of this._organisationseinheit.gliederung) {
			if (!JavaObject.equalsTranspiler(this._organisationseinheit.schulnummer, (gliederung.schulnummer)))
				throw new IllegalArgumentException("Die Schulnummer " + gliederung.schulnummer + " bei der Erreichbarkeit passt nicht zu der Schulnummer der Organisationseinheit " + this._organisationseinheit.schulnummer + ".")
			if ((!JavaString.isEmpty(gliederung.gliederung)) && (!this._managerSchuldatei.katalogGliederungen.hasEintragInZeitraum(gliederung, gliederung.gliederung)))
				throw new IllegalArgumentException("Die Gliederung " + gliederung.gliederung + JavaString.format("mit der Id %d in der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", gliederung.id, this._organisationseinheit.schulnummer))
			if ((!JavaString.isEmpty(gliederung.foerderschwerpunkt)) && (!this._managerSchuldatei.katalogFoerderschwerpunkte.hasEintragInZeitraum(gliederung, gliederung.foerderschwerpunkt)))
				throw new IllegalArgumentException("Der Förderschwerpunkt " + gliederung.foerderschwerpunkt + JavaString.format("mit der Id %d in der Organisationseinheit mit der Schulnummer %s hat keinen zugehörigen Katalog-Eintrag.", gliederung.id, this._organisationseinheit.schulnummer))
		}
	}

	/**
	 * Gibt die Schulnummer der Organisationseinheit zurück.
	 *
	 * @return die Schulnummer
	 */
	public getSchulnummer() : string {
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
	 * @param	schuljahr	das Schuljahr
	 *
	 * @return die Bezeichnung der Art der Organisationseinheit
	 */
	public getArtBezeichnung(schuljahr : number) : string | null {
		return this._managerSchuldatei.katalogOrganisationseinheitarten.getBezeichnung(schuljahr, this._organisationseinheit.oeart);
	}

	/**
	 * Gibt den ersten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public getAmtlicheBezeichnung1() : string {
		return this._organisationseinheit.amtsbez1;
	}

	/**
	 * Gibt den zweiten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public getAmtlicheBezeichnung2() : string {
		return this._organisationseinheit.amtsbez2;
	}

	/**
	 * Gibt den dritten Teil der amtlichen Bezeichnung der Organisationseinheit zurück
	 *
	 * @return die amtliche Bezeichnung
	 */
	public getAmtlicheBezeichnung3() : string {
		return this._organisationseinheit.amtsbez3;
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
		const sf : Array<string> = SchuldateiOrganisationseinheitManager.getSchulformInfo(schuljahr, eintrag);
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
	public getRechtsstatus(schuljahr : number) : string {
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
	public getSchultraegernummer(schuljahr : number) : string {
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
		const nummer : string = this.getGrunddaten(schuljahr).schultraegernummer;
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
	public getArtDerTraegerschaft(schuljahr : number) : string {
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
	public getSchulbetriebsschluessel(schuljahr : number) : string {
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
	public getKapitel(schuljahr : number) : string {
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
	public getObereSchulaufsichtNummer(schuljahr : number) : string {
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
		const nummer : string = this.getGrunddaten(schuljahr).obereschulaufsicht;
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
	public getUntererSchulaufsichtNummer(schuljahr : number) : string {
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
		const nummer : string = this.getGrunddaten(schuljahr).untereschulaufsicht;
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
	public getZfsLNummer(schuljahr : number) : string {
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
		const nummer : string = this.getGrunddaten(schuljahr).zfsl;
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
	public getDienststellenschluessel(schuljahr : number) : string {
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
		return grunddaten.internatsplaetze;
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

	/**
	 * Gibt die Gliederungen der Organisationseinheit in dem angegebenen Schuljahr zurück
	 *
	 * @param schuljahr		das Schuljahr
	 *
	 * @return die Gliederungen im betreffenden Schuljahr
	 */
	public getGliederungen(schuljahr : number) : List<SchuldateiOrganisationseinheitGliederung> {
		let listGliederungen : List<SchuldateiOrganisationseinheitGliederung> | null = this._mapGliederungenBySchuljahr.get(schuljahr);
		if (listGliederungen !== null)
			return listGliederungen;
		listGliederungen = new ArrayList();
		for (const eintrag of this._organisationseinheit.gliederung)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listGliederungen.add(eintrag);
		this._mapGliederungenBySchuljahr.put(schuljahr, listGliederungen);
		return listGliederungen;
	}

	/**
	 * Prüfe ob eine Adresse mit der Liegenschaftsnummer existiert
	 *
	 * @param liegenschaft		die Liegenschaftsnummer
	 *
	 * @return Die Existenz der Liegenschaftsnummer
	 */
	public existsLiegenschaftInAdressen(liegenschaft : number) : boolean {
		for (const adresse of this._mapAdressManagerByID.values()) {
			if (adresse.getLiegenschaftnummer() === liegenschaft)
				return true;
		}
		return false;
	}

	/**
	 * Gibt die Eigenschaften der Organisationseinheit in dem angegebenen Schuljahr zurück
	 *
	 * @param schuljahr		das Schuljahr
	 *
	 * @return die Eigenschaften im betreffenden Schuljahr
	 */
	public getEigenschaften(schuljahr : number) : List<SchuldateiOrganisationseinheitEigenschaft> {
		let listEigenschaften : List<SchuldateiOrganisationseinheitEigenschaft> | null = this._mapEigenschaftenBySchuljahr.get(schuljahr);
		if (listEigenschaften !== null)
			return listEigenschaften;
		listEigenschaften = new ArrayList();
		for (const eintrag of this._organisationseinheit.oeEigenschaften)
			if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
				listEigenschaften.add(eintrag);
		this._mapEigenschaftenBySchuljahr.put(schuljahr, listEigenschaften);
		return listEigenschaften;
	}

	/**
	 * Prüft, ob die Merkmal/Attributs-Kombination der Organisationseinheit in dem angegebenen Schuljahr gesetzt ist.
	 *
	 * @param merkmal       die Merkmals-ID
	 * @param attribut      die Attributs-ID
	 * @param schuljahr		das Schuljahr
	 *
	 * @return boolean, ob die Kombi im betreffenden Schuljahr vorhanden ist
	 */
	public hatMerkmalAttributInSchuljahr(merkmal : number, attribut : number, schuljahr : number) : boolean {
		let listMerkmale : List<SchuldateiOrganisationseinheitMerkmal> | null = this._mapMerkmaleBySchuljahr.get(schuljahr);
		if (listMerkmale === null) {
			listMerkmale = new ArrayList();
			for (const eintrag of this._organisationseinheit.merkmal)
				if (SchuldateiUtils.pruefeSchuljahr(schuljahr, eintrag))
					listMerkmale.add(eintrag);
			this._mapMerkmaleBySchuljahr.put(schuljahr, listMerkmale);
		}
		for (const eintrag of listMerkmale)
			if ((eintrag.merkmal === merkmal) && (eintrag.attribut === attribut))
				return true;
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitManager'].includes(name);
	}

	public static class = new Class<SchuldateiOrganisationseinheitManager>('de.svws_nrw.schulen.v1.utils.SchuldateiOrganisationseinheitManager');

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiOrganisationseinheitManager(obj : unknown) : SchuldateiOrganisationseinheitManager {
	return obj as SchuldateiOrganisationseinheitManager;
}
