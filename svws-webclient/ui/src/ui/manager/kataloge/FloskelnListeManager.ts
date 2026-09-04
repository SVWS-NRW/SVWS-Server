import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { Floskel } from "@core/core/data/schule/Floskel";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class FloskelnListeManager extends AuswahlManager<number, Floskel, Floskel> {

	private static readonly toId = (f: Floskel) => f.id;
	private static readonly _NIVEAUS: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
	private readonly _floskelgruppenById: Map<number, Floskelgruppe> = new Map();
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten> = new Map();
	private readonly _faecherById: Map<number, FachDaten> = new Map<number, FachDaten>();
	private _searchTerm: string = "";
	private _filterJahrgaenge: JahrgangsDaten[] = [];
	private _filterFloskelgruppen: Floskelgruppe[] = [];
	private _filterFaecher: FachDaten[] = [];
	private _filterNiveaus: number[] = [];

	public static readonly comparator = {
		compare: (a: Floskel, b: Floskel) => {
			let cmp;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
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
	 * @param floskeln							die Liste der Floskeln
	 * @param floskelgruppen					die Liste der Floskelgruppen
	 * @param jahrgaenge						die Liste der Jahrgänge
	 * @param faecher							die Liste der Fächer
	 */
	public constructor(
		idSchuljahresabschnitt: number,
		idSchuljahresabschnittSchule: number,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		jahrgaenge: List<JahrgangsDaten>,
		schulform: Schulform | null,
		floskeln: List<Floskel>,
		floskelgruppen: List<Floskelgruppe>,
		faecher: List<FachDaten>
	) {
		super(
			idSchuljahresabschnitt,
			idSchuljahresabschnittSchule,
			schuljahresabschnitte,
			schulform,
			floskeln,
			FloskelnListeManager.comparator,
			FloskelnListeManager.toId,
			FloskelnListeManager.toId,
			[]
		);
		this.mapFloskelgruppen(floskelgruppen);
		this.mapJahrgaenge(jahrgaenge);
		this.mapFaecher(faecher);
		this._filterPermitAuswahl = false;
	}

	private mapFloskelgruppen(floskelgruppen: List<Floskelgruppe>) {
		for (const f of floskelgruppen) {
			this._floskelgruppenById.set(f.id, f);
		}
	}

	private mapJahrgaenge(jahrgaenge: List<JahrgangsDaten>) {
		for (const jg of jahrgaenge) {
			this._jahrgaengeById.set(jg.id, jg);
		}
	}

	private mapFaecher(faecher: List<FachDaten>) {
		for (const f of faecher) {
			this._faecherById.set(f.id, f);
		}
	}

	protected compareAuswahl(a: Floskel, b: Floskel): number {
		return FloskelnListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Floskel): boolean {
		// Jahrgangfilter: wenn gesetzt, prüfe ob Floskel den Jahrgang enthält.
		if (this._filterJahrgaenge.length > 0) {
			const ids = eintrag.idsJahrgaenge;
			if (!ids || ids.isEmpty()) {
				return false;
			}
			if (!this._filterJahrgaenge.some(jg => [...ids].some(id => id === jg.id))) {
				return false;
			}
		}

		// Floskelgruppenfilter: wenn gesetzt, prüfe ob Floskel zur Gruppe gehört.
		if (this._filterFloskelgruppen.length > 0) {
			const id: number | null = eintrag.idFloskelgruppe;
			if (id === null) {
				return false;
			}
			if (!this._filterFloskelgruppen.some(fg => fg.id === id)) {
				return false;
			}
		}

		// Niveaufilter: wenn gesetzt, prüfe ob Floskel das Niveau hat.
		if (this._filterNiveaus.length > 0) {
			if ((eintrag.niveau === null) || !this._filterNiveaus.includes(eintrag.niveau)) {
				return false;
			}
		}

		// Faecherfilter: wenn gesetzt, prüfe ob Floskel das Fach hat.
		if (this._filterFaecher.length > 0) {
			if ((eintrag.idFach === null) || !this._filterFaecher.some(fach => fach.id === eintrag.idFach)) {
				return false;
			}
		}

		return this.entryMatchesSearchTerm(eintrag);
	}

	private entryMatchesSearchTerm(eintrag: Floskel) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		if (eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower)) {
			return true;
		}
		return (eintrag.text.toLocaleLowerCase().includes(searchTermLower));
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get filterJahrgaenge(): JahrgangsDaten[] {
		return this._filterJahrgaenge;
	}

	set filterJahrgaenge(value: JahrgangsDaten[]) {
		this._filterJahrgaenge = value;
		this._eventHandlerFilterChanged();
	}

	get filterFloskelgruppen(): Floskelgruppe[] {
		return this._filterFloskelgruppen;
	}

	set filterFloskelgruppen(value: Floskelgruppe[]) {
		this._filterFloskelgruppen = value;
		this._eventHandlerFilterChanged();
	}

	get filterFaecher(): FachDaten[] {
		return this._filterFaecher;
	}

	set filterFaecher(value: FachDaten[]) {
		this._filterFaecher = value;
		this._eventHandlerFilterChanged();
	}

	get filterNiveaus(): number[] {
		return this._filterNiveaus;
	}

	set filterNiveaus(value: number[]) {
		this._filterNiveaus = value;
		this._eventHandlerFilterChanged();
	}

	get niveaus(): number[] {
		return FloskelnListeManager._NIVEAUS;
	}

	get floskelgruppenById(): Map<number, Floskelgruppe> {
		return this._floskelgruppenById;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}
}
