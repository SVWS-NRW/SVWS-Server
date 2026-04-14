import { JavaObject } from '../../java/lang/JavaObject';
import { SchuelerblockungOutput } from '../../core/data/kursblockung/SchuelerblockungOutput';
import { SchuelerblockungInputKurs } from '../../core/data/kursblockung/SchuelerblockungInputKurs';
import { SchuelerblockungOutputFachwahlZuKurs } from '../../core/data/kursblockung/SchuelerblockungOutputFachwahlZuKurs';
import { ArrayList } from '../../java/util/ArrayList';
import { GostFachwahl } from '../../core/data/gost/GostFachwahl';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../java/lang/JavaString';
import { JavaMath } from '../../java/lang/JavaMath';
import { System } from '../../java/lang/System';
import { JavaInteger } from '../../java/lang/JavaInteger';
import { SchuelerblockungInput } from '../../core/data/kursblockung/SchuelerblockungInput';
import { KursblockungMatrix } from '../../core/kursblockung/KursblockungMatrix';
import { Random } from '../../java/util/Random';
import { Class } from '../../java/lang/Class';
import { Arrays } from '../../java/util/Arrays';
import { HashSet } from '../../java/util/HashSet';

export class SchuelerblockungDynDaten extends JavaObject {

	private static readonly UNENDLICH: number = 1000000;

	private static readonly MALUS_ZUSAMMEN_MIT_IM_KURS: number = -1000;

	private static readonly MALUS_VERBOTEN_MIT_IM_KURS: number = 1000;

	private readonly nFachwahlen: number;

	private readonly nSchienen: number;

	private readonly fachwahlZuKurse: ArrayList<ArrayList<SchuelerblockungInputKurs>>;

	private readonly fachwahlZuHatMultikurse: Array<boolean>;

	private readonly fachwahlZuFachID: Array<number>;

	private readonly fachwahlZuKursartID: Array<number>;

	private readonly dynMatrix: KursblockungMatrix;

	private readonly dynGesperrteSchiene: Array<boolean>;

	private readonly dynFachwahlZuKurs: Array<number>;

	private readonly dynFachwahlZuKursBest: Array<number>;

	private dynNichtwahlen: number = 0;

	private dynNichtwahlenBest: number = 0;

	private dynBewertung: number = 0;

	private dynBewertungBest: number = 0;


	/**
	 * Der Konstruktor der Klasse liest alle Daten von {@link SchuelerblockungInput} ein und baut die relevanten
	 * Datenstrukturen auf.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pInput  Die Eingabedaten (Schnittstelle zur GUI).
	 */
	public constructor(pRandom: Random, pInput: SchuelerblockungInput) {
		super();
		this.aktionPruefeEingabedaten(pInput);
		this.nFachwahlen = pInput.fachwahlen.size();
		this.nSchienen = pInput.schienen;
		this.fachwahlZuKurse = new ArrayList();
		this.fachwahlZuHatMultikurse = Array(this.nFachwahlen).fill(false);
		this.fachwahlZuFachID = Array(this.nFachwahlen).fill(0);
		this.fachwahlZuKursartID = Array(this.nFachwahlen).fill(0);
		this.aktionInitialisiereDatenstrukturen(pInput);
		this.dynMatrix = new KursblockungMatrix(pRandom, this.nFachwahlen, this.nSchienen);
		this.dynGesperrteSchiene = Array(this.nSchienen).fill(false);
		this.dynFachwahlZuKurs = Array(this.nFachwahlen).fill(0);
		this.dynFachwahlZuKursBest = Array(this.nFachwahlen).fill(0);
		this.dynBewertung = 0;
		this.dynBewertungBest = 0;
	}

	/**
	 * Überprüft die Konsistenz und referentielle Integrität der Eingabedaten.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	aktionPruefeEingabedaten(pInput: SchuelerblockungInput): void {
		if (pInput.fachwahlen === null) {
			throw new DeveloperNotificationException("pInput.fachwahlen == NULL")
		}
		if (pInput.kurse === null) {
			throw new DeveloperNotificationException("pInput.kurse == NULL")
		}
		SchuelerblockungDynDaten.aktionPruefeEingabedatenAnzahlen(pInput);
		SchuelerblockungDynDaten.aktionPruefeEingabedatenKurse(pInput);
		SchuelerblockungDynDaten.aktionPruefeEingabedatenFachwahlenAttribute(pInput);
		SchuelerblockungDynDaten.aktionPruefeEingabedatenFachwahlenDoppelfixierungen(pInput);
		SchuelerblockungDynDaten.aktionPruefeEingabedatenFachwahlenZuordnungen(pInput);
	}

	private static aktionPruefeEingabedatenFachwahlenZuordnungen(pInput: SchuelerblockungInput): void {
		for (const kurs of pInput.kurse) {
			let gefunden: number = 0;
			for (let iFachwahl: number = 0; iFachwahl < pInput.fachwahlen.size(); iFachwahl++) {
				const fachwahl: GostFachwahl = pInput.fachwahlen.get(iFachwahl);
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart)) {
					gefunden++;
				}
			}
			DeveloperNotificationException.ifTrue(JavaString.format("Der Kurs (%d) konnte keiner Fachart/Fachwahl zugeordnet werden!", kurs.id), gefunden === 0);
		}
	}

	private static aktionPruefeEingabedatenFachwahlenDoppelfixierungen(pInput: SchuelerblockungInput): void {
		for (let iFachwahl: number = 0; iFachwahl < pInput.fachwahlen.size(); iFachwahl++) {
			DeveloperNotificationException.ifTrue(JavaString.format("pInput.fachwahlenText: Es fehlt der Text zur Fachwahl (%d)!", iFachwahl), iFachwahl >= pInput.fachwahlenText.size());
			const representation: string = pInput.fachwahlenText.get(iFachwahl);
			const fachwahl: GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			let kursWurdeFixiert: boolean = false;
			for (const kurs of pInput.kurse) {
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart) && (kurs.istFixiert)) {
					DeveloperNotificationException.ifTrue(JavaString.format("Die Fachart/Fachwahl (%s) hat mehr als eine Fixierung!", representation), kursWurdeFixiert);
					kursWurdeFixiert = true;
				}
			}
		}
	}

	private static aktionPruefeEingabedatenFachwahlenAttribute(pInput: SchuelerblockungInput): void {
		for (const fachwahl of pInput.fachwahlen) {
			DeveloperNotificationException.ifInvalidID("fachwahl.schuelerID", fachwahl.schuelerID);
			DeveloperNotificationException.ifInvalidID("fachwahl.fachID", fachwahl.fachID);
			DeveloperNotificationException.ifInvalidID("fachwahl.kursartID", fachwahl.kursartID);
		}
	}

	private static aktionPruefeEingabedatenKurse(pInput: SchuelerblockungInput): void {
		const setKursID: HashSet<number> | null = new HashSet<number>();
		for (const kurs of pInput.kurse) {
			DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
			DeveloperNotificationException.ifSetAddsDuplicate("setKursID", setKursID, kurs.id);
			DeveloperNotificationException.ifInvalidID("kurs.fach", kurs.fach);
			DeveloperNotificationException.ifTrue(JavaString.format("kurs.kursart (%d) ist zu gering!", kurs.kursart), kurs.kursart < 0);
			DeveloperNotificationException.ifTrue(JavaString.format("kurs.anzahlSuS (%d) ist zu gering!", kurs.anzahlSuS), kurs.anzahlSuS < 0);
			DeveloperNotificationException.ifTrue("kurs.schienen == null, also nicht definiert!", kurs.schienen === null);
			DeveloperNotificationException.ifTrue(JavaString.format("kurs.schienen.length (%d) ist zu gering!", kurs.schienen.length), kurs.schienen.length <= 0);
			DeveloperNotificationException.ifTrue(JavaString.format("kurs.schienen.length (%d > %d) ist zu groß!", kurs.schienen.length, pInput.schienen), kurs.schienen.length > pInput.schienen);
			for (const schiene1 of kurs.schienen) {
				DeveloperNotificationException.ifTrue(JavaString.format("Kurs %d ist in zu kleiner Schiene (%d)!", kurs.id, schiene1), schiene1 < 1);
				DeveloperNotificationException.ifTrue(JavaString.format("Kurs %d ist in zu großer Schiene (%d)!", kurs.id, schiene1), schiene1 > pInput.schienen);
			}
			DeveloperNotificationException.ifTrue(JavaString.format("Kurs %d ist fixiert und gesperrt, das sollte nicht möglich sein!", kurs.id), kurs.istFixiert && kurs.istGesperrt);
		}
	}

	private static aktionPruefeEingabedatenAnzahlen(pInput: SchuelerblockungInput): void {
		DeveloperNotificationException.ifTrue("Der Schüler hat keine Fachwahlen, ein Blocken sollte gar nicht angeboten werden!", pInput.fachwahlen.isEmpty());
		const nSchienen: number = pInput.schienen;
		DeveloperNotificationException.ifTrue(JavaString.format("Die Schienenanzahl (%d) ist zu gering!", nSchienen), nSchienen < 1);
		const nKurse: number = pInput.kurse.size();
		DeveloperNotificationException.ifTrue(JavaString.format("Die Kursanzahl (%d) ist zu gering!", nKurse), nKurse < 1);
	}

	/**
	 * Initialisiert {@link #fachwahlZuFachID}, {@link #fachwahlZuFachID} und
	 *  {@link #fachwahlZuKurse}, {@link #fachwahlZuHatMultikurse}.
	 *
	 * @param pInput Die Eingabedaten (Schnittstelle zur GUI).
	 */
	private aktionInitialisiereDatenstrukturen(pInput: SchuelerblockungInput): void {
		for (let iFachwahl: number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			const fachwahl: GostFachwahl = pInput.fachwahlen.get(iFachwahl);
			this.fachwahlZuFachID[iFachwahl] = fachwahl.fachID;
			this.fachwahlZuKursartID[iFachwahl] = fachwahl.kursartID;
			const kurse: ArrayList<SchuelerblockungInputKurs> | null = new ArrayList<SchuelerblockungInputKurs>();
			let hatFixiertenKurs: boolean = false;
			for (const kurs of pInput.kurse) {
				if ((fachwahl.fachID === kurs.fach) && (fachwahl.kursartID === kurs.kursart) && (!kurs.istGesperrt) && (!hatFixiertenKurs)) {
					if (kurs.istFixiert) {
						hatFixiertenKurs = true;
						kurse.clear();
					}
					kurse.add(kurs);
				}
			}
			this.fachwahlZuKurse.add(kurse);
			let max: number = 1;
			for (const kurs of kurse) {
				max = Math.max(max, kurs.schienen.length);
			}
			this.fachwahlZuHatMultikurse[iFachwahl] = max >= 2;
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
	gibBestesMatching(): SchuelerblockungOutput {
		this.dynNichtwahlen = 0;
		this.dynBewertung = 0;
		this.dynNichtwahlenBest = SchuelerblockungDynDaten.UNENDLICH;
		this.dynBewertungBest = SchuelerblockungDynDaten.UNENDLICH;
		Arrays.fill(this.dynFachwahlZuKurs, -1);
		Arrays.fill(this.dynFachwahlZuKursBest, -1);
		Arrays.fill(this.dynGesperrteSchiene, false);
		this.aktionVerteileMultikurseRekursiv(0);
		const out: SchuelerblockungOutput = new SchuelerblockungOutput();
		for (let iFachwahl: number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			const wahl: SchuelerblockungOutputFachwahlZuKurs = new SchuelerblockungOutputFachwahlZuKurs();
			wahl.fachID = this.fachwahlZuFachID[iFachwahl];
			wahl.kursartID = this.fachwahlZuKursartID[iFachwahl];
			wahl.kursID = this.dynFachwahlZuKursBest[iFachwahl];
			out.fachwahlenZuKurs.add(wahl);
		}
		return out;
	}

	private aktionVerteileMultikurseRekursiv(iFachwahl: number): void {
		if (iFachwahl >= this.nFachwahlen) {
			this.aktionVerteileMitMatching();
			return;
		}
		if (!this.fachwahlZuHatMultikurse[iFachwahl]) {
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
			return;
		}
		let schienenAnzahl: number = 2;
		for (const kurs of this.fachwahlZuKurse.get(iFachwahl)) {
			schienenAnzahl = Math.max(schienenAnzahl, kurs.schienen.length);
			if (this.aktionBelegeKurs(iFachwahl, kurs)) {
				this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
				if (!this.aktionBelegeKursUndo(iFachwahl, kurs)) {
					throw new DeveloperNotificationException("In der Methode 'SchuelerblockungDynDaten.aktionVerteileMultikurseRekursiv' ist ein unerwarteter Fehler passiert: Der Kurs (" + kurs.id + ") konnte vom Algorithmus nicht entfernt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
				}
			}
		}
		this.dynNichtwahlen += schienenAnzahl;
		if (this.dynNichtwahlen <= this.dynNichtwahlenBest) {
			this.aktionVerteileMultikurseRekursiv(iFachwahl + 1);
		}
		this.dynNichtwahlen -= schienenAnzahl;
	}

	private static gibKursBewertung(kurs: SchuelerblockungInputKurs): number {
		let bewertung: number = 0;
		bewertung += kurs.anzahlSuS * kurs.anzahlSuS as number;
		bewertung += kurs.anzahlZusammenMitWuensche * SchuelerblockungDynDaten.MALUS_ZUSAMMEN_MIT_IM_KURS;
		bewertung += kurs.anzahlVerbotenMitWuensche * SchuelerblockungDynDaten.MALUS_VERBOTEN_MIT_IM_KURS;
		return bewertung;
	}

	private aktionVerteileMitMatching(): void {
		const matrix: Array<Array<number>> = this.dynMatrix.getMatrix();
		this.aktionVerteileMitMatchingFuelleMatrix();
		const r2c: Array<number> = this.dynMatrix.gibMinimalesBipartitesMatchingGewichtet(true);
		for (let iFachwahl: number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			this.aktionVerteileMitMatchingKursHinzufuegen(iFachwahl, matrix, r2c);
		}
		if ((this.dynNichtwahlen < this.dynNichtwahlenBest) || ((this.dynNichtwahlen === this.dynNichtwahlenBest) && (this.dynBewertung < this.dynBewertungBest))) {
			this.dynNichtwahlenBest = this.dynNichtwahlen;
			this.dynBewertungBest = this.dynBewertung;
			System.arraycopy(this.dynFachwahlZuKurs, 0, this.dynFachwahlZuKursBest, 0, this.nFachwahlen);
		}
		for (let iFachwahl: number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			this.aktionVerteileMitMatchingKursEntfernen(iFachwahl, matrix, r2c);
		}
	}

	private aktionVerteileMitMatchingFuelleMatrix(): void {
		const data: Array<Array<number>> = this.dynMatrix.getMatrix();
		this.dynMatrix.fuelleMitWert(SchuelerblockungDynDaten.UNENDLICH);
		for (let iFachwahl: number = 0; iFachwahl < this.nFachwahlen; iFachwahl++) {
			if (!this.fachwahlZuHatMultikurse[iFachwahl]) {
				for (let schiene: number = 0; schiene < this.nSchienen; schiene++) {
					if (!this.dynGesperrteSchiene[schiene]) {
						const kurs: SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this.fachwahlZuKurse.get(iFachwahl), schiene);
						if (kurs !== null) {
							data[iFachwahl][schiene] = SchuelerblockungDynDaten.gibKursBewertung(kurs);
						}
					}
				}
			}
		}
	}

	private aktionVerteileMitMatchingKursHinzufuegen(iFachwahl: number, matrix: Array<Array<number>>, r2c: Array<number>): void {
		if (this.fachwahlZuHatMultikurse[iFachwahl]) {
			return;
		}
		const schiene: number = r2c[iFachwahl];
		if ((schiene < 0) || (matrix[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
			this.dynNichtwahlen++;
			return;
		}
		const kurs: SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this.fachwahlZuKurse.get(iFachwahl), schiene);
		if (kurs === null) {
			throw new DeveloperNotificationException("Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! Diesen Fehler kann nur das Programmier-Team beheben.")
		}
		if (!this.aktionBelegeKurs(iFachwahl, kurs)) {
			throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte nicht belegt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
		}
	}

	private aktionVerteileMitMatchingKursEntfernen(iFachwahl: number, matrix: Array<Array<number>>, r2c: Array<number>): void {
		if (this.fachwahlZuHatMultikurse[iFachwahl]) {
			return;
		}
		const schiene: number = r2c[iFachwahl];
		if ((schiene < 0) || (matrix[iFachwahl][schiene] === SchuelerblockungDynDaten.UNENDLICH)) {
			this.dynNichtwahlen--;
			return;
		}
		const kurs: SchuelerblockungInputKurs | null = SchuelerblockungDynDaten.gibKleinstenKursInSchiene(this.fachwahlZuKurse.get(iFachwahl), schiene);
		if (kurs === null) {
			throw new DeveloperNotificationException("Der Fachart (" + iFachwahl + ") wurde ein NULL-Kurs zugeordnet! Diesen Fehler kann nur das Programmier-Team beheben.")
		}
		if (!this.aktionBelegeKursUndo(iFachwahl, kurs)) {
			throw new DeveloperNotificationException("Der Kurs (" + kurs.id + ") konnte nicht entfernt werden! Diesen Fehler kann nur das Programmier-Team beheben.")
		}
	}

	private static gibKleinstenKursInSchiene(pKurse: ArrayList<SchuelerblockungInputKurs>, pSchiene: number): SchuelerblockungInputKurs | null {
		let maxSuS: number = JavaInteger.MAX_VALUE;
		let best: SchuelerblockungInputKurs | null = null;
		for (const kurs of pKurse) {
			if (((kurs.schienen[0] - 1) === pSchiene) && (kurs.anzahlSuS < maxSuS)) {
				best = kurs;
				maxSuS = kurs.anzahlSuS;
			}
		}
		return best;
	}

	private aktionBelegeKurs(iFachwahl: number, kurs: SchuelerblockungInputKurs): boolean {
		for (const schiene1 of kurs.schienen) {
			if (this.dynGesperrteSchiene[schiene1 - 1]) {
				return false;
			}
		}
		this.dynFachwahlZuKurs[iFachwahl] = kurs.id;
		for (const schiene1 of kurs.schienen) {
			this.dynGesperrteSchiene[schiene1 - 1] = true;
		}
		this.dynBewertung += SchuelerblockungDynDaten.gibKursBewertung(kurs);
		return true;
	}

	private aktionBelegeKursUndo(iFachwahl: number, kurs: SchuelerblockungInputKurs): boolean {
		if (this.dynFachwahlZuKurs[iFachwahl] < 0) {
			return false;
		}
		for (const schiene1 of kurs.schienen) {
			if (!this.dynGesperrteSchiene[schiene1 - 1]) {
				return false;
			}
		}
		this.dynFachwahlZuKurs[iFachwahl] = -1;
		for (const schiene1 of kurs.schienen) {
			this.dynGesperrteSchiene[schiene1 - 1] = false;
		}
		this.dynBewertung -= SchuelerblockungDynDaten.gibKursBewertung(kurs);
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.kursblockung.SchuelerblockungDynDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.kursblockung.SchuelerblockungDynDaten'].includes(name);
	}

	public static readonly class = new Class<SchuelerblockungDynDaten>('de.svws_nrw.core.kursblockung.SchuelerblockungDynDaten');

}

export function cast_de_svws_nrw_core_kursblockung_SchuelerblockungDynDaten(obj: unknown): SchuelerblockungDynDaten {
	return obj as SchuelerblockungDynDaten;
}
