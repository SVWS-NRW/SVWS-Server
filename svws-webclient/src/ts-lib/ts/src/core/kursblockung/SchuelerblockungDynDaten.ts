import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { AVLSet, cast_de_nrw_schule_svws_core_adt_set_AVLSet } from '../../core/adt/set/AVLSet';
import { SchuelerblockungOutput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputKurs } from '../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungOutputFachwahlZuKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutputFachwahlZuKurs } from '../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { SchuelerblockungInputFachwahl, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputFachwahl } from '../../core/data/kursblockung/SchuelerblockungInputFachwahl';
import { SchuelerblockungException, cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungException } from '../../core/kursblockung/SchuelerblockungException';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_logger_Logger } from '../../logger/Logger';
import { LogLevel, cast_de_nrw_schule_svws_logger_LogLevel } from '../../logger/LogLevel';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { SchuelerblockungInput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungMatrix, cast_de_nrw_schule_svws_core_kursblockung_KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { Arrays, cast_java_util_Arrays } from '../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';

export class SchuelerblockungDynDaten extends JavaObject {

	private static readonly UNENDLICH : number = 1000000;

	private readonly _random : Random;

	private readonly _logger : Logger;

	private readonly nFachwahlen : number;

	private readonly nSchienen : number;

	private readonly _fachwahlen : Array<number>;

	private readonly _fachwahlZuKurse : Vector<Vector<SchuelerblockungInputKurs>>;

	private readonly _fachwahlZuHatMultikurse : Array<boolean>;

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
		this._fachwahlen = Array(this.nFachwahlen).fill(0);
		this._fachwahlZuKurse = new Vector();
		this._fachwahlZuHatMultikurse = Array(this.nFachwahlen).fill(false);
		this.aktionInitialisiereDatenstrukturen(pInput);
		this._aktuellMatrix = new KursblockungMatrix(pRandom, this.nFachwahlen, this.nSchienen);
		this._aktuellGesperrteSchiene = Array(this.nSchienen).fill(false);
		this._aktuellFachwahlZuKurs = Array(this.nFachwahlen).fill(0);
		this._aktuellFachwahlZuKursBest = Array(this.nFachwahlen).fill(0);
		this._aktuellBewertung = 0;
		this._aktuellBewertungBest = 0;
	}

	/**
	 * Überprüft die Konstistenz und referentielle Integrität der Eingabedaten.
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
		let setKursID : AVLSet<Number | null> | null = new AVLSet();
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
			if (kurs.representation === null) 
				throw this.fehler("kurs.representation ist undefiniert! --> " + kurs.representation)
			if (JavaObject.equalsTranspiler(kurs.representation, (""))) 
				throw this.fehler("kurs.representation ist leer! --> " + kurs.representation)
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
					throw this.fehler("Kurs " + kurs.representation + " ist in zu kleiner Schiene! --> " + schiene1)
				if (schiene1 > nSchienen) 
					throw this.fehler("Kurs " + kurs.representation + " ist in zu großer Schiene! --> " + schiene1)
			}
			if (kurs.istFixiert && kurs.istGesperrt) 
				throw this.fehler("kurs.istFixiert && kurs.istGesperrt ist unmöglich! --> " + kurs.representation)
		}
		let setFachwahlID : AVLSet<Number | null> | null = new AVLSet();
		for (let fachwahl of pInput.fachwahlen) {
			if (fachwahl.id < 0) 
				throw this.fehler("fachwahl.id ist zu gering! --> " + fachwahl.id)
			if (setFachwahlID.add(fachwahl.id) === false) 
				throw this.fehler("fachwahl.id existiert doppelt! --> " + fachwahl.id)
			if (fachwahl.fach < 0) 
				throw this.fehler("fachwahl.fach ist zu gering! --> " + fachwahl.fach)
			if (fachwahl.kursart < 0) 
				throw this.fehler("fachwahl.kursart ist zu gering! --> " + fachwahl.kursart)
			if (fachwahl.representation === null) 
				throw this.fehler("fachwahl.representation ist undefiniert! --> " + fachwahl.representation)
			if (JavaObject.equalsTranspiler(fachwahl.representation, (""))) 
				throw this.fehler("fachwahl.representation ist leer! --> " + fachwahl.representation)
		}
		for (let iFachwahl : number = 0; iFachwahl < nFachwahlen; iFachwahl++){
			let fachwahl : SchuelerblockungInputFachwahl = pInput.fachwahlen.get(iFachwahl);
			let kursWurdeFixiert : boolean = false;
			let kurseWaehlbar : number = 0;
			for (let kurs of pInput.kurse) 
				if ((fachwahl.fach === kurs.fach) && (fachwahl.kursart === kurs.kursart)) {
					if (kurs.istGesperrt) 
						continue;
					if (kursWurdeFixiert) {
						if (kurs.istFixiert) 
							throw this.fehler("Die Fachart " + fachwahl.representation + " hat mehr als eine Fixierung!")
						continue;
					}
					if (kurs.istFixiert) {
						kursWurdeFixiert = true;
						kurseWaehlbar = 1;
					} else {
						kurseWaehlbar++;
					}
				}
			if (kurseWaehlbar <= 0) 
				throw this.fehler("Die Fachart " + fachwahl.representation + " hat keine wählbaren Kurse!")
		}
		for (let kurs of pInput.kurse) {
			let gefunden : number = 0;
			for (let r : number = 0; r < nFachwahlen; r++){
				let fachwahl : SchuelerblockungInputFachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fach === kurs.fach) && (fachwahl.kursart === kurs.kursart)) 
					gefunden++;
			}
			if (gefunden === 0) 
				throw this.fehler("Der Kurs " + kurs.representation + " konnte keiner Fachwahl zugeordnet werden!")
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
			let fachwahl : SchuelerblockungInputFachwahl = pInput.fachwahlen.get(iFachwahl);
			this._fachwahlen[iFachwahl] = fachwahl.id;
			let kurse : Vector<SchuelerblockungInputKurs> | null = new Vector();
			let hatFixiertenKurs : boolean = false;
			for (let kurs of pInput.kurse) 
				if ((fachwahl.fach === kurs.fach) && (fachwahl.kursart === kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
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
			wahl.fachwahl = this._fachwahlen[iFachwahl];
			wahl.kurs = this._aktuellFachwahlZuKursBest[iFachwahl];
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
						let kurs : SchuelerblockungInputKurs | null = this.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
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
				let kurs : SchuelerblockungInputKurs | null = this.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
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
				let kurs : SchuelerblockungInputKurs | null = this.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs === null) 
					throw this.fehler("Der Fachart " + iFachwahl + " wurde ein NULL-Kurs zugeordnet!")
				if (this.aktionBelegeKursUndo(iFachwahl, kurs) === false) 
					throw this.fehler("Der Kurs " + kurs.id + " konnte nicht entfernt werden!")
			}
	}

	private gibKleinstenKursInSchiene(pKurse : Vector<SchuelerblockungInputKurs>, pSchiene : number) : SchuelerblockungInputKurs | null {
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
