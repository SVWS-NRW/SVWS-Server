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
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { LehrerUtils } from '../../../core/utils/lehrer/LehrerUtils';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import { Schueler } from '../../../core/data/schueler/Schueler';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Arrays } from '../../../java/util/Arrays';
import { Pair } from '../../../core/adt/Pair';

export class KlassenListeManager extends AuswahlManager<number, KlassenListeEintrag, KlassenDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klasseToId : JavaFunction<KlassenListeEintrag, number> = { apply : (k: KlassenListeEintrag) => k.id };

	private static readonly _klassenDatenToId : JavaFunction<KlassenDaten, number> = { apply : (k: KlassenDaten) => k.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
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
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public constructor(schulform : Schulform | null, klassen : List<KlassenListeEintrag>, jahrgaenge : List<JahrgangsListeEintrag>, lehrer : List<LehrerListeEintrag>) {
		super(schulform, klassen, KlassenUtils.comparator, KlassenListeManager._klasseToId, KlassenListeManager._klassenDatenToId, Arrays.asList(new Pair("klassen", true), new Pair("schueleranzahl", true)));
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, KlassenListeManager._jahrgangToId, JahrgangsUtils.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(lehrer, KlassenListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, KlassenListeManager._schulgliederungToId, KlassenListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.initKlassen();
	}

	private initKlassen() : void {
		for (const k of this.liste.list()) {
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
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : KlassenListeEintrag, daten : KlassenDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		return updateEintrag;
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
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compare(a : KlassenListeEintrag, b : KlassenListeEintrag) : number {
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
	 * Gibt eine gefilterte Liste der Klassen zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassenlehrer und ein Filter auf nur sichtbare Klassen beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	protected onFilter() : List<KlassenListeEintrag> {
		const tmpList : List<KlassenListeEintrag> = new ArrayList();
		for (const eintrag of this.liste.list()) {
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
				if ((j.kuerzelSchulgliederung === null) || ((j.kuerzelSchulgliederung !== null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))))
					continue;
			}
			tmpList.add(eintrag);
		}
		const comparator : Comparator<KlassenListeEintrag> = { compare : (a: KlassenListeEintrag, b: KlassenListeEintrag) => this.compare(a, b) };
		tmpList.sort(comparator);
		return tmpList;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klassen.KlassenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.klassen.KlassenListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_klassen_KlassenListeManager(obj : unknown) : KlassenListeManager {
	return obj as KlassenListeManager;
}
