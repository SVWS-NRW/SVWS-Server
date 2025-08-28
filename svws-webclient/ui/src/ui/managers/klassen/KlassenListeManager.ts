import { JavaObject } from '../../../../../core/src/java/lang/JavaObject';
import { HashMap2D } from '../../../../../core/src/core/adt/map/HashMap2D';
import type { KlassenDaten } from '../../../../../core/src/asd/data/klassen/KlassenDaten';
import type { SchuelerListeEintrag } from '../../../../../core/src/core/data/schueler/SchuelerListeEintrag';
import type { SchuelerStatusKatalogEintrag } from '../../../../../core/src/asd/data/schueler/SchuelerStatusKatalogEintrag';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import { HashMap } from '../../../../../core/src/java/util/HashMap';
import type { Schulform } from '../../../../../core/src/asd/types/schule/Schulform';
import { KlassenUtils } from '../../../../../core/src/core/utils/klassen/KlassenUtils';
import { SchuelerUtils } from '../../../../../core/src/core/utils/schueler/SchuelerUtils';
import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
import type { JahrgangsDaten } from '../../../../../core/src/core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../../../core/src/java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
import { SchuelerStatus } from '../../../../../core/src/asd/types/schueler/SchuelerStatus';
import type { Comparator } from '../../../../../core/src/java/util/Comparator';
import type { JavaFunction } from '../../../../../core/src/java/util/function/JavaFunction';
import type { LehrerListeEintrag } from '../../../../../core/src/core/data/lehrer/LehrerListeEintrag';
import { Schulgliederung } from '../../../../../core/src/asd/types/schule/Schulgliederung';
import type { SchulgliederungKatalogEintrag } from '../../../../../core/src/asd/data/schule/SchulgliederungKatalogEintrag';
import type { List } from '../../../../../core/src/java/util/List';
import { IllegalArgumentException } from '../../../../../core/src/java/lang/IllegalArgumentException';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { Pair } from '../../../../../core/src/asd/adt/Pair';
import { AttributMitAuswahl } from '../../../../../core/src/core/utils/AttributMitAuswahl';
import { JahrgaengeListeManager } from '../kataloge/jahrgaenge/JahrgaengeListeManager';
import { AuswahlManager } from '../../AuswahlManager';
import { JavaInteger } from '../../../../../core/src/java/lang/JavaInteger';
import { LehrerUtils } from '../../../../../core/src/core/utils/lehrer/LehrerUtils';
import type { Schueler } from '../../../../../core/src/asd/data/schueler/Schueler';
import type { Runnable } from '../../../../../core/src/java/lang/Runnable';
import { JavaLong } from '../../../../../core/src/java/lang/JavaLong';
import { Class } from '../../../../../core/src/java/lang/Class';
import { Arrays } from '../../../../../core/src/java/util/Arrays';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';

export class KlassenListeManager extends AuswahlManager<number, KlassenDaten, KlassenDaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klasseToId : JavaFunction<KlassenDaten, number> = { apply : (k: KlassenDaten) => k.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapKlasseInJahrgang : HashMap2D<number, number, KlassenDaten> = new HashMap2D<number, number, KlassenDaten>();

	private readonly _mapKlasseHatSchueler : HashMap2D<number, number, KlassenDaten> = new HashMap2D<number, number, KlassenDaten>();

	private readonly _mapKlassenlehrerInKlasse : HashMap2D<number, number, KlassenDaten> = new HashMap2D<number, number, KlassenDaten>();

	private readonly _mapKlasseInSchulgliederung : HashMap2D<string, number, KlassenDaten> = new HashMap2D<string, number, KlassenDaten>();

	private readonly _mapKlasseByKuerzel : HashMap<string, KlassenDaten> = new HashMap<string, KlassenDaten>();

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setKlassenIDsMitSchuelern : HashSet<number> = new HashSet<number>();

	/**
	 * Die ausgewählte Klassenleitung
	 */
	public auswahlKlassenLeitung : LehrerListeEintrag | null = null;

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
	 *  Trigger, wenn eine Checkbox zum Hinzufügen von Schülern zu einer Klasse verwendet wird.
	 */
	protected readonly _eventSchuelerAuswahlChanged : Runnable = { run : () => {
		// empty block
	} };


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    der Schuljahresabschnitt, auf den sich die Klassenauswahl bezieht
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform     die Schulform der Schule
	 * @param klassen       die Liste der Klassen
	 * @param schueler      die Liste der Schüler
	 * @param jahrgaenge    die Liste der Jahrgänge
	 * @param lehrer        die Liste der Lehrer
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, klassen : List<KlassenDaten>, schueler : List<SchuelerListeEintrag>, jahrgaenge : List<JahrgangsDaten>, lehrer : List<LehrerListeEintrag>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, klassen, KlassenUtils.comparator, KlassenListeManager._klasseToId, KlassenListeManager._klasseToId, Arrays.asList(new Pair("klassen", true), new Pair("schueleranzahl", true)));
		this.schuelerstatus = new AttributMitAuswahl(Arrays.asList(...SchuelerStatus.values()), this._schuelerstatusToId, KlassenListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.schueler = new AttributMitAuswahl(schueler, KlassenListeManager._schuelerToId, SchuelerUtils.comparator, this._eventSchuelerAuswahlChanged);
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, KlassenListeManager._jahrgangToId, JahrgaengeListeManager.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new AttributMitAuswahl(lehrer, KlassenListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(this.getSchuljahr(), schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, this._schulgliederungToId, KlassenListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.initKlassen();
		this.auswahlKlassenLeitung = null;
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.WARTELISTE);
	}

	private initKlassen() : void {
		for (const k of this.liste.list()) {
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

	protected onSetDaten(eintrag : KlassenDaten, daten : KlassenDaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (this.auswahlKlassenLeitung !== null) {
			this.auswahlKlassenLeitung = null;
			updateEintrag = true;
		}
		this._filteredSchuelerListe = null;
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
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
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

	protected onMehrfachauswahlChanged() : void {
		this.setKlassenIDsMitSchuelern.clear();
		for (const k of this.liste.auswahl())
			if (!k.schueler.isEmpty())
				this.setKlassenIDsMitSchuelern.add(k.id);
	}

	protected checkFilter(eintrag : KlassenDaten) : boolean {
		this._filteredSchuelerListe = null;
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
			if ((j.kuerzelSchulgliederung === null) || (!this.schulgliederungen.auswahlHasKey(j.kuerzelSchulgliederung)))
				return false;
		}
		return true;
	}

	/**
	 * Gibt die Schülerliste der aktuelle ausgewählten Klasse zurück. Ist
	 * keine Klasse ausgewählt, so wird eine leere Liste zurückgegeben.
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
	 *Gibt das Set mit den KlassenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Klassen, die Schüler haben
	 */
	public getKlassenIDsMitSchuelern() : JavaSet<number> {
		return this.setKlassenIDsMitSchuelern;
	}

	/**
	 * Gibt die ausgewählte Klassenleitung zurück
	 *
	 * @return die ausgewählte Klassenleitung
	 */
	public getAuswahlKlassenLeitung() : LehrerListeEintrag | null {
		return this.auswahlKlassenLeitung;
	}

	/**
	 * Setzt die angegebene Lehrkraft zur ausgewählten Klassenleitung
	 *
	 * @param klassenLeitung neue ausgewählte Klassenleitung
	 */
	public setAuswahlKlassenLeitung(klassenLeitung : LehrerListeEintrag | null) : void {
		this.auswahlKlassenLeitung = klassenLeitung;
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

	/**
	 * Erhöht, bzw. senkt die Position der Klassenleitung mit der angegebenen Lehrer-ID auf der lokalen Klassenleitungs-Liste.
	 * Dabei wird der Reihenfolgen-Wert zwischen dem nächstgrößeren (bzw. nächskleineren) Eintrag
	 * und dem angegebenen Eintrag getauscht.
	 *
	 * @param klassenleitungen   die Liste der Klassenleitungen
	 * @param lehrerId           Lehrer-ID der zu höher- bzw. tieferstellenden Klassenleitung
	 * @param erhoehe            true, falls die Klassenleitung eine höhere Position auf der Klassenleitungs-Liste haben soll,
	 *                           false, wenn sie eine tiefere Position auf der Klassenleitungs-Liste haben soll.
	 *
	 * @return true, falls Änderungen durchgeführt wurden und ansonsten false
	 *
	 * @throws DeveloperNotificationException wenn die Klassen-Daten oder die übergebene Lehrer-ID ungültig sind
	 */
	public static updateReihenfolgeKlassenleitung(klassenleitungen : List<number>, lehrerId : number, erhoehe : boolean) : boolean {
		if (klassenleitungen.size() === 1)
			return false;
		const posLehrer : number = klassenleitungen.indexOf(lehrerId);
		if (posLehrer < 0)
			throw new DeveloperNotificationException("Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden.")
		if (erhoehe) {
			if (posLehrer === 0)
				return false;
			const lehrerIdVorgaenger : number = klassenleitungen.get(posLehrer - 1).valueOf();
			klassenleitungen.set(posLehrer, lehrerIdVorgaenger);
			klassenleitungen.set(posLehrer - 1, lehrerId);
			return true;
		}
		if ((posLehrer + 1) >= klassenleitungen.size())
			return false;
		const lehrerIdNachfolger : number = klassenleitungen.get(posLehrer + 1).valueOf();
		klassenleitungen.set(posLehrer, lehrerIdNachfolger);
		klassenleitungen.set(posLehrer + 1, lehrerId);
		return true;
	}

	/**
	 * Wenn das Kürzel nicht leer, für den Schuljahresabschnitt einzigartig und zwischen 1 und 15 Zeichen lang ist,
	 * wird <code>true</code>, andernfalls <code>false</code> zurückgegeben.
	 *
	 * @param kuerzel das Kürzel der Klasse
	 *
	 * @return <code>true</code> wenn Kürzel der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public validateKuerzel(kuerzel : string | null) : boolean {
		if ((kuerzel === null) || JavaString.isBlank(kuerzel) || (kuerzel.trim().length > 15))
			return false;
		for (const klasse of this.liste.list())
			if ((this.auswahlID() !== klasse.id) && JavaObject.equalsTranspiler(klasse.kuerzel, (kuerzel.trim())))
				return false;
		return true;
	}

	/**
	 * Die Beschreibung ist optional und darf maximal 150 Zeichen lang sein.
	 *
	 * @param beschreibung die Beschreibung der Klasse
	 *
	 * @return <code>true</code> wenn Beschreibung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public validateBeschreibung(beschreibung : string | null) : boolean {
		if (beschreibung === null)
			return true;
		return beschreibung.trim().length <= 150;
	}

	/**
	 * Der Sortierungsindex darf nicht <code>null</code> sein und muss größer gleich 0 sein.
	 *
	 * @param sortierung der Sortierungsindex der Klasse
	 *
	 * @return <code>true</code> wenn Sortierung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public validateSortierung(sortierung : number | null) : boolean {
		return (sortierung !== null) && (sortierung >= 0);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link KlassenListeManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager : KlassenListeManager) : void {
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.klassen.KlassenListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.klassen.KlassenListeManager'].includes(name);
	}

	public static class = new Class<KlassenListeManager>('de.svws_nrw.core.utils.klassen.KlassenListeManager');

}

export function cast_de_svws_nrw_core_utils_klassen_KlassenListeManager(obj : unknown) : KlassenListeManager {
	return obj as KlassenListeManager;
}
