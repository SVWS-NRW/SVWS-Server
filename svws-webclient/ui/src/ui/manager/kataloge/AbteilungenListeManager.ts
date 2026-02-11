import type { KlassenDaten } from '../../../../../core/src/asd/data/klassen/KlassenDaten';
import type { Abteilung } from '../../../../../core/src/core/data/schule/Abteilung';
import { HashMap } from '../../../../../core/src/java/util/HashMap';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { LehrerListeEintrag } from '../../../../../core/src/core/data/lehrer/LehrerListeEintrag';
import type { AbteilungKlassenzuordnung } from '../../../../../core/src/core/data/schule/AbteilungKlassenzuordnung';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { JavaMap } from '../../../../../core/src/java/util/JavaMap';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class AbteilungenListeManager extends AuswahlManager<number, Abteilung, Abteilung> {

	private static readonly _abteilungToId: JavaFunction<Abteilung, number> = { apply: (a: Abteilung) => a.id };
	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private readonly _lehrerById: JavaMap<number, LehrerListeEintrag>;
	private readonly _klassenById: JavaMap<number, KlassenDaten>;

	/**
	 * Ein Default-Comparator für den Vergleich von Abteilungen.
	 */
	public static readonly comparator: Comparator<Abteilung> = { compare: (a: Abteilung, b: Abteilung) => {
		let cmp: number = JavaInteger.compare(a.sortierung, b.sortierung);
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
	 * @param idSchuljahresabschnittAuswahl    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param abteilungen     				  die Liste der Abteilungen
	 * @param lehrer     					  die Liste der Lehrer
	 * @param klassen						  die Liste der Klassen
	 */
	public constructor(idSchuljahresabschnittAuswahl: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, abteilungen: List<Abteilung>, lehrer: List<LehrerListeEintrag>, klassen: List<KlassenDaten>) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungen, AbteilungenListeManager.comparator, AbteilungenListeManager._abteilungToId, AbteilungenListeManager._abteilungToId, Arrays.asList());
		this._lehrerById = AbteilungenListeManager.mapLehrer(lehrer);
		this._klassenById = AbteilungenListeManager.mapKlassen(klassen);
	}

	/**
	 * Ein Getter der Klassen für die aktuelle Auswahl
	 *
	 * @return klassen
	 */
	public getKlassenByAuswahl(): List<KlassenDaten> {
		const result: List<KlassenDaten> | null = new ArrayList<KlassenDaten>();
		if ((this._daten === null) || (this._daten.klassenzuordnungen.isEmpty())) {
			return result;
		}
		for (const a of this._daten.klassenzuordnungen) {
			const klasse: KlassenDaten | null = this._klassenById.get(a.idKlasse);
			if (klasse !== null) {
				result.add(klasse);
			}
		}
		return result;
	}

	/**
	 * Fügt die Liste der AbteilungsKlassenzuordnungen der ausgewählten Abteilung hinzu
	 *
	 * @param zuordnungen    Liste der AbteilungsKlassenzuordnungen
	 */
	public addKlassenToAuswahl(zuordnungen: List<AbteilungKlassenzuordnung>): void {
		if (this._daten !== null) {
			this._daten.klassenzuordnungen.addAll(zuordnungen);
			this._daten.klassenzuordnungen.sort(this.comparatorKlassenzuordnung);
		}
	}

	/**
	 * Löscht Klassenzuordnungen anhand der IDs
	 *
	 * @param ids    Ids der Klassenzuordnungen
	 */
	public deleteKlassenzuordnungen(ids: List<number>): void {
		if (this._daten === null) {
			return;
		}
		for (const id of ids) {
			let toBeDeleted: AbteilungKlassenzuordnung | null = null;
			for (const v of this._daten.klassenzuordnungen) {
				if (v.id === id) {
					toBeDeleted = v;
					break;
				}
			}
			if (toBeDeleted !== null) {
				this._daten.klassenzuordnungen.remove(toBeDeleted);
			}
		}
	}

	private readonly comparatorKlassenzuordnung: Comparator<AbteilungKlassenzuordnung> = { compare: (a: AbteilungKlassenzuordnung, b: AbteilungKlassenzuordnung) => {
		const firstClass: KlassenDaten | null = this._klassenById.get(a.idKlasse);
		const secondClass: KlassenDaten | null = this._klassenById.get(b.idKlasse);
		if ((firstClass === null) || (firstClass.kuerzel === null) || (secondClass === null) || (secondClass.kuerzel === null)) {
			return 0;
		}
		return JavaString.compareTo(firstClass.kuerzel, secondClass.kuerzel);
	} };

	private static mapLehrer(lehrerListe: List<LehrerListeEintrag>): JavaMap<number, LehrerListeEintrag> {
		const result: JavaMap<number, LehrerListeEintrag> | null = new HashMap<number, LehrerListeEintrag>();
		for (const v of lehrerListe) {
			result.put(v.id, v);
		}
		return result;
	}

	private static mapKlassen(klassen: List<KlassenDaten>): JavaMap<number, KlassenDaten> {
		const result: JavaMap<number, KlassenDaten> | null = new HashMap<number, KlassenDaten>();
		for (const v of klassen) {
			result.put(v.id, v);
		}
		return result;
	}

	protected checkFilter(eintrag: Abteilung): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}

		return this.entryMatchesSearchterm(eintrag);
	}

	private entryMatchesSearchterm(eintrag: Abteilung) {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	protected compareAuswahl(a: Abteilung, b: Abteilung): number {
		return AbteilungenListeManager.comparator.compare(a, b);
	}

	get lehrerById(): JavaMap<number, LehrerListeEintrag> {
		return this._lehrerById;
	}

	get klassenById(): JavaMap<number, KlassenDaten> {
		return this._klassenById;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

}

