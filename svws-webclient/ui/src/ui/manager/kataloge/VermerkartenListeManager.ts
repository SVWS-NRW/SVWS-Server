import type { VermerkartEintrag } from '../../../../../core/src/core/data/schule/VermerkartEintrag';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { SchuelerVermerkartZusammenfassung } from '../../../../../core/src/core/data/schueler/SchuelerVermerkartZusammenfassung';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { JavaInteger } from "../../../../../core/src/java/lang/JavaInteger";



export class VermerkartenListeManager extends AuswahlManager<number, VermerkartEintrag, VermerkartEintrag> {

	private static readonly _vermerkartToId: JavaFunction<VermerkartEintrag, number> = { apply: (v: VermerkartEintrag) => v.id };
	private readonly _idsReferencedEinwilligungsarten: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private _schuelerVermerkartZusammenfassungen: List<SchuelerVermerkartZusammenfassung>;
	private static readonly comparator: Comparator<VermerkartEintrag> = { compare: (a: VermerkartEintrag, b: VermerkartEintrag) => {
		let cmp: number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0) {
			return cmp;
		}
		if ((a.bezeichnung !== null) && b.bezeichnung !== null) {
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
	 * @param idSchuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param vermerkarten     					die Liste der Vermerkarten
	 * @param schuelerVermerkartZusammenfassungen     	die Liste der SchuelerVermerkartZusammenfassung
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, vermerkarten: List<VermerkartEintrag>, schuelerVermerkartZusammenfassungen: List<SchuelerVermerkartZusammenfassung>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, vermerkarten, VermerkartenListeManager.comparator,
			VermerkartenListeManager._vermerkartToId, VermerkartenListeManager._vermerkartToId, Arrays.asList());
		this._schuelerVermerkartZusammenfassungen = schuelerVermerkartZusammenfassungen;
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedEinwilligungsarten.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedEinwilligungsarten.add(l.id);
			}
		}
	}

	protected compareAuswahl(a: VermerkartEintrag, b: VermerkartEintrag): number {
		return VermerkartenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: VermerkartEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Vermerkarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Vermerkarten zurück.
	 *
	 * @return true, wenn nur sichtbare Vermerkarten angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get schuelerVermerkartZusammenfassungen(): List<SchuelerVermerkartZusammenfassung> {
		return this._schuelerVermerkartZusammenfassungen;
	}

	set schuelerVermerkartZusammenfassungen(value: List<SchuelerVermerkartZusammenfassung>) {
		this._schuelerVermerkartZusammenfassungen = value;
	}

	get idsReferencedEinwilligungsarten(): HashSet<number> {
		return this._idsReferencedEinwilligungsarten;
	}

	private entryMatchesSearchterm(eintrag: VermerkartEintrag) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return ((eintrag.bezeichnung !== null) && eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

}

