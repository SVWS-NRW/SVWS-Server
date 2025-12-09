import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { SchulenKatalogEintrag } from '../../../../../core/src/core/data/schule/SchulenKatalogEintrag';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Class } from '../../../../../core/src/java/lang/Class';
import type { SchulEintrag } from '../../../../../core/src/core/data/kataloge/SchulEintrag';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class KatalogSchuleListeManager extends AuswahlManager<number, SchulEintrag, SchulEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _schuleToId: JavaFunction<SchulEintrag, number> = { apply: (schulEintrag: SchulEintrag) => schulEintrag.id };

	/**
	 * die Liste der Schulen aus Gesamt-NRW
	 */
	private readonly schulenKatalogEintraege: List<SchulenKatalogEintrag>;

	/**
	 * Ein Default-Comparator für den Vergleich von Schulen im Schule Katalog.
	 */
	public static readonly _defaultComparator: Comparator<SchulEintrag> = { compare: (a: SchulEintrag, b: SchulEintrag) => {
		let cmp: number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel === null) && (b.kuerzel !== null)) {
			return 1;
		} else
			if ((a.kuerzel !== null) && (b.kuerzel === null)) {
				return -1;
			} else
				if ((a.kuerzel !== null)) {
					cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
				}
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt			 der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte          die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule     der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						 die Schulform der Schule
	 * @param schulen						 die Liste der Schulen
	 * @param schulenKatalogEintraege        die Liste der Schulen aus Gesamt-NRW
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, schulen: List<SchulEintrag>, schulenKatalogEintraege: List<SchulenKatalogEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, schulen, KatalogSchuleListeManager._defaultComparator, KatalogSchuleListeManager._schuleToId, KatalogSchuleListeManager._schuleToId, Arrays.asList());
		this.schulenKatalogEintraege = schulenKatalogEintraege;
	}

	/**
	 * Getter der Gesamtliste der Schulen aus NRW
	 *
	 * @return die Liste der Schulen aus Gesamt-NRW
	 */
	public getSchulenKatalogEintraege(): List<SchulenKatalogEintrag> {
		return new ArrayList<SchulenKatalogEintrag>(this.schulenKatalogEintraege);
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag: SchulEintrag, daten: SchulEintrag): boolean {
		let updateEintrag: boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.kurzbezeichnung, (eintrag.kurzbezeichnung))) {
			eintrag.kurzbezeichnung = daten.kurzbezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Vergleicht zwei SchulEintrag Objekten, anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: SchulEintrag, b: SchulEintrag): number {
		for (const criteria of this._order) {
			const field: string | null = criteria.a;
			const asc: boolean = (criteria.b === null) || criteria.b;
			let cmp: number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				if ((a.kuerzel === null) && (b.kuerzel !== null)) {
					cmp = 1;
				} else
					if ((a.kuerzel !== null) && (b.kuerzel === null)) {
						cmp = -1;
					} else
						if ((a.kuerzel !== null)) {
							cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
						}
			} else
				if (JavaObject.equalsTranspiler("kurzbezeichnung", (field))) {
					if ((a.kurzbezeichnung === null) && (b.kurzbezeichnung !== null)) {
						cmp = 1;
					} else
						if ((a.kurzbezeichnung !== null) && (b.kurzbezeichnung === null)) {
							cmp = -1;
						} else
							if ((a.kurzbezeichnung !== null)) {
								cmp = JavaString.compareTo(a.kurzbezeichnung, b.kurzbezeichnung);
							}
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return KatalogSchuleListeManager._defaultComparator.compare(a, b);
	}

	protected checkFilter(eintrag: SchulEintrag): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schule.KatalogSchuleListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.schule.KatalogSchuleListeManager'].includes(name);
	}

	public static class = new Class<KatalogSchuleListeManager>('de.svws_nrw.core.utils.schule.KatalogSchuleListeManager');

}

export function cast_de_svws_nrw_core_utils_schule_KatalogSchuleListeManager(obj: unknown): KatalogSchuleListeManager {
	return obj as KatalogSchuleListeManager;
}
