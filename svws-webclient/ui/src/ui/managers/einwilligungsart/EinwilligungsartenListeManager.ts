import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import type { SchuelerEinwilligungsartenZusammenfassung } from '../../../../../core/src/core/data/schueler/SchuelerEinwilligungsartenZusammenfassung';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import { AuswahlManager } from '../../AuswahlManager';
import { AttributMitAuswahl } from '../../AttributMitAuswahl';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { Einwilligungsart } from '../../../../../core/src/core/data/schule/Einwilligungsart';
import type { Runnable } from '../../../../../core/src/java/lang/Runnable';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import type { List } from '../../../../../core/src/java/util/List';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { Pair } from '../../../../../core/src/asd/adt/Pair';

export class EinwilligungsartenListeManager extends AuswahlManager<number, Einwilligungsart, Einwilligungsart> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _einwilligungsArtToId: JavaFunction<Einwilligungsart, number> = { apply: (ea: Einwilligungsart) => ea.id };

	/**
	 * Liste der Schülereinwilligungsarten-Zusammenfassungen
	 */
	private listSchuelerEinwilligungsartenZusammenfassung: AttributMitAuswahl<number, SchuelerEinwilligungsartenZusammenfassung>;

	private static readonly _schuelerToId: JavaFunction<SchuelerEinwilligungsartenZusammenfassung, number> = { apply: (s: SchuelerEinwilligungsartenZusammenfassung) => s.id };

	/**
	 * Das Filter-Attribut auf nur sichtbare Einwilligungsarten
	 */
	private _filterNurSichtbar: boolean = true;

	/**
	 * Ein Dummy-Event.
	 */
	protected static readonly _dummyEvent: Runnable = { run: () => {
		// empty block
	} };

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setEinwilligungsartenIDsMitSchuelern: HashSet<number> = new HashSet<number>();

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Klassenlisten.
	 */
	public static readonly comparator: Comparator<Einwilligungsart> = { compare: (a: Einwilligungsart, b: Einwilligungsart) => {
		let cmp: number;
		cmp = JavaInteger.compare(a.sortierung, b.sortierung);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.bezeichnung, b.bezeichnung);
		if (cmp !== 0)
			return cmp;
		cmp = a.anzahlEinwilligungen - b.anzahlEinwilligungen;
		if (cmp !== 0)
			return cmp;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Schülern in Schuelerlisten.
	 */
	public static readonly comparatorSchuelerEinwilligungsartenZusammenfassung: Comparator<SchuelerEinwilligungsartenZusammenfassung> = { compare: (a: SchuelerEinwilligungsartenZusammenfassung, b: SchuelerEinwilligungsartenZusammenfassung) => {
		let cmp: number = JavaString.compareTo(a.nachname, b.nachname);
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
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, listEinwilligungsart: List<Einwilligungsart>, listSchuelerEinwilligungsartenZusammenfassung: List<SchuelerEinwilligungsartenZusammenfassung>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, listEinwilligungsart, EinwilligungsartenListeManager.comparator, EinwilligungsartenListeManager._einwilligungsArtToId, EinwilligungsartenListeManager._einwilligungsArtToId, Arrays.asList(new Pair("einwilligungsart", true), new Pair("schueleranzahl", true)));
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl(listSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._schuelerToId, EinwilligungsartenListeManager.comparatorSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._dummyEvent);
	}

	/**
	 * Setzt die Liste der SchülereinwilligungsartZusammenfassungen.
	 *
	 * @param listSchuelerEinwilligungsartenZusammenfassung Eine Liste von SchülereinwilligungsartZusammenfassungen
	 */
	public setListSchuelerEinwilligungsartenZusammenfassung(listSchuelerEinwilligungsartenZusammenfassung: List<SchuelerEinwilligungsartenZusammenfassung>): void {
		this.listSchuelerEinwilligungsartenZusammenfassung = new AttributMitAuswahl(listSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._schuelerToId, EinwilligungsartenListeManager.comparatorSchuelerEinwilligungsartenZusammenfassung, EinwilligungsartenListeManager._dummyEvent);
	}

	/**
	 *Gibt das Set mit den EinwilligungsartenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Einwilligungsarten, die Schüler haben
	 */
	public getEinwilligungsartenIDsMitSchuelern(): JavaSet<number> {
		return this.setEinwilligungsartenIDsMitSchuelern;
	}

	/**
	 * Gibt die Liste der SchülereinwilligungsartZusammenfassungen zurück.
	 *
	 * @return Eine Instanz von AttributMitAuswahl, die eine Liste von SchülereinwilligungsartZusammenfassungen enthält.
	 */
	public getListSchuelerEinwilligungsartenZusammenfassung(): AttributMitAuswahl<number, SchuelerEinwilligungsartenZusammenfassung> {
		return this.listSchuelerEinwilligungsartenZusammenfassung;
	}

	protected onSetDaten(eintrag: Einwilligungsart, daten: Einwilligungsart): boolean {
		let updateEintrag: boolean = false;
		if (!JavaObject.equalsTranspiler(daten.bezeichnung, (eintrag.bezeichnung))) {
			eintrag.bezeichnung = daten.bezeichnung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	protected onMehrfachauswahlChanged(): void {
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
	protected compareAuswahl(a: Einwilligungsart, b: Einwilligungsart): number {
		return EinwilligungsartenListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Einwilligungsart): boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;

		return true;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Einwilligungsarten.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value: boolean): void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Einwilligungsarten zurück.
	 *
	 * @return true, wenn nur sichtbare Einwilligungsarten angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar(): boolean {
		return this._filterNurSichtbar;
	}

}

