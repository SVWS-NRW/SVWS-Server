import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class JahrgaengeListeManager extends AuswahlManager<number, JahrgangsDaten, JahrgangsDaten> {

	private static readonly _jahrgangToId = (j: JahrgangsDaten) => j.id;
	private readonly idsReferencedJahrgaenge: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Jahrgängen in Jahrgangslisten.
	 */
	public static readonly comparator: Comparator<JahrgangsDaten> = { compare: (a: JahrgangsDaten, b: JahrgangsDaten) => {
		let cmp: number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		if (a.kuerzel !== null && b.kuerzel !== null) {
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			if (cmp !== 0) {
				return cmp;
			}
		}
		if (a.bezeichnung !== null && b.bezeichnung !== null) {
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
	 * @param idSchuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param jahrgaenge       die Liste der Jahrgänge
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, jahrgaenge: List<JahrgangsDaten>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, jahrgaenge, JahrgaengeListeManager.comparator,
			JahrgaengeListeManager._jahrgangToId, JahrgaengeListeManager._jahrgangToId, []);
	}

	/**
	 * Vergleicht zwei JahrgangsDaten Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: JahrgangsDaten, b: JahrgangsDaten): number {
		return JahrgaengeListeManager.comparator.compare(a, b);
	}
	/**
	 *Gibt das Set mit den Ids der Jahrgänge zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Jahrgängen, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedJahrgaenge(): JavaSet<number> {
		return this.idsReferencedJahrgaenge;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferencedJahrgaenge.clear();
		for (const l of this.liste.auswahl()) {
			if ((l.referenziertInAnderenTabellen !== null) && l.referenziertInAnderenTabellen) {
				this.idsReferencedJahrgaenge.add(l.id);
			}
		}
	}

	protected checkFilter(eintrag: JahrgangsDaten): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: JahrgangsDaten) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		if ((eintrag.kuerzel !== null) && eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower)) {
			return true;
		}
		return ((eintrag.bezeichnung !== null) && eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Jahrgänge.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Jahrgänge zurück.
	 *
	 * @return true, wenn nur sichtbare Jahrgänge angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}
}

