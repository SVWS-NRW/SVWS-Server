import { JavaObject } from '../../java/lang/JavaObject';
import { SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs } from '../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungOutputFachwahlZuKurs } from '../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { ArrayList } from '../../java/util/ArrayList';
import { GostFachwahl } from '../../core/data/gost/GostFachwahl';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { Random } from '../../java/util/Random';
import { KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { Arrays } from '../../java/util/Arrays';
import { UserNotificationException } from '../../core/exceptions/UserNotificationException';
import { HashSet } from '../../java/util/HashSet';

export class SchuelerblockungDynDaten extends JavaObject {

	private static readonly UNENDLICH : number = 1000000;

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	private readonly _random : Random;

	private readonly nFachwahlen : number;

	private readonly nSchienen : number;

	private readonly _fachwahlZuKurse : ArrayList<ArrayList<SchuelerblockungInputKurs>>;

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
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(pRandom : Random, pInput : SchuelerblockungInput) {
		super();
		this._random = pRandom;
		this.aktionPruefeEingabedaten(pInput);
		this.nFachwahlen = pInput.fachwahlen.size();
		this.nSchienen = pInput.schienen;
		this._fachwahlZuKurse = new ArrayList();
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
			throw new DeveloperNotificationException("pInput == NULL")
		if (pInput.fachwahlen === null)
			throw new DeveloperNotificationException("pInput.fachwahlen == NULL")
		if (pInput.kurse === null)
			throw new DeveloperNotificationException("pInput.kurse == NULL")
		const tmpNFachwahlen : number = pInput.fachwahlen.size();
		if (tmpNFachwahlen < 1)
			throw new DeveloperNotificationException("Der Schüler hat zu wenig Fachwahlen (" + tmpNFachwahlen + "), ein Blocken sollte gar nicht angeboten werden!")
		const tmpNSchienen : number = pInput.schienen;
		if (tmpNSchienen < 1)
			throw new DeveloperNotificationException("Die Schienenanzahl (" + tmpNSchienen + ") ist zu gering!")
		const nKurse : number = pInput.kurse.size();
		if (nKurse < 1)
			throw new DeveloperNotificationException("Die Kursanzahl (" + nKurse + ") ist zu gering!")
		const setKursID : HashSet<number | null> | null = new HashSet();
		for (const kurs of pInput.kurse) {
			if (kurs.id < 0)
				throw new DeveloperNotificationException("kurs.id (" + kurs.id + ") ist zu gering!")
			if (!setKursID.add(kurs.id))
				throw new DeveloperNotificationException("kurs.id (" + kurs.id + ") existiert doppelt!")
			if (kurs.fach < 0)
				throw new DeveloperNotificationException("kurs.fach (" + kurs.fach + ") ist zu gering!")
			if (kurs.kursart < 0)
				throw new DeveloperNotificationException("kurs.kursart (" + kurs.kursart + ") ist zu gering!")
			if (kurs.anzahlSuS < 0)
				throw new DeveloperNotificationException("kurs.anzahlSuS (" + kurs.anzahlSuS + ") ist zu gering!")
			if (kurs.schienen === null)
				throw new DeveloperNotificationException("kurs.schienen == (" + kurs.schienen + ") ist nicht definiert!")
			if (kurs.schienen.length <= 0)
				throw new DeveloperNotificationException("kurs.schienen.length (" + kurs.schienen.length + ") ist zu gering!")
			if (kurs.schienen.length > tmpNSchienen)
				throw new DeveloperNotificationException("kurs.schienen.length (" + kurs.schienen.length + ") ist zu groß!")
			for (const schiene1 of kurs.schienen) {
				if (schiene1 < 1)
					throw new DeveloperNotificationException("Kurs " + kurs.id + " ist in zu kleiner Schiene (" + schiene1 + ")!")
				if (schiene1 > tmpNSchienen)
					throw new DeveloperNotificationException("Kurs " + kurs.id + " ist in zu großer Schiene (" + schiene1 + ")!")
			}
			if (kurs.istFixiert && kurs.istGesperrt)
				throw new DeveloperNotificationException("Kurs " + kurs.id + " ist fixiert und gesperrt, das sollte nicht möglich sein!")
		}
		for (const fachwahl of pInput.fachwahlen) {
			if (fachwahl.schuelerID < 0)
				throw new DeveloperNotificationException("fachwahl.schuelerID (" + fachwahl.schuelerID + ") ist zu gering!")
			if (fachwahl.fachID < 0)
				throw new DeveloperNotificationException("fachwahl.fachID (" + fachwahl.fachID + ") ist zu gering!")
			if (fachwahl.kursartID < 0)
				throw new DeveloperNotificationException("fachwahl.kursartID (" + fachwahl.kursartID + ") ist zu gering!")
		}
		for (let iFachwahl : number = 0; iFachwahl < tmpNFachwahlen; iFachwahl++) {
			const fachwahl : GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			if (iFachwahl >= pInput.fachwahlenText.size())
				throw new DeveloperNotificationException("pInput.fachwahlenText: Es fehlt der Text zur Fachwahl (" + iFachwahl + ")!")
			const representation : string | null = pInput.fachwahlenText.get(iFachwahl);
			let kursWurdeFixiert : boolean = false;
			for (const kurs of pInput.kurse)
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart)) {
					if (kurs.istGesperrt)
						continue;
					if (kurs.istFixiert) {
						if (kursWurdeFixiert)
							throw new UserNotificationException("Die Fachart/Fachwahl (" + representation! + ") hat mehr als eine Fixierung!")
						kursWurdeFixiert = true;
					}
				}
		}
		for (const kurs of pInput.kurse) {
			let gefunden : number = 0;
			for (let r : number = 0; r < tmpNFachwahlen; r++) {
				const fachwahl : GostFachwahl = pInput.fachwahlen.get(r);
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart))
					gefunden++;
			}
			if (gefunden === 0)
				throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte keiner Fachart/Fachwahl zugeordnet werden!")
		}
	}

	/**
	 * Initialisiert {@link #_fachwahlen}, {@link #_fachwahlZuKurse} und {@link #_fachwahlZuHatMultikurse}.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private aktionInitialisiereDatenstrukturen(pInput : SchuelerblockungInput) : void {
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			const fachwahl : GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			this._fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			this._fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;
			const kurse : ArrayList<SchuelerblockungInputKurs> | null = new ArrayList();
			let hatFixiertenKurs : boolean = false;
			for (const kurs of pInput.kurse)
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
					if (kurs.istFixiert) {
						hatFixiertenKurs = true;
						kurse.clear();
					}
					kurse.add(kurs);
				}
			this._fachwahlZuKurse.add(kurse);
			let max : number = 1;
			for (const kurs of kurse)
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
		const out : SchuelerblockungOutput = new SchuelerblockungOutput();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			const wahl : SchuelerblockungOutputFachwahlZuKurs = new SchuelerblockungOutputFachwahlZuKurs();
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
		if (!this._fachwahlZuHatMultikurse[iFachwahl]) {
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}
		let schienenAnzahl : number = 2;
		for (const kurs of this._fachwahlZuKurse.get(iFachwahl)) {
			schienenAnzahl = Math.max(schienenAnzahl, kurs.schienen.length);
			if (this.aktionBelegeKurs(iFachwahl, kurs)) {
				this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
				if (!this.aktionBelegeKursUndo(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMultikurseRekursiv' ist ein unerwarteter Fehler passiert: Der Kurs (" + kurs.id + ") konnte vom Algorithmus nicht entfernt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
			}
		}
		this._aktuellNichtwahlen += schienenAnzahl;
		if (this._aktuellNichtwahlen <= this._aktuellNichtwahlenBest)
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		this._aktuellNichtwahlen -= schienenAnzahl;
	}

	private aktionVerteileMitMatching() : void {
		const data : Array<Array<number>> = this._aktuellMatrix.getMatrix();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			for (let iSchiene : number = 0; iSchiene < this.nSchienen; iSchiene++)
				data[iFachwahl][iSchiene] = SchuelerblockungDynDaten.UNENDLICH;
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (!this._fachwahlZuHatMultikurse[iFachwahl])
				for (let schiene : number = 0; schiene < this.nSchienen; schiene++)
					if (!this._aktuellGesperrteSchiene[schiene]) {
						const kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs !== null)
							data[iFachwahl][schiene] = kurs.anzahlSuS * kurs.anzahlSuS;
					}
		const r2c : Array<number> = this._aktuellMatrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (!this._fachwahlZuHatMultikurse[iFachwahl]) {
				const schiene : number = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
					this._aktuellNichtwahlen++;
					continue;
				}
				const kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs === null)
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! Diesen Fehler kann nur das Programmier-Team beheben.")
				if (!this.aktionBelegeKurs(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: Der Kurs (" + kurs.id + ") konnte nicht belegt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
			}
		if ((this._aktuellNichtwahlen < this._aktuellNichtwahlenBest) || ((this._aktuellNichtwahlen === this._aktuellNichtwahlenBest) && (this._aktuellBewertung < this._aktuellBewertungBest))) {
			this._aktuellNichtwahlenBest = this._aktuellNichtwahlen;
			this._aktuellBewertungBest = this._aktuellBewertung;
			for (let i : number = 0; i < this.nFachwahlen; i++)
				this._aktuellFachwahlZuKursBest[i] = this._aktuellFachwahlZuKurs[i];
		}
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++)
			if (!this._fachwahlZuHatMultikurse[iFachwahl]) {
				const schiene : number = r2c[iFachwahl];
				if ((schiene < 0) || (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
					this._aktuellNichtwahlen--;
					continue;
				}
				const kurs : SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this._fachwahlZuKurse.get(iFachwahl), schiene);
				if (kurs === null)
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! Diesen Fehler kann nur das Programmier-Team beheben.")
				if (!this.aktionBelegeKursUndo(iFachwahl, kurs))
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMitMatching' ist ein unerwarteter Fehler passiert: Der Kurs (" + kurs.id + ") konnte nicht entfernt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
			}
	}

	private static gibKleinstenKursInSchiene(pKurse : ArrayList<SchuelerblockungInputKurs>, pSchiene : number) : SchuelerblockungInputKurs | null {
		const maxSuS : number = JavaInteger.MAX_VALUE;
		let best : SchuelerblockungInputKurs | null = null;
		for (const kurs of pKurse)
			if ((kurs.schienen[0] - 1 === pSchiene) && (kurs.anzahlSuS < maxSuS))
				best = kurs;
		return best;
	}

	private aktionBelegeKurs(iFachwahl : number, kurs : SchuelerblockungInputKurs) : boolean {
		for (const schiene1 of kurs.schienen)
			if (this._aktuellGesperrteSchiene[schiene1 - 1])
				return false;
		this._aktuellFachwahlZuKurs[iFachwahl] = kurs.id;
		for (const schiene1 of kurs.schienen)
			this._aktuellGesperrteSchiene[schiene1 - 1] = true;
		this._aktuellBewertung += kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private aktionBelegeKursUndo(iFachwahl : number, kurs : SchuelerblockungInputKurs) : boolean {
		if (this._aktuellFachwahlZuKurs[iFachwahl] < 0)
			return false;
		for (const schiene1 of kurs.schienen)
			if (!this._aktuellGesperrteSchiene[schiene1 - 1])
				return false;
		this._aktuellFachwahlZuKurs[iFachwahl] = -1;
		for (const schiene1 of kurs.schienen)
			this._aktuellGesperrteSchiene[schiene1 - 1] = false;
		this._aktuellBewertung -= kurs.anzahlSuS * kurs.anzahlSuS;
		return true;
	}

	private debug(pHeader : string, pPrintMatrix : boolean) : void {
		console.log();
		console.log(JSON.stringify("#################### " + pHeader! + " ####################"));
		console.log(JSON.stringify("Bewertung      = " + this._aktuellNichtwahlen + " / " + this._aktuellBewertung));
		console.log(JSON.stringify("Fachwahlen     = " + Arrays.toString(this._aktuellFachwahlZuKurs)!));
		console.log(JSON.stringify("BewertungBest  = " + this._aktuellNichtwahlenBest + " / " + this._aktuellBewertungBest));
		console.log(JSON.stringify("FachwahlenBest = " + Arrays.toString(this._aktuellFachwahlZuKursBest)!));
		if (!pPrintMatrix)
			return;
		const data : Array<Array<number>> = this._aktuellMatrix.getMatrix();
		for (let schiene : number = 0; schiene < this.nSchienen; schiene++) {
			let sData : string | null = this._aktuellGesperrteSchiene[schiene] ? "1" : "0";
			while (sData.length < 5)
				sData = " " + sData!;
			console.log(JSON.stringify(sData));
		}
		console.log();
		for (let iFachwahl : number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			for (let schiene : number = 0; schiene < this.nSchienen; schiene++) {
				let sData : string = "" + data[iFachwahl][schiene];
				if (data[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)
					sData = "INF";
				while (sData.length < 5)
					sData = " " + sData!;
				console.log(JSON.stringify(sData));
			}
			console.log();
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.SchuelerblockungDynDaten'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_SchuelerblockungDynDaten(obj : unknown) : SchuelerblockungDynDaten {
	return obj as SchuelerblockungDynDaten;
}
