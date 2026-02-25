import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { List } from '../../../../../core/src/java/util/List';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Ankreuzkompetenz } from '../../../../../core/src/core/data/schule/Ankreuzkompetenz';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import type { FachDaten } from '../../../../../core/src/core/data/fach/FachDaten';
import type { Schulgliederung } from "../../../../../core/src/asd/types/schule/Schulgliederung";
import type { JahrgangsDaten } from '../../../../../core/src/core/data/jahrgang/JahrgangsDaten';

export class AnkreuzkompetenzenListeManager extends AuswahlManager<number, Ankreuzkompetenz, Ankreuzkompetenz> {

	private static readonly _ankreuzkompetenzToId: JavaFunction<Ankreuzkompetenz, number> = { apply: (a: Ankreuzkompetenz) => a.id };
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
			ArrayList.of()
		);
		this.mapFaecher(faecher);
		this.mapJahrgaenge(jahrgaenge);
	}

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
			if ((eintrag.idFach === null) || !this._filterFaecher.some(fach => fach.id === eintrag.idFach)) {
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
			(eintrag.istASV === 1) ? "ASV" : null,
		];

		return searchableFields.some(field =>
			field?.toLowerCase().includes(searchTermLower) ?? false
		);
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
		this._eventHandlerFilterChanged.run();
	}

	get filterFaecher(): FachDaten[] {
		return this._filterFaecher;
	}

	set filterFaecher(value: FachDaten[]) {
		this._filterFaecher = value;
		this._eventHandlerFilterChanged.run();
	}

	get filterSchulgliederungen(): Schulgliederung[] {
		return this._filterSchulgliederungen;
	}
	set filterSchulgliederungen(value: Schulgliederung[]) {
		this._filterSchulgliederungen = value;
		this._eventHandlerFilterChanged.run();
	}

	get filterJahrgaenge(): JahrgangsDaten[] {
		return this._filterJahrgaenge;
	}

	set filterJahrgaenge(value: JahrgangsDaten[]) {
		this._filterJahrgaenge = value;
		this._eventHandlerFilterChanged.run();
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

}
