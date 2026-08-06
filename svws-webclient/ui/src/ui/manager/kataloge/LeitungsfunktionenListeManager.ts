import type { Leitungsfunktion } from "../../../../../core/src/core/data/schule/Leitungsfunktion";
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import type { List } from '../../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { AuswahlManager } from "../AuswahlManager";

export class LeitungsfunktionenListeManager extends AuswahlManager<number, Leitungsfunktion, Leitungsfunktion> {
	private static readonly _leitungsfunktionToId = (lf: Leitungsfunktion) => lf.id;
	private readonly _idsReferencedLeitungsfunktionen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Leitungsfunktionen.
	 */
	public static readonly comparator: Comparator<Leitungsfunktion> = {
		compare: (a: Leitungsfunktion, b: Leitungsfunktion) => {
			let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
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
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param leitungsfunktionen				die Liste der Leitungsfunktionen
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, leitungsfunktionen: List<Leitungsfunktion>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, leitungsfunktionen, LeitungsfunktionenListeManager.comparator, LeitungsfunktionenListeManager._leitungsfunktionToId, LeitungsfunktionenListeManager._leitungsfunktionToId, []);
	}

	protected compareAuswahl(a: Leitungsfunktion, b: Leitungsfunktion): number {
		return LeitungsfunktionenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Leitungsfunktion): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this._searchTerm !== "") {
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	private entryMatchesSearchterm(eintrag: Leitungsfunktion): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedLeitungsfunktionen.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedLeitungsfunktionen.add(l.id);
			}
		}
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

	get idsReferencedLeitungsfunktionen(): JavaSet<number> {
		return this._idsReferencedLeitungsfunktionen;
	}
}
