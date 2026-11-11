import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";
import { StateManager } from "@ui/ui/StateManager";

/** Der reaktive State des Katalogs */
interface AuswahlKatalogReactiveState<T> {
	items: List<T>;
	byId: Map<number, T>;
}

/**
 * Ein Katalog mit Einträgen, die für die Auswahl benötigt werden (z.B. die Jahrgänge eines Schuljahresabschnittes).
 * Der Katalog hält die Einträge sortiert und ermöglicht den Zugriff über deren ID.
 *
 * @typeParam T   der Typ der Einträge des Katalogs
 */
export class AuswahlKatalog<T> extends StateManager<AuswahlKatalogReactiveState<T>> {

	/** Die Funktion, welche die ID zu einem Eintrag liefert */
	private readonly _getId: (item: T) => number;

	/** Der Comparator, mit dem die Einträge sortiert werden */
	private readonly _comparator: Comparator<T>;

	/**
	 * Erzeugt einen neuen, leeren Katalog.
	 *
	 * @param getId        die Funktion, welche die ID zu einem Eintrag liefert
	 * @param comparator   der Comparator, mit dem die Einträge sortiert werden
	 */
	public constructor(getId: (item: T) => number, comparator: Comparator<T>) {
		super({ items: new ArrayList<T>(), byId: new Map<number, T>() });
		this._getId = getId;
		this._comparator = comparator;
	}

	/**
	 * Ersetzt die Einträge des Katalogs. Die übergebenen Einträge werden dabei nicht verändert.
	 *
	 * @param items   die neuen Einträge
	 */
	public set(items: Iterable<T>): void {
		const sorted = new ArrayList<T>();
		for (const item of items) {
			sorted.add(item);
		}
		sorted.sort(this._comparator);
		const byId = new Map<number, T>();
		for (const item of sorted) {
			byId.set(this._getId(item), item);
		}
		this.setPatchedState({ items: sorted, byId });
	}

	/**
	 * Gibt die sortierten Einträge des Katalogs zurück.
	 *
	 * @returns die Liste der Einträge
	 */
	public list(): List<T> {
		return this._state.value.items;
	}

	/**
	 * Gibt den Eintrag zu der ID zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag oder null, falls die ID unbekannt ist
	 */
	public get(id: number): T | null {
		return this._state.value.byId.get(id) ?? null;
	}

	/**
	 * Gibt zurück, ob der Katalog einen Eintrag mit der ID enthält.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns true, wenn der Eintrag vorhanden ist, und ansonsten false
	 */
	public has(id: number): boolean {
		return this._state.value.byId.has(id);
	}

	/**
	 * Leert den Katalog.
	 */
	public reset(): void {
		this.setDefaultState();
	}

}
