import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ArrayList } from '../../../java/util/ArrayList';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import type { Comparator } from '../../../java/util/Comparator';
import type { JavaFunction } from '../../../java/util/function/JavaFunction';
import { KAOAEbene4 } from '../../../core/types/kaoa/KAOAEbene4';
import { SchuelerKAoADaten } from '../../../core/data/schueler/SchuelerKAoADaten';
import { KAOAKategorie } from '../../../core/types/kaoa/KAOAKategorie';
import type { List } from '../../../java/util/List';
import { KAOAMerkmal } from '../../../core/types/kaoa/KAOAMerkmal';
import { Pair } from '../../../core/adt/Pair';
import { KAOAAnschlussoption } from '../../../core/types/kaoa/KAOAAnschlussoption';
import { AttributMitAuswahl } from '../../../core/utils/AttributMitAuswahl';
import { MapUtils } from '../../../core/utils/MapUtils';
import { AuswahlManager } from '../../../core/utils/AuswahlManager';
import { KAOABerufsfeld } from '../../../core/types/kaoa/KAOABerufsfeld';
import { SchuelerKAoAUtils } from '../../../core/utils/schueler/SchuelerKAoAUtils';
import { SchuelerLernabschnittsdaten } from '../../../core/data/schueler/SchuelerLernabschnittsdaten';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';
import { KAOAZusatzmerkmal } from '../../../core/types/kaoa/KAOAZusatzmerkmal';

export class SchuelerKAoAManager extends AuswahlManager<number, SchuelerKAoADaten, SchuelerKAoADaten> {

	/**
	 *  Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _kaoaToId : JavaFunction<SchuelerKAoADaten, number> = { apply : (kaoa: SchuelerKAoADaten) => kaoa.id };

	private static readonly _kategorieToId : JavaFunction<KAOAKategorie, number> = { apply : (kategorie: KAOAKategorie) => kategorie.daten.id };

	private static readonly _comparatorKategorie : Comparator<KAOAKategorie> = { compare : (a: KAOAKategorie, b: KAOAKategorie) => a.ordinal() - b.ordinal() };

	private static readonly _merkmalToId : JavaFunction<KAOAMerkmal, number> = { apply : (merkmal: KAOAMerkmal) => merkmal.daten.id };

	private static readonly _comparatorMerkmal : Comparator<KAOAMerkmal> = { compare : (a: KAOAMerkmal, b: KAOAMerkmal) => a.ordinal() - b.ordinal() };

	private static readonly _zusatzmerkmalToId : JavaFunction<KAOAZusatzmerkmal, number> = { apply : (zusatzmerkmal: KAOAZusatzmerkmal) => zusatzmerkmal.daten.id };

	private static readonly _comparatorZusatzmerkmal : Comparator<KAOAZusatzmerkmal> = { compare : (a: KAOAZusatzmerkmal, b: KAOAZusatzmerkmal) => a.ordinal() - b.ordinal() };

	private static readonly _anschlussoptionToId : JavaFunction<KAOAAnschlussoption, number> = { apply : (anschlussoption: KAOAAnschlussoption) => anschlussoption.daten.id };

	private static readonly _comparatorAnschlussoptionen : Comparator<KAOAAnschlussoption> = { compare : (a: KAOAAnschlussoption, b: KAOAAnschlussoption) => a.ordinal() - b.ordinal() };

	private static readonly _ebene4ToId : JavaFunction<KAOAEbene4, number> = { apply : (ebene4: KAOAEbene4) => ebene4.daten.id };

	private static readonly _comparatorEbene4 : Comparator<KAOAEbene4> = { compare : (a: KAOAEbene4, b: KAOAEbene4) => a.ordinal() - b.ordinal() };

	private static readonly _comparatorBerufsfelder : Comparator<KAOABerufsfeld> = { compare : (a: KAOABerufsfeld, b: KAOABerufsfeld) => a.ordinal() - b.ordinal() };

	/**
	 * Das Filter-Attribut für die Kategorien
	 */
	public readonly _kategorien : AttributMitAuswahl<number, KAOAKategorie>;

	/**
	 * Das Filter-Attribut für die Merkmale
	 */
	public readonly _merkmale : AttributMitAuswahl<number, KAOAMerkmal>;

	/**
	 * Das Filter-Attribut für die Zusatzmerkmale
	 */
	public readonly _zusatzmerkmale : AttributMitAuswahl<number, KAOAZusatzmerkmal>;

	/**
	 * Das Filter-Attribut für die Anschlussoptionen
	 */
	public readonly _anschlussoptionen : AttributMitAuswahl<number, KAOAAnschlussoption>;

	/**
	 * Das Filter-Attribut für die Ebene4
	 */
	public readonly _ebene4 : AttributMitAuswahl<number, KAOAEbene4>;

	private readonly _lernabschnittsdaten : List<SchuelerLernabschnittsdaten>;

	/**
	 * Das Filter-Attribut für die Berufsfelder
	 */
	private readonly _berufsfelder : List<KAOABerufsfeld>;

	/**
	 *  Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste
	 *  verwendet werden können
	 */
	private readonly _mapKAoABySchueler : JavaMap<number, List<SchuelerKAoADaten>> = new HashMap<number, List<SchuelerKAoADaten>>();

	private readonly _mapMerkmalByKategorie : JavaMap<string, List<KAOAMerkmal>> = new HashMap<string, List<KAOAMerkmal>>();

	private readonly _mapZusatzmerkmalByMerkmal : JavaMap<string, List<KAOAZusatzmerkmal>> = new HashMap<string, List<KAOAZusatzmerkmal>>();

	private readonly _mapAnschlussoptionByZusatzmerkmal : JavaMap<string, List<KAOAAnschlussoption>> = new HashMap<string, List<KAOAAnschlussoption>>();

	private readonly _mapEbene4ByZusatzmerkmal : JavaMap<string, List<KAOAEbene4>> = new HashMap<string, List<KAOAEbene4>>();


	/**
	 * Erstellt einen neuen Manager mit den übergebenen KAoA Daten
	 *
	 * @param schuljahresabschnitt       Der Schuljahresabschnitt, auf den sich dien KAoA-Daten bezieht
	 * @param schuljahresabschnittSchule Die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnitte      Der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                  Die Schulform der Schule
	 * @param schuelerKAoA               KAoA Daten des Schülers
	 * @param schuelerLernabschnitt      the schueler lernabschnitt
	 */
	public constructor(schuljahresabschnitt : number, schuljahresabschnittSchule : number, schuljahresabschnitte : List<Schuljahresabschnitt>, schulform : Schulform | null, schuelerKAoA : List<SchuelerKAoADaten>, schuelerLernabschnitt : List<SchuelerLernabschnittsdaten>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, schuelerKAoA, SchuelerKAoAUtils.comparator, SchuelerKAoAManager._kaoaToId, SchuelerKAoAManager._kaoaToId, Arrays.asList(new Pair("schuljahr", true), new Pair("kategorie", true)));
		this._lernabschnittsdaten = schuelerLernabschnitt;
		this._kategorien = new AttributMitAuswahl(Arrays.asList(...KAOAKategorie.values()), SchuelerKAoAManager._kategorieToId, SchuelerKAoAManager._comparatorKategorie, this._eventHandlerFilterChanged);
		this._merkmale = new AttributMitAuswahl(Arrays.asList(...KAOAMerkmal.values()), SchuelerKAoAManager._merkmalToId, SchuelerKAoAManager._comparatorMerkmal, this._eventHandlerFilterChanged);
		this._zusatzmerkmale = new AttributMitAuswahl(Arrays.asList(...KAOAZusatzmerkmal.values()), SchuelerKAoAManager._zusatzmerkmalToId, SchuelerKAoAManager._comparatorZusatzmerkmal, this._eventHandlerFilterChanged);
		this._anschlussoptionen = new AttributMitAuswahl(Arrays.asList(...KAOAAnschlussoption.values()), SchuelerKAoAManager._anschlussoptionToId, SchuelerKAoAManager._comparatorAnschlussoptionen, this._eventHandlerFilterChanged);
		this._berufsfelder = Arrays.asList(...KAOABerufsfeld.values());
		this._berufsfelder.sort(SchuelerKAoAManager._comparatorBerufsfelder);
		this._ebene4 = new AttributMitAuswahl(Arrays.asList(...KAOAEbene4.values()), SchuelerKAoAManager._ebene4ToId, SchuelerKAoAManager._comparatorEbene4, this._eventHandlerFilterChanged);
		this.initKAoA();
		this.initSchuelerKAoA();
	}

	private initKAoA() : void {
		for (const kategorie of this._kategorien.list()) {
			const merkmaleOfKategorie : ArrayList<KAOAMerkmal> = new ArrayList<KAOAMerkmal>();
			for (const merkmal of this._merkmale.list()) {
				if (JavaObject.equalsTranspiler(merkmal.daten.kategorie, (kategorie.daten.kuerzel)))
					merkmaleOfKategorie.add(merkmal);
				const zusatzmerkmaleOfMerkmal : ArrayList<KAOAZusatzmerkmal> = new ArrayList<KAOAZusatzmerkmal>();
				for (const zusatzmerkmal of this._zusatzmerkmale.list()) {
					if (JavaObject.equalsTranspiler(zusatzmerkmal.daten.merkmal, (merkmal.daten.kuerzel)))
						zusatzmerkmaleOfMerkmal.add(zusatzmerkmal);
					this.processZusatzmerkmal(zusatzmerkmal);
				}
				this._mapZusatzmerkmalByMerkmal.put(merkmal.daten.kuerzel, zusatzmerkmaleOfMerkmal);
			}
			this._mapMerkmalByKategorie.put(kategorie.daten.kuerzel, merkmaleOfKategorie);
		}
	}

	private processZusatzmerkmal(zusatzmerkmal : KAOAZusatzmerkmal) : void {
		const anschlussoptionOfZusatzmerkmal : ArrayList<KAOAAnschlussoption> = new ArrayList<KAOAAnschlussoption>();
		for (const anschlussoption of this._anschlussoptionen.list())
			for (const anzeigeMerkmal of anschlussoption.daten.anzeigeZusatzmerkmal)
				if (JavaObject.equalsTranspiler(anzeigeMerkmal, (zusatzmerkmal.daten.kuerzel)))
					anschlussoptionOfZusatzmerkmal.add(anschlussoption);
		this._mapAnschlussoptionByZusatzmerkmal.put(zusatzmerkmal.daten.kuerzel, anschlussoptionOfZusatzmerkmal);
		const ebene4OfZusatzmerkmal : ArrayList<KAOAEbene4> = new ArrayList<KAOAEbene4>();
		for (const ebene4 of this._ebene4.list())
			if (JavaObject.equalsTranspiler(ebene4.daten.zusatzmerkmal, (zusatzmerkmal.daten.kuerzel)))
				ebene4OfZusatzmerkmal.add(ebene4);
		this._mapEbene4ByZusatzmerkmal.put(zusatzmerkmal.daten.kuerzel, ebene4OfZusatzmerkmal);
	}

	private initSchuelerKAoA() : void {
		for (const lernabschnitt of this._lernabschnittsdaten) {
			const schuelerKAoA : ArrayList<SchuelerKAoADaten> = new ArrayList<SchuelerKAoADaten>();
			for (const kaoa of this.liste.list())
				if (lernabschnitt.id === kaoa.abschnitt)
					schuelerKAoA.add(kaoa);
			this._mapKAoABySchueler.put(lernabschnitt.schuelerID, schuelerKAoA);
		}
	}

	/**
	 * Prüft, ob der angegebene KAoA-Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag Der zu prüfende KAoA-Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	protected checkFilter(eintrag : SchuelerKAoADaten) : boolean {
		if (this._kategorien.auswahlExists() && !this._kategorien.auswahlHasKey(eintrag.kategorie))
			return false;
		if (this._merkmale.auswahlExists() && !this._merkmale.auswahlHasKey(eintrag.merkmal))
			return false;
		if ((eintrag.zusatzmerkmal !== null) && this._zusatzmerkmale.auswahlExists() && !this._zusatzmerkmale.auswahlHasKey(eintrag.zusatzmerkmal))
			return false;
		if ((eintrag.anschlussoption !== null) && this._anschlussoptionen.auswahlExists() && !this._anschlussoptionen.auswahlHasKey(eintrag.anschlussoption))
			return false;
		return ((eintrag.ebene4 !== null) && this._ebene4.auswahlExists() && !this._ebene4.auswahlHasKey(eintrag.ebene4));
	}

	/**
	 * Vergleicht zwei KAoA-Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a Der erste Eintrag
	 * @param b Der zweite Eintrag
	 *
	 * @return Das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a : SchuelerKAoADaten, b : SchuelerKAoADaten) : number {
		for (const criteria of this._order) {
			const field : string | null = criteria.a;
			const asc : boolean = (criteria.b === null) || criteria.b;
			let cmp : number;
			if (JavaObject.equalsTranspiler("schuljahr", (field)) || JavaObject.equalsTranspiler("kategorie", (field)))
				cmp = SchuelerKAoAUtils.comparator.compare(a, b);
			else
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom SchuelerKAoAManager nicht unterstützt.")
			if (cmp === 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return JavaLong.compare(a.id, b.id);
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag           Der Auswahl-Eintrag
	 * @param schuelerKAoADaten Das neue KAoA-Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag : SchuelerKAoADaten, schuelerKAoADaten : SchuelerKAoADaten) : boolean {
		let updateEintrag : boolean = false;
		if (schuelerKAoADaten.id !== eintrag.id) {
			eintrag.id = schuelerKAoADaten.id;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die KAoA Daten des Schülers zurück. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param schueler Der Schüler
	 *
	 * @return Die KAoA Daten des Schülers
	 */
	public getKAoABySchuelerID(schueler : SchuelerListeEintrag) : List<SchuelerKAoADaten> {
		return MapUtils.getOrCreateArrayList(this._mapKAoABySchueler, schueler.id);
	}

	/**
	 * Gibt alle KAoA Kategorien zurück.
	 *
	 * @return Die KAoA Kategorien
	 */
	public getKAOAKategorien() : List<KAOAKategorie> {
		return this._kategorien.list();
	}

	/**
	 * Gibt alle KAoA Merkmale zurück, die der angegebenen Kategorie zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param kategorie Die Kategorie
	 *
	 * @return Die KAoA Merkmale
	 */
	public getKAOAMerkmaleByKategorie(kategorie : KAOAKategorie) : List<KAOAMerkmal> {
		return MapUtils.getOrCreateArrayList(this._mapMerkmalByKategorie, kategorie.daten.kuerzel);
	}

	/**
	 * Gibt alle Zusatzmerkmale zurück, die dem angegeben Merkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param merkmal Das Merkmal
	 *
	 * @return Die Zusatzmerkmale
	 */
	public getKAOAZusatzmerkmaleByMerkmal(merkmal : KAOAMerkmal) : List<KAOAZusatzmerkmal> {
		return MapUtils.getOrCreateArrayList(this._mapZusatzmerkmalByMerkmal, merkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle KAoA Anschlussoptionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste
	 * zurückgegeben.
	 *
	 * @param zusatzmerkmal Das Zusatzmerkmal
	 *
	 * @return Die Anschlussoptionen
	 */
	public getKAOAAnschlussoptionByZusatzmerkmal(zusatzmerkmal : KAOAZusatzmerkmal) : List<KAOAAnschlussoption> {
		return MapUtils.getOrCreateArrayList(this._mapAnschlussoptionByZusatzmerkmal, zusatzmerkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle Ebene4 Optionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param zusatzmerkmal Das Zusatzmerkmal
	 *
	 * @return Die Ebene4 Optionen
	 */
	public getKAOAEbene4ByZusatzmerkmal(zusatzmerkmal : KAOAZusatzmerkmal) : List<KAOAEbene4> {
		return MapUtils.getOrCreateArrayList(this._mapEbene4ByZusatzmerkmal, zusatzmerkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle Berufsfelder zurück.
	 *
	 * @return Die Berufsfelder
	 */
	public getKAOABerufsfelder() : List<KAOABerufsfeld> {
		return this._berufsfelder;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerKAoAManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.schueler.SchuelerKAoAManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerKAoAManager(obj : unknown) : SchuelerKAoAManager {
	return obj as SchuelerKAoAManager;
}
