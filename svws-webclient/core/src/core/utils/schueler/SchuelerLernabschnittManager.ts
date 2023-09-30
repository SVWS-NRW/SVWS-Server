import { JavaObject } from '../../../java/lang/JavaObject';
import { SchuelerListeEintrag } from '../../../core/data/schueler/SchuelerListeEintrag';
import { SchuelerLeistungsdaten } from '../../../core/data/schueler/SchuelerLeistungsdaten';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { FaecherListeEintrag } from '../../../core/data/fach/FaecherListeEintrag';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { KursListeEintrag } from '../../../core/data/kurse/KursListeEintrag';
import { LehrerListeEintrag } from '../../../core/data/lehrer/LehrerListeEintrag';
import { SchuelerLernabschnittsdaten } from '../../../core/data/schueler/SchuelerLernabschnittsdaten';
import { Note } from '../../../core/types/Note';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import type { List } from '../../../java/util/List';
import type { JavaMap } from '../../../java/util/JavaMap';

export class SchuelerLernabschnittManager extends JavaObject {

	private readonly _schueler : SchuelerListeEintrag;

	private readonly _lernabschnittsdaten : SchuelerLernabschnittsdaten;

	private readonly _mapLeistungById : JavaMap<number, SchuelerLeistungsdaten> = new HashMap();

	private readonly faecher : List<FaecherListeEintrag> = new ArrayList();

	private readonly _mapFachByID : JavaMap<number, FaecherListeEintrag> = new HashMap();

	private readonly kurse : List<KursListeEintrag> = new ArrayList();

	private readonly _mapKursByID : JavaMap<number, KursListeEintrag> = new HashMap();

	private readonly lehrer : List<LehrerListeEintrag> = new ArrayList();

	private readonly _mapLehrerByID : JavaMap<number, LehrerListeEintrag> = new HashMap();


	/**
	 * Erstellt einen neuen Manager mit den übergebenen Lernabschnittsdaten und den übergebenen Katalogen
	 *
	 * @param schueler              Informationen zu dem Schüler
	 * @param lernabschnittsdaten   die Lernabschnittsdaten
	 * @param faecher               der Katalog der Fächer
	 * @param kurse                 der Katalog der Kurse
	 * @param lehrer                der Katalog der Lehrer
	 */
	public constructor(schueler : SchuelerListeEintrag, lernabschnittsdaten : SchuelerLernabschnittsdaten, faecher : List<FaecherListeEintrag>, kurse : List<KursListeEintrag>, lehrer : List<LehrerListeEintrag>) {
		super();
		this._schueler = schueler;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this.initLeistungsdaten(lernabschnittsdaten.leistungsdaten);
		this.initFaecher(faecher);
		this.initKurse(kurse);
		this.initLehrer(lehrer);
	}

	private initLeistungsdaten(leistungsdaten : List<SchuelerLeistungsdaten>) : void {
		for (const leistung of leistungsdaten) {
			this.leistungAdd(leistung);
		}
	}

	private initFaecher(faecher : List<FaecherListeEintrag>) : void {
		this.faecher.clear();
		this.faecher.addAll(faecher);
		this._mapFachByID.clear();
		for (const f of faecher)
			this._mapFachByID.put(f.id, f);
	}

	private initKurse(kurse : List<KursListeEintrag>) : void {
		this.kurse.clear();
		this.kurse.addAll(kurse);
		this._mapKursByID.clear();
		for (const k of kurse)
			this._mapKursByID.put(k.id, k);
	}

	private initLehrer(lehrer : List<LehrerListeEintrag>) : void {
		this.lehrer.clear();
		this.lehrer.addAll(lehrer);
		this._mapLehrerByID.clear();
		for (const l of lehrer)
			this._mapLehrerByID.put(l.id, l);
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.schueler.SchuelerLernabschnittManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_schueler_SchuelerLernabschnittManager(obj : unknown) : SchuelerLernabschnittManager {
	return obj as SchuelerLernabschnittManager;
}
