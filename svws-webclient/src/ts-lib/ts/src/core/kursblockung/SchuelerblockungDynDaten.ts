import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { SchuelerblockungOutput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputKurs } from '../../core/data/kursblockung/SchuelerblockungInputKurs';
import { KursblockungStatic, cast_de_nrw_schule_svws_core_kursblockung_KursblockungStatic } from '../../core/kursblockung/KursblockungStatic';
import { SchuelerblockungInputFachwahl, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInputFachwahl } from '../../core/data/kursblockung/SchuelerblockungInputFachwahl';
import { SchuelerblockungException, cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungException } from '../../core/kursblockung/SchuelerblockungException';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Logger, cast_de_nrw_schule_svws_logger_Logger } from '../../logger/Logger';
import { LogLevel, cast_de_nrw_schule_svws_logger_LogLevel } from '../../logger/LogLevel';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { SchuelerblockungInput, cast_de_nrw_schule_svws_core_data_kursblockung_SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { KursblockungMatrix, cast_de_nrw_schule_svws_core_kursblockung_KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { JavaLong, cast_java_lang_Long } from '../../java/lang/JavaLong';
import { Arrays, cast_java_util_Arrays } from '../../java/util/Arrays';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';

export class SchuelerblockungDynDaten extends JavaObject {

	private readonly _random : Random;

	private readonly _logger : Logger;

	private readonly _matrix : KursblockungMatrix;

	private readonly _fachwahlen : Vector<Number>;

	private readonly _fachwahlZuZeile : Vector<Number>;

	private readonly _fachwahlZuKurse : Vector<Vector<SchuelerblockungInputKurs>>;

	private readonly _fachwahlZuHatMultikurse : Array<boolean>;

	private readonly _fachwahlZuKurs : Array<number>;

	private readonly _gesperrteSchiene : Array<boolean>;

	private readonly nFachwahlen : number;

	private readonly nSchienen : number;


	/**
	 *Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
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
		this.nFachwahlen = pInput.fachwahlen.size();
		this.nSchienen = pInput.schienen;
		this.pruefeDaten(pInput);
		this._matrix = new KursblockungMatrix(pRandom, this.nFachwahlen, this.nSchienen);
		this._fachwahlen = new Vector();
		this._fachwahlZuZeile = new Vector();
		this._fachwahlZuKurse = new Vector();
		this._fachwahlZuHatMultikurse = Array(this.nFachwahlen).fill(false);
		this._fachwahlZuKurs = Array(this.nFachwahlen).fill(0);
		this._gesperrteSchiene = Array(this.nSchienen).fill(false);
	}

	/**
	 *Erzeugt einen Fehler und teilt dem Logger einen Fehler mit.
	 * 
	 * @param fehlermeldung Die Fehlermeldung. 
	 */
	private fehler(fehlermeldung : String) : SchuelerblockungException | null {
		this._logger.logLn(LogLevel.ERROR, fehlermeldung);
		return new SchuelerblockungException(fehlermeldung);
	}

	private pruefeDaten(pInput : SchuelerblockungInput) : void {
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
		for (let kurs of pInput.kurse) {
			if (kurs.id < 0) 
				throw this.fehler("kurs.id ist zu gering! --> " + kurs.id)
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
			if (kurs.schienen.length > nSchienen) 
				throw this.fehler("kurs.schienen.length ist zu groß! --> " + kurs.schienen.length)
		}
		for (let fachwahl of pInput.fachwahlen) {
			if (fachwahl.id < 0) 
				throw this.fehler("fachwahl.id ist zu gering! --> " + fachwahl.id)
			if (fachwahl.fach < 0) 
				throw this.fehler("fachwahl.fach ist zu gering! --> " + fachwahl.fach)
			if (fachwahl.kursart < 0) 
				throw this.fehler("fachwahl.kursart ist zu gering! --> " + fachwahl.kursart)
			if (fachwahl.representation === null) 
				throw this.fehler("fachwahl.representation ist undefiniert! --> " + fachwahl.representation)
			if (JavaObject.equalsTranspiler(fachwahl.representation, (""))) 
				throw this.fehler("fachwahl.representation ist leer! --> " + fachwahl.representation)
		}
		let fachwahlZuKurse : Vector<Vector<SchuelerblockungInputKurs>> | null = new Vector();
		for (let r : number = 0; r < nFachwahlen; r++)
			fachwahlZuKurse.add(new Vector());
		for (let kurs of pInput.kurse) {
			let gefunden : number = 0;
			for (let r : number = 0; r < nFachwahlen; r++){
				let fachwahl : SchuelerblockungInputFachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fach === kurs.fach) && (fachwahl.kursart === kurs.kursart)) {
					fachwahlZuKurse.get(r).add(kurs);
					gefunden++;
				}
			}
			if (gefunden === 0) 
				throw this.fehler("Der Kurs " + kurs.representation + " konnte keiner Fachwahl zugeordnet werden!")
		}
		for (let kurs of pInput.kurse) {
		}
	}

	/**
	 *Entfernt den Schüler zunächst aus allen seinen Kursen. Verteilt dann die Multikurse zuerst, da ein Matching hier
	 * nicht funktioniert. Verteilt zuletzt die übrigen Kurse mit einem gewichteten bipartiten Matching. 
	 */
	aktionFachwahlenEntfernenUndVerteilen() : void {
		let data : Array<Array<number>> = this._matrix.getMatrix();
		Arrays.fill(this._fachwahlZuKurs, -1);
		Arrays.fill(this._gesperrteSchiene, false);
		let UNENDLICH : number = Number.MAX_VALUE;
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			for (let iSchiene : number = 0; iSchiene < this.nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = UNENDLICH;
		for (let iFachwahl of KursblockungStatic.gibPermutation(this._random, this.nFachwahlen)) 
			if (this._fachwahlZuHatMultikurse[iFachwahl]) 
				for (let iKurs of KursblockungStatic.gibPermutation(this._random, this._fachwahlZuKurse.get(iFachwahl).size())) {
					let kurs : SchuelerblockungInputKurs = this._fachwahlZuKurse.get(iFachwahl).get(iKurs);
				}
	}

	aktionZustand1Speichern() : void {
	}

	gibCompareZustand1() : number {
		return 0;
	}

	gibErzeugeZustand1() : SchuelerblockungOutput {
		return new SchuelerblockungOutput();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.SchuelerblockungDynDaten'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_SchuelerblockungDynDaten(obj : unknown) : SchuelerblockungDynDaten {
	return obj as SchuelerblockungDynDaten;
}
