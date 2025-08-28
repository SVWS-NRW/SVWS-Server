import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import { HashMap2D } from '../../../../../core/src/core/adt/map/HashMap2D';
import type { SchuelerListeEintrag } from '../../../../../core/src/core/data/schueler/SchuelerListeEintrag';
import type { SchuelerStatusKatalogEintrag } from '../../../../../core/src/asd/data/schueler/SchuelerStatusKatalogEintrag';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { SchuelerUtils } from '../../../../../core/src/core/utils/schueler/SchuelerUtils';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { JahrgangsDaten } from '../../../../../core/src/core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import { SchuelerStatus } from '../../../../../core/src/asd/types/schueler/SchuelerStatus';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import type { KursDaten } from '../../../../../core/src/asd/data/kurse/KursDaten';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { LehrerListeEintrag } from '../../../../../core/src/core/data/lehrer/LehrerListeEintrag';
import { Schulgliederung } from '../../../../../core/src/asd/types/schule/Schulgliederung';
import type { SchulgliederungKatalogEintrag } from '../../../../../core/src/asd/data/schule/SchulgliederungKatalogEintrag';
import type { List } from '../../../../../core/src/java/util/List';
import { IllegalArgumentException } from '../../../../../core/src/java/lang/IllegalArgumentException';
import { Pair } from '../../../../../core/src/asd/adt/Pair';
import { AttributMitAuswahl } from '../../../../../core/src/core/utils/AttributMitAuswahl';
import { JahrgaengeListeManager } from '../kataloge/jahrgaenge/JahrgaengeListeManager';
import type { FaecherListeEintrag } from '../../../../../core/src/core/data/fach/FaecherListeEintrag';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import { LehrerUtils } from '../../../../../core/src/core/utils/lehrer/LehrerUtils';
import type { Schueler } from '../../../../../core/src/asd/data/schueler/Schueler';
import type { Runnable } from '../../../../../core/src/java/lang/Runnable';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { Class } from '../../../../../core/src/java/lang/Class';
import { KursUtils } from '../../../../../core/src/core/utils/kurse/KursUtils';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class KursListeManager extends AuswahlManager<number, KursDaten, KursDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _kursToId : JavaFunction<KursDaten, number> = { apply : (k: KursDaten) => k.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapKursIstSichtbar : HashMap2D<boolean, number, KursDaten> = new HashMap2D<boolean, number, KursDaten>();

	private readonly _mapKursInJahrgang : HashMap2D<number, number, KursDaten> = new HashMap2D<number, number, KursDaten>();

	private readonly _mapKursHatSchueler : HashMap2D<number, number, KursDaten> = new HashMap2D<number, number, KursDaten>();

	private readonly _mapLehrerInKurs : HashMap2D<number, number, KursDaten> = new HashMap2D<number, number, KursDaten>();

	private readonly _mapKursHatFach : HashMap2D<number, number, KursDaten> = new HashMap2D<number, number, KursDaten>();

	private readonly _mapKursInSchulgliederung : HashMap2D<string, number, KursDaten> = new HashMap2D<string, number, KursDaten>();

	private readonly _mapKursByKuerzelAndJahrgang : HashMap2D<string, number, KursDaten> = new HashMap2D<string, number, KursDaten>();

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
	 * Das Filter-Attribut für die Fächer
	 */
	public readonly faecher : AttributMitAuswahl<number, FaecherListeEintrag>;

	private static readonly _fachToId : JavaFunction<FaecherListeEintrag, number> = { apply : (f: FaecherListeEintrag) => f.id };

	/**
	 * Das Filter-Attribut für die Schüler
	 */
	public readonly schueler : AttributMitAuswahl<number, SchuelerListeEintrag>;

	private static readonly _schuelerToId : JavaFunction<SchuelerListeEintrag, number> = { apply : (s: SchuelerListeEintrag) => s.id };

	private _filteredSchuelerListe : List<Schueler> | null = null;

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen : AttributMitAuswahl<string, Schulgliederung>;

	private readonly _schulgliederungToId : JavaFunction<Schulgliederung, string> = { apply : (sg: Schulgliederung) => {
		const sglke : SchulgliederungKatalogEintrag | null = sg.daten(this.getSchuljahr());
		if (sglke === null)
			throw new IllegalArgumentException(JavaString.format("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.", sg.name(), this.getSchuljahr()))
		return sglke.kuerzel;
	} };

	private static readonly _comparatorSchulgliederung : Comparator<Schulgliederung> = { compare : (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut für den Schüler-Status
	 */
	public readonly schuelerstatus : AttributMitAuswahl<number, SchuelerStatus>;

	private readonly _schuelerstatusToId : JavaFunction<SchuelerStatus, number> = { apply : (s: SchuelerStatus) => {
		const sske : SchuelerStatusKatalogEintrag | null = s.daten(this.getSchuljahr());
		if (sske === null)
			throw new IllegalArgumentException(JavaString.format("Der Schülerstatus %s ist in dem Schuljahr %d nicht gültig.", s.name(), this.getSchuljahr()))
		return JavaInteger.parseInt(sske.kuerzel);
	} };

	private static readonly _comparatorSchuelerStatus : Comparator<SchuelerStatus> = { compare : (a: SchuelerStatus, b: SchuelerStatus) => a.ordinal() - b.ordinal() };

	/**
	 * Ein Default-Comparator für den Vergleich von Fächern in Fächerlisten.
	 */
	public static readonly comparatorFaecherListe : Comparator<FaecherListeEintrag> = { compare : (a: FaecherListeEintrag, b: FaecherListeEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Das Filter-Attribut auf nur sichtbare Kurse
	 */
	private _filterNurSichtbar : boolean = true;

	/**
	 *  Trigger, wenn eine Checkbox zum Hinzufügen von Schülern zu einem Kurs verwendet wird.
	 */
	protected readonly _eventSchuelerAuswahlChanged : Runnable = { run : () => {
		// empty block
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Kursauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param kurse         die Liste der Kurse
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 * @param faecher       die Liste der Fächer
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, kurse : List<KursDaten>, schueler : List<SchuelerListeEintrag>, jahrgaenge : List<JahrgangsDaten>, lehrer : List<LehrerListeEintrag>, faecher : List<FaecherListeEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, kurse, KursUtils.comparator, KursListeManager._kursToId, KursListeManager._kursToId, Arrays.asList(new Pair("idJahrgaenge", true), new Pair("kuerzel", true)));
		this.schuelerstatus = new AttributMitAuswahl(Arrays.asList(...SchuelerStatus.values()), this._schuelerstatusToId, KursListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl(schueler, KursListeManager._schuelerToId, SchuelerUtils.comparator, this._eventHandlerFilterChanged);
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, KursListeManager._jahrgangToId, JahrgaengeListeManager.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(lehrer, KursListeManager._lehrerToId, LehrerUtils.comparatorKuerzel, this._eventHandlerFilterChanged);
		this.faecher = new AttributMitAuswahl(faecher, KursListeManager._fachToId, KursListeManager.comparatorFaecherListe, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(this.getSchuljahr(), schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, this._schulgliederungToId, KursListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.initKurse();
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
	}

	private initKurse() : void {
		for (const k of this.liste.list()) {
			this._mapKursIstSichtbar.put(k.istSichtbar, k.id, k);
			for (const idJahrgang of k.idJahrgaenge) {
				this._mapKursInJahrgang.put(idJahrgang, k.id, k);
				const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(idJahrgang);
				if (j.kuerzelSchulgliederung !== null) {
					const gliederung : Schulgliederung | null = this.schulgliederungen.get(j.kuerzelSchulgliederung);
					if (gliederung !== null)
						this._mapKursInSchulgliederung.put(j.kuerzelSchulgliederung, k.id, k);
				}
			}
			for (const s of k.schueler)
				this._mapKursHatSchueler.put(s.id, k.id, k);
			if (k.lehrer !== null)
				this._mapLehrerInKurs.put(k.lehrer, k.id, k);
			this._mapKursHatFach.put(k.idFach, k.id, k);
			for (const idJahrgang of k.idJahrgaenge)
				this._mapKursByKuerzelAndJahrgang.put(k.kuerzel, idJahrgang, k);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : KursDaten, daten : KursDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		this._filteredSchuelerListe = null;
		return updateEintrag;
	}

	/**
	 * Gibt die Schulgliederungen für den aktuell ausgewählten Kurs zurück.
	 *
	 * @return die Schulgliederungen des Kurses
	 */
	public datenGetSchulgliederung() : List<Schulgliederung> {
		const result : List<Schulgliederung> = new ArrayList<Schulgliederung>();
		if ((this._daten === null) || (this._daten.idJahrgaenge.isEmpty()))
			return result;
		for (const idJahrgang of this._daten.idJahrgaenge) {
			const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(idJahrgang);
			if (j.kuerzelSchulgliederung !== null)
				result.add(this.schulgliederungen.get(j.kuerzelSchulgliederung));
		}
		return result;
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
	 * Vergleicht zwei Kurslisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : KursDaten, b : KursDaten) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("kuerzel", (field))) {
				cmp = KursUtils.comparator.compare(a, b);
			} else
				if (JavaObject.equalsTranspiler("lehrer", (field))) {
					if ((a.lehrer === null) && (b.lehrer === null)) {
						cmp = 0;
					} else
						if (a.lehrer === null) {
							cmp = -1;
						} else
							if (b.lehrer === null) {
								cmp = 1;
							} else {
								const la : LehrerListeEintrag | null = this.lehrer.get(a.lehrer);
								const lb : LehrerListeEintrag | null = this.lehrer.get(b.lehrer);
								if ((la === null) && (lb === null))
									cmp = 0;
								else
									if (la === null)
										cmp = -1;
									else
										if (lb === null)
											cmp = 1;
										else
											cmp = LehrerUtils.comparator.compare(la, lb);
							}
				} else
					if (JavaObject.equalsTranspiler("idJahrgaenge", (field))) {
						if ((a.idJahrgaenge.isEmpty()) && (b.idJahrgaenge.isEmpty())) {
							cmp = 0;
						} else
							if (a.idJahrgaenge.isEmpty()) {
								cmp = -1;
							} else
								if (b.idJahrgaenge.isEmpty()) {
									cmp = 1;
								} else {
									const ja : JahrgangsDaten | null = this.jahrgaenge.get(a.idJahrgaenge.get(0));
									const jb : JahrgangsDaten | null = this.jahrgaenge.get(b.idJahrgaenge.get(0));
									if ((ja === null) && (jb === null))
										cmp = 0;
									else
										if (ja === null)
											cmp = -1;
										else
											if (jb === null)
												cmp = 1;
											else
												cmp = JahrgaengeListeManager.comparator.compare(ja, jb);
								}
					} else
						if (JavaObject.equalsTranspiler("schueler", (field))) {
							cmp = JavaInteger.compare(a.schueler.size(), b.schueler.size());
						} else
							throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag : KursDaten) : boolean {
		this._filteredSchuelerListe = null;
		if (this._filterNurSichtbar && !eintrag.istSichtbar)
			return false;
		if (this.faecher.auswahlExists() && !this.faecher.auswahlHasKey(eintrag.idFach))
			return false;
		if (this.jahrgaenge.auswahlExists()) {
			let hatEinenJahrgang : boolean = false;
			for (const idJahrgang of eintrag.idJahrgaenge)
				if (this.jahrgaenge.auswahlHasKey(idJahrgang))
					hatEinenJahrgang = true;
			if (!hatEinenJahrgang)
				return false;
		}
		if (this.lehrer.auswahlExists()) {
			const hatEinenLehrer : boolean = (eintrag.lehrer !== null) && (this.lehrer.auswahlHasKey(eintrag.lehrer));
			if (!hatEinenLehrer)
				return false;
		}
		if (this.schueler.auswahlExists()) {
			let hatEinenSchueler : boolean = false;
			for (const s of eintrag.schueler)
				if (this.schueler.auswahlHasKey(s.id))
					hatEinenSchueler = true;
			if (!hatEinenSchueler)
				return false;
		}
		if (this.schulgliederungen.auswahlExists()) {
			if (eintrag.idJahrgaenge.isEmpty())
				return false;
			let hatEineSchulglierung : boolean = false;
			for (const idJahrgang of eintrag.idJahrgaenge) {
				const j : JahrgangsDaten | null = this.jahrgaenge.getOrException(idJahrgang);
				if ((j.kuerzelSchulgliederung !== null) && (this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung))) {
					hatEineSchulglierung = true;
					break;
				}
			}
			if (!hatEineSchulglierung)
				return false;
		}
		return true;
	}

	/**
	 * Gibt die Schülerliste des aktuell ausgewählten Kurses zurück. Ist
	 * kein Kurs ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public getSchuelerListe() : List<Schueler> {
		if (this._filteredSchuelerListe === null) {
			this._filteredSchuelerListe = new ArrayList();
			if (this._daten !== null)
				for (const s of this._daten.schueler)
					if (!this.schuelerstatus.auswahlExists() || this.schuelerstatus.auswahlHasKey(s.status))
						this._filteredSchuelerListe.add(s);
		}
		return this._filteredSchuelerListe;
	}

	/**
	 * Gibt die Kursdaten anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel      das Kürzel
	 * @param idJahrgang   die ID des Jahrgangs
	 *
	 * @return die Kursdaten oder null
	 */
	public getByKuerzelAndJahrgangOrNull(kuerzel : string, idJahrgang : number) : KursDaten | null {
		return this._mapKursByKuerzelAndJahrgang.getOrNull(kuerzel, idJahrgang);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link KursListeManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager : KursListeManager) : void {
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
		this.faecher.setAuswahl(srcManager.faecher);
		this.schueler.setAuswahl(srcManager.schueler);
		this.setFilterNurSichtbar(srcManager._filterNurSichtbar);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.kurse.KursListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.kurse.KursListeManager'].includes(name);
	}

	public static class = new Class<KursListeManager>('de.svws_nrw.core.utils.kurse.KursListeManager');

}

export function cast_de_svws_nrw_core_utils_kurse_KursListeManager(obj : unknown) : KursListeManager {
	return obj as KursListeManager;
}
