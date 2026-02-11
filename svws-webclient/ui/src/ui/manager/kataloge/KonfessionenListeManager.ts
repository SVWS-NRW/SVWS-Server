import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { ReligionEintrag } from "../../../../../core/src/core/data/schule/ReligionEintrag";
import { JavaLong } from "../../../../../core/src/java/lang/JavaLong";
import { JavaString } from "../../../../../core/src/java/lang/JavaString";
import { Arrays } from "../../../../../core/src/java/util/Arrays";
import { HashSet } from "../../../../../core/src/java/util/HashSet";
import type { Comparator } from "../../../../../core/src/java/util/Comparator";
import type { JavaFunction } from "../../../../../core/src/java/util/function/JavaFunction";
import type { List } from "../../../../../core/src/java/util/List";
import { AuswahlManager } from "../../AuswahlManager";


export class KonfessionenListeManager extends AuswahlManager<number, ReligionEintrag, ReligionEintrag> {

	private static readonly _konfessionToId: JavaFunction<ReligionEintrag, number> = { apply: (r: ReligionEintrag) => r.id };
	private readonly _idsOfReferencedKonfessionen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Religion-Einträgen.
	 */
	public static readonly comparator: Comparator<ReligionEintrag> = { compare: (a: ReligionEintrag, b: ReligionEintrag) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0) {
			return cmp;
		}
		if (a.kuerzel !== null && b.kuerzel !== null) {
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			if (cmp !== 0) {
				return cmp;
			}
		}
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp !== 0) {
			return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param konfessionen                 die Liste der Katalog-Einträge
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, konfessionen: List<ReligionEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, konfessionen, KonfessionenListeManager.comparator,
			KonfessionenListeManager._konfessionToId, KonfessionenListeManager._konfessionToId, Arrays.asList());
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}


	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected compareAuswahl(a: ReligionEintrag, b: ReligionEintrag): number {
		return KonfessionenListeManager.comparator.compare(a, b);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedKonfessionen.clear();
		for (const b of this.liste.auswahl()) {
			if (b.referenziertInAnderenTabellen) {
				this._idsOfReferencedKonfessionen.add(b.id);
			}
		}
	}

	protected checkFilter(eintrag: ReligionEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}
		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: ReligionEintrag) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower)
			|| ((eintrag.kuerzel !== null) && eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower)));
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get idsReferencedKonfessionen(): HashSet<number> {
		return this._idsOfReferencedKonfessionen;
	}
}
