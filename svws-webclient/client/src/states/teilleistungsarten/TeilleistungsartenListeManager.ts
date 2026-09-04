import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "@ui/ui/manager/AuswahlManager";

export class TeilleistungsartenListeManager extends AuswahlManager<number, Teilleistungsart, Teilleistungsart> {

	/**
     * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
     */
	private static readonly _TeilleistungsartenToId = (ba: Teilleistungsart) => ba.id;
	private readonly _idsOfReferencedTeilleistungsarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Teilleistungsarten in Teilleistungsartlisten.
	 */
	public static readonly comparator: Comparator<Teilleistungsart> = { compare: (a: Teilleistungsart, b: Teilleistungsart) => {
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
	 * @param idSchuljahresabschnitt       der Schuljahresabschnitt, auf den sich die Teilleistungsart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param Teilleistungsarten     	           die Liste der Teilleistungsarten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, Teilleistungsarten: List<Teilleistungsart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, Teilleistungsarten, TeilleistungsartenListeManager.comparator,
			TeilleistungsartenListeManager._TeilleistungsartenToId, TeilleistungsartenListeManager._TeilleistungsartenToId, []);
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	protected compareAuswahl(a: Teilleistungsart, b: Teilleistungsart): number {
		return TeilleistungsartenListeManager.comparator.compare(a, b);
	}

	get idsOfReferencedTeilleistungsarten(): HashSet<number> {
		return this._idsOfReferencedTeilleistungsarten;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedTeilleistungsarten.clear();
		for (const ba of this.liste.auswahl()) {
			if (ba.referenziertInAnderenTabellen) {
				this._idsOfReferencedTeilleistungsarten.add(ba.id);
			}
		}
	}

	protected checkFilter(eintrag: Teilleistungsart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this.searchTerm !== "") {
			// if searchTerm is defined filter for matching entries
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	private entryMatchesSearchterm(eintrag: Teilleistungsart) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung === null ? false : eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

}
