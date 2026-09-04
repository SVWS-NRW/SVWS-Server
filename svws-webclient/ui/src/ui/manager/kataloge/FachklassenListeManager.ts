import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { FachklasseEintrag } from "@core/core/data/schule/FachklasseEintrag";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Collection } from "@core/java/util/Collection";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class FachklassenListeManager extends AuswahlManager<number, FachklasseEintrag, FachklasseEintrag> {

	private static readonly _fachklasseToId = (f: FachklasseEintrag) => f.id;
	private readonly _idsOfReferencedFachklassen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private _filterSchulgliederungen: SchulgliederungKatalogEintrag[] = [];

	/**
	 * Ein Default-Comparator für den Vergleich von Fachklassen.
	 */
	public static readonly comparator: Comparator<FachklasseEintrag> = {
		compare: (a: FachklasseEintrag, b: FachklasseEintrag) => {
			let cmp;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			if (a.kuerzel !== null && b.kuerzel !== null) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
				if (cmp !== 0) {
					return cmp;
				}
			}
			if (a.bezeichnung !== null && b.bezeichnung !== null) {
				cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		},
	};

	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, fachklassen: Collection<FachklasseEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, fachklassen, FachklassenListeManager.comparator,
			FachklassenListeManager._fachklasseToId, FachklassenListeManager._fachklasseToId, []);
	}

	protected checkFilter(eintrag: FachklasseEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this._filterSchulgliederungen.length > 0) {
			if ((eintrag.idSchulgliederung === null) || !this._filterSchulgliederungen.some(sg => sg.id === eintrag.idSchulgliederung)) {
				return false;
			}
		}

		if (this.searchTerm !== "") {
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedFachklassen.clear();
		for (const ba of this.liste.auswahl()) {
			if (ba.referenziertInAnderenTabellen) {
				this._idsOfReferencedFachklassen.add(ba.id);
			}
		}
	}

	private entryMatchesSearchterm(eintrag: FachklasseEintrag) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return ((eintrag.bezeichnung?.toLocaleLowerCase().includes(searchTermLower)) === true)
		|| ((eintrag.kuerzel?.toLocaleLowerCase().includes(searchTermLower)) === true);
	}

	protected compareAuswahl(a: FachklasseEintrag, b: FachklasseEintrag): number {
		return FachklassenListeManager.comparator.compare(a, b);
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get idsOfReferencedFachklassen(): HashSet<number> {
		return this._idsOfReferencedFachklassen;
	}

	get filterSchulgliederungen(): SchulgliederungKatalogEintrag[] {
		return this._filterSchulgliederungen;
	}
	set filterSchulgliederungen(value: SchulgliederungKatalogEintrag[]) {
		this._filterSchulgliederungen = value;
		this._eventHandlerFilterChanged();
	}

}
