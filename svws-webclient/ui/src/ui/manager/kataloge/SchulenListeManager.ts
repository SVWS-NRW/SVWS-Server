import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { SchulenKatalogEintrag } from '../../../../../core/src/core/data/schule/SchulenKatalogEintrag';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import type { SchulEintrag } from '../../../../../core/src/core/data/kataloge/SchulEintrag';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from "../../../../../core/src";

export class SchulenListeManager extends AuswahlManager<number, SchulEintrag, SchulEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _schuleToId: JavaFunction<SchulEintrag, number> = { apply: (schulEintrag: SchulEintrag) => schulEintrag.id };
	private readonly _idsReferencedSchulen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * die Liste der Schulen aus Gesamt-NRW
	 */
	private readonly _schulenKatalogEintraege: List<SchulenKatalogEintrag>;

	/**
	 * Ein Default-Comparator für den Vergleich von Schulen im Schule Katalog.
	 */
	public static readonly comparator: Comparator<SchulEintrag> = {
		compare: (a: SchulEintrag, b: SchulEintrag) => {
			let cmp: number = a.sortierung - b.sortierung;
			if (cmp !== 0) {
				return cmp;
			}
			if (a.kuerzel === null && b.kuerzel !== null) {
				return 1;
			}
			if (a.kuerzel !== null && b.kuerzel === null) {
				return -1;
			}
			if (a.kuerzel !== null && b.kuerzel !== null) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
				if (cmp !== 0) {
					return cmp;
				}
			}
			if (a.kurzbezeichnung !== null && b.kurzbezeichnung !== null) {
				cmp = JavaString.compareTo(a.kurzbezeichnung, b.kurzbezeichnung);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		} };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnitt			 der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param idSchuljahresabschnitte          die Liste der Schuljahresabschnitte
	 * @param idSchuljahresabschnittSchule     der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						 die Schulform der Schule
	 * @param schulen						 die Liste der Schulen
	 * @param schulenKatalogEintraege        die Liste der Schulen aus Gesamt-NRW
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, idSchuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, schulen: List<SchulEintrag>, schulenKatalogEintraege: List<SchulenKatalogEintrag>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, idSchuljahresabschnitte, schulform, schulen, SchulenListeManager.comparator,
			SchulenListeManager._schuleToId, SchulenListeManager._schuleToId, Arrays.asList());
		this._schulenKatalogEintraege = schulenKatalogEintraege;
	}

	protected compareAuswahl(a: SchulEintrag, b: SchulEintrag): number {
		return SchulenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: SchulEintrag): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}
		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: SchulEintrag): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		const searchableFields = [
			eintrag.schulnummerStatistik,
			eintrag.ort,
			eintrag.kurzbezeichnung,
			eintrag.kuerzel,
		];
		return searchableFields.some(field =>
			field?.toLowerCase().includes(searchTermLower) ?? false
		);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedSchulen.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedSchulen.add(l.id);
			}
		}
	}

	get schulenKatalogEintraege(): List<SchulenKatalogEintrag> {
		return this._schulenKatalogEintraege;
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

	get idsReferencedSchulen(): HashSet<number> {
		return this._idsReferencedSchulen;
	}
}
