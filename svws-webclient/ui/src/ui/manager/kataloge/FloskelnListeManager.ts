import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { Floskel } from "../../../../../core/src/core/data/schule/Floskel";
import type { Floskelgruppe } from "../../../../../core/src/core/data/schule/Floskelgruppe";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { JavaFunction } from "../../../../../core/src/java/util/function/JavaFunction";
import type { List } from "../../../../../core/src/java/util/List";
import { AuswahlManager } from "../../AuswahlManager";


export class FloskelnListeManager extends AuswahlManager<number, Floskel, Floskel> {

	private static readonly toId: JavaFunction<Floskel, number> = { apply: (f: Floskel) => f.id };

	private readonly floskelgruppenById: Map<number, Floskelgruppe>;

	private readonly floskelgruppen: List<Floskelgruppe>;

	/**
	 * Ein Default-Comparator für den Vergleich von Floskeln.
	 */
	public static readonly comparator = {
		compare: (a: Floskel, b: Floskel) => {
			const cmp = a.kuerzel?.localeCompare(b.kuerzel ?? "") ?? 0;
			if (cmp !== 0) {
				return cmp;
			}

			return a.id - b.id;
		},
	};

	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, values: List<Floskel>, floskelgruppen: List<Floskelgruppe>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, values, FloskelnListeManager.comparator,
			FloskelnListeManager.toId, FloskelnListeManager.toId, ArrayList.of());
		this.floskelgruppen = floskelgruppen;
		this.floskelgruppenById = this.mapFloskelgruppen(floskelgruppen);
	}

	private mapFloskelgruppen(floskelgruppen: List<Floskelgruppe>) {
		const result = new Map<number, Floskelgruppe>();
		for (const f of floskelgruppen) {
			result.set(f.id, f);
		}
		return result;
	}

	public getFloskelgruppen(): List<Floskelgruppe> {
		return this.floskelgruppen;
	}

	public getFloskelgruppenById() {
		return new Map(this.floskelgruppenById);
	}

	protected checkFilter(): boolean {
		return true;
	}

	protected compareAuswahl(a: Floskel, b: Floskel): number {
		return FloskelnListeManager.comparator.compare(a, b);
	}

}
