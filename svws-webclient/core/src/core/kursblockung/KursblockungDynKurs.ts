import { JavaObject } from '../../java/lang/JavaObject';
import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { Random } from '../../java/util/Random';
import { LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { Class } from '../../java/lang/Class';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { LogLevel } from '../../core/logger/LogLevel';

export class KursblockungDynKurs extends JavaObject {

	/**
	 * Der Default-Wert für die höchste Schüleranzahl pro Kurs.
	 */
	private static readonly MAX_SUS_DEFAULT : number = 1000;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	/**
	 * Die ID des Kurses, welche von der Datenbank kommt.
	 */
	private readonly databaseID : number;

	/**
	 * Die interne ID des Kurses.
	 */
	private readonly internalKursID : number;

	/**
	 * Die Fachart die zu diesem Kurs gehört.
	 */
	private readonly fachart : KursblockungDynFachart;

	/**
	 * Schienen in denen der Kurs gerade ist, zum Teil fixiert.
	 */
	private readonly schienenLage : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage}
	 */
	private readonly schienenLageSaveS : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage}
	 */
	private readonly schienenLageSaveK : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenLage}
	 */
	private readonly schienenLageSaveG : Array<KursblockungDynSchiene>;

	/**
	 * Anzahl der Fixierungen.
	 */
	private readonly schienenLageFixiert : number;

	/**
	 * Schienen die getauscht werden könnten.
	 */
	private readonly schienenFrei : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei}
	 */
	private readonly schienenFreiSaveS : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei}
	 */
	private readonly schienenFreiSaveK : Array<KursblockungDynSchiene>;

	/**
	 * Zum Speichern des Arrays {@link KursblockungDynKurs#schienenFrei}
	 */
	private readonly schienenFreiSaveG : Array<KursblockungDynSchiene>;

	/**
	 * Die Anzahl an SuS in diesem Kurs.
	 */
	private schuelerAnzahl : number = 0;

	/**
	 * Die Anzahl an Dummy-SuS in diesem Kurs.
	 */
	private schuelerAnzahlDummy : number = 0;

	/**
	 * Die maximale Anzahl an erlaubten SuS in diesem Kurs.
	 */
	private schuelerAnzahlMaximal : number = 0;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler.
	 */
	private readonly logger : Logger;

	/**
	 * Eine Array, welches dynamisch definiert, ob ein Schüler für diesen Kurs verboten ist.
	 */
	private readonly schuelerVerboten : Array<number>;

	/**
	 * Eine Array, welches definiert, ob ein Schüler in diesem Kurs fixiert ist.
	 */
	private readonly schuelerFixiert : Array<boolean>;


	/**
	 * Der Kurs wählt eine zufällige Schienenlage und fügt sich selbst den entsprechenden Schienen hinzu.
	 *
	 * @param pRandom              Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pSchienenLage        Ein Array aller Schienen, in denen der Kurs gerade liegt.
	 * @param pSchienenLageFixiert Anzahl an Schienen in {@code pSchienenLage}, die fixiert ist.
	 * @param pSchienenFrei        Ein Array aller Schienen, in die der Kurs wechseln könnte.
	 * @param pKursID              Die ID des Kurses.
	 * @param pFachart             Die zu diesem Kurs zugehörige Fachart.
	 * @param pLogger              Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInternalID          Eine interne ID für schnellen Zugriff.
	 * @param pSchuelerAnzahl      Die Anzahl aller Schüler.
	 */
	constructor(pRandom : Random, pSchienenLage : Array<KursblockungDynSchiene>, pSchienenLageFixiert : number, pSchienenFrei : Array<KursblockungDynSchiene>, pKursID : number, pFachart : KursblockungDynFachart, pLogger : Logger, pInternalID : number, pSchuelerAnzahl : number) {
		super();
		this._random = pRandom;
		this.schienenLage = pSchienenLage;
		this.schienenLageFixiert = pSchienenLageFixiert;
		this.schienenFrei = pSchienenFrei;
		this.databaseID = pKursID;
		this.fachart = pFachart;
		this.schuelerAnzahl = 0;
		this.schuelerAnzahlDummy = 0;
		this.logger = pLogger;
		this.internalKursID = pInternalID;
		this.schuelerAnzahlMaximal = KursblockungDynKurs.MAX_SUS_DEFAULT;
		this.schuelerVerboten = Array(pSchuelerAnzahl).fill(0);
		this.schuelerFixiert = Array(pSchuelerAnzahl).fill(false);
		this.schienenLageSaveS = Array(this.schienenLage.length).fill(null);
		this.schienenLageSaveK = Array(this.schienenLage.length).fill(null);
		this.schienenLageSaveG = Array(this.schienenLage.length).fill(null);
		for (let i : number = 0; i < this.schienenLage.length; i++) {
			this.schienenLageSaveS[i] = this.schienenLage[i];
			this.schienenLageSaveK[i] = this.schienenLage[i];
			this.schienenLageSaveG[i] = this.schienenLage[i];
		}
		this.schienenFreiSaveS = Array(this.schienenFrei.length).fill(null);
		this.schienenFreiSaveK = Array(this.schienenFrei.length).fill(null);
		this.schienenFreiSaveG = Array(this.schienenFrei.length).fill(null);
		for (let i : number = 0; i < this.schienenFrei.length; i++) {
			this.schienenFreiSaveS[i] = this.schienenFrei[i];
			this.schienenFreiSaveK[i] = this.schienenFrei[i];
			this.schienenFreiSaveG[i] = this.schienenFrei[i];
		}
		for (const schiene of this.schienenLage)
			schiene.aktionKursHinzufuegen(this);
	}

	/**
	 * Gibt die String-Repräsentation des Kurses zurück.
	 *
	 * @return die String-Repräsentation des Kurses
	 */
	public toString() : string {
		return "Kurs (dbID=" + this.databaseID + ", intiD=" + this.internalKursID + ")";
	}

	/**
	 *Die Kurs-ID der GUI.
	 *
	 * @return Die Kurs-ID der GUI.
	 */
	gibDatenbankID() : number {
		return this.databaseID;
	}

	/**
	 *Liefert die zum Kurs zugehörige Fachart.
	 *
	 * @return Die zum Kurs zugehörige Fachart.
	 */
	gibFachart() : KursblockungDynFachart {
		return this.fachart;
	}

	/**
	 *Liefert die aktuelle Anzahl an Schülern in diesem Kurs.
	 *
	 * @return Die aktuelle Anzahl an Schülern in diesem Kurs.
	 */
	gibSchuelerAnzahl() : number {
		return this.schuelerAnzahl + this.schuelerAnzahlDummy;
	}

	/**
	 * Liefert {@code true} falls die Schienenlage des Kurses noch veränderbar ist. Wenn der Kurs komplett fixiert ist,
	 * oder so viele Schienen gesperrt sind, dass der Kurs keine Wahlmöglichkeit bezüglich seiner Schienen hat, dann ist
	 * das Resultat {@code false}.
	 *
	 * @return Liefert {@code true}, falls die Schienenlage des Kurses noch veränderbar ist.
	 */
	gibHatFreiheitsgrade() : boolean {
		return (this.schienenLageFixiert < this.schienenLage.length) && (this.schienenFrei.length > 0);
	}

	/**
	 * Liefert die aktuelle Schienenlage dieses Kurses.
	 *
	 * @return Ein Array, das angibt, in welchen Schienen der Kurs ist. Die Werte sind 0-indiziert.
	 */
	gibSchienenLage() : Array<number> {
		const length : number = this.schienenLage.length;
		const lage : Array<number> = Array(length).fill(0);
		for (let i : number = 0; i < length; i++)
			lage[i] = this.schienenLage[i].gibNr();
		return lage;
	}

	/**
	 *Liefert die Anzahl an Schienen, die dieser Kurs belegt.
	 *
	 * @return Die Anzahl an Schienen, die dieser Kurs belegt.
	 */
	gibSchienenAnzahl() : number {
		return this.schienenLage.length;
	}

	/**
	 *Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser.
	 *
	 * @return Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser.
	 */
	gibGewichtetesMatchingBewertung() : number {
		const anzahl : number = this.gibSchuelerAnzahl();
		return anzahl * anzahl;
	}

	/**
	 *Liefert TRUE, wenn die Schiene für den Kurs gesperrt wurde.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs gesperrt wurde.
	 */
	gibIstSchieneGesperrt(pSchiene : number) : boolean {
		for (const s of this.schienenLage)
			if (s.gibNr() === pSchiene)
				return false;
		for (const s of this.schienenFrei)
			if (s.gibNr() === pSchiene)
				return false;
		return true;
	}

	/**
	 *Liefert TRUE, wenn die Schiene für den Kurs fixiert wurde.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs fixiert wurde.
	 */
	gibIstSchieneFixiert(pSchiene : number) : boolean {
		for (let iLage : number = 0; iLage < this.schienenLageFixiert; iLage++)
			if (this.schienenLage[iLage].gibNr() === pSchiene)
				return true;
		return false;
	}

	/**
	 *Liefert TRUE, falls der Kurs gerade in Schiene pSchiene ist.
	 *
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, falls der Kurs gerade in Schiene pSchiene ist.
	 */
	gibIstInSchiene(pSchiene : number) : boolean {
		for (const schiene of this.schienenLage)
			if (schiene.gibNr() === pSchiene)
				return true;
		return false;
	}

	/**
	 *Liefert TRUE, falls sich der Kurs in einer Schiene aus [schieneVon, schieneBis] befindet.
	 *
	 * @param  schieneVon Der Anfang des Intervalls (inklusive).
	 * @param  schieneBis Das Ende des Intervalls (inklusive).
	 * @return            TRUE, falls sich der Kurs in einer Schiene aus [schieneVon, schieneBis] befindet.
	 */
	gibIstImSchienenIntervall(schieneVon : number, schieneBis : number) : boolean {
		for (const schiene of this.schienenLage)
			if ((schiene.gibNr() >= schieneVon) && (schiene.gibNr() <= schieneBis))
				return true;
		return false;
	}

	/**
	 *Liefert TRUE, falls dieser Kurs in Schiene c wandern darf.
	 *
	 * @param  pSchiene Die Schiene, die angefragt wurde.
	 * @return          TRUE, falls dieser Kurs in Schiene c wandern darf.
	 */
	gibIstSchieneFrei(pSchiene : number) : boolean {
		for (const schiene of this.schienenFrei)
			if (schiene.gibNr() === pSchiene)
				return true;
		return false;
	}

	/**
	 *Liefert die interne ID des Kurses.
	 *
	 * @return Die interne ID des Kurses.
	 */
	gibInternalID() : number {
		return this.internalKursID;
	}

	/**
	 * Liefert TRUE, falls der Schueler theoretisch in den Kurs könnte.
	 *
	 * @param s  Das  {@link KursblockungDynSchueler}-Objekt.
	 *
	 * @return TRUE, falls der Schueler theoretisch in den Kurs könnte.
	 */
	gibIstErlaubtFuerSchueler(s : KursblockungDynSchueler) : boolean {
		if (s.kursGesperrt[this.internalKursID])
			return false;
		if (this.schuelerVerboten[s.internalSchuelerID] > 0)
			return false;
		if (this.schuelerFixiert[s.internalSchuelerID])
			return true;
		return ((this.schuelerAnzahlMaximal - this.schuelerAnzahl - this.schuelerAnzahlDummy) > 0);
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand S,
	 * um diese bei Bedarf mit der Methode {@link #aktionZustandLadenS} zu laden.
	 */
	aktionZustandSpeichernS() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveS, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveS, 0, this.schienenFrei.length);
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand K,
	 * um diese bei Bedarf mit der Methode {@link #aktionZustandLadenK} zu laden.
	 */
	aktionZustandSpeichernK() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveK, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveK, 0, this.schienenFrei.length);
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand G,
	 * um diese bei Bedarf mit der Methode {@link #aktionZustandLadenG} zu laden.
	 */
	aktionZustandSpeichernG() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveG, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveG, 0, this.schienenFrei.length);
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernS} gespeicherte Lage der Schienen.
	 */
	aktionZustandLadenS() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveS, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveS, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernK} gespeicherte Lage der Schienen.
	 */
	aktionZustandLadenK() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveK, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveK, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 * Lädt die Schienenlage des anderen {@link KursblockungDynKurs}-Objekts.
	 *
	 * @param b            Das andere {@link KursblockungDynKurs}-Objekt.
	 * @param schienenArr  Das Array aller {@link KursblockungDynSchiene}-Objekte.
	 */
	aktionZustandLadenVon(b : KursblockungDynKurs, schienenArr : Array<KursblockungDynSchiene>) : void {
		this.aktionSchienenLageEntfernen();
		for (let i : number = 0; i < this.schienenLage.length; i++)
			this.schienenLage[i] = schienenArr[b.schienenLage[i].gibNr()];
		for (let i : number = 0; i < this.schienenFrei.length; i++)
			this.schienenFrei[i] = schienenArr[b.schienenFrei[i].gibNr()];
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernG} gespeicherte Lage der Schienen.
	 */
	aktionZustandLadenG() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveG, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveG, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Verteilt den Kurs auf die Schienen zufällig.
	 */
	aktionZufaelligVerteilen() : void {
		if (!this.gibHatFreiheitsgrade())
			return;
		if (this.schuelerAnzahl > 0) {
			this.logger.log(LogLevel.ERROR, "Kurs.aktionZufaelligVerteilen: schuelerAnz > 0 (Ein Kurs mit SuS darf nicht verteilt werden)");
			return;
		}
		for (let i1 : number = this.schienenLageFixiert; i1 < this.schienenLage.length; i1++) {
			const i2 : number = this._random.nextInt(this.schienenFrei.length);
			const schiene1 : KursblockungDynSchiene = this.schienenLage[i1];
			const schiene2 : KursblockungDynSchiene = this.schienenFrei[i2];
			schiene1.aktionKursEntfernen(this);
			schiene2.aktionKursHinzufuegen(this);
			this.schienenLage[i1] = schiene2;
			this.schienenFrei[i2] = schiene1;
		}
	}

	/**
	 *Setzt die Lage des Kurses auf die in der Liste übergebenen Schienen.
	 *
	 * @param pSchienenWahl Die Schienen (0-indiziert), in denen der Kurs liegen soll.
	 */
	aktionVerteileAufSchienen(pSchienenWahl : LinkedCollection<number>) : void {
		for (let iLage : number = this.schienenLageFixiert; iLage < this.schienenLage.length; iLage++) {
			const schieneL : KursblockungDynSchiene = this.schienenLage[iLage];
			if (pSchienenWahl.contains(schieneL.gibNr()))
				continue;
			for (let iFrei : number = 0; iFrei < this.schienenFrei.length; iFrei++) {
				const schieneF : KursblockungDynSchiene = this.schienenFrei[iFrei];
				if (pSchienenWahl.contains(schieneF.gibNr())) {
					schieneL.aktionKursEntfernen(this);
					schieneF.aktionKursHinzufuegen(this);
					this.schienenFrei[iFrei] = schieneL;
					this.schienenLage[iLage] = schieneF;
					break;
				}
			}
		}
	}

	/**
	 *Setzt die Lage des Kurses auf die übergebene Schiene.
	 *
	 * @param pSchiene Die Schiene (0-indiziert), in der der Kurs liegen soll.
	 */
	aktionSetzeInSchiene(pSchiene : number) : void {
		for (let iLage : number = this.schienenLageFixiert; iLage < this.schienenLage.length; iLage++) {
			const schieneL : KursblockungDynSchiene = this.schienenLage[iLage];
			if (schieneL.gibNr() === pSchiene)
				return;
			for (let iFrei : number = 0; iFrei < this.schienenFrei.length; iFrei++) {
				const schieneF : KursblockungDynSchiene = this.schienenFrei[iFrei];
				if (pSchiene === schieneF.gibNr()) {
					schieneL.aktionKursEntfernen(this);
					schieneF.aktionKursHinzufuegen(this);
					this.schienenFrei[iFrei] = schieneL;
					this.schienenLage[iLage] = schieneF;
					return;
				}
			}
		}
	}

	/**
	 * Fügt einen Schüler diesem Kurs hinzu.
	 *
	 * @param schuelerNr  Die interne Nummer des Schülers.
	 */
	aktionSchuelerHinzufuegen(schuelerNr : number) : void {
		this.fachart.aktionKursdifferenzEntfernen();
		this.schuelerAnzahl++;
		if (this.schuelerFixiert[schuelerNr])
			this.schuelerAnzahlMaximal++;
		this.fachart.aktionSchuelerWurdeHinzugefuegt();
		this.fachart.aktionKursdifferenzHinzufuegen();
		for (const verbotenMitNr of this.fachart.gibVonSchuelerVerbotenMit(schuelerNr))
			this.schuelerVerboten[verbotenMitNr]++;
		for (const zusammenMitNr of this.fachart.gibVonSchuelerZusammenMit(schuelerNr))
			for (const kursDerFachart of this.fachart.gibKurse())
				if (kursDerFachart as unknown !== this as unknown)
					kursDerFachart.schuelerVerboten[zusammenMitNr]++;
	}

	/**
	 * Entfernt einen Schüler aus diesem Kurs.
	 *
	 * @param schuelerNr  Die interne Nummer des Schülers.
	 */
	aktionSchuelerEntfernen(schuelerNr : number) : void {
		this.fachart.aktionKursdifferenzEntfernen();
		this.schuelerAnzahl--;
		if (this.schuelerFixiert[schuelerNr])
			this.schuelerAnzahlMaximal--;
		this.fachart.aktionSchuelerWurdeEntfernt();
		this.fachart.aktionKursdifferenzHinzufuegen();
		for (const verbotenMitNr of this.fachart.gibVonSchuelerVerbotenMit(schuelerNr))
			this.schuelerVerboten[verbotenMitNr]--;
		for (const zusammenMitNr of this.fachart.gibVonSchuelerZusammenMit(schuelerNr))
			for (const kursDerFachart of this.fachart.gibKurse())
				if (kursDerFachart as unknown !== this as unknown)
					kursDerFachart.schuelerVerboten[zusammenMitNr]--;
	}

	/**
	 * Setzt die Höchstgrenze für die SuS in diesem Kurs.
	 *
	 * @param maxSuS  Die maximale Anzahl an SuS für diesen Kurs.
	 */
	regel_15_setzeMaxSuS(maxSuS : number) : void {
		this.schuelerAnzahlMaximal = maxSuS;
		for (const istFixiert of this.schuelerFixiert)
			if (istFixiert)
				this.schuelerAnzahlMaximal--;
	}

	/**
	 * Merkt sich, dass ein Schüler in diesem Kurs fixiert ist.
	 *
	 * @param internalSchuelerID  Die interne ID des Schülers.
	 */
	regel_04_setzeSchuelerFixierung(internalSchuelerID : number) : void {
		this.schuelerFixiert[internalSchuelerID] = true;
	}

	/**
	 * Fügt einen Dummy-Schüler diesem Kurs hinzu.
	 */
	aktionSchuelerDummyHinzufuegen() : void {
		this.fachart.aktionKursdifferenzEntfernen();
		this.schuelerAnzahlDummy++;
		this.fachart.aktionSchuelerWurdeHinzugefuegt();
		this.fachart.aktionKursdifferenzHinzufuegen();
	}

	/**
	 * Debug Ausgabe. Nur für Testzwecke.
	 *
	 * @param schuelerArr Nötig, um den Kursen SuS zuzuordnen.
	 */
	debug(schuelerArr : Array<KursblockungDynSchueler>) : void {
		this.logger.modifyIndent(+4);
		this.logger.logLn(this.toString() + " --> " + this.schuelerAnzahl + " SuS.");
		for (const s of schuelerArr) {
			const kurse : Array<KursblockungDynKurs | null> = s.gibKurswahlen();
			for (const kurs of kurse)
				if (kurs as unknown === this as unknown)
					this.logger.logLn("        " + s.gibDatenbankID());
		}
		this.logger.modifyIndent(-4);
	}

	private aktionSchienenLageHinzufuegen() : void {
		for (const schiene of this.schienenLage)
			schiene.aktionKursHinzufuegen(this);
	}

	private aktionSchienenLageEntfernen() : void {
		for (const schiene of this.schienenLage)
			schiene.aktionKursEntfernen(this);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.KursblockungDynKurs';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungDynKurs'].includes(name);
	}

	public static class = new Class<KursblockungDynKurs>('de.svws_nrw.core.kursblockung.KursblockungDynKurs');

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungDynKurs(obj : unknown) : KursblockungDynKurs {
	return obj as KursblockungDynKurs;
}
