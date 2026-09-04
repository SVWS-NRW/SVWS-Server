import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class SchwerpunkteListeManager extends AuswahlManager<number, SchuelerSchwerpunkt, SchuelerSchwerpunkt> {

	/**
     * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
     */
	private static readonly _schwerpunkteToId = (ba: SchuelerSchwerpunkt) => ba.id;
	private readonly _idsOfReferencedSchwerpunkte: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Schwerpunkte in Schwerpunktlisten.
	 */
	public static readonly comparator: Comparator<SchuelerSchwerpunkt> = { compare: (a: SchuelerSchwerpunkt, b: SchuelerSchwerpunkt) => {
		let cmp;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
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
	 * @param idSchuljahresabschnitt       der Schuljahresabschnitt, auf den sich die Schwerpunkt bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param schwerpunkte     	           die Liste der Schwerpunkte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, schwerpunkte: List<SchuelerSchwerpunkt>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, schwerpunkte, SchwerpunkteListeManager.comparator,
			SchwerpunkteListeManager._schwerpunkteToId, SchwerpunkteListeManager._schwerpunkteToId, []);
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	protected compareAuswahl(a: SchuelerSchwerpunkt, b: SchuelerSchwerpunkt): number {
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

	protected checkFilter(eintrag: SchuelerSchwerpunkt): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this.searchTerm !== "") {
			// if searchTerm is defined filter for matching entries
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	private entryMatchesSearchterm(eintrag: SchuelerSchwerpunkt) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

}
