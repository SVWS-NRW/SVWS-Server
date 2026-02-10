import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { Floskelgruppe } from "../../../../../core/src/core/data/schule/Floskelgruppe";
import type { JavaFunction } from "../../../../../core/src/java/util/function/JavaFunction";
import type { List } from "../../../../../core/src/java/util/List";
import { AuswahlManager } from "../../AuswahlManager";
import { Arrays, HashSet, JavaLong } from "../../../../../core/src";


export class FloskelgruppenListeManager extends AuswahlManager<number, Floskelgruppe, Floskelgruppe> {

	private static readonly toId: JavaFunction<Floskelgruppe, number> = { apply: (f: Floskelgruppe) => f.id };
	private readonly _idsOfReferencedFloskelgruppen: HashSet<number> = new HashSet<number>();
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Floskelgruppen.
	 */
	public static readonly comparator = {
		compare: (a: Floskelgruppe, b: Floskelgruppe) => {
			const collator = new Intl.Collator(('de'));
			let cmp = collator.compare(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0) {
				return cmp;
			}

			cmp = collator.compare(a.kuerzel, b.kuerzel);
			if (cmp !== 0) {
				return cmp;
			}

			return JavaLong.compare(a.id, b.id);
		},
	};


	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, values: List<Floskelgruppe>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, values, FloskelgruppenListeManager.comparator,
			FloskelgruppenListeManager.toId, FloskelgruppenListeManager.toId, Arrays.asList());
	}

	protected compareAuswahl(a: Floskelgruppe, b: Floskelgruppe): number {
		return FloskelgruppenListeManager.comparator.compare(a, b);
	}

	get idsOfReferencedFloskelgruppen(): HashSet<number> {
		return this._idsOfReferencedFloskelgruppen;
	}

	protected onMehrfachauswahlChanged() {
		this._idsOfReferencedFloskelgruppen.clear();
		for (const fg of this.liste.auswahl()) {
			if (fg.referenziertInAnderenTabellen) {
				this._idsOfReferencedFloskelgruppen.add(fg.id);
			}
		}
	}

	protected checkFilter(eintrag: Floskelgruppe): boolean {
		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Floskelgruppe) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower)
				|| eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower);
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

}
