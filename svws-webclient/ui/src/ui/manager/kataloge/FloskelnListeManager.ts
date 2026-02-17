import type { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
import type { Floskel } from "../../../../../core/src/core/data/schule/Floskel";
import type { Floskelgruppe } from "../../../../../core/src/core/data/schule/Floskelgruppe";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import type { JavaFunction } from "../../../../../core/src/java/util/function/JavaFunction";
import type { List } from "../../../../../core/src/java/util/List";
import { AuswahlManager } from "../../AuswahlManager";
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { JahrgangsDaten } from '../../../../../core/src/core/data/jahrgang/JahrgangsDaten';
import type { FachDaten } from '../../../../../core/src/core/data/fach/FachDaten';


export class FloskelnListeManager extends AuswahlManager<number, Floskel, Floskel> {

	private static readonly toId: JavaFunction<Floskel, number> = { apply: (f: Floskel) => f.id };
	private static readonly NIVEAUS: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

	public static readonly comparator = {
		compare: (a: Floskel, b: Floskel) => {
			let cmp;
			cmp = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			if ((a.kuerzel !== null) && (b.kuerzel !== null)) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		},
	};

	// --- Stammdaten ---
	private readonly _floskelgruppen: List<Floskelgruppe>;
	private readonly _floskelgruppenById: Map<number, Floskelgruppe> = new Map();
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten> = new Map();
	private readonly _faecherById: Map<number, FachDaten> = new Map<number, FachDaten>();

	// --- Filter-State ---
	private _searchTerm: string = "";
	private _filterNurSichtbar: boolean = true;
	private _filterJahrgaenge: JahrgangsDaten[] = [];
	private _filterFloskelgruppen: Floskelgruppe[] = [];
	private _filterFaecher: FachDaten[] = [];
	private _filterNiveaus: number[] = [];

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
	public constructor(idSchuljahresabschnitt: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		jahrgaenge: List<JahrgangsDaten>, schulform: Schulform | null, floskeln: List<Floskel>, floskelgruppen: List<Floskelgruppe>, faecher: List<FachDaten>) {
		super(idSchuljahresabschnitt, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, floskeln, FloskelnListeManager.comparator,
			FloskelnListeManager.toId, FloskelnListeManager.toId, ArrayList.of());
		this._floskelgruppen = floskelgruppen;
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
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		// Jahrgangfilter: wenn gesetzt, prüfe ob Floskel den Jahrgang enthält.
		if (this._filterJahrgaenge.length > 0) {
			const ids = eintrag.idsJahrgaenge;
			if (!ids || ids.isEmpty()) {
				return false;
			}
			if (!this._filterJahrgaenge.some(jg => [...ids].includes(jg.id))) {
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
		if ((eintrag.kuerzel !== null) && eintrag.kuerzel.toLocaleLowerCase().includes(searchTermLower)) {
			return true;
		}
		return ((eintrag.text !== null) && eintrag.text.toLocaleLowerCase().includes(searchTermLower));
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	public getJahrgaenge(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	public filterJahrgaenge(): JahrgangsDaten[] {
		return this._filterJahrgaenge;
	}

	public setFilterJahrgang(value: JahrgangsDaten[]) {
		this._filterJahrgaenge = value;
		this._eventHandlerFilterChanged.run();
	}

	public getFloskelgruppenById() {
		return this._floskelgruppenById;
	}

	public getFloskelgruppen(): List<Floskelgruppe> {
		return this._floskelgruppen;
	}

	public filterFloskelgruppe(): Floskelgruppe[] {
		return this._filterFloskelgruppen;
	}

	public setFilterFloskelgruppe(value: Floskelgruppe[]) {
		this._filterFloskelgruppen = value;
		this._eventHandlerFilterChanged.run();
	}

	public getFaecher(): Map<number, FachDaten> {
		return this._faecherById;
	}

	public filterFaecher(): FachDaten[] {
		return this._filterFaecher;
	}

	public setFilterFaecher(value: FachDaten[]) {
		this._filterFaecher = value;
		this._eventHandlerFilterChanged.run();
	}

	public get niveaus(): number[] {
		return FloskelnListeManager.NIVEAUS;
	}

	public filterNiveaus(): number[] {
		return this._filterNiveaus;
	}

	public setFilterNiveau(value: number[]) {
		this._filterNiveaus = value;
		this._eventHandlerFilterChanged.run();
	}

}
