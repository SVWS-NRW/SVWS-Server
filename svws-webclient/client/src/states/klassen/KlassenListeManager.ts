import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KlassenDatenMinimal } from "@core/asd/data/klassen/KlassenDatenMinimal";
import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
import type { SchulgliederungKatalogEintrag } from "@core/asd/data/schule/SchulgliederungKatalogEintrag";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { SchuelerUtils } from "@core/core/utils/schueler/SchuelerUtils";
import { IllegalArgumentException } from "@core/java/lang/IllegalArgumentException";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaObject } from "@core/java/lang/JavaObject";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { Comparator } from "@core/java/util/Comparator";
import { HashMap } from "@core/java/util/HashMap";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";
import { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

export interface KlassenLookups {
	schuljahresabschnitte: List<Schuljahresabschnitt>,
	klassenAktAbschnitt: List<KlassenListeEintrag>,
	klassenVorabschnitt: List<KlassenDatenMinimal>,
	klassenFolgeabschnitt: List<KlassenDatenMinimal>,
	schueler: List<SchuelerListeEintrag>,
	jahrgaenge: List<JahrgangsDaten>,
	lehrer: List<LehrerListeEintrag>
}

export class KlassenListeManager extends AuswahlManager<number, KlassenListeEintrag, KlassenDaten> {

	/**
	 * Ein Default-Comparator für den Vergleich von KlassenListeEinträgen.
	 */
	public static readonly comparator: Comparator<KlassenListeEintrag> = {
		compare: (a: KlassenListeEintrag, b: KlassenListeEintrag) => {
			if (a.kuerzel !== null && b.kuerzel !== null) {
				const cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(a.id, b.id);
		},
	};


	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klassenListeToId = (k: KlassenListeEintrag) => k.id;

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _klassenDatenToId = (k: KlassenDaten) => k.id;

	/**
	 * Sets mit Listen zur aktuellen Auswahl
	 */
	private readonly setKlassenIDsMitSchuelern: HashSet<number> = new HashSet<number>();

	/**
	 * Die ausgewählte Klassenleitung
	 */
	public auswahlKlassenLeitung: LehrerListeEintrag | null = null;

	/**
	 * Das Filter-Attribut für die Jahrgänge
	 */
	public readonly jahrgaenge: ListeMitAuswahl<number, JahrgangsDaten>;

	private static readonly _jahrgangToId = (jg: JahrgangsDaten) => jg.id;

	/**
	 * Das Filter-Attribut für die Lehrer
	 */
	public readonly lehrer: ListeMitAuswahl<number, LehrerListeEintrag>;

	private static readonly _lehrerToId = (l: LehrerListeEintrag) => l.id;

	/**
	 * Das Filter-Attribut für die Schüler
	 */
	public readonly schueler: ListeMitAuswahl<number, SchuelerListeEintrag>;

	private static readonly _schuelerToId = (s: SchuelerListeEintrag) => s.id;

	private _filteredSchuelerListe: List<Schueler> | null = null;

	/**
	 * Das Filter-Attribut für die Schulgliederungen
	 */
	public readonly schulgliederungen: ListeMitAuswahl<string, Schulgliederung>;

	private readonly _schulgliederungToId = (sg: Schulgliederung) => {
		const sglke: SchulgliederungKatalogEintrag | null = sg.daten(schuleStateImpl.schuljahr);
		if (sglke === null) {
			throw new IllegalArgumentException(JavaString.format("Die Schulgliederung %s ist in dem Schuljahr %d nicht gültig.", sg.name(), schuleStateImpl.schuljahr));
		}
		return sglke.kuerzel;
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
	 *  Trigger, wenn eine Checkbox zum Hinzufügen von Schülern zu einer Klasse verwendet wird.
	 */
	protected readonly _eventSchuelerAuswahlChanged = () => {
		// empty block
	};

	private readonly _klassenByIdVorabschnitt: JavaMap<number, KlassenDatenMinimal>;
	private readonly _klassenByIdFolgeAbschnitt: JavaMap<number, KlassenDatenMinimal>;


	public constructor(
		schuljahresabschnitt: number,
		schuljahresabschnittSchule: number,
		schulform: Schulform | null,
		lookups: KlassenLookups
	) {
		super(
			schuljahresabschnitt,
			schuljahresabschnittSchule,
			lookups.schuljahresabschnitte,
			schulform,
			lookups.klassenAktAbschnitt,
			KlassenListeManager.comparator,
			KlassenListeManager._klassenListeToId,
			KlassenListeManager._klassenDatenToId,
			[{ field: "klassen", ascending: true }, { field: "schueleranzahl", ascending: true }]
		);
		this.schuelerstatus = new ListeMitAuswahl(Arrays.asList(...SchuelerStatus.values()), this._schuelerstatusToId, KlassenListeManager._comparatorSchuelerStatus, this._eventHandlerFilterChanged);
		this.schueler = new ListeMitAuswahl(lookups.schueler, KlassenListeManager._schuelerToId, SchuelerUtils.comparator, this._eventSchuelerAuswahlChanged);
		this.jahrgaenge = new ListeMitAuswahl(lookups.jahrgaenge, KlassenListeManager._jahrgangToId, JahrgaengeListeManager.comparator, this._eventHandlerFilterChanged);
		this.lehrer = new ListeMitAuswahl(lookups.lehrer, KlassenListeManager._lehrerToId, LehrerUtils.comparator, this._eventHandlerFilterChanged);
		const gliederungen: List<Schulgliederung> = (schulform === null) ? Arrays.asList(...Schulgliederung.values()) : Schulgliederung.getBySchuljahrAndSchulform(schuleStateImpl.schuljahr, schulform);
		this.schulgliederungen = new ListeMitAuswahl(gliederungen, this._schulgliederungToId, KlassenListeManager._comparatorSchulgliederung, this._eventHandlerFilterChanged);
		this.auswahlKlassenLeitung = null;
		this.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		this.schuelerstatus.auswahlAdd(SchuelerStatus.WARTELISTE);
		this._klassenByIdVorabschnitt = this.mapKlassen(lookups.klassenVorabschnitt);
		this._klassenByIdFolgeAbschnitt = this.mapKlassen(lookups.klassenFolgeabschnitt);
	}

	private mapKlassen(klassen: List<KlassenDatenMinimal>): JavaMap<number, KlassenDatenMinimal> {
		const result: JavaMap<number, KlassenDatenMinimal> | null = new HashMap<number, KlassenDatenMinimal>();
		for (const v of klassen) {
			result.put(v.id, v);
		}
		return result;
	}

	protected onSetDaten(eintrag: KlassenListeEintrag, daten: KlassenDaten): boolean {
		let updateEintrag: boolean = false;
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
	public datenGetSchulgliederung(): Schulgliederung | null {
		if ((this._daten === null) || (this._daten.idJahrgang === null)) {
			return null;
		}
		const j: JahrgangsDaten | null = this.jahrgaenge.getOrException(this._daten.idJahrgang);
		return (j.idSchulgliederung === null) ? null : this.schulgliederungen.get(
			Schulgliederung.data().getEintragByID(j.idSchulgliederung)?.kuerzel ?? ''
		);
	}

	/**
	 * Vergleicht zwei Klassenlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: KlassenListeEintrag, b: KlassenListeEintrag): number {
		for (const { field, ascending } of this._order) {
			let cmp: number;

			if (field === "klassen") {
				cmp = KlassenListeManager.comparator.compare(a, b);
			} else if (field === "schueleranzahl") {
				cmp = JavaInteger.compare(a.anzahlZugeordneteSchueler, b.anzahlZugeordneteSchueler);
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

	protected onMehrfachauswahlChanged(): void {
		this.setKlassenIDsMitSchuelern.clear();
		for (const k of this.liste.auswahl()) {
			if (k.anzahlZugeordneteSchueler !== 0) {
				this.setKlassenIDsMitSchuelern.add(k.id);
			}
		}
	}

	protected checkFilter(eintrag: KlassenListeEintrag): boolean {
		this._filteredSchuelerListe = null;
		if (this.jahrgaenge.auswahlExists() && ((eintrag.idJahrgang === null) || (!this.jahrgaenge.auswahlHasKey(eintrag.idJahrgang)))) {
			return false;
		}
		if (this.lehrer.auswahlExists()) {
			let hatEinenLehrer: boolean = false;
			for (const idLehrer of eintrag.idsKlassenleitungen) {
				if (this.lehrer.auswahlHasKey(idLehrer)) {
					hatEinenLehrer = true;
				}
			}
			if (!hatEinenLehrer) {
				return false;
			}
		}
		if (this.schulgliederungen.auswahlExists()) {
			if (eintrag.idJahrgang === null) {
				return false;
			}
			const j: JahrgangsDaten | null = this.jahrgaenge.getOrException(eintrag.idJahrgang);
			if ((j.idSchulgliederung === null) || (!this.schulgliederungen.auswahlHasKey(
				Schulgliederung.data().getEintragByID(j.idSchulgliederung)?.kuerzel ?? ''
			))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gibt die Schülerliste der aktuelle ausgewählten Klasse zurück. Ist
	 * keine Klasse ausgewählt, so wird eine leere Liste zurückgegeben.
	 *
	 * @return die Liste der Schüler
	 */
	public getSchuelerListe(): List<Schueler> {
		if (this._filteredSchuelerListe === null) {
			this._filteredSchuelerListe = new ArrayList();
			if (this._daten !== null) {
				for (const s of this._daten.schueler) {
					if (!this.schuelerstatus.auswahlExists() || this.schuelerstatus.auswahlHasKey(s.status)) {
						this._filteredSchuelerListe.add(s);
					}
				}
			}
		}
		return this._filteredSchuelerListe;
	}

	/**
	 *Gibt das Set mit den KlassenIds zurück, die in der Auswahl sind und Schüler beinhalten
	 *
	 * @return Das Set mit IDs von Klassen, die Schüler haben
	 */
	public getKlassenIDsMitSchuelern(): JavaSet<number> {
		return this.setKlassenIDsMitSchuelern;
	}

	/**
	 * Gibt die ausgewählte Klassenleitung zurück
	 *
	 * @return die ausgewählte Klassenleitung
	 */
	public getAuswahlKlassenLeitung(): LehrerListeEintrag | null {
		return this.auswahlKlassenLeitung;
	}

	/**
	 * Setzt die angegebene Lehrkraft zur ausgewählten Klassenleitung
	 *
	 * @param klassenLeitung neue ausgewählte Klassenleitung
	 */
	public setAuswahlKlassenLeitung(klassenLeitung: LehrerListeEintrag | null): void {
		this.auswahlKlassenLeitung = klassenLeitung;
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
	public static updateReihenfolgeKlassenleitung(klassenleitungen: List<number>, lehrerId: number, erhoehe: boolean): boolean {
		if (klassenleitungen.size() === 1) {
			return false;
		}
		const posLehrer: number = klassenleitungen.indexOf(lehrerId);
		if (posLehrer < 0) {
			throw new DeveloperNotificationException("Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden.");
		}
		if (erhoehe) {
			if (posLehrer === 0) {
				return false;
			}
			const lehrerIdVorgaenger: number = klassenleitungen.get(posLehrer - 1).valueOf();
			klassenleitungen.set(posLehrer, lehrerIdVorgaenger);
			klassenleitungen.set(posLehrer - 1, lehrerId);
			return true;
		}
		if ((posLehrer + 1) >= klassenleitungen.size()) {
			return false;
		}
		const lehrerIdNachfolger: number = klassenleitungen.get(posLehrer + 1).valueOf();
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
	public validateKuerzel(kuerzel: string | null): boolean {
		if ((kuerzel === null) || JavaString.isBlank(kuerzel) || (kuerzel.trim().length > 15)) {
			return false;
		}
		for (const klasse of this.liste.list()) {
			if ((this.auswahlID() !== klasse.id) && JavaObject.equalsTranspiler(klasse.kuerzel, (kuerzel.trim()))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Die Beschreibung ist optional und darf maximal 150 Zeichen lang sein.
	 *
	 * @param beschreibung die Beschreibung der Klasse
	 *
	 * @return <code>true</code> wenn Beschreibung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public validateBeschreibung(beschreibung: string | null): boolean {
		if (beschreibung === null) {
			return true;
		}
		return beschreibung.trim().length <= 150;
	}

	/**
	 * Der Sortierungsindex darf nicht <code>null</code> sein und muss größer gleich 0 sein.
	 *
	 * @param sortierung der Sortierungsindex der Klasse
	 *
	 * @return <code>true</code> wenn Sortierung der Klasse gültig ist, ansonsten <code>false</code>
	 */
	public validateSortierung(sortierung: number | null): boolean {
		return (sortierung !== null) && (sortierung >= 0);
	}

	/**
	 * Methode übernimmt Filterinformationen aus dem übergebenen {@link KlassenListeManager}
	 *
	 * @param srcManager Manager, aus dem die Filterinformationen übernommen werden
	 */
	public useFilter(srcManager: KlassenListeManager): void {
		this.jahrgaenge.setAuswahl(srcManager.jahrgaenge);
		this.lehrer.setAuswahl(srcManager.lehrer);
		this.schulgliederungen.setAuswahl(srcManager.schulgliederungen);
	}


	get klassenByIdFolgeAbschnitt(): JavaMap<number, KlassenDatenMinimal> {
		return this._klassenByIdFolgeAbschnitt;
	}

	get klassenByIdVorabschnitt(): JavaMap<number, KlassenDatenMinimal> {
		return this._klassenByIdVorabschnitt;
	}
}

