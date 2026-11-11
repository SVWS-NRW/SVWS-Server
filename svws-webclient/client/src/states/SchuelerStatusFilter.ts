import type { Schueler } from "@core/asd/data/schueler/Schueler";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { List } from "@core/java/util/List";
import { StateManager } from "@ui/ui/StateManager";

import { schuleStateImpl } from "./SchuleStateImpl";

/** Der reaktive State des Schüler-Status-Filters */
interface SchuelerStatusFilterReactiveState {
	values: List<SchuelerStatus>;
}

/**
 * Der Filter nach dem Status für die Schülerliste in den Detail-Ansichten (z.B. die Schüler einer Klasse oder
 * eines Kurses). Ist kein Status ausgewählt, so werden alle Schüler angezeigt.
 */
export class SchuelerStatusFilter extends StateManager<SchuelerStatusFilterReactiveState> {

	/** Die Status, die ausgewählt sind, wenn der Filter zurückgesetzt wird */
	private readonly _defaultValues: List<SchuelerStatus>;

	/**
	 * Erzeugt einen neuen Filter.
	 *
	 * @param defaultValues   die Status, die standardmäßig ausgewählt sind
	 */
	public constructor(defaultValues: SchuelerStatus[]) {
		const values = Arrays.asList(...defaultValues);
		super({ values });
		this._defaultValues = values;
	}

	/**
	 * Gibt die Status zurück, die für den Filter ausgewählt werden können.
	 *
	 * @returns die Status
	 */
	public get options(): List<SchuelerStatus> {
		return Arrays.asList(...SchuelerStatus.values());
	}

	/**
	 * Gibt die ausgewählten Status zurück.
	 *
	 * @returns die ausgewählten Status
	 */
	public get values(): List<SchuelerStatus> {
		return this._state.value.values;
	}

	/**
	 * Setzt die ausgewählten Status.
	 *
	 * @param values   die ausgewählten Status
	 */
	public setValues(values: Iterable<SchuelerStatus>): void {
		this.setPatchedState({ values: Arrays.asList(...values) });
	}

	/**
	 * Setzt die ausgewählten Status auf die Default-Werte zurück.
	 */
	public reset(): void {
		this.setPatchedState({ values: this._defaultValues });
	}

	/**
	 * Filtert die Schüler nach den ausgewählten Status.
	 *
	 * @param schueler   die zu filternden Schüler
	 *
	 * @returns die Schüler mit einem ausgewählten Status oder alle Schüler, wenn kein Status ausgewählt ist
	 */
	public filter(schueler: Iterable<Schueler>): List<Schueler> {
		const ids = new Set<number>();
		for (const status of this._state.value.values) {
			const eintrag = status.daten(schuleStateImpl.schuljahr);
			if (eintrag !== null) {
				ids.add(parseInt(eintrag.kuerzel, 10));
			}
		}
		const result = new ArrayList<Schueler>();
		for (const s of schueler) {
			if (ids.size === 0 || ids.has(s.status)) {
				result.add(s);
			}
		}
		return result;
	}

}
