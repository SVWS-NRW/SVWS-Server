import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import type { Haltestelle } from '../../../../../core/src/core/data/schule/Haltestelle';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { HashSet, type JavaSet } from "../../../../../core/src";

export class HaltestellenListeManager extends AuswahlManager<number, Haltestelle, Haltestelle> {

	private static readonly _haltestelleToId: JavaFunction<Haltestelle, number> = { apply: (h: Haltestelle) => h.id };
	private readonly _idsReferencedHaltestellen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Haltestellen.
	 */
	public static readonly comparator: Comparator<Haltestelle> = { compare: (a: Haltestelle, b: Haltestelle) => {
		let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null)) {
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0) {
				return cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param haltestellen	     			  die Liste der Haltestellen
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, haltestellen: List<Haltestelle>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, haltestellen, HaltestellenListeManager.comparator,
			HaltestellenListeManager._haltestelleToId, HaltestellenListeManager._haltestelleToId, ArrayList.of());
	}

	protected compareAuswahl(a: Haltestelle, b: Haltestelle): number {
		return HaltestellenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Haltestelle): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Haltestelle): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		return ((eintrag.bezeichnung !== null) && eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedHaltestellen.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedHaltestellen.add(l.id);
			}
		}
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

	get idsReferencedHaltestellen(): JavaSet<number> {
		return this._idsReferencedHaltestellen;
	}
}
