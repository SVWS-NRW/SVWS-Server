import type { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

/**
 * Gibt die für die Auswahl relevanten Schulgliederungen sortiert zurück. Ist keine Schulform bekannt, so
 * werden alle Schulgliederungen geliefert.
 *
 * @param schulform   die Schulform der Schule oder null
 * @param schuljahr   das Schuljahr, für welches die Schulgliederungen ermittelt werden
 *
 * @returns die Liste der Schulgliederungen in der Reihenfolge ihrer Definition
 */
export function getSchulgliederungen(schulform: Schulform | null, schuljahr: number): List<Schulgliederung> {
	const gliederungen = (schulform === null) ? Schulgliederung.values() : Schulgliederung.getBySchuljahrAndSchulform(schuljahr, schulform);
	const result = new ArrayList<Schulgliederung>();
	for (const gliederung of gliederungen) {
		result.add(gliederung);
	}
	result.sort({ compare: (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() });
	return result;
}
