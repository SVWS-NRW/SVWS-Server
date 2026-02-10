import { AuswahlManager } from "../../AuswahlManager";
import type { Betriebsart } from "../../../../../core/src/core/data/schule/Betriebsart";
import type { Comparator, JavaFunction, List, Schulform, Schuljahresabschnitt } from "../../../../../core/src";
import { Arrays, HashSet, JavaLong, JavaString } from "../../../../../core/src";

export class BetriebsartenListeManager extends AuswahlManager<number, Betriebsart, Betriebsart> {

	/**
     * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
     */
	private static readonly _betriebsartenToId: JavaFunction<Betriebsart, number> = { apply: (ba: Betriebsart) => ba.id };
	private readonly _idsOfReferencedBetriebsarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Betriebsarten in Betriebsartlisten.
	 */
	public static readonly comparator: Comparator<Betriebsart> = { compare: (a: Betriebsart, b: Betriebsart) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0) {
			return cmp;
		}
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp !== 0) {
			return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Betriebsart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param betriebsarten     	       die Liste der Betriebsarten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, betriebsarten: List<Betriebsart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, betriebsarten, BetriebsartenListeManager.comparator,
			BetriebsartenListeManager._betriebsartenToId, BetriebsartenListeManager._betriebsartenToId, Arrays.asList());
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected compareAuswahl(a: Betriebsart, b: Betriebsart): number {
		return BetriebsartenListeManager.comparator.compare(a, b);
	}

	get idsOfReferencedBetriebsarten(): HashSet<number> {
		return this._idsOfReferencedBetriebsarten;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedBetriebsarten.clear();
		for (const ba of this.liste.auswahl()) {
			if (ba.referenziertInAnderenTabellen) {
				this._idsOfReferencedBetriebsarten.add(ba.id);
			}
		}
	}

	protected checkFilter(eintrag: Betriebsart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}
		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Betriebsart) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

}
