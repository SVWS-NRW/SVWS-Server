import { isRef, shallowRef, toRaw, toValue, triggerRef, watch, type MaybeRef, type ShallowRef } from "vue";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";
import type { SelectFilter } from "./SelectFilter";
import type { Fachgruppe, FachKatalogEintrag } from "../../../../../../core/src";

/**
 * Ein Filter für die UiSelect-Komponente. Er filtert Fächer auf Basis der übergebenen Fachgruppen.
 */
export class FachSelectFilter implements SelectFilter<FachKatalogEintrag> {

	public key: string;

	protected _fachgruppen: ShallowRef<Set<string>>;

	/**
	 * Konstruktor für den FachSelectFilter
	 *
	 * @param key   	    eindeutiger Key für den Filter.
	 * @param fachgruppen   Set der Fachgruppennamen, nach denen gefiltert wird.
	 */
	constructor(key: string, fachgruppen: MaybeRef<Iterable<Fachgruppe>>) {
		this.key = key;
		this._fachgruppen = (isRef(fachgruppen)) ? shallowRef(new Set([...toRaw(fachgruppen.value)].map(fg => fg.name())))
			: shallowRef(new Set([...toRaw(fachgruppen)].map(fg => fg.name())));

		watch(
			() => fachgruppen,
			(newFachgruppen) => {
				this._fachgruppen.value = new Set([...toValue(newFachgruppen)].map(fg => fg.name()));
			}, { deep: true }
		);
	}

	/**
	 * Wendet den Filter auf die übergebenen Optionen an. Falls nach mehreren Fachgruppen gefiltert wird, werden diese per union kombiniert.
	 *
	 * @param options
	 *
	 * @returns Liste der gefilterten Optionen
	 */
	apply(options: List<FachKatalogEintrag>): List<FachKatalogEintrag> {
		if (this._fachgruppen.value.size === 0)
			return options;

		const filteredOptions: List<FachKatalogEintrag> = new ArrayList<FachKatalogEintrag>();
		for (const option of options) {
			if (option.fachgruppe !== null && this._fachgruppen.value.has(option.fachgruppe))
				filteredOptions.add(option);
		}
		return filteredOptions;
	}
}
