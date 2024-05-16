import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiKataloge } from '../../../schuldatei/v1/data/SchuldateiKataloge';
import { HashMap } from '../../../java/util/HashMap';
import { Schuldatei } from '../../../schuldatei/v1/data/Schuldatei';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import { SchuldateiKatalogManager } from '../../../schuldatei/v1/utils/SchuldateiKatalogManager';
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
	 * Eine Map mit allen Organisationseinheiten, welche ihrer Schulnummer zugeordnet sind
	 */
	private readonly _mapOrganisationseinheitenBySchulnummer : JavaMap<number, SchuldateiOrganisationseinheit> = new HashMap<number, SchuldateiOrganisationseinheit>();


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
			if (katalog !== null) {
				katalog.addEintrag(eintrag);
			}
		}
		for (const organisationseinheit of schuldatei.organisationseinheit) {
			if (this._mapOrganisationseinheitenBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")")
			this._mapOrganisationseinheitenBySchulnummer.put(organisationseinheit.schulnummer, organisationseinheit);
		}
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
	 * Gibt die Organisationseinheit für die übergebene Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return die Organisationseinheit
	 */
	public getOrganisationsheinheitBySchulnummer(schulnummer : number) : SchuldateiOrganisationseinheit | null {
		return this._mapOrganisationseinheitenBySchulnummer.get(schulnummer);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schuldatei.v1.utils.SchuldateiManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schuldatei.v1.utils.SchuldateiManager'].includes(name);
	}

}

export function cast_de_svws_nrw_schuldatei_v1_utils_SchuldateiManager(obj : unknown) : SchuldateiManager {
	return obj as SchuldateiManager;
}
