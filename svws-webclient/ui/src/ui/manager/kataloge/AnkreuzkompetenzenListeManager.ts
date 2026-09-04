import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '@core/asd/types/schule/Schulform';
import type { Schulgliederung } from '@core/asd/types/schule/Schulgliederung';
import type { FachDaten } from '@core/core/data/fach/FachDaten';
import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
import type { Ankreuzkompetenz } from '@core/core/data/schule/Ankreuzkompetenz';
import type { AnkreuzkompetenzJahrgangszuordnung } from '@core/core/data/schule/AnkreuzkompetenzJahrgangszuordnung';
import { JavaInteger } from '@core/java/lang/JavaInteger';
import { JavaLong } from '@core/java/lang/JavaLong';
import { JavaString } from '@core/java/lang/JavaString';
import { ArrayList } from '@core/java/util/ArrayList';
import type { Comparator } from '@core/java/util/Comparator';
import { HashMap } from '@core/java/util/HashMap';
import { HashSet } from '@core/java/util/HashSet';
import type { List } from '@core/java/util/List';
import { AuswahlManager } from '../AuswahlManager';

export class AnkreuzkompetenzenListeManager extends AuswahlManager<number, Ankreuzkompetenz, Ankreuzkompetenz> {

	private static readonly _ankreuzkompetenzToId = (a: Ankreuzkompetenz) => a.id;
	private readonly _idsReferencedAnkreuzkompetenzen: HashSet<number> = new HashSet<number>();
	private readonly _faecherById: Map<number, FachDaten> = new Map();
	private _filterNurSichtbar: boolean = true;
	private _filterFaecher: FachDaten[] = [];
	private _filterSchulgliederungen: Schulgliederung[] = [];
	private _filterJahrgaenge: JahrgangsDaten[] = [];
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten> = new Map();
	private _searchTerm: string = "";

	/**
	 * Ein Default-Comparator für den Vergleich von Ankreuzkompetenzen.
	 */
	public static readonly comparator: Comparator<Ankreuzkompetenz> = {
		compare: (a: Ankreuzkompetenz, b: Ankreuzkompetenz) => {
			let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
			if (cmp !== 0) {
				return cmp;
			}
			cmp = JavaString.compareTo(a.floskelText, b.floskelText);
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
	 * @param ankreuzkompetenzen				die Liste der Ankreuzkompetenzen
	 * @param faecher							die Liste der Fächer
	 * @param jahrgaenge						die Liste der jahrgaenge
	 */
	public constructor(
		idSchuljahresabschnitt: number,
		idSchuljahresabschnittSchule: number,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null,
		ankreuzkompetenzen: List<Ankreuzkompetenz>,
		faecher: List<FachDaten>,
		jahrgaenge: List<JahrgangsDaten>
	) {
		super(
			idSchuljahresabschnitt,
			idSchuljahresabschnittSchule,
			schuljahresabschnitte,
			schulform,
			ankreuzkompetenzen,
			AnkreuzkompetenzenListeManager.comparator,
			AnkreuzkompetenzenListeManager._ankreuzkompetenzToId,
			AnkreuzkompetenzenListeManager._ankreuzkompetenzToId,
			[]
		);
		this.mapFaecher(faecher);
		this.mapJahrgaenge(jahrgaenge);
	}

	/**
	 * Ein Getter der Jahrgänge für die aktuelle Auswahl
	 *
	 * @return jahrgaenge
	 */
	public getJahrgaengeByAuswahl(): List<JahrgangsDaten> {
		const result: List<JahrgangsDaten> | null = new ArrayList<JahrgangsDaten>();
		if ((this._daten === null) || (this._daten.jahrgaengezuordnung.isEmpty())) {
			return result;
		}

		for (const jahrgangzuordnung of this._daten.jahrgaengezuordnung) {
			const jahrgang: JahrgangsDaten | undefined = this._jahrgaengeById.get(jahrgangzuordnung.idJahrgang);
			if (jahrgang !== undefined) {
				result.add(jahrgang);
			}
		}
		return result;
	}

	/**
	 * Liefert alle Jahrgänge, die der aktuellen Auswahl nicht zugeordnet sind.
	 */
	public getAddableJahrgaenge(): JahrgangsDaten[] {
		const alreadyAdded = new Set(this.getJahrgaengeByAuswahl());
		return [...this._jahrgaengeById.values()].filter(v => !alreadyAdded.has(v));
	}

	/**
	 * Gibt eine Map von idJahrgang auf AnkreuzkompetenzJahrgangszuordnung für die aktuelle Auswahl zurück.
	 */
	public getJahrgaengezuordnungenByIdJahrgang(): HashMap<number, AnkreuzkompetenzJahrgangszuordnung> {
		const result = new HashMap<number, AnkreuzkompetenzJahrgangszuordnung>();
		if (this._daten === null) {
			return result;
		}
		for (const jahrgangzuordnung of this._daten.jahrgaengezuordnung) {
			result.put(jahrgangzuordnung.idJahrgang, jahrgangzuordnung);
		}
		return result;
	}

	/**
	 * Fügt die Liste der AnkreuzkompetnezenJahrgaengezuordnungen der ausgewählten Ankreuzkompetenz hinzu
	 *
	 * @param zuordnungen    Liste der AnkreuzkompetnezenJahrgaengezuordnungen
	 */
	public addJahrgaengezuordnungen(zuordnungen: List<AnkreuzkompetenzJahrgangszuordnung>): void {
		if (this._daten !== null) {
			this._daten.jahrgaengezuordnung.addAll(zuordnungen);
			this._daten.jahrgaengezuordnung.sort(this.comparatorJahrgaengezuordnung);
		}
	}

	/**
	 * Löscht Jahrgängezuordnungen anhand der IDs
	 *
	 * @param ids    Ids der Jahrgängezuordnungen
	 */
	public deleteJahrgaengezuordnungen(ids: List<number>): void {
		if (this._daten === null) {
			return;
		}

		const zuordnungenToRemove = new ArrayList<AnkreuzkompetenzJahrgangszuordnung>();
		for (const id of ids) {
			for (const jahrgangzuordnung of this._daten.jahrgaengezuordnung) {
				if (jahrgangzuordnung.id === id) {
					zuordnungenToRemove.add(jahrgangzuordnung);
				}
			}
		}

		this._daten.jahrgaengezuordnung.removeAll(zuordnungenToRemove);
	}

	private readonly comparatorJahrgaengezuordnung: Comparator<AnkreuzkompetenzJahrgangszuordnung> = {
		compare: (a: AnkreuzkompetenzJahrgangszuordnung, b: AnkreuzkompetenzJahrgangszuordnung) => {
			const jahrgang1: JahrgangsDaten | undefined = this._jahrgaengeById.get(a.idJahrgang);
			const jahrgang2: JahrgangsDaten | undefined = this._jahrgaengeById.get(b.idJahrgang);

			if ((jahrgang1 === undefined) || (jahrgang2 === undefined)) {
				return 0;
			}

			const kuerzel1 = jahrgang1.kuerzel;
			const kuerzel2 = jahrgang2.kuerzel;

			if ((kuerzel1 === null) && (kuerzel2 === null)) {
				return 0;
			}

			if (kuerzel1 === null) {
				return 1;
			}

			if (kuerzel2 === null) {
				return -1;
			}

			return JavaString.compareTo(kuerzel1, kuerzel2);
		},
	};

	private mapFaecher(faecher: List<FachDaten>) {
		for (const f of faecher) {
			this._faecherById.set(f.id, f);
		}
	}

	private mapJahrgaenge(jahrgaenge: List<JahrgangsDaten>) {
		for (const j of jahrgaenge) {
			this._jahrgaengeById.set(j.id, j);
		}
	}

	protected compareAuswahl(a: Ankreuzkompetenz, b: Ankreuzkompetenz): number {
		return AnkreuzkompetenzenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Ankreuzkompetenz): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		// Faecherfilter: wenn gesetzt, prüfe ob Ankreuzkompetenz das Fach hat.
		if (this._filterFaecher.length > 0) {
			const filterIncludesASV = this._filterFaecher.some(fach => fach.id === -1);
			const filterFaecherWithoutASV = this._filterFaecher.filter(fach => fach.id !== -1);

			const matchesASV = filterIncludesASV && eintrag.istASV;
			const matchesFach = (filterFaecherWithoutASV.length > 0)
			&& (eintrag.idFach !== null)
			&& filterFaecherWithoutASV.some(fach => fach.id === eintrag.idFach);

			if (!matchesASV && !matchesFach) {
				return false;
			}
		}

		// Schulgliederungsfilter
		if (this._filterSchulgliederungen.length > 0) {
			if ((eintrag.schulgliederung === null) || !this._filterSchulgliederungen.some(sg => sg.name() === eintrag.schulgliederung)) {
				return false;
			}
		}

		// Jahrgangsfilter
		if (this._filterJahrgaenge.length > 0) {
			const zuordnungen = [...eintrag.jahrgaengezuordnung];
			const hasMatchingJahrgang = this._filterJahrgaenge.some(jgFilter =>
				zuordnungen.some(z => z.idJahrgang === jgFilter.id)
			);

			if (!hasMatchingJahrgang) {
				return false;
			}
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Ankreuzkompetenz): boolean {
		const searchTermLower = this._searchTerm.toLowerCase().trim();

		const fach = (eintrag.idFach === null) ? null : this._faecherById.get(eintrag.idFach);

		const searchableFields = [
			eintrag.floskelText,
			fach?.kuerzel,
			fach?.bezeichnung,
			eintrag.istASV ? "ASV" : null,
		];

		return searchableFields.some(field =>
			field?.toLowerCase().includes(searchTermLower) ?? false
		);
	}

	protected onMehrfachauswahlChanged(): void {
		this._idsReferencedAnkreuzkompetenzen.clear();
		for (const a of this.liste.auswahl()) {
			if (a.referenziertInAnderenTabellen) {
				this._idsReferencedAnkreuzkompetenzen.add(a.id);
			}
		}
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
	}

	get filterFaecher(): FachDaten[] {
		return this._filterFaecher;
	}

	set filterFaecher(value: FachDaten[]) {
		this._filterFaecher = value;
		this._eventHandlerFilterChanged();
	}

	get filterSchulgliederungen(): Schulgliederung[] {
		return this._filterSchulgliederungen;
	}
	set filterSchulgliederungen(value: Schulgliederung[]) {
		this._filterSchulgliederungen = value;
		this._eventHandlerFilterChanged();
	}

	get filterJahrgaenge(): JahrgangsDaten[] {
		return this._filterJahrgaenge;
	}

	set filterJahrgaenge(value: JahrgangsDaten[]) {
		this._filterJahrgaenge = value;
		this._eventHandlerFilterChanged();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get idsReferencedAnkreuzkompetenzen(): HashSet<number> {
		return this._idsReferencedAnkreuzkompetenzen;
	}
}
