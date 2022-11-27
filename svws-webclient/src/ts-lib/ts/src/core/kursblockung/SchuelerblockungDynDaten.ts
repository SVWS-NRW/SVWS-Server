import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { SchuelerblockungOutput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputKurs } from '../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungOutputFachwahlZuKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutputFachwahlZuKurs } from '../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { GostFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostFachwahl } from '../../core/data/gost/GostFachwahl';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { SchuelerblockungException, cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungException } from '../../core/kursblockung/SchuelerblockungException';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { SchuelerblockungInput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungMatrix, cast_de_nrw_schule_svws_core_kursblockung_KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { Arrays, cast_java_util_Arrays } from '../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';
import { HashSet, cast_java_util_HashSet } from '../../java/util/HashSet';

export class SchuelerblockungDynDaten extends JavaObject {

	private static readonly UNENDLICH : number = 1000000;

	private readonly _random : Random;

	private readonly _logger : Logger;

	private readonly nFachwahlen : number;

	private readonly nSchienen : number;

	private readonly _fachwahlZuKurse : Vector<Vector<SchuelerblockungInputKurs>>;

	private readonly _fachwahlZuHatMultikurse : Array<boolean>;

	private readonly _fachwahlZuFachID : Array<number>;

	private readonly _fachwahlZuKursartID : Array<number>;

	private readonly _aktuellMatrix : KursblockungMatrix;

	private readonly _aktuellGesperrteSchiene : Array<boolean>;

	private readonly _aktuellFachwahlZuKurs : Array<number>;

	private readonly _aktuellFachwahlZuKursBest : Array<number>;

	private _aktuellNichtwahlen : number = 0;

	private _aktuellNichtwahlenBest : number = 0;

	private _aktuellBewertung : number = 0;

	private _aktuellBewertungBest : number = 0;


	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(pRandom : Random, pLogger : Logger, pInput : SchuelerblockungInput) {
		super();
		this._logger = pLogger;
		this._random = pRandom;
		this.aktionPruefeEingabedaten(pInput);
		this.nFachwahlen = pInput.fachwahlen.size();
		this.nSchienen = pInput.schienen;
		this._fachwahlZuKurse = new Vector();
		this._fachwahlZuHatMultikurse = Array(this.nFachwahlen).fill(false);
		this._fachwahlZuFachID = Array(this.nFachwahlen).fill(0);
		this._fachwahlZuKursartID = Array(this.nFachwahlen).fill(0);
		this.aktionInitialisiereDatenstrukturen(pInput);
		this._aktuellMatrix = new KursblockungMatrix(pRandom, this.nFachwahlen, this.nSchienen);
		this._aktuellGesperrteSchiene = Array(this.nSchienen).fill(false);
		this._aktuellFachwahlZuKurs = Array(this.nFachwahlen).fill(0);
		this._aktuellFachwahlZuKursBest = Array(this.nFachwahlen).fill(0);
		this._aktuellBewertung = 0;
		this._aktuellBewertungBest = 0;
	}

	/**
	 * Überprüft die Konsistenz und referentielle Integrität der Eingabedaten.
	 * 
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	aktionPruefeEingabedaten(pInput : SchuelerblockungInput) : void {
		if (pInput === null) 
			throw this.fehler("pInput == NULL")
		if (pInput.fachwahlen === null) 
			throw this.fehler("pInput.fachwahlen == NULL")
		if (pInput.kurse === null) 
			throw this.fehler("pInput.kurse == NULL")
		let nFachwahlen : number = pInput.fachwahlen.size();
		if (nFachwahlen < 1) 
			throw this.fehler("Der Schüler hat zu wenig Fachwahlen! --> " + nFachwahlen)
		let nSchienen : number = pInput.schienen;
		if (nSchienen < 1) 
			throw this.fehler("Die Schienenanzahl ist zu gering! --> " + nSchienen)
		let nKurse : number = pInput.kurse.size();
		if (nKurse < 1) 
			throw this.fehler("Die Kursanzahl ist zu gering! --> " + nKurse)
		let setKursID : HashSet<Number | null> | null = new HashSet();
		for (let kurs of pInput.kurse) {
			if (kurs.id < 0) 
				throw this.fehler("kurs.id ist zu gering! --> " + kurs.id)
			if (setKursID.add(kurs.id) === false) 
				throw this.fehler("kurs.id existiert doppelt! --> " + kurs.id)
			if (kurs.fach < 0) 
				throw this.fehler("kurs.fach ist zu gering! --> " + kurs.fach)
			if (kurs.kursart < 0) 
				throw this.fehler("kurs.kursart ist zu gering! --> " + kurs.kursart)
			if (kurs.anzahlSuS < 0) 
				throw this.fehler("kurs.anzahlSuS ist zu gering! --> " + kurs.anzahlSuS)
			if (kurs.schienen.length < 1) 
				throw this.fehler("kurs.schienen.length ist zu gering! --> " + kurs.schienen.length)
			if (kurs.schienen === null) 
				throw this.fehler("kurs.schienen ist undefiniert! --> " + kurs.schienen)
			if (kurs.schienen.length <= 0) 
				throw this.fehler("kurs.schienen.length ist zu klein! --> " + kurs.schienen.length)
			if (kurs.schienen.length > nSchienen) 
				throw this.fehler("kurs.schienen.length ist zu groß! --> " + kurs.schienen.length)
			for (let schiene1 of kurs.schienen) {
				if (schiene1 < 1) 
					throw this.fehler("Kurs " + kurs.id + " ist in zu kleiner Schiene! --> " + schiene1)
				if (schiene1 > nSchienen) 
					throw this.fehler("Kurs " + kurs.id + " ist in zu großer Schiene! --> " + schiene1)
			}
			if (kurs.istFixiert && kurs.istGesperrt) 
				throw this.fehler("kurs.istFixiert && kurs.istGesperrt ist unmöglich! --> " + kurs.id)
		}
		for (let fachwahl of pInput.fachwahlen) {
			if (fachwahl.schuelerID < 0) 
				throw this.fehler("fachwahl.schuelerID ist zu gering! --> " + fachwahl.schuelerID)
			if (fachwahl.fachID < 0) 
				throw this.fehler("fachwahl.fachID ist zu gering! --> " + fachwahl.fachID)
			if (fachwahl.kursartID < 0) 
				throw this.fehler("fachwahl.kursartID ist zu gering! --> " + fachwahl.kursartID)
		}
		for (let iFachwahl : number = 0; iFachwahl < nFachwahlen; iFachwahl++){
			let fachwahl : GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			let representation : String | null = fachwahl.fachID + ";" + fachwahl.kursartID;
			let kursWurdeFixiert : boolean = false;
			for (let kurs of pInput.kurse) 
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart)) {
					if (kurs.istGesperrt) 
						continue;
					if (kurs.istFixiert) {
						if (kursWurdeFixiert) 
							throw this.fehler("Die Fachart " + representation.valueOf() + " hat mehr als eine Fixierung!")
						kursWurdeFixiert = true;
					}
				}
		}
		for (let kurs of pInput.kurse) {
			let gefunden : number = 0;
			for (let r : number = 0; r < nFachwahlen; r++){
				let fachwahl : GostFachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart)) 
					gefunden++;
			}
			if (gefunden === 0) 
				throw this.fehler("Der Kurs " + kurs.id + " konnte keiner Fachwahl zugeordnet werden!")
		}
	}

	/**
	 * Erzeugt einen Fehler und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung.
	 */
	private fehler(fehlermeldung : String) : SchuelerblockungException | null {
		this._logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new SchuelerblockungException(fehlermeldung);
	}

	/**
	 * Initialisiert {@link #_fachwahlen}, {@link #_fachwahlZuKurse} und {@link #_fachwahlZuHatMultikurse}.
	 * 
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private aktionInitialisiereDatenstrukturen(pInput : SchuelerblockungInput) : void {
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++){
			let fachwahl : GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			this._fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			this._fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;
			let kurse : Vector<SchuelerblockungInputKurs> | null = new Vector();
			let hatFixiertenKurs : boolean = false;
			for (let kurs of pInput.kurse) 
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
					if (kurs.istFixiert) {
						hatFixiertenKurs = true;
						kurse.clear();
					}
					kurse.add(kurs);
				}
			this._fachwahlZuKurse.add(kurse);
			let max : number = 1;
			for (let kurs of kurse) 
				max = Math.max(max, kurs.schienen.length);
			this._fachwahlZuHatMultikurse[iFachwahl] = max >= 2;
		}
	}

	/**
	 * Berechnet das optimale Matching. Zuerst werden die Multikurse verteilt, indem alle Kombination
	 * durchgegangen werden. Dann wird pro Verteilung der Multikurse die anderen Kurse mit einem bipartiten
	 * gewichteten Matching-Algorithmus verteilt. Das beste Ergebnis wird zurückgeliefert. Gibt es mehrere beste
	 * Ergebnisse wird ein zufälliges gewählt.
	 * 
	 * @return Eine optimale Zuordnung des Schülers auf seine gewählten Kurse.
	 */
	gibBestesMatching() : SchuelerblockungOutput {
		this._aktuellNichtwahlen = 0;
		this._aktuellBewertung = 0;
		this._aktuellNichtwahlenBest = SchuelerblockungDynDaten.UNENDLICH;
		this._aktuellBewertungBest = SchuelerblockungDynDaten.UNENDLICH;
		Arrays.fill(this._aktuellFachwahlZuKurs, -1);
		Arrays.fill(this._aktuellFachwahlZuKursBest, -1);
		Arrays.fill(this._aktuellGesperrteSchiene, false);
		this.aktionVerteileMultikurseRekursiv(0);
		let out : SchuelerblockungOutput = new SchuelerblockungOutput();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++){
			let wahl : SchuelerblockungOutputFachwahlZuKurs = new SchuelerblockungOutputFachwahlZuKurs();
			wahl.fachID = this._fachwahlZuFachID[iFachwahl];
			wahl.kursartID = this._fachwahlZuKursartID[iFachwahl];
			wahl.kursID = this._aktuellFachwahlZuKursBest[iFachwahl];
			out.fachwahlenZuKurs.add(wahl);
		}
		return out;
	}

	private aktionVerteileMultikurseRekursiv(iFachwahl : number) : void {
		if (iFachwahl >= this.nFachwahlen) {
			this.aktionVerteileMitMatching();
			return;
		}
		if (this._fachwahlZuHatMultikurse[iFachwahl] === false) {
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}
		let schienenAnzahl : number = 2;
		for (let kurs of this._fachwahlZuKurse.get(iFachwahl)) {
			schienenAnzahl = Math.max(schienenAnzahl, kurs.schienen.length);
			if (this.aktionBelegeKurs(iFachwahl, kurs) === true) {
				this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
				if (this.aktionBelegeKursUndo(iFachwahl, kurs) === false) 
					throw this.fehler("Der Kurs " + kurs.id + " konnte nicht entfernt werden!")
			}
		}
		this._aktuellNichtwahlen += schienenAnzahl;
		if (this._aktuellNichtwahlen <= this._aktuellNichtwahlenBest) 
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		this._aktuellNichtwahlen -= schienenAnzahl;
	}

	private aktionVerteileMitMatching() : void {
		let data : Array<Array<number>> = this._aktuellMatrix.getMatrix();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			for (let iSchiene : number = 0; iSchiene < this.nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = SchuelerblockungDynDaten.UNENDLICH;
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (this._fachwahlZuHatMultikurse[iFachwahl] === false) 
				for (let schiene : number = 0; schiene < this.nSchienen; schiene++)
					if (!this._aktuellGesperrteSchiene[schiene]) {
						let kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs !== null) 
							data[iFachwahl][schiene] = kurs.anzahlSuS * kurs.anzahlSuS;
					}
		let r2c : Array<number> = this._aktuellMatrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (this._fachwahlZuHatMultikurse[iFachwahl] === false) {
				let schiene : number = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
					this._aktuellNichtwahlen++;
					continue;
				}
				let kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs === null) 
					throw this.fehler("Der Fachart " + iFachwahl + " wurde ein NULL-Kurs zugeordnet!")
				if (this.aktionBelegeKurs(iFachwahl, kurs) === false) 
					throw this.fehler("Der Kurs " + kurs.id + " konnte nicht belegt werden!")
			}
		if ((this._aktuellNichtwahlen < this._aktuellNichtwahlenBest) || ((this._aktuellNichtwahlen === this._aktuellNichtwahlenBest) && (this._aktuellBewertung < this._aktuellBewertungBest))) {
			this._aktuellNichtwahlenBest = this._aktuellNichtwahlen;
			this._aktuellBewertungBest = this._aktuellBewertung;
			for (let i : number = 0; i < this.nFachwahlen; i++)
				this._aktuellFachwahlZuKursBest[i] = this._aktuellFachwahlZuKurs[i];
		}
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (this._fachwahlZuHatMultikurse[iFachwahl] === false) {
				let schiene : number = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
					this._aktuellNichtwahlen--;
					continue;
				}
				let kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs === null) 
					throw this.fehler("Der Fachart " + iFachwahl + " wurde ein NULL-Kurs zugeordnet!")
				if (this.aktionBelegeKursUndo(iFachwahl, kurs) === false) 
					throw this.fehler("Der Kurs " + kurs.id + " konnte nicht entfernt werden!")
			}
	}

	private static gibKleinstenKursInSchiene(pKurse : Vector<SchuelerblockungInputKurs>, pSchiene : number) : SchuelerblockungInputKurs | null {
		let maxSuS : number = Number.MAX_VALUE;
		let best : SchuelerblockungInputKurs | null = null;
		for (let kurs of pKurse) 
			if (kurs.schienen[0] - 1 === pSchiene) 
				if (kurs.anzahlSuS < maxSuS) 
					best = kurs;
		return best;
	}

	private aktionBelegeKurs(iFachwahl : number, kurs : SchuelerblockungInputKurs) : boolean {
		for (let schiene1 of kurs.schienen) 
			if (this._aktuellGesperrteSchiene[schiene1 - 1]) 
				return false;
		this._aktuellFachwahlZuKurs[iFachwahl] = kurs.id;
		for (let schiene1 of kurs.schienen) 
			this._aktuellGesperrteSchiene[schiene1 - 1] = true;
		this._aktuellBewertung += kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private aktionBelegeKursUndo(iFachwahl : number, kurs : SchuelerblockungInputKurs) : boolean {
		if (this._aktuellFachwahlZuKurs[iFachwahl] < 0) 
			return false;
		for (let schiene1 of kurs.schienen) 
			if (this._aktuellGesperrteSchiene[schiene1 - 1] === false) 
				return false;
		this._aktuellFachwahlZuKurs[iFachwahl] = -1;
		for (let schiene1 of kurs.schienen) 
			this._aktuellGesperrteSchiene[schiene1 - 1] = false;
		this._aktuellBewertung -= kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private debug(pHeader : String, pPrintMatrix : boolean) : void {
		console.log();
		console.log(JSON.stringify("#################### " + pHeader.valueOf() + " ####################"));
		console.log(JSON.stringify("Bewertung      = " + this._aktuellNichtwahlen + " / " + this._aktuellBewertung));
		console.log(JSON.stringify("Fachwahlen     = " + Arrays.toString(this._aktuellFachwahlZuKurs).valueOf()));
		console.log(JSON.stringify("BewertungBest  = " + this._aktuellNichtwahlenBest + " / " + this._aktuellBewertungBest));
		console.log(JSON.stringify("FachwahlenBest = " + Arrays.toString(this._aktuellFachwahlZuKursBest).valueOf()));
		if (!pPrintMatrix) 
			return;
		let data : Array<Array<number>> = this._aktuellMatrix.getMatrix();
		for (let schiene : number = 0; schiene < this.nSchienen; schiene++){
			let sData : String | null = this._aktuellGesperrteSchiene[schiene] ? "1" : "0";
			while (sData.length < 5) 
				sData = " " + sData.valueOf();
			console.log(JSON.stringify(sData));
		}
		console.log();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++){
			for (let schiene : number = 0; schiene < this.nSchienen; schiene++){
				let sData : String = "" + data[iFachwahl][schiene];
				if (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH) 
					sData = "INF";
				while (sData.length < 5) 
					sData = " " + sData.valueOf();
				console.log(JSON.stringify(sData));
			}
			console.log();
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.SchuelerblockungDynDaten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungDynDaten(obj : unknown) : SchuelerblockungDynDaten {
	return obj as SchuelerblockungDynDaten;
}
