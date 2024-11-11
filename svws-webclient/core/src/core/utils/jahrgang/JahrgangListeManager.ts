import { JavaObject } from '../../../java/lang/JavaObject';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';

export class JahrgangListeManager extends AuswahlManager<number, JahrgangsDaten, JahrgangsDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _jahrgangToId : JavaFunction<JahrgangsDaten, number> = { apply : (j: JahrgangsDaten) => j.id };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param jahrgaenge       die Liste der Jahrgänge
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, jahrgaenge : List<JahrgangsDaten>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, jahrgaenge, JahrgangsUtils.comparator, JahrgangListeManager._jahrgangToId, JahrgangListeManager._jahrgangToId, Arrays.asList());
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : JahrgangsDaten, daten : JahrgangsDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.anzahlRestabschnitte, (eintrag.anzahlRestabschnitte))) {
			eintrag.anzahlRestabschnitte = daten.anzahlRestabschnitte;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Vergleicht zwei JahrgangsDaten Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : JahrgangsDaten, b : JahrgangsDaten) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				if ((a.kuerzel === null) && (b.kuerzel !== null)) {
					cmp = -1;
				} else
					if ((a.kuerzel !== null) && (b.kuerzel === null)) {
						cmp = 1;
					} else
						if ((a.kuerzel !== null) && (b.kuerzel !== null)) {
							cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
						}
			} else
				if (JavaObject.equalsTranspiler("bezeichnung", (field))) {
					cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JahrgangsUtils.comparator.compare(a, b);
	}

	protected checkFilter(eintrag : JahrgangsDaten) : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.jahrgang.JahrgangListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.jahrgang.JahrgangListeManager'].includes(name);
	}

	public static class = new Class<JahrgangListeManager>('de.svws_nrw.core.utils.jahrgang.JahrgangListeManager');

}

export function cast_de_svws_nrw_core_utils_jahrgang_JahrgangListeManager(obj : unknown) : JahrgangListeManager {
	return obj as JahrgangListeManager;
}
