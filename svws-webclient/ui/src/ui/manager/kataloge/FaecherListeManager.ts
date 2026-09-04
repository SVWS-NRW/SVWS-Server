import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class FaecherListeManager extends AuswahlManager<number, FachDaten, FachDaten> {

	private static readonly _faecherToId = (ea: FachDaten) => ea.id;
	private readonly _idsReferencedFaecher: HashSet<number> = new HashSet<number>();
	private readonly _stundenplaeneById: Map<number, StundenplanListeEintrag> = new Map();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator: Comparator<FachDaten> = {
		compare: (a: FachDaten, b: FachDaten) => {
			let cmp: number;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
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
	 * @param idSchuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte       		die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule  	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     					die Schulform der Schule
	 * @param faecher     						die Liste der Fächer
	 * @param stundenplaene						die Liste der Stundenpläne
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, faecher: List<FachDaten>, stundenplaene: List<StundenplanListeEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, faecher, FaecherListeManager.comparator,
			FaecherListeManager._faecherToId, FaecherListeManager._faecherToId, []);
		this.mapStundenplaene(stundenplaene);
	}

	private mapStundenplaene(stundenplaene: List<StundenplanListeEintrag>) {
		for (const stundenplan of stundenplaene) {
			this._stundenplaeneById.set(stundenplan.id, stundenplan);
		}
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedFaecher.clear();
		for (const fach of this.liste.auswahl()) {
			if (fach.referenziertInAnderenTabellen) {
				this._idsReferencedFaecher.add(fach.id);
			}
		}
	}

	protected compareAuswahl(a: FachDaten, b: FachDaten): number {
		return FaecherListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: FachDaten): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: FachDaten) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower)
				|| eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get idsReferencedFaecher(): HashSet<number> {
		return this._idsReferencedFaecher;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get stundenplaeneById(): Map<number, StundenplanListeEintrag> {
		return this._stundenplaeneById;
	}
}
