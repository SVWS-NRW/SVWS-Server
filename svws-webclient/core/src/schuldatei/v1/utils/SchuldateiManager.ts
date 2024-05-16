import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuldateiEintrag } from '../../../schuldatei/v1/data/SchuldateiEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { SchuldateiOrganisationseinheit } from '../../../schuldatei/v1/data/SchuldateiOrganisationseinheit';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';

export class SchuldateiManager extends JavaObject {

	/**
	 * Die Liste mit allen Einträgen der Schuldatei
	 */
	private readonly listEintraege : List<SchuldateiEintrag>;

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
	public constructor(daten : List<SchuldateiEintrag>) {
		super();
		this.listEintraege = daten;
		for (const eintrag of daten) {
			const organisationseinheit : SchuldateiOrganisationseinheit = eintrag.organisationseinheit;
			if (this.mapOrganisationseinheitenBySchulnummer.containsKey(organisationseinheit.schulnummer))
				throw new IllegalArgumentException("Die Liste mit den Organisationseinheiten enthält mindestens einen doppelten Eintrag (Schulnummer " + organisationseinheit.schulnummer + ")")
			this.mapOrganisationseinheitenBySchulnummer.put(organisationseinheit.schulnummer, organisationseinheit);
		}
	}

	/**
	 * Gibt die Liste aller Einträge der Schuldatei zurück.
	 *
	 * @return die Liste aller Einträge der Schuldatei
	 */
	public getList() : List<SchuldateiEintrag> {
		return this.listEintraege;
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
