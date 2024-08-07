import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKataloge } from '../../../schulen/v1/data/SchuldateiKataloge';
import { HashMap } from '../../../java/util/HashMap';
import { Schuldatei } from '../../../schulen/v1/data/Schuldatei';
import { SchuldateiOrganisationseinheitManager } from '../../../schulen/v1/utils/SchuldateiOrganisationseinheitManager';
import { SchuldateiOrganisationseinheit } from '../../../schulen/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import { SchuldateiKatalogManager } from '../../../schulen/v1/utils/SchuldateiKatalogManager';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class SchuldateiManager extends JavaObject {

	/**
	 * Die Daten der Schuldatei
	 */
	private readonly _schuldatei : Schuldatei;

	/**
	 * Die Kataloge zu der Schuldatei
	 */
	private readonly _kataloge : SchuldateiKataloge;

	/**
	 * Die Kataloge zu der Schuldatei anhand ihrer Namen
	 */
	private readonly _mapKataloge : JavaMap<string, SchuldateiKatalogManager> = new HashMap<string, SchuldateiKatalogManager>();

	/**
	 * Der Katalog der Arten von Adressen
	 */
	public readonly katalogAddressarten : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Arten von Attributen
	 */
	public readonly katalogAttribute : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schulbetriebsschlüssel (Betriebsschlüssel)
	 */
	public readonly katalogSchulbetriebsschluessel : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Erreichbarkeiten
	 */
	public readonly katalogErreichbarkeiten : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Gliederung
	 */
	public readonly katalogGliederungen : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Hauptstandortart (Hauptstandortadresse)
	 */
	public readonly katalogHauptstandort : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Art von Heimen/Internaten
	 */
	public readonly katalogHeimInternat : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Kommunikationsgruppen
	 */
	public readonly katalogKommunikationsgruppen : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Liegenschaften
	 */
	public readonly katalogLiegenschaftsarten : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Merkmal
	 */
	public readonly katalogMerkmale : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Eigenschaften von Organisationseinheiten
	 */
	public readonly katalogOergangisationseinheitEigenschaften : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Arten von Organisationseinheiten
	 */
	public readonly katalogOrganisationseinheitarten : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Qualitäten zu den Ortskoordinaten
	 */
	public readonly katalogQualitaetenVerortung : SchuldateiKatalogManager;

	/**
	 * Der Katalog des Rechtstatus von Schulen
	 */
	public readonly katalogRechtsstatus : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schließungsgründe
	 */
	public readonly katalogSchliessungsGruende : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schularten
	 */
	public readonly katalogSchularten : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schulaufsichten
	 */
	public readonly katalogSchulaufsicht : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schulformen
	 */
	public readonly katalogSchulformen : SchuldateiKatalogManager;

	/**
	 * Der Katalog der Schulträger
	 */
	public readonly katalogArtDerTraegerschaft : SchuldateiKatalogManager;

	/**
	 * Eine Map mit den Managern für alle Organisationseinheiten, welche den Schulnummern ihrer Organisationseinheiten zugeordnet sind
	 */
	private readonly _mapOrganisationseinheitManagerBySchulnummer : JavaMap<number, SchuldateiOrganisationseinheitManager> = new HashMap<number, SchuldateiOrganisationseinheitManager>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen
	 * Organisationseinheiten der Schuldatei.
	 *
	 * @param schuldatei   die Schuldatei
	 * @param kataloge     die Kataloge zu der Schuldatei
	 */
	public constructor(schuldatei : Schuldatei, kataloge : SchuldateiKataloge) {
		super();
		this._schuldatei = schuldatei;
		this._kataloge = kataloge;
		for (const eintrag of kataloge.katalog) {
			const katalog : SchuldateiKatalogManager | null = this._mapKataloge.computeIfAbsent(eintrag.katalog, { apply : (k: string) => new SchuldateiKatalogManager(k) });
			if (katalog !== null)
				katalog.addEintrag(eintrag);
		}
		this.katalogAddressarten = this.getKatalogFromMap("ArtDerAdresse");
		this.katalogAttribute = this.getKatalogFromMap("Attribut");
		this.katalogSchulbetriebsschluessel = this.getKatalogFromMap("Betriebsschluessel");
		this.katalogErreichbarkeiten = this.getKatalogFromMap("Erreichbarkeit");
		this.katalogGliederungen = this.getKatalogFromMap("Gliederung");
		this.katalogHauptstandort = this.getKatalogFromMap("Hauptstandortadresse");
		this.katalogHeimInternat = this.getKatalogFromMap("HeimInternat");
		this.katalogKommunikationsgruppen = this.getKatalogFromMap("Kommunikationsgruppe");
		this.katalogLiegenschaftsarten = this.getKatalogFromMap("LiegenschaftArt");
		this.katalogMerkmale = this.getKatalogFromMap("Merkmal");
		this.katalogOergangisationseinheitEigenschaften = this.getKatalogFromMap("OE_Eigenschaften");
		this.katalogOrganisationseinheitarten = this.getKatalogFromMap("OrganisationseinheitArt");
		this.katalogQualitaetenVerortung = this.getKatalogFromMap("QualitaetVerortung");
		this.katalogRechtsstatus = this.getKatalogFromMap("Rechtsstatus");
		this.katalogSchliessungsGruende = this.getKatalogFromMap("SchliessungGrund");
		this.katalogSchularten = this.getKatalogFromMap("Schulart");
		this.katalogSchulaufsicht = this.getKatalogFromMap("Schulaufsicht");
		this.katalogSchulformen = this.getKatalogFromMap("Schulform");
		this.katalogArtDerTraegerschaft = this.getKatalogFromMap("Traeger");
		for (const organisationseinheit of schuldatei.organisationseinheit) {
			if (this._mapOrganisationseinheitManagerBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")")
			this._mapOrganisationseinheitManagerBySchulnummer.put(organisationseinheit.schulnummer, new SchuldateiOrganisationseinheitManager(this, organisationseinheit));
		}
		for (const manager of this._mapOrganisationseinheitManagerBySchulnummer.values()) {
			manager.validateOeReferenzen();
		}
	}

	/**
	 * Ermittelt den Manager für den Katalog mit angegebenen Namen aus der übergebenen Map der Katalog-Manager.
	 *
	 * @param nameKatalog   der Name des Katalogs
	 *
	 * @return der Katalog-Manager
	 *
	 * @throws IllegalArgumentException falls kein Katalog mit dem Namen existiert
	 */
	private getKatalogFromMap(nameKatalog : string) : SchuldateiKatalogManager {
		const katalog : SchuldateiKatalogManager | null = this._mapKataloge.get(nameKatalog);
		if (katalog === null)
			throw new IllegalArgumentException("Die Kataloge enthält keine Einträge für den Katalog mit dem Namen '" + nameKatalog! + "'")
		return katalog;
	}

	/**
	 * Gibt die Liste aller Organisationseinheiten der Schuldatei zurück.
	 *
	 * @return die Liste aller Organisationseinheiten der Schuldatei
	 */
	public getList() : List<SchuldateiOrganisationseinheit> {
		return this._schuldatei.organisationseinheit;
	}

	/**
	 * Gibt den Manager für die Organisationseinheit mit der übergebenen Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return der Manager für die Organisationseinheit
	 */
	public getOrganisationsheinheitManager(schulnummer : number) : SchuldateiOrganisationseinheitManager | null {
		return this._mapOrganisationseinheitManagerBySchulnummer.get(schulnummer);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiManager(obj : unknown) : SchuldateiManager {
	return obj as SchuldateiManager;
}
