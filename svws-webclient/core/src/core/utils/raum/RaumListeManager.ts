import { JavaObject } from '../../../java/lang/JavaObject';
import { Raum } from '../../../core/data/schule/Raum';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { RaumUtils } from '../../../core/utils/raum/RaumUtils';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class RaumListeManager extends AuswahlManager<number, Raum, Raum> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _raumToId : JavaFunction<Raum, number> = { apply : (f: Raum) => f.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapRaumByKuerzel : HashMap<string, Raum> = new HashMap<string, Raum>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte			die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						die Schulform der Schule
	 * @param raeume						die Liste der Raum
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, raeume : List<Raum>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, raeume, RaumUtils.comparator, RaumListeManager._raumToId, RaumListeManager._raumToId, Arrays.asList());
		this.initRaeume();
	}

	private initRaeume() : void {
		for (const f of this.liste.list()) {
			if (f.kuerzel !== null)
				this._mapRaumByKuerzel.put(f.kuerzel, f);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : Raum, daten : Raum) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.beschreibung, (eintrag.beschreibung))) {
			eintrag.beschreibung = daten.beschreibung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Vergleicht zwei Raumlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : Raum, b : Raum) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else
				if (JavaObject.equalsTranspiler("beschreibung", (field))) {
					cmp = JavaString.compareTo(a.beschreibung, b.beschreibung);
				} else
					if (JavaObject.equalsTranspiler("groesse", (field))) {
						cmp = JavaLong.compare(a.groesse, b.groesse);
					} else
						throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return RaumUtils.comparator.compare(a, b);
	}

	protected checkFilter(eintrag : Raum) : boolean {
		return true;
	}

	/**
	 * Gibt die Raum anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Raum oder null
	 */
	public getByKuerzelOrNull(kuerzel : string) : Raum | null {
		return this._mapRaumByKuerzel.get(kuerzel);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.raum.RaumListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.raum.RaumListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_raum_RaumListeManager(obj : unknown) : RaumListeManager {
	return obj as RaumListeManager;
}
