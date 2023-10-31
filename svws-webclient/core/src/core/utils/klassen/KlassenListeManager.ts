import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { KlassenDaten } from '../../../core/data/klassen/KlassenDaten';
import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { Schulform } from '../../../core/types/schule/Schulform';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../java/util/Comparator';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { LehrerUtils } from '../../../core/utils/lehrer/LehrerUtils';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import { Schueler } from '../../../core/data/schueler/Schueler';
import type { Runnable } from '../../../java/lang/Runnable';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Pair } from '../../../core/adt/Pair';

export class KlassenListeManager extends JavaObject {

	/**
	 * Ein Filter-Attribut für die Klassenliste. Dieses wird nicht für das Filtern der Klassen verwendet, sondern für eine Mehrfachauswahl
	 */
	public readonly klassen : AttributMitAuswahl<number, KlassenListeEintrag>;

	private static readonly _klasseToId : JavaFunction<KlassenListeEintrag, number> = { apply : (k: KlassenListeEintrag) => k.id };

	private readonly _mapKlasseIstSichtbar : HashMap2D<boolean, number, KlassenListeEintrag> = new HashMap2D();

	private readonly _mapKlasseInJahrgang : HashMap2D<number, number, KlassenListeEintrag> = new HashMap2D();

	private readonly _mapKlasseHatSchueler : HashMap2D<number, number, KlassenListeEintrag> = new HashMap2D();

	private readonly _mapKlassenlehrerInKlasse : HashMap2D<number, number, KlassenListeEintrag> = new HashMap2D();

	private readonly _mapKlasseInSchulgliederung : HashMap2D<string, number, KlassenListeEintrag> = new HashMap2D();

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge : AttributMitAuswahl<number, JahrgangsListeEintrag>;

	private static readonly _jahrgangToId : JavaFunction<JahrgangsListeEintrag, number> = { apply : (jg: JahrgangsListeEintrag) => jg.id };

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer : AttributMitAuswahl<number, LehrerListeEintrag>;

	private static readonly _lehrerToId : JavaFunction<LehrerListeEintrag, number> = { apply : (l: LehrerListeEintrag) => l.id };

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen : AttributMitAuswahl<string, Schulgliederung>;

	private static readonly _schulgliederungToId : JavaFunction<Schulgliederung, string> = { apply : (sg: Schulgliederung) => sg.daten.kuerzel };

	private static readonly _comparatorSchulgliederung : Comparator<Schulgliederung> = { compare : (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut auf nur sichtbare Klassen
	 */
	private _filterNurSichtbar : boolean = true;

	/**
	 * Die gefilterte Klassen-Liste, sofern sie schon berechnet wurde
	 */
	private _filtered : List<KlassenListeEintrag> | null = null;

	/**
	 * Ein Handler für das Ereignis, dass der Klassen-Filter angepasst wurde
	 */
	private readonly _eventHandlerFilterChanged : Runnable = { run : () => this._filtered = null };

	/**
	 * Ein Handler für das Ereignis, dass die Klassenauswahl angepasst wurde
	 */
	private static readonly _eventHandlerKlassenAuswahlChanged : Runnable = { run : () => {
		// empty block
	} };

	/**
	 * Die Sortier-Ordnung, welche vom Comparator verwendet wird.
	 */
	private _order : List<Pair<string, boolean>> = Arrays.asList(new Pair("klassen", true), new Pair("schueleranzahl", true));

	/**
	 * Die Schulform der Schule
	 */
	private readonly _schulform : Schulform | null;

	/**
	 * Die Daten der Klasse, sofern eine Klasse ausgewählt ist.
	 */
	private _daten : KlassenDaten | null = null;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public constructor(schulform : Schulform | null, klassen : List<KlassenListeEintrag>, jahrgaenge : List<JahrgangsListeEintrag>, lehrer : List<LehrerListeEintrag>) {
		super();
		this._schulform = schulform;
		this.klassen = new AttributMitAuswahl(klassen, KlassenListeManager._klasseToId, KlassenUtils.comparator, KlassenListeManager._eventHandlerKlassenAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, KlassenListeManager._jahrgangToId, JahrgangsUtils.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(lehrer, KlassenListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, KlassenListeManager._schulgliederungToId, KlassenListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.initKlassen();
	}

	private initKlassen() : void {
		for (const k of this.klassen.list()) {
			this._mapKlasseIstSichtbar.put(k.istSichtbar, k.id, k);
			if (k.idJahrgang !== null) {
				this._mapKlasseInJahrgang.put(k.idJahrgang, k.id, k);
				const j : JahrgangsListeEintrag | null = this.jahrgaenge.getOrException(k.idJahrgang);
				if (j.kuerzelSchulgliederung !== null) {
					const gliederung : Schulgliederung | null = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung !== null)
						this._mapKlasseInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (const s of k.schueler)
				this._mapKlasseHatSchueler.put(s.id, k.id, k);
			for (const l of k.klassenLehrer)
				this._mapKlassenlehrerInKlasse.put(l, k.id, k);
		}
	}

	/**
	 * Setzt die Sortier-Ordnung für die gefilterten Listen. Hier wird eine Menge von Paaren angegeben,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @param order   die Sortier-Ordnung
	 */
	public orderSet(order : List<Pair<string, boolean>>) : void {
		this._order = order;
		this._filtered = null;
	}

	/**
	 * Gibt die Sortier-Ordnung für die gefilterten Listen zurück als eine Menge von Paaren,
	 * welche das zu sortierende Feld als String angebenen und als boolean ob es aufsteigend (true)
	 * oder absteigend (false) sortiert werden soll.
	 *
	 * @return   die Sortier-Ordnung
	 */
	public orderGet() : List<Pair<string, boolean>> {
		return new ArrayList(this._order);
	}

	/**
	 * Aktualisiert die Reihenfolge bei der Sortierung für das angegebene Feld. Dabei
	 * werden vorhande Feld-Eintrage angepasst oder bei null entfernt. Nicht vorhande
	 * Feld-Einträge werden ergänzt, sofern eine Reihenfolge definiert wird.
	 *
	 * @param field   das Feld
	 * @param order   die Reihenfolge für dieses Feld (ascending: true, descending: false, deaktivieren: null)
	 */
	public orderUpdate(field : string, order : boolean | null) : void {
		if (order === null) {
			for (let i : number = 0; i < this._order.size(); i++) {
				const eintrag : Pair<string, boolean> = this._order.get(i);
				if (JavaObject.equalsTranspiler(eintrag.a, (field))) {
					this._order.remove(eintrag);
					this._filtered = null;
					return;
				}
			}
			return;
		}
		for (let i : number = 0; i < this._order.size(); i++) {
			const eintrag : Pair<string, boolean> = this._order.get(i);
			if (JavaObject.equalsTranspiler(eintrag.a, (field))) {
				if (eintrag.b === order)
					return;
				this._order.remove(eintrag);
				eintrag.b = order;
				this._order.add(0, eintrag);
				this._filtered = null;
				return;
			}
		}
		const eintrag : Pair<string, boolean> = new Pair(field, order);
		this._order.add(0, eintrag);
		this._filtered = null;
	}

	/**
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	private compare(a : KlassenListeEintrag, b : KlassenListeEintrag) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("klassen", (field))) {
				cmp = KlassenUtils.comparator.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("schueleranzahl", (field))) {
					cmp = JavaInteger.compare(a.schueler.size(), b.schueler.size());
				} else
					throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf nur sichtbare Klassen zurück.
	 *
	 * @return true, wenn nur nichtbare Klassen angezeigt werden und ansonsten false
	 */
	public filterNurSichtbar() : boolean {
		return this._filterNurSichtbar;
	}

	/**
	 * Setzt die Filtereinstellung auf nur sichtbare Klassen.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurSichtbar(value : boolean) : void {
		this._filterNurSichtbar = value;
		this._eventHandlerFilterChanged.run();
	}

	/**
	 * Gibt eine gefilterte Liste der Klassen zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassenlehrer und ein Filter auf nur sichtbare Klassen beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	public filtered() : List<KlassenListeEintrag> {
		if (this._filtered !== null)
			return this._filtered;
		const tmpList : List<KlassenListeEintrag> = new ArrayList();
		for (const eintrag of this.klassen.list()) {
			if (this._filterNurSichtbar && !eintrag.istSichtbar)
				continue;
			if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang === null) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
				continue;
			if (this.lehrer.auswahlExists()) {
				let hatEinenLehrer : boolean = false;
				for (const idLehrer of eintrag.klassenLehrer)
					if (this.lehrer.auswahlHasKey(idLehrer))
						hatEinenLehrer = true;
				if (!hatEinenLehrer)
					continue;
			}
			if (this.schulgliederungen.auswahlExists()) {
				if (eintrag.idJahrgang === null)
					continue;
				const j : JahrgangsListeEintrag | null = this.jahrgaenge.getOrException(eintrag.idJahrgang);
				if ((j.kuerzelStatistik === null) || ((j.kuerzelStatistik !== null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelStatistik))))
					continue;
			}
			tmpList.add(eintrag);
		}
		const comparator : Comparator<KlassenListeEintrag> = { compare : (a: KlassenListeEintrag, b: KlassenListeEintrag) => this.compare(a, b) };
		tmpList.sort(comparator);
		this._filtered = tmpList;
		return this._filtered;
	}

	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform der Schule
	 */
	public schulform() : Schulform {
		if (this._schulform === null)
			throw new DeveloperNotificationException("Der Klassenlisten-Manager sollte nur mit einer korrekt gesetzten Schulform verwendet werden.")
		return this._schulform;
	}

	/**
	 * Gibt zurück, ob eine Klassen ausgewählt ist und Daten vorliegen.
	 *
	 * @return true, wenn Daten vorliegen, und ansonsten false
	 */
	public hasDaten() : boolean {
		return this._daten !== null;
	}

	/**
	 * Gibt die Daten der aktuell ausgewählten Klassen zurück.
	 *
	 * @return die Daten
	 */
	public daten() : KlassenDaten {
		if (this._daten === null)
			throw new DeveloperNotificationException("Es exitsiert derzeit keine Klassenauswahl")
		return this._daten;
	}

	/**
	 * Setzt die Daten der Klasse. Dabei wird ggf. die Auswahl angepasst.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Klasse nicht in der Liste der Klassen vorhanden ist
	 */
	public setDaten(daten : KlassenDaten | null) : void {
		if (daten === null) {
			this._daten = null;
			return;
		}
		const eintrag : KlassenListeEintrag = this.klassen.getOrException(daten.id);
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		this._daten = daten;
		if (updateEintrag)
			this.orderSet(this.orderGet());
	}

	/**
	 * Gibt die ID der Auswahl zurück. Ist keine Auswahl vorhanden, so wird null zurückgegeben.
	 *
	 * @return die ID oder null
	 */
	public auswahlID() : number | null {
		return this._daten === null ? null : this._daten.id;
	}

	/**
	 * Gibt den Eintrag der aktuellen Klassenauswahl in der Liste zurück. Hiefür muss eine
	 * gültige Auswahl vorliegen. Dies kann ggf. vorher über hasDaten geprüft werden.
	 *
	 * @return der Eintrag in der Klassenliste
	 *
	 * @throws DeveloperNotificationException wenn keine gültige Auswahl vorliegt
	 */
	public auswahl() : KlassenListeEintrag {
		if (this._daten === null)
			throw new DeveloperNotificationException("Für den Aufruf dieser Methode muss zuvor eine Klassen-Auswahl vorliegen.")
		return this.klassen.getOrException(this._daten.id);
	}

	/**
	 * Gibt die Schulgliederung für die aktuell ausgewählte Klasse zurück.
	 *
	 * @return die Schulgliederung der Klasse
	 */
	public datenGetSchulgliederung() : Schulgliederung | null {
		if ((this._daten === null) || (this._daten.idJahrgang === null))
			return null;
		const j : JahrgangsListeEintrag | null = this.jahrgaenge.getOrException(this._daten.idJahrgang);
		return (j.kuerzelSchulgliederung === null) ? null : this.schulgliederungen.get(j.kuerzelSchulgliederung);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.klassen.KlassenListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klassen_KlassenListeManager(obj : unknown) : KlassenListeManager {
	return obj as KlassenListeManager;
}
