import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Einwilligungsart } from '../../../../../core/src/core/data/schule/Einwilligungsart';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";

export class EinwilligungsartenListeManager extends AuswahlManager<number, Einwilligungsart, Einwilligungsart> {

	private static readonly _einwilligungsArtToId: JavaFunction<Einwilligungsart, number> = { apply: (ea: Einwilligungsart) => ea.id };
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
			EinwilligungsartenListeManager._einwilligungsArtToId, EinwilligungsartenListeManager._einwilligungsArtToId, ArrayList.of());
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
		this._eventHandlerFilterChanged.run();
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

