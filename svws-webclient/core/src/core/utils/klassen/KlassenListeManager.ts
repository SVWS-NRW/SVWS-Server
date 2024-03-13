import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { KlassenDaten } from '../../../core/data/klassen/KlassenDaten';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuelerUtils } from '../../../core/utils/schueler/SchuelerUtils';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { SchuelerStatus } from '../../../core/types/SchuelerStatus';
import type { Comparator } from '../../../java/util/Comparator';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Pair } from '../../../core/adt/Pair';
import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { LehrerUtils } from '../../../core/utils/lehrer/LehrerUtils';
import { Schueler } from '../../../core/data/schueler/Schueler';
import type { Runnable } from '../../../java/lang/Runnable';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Arrays } from '../../../java/util/Arrays';

export class KlassenListeManager extends AuswahlManager<number, KlassenDaten, KlassenDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klasseToId : JavaFunction<KlassenDaten, number> = { apply : (k: KlassenDaten) => k.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapKlasseIstSichtbar : HashMap2D<boolean, number, KlassenDaten> = new HashMap2D();

	private readonly _mapKlasseInJahrgang : HashMap2D<number, number, KlassenDaten> = new HashMap2D();

	private readonly _mapKlasseHatSchueler : HashMap2D<number, number, KlassenDaten> = new HashMap2D();

	private readonly _mapKlassenlehrerInKlasse : HashMap2D<number, number, KlassenDaten> = new HashMap2D();

	private readonly _mapKlasseInSchulgliederung : HashMap2D<string, number, KlassenDaten> = new HashMap2D();

	private readonly _mapKlasseByKuerzel : HashMap<string, KlassenDaten> = new HashMap();

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge : AttributMitAuswahl<number, JahrgangsDaten>;

	private static readonly _jahrgangToId : JavaFunction<JahrgangsDaten, number> = { apply : (jg: JahrgangsDaten) => jg.id };

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer : AttributMitAuswahl<number, LehrerListeEintrag>;

	private static readonly _lehrerToId : JavaFunction<LehrerListeEintrag, number> = { apply : (l: LehrerListeEintrag) => l.id };

	/**
	 * Das Filter-Attribut für die Schüler
	 */
	public readonly schueler : AttributMitAuswahl<number, SchuelerListeEintrag>;

	private static readonly _schuelerToId : JavaFunction<SchuelerListeEintrag, number> = { apply : (s: SchuelerListeEintrag) => s.id };

	private schuelerListe : List<Schueler> = new ArrayList<Schueler>();

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen : AttributMitAuswahl<string, Schulgliederung>;

	private static readonly _schulgliederungToId : JavaFunction<Schulgliederung, string> = { apply : (sg: Schulgliederung) => sg.daten.kuerzel };

	private static readonly _comparatorSchulgliederung : Comparator<Schulgliederung> = { compare : (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut für den Schüler-Status
	 */
	public readonly schuelerstatus : AttributMitAuswahl<number, SchuelerStatus>;

	private static readonly _schuelerstatusToId : JavaFunction<SchuelerStatus, number> = { apply : (s: SchuelerStatus) => s.id };

	private static readonly _comparatorSchuelerStatus : Comparator<SchuelerStatus> = { compare : (a: SchuelerStatus, b: SchuelerStatus) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut auf nur sichtbare Klassen
	 */
	private _filterNurSichtbar : boolean = true;

	protected readonly _eventSchuelerAuswahlChanged : Runnable = { run : () => {
		// empty block
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public constructor(schuljahresabschnitt : number, schulform : Schulform | null, klassen : List<KlassenDaten>, schueler : List<SchuelerListeEintrag>, jahrgaenge : List<JahrgangsDaten>, lehrer : List<LehrerListeEintrag>) {
		super(schuljahresabschnitt, schulform, klassen, KlassenUtils.comparator, KlassenListeManager._klasseToId, KlassenListeManager._klasseToId, Arrays.asList(new Pair("klassen", true), new Pair("schueleranzahl", true)));
		this.schuelerstatus = new AttributMitAuswahl(Arrays.asList(...SchuelerStatus.values()), KlassenListeManager._schuelerstatusToId, KlassenListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl(schueler, KlassenListeManager._schuelerToId, SchuelerUtils.comparator, this._eventSchuelerAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, KlassenListeManager._jahrgangToId, JahrgangsUtils.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(lehrer, KlassenListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, KlassenListeManager._schulgliederungToId, KlassenListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.initKlassen();
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
	}

	private initKlassen() : void {
		for (const k of this.liste.list()) {
			this._mapKlasseIstSichtbar.put(k.istSichtbar, k.id, k);
			if (k.idJahrgang !== null) {
				this._mapKlasseInJahrgang.put(k.idJahrgang, k.id, k);
				const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(k.idJahrgang);
				if (j.kuerzelSchulgliederung !== null) {
					const gliederung : Schulgliederung | null = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung !== null)
						this._mapKlasseInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (const s of k.schueler)
				this._mapKlasseHatSchueler.put(s.id, k.id, k);
			for (const l of k.klassenLeitungen)
				this._mapKlassenlehrerInKlasse.put(l, k.id, k);
			if (k.kuerzel !== null)
				this._mapKlasseByKuerzel.put(k.kuerzel, k);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : KlassenDaten, daten : KlassenDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		this.schuelerListe = (daten !== null) ? this.filterSchueler(daten) : new ArrayList<Schueler>();
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
		const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(this._daten.idJahrgang);
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
	protected compareAuswahl(a : KlassenDaten, b : KlassenDaten) : number {
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

	protected checkFilter(eintrag : KlassenDaten) : boolean {
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang === null) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
			return false;
		if (this.lehrer.auswahlExists()) {
			let hatEinenLehrer : boolean = false;
			for (const idLehrer of eintrag.klassenLeitungen)
				if (this.lehrer.auswahlHasKey(idLehrer))
					hatEinenLehrer = true;
			if (!hatEinenLehrer)
				return false;
		}
		if (this.schulgliederungen.auswahlExists()) {
			if (eintrag.idJahrgang === null)
				return false;
			const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(eintrag.idJahrgang);
			if ((j.kuerzelSchulgliederung === null) || ((j.kuerzelSchulgliederung !== null) && (!this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))))
				return false;
		}
		if (this.schuelerstatus.auswahlExists())
			this.schuelerListe = this.filterSchueler(this._daten);
		return true;
	}

	protected filterSchueler(daten : KlassenDaten | null) : List<Schueler> {
		const result : List<Schueler> = new ArrayList<Schueler>();
		if (daten !== null)
			for (const s of daten.schueler)
				if (!this.schuelerstatus.auswahlExists() || this.schuelerstatus.auswahlHasKey(s.status))
					result.add(s);
		return result;
	}

	/**
	 * Gibt die Schülerliste der aktuelle ausgewählten Klasse zurück. Ist
	 * keine Klasse ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public getSchuelerListe() : List<Schueler> {
		return this.schuelerListe;
	}

	/**
	 * Gibt die Klassendaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Klassendaten oder null
	 */
	public getByKuerzelOrNull(kuerzel : string) : KlassenDaten | null {
		return this._mapKlasseByKuerzel.get(kuerzel);
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
