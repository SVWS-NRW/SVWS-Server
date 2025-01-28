import { JavaObject } from '../../../java/lang/JavaObject';
import type { JavaSet } from '../../../java/util/JavaSet';
import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { SchuelerEinwilligungsartenZusammenfassung } from '../../../core/data/schueler/SchuelerEinwilligungsartenZusammenfassung';
import type { Comparator } from '../../../java/util/Comparator';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { Einwilligungsart } from '../../../core/data/schule/Einwilligungsart';
import type { Runnable } from '../../../java/lang/Runnable';
import { JavaLong } from '../../../java/lang/JavaLong';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../java/util/HashSet';
import { Pair } from '../../../asd/adt/Pair';

export class EinwilligungsartenListeManager extends AuswahlManager<number, Einwilligungsart, Einwilligungsart> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _einwilligungsArtToId : JavaFunction<Einwilligungsart, number> = { apply : (ea: Einwilligungsart) => ea.id };

	/**
	 * Liste der Schülereinwilligungsarten-Zusammenfassungen
	 */
	private listSchuelerEinwilligungsartenZusammenfassung : AttributMitAuswahl<number, SchuelerEinwilligungsartenZusammenfassung>;

	private static readonly _schuelerToId : JavaFunction<SchuelerEinwilligungsartenZusammenfassung, number> = { apply : (s: SchuelerEinwilligungsartenZusammenfassung) => s.id };

	/**
	 * Das Filter-Attribut auf nur sichtbare Einwilligungsarten
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
	private readonly setEinwilligungsartenIDsMitSchuelern : HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator : Comparator<Einwilligungsart> = { compare : (a: Einwilligungsart, b: Einwilligungsart) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.bezeichnung === null) || (b.bezeichnung === null))
			return JavaLong.compare(a.id, b.id);
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten.
	 */
	public static readonly comparatorSchuelerEinwilligungsartenZusammenfassung : Comparator<SchuelerEinwilligungsartenZusammenfassung> = { compare : (a: SchuelerEinwilligungsartenZusammenfassung, b: SchuelerEinwilligungsartenZusammenfassung) => {
		let cmp : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.vorname, b.vorname);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param listEinwilligungsart     						      die Liste der Einwilligungsarten
	 * @param listSchuelerEinwilligungsartenZusammenfassung         die Liste der SchuelerEinwilligungsartZusammenfassung
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, listEinwilligungsart : List<Einwilligungsart>, listSchuelerEinwilligungsartenZusammenfassung : List<SchuelerEinwilligungsartenZusammenfassung>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listEinwilligungsart, EinwilligungsartenListeManager.comparator, EinwilligungsartenListeManager._einwilligungsArtToId, EinwilligungsartenListeManager._einwilligungsArtToId, Arrays.asList(new Pair("einwilligungsart", true), new Pair("schueleranzahl", true)));
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl(listSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._schuelerToId, EinwilligungsartenListeManager.comparatorSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülereinwilligungsartZusammenfassungen.
	 *
	 * @param listSchuelerEinwilligungsartenZusammenfassung Eine Liste von SchülereinwilligungsartZusammenfassungen
	 */
	public setListSchuelerEinwilligungsartenZusammenfassung(listSchuelerEinwilligungsartenZusammenfassung : List<SchuelerEinwilligungsartenZusammenfassung>) : void {
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl(listSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._schuelerToId, EinwilligungsartenListeManager.comparatorSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._dummyEvent);
	}

	/**
	 *Gibt das Set mit den EinwilligungsartenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Einwilligungsarten, die Schüler haben
	 */
	public getEinwilligungsartenIDsMitSchuelern() : JavaSet<number> {
		return this.setEinwilligungsartenIDsMitSchuelern;
	}

	/**
	 * Gibt die Liste der SchülereinwilligungsartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülereinwilligungsartZusammenfassungen enthält.
	 */
	public getListSchuelerEinwilligungsartenZusammenfassung() : AttributMitAuswahl<number, SchuelerEinwilligungsartenZusammenfassung> {
		return this.listSchuelerEinwilligungsartenZusammenfassung;
	}

	protected onSetDaten(eintrag : Einwilligungsart, daten : Einwilligungsart) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Einwilligungsarten zurück.
	 *
	 * @return true, wenn nur sichtbare Einwilligungsarten angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar() : boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Einwilligungsarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value : boolean) : void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	protected onMehrfachauswahlChanged() : void {
		this.setEinwilligungsartenIDsMitSchuelern.clear();
		for (const k of this.liste.auswahl())
			if (k.anzahlEinwilligungen !== 0)
				this.setEinwilligungsartenIDsMitSchuelern.add(k.id);
	}

	/**
	 * Vergleicht zwei EinwilligungArtEinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : Einwilligungsart, b : Einwilligungsart) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("einwilligungsart", (field))) {
				cmp = EinwilligungsartenListeManager.comparator.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("schueleranzahl", (field))) {
					cmp = JavaInteger.compare(a.anzahlEinwilligungen, b.anzahlEinwilligungen);
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag : Einwilligungsart) : boolean {
		return !(this._filterNurSichtbar && !eintrag.istSichtbar);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.einwilligungsart.EinwilligungsartenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.einwilligungsart.EinwilligungsartenListeManager'].includes(name);
	}

	public static class = new Class<EinwilligungsartenListeManager>('de.svws_nrw.core.utils.einwilligungsart.EinwilligungsartenListeManager');

}

export function cast_de_svws_nrw_core_utils_einwilligungsart_EinwilligungsartenListeManager(obj : unknown) : EinwilligungsartenListeManager {
	return obj as EinwilligungsartenListeManager;
}
