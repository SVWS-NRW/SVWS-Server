import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { GostBlockungsergebnisManager } from '../../../core/utils/gost/GostBlockungsergebnisManager';
import type { JavaSet } from '../../../java/util/JavaSet';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { LongArrayKey } from '../../../core/adt/LongArrayKey';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostBlockungRegel } from '../../../core/data/gost/GostBlockungRegel';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { SchuelerStatus } from '../../../asd/types/schueler/SchuelerStatus';
import type { Comparator } from '../../../java/util/Comparator';
import { GostKursblockungRegelTyp } from '../../../core/types/kursblockung/GostKursblockungRegelTyp';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../java/util/List';
import { Geschlecht } from '../../../asd/types/Geschlecht';
import { GostBlockungKurs } from '../../../core/data/gost/GostBlockungKurs';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { SetUtils } from '../../../core/utils/SetUtils';
import { GostBlockungKursLehrer } from '../../../core/data/gost/GostBlockungKursLehrer';
import { GostFachwahl } from '../../../core/data/gost/GostFachwahl';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import { MapUtils } from '../../../core/utils/MapUtils';
import { GostKursblockungRegelParameterTyp } from '../../../core/types/kursblockung/GostKursblockungRegelParameterTyp';
import { Map2DUtils } from '../../../core/utils/Map2DUtils';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { GostBlockungsergebnis } from '../../../core/data/gost/GostBlockungsergebnis';
import { GostBlockungsdaten } from '../../../core/data/gost/GostBlockungsdaten';
import { Schueler } from '../../../asd/data/schueler/Schueler';
import { GostBlockungSchiene } from '../../../core/data/gost/GostBlockungSchiene';
import { JavaLong } from '../../../java/lang/JavaLong';
import { Class } from '../../../java/lang/Class';
import { ListUtils } from '../../../core/utils/ListUtils';
import { DTOUtils } from '../../../core/utils/DTOUtils';
import type { JavaMap } from '../../../java/util/JavaMap';
import { GostBlockungsergebnisComparator } from '../../../core/utils/gost/GostBlockungsergebnisComparator';
import { UserNotificationException } from '../../../core/exceptions/UserNotificationException';

export class GostBlockungsdatenManager extends JavaObject {

	/**
	 * Die Blockungsdaten, die im Manager vorhanden sind.
	 */
	private readonly _daten : GostBlockungsdaten;

	/**
	 * Der Fächermanager mit den Fächern der gymnasialen Oberstufe.
	 */
	private readonly _faecherManager : GostFaecherManager;

	/**
	 * Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern!
	 */
	private static readonly _compKursnummer : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für Schienen der Blockung
	 */
	private static readonly _compSchiene : Comparator<GostBlockungSchiene> = { compare : (a: GostBlockungSchiene, b: GostBlockungSchiene) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für die Lehrkräfte eines Kurses
	 */
	private static readonly _compLehrkraefte : Comparator<GostBlockungKursLehrer> = { compare : (a: GostBlockungKursLehrer, b: GostBlockungKursLehrer) => {
		const result : number = JavaInteger.compare(a.reihenfolge, b.reihenfolge);
		if (result !== 0)
			return result;
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Ergebnisse sortiert nach ID.
	 */
	private static readonly _compErgebnisseNachID : Comparator<GostBlockungsergebnis> = { compare : (a: GostBlockungsergebnis, b: GostBlockungsergebnis) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Comparator für die Schüler.
	 */
	private readonly _compSchueler : Comparator<Schueler>;

	/**
	 * Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART)
	 */
	private readonly _compFachwahlen : Comparator<GostFachwahl>;

	/**
	 * Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 */
	private readonly _compErgebnisse : Comparator<GostBlockungsergebnis> = new GostBlockungsergebnisComparator();

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _compKurs_kursart_fach_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _compKurs_fach_kursart_kursnummer : Comparator<GostBlockungKurs>;

	/**
	 * Ein Comparator für Regeln der Blockung
	 */
	private readonly _compRegel : Comparator<GostBlockungRegel>;

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idKurs_kurs : HashMap<number, GostBlockungKurs> = new HashMap<number, GostBlockungKurs>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly _map2d_idFach_idKursart_kurse : HashMap2D<number, number, List<GostBlockungKurs>> = new HashMap2D<number, number, List<GostBlockungKurs>>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly _map2d_idFach_idKursart_fachwahlen : HashMap2D<number, number, List<GostFachwahl>> = new HashMap2D<number, number, List<GostFachwahl>>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idSchiene_schiene : HashMap<number, GostBlockungSchiene> = new HashMap<number, GostBlockungSchiene>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idRegel_regel : HashMap<number, GostBlockungRegel> = new HashMap<number, GostBlockungRegel>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly _map_regeltyp_regeln : JavaMap<GostKursblockungRegelTyp, List<GostBlockungRegel>> = new ArrayMap<GostKursblockungRegelTyp, List<GostBlockungRegel>>(GostKursblockungRegelTyp.values());

	/**
	 * Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly _map_multikey_regeln : HashMap<LongArrayKey, GostBlockungRegel> = new HashMap<LongArrayKey, GostBlockungRegel>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schueler anhand ihrer Datenbank-ID.
	 */
	private readonly _map_idSchueler_schueler : HashMap<number, Schueler> = new HashMap<number, Schueler>();

	/**
	 * Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart.
	 */
	private readonly _map_idSchueler_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap<number, List<GostFachwahl>>();

	/**
	 * (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet.
	 */
	private readonly _map2d_idSchueler_idFach_fachwahl : HashMap2D<number, number, GostFachwahl> = new HashMap2D<number, number, GostFachwahl>();

	/**
	 * Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart.
	 */
	private readonly _map_idFachart_fachwahlen : HashMap<number, List<GostFachwahl>> = new HashMap<number, List<GostFachwahl>>();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnis}
	 */
	private readonly _map_idErgebnis_Ergebnis : HashMap<number, GostBlockungsergebnis> = new HashMap<number, GostBlockungsergebnis>();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnisManager}
	 */
	private readonly _map_idErgebnis_ErgebnisManager : HashMap<number, GostBlockungsergebnisManager> = new HashMap<number, GostBlockungsergebnisManager>();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER).
	 */
	private readonly _list_kurse_sortiert_fach_kursart_kursnummer : List<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER)
	 */
	private readonly _list_kurse_sortiert_kursart_fach_kursnummer : List<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();

	/**
	 * Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf.
	 */
	private _maxTimeMillis : number = 1000;


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten           die Blockungsdaten
	 * @param faecherManager  der Fächer-Manager
	 */
	public constructor(daten : GostBlockungsdaten, faecherManager : GostFaecherManager) {
		super();
		this._faecherManager = faecherManager;
		this._compKurs_fach_kursart_kursnummer = this.createComparatorKursFachKursartNummer();
		this._compKurs_kursart_fach_kursnummer = this.createComparatorKursKursartFachNummer();
		this._compFachwahlen = this.createComparatorFachwahlen();
		this._compRegel = this.createComparatorRegeln();
		this._compSchueler = this.createComparatorSchueler();
		this._daten = new GostBlockungsdaten();
		this._daten.id = daten.id;
		this._daten.name = daten.name;
		this._daten.abijahrgang = daten.abijahrgang;
		this._daten.gostHalbjahr = daten.gostHalbjahr;
		this._daten.istAktiv = daten.istAktiv;
		this.schieneAddListe(daten.schienen);
		this.fachwahlAddListe(daten.fachwahlen);
		this.schuelerAddListe(daten.schueler);
		this.kursAddListe(daten.kurse);
		this.regelAddListe(daten.regeln);
		this.ergebnisAddListe(daten.ergebnisse);
	}

	/**
	 * Liefert eine Kurzdarstellung der Kursart mit der übergebenen ID.
	 *
	 * @param kursart  Die ID der Kursart.
	 *
	 * @return eine Kurzdarstellung der Kursart mit der übergebenen ID.
	 */
	public toStringKursartSimple(kursart : number) : string {
		const gKursart : GostKursart | null = GostKursart.fromIDorNull(kursart);
		return (gKursart === null) ? ("[Kursart-ID = " + kursart + " (ohne Mapping)]") : gKursart.kuerzel;
	}

	/**
	 * Liefert möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 */
	public toStringKurs(idKurs : number) : string {
		const kurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs);
		if (kurs === null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";
		const gFach : GostFach | null = this._faecherManager.get(kurs.fach_id);
		let sFach : string = "Fach-ID = " + kurs.fach_id + " (ohne Mapping)";
		if (gFach !== null)
			sFach = (gFach.kuerzelAnzeige === null) ? ("Fach-ID = " + kurs.fach_id + " (ohne 'kuerzelAnzeige')") : gFach.kuerzelAnzeige;
		return "[Kurs " + sFach + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 */
	public toStringKursSimple(idKurs : number) : string {
		const kurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs);
		if (kurs === null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";
		return "(" + kurs.id + ") " + this.toStringFachSimple(kurs.fach_id) + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 */
	public toStringKursSimpleOhneID(idKurs : number) : string {
		const kurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs);
		if (kurs === null)
			return "[Kurs (" + idKurs + ") ohne Mapping]";
		return this.toStringFachSimple(kurs.fach_id) + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Faches mit der übergebenen ID.
	 *
	 * @param idFach  Die Datenbank-ID des Faches.
	 *
	 * @return eine Kurzdarstellung des Faches mit der übergebenen ID.
	 */
	public toStringFachSimple(idFach : number) : string {
		const gFach : GostFach | null = this._faecherManager.get(idFach);
		if (gFach === null)
			return "[Fach-ID = " + idFach + " (ohne Mapping)]";
		if (gFach.kuerzelAnzeige === null)
			return "[Fach-ID = " + idFach + " (ohne 'kuerzelAnzeige')]";
		return gFach.kuerzelAnzeige;
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFach   Die Datenbank-ID des Faches.
	 * @param kursart  Die Datenbank-ID der Kursart.
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart).
	 */
	public toStringFachartSimple(idFach : number, kursart : number) : string {
		return this.toStringFachSimple(idFach) + "-" + this.toStringKursartSimple(kursart);
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFachart  Die Fachart (zusammengesetzt aus Fach und Kursart)
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart).
	 */
	public toStringFachartSimpleByFachartID(idFachart : number) : string {
		const idFach : number = GostKursart.getFachID(idFachart);
		const kursart : number = GostKursart.getKursartID(idFachart);
		return this.toStringFachSimple(idFach) + "-" + this.toStringKursartSimple(kursart);
	}

	/**
	 * Liefert möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 */
	public toStringSchueler(idSchueler : number) : string {
		const schueler : Schueler | null = this._map_idSchueler_schueler.get(idSchueler);
		if (schueler === null)
			return "[Schüler (" + idSchueler + ") ohne Mapping]";
		return "[Schüler (" + schueler.id + "): " + schueler.nachname + ", " + schueler.vorname + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 */
	public toStringSchuelerSimple(idSchueler : number) : string {
		const schueler : Schueler | null = this._map_idSchueler_schueler.get(idSchueler);
		if (schueler === null)
			return "[Schüler (" + idSchueler + ") ohne Mapping]";
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 */
	public toStringSchiene(idSchiene : number) : string {
		const schiene : GostBlockungSchiene | null = this._map_idSchiene_schiene.get(idSchiene);
		if (schiene === null)
			return "[Schiene (" + idSchiene + ") ohne Mapping]";
		return "[Schiene: ID " + schiene.id + ", Nr. " + schiene.nummer + ", Bez. " + schiene.bezeichnung + ", Stunden " + schiene.wochenstunden + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 */
	public toStringSchieneSimple(idSchiene : number) : string {
		const schiene : GostBlockungSchiene | null = this._map_idSchiene_schiene.get(idSchiene);
		if (schiene === null)
			return "[Schiene (" + idSchiene + ") ohne Mapping]";
		return "Schiene Nr. " + schiene.nummer;
	}

	/**
	 * Liefert möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der Lehrkraft.
	 *
	 * @return möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 */
	public toStringKursLehrkraft(idKurs : number, idLehrkraft : number) : string {
		const kurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs);
		if (kurs === null)
			return "[Lehrkraft (ID=" + idLehrkraft + ")]";
		for (const lehrer of kurs.lehrer)
			if (lehrer.id === idLehrkraft)
				return "[Lehrkraft (ID=" + idLehrkraft + ") " + lehrer.kuerzel + "]";
		return "[Lehrkraft (ID=" + idLehrkraft + ")]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 *
	 * @param gFachwahl  Das {@link GostFachwahl}-Objekt.
	 *
	 * @return eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 */
	public toStringFachwahlSimple(gFachwahl : GostFachwahl) : string | null {
		return this.toStringSchuelerSimple(gFachwahl.schuelerID) + " wählt " + this.toStringFachartSimple(gFachwahl.fachID, gFachwahl.kursartID);
	}

	/**
	 * Liefert möglichst viele Informationen zur Regel mit der übergebenen ID.
	 *
	 * @param idRegel  Die Datenbank-ID der Regel.
	 *
	 * @return möglichst viele Informationen zur Regel mit der übergebenen ID.
	 */
	public toStringRegel(idRegel : number) : string {
		const regel : GostBlockungRegel | null = this._map_idRegel_regel.get(idRegel);
		if (regel === null)
			return "[Regel (" + idRegel + ") ohne Mapping]";
		return "[Regel (" + regel.id + ", Nr. " + regel.typ + "): " + regel.parameter + "]";
	}

	private createComparatorRegeln() : Comparator<GostBlockungRegel> {
		const comp : Comparator<GostBlockungRegel> = { compare : (a: GostBlockungRegel, b: GostBlockungRegel) => {
			const cmp1 : number = JavaInteger.compare(a.typ, b.typ);
			if (cmp1 !== 0)
				return cmp1;
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(a.typ);
			let cmp2 : number;
			const _seexpr_2075240461 = (typ);
			if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE) {
				cmp2 = this.compareRegel_Kurs_Nummer(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE) {
				cmp2 = this.compareRegel_Kurs_Nummer(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				cmp2 = this.compareRegel_Schueler_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				cmp2 = this.compareRegel_Schueler_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS) {
				cmp2 = this.compareRegel_Kurs_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS) {
				cmp2 = this.compareRegel_Kurs_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN) {
				cmp2 = this.compareRegel_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL) {
				cmp2 = this.compareRegel_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN) {
				cmp2 = this.compareRegel_Kurs(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH) {
				cmp2 = this.compareRegel_Schueler_Schueler_Fach(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH) {
				cmp2 = this.compareRegel_Schueler_Schueler_Fach(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER) {
				cmp2 = this.compareRegel_Schueler_Schueler(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER) {
				cmp2 = this.compareRegel_Schueler_Schueler(a, b);
			} else if (_seexpr_2075240461 === GostKursblockungRegelTyp.SCHUELER_IGNORIEREN) {
				cmp2 = this.compareRegel_Schueler(a, b);
			} else {
				cmp2 = 0;
			}
			;
			if (cmp2 !== 0)
				return cmp2;
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private createComparatorSchueler() : Comparator<Schueler> {
		const comp : Comparator<Schueler> = { compare : (a: Schueler, b: Schueler) => {
			const cmpSchueler : number = this.compareSchueler(a.id, b.id);
			if (cmpSchueler !== 0)
				return cmpSchueler;
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private createComparatorFachwahlen() : Comparator<GostFachwahl> {
		const comp : Comparator<GostFachwahl> = { compare : (a: GostFachwahl, b: GostFachwahl) => {
			const cmpSchueler : number = this.compareSchueler(a.schuelerID, b.schuelerID);
			if (cmpSchueler !== 0)
				return cmpSchueler;
			const cmpFach : number = this.compareFach(a.fachID, b.fachID);
			if (cmpFach !== 0)
				return cmpFach;
			return JavaInteger.compare(a.kursartID, b.kursartID);
		} };
		return comp;
	}

	private createComparatorKursFachKursartNummer() : Comparator<GostBlockungKurs> {
		const comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			const cmpFach : number = this.compareFach(a.fach_id, b.fach_id);
			if (cmpFach !== 0)
				return cmpFach;
			const cmpKursart : number = JavaInteger.compare(a.kursart, b.kursart);
			if (cmpKursart !== 0)
				return cmpKursart;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorKursKursartFachNummer() : Comparator<GostBlockungKurs> {
		const comp : Comparator<GostBlockungKurs> = { compare : (a: GostBlockungKurs, b: GostBlockungKurs) => {
			const k1 : number = (a.kursart === GostKursart.ZK.id) ? GostKursart.GK.id : a.kursart;
			const k2 : number = (b.kursart === GostKursart.ZK.id) ? GostKursart.GK.id : b.kursart;
			const cmpKursartGKZK : number = JavaInteger.compare(k1, k2);
			if (cmpKursartGKZK !== 0)
				return cmpKursartGKZK;
			const cmpFach : number = this.compareFach(a.fach_id, b.fach_id);
			if (cmpFach !== 0)
				return cmpFach;
			const cmpKursart : number = JavaInteger.compare(a.kursart, b.kursart);
			if (cmpKursart !== 0)
				return cmpKursart;
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private compareRegel_Kurs(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpKurs1 : number = this.compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0)
			return cmpKurs1;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Kurs_Nummer(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpKurs1 : number = this.compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0)
			return cmpKurs1;
		const cmpSchienenNr : number = JavaLong.compare(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchienenNr !== 0)
			return cmpSchienenNr;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Schueler(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpSchueler1 : number = this.compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0)
			return cmpSchueler1;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Schueler_Kurs(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpSchueler1 : number = this.compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0)
			return cmpSchueler1;
		const cmpKurs1 : number = this.compareKurs_Kursart_Fach_Nummer(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs1 !== 0)
			return cmpKurs1;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Kurs_Kurs(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpKurs1 : number = this.compareKurs_Kursart_Fach_Nummer(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0)
			return cmpKurs1;
		const cmpKurs2 : number = this.compareKurs_Kursart_Fach_Nummer(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs2 !== 0)
			return cmpKurs2;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Schueler_Schueler_Fach(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpSchueler1 : number = this.compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0)
			return cmpSchueler1;
		const cmpSchueler2 : number = this.compareSchueler(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 !== 0)
			return cmpSchueler2;
		const cmpFach : number = this.compareFach(a.parameter.get(2), b.parameter.get(2));
		if (cmpFach !== 0)
			return cmpFach;
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel_Schueler_Schueler(a : GostBlockungRegel, b : GostBlockungRegel) : number {
		const cmpSchueler1 : number = this.compareSchueler(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0)
			return cmpSchueler1;
		const cmpSchueler2 : number = this.compareSchueler(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 !== 0)
			return cmpSchueler2;
		return JavaLong.compare(a.id, b.id);
	}

	private compareSchueler(idSchueler1 : number, idSchueler2 : number) : number {
		const a : Schueler | null = this._map_idSchueler_schueler.get(idSchueler1);
		const b : Schueler | null = this._map_idSchueler_schueler.get(idSchueler2);
		if (a === null)
			return (b === null) ? 0 : -1;
		if (b === null)
			return +1;
		const cNachname : number = JavaString.compareTo(a.nachname, b.nachname);
		if (cNachname !== 0)
			return cNachname;
		const cVorname : number = JavaString.compareTo(a.vorname, b.vorname);
		if (cVorname !== 0)
			return cVorname;
		return JavaLong.compare(a.id, b.id);
	}

	private compareFach(idFach1 : number, idFach2 : number) : number {
		const aFach : GostFach | null = this._faecherManager.get(idFach1);
		const bFach : GostFach | null = this._faecherManager.get(idFach2);
		if (aFach === null)
			return (bFach === null) ? 0 : -1;
		return (bFach === null) ? +1 : GostFaecherManager.comp.compare(aFach, bFach);
	}

	private compareKurs_Kursart_Fach_Nummer(idKurs1 : number, idKurs2 : number) : number {
		const aKurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs1);
		const bKurs : GostBlockungKurs | null = this._map_idKurs_kurs.get(idKurs2);
		if (aKurs === null)
			return (bKurs === null) ? 0 : -1;
		if (bKurs === null)
			return +1;
		const cmpKursart : number = JavaLong.compare(aKurs.kursart, bKurs.kursart);
		if (cmpKursart !== 0)
			return cmpKursart;
		const cmpFach : number = this.compareFach(aKurs.fach_id, bKurs.fach_id);
		if (cmpFach !== 0)
			return cmpFach;
		const cmpNummer : number = JavaLong.compare(aKurs.fach_id, bKurs.fach_id);
		if (cmpNummer !== 0)
			return cmpNummer;
		return JavaLong.compare(aKurs.id, bKurs.id);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis Das {@link GostBlockungsergebnis}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public ergebnisAdd(ergebnis : GostBlockungsergebnis) : void {
		this.ergebnisAddListe(ListUtils.create1(ergebnis));
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public ergebnisAddListe(ergebnismenge : List<GostBlockungsergebnis>) : void {
		for (const ergebnis of ergebnismenge) {
			DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
			DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
			DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_Ergebnis", this._map_idErgebnis_Ergebnis, ergebnis.id);
			DeveloperNotificationException.ifMapContains("_map_idErgebnis_ErgebnisManager", this._map_idErgebnis_ErgebnisManager, ergebnis.id);
		}
		for (const ergebnis of ergebnismenge) {
			const ergebnisManager : GostBlockungsergebnisManager | null = new GostBlockungsergebnisManager(this, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_idErgebnis_Ergebnis, ergebnis.id, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(this._map_idErgebnis_ErgebnisManager, ergebnis.id, ergebnisManager);
			this._daten.ergebnisse.add(ergebnis);
		}
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGet(idErgebnis : number) : GostBlockungsergebnis {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", this._map_idErgebnis_Ergebnis.get(idErgebnis));
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisManager} für das Ergebnis mit der übergebenen ID.
	 * Wirft eine Exception, falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return einen {@link GostBlockungsergebnisManager} für das Ergebnis.
	 * @throws DeveloperNotificationException Falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisManagerGet(idErgebnis : number) : GostBlockungsergebnisManager {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", this._map_idErgebnis_ErgebnisManager.get(idErgebnis));
	}

	/**
	 * Liefert TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 */
	public ergebnisManagerExists(idErgebnis : number) : boolean {
		return this._map_idErgebnis_ErgebnisManager.containsKey(idErgebnis);
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 *
	 * @return die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 */
	public ergebnisManagerGetListeUnsortiert() : List<GostBlockungsergebnisManager> {
		return new ArrayList<GostBlockungsergebnisManager>(this._map_idErgebnis_ErgebnisManager.values());
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 */
	public ergebnisGetListeSortiertNachBewertung() : List<GostBlockungsergebnis> {
		return new ArrayList<GostBlockungsergebnis>(this._daten.ergebnisse);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 *
	 * @return Eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 */
	public ergebnisGetListeSortiertNachID() : List<GostBlockungsergebnis> {
		const list : List<GostBlockungsergebnis> = new ArrayList<GostBlockungsergebnis>(this._daten.ergebnisse);
		list.sort(GostBlockungsdatenManager._compErgebnisseNachID);
		return list;
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten anhand ihrer ID.
	 *
	 * @param listeDerErgebnisIDs  Die IDs der Ergebnisse.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public ergebnisRemoveListeByIDs(listeDerErgebnisIDs : JavaSet<number>) : void {
		for (const idErgebnis of listeDerErgebnisIDs) {
			DeveloperNotificationException.ifMapNotContains("_map_idErgebnis_Ergebnis", this._map_idErgebnis_Ergebnis, idErgebnis);
			DeveloperNotificationException.ifMapNotContains("_map_idErgebnis_ErgebnisManager", this._map_idErgebnis_ErgebnisManager, idErgebnis);
		}
		for (const idErgebnis of listeDerErgebnisIDs) {
			const e : GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
			this._daten.ergebnisse.remove(e);
			this._map_idErgebnis_Ergebnis.remove(e.id);
			this._map_idErgebnis_ErgebnisManager.remove(e.id);
		}
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten.
	 *
	 * @param ergebnismenge Die Menge an Ergebnissen.
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public ergebnisRemoveListe(ergebnismenge : List<GostBlockungsergebnis>) : void {
		const listIDs : HashSet<number> = new HashSet<number>();
		for (const e of ergebnismenge)
			listIDs.add(e.id);
		this.ergebnisRemoveListeByIDs(listIDs);
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis  Die Datenbank-ID des zu entfernenden Ergebnisses.
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisRemoveByID(idErgebnis : number) : void {
		this.ergebnisRemoveListeByIDs(SetUtils.create1(idErgebnis));
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis   Das zu entfernende Ergebnis.
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisRemove(ergebnis : GostBlockungsergebnis) : void {
		this.ergebnisRemoveListeByIDs(SetUtils.create1(ergebnis.id));
	}

	/**
	 * Sortiert alle Ergebnisse neu (nach ihrer Bewertung).
	 *
	 * @param ergebnis  Das Ergebnis mit der neuen Bewertung.
	 *
	 * @throws DeveloperNotificationException falls die Daten inkonsistent sind.
	 */
	public ergebnisUpdateBewertung(ergebnis : GostBlockungsergebnis) : void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
		this._daten.ergebnisse.sort(this._compErgebnisse);
	}

	/**
	 * Revalidiert alle Ergebnisse. Dies führt zur Aktualisierung aller Ergebnisse.
	 */
	public ergebnisAlleRevalidieren() : void {
		for (const ergebnisManager of this._map_idErgebnis_ErgebnisManager.values())
			ergebnisManager.stateRevalidateEverything();
	}

	/**
	 * Liefert die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 *
	 * @return die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 */
	public ergebnisGetAnzahl() : number {
		return this._daten.ergebnisse.size();
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 1. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		let summe : number = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Intervall(idErgebnis : number) : number {
		const summe : number = this.ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 2. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		let summe : number = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Intervall(idErgebnis : number) : number {
		const summe : number = this.ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 3. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Intervall(idErgebnis : number) : number {
		let wert : number = this.ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0)
			wert--;
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Den Wert des 4. Bewertungskriteriums.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Wert(idErgebnis : number) : number {
		const e : GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis  Die Datenbank-ID des Ergebnisses.
	 *
	 * @return Eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal.
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Intervall(idErgebnis : number) : number {
		const wert : number = this.ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	private kursAddKursOhneSortierung(kurs : GostBlockungKurs) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idKurs_kurs, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_fach_kursart_kursnummer", this._list_kurse_sortiert_fach_kursart_kursnummer, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("_kurse_sortiert_kursart_fach_kursnummer", this._list_kurse_sortiert_kursart_fach_kursnummer, kurs);
		const liste : List<GostBlockungKurs> | null = Map2DUtils.getOrCreateArrayList(this._map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		this._daten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs  Das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public kursAdd(kurs : GostBlockungKurs) : void {
		this.kursAddListe(ListUtils.create1(kurs));
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge Die Menge an Kursen.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public kursAddListe(kursmenge : List<GostBlockungKurs>) : void {
		const setId : HashSet<number> = new HashSet<number>();
		for (const kAlt of this._daten.kurse)
			setId.add(kAlt.id);
		const nSchienen : number = this.schieneGetAnzahl();
		for (const kNeu of kursmenge) {
			DeveloperNotificationException.ifInvalidID("pKurs.id", kNeu.id);
			DeveloperNotificationException.ifNull("_faecherManager.get(pKurs.fach_id)", this._faecherManager.get(kNeu.fach_id));
			DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kNeu.kursart));
			DeveloperNotificationException.ifTrue("Kurs.wochenstunden " + kNeu.wochenstunden + " < 0", kNeu.wochenstunden < 0);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen " + kNeu.anzahlSchienen + " zu klein!", kNeu.anzahlSchienen < 1);
			DeveloperNotificationException.ifTrue("Kurs.anzahlSchienen " + kNeu.anzahlSchienen + " zu groß!", kNeu.anzahlSchienen > nSchienen);
			DeveloperNotificationException.ifTrue("Kurs.nummer " + kNeu.nummer + " zu klein!", kNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Kurs.id " + kNeu.id + " Dopplung!", !setId.add(kNeu.id));
		}
		for (const gKurs of kursmenge)
			this.kursAddKursOhneSortierung(gKurs);
		this._list_kurse_sortiert_fach_kursart_kursnummer.sort(this._compKurs_fach_kursart_kursnummer);
		this._list_kurse_sortiert_kursart_fach_kursnummer.sort(this._compKurs_kursart_fach_kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs mit der übergebenen ID existiert.
	 */
	public kursGetExistiert(idKurs : number) : boolean {
		return this._map_idKurs_kurs.get(idKurs) !== null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return Die Anzahl an Kursen.
	 */
	public kursGetAnzahl() : number {
		return this._map_idKurs_kurs.size();
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return Den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetName(idKurs : number) : string {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		const sSuffix : string = JavaObject.equalsTranspiler("", (kurs.suffix)) ? "" : ("-" + kurs.suffix);
		const kursart : GostKursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetNameOhneSuffix(idKurs : number) : string {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const gFach : GostFach = this._faecherManager.getOrException(kurs.fach_id);
		const kursart : GostKursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGet(idKurs : number) : GostBlockungKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this._map_idKurs_kurs, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exceptions, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return die Lehrkraft des Kurses, welche die angegebene Nummer hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitNummer(idKurs : number, reihenfolgeNr : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge === reihenfolgeNr)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + this.toStringKurs(idKurs) + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!")
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return Die Lehrkraft des Kurses, welche die angegebene ID hat.
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitID(idKurs : number, idLehrkraft : number) : GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id === idLehrkraft)
				return lehrkraft;
		throw new DeveloperNotificationException("Es gibt im Kurs " + this.toStringKurs(idKurs) + " keine Lehrkraft mit ID " + idLehrkraft + "!")
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param reihenfolgeNr  Die Lehrkraft mit der Nummer, die gesucht wird.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 * @throws DeveloperNotificationException  Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetLehrkraftMitNummerExists(idKurs : number, reihenfolgeNr : number) : boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.reihenfolge === reihenfolgeNr)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 * @param idLehrkraft  Die Datenbank-ID der gesuchten Lehrkraft.
	 *
	 * @return TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 */
	public kursGetLehrkraftMitIDExists(idKurs : number, idLehrkraft : number) : boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs))
			if (lehrkraft.id === idLehrkraft)
				return true;
		return false;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetLehrkraefteSortiert(idKurs : number) : List<GostBlockungKursLehrer> {
		return this.kursGet(idKurs).lehrer;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu.
	 *
	 * @param idKurs         Die Datenbank-ID des Kurses.
	 * @param neueLehrkraft  Das {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public kursAddLehrkraft(idKurs : number, neueLehrkraft : GostBlockungKursLehrer) : void {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (const lehrkraft of listOfLehrer) {
			DeveloperNotificationException.ifTrue(this.toStringKurs(idKurs) + " hat bereits " + this.toStringKursLehrkraft(idKurs, lehrkraft.id), lehrkraft.id === neueLehrkraft.id);
			DeveloperNotificationException.ifTrue(this.toStringKurs(idKurs) + " hat bereits " + this.toStringKursLehrkraft(idKurs, lehrkraft.id) + " mit Reihenfolge " + lehrkraft.reihenfolge, lehrkraft.reihenfolge === neueLehrkraft.reihenfolge);
		}
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(GostBlockungsdatenManager._compLehrkraefte);
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft.
	 *
	 * @param idKurs           Die Datenbank-ID des Kurses.
	 * @param idAlteLehrkraft  Die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public kursRemoveLehrkraft(idKurs : number, idAlteLehrkraft : number) : void {
		const kurs : GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer : List<GostBlockungKursLehrer> = kurs.lehrer;
		for (let i : number = 0; i < listOfLehrer.size(); i++)
			if (listOfLehrer.get(i).id === idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));
				return;
			}
		throw new DeveloperNotificationException(this.toStringKurs(idKurs) + " enthält nicht " + this.toStringKursLehrkraft(idKurs, idAlteLehrkraft))
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public kursGetListeSortiertNachFachKursartNummer() : List<GostBlockungKurs> {
		return this._list_kurse_sortiert_fach_kursart_kursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return Eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 */
	public kursGetListeSortiertNachKursartFachNummer() : List<GostBlockungKurs> {
		return this._list_kurse_sortiert_kursart_fach_kursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortiere Liste der Kurse für das angegebenen Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortiere Liste der Kurse für das Fach und die Kursart
	 */
	public kursGetListeByFachUndKursart(idFach : number, idKursart : number) : List<GostBlockungKurs> {
		const liste : List<GostBlockungKurs> | null = this._map2d_idFach_idKursart_kurse.getOrNull(idFach, idKursart);
		if (liste === null)
			return new ArrayList();
		liste.sort(GostBlockungsdatenManager._compKursnummer);
		return liste;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls ein Löschen des Kurses erlaubt ist.
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetIsRemoveAllowed(idKurs : number) : boolean {
		return (this._map_idKurs_kurs.get(idKurs) !== null) && this.getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetIstVerbotenInSchiene(idKurs : number, idSchiene : number) : boolean {
		if (this.kursGetHatSperrungInSchiene(idKurs, idSchiene))
			return true;
		const nummer : number = this.schieneGet(idSchiene).nummer;
		const kursart : number = this.kursGet(idKurs).kursart;
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS))
			if ((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) {
				if (regel.parameter.get(0) !== kursart)
					return true;
			} else {
				if (regel.parameter.get(0) === kursart)
					return true;
			}
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS))
			if (((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) && (regel.parameter.get(0) === kursart))
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public kursGetHatSperrungInSchiene(idKurs : number, idSchiene : number) : boolean {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public kursGetRegelGesperrtInSchiene(idKurs : number, idSchiene : number) : GostBlockungRegel {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull("" + this.toStringKurs(idKurs) + " ist nicht gesperrt in Schiene " + this.toStringSchiene(idSchiene) + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public kursGetHatFixierungInSchiene(idKurs : number, idSchiene : number) : boolean {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs     Die Datenbank-ID des Kurses.
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert hat.
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public kursGetRegelFixierungInSchiene(idKurs : number, idSchiene : number) : GostBlockungRegel {
		const nrSchiene : number = this.schieneGet(idSchiene).nummer;
		const key : LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull(this.toStringKurs(idKurs) + " ist nicht fixiert in Schiene " + this.toStringSchiene(idSchiene) + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs nicht nicht vollständig fixiert ist.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Kurs nicht nicht vollständig fixiert ist.
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public kursIstWeitereFixierungErlaubt(idKurs : number) : boolean {
		const anzahlSchienen : number = this.kursGet(idKurs).anzahlSchienen;
		let anzahlFixierungen : number = 0;
		for (let nr : number = 1; nr <= this.schieneGetAnzahl(); nr++) {
			const kFixierungAlt : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
			const rFixierungAlt : GostBlockungRegel | null = this.regelGetByLongArrayKeyOrNull(kFixierungAlt);
			if (rFixierungAlt !== null)
				anzahlFixierungen++;
		}
		return anzahlFixierungen < anzahlSchienen;
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 */
	public kursGetRegelDummySchuelerOrNull(idKurs : number) : GostBlockungRegel | null {
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (regel.parameter.get(0) === idKurs)
				return regel;
		return null;
	}

	/**
	 * Liefert ein Set aller Kurs-IDs.
	 *
	 * @return ein Set aller Kurs-IDs.
	 */
	public kursmengeGetSetDerIDs() : JavaSet<number> {
		const setKursID : HashSet<number> = new HashSet<number>();
		for (const kurs of this._list_kurse_sortiert_fach_kursart_kursnummer)
			setKursID.add(kurs.id);
		return setKursID;
	}

	/**
	 * Entfernt alle Kurse mit den übergebenen IDs aus der Blockung.
	 * <br>(1) Überprüft, ob es eine Blockungsvorlage ist und ob alle IDs existieren, sonst Exception.
	 * <br>(2) Entfernt dann alle Kurse aus den Datenstrukturen.
	 * <br>(3) Entfernt dann alle Regeln, die einen der Kurse tangieren.
	 * <br>(4) Dann muss der Client den ErgebnisManager über die Löschung des Kurses informieren.
	 *
	 * @param idKurse  Die Datenbank-IDs der zu entfernenden Kurse.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemoveByID(idKurse : JavaSet<number>) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen von Kursen ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		for (const idKurs of idKurse)
			DeveloperNotificationException.ifTrue("Löschen von Kurs.id=" + idKurs + " nicht möglich, da nicht vorhanden!", !this.kursGetExistiert(idKurs));
		for (const idKurs of idKurse) {
			const kurs : GostBlockungKurs = this.kursGet(idKurs);
			this._list_kurse_sortiert_fach_kursart_kursnummer.remove(kurs);
			this._list_kurse_sortiert_kursart_fach_kursnummer.remove(kurs);
			Map2DUtils.removeFromListAndTrimOrException(this._map2d_idFach_idKursart_kurse, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(this._map_idKurs_kurs, idKurs);
			this._daten.kurse.remove(kurs);
		}
		const regelIDs : HashSet<number> = new HashSet<number>();
		for (const regel of this._daten.regeln)
			for (const idKurs of idKurse)
				if (GostBlockungsdatenManager.regelGetHatKursIDs(regel, idKurs)) {
					regelIDs.add(regel.id);
					break;
				}
		this.regelRemoveListeByIDsOhneRevalidierung(regelIDs);
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs  Die Datenbank-ID des zu entfernenden Kurses.
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursRemoveByID(idKurs : number) : void {
		this.kurseRemoveByID(SetUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param kurs  Der zu entfernende Kurs.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public kursRemove(kurs : GostBlockungKurs) : void {
		this.kurseRemoveByID(SetUtils.create1(kurs.id));
	}

	/**
	 * Entfernt alle {@link GostBlockungKurs}-Objekte.
	 *
	 * @param kurse  Die zu entfernenden {@link GostBlockungKurs}-Objekte.
	 *
	 * @throws DeveloperNotificationException falls einer der Kurse nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemove(kurse : List<GostBlockungKurs>) : void {
		const idKurse : HashSet<number> = new HashSet<number>();
		for (const kursExtern of kurse)
			idKurse.add(kursExtern.id);
		this.kurseRemoveByID(idKurse);
	}

	/**
	 * Kombiniert zwei Kurse zu einem Kurs. Die Regel  {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN}
	 * muss dabei ggf. auch kombiniert werden, wobei eine existierende Regel recycled wird.
	 *
	 * @param idKursID1keep    Die Kurs-ID des Ziel-Kurses (wird nicht gelöscht).
	 * @param idKursID2delete  Die Kurs-ID des Quell-Kurses (wird gelöscht).
	 * @throws DeveloperNotificationException falls es keine Blockungsvorlage ist, oder die Kurse nicht existieren, oder die Kurse identisch sind.
	 */
	public kursMerge(idKursID1keep : number, idKursID2delete : number) : void {
		DeveloperNotificationException.ifTrue("Die Kurse müssen sich unterscheiden!", idKursID1keep === idKursID2delete);
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID1keep + " des Ziel Kurses-gibt es nicht!", !this._map_idKurs_kurs.containsKey(idKursID1keep));
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID2delete + " des Quell-Kurses gibt es nicht!", !this._map_idKurs_kurs.containsKey(idKursID2delete));
		const regelKursKeep : GostBlockungRegel | null = this.regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID1keep);
		const regelKursDelete : GostBlockungRegel | null = this.regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKursID2delete);
		if (regelKursDelete !== null) {
			if (regelKursKeep !== null) {
				const summe : number = regelKursDelete.parameter.get(1) + regelKursKeep.parameter.get(1);
				this.regelRemove(regelKursKeep);
				regelKursKeep.parameter.set(1, summe);
				this.regelAdd(regelKursKeep);
			} else {
				this.regelRemove(regelKursDelete);
				regelKursDelete.parameter.set(0, idKursID1keep);
				this.regelAdd(regelKursDelete);
			}
		}
		this.kurseRemoveByID(SetUtils.create1(idKursID2delete));
	}

	/**
	 * Setzt den Suffix des Kurses.
	 *
	 * @param  idKurs  Die Datenbank-ID des Kurses.
	 * @param  suffix  Der neue Suffix des Kurses.
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public kursSetSuffix(idKurs : number, suffix : string) : void {
		this.kursGet(idKurs).suffix = suffix;
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schiene  Die hinzuzufügende Schiene.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAdd(schiene : GostBlockungSchiene) : void {
		this.schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schienenmenge  Die Menge an Schienen.
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAddListe(schienenmenge : List<GostBlockungSchiene>) : void {
		const setNr : HashSet<number> = new HashSet<number>();
		const setId : HashSet<number> = new HashSet<number>();
		for (const sAlt of this._daten.schienen) {
			setId.add(sAlt.id);
			setNr.add(sAlt.nummer);
		}
		for (const sNeu of schienenmenge) {
			DeveloperNotificationException.ifInvalidID("Schiene.id", sNeu.id);
			DeveloperNotificationException.ifTrue("Schiene.bezeichnung darf nicht leer sein!", JavaObject.equalsTranspiler("", (sNeu.bezeichnung)));
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + sNeu.nummer + " < 1", sNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Schienen-WochenStd. " + sNeu.wochenstunden + " < 1", sNeu.wochenstunden < 1);
			DeveloperNotificationException.ifTrue("Schienen-ID-Dopplung " + sNeu.id, !setId.add(sNeu.id));
			DeveloperNotificationException.ifTrue("Schienen-Nr-Dopplung " + sNeu.id, !setNr.add(sNeu.nummer));
		}
		for (let nr : number = 1; nr <= this._daten.schienen.size() + schienenmenge.size(); nr++)
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + nr + " fehlt in der Reihenfolge!", !setNr.contains(nr));
		for (const schiene of schienenmenge) {
			this._map_idSchiene_schiene.put(schiene.id, schiene);
			this._daten.schienen.add(schiene);
		}
		this._daten.schienen.sort(GostBlockungsdatenManager._compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param  idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return Das zugehörige {@link GostBlockungSchiene} Objekt.
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public schieneGet(idSchiene : number) : GostBlockungSchiene {
		return DeveloperNotificationException.ifNull("_mapSchienen.get(" + idSchiene + ")", this._map_idSchiene_schiene.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 */
	public schieneGetExistiert(idSchiene : number) : boolean {
		return this._map_idSchiene_schiene.get(idSchiene) !== null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 *
	 * @return Die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 */
	public schieneGetListe() : List<GostBlockungSchiene> {
		return new ArrayList<GostBlockungSchiene>(this._daten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.
	 *
	 * @param idSchiene  Die Datenbank-ID der Schiene.
	 *
	 * @return TRUE, falls ein Löschen der Schiene erlaubt ist.
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public schieneGetIsRemoveAllowed(idSchiene : number) : boolean {
		this.schieneGet(idSchiene);
		return this.getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene    Die Datenbank-ID der Schiene.
	 * @param bezeichnung  Die neue Bezeichnung.
	 *
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public schienePatchBezeichnung(idSchiene : number, bezeichnung : string) : void {
		this.schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene      Die Datenbank-ID der Schiene.
	 * @param wochenstunden  Die neuen Wochenstunden.
	 */
	public schienePatchWochenstunden(idSchiene : number, wochenstunden : number) : void {
		this.schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein und die Schiene muss existieren, sonst Exception. <br>
	 * (2) Die Schiene wird entfernt und Schienen mit größerer Nr. werden um 1 reduziert. <br>
	 * (3) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene  Die Datenbank-ID der zu entfernenden Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemoveByID(idSchiene : number) : void {
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const schieneR : GostBlockungSchiene = this.schieneGet(idSchiene);
		for (const eManager of this._map_idErgebnis_ErgebnisManager.values())
			DeveloperNotificationException.ifTrue("Schiene kann nicht gelöscht werden, da sie Kurse enthält!", !eManager.getOfSchieneIstLeer(idSchiene));
		this._map_idSchiene_schiene.remove(idSchiene);
		this._daten.schienen.remove(schieneR);
		for (const schiene of this._daten.schienen)
			if (schiene.nummer > schieneR.nummer)
				schiene.nummer--;
		const setLoeschen : JavaSet<number> = new HashSet<number>();
		const listHinzufuegen : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>();
		for (const r of this._daten.regeln) {
			const a : Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a === null) {
				setLoeschen.add(r.id);
				continue;
			}
			if (DTOUtils.testRegelParameterChanged(r, a)) {
				setLoeschen.add(r.id);
				listHinzufuegen.add(r);
			}
		}
		this.regelRemoveListeByIDsOhneRevalidierung(setLoeschen);
		for (const r of listHinzufuegen) {
			const a : Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a !== null)
				for (let i : number = 0; i < a.length; i++)
					r.parameter.set(i, a[i]);
		}
		this.regelAddListeOhneRevalidierung(listHinzufuegen);
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * <br>Hinweis: Es muss nicht dasselbe Objekt sein, nur die ID muss übereinstimmen.
	 *
	 * @param schiene  Die zu entfernende Schiene.
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemove(schiene : GostBlockungSchiene) : void {
		this.schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return Die Anzahl an Schienen.
	 */
	public schieneGetAnzahl() : number {
		return this._map_idSchiene_schiene.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param  halbjahr  Das Halbjahr, für welches die Blockung angelegt werden soll.
	 *
	 * @return Die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 */
	public static schieneGetDefaultAnzahl(halbjahr : GostHalbjahr) : number {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	private regelAddOhneSortierung(regel : GostBlockungRegel) : void {
		const multikey : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idRegel_regel, regel.id, regel);
		MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ).add(regel);
		this._map_multikey_regeln.put(multikey, regel);
		this._daten.regeln.add(regel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel  Die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public regelAdd(regel : GostBlockungRegel) : void {
		this.regelAddListe(ListUtils.create1(regel));
	}

	private regelAddListeOhneRevalidierung(regelmenge : List<GostBlockungRegel>) : void {
		const setMultiKey : JavaSet<LongArrayKey> = new HashSet<LongArrayKey>();
		const setIDs : JavaSet<number> = new HashSet<number>();
		const menge1 : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>(this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS));
		const menge6 : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>(this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS));
		const menge9 : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>(this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN));
		const menge10 : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>(this.regelGetListeOfTyp(GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN));
		const menge15 : List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>(this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL));
		for (const r of regelmenge) {
			this.regelCheckParameterReferences(r);
			this.regelCheckParameterValues(r);
			this.regelCheckDuplicates(r, setIDs, setMultiKey, menge1, menge6, menge9, menge10, menge15);
		}
		for (const regel of regelmenge)
			this.regelAddOhneSortierung(regel);
		this._daten.regeln.sort(this._compRegel);
		for (const listOfTyp of this._map_regeltyp_regeln.values())
			listOfTyp.sort(this._compRegel);
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge  Die Menge an Regeln.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelAddListe(regelmenge : List<GostBlockungRegel>) : void {
		this.regelAddListeOhneRevalidierung(regelmenge);
		this.ergebnisAlleRevalidieren();
	}

	private regelCheckParameterReferences(r : GostBlockungRegel) : void {
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
		const paramCount : number = typ.getParamCount();
		DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Parameter-Anzahl!", paramCount !== r.parameter.size());
		for (let i : number = 0; i < paramCount; i++) {
			let value : number = r.parameter.get(i).valueOf();
			if (typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.SCHUELER_ID as unknown)
				DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Schüler-ID-Referenz!", this.schuelerGetOrNull(value) === null);
			if (typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.KURS_ID as unknown)
				DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Kurs-ID-Referenz!", !this.kursGetExistiert(value));
			if (typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.SCHIENEN_NR as unknown)
				DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Schienen-Nr-Referenz!", (value < 1) || (value > this.schieneGetAnzahl()));
			if (typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.FACH_ID as unknown)
				DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Fach-ID-Referenz!", this._faecherManager.get(value) === null);
			if (typ.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.KURSART as unknown)
				DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Kursart-Referenz!", GostKursart.fromIDorNull(value as number) === null);
		}
	}

	private regelCheckParameterValues(r : GostBlockungRegel) : void {
		DeveloperNotificationException.ifInvalidID("Regel.id", r.id);
		const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
		const paramCount : number = typ.getParamCount();
		DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " hat falsche Parameter-Anzahl!", paramCount !== r.parameter.size());
		const p : List<number> = r.parameter;
		switch (typ) {
			case GostKursblockungRegelTyp.UNDEFINIERT: {
				throw new DeveloperNotificationException(this.toStringRegel(r.id) + " hat unbekannten Typ (" + r.typ + ")!")
			}
			case GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE:
			case GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE:
			case GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS:
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS:
			case GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS:
			case GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS:
			case GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN:
			case GostKursblockungRegelTyp.SCHUELER_IGNORIEREN:
			case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH:
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH:
			case GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER:
			case GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER:
			case GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN: {
				break;
			}
			case GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS:
			case GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS: {
				if (p.get(2) < p.get(1))
					throw new DeveloperNotificationException("Die BIS-Schiene kann nicht kleiner sein als die VON-Schiene!")
				break;
			}
			case GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN: {
				if (p.get(1) < GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN)
					throw new DeveloperNotificationException("KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit " + p.get(1) + " zu klein!")
				if (p.get(1) > GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX)
					throw new DeveloperNotificationException("KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit " + p.get(1) + " zu groß!")
				break;
			}
			case GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL: {
				if (p.get(1) < GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN)
					throw new DeveloperNotificationException("KURS_MAXIMALE_SCHUELERANZAHL ist mit " + p.get(1) + " zu klein!")
				if (p.get(1) > GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX)
					throw new DeveloperNotificationException("KURS_MAXIMALE_SCHUELERANZAHL ist mit " + p.get(1) + " zu groß!")
				break;
			}
			case GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE: {
				if (p.get(2) < GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN)
					throw new DeveloperNotificationException("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit " + p.get(2) + " zu klein!")
				if (p.get(2) > GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX)
					throw new DeveloperNotificationException("FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit " + p.get(2) + " zu groß!")
				break;
			}
			default: {
				throw new DeveloperNotificationException("Regeltypüberprüfung: Der Regeltyp ist unbekannt!")
			}
		}
	}

	private regelCheckDuplicates(r : GostBlockungRegel, setIDs : JavaSet<number>, setMultiKey : JavaSet<LongArrayKey>, menge1 : List<GostBlockungRegel>, menge6 : List<GostBlockungRegel>, menge9 : List<GostBlockungRegel>, menge10 : List<GostBlockungRegel>, menge15 : List<GostBlockungRegel>) : void {
		DeveloperNotificationException.ifTrue("Regel-ID " + r.id + " Dopplung!", this._map_idRegel_regel.containsKey(r.id));
		DeveloperNotificationException.ifTrue("Regel-ID " + r.id + " Dopplung!", !setIDs.add(r.id));
		const multikey : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(r);
		DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " existiert bereits!", !setMultiKey.add(multikey));
		DeveloperNotificationException.ifTrue(this.toStringRegel(r.id) + " existiert bereits!", this._map_multikey_regeln.containsKey(multikey));
		if (r.typ === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ) {
			for (let rAlt of menge1)
				if (GostBlockungsdatenManager.regelKursartIntervallSchnitt(rAlt, r))
					throw new DeveloperNotificationException("Intervallschnitt bei " + this.toStringRegel(r.id) + " und " + this.toStringRegel(rAlt.id) + "!")
			menge1.add(r);
		}
		if (r.typ === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ) {
			for (let rAlt of menge6)
				if (GostBlockungsdatenManager.regelKursartIntervallSchnitt(rAlt, r))
					throw new DeveloperNotificationException("Intervallschnitt bei " + this.toStringRegel(r.id) + " und " + this.toStringRegel(rAlt.id) + "!")
			menge6.add(r);
		}
		if (r.typ === GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ) {
			for (let rAlt of menge9)
				if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0))))
					throw new DeveloperNotificationException(this.toStringRegel(r.id) + " Regel-Dopplung!")
			menge9.add(r);
		}
		if (r.typ === GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ) {
			if (!menge10.isEmpty())
				throw new DeveloperNotificationException(this.toStringRegel(r.id) + " Regel-Dopplung!")
			menge10.add(r);
		}
		if (r.typ === GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ) {
			for (let rAlt of menge15)
				if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0))))
					throw new DeveloperNotificationException(this.toStringRegel(r.id) + " Regel-Dopplung!")
			menge15.add(r);
		}
	}

	private static regelKursartIntervallSchnitt(r1 : GostBlockungRegel, r2 : GostBlockungRegel) : boolean {
		if (!JavaObject.equalsTranspiler(r1.parameter.get(0), (r2.parameter.get(0))))
			return false;
		const von1 : number = r1.parameter.get(1).valueOf();
		const bis1 : number = r1.parameter.get(2).valueOf();
		const von2 : number = r2.parameter.get(1).valueOf();
		const bis2 : number = r2.parameter.get(2).valueOf();
		return !((bis1 < von2) || (bis2 < von1));
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return Die Anzahl an Regeln.
	 */
	public regelGetAnzahl() : number {
		return this._map_idRegel_regel.size();
	}

	/**
	 * Liefert die Regel mit der übergebenen ID zurück.
	 *
	 * @param idRegel   Die Datenbank-ID der Regel.
	 *
	 * @return die Regel mit der übergebenen ID zurück.
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelGet(idRegel : number) : GostBlockungRegel {
		return DeveloperNotificationException.ifNull("_mapRegeln.get(" + idRegel + ")", this._map_idRegel_regel.get(idRegel));
	}

	/**
	 * Liefert die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 *
	 * @param key  Der {@link LongArrayKey}-Schlüssel.
	 *
	 * @return die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 */
	public regelGetByLongArrayKeyOrNull(key : LongArrayKey) : GostBlockungRegel | null {
		return this._map_multikey_regeln.get(key);
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return Die aktuelle Menge aller Regeln sortiert nach (TYP, id).
	 */
	public regelGetListe() : List<GostBlockungRegel> {
		return this._daten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ Der {@link GostKursblockungRegelTyp}.
	 *
	 * @return die aktuelle Menge aller  Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	public regelGetListeOfTyp(typ : GostKursblockungRegelTyp) : List<GostBlockungRegel> {
		return MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param nrSchiene   Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummyKursGesperrtInSchiene(idKurs : number, nrSchiene : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 * @param nrSchiene   Die Nummer der Schiene.
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummyKursFixierungInSchiene(idKurs : number, nrSchiene : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 */
	public regelGetRegelOrDummySchuelerInKursFixierung(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		const regel : GostBlockungRegel | null = this._map_multikey_regeln.get(key);
		if (regel !== null)
			return regel;
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel   Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls die Regel mit der übergebenen ID existiert.
	 */
	public regelGetExistiert(idRegel : number) : boolean {
		return this._map_idRegel_regel.get(idRegel) !== null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist.
	 * <br> Hinweis: Die alte Implementierung verlangte noch, dass es sich um eine Blockungsvorlage handelt,
	 *               nun reicht es, dass die Regel existiert.
	 *
	 * @param   idRegel Die Datenbank-ID der Regel.
	 *
	 * @return TRUE, falls ein Löschen der Regel erlaubt ist.
	 */
	public regelGetIsRemoveAllowed(idRegel : number) : boolean {
		return this._map_idRegel_regel.containsKey(idRegel);
	}

	private regelGet_KURS_MIT_DUMMY_SUS_AUFFUELLEN(idKurs : number) : GostBlockungRegel | null {
		for (const r of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN))
			if (r.parameter.get(0) === idKurs)
				return r;
		return null;
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 *
	 * @param regel   Das {@link GostBlockungRegel}-Objekt.
	 * @param idKurs  Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 */
	private static regelGetHatKursIDs(regel : GostBlockungRegel, idKurs : number) : boolean {
		const regelTyp : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		for (let i : number = 0; i < regelTyp.getParamCount(); i++)
			if ((regelTyp.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.KURS_ID as unknown) && (regel.parameter.get(i) === idKurs))
				return true;
		return false;
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 *
	 * @param idRegel   Die Datenbank-ID der zu entfernenden Regel.
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelRemoveByID(idRegel : number) : void {
		this.regelRemoveListeByIDs(SetUtils.create1(idRegel));
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge   Die Menge an Regeln, die entfernt werden soll.
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelRemoveListe(regelmenge : List<GostBlockungRegel>) : void {
		const setRegelIDs : HashSet<number> = new HashSet<number>();
		for (const regel of regelmenge)
			setRegelIDs.add(regel.id);
		this.regelRemoveListeByIDs(setRegelIDs);
	}

	private regelRemoveListeByIDsOhneRevalidierung(regelmenge : JavaSet<number>) : void {
		for (const idRegel of regelmenge) {
			const regel : GostBlockungRegel = this.regelGet(idRegel);
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
			DeveloperNotificationException.ifTrue("Der Regeltyp ist undefiniert!", typ as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown);
			DeveloperNotificationException.ifTrue("Die Multi-Map enthält die Regel nicht!", !this._map_multikey_regeln.containsKey(GostBlockungsdatenManager.regelToMultikey(regel)));
		}
		for (const idRegel of regelmenge) {
			const regel : GostBlockungRegel = this.regelGet(idRegel);
			const typ : GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
			const multikey : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
			this._map_idRegel_regel.remove(idRegel);
			MapUtils.getOrCreateArrayList(this._map_regeltyp_regeln, typ).remove(regel);
			this._map_multikey_regeln.remove(multikey);
			this._daten.regeln.remove(regel);
		}
	}

	/**
	 * Löscht eine Menge an Regeln anhand ihrer IDs.
	 *
	 * @param regelmenge   Die Menge der IDs der Regeln.
	 *
	 * @throws DeveloperNotificationException falls mindestens eine Regel nicht existiert.
	 */
	public regelRemoveListeByIDs(regelmenge : JavaSet<number>) : void {
		this.regelRemoveListeByIDsOhneRevalidierung(regelmenge);
		this.ergebnisAlleRevalidieren();
	}

	private static regelToMultikey(regel : GostBlockungRegel) : LongArrayKey {
		let a : Array<number> | null = Array(regel.parameter.size() + 1).fill(0);
		a[0] = regel.typ;
		for (let i : number = 1; i < a.length; i++)
			a[i] = regel.parameter.get(i - 1).valueOf();
		return new LongArrayKey(a);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel   Die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelRemove(regel : GostBlockungRegel) : void {
		this.regelRemoveListeByIDs(SetUtils.create1(regel.id));
	}

	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach   Die Datenbank-ID des Faches.
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 */
	public fachGetMengeKursarten(idFach : number) : List<GostKursart> {
		const idKursarten : HashSet<number> = new HashSet<number>();
		if (this._map2d_idFach_idKursart_kurse.containsKey1(idFach))
			idKursarten.addAll(this._map2d_idFach_idKursart_kurse.getKeySetOf(idFach));
		if (this._map2d_idFach_idKursart_fachwahlen.containsKey1(idFach))
			idKursarten.addAll(this._map2d_idFach_idKursart_fachwahlen.getKeySetOf(idFach));
		const list : List<GostKursart> = new ArrayList<GostKursart>();
		for (const kursart of GostKursart.values())
			if (idKursarten.contains(kursart.id))
				list.add(kursart);
		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 *
	 * @param fachwahl   Die Fachwahl, die hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAdd(fachwahl : GostFachwahl) : void {
		this.fachwahlAddListe(ListUtils.create1(fachwahl));
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge   Die Menge an Fachwahlen.
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAddListe(fachwahlmenge : List<GostFachwahl>) : void {
		const setSchuelerFach : JavaSet<LongArrayKey> = new HashSet<LongArrayKey>();
		for (const fNeu of fachwahlmenge) {
			GostKursart.fromFachwahlOrException(fNeu);
			DeveloperNotificationException.ifTrue("Fachwahl verweist auf ungültig Fach " + fNeu.fachID, this._faecherManager.get(fNeu.fachID) === null);
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", this._map2d_idSchueler_idFach_fachwahl.contains(fNeu.schuelerID, fNeu.fachID));
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", !setSchuelerFach.add(new LongArrayKey(fNeu.schuelerID, fNeu.fachID)));
		}
		for (const fNeu of fachwahlmenge) {
			DeveloperNotificationException.ifMap2DPutOverwrites(this._map2d_idSchueler_idFach_fachwahl, fNeu.schuelerID, fNeu.fachID, fNeu);
			const fachwahlenDesSchuelers : List<GostFachwahl> = MapUtils.getOrCreateArrayList(this._map_idSchueler_fachwahlen, fNeu.schuelerID);
			fachwahlenDesSchuelers.add(fNeu);
			fachwahlenDesSchuelers.sort(this._compFachwahlen);
			const fachartID : number = GostKursart.getFachartIDByFachwahl(fNeu);
			this.fachwahlGetListeOfFachart(fachartID).add(fNeu);
			Map2DUtils.getOrCreateArrayList(this._map2d_idFach_idKursart_fachwahlen, fNeu.fachID, fNeu.kursartID).add(fNeu);
			this._daten.fachwahlen.add(fNeu);
		}
		this._daten.fachwahlen.sort(this._compFachwahlen);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return die Anzahl an Fachwahlen.
	 */
	public fachwahlGetAnzahl() : number {
		return this._daten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * <br> Die Information über den Schüler dieser Fachwahl wird nicht dargestellt.
	 *
	 * @param fachwahl   Das Fachwahl-Objekt.
	 *
	 * @return den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * @throws DeveloperNotificationException falls die Fach-Referenz oder die Kursart-Referenz nicht existiert.
	 */
	public fachwahlGetName(fachwahl : GostFachwahl) : string {
		const gFach : GostFach = this._faecherManager.getOrException(fachwahl.fachID);
		const gKursart : GostKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 * <br> Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart Die Fachart-ID berechnet aus Fach-ID und Kursart-ID.
	 *
	 * @return die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 */
	public fachwahlGetListeOfFachart(idFachart : number) : List<GostFachwahl> {
		const list : List<GostFachwahl> = MapUtils.getOrCreateArrayList(this._map_idFachart_fachwahlen, idFachart);
		list.sort(this._compFachwahlen);
		return list;
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten.
	 *
	 * @return Die Anzahl verschiedenen Kursarten.
	 */
	public fachwahlGetAnzahlVerwendeterKursarten() : number {
		const setKursartenIDs : HashSet<number> = new HashSet<number>();
		for (const fachwahl of this._daten.fachwahlen)
			setKursartenIDs.add(fachwahl.kursartID);
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler  Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private schuelerAddOhneSortierung(schueler : Schueler) : void {
		DeveloperNotificationException.ifMapPutOverwrites(this._map_idSchueler_schueler, schueler.id, schueler);
		if (!this._map_idSchueler_fachwahlen.containsKey(schueler.id))
			this._map_idSchueler_fachwahlen.put(schueler.id, new ArrayList<GostFachwahl>());
		this._daten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.
	 *
	 * @param schueler   Der Schüler, der hinzugefügt wird.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public schuelerAdd(schueler : Schueler) : void {
		this.schuelerAddListe(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge  Die Menge an Schülern.
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public schuelerAddListe(schuelermenge : List<Schueler>) : void {
		const setId : HashSet<number> = new HashSet<number>();
		for (const sAlt of this._daten.schueler)
			setId.add(sAlt.id);
		for (const sNeu of schuelermenge) {
			DeveloperNotificationException.ifInvalidID("schueler.id", sNeu.id);
			DeveloperNotificationException.ifNull("schueler.geschlecht", Geschlecht.fromValue(sNeu.geschlecht));
			DeveloperNotificationException.ifNull("schueler.status", SchuelerStatus.data().getWertByID(sNeu.status));
			DeveloperNotificationException.ifTrue("schueler.id " + sNeu.id + " Dopplung!", !setId.add(sNeu.id));
		}
		for (const schueler of schuelermenge)
			this.schuelerAddOhneSortierung(schueler);
		this._daten.schueler.sort(this._compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 */
	public schuelerGetAnzahlMitMindestensEinerFachwahl() : number {
		const setSchuelerIDs : HashSet<number> | null = new HashSet<number>();
		for (const fachwahl of this._daten.fachwahlen)
			setSchuelerIDs.add(fachwahl.schuelerID);
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return die Anzahl an Schülern.
	 */
	public schuelerGetAnzahl() : number {
		return this._daten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt.
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGet(idSchueler : number) : Schueler {
		return DeveloperNotificationException.ifNull("_map_id_schueler.get(" + idSchueler + ")", this._map_idSchueler_schueler.get(idSchueler));
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Gibt null zurück, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 *
	 * @return Das zugehörige {@link Schueler}-Objekt oder null
	 */
	public schuelerGetOrNull(idSchueler : number) : Schueler | null {
		return this._map_idSchueler_schueler.get(idSchueler);
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return Die aktuelle Menge aller Schüler.
	 */
	public schuelerGetListe() : List<Schueler> {
		return this._daten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idFach      Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}.
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachKursart(idSchueler : number, idFach : number) : GostKursart {
		const fachwahl : GostFachwahl = this.schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}.
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachFachwahl(idSchueler : number, idFach : number) : GostFachwahl {
		return this._map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler, idFach);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Gibt null zurück, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches.
	 *
	 * @return Zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl} oder null.
	 */
	public schuelerGetOfFachFachwahlOrNull(idSchueler : number, idFach : number) : GostFachwahl | null {
		return this._map2d_idSchueler_idFach_fachwahl.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler   Die Datenbank.ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 */
	public schuelerGetHatFach(idSchueler : number, idFach : number) : boolean {
		return this._map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 *
	 * @param idSchueler1   Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2   Die Datenbank-ID des 2. Schülers.
	 * @param idFach        Die Datenbank-ID des Faches
	 *
	 * @return TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 * @throws DeveloperNotificationException falls einer der beiden Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1 : number, idSchueler2 : number, idFach : number) : boolean {
		const fachwahl1 : GostFachwahl = this._map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler1, idFach);
		const fachwahl2 : GostFachwahl = this._map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler2, idFach);
		return fachwahl1.kursartID === fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls es den Schüler gibt mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idFach       Die Datenbank-ID des Faches der Fachwahl des Schülers.
	 * @param idKursart    Die Datenbank-ID der Kursart der Fachwahl des Schülers.
	 *
	 * @return TRUE, falls es den Schüler gibt mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 */
	public schuelerGetHatFachart(idSchueler : number, idFach : number, idKursart : number) : boolean {
		if (!this._map2d_idSchueler_idFach_fachwahl.contains(idSchueler, idFach))
			return false;
		return this._map2d_idSchueler_idFach_fachwahl.getOrException(idSchueler, idFach).kursartID === idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 * <br> Bei ungültiger Schüler-ID wird eine leere Liste geliefert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 *
	 * @return die Menge aller {@link GostFachwahl} des Schülers.
	 */
	public schuelerGetListeOfFachwahlen(idSchueler : number) : List<GostFachwahl> {
		const fachwahlen : List<GostFachwahl> | null = this._map_idSchueler_fachwahlen.get(idSchueler);
		return (fachwahlen === null) ? new ArrayList() : fachwahlen;
	}

	/**
	 * Liefert eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 *
	 * @param idSchueler1   Die Datenbank-ID des 1. Schülers.
	 * @param idSchueler2   Die Datenbank-ID des 2. Schülers.
	 *
	 * @return eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 */
	public schuelerGetFachListeGemeinsamerFacharten(idSchueler1 : number, idSchueler2 : number) : List<GostFach> {
		const temp : List<GostFach> = new ArrayList<GostFach>();
		for (const fachwahl1 of this.schuelerGetListeOfFachwahlen(idSchueler1))
			if (this.schuelerGetHatFachart(idSchueler2, fachwahl1.fachID, fachwahl1.kursartID))
				temp.add(this._faecherManager.getOrException(fachwahl1.fachID));
		return temp;
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 */
	public schuelerGetIstVerbotenInKurs(idSchueler : number, idKurs : number) : boolean {
		const key : LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler  Die Datenbank-ID des Schülers.
	 * @param idKurs      Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelVerbotenInKurs(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull(this.toStringSchueler(idSchueler) + " hat gar kein Verbot für " + this.toStringKurs(idKurs) + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 */
	public schuelerGetIstFixiertInKurs(idSchueler : number, idKurs : number) : boolean {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return this._map_multikey_regeln.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler   Die Datenbank-ID des Schülers.
	 * @param idKurs       Die Datenbank-ID des Kurses.
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert.
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelFixiertInKurs(idSchueler : number, idKurs : number) : GostBlockungRegel {
		const key : LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull(this.toStringSchueler(idSchueler) + " hat gar keine Fixierung für " + this.toStringKurs(idKurs) + "!", this._map_multikey_regeln.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public getID() : number {
		return this._daten.id;
	}

	/**
	 * Setzt die ID dieser Blockung.
	 *
	 * @param   idNeu  Die Datenbank-ID, welche der Blockung zugewiesen wird.
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig ist.
	 */
	public setID(idNeu : number) : void {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", idNeu);
		this._daten.id = idNeu;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return Die maximale Blockungszeit in Millisekunden.
	 */
	public getMaxTimeMillis() : number {
		return this._maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param   blockungszeit   die maximale Blockungszeit in Millisekunden.
	 * @throws DeveloperNotificationException falls der Wert nicht positiv ist.
	 */
	public setMaxTimeMillis(blockungszeit : number) : void {
		DeveloperNotificationException.ifTrue("Der Wert muss positiv sein!", blockungszeit <= 0);
		this._maxTimeMillis = blockungszeit;
	}

	/**
	 * Liefert den Namen der Blockung.
	 *
	 * @return den Namen der Blockung.
	 */
	public getName() : string {
		return this._daten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param name   der Name, welcher der Blockung zugewiesen wird.
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public setName(name : string) : void {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", JavaObject.equalsTranspiler("", (name)));
		this._daten.name = name;
	}

	/**
	 * Liefert das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 */
	public getHalbjahr() : GostHalbjahr {
		return GostHalbjahr.fromIDorException(this._daten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public setHalbjahr(halbjahr : GostHalbjahr) : void {
		this._daten.gostHalbjahr = halbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 */
	public getIstBlockungsVorlage() : boolean {
		return this._daten.ergebnisse.size() === 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return die Anzahl an Fächern.
	 */
	public getFaecherAnzahl() : number {
		return this._faecherManager.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public faecherManager() : GostFaecherManager {
		return this._faecherManager;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public daten() : GostBlockungsdaten {
		return this._daten;
	}

	/**
	 * Liefert eine String-Representation vieler Daten.
	 *
	 * @return eine String-Representation vieler Daten.
	 */
	public getDebugString() : string {
		const sb : StringBuilder = new StringBuilder();
		sb.append("\nErgebnisse = " + this._daten.ergebnisse.size() + "\n");
		sb.append("\nSchienen = " + this._daten.schienen.size() + "\n");
		for (const s of this._daten.schienen) {
			sb.append("    ID=" + s.id + ", NR=" + s.nummer + ", BEZ=" + s.bezeichnung + ", W-STD=" + s.wochenstunden + "\n");
			for (const e of this.ergebnisGetListeSortiertNachID())
				sb.append("    Hat E " + e.id + " Schiene " + s.id + "--> " + this.ergebnisManagerGet(e.id).getOfSchieneExists(s.id) + "\n");
		}
		sb.append("\nSchülermenge = " + this._daten.schueler.size() + "\n");
		for (const s of this._daten.schueler)
			sb.append("    " + s.id + ", " + s.nachname + ", " + s.vorname + "\n");
		sb.append("\nKurse = " + this._daten.kurse.size() + "\n");
		for (const k of this._daten.kurse)
			sb.append("    " + k.id + ", " + k.fach_id + ", " + k.kursart + ", " + k.nummer + "\n");
		sb.append("\nFachwahlen = " + this._daten.fachwahlen.size() + "\n");
		for (const fw of this._daten.fachwahlen)
			sb.append("    " + fw.fachID + ", " + fw.kursartID + ", " + fw.schuelerID + ", " + fw.abiturfach + ", " + fw.istSchriftlich + "\n");
		sb.append("\nRegeln = " + this._daten.regeln.size() + "\n");
		for (const r of this._daten.regeln)
			sb.append("    " + r.id + ", " + r.typ + ", " + r.parameter + "\n");
		return sb.toString();
	}

	/**
	 * Liefert TRUE, falls die Multimap den Test erfolgreich besteht.
	 *
	 * @return TRUE, falls die Multimap den Test erfolgreich besteht.
	 */
	public testMultimap() : boolean {
		if (this._daten.regeln.size() !== this._map_multikey_regeln.size())
			return false;
		for (const r of this._daten.regeln) {
			const key : LongArrayKey = GostBlockungsdatenManager.regelToMultikey(r);
			if (!this._map_multikey_regeln.containsKey(key))
				return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsdatenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

	public static class = new Class<GostBlockungsdatenManager>('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager');

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(obj : unknown) : GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
