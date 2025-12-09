import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { Floskelgruppe } from "../../../../../core/src/core/data/schule/Floskelgruppe";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { JavaFunction } from "../../../../../core/src/java/util/function/JavaFunction";
import type { List } from "../../../../../core/src/java/util/List";
import { AuswahlManager } from "../../AuswahlManager";


export class FloskelgruppenListeManager extends AuswahlManager<number, Floskelgruppe, Floskelgruppe> {

	private static readonly toId: JavaFunction<Floskelgruppe, number> = { apply: (f: Floskelgruppe) => f.id };

	/**
	 * Ein Default-Comparator für den Vergleich von Floskelgruppen.
	 */
	public static readonly comparator = {
		compare: (a: Floskelgruppe, b: Floskelgruppe) => {
			let cmp = a.bezeichnung?.localeCompare(b.bezeichnung ?? "") ?? 0;
			if (cmp !== 0)
				return cmp;

			cmp = a.kuerzel?.localeCompare(b.kuerzel ?? "") ?? 0;
			if (cmp !== 0)
				return cmp;

			return a.id - b.id;
		},
	};


	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, values: List<Floskelgruppe>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, values, FloskelgruppenListeManager.comparator,
			FloskelgruppenListeManager.toId, FloskelgruppenListeManager.toId, ArrayList.of());
	}

	protected checkFilter(): boolean {
		return true;
	}

	protected compareAuswahl(a: Floskelgruppe, b: Floskelgruppe): number {
		return FloskelgruppenListeManager.comparator.compare(a, b);
	}

}
