import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { Schulform } from '../../../core/types/schule/Schulform';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { SchuelerUtils } from '../../../core/utils/schueler/SchuelerUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchuljahresabschnittsUtils } from '../../../core/utils/schule/SchuljahresabschnittsUtils';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { SchuelerStatus } from '../../../core/types/SchuelerStatus';
import type { Comparator } from '../../../java/util/Comparator';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Pair } from '../../../core/adt/Pair';
import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { SchuelerStammdaten } from '../../../core/data/schueler/SchuelerStammdaten';
import { GostAbiturjahrUtils } from '../../../core/utils/gost/GostAbiturjahrUtils';
import { GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { JavaLong } from '../../../java/lang/JavaLong';
import { KursUtils } from '../../../core/utils/kurse/KursUtils';
import { Arrays } from '../../../java/util/Arrays';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class SchuelerListeManager extends AuswahlManager<number, SchuelerListeEintrag, SchuelerStammdaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _schuelerToId : JavaFunction<SchuelerListeEintrag, number> = { apply : (s: SchuelerListeEintrag) => s.id };

	private static readonly _stammdatenToId : JavaFunction<SchuelerStammdaten, number> = { apply : (s: SchuelerStammdaten) => s.id };

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapSchuelerMitStatus : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInJahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKlasse : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInKurs : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchuljahresabschnitt : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInAbiturjahrgang : HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D();

	private readonly _mapSchuelerInSchulgliederung : HashMap2D<string, number, SchuelerListeEintrag> = new HashMap2D();

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge : AttributMitAuswahl<number, JahrgangsListeEintrag>;

	private static readonly _jahrgangToId : JavaFunction<JahrgangsListeEintrag, number> = { apply : (jg: JahrgangsListeEintrag) => jg.id };

	/**
	 * Das Filter-Attribut für die Klassen
	 */
	public readonly klassen : AttributMitAuswahl<number, KlassenListeEintrag>;

	private static readonly _klasseToId : JavaFunction<KlassenListeEintrag, number> = { apply : (k: KlassenListeEintrag) => k.id };

	/**
	 * Das Filter-Attribut für die Kurse
	 */
	public readonly kurse : AttributMitAuswahl<number, KursListeEintrag>;

	private static readonly _kursToId : JavaFunction<KursListeEintrag, number> = { apply : (k: KursListeEintrag) => k.id };

	/**
	 * Das Filter-Attribut für die Schuljahresabschnitte - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly schuljahresabschnitte : AttributMitAuswahl<number, Schuljahresabschnitt>;

	private static readonly _schuljahresabschnittToId : JavaFunction<Schuljahresabschnitt, number> = { apply : (sja: Schuljahresabschnitt) => sja.id };

	/**
	 * Das Filter-Attribut für die Abiturjahrgänge - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly abiturjahrgaenge : AttributMitAuswahl<number, GostJahrgang>;

	private static readonly _abiturjahrgangToId : JavaFunction<GostJahrgang, number> = { apply : (a: GostJahrgang) => a.abiturjahr };

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
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform               die Schulform der Schule
	 * @param schueler                die Liste der Schüler
	 * @param jahrgaenge              die Liste des Jahrgangskatalogs
	 * @param klassen                 die Liste des Klassenkatalogs
	 * @param kurse                   die Liste des Kurskatalogs
	 * @param schuljahresabschnitte   die Liste der Schuljahresabschnitte
	 * @param abiturjahrgaenge        die Liste der Abiturjahrgänge
	 */
	public constructor(schulform : Schulform | null, schueler : List<SchuelerListeEintrag>, jahrgaenge : List<JahrgangsListeEintrag>, klassen : List<KlassenListeEintrag>, kurse : List<KursListeEintrag>, schuljahresabschnitte : List<Schuljahresabschnitt>, abiturjahrgaenge : List<GostJahrgang>) {
		super(schulform, schueler, SchuelerUtils.comparator, SchuelerListeManager._schuelerToId, SchuelerListeManager._stammdatenToId, Arrays.asList(new Pair("klassen", true), new Pair("nachname", true), new Pair("vorname", true)));
		this.jahrgaenge = new AttributMitAuswahl(jahrgaenge, SchuelerListeManager._jahrgangToId, JahrgangsUtils.comparator, this._eventHandlerFilterChanged);
		this.klassen = new AttributMitAuswahl(klassen, SchuelerListeManager._klasseToId, KlassenUtils.comparator, this._eventHandlerFilterChanged);
		this.kurse = new AttributMitAuswahl(kurse, SchuelerListeManager._kursToId, KursUtils.comparator, this._eventHandlerFilterChanged);
		this.schuljahresabschnitte = new AttributMitAuswahl(schuljahresabschnitte, SchuelerListeManager._schuljahresabschnittToId, SchuljahresabschnittsUtils.comparator, this._eventHandlerFilterChanged);
		this.abiturjahrgaenge = new AttributMitAuswahl(abiturjahrgaenge, SchuelerListeManager._abiturjahrgangToId, GostAbiturjahrUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen : List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.get(schulform);
		this.schulgliederungen = new AttributMitAuswahl(gliederungen, SchuelerListeManager._schulgliederungToId, SchuelerListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.schuelerstatus = new AttributMitAuswahl(Arrays.asList(...SchuelerStatus.values()), SchuelerListeManager._schuelerstatusToId, SchuelerListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.initSchueler();
	}

	private initSchueler() : void {
		for (const s of this.liste.list()) {
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			if (s.idJahrgang >= 0)
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			if (s.idKlasse >= 0)
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			for (const idKurs of s.kurse)
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			if (s.idSchuljahresabschnitt >= 0)
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			if (s.abiturjahrgang !== null)
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			if (!JavaString.isBlank(s.schulgliederung))
				this._mapSchuelerInSchulgliederung.put(s.schulgliederung, s.id, s);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : SchuelerListeEintrag, daten : SchuelerStammdaten) : boolean {
		let updateEintrag : boolean = false;
		if (!JavaObject.equalsTranspiler(daten.vorname, (eintrag.vorname))) {
			eintrag.vorname = daten.vorname;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.nachname, (eintrag.nachname))) {
			eintrag.nachname = daten.nachname;
			updateEintrag = true;
		}
		if (daten.status !== eintrag.status) {
			eintrag.status = daten.status;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Aktualisiert die Klassen-ID bei dem Schüler
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @throws DeveloperNotificationException   falls kein Schüler ausgewählt ist oder die Klassen-ID nicht zulässig ist
	 */
	public updateKlassenID(idKlasse : number | null) : void {
		if (this._daten === null)
			throw new DeveloperNotificationException(JavaString.format("Für das Setzen der Klassen-ID %d muss ein Schüler ausgewählt sein.", idKlasse))
		if ((idKlasse !== null) && (idKlasse >= 0) && (!this.klassen.has(idKlasse)))
			throw new DeveloperNotificationException(JavaString.format("Die Klassen-ID %d muss zu dem aktuell ausgewählten Schuljahresabschnitt passen.", idKlasse))
		const eintrag : SchuelerListeEintrag = this.liste.getOrException(this._daten.id);
		eintrag.idKlasse = ((idKlasse === null) || (idKlasse < 0)) ? -1 : idKlasse;
		this.orderSet(this.orderGet());
	}

	/**
	 * Vergleicht zwei Schülerlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	private compare(a : SchuelerListeEintrag, b : SchuelerListeEintrag) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number = 0;
			if (JavaObject.equalsTranspiler("klassen", (field))) {
				const aKlasse : KlassenListeEintrag | null = this.klassen.get(a.idKlasse);
				const bKlasse : KlassenListeEintrag | null = this.klassen.get(b.idKlasse);
				if ((aKlasse === null) && (bKlasse === null)) {
					cmp = 0;
				} else
					if (aKlasse === null) {
						cmp = -1;
					} else
						if (bKlasse === null) {
							cmp = 1;
						} else {
							cmp = KlassenUtils.comparator.compare(aKlasse, bKlasse);
						}
			} else
				if (JavaObject.equalsTranspiler("nachname", (field))) {
					cmp = JavaString.compareTo(a.nachname, b.nachname);
				} else
					if (JavaObject.equalsTranspiler("vorname", (field))) {
						cmp = JavaString.compareTo(a.vorname, b.vorname);
					} else
						throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	/**
	 * Gibt eine gefilterte Liste der Schüler zurück. Als Filter werden dabei
	 * die Jahrgänge, die Klassen, die Kurse, die Schulgliederungen und der Schülerstatus
	 * beachtet.
	 *
	 * @return die gefilterte Liste
	 */
	protected onFilter() : List<SchuelerListeEintrag> {
		const tmpList : List<SchuelerListeEintrag> = new ArrayList();
		for (const eintrag of this.liste.list()) {
			if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang < 0) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang))))
				continue;
			if (this.klassen.auswahlExists() && ((eintrag.idKlasse < 0) || (!this.klassen.auswahlHasKey(eintrag.idKlasse))))
				continue;
			if (this.kurse.auswahlExists()) {
				let hatEinenKurs : boolean = false;
				for (const idKurs of eintrag.kurse)
					if (this.kurse.auswahlHasKey(idKurs))
						hatEinenKurs = true;
				if (!hatEinenKurs)
					continue;
			}
			if (this.schulgliederungen.auswahlExists() && ((JavaString.isBlank(eintrag.schulgliederung)) || (!this.schulgliederungen.auswahlHasKey(eintrag.schulgliederung))))
				continue;
			if (this.schuelerstatus.auswahlExists() && (!this.schuelerstatus.auswahlHasKey(eintrag.status)))
				continue;
			tmpList.add(eintrag);
		}
		const comparator : Comparator<SchuelerListeEintrag> = { compare : (a: SchuelerListeEintrag, b: SchuelerListeEintrag) => this.compare(a, b) };
		tmpList.sort(comparator);
		return tmpList;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerListeManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.schueler.SchuelerListeManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerListeManager(obj : unknown) : SchuelerListeManager {
	return obj as SchuelerListeManager;
}
