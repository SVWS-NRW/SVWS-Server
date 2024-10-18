import { JavaObject } from '../../../java/lang/JavaObject';
import { KlassenDaten } from '../../../core/data/klassen/KlassenDaten';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';
import { KursDaten } from '../../../core/data/kurse/KursDaten';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { FachDaten } from '../../../core/data/fach/FachDaten';
import { FoerderschwerpunktEintrag } from '../../../core/data/schule/FoerderschwerpunktEintrag';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import type { List } from '../../../java/util/List';
import { Fach } from '../../../asd/types/fach/Fach';
import { SchuelerLeistungsdaten } from '../../../core/data/schueler/SchuelerLeistungsdaten';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { Jahrgaenge } from '../../../asd/types/jahrgang/Jahrgaenge';
import { SchuelerLernabschnittsdaten } from '../../../core/data/schueler/SchuelerLernabschnittsdaten';
import { Note } from '../../../asd/types/Note';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Class } from '../../../java/lang/Class';
import { KursUtils } from '../../../core/utils/kurse/KursUtils';
import { Schuljahresabschnitt } from '../../../asd/data/schule/Schuljahresabschnitt';
import type { JavaMap } from '../../../java/util/JavaMap';

export class SchuelerLernabschnittManager extends JavaObject {

	private readonly _schulform : Schulform;

	private readonly _schueler : SchuelerListeEintrag;

	private readonly _lernabschnittsdaten : SchuelerLernabschnittsdaten;

	private readonly _schuljahresabschnitt : Schuljahresabschnitt;

	private readonly _mapLeistungById : JavaMap<number, SchuelerLeistungsdaten> = new HashMap<number, SchuelerLeistungsdaten>();

	private readonly _faecher : List<FachDaten> = new ArrayList<FachDaten>();

	private readonly _mapFachByID : JavaMap<number, FachDaten> = new HashMap<number, FachDaten>();

	private readonly _foerderschwerpunkte : List<FoerderschwerpunktEintrag> = new ArrayList<FoerderschwerpunktEintrag>();

	private readonly _mapFoerderschwerpunktByID : JavaMap<number, FoerderschwerpunktEintrag> = new HashMap<number, FoerderschwerpunktEintrag>();

	private readonly _jahrgaenge : List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>();

	private readonly _mapJahrgangByID : JavaMap<number, JahrgangsDaten> = new HashMap<number, JahrgangsDaten>();

	private readonly _klassen : List<KlassenDaten> = new ArrayList<KlassenDaten>();

	private readonly _mapKlasseByID : JavaMap<number, KlassenDaten> = new HashMap<number, KlassenDaten>();

	private readonly _kurse : List<KursDaten> = new ArrayList<KursDaten>();

	private readonly _mapKursByID : JavaMap<number, KursDaten> = new HashMap<number, KursDaten>();

	private readonly _lehrer : List<LehrerListeEintrag> = new ArrayList<LehrerListeEintrag>();

	private readonly _mapLehrerByID : JavaMap<number, LehrerListeEintrag> = new HashMap<number, LehrerListeEintrag>();

	private static readonly _compFach : Comparator<FachDaten> = { compare : (a: FachDaten, b: FachDaten) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		if ((a.kuerzel === null) || (b.kuerzel === null))
			throw new DeveloperNotificationException("Fachkürzel dürfen nicht null sein")
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	private static readonly _compFoerderschwerpunkte : Comparator<FoerderschwerpunktEintrag> = { compare : (a: FoerderschwerpunktEintrag, b: FoerderschwerpunktEintrag) => {
		if (a.text === null)
			return -1;
		if (b.text === null)
			return 1;
		return JavaString.compareTo(a.text, b.text);
	} };

	private static readonly _compLehrer : Comparator<LehrerListeEintrag> = { compare : (a: LehrerListeEintrag, b: LehrerListeEintrag) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.nachname, b.nachname);
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.vorname, b.vorname);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	private readonly _compLeistungenByFach : Comparator<SchuelerLeistungsdaten> = { compare : (a: SchuelerLeistungsdaten, b: SchuelerLeistungsdaten) => {
		const aFach : FachDaten = DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, a.fachID);
		const bFach : FachDaten = DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, b.fachID);
		return SchuelerLernabschnittManager._compFach.compare(aFach, bFach);
	} };


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Lernabschnittsdaten und den übergebenen Katalogen
	 *
	 * @param schulform             die Schulform der Schule des Schülers
	 * @param schueler              Informationen zu dem Schüler
	 * @param lernabschnittsdaten   die Lernabschnittsdaten
	 * @param schuljahresabschnitt  der Schuljahresabschnitt der Lernabschnittsdaten
	 * @param faecher               der Katalog der Fächer
	 * @param jahrgaenge            der Katalog der Jahrgänge
	 * @param klassen               der Katalog der Klassen
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 * @param foerderschwerpunkte   der Katalog der Förderschwerpunkte
	 */
	public constructor(schulform : Schulform, schueler : SchuelerListeEintrag, lernabschnittsdaten : SchuelerLernabschnittsdaten, schuljahresabschnitt : Schuljahresabschnitt, faecher : List<FachDaten>, foerderschwerpunkte : List<FoerderschwerpunktEintrag>, jahrgaenge : List<JahrgangsDaten>, klassen : List<KlassenDaten>, kurse : List<KursDaten>, lehrer : List<LehrerListeEintrag>) {
		super();
		this._schulform = schulform;
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this._schuljahresabschnitt = schuljahresabschnitt;
		this.initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		this.initFaecher(faecher);
		this.initFoerderschwerpunkte(foerderschwerpunkte);
		this.initJahrgaenge(jahrgaenge);
		this.initKlassen(klassen);
		this.initKurse(kurse);
		this.initLehrer(lehrer);
	}

	private initLeistungsdaten(leistungsdaten : List<SchuelerLeistungsdaten>) : void {
		for (const leistung of leistungsdaten)
			this.leistungAddInternal(leistung);
	}

	private initFaecher(faecher : List<FachDaten>) : void {
		this._faecher.clear();
		this._faecher.addAll(faecher);
		this._faecher.sort(SchuelerLernabschnittManager._compFach);
		this._mapFachByID.clear();
		for (const f of faecher)
			this._mapFachByID.put(f.id, f);
	}

	private initFoerderschwerpunkte(foerderschwerpunkte : List<FoerderschwerpunktEintrag>) : void {
		this._foerderschwerpunkte.clear();
		this._foerderschwerpunkte.addAll(foerderschwerpunkte);
		this._foerderschwerpunkte.sort(SchuelerLernabschnittManager._compFoerderschwerpunkte);
		this._mapFoerderschwerpunktByID.clear();
		for (const f of foerderschwerpunkte)
			this._mapFoerderschwerpunktByID.put(f.id, f);
	}

	private initJahrgaenge(jahrgaenge : List<JahrgangsDaten>) : void {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (const j of jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private initKlassen(klassen : List<KlassenDaten>) : void {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (const k of klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private initKurse(kurse : List<KursDaten>) : void {
		this._kurse.clear();
		this._kurse.addAll(kurse);
		this._kurse.sort(KursUtils.comparator);
		this._mapKursByID.clear();
		for (const k of kurse)
			this._mapKursByID.put(k.id, k);
	}

	private initLehrer(lehrer : List<LehrerListeEintrag>) : void {
		this._lehrer.clear();
		this._lehrer.addAll(lehrer);
		this._lehrer.sort(SchuelerLernabschnittManager._compLehrer);
		this._mapLehrerByID.clear();
		for (const l of lehrer)
			this._mapLehrerByID.put(l.id, l);
	}

	/**
	 * Gibt die Lernabschnittsdaten dieses Managers zurück.
	 *
	 * @return die Lernabschnittsdaten
	 */
	public lernabschnittGet() : SchuelerLernabschnittsdaten {
		return this._lernabschnittsdaten;
	}

	/**
	 * Gibt die Schulgliederung zurück, die dem Lernabschnitt zugeordnet ist oder
	 * null, falls keine Zuordnung existiert.
	 *
	 * @return die Schulgliederung oder null
	 */
	public lernabschnittGetGliederung() : Schulgliederung | null {
		if (this._lernabschnittsdaten.schulgliederung === null)
			return null;
		return Schulgliederung.data().getWertByKuerzel(this._lernabschnittsdaten.schulgliederung);
	}

	/**
	 * Gibt den Statistik-Jahrgang zurück, der dem Lernabschnitt zugeordnet ist oder null,
	 * falls kein Jahrgang zugeordnet ist.
	 *
	 * @return der Statistik-Jahrgang
	 */
	public lernabschnittGetStatistikJahrgang() : Jahrgaenge | null {
		if (this._lernabschnittsdaten.jahrgangID === null)
			return null;
		const eintrag : JahrgangsDaten | null = this._mapJahrgangByID.get(this._lernabschnittsdaten.jahrgangID);
		if ((eintrag === null) || (eintrag.kuerzelStatistik === null))
			return null;
		return Jahrgaenge.data().getWertByKuerzel(eintrag.kuerzelStatistik);
	}

	/**
	 * Gibt die Bezeichnung für die Lernbereichtsnote 1 zurück, sofern eine angegeben werden kann.
	 *
	 * @return die Bezeichnung für die Lernbereichtsnote 1
	 */
	public lernabschnittGetLernbereichsnote1Bezeichnung() : string | null {
		const jg : Jahrgaenge | null = this.lernabschnittGetStatistikJahrgang();
		if (jg === null)
			return null;
		return jg.getLernbereichsnote1Bezeichnung(this._schulform, this.lernabschnittGetGliederung(), this._schuljahresabschnitt.schuljahr);
	}

	/**
	 * Gibt die Bezeichnung für die Lernbereichtsnote 2 zurück, sofern eine angegeben werden kann.
	 *
	 * @return die Bezeichnung für die Lernbereichtsnote 2
	 */
	public lernabschnittGetLernbereichsnote2Bezeichnung() : string | null {
		const jg : Jahrgaenge | null = this.lernabschnittGetStatistikJahrgang();
		if (jg === null)
			return null;
		return jg.getLernbereichsnote2Bezeichnung(this._schulform, this.lernabschnittGetGliederung(), this._schuljahresabschnitt.schuljahr);
	}

	private leistungAddInternal(leistungsdaten : SchuelerLeistungsdaten) : void {
		this._mapLeistungById.put(leistungsdaten.id, leistungsdaten);
	}

	/**
	 * Fügt die übergebenen Leistungsdaten zu dem Lernabschnitt hinzu
	 *
	 * @param leistungsdaten   die hinzuzufügenden Leistungsdaten
	 */
	public leistungAdd(leistungsdaten : SchuelerLeistungsdaten) : void {
		this._lernabschnittsdaten.leistungsdaten.add(leistungsdaten);
		this.leistungAddInternal(leistungsdaten);
	}

	/**
	 * Entfernt die übergebenen Leistungsdaten anhand der ID aus dem Lernabschnitt
	 *
	 * @param idLeistungsdaten   die ID der zu entfernenden Leistungsdaten
	 */
	public leistungRemoveByID(idLeistungsdaten : number) : void {
		for (let i : number = this._lernabschnittsdaten.leistungsdaten.size() - 1; i >= 0; i--) {
			const leistung : SchuelerLeistungsdaten = this._lernabschnittsdaten.leistungsdaten.get(i);
			if (leistung.id === idLeistungsdaten)
				this._lernabschnittsdaten.leistungsdaten.remove(leistung);
		}
		this._mapLeistungById.remove(idLeistungsdaten);
	}

	/**
	 * Gibt die Leistungsdaten für die übergebene ID zurück.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Leistungsdaten
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public leistungGetByIdOrException(idLeistung : number) : SchuelerLeistungsdaten {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
	}

	/**
	 * Gibt die Menge der Leistungsdaten sortiert anhand des Faches zurück.
	 *
	 * @return die Menge der Leistungsdaten
	 */
	public leistungGetMengeAsListSortedByFach() : List<SchuelerLeistungsdaten> {
		const result : List<SchuelerLeistungsdaten> = new ArrayList<SchuelerLeistungsdaten>();
		result.addAll(this._lernabschnittsdaten.leistungsdaten);
		result.sort(this._compLeistungenByFach);
		return result;
	}

	/**
	 * Prüft, ob ein Kurs mit den Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return true, falls ein Kurs mit den Leistungsdaten verknüpft ist
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public leistungHatKurs(idLeistung : number) : boolean {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return this._mapKursByID.get(leistung.kursID) !== null;
	}

	/**
	 * Prüft, ob ein Lehrer mit den Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return true, falls ein Lehrer mit den Leistungsdaten verknüpft ist
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public leistungHatLehrer(idLeistung : number) : boolean {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return this._mapLehrerByID.get(leistung.lehrerID) !== null;
	}

	/**
	 * Ermittelt die Informationen zu dem Fach mit der angegebenen ID.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return die Fach-Informationen
	 * @throws DeveloperNotificationException falls kein Fach mit der ID existiert
	 */
	public fachGetByIdOrException(id : number) : FachDaten {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, id);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen oder null, wenn kein Fach zugeordnet ist
	 */
	public fachGetByLeistungId(idLeistung : number) : FachDaten | null {
		const leistung : SchuelerLeistungsdaten | null = this._mapLeistungById.get(idLeistung);
		if (leistung === null)
			return null;
		return this._mapFachByID.get(leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen.
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public fachGetByLeistungIdOrException(idLeistung : number) : FachDaten {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zu der Fach-Farbe, welche den Leistungsdaten zugeordnet ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Farbe und falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist, die Default-Farbe rgb(220,220,220)
	 */
	public fachFarbeGetByLeistungsIdOrDefault(idLeistung : number) : string {
		const fachDaten : FachDaten | null = this.fachGetByLeistungId(idLeistung);
		if (fachDaten === null)
			return "rgb(220,220,220)";
		return Fach.getBySchluesselOrDefault(fachDaten.kuerzel).getHMTLFarbeRGB(this._schuljahresabschnitt.schuljahr);
	}

	/**
	 * Gibt die Liste der Fächer zurück.
	 *
	 * @return die Liste der Fächer
	 */
	public fachGetMenge() : List<FachDaten> {
		return this._faecher;
	}

	/**
	 * Ermittelt die Informationen zu dem Förderschwerpunkt mit der angegebenen ID.
	 *
	 * @param id   die ID des Förderschwerpunktes
	 *
	 * @return die Förderschwerpunkt-Informationen
	 * @throws DeveloperNotificationException falls kein Förderschwerpunkt mit der ID existiert
	 */
	public foerderschwerpunktGetByIdOrException(id : number) : FoerderschwerpunktEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapFoerderschwerpunktByID, id);
	}

	/**
	 * Gibt die Liste der Förderschwerpunkte zurück.
	 *
	 * @return die Liste der Förderschwerpunkte
	 */
	public foerderschwerpunktGetMenge() : List<FoerderschwerpunktEintrag> {
		return this._foerderschwerpunkte;
	}

	/**
	 * Ermittelt die Informationen zu dem Jahrgang mit der angegebenen ID.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return die Jahrgangs-Informationen
	 * @throws DeveloperNotificationException falls kein Jahrgang mit der ID existiert
	 */
	public jahrgangGetByIdOrException(id : number) : JahrgangsDaten {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapJahrgangByID, id);
	}

	/**
	 * Gibt die Liste der Jahrgänge zurück.
	 *
	 * @return die Liste der Jahrgänge
	 */
	public jahrgangGetMenge() : List<JahrgangsDaten> {
		return this._jahrgaenge;
	}

	/**
	 * Ermittelt die Informationen zu der Klasse mit der angegebenen ID.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Klassen-Informationen
	 * @throws DeveloperNotificationException falls keine Klasse mit der ID existiert
	 */
	public klasseGetByIdOrException(id : number) : KlassenDaten {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKlasseByID, id);
	}

	/**
	 * Gibt die Liste der Klassen zurück.
	 *
	 * @return die Liste der Klassen
	 */
	public klasseGetMenge() : List<KlassenDaten> {
		return this._klassen;
	}

	/**
	 * Ermittelt die Informationen zu dem Kurs mit der angegebenen ID.
	 *
	 * @param id   die ID des Kurses
	 *
	 * @return die Kurs-Informationen
	 * @throws DeveloperNotificationException falls kein Kurs mit der ID existiert
	 */
	public kursGetByIdOrException(id : number) : KursDaten {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKursByID, id);
	}

	/**
	 * Ermittelt die Informationen zu dem Kurs, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Kurs-Informationen oder null, falls kein Kurs zugeordnet ist.
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public kursGetByLeistungIdOrNull(idLeistung : number) : KursDaten | null {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return this._mapKursByID.get(leistung.kursID);
	}

	/**
	 * Ermittelt die Informationen zu dem Kurs, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Kurs-Informationen
	 * @throws DeveloperNotificationException falls kein Kurs zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public kursGetByLeistungIdOrException(idLeistung : number) : KursDaten {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKursByID, leistung.kursID);
	}

	/**
	 * Gibt die Liste der Kurse zurück.
	 *
	 * @return die Liste der Kurse
	 */
	public kursGetMenge() : List<KursDaten> {
		return this._kurse;
	}

	/**
	 * Gibt die Liste der Kurse zurück und filtert diese anhand des Jahrgangs des Schülers sowie
	 * des Faches der Leistungsdaten.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die gefilterte Liste der Kurse
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public kursGetMengeFilteredByLeistung(idLeistung : number) : List<KursDaten> {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		const result : List<KursDaten> = new ArrayList<KursDaten>();
		for (const k of this._kurse) {
			if ((k.idFach === leistung.fachID) && (k.idJahrgaenge.isEmpty() || k.idJahrgaenge.contains(this._lernabschnittsdaten.jahrgangID)))
				result.add(k);
		}
		return result;
	}

	/**
	 * Ermittelt die Informationen zu dem Lehrer mit der angegebenen ID.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return die Lehrer-Informationen
	 * @throws DeveloperNotificationException falls kein Lehrer mit der ID existiert
	 */
	public lehrerGetByIdOrException(id : number) : LehrerListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapLehrerByID, id);
	}

	/**
	 * Ermittelt die Informationen zu dem Lehrer, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Lehrer-Informationen oder null, falls kein Lehrer zugeordnet ist.
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public lehrerGetByLeistungIdOrNull(idLeistung : number) : LehrerListeEintrag | null {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return this._mapLehrerByID.get(leistung.lehrerID);
	}

	/**
	 * Ermittelt die Informationen zu dem Lehrer, sofern einer mit diesen Leistungsdaten verknüpft ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Lehrer-Informationen
	 * @throws DeveloperNotificationException falls kein Lehrer zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public lehrerGetByLeistungIdOrException(idLeistung : number) : LehrerListeEintrag {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(this._mapLehrerByID, leistung.lehrerID);
	}

	/**
	 * Gibt die Liste der Lehrer zurück.
	 *
	 * @return die Liste der Lehrer
	 */
	public lehrerGetMenge() : List<LehrerListeEintrag> {
		return this._lehrer;
	}

	/**
	 * Ermittelt die Note, welche den Leistungsdaten zugewiesen ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die zugewiesene Note - falls keine zugewiesen ist wird Note.KEINE oder eine Pseudonote zurückgegeben
	 * @throws DeveloperNotificationException falls die ID der Leistungsdaten nicht korrekt ist
	 */
	public noteGetByLeistungIdOrException(idLeistung : number) : Note {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return Note.fromKuerzel(leistung.note);
	}

	/**
	 * Gibt die Schulform der Schule des Schülers zurück.
	 *
	 * @return die Schulform der Schule des Schülers
	 */
	public schulformGet() : Schulform {
		return this._schulform;
	}

	/**
	 * Gibt den Schuljahresabschnitt des Lernabschnittes zurück.
	 *
	 * @return der Schuljahresabschnitt des Lernabschnittes
	 */
	public schuljahresabschnittGet() : Schuljahresabschnitt {
		return this._schuljahresabschnitt;
	}

	/**
	 * Gibt das Schuljahr des Lernabschnittes zurück.
	 *
	 * @return das Schuljahr des Lernabschnittes
	 */
	public schuljahrGet() : number {
		return this._schuljahresabschnitt.schuljahr;
	}

	/**
	 * Gibt die Informationen des Schülers zurück, zu dem die Lernabschnittsdaten gehören.
	 *
	 * @return die Informationen des Schülers
	 */
	public schuelerGet() : SchuelerListeEintrag {
		return this._schueler;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.schueler.SchuelerLernabschnittManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerLernabschnittManager'].includes(name);
	}

	public static class = new Class<SchuelerLernabschnittManager>('de.svws_nrw.core.utils.schueler.SchuelerLernabschnittManager');

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerLernabschnittManager(obj : unknown) : SchuelerLernabschnittManager {
	return obj as SchuelerLernabschnittManager;
}
