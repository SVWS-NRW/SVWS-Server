import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { FoerderschwerpunktEintrag } from "@core/core/data/schule/FoerderschwerpunktEintrag";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class FoerderschwerpunkteListeManager extends AuswahlManager<number, FoerderschwerpunktEintrag, FoerderschwerpunktEintrag> {

	private static readonly _foerderschwerpunktToId = (a: FoerderschwerpunktEintrag) => a.id;

	private readonly idsReferencedFoerderschwerpunkte: HashSet<number> = new HashSet<number>();

	private _filterNurSichtbar: boolean = true;

	/**
	 * Ein Default-Comparator für den Vergleich von Förderschwerpunkten
	 */
	public static readonly comparator: Comparator<FoerderschwerpunktEintrag> = { compare: (a: FoerderschwerpunktEintrag, b: FoerderschwerpunktEintrag) => {
		let cmp: number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		if (cmp !== 0) {
			return cmp;
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
	 * @param foerderschwerpunkte     			  die Liste der Foerderschwerpunkte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, foerderschwerpunkte: List<FoerderschwerpunktEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, foerderschwerpunkte, FoerderschwerpunkteListeManager.comparator,
			FoerderschwerpunkteListeManager._foerderschwerpunktToId, FoerderschwerpunkteListeManager._foerderschwerpunktToId, []);
	}

	/**
	 *Gibt das Set mit den Ids der Förderschwerpunkten zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Förderschwerpunkten, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedFoerderschwerpunkte(): JavaSet<number> {
		return this.idsReferencedFoerderschwerpunkte;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferencedFoerderschwerpunkte.clear();
		for (const l of this.liste.auswahl()) {
			if ((l.referenziertInAnderenTabellen !== null) && l.referenziertInAnderenTabellen) {
				this.idsReferencedFoerderschwerpunkte.add(l.id);
			}
		}
	}

	protected checkFilter(eintrag: FoerderschwerpunktEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return true;
	}

	protected compareAuswahl(a: FoerderschwerpunktEintrag, b: FoerderschwerpunktEintrag): number {
		return FoerderschwerpunkteListeManager.comparator.compare(a, b);
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Fächer.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Fächer zurück.
	 *
	 * @return true, wenn nur sichtbare Fächer angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}
}
