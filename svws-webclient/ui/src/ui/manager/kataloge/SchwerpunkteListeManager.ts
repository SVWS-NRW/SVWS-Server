import { AuswahlManager } from "../../AuswahlManager";
import { JavaInteger } from "../../../../../core/src";
import type { Comparator, JavaFunction, List, Schulform, Schuljahresabschnitt, SchuelerSchwerpunkt as Schwerpunkt } from "../../../../../core/src";
import { Arrays, HashSet, JavaLong, JavaString } from "../../../../../core/src";

export class SchwerpunkteListeManager extends AuswahlManager<number, Schwerpunkt, Schwerpunkt> {

	/**
     * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
     */
	private static readonly _schwerpunkteToId: JavaFunction<Schwerpunkt, number> = { apply: (ba: Schwerpunkt) => ba.id };
	private readonly _idsOfReferencedSchwerpunkte: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Schwerpunkte in Schwerpunktlisten.
	 */
	public static readonly comparator: Comparator<Schwerpunkt> = { compare: (a: Schwerpunkt, b: Schwerpunkt) => {
		let cmp;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
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
	 * @param idSchuljahresabschnitt       der Schuljahresabschnitt, auf den sich die Schwerpunkt bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param schwerpunkte     	           die Liste der Schwerpunkte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, schwerpunkte: List<Schwerpunkt>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, schwerpunkte, SchwerpunkteListeManager.comparator,
			SchwerpunkteListeManager._schwerpunkteToId, SchwerpunkteListeManager._schwerpunkteToId, Arrays.asList());
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected compareAuswahl(a: Schwerpunkt, b: Schwerpunkt): number {
		return SchwerpunkteListeManager.comparator.compare(a, b);
	}

	get idsOfReferencedSchwerpunkte(): HashSet<number> {
		return this._idsOfReferencedSchwerpunkte;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedSchwerpunkte.clear();
		for (const ba of this.liste.auswahl()) {
			if (ba.referenziertInAnderenTabellen) {
				this._idsOfReferencedSchwerpunkte.add(ba.id);
			}
		}
	}

	protected checkFilter(eintrag: Schwerpunkt): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this.searchTerm !== "") {
			// if searchTerm is defined filter for matching entries
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	private entryMatchesSearchterm(eintrag: Schwerpunkt) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung !== null ? eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower) : false;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

}
