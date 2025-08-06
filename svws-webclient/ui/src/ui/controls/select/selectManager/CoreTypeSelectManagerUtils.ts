import { toRaw } from "vue";
import type { CoreTypeData } from "../../../../../../core/src/asd/data/CoreTypeData";
import type { CoreType } from "../../../../../../core/src/asd/types/CoreType";
import type { CoreTypeDataManager } from "../../../../../../core/src/asd/utils/CoreTypeDataManager";
import { Schulform } from "../../../../../../core/src/asd/types/schule/Schulform";
import type { List } from "../../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";

export const CoreTypeSelectUtils = {

	/**
	 * Berechnet die konkreten Einträge des CoreTypes basierend auf dem CoreTypeDataManager, dem Schuljahr und den Schulformen. Die Einträge bilden die
	 * Optionen des Selects ab.
	 *
	 * @param manager       der CoreTypeDataManager, der die CoreType-Daten enthält
	 * @param schuljahr     das Schuljahr der Einträge
	 * @param schulformen   die Schulformen der Einträge
	 *
	 * @returns Liste aller Einträge. Bei Schuljahr oder Manager = null wird eine leere Liste zurückgegeben.
	 */
	getEintraege<T extends CoreTypeData, U extends CoreType<T, U>>(manager: CoreTypeDataManager<T, U> | null, schuljahr: number | null,
		schulformen: Schulform | Iterable<Schulform> | null): Iterable<T> {
		if (manager === null || schuljahr === null)
			return new ArrayList<T>();
		let werte: List<U>;
		if (schulformen === null) {
			werte = manager.getWerteBySchuljahr(schuljahr);
		} else if (schulformen instanceof Schulform) {
			werte = manager.getListBySchuljahrAndSchulform(schuljahr, toRaw(schulformen));
		} else {
			const result = new ArrayList<U>();
			for (const schulform of schulformen) {
				const list = manager.getListBySchuljahrAndSchulform(schuljahr, schulform);
				for (const item of list)
					if (!result.contains(item))
						result.add(item);
			}
			werte = result;
		}
		const eintraege = new ArrayList<T>();
		for (const coreType of werte)
			eintraege.add(manager.getEintragBySchuljahrUndWert(schuljahr, coreType));
		return eintraege;
	},

	/**
	 * Generiert den Anzeigetext von selektierten Elementen. Dieser kann eine Standardanzeige sein ("kuerzel" | "text" | "kuerzelText") oder eine selbst
	 * definierte Anzeige.
	 *
	 * @param option								 die seletierte Option, deren Text berechnet werden soll.
	 * @param selectionDisplayText   die Art, wie die Texte angezeigt werden sollen.
	 * @returns den Anzeigetext der Selektion
	 */
	getSelectionText<T extends CoreTypeData, U extends CoreType<T, U>>(option: T | null,
		selectionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string)): string {
		if (option === null)
			return "";
		if (typeof selectionDisplayText === "function")
			return selectionDisplayText(option);
		if (selectionDisplayText === "kuerzel")
			return option.kuerzel;
		if (selectionDisplayText === "text")
			return option.text;
		return `${option.kuerzel} - ${option.text}`;
	},

	/**
	 * Generiert den Anzeigetext von Optionen. Dieser kann eine Standardanzeige sein ("kuerzel" | "text" | "kuerzelText") oder eine selbst
	 * definierte Anzeige.
	 *
	 * @param option								 die Option, deren Text berechnet werden soll.
	 * @param selectionDisplayText   die Art, wie die Texte angezeigt werden sollen.
	 * @returns den Anzeigetext der Option
	 */
	getOptionText<T extends CoreTypeData, U extends CoreType<T, U>>(option: T | null,
		optionDisplayText: "kuerzel" | "text" | "kuerzelText" | ((option: T) => string)): string {
		if (option === null)
			return "";
		if (typeof optionDisplayText === "function")
			return optionDisplayText(option);
		if (optionDisplayText === "kuerzel")
			return option.kuerzel;
		if (optionDisplayText === "text")
			return option.text;
		return `${option.kuerzel} - ${option.text}`;
	},
}
