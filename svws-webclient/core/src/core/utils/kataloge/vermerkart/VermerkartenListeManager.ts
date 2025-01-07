import { JavaObject } from '../../../../java/lang/JavaObject';
import type { JavaSet } from '../../../../java/util/JavaSet';
import { VermerkartEintrag } from '../../../../core/data/schule/VermerkartEintrag';
import { AttributMitAuswahl } from '../../../../core/utils/AttributMitAuswahl';
import { Schulform } from '../../../../asd/types/schule/Schulform';
import { SchuelerUtils } from '../../../../core/utils/schueler/SchuelerUtils';
import { JavaString } from '../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { SchuelerVermerkartZusammenfassung } from '../../../../core/data/schueler/SchuelerVermerkartZusammenfassung';
import type { Comparator } from '../../../../java/util/Comparator';
import { AuswahlManager } from '../../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../../java/util/function/JavaFunction';
import type { Runnable } from '../../../../java/lang/Runnable';
import { JavaLong } from '../../../../java/lang/JavaLong';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { Arrays } from '../../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../../asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../java/util/HashSet';
import { Pair } from '../../../../asd/adt/Pair';

export class VermerkartenListeManager extends AuswahlManager<number, VermerkartEintrag, VermerkartEintrag> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _vermerkartToId : JavaFunction<VermerkartEintrag, number> = { apply : (v: VermerkartEintrag) => v.id };

	private listSchuelerVermerkartZusammenfassung : AttributMitAuswahl<number, SchuelerVermerkartZusammenfassung>;

	private static readonly _schuelerToId : JavaFunction<SchuelerVermerkartZusammenfassung, number> = { apply : (s: SchuelerVermerkartZusammenfassung) => s.id };

	/**
	 * Das Filter-Attribut auf nur sichtbare Vermerkarten
	 */
	private _filterNurSichtbar : boolean = true;

	/**
	 * Ein Dummy-Event.
	 */
	protected static readonly _dummyEvent : Runnable = { run : () => {
		// empty block
	} };

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setVermerkartenIDsMitSchuelern : HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Vermerkarten in Vermerkartenlisten.
	 */
	public static readonly comparator : Comparator<VermerkartEintrag> = { compare : (a: VermerkartEintrag, b: VermerkartEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
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
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param vermerkarten     					die Liste der Vermerkarten
	 * @param listSchuelerVermerkartZusammenfassung     	die Liste der SchuelerVermerkartZusammenfassung
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, vermerkarten : List<VermerkartEintrag>, listSchuelerVermerkartZusammenfassung : List<SchuelerVermerkartZusammenfassung>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, vermerkarten, VermerkartenListeManager.comparator, VermerkartenListeManager._vermerkartToId, VermerkartenListeManager._vermerkartToId, Arrays.asList(new Pair("Vermerkart", true), new Pair("schueleranzahl", true)));
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl(listSchuelerVermerkartZusammenfassung, VermerkartenListeManager._schuelerToId, SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, VermerkartenListeManager._dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülervermerkartZusammenfassungen.
	 *
	 * @param listSchuelerVermerkartZusammenfassung Eine Liste von SchülervermerkartZusammenfassungen
	 */
	public setListSchuelerVermerkartZusammenfassung(listSchuelerVermerkartZusammenfassung : List<SchuelerVermerkartZusammenfassung>) : void {
		this.listSchuelerVermerkartZusammenfassung = new AttributMitAuswahl(listSchuelerVermerkartZusammenfassung, VermerkartenListeManager._schuelerToId, SchuelerUtils.comparatorSchuelerVermerkartZusammenfassung, VermerkartenListeManager._dummyEvent);
	}

	/**
	 *Gibt das Set mit den VermerkartenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Vermerkarten, die Schüler haben
	 */
	public getVermerkartenIDsMitSchuelern() : JavaSet<number> {
		return this.setVermerkartenIDsMitSchuelern;
	}

	/**
	 * Gibt die Liste der SchülervermerkartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülervermerkartZusammenfassungen enthält.
	 */
	public getListSchuelerVermerkartZusammenfassung() : AttributMitAuswahl<number, SchuelerVermerkartZusammenfassung> {
		return this.listSchuelerVermerkartZusammenfassung;
	}

	protected onSetDaten(eintrag : VermerkartEintrag, daten : VermerkartEintrag) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Vermerkarten zurück.
	 *
	 * @return true, wenn nur sichtbare Vermerkarten angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar() : boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Vermerkarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value : boolean) : void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected onMehrfachauswahlChanged() : void {
		this.setVermerkartenIDsMitSchuelern.clear();
		for (const k of this.liste.auswahl())
			if (k.anzahlVermerke !== 0)
				this.setVermerkartenIDsMitSchuelern.add(k.id);
	}

	/**
	 * Vergleicht zwei VermerkArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : VermerkartEintrag, b : VermerkartEintrag) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("Vermerkart", (field))) {
				cmp = VermerkartenListeManager.comparator.compare(a, b);
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
		return !(this._filterNurSichtbar && !eintrag.istSichtbar);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kataloge.vermerkart.VermerkartenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kataloge.vermerkart.VermerkartenListeManager'].includes(name);
	}

	public static class = new Class<VermerkartenListeManager>('de.svws_nrw.core.utils.kataloge.vermerkart.VermerkartenListeManager');

}

export function cast_de_svws_nrw_core_utils_kataloge_vermerkart_VermerkartenListeManager(obj : unknown) : VermerkartenListeManager {
	return obj as VermerkartenListeManager;
}
