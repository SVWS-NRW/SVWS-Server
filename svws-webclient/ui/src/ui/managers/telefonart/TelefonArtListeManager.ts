import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import { TelefonArt } from '../../../../../core/src/core/data/schule/TelefonArt';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
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

export class TelefonArtListeManager extends AuswahlManager<number, TelefonArt, TelefonArt> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _telefonArtenToId : JavaFunction<TelefonArt, number> = { apply : (ta: TelefonArt) => ta.id };

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setTelefonArtIDsMitPersonen : HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Telefonarten in Telefonartlisten.
	 */
	public static readonly comparator : Comparator<TelefonArt> = { compare : (a: TelefonArt, b: TelefonArt) => {
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
	 * @param schuljahresabschnitt         der Schuljahresabschnitt, auf den sich die Telefonart bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                    die Schulform der Schule
	 * @param listTelefonArt     	       die Liste der Telefonart
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, listTelefonArt : List<TelefonArt>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listTelefonArt, TelefonArtListeManager.comparator, TelefonArtListeManager._telefonArtenToId, TelefonArtListeManager._telefonArtenToId, Arrays.asList(new Pair("telefonArt", true)));
	}

	/**
	 *Gibt das Set mit den TelefonArtIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Telefonarten, die Schüler haben
	 */
	public getTelefonArtIDsMitPersonen() : JavaSet<number> {
		return this.setTelefonArtIDsMitPersonen;
	}

	protected onSetDaten(eintrag : TelefonArt, daten : TelefonArt) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	protected onMehrfachauswahlChanged() : void {
		this.setTelefonArtIDsMitPersonen.clear();
		for (const t of this.liste.auswahl())
			if (t.anzahlTelefonnummern !== 0)
				this.setTelefonArtIDsMitPersonen.add(t.id);
	}

	protected compareAuswahl(a : TelefonArt, b : TelefonArt) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("telefonArt", (field))) {
				cmp = TelefonArtListeManager.comparator.compare(a, b);
			} else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag : TelefonArt) : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.telefonart.TelefonArtListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.telefonart.TelefonArtListeManager'].includes(name);
	}

	public static class = new Class<TelefonArtListeManager>('de.svws_nrw.core.utils.telefonart.TelefonArtListeManager');

}

export function cast_de_svws_nrw_core_utils_telefonart_TelefonArtListeManager(obj : unknown) : TelefonArtListeManager {
	return obj as TelefonArtListeManager;
}
