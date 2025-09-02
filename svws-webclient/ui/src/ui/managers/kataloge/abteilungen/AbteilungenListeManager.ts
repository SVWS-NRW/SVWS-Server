import { JavaObject } from '../../../../../../core/src/java/lang/JavaObject';
import { KlassenDaten } from '../../../../../../core/src/asd/data/klassen/KlassenDaten';
import { Abteilung } from '../../../../../../core/src/core/data/schule/Abteilung';
import { HashMap } from '../../../../../../core/src/java/util/HashMap';
import { Schulform } from '../../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../../core/src/java/util/ArrayList';
import { JavaString } from '../../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../../AuswahlManager';
import { JavaInteger } from '../../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../../core/src/java/util/function/JavaFunction';
import { LehrerListeEintrag } from '../../../../../../core/src/core/data/lehrer/LehrerListeEintrag';
import { AbteilungKlassenzuordnung } from '../../../../../../core/src/core/data/schule/AbteilungKlassenzuordnung';
import { JavaLong } from '../../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../../core/src/java/util/List';
import { Class } from '../../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../../core/src/java/util/Arrays';
import type { JavaMap } from '../../../../../../core/src/java/util/JavaMap';
import { Schuljahresabschnitt } from '../../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class AbteilungenListeManager extends AuswahlManager<number, Abteilung, Abteilung> {

	private static readonly _abteilungToId : JavaFunction<Abteilung, number> = { apply : (a: Abteilung) => a.id };

	private readonly _lehrerById : JavaMap<number, LehrerListeEintrag>;

	private _klassenById : JavaMap<number, KlassenDaten>;

	/**
	 * Ein Default-Comparator für den Vergleich von Abteilungen.
	 */
	public static readonly comparator : Comparator<Abteilung> = { compare : (a: Abteilung, b: Abteilung) => {
		let cmp : number = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0)
			return cmp;
		if ((a.bezeichnung !== null) && (b.bezeichnung !== null)) {
			cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			if (cmp !== 0)
				return cmp;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	private readonly comparatorKlassenzuordnung : Comparator<AbteilungKlassenzuordnung> = { compare : (a: AbteilungKlassenzuordnung, b: AbteilungKlassenzuordnung) => {
		const firstClass : KlassenDaten | null = this._klassenById.get(a.idKlasse);
		const secondClass : KlassenDaten | null = this._klassenById.get(b.idKlasse);
		if ((firstClass === null) || (firstClass.kuerzel === null) || (secondClass === null) || (secondClass.kuerzel === null))
			return 0;
		return JavaString.compareTo(firstClass.kuerzel, secondClass.kuerzel);
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
	public constructor(idSchuljahresabschnittAuswahl : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, abteilungen : List<Abteilung>, lehrer : List<LehrerListeEintrag>, klassen : List<KlassenDaten>) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungen, AbteilungenListeManager.comparator, AbteilungenListeManager._abteilungToId, AbteilungenListeManager._abteilungToId, Arrays.asList());
		this._lehrerById = AbteilungenListeManager.mapLehrer(lehrer);
		this._klassenById = AbteilungenListeManager.mapKlassen(klassen);
	}

	private static mapLehrer(lehrerListe : List<LehrerListeEintrag>) : JavaMap<number, LehrerListeEintrag> {
		const result : JavaMap<number, LehrerListeEintrag> | null = new HashMap<number, LehrerListeEintrag>();
		for (const v of lehrerListe)
			result.put(v.id, v);
		return result;
	}

	private static mapKlassen(klassen : List<KlassenDaten>) : JavaMap<number, KlassenDaten> {
		const result : JavaMap<number, KlassenDaten> | null = new HashMap<number, KlassenDaten>();
		for (const v of klassen)
			result.put(v.id, v);
		return result;
	}

	/**
	 * Löscht Klassenzuordnungen anhand der IDs
	 *
	 * @param ids    Ids der Klassenzuordnungen
	 */
	public deleteKlassenzuordnungen(ids : List<number>) : void {
		if (this._daten === null)
			return;
		for (const id of ids) {
			let toBeDeleted : AbteilungKlassenzuordnung | null = null;
			for (const v of this._daten.klassenzuordnungen) {
				if (v.id === id) {
					toBeDeleted = v;
					break;
				}
			}
			if (toBeDeleted !== null)
				this._daten.klassenzuordnungen.remove(toBeDeleted);
		}
	}

	/**
	 * Ein Getter für die Liste der Lehrer
	 *
	 * @return lehrer
	 */
	public getLehrer() : JavaMap<number, LehrerListeEintrag> {
		return this._lehrerById;
	}

	/**
	 * Ein Getter für die Liste der klassen
	 *
	 * @return klassen
	 */
	public getKlassen() : JavaMap<number, KlassenDaten> {
		return this._klassenById;
	}

	/**
	 * Ein Getter der Klassen für die aktuelle Auswahl
	 *
	 * @return klassen
	 */
	public getKlassenByAuswahl() : List<KlassenDaten> {
		const result : List<KlassenDaten> | null = new ArrayList<KlassenDaten>();
		if ((this._daten === null) || (this._daten.klassenzuordnungen.isEmpty()))
			return result;
		for (const a of this._daten.klassenzuordnungen) {
			const klasse : KlassenDaten | null = this._klassenById.get(a.idKlasse);
			if (klasse !== null)
				result.add(klasse);
		}
		return result;
	}

	/**
	 * Gibt einen Lehrer anhand der gegebenen ID zurück
	 *
	 * @param id	id des Lehrers
	 *
	 * @return		lehrer
	 */
	public getLehrerById(id : number) : LehrerListeEintrag | null {
		return this._lehrerById.get(id);
	}

	/**
	 * Fügt die Liste der AbteilungsKlassenzuordnungen der ausgewählten Abteilung hinzu
	 *
	 * @param zuordnungen    Liste der AbteilungsKlassenzuordnungen
	 */
	public addKlassenToAuswahl(zuordnungen : List<AbteilungKlassenzuordnung>) : void {
		if (this._daten !== null) {
			this._daten.klassenzuordnungen.addAll(zuordnungen);
			this._daten.klassenzuordnungen.sort(this.comparatorKlassenzuordnung);
		}
	}

	protected checkFilter(eintrag : Abteilung | null) : boolean {
		return true;
	}

	protected compareAuswahl(a : Abteilung, b : Abteilung) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("bezeichnung", (field)))
				cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
			else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return AbteilungenListeManager.comparator.compare(a, b);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.abteilungen.AbteilungenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.abteilungen.AbteilungenListeManager'].includes(name);
	}

	public static class = new Class<AbteilungenListeManager>('de.svws_nrw.core.utils.kataloge.abteilungen.AbteilungenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_abteilungen_AbteilungenListeManager(obj : unknown) : AbteilungenListeManager {
	return obj as AbteilungenListeManager;
}
