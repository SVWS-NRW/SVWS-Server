import type { Schuljahresabschnitt } from '@core/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '@core/asd/types/schule/Schulform';
import type { ENMv2Lerngruppe } from '@core/core/data/enm/v2/ENMv2Lerngruppe';
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import { JavaString } from '@core/java/lang/JavaString';
import type { List } from '@core/java/util/List';
import { AuswahlManager } from '@ui/ui/manager/AuswahlManager';
import type { EnmManager } from './EnmManager';

/**
 * Ein Manager für die Auswahl-Liste der Lerngruppen im Externen Notenmodul (ENM)
 */
export class EnmLerngruppenAuswahlListeManager extends AuswahlManager<number, ENMv2Lerngruppe, ENMv2Lerngruppe> {

	/** Der ENM-Manager zum Verwalten der ENM-Daten */
	public readonly enmManager: EnmManager;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _lerngruppeToId = (l: ENMv2Lerngruppe) => l.id;

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
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, enmManager.daten.lerngruppen, enmManager.comparatorLerngruppen,
			EnmLerngruppenAuswahlListeManager._lerngruppeToId, EnmLerngruppenAuswahlListeManager._lerngruppeToId, []);
		this.enmManager = enmManager;
	}

	/**
	 * Vergleicht zwei Lerngruppeneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: ENMv2Lerngruppe, b: ENMv2Lerngruppe): number {
		for (const { field, ascending } of this._order) {
			let cmp: number;

			if (field === "bezeichnung") {
				cmp = JavaString.compareTo(a.bezeichnung ?? "", b.bezeichnung);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			}

			if (cmp === 0) {
				continue;
			}

			return ascending ? cmp : -cmp;
		}

		return this.enmManager.comparatorLerngruppen.compare(a, b);
	}

	protected checkFilter(eintrag: ENMv2Lerngruppe): boolean {
		return true;
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager'].includes(name);
	}

}
