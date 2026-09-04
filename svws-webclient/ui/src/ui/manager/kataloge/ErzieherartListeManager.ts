import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class ErzieherartListeManager extends AuswahlManager<number, Erzieherart, Erzieherart> {

	private static readonly _erzieherartenToId = (ea: Erzieherart) => ea.id;
	private readonly _idsReferencedErzieherarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Erzieherarten in Erzieherartlisten.
	 */
	public static readonly comparator: Comparator<Erzieherart> = {
		compare: (a: Erzieherart, b: Erzieherart) => {
			let cmp: number;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0) {
				return cmp;
			}
			return JavaLong.compare(a.id, b.id);
		},
	};


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt        der Schuljahresabschnitt, auf den sich die Erzieherart bezieht
	 * @param schuljahresabschnitte        	die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule 	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    	die Schulform der Schule
	 * @param erzieherarten     	      	die Liste der Erzieherart
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, erzieherarten: List<Erzieherart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, erzieherarten,
			ErzieherartListeManager.comparator, ErzieherartListeManager._erzieherartenToId, ErzieherartListeManager._erzieherartenToId, []);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedErzieherarten.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedErzieherarten.add(l.id);
			}
		}
	}

	protected compareAuswahl(a: Erzieherart, b: Erzieherart): number {
		return ErzieherartListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Erzieherart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Erzieherart): boolean {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
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

	get idsReferencedErzieherarten(): HashSet<number> {
		return this._idsReferencedErzieherarten;
	}

}
