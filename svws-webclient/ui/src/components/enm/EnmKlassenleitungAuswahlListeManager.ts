import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '@core/asd/types/schule/Schulform';
import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import { JavaString } from '@core/java/lang/JavaString';
import type { List } from '@core/java/util/List';
import { AuswahlManager } from '@ui/ui/manager/AuswahlManager';
import type { EnmManager } from './EnmManager';

/**
 * Ein Manager für die Auswahl-Liste der Klassenleitungen im Externen Notenmodul (ENM)
 */
export class EnmKlassenleitungAuswahlListeManager extends AuswahlManager<number, ENMv2Klasse, ENMv2Klasse> {

	/** Der ENM-Manager zum Verwalten der ENM-Daten */
	public readonly enmManager: EnmManager;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klasseToId = (k: ENMv2Klasse) => k.id;

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param enmManager                   der ENM-Manager für die ENM-Daten
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 */
	public constructor(enmManager: EnmManager, schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, enmManager.listKlassenKlassenlehrer, enmManager.comparatorKlassen,
			EnmKlassenleitungAuswahlListeManager._klasseToId, EnmKlassenleitungAuswahlListeManager._klasseToId, []);
		this.enmManager = enmManager;
	}

	/**
	 * Vergleicht zwei Klasseneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: ENMv2Klasse, b: ENMv2Klasse): number {
		for (const { field, ascending } of this._order) {
			let cmp: number;

			if (field === "kuerzelAnzeige") {
				cmp = JavaString.compareTo(a.kuerzelAnzeige ?? "", b.kuerzelAnzeige);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			}

			if (cmp === 0) {
				continue;
			}

			return ascending ? cmp : -cmp;
		}

		return this.enmManager.comparatorKlassen.compare(a, b);
	}

	protected checkFilter(eintrag: ENMv2Klasse): boolean {
		return true;
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager'].includes(name);
	}

}
