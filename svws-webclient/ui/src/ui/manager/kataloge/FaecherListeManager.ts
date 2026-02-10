import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { FachDaten } from '../../../../../core/src/core/data/fach/FachDaten';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { StundenplanListeEintrag } from "../../../../../core/src/core/data/stundenplan/StundenplanListeEintrag";


export class FaecherListeManager extends AuswahlManager<number, FachDaten, FachDaten> {

	private static readonly _faecherToId: JavaFunction<FachDaten, number> = { apply: (ea: FachDaten) => ea.id };
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
			FaecherListeManager._faecherToId, FaecherListeManager._faecherToId, ArrayList.of());
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
		this._eventHandlerFilterChanged.run();
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
		this._eventHandlerFilterChanged.run();
	}

	get stundenplaeneById(): Map<number, StundenplanListeEintrag> {
		return this._stundenplaeneById;
	}
}
