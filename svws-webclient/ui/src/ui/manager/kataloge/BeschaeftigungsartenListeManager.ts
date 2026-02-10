import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Beschaeftigungsart } from '../../../../../core/src/core/data/betrieb/Beschaeftigungsart';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { HashSet, type JavaSet } from "../../../../../core/src";

export class BeschaeftigungsartenListeManager extends AuswahlManager<number, Beschaeftigungsart, Beschaeftigungsart> {

	private static readonly _beschaeftigungsartToId: JavaFunction<Beschaeftigungsart, number> = { apply: (a: Beschaeftigungsart) => a.id };
	private readonly _idsReferencedBeschaeftigungsarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Beschäftigungsarten.
	 */
	public static readonly comparator: Comparator<Beschaeftigungsart> = {
		compare: (a: Beschaeftigungsart, b: Beschaeftigungsart) => {
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
		},
	};


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt    	  	der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           	die Liste der Schuljahresabschnitte
	 * @param schulform     				  	die Schulform der Schule
	 * @param beschaeftigungsarten				die Liste der Beschäftigungsarten
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, beschaeftigungsarten: List<Beschaeftigungsart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, beschaeftigungsarten, BeschaeftigungsartenListeManager.comparator, BeschaeftigungsartenListeManager._beschaeftigungsartToId, BeschaeftigungsartenListeManager._beschaeftigungsartToId, ArrayList.of());
	}

	protected compareAuswahl(a: Beschaeftigungsart, b: Beschaeftigungsart): number {
		return BeschaeftigungsartenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Beschaeftigungsart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Beschaeftigungsart): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		return eintrag.bezeichnung?.toLocaleLowerCase().includes(searchTermLower) ?? false;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedBeschaeftigungsarten.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedBeschaeftigungsarten.add(l.id);
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

	get idsReferencedBeschaeftigungsarten(): JavaSet<number> {
		return this._idsReferencedBeschaeftigungsarten;
	}
}
