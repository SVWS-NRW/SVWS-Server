import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Einwilligungsart } from "@core/core/data/schule/Einwilligungsart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class EinwilligungsartenListeManager extends AuswahlManager<number, Einwilligungsart, Einwilligungsart> {

	private static readonly _einwilligungsArtToId = (ea: Einwilligungsart) => ea.id;
	private readonly idsReferencedEinwilligungsarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator: Comparator<Einwilligungsart> = { compare: (a: Einwilligungsart, b: Einwilligungsart) => {
		let cmp: number;
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
	 * @param idSchuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte       		die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule  	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     					die Schulform der Schule
	 * @param einwilligungsarten     			die Liste der Einwilligungsarten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, einwilligungsarten: List<Einwilligungsart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, einwilligungsarten, EinwilligungsartenListeManager.comparator,
			EinwilligungsartenListeManager._einwilligungsArtToId, EinwilligungsartenListeManager._einwilligungsArtToId, []);
	}

	/**
	 *Gibt das Set mit den Ids der Einwilligungsarten zurück, die in der Auswahl sind und in anderen Datenbanktabellen referenziert werden
	 *
	 * @return Das Set mit IDs von Einwilligungsarten, die in anderen Datenbanktabellen referenziert werden
	 */
	public getIdsReferencedEinwilligungsarten(): JavaSet<number> {
		return this.idsReferencedEinwilligungsarten;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsReferencedEinwilligungsarten.clear();
		for (const l of this.liste.auswahl()) {
			if ((l.referenziertInAnderenTabellen !== null) && l.referenziertInAnderenTabellen) {
				this.idsReferencedEinwilligungsarten.add(l.id);
			}
		}
	}

	protected compareAuswahl(a: Einwilligungsart, b: Einwilligungsart): number {
		return EinwilligungsartenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Einwilligungsart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return true;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Einwilligungsarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Einwilligungsarten zurück.
	 *
	 * @return true, wenn nur sichtbare Einwilligungsarten angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

}

