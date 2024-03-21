import { JavaObject } from '../../java/lang/JavaObject';
import { GostFach } from '../../core/data/gost/GostFach';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { Random } from '../../java/util/Random';
import { KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { ArrayUtils } from '../../core/utils/ArrayUtils';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { GostKursart } from '../../core/types/gost/GostKursart';

export class KursblockungDynFachart extends JavaObject {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Eine laufende Nummer der Fachart.
	 */
	private readonly nr : number;

	/**
	 * Referenz zum zugehörigen GOST-Fach.
	 */
	private readonly gostFach : GostFach;

	/**
	 * Referenz zur zugehörigen GOST-Kursart.
	 */
	private readonly gostKursart : GostKursart;

	/**
	 * Ein Array aller Kurse dieser Fachart. Das Array bleibt stets aufsteigend nach Schülerzahlen sortiert.
	 */
	private kursArr : Array<KursblockungDynKurs>;

	/**
	 * Die maximale Anzahl an Schülern, die dieser Fachart zugeordnet sein können. Also die Anzahl der Schüler, die diese Fachart gewählt haben.
	 */
	private schuelerMax : number = 0;

	/**
	 * Die maximale Anzahl an Kursen, die dieser Fachart zugeordnet sein können.
	 */
	private kurseMax : number = 0;

	/**
	 * Die aktuelle Anzahl an Schülern, die dieser Fachart zugeordnet sind.
	 */
	private schuelerAnzNow : number = 0;

	/**
	 * Dem Statistik-Objekt wird eine Veränderung der Kursdifferenz mitgeteilt.
	 */
	private readonly statistik : KursblockungDynStatistik;

	/**
	 * Ordnet jedem Schüler die verbotenen anderen Schüler zu. Dimension des 2D-Arrays: [Schülerzahl][Dynamisch je Zeile]
	 */
	private readonly schuelerVerbotenMitSchueler : Array<Array<number>>;

	/**
	 * Ordnet jedem Schüler die zusammen-forcierten anderen Schüler zu. Dimension des 2D-Arrays: [Schülerzahl][Dynamisch je Zeile]
	 */
	private readonly schuelerZusammenMitSchueler : Array<Array<number>>;

	/**
	 * Wie oft die Fachart pro Schiene aktuell vertreten ist.
	 */
	private readonly _schienenCounter : Array<number>;

	/**
	 * Maximal erlaubte Anzahl von Kursen (dieser Fachart) pro Schiene.
	 */
	private _maxKurseProSchiene : number = 0;


	/**
	 * @param pRandom         Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pNr             Eine laufende Nummer (ID) für alle Facharten.
	 * @param pGostFach       Referenz zum zugehörigen GOST-Fach.
	 * @param pGostKursart    Referenz zur zugehörigen GOST-Kursart.
	 * @param pStatistik      Dem Statistik-Objekt wird eine Veränderung der Kursdifferenz mitgeteilt.
	 * @param schuelerAnzahl  Die Gesamtanzahl aller Schüler.
	 * @param schienenAnzahl  Die Gesamtanzahl aller Schienen.
	 */
	constructor(pRandom : Random, pNr : number, pGostFach : GostFach, pGostKursart : GostKursart, pStatistik : KursblockungDynStatistik, schuelerAnzahl : number, schienenAnzahl : number) {
		super();
		this._random = pRandom;
		this.nr = pNr;
		this.gostFach = pGostFach;
		this.gostKursart = pGostKursart;
		this.statistik = pStatistik;
		this.kursArr = Array(0).fill(null);
		this.kurseMax = 0;
		this.schuelerMax = 0;
		this.schuelerAnzNow = 0;
		this.schuelerVerbotenMitSchueler = [...Array(schuelerAnzahl)].map(e => Array(0).fill(0));
		this.schuelerZusammenMitSchueler = [...Array(schuelerAnzahl)].map(e => Array(0).fill(0));
		this._schienenCounter = Array(schienenAnzahl).fill(0);
		this._maxKurseProSchiene = 0;
	}

	/**
	 * Durch das Überschreiben dieser Methode, liefert dieses Objekt eine automatische String-Darstellung, beispielsweise 'D;LK'.
	 */
	public toString() : string {
		return this.gostFach.kuerzel + ";" + this.gostKursart.kuerzel;
	}

	/**
	 * Liefert die interne Nummer dieser Fachart.
	 *
	 * @return Die interne Nummer dieser Fachart.
	 */
	gibNr() : number {
		return this.nr;
	}

	/**
	 * Liefert das zugehörige Fach-Objekt.
	 *
	 * @return Das zugehörige Fach-Objekt.
	 */
	gibFach() : GostFach {
		return this.gostFach;
	}

	/**
	 * Liefert das zugehörige Kursart-Objekt.
	 *
	 * @return Das zugehörige Kursart-Objekt.
	 */
	gibKursart() : GostKursart {
		return this.gostKursart;
	}

	/**
	 * Liefert die Anzahl der SuS, die diese Fachart gewählt haben.
	 *
	 * @return die Anzahl der SuS, die diese Fachart gewählt haben.
	 */
	gibSchuelerMax() : number {
		return this.schuelerMax;
	}

	/**
	 * Liefert die aktuelle Anzahl an SuS, die dieser Fachart zugeordnet sind.
	 *
	 * @return die aktuelle Anzahl an SuS, die dieser Fachart zugeordnet sind.
	 */
	gibSchuelerZordnungen() : number {
		return this.schuelerAnzNow;
	}

	/**
	 * Liefert die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 *
	 * @return die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 */
	gibKurseMax() : number {
		return this.kurseMax;
	}

	/**
	 * Liefert die aktuell größte Kursdifferenz.
	 * <br>Das ist: Größter Kurs - Kleinster Kurs
	 *
	 * @return die aktuell größte Kursdifferenz.
	 */
	gibKursdifferenz() : number {
		return this.kursArr[this.kursArr.length - 1].gibSchuelerAnzahl() - this.kursArr[0].gibSchuelerAnzahl();
	}

	/**
	 * Liefert das Array aller Kurse dieser Fachart.
	 * <br>Die Kurse sind aufsteigend nach ihrer Schüleranzahl sortiert.
	 *
	 * @return Das Array aller Kurse dieser Fachart.
	 */
	gibKurse() : Array<KursblockungDynKurs> {
		return this.kursArr;
	}

	/**
	 * Liefert den Kurs mit der geringsten SuS-Anzahl, welcher in Schiene vorkommt.
	 * <br>Ignoriert Kurse, die bereits voll sind (Regel: max. SuS).
	 * <br>Ignoriert Kurse, die für den aktuellen Schüler gesperrt sind.
	 *
	 * @param  pSchiene      Die Schiene, in der gesucht wird.
	 * @param  s             Das {@link KursblockungDynSchueler}-Objekt.
	 *
	 * @return den Kurs mit der geringsten SuS-Anzahl, welcher in Schiene vorkommt.
	 */
	gibKleinstenKursInSchieneFuerSchueler(pSchiene : number, s : KursblockungDynSchueler) : KursblockungDynKurs | null {
		for (let i : number = 0; i < this.kursArr.length; i++) {
			const kurs : KursblockungDynKurs = this.kursArr[i];
			if (kurs.gibIstErlaubtFuerSchueler(s))
				for (const c of kurs.gibSchienenLage())
					if (c === pSchiene)
						return kurs;
		}
		return null;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 */
	gibHatMultikurs() : boolean {
		for (const kurs of this.kursArr)
			if (kurs.gibSchienenAnzahl() > 1)
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 *
	 * @param  pSchiene      Die Schiene, die angefragt wurde.
	 * @param  s             Das {@link KursblockungDynSchueler}-Objekt.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 */
	gibHatSchuelerKursInSchiene(pSchiene : number, s : KursblockungDynSchueler) : boolean {
		for (const kurs of this.kursArr)
			if (kurs.gibIstErlaubtFuerSchueler(s) && kurs.gibIstInSchiene(pSchiene))
				return true;
		return false;
	}

	/**
	 * Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 *
	 * @param  pSchiene      Die Schiene, die angefragt wurde.
	 * @param  s             Das {@link KursblockungDynSchueler}-Objekt.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 */
	gibHatSchuelerKursMitFreierSchiene(pSchiene : number, s : KursblockungDynSchueler) : boolean {
		for (const kurs of this.kursArr)
			if (kurs.gibIstErlaubtFuerSchueler(s) && kurs.gibIstSchieneFrei(pSchiene))
				return true;
		return false;
	}

	/**
	 * Liefert alle anderen Schüler, die mit dem übergebenen Schüler bei dieser Fachart nicht im selben Kurs landen sollen.
	 *
	 * @param schuelerNr  Die Nummer des übergebenen Schülers.
	 *
	 * @return alle anderen Schüler, die mit dem übergebenen Schüler bei dieser Fachart nicht im selben Kurs landen sollen.
	 */
	gibVonSchuelerVerbotenMit(schuelerNr : number) : Array<number> {
		return this.schuelerVerbotenMitSchueler[schuelerNr];
	}

	/**
	 * Liefert alle anderen Schüler, die mit dem übergebenen Schüler bei dieser Fachart im selben Kurs landen sollen.
	 *
	 * @param schuelerNr  Die Nummer des übergebenen Schülers.
	 *
	 * @return alle anderen Schüler, die mit dem übergebenen Schüler bei dieser Fachart im selben Kurs landen sollen.
	 */
	gibVonSchuelerZusammenMit(schuelerNr : number) : Array<number> {
		return this.schuelerZusammenMitSchueler[schuelerNr];
	}

	/**
	 * Ordnet alle Kurse der Fachart zu. Die Kurse haben noch keine SuS und sind somit automatisch sortiert.
	 *
	 * @param pKursArr  Alle Kurse der Fachart.
	 */
	aktionSetKurse(pKursArr : Array<KursblockungDynKurs>) : void {
		this.kursArr = pKursArr;
	}

	/**
	 * Erhöht die Anzahl ({@link #schuelerMax}) an SuS, die diese Fachart gewählt haben um 1.
	 */
	aktionMaxSchuelerErhoehen() : void {
		this.schuelerMax++;
	}

	/**
	 * Erhöht die Anzahl ({@link #kurseMax}) an Kursen, die zu dieser Fachart gehören.
	 */
	aktionMaxKurseErhoehen() : void {
		this.kurseMax++;
	}

	/**
	 * Muss aufgerufen werden, bevor die Schüleranzahl eines Kurses verändert wird.
	 */
	aktionKursdifferenzEntfernen() : void {
		this.statistik.aktionKursdifferenzEntfernen(this.gibKursdifferenz());
	}

	/**
	 * Muss aufgerufen werden, nachdem die Schüleranzahl eines Kurses verändert wird.
	 */
	aktionKursdifferenzHinzufuegen() : void {
		this.statistik.aktionKursdifferenzHinzufuegen(this.gibKursdifferenz());
	}

	/**
	 * Erhöht die Anzahl ({@link #schuelerAnzNow}) an Schülern, die dieser Fachart momentan zugeordnet sind um 1.
	 * Da ein (bestimmter) Kurs nun einen S. mehr hat, muss das Array einmalig von links nach rechts sortiert werden.
	 */
	aktionSchuelerWurdeHinzugefuegt() : void {
		this.schuelerAnzNow++;
		for (let i : number = 1; i < this.kursArr.length; i++) {
			const kursL : KursblockungDynKurs = this.kursArr[i - 1];
			const kursR : KursblockungDynKurs = this.kursArr[i];
			const b1 : boolean = kursL.gibSchuelerAnzahl() > kursR.gibSchuelerAnzahl();
			const b2 : boolean = (kursL.gibSchuelerAnzahl() === kursR.gibSchuelerAnzahl()) && (kursL.gibDatenbankID() > kursR.gibDatenbankID());
			if (b1 || b2) {
				this.kursArr[i - 1] = kursR;
				this.kursArr[i] = kursL;
			}
		}
	}

	/**
	 * Verringert die Anzahl ({@link #schuelerAnzNow}) an SuS, die dieser Fachart momentan zugeordnet sind um 1.
	 * Da ein (bestimmter) Kurs nun einen S. weniger hat, muss das Array einmalig von rechts nach links sortiert werden.
	 */
	aktionSchuelerWurdeEntfernt() : void {
		this.schuelerAnzNow--;
		for (let i : number = this.kursArr.length - 1; i >= 1; i--) {
			const kursL : KursblockungDynKurs = this.kursArr[i - 1];
			const kursR : KursblockungDynKurs = this.kursArr[i];
			const b1 : boolean = kursL.gibSchuelerAnzahl() > kursR.gibSchuelerAnzahl();
			const b2 : boolean = (kursL.gibSchuelerAnzahl() === kursR.gibSchuelerAnzahl()) && (kursL.gibDatenbankID() > kursR.gibDatenbankID());
			if (b1 || b2) {
				this.kursArr[i - 1] = kursR;
				this.kursArr[i] = kursL;
			}
		}
	}

	/**
	 * Lässt einen zufälligen Kurs dieser Fachart in die angegebene Schiene wandern.
	 *
	 * @param pSchiene  Die Schiene, in die einer Kurs der Fachart wandern soll.
	 */
	aktionZufaelligerKursWandertNachSchiene(pSchiene : number) : void {
		const perm : Array<number> = KursblockungStatic.gibPermutation(this._random, this.kursArr.length);
		for (let p : number = 0; p < perm.length; p++) {
			const kurs : KursblockungDynKurs | null = this.kursArr[perm[p]];
			if (kurs.gibIstSchieneFrei(pSchiene)) {
				kurs.aktionSetzeInSchiene(pSchiene);
				return;
			}
		}
		throw new DeveloperNotificationException("aktionZufaelligerKursWandertNachSchiene(" + pSchiene + ")")
	}

	/**
	 * Erhöht das Vorkommen dieser Fachart in der übergebenen Schiene. Aktualisiert ggf. eine Verletzung der Regel 18.
	 *
	 * @param schiene  Die Schiene um die es geht.
	 */
	aktionSchieneWurdeHinzugefuegt(schiene : KursblockungDynSchiene) : void {
		if (this._maxKurseProSchiene <= 0)
			return;
		this._schienenCounter[schiene.gibNr()]++;
		if (this._schienenCounter[schiene.gibNr()] === this._maxKurseProSchiene)
			this.statistik.regelverletzungVeraendern(+1);
	}

	/**
	 * Verringert das Vorkommen dieser Fachart in der übergebenen Schiene. Aktualisiert ggf. eine Verletzung der Regel 18.
	 *
	 * @param schiene  Die Schiene um die es geht.
	 */
	aktionSchieneWurdeEntfernt(schiene : KursblockungDynSchiene) : void {
		if (this._maxKurseProSchiene <= 0)
			return;
		if (this._schienenCounter[schiene.gibNr()] === this._maxKurseProSchiene)
			this.statistik.regelverletzungVeraendern(-1);
		this._schienenCounter[schiene.gibNr()]--;
	}

	/**
	 * Debug Ausgabe. Nur für Testzwecke.
	 *
	 * @param schuelerArr  Das Array mit den Schülerdaten.
	 */
	debug(schuelerArr : Array<KursblockungDynSchueler>) : void {
		for (let i : number = 0; i < this.kursArr.length; i++)
			this.kursArr[i].debug(schuelerArr);
	}

	/**
	 * Verbietet, dass zwei Schüler den selben Kurs der Fachart besuchen.
	 *
	 * @param internalID1  Die interne ID des 1. Schülers.
	 * @param internalID2  Die interne ID des 2. Schülers.
	 */
	regel_schueler_verbieten_mit_schueler(internalID1 : number, internalID2 : number) : void {
		this.schuelerVerbotenMitSchueler[internalID1] = ArrayUtils.erweitern(this.schuelerVerbotenMitSchueler[internalID1], internalID2);
		this.schuelerVerbotenMitSchueler[internalID2] = ArrayUtils.erweitern(this.schuelerVerbotenMitSchueler[internalID2], internalID1);
	}

	/**
	 * Forciert, dass zwei Schüler den selben Kurs der Fachart besuchen.
	 *
	 * @param internalID1  Die interne ID des 1. Schülers.
	 * @param internalID2  Die interne ID des 2. Schülers.
	 */
	regel_schueler_zusammen_mit_schueler(internalID1 : number, internalID2 : number) : void {
		this.schuelerZusammenMitSchueler[internalID1] = ArrayUtils.erweitern(this.schuelerZusammenMitSchueler[internalID1], internalID2);
		this.schuelerZusammenMitSchueler[internalID2] = ArrayUtils.erweitern(this.schuelerZusammenMitSchueler[internalID2], internalID1);
	}

	/**
	 * Definiert, wie oft ein Kurs dieser Fachart maximal pro Schiene vorkommen darf.
	 *
	 * @param maximalProSchiene  Die maximale Anzahl pro Schiene.
	 */
	regel_18_maximalProSchiene(maximalProSchiene : number) : void {
		this._maxKurseProSchiene = maximalProSchiene;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynFachart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynFachart'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynFachart(obj : unknown) : KursblockungDynFachart {
	return obj as KursblockungDynFachart;
}
