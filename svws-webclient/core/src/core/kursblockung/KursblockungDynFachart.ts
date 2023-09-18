import { JavaObject } from '../../java/lang/JavaObject';
import { GostFach } from '../../core/data/gost/GostFach';
import { KursblockungDynStatistik } from '../../core/kursblockung/KursblockungDynStatistik';
import { KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { Random } from '../../java/util/Random';
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
	 * Ein Array aller Kurse dieser Fachart. Das Array bleibt dynamisch sortiert, so dass im Array zunächst der Kurs
	 *  mit der geringsten Schüleranzahl ist.
	 */
	private kursArr : Array<KursblockungDynKurs>;

	/**
	 * Die maximale Anzahl an Schülern, die dieser Fachart zugeordnet sein können. Also die Anzahl der Schüler, die
	 *  diese Fachart gewählt haben.
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
	 * @param pRandom  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pNr          Eine laufende Nummer (ID) für alle Facharten.
	 * @param pGostFach    Referenz zum zugehörigen GOST-Fach.
	 * @param pGostKursart Referenz zur zugehörigen GOST-Kursart.
	 * @param pStatistik   Dem Statistik-Objekt wird eine Veränderung der Kursdifferenz mitgeteilt.
	 */
	public constructor(pRandom : Random, pNr : number, pGostFach : GostFach, pGostKursart : GostKursart, pStatistik : KursblockungDynStatistik) {
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
	}

	/**
	 *Durch das Überschreiben dieser Methode, liefert dieses Objekt eine automatische String-Darstellung,
	 * beispielsweise 'D;LK'.
	 */
	public toString() : string {
		return this.gostFach.kuerzel + ";" + this.gostKursart.kuerzel;
	}

	/**
	 *Liefert die Nummer dieser Fachart.
	 *
	 * @return Die Nummer dieser Fachart.
	 */
	gibNr() : number {
		return this.nr;
	}

	/**
	 *Liefert die maximale Anzahl ({@link #schuelerMax}) an SuS, die dieser Fachart zugeordnet sein können. Das ist
	 * die Anzahl der Fachwahlen.
	 *
	 * @return Die Anzahl der SuS, die diese Fachart gewählt haben.
	 */
	gibSchuelerMax() : number {
		return this.schuelerMax;
	}

	/**
	 *Liefert die aktuelle Anzahl ({@link #schuelerAnzNow}) an SuS, die dieser Fachart zugeordnet sind.
	 *
	 * @return Die Anzahl der SuS, die diese Fachart aktuell zugeordnet sind.
	 */
	gibSchuelerZordnungen() : number {
		return this.schuelerAnzNow;
	}

	/**
	 *Liefert die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 *
	 * @return Die Anzahl der Kurse die dieser Fachart zugeordnet sind.
	 */
	gibKurseMax() : number {
		return this.kurseMax;
	}

	/**
	 * Liefert die aktuell größte Kursdifferenz.
	 *
	 * @return Die aktuell größte Kursdifferenz.
	 */
	gibKursdifferenz() : number {
		return this.kursArr[this.kursArr.length - 1].gibSchuelerAnzahl() - this.kursArr[0].gibSchuelerAnzahl();
	}

	/**
	 * Liefert das Array aller Kurse dieser Fachart.
	 *
	 * @return Das Array aller Kurse dieser Fachart.
	 */
	gibKurse() : Array<KursblockungDynKurs> {
		return this.kursArr;
	}

	/**
	 * Liefert den Kurs mit der geringsten SuS-Anzahl, welcher in Schiene {@code pSchiene } vorkommt.
	 *
	 * @param  pSchiene     Die Schiene, in der gesucht wird.
	 * @param  kursGesperrt Definiert, alle Kurse des S. die gesperrt sind und somit ignoriert werden sollen.
	 * @return              Der kleinste Kurs in der Schiene pSchiene, oder null.
	 */
	gibKleinstenKursInSchiene(pSchiene : number, kursGesperrt : Array<boolean>) : KursblockungDynKurs | null {
		for (let i : number = 0; i < this.kursArr.length; i++) {
			const kurs : KursblockungDynKurs = this.kursArr[i];
			if (kursGesperrt[kurs.gibInternalID()]) {
				continue;
			}
			for (const c of kurs.gibSchienenLage()) {
				if (c === pSchiene) {
					return kurs;
				}
			}
		}
		return null;
	}

	/**
	 *Liefert TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 *
	 * @return TRUE, falls mindestens ein Kurs dieser Fachart ein Multikurs ist.
	 */
	gibHatMultikurs() : boolean {
		for (const kurs of this.kursArr) {
			if (kurs.gibSchienenAnzahl() > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 *Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 *
	 * @param  pSchiene     Die Schiene, die angefragt wurde.
	 * @param  kursGesperrt Falls TRUE, muss dieser Kurs ignoriert werden.
	 * @return              TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c ist.
	 */
	gibHatKursInSchiene(pSchiene : number, kursGesperrt : Array<boolean>) : boolean {
		for (const kurs of this.kursArr) {
			if (kursGesperrt[kurs.gibInternalID()]) {
				continue;
			}
			if (kurs.gibIstInSchiene(pSchiene)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *Liefert TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 *
	 * @param  pSchiene     Die Schiene, die angefragt wurde.
	 * @param  kursGesperrt Falls TRUE, muss dieser Kurs ignoriert werden.
	 * @return              TRUE, falls mindestens ein Kurs dieser Fachart in Schiene c wandern darf.
	 */
	public gibHatKursMitFreierSchiene(pSchiene : number, kursGesperrt : Array<boolean>) : boolean {
		for (const kurs of this.kursArr) {
			if (kursGesperrt[kurs.gibInternalID()]) {
				continue;
			}
			if (kurs.gibIstSchieneFrei(pSchiene)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *Ordnet alle Kurse der Fachart zu. Die Kurse haben noch keine SuS und sind somit automatisch sortiert.
	 *
	 * @param pKursArr Alle Kurse der Fachart.
	 */
	public aktionSetKurse(pKursArr : Array<KursblockungDynKurs>) : void {
		this.kursArr = pKursArr;
	}

	/**
	 *Erhöht die Anzahl ({@link #schuelerMax}) an SuS, die diese Fachart gewählt haben um 1.
	 */
	public aktionMaxSchuelerErhoehen() : void {
		this.schuelerMax++;
	}

	/**
	 *Erhöht die Anzahl ({@link #kurseMax}) an Kursen, die zu dieser Fachart gehören.
	 */
	public aktionMaxKurseErhoehen() : void {
		this.kurseMax++;
	}

	/**
	 *Muss aufgerufen werden, bevor die Schüleranzahl eines Kurses verändert wird.
	 */
	public aktionKursdifferenzEntfernen() : void {
		this.statistik.aktionKursdifferenzEntfernen(this.gibKursdifferenz());
	}

	/**
	 *Muss aufgerufen werden, nachdem die Schüleranzahl eines Kurses verändert wird.
	 */
	public aktionKursdifferenzHinzufuegen() : void {
		this.statistik.aktionKursdifferenzHinzufuegen(this.gibKursdifferenz());
	}

	/**
	 *Erhöht die Anzahl ({@link #schuelerAnzNow}) an Schülern, die dieser Fachart momentan zugeordnet sind um 1. Da
	 * ein (bestimmter) Kurs nun einen S. mehr hat, muss das Array einmalig von links nach rechts sortiert werden.
	 */
	public aktionSchuelerWurdeHinzugefuegt() : void {
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
	 *Verringert die Anzahl ({@link #schuelerAnzNow}) an SuS, die dieser Fachart momentan zugeordnet sind um 1. Da ein
	 * (bestimmter) Kurs nun einen S. weniger hat, muss das Array einmalig von rechts nach links sortiert werden.
	 */
	public aktionSchuelerWurdeEntfernt() : void {
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
	 *Lässt einen zufälligen Kurs dieser Fachart in die angegebene Schiene wandern.
	 *
	 * @param pSchiene Die Schiene, in die einer Kurs der Fachart wandern soll.
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
		throw new DeveloperNotificationException("aktionZufaelligerKursWandertNachSchiene: THIS SHOULD NOT BE REACHED!!!")
	}

	/**
	 *Debug Ausgabe. Nur für Testzwecke.
	 *
	 * @param schuelerArr Das Array mit den Schülerdaten.
	 */
	debug(schuelerArr : Array<KursblockungDynSchueler>) : void {
		for (let i : number = 0; i < this.kursArr.length; i++) {
			this.kursArr[i].debug(schuelerArr);
		}
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynFachart'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynFachart(obj : unknown) : KursblockungDynFachart {
	return obj as KursblockungDynFachart;
}
