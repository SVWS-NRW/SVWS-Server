import { JavaObject } from '../../../../java/lang/JavaObject';
import { VermerkartEintrag } from '../../../../core/data/schule/VermerkartEintrag';
import { AttributMitAuswahl } from '../../../../core/utils/AttributMitAuswahl';
import { VermerkArtUtils } from '../../../../core/utils/kataloge/vermerkart/VermerkArtUtils';
import { ArrayList } from '../../../../java/util/ArrayList';
import { SchuelerUtils } from '../../../../core/utils/schueler/SchuelerUtils';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { SchuelerVermerkartZusammenfassung } from '../../../../core/data/schueler/SchuelerVermerkartZusammenfassung';
import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import type { Runnable } from '../../../../java/lang/Runnable';
import { JavaLong } from '../../../../java/lang/JavaLong';
import type { List } from '../../../../java/util/List';
import { Arrays } from '../../../../java/util/Arrays';
import { Pair } from '../../../../core/adt/Pair';

export class VermerkartenManager extends AuswahlManager<number, VermerkartEintrag, VermerkartEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _vermerkArtToId : JavaFunction<VermerkartEintrag, number> = { apply : (vme: VermerkartEintrag) => vme.id };

	private listSchuelerVermerkartZusammenfassung : AttributMitAuswahl<number, SchuelerVermerkartZusammenfassung>;

	private static readonly _schuelerToId : JavaFunction<SchuelerVermerkartZusammenfassung, number> = { apply : (s: SchuelerVermerkartZusammenfassung) => s.id };

	protected static readonly _dummyEvent : Runnable = { run : () => {
		// empty block
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param listVermerkArtEintrag     					die Liste der Vermerkarten
	 * @param listSchuelerVermerkartZusammenfassung     	die Liste der SchuelerVermerkartZusammenfassung
	 */
	public constructor(listVermerkArtEintrag : List<VermerkartEintrag>, listSchuelerVermerkartZusammenfassung : List<SchuelerVermerkartZusammenfassung>) {
		super(-1, -1, new ArrayList(), null, listVermerkArtEintrag, VermerkArtUtils.comparator, VermerkartenManager._vermerkArtToId, VermerkartenManager._vermerkArtToId, Arrays.asList(new Pair("VermerkArt", true), new Pair("schueleranzahl", true)));
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl(listSchuelerVermerkartZusammenfassung, VermerkartenManager._schuelerToId, SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, VermerkartenManager._dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülervermerkartZusammenfassungen.
	 *
	 * @param listSchuelerVermerkartZusammenfassung Eine Liste von SchülervermerkartZusammenfassungen
	 */
	public setListSchuelerVermerkartZusammenfassung(listSchuelerVermerkartZusammenfassung : List<SchuelerVermerkartZusammenfassung>) : void {
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl(listSchuelerVermerkartZusammenfassung, VermerkartenManager._schuelerToId, SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, VermerkartenManager._dummyEvent);
	}

	/**
	 * Gibt die Liste der SchülervermerkartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülervermerkartZusammenfassungen enthält.
	 */
	public getListSchuelerVermerkartZusammenfassung() : AttributMitAuswahl<number, SchuelerVermerkartZusammenfassung> {
		return this.listSchuelerVermerkartZusammenfassung;
	}

	/**
	 * Vergleicht zwei VermerkArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : VermerkartEintrag, b : VermerkartEintrag) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("VermerkArt", (field))) {
				cmp = VermerkArtUtils.comparator.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("schueleranzahl", (field))) {
					cmp = JavaInteger.compare(a.anzahlVermerke, b.anzahlVermerke);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag : VermerkartEintrag) : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.vermerkart.VermerkartenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.vermerkart.VermerkartenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_kataloge_vermerkart_VermerkartenManager(obj : unknown) : VermerkartenManager {
	return obj as VermerkartenManager;
}
