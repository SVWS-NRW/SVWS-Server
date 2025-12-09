import { AuswahlManager } from "../../../AuswahlManager";
import type { Betrieb } from '../../../../../../core/src/core/data/schule/Betrieb';
import type { List } from '../../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../../core/src/java/util/ArrayList';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';
import { JavaInteger } from '../../../../../../core/src/java/lang/JavaInteger';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { HashSet } from "../../../../../../core/src/java/util/HashSet";


export class BetriebeListeManager extends AuswahlManager<number, Betrieb, Betrieb> {

	private static readonly _betriebToId: JavaFunction<Betrieb, number> = { apply: (a: Betrieb) => a.id };
	private readonly _idsOfReferencedBetriebe: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Betrieben.
	 */
	public static readonly comparator: Comparator<Betrieb> = { compare: (a: Betrieb, b: Betrieb) => {
		let cmp;
		if ((a.sortierung !== null) && (b.sortierung !== null)) {
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
		}
		if ((a.name !== null) && (b.name !== null)) {
			cmp = JavaString.compareTo(a.name, b.name);
			if (cmp !== 0) {
				return cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param betriebe							die Liste der Betriebe
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, betriebe: List<Betrieb>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, betriebe,
			BetriebeListeManager.comparator, BetriebeListeManager._betriebToId, BetriebeListeManager._betriebToId, ArrayList.of());
	}

	/**
	 * Vergleicht zwei Betriebe Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: Betrieb, b: Betrieb): number {
		return BetriebeListeManager.comparator.compare(a, b);
	}


	get idsOfReferencedBetriebe(): HashSet<number> {
		return this._idsOfReferencedBetriebe;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedBetriebe.clear();
		for (const b of this.liste.auswahl()) {
			if (b.referenziertInAnderenTabellen) {
				this._idsOfReferencedBetriebe.add(b.id);
			}
		}
	}

	protected checkFilter(eintrag: any): boolean {
		if (this._filterNurSichtbar && eintrag.istSichtbar === false) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Betrieb) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return ((eintrag.name !== null) && eintrag.name.toLocaleLowerCase().includes(searchTermLower));
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Betriebe.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Betriebe zurück.
	 *
	 * @return true, wenn nur sichtbare Betriebe angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

}
