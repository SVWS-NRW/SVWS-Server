import type { KlasseDetails } from '../../../../../core/src/asd/data/klassen/KlasseDetails';
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
import { HashSet } from "../../../../../core/src/java/util/HashSet";
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { KlasseListItem } from "../../../../../core/src/asd/data/klassen/KlasseListItem";

export class AbteilungenListeManager extends AuswahlManager<number, Abteilung, Abteilung> {

	private static readonly _abteilungToId: JavaFunction<Abteilung, number> = { apply: (a: Abteilung) => a.id };

	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private _deleteAbteilungenInFolgeAbschnitt: boolean = true;

	private readonly _abteilungenFolgeAbschnittByBezeichnung: JavaMap<string, Abteilung> = new HashMap();
	private readonly _abteilungenFolgeAbschnittById: JavaMap<number, Abteilung> = new HashMap();
	private readonly _lehrerById: JavaMap<number, LehrerListeEintrag>;
	private readonly _klassenByIdAktAbschnitt: JavaMap<number, KlasseListItem>;
	private readonly _klassenByIdFolgeAbschnitt: JavaMap<number, KlasseListItem>;

	/**
	 * Ein Default-Comparator für den Vergleich von Abteilungen.
	 */
	public static readonly COMPARATOR_ABTEILUNGEN_DEFAULT: Comparator<Abteilung> = {
		compare: (a: Abteilung, b: Abteilung) => {
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

	private static readonly COMPARATOR_KLASSEN_BY_KUERZEL_ASC: Comparator<KlasseDetails> = {
		compare: (klasse1: KlasseDetails, klasse2: KlasseDetails) => {
			if (klasse1.kuerzel === null) {
				return 0;
			}
			return JavaString.compareTo(klasse1.kuerzel, klasse2.kuerzel);
		},
	};

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnittAuswahl   der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste aller Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param abteilungenAktAbschnitt     	  die Liste der Abteilungen im aktuellen Schuljahresabschnitt
	 * @param lehrer     					  die Liste der Lehrer
	 * @param klassenAktAbschnitt			  die Liste der Klassen des aktuellen Schuljahresabschnittes
	 * @param klassenFolgeAbschnitt			  die Liste der Klassen des folgenden Schuljahresabschnittes
	 */
	public constructor(idSchuljahresabschnittAuswahl: number, idSchuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, abteilungenAktAbschnitt: List<Abteilung>, abteilungenFolgeAbschnitt: List<Abteilung>, lehrer: List<LehrerListeEintrag>,
		klassenAktAbschnitt: List<KlasseListItem>, klassenFolgeAbschnitt: List<KlasseListItem>) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungenAktAbschnitt,
			AbteilungenListeManager.COMPARATOR_ABTEILUNGEN_DEFAULT, AbteilungenListeManager._abteilungToId, AbteilungenListeManager._abteilungToId, Arrays.asList());
		this.mapAbteilungenFolgeAbschnitt(abteilungenFolgeAbschnitt);
		this._lehrerById = this.mapLehrer(lehrer);
		this._klassenByIdAktAbschnitt = this.mapKlassen(klassenAktAbschnitt);
		this._klassenByIdFolgeAbschnitt = this.mapKlassen(klassenFolgeAbschnitt);
	}

	protected checkFilter(eintrag: Abteilung): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar) {
			return false;
		}
		return this.entryMatchesSearchterm(eintrag);
	}

	protected compareAuswahl(a: Abteilung, b: Abteilung): number {
		return AbteilungenListeManager.COMPARATOR_ABTEILUNGEN_DEFAULT.compare(a, b);
	}

	public getKlassenByAuswahl(): List<KlasseListItem> {
		const result: List<KlasseListItem> | null = new ArrayList<KlasseListItem>();
		if ((this._daten === null) || (this._daten.klassenzuordnungen.isEmpty())) {
			return result;
		}
		for (const a of this._daten.klassenzuordnungen) {
			const klasse: KlasseListItem | null = this._klassenByIdAktAbschnitt.get(a.idKlasse);
			if (klasse !== null) {
				result.add(klasse);
			}
		}
		result.sort(AbteilungenListeManager.COMPARATOR_KLASSEN_BY_KUERZEL_ASC);
		return result;
	}

	public addKlassenzuordnungen(klassenzuordnungen: List<AbteilungKlassenzuordnung>): void {
		if (this._daten !== null) {
			this._daten.klassenzuordnungen.addAll(klassenzuordnungen);
		}
	}

	public deleteKlassenzuordnungen(klassenzuordnungen: List<AbteilungKlassenzuordnung>): void {
		if (this._daten === null) {
			return;
		}

		for (const klassenzuordnungToDelete of klassenzuordnungen) {
			for (const zuordnung of this._daten.klassenzuordnungen) {
				if (zuordnung.id === klassenzuordnungToDelete.id) {
					this._daten.klassenzuordnungen.remove(zuordnung);
					break;
				}
			}
		}
	}

	public getAbteilungFolgeAbschnitt(abteilungAktAbschnitt: Abteilung): Abteilung | null {
		return this._abteilungenFolgeAbschnittByBezeichnung.get(abteilungAktAbschnitt.bezeichnung);
	}

	public getKlassenzuordnungenIdsFolgeAbschnitt(abteilungFolgeAbschnitt: Abteilung, klassenzuordnungenAktAbschnitt: List<AbteilungKlassenzuordnung>): List<number> {
		const idsKlassenAktAbschnitt = Arrays.asList([...klassenzuordnungenAktAbschnitt].map(zuordnung => zuordnung.idKlasse));
		const idsKlassenFolgeAbschnitt = this.getKlassenIdsFuerFolgeAbschnitt(idsKlassenAktAbschnitt);

		const idsKlassenzuordnungenFolgeAbschnitt = new ArrayList<number>();
		for (const zuordnungFolgeAbschnitt of abteilungFolgeAbschnitt.klassenzuordnungen) {
			for (const idKlasseFolgeAbschnitt of idsKlassenFolgeAbschnitt) {
				if (idKlasseFolgeAbschnitt === zuordnungFolgeAbschnitt.idKlasse) {
					idsKlassenzuordnungenFolgeAbschnitt.add(zuordnungFolgeAbschnitt.id);
				}
			}
		}

		return idsKlassenzuordnungenFolgeAbschnitt;
	}

	public addAbteilungFolgeAbschnitt(abteilung: Abteilung): void {
		this._abteilungenFolgeAbschnittById.put(abteilung.id, abteilung);
		if (this._abteilungenFolgeAbschnittByBezeichnung.containsKey(abteilung.bezeichnung)) {
			this._abteilungenFolgeAbschnittByBezeichnung.remove(abteilung.bezeichnung);
		} else {
			this._abteilungenFolgeAbschnittByBezeichnung.put(abteilung.bezeichnung, abteilung);
		}
	}

	public getAvailableKlassenToAdd(): List<KlasseListItem> {
		const alleKlassen = [...this._klassenByIdAktAbschnitt.values()];
		const alreadyAdded = new Set(this.getKlassenByAuswahl());
		return Arrays.asList(alleKlassen.filter(v => !alreadyAdded.has(v)));
	}

	public getKlassenIdsFuerFolgeAbschnitt(klassenIds: List<number>): List<number> {
		const assignedKlassenIdsFolgeAbschnitt = new ArrayList<number>();
		for (const idKlasse of klassenIds) {
			const idKlasseFolgeAbschnitt = this.getKlassenIdFuerFolgeAbschnitt(idKlasse);
			if (idKlasseFolgeAbschnitt !== null) {
				assignedKlassenIdsFolgeAbschnitt.add(idKlasseFolgeAbschnitt);
			}
		}
		return assignedKlassenIdsFolgeAbschnitt;
	}

	private getKlassenIdFuerFolgeAbschnitt(idKlasse: number): number | null {
		const klasse = this._klassenByIdAktAbschnitt.get(idKlasse);
		if (klasse === null) {
			return null;
		}

		let idKlasseFolgeAbschnitt: number | null = null;
		for (const klasseFolgeAbschnitt of this._klassenByIdFolgeAbschnitt.values()) {
			if (this.klassenEquals(klasseFolgeAbschnitt, klasse)) {
				if (idKlasseFolgeAbschnitt !== null) {
					return null;
				}
				idKlasseFolgeAbschnitt = klasseFolgeAbschnitt.id;
			}
		}
		return idKlasseFolgeAbschnitt;
	}

	private mapLehrer(lehrerListe: List<LehrerListeEintrag>): JavaMap<number, LehrerListeEintrag> {
		const result: JavaMap<number, LehrerListeEintrag> | null = new HashMap<number, LehrerListeEintrag>();
		for (const v of lehrerListe) {
			result.put(v.id, v);
		}
		return result;
	}

	private mapKlassen(klassen: List<KlasseListItem>): JavaMap<number, KlasseListItem> {
		const result: JavaMap<number, KlasseListItem> | null = new HashMap<number, KlasseListItem>();
		for (const v of klassen) {
			result.put(v.id, v);
		}
		return result;
	}

	private klassenEquals(klasse1: KlasseListItem, klasse2: KlasseListItem): boolean {
		return (klasse1.kuerzel === klasse2.kuerzel)
			&& (klasse1.idJahrgang === klasse2.idJahrgang)
			&& (klasse1.parallelitaet === klasse2.parallelitaet);
	}

	private entryMatchesSearchterm(eintrag: Abteilung): boolean {
		const searchTermLower = this._searchTerm.toLocaleLowerCase();
		return (eintrag.bezeichnung.toLocaleLowerCase().includes(searchTermLower));
	}

	private mapAbteilungenFolgeAbschnitt(abteilungenFolgeAbschnitt: List<Abteilung>): void {
		this._abteilungenFolgeAbschnittByBezeichnung.clear();
		this._abteilungenFolgeAbschnittById.clear();

		const abteilungenToSkipByBezeichnung = new HashSet<string>();
		for (const abteilung of abteilungenFolgeAbschnitt) {
			this._abteilungenFolgeAbschnittById.put(abteilung.id, abteilung);
			if (this._abteilungenFolgeAbschnittByBezeichnung.containsKey(abteilung.bezeichnung) || abteilungenToSkipByBezeichnung.contains(abteilung.bezeichnung)) {
				this._abteilungenFolgeAbschnittByBezeichnung.remove(abteilung.bezeichnung);
				abteilungenToSkipByBezeichnung.add(abteilung.bezeichnung);
			} else {
				this._abteilungenFolgeAbschnittByBezeichnung.put(abteilung.bezeichnung, abteilung);
			}
		}
	}

	get lehrerById(): JavaMap<number, LehrerListeEintrag> {
		return this._lehrerById;
	}

	get klassenByIdAktAbschnitt(): JavaMap<number, KlasseListItem> {
		return this._klassenByIdAktAbschnitt;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged.run();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	get abteilungenFolgeAbschnittByBezeichnung(): JavaMap<string, Abteilung> {
		return this._abteilungenFolgeAbschnittByBezeichnung;
	}

	get abteilungenFolgeAbschnittById(): JavaMap<number, Abteilung> {
		return this._abteilungenFolgeAbschnittById;
	}

	get deleteAbteilungenInFolgeAbschnitt(): boolean {
		return this._deleteAbteilungenInFolgeAbschnitt;
	}

	set deleteAbteilungenInFolgeAbschnitt(value: boolean) {
		this._deleteAbteilungenInFolgeAbschnitt = value;
	}
}

