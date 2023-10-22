import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { SchuelerLeistungsdaten } from '../../../core/data/schueler/SchuelerLeistungsdaten';
import { HashMap } from '../../../java/util/HashMap';
import { KlassenUtils } from '../../../core/utils/klassen/KlassenUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { KlassenListeEintrag } from '../../../core/data/klassen/KlassenListeEintrag';
import { FaecherListeEintrag } from '../../../core/data/fach/FaecherListeEintrag';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import type { Comparator } from '../../../java/util/Comparator';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { JahrgangsListeEintrag } from '../../../core/data/jahrgang/JahrgangsListeEintrag';
import { SchuelerLernabschnittsdaten } from '../../../core/data/schueler/SchuelerLernabschnittsdaten';
import { Note } from '../../../core/types/Note';
import { JavaLong } from '../../../java/lang/JavaLong';
import { FoerderschwerpunktEintrag } from '../../../core/data/schule/FoerderschwerpunktEintrag';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import type { List } from '../../../java/util/List';
import { KursUtils } from '../../../core/utils/kurse/KursUtils';
import type { JavaMap } from '../../../java/util/JavaMap';

export class SchuelerLernabschnittManager extends JavaObject {

	private readonly _schueler : SchuelerListeEintrag;

	private readonly _lernabschnittsdaten : SchuelerLernabschnittsdaten;

	private readonly _mapLeistungById : JavaMap<number, SchuelerLeistungsdaten> = new HashMap();

	private readonly _faecher : List<FaecherListeEintrag> = new ArrayList();

	private readonly _mapFachByID : JavaMap<number, FaecherListeEintrag> = new HashMap();

	private readonly _foerderschwerpunkte : List<FoerderschwerpunktEintrag> = new ArrayList();

	private readonly _mapFoerderschwerpunktByID : JavaMap<number, FoerderschwerpunktEintrag> = new HashMap();

	private readonly _jahrgaenge : List<JahrgangsListeEintrag> = new ArrayList();

	private readonly _mapJahrgangByID : JavaMap<number, JahrgangsListeEintrag> = new HashMap();

	private readonly _klassen : List<KlassenListeEintrag> = new ArrayList();

	private readonly _mapKlasseByID : JavaMap<number, KlassenListeEintrag> = new HashMap();

	private readonly _kurse : List<KursListeEintrag> = new ArrayList();

	private readonly _mapKursByID : JavaMap<number, KursListeEintrag> = new HashMap();

	private readonly _lehrer : List<LehrerListeEintrag> = new ArrayList();

	private readonly _mapLehrerByID : JavaMap<number, LehrerListeEintrag> = new HashMap();

	private static readonly _compFach : Comparator<FaecherListeEintrag> = { compare : (a: FaecherListeEintrag, b: FaecherListeEintrag) => {
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
		const aFach : FaecherListeEintrag = DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, a.fachID);
		const bFach : FaecherListeEintrag = DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, b.fachID);
		return SchuelerLernabschnittManager._compFach.compare(aFach, bFach);
	} };


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Lernabschnittsdaten und den übergebenen Katalogen
	 *
	 * @param schueler              Informationen zu dem Schüler
	 * @param lernabschnittsdaten   die Lernabschnittsdaten
	 * @param faecher               der Katalog der Fächer
	 * @param jahrgaenge            der Katalog der Jahrgänge
	 * @param klassen               der Katalog der Klassen
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 * @param foerderschwerpunkte   der Katalog der Förderschwerpunkte
	 */
	public constructor(schueler : SchuelerListeEintrag, lernabschnittsdaten : SchuelerLernabschnittsdaten, faecher : List<FaecherListeEintrag>, foerderschwerpunkte : List<FoerderschwerpunktEintrag>, jahrgaenge : List<JahrgangsListeEintrag>, klassen : List<KlassenListeEintrag>, kurse : List<KursListeEintrag>, lehrer : List<LehrerListeEintrag>) {
		super();
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this.initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		this.initFaecher(faecher);
		this.initFoerderschwerpunkte(foerderschwerpunkte);
		this.initJahrgaenge(jahrgaenge);
		this.initKlassen(klassen);
		this.initKurse(kurse);
		this.initLehrer(lehrer);
	}

	private initLeistungsdaten(leistungsdaten : List<SchuelerLeistungsdaten>) : void {
		for (const leistung of leistungsdaten) {
			this.leistungAdd(leistung);
		}
	}

	private initFaecher(faecher : List<FaecherListeEintrag>) : void {
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

	private initJahrgaenge(jahrgaenge : List<JahrgangsListeEintrag>) : void {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (const j of jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private initKlassen(klassen : List<KlassenListeEintrag>) : void {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (const k of klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private initKurse(kurse : List<KursListeEintrag>) : void {
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
	 * Fügt die übergebenen Leistungsdaten zu dem Lernabschnitt hinzu
	 *
	 * @param leistungsdaten   die hinzuzufügenden Leistungsdaten
	 */
	public leistungAdd(leistungsdaten : SchuelerLeistungsdaten) : void {
		this._mapLeistungById.put(leistungsdaten.id, leistungsdaten);
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
		const result : List<SchuelerLeistungsdaten> = new ArrayList();
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
	public fachGetByIdOrException(id : number) : FaecherListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, id);
	}

	/**
	 * Ermittelt die Informationen zum Fach, welche mit den Leistungsdaten verknüpft sind.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Fach-Informationen.
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public fachGetByLeistungIdOrException(idLeistung : number) : FaecherListeEintrag {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(this._mapFachByID, leistung.fachID);
	}

	/**
	 * Ermittelt die Informationen zu der Fach-Farbe, welche den Leistungsdaten zugeordnet ist.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Farbe
	 * @throws DeveloperNotificationException falls kein Fach zugeordnet ist oder die ID der Leistungsdaten nicht korrekt ist
	 */
	public fachFarbeGetByLeistungsIdOrException(idLeistung : number) : string {
		const fach : FaecherListeEintrag = this.fachGetByLeistungIdOrException(idLeistung);
		return ZulaessigesFach.getByKuerzelASD(fach.kuerzel).getHMTLFarbeRGB();
	}

	/**
	 * Gibt die Liste der Fächer zurück.
	 *
	 * @return die Liste der Fächer
	 */
	public fachGetMenge() : List<FaecherListeEintrag> {
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
	public jahrgangGetByIdOrException(id : number) : JahrgangsListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapJahrgangByID, id);
	}

	/**
	 * Gibt die Liste der Jahrgänge zurück.
	 *
	 * @return die Liste der Jahrgänge
	 */
	public jahrgangGetMenge() : List<JahrgangsListeEintrag> {
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
	public klasseGetByIdOrException(id : number) : KlassenListeEintrag {
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKlasseByID, id);
	}

	/**
	 * Gibt die Liste der Klassen zurück.
	 *
	 * @return die Liste der Klassen
	 */
	public klasseGetMenge() : List<KlassenListeEintrag> {
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
	public kursGetByIdOrException(id : number) : KursListeEintrag {
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
	public kursGetByLeistungIdOrNull(idLeistung : number) : KursListeEintrag | null {
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
	public kursGetByLeistungIdOrException(idLeistung : number) : KursListeEintrag {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		return DeveloperNotificationException.ifMapGetIsNull(this._mapKursByID, leistung.kursID);
	}

	/**
	 * Gibt die Liste der Kurse zurück.
	 *
	 * @return die Liste der Kurse
	 */
	public kursGetMenge() : List<KursListeEintrag> {
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
	public kursGetMengeFilteredByLeistung(idLeistung : number) : List<KursListeEintrag> {
		const leistung : SchuelerLeistungsdaten = DeveloperNotificationException.ifMapGetIsNull(this._mapLeistungById, idLeistung);
		const result : List<KursListeEintrag> = new ArrayList();
		for (const k of this._kurse) {
			if ((k.idFach === leistung.fachID) && (k.idJahrgaenge.isEmpty() || k.idJahrgaenge.contains(this._schueler.idJahrgang)))
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
	 * Gibt die Informationen des Schülers zurück, zu dem die Lernabschnittsdaten gehören.
	 *
	 * @return die Informationen des Schülers
	 */
	public schuelerGet() : SchuelerListeEintrag {
		return this._schueler;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerLernabschnittManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerLernabschnittManager(obj : unknown) : SchuelerLernabschnittManager {
	return obj as SchuelerLernabschnittManager;
}
