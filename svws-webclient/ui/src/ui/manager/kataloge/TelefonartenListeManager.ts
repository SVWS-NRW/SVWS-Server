import type { Telefonart } from '../../../../../core/src/core/data/schule/Telefonart';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';

export class TelefonartenListeManager extends AuswahlManager<number, Telefonart, Telefonart> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _telefonArtenToId: JavaFunction<Telefonart, number> = { apply: (ta: Telefonart) => ta.id };
	private readonly _idsOfReferencedTelefonarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	/**
	 * Sets der Ids der Telefonarten, die von Personen verwendet und daher nicht gelöscht werden können.
	 */

	/**
	 * Ein Default-Comparator für den Vergleich von Telefonarten in Telefonartlisten.
	 */
	public static readonly comparator: Comparator<Telefonart> = { compare: (a: Telefonart, b: Telefonart) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0) {
			return cmp;
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
	 * @param idSchuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Telefonart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param telefonarten     	       die Liste der Telefonart
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, telefonarten: List<Telefonart>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, telefonarten, TelefonartenListeManager.comparator,
			TelefonartenListeManager._telefonArtenToId, TelefonartenListeManager._telefonArtenToId, Arrays.asList());
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected compareAuswahl(a: Telefonart, b: Telefonart): number {
		return TelefonartenListeManager.comparator.compare(a, b);
	}

	get idsOfReferencedTelefonarten(): HashSet<number> {
		return this._idsOfReferencedTelefonarten;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsOfReferencedTelefonarten.clear();
		for (const t of this.liste.auswahl()) {
			if (t.referenziertInAnderenTabellen) {
				this._idsOfReferencedTelefonarten.add(t.id);
			}
		}
	}

	protected checkFilter(eintrag: Telefonart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}
		return this.entryMathcesSearchterm(eintrag);
	}

	private entryMathcesSearchterm(eintrag: Telefonart) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}
}
