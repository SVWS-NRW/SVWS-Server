import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Erzieherart } from '../../../../../core/src/core/data/erzieher/Erzieherart';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';

export class ErzieherartListeManager extends AuswahlManager<number, Erzieherart, Erzieherart> {

	private static readonly _erzieherartenToId: JavaFunction<Erzieherart, number> = { apply: (ea: Erzieherart) => ea.id };
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
			ErzieherartListeManager.comparator, ErzieherartListeManager._erzieherartenToId, ErzieherartListeManager._erzieherartenToId, Arrays.asList());
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
		this._eventHandlerFilterChanged.run();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get idsReferencedErzieherarten(): HashSet<number> {
		return this._idsReferencedErzieherarten;
	}

}
