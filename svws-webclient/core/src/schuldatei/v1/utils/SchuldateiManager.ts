import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap } from '../../../java/util/HashMap';
import { Schuldatei } from '../../../schuldatei/v1/data/Schuldatei';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class SchuldateiManager extends JavaObject {

	/**
	 * Die Daten der Schuldatei
	 */
	private readonly schuldatei : Schuldatei;

	/**
	 * Eine Map mit allen Organisationseinheiten, welche ihrer Schulnummer zugeordnet sind
	 */
	private readonly mapOrganisationseinheitenBySchulnummer : JavaMap<number, SchuldateiOrganisationseinheit> = new HashMap<number, SchuldateiOrganisationseinheit>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen
	 * Organisationseinheiten der Schuldatei.
	 *
	 * @param daten   die Liste mit den Organisationseinheiten der Schuldatei
	 */
	public constructor(daten : Schuldatei) {
		super();
		this.schuldatei = daten;
		for (const organisationseinheit of this.schuldatei.organisationseinheit) {
			if (this.mapOrganisationseinheitenBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")")
			this.mapOrganisationseinheitenBySchulnummer.put(organisationseinheit.schulnummer, organisationseinheit);
		}
	}

	/**
	 * Gibt die Liste aller Organisationseinheiten der Schuldatei zurück.
	 *
	 * @return die Liste aller Organisationseinheiten der Schuldatei
	 */
	public getList() : List<SchuldateiOrganisationseinheit> {
		return this.schuldatei.organisationseinheit;
	}

	/**
	 * Gibt die Organisationseinheit für die übergebene Schulnummer zurück.
	 *
	 * @param schulnummer   die Schulnummer
	 *
	 * @return die Organisationseinheit
	 */
	public getOrganisationsheinheitBySchulnummer(schulnummer : number) : SchuldateiOrganisationseinheit | null {
		return this.mapOrganisationseinheitenBySchulnummer.get(schulnummer);
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
