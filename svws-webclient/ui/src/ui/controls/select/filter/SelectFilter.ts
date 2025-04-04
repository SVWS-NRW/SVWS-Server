import type { List } from "../../../../../../core/src/java/util/List";

/**
 * Interface für Filter, die in der UiSelect Komponente verwendet werden.
 */
export interface SelectFilter<T> {
	// Eindeutiger Key zur Identifizierung eines Filters im SelectManager
	key: string;
	/**
	 * Wendet den Filter auf die übergebene Liste an Optionen an.
	 *
	 * @param options   die Liste an Optionen, die gefiltert werden soll
	 */
	apply(options: List<T>): List<T>;
}