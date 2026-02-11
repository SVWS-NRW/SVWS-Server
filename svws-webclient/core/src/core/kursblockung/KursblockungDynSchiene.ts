import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { HashMap } from '../../java/util/HashMap';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../core/logger/Logger';
import { GostKursart } from '../../core/types/gost/GostKursart';
import { LogLevel } from '../../core/logger/LogLevel';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { Class } from '../../java/lang/Class';
import { HashSet } from '../../java/util/HashSet';

export class KursblockungDynSchiene extends JavaObject {

	/**
	 * Die Nummer der Schiene. Wenn es 14 Schienen gibt, dann gibt es 14 Objekte dieser Klasse mit den Nummern 0 bis 13.
	 */
	private readonly nr: number;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly logger: Logger;

	/**
	 * Die aktuellen Kurse in dieser Schiene. Über die ID (Long-Wert der GUI) kann man schnell darauf zugreifen.
	 */
	private readonly kursMap: HashMap<number, KursblockungDynKurs>;

	/**
	 * Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert.
	 */
	private readonly statistik: KursblockungDynStatistik;


	/**
	 * Im Konstruktor werden die Referenzen übernommen und das HashMap erzeugt.
	 *
	 * @param pLogger     Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pNr         Die Nummer der Schiene.
	 * @param pStatistik  Das Statistik-Objekt wird über die aktuellen Kurs-Paarungen informiert.
	 */
	public constructor(pLogger: Logger, pNr: number, pStatistik: KursblockungDynStatistik) {
		super();
		this.logger = pLogger;
		this.nr = pNr;
		this.kursMap = new HashMap();
		this.statistik = pStatistik;
	}

	/**
	 * Gibt die String-Repräsentation der Schiene zurück.
	 *
	 * @return die String-Repräsentation der Schiene
	 */
	public toString(): string {
		return "" + this.nr;
	}

	/**
	 * Fügt der Schiene einen Kurs hinzu. Das Statistik-Objekt wird über neue Kurs-Paarungen informiert.
	 *
	 * @param kurs1 Der Kurs, welcher der Schiene hinzugefügt werden soll.
	 */
	public aktionKursHinzufuegen(kurs1: KursblockungDynKurs): void {
		const kursID: number = kurs1.gibDatenbankID();
		if (this.kursMap.containsKey(kursID)) {
			const fehler: string | null = "Kurs '" + kurs1.toString() + "' soll in Schiene " + this.nr + ", ist aber bereits drin.";
			this.logger.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler)
		}
		kurs1.gibFachart().aktionSchieneWurdeHinzugefuegt(this);
		for (const kurs2 of this.kursMap.values())
			this.statistik.aktionKurspaarInSchieneHinzufuegen(kurs1, kurs2);
		this.kursMap.put(kursID, kurs1);
	}

	/**
	 * Entfernt aus der Schiene einen Kurs. Das Statistik-Objekt wird über zu entfernende Kurs-Paarungen informiert.
	 *
	 * @param kurs1 Der Kurs, welcher aus der Schiene entfernt werden soll.
	 */
	public aktionKursEntfernen(kurs1: KursblockungDynKurs): void {
		const kursID: number = kurs1.gibDatenbankID();
		if (!this.kursMap.containsKey(kursID)) {
			const fehler: string | null = "Kurs '" + kurs1.toString() + "' soll aus Schiene " + this.nr + " entfernt werden, ist aber nicht drin.";
			this.logger.logLn(LogLevel.ERROR, fehler);
			throw new DeveloperNotificationException(fehler)
		}
		this.kursMap.remove(kursID);
		kurs1.gibFachart().aktionSchieneWurdeEntfernt(this);
		for (const kurs2 of this.kursMap.values())
			this.statistik.aktionKurspaarInSchieneEntfernen(kurs1, kurs2);
	}

	/**
	 * Liefert die aktuelle Nummer der Schiene (0-indiziert).
	 *
	 * @return Die aktuelle Nummer der Schiene (0-indiziert).
	 */
	public gibNr(): number {
		return this.nr;
	}

	/**
	 * Liefert die aktuelle Anzahl an Kursen in dieser Schiene.
	 *
	 * @return Die aktuelle Anzahl an Kursen in dieser Schiene.
	 */
	public gibKursAnzahl(): number {
		return this.kursMap.size();
	}

	/**
	 * Liefert die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 * für die Blockung verwendet.
	 *
	 * @return die Anzahl an Kursen mit gleicher Fachart in dieser Schiene. Diese Anzahl wird als Bewertungskriterium
	 *         für die Blockung verwendet.
	 */
	gibAnzahlGleicherFacharten(): number {
		const setFachart: HashSet<number> | null = new HashSet<number>();
		let summe: number = 0;
		for (const kurs of this.kursMap.values())
			if (!setFachart.add(kurs.gibFachart().gibNr()))
				summe++;
		return summe;
	}

	/**
	 * Debug-Ausgabe. Nur für Testzwecke.
	 *
	 * @param nurMultikurse Falls TRUE, werden nur Multikurse angezeigt.
	 */
	public debug(nurMultikurse: boolean): void {
		this.logger.modifyIndent(+4);
		for (const k of this.kursMap.values()) {
			if ((nurMultikurse) && (k.gibSchienenAnzahl() < 2))
				continue;
			this.logger.logLn("    " + k.toString());
		}
		this.logger.modifyIndent(-4);
	}

	/**
	 * Ausgabe der Kurse dieser Schiene (4 eingerückt).
	 */
	public printlnKurse(): void {
		console.log(JSON.stringify("Schiene " + (this.nr + 1)));
		for (const k of this.kursMap.values())
			console.log(JSON.stringify("    ID " + k.gibDatenbankID() + ", " + k.gibFachart()));
	}

	/**
	 * Ausgabe der Kurse (4 eingerückt) dieser Schiene zusammen mit den SuS (8 eingerückt) der Kurse.
	 *
	 * @param _schuelerArr  Die Menge alle SuS.
	 */
	public printlnKurseUndSchueler(_schuelerArr: Array<KursblockungDynSchueler>): void {
		console.log(JSON.stringify("Schiene " + (this.nr + 1)));
		for (const k of this.kursMap.values()) {
			console.log(JSON.stringify("    ID " + k.gibDatenbankID() + ", " + k.gibFachart() + ", Fach-ID=" + k.gibFachID()));
			for (const s of _schuelerArr)
				if (s.gibIstInKurs(k))
					console.log(JSON.stringify("        ID " + s.gibDatenbankID() + ", " + s.gibRepresentation()));
		}
	}

	/**
	 * Liefert true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 *
	 * @return true, falls in der Schiene nur Kurse der Kursart LK sind (oder keine Kurse).
	 */
	public gibHatNurLK(): boolean {
		for (const k of this.kursMap.values())
			if (k.gibFachart().gibKursart() as unknown !== GostKursart.LK as unknown)
				return false;
		return true;
	}

	/**
	 * Liefert true, falls in der Schiene keine Kurse der Kursart LK sind.
	 *
	 * @return true, falls in der Schiene keine Kurse der Kursart LK sind.
	 */
	public gibHatKeineLK(): boolean {
		for (const k of this.kursMap.values())
			if (k.gibFachart().gibKursart() as unknown === GostKursart.LK as unknown)
				return false;
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynSchiene';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynSchiene'].includes(name);
	}

	public static readonly class = new Class<KursblockungDynSchiene>('de.svws_nrw.core.kursblockung.KursblockungDynSchiene');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynSchiene(obj: unknown): KursblockungDynSchiene {
	return obj as KursblockungDynSchiene;
}
