import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { AVLSet, cast_de_nrw_schule_svws_core_adt_set_AVLSet } from '../../core/adt/set/AVLSet';
import { KursblockungDynStatistik, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { HashMap, cast_java_util_HashMap } from '../../java/util/HashMap';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';
import { UserNotificationException, cast_de_nrw_schule_svws_core_exceptions_UserNotificationException } from '../../core/exceptions/UserNotificationException';
import { System, cast_java_lang_System } from '../../java/lang/System';

export class KursblockungDynSchiene extends JavaObject {

	/**
	 * Die Nummer der Schiene. Wenn es 14 Schienen gibt, dann gibt es 14 Objekte dieser Klasse mit den Nummern 0 bis 13. 
	 */
	private readonly nr : number;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler. 
	 */
	private readonly logger : Logger;

	/**
	 * Die aktuellen Kurse in dieser Schiene. Über die ID (Long-Wert der GUI) kann man schnell darauf zugreifen. 
	 */
	private readonly kursMap : HashMap<number, KursblockungDynKurs>;

	/**
	 * Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. 
	 */
	private readonly statistik : KursblockungDynStatistik;


	/**
	 *Im Konstruktor werden die Referenzen übernommen und das HashMap erzeugt.
	 * 
	 * @param pLogger    Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pNr        Die Nummer der Schiene.
	 * @param pStatistik Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert. 
	 */
	public constructor(pLogger : Logger, pNr : number, pStatistik : KursblockungDynStatistik) {
		super();
		this.logger = pLogger;
		this.nr = pNr;
		this.kursMap = new HashMap();
		this.statistik = pStatistik;
	}

	public toString() : string {
		return "" + this.nr;
	}

	/**
	 *
	 * Fügt der Schiene einen Kurs hinzu. Das Statistik-Objekt wird über neue Kurs-Paarungen informiert.
	 * 
	 * @param kurs1 Der Kurs, welcher der Schiene hinzugefügt werden soll. 
	 */
	public aktionKursHinzufuegen(kurs1 : KursblockungDynKurs) : void {
		let kursID : number = kurs1.gibDatenbankID();
		if (this.kursMap.containsKey(kursID)) {
			let fehler : string | null = "Kurs \'" + kurs1.toString()! + "\' soll in Schiene " + this.nr + ", ist aber bereits drin.";
			this.logger.logLn(LogLevel.ERROR, fehler);
			throw new UserNotificationException(fehler)
		}
		for (let kurs2 of this.kursMap.values()) 
			this.statistik.aktionKurspaarInSchieneHinzufuegen(kurs1, kurs2);
		this.kursMap.put(kursID, kurs1);
	}

	/**
	 *Entfernt aus der Schiene einen Kurs. Das Statistik-Objekt wird über zu entfernende Kurs-Paarungen informiert.
	 * 
	 * @param kurs1 Der Kurs, welcher aus der Schiene entfernt werden soll. 
	 */
	public aktionKursEntfernen(kurs1 : KursblockungDynKurs) : void {
		let kursID : number = kurs1.gibDatenbankID();
		if (!this.kursMap.containsKey(kursID)) {
			let fehler : string | null = "Kurs \'" + kurs1.toString()! + "\' soll aus Schiene " + this.nr + " entfernt werden, ist aber nicht drin.";
			this.logger.logLn(LogLevel.ERROR, fehler);
			throw new UserNotificationException(fehler)
		}
		this.kursMap.remove(kursID);
		for (let kurs2 of this.kursMap.values()) 
			this.statistik.aktionKurspaarInSchieneEntfernen(kurs1, kurs2);
	}

	/**
	 *Liefert die aktuelle Nummer der Schiene (0-indiziert).
	 * 
	 * @return Die aktuelle Nummer der Schiene (0-indiziert). 
	 */
	public gibNr() : number {
		return this.nr;
	}

	/**
	 *Liefert die aktuelle Anzahl an Kursen in dieser Schiene.
	 * 
	 * @return Die aktuelle Anzahl an Kursen in dieser Schiene. 
	 */
	public gibKursAnzahl() : number {
		return this.kursMap.size();
	}

	/**
	 *Debug-Ausgabe. Nur für Testzwecke.
	 * 
	 * @param nurMultikurse Falls TRUE, werden nur Multikurse angezeigt. 
	 */
	public debug(nurMultikurse : boolean) : void {
		for (let k of this.kursMap.values()) {
			if (nurMultikurse) {
				if (k.gibSchienenAnzahl() < 2) {
					continue;
				}
			}
			console.log(JSON.stringify("    " + k.toString()! + "\n"));
		}
	}

	/**
	 *Liefert die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 * für die Blockung verwendet.
	 * 
	 * @return die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 *         für die Blockung verwendet. 
	 */
	gibAnzahlGleicherFacharten() : number {
		let setFachart : AVLSet<number | null> | null = new AVLSet();
		let summe : number = 0;
		for (let kurs of this.kursMap.values()) 
			if (setFachart.add(kurs.gibFachart().gibNr()) === false) 
				summe++;
		return summe;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynSchiene'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchiene(obj : unknown) : KursblockungDynSchiene {
	return obj as KursblockungDynSchiene;
}
