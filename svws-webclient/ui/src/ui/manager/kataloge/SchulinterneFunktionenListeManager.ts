import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { SchulinterneFunktion } from "@core/core/data/schule/SchulinterneFunktion";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";

import { AuswahlManager } from "../AuswahlManager";

export class SchulinterneFunktionenListeManager extends AuswahlManager<number, SchulinterneFunktion, SchulinterneFunktion> {
	private static readonly _schulinterneFunktionToId = (lf: SchulinterneFunktion) => lf.id;
	private readonly _idsReferencedSchulinterneFunktionen: HashSet<number> = new HashSet<number>();
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von schulinternen Funktionen.
	 */
	public static readonly comparator: Comparator<SchulinterneFunktion> = {
		compare: (a: SchulinterneFunktion, b: SchulinterneFunktion) => {
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
	 * @param schulinterneFunktionen				die Liste der schulinternen Funktionen
	 */
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, schulinterneFunktionen: List<SchulinterneFunktion>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, schulinterneFunktionen, SchulinterneFunktionenListeManager.comparator, SchulinterneFunktionenListeManager._schulinterneFunktionToId, SchulinterneFunktionenListeManager._schulinterneFunktionToId, []);
	}

	protected compareAuswahl(a: SchulinterneFunktion, b: SchulinterneFunktion): number {
		return SchulinterneFunktionenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: SchulinterneFunktion): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		if (this._searchTerm !== "") {
			return this.entryMatchesSearchterm(eintrag);
		}

		return true;
	}

	private entryMatchesSearchterm(eintrag: SchulinterneFunktion): boolean {
		const searchTermLower = this._searchTerm.toLowerCase();
		return eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedSchulinterneFunktionen.clear();
		for (const l of this.liste.auswahl()) {
			if (l.referenziertInAnderenTabellen) {
				this._idsReferencedSchulinterneFunktionen.add(l.id);
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

	get idsReferencedSchulinterneFunktionen(): JavaSet<number> {
		return this._idsReferencedSchulinterneFunktionen;
	}
}
