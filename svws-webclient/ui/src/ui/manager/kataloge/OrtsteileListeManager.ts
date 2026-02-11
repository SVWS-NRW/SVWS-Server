import { AuswahlManager } from "../../AuswahlManager";
import type { OrtsteilKatalogEintrag } from '../../../../../core/src/core/data/kataloge/OrtsteilKatalogEintrag';
import type { OrtKatalogEintrag } from '../../../../../core/src/core/data/kataloge/OrtKatalogEintrag';
import type { List } from '../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { HashSet } from "../../../../../core/src/java/util/HashSet";


export class OrtsteileListeManager extends AuswahlManager<number, OrtsteilKatalogEintrag, OrtsteilKatalogEintrag> {

	private static readonly _ortsteileToId: JavaFunction<OrtsteilKatalogEintrag, number> = { apply: (a: OrtsteilKatalogEintrag) => a.id };
	private readonly _idsOfReferencedOrtsteile: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private readonly _orteById: Map<number, OrtKatalogEintrag> = new Map();

	/**
	 * Ein Default-Comparator für den Vergleich von Ortsteilen.
	 */
	public static readonly comparator: Comparator<OrtsteilKatalogEintrag> = { compare: (a: OrtsteilKatalogEintrag, b: OrtsteilKatalogEintrag) => {
		let cmp;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		if ((a.ortsteil !== null) && (b.ortsteil !== null)) {
			cmp = JavaString.compareTo(a.ortsteil, b.ortsteil);
			if (cmp !== 0) {
				return cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param ortsteile							die Liste der Ortsteile
	 * @param orte								die Liste der Orte
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, ortsteile: List<OrtsteilKatalogEintrag>, orte: List<OrtKatalogEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, ortsteile,
			OrtsteileListeManager.comparator, OrtsteileListeManager._ortsteileToId, OrtsteileListeManager._ortsteileToId, ArrayList.of());
		this.mapOrte(orte);
	}

	private mapOrte(orte: List<OrtKatalogEintrag>) {
		for (const ort of orte) {
			this._orteById.set(ort.id, ort);
		}
	}

	/**
	 * Vergleicht zwei Ortsteile Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: OrtsteilKatalogEintrag, b: OrtsteilKatalogEintrag): number {
		return OrtsteileListeManager.comparator.compare(a, b);
	}


	get idsOfReferencedOrtsteile(): HashSet<number> {
		return this._idsOfReferencedOrtsteile;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedOrtsteile.clear();
		for (const b of this.liste.auswahl()) {
			if (b.referenziertInAnderenTabellen) {
				this._idsOfReferencedOrtsteile.add(b.id);
			}
		}
	}

	protected checkFilter(eintrag: OrtsteilKatalogEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: OrtsteilKatalogEintrag) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (((eintrag.ortsteil !== null) && eintrag.ortsteil.toLocaleLowerCase().includes(searchTermLower))
				|| ((eintrag.bezeichnungOrt !== null) && eintrag.bezeichnungOrt.toLocaleLowerCase().includes(searchTermLower))
				|| ((eintrag.plzOrt !== null) && eintrag.plzOrt.includes(searchTermLower)));
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get orteById(): Map<number, OrtKatalogEintrag> {
		return this._orteById;
	}

}
