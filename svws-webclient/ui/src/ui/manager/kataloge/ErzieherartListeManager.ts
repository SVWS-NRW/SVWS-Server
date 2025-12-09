import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Erzieherart } from '../../../../../core/src/core/data/erzieher/Erzieherart';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Class } from '../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { Pair } from '../../../../../core/src/asd/adt/Pair';

export class ErzieherartListeManager extends AuswahlManager<number, Erzieherart, Erzieherart> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _erzieherartenToId: JavaFunction<Erzieherart, number> = { apply: (ea: Erzieherart) => ea.id };

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly idsVerwendeteErzieherarten: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Erzieherarten in Erzieherartlisten.
	 */
	public static readonly comparator: Comparator<Erzieherart> = { compare: (a: Erzieherart, b: Erzieherart) => {
		let cmp: number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp === 0)
			cmp = JavaLong.compare(a.id, b.id);
		return cmp;
	} };

	protected onSetDaten(eintrag: Erzieherart, daten: Erzieherart): boolean {
		if (JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung)))
			return false;

		eintrag.bezeichnung = daten.bezeichnung;
		return true;
	}


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Erzieherart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param erzieherarten     	       die Liste der Erzieherart
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, erzieherarten: List<Erzieherart>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, erzieherarten, ErzieherartListeManager.comparator,
			ErzieherartListeManager._erzieherartenToId, ErzieherartListeManager._erzieherartenToId, Arrays.asList(new Pair("erzieherart", true)));
	}

	/**
	 *Gibt das Set mit den ErzieherartIds zurück, die in der Auswahl sind und Erziehungsberechtigte beinhalten
	 *
	 * @return Das Set mit IDs von Erzieherarten, die Schüler haben
	 */
	public getIdsVerwendeteErzieherarten(): JavaSet<number> {
		return this.idsVerwendeteErzieherarten;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsVerwendeteErzieherarten.clear();
		for (const e of this.liste.auswahl())
			if (e.anzahlErziehungsberechtigte !== 0)
				this.idsVerwendeteErzieherarten.add(e.id);
	}

	protected compareAuswahl(a: Erzieherart, b: Erzieherart): number {
		for (const criteria of this._order) {
			const field: string | null = criteria.a;
			const asc: boolean = (criteria.b === null) || criteria.b;
			let cmp: number = 0;
			if (JavaObject.equalsTranspiler("erzieherart", (field))) {
				cmp = ErzieherartListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.erzieherart.ErzieherartListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.erzieherart.ErzieherartListeManager'].includes(name);
	}

	public static class = new Class<ErzieherartListeManager>('de.svws_nrw.core.utils.erzieherart.ErzieherartListeManager');

}

export function cast_de_svws_nrw_core_utils_erzieherart_ErzieherartListeManager(obj: unknown): ErzieherartListeManager {
	return obj as ErzieherartListeManager;
}
