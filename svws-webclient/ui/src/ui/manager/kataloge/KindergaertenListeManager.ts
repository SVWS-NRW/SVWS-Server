import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";


export class KindergaertenListeManager extends AuswahlManager<number, Kindergarten, Kindergarten> {

	private static readonly _kindergartenToId = (k: Kindergarten) => k.id;
	private readonly _idsOfReferencedKindergaerten: HashSet<number> = new HashSet();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Kindergärten
	 */
	public static readonly comparator: Comparator<Kindergarten> = {
		compare: (a: Kindergarten, b: Kindergarten) => {
			let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0) {
				return cmp;
			}
			return JavaLong.compare(a.id, b.id);
		},
	};


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param kindergaerten     			  die Liste der Kindergärten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, kindergaerten: List<Kindergarten>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, kindergaerten,
			KindergaertenListeManager.comparator, KindergaertenListeManager._kindergartenToId, KindergaertenListeManager._kindergartenToId, []);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedKindergaerten.clear();
		for (const k of this.liste.auswahl()) {
			if (k.referenziertInAnderenTabellen) {
				this._idsOfReferencedKindergaerten.add(k.id);
			}
		}
	}

	protected compareAuswahl(a: Kindergarten, b: Kindergarten): number {
		return KindergaertenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(kindergarten: Kindergarten): boolean {
		if (this._filterNurSichtbar && !kindergarten.istSichtbar) {
			return false;
		}
		return this.entryMatchesSearchTerm(kindergarten);
	}

	private entryMatchesSearchTerm(kindergarten: Kindergarten): boolean {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return kindergarten.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}


	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get idsOfReferencedKindergaerten(): HashSet<number> {
		return this._idsOfReferencedKindergaerten;
	}
}
