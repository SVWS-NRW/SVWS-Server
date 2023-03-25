import { KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { SatSolver3 } from '../../core/kursblockung/satsolver/SatSolver3';
import { HashMap } from '../../java/util/HashMap';
import { SatSolverWrapper } from '../../core/kursblockung/satsolver/SatSolverWrapper';
import { LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { Logger } from '../../core/logger/Logger';
import { System } from '../../java/lang/System';
import { Random } from '../../java/util/Random';
import { SatSolverA } from '../../core/kursblockung/satsolver/SatSolverA';
import { NullPointerException } from '../../java/lang/NullPointerException';
import { KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';

export class KursblockungAlgorithmusKSatSolver extends KursblockungAlgorithmusK {

	private readonly kurseAlle : Array<KursblockungDynKurs>;

	private readonly schuelerAlle : Array<KursblockungDynSchueler>;

	private maxNichtWaehler : number = 0;


	/**
	 * Im Konstruktor kann die Klasse die jeweiligen Datenstrukturen aufbauen. Kurse dürfen in diese Methode noch nicht
	 * auf Schienen verteilt werden.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param logger  Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param dynDat  Die dynamischen Blockungsdaten.
	 */
	public constructor(pRandom : Random, logger : Logger, dynDat : KursblockungDynDaten) {
		super(pRandom, logger, dynDat);
		this.kurseAlle = dynDat.gibKurseAlle();
		this.schuelerAlle = this.dynDaten.gibSchuelerArray(false);
		this.maxNichtWaehler = 10;
	}

	public berechne(pMaxTimeMillis : number) : void {
		if (this.dynDaten.gibKurseDieFreiSindAnzahl() === 0) {
			return;
		}
		const timeStart : number = System.currentTimeMillis();
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionZustandSpeichernK();
		let difNichtWaehler : number = 1;
		while (System.currentTimeMillis() - timeStart < pMaxTimeMillis) {
			const result : number = this.berechneSchritt(pMaxTimeMillis);
			if (result === SatSolverA.RESULT_UNKNOWN) {
				return;
			}
			if (result === SatSolverA.RESULT_SATISFIABLE) {
				this.maxNichtWaehler = Math.max(this.maxNichtWaehler - difNichtWaehler, 0);
				continue;
			}
			this.maxNichtWaehler++;
			difNichtWaehler = 0;
		}
	}

	/**
	 * Erzeugt eine Formel in konjunktiver Normalform (CNF) und übergibt sie dem SAT-Solver. Der Solver versucht die
	 * Formel innerhalb des Zeitlimits zu lösen. Falls er es nicht schafft, dann wurde zuvor eine zufällige
	 * Kursverteilung definiert.
	 *
	 * @param pMaxTimeMillis Die maximale Blockungszeit in Millisekunde.
	 *
	 * @return Liefert eines der drei möglichen Ergebnisse {@link SatSolverA#RESULT_SATISFIABLE oder
	 *         SatSolverI#RESULT_UNKNOWN oder SatSolverI#RESULT_UNSATISFIABLE. }
	 */
	public berechneSchritt(pMaxTimeMillis : number) : number {
		const ssw : SatSolverWrapper = new SatSolverWrapper(new SatSolver3(this._random));
		const nSchienen : number = this.dynDaten.gibSchienenAnzahl();
		const mapKursSchiene : HashMap<KursblockungDynKurs, Array<number> | null> = new HashMap();
		for (const kurs of this.kurseAlle) {
			const schienen : Array<number> = Array(nSchienen).fill(0);
			for (let s : number = 0; s < nSchienen; s++) {
				schienen[s] = kurs.gibIstSchieneFixiert(s) ? ssw.getVarTRUE() : kurs.gibIstSchieneGesperrt(s) ? ssw.getVarFALSE() : ssw.createNewVar();
			}
			mapKursSchiene.put(kurs, schienen);
		}
		const mapSchuelerFachartNichtwahl : HashMap<KursblockungDynSchueler, HashMap<KursblockungDynFachart, number>> = new HashMap();
		const mapSchuelerIstInKurs : HashMap<KursblockungDynSchueler, HashMap<KursblockungDynKurs, number>> = new HashMap();
		const listNichtwahlen : LinkedCollection<number> = new LinkedCollection();
		for (const schueler of this.schuelerAlle) {
			mapSchuelerFachartNichtwahl.put(schueler, new HashMap());
			mapSchuelerIstInKurs.put(schueler, new HashMap());
			for (const fachart of schueler.gibFacharten()) {
				const varNichtwahlen : number = ssw.createNewVar();
				const mapFachartNichtwahl : HashMap<KursblockungDynFachart, number> | null = mapSchuelerFachartNichtwahl.get(schueler);
				if (mapFachartNichtwahl === null)
					throw new NullPointerException()
				mapFachartNichtwahl.put(fachart, varNichtwahlen);
				listNichtwahlen.add(varNichtwahlen);
				for (const kurs of fachart.gibKurse()) {
					const varKurs : number = ssw.createNewVar();
					const mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
					if (mapIstInKurs === null)
						throw new NullPointerException()
					mapIstInKurs.put(kurs, varKurs);
				}
			}
		}
		for (const kurs of this.kurseAlle) {
			const list : LinkedCollection<number> = new LinkedCollection();
			const schienen : Array<number> | null = mapKursSchiene.get(kurs);
			if (schienen === null)
				throw new NullPointerException()
			for (let s : number = 0; s < nSchienen; s++) {
				list.add(schienen[s]);
			}
			const amount : number = kurs.gibSchienenAnzahl();
			ssw.c_exactly_GENERIC(list, amount);
		}
		for (const schueler of this.schuelerAlle) {
			for (const fachart of schueler.gibFacharten()) {
				const list : LinkedCollection<number> = new LinkedCollection();
				for (const kurs of fachart.gibKurse()) {
					const mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
					if (mapIstInKurs === null)
						throw new NullPointerException()
					const varKurs : number | null = mapIstInKurs.get(kurs);
					if (varKurs === null)
						throw new NullPointerException()
					list.add(varKurs);
				}
				ssw.c_exactly_GENERIC(list, 1);
			}
		}
		for (const schueler of this.schuelerAlle) {
			const mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
			if (mapIstInKurs === null)
				throw new NullPointerException()
			for (const fachart1 of schueler.gibFacharten()) {
				for (const fachart2 of schueler.gibFacharten()) {
					if (fachart1.gibNr() < fachart2.gibNr()) {
						for (const kurs1 of fachart1.gibKurse()) {
							for (const kurs2 of fachart2.gibKurse()) {
								const var1 : number | null = mapIstInKurs.get(kurs1);
								const var2 : number | null = mapIstInKurs.get(kurs2);
								if ((var1 === null) || (var2 === null))
									throw new NullPointerException()
								const x : number = ssw.c_new_var_AND(var1!, var2!);
								for (let s : number = 0; s < nSchienen; s++) {
									const schienenKurs1 : Array<number> | null = mapKursSchiene.get(kurs1);
									const schienenKurs2 : Array<number> | null = mapKursSchiene.get(kurs2);
									if ((schienenKurs1 === null) || (schienenKurs2 === null))
										throw new NullPointerException()
									const var3 : number = schienenKurs1[s];
									const var4 : number = schienenKurs2[s];
									ssw.c_3(-x, -var3, -var4);
								}
							}
						}
					}
				}
			}
		}
		console.log(JSON.stringify("V=" + ssw.getVarCount() + ", C=" + ssw.getClauseCount()));
		const satresult : number = ssw.solve(pMaxTimeMillis);
		if (satresult !== SatSolverA.RESULT_SATISFIABLE) {
			return satresult;
		}
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		for (const kurs of this.kurseAlle) {
			const schienen : LinkedCollection<number> = new LinkedCollection();
			for (let s : number = 0; s < nSchienen; s++) {
				const schienenKurs : Array<number> | null = mapKursSchiene.get(kurs);
				if (schienenKurs === null)
					throw new NullPointerException()
				if (ssw.isVarTrue(schienenKurs[s])) {
					schienen.add(s);
				}
			}
			kurs.aktionVerteileAufSchienen(schienen);
		}
		return satresult;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.kursblockung.KursblockungAlgorithmusK', 'de.svws_nrw.core.kursblockung.KursblockungAlgorithmusKSatSolver'].includes(name);
	}

}

export function cast_de_svws_nrw_core_kursblockung_KursblockungAlgorithmusKSatSolver(obj : unknown) : KursblockungAlgorithmusKSatSolver {
	return obj as KursblockungAlgorithmusKSatSolver;
}
