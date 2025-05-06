import { JavaObject } from '../../../../java/lang/JavaObject';
import { Abteilung } from '../../../../core/data/schule/Abteilung';
import { HashMap } from '../../../../java/util/HashMap';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../java/util/Comparator';
import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import { LehrerListeEintrag } from '../../../../core/data/lehrer/LehrerListeEintrag';
import { JavaLong } from '../../../../java/lang/JavaLong';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import type { JavaMap } from '../../../../java/util/JavaMap';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';

export class AbteilungenListeManager extends AuswahlManager<number, Abteilung, Abteilung> {

	private static readonly _abteilungToId : JavaFunction<Abteilung, number> = { apply : (a: Abteilung) => a.id };

	private readonly _lehrer : JavaMap<number, LehrerListeEintrag>;

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


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param idSchuljahresabschnittAuswahl    	  der Schuljahresabschnitt, auf den sich die Abteilungsauswahl bezieht
	 * @param idSchuljahresabschnittSchule    der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schuljahresabschnitte           die Liste der Schuljahresabschnitte
	 * @param schulform     				  die Schulform der Schule
	 * @param abteilungen     				  die Liste der Abteilungen
	 * @param lehrer     					  die Liste der Lehrer
	 */
	public constructor(idSchuljahresabschnittAuswahl : number, idSchuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, abteilungen : List<Abteilung>, lehrer : List<LehrerListeEintrag>) {
		super(idSchuljahresabschnittAuswahl, idSchuljahresabschnittSchule, schuljahresabschnitte, schulform, abteilungen, AbteilungenListeManager.comparator, AbteilungenListeManager._abteilungToId, AbteilungenListeManager._abteilungToId, Arrays.asList());
		this._lehrer = AbteilungenListeManager.mapLehrer(lehrer);
	}

	private static mapLehrer(lehrerListe : List<LehrerListeEintrag>) : JavaMap<number, LehrerListeEintrag> {
		const result : JavaMap<number, LehrerListeEintrag> | null = new HashMap<number, LehrerListeEintrag>();
		for (const v of lehrerListe)
			result.put(v.id, v);
		return result;
	}

	/**
	 * Ein Getter für die Liste der Lehrer
	 *
	 * @return lehrer
	 */
	public getLehrer() : JavaMap<number, LehrerListeEintrag> {
		return this._lehrer;
	}

	/**
	 * Gibt einen Lehrer anhand der gegebenen ID zurück
	 *
	 * @param id	id des Lehrers
	 *
	 * @return		lehrer
	 */
	public getLehrerById(id : number) : LehrerListeEintrag | null {
		return this._lehrer.get(id);
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
