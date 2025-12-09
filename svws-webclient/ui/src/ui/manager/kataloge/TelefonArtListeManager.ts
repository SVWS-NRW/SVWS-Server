import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import type { TelefonArt } from '../../../../../core/src/core/data/schule/TelefonArt';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Class } from '../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { Pair } from '../../../../../core/src/asd/adt/Pair';

export class TelefonArtListeManager extends AuswahlManager<number, TelefonArt, TelefonArt> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _telefonArtenToId: JavaFunction<TelefonArt, number> = { apply: (ta: TelefonArt) => ta.id };

	/**
	 * Sets der Ids der Telefonarten, die von Personen verwendet und daher nicht gelöscht werden können.
	 */
	private readonly idsVerwendeteTelefonarten: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Telefonarten in Telefonartlisten.
	 */
	public static readonly comparator: Comparator<TelefonArt> = { compare: (a: TelefonArt, b: TelefonArt) => {
		let cmp: number = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp === 0)
			cmp = JavaLong.compare(a.id, b.id);
		return cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Telefonart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param telefonarten     	       die Liste der Telefonart
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>,
		schulform: Schulform | null, telefonarten: List<TelefonArt>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, telefonarten, TelefonArtListeManager.comparator,
			TelefonArtListeManager._telefonArtenToId, TelefonArtListeManager._telefonArtenToId, Arrays.asList(new Pair("telefonArt", true)));
	}

	/**
	 * Gibt das Set der Ids der Telefonarten zurück, die von Personen verwendet und daher nicht gelöscht werden können.
	 *
	 * @return Das Set der Ids der Telefonarten zurück, die von Personen verwendet und daher nicht gelöscht werden können.
	 */
	public getIdsVerwendeteTelefonarten(): JavaSet<number> {
		return this.idsVerwendeteTelefonarten;
	}

	protected onSetDaten(eintrag: TelefonArt, daten: TelefonArt): boolean {
		if (JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung)))
			return false;

		eintrag.bezeichnung = daten.bezeichnung;
		return true;
	}

	protected onMehrfachauswahlChanged(): void {
		this.idsVerwendeteTelefonarten.clear();
		for (const t of this.liste.auswahl())
			if (t.anzahlTelefonnummern !== 0)
				this.idsVerwendeteTelefonarten.add(t.id);
	}

	protected compareAuswahl(a: TelefonArt, b: TelefonArt): number {
		for (const criteria of this._order) {
			const field: string | null = criteria.a;
			const asc: boolean = (criteria.b === null) || criteria.b;
			let cmp: number = 0;
			if (JavaObject.equalsTranspiler("telefonArt", (field))) {
				cmp = TelefonArtListeManager.comparator.compare(a, b);
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
		return 'de.svws_nrw.core.utils.telefonart.TelefonArtListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.telefonart.TelefonArtListeManager'].includes(name);
	}

	public static class = new Class<TelefonArtListeManager>('de.svws_nrw.core.utils.telefonart.TelefonArtListeManager');

}

export function cast_de_svws_nrw_core_utils_telefonart_TelefonArtListeManager(obj: unknown): TelefonArtListeManager {
	return obj as TelefonArtListeManager;
}
