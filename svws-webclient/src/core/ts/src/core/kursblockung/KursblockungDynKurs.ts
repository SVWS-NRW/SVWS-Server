import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { KursblockungDynFachart, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { KursblockungDynSchiene, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchiene } from '../../core/kursblockung/KursblockungDynSchiene';
import { KursblockungDynSchueler, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';

export class KursblockungDynKurs extends JavaObject {

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
	private readonly internalID : number;

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
	private schuelerAnz : number = 0;

	/**
	 * Logger für Benutzerhinweise, Warnungen und Fehler. 
	 */
	private readonly logger : Logger;


	/**
	 *Der Kurs wählt eine zufällige Schienenlage und fügt sich selbst den entsprechenden Schienen hinzu.
	 * 
	 * @param pRandom              Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pSchienenLage        Ein Array aller Schienen, in denen der Kurs gerade liegt.
	 * @param pSchienenLageFixiert Anzahl an Schienen in {@code pSchienenLage}, die fixiert ist.
	 * @param pSchienenFrei        Ein Array aller Schienen, in die der Kurs wechseln könnte.
	 * @param pKursID              Die ID des Kurses.
	 * @param pFachart             Die zu diesem Kurs zugehörige Fachart.
	 * @param pLogger              Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInternalID          Eine interne ID für schnellen Zugriff. 
	 */
	public constructor(pRandom : Random, pSchienenLage : Array<KursblockungDynSchiene>, pSchienenLageFixiert : number, pSchienenFrei : Array<KursblockungDynSchiene>, pKursID : number, pFachart : KursblockungDynFachart, pLogger : Logger, pInternalID : number) {
		super();
		this._random = pRandom;
		this.schienenLage = pSchienenLage;
		this.schienenLageFixiert = pSchienenLageFixiert;
		this.schienenFrei = pSchienenFrei;
		this.databaseID = pKursID;
		this.fachart = pFachart;
		this.schuelerAnz = 0;
		this.logger = pLogger;
		this.internalID = pInternalID;
		this.schienenLageSaveS = Array(this.schienenLage.length).fill(null);
		this.schienenLageSaveK = Array(this.schienenLage.length).fill(null);
		this.schienenLageSaveG = Array(this.schienenLage.length).fill(null);
		for (let i : number = 0; i < this.schienenLage.length; i++){
			this.schienenLageSaveS[i] = this.schienenLage[i];
			this.schienenLageSaveK[i] = this.schienenLage[i];
			this.schienenLageSaveG[i] = this.schienenLage[i];
		}
		this.schienenFreiSaveS = Array(this.schienenFrei.length).fill(null);
		this.schienenFreiSaveK = Array(this.schienenFrei.length).fill(null);
		this.schienenFreiSaveG = Array(this.schienenFrei.length).fill(null);
		for (let i : number = 0; i < this.schienenFrei.length; i++){
			this.schienenFreiSaveS[i] = this.schienenFrei[i];
			this.schienenFreiSaveK[i] = this.schienenFrei[i];
			this.schienenFreiSaveG[i] = this.schienenFrei[i];
		}
		for (let i : number = 0; i < this.schienenLage.length; i++){
			this.schienenLage[i].aktionKursHinzufuegen(this);
		}
	}

	public toString() : string {
		return "Kurs (dbID=" + this.databaseID + ", intiD=" + this.internalID + ")";
	}

	/**
	 *Die Kurs-ID der GUI.
	 * 
	 * @return Die Kurs-ID der GUI. 
	 */
	public gibDatenbankID() : number {
		return this.databaseID;
	}

	/**
	 *Liefert die zum Kurs zugehörige Fachart.
	 * 
	 * @return Die zum Kurs zugehörige Fachart. 
	 */
	public gibFachart() : KursblockungDynFachart {
		return this.fachart;
	}

	/**
	 *Liefert die aktuelle Anzahl an Schülern in diesem Kurs.
	 * 
	 * @return Die aktuelle Anzahl an Schülern in diesem Kurs. 
	 */
	public gibSchuelerAnzahl() : number {
		return this.schuelerAnz;
	}

	/**
	 *Liefert {@code true} falls die Schienenlage des Kurses noch veränderbar ist. Wenn der Kurs komplett fixiert ist,
	 * oder so viele Schienen gesperrt sind, dass der Kurs keine Wahlmöglichkeit bezüglich seiner Schienen hat, dann ist
	 * das Resultat {@code false}
	 * 
	 * @return Liefert {@code true}, falls die Schienenlage des Kurses noch veränderbar ist. 
	 */
	public gibHatFreiheitsgrade() : boolean {
		return (this.schienenLageFixiert < this.schienenLage.length) && (this.schienenFrei.length > 0);
	}

	/**
	 *Liefert die aktuelle Schienenlage dieses Kurses.
	 * 
	 * @return Ein Array, das angibt, in welchen Schienen der Kurs ist. Die Werte sind 0-indiziert. 
	 */
	public gibSchienenLage() : Array<number> {
		let length : number = this.schienenLage.length;
		let lage : Array<number> = Array(length).fill(0);
		for (let i : number = 0; i < length; i++){
			lage[i] = this.schienenLage[i].gibNr();
		}
		return lage;
	}

	/**
	 *Liefert die Anzahl an Schienen, die dieser Kurs belegt.
	 * 
	 * @return Die Anzahl an Schienen, die dieser Kurs belegt. 
	 */
	public gibSchienenAnzahl() : number {
		return this.schienenLage.length;
	}

	/**
	 *Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser.
	 * 
	 * @return Beurteilt das Hinzufügen eines Schülers zu diesem Kurs. Je kleiner der Wert desto besser. 
	 */
	public gibGewichtetesMatchingBewertung() : number {
		return this.schuelerAnz * this.schuelerAnz;
	}

	/**
	 *Liefert TRUE, wenn die Schiene für den Kurs gesperrt wurde.
	 * 
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs gesperrt wurde. 
	 */
	gibIstSchieneGesperrt(pSchiene : number) : boolean {
		for (let s of this.schienenLage) {
			if (s.gibNr() === pSchiene) {
				return false;
			}
		}
		for (let s of this.schienenFrei) {
			if (s.gibNr() === pSchiene) {
				return false;
			}
		}
		return true;
	}

	/**
	 *Liefert TRUE, wenn die Schiene für den Kurs fixiert wurde.
	 * 
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, wenn die Schiene für den Kurs fixiert wurde. 
	 */
	gibIstSchieneFixiert(pSchiene : number) : boolean {
		for (let iLage : number = 0; iLage < this.schienenLageFixiert; iLage++){
			if (this.schienenLage[iLage].gibNr() === pSchiene) {
				return true;
			}
		}
		return false;
	}

	/**
	 *Liefert TRUE, falls der Kurs gerade in Schiene pSchiene ist.
	 * 
	 * @param  pSchiene Die Schiene nach der gefragt wurde.
	 * @return          TRUE, falls der Kurs gerade in Schiene pSchiene ist. 
	 */
	gibIstInSchiene(pSchiene : number) : boolean {
		for (let i : number = 0; i < this.schienenLage.length; i++)
			if (this.schienenLage[i].gibNr() === pSchiene) 
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
		for (let i : number = 0; i < this.schienenLage.length; i++)
			if ((this.schienenLage[i].gibNr() >= schieneVon) && (this.schienenLage[i].gibNr() <= schieneBis)) 
				return true;
		return false;
	}

	/**
	 *Liefert TRUE, falls dieser Kurs in Schiene c wandern darf.
	 * 
	 * @param  pSchiene Die Schiene, die angefragt wurde.
	 * @return          TRUE, falls dieser Kurs in Schiene c wandern darf. 
	 */
	public gibIstSchieneFrei(pSchiene : number) : boolean {
		for (let i : number = 0; i < this.schienenFrei.length; i++){
			if (this.schienenFrei[i].gibNr() === pSchiene) {
				return true;
			}
		}
		return false;
	}

	/**
	 *Liefert die interne ID des Kurses.
	 * 
	 * @return Die interne ID des Kurses. 
	 */
	gibInternalID() : number {
		return this.internalID;
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand S, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenS} zu laden. 
	 */
	public aktionZustandSpeichernS() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveS, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveS, 0, this.schienenFrei.length);
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand K, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenK} zu laden. 
	 */
	public aktionZustandSpeichernK() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveK, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveK, 0, this.schienenFrei.length);
	}

	/**
	 *Speichert die aktuelle Lage der Schienen im Zustand G, um diese bei Bedarf mit der Methode
	 * {@link #aktionZustandLadenG} zu laden. 
	 */
	public aktionZustandSpeichernG() : void {
		System.arraycopy(this.schienenLage, 0, this.schienenLageSaveG, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFrei, 0, this.schienenFreiSaveG, 0, this.schienenFrei.length);
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernS} gespeicherte Lage der Schienen. 
	 */
	public aktionZustandLadenS() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveS, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveS, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernK} gespeicherte Lage der Schienen. 
	 */
	public aktionZustandLadenK() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveK, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveK, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Lädt die zuvor mit der Methode {@link #aktionZustandSpeichernG} gespeicherte Lage der Schienen. 
	 */
	public aktionZustandLadenG() : void {
		this.aktionSchienenLageEntfernen();
		System.arraycopy(this.schienenLageSaveG, 0, this.schienenLage, 0, this.schienenLage.length);
		System.arraycopy(this.schienenFreiSaveG, 0, this.schienenFrei, 0, this.schienenFrei.length);
		this.aktionSchienenLageHinzufuegen();
	}

	/**
	 *Verteilt den Kurs auf die Schienen zufällig. 
	 */
	public aktionZufaelligVerteilen() : void {
		if (!this.gibHatFreiheitsgrade()) {
			return;
		}
		if (this.schuelerAnz > 0) {
			this.logger.log(LogLevel.ERROR, "Kurs.aktionZufaelligVerteilen: schuelerAnz > 0 (Ein Kurs mit SuS darf nicht verteilt werden)");
			return;
		}
		for (let i1 : number = this.schienenLageFixiert; i1 < this.schienenLage.length; i1++){
			let i2 : number = this._random.nextInt(this.schienenFrei.length);
			let schiene1 : KursblockungDynSchiene = this.schienenLage[i1];
			let schiene2 : KursblockungDynSchiene = this.schienenFrei[i2];
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
	public aktionVerteileAufSchienen(pSchienenWahl : LinkedCollection<number>) : void {
		for (let iLage : number = this.schienenLageFixiert; iLage < this.schienenLage.length; iLage++){
			let schieneL : KursblockungDynSchiene = this.schienenLage[iLage];
			if (pSchienenWahl.contains(schieneL.gibNr())) {
				continue;
			}
			for (let iFrei : number = 0; iFrei < this.schienenFrei.length; iFrei++){
				let schieneF : KursblockungDynSchiene = this.schienenFrei[iFrei];
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
		for (let iLage : number = this.schienenLageFixiert; iLage < this.schienenLage.length; iLage++){
			let schieneL : KursblockungDynSchiene = this.schienenLage[iLage];
			if (schieneL.gibNr() === pSchiene) {
				return;
			}
			for (let iFrei : number = 0; iFrei < this.schienenFrei.length; iFrei++){
				let schieneF : KursblockungDynSchiene = this.schienenFrei[iFrei];
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
	 *Entfernt einen Schüler aus diesem Kurs. 
	 */
	public aktionSchuelerEntfernen() : void {
		this.fachart.aktionKursdifferenzEntfernen();
		this.schuelerAnz--;
		this.fachart.aktionSchuelerWurdeEntfernt();
		this.fachart.aktionKursdifferenzHinzufuegen();
	}

	/**
	 *Fügt einen Schüler diesem Kurs hinzu. 
	 */
	public aktionSchuelerHinzufügen() : void {
		this.fachart.aktionKursdifferenzEntfernen();
		this.schuelerAnz++;
		this.fachart.aktionSchuelerWurdeHinzugefuegt();
		this.fachart.aktionKursdifferenzHinzufuegen();
	}

	private aktionSchienenLageHinzufuegen() : void {
		for (let i : number = 0; i < this.schienenLage.length; i++){
			this.schienenLage[i].aktionKursHinzufuegen(this);
		}
	}

	private aktionSchienenLageEntfernen() : void {
		for (let i : number = 0; i < this.schienenLage.length; i++){
			this.schienenLage[i].aktionKursEntfernen(this);
		}
	}

	/**
	 *Debug Ausgabe. Nur für Testzwecke.
	 * 
	 * @param schuelerArr Nötig, um den Kursen SuS zuzuordnen. 
	 */
	debug(schuelerArr : Array<KursblockungDynSchueler>) : void {
		console.log(JSON.stringify(this.toString()! + " --> " + this.schuelerAnz + " SuS."));
		for (let s of schuelerArr) {
			let kurse : Array<KursblockungDynKurs | null> = s.gibKurswahlen();
			for (let kurs of kurse) {
				if (kurs as unknown === this as unknown) {
					console.log(JSON.stringify("        " + s.gibDatenbankID()));
				}
			}
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungDynKurs'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs(obj : unknown) : KursblockungDynKurs {
	return obj as KursblockungDynKurs;
}
