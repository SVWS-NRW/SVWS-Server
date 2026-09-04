import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";


export class OrteListeManager extends AuswahlManager<number, OrtKatalogEintrag, OrtKatalogEintrag> {

	private static readonly _ortToId = (ea: OrtKatalogEintrag) => ea.id;
	private readonly _idsReferencedOrte: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator: Comparator<OrtKatalogEintrag> = {
		compare: (a: OrtKatalogEintrag, b: OrtKatalogEintrag) => {
			let cmp: number;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			if ((a.ortsname !== null) && (b.ortsname !== null)) {
				cmp = JavaString.compareTo(a.ortsname, b.ortsname);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		},
	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte       		die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule  	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     					die Schulform der Schule
	 * @param orte     							die Liste der Orte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, orte: List<OrtKatalogEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, orte, OrteListeManager.comparator,
			OrteListeManager._ortToId, OrteListeManager._ortToId, []);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedOrte.clear();
		for (const o of this.liste.auswahl()) {
			if (o.referenziertInAnderenTabellen === true) {
				this._idsReferencedOrte.add(o.id);
			}
		}
	}

	protected compareAuswahl(a: OrtKatalogEintrag, b: OrtKatalogEintrag): number {
		return OrteListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: OrtKatalogEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: OrtKatalogEintrag) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (((eintrag.ortsname !== null) && eintrag.ortsname.toLocaleLowerCase().includes(searchTermLower))
				|| ((eintrag.plz !== null) && eintrag.plz.toLocaleLowerCase().includes(searchTermLower)));
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get idsReferencedOrte(): HashSet<number> {
		return this._idsReferencedOrte;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}
}
