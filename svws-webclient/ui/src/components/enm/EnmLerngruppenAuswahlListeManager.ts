import { JavaObject } from '../../../../core/src/java/lang/JavaObject';
import type { Schulform } from '../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/src/core/exceptions/DeveloperNotificationException';
import { AuswahlManager } from '../../ui/AuswahlManager';
import type { JavaFunction } from '../../../../core/src/java/util/function/JavaFunction';
import type { List } from '../../../../core/src/java/util/List';
import { Arrays } from '../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { ENMLerngruppe } from '../../../../core/src';
import type { EnmManager } from './EnmManager';

/**
 * Ein Manager für die Auswahl-Liste der Lerngruppen im Externen Notenmodul (ENM)
 */
export class EnmLerngruppenAuswahlListeManager extends AuswahlManager<number, ENMLerngruppe, ENMLerngruppe> {

	/** Der ENM-Manager zum Verwalten der ENM-Daten */
	public readonly enmManager: EnmManager;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _lerngruppeToId : JavaFunction<ENMLerngruppe, number> = { apply : (l: ENMLerngruppe) => l.id };

	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param enmManager                   der ENM-Manager für die ENM-Daten
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 */
	public constructor(enmManager: EnmManager, schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, enmManager.daten.lerngruppen, enmManager.comparatorLerngruppen,
			EnmLerngruppenAuswahlListeManager._lerngruppeToId, EnmLerngruppenAuswahlListeManager._lerngruppeToId, Arrays.asList());
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
	protected compareAuswahl(a : ENMLerngruppe, b : ENMLerngruppe) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("bezeichnung", (field))) {
				cmp = JavaString.compareTo(a.bezeichnung ?? "", b.bezeichnung);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return this.enmManager.comparatorLerngruppen.compare(a, b);
	}

	protected checkFilter(eintrag : ENMLerngruppe) : boolean {
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return [ 'de.svws_nrw.core.utils.AuswahlManager' ].includes(name);
	}

}
