import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { SchuelerVermerkartZusammenfassung } from "@core/core/data/schueler/SchuelerVermerkartZusammenfassung";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";


export class VermerkartenListeManager extends AuswahlManager<number, VermerkartEintrag, VermerkartEintrag> {

	private static readonly _vermerkartToId = (v: VermerkartEintrag) => v.id;
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
			VermerkartenListeManager._vermerkartToId, VermerkartenListeManager._vermerkartToId, []);
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
		this._eventHandlerFilterChanged();
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
		this._eventHandlerFilterChanged();
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

