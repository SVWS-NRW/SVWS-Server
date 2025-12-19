import { AuswahlManager } from '../../AuswahlManager';
import type { Fahrschuelerart } from '../../../../../core/src/core/data/schule/Fahrschuelerart';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { HashSet, type JavaSet } from "../../../../../core/src";

export class FahrschuelerartenListeManager extends AuswahlManager<number, Fahrschuelerart, Fahrschuelerart> {

	private static readonly _fahrschuelerartToId: JavaFunction<Fahrschuelerart, number> = { apply: (m: Fahrschuelerart) => m.id };
	private readonly _idsReferencedFahrschuelerarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Fahrschülerarten.
	 */
	public static readonly comparator: Comparator<Fahrschuelerart> = { compare: (a: Fahrschuelerart, b: Fahrschuelerart) => {
		let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null)) {
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0) {
				return cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param abschnitte           			  die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param fahrschuelerarten		          die Liste der Fahrschülerarten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, abschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, fahrschuelerarten: List<Fahrschuelerart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, abschnitte, schulform, fahrschuelerarten, FahrschuelerartenListeManager.comparator,
			FahrschuelerartenListeManager._fahrschuelerartToId, FahrschuelerartenListeManager._fahrschuelerartToId, ArrayList.of());
	}

	protected compareAuswahl(a: Fahrschuelerart, b: Fahrschuelerart): number {
		return FahrschuelerartenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Fahrschuelerart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Fahrschuelerart): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		return ((eintrag.bezeichnung !== null) && eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedFahrschuelerarten.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedFahrschuelerarten.add(l.id);
			}
		}
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get idsReferencedFahrschuelerarten(): JavaSet<number> {
		return this._idsReferencedFahrschuelerarten;
	}
}

