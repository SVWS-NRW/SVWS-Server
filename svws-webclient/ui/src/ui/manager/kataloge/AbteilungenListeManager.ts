import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { Abteilung } from "@core/core/data/schule/Abteilung";
import type { AbteilungKlassenzuordnung } from "@core/core/data/schule/AbteilungKlassenzuordnung";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { Comparator } from "@core/java/util/Comparator";
import { HashMap } from "@core/java/util/HashMap";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export interface AbteilungenLookups {
	schuljahresabschnitte: List<Schuljahresabschnitt>,
	abteilungenAktAbschnitt: List<Abteilung>,
	abteilungenFolgeAbschnitt: List<Abteilung>,
	lehrer: List<LehrerListeEintrag>,
	klassenAktAbschnitt: List<KlassenDatenMinimal>,
	klassenFolgeAbschnitt: List<KlassenDatenMinimal>
}

export class AbteilungenListeManager extends AuswahlManager<number, Abteilung, Abteilung> {

	private static readonly _abteilungToId = (a: Abteilung) => a.id;

	private _filterNurSichtbar: boolean = true;
	private _searchTerm: string = "";
	private _deleteAbteilungenInFolgeAbschnitt: boolean = true;

	private readonly _abteilungenFolgeAbschnittByBezeichnung: JavaMap<string, Abteilung> = new HashMap();
	private readonly _abteilungenFolgeAbschnittById: JavaMap<number, Abteilung> = new HashMap();
	private readonly _lehrerById: JavaMap<number, LehrerListeEintrag>;
	private readonly _klassenByIdAktAbschnitt: JavaMap<number, KlassenDatenMinimal>;
	private readonly _klassenByIdFolgeAbschnitt: JavaMap<number, KlassenDatenMinimal>;

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

	private static readonly COMPARATOR_KLASSEN_BY_KUERZEL_ASC: Comparator<KlassenDaten> = {
		compare: (klasse1: KlassenDaten, klasse2: KlassenDaten) => {
			if (klasse1.kuerzel === null) {
				return 0;
			}
			return JavaString.compareTo(klasse1.kuerzel, klasse2.kuerzel);
		},
	};

	public constructor(
		idSchuljahresabschnittAuswahl: number,
		idSchuljahresabschnittSchule: number,
		schulform: Schulform | null,
		lookups: AbteilungenLookups
	) {
		super(
			idSchuljahresabschnittAuswahl,
			idSchuljahresabschnittSchule,
			lookups.schuljahresabschnitte,
			schulform,
			lookups.abteilungenAktAbschnitt,
			AbteilungenListeManager.COMPARATOR_ABTEILUNGEN_DEFAULT,
			AbteilungenListeManager._abteilungToId,
			AbteilungenListeManager._abteilungToId,
			[]);
		this.mapAbteilungenFolgeAbschnitt(lookups.abteilungenFolgeAbschnitt);
		this._lehrerById = this.mapLehrer(lookups.lehrer);
		this._klassenByIdAktAbschnitt = this.mapKlassen(lookups.klassenAktAbschnitt);
		this._klassenByIdFolgeAbschnitt = this.mapKlassen(lookups.klassenFolgeAbschnitt);
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

	public getKlassenByAuswahl(): List<KlassenDatenMinimal> {
		const result: List<KlassenDatenMinimal> | null = new ArrayList<KlassenDatenMinimal>();
		if ((this._daten === null) || (this._daten.klassenzuordnungen.isEmpty())) {
			return result;
		}
		for (const a of this._daten.klassenzuordnungen) {
			const klasse: KlassenDatenMinimal | null = this._klassenByIdAktAbschnitt.get(a.idKlasse);
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

	public getAvailableKlassenToAdd(): List<KlassenDatenMinimal> {
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

	private mapKlassen(klassen: List<KlassenDatenMinimal>): JavaMap<number, KlassenDatenMinimal> {
		const result: JavaMap<number, KlassenDatenMinimal> | null = new HashMap<number, KlassenDatenMinimal>();
		for (const v of klassen) {
			result.put(v.id, v);
		}
		return result;
	}

	private klassenEquals(klasse1: KlassenDatenMinimal, klasse2: KlassenDatenMinimal): boolean {
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

	get klassenByIdAktAbschnitt(): JavaMap<number, KlassenDatenMinimal> {
		return this._klassenByIdAktAbschnitt;
	}

	get searchTerm(): string {
		return this._searchTerm;
	}

	set searchTerm(value: string) {
		this._searchTerm = value;
		this._eventHandlerFilterChanged();
	}

	get filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

	set filterNurSichtbar(value: boolean) {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged();
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

