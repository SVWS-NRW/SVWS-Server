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
import { System } from '../../../java/lang/System';
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
	 * Damit man nicht immer "\n" schreibt.
	 */
	private readonly lineSeparator: string = System.lineSeparator();

	/**
	 * Die Blockungsdaten, die im Manager vorhanden sind.
	 */
	private readonly dtoDaten: GostBlockungsdaten;

	/**
	 * Der Fächermanager mit den Fächern der gymnasialen Oberstufe.
	 */
	private readonly manFaecher: GostFaecherManager;

	/**
	 * Ein Comparator für Kurse der Blockung. Dieser vergleicht nur die Kursnummern!
	 */
	private readonly compKursnummer: Comparator<GostBlockungKurs> = { compare: (a: GostBlockungKurs, b: GostBlockungKurs) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für Schienen der Blockung
	 */
	private readonly compSchiene: Comparator<GostBlockungSchiene> = { compare: (a: GostBlockungSchiene, b: GostBlockungSchiene) => JavaInteger.compare(a.nummer, b.nummer) };

	/**
	 * Ein Comparator für die Lehrkräfte eines Kurses
	 */
	private readonly compLehrkraefte: Comparator<GostBlockungKursLehrer> = { compare: (a: GostBlockungKursLehrer, b: GostBlockungKursLehrer) => {
		const result: number = JavaInteger.compare(a.reihenfolge, b.reihenfolge);
		if (result !== 0) {
			return result;
		}
		return JavaLong.compare(a.id, b.id);
	} };

	/**
	 * Ein Comparator für die Ergebnisse sortiert nach ID.
	 */
	private readonly compErgebnisseNachID: Comparator<GostBlockungsergebnis> = { compare: (a: GostBlockungsergebnis, b: GostBlockungsergebnis) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Comparator für die Schüler.
	 */
	private readonly compSchueler: Comparator<Schueler>;

	/**
	 * Ein Comparator für die Fachwahlen (SCHÜLERID, FACH, KURSART)
	 */
	private readonly compFachwahlen: Comparator<GostFachwahl>;

	/**
	 * Ein Comparator für die {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 */
	private readonly compErgebnisse: Comparator<GostBlockungsergebnis> = new GostBlockungsergebnisComparator();

	/**
	 * Ein Comparator für Kurse der Blockung (KURSART, FACH, KURSNUMMER)
	 */
	private readonly compKurs1kursart2fach3kursnummer: Comparator<GostBlockungKurs>;

	/**
	 * Ein Comparator für Kurse der Blockung (FACH, KURSART, KURSNUMMER).
	 */
	private readonly compKurs1fach2kursart3kursnummer: Comparator<GostBlockungKurs>;

	/**
	 * Ein Comparator für Regeln der Blockung
	 */
	private readonly compRegel: Comparator<GostBlockungRegel>;

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Kurse anhand ihrer Datenbank-ID.
	 */
	private readonly kursById: HashMap<number, GostBlockungKurs> = new HashMap<number, GostBlockungKurs>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Kurse, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly kursmengeByFachIdAndKursartId: HashMap2D<number, number, List<GostBlockungKurs>> = new HashMap2D<number, number, List<GostBlockungKurs>>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Listen der Fachwahlen, welche Fach und Kursart gemeinsam haben, anhand der beiden IDs.
	 */
	private readonly fachwahlmengeByFachIdAndKursartId: HashMap2D<number, number, List<GostFachwahl>> = new HashMap2D<number, number, List<GostFachwahl>>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schienen anhand ihrer Datenbank-ID.
	 */
	private readonly schieneById: HashMap<number, GostBlockungSchiene> = new HashMap<number, GostBlockungSchiene>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln anhand ihrer Datenbank-ID.
	 */
	private readonly regelById: HashMap<number, GostBlockungRegel> = new HashMap<number, GostBlockungRegel>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly regelmengeByRegeltyp: JavaMap<GostKursblockungRegelTyp, List<GostBlockungRegel>> = new ArrayMap<GostKursblockungRegelTyp, List<GostBlockungRegel>>(GostKursblockungRegelTyp.values());

	/**
	 * Eine interne Hashmap zum Multi-Key-Zugriff auf die Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 */
	private readonly regelByMultikey: HashMap<LongArrayKey, GostBlockungRegel> = new HashMap<LongArrayKey, GostBlockungRegel>();

	/**
	 * Eine interne Hashmap zum schnellen Zugriff auf die Schüler anhand ihrer Datenbank-ID.
	 */
	private readonly schuelerById: HashMap<number, Schueler> = new HashMap<number, Schueler>();

	/**
	 * Schüler-ID --> List<Fachwahl> = Die Fachwahlen des Schülers der jeweiligen Fachart.
	 */
	private readonly fachwahlmengeBySchuelerId: HashMap<number, List<GostFachwahl>> = new HashMap<number, List<GostFachwahl>>();

	/**
	 * (Schüler-ID, Fach-ID) --> Kursart = Die Fachwahl des Schülers die dem Fach die Kursart zuordnet.
	 */
	private readonly fachwahlBySchuelerIdAndFachId: HashMap2D<number, number, GostFachwahl> = new HashMap2D<number, number, GostFachwahl>();

	/**
	 * Fachart-ID --> List<Fachwahl> = Die Fachwahlen einer Fachart.
	 */
	private readonly fachwahlmengeByFachartId: HashMap<number, List<GostFachwahl>> = new HashMap<number, List<GostFachwahl>>();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnis}
	 */
	private readonly ergebnisById: HashMap<number, GostBlockungsergebnis> = new HashMap<number, GostBlockungsergebnis>();

	/**
	 * Ergebnis-ID --> {@link GostBlockungsergebnisManager}
	 */
	private readonly ergebnisManagerByErgebnisId: HashMap<number, GostBlockungsergebnisManager> = new HashMap<number, GostBlockungsergebnisManager>();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (FACH, KURSART, KURSNUMMER).
	 */
	private readonly kursmengeSortiertNachFachKursartKursnummer: List<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();

	/**
	 * Eine sortierte, gecachte Menge der Kurse nach: (KURSART, FACH, KURSNUMMER)
	 */
	private readonly kursmengeSortiertNachKursartFachKursnummer: List<GostBlockungKurs> = new ArrayList<GostBlockungKurs>();

	/**
	 * Die maximale Zeit in Millisekunden die der Blockungsalgorithmus verwenden darf.
	 */
	private maxTimeMillis: number = 1000;

	/**
	 * Map ungültiger Regeln, bei denen Fehlern vorliegen und Map die den jeweiligen Fehler beschreibt.
	 */
	private readonly regelUngueltigById: HashMap<number, GostBlockungRegel> = new HashMap<number, GostBlockungRegel>();

	private readonly regelUngueltigBeschreibungById: HashMap<number, string> = new HashMap<number, string>();


	/**
	 * Erstellt einen neuen Manager mit den angegebenen Blockungsdaten und dem Fächer-Manager.
	 *
	 * @param daten            die Blockungsdaten
	 * @param faecherManager   der Fächer-Manager
	 */
	public constructor(daten: GostBlockungsdaten, faecherManager: GostFaecherManager) {
		super();
		this.manFaecher = faecherManager;
		this.compKurs1fach2kursart3kursnummer = this.createComparatorKurs1Fach2Kursart3Nummer();
		this.compKurs1kursart2fach3kursnummer = this.createComparatorKurs1Kursart2Fach3Nummer();
		this.compFachwahlen = this.createComparatorFachwahlen();
		this.compRegel = this.createComparatorRegeln();
		this.compSchueler = this.createComparatorSchueler();
		this.dtoDaten = new GostBlockungsdaten();
		this.dtoDaten.id = daten.id;
		this.dtoDaten.name = daten.name;
		this.dtoDaten.abijahrgang = daten.abijahrgang;
		this.dtoDaten.gostHalbjahr = daten.gostHalbjahr;
		this.dtoDaten.istAktiv = daten.istAktiv;
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
	 * @param kursart   die ID der Kursart
	 *
	 * @return eine Kurzdarstellung der Kursart
	 */
	public toStringKursartSimple(kursart: number): string {
		const gKursart: GostKursart | null = GostKursart.fromIDorNull(kursart);
		return (gKursart === null) ? ("[Kursart-ID = " + kursart + " (ohne Mapping)]") : gKursart.kuerzel;
	}

	/**
	 * Liefert möglichst viele Informationen zum Kurs mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return möglichst viele Informationen zum Kurs
	 */
	public toStringKurs(idKurs: number): string {
		const kurs: GostBlockungKurs | null = this.kursById.get(idKurs);
		if (kurs === null) {
			return JavaString.format("[Kurs-ID=%d nicht im Mapping]", idKurs);
		}
		const gFach: GostFach | null = this.manFaecher.get(kurs.fach_id);
		let sFach: string = "Fach-ID = " + kurs.fach_id + " (ohne Mapping)";
		if (gFach !== null) {
			sFach = (gFach.kuerzelAnzeige === null) ? ("Fach-ID = " + kurs.fach_id + " (ohne 'kuerzelAnzeige')") : gFach.kuerzelAnzeige;
		}
		return "[Kurs " + sFach + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return eine Kurzdarstellung des Kurses
	 */
	public toStringKursSimple(idKurs: number): string {
		const kurs: GostBlockungKurs | null = this.kursById.get(idKurs);
		if (kurs === null) {
			return JavaString.format("[Kurs (%d) nicht vorhanden]", idKurs);
		}
		return "(" + kurs.id + ") " + this.toStringFachSimple(kurs.fach_id) + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet).
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return eine Kurzdarstellung des Kurses (ohne ID, außer der ID ist kein Kurs zugeordnet)
	 */
	public toStringKursSimpleOhneID(idKurs: number): string {
		const kurs: GostBlockungKurs | null = this.kursById.get(idKurs);
		if (kurs === null) {
			return JavaString.format("[Kurs-ID %d nicht zugeordnet]", idKurs);
		}
		return this.toStringFachSimple(kurs.fach_id) + "-" + this.toStringKursartSimple(kurs.kursart) + kurs.nummer + (JavaString.isEmpty(kurs.suffix) ? "" : "-") + kurs.suffix;
	}

	/**
	 * Liefert eine Kurzdarstellung des Faches mit der übergebenen ID.
	 *
	 * @param idFach   die Datenbank-ID des Faches
	 *
	 * @return eine Kurzdarstellung des Faches
	 */
	public toStringFachSimple(idFach: number): string {
		const gFach: GostFach | null = this.manFaecher.get(idFach);
		if (gFach === null) {
			return JavaString.format("[Fach-ID = %d (ohne Mapping)]", idFach);
		}
		if (gFach.kuerzelAnzeige === null) {
			return JavaString.format("[Fach-ID = %d (ohne 'kuerzelAnzeige')]", idFach);
		}
		return gFach.kuerzelAnzeige;
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFach    die Datenbank-ID des Faches
	 * @param kursart   die Datenbank-ID der Kursart
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart)
	 */
	public toStringFachartSimple(idFach: number, kursart: number): string {
		return this.toStringFachSimple(idFach) + "-" + this.toStringKursartSimple(kursart);
	}

	/**
	 * Liefert eine Kurzdarstellung der Fachart (Fach, Kursart).
	 *
	 * @param idFachart   die Fachart (zusammengesetzt aus Fach und Kursart)
	 *
	 * @return eine Kurzdarstellung der Fachart (Fach, Kursart)
	 */
	public toStringFachartSimpleByFachartID(idFachart: number): string {
		const idFach: number = GostKursart.getFachID(idFachart);
		const kursart: number = GostKursart.getKursartID(idFachart);
		return this.toStringFachSimple(idFach) + "-" + this.toStringKursartSimple(kursart);
	}

	/**
	 * Liefert möglichst viele Informationen zum Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return möglichst viele Informationen zum Schüler
	 */
	public toStringSchueler(idSchueler: number): string {
		const schueler: Schueler | null = this.schuelerById.get(idSchueler);
		if (schueler === null) {
			return JavaString.format("[Schüler (%d) ohne Mapping]", idSchueler);
		}
		return "[Schüler (" + schueler.id + "): " + schueler.nachname + ", " + schueler.vorname + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung des Schüler mit der übergebenen ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return eine Kurzdarstellung des Schülers
	 */
	public toStringSchuelerSimple(idSchueler: number): string {
		const schueler: Schueler | null = this.schuelerById.get(idSchueler);
		if (schueler === null) {
			return JavaString.format("[Schüler (%d) ohne Mapping]", idSchueler);
		}
		return schueler.nachname + ", " + schueler.vorname;
	}

	/**
	 * Liefert möglichst viele Informationen zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return möglichst viele Informationen zur Schiene
	 */
	public toStringSchiene(idSchiene: number): string {
		const schiene: GostBlockungSchiene | null = this.schieneById.get(idSchiene);
		if (schiene === null) {
			return JavaString.format("[Schiene (%d) ohne Mapping]", idSchiene);
		}
		return "[Schiene: ID " + schiene.id + ", Nr. " + schiene.nummer + ", Bez. " + schiene.bezeichnung + ", Stunden " + schiene.wochenstunden + "]";
	}

	/**
	 * Liefert eine Kurzdarstellung zur Schiene mit der übergebenen ID.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return eine Kurzdarstellung zur Schiene
	 */
	public toStringSchieneSimple(idSchiene: number): string {
		const schiene: GostBlockungSchiene | null = this.schieneById.get(idSchiene);
		if (schiene === null) {
			return JavaString.format("[Schiene (%d) ohne Mapping]", idSchiene);
		}
		return "Schiene Nr. " + schiene.nummer;
	}

	/**
	 * Liefert möglichst viele Informationen zur Lehrkraft mit der übergebenen ID.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der Lehrkraft
	 *
	 * @return möglichst viele Informationen zur Lehrkraft
	 */
	public toStringKursLehrkraft(idKurs: number, idLehrkraft: number): string {
		const kurs: GostBlockungKurs | null = this.kursById.get(idKurs);
		if (kurs === null) {
			return JavaString.format("[Lehrkraft (ID=%d)]", idLehrkraft);
		}
		for (const lehrer of kurs.lehrer) {
			if (lehrer.id === idLehrkraft) {
				return "[Lehrkraft (ID=" + idLehrkraft + ") " + lehrer.kuerzel + "]";
			}
		}
		return JavaString.format("[Lehrkraft (ID=%d)]", idLehrkraft);
	}

	/**
	 * Liefert eine Kurzdarstellung zur übergebenen Fachwahl eines Schülers.
	 *
	 * @param gFachwahl   das {@link GostFachwahl}-Objekt
	 *
	 * @return eine Kurzdarstellung zur Fachwahl eines Schülers
	 */
	public toStringFachwahlSimple(gFachwahl: GostFachwahl): string | null {
		return this.toStringSchuelerSimple(gFachwahl.schuelerID) + " wählt " + this.toStringFachartSimple(gFachwahl.fachID, gFachwahl.kursartID);
	}

	/**
	 * Liefert möglichst viele Informationen zur Regel mit der übergebenen ID.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return möglichst viele Informationen zur Regel
	 */
	public toStringRegel(idRegel: number): string {
		const regel: GostBlockungRegel | null = this.regelById.get(idRegel);
		if (regel === null) {
			return JavaString.format("[Regel (%d) ohne Mapping]", idRegel);
		}
		return "[Regel (" + regel.id + ", Nr. " + regel.typ + "): " + regel.parameter + "]";
	}

	private createComparatorRegeln(): Comparator<GostBlockungRegel> {
		const comp: Comparator<GostBlockungRegel> = { compare: (a: GostBlockungRegel, b: GostBlockungRegel) => {
			const cmp1: number = JavaInteger.compare(a.typ, b.typ);
			if (cmp1 !== 0) {
				return cmp1;
			}
			const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(a.typ);
			let cmp2: number;
			const _seexpr_1160507597 = (typ);
			if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE) {
				cmp2 = this.compareRegel1Kurs2Nummer3Id(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE) {
				cmp2 = this.compareRegel1Kurs2Nummer3Id(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
				cmp2 = this.compareRegel1Schueler2Kurs(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
				cmp2 = this.compareRegel1Schueler2Kurs(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS) {
				cmp2 = this.compareRegel1Kurs2Kurs(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS) {
				cmp2 = this.compareRegel1Kurs2Kurs(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN) {
				cmp2 = this.compareRegel1Kurs2Id(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL) {
				cmp2 = this.compareRegel1Kurs2Id(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN) {
				cmp2 = this.compareRegel1Kurs2Id(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH) {
				cmp2 = this.compareRegel1Schueler2Schueler3Fach(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH) {
				cmp2 = this.compareRegel1Schueler2Schueler3Fach(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER) {
				cmp2 = this.compareRegel1Schueler2Schueler(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER) {
				cmp2 = this.compareRegel1Schueler2Schueler(a, b);
			} else if (_seexpr_1160507597 === GostKursblockungRegelTyp.SCHUELER_IGNORIEREN) {
				cmp2 = this.compareRegelSchueler(a, b);
			} else {
				cmp2 = 0;
			}
			;
			if (cmp2 !== 0) {
				return cmp2;
			}
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private createComparatorSchueler(): Comparator<Schueler> {
		const comp: Comparator<Schueler> = { compare: (a: Schueler, b: Schueler) => {
			const cmpSchueler: number = this.compareSchueler1Nachname2Vorname3Id(a.id, b.id);
			if (cmpSchueler !== 0) {
				return cmpSchueler;
			}
			return JavaLong.compare(a.id, b.id);
		} };
		return comp;
	}

	private createComparatorFachwahlen(): Comparator<GostFachwahl> {
		const comp: Comparator<GostFachwahl> = { compare: (a: GostFachwahl, b: GostFachwahl) => {
			const cmpSchueler: number = this.compareSchueler1Nachname2Vorname3Id(a.schuelerID, b.schuelerID);
			if (cmpSchueler !== 0) {
				return cmpSchueler;
			}
			const cmpFach: number = this.compareFach(a.fachID, b.fachID);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			return JavaInteger.compare(a.kursartID, b.kursartID);
		} };
		return comp;
	}

	private createComparatorKurs1Fach2Kursart3Nummer(): Comparator<GostBlockungKurs> {
		const comp: Comparator<GostBlockungKurs> = { compare: (a: GostBlockungKurs, b: GostBlockungKurs) => {
			const cmpFach: number = this.compareFach(a.fach_id, b.fach_id);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			const cmpKursart: number = JavaInteger.compare(a.kursart, b.kursart);
			if (cmpKursart !== 0) {
				return cmpKursart;
			}
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private createComparatorKurs1Kursart2Fach3Nummer(): Comparator<GostBlockungKurs> {
		const comp: Comparator<GostBlockungKurs> = { compare: (a: GostBlockungKurs, b: GostBlockungKurs) => {
			const k1: number = (a.kursart === GostKursart.ZK.id) ? GostKursart.GK.id : a.kursart;
			const k2: number = (b.kursart === GostKursart.ZK.id) ? GostKursart.GK.id : b.kursart;
			const cmpKursartGKZK: number = JavaInteger.compare(k1, k2);
			if (cmpKursartGKZK !== 0) {
				return cmpKursartGKZK;
			}
			const cmpFach: number = this.compareFach(a.fach_id, b.fach_id);
			if (cmpFach !== 0) {
				return cmpFach;
			}
			const cmpKursart: number = JavaInteger.compare(a.kursart, b.kursart);
			if (cmpKursart !== 0) {
				return cmpKursart;
			}
			return JavaInteger.compare(a.nummer, b.nummer);
		} };
		return comp;
	}

	private compareRegel1Kurs2Id(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpKurs1: number = this.compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0) {
			return cmpKurs1;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel1Kurs2Nummer3Id(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpKurs1: number = this.compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0) {
			return cmpKurs1;
		}
		const cmpSchienenNr: number = JavaLong.compare(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchienenNr !== 0) {
			return cmpSchienenNr;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegelSchueler(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpSchueler1: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0) {
			return cmpSchueler1;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel1Schueler2Kurs(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpSchueler1: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0) {
			return cmpSchueler1;
		}
		const cmpKurs1: number = this.compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs1 !== 0) {
			return cmpKurs1;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel1Kurs2Kurs(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpKurs1: number = this.compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpKurs1 !== 0) {
			return cmpKurs1;
		}
		const cmpKurs2: number = this.compareKurs1Kursart2Fach3Nummer4Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpKurs2 !== 0) {
			return cmpKurs2;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel1Schueler2Schueler3Fach(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpSchueler1: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0) {
			return cmpSchueler1;
		}
		const cmpSchueler2: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 !== 0) {
			return cmpSchueler2;
		}
		const cmpFach: number = this.compareFach(a.parameter.get(2), b.parameter.get(2));
		if (cmpFach !== 0) {
			return cmpFach;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareRegel1Schueler2Schueler(a: GostBlockungRegel, b: GostBlockungRegel): number {
		const cmpSchueler1: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(0), b.parameter.get(0));
		if (cmpSchueler1 !== 0) {
			return cmpSchueler1;
		}
		const cmpSchueler2: number = this.compareSchueler1Nachname2Vorname3Id(a.parameter.get(1), b.parameter.get(1));
		if (cmpSchueler2 !== 0) {
			return cmpSchueler2;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareSchueler1Nachname2Vorname3Id(idSchueler1: number, idSchueler2: number): number {
		const a: Schueler | null = this.schuelerById.get(idSchueler1);
		const b: Schueler | null = this.schuelerById.get(idSchueler2);
		if (a === null) {
			return (b === null) ? 0 : -1;
		}
		if (b === null) {
			return +1;
		}
		const cNachname: number = JavaString.compareTo(a.nachname, b.nachname);
		if (cNachname !== 0) {
			return cNachname;
		}
		const cVorname: number = JavaString.compareTo(a.vorname, b.vorname);
		if (cVorname !== 0) {
			return cVorname;
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareFach(idFach1: number, idFach2: number): number {
		const aFach: GostFach | null = this.manFaecher.get(idFach1);
		const bFach: GostFach | null = this.manFaecher.get(idFach2);
		if (aFach === null) {
			return (bFach === null) ? 0 : -1;
		}
		return (bFach === null) ? +1 : GostFaecherManager.comp.compare(aFach, bFach);
	}

	private compareKurs1Kursart2Fach3Nummer4Id(idKurs1: number, idKurs2: number): number {
		const aKurs: GostBlockungKurs | null = this.kursById.get(idKurs1);
		const bKurs: GostBlockungKurs | null = this.kursById.get(idKurs2);
		if (aKurs === null) {
			return (bKurs === null) ? 0 : -1;
		}
		if (bKurs === null) {
			return +1;
		}
		const cmpKursart: number = JavaLong.compare(aKurs.kursart, bKurs.kursart);
		if (cmpKursart !== 0) {
			return cmpKursart;
		}
		const cmpFach: number = this.compareFach(aKurs.fach_id, bKurs.fach_id);
		if (cmpFach !== 0) {
			return cmpFach;
		}
		const cmpNummer: number = JavaLong.compare(aKurs.nummer, bKurs.nummer);
		if (cmpNummer !== 0) {
			return cmpNummer;
		}
		return JavaLong.compare(aKurs.id, bKurs.id);
	}

	/**
	 * Fügt das übergebenen Ergebnis der Blockung hinzu.
	 *
	 * @param ergebnis   das {@link GostBlockungsergebnis}-Objekt, welches hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public ergebnisAdd(ergebnis: GostBlockungsergebnis): void {
		this.ergebnisAddListe(ListUtils.create1(ergebnis));
	}

	/**
	 * Fügt die Menge an Ergebnissen {@link GostBlockungsergebnis} hinzu.
	 *
	 * @param ergebnismenge   die Menge an Ergebnissen
	 *
	 * @throws DeveloperNotificationException Falls in den Daten Inkonsistenzen sind.
	 */
	public ergebnisAddListe(ergebnismenge: List<GostBlockungsergebnis>): void {
		const setId: HashSet<number> = new HashSet<number>(this.ergebnisById.keySet());
		for (const ergebnis of ergebnismenge) {
			DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
			DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
			DeveloperNotificationException.ifNull("GostHalbjahr.fromID(" + ergebnis.gostHalbjahr + ")", GostHalbjahr.fromID(ergebnis.gostHalbjahr));
			DeveloperNotificationException.ifTrue("Ergebnis-ID " + ergebnis.id + " Doppelung!", !setId.add(ergebnis.id));
		}
		for (const ergebnis of ergebnismenge) {
			const ergebnisManager: GostBlockungsergebnisManager | null = new GostBlockungsergebnisManager(this, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(this.ergebnisById, ergebnis.id, ergebnis);
			DeveloperNotificationException.ifMapPutOverwrites(this.ergebnisManagerByErgebnisId, ergebnis.id, ergebnisManager);
			this.dtoDaten.ergebnisse.add(ergebnis);
		}
		this.dtoDaten.ergebnisse.sort(this.compErgebnisse);
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnis} aus der Liste der Ergebnisse.
	 * Wirft eine Exception, falls es keinen Listeneintrag mit dieser ID gibt.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return einen {@link GostBlockungsergebnis}
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGet(idErgebnis: number): GostBlockungsergebnis {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", this.ergebnisById.get(idErgebnis));
	}

	/**
	 * Liefert einen {@link GostBlockungsergebnisManager} für das Ergebnis mit der übergebenen ID.
	 * Wirft eine Exception, falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return einen {@link GostBlockungsergebnisManager}
	 * @throws DeveloperNotificationException Falls es keinen Manager für ein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisManagerGet(idErgebnis: number): GostBlockungsergebnisManager {
		return DeveloperNotificationException.ifNull("Es wurde kein Ergebnis mit ID(" + idErgebnis + ") gefunden!", this.ergebnisManagerByErgebnisId.get(idErgebnis));
	}

	/**
	 * Liefert TRUE, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return true, falls ein {@link GostBlockungsergebnisManager}-Objekt mit der ID existiert
	 */
	public ergebnisManagerExists(idErgebnis: number): boolean {
		return this.ergebnisManagerByErgebnisId.containsKey(idErgebnis);
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostBlockungsergebnisManager}.
	 *
	 * @return die sortierte Menge aller {@link GostBlockungsergebnisManager}
	 */
	public ergebnisManagerGetListeUnsortiert(): List<GostBlockungsergebnisManager> {
		return new ArrayList<GostBlockungsergebnisManager>(this.ergebnisManagerByErgebnisId.values());
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung.
	 *
	 * @return eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer Bewertung
	 */
	public ergebnisGetListeSortiertNachBewertung(): List<GostBlockungsergebnis> {
		return new ArrayList<GostBlockungsergebnis>(this.dtoDaten.ergebnisse);
	}

	/**
	 * Liefert eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID.
	 *
	 * @return eine sortierte Menge der {@link GostBlockungsergebnis} nach ihrer ID
	 */
	public ergebnisGetListeSortiertNachID(): List<GostBlockungsergebnis> {
		const list: List<GostBlockungsergebnis> = new ArrayList<GostBlockungsergebnis>(this.dtoDaten.ergebnisse);
		list.sort(this.compErgebnisseNachID);
		return list;
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten anhand ihrer ID.
	 *
	 * @param listeDerErgebnisIDs   die IDs der Ergebnisse
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public ergebnisRemoveListeByIDs(listeDerErgebnisIDs: JavaSet<number>): void {
		for (const idErgebnis of listeDerErgebnisIDs) {
			DeveloperNotificationException.ifMapNotContains("Ergebnis-Map", this.ergebnisById, idErgebnis);
			DeveloperNotificationException.ifMapNotContains("ErgebnisManager-Map", this.ergebnisManagerByErgebnisId, idErgebnis);
		}
		for (const idErgebnis of listeDerErgebnisIDs) {
			const e: GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
			this.dtoDaten.ergebnisse.remove(e);
			this.ergebnisById.remove(e.id);
			this.ergebnisManagerByErgebnisId.remove(e.id);
		}
	}

	/**
	 * Entfernt die Menge an {@link GostBlockungsergebnis}-Objekten.
	 *
	 * @param ergebnismenge   die Menge an Ergebnissen
	 *
	 * @throws DeveloperNotificationException Falls es keine Ergebnisse mit diesen IDs gibt.
	 */
	public ergebnisRemoveListe(ergebnismenge: List<GostBlockungsergebnis>): void {
		const listIDs: HashSet<number> = new HashSet<number>();
		for (const e of ergebnismenge) {
			listIDs.add(e.id);
		}
		this.ergebnisRemoveListeByIDs(listIDs);
	}

	/**
	 * Entfernt das Ergebnis mit der übergebenen ID aus der Blockung.
	 *
	 * @param idErgebnis   die Datenbank-ID des zu entfernenden Ergebnisses
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisRemoveByID(idErgebnis: number): void {
		this.ergebnisRemoveListeByIDs(SetUtils.create1(idErgebnis));
	}

	/**
	 * Entfernt das übergebenen Ergebnis aus der Blockung.
	 *
	 * @param ergebnis   das zu entfernende Ergebnis
	 *
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisRemove(ergebnis: GostBlockungsergebnis): void {
		this.ergebnisRemoveListeByIDs(SetUtils.create1(ergebnis.id));
	}

	/**
	 * Sortiert alle Ergebnisse neu (nach ihrer Bewertung).
	 *
	 * @param ergebnis   das Ergebnis mit der neuen Bewertung
	 *
	 * @throws DeveloperNotificationException falls die Daten inkonsistent sind.
	 */
	public ergebnisUpdateBewertung(ergebnis: GostBlockungsergebnis): void {
		DeveloperNotificationException.ifInvalidID("pErgebnis.id", ergebnis.id);
		DeveloperNotificationException.ifInvalidID("pErgebnis.blockungID", ergebnis.blockungID);
		this.dtoDaten.ergebnisse.sort(this.compErgebnisse);
	}

	/**
	 * Revalidiert alle Ergebnisse. Dies führt zur Aktualisierung aller Ergebnisse.
	 */
	public ergebnisAlleRevalidieren(): void {
		for (const ergebnisManager of this.ergebnisManagerByErgebnisId.values()) {
			ergebnisManager.stateRevalidateEverything();
		}
	}

	/**
	 * Liefert die aktuelle Anzahl an Ergebnissen, die im Manager gespeichert sind.
	 *
	 * @return die Anzahl an Ergebnissen
	 */
	public ergebnisGetAnzahl(): number {
		return this.dtoDaten.ergebnisse.size();
	}

	/**
	 * Liefert den Wert des 1. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 1. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Wert(idErgebnis: number): number {
		const e: GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		let summe: number = 0;
		summe += e.bewertung.anzahlKurseNichtZugeordnet;
		summe += e.bewertung.regelVerletzungen.size();
		return summe;
	}

	/**
	 * Liefert eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der Regelverletzungen. <br>
	 * - Die Anzahl der nicht genügend gesetzten Kurse. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 1. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es keinen Listeneintrag mit dieser ID gibt.
	 */
	public ergebnisGetBewertung1Intervall(idErgebnis: number): number {
		const summe: number = this.ergebnisGetBewertung1Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 2. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 2. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Wert(idErgebnis: number): number {
		const e: GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		let summe: number = 0;
		summe += e.bewertung.anzahlSchuelerNichtZugeordnet;
		summe += e.bewertung.anzahlSchuelerKollisionen;
		return summe;
	}

	/**
	 * Liefert eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl der nicht zugeordneten Schülerfachwahlen. <br>
	 * - Die Anzahl der Schülerkollisionen. <br>
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 2. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung2Intervall(idErgebnis: number): number {
		const summe: number = this.ergebnisGetBewertung2Wert(idErgebnis);
		return 1 - (1 / ((0.25 * summe) + 1));
	}

	/**
	 * Liefert den Wert des 3. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 3. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Wert(idErgebnis: number): number {
		const e: GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		return e.bewertung.kursdifferenzMax;
	}

	/**
	 * Liefert eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Größte Kursdifferenz. <br>
	 * Der Wert 0 und 1 werden unterschieden, sind aber von der Bewertung her Äquivalent.
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 3. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung3Intervall(idErgebnis: number): number {
		let wert: number = this.ergebnisGetBewertung3Wert(idErgebnis);
		if (wert > 0) {
			wert--;
		}
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	/**
	 * Liefert den Wert des 4. Bewertungskriteriums. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return den Wert des 4. Bewertungskriteriums
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Wert(idErgebnis: number): number {
		const e: GostBlockungsergebnis = this.ergebnisGet(idErgebnis);
		return e.bewertung.anzahlKurseMitGleicherFachartProSchiene;
	}

	/**
	 * Liefert eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal. Darin enthalten sind: <br>
	 * - Die Anzahl an Kursen mit gleicher Fachart (Fach, Kursart) in einer Schiene. <br>
	 * Dieses Bewertungskriterium wird teilweise absichtlich verletzt, wenn z. B. Schienen erzeugt werden mit dem selben
	 * Fach (Sport-Schiene). Nichtsdestotrotz möchte man häufig nicht die selben Fächer in einer Schiene, aufgrund von
	 * Raumkapazitäten (Fachräume).
	 *
	 * @param idErgebnis   die Datenbank-ID des Ergebnisses
	 *
	 * @return eine Güte des 4. Bewertungskriteriums im Bereich [0;1], mit 0=optimal
	 * @throws DeveloperNotificationException Falls es kein Ergebnis mit dieser ID gibt.
	 */
	public ergebnisGetBewertung4Intervall(idErgebnis: number): number {
		const wert: number = this.ergebnisGetBewertung4Wert(idErgebnis);
		return 1 - (1 / ((0.25 * wert) + 1));
	}

	private kursAddKursOhneSortierung(kurs: GostBlockungKurs): void {
		DeveloperNotificationException.ifMapPutOverwrites(this.kursById, kurs.id, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("Kursmenge nach Fach, Kursart und Kursnummer sortiert", this.kursmengeSortiertNachFachKursartKursnummer, kurs);
		DeveloperNotificationException.ifListAddsDuplicate("Kursmenge nach Kursart, Fach und Kursnummer sortiert", this.kursmengeSortiertNachKursartFachKursnummer, kurs);
		const liste: List<GostBlockungKurs> | null = Map2DUtils.getOrCreateArrayList(this.kursmengeByFachIdAndKursartId, kurs.fach_id, kurs.kursart);
		liste.add(kurs);
		liste.sort(this.compKursnummer);
		this.dtoDaten.kurse.add(kurs);
	}

	/**
	 * Fügt den übergebenen Kurs zu der Blockung hinzu.
	 *
	 * @param kurs   das {@link GostBlockungKurs}-Objekt, welches hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException falls die Daten des Kurses inkonsistent sind.
	 */
	public kursAdd(kurs: GostBlockungKurs): void {
		this.kursAddListe(ListUtils.create1(kurs));
	}

	/**
	 * Fügt die Menge an Kursen hinzu.
	 *
	 * @param kursmenge   die Menge an Kursen
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Kurse inkonsistent sind.
	 */
	public kursAddListe(kursmenge: List<GostBlockungKurs>): void {
		const setId: HashSet<number> = new HashSet<number>();
		for (const kAlt of this.dtoDaten.kurse) {
			setId.add(kAlt.id);
		}
		const nSchienen: number = this.schieneGetAnzahl();
		for (const kNeu of kursmenge) {
			DeveloperNotificationException.ifInvalidID("pKurs.id", kNeu.id);
			DeveloperNotificationException.ifNull("manFaecher.get(pKurs.fach_id)", this.manFaecher.get(kNeu.fach_id));
			DeveloperNotificationException.ifNull("GostKursart.fromIDorNull(pKurs.kursart)", GostKursart.fromIDorNull(kNeu.kursart));
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs.wochenstunden %d < 0", kNeu.wochenstunden), kNeu.wochenstunden < 0);
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs.anzahlSchienen %d zu klein!", kNeu.anzahlSchienen), kNeu.anzahlSchienen < 1);
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs.anzahlSchienen %d zu groß!", kNeu.anzahlSchienen), kNeu.anzahlSchienen > nSchienen);
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs.nummer %d zu klein!", kNeu.nummer), kNeu.nummer < 1);
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs.id %d Doppelung!", kNeu.id), !setId.add(kNeu.id));
		}
		for (const gKurs of kursmenge) {
			this.kursAddKursOhneSortierung(gKurs);
		}
		this.kursmengeSortiertNachFachKursartKursnummer.sort(this.compKurs1fach2kursart3kursnummer);
		this.kursmengeSortiertNachKursartFachKursnummer.sort(this.compKurs1kursart2fach3kursnummer);
	}

	/**
	 * Liefert TRUE, falls der Kurs mit der übergebenen ID existiert.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs mit der übergebenen ID existiert
	 */
	public kursGetExistiert(idKurs: number): boolean {
		return this.kursById.get(idKurs) !== null;
	}

	/**
	 * Liefert die Anzahl an Kursen.
	 *
	 * @return die Anzahl an Kursen
	 */
	public kursGetAnzahl(): number {
		return this.kursById.size();
	}

	/**
	 * Liefert die Anzahl an Kursen, die keine KOOP-Kurse sind.
	 *
	 * @return die Anzahl an Kursen, die keine KOOP-Kurse sind
	 */
	public kursGetAnzahlIntener(): number {
		let nKurse: number = 0;
		for (const k of this.kursById.values()) {
			if (!k.istKoopKurs) {
				nKurse++;
			}
		}
		return nKurse;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer][-Suffix], beispielsweise D-GK1
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetName(idKurs: number): string {
		const kurs: GostBlockungKurs = this.kursGet(idKurs);
		const gFach: GostFach = this.manFaecher.getOrException(kurs.fach_id);
		const sSuffix: string = JavaObject.equalsTranspiler("", (kurs.suffix)) ? "" : ("-" + kurs.suffix);
		const kursart: GostKursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer + sSuffix;
	}

	/**
	 * Liefert den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return den Namen des Kurses der Form [Fach]-[Kursart][Kursnummer] ohne den potentiellen Suffix, beispielsweise D-GK1
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetNameOhneSuffix(idKurs: number): string {
		const kurs: GostBlockungKurs = this.kursGet(idKurs);
		const gFach: GostFach = this.manFaecher.getOrException(kurs.fach_id);
		const kursart: GostKursart = GostKursart.fromID(kurs.kursart);
		return gFach.kuerzelAnzeige + "-" + kursart.kuerzel + kurs.nummer;
	}

	/**
	 * Liefert das {@link GostBlockungKurs}-Objekt mit der übergebenen ID.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return das {@link GostBlockungKurs}-Objekt
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGet(idKurs: number): GostBlockungKurs {
		return DeveloperNotificationException.ifMapGetIsNull(this.kursById, idKurs);
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene Nummer hat. <br>
	 * Wirft eine Exception, falls es eine solche Lehrkraft nicht gibt.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param reihenfolgeNr   die Lehrkraft mit der Nummer, die gesucht wird
	 *
	 * @return die Lehrkraft des Kurses mit der Nummer
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitNummer(idKurs: number, reihenfolgeNr: number): GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.reihenfolge === reihenfolgeNr) {
				return lehrkraft;
			}
		}
		throw new DeveloperNotificationException("Es gibt im Kurs " + this.toStringKurs(idKurs) + " keine Lehrkraft mit ReihenfolgeNr. " + reihenfolgeNr + "!");
	}

	/**
	 * Liefert die Lehrkraft des Kurses, welche die angegebene ID hat.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der gesuchten Lehrkraft
	 *
	 * @return die Lehrkraft des Kurses mit der ID
	 * @throws DeveloperNotificationException Falls es eine solche Lehrkraft nicht gibt.
	 */
	public kursGetLehrkraftMitID(idKurs: number, idLehrkraft: number): GostBlockungKursLehrer | null {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.id === idLehrkraft) {
				return lehrkraft;
			}
		}
		throw new DeveloperNotificationException("Es gibt im Kurs " + this.toStringKurs(idKurs) + " keine Lehrkraft mit ID " + idLehrkraft + "!");
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der Nummer existiert.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param reihenfolgeNr   die Lehrkraft mit der Nummer, die gesucht wird
	 *
	 * @return true, falls im Kurs die Lehrkraft mit der Nummer existiert
	 * @throws DeveloperNotificationException  Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetLehrkraftMitNummerExists(idKurs: number, reihenfolgeNr: number): boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.reihenfolge === reihenfolgeNr) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls im Kurs die Lehrkraft mit der ID existiert.
	 *
	 * @param idKurs        die Datenbank-ID des Kurses
	 * @param idLehrkraft   die Datenbank-ID der gesuchten Lehrkraft
	 *
	 * @return true, falls im Kurs die Lehrkraft mit der ID existiert
	 */
	public kursGetLehrkraftMitIDExists(idKurs: number, idLehrkraft: number): boolean {
		for (const lehrkraft of this.kursGetLehrkraefteSortiert(idKurs)) {
			if (lehrkraft.id === idLehrkraft) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return alle Lehrkräfte eines Kurses sortiert nach {@link GostBlockungKursLehrer#reihenfolge}
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetLehrkraefteSortiert(idKurs: number): List<GostBlockungKursLehrer> {
		return this.kursGet(idKurs).lehrer;
	}

	/**
	 * Fügt die übergebene Lehrkraft zum Kurs hinzu.
	 *
	 * @param idKurs          die Datenbank-ID des Kurses
	 * @param neueLehrkraft   das {@link GostBlockungKursLehrer}-Objekt
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder die Lehrkraft oder die ReihenfolgeNr bereits im Kurs existiert.
	 */
	public kursAddLehrkraft(idKurs: number, neueLehrkraft: GostBlockungKursLehrer): void {
		const kurs: GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer: List<GostBlockungKursLehrer> = kurs.lehrer;
		for (const lehrkraft of listOfLehrer) {
			DeveloperNotificationException.ifTrue(this.toStringKurs(idKurs) + " hat bereits " + this.toStringKursLehrkraft(idKurs, lehrkraft.id), lehrkraft.id === neueLehrkraft.id);
			DeveloperNotificationException.ifTrue(this.toStringKurs(idKurs) + " hat bereits " + this.toStringKursLehrkraft(idKurs, lehrkraft.id) + " mit Reihenfolge " + lehrkraft.reihenfolge, lehrkraft.reihenfolge === neueLehrkraft.reihenfolge);
		}
		listOfLehrer.add(neueLehrkraft);
		listOfLehrer.sort(this.compLehrkraefte);
		this.ergebnisAlleRevalidieren();
	}

	/**
	 * Löscht aus dem übergebenen Kurs die angegebene Lehrkraft.
	 *
	 * @param idKurs            die Datenbank-ID des Kurses
	 * @param idAlteLehrkraft   die Datenbank-ID des {@link GostBlockungKursLehrer}-Objekt
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert oder es eine solche Lehrkraft im Kurs nicht gibt.
	 */
	public kursRemoveLehrkraft(idKurs: number, idAlteLehrkraft: number): void {
		const kurs: GostBlockungKurs = this.kursGet(idKurs);
		const listOfLehrer: List<GostBlockungKursLehrer> = kurs.lehrer;
		for (let i: number = 0; i < listOfLehrer.size(); i++) {
			if (listOfLehrer.get(i).id === idAlteLehrkraft) {
				listOfLehrer.remove(listOfLehrer.get(i));
				this.ergebnisAlleRevalidieren();
				return;
			}
		}
		throw new DeveloperNotificationException(this.toStringKurs(idKurs) + " enthält nicht " + this.toStringKursLehrkraft(idKurs, idAlteLehrkraft));
	}

	/**
	 * Liefert eine nach 'Fach, Kursart, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return eine nach 'Fach, Kursart, Kursnummer' sortierte Liste der Kurse
	 */
	public kursGetListeSortiertNachFachKursartNummer(): List<GostBlockungKurs> {
		return this.kursmengeSortiertNachFachKursartKursnummer;
	}

	/**
	 * Liefert eine nach 'Kursart, Fach, Kursnummer' sortierte Kopie der Menge der Kurse.
	 *
	 * @return eine nach 'Kursart, Fach, Kursnummer' sortierte Liste der Kurse
	 */
	public kursGetListeSortiertNachKursartFachNummer(): List<GostBlockungKurs> {
		return this.kursmengeSortiertNachKursartFachKursnummer;
	}

	/**
	 * Liefert eine nach Kursnummer sortierte Liste der Kurse für das angegebene Fach und die angegebene Kursart.
	 *
	 * @param idFach      die ID des Fachs
	 * @param idKursart   die ID der Kursart
	 *
	 * @return die sortierte Liste der Kurse für das Fach und die Kursart
	 */
	public kursGetListeByFachUndKursart(idFach: number, idKursart: number): List<GostBlockungKurs> {
		const liste: List<GostBlockungKurs> | null = this.kursmengeByFachIdAndKursartId.getOrNull(idFach, idKursart);
		if (liste === null) {
			return new ArrayList();
		}
		liste.sort(this.compKursnummer);
		return liste;
	}

	/**
	 * Liefert TRUE, falls ein Löschen des Kurses erlaubt ist. <br>
	 * Kriterium: Der Kurs muss existieren und das aktuelle Ergebnis muss eine Vorlage sein.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls ein Löschen des Kurses erlaubt ist
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursGetIsRemoveAllowed(idKurs: number): boolean {
		return (this.kursGet(idKurs).id === idKurs) && this.getIstBlockungsVorlage();
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund von Regeln in der angegebenen Schiene verboten ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs in der angegebenen Schiene verboten ist
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene in der Blockung nicht existiert.
	 */
	public kursGetIstVerbotenInSchiene(idKurs: number, idSchiene: number): boolean {
		if (this.kursGetHatSperrungInSchiene(idKurs, idSchiene)) {
			return true;
		}
		const nummer: number = this.schieneGet(idSchiene).nummer;
		const kursart: number = this.kursGet(idKurs).kursart;
		return this.kursGetIstVerbotenInSchieneDurchTyp06(nummer, kursart) || this.kursGetIstVerbotenInSchieneDurchTyp01(nummer, kursart);
	}

	private kursGetIstVerbotenInSchieneDurchTyp06(nummer: number, kursart: number): boolean {
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS)) {
			if ((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) {
				if (regel.parameter.get(0) !== kursart) {
					return true;
				}
			} else {
				if (regel.parameter.get(0) === kursart) {
					return true;
				}
			}
		}
		return false;
	}

	private kursGetIstVerbotenInSchieneDurchTyp01(nummer: number, kursart: number): boolean {
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS)) {
			if (((nummer >= regel.parameter.get(1)) && (nummer <= regel.parameter.get(2))) && (regel.parameter.get(0) === kursart)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE} in der angegebenen Schiene gesperrt ist
	 * @throws DeveloperNotificationException falls der Kurs oder die Schiene nicht existiert.
	 */
	public kursGetHatSperrungInSchiene(idKurs: number, idSchiene: number): boolean {
		const kurs: GostBlockungKurs = this.kursGet(idKurs);
		const schiene: GostBlockungSchiene = this.schieneGet(idSchiene);
		const key: LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, kurs.id, schiene.nummer);
		return this.regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene gesperrt hat.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return die Regel, die den Kurs in der Schiene sperrt
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public kursGetRegelGesperrtInSchiene(idKurs: number, idSchiene: number): GostBlockungRegel {
		const nrSchiene: number = this.schieneGet(idSchiene).nummer;
		const key: LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull("" + this.toStringKurs(idKurs) + " ist nicht gesperrt in Schiene " + this.toStringSchiene(idSchiene) + "!", this.regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls der Kurs aufgrund der Regel {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE} in der angegebenen Schiene fixiert ist
	 * @throws DeveloperNotificationException falls die Schiene nicht existiert.
	 */
	public kursGetHatFixierungInSchiene(idKurs: number, idSchiene: number): boolean {
		const nrSchiene: number = this.schieneGet(idSchiene).nummer;
		const key: LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return this.regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert hat.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return die Regel, die den Kurs in der Schiene fixiert
	 * @throws DeveloperNotificationException falls die Schiene oder die Regel nicht existiert.
	 */
	public kursGetRegelFixierungInSchiene(idKurs: number, idSchiene: number): GostBlockungRegel {
		const nrSchiene: number = this.schieneGet(idSchiene).nummer;
		const key: LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
		return DeveloperNotificationException.ifNull(this.toStringKurs(idKurs) + " ist nicht fixiert in Schiene " + this.toStringSchiene(idSchiene) + "!", this.regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Kurs nicht vollständig fixiert ist.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs nicht vollständig fixiert ist
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public kursIstWeitereFixierungErlaubt(idKurs: number): boolean {
		const anzahlSchienen: number = this.kursGet(idKurs).anzahlSchienen;
		let anzahlFixierungen: number = 0;
		for (let nr: number = 1; nr <= this.schieneGetAnzahl(); nr++) {
			const kFixierungAlt: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nr]);
			const rFixierungAlt: GostBlockungRegel | null = this.regelGetByLongArrayKeyOrNull(kFixierungAlt);
			if (rFixierungAlt !== null) {
				anzahlFixierungen++;
			}
		}
		return anzahlFixierungen < anzahlSchienen;
	}

	/**
	 * Liefert die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL.
	 *
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche die Anzahl der DummySuS eines Kurses definiert oder NULL
	 */
	public kursGetRegelDummySchuelerOrNull(idKurs: number): GostBlockungRegel | null {
		for (const regel of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			if (regel.parameter.get(0) === idKurs) {
				return regel;
			}
		}
		return null;
	}

	/**
	 * Liefert ein Set aller Kurs-IDs.
	 *
	 * @return ein Set aller Kurs-IDs
	 */
	public kursmengeGetSetDerIDs(): JavaSet<number> {
		const setKursID: HashSet<number> = new HashSet<number>();
		for (const kurs of this.kursmengeSortiertNachFachKursartKursnummer) {
			setKursID.add(kurs.id);
		}
		return setKursID;
	}

	/**
	 * Entfernt alle Kurse mit den übergebenen IDs aus der Blockung.
	 * <br>(1) Überprüft, ob es eine Blockungsvorlage ist und ob alle IDs existieren, sonst Exception.
	 * <br>(2) Entfernt dann alle Kurse aus den Datenstrukturen.
	 * <br>(3) Entfernt dann alle Regeln, die einen der Kurse tangieren.
	 * <br>(4) Dann muss der Client den ErgebnisManager über die Löschung des Kurses informieren.
	 *
	 * @param idKurse   die Datenbank-IDs der zu entfernenden Kurse
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemoveByID(idKurse: JavaSet<number>): void {
		DeveloperNotificationException.ifTrue("Ein Löschen von Kursen ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		for (const idKurs of idKurse) {
			DeveloperNotificationException.ifTrue("Löschen von Kurs.id=" + idKurs + " nicht möglich, da nicht vorhanden!", !this.kursGetExistiert(idKurs));
		}
		for (const idKurs of idKurse) {
			const kurs: GostBlockungKurs = this.kursGet(idKurs);
			this.kursmengeSortiertNachFachKursartKursnummer.remove(kurs);
			this.kursmengeSortiertNachKursartFachKursnummer.remove(kurs);
			Map2DUtils.removeFromListAndTrimOrException(this.kursmengeByFachIdAndKursartId, kurs.fach_id, kurs.kursart, kurs);
			DeveloperNotificationException.ifMapRemoveFailes(this.kursById, idKurs);
			this.dtoDaten.kurse.remove(kurs);
		}
		const regelIDs: HashSet<number> = new HashSet<number>();
		for (const regel of this.dtoDaten.regeln) {
			for (const idKurs of idKurse) {
				if (GostBlockungsdatenManager.regelGetHatKursIDs(regel, idKurs)) {
					regelIDs.add(regel.id);
					break;
				}
			}
		}
		this.regelRemoveListeByIDsOhneRevalidierung(regelIDs);
	}

	/**
	 * Entfernt den Kurs mit der übergebenen ID aus der Blockung.
	 *
	 * @param idKurs   die Datenbank-ID des zu entfernenden Kurses
	 *
	 * @throws DeveloperNotificationException Falls der Kurs nicht in der Blockung existiert.
	 */
	public kursRemoveByID(idKurs: number): void {
		this.kurseRemoveByID(SetUtils.create1(idKurs));
	}

	/**
	 * Entfernt den übergebenen Kurs aus der Blockung.
	 *
	 * @param kurs   der zu entfernende Kurs
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht existiert.
	 */
	public kursRemove(kurs: GostBlockungKurs): void {
		this.kurseRemoveByID(SetUtils.create1(kurs.id));
	}

	/**
	 * Entfernt alle {@link GostBlockungKurs}-Objekte.
	 *
	 * @param kurse   die zu entfernenden {@link GostBlockungKurs}-Objekte
	 *
	 * @throws DeveloperNotificationException falls einer der Kurse nicht existiert oder es sich nicht um eine Blockungsvorlage handelt.
	 */
	public kurseRemove(kurse: List<GostBlockungKurs>): void {
		const idKurse: HashSet<number> = new HashSet<number>();
		for (const kursExtern of kurse) {
			idKurse.add(kursExtern.id);
		}
		this.kurseRemoveByID(idKurse);
	}

	/**
	 * Kombiniert zwei Kurse zu einem Kurs. Die Regel  {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN}
	 * muss dabei ggf. auch kombiniert werden, wobei eine existierende Regel recycled wird.
	 *
	 * @param idKursID1keep     die Kurs-ID des Ziel-Kurses (wird nicht gelöscht)
	 * @param idKursID2delete   die Kurs-ID des Quell-Kurses (wird gelöscht)
	 * @throws DeveloperNotificationException falls es keine Blockungsvorlage ist, oder die Kurse nicht existieren, oder die Kurse identisch sind.
	 */
	public kursMerge(idKursID1keep: number, idKursID2delete: number): void {
		DeveloperNotificationException.ifTrue("Die Kurse müssen sich unterscheiden!", idKursID1keep === idKursID2delete);
		DeveloperNotificationException.ifTrue("Ein Löschen des Kurses ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID1keep + " des Ziel-Kurses gibt es nicht!", !this.kursById.containsKey(idKursID1keep));
		DeveloperNotificationException.ifTrue("Die ID=" + idKursID2delete + " des Quell-Kurses gibt es nicht!", !this.kursById.containsKey(idKursID2delete));
		const regelKursKeep: GostBlockungRegel | null = this.regelGetKursMitDummySusAuffuellen(idKursID1keep);
		const regelKursDelete: GostBlockungRegel | null = this.regelGetKursMitDummySusAuffuellen(idKursID2delete);
		if (regelKursDelete !== null) {
			if (regelKursKeep !== null) {
				const summe: number = regelKursDelete.parameter.get(1) + regelKursKeep.parameter.get(1);
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
	 * @param idKurs   die Datenbank-ID des Kurses
	 * @param suffix   der neue Suffix des Kurses
	 *
	 * @throws DeveloperNotificationException falls der Kurs nicht in der Blockung existiert.
	 */
	public kursSetSuffix(idKurs: number, suffix: string): void {
		this.kursGet(idKurs).suffix = suffix;
	}

	/**
	 * Fügt die übergebene Schiene zu der Blockung hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schiene   die hinzuzufügende Schiene
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAdd(schiene: GostBlockungSchiene): void {
		this.schieneAddListe(ListUtils.create1(schiene));
	}

	/**
	 * Fügt die Menge an Schienen hinzu.
	 * <br>: Wichtig: Beim Ergebnismanager müssen danach die Schienen auch hinzugefügt werden!
	 *
	 * @param schienenmenge   die Menge an Schienen
	 * @throws DeveloperNotificationException Falls die Schienen-Daten inkonsistent sind.
	 */
	public schieneAddListe(schienenmenge: List<GostBlockungSchiene>): void {
		const setNr: HashSet<number> = new HashSet<number>();
		const setId: HashSet<number> = new HashSet<number>();
		for (const sAlt of this.dtoDaten.schienen) {
			setId.add(sAlt.id);
			setNr.add(sAlt.nummer);
		}
		for (const sNeu of schienenmenge) {
			DeveloperNotificationException.ifInvalidID("Schiene.id", sNeu.id);
			DeveloperNotificationException.ifTrue("Schiene.bezeichnung darf nicht leer sein!", JavaObject.equalsTranspiler("", (sNeu.bezeichnung)));
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + sNeu.nummer + " < 1", sNeu.nummer < 1);
			DeveloperNotificationException.ifTrue("Schienen-WochenStd. " + sNeu.wochenstunden + " < 1", sNeu.wochenstunden < 1);
			DeveloperNotificationException.ifTrue("Schienen-ID-Doppelung " + sNeu.id, !setId.add(sNeu.id));
			DeveloperNotificationException.ifTrue("Schienen-Nr-Doppelung " + sNeu.nummer, !setNr.add(sNeu.nummer));
		}
		for (let nr: number = 1; nr <= this.dtoDaten.schienen.size() + schienenmenge.size(); nr++) {
			DeveloperNotificationException.ifTrue("Schienen-Nr. " + nr + " fehlt in der Reihenfolge!", !setNr.contains(nr));
		}
		for (const schiene of schienenmenge) {
			this.schieneById.put(schiene.id, schiene);
			this.dtoDaten.schienen.add(schiene);
		}
		this.dtoDaten.schienen.sort(this.compSchiene);
	}

	/**
	 * Gibt die Schiene der Blockung anhand von deren ID zurück.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return das zugehörige {@link GostBlockungSchiene}-Objekt
	 * @throws DeveloperNotificationException Falls die Schiene nicht in der Blockung existiert.
	 */
	public schieneGet(idSchiene: number): GostBlockungSchiene {
		return DeveloperNotificationException.ifNull("Schienen-Map.get(" + idSchiene + ")", this.schieneById.get(idSchiene));
	}

	/**
	 * Liefert TRUE, falls eine Schiene mit der übergebenen ID existiert.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls die Schiene existiert
	 */
	public schieneGetExistiert(idSchiene: number): boolean {
		return this.schieneById.get(idSchiene) !== null;
	}

	/**
	 * Liefert die aktuelle Menge aller Schienen sortiert nach der Schienen-Nummer.
	 *
	 * @return die Menge aller Schienen sortiert nach der Nummer
	 */
	public schieneGetListe(): List<GostBlockungSchiene> {
		return new ArrayList<GostBlockungSchiene>(this.dtoDaten.schienen);
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Schiene erlaubt ist.
	 *
	 * @param idSchiene   die Datenbank-ID der Schiene
	 *
	 * @return true, falls ein Löschen der Schiene erlaubt ist
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public schieneGetIsRemoveAllowed(idSchiene: number): boolean {
		this.schieneGet(idSchiene);
		return this.getIstBlockungsVorlage();
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#bezeichnung} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene     die Datenbank-ID der Schiene
	 * @param bezeichnung   die neue Bezeichnung
	 *
	 * @throws DeveloperNotificationException Falls die ID der Schiene nicht existiert.
	 */
	public schienePatchBezeichnung(idSchiene: number, bezeichnung: string): void {
		this.schieneGet(idSchiene).bezeichnung = bezeichnung;
	}

	/**
	 * Ändert das Attribut {@link GostBlockungSchiene#wochenstunden} der Schiene mit der jeweiligen ID.
	 *
	 * @param idSchiene       die Datenbank-ID der Schiene
	 * @param wochenstunden   die neuen Wochenstunden
	 */
	public schienePatchWochenstunden(idSchiene: number, wochenstunden: number): void {
		this.schieneGet(idSchiene).wochenstunden = wochenstunden;
	}

	/**
	 * Entfernt die Schiene mit der übergebenen ID aus der Blockung.
	 * Konsequenz: <br>
	 * (1) Das Löschen der Schiene muss erlaubt sein und die Schiene muss existieren, sonst Exception. <br>
	 * (2) Die Schiene wird entfernt und Schienen mit größerer Nr. werden um 1 reduziert. <br>
	 * (3) Die Regeln müssen bei Schienen-Nummern angepasst werden. <br>
	 *
	 * @param idSchiene   die Datenbank-ID der zu entfernenden Schiene
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemoveByID(idSchiene: number): void {
		DeveloperNotificationException.ifTrue("Ein Löschen einer Schiene ist nur bei einer Blockungsvorlage erlaubt!", !this.getIstBlockungsVorlage());
		const schieneR: GostBlockungSchiene = this.schieneGet(idSchiene);
		for (const eManager of this.ergebnisManagerByErgebnisId.values()) {
			DeveloperNotificationException.ifTrue("Schiene kann nicht gelöscht werden, da sie Kurse enthält!", !eManager.getOfSchieneIstLeer(idSchiene));
		}
		this.schieneById.remove(idSchiene);
		this.dtoDaten.schienen.remove(schieneR);
		for (const schiene of this.dtoDaten.schienen) {
			if (schiene.nummer > schieneR.nummer) {
				schiene.nummer--;
			}
		}
		const setLoeschen: JavaSet<number> = new HashSet<number>();
		const listHinzufuegen: List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>();
		for (const r of this.dtoDaten.regeln) {
			const a: Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
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
			const a: Array<number> | null = GostKursblockungRegelTyp.getNeueParameterBeiSchienenLoeschung(r, schieneR.nummer);
			if (a !== null) {
				for (let i: number = 0; i < a.length; i++) {
					r.parameter.set(i, a[i]);
				}
			}
		}
		this.regelAddListeOhneRevalidierung(listHinzufuegen);
	}

	/**
	 * Entfernt die übergebene Schiene aus der Blockung.
	 * <br>Hinweis: Es muss nicht dasselbe Objekt sein, nur die ID muss übereinstimmen.
	 *
	 * @param schiene   die zu entfernende Schiene
	 *
	 * @throws DeveloperNotificationException Falls die Schiene nicht existiert oder ein Löschen nicht erlaubt ist.
	 */
	public schieneRemove(schiene: GostBlockungSchiene): void {
		this.schieneRemoveByID(schiene.id);
	}

	/**
	 * Liefert die Anzahl an Schienen.
	 *
	 * @return die Anzahl an Schienen
	 */
	public schieneGetAnzahl(): number {
		return this.schieneById.size();
	}

	/**
	 * Liefert die Default-Anzahl an Schienen zurück, die für eine neue Blockung verwendet wird.
	 *
	 * @param halbjahr   das Halbjahr, für welches die Blockung angelegt werden soll
	 *
	 * @return die Default-Anzahl an Schienen für eine neue Blockung
	 */
	public static schieneGetDefaultAnzahl(halbjahr: GostHalbjahr): number {
		return (halbjahr.id < 2) ? 13 : 11;
	}

	private regelAddOhneSortierung(regel: GostBlockungRegel): void {
		const multikey: LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
		const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		DeveloperNotificationException.ifMapPutOverwrites(this.regelById, regel.id, regel);
		MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, typ).add(regel);
		this.regelByMultikey.put(multikey, regel);
		this.dtoDaten.regeln.add(regel);
	}

	/**
	 * Fügt die übergebene Regel zu der Blockung hinzu.
	 *
	 * @param regel   die hinzuzufügende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regel inkonsistent sind.
	 */
	public regelAdd(regel: GostBlockungRegel): void {
		this.regelAddListe(ListUtils.create1(regel));
	}

	/**
	 * Fügt eine Menge an Regeln hinzu.
	 *
	 * @param regelmenge   die Menge an Regeln
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelAddListe(regelmenge: List<GostBlockungRegel>): void {
		this.regelAddListeOhneRevalidierung(regelmenge);
		this.ergebnisAlleRevalidieren();
	}

	private regelAddListeOhneRevalidierung(regeln: List<GostBlockungRegel>): void {
		const setIDs: JavaSet<number> = new HashSet<number>();
		const setMultiKey: JavaSet<LongArrayKey> = new HashSet<LongArrayKey>();
		const mengeByTyp: JavaMap<number, JavaSet<GostBlockungRegel>> = new HashMap<number, JavaSet<GostBlockungRegel>>();
		const regelmengeOkay: List<GostBlockungRegel> = new ArrayList<GostBlockungRegel>();
		for (const r of regeln) {
			DeveloperNotificationException.ifTrue(JavaString.format("%s hat eine ungültige ID %d", this.toStringRegel(r.id), r.id), r.id < 0);
			DeveloperNotificationException.ifTrue(JavaString.format("%s Regel-ID %d Doppelung!", this.toStringRegel(r.id), r.id), this.regelById.containsKey(r.id) || this.regelUngueltigById.containsKey(r.id) || !setIDs.add(r.id));
			const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
			DeveloperNotificationException.ifTrue(JavaString.format("%s hat falsche Parameter-Anzahl!", this.toStringRegel(r.id)), typ.getParamCount() !== r.parameter.size());
			const menge: JavaSet<GostBlockungRegel> = MapUtils.getOrCreateHashSet(mengeByTyp, r.typ);
			const warnung: string = this.regelGetWarnung(r, setMultiKey, menge);
			if (JavaString.isEmpty(warnung)) {
				regelmengeOkay.add(r);
			} else {
				this.regelUngueltigById.put(r.id, r);
				this.regelUngueltigBeschreibungById.put(r.id, warnung);
			}
		}
		for (const regel of regelmengeOkay) {
			this.regelAddOhneSortierung(regel);
		}
		this.dtoDaten.regeln.sort(this.compRegel);
		for (const listOfTyp of this.regelmengeByRegeltyp.values()) {
			listOfTyp.sort(this.compRegel);
		}
	}

	private regelGetWarnung(r: GostBlockungRegel, setMultiKey: JavaSet<LongArrayKey>, menge: JavaSet<GostBlockungRegel>): string {
		const multikey: LongArrayKey = GostBlockungsdatenManager.regelToMultikey(r);
		if (this.regelByMultikey.containsKey(multikey) || !setMultiKey.add(multikey)) {
			return JavaString.format("%s existiert bereits als gleiche (nicht als selbe) Regel im MultiMap!", this.toStringRegel(r.id));
		}
		const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(r.typ);
		let _sevar_1897008228 : any;
		const _seexpr_1897008228 = (typ);
		if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS) {
			_sevar_1897008228 = this.regelCheckTyp01(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE) {
			_sevar_1897008228 = this.regelCheckTyp02und03(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE) {
			_sevar_1897008228 = this.regelCheckTyp02und03(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS) {
			_sevar_1897008228 = this.regelCheckTyp04und05(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS) {
			_sevar_1897008228 = this.regelCheckTyp04und05(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS) {
			_sevar_1897008228 = this.regelCheckTyp06(r, menge);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS) {
			_sevar_1897008228 = this.regelCheckTyp07und08(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS) {
			_sevar_1897008228 = this.regelCheckTyp07und08(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN) {
			_sevar_1897008228 = this.regelCheckTyp09(r, menge);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN) {
			_sevar_1897008228 = this.regelCheckTyp10(r, menge);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH) {
			_sevar_1897008228 = this.regelCheckTyp11und12(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH) {
			_sevar_1897008228 = this.regelCheckTyp11und12(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER) {
			_sevar_1897008228 = this.regelCheckTyp13und14(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER) {
			_sevar_1897008228 = this.regelCheckTyp13und14(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL) {
			_sevar_1897008228 = this.regelCheckTyp15(r, menge);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.SCHUELER_IGNORIEREN) {
			_sevar_1897008228 = this.regelCheckTyp16(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN) {
			_sevar_1897008228 = this.regelCheckTyp17(r);
		} else if (_seexpr_1897008228 === GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE) {
			_sevar_1897008228 = this.regelCheckTyp18(r);
		} else {
			_sevar_1897008228 = JavaString.format("%s Regeltypüberprüfung: Der Regeltyp ist unbekannt!", this.toStringRegel(r.id));
		}
		return _sevar_1897008228;
	}

	private regelCheckTyp01(r: GostBlockungRegel): string {
		const wKursart: string = this.regelCheckReferenzKursart(r, 0);
		if (!JavaString.isEmpty(wKursart)) {
			return wKursart;
		}
		const wSchiene1: string = this.regelCheckReferenzSchienenNr(r, 1);
		if (!JavaString.isEmpty(wSchiene1)) {
			return wSchiene1;
		}
		const wSchiene2: string = this.regelCheckReferenzSchienenNr(r, 2);
		if (!JavaString.isEmpty(wSchiene2)) {
			return wSchiene2;
		}
		const bis: number = r.parameter.get(2).valueOf();
		const von: number = r.parameter.get(1).valueOf();
		if (bis < von) {
			return JavaString.format("%s Die BIS-Schiene %d kann nicht kleiner sein als die VON-Schiene %d!", this.toStringRegel(r.id), bis, von);
		}
		return "";
	}

	private regelCheckTyp02und03(r: GostBlockungRegel): string {
		const wKurs0: string = this.regelCheckReferenzKursID(r, 0);
		if (!JavaString.isEmpty(wKurs0)) {
			return wKurs0;
		}
		const wSchiene1: string = this.regelCheckReferenzSchienenNr(r, 1);
		if (!JavaString.isEmpty(wSchiene1)) {
			return wSchiene1;
		}
		return "";
	}

	private regelCheckTyp04und05(r: GostBlockungRegel): string {
		const wSchueler0: string = this.regelCheckReferenzSchuelerID(r, 0);
		if (!JavaString.isEmpty(wSchueler0)) {
			return wSchueler0;
		}
		const wKurs1: string = this.regelCheckReferenzKursID(r, 1);
		if (!JavaString.isEmpty(wKurs1)) {
			return wKurs1;
		}
		return "";
	}

	private regelCheckTyp06(r: GostBlockungRegel, menge: JavaSet<GostBlockungRegel>): string {
		const wKursart0: string = this.regelCheckReferenzKursart(r, 0);
		if (!JavaString.isEmpty(wKursart0)) {
			return wKursart0;
		}
		const wSchiene1: string = this.regelCheckReferenzSchienenNr(r, 1);
		if (!JavaString.isEmpty(wSchiene1)) {
			return wSchiene1;
		}
		const wSchiene2: string = this.regelCheckReferenzSchienenNr(r, 2);
		if (!JavaString.isEmpty(wSchiene2)) {
			return wSchiene2;
		}
		const bis: number = r.parameter.get(2).valueOf();
		const von: number = r.parameter.get(1).valueOf();
		if (bis < von) {
			return JavaString.format("%s Die BIS-Schiene %d kann nicht kleiner sein als die VON-Schiene %d!", this.toStringRegel(r.id), bis, von);
		}
		const bestehendeRegeln: List<GostBlockungRegel> = MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS);
		for (const rAlt of bestehendeRegeln) {
			if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 6 - Cross-Call-Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		for (const rAlt of menge) {
			if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 6 - Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private regelCheckTyp07und08(r: GostBlockungRegel): string {
		const wKurs0: string = this.regelCheckReferenzKursID(r, 0);
		if (!JavaString.isEmpty(wKurs0)) {
			return wKurs0;
		}
		const wKurs1: string = this.regelCheckReferenzKursID(r, 1);
		if (!JavaString.isEmpty(wKurs1)) {
			return wKurs1;
		}
		return "";
	}

	private regelCheckTyp09(r: GostBlockungRegel, menge: JavaSet<GostBlockungRegel>): string {
		const wKurs0: string = this.regelCheckReferenzKursID(r, 0);
		if (!JavaString.isEmpty(wKurs0)) {
			return wKurs0;
		}
		const anzahl: number = r.parameter.get(1).valueOf();
		if (anzahl < GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN) {
			return JavaString.format("%s KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit %d zu klein!", this.toStringRegel(r.id), anzahl);
		}
		if (anzahl > GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX) {
			return JavaString.format("%s KURS_MIT_DUMMY_SUS_AUFFUELLEN ist mit %d zu groß!", this.toStringRegel(r.id), anzahl);
		}
		const bestehendeRegeln: List<GostBlockungRegel> = MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN);
		for (const rAlt of bestehendeRegeln) {
			if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 9 - Cross-Call-Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		for (const rNeu of menge) {
			if (JavaObject.equalsTranspiler(rNeu.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 9 - Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private regelCheckTyp10(r: GostBlockungRegel, menge: JavaSet<GostBlockungRegel>): string {
		if (!menge.isEmpty()) {
			return JavaString.format("Regel 10 - Doppelung: %s", this.toStringRegel(r.id));
		}
		menge.add(r);
		return "";
	}

	private regelCheckTyp11und12(r: GostBlockungRegel): string {
		const wSchueler0: string = this.regelCheckReferenzSchuelerID(r, 0);
		if (!JavaString.isEmpty(wSchueler0)) {
			return wSchueler0;
		}
		const wSchueler1: string = this.regelCheckReferenzSchuelerID(r, 1);
		if (!JavaString.isEmpty(wSchueler1)) {
			return wSchueler1;
		}
		const wFach2: string = this.regelCheckReferenzFachID(r, 2);
		if (!JavaString.isEmpty(wFach2)) {
			return wFach2;
		}
		return "";
	}

	private regelCheckTyp13und14(r: GostBlockungRegel): string {
		const wSchueler0: string = this.regelCheckReferenzSchuelerID(r, 0);
		if (!JavaString.isEmpty(wSchueler0)) {
			return wSchueler0;
		}
		const wSchueler1: string = this.regelCheckReferenzSchuelerID(r, 1);
		if (!JavaString.isEmpty(wSchueler1)) {
			return wSchueler1;
		}
		return "";
	}

	private regelCheckTyp15(r: GostBlockungRegel, menge: JavaSet<GostBlockungRegel>): string {
		const wKurs0: string = this.regelCheckReferenzKursID(r, 0);
		if (!JavaString.isEmpty(wKurs0)) {
			return wKurs0;
		}
		const anzahl1: number = r.parameter.get(1).valueOf();
		if (anzahl1 < GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN) {
			return JavaString.format("%s KURS_MAXIMALE_SCHUELERANZAHL ist mit %d zu klein!", this.toStringRegel(r.id), anzahl1);
		}
		if (anzahl1 > GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX) {
			return JavaString.format("%s KURS_MAXIMALE_SCHUELERANZAHL ist mit %d zu groß!", this.toStringRegel(r.id), anzahl1);
		}
		const bestehendeRegeln: List<GostBlockungRegel> = MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL);
		for (const rAlt of bestehendeRegeln) {
			if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 15 - Cross-Call-Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		for (const rAlt of menge) {
			if (JavaObject.equalsTranspiler(rAlt.parameter.get(0), (r.parameter.get(0)))) {
				return JavaString.format("Regel 15 - Dopplung: %s", this.toStringRegel(r.id));
			}
		}
		menge.add(r);
		return "";
	}

	private regelCheckTyp16(r: GostBlockungRegel): string {
		const wSchueler0: string = this.regelCheckReferenzSchuelerID(r, 0);
		if (!JavaString.isEmpty(wSchueler0)) {
			return wSchueler0;
		}
		return "";
	}

	private regelCheckTyp17(r: GostBlockungRegel): string {
		const wKurs0: string = this.regelCheckReferenzKursID(r, 0);
		if (!JavaString.isEmpty(wKurs0)) {
			return wKurs0;
		}
		return "";
	}

	private regelCheckTyp18(r: GostBlockungRegel): string {
		const wFach0: string = this.regelCheckReferenzFachID(r, 0);
		if (!JavaString.isEmpty(wFach0)) {
			return wFach0;
		}
		const wKursart1: string = this.regelCheckReferenzKursart(r, 1);
		if (!JavaString.isEmpty(wKursart1)) {
			return wKursart1;
		}
		const anzahl2: number = r.parameter.get(2).valueOf();
		if (anzahl2 < GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN) {
			return JavaString.format("%s FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit %d zu klein!", this.toStringRegel(r.id), anzahl2);
		}
		if (anzahl2 > GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX) {
			return JavaString.format("%s FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE ist mit %d zu groß!", this.toStringRegel(r.id), anzahl2);
		}
		return "";
	}

	private regelCheckReferenzSchuelerID(r: GostBlockungRegel, index: number): string {
		const idSchueler: number = r.parameter.get(index).valueOf();
		if (this.schuelerGetOrNull(idSchueler) === null) {
			return JavaString.format("%s hat falsche Schüler-ID-Referenz %d!", this.toStringRegel(r.id), idSchueler);
		}
		return "";
	}

	private regelCheckReferenzKursID(r: GostBlockungRegel, index: number): string {
		const idKurs: number = r.parameter.get(index).valueOf();
		if (!this.kursGetExistiert(idKurs)) {
			return JavaString.format("%s hat falsche Kurs-ID-Referenz %d!", this.toStringRegel(r.id), idKurs);
		}
		return "";
	}

	private regelCheckReferenzSchienenNr(r: GostBlockungRegel, index: number): string {
		const nrSchiene: number = r.parameter.get(index).valueOf();
		if ((nrSchiene < 1) || (nrSchiene > this.schieneGetAnzahl())) {
			return JavaString.format("%s hat falsche Schienen-Nr-Referenz %d!", this.toStringRegel(r.id), nrSchiene);
		}
		return "";
	}

	private regelCheckReferenzFachID(r: GostBlockungRegel, index: number): string {
		const idFach: number = r.parameter.get(index).valueOf();
		if (this.manFaecher.get(idFach) === null) {
			return JavaString.format("%s hat falsche Fach-ID-Referenz %d!", this.toStringRegel(r.id), idFach);
		}
		return "";
	}

	private regelCheckReferenzKursart(r: GostBlockungRegel, index: number): string {
		const idKursart: number = r.parameter.get(index).valueOf();
		if (GostKursart.fromIDorNull(idKursart as number) === null) {
			return JavaString.format("%s hat falsche Kursart-Referenz %d!", this.toStringRegel(r.id), idKursart);
		}
		return "";
	}

	/**
	 * Liefert die Anzahl an Regeln.
	 *
	 * @return die Anzahl an Regeln
	 */
	public regelGetAnzahl(): number {
		return this.regelById.size();
	}

	/**
	 * Liefert die Regel mit der übergebenen ID zurück.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return die Regel mit der übergebenen ID
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelGet(idRegel: number): GostBlockungRegel {
		return DeveloperNotificationException.ifNull("Regel-Map.get(" + idRegel + ")", this.regelById.get(idRegel));
	}

	/**
	 * Liefert die {@link GostBlockungRegel} anhand des {@link LongArrayKey}-Schlüssels, oder NULL falls keine existiert.
	 *
	 * @param key   der {@link LongArrayKey}-Schlüssel
	 *
	 * @return die {@link GostBlockungRegel} zum {@link LongArrayKey}-Schlüssel, oder null
	 */
	public regelGetByLongArrayKeyOrNull(key: LongArrayKey): GostBlockungRegel | null {
		return this.regelByMultikey.get(key);
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln.
	 * Das ist die interne Referenz zur Liste der Regeln im {@link GostBlockungsdaten}-Objekt.
	 * Diese Liste ist stets sortiert nach (TYP, ID).
	 *
	 * @return die Menge aller Regeln sortiert nach (TYP, id)
	 */
	public regelGetListe(): List<GostBlockungRegel> {
		return this.dtoDaten.regeln;
	}

	/**
	 * Liefert die aktuelle Menge aller Regeln eines bestimmten {@link GostKursblockungRegelTyp}.
	 *
	 * @param typ   der {@link GostKursblockungRegelTyp}
	 *
	 * @return die Menge aller Regeln eines {@link GostKursblockungRegelTyp}
	 */
	public regelGetListeOfTyp(typ: GostKursblockungRegelTyp): List<GostBlockungRegel> {
		return MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, typ);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param nrSchiene   die Nummer der Schiene
	 *
	 * @return die Regel, welche den Kurs in einer Schiene sperrt, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public regelGetRegelOrDummyKursGesperrtInSchiene(idKurs: number, nrSchiene: number): GostBlockungRegel {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel: GostBlockungRegel | null = this.regelByMultikey.get(key);
		if (regel !== null) {
			return regel;
		}
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idKurs      die Datenbank-ID des Kurses
	 * @param nrSchiene   die Nummer der Schiene
	 *
	 * @return die Regel, welche den Kurs in einer Schiene fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public regelGetRegelOrDummyKursFixierungInSchiene(idKurs: number, nrSchiene: number): GostBlockungRegel {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene]);
		const regel: GostBlockungRegel | null = this.regelByMultikey.get(key);
		if (regel !== null) {
			return regel;
		}
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, idKurs, nrSchiene);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert, oder die Dummy-Regel (ID negativ), falls die Regel nicht existiert
	 */
	public regelGetRegelOrDummySchuelerInKursFixierung(idSchueler: number, idKurs: number): GostBlockungRegel {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		const regel: GostBlockungRegel | null = this.regelByMultikey.get(key);
		if (regel !== null) {
			return regel;
		}
		return DTOUtils.newGostBlockungRegel2(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs);
	}

	/**
	 * Liefert TRUE, falls die Regel mit der übergebenen ID existiert.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return true, falls die Regel mit der übergebenen ID existiert
	 */
	public regelGetExistiert(idRegel: number): boolean {
		return this.regelById.get(idRegel) !== null;
	}

	/**
	 * Liefert TRUE, falls ein Löschen der Regel erlaubt ist.
	 * <br> Hinweis: Die alte Implementierung verlangte noch, dass es sich um eine Blockungsvorlage handelt,
	 *               nun reicht es, dass die Regel existiert.
	 *
	 * @param idRegel   die Datenbank-ID der Regel
	 *
	 * @return true, falls ein Löschen der Regel erlaubt ist
	 */
	public regelGetIsRemoveAllowed(idRegel: number): boolean {
		return this.regelById.containsKey(idRegel);
	}

	private regelGetKursMitDummySusAuffuellen(idKurs: number): GostBlockungRegel | null {
		for (const r of this.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN)) {
			if (r.parameter.get(0) === idKurs) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Liefert TRUE, falls der übergebene Kurs in der übergebenen Regeln enthalten ist.
	 *
	 * @param regel    das {@link GostBlockungRegel}-Objekt
	 * @param idKurs   die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Kurs in der Regel enthalten ist
	 */
	private static regelGetHatKursIDs(regel: GostBlockungRegel, idKurs: number): boolean {
		const regelTyp: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
		for (let i: number = 0; i < regelTyp.getParamCount(); i++) {
			if ((regelTyp.getParamType(i) as unknown === GostKursblockungRegelParameterTyp.KURS_ID as unknown) && (regel.parameter.get(i) === idKurs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Liefert alle Regeln, die aufgrund von Fehlern ungültig sind.
	 *
	 * @return alle Regeln, die aufgrund von Fehlern ungültig sind
	 */
	public regelGetMapUngueltig(): JavaMap<number, GostBlockungRegel> {
		return this.regelUngueltigById;
	}

	/**
	 * Liefert die Beschreibung der jeweiligen ungültigen Regeln.
	 *
	 * @return die Beschreibung der jeweiligen ungültigen Regeln
	 */
	public regelGetMapUngueltigBeschreibung(): JavaMap<number, string> {
		return this.regelUngueltigBeschreibungById;
	}

	/**
	 * Entfernt die Regel mit der übergebenen ID aus der Blockung.
	 *
	 * @param idRegel   die Datenbank-ID der zu entfernenden Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelRemoveByID(idRegel: number): void {
		this.regelRemoveListeByIDs(SetUtils.create1(idRegel));
	}

	/**
	 * Entfernt eine Menge von Regeln.
	 *
	 * @param regelmenge   die Menge an Regeln, die entfernt werden soll
	 *
	 * @throws DeveloperNotificationException Falls die Daten der Regeln inkonsistent sind.
	 */
	public regelRemoveListe(regelmenge: List<GostBlockungRegel>): void {
		const setRegelIDs: HashSet<number> = new HashSet<number>();
		for (const regel of regelmenge) {
			setRegelIDs.add(regel.id);
		}
		this.regelRemoveListeByIDs(setRegelIDs);
	}

	private regelRemoveListeByIDsOhneRevalidierung(regelmengeGesamt: JavaSet<number>): void {
		const regelnUngueltig: ArrayList<number> | null = new ArrayList<number>();
		const regelnGueltig: ArrayList<number> | null = new ArrayList<number>();
		for (const idRegel of regelmengeGesamt) {
			if (this.regelUngueltigById.containsKey(idRegel)) {
				regelnUngueltig.add(idRegel);
			} else {
				regelnGueltig.add(idRegel);
			}
		}
		if (!regelnGueltig.isEmpty()) {
			for (const idRegel of regelnGueltig) {
				const regel: GostBlockungRegel = this.regelGet(idRegel);
				const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
				DeveloperNotificationException.ifTrue("Der Regeltyp ist undefiniert!", typ as unknown === GostKursblockungRegelTyp.UNDEFINIERT as unknown);
				DeveloperNotificationException.ifTrue("Die Multi-Map enthält die Regel nicht!", !this.regelByMultikey.containsKey(GostBlockungsdatenManager.regelToMultikey(regel)));
			}
			for (const idRegel of regelnGueltig) {
				const regel: GostBlockungRegel = this.regelGet(idRegel);
				const typ: GostKursblockungRegelTyp = GostKursblockungRegelTyp.fromTyp(regel.typ);
				const multikey: LongArrayKey = GostBlockungsdatenManager.regelToMultikey(regel);
				this.regelById.remove(idRegel);
				MapUtils.getOrCreateArrayList(this.regelmengeByRegeltyp, typ).remove(regel);
				this.regelByMultikey.remove(multikey);
				this.dtoDaten.regeln.remove(regel);
			}
		}
		if (!regelnUngueltig.isEmpty()) {
			for (const idRegel of regelnUngueltig) {
				this.regelUngueltigById.remove(idRegel);
				this.regelUngueltigBeschreibungById.remove(idRegel);
			}
		}
	}

	/**
	 * Löscht eine Menge an Regeln anhand ihrer IDs.
	 *
	 * @param regelmenge   die Menge der IDs der Regeln
	 *
	 * @throws DeveloperNotificationException falls mindestens eine Regel nicht existiert.
	 */
	public regelRemoveListeByIDs(regelmenge: JavaSet<number>): void {
		this.regelRemoveListeByIDsOhneRevalidierung(regelmenge);
		this.ergebnisAlleRevalidieren();
	}

	private static regelToMultikey(regel: GostBlockungRegel): LongArrayKey {
		const a: Array<number> | null = Array(regel.parameter.size() + 1).fill(0);
		a[0] = regel.typ;
		for (let i: number = 1; i < a.length; i++) {
			a[i] = regel.parameter.get(i - 1).valueOf();
		}
		return new LongArrayKey(a);
	}

	/**
	 * Entfernt die übergebene Regel aus der Blockung.
	 *
	 * @param regel   die zu entfernende Regel
	 *
	 * @throws DeveloperNotificationException Falls die Regel nicht existiert.
	 */
	public regelRemove(regel: GostBlockungRegel): void {
		this.regelRemoveListeByIDs(SetUtils.create1(regel.id));
	}

	/**
	 * Liefert die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen.
	 *
	 * @param idFach   die Datenbank-ID des Faches
	 *
	 * @return die Menge aller Kursarten des Faches, welche in Kursen oder Fachwahlen vorkommen
	 */
	public fachGetMengeKursarten(idFach: number): List<GostKursart> {
		const idKursarten: HashSet<number> = new HashSet<number>();
		if (this.kursmengeByFachIdAndKursartId.containsKey1(idFach)) {
			idKursarten.addAll(this.kursmengeByFachIdAndKursartId.getKeySetOf(idFach));
		}
		if (this.fachwahlmengeByFachIdAndKursartId.containsKey1(idFach)) {
			idKursarten.addAll(this.fachwahlmengeByFachIdAndKursartId.getKeySetOf(idFach));
		}
		const list: List<GostKursart> = new ArrayList<GostKursart>();
		for (const kursart of GostKursart.values()) {
			if (idKursarten.contains(kursart.id)) {
				list.add(kursart);
			}
		}
		return list;
	}

	/**
	 * Fügt eine Fachwahl hinzu.
	 *
	 * @param fachwahl   die Fachwahl, die hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAdd(fachwahl: GostFachwahl): void {
		this.fachwahlAddListe(ListUtils.create1(fachwahl));
	}

	/**
	 * Fügt alle Fachwahlen hinzu.
	 *
	 * @param fachwahlmenge   die Menge an Fachwahlen
	 *
	 * @throws DeveloperNotificationException Falls die Fachwahl-Daten inkonsistent sind.
	 */
	public fachwahlAddListe(fachwahlmenge: List<GostFachwahl>): void {
		const setSchuelerFach: JavaSet<LongArrayKey> = new HashSet<LongArrayKey>();
		for (const fNeu of fachwahlmenge) {
			GostKursart.fromFachwahlOrException(fNeu);
			DeveloperNotificationException.ifTrue("Fachwahl verweist auf ungültiges Fach " + fNeu.fachID, this.manFaecher.get(fNeu.fachID) === null);
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", this.fachwahlBySchuelerIdAndFachId.contains(fNeu.schuelerID, fNeu.fachID));
			DeveloperNotificationException.ifTrue("Fachwahl Duplikat!", !setSchuelerFach.add(new LongArrayKey(fNeu.schuelerID, fNeu.fachID)));
		}
		for (const fNeu of fachwahlmenge) {
			DeveloperNotificationException.ifMap2DPutOverwrites(this.fachwahlBySchuelerIdAndFachId, fNeu.schuelerID, fNeu.fachID, fNeu);
			const fachwahlenDesSchuelers: List<GostFachwahl> = MapUtils.getOrCreateArrayList(this.fachwahlmengeBySchuelerId, fNeu.schuelerID);
			fachwahlenDesSchuelers.add(fNeu);
			fachwahlenDesSchuelers.sort(this.compFachwahlen);
			const fachartID: number = GostKursart.getFachartIDByFachwahl(fNeu);
			this.fachwahlGetListeOfFachart(fachartID).add(fNeu);
			Map2DUtils.getOrCreateArrayList(this.fachwahlmengeByFachIdAndKursartId, fNeu.fachID, fNeu.kursartID).add(fNeu);
			this.dtoDaten.fachwahlen.add(fNeu);
		}
		this.dtoDaten.fachwahlen.sort(this.compFachwahlen);
	}

	/**
	 * Liefert die Anzahl an Fachwahlen.
	 *
	 * @return die Anzahl an Fachwahlen
	 */
	public fachwahlGetAnzahl(): number {
		return this.dtoDaten.fachwahlen.size();
	}

	/**
	 * Liefert den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'.
	 * <br> Die Information über den Schüler dieser Fachwahl wird nicht dargestellt.
	 *
	 * @param fachwahl   das Fachwahl-Objekt
	 *
	 * @return den Namen der Fachwahl (Fach-Kursart), beispielsweise 'M-GK'
	 * @throws DeveloperNotificationException falls die Fach-Referenz oder die Kursart-Referenz nicht existiert.
	 */
	public fachwahlGetName(fachwahl: GostFachwahl): string {
		const gFach: GostFach = this.manFaecher.getOrException(fachwahl.fachID);
		const gKursart: GostKursart = GostKursart.fromID(fachwahl.kursartID);
		return gFach.kuerzelAnzeige + "-" + gKursart.kuerzel;
	}

	/**
	 * Liefert die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID.
	 * <br> Die Fachart-ID lässt sich mit {@link GostKursart#getFachartID} berechnen.
	 *
	 * @param idFachart   die Fachart-ID berechnet aus Fach-ID und Kursart-ID
	 *
	 * @return die sortierte Menge aller {@link GostFachwahl} einer bestimmten Fachart-ID
	 */
	public fachwahlGetListeOfFachart(idFachart: number): List<GostFachwahl> {
		const list: List<GostFachwahl> = MapUtils.getOrCreateArrayList(this.fachwahlmengeByFachartId, idFachart);
		list.sort(this.compFachwahlen);
		return list;
	}

	/**
	 * Liefert die Anzahl verschiedenen Kursarten.
	 *
	 * @return die Anzahl verschiedener Kursarten
	 */
	public fachwahlGetAnzahlVerwendeterKursarten(): number {
		const setKursartenIDs: HashSet<number> = new HashSet<number>();
		for (const fachwahl of this.dtoDaten.fachwahlen) {
			setKursartenIDs.add(fachwahl.kursartID);
		}
		return setKursartenIDs.size();
	}

	/**
	 * Fügt einen Schüler hinzu.<br>
	 * Wirft eine Exception, falls die Schüler Daten inkonsistent sind.
	 *
	 * @param schueler   der Schüler, der hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Schüler Daten inkonsistent sind.
	 */
	private schuelerAddOhneSortierung(schueler: Schueler): void {
		DeveloperNotificationException.ifMapPutOverwrites(this.schuelerById, schueler.id, schueler);
		if (!this.fachwahlmengeBySchuelerId.containsKey(schueler.id)) {
			this.fachwahlmengeBySchuelerId.put(schueler.id, new ArrayList<GostFachwahl>());
		}
		this.dtoDaten.schueler.add(schueler);
	}

	/**
	 * Fügt einen Schüler hinzu.
	 *
	 * @param schueler   der Schüler, der hinzugefügt wird
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public schuelerAdd(schueler: Schueler): void {
		this.schuelerAddListe(ListUtils.create1(schueler));
	}

	/**
	 * Fügt alle Schüler hinzu.
	 *
	 * @param schuelermenge   die Menge an Schülern
	 *
	 * @throws DeveloperNotificationException Falls die Schüler-Daten inkonsistent sind.
	 */
	public schuelerAddListe(schuelermenge: List<Schueler>): void {
		const setId: HashSet<number> = new HashSet<number>();
		for (const sAlt of this.dtoDaten.schueler) {
			setId.add(sAlt.id);
		}
		for (const sNeu of schuelermenge) {
			DeveloperNotificationException.ifInvalidID("schueler.id", sNeu.id);
			DeveloperNotificationException.ifNull("schueler.geschlecht", Geschlecht.fromValue(sNeu.geschlecht));
			DeveloperNotificationException.ifNull("schueler.status", SchuelerStatus.data().getWertByID(sNeu.status as number));
			DeveloperNotificationException.ifTrue(JavaString.format("schueler.id %d Doppelung!", sNeu.id), !setId.add(sNeu.id));
		}
		for (const schueler of schuelermenge) {
			this.schuelerAddOhneSortierung(schueler);
		}
		this.dtoDaten.schueler.sort(this.compSchueler);
	}

	/**
	 * Liefert die Anzahl an Schülern, die mindestens eine Fachwahl haben.
	 *
	 * @return die Anzahl an Schülern, die mindestens eine Fachwahl haben
	 */
	public schuelerGetAnzahlMitMindestensEinerFachwahl(): number {
		const setSchuelerIDs: HashSet<number> | null = new HashSet<number>();
		for (const fachwahl of this.dtoDaten.fachwahlen) {
			setSchuelerIDs.add(fachwahl.schuelerID);
		}
		return setSchuelerIDs.size();
	}

	/**
	 * Liefert die Anzahl an Schülern.
	 *
	 * @return die Anzahl an Schülern
	 */
	public schuelerGetAnzahl(): number {
		return this.dtoDaten.schueler.size();
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return das zugehörige {@link Schueler}-Objekt
	 * @throws DeveloperNotificationException  Falls die Schüler-ID unbekannt ist.
	 */
	public schuelerGet(idSchueler: number): Schueler {
		return DeveloperNotificationException.ifNull("Schüler-Map.get(" + idSchueler + ")", this.schuelerById.get(idSchueler));
	}

	/**
	 * Ermittelt den Schüler für die angegebene ID. <br>
	 * Gibt null zurück, falls die Schüler-ID unbekannt ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return das zugehörige {@link Schueler}-Objekt oder null
	 */
	public schuelerGetOrNull(idSchueler: number): Schueler | null {
		return this.schuelerById.get(idSchueler);
	}

	/**
	 * Liefert die aktuelle Menge aller Schüler.
	 * Das ist die interne Referenz zur Liste der Schüler im {@link GostBlockungsdaten}-Objekt.
	 *
	 * @return die Menge aller Schüler
	 */
	public schuelerGetListe(): List<Schueler> {
		return this.dtoDaten.schueler;
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Kursart.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostKursart}
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachKursart(idSchueler: number, idFach: number): GostKursart {
		const fachwahl: GostFachwahl = this.schuelerGetOfFachFachwahl(idSchueler, idFach);
		return GostKursart.fromID(fachwahl.kursartID);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl}
	 * @throws DeveloperNotificationException falls der Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetOfFachFachwahl(idSchueler: number, idFach: number): GostFachwahl {
		return this.fachwahlBySchuelerIdAndFachId.getOrException(idSchueler, idFach);
	}

	/**
	 * Liefert zum Tupel (Schüler, Fach) die jeweilige Fachwahl. <br>
	 * Gibt null zurück, falls der Schüler das Fach nicht gewählt hat.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches
	 *
	 * @return zum Tupel (Schüler, Fach) jeweilige {@link GostFachwahl} oder null
	 */
	public schuelerGetOfFachFachwahlOrNull(idSchueler: number, idFach: number): GostFachwahl | null {
		return this.fachwahlBySchuelerIdAndFachId.getOrNull(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls der übergebene Schüler das entsprechende Fach gewählt hat.
	 *
	 * @param idSchueler   die Datenbank.ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches der Fachwahl des Schülers
	 *
	 * @return true, falls der Schüler das Fach gewählt hat
	 */
	public schuelerGetHatFach(idSchueler: number, idFach: number): boolean {
		return this.fachwahlBySchuelerIdAndFachId.contains(idSchueler, idFach);
	}

	/**
	 * Liefert TRUE, falls beide Schüler bezogen auf das Fach die selbe Kursart haben oder eine Exception.
	 *
	 * @param idSchueler1   die Datenbank-ID des 1. Schülers
	 * @param idSchueler2   die Datenbank-ID des 2. Schülers
	 * @param idFach        die Datenbank-ID des Faches
	 *
	 * @return true, falls beide Schüler im Fach die selbe Kursart haben
	 * @throws DeveloperNotificationException falls einer der beiden Schüler das Fach nicht gewählt hat.
	 */
	public schuelerGetHatDieSelbeKursartMitSchuelerInFach(idSchueler1: number, idSchueler2: number, idFach: number): boolean {
		const fachwahl1: GostFachwahl = this.fachwahlBySchuelerIdAndFachId.getOrException(idSchueler1, idFach);
		const fachwahl2: GostFachwahl = this.fachwahlBySchuelerIdAndFachId.getOrException(idSchueler2, idFach);
		return fachwahl1.kursartID === fachwahl2.kursartID;
	}

	/**
	 * Liefert TRUE, falls es den Schüler mit der entsprechenden Fachwahl (Fach + Kursart) gibt.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idFach       die Datenbank-ID des Faches der Fachwahl des Schülers
	 * @param idKursart    die Datenbank-ID der Kursart der Fachwahl des Schülers
	 *
	 * @return true, falls der Schüler die Fachwahl (Fach + Kursart) hat
	 */
	public schuelerGetHatFachart(idSchueler: number, idFach: number, idKursart: number): boolean {
		if (!this.fachwahlBySchuelerIdAndFachId.contains(idSchueler, idFach)) {
			return false;
		}
		return this.fachwahlBySchuelerIdAndFachId.getOrException(idSchueler, idFach).kursartID === idKursart;
	}

	/**
	 * Liefert die Menge aller {@link GostFachwahl} des Schülers.
	 * <br> Bei ungültiger Schüler-ID wird eine leere Liste geliefert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 *
	 * @return die Menge aller {@link GostFachwahl} des Schülers
	 */
	public schuelerGetListeOfFachwahlen(idSchueler: number): List<GostFachwahl> {
		const fachwahlen: List<GostFachwahl> | null = this.fachwahlmengeBySchuelerId.get(idSchueler);
		return (fachwahlen === null) ? new ArrayList() : fachwahlen;
	}

	/**
	 * Liefert eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler.
	 *
	 * @param idSchueler1   die Datenbank-ID des 1. Schülers
	 * @param idSchueler2   die Datenbank-ID des 2. Schülers
	 *
	 * @return eine Liste der gemeinsamen Fächer (auch in der Kursart übereinstimmend) beider Schüler
	 */
	public schuelerGetFachListeGemeinsamerFacharten(idSchueler1: number, idSchueler2: number): List<GostFach> {
		const temp: List<GostFach> = new ArrayList<GostFach>();
		for (const fachwahl1 of this.schuelerGetListeOfFachwahlen(idSchueler1)) {
			if (this.schuelerGetHatFachart(idSchueler2, fachwahl1.fachID, fachwahl1.kursartID)) {
				temp.add(this.manFaecher.getOrException(fachwahl1.fachID));
			}
		}
		return temp;
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS} im angegebenen Kurs verboten ist
	 */
	public schuelerGetIstVerbotenInKurs(idSchueler: number, idKurs: number): boolean {
		const key: LongArrayKey = new LongArrayKey(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs);
		return this.regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs verbietet.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs verbietet
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelVerbotenInKurs(idSchueler: number, idKurs: number): GostBlockungRegel {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull(this.toStringSchueler(idSchueler) + " hat gar kein Verbot für " + this.toStringKurs(idKurs) + "!", this.regelByMultikey.get(key));
	}

	/**
	 * Liefert TRUE, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return true, falls der Schüler aufgrund der Regel {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS} im angegebenen Kurs fixiert ist
	 */
	public schuelerGetIstFixiertInKurs(idSchueler: number, idKurs: number): boolean {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return this.regelByMultikey.containsKey(key);
	}

	/**
	 * Liefert die Regel, welche den Schüler in einem Kurs fixiert.
	 *
	 * @param idSchueler   die Datenbank-ID des Schülers
	 * @param idKurs       die Datenbank-ID des Kurses
	 *
	 * @return die Regel, welche den Schüler in einem Kurs fixiert
	 * @throws DeveloperNotificationException falls der Schüler oder der Kurs in der Blockung nicht existiert.
	 */
	public schuelerGetRegelFixiertInKurs(idSchueler: number, idKurs: number): GostBlockungRegel {
		const key: LongArrayKey = new LongArrayKey([GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, idSchueler, idKurs]);
		return DeveloperNotificationException.ifNull(this.toStringSchueler(idSchueler) + " hat gar keine Fixierung für " + this.toStringKurs(idKurs) + "!", this.regelByMultikey.get(key));
	}

	/**
	 * Gibt die ID der Blockung zurück.
	 *
	 * @return die ID der Blockung
	 */
	public getID(): number {
		return this.dtoDaten.id;
	}

	/**
	 * Setzt die ID dieser Blockung.
	 *
	 * @param idNeu   die Datenbank-ID, welche der Blockung zugewiesen wird
	 * @throws DeveloperNotificationException Falls die übergebene ID ungültig ist.
	 */
	public setID(idNeu: number): void {
		DeveloperNotificationException.ifInvalidID("pBlockungsID", idNeu);
		this.dtoDaten.id = idNeu;
	}

	/**
	 * Liefert die maximale Blockungszeit in Millisekunden.
	 *
	 * @return die maximale Blockungszeit in Millisekunden
	 */
	public getMaxTimeMillis(): number {
		return this.maxTimeMillis;
	}

	/**
	 * Setzt die maximale Blockungszeit in Millisekunden.
	 *
	 * @param blockungszeit   die maximale Blockungszeit in Millisekunden
	 * @throws DeveloperNotificationException falls der Wert nicht positiv ist.
	 */
	public setMaxTimeMillis(blockungszeit: number): void {
		DeveloperNotificationException.ifTrue("Der Wert muss positiv sein!", blockungszeit <= 0);
		this.maxTimeMillis = blockungszeit;
	}

	/**
	 * Liefert den Namen der Blockung.
	 *
	 * @return den Namen der Blockung
	 */
	public getName(): string {
		return this.dtoDaten.name;
	}

	/**
	 * Setzt den Namen der Blockung
	 *
	 * @param name   der Name, welcher der Blockung zugewiesen wird
	 * @throws UserNotificationException Falls der übergebene String leer ist.
	 */
	public setName(name: string): void {
		UserNotificationException.ifTrue("Ein leerer Name ist für die Blockung nicht zulässig.", JavaObject.equalsTranspiler("", (name)));
		this.dtoDaten.name = name;
	}

	/**
	 * Liefert das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @return das Halbjahr der gymnasialen Oberstufe
	 */
	public getHalbjahr(): GostHalbjahr {
		return GostHalbjahr.fromIDorException(this.dtoDaten.gostHalbjahr);
	}

	/**
	 * Setzt das Halbjahr der gymnasialen Oberstufe, für welches die Blockung angelegt wurde.
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe
	 */
	public setHalbjahr(halbjahr: GostHalbjahr): void {
		this.dtoDaten.gostHalbjahr = halbjahr.id;
	}

	/**
	 * Liefert TRUE, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist.
	 *
	 * @return true, falls in dieser Blockung genau 1 Ergebnis (die Blockungsvorlage) vorhanden ist
	 */
	public getIstBlockungsVorlage(): boolean {
		return this.dtoDaten.ergebnisse.size() === 1;
	}

	/**
	 * Liefert die Anzahl an Fächern.
	 *
	 * @return die Anzahl an Fächern
	 */
	public getFaecherAnzahl(): number {
		return this.manFaecher.faecher().size();
	}

	/**
	 * Gibt den Fächer-Manager zurück, der für die Blockungsdaten verwendet wird.
	 *
	 * @return der Fächer-Manager (siehe {@link GostFaecherManager})
	 */
	public faecherManager(): GostFaecherManager {
		return this.manFaecher;
	}

	/**
	 * Gibt die Blockungsdaten zurück.
	 *
	 * @return die Blockungsdaten (siehe {@link GostBlockungsdaten})
	 */
	public daten(): GostBlockungsdaten {
		return this.dtoDaten;
	}

	/**
	 * Liefert eine String-Representation vieler Daten.
	 *
	 * @return eine String-Representation vieler Daten
	 */
	public getDebugString(): string {
		const sb: StringBuilder = new StringBuilder();
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Ergebnisse = %d", this.dtoDaten.ergebnisse.size()));
		sb.append(this.lineSeparator);
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Schienen = %d", this.dtoDaten.schienen.size()));
		sb.append(this.lineSeparator);
		for (const s of this.dtoDaten.schienen) {
			sb.append(JavaString.format("    ID=%d, NR=%d, BEZ=%s, W-STD=%d", s.id, s.nummer, s.bezeichnung, s.wochenstunden));
			sb.append(this.lineSeparator);
			for (const e of this.ergebnisGetListeSortiertNachID()) {
				sb.append(JavaString.format("    Hat E %d Schiene %d --> %b", e.id, s.id, this.ergebnisManagerGet(e.id).getOfSchieneExists(s.id)));
				sb.append(this.lineSeparator);
			}
		}
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Schülermenge = %d", this.dtoDaten.schueler.size()));
		sb.append(this.lineSeparator);
		for (const s of this.dtoDaten.schueler) {
			sb.append(JavaString.format("    %d, %s, %s", s.id, s.nachname, s.vorname));
			sb.append(this.lineSeparator);
		}
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Kurse = %d", this.dtoDaten.kurse.size()));
		sb.append(this.lineSeparator);
		for (const k of this.dtoDaten.kurse) {
			sb.append(JavaString.format("    %d, %d, %d, %d", k.id, k.fach_id, k.kursart, k.nummer));
			sb.append(this.lineSeparator);
		}
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Fachwahlen = %d", this.dtoDaten.fachwahlen.size()));
		sb.append(this.lineSeparator);
		for (const fw of this.dtoDaten.fachwahlen) {
			sb.append(JavaString.format("    %d, %d, %d, %d, %b", fw.fachID, fw.kursartID, fw.schuelerID, fw.abiturfach, fw.istSchriftlich));
			sb.append(this.lineSeparator);
		}
		sb.append(this.lineSeparator);
		sb.append(JavaString.format("Regeln = %d", this.dtoDaten.regeln.size()));
		sb.append(this.lineSeparator);
		for (const r of this.dtoDaten.regeln) {
			sb.append(JavaString.format("    %d, %d, %s", r.id, r.typ, r.parameter));
			sb.append(this.lineSeparator);
		}
		return sb.toString();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostBlockungsdatenManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostBlockungsdatenManager'].includes(name);
	}

	public static readonly class = new Class<GostBlockungsdatenManager>('de.svws_nrw.core.utils.gost.GostBlockungsdatenManager');

}

export function cast_de_svws_nrw_core_utils_gost_GostBlockungsdatenManager(obj: unknown): GostBlockungsdatenManager {
	return obj as GostBlockungsdatenManager;
}
