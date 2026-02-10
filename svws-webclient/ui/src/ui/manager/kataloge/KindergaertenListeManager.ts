import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { Kindergarten } from '../../../../../core/src/core/data/schule/Kindergarten';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';


export class KindergaertenListeManager extends AuswahlManager<number, Kindergarten, Kindergarten> {

	private static readonly _kindergartenToId: JavaFunction<Kindergarten, number> = { apply: (k: Kindergarten) => k.id };
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
			KindergaertenListeManager.comparator, KindergaertenListeManager._kindergartenToId, KindergaertenListeManager._kindergartenToId, ArrayList.of());
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
		this._eventHandlerFilterChanged.run();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get idsOfReferencedKindergaerten(): HashSet<number> {
		return this._idsOfReferencedKindergaerten;
	}
}
