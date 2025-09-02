import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { Lernplattform } from '../../../../../core/src/core/data/schule/Lernplattform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Class } from '../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { Pair } from '../../../../../core/src/asd/adt/Pair';

export class LernplattformListeManager extends AuswahlManager<number, Lernplattform, Lernplattform> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _lernplattformenToId : JavaFunction<Lernplattform, number> = { apply : (ea: Lernplattform) => ea.id };

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setLernplattformIDsMitPersonen : HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Lernplattformen in Lernplattformlisten.
	 */
	public static readonly comparator : Comparator<Lernplattform> = { compare : (a: Lernplattform, b: Lernplattform) => {
		let cmp : number = (a.id - b.id) as number;
		if (cmp !== 0)
			return cmp;
		if ((a.bezeichnung === null) || (b.bezeichnung === null))
			return JavaLong.compare(a.id, b.id);
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Lernplattform bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param listLernplattform     	   die Liste der Lernplattform
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, listLernplattform : List<Lernplattform>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listLernplattform, LernplattformListeManager.comparator, LernplattformListeManager._lernplattformenToId, LernplattformListeManager._lernplattformenToId, Arrays.asList(new Pair("lernplattform", true)));
	}

	/**
	 *Gibt das Set mit den LernplattformIds zurück, die in der Auswahl sind und Schüler oder Lehrer beinhalten
	 *
	 * @return Das Set mit IDs von Lernplattformen, die Schüler oder Lehrer haben
	 */
	public getLernplattformIDsMitPersonen() : JavaSet<number> {
		return this.setLernplattformIDsMitPersonen;
	}

	protected onSetDaten(eintrag : Lernplattform, daten : Lernplattform) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	protected onMehrfachauswahlChanged() : void {
		this.setLernplattformIDsMitPersonen.clear();
		for (const l of this.liste.auswahl())
			if (l.anzahlEinwilligungen !== 0)
				this.setLernplattformIDsMitPersonen.add(l.id);
	}

	protected compareAuswahl(a : Lernplattform, b : Lernplattform) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("lernplattform", (field))) {
				cmp = LernplattformListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag : Lernplattform) : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.lernplattform.LernplattformListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.lernplattform.LernplattformListeManager'].includes(name);
	}

	public static class = new Class<LernplattformListeManager>('de.svws_nrw.core.utils.lernplattform.LernplattformListeManager');

}

export function cast_de_svws_nrw_core_utils_lernplattform_LernplattformListeManager(obj : unknown) : LernplattformListeManager {
	return obj as LernplattformListeManager;
}
