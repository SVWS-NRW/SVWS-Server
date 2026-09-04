import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import { HashMap2D } from "@core/core/adt/map/HashMap2D";
import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListe } from "@core/core/data/schueler/SchuelerListe";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { GostAbiturjahrUtils } from "@core/core/utils/gost/GostAbiturjahrUtils";
import { KlassenUtils } from "@core/core/utils/klassen/KlassenUtils";
import { KursUtils } from "@core/core/utils/kurse/KursUtils";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { SchuelerUtils } from "@core/core/utils/schueler/SchuelerUtils";
import { Class } from "@core/java/lang/Class";
import { IllegalArgumentException } from "@core/java/lang/IllegalArgumentException";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaObject } from "@core/java/lang/JavaObject";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { Comparator } from "@core/java/util/Comparator";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

export class SchuelerListeManager extends AuswahlManager<number, SchuelerListeEintrag, SchuelerStammdaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _schuelerToId = (s: SchuelerListeEintrag) => s.id;

	private static readonly _stammdatenToId = (s: SchuelerStammdaten) => s.id;

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapSchuelerMitStatus: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInJahrgang: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInKlasse: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInKurs: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInSchuljahresabschnitt: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInAbiturjahrgang: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapSchuelerInSchulgliederung: HashMap2D<number, number, SchuelerListeEintrag> = new HashMap2D<number, number, SchuelerListeEintrag>();

	private readonly _mapKlassenlehrerInSchueler: HashMap2D<number, number, LehrerListeEintrag> = new HashMap2D<number, number, LehrerListeEintrag>();

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge: ListeMitAuswahl<number, JahrgangsDaten>;

	private static readonly _jahrgangToId = (jg: JahrgangsDaten) => jg.id;

	/**
	 * Das Filter-Attribut für die Klassen
	 */
	public readonly klassen: ListeMitAuswahl<number, KlassenDaten>;

	private static readonly _klasseToId = (k: KlassenDaten) => k.id;

	private readonly _mapKlassenAlle: JavaMap<number, KlassenDaten> = new HashMap<number, KlassenDaten>();

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer: ListeMitAuswahl<number, LehrerListeEintrag>;

	private static readonly _lehrerToId = (l: LehrerListeEintrag) => l.id;

	/**
	 * Das Filter-Attribut für die Kurse
	 */
	public readonly kurse: ListeMitAuswahl<number, KursDaten>;

	private static readonly _kursToId = (k: KursDaten) => k.id;

	/**
	 * Das Filter-Attribut für die Abiturjahrgänge - die Filterfunktion wird zur Zeit noch nicht genutzt
	 */
	public readonly abiturjahrgaenge: ListeMitAuswahl<number, GostJahrgang>;

	private static readonly _abiturjahrgangToId = (a: GostJahrgang) => a.abiturjahr;

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen: ListeMitAuswahl<number, Schulgliederung>;

	private readonly _schulgliederungToId = (sg: Schulgliederung) => {
		const sglke: SchulgliederungKatalogEintrag | null = sg.daten(schuleStateImpl.schuljahr);
		if (sglke === null) {
			throw new IllegalArgumentException(JavaString.format("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.", sg.name(), schuleStateImpl.schuljahr));
		}
		return sglke.id;
	};

	private static readonly _comparatorSchulgliederung: Comparator<Schulgliederung> = { compare: (a: Schulgliederung, b: Schulgliederung) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut für den Schüler-Status
	 */
	public readonly schuelerstatus: ListeMitAuswahl<number, SchuelerStatus>;

	private readonly _schuelerstatusToId = (s: SchuelerStatus) => {
		const sske: SchuelerStatusKatalogEintrag | null = s.daten(schuleStateImpl.schuljahr);
		if (sske === null) {
			throw new IllegalArgumentException(JavaString.format("Der Schülerstatus %s ist in dem Schuljahr %d nicht gültig.", s.name(), schuleStateImpl.schuljahr));
		}
		return JavaInteger.parseInt(sske.kuerzel);
	};

	private static readonly _comparatorSchuelerStatus: Comparator<SchuelerStatus> = { compare: (a: SchuelerStatus, b: SchuelerStatus) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut auf Schüler mit einem Lernabschnitt in dem Schuljahresabschnitt
	 */
	private _filterNurMitLernabschitt: boolean = true;


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schulform                    die Schulform der Schule
	 * @param daten                        die Informationen zur Schüler-Auswahlliste
	 * @param lehrer        			   die Liste der Lehrer
	 * @param schuljahresabschnitte        die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule   der Schuljahresabschnitt, in dem sich die Schule aktuell befindet
	 */
	public constructor(schulform: Schulform | null, daten: SchuelerListe, lehrer: List<LehrerListeEintrag>, schuljahresabschnitte: List<Schuljahresabschnitt>, schuljahresabschnittSchule: number) {
		super(daten.idSchuljahresabschnitt,
			schuljahresabschnittSchule,
			schuljahresabschnitte,
			schulform,
			daten.schueler,
			SchuelerUtils.comparator,
			SchuelerListeManager._schuelerToId,
			SchuelerListeManager._stammdatenToId,
			[{ field: "klassen", ascending: true }, { field: "nachname", ascending: true }]
		);
		const aktuelleKlassen: List<KlassenDaten> = new ArrayList<KlassenDaten>();
		for (const klasse of daten.klassen) {
			this._mapKlassenAlle.put(klasse.id, klasse);
			if (klasse.idSchuljahresabschnitt === daten.idSchuljahresabschnitt) {
				aktuelleKlassen.add(klasse);
			}
		}
		this.klassen = new ListeMitAuswahl(aktuelleKlassen, SchuelerListeManager._klasseToId, KlassenUtils.comparator, this._eventHandlerFilterChanged);
		this.jahrgaenge = new ListeMitAuswahl(daten.jahrgaenge, SchuelerListeManager._jahrgangToId, JahrgaengeListeManager.comparator, this._eventHandlerFilterChanged);
		this.kurse = new ListeMitAuswahl(daten.kurse, SchuelerListeManager._kursToId, KursUtils.comparator, this._eventHandlerFilterChanged);
		this.abiturjahrgaenge = new ListeMitAuswahl(daten.jahrgaengeGost, SchuelerListeManager._abiturjahrgangToId, GostAbiturjahrUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen: List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(schuleStateImpl.schuljahr, schulform);
		this.schulgliederungen = new ListeMitAuswahl(gliederungen, this._schulgliederungToId, SchuelerListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.schuelerstatus = new ListeMitAuswahl(Arrays.asList(...SchuelerStatus.values()), this._schuelerstatusToId, SchuelerListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.lehrer = new ListeMitAuswahl(lehrer, SchuelerListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		this.setFilterAuswahlPermitted(false);
		this.initSchueler();
	}

	private initSchueler(): void {
		for (const s of this.liste.list()) {
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			const klasse: KlassenDaten | null = this._mapKlassenAlle.get(s.idKlasse);
			if (s.idJahrgang >= 0) {
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			}
			if (s.idKlasse >= 0) {
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			}
			for (const idKurs of s.kurse) {
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			}
			if (s.idSchuljahresabschnitt >= 0) {
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			}
			if (s.abiturjahrgang !== null) {
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			}
			if (s.idSchulgliederung >= 0) {
				this._mapSchuelerInSchulgliederung.put(s.idSchulgliederung, s.id, s);
			}
			if (klasse !== null) {
				for (const idKlassenlehrer of klasse.klassenLeitungen) {
					this._mapKlassenlehrerInSchueler.put(s.id, idKlassenlehrer, this.lehrer.getOrException(idKlassenlehrer));
				}
			}
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag: SchuelerListeEintrag, daten: SchuelerStammdaten): boolean {
		let updateEintrag: boolean = false;
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
			this.schuelerstatus.auswahlAdd(SchuelerStatus.data().getWertByID(eintrag.status));
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die aktuelle Filtereinstellung auf Schüler mit Lernabschnitt
	 * im ausgewählten Schuljahresabschnitt zurück.
	 *
	 * @return true, wenn nur Schüler mit Lernabschnitt angezeigt werden und ansonsten false
	 */
	public filterNurMitLernabschitt(): boolean {
		return this._filterNurMitLernabschitt;
	}

	/**
	 * Setzt die Filtereinstellung auf Schüler mit Lernabschnitt im ausgewählten
	 * Schuljahresabschnitt.
	 *
	 * @param value   true, wenn der Filter aktiviert werden soll, und ansonsten false
	 */
	public setFilterNurMitLernabschitt(value: boolean): void {
		this._filterNurMitLernabschitt = value;
		this._eventHandlerFilterChanged();
	}

	/**
	 * Aktualisiert die Klassen-ID bei dem Schüler
	 *
	 * @param idKlasse   die ID der Klasse
	 *
	 * @throws DeveloperNotificationException   falls kein Schüler ausgewählt ist oder die Klassen-ID nicht zulässig ist
	 */
	public updateKlassenID(idKlasse: number | null): void {
		if (this._daten === null) {
			throw new DeveloperNotificationException(JavaString.format("Für das Setzen der Klassen-ID %d muss ein Schüler ausgewählt sein.", idKlasse));
		}
		if ((idKlasse !== null) && (idKlasse >= 0) && (!this.klassen.has(idKlasse))) {
			throw new DeveloperNotificationException(JavaString.format("Die Klassen-ID %d muss zu dem aktuell ausgewählten Schuljahresabschnitt passen.", idKlasse));
		}
		const eintrag: SchuelerListeEintrag = this.liste.getOrException(this._daten.id);
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
	protected compareAuswahl(a: SchuelerListeEintrag, b: SchuelerListeEintrag): number {
		for (const { field, ascending } of this._order) {
			let cmp: number;

			if (field === "klassen") {
				const aKlasse: KlassenDaten | null = this.klasseGetOrNull(a.idKlasse);
				const bKlasse: KlassenDaten | null = this.klasseGetOrNull(b.idKlasse);
				if ((aKlasse === null) && (bKlasse === null)) {
					cmp = 0;
				} else if (aKlasse === null) {
					cmp = -1;
				} else if (bKlasse === null) {
					cmp = 1;
				} else {
					cmp = KlassenUtils.comparator.compare(aKlasse, bKlasse);
				}
			} else if (field === "nachname") {
				cmp = JavaString.compareTo(a.nachname, b.nachname);
			} else if (field === "vorname") {
				cmp = JavaString.compareTo(a.vorname, b.vorname);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			}

			if (cmp === 0) {
				continue;
			}

			return ascending ? cmp : -cmp;
		}

		return JavaLong.compare(a.id, b.id);
	}

	protected checkFilter(eintrag: SchuelerListeEintrag): boolean {
		if (this._filterNurMitLernabschitt && (!abschnittStateImpl.istSchuljahresabschnittAktuell()) && (eintrag.idSchuljahresabschnitt !== this._schuljahresabschnitt)) {
			return false;
		}
		if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang < 0) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang)))) {
			return false;
		}
		if (this.klassen.auswahlExists() && ((eintrag.idKlasse < 0) || (eintrag.idSchuljahresabschnitt !== this._schuljahresabschnitt) || (!this.klassen.auswahlHasKey(eintrag.idKlasse)))) {
			return false;
		}
		if (this.kurse.auswahlExists()) {
			let hatEinenKurs: boolean = false;
			for (const idKurs of eintrag.kurse) {
				if (this.kurse.auswahlHasKey(idKurs)) {
					hatEinenKurs = true;
				}
			}
			if (!hatEinenKurs) {
				return false;
			}
		}
		if (this.schulgliederungen.auswahlExists() && ((eintrag.idSchulgliederung < 0) || (!this.schulgliederungen.auswahlHasKey(eintrag.idSchulgliederung)))) {
			return false;
		}
		if (this.schuelerstatus.auswahlExists() && (abschnittStateImpl.istSchuljahresabschnittAktuell()) && (!this.schuelerstatus.auswahlHasKey(eintrag.status))) {
			return false;
		}
		return true;
	}

	/**
	 * Gibt für die angegebene Klassen-ID die Daten zur Klasse zurück.
	 * Sollte die ID ungültig sein, so wird null zurückgegeben.
	 *
	 * @param idKlasse    die ID der Klasse
	 *
	 * @return die Daten der Klasse
	 */
	public klasseGetOrNull(idKlasse: number): KlassenDaten | null {
		return this._mapKlassenAlle.get(idKlasse);
	}

	/**
	 * Gibt zurück, ob der Schüler mit der angegebenen ID einen Lernabschnitt in
	 * dem Schuljahresabschnitt dieser Auswahl hat.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return true, falls ein Lernabschnitt vorhanden ist und ansonsten false
	 */
	public schuelerIstImSchuljahresabschnitt(idSchueler: number): boolean {
		const schueler: SchuelerListeEintrag | null = this.liste.get(idSchueler);
		return (schueler !== null) && (schueler.idSchuljahresabschnittSchueler === this._schuljahresabschnitt);
	}

	/**
	 * Gibt den Schuljahresabschnitt des Schülers als String zurück.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return der Schuljahresabschnitt als String
	 */
	public schuelerSchuljahresabschnittAsString(idSchueler: number): string {
		const schueler: SchuelerListeEintrag | null = this.liste.get(idSchueler);
		if (schueler === null) {
			return "----.-";
		}
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.schuljahresabschnitte.get(schueler.idSchuljahresabschnittSchueler);
		if (schuljahresabschnitt === null) {
			return "----.-";
		}
		return schuljahresabschnitt.schuljahr + "." + schuljahresabschnitt.abschnitt;
	}

	/**
	 * Gibt das Schuljahr zurück, in dem der Schüler mit der angegebenen ID ist.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return das Schuljahr
	 */
	public schuelerGetSchuljahrByIdOrException(idSchueler: number): number {
		const schueler: SchuelerListeEintrag | null = this.liste.get(idSchueler);
		if (schueler === null) {
			throw new DeveloperNotificationException("");
		}
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.schuljahresabschnitte.get(schueler.idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException("");
		}
		return schuljahresabschnitt.schuljahr;
	}

	/**
	 * Gibt das Schuljahr zurück, in dem sich der ausgewählte Schüler befindet.
	 *
	 * @return das Schuljahr
	 */
	public schuelerGetSchuljahrOrException(): number {
		const schuljahresabschnitt: Schuljahresabschnitt | null = this.schuljahresabschnitte.get(this.auswahl().idSchuljahresabschnitt);
		if (schuljahresabschnitt === null) {
			throw new DeveloperNotificationException("Der Schuljahresabschnitt des Schülers fehlt.");
		}
		return schuljahresabschnitt.schuljahr;
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link SchuelerListeManager}
	 *
	 * @param srcManager Manager aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager: SchuelerListeManager): void {
		this.schuelerstatus.setAuswahl(srcManager.schuelerstatus);
		this.klassen.setAuswahl(srcManager.klassen);
		this.kurse.setAuswahl(srcManager.kurse);
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.schueler.SchuelerListeManager'].includes(name);
	}

	public static readonly class = new Class<SchuelerListeManager>('de.svws_nrw.core.utils.schueler.SchuelerListeManager');

}
