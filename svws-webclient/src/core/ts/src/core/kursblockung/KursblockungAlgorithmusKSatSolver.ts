import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { KursblockungDynFachart, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynFachart } from '../../core/kursblockung/KursblockungDynFachart';
import { SatSolver3, cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolver3 } from '../../core/kursblockung/satsolver/SatSolver3';
import { HashMap, cast_java_util_HashMap } from '../../java/util/HashMap';
import { SatSolverWrapper, cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolverWrapper } from '../../core/kursblockung/satsolver/SatSolverWrapper';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../core/adt/collection/LinkedCollection';
import { KursblockungDynKurs, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynKurs } from '../../core/kursblockung/KursblockungDynKurs';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../../core/logger/Logger';
import { System, cast_java_lang_System } from '../../java/lang/System';
import { JavaInteger, cast_java_lang_Integer } from '../../java/lang/JavaInteger';
import { Random, cast_java_util_Random } from '../../java/util/Random';
import { SatSolverA, cast_de_nrw_schule_svws_core_kursblockung_satsolver_SatSolverA } from '../../core/kursblockung/satsolver/SatSolverA';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../java/lang/NullPointerException';
import { KursblockungDynDaten, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynDaten } from '../../core/kursblockung/KursblockungDynDaten';
import { KursblockungAlgorithmusK, cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusK } from '../../core/kursblockung/KursblockungAlgorithmusK';
import { KursblockungDynSchueler, cast_de_nrw_schule_svws_core_kursblockung_KursblockungDynSchueler } from '../../core/kursblockung/KursblockungDynSchueler';

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
		let timeStart : number = System.currentTimeMillis();
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		this.dynDaten.aktionKurseFreieZufaelligVerteilen();
		this.dynDaten.aktionZustandSpeichernK();
		let difNichtWaehler : number = 1;
		while (System.currentTimeMillis() - timeStart < pMaxTimeMillis) {
			let result : number = this.berechneSchritt(pMaxTimeMillis);
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
		let ssw : SatSolverWrapper = new SatSolverWrapper(new SatSolver3(this._random));
		let nSchienen : number = this.dynDaten.gibSchienenAnzahl();
		let mapKursSchiene : HashMap<KursblockungDynKurs, Array<number> | null> = new HashMap();
		for (let kurs of this.kurseAlle) {
			let schienen : Array<number> = Array(nSchienen).fill(0);
			for (let s : number = 0; s < nSchienen; s++) {
				schienen[s] = kurs.gibIstSchieneFixiert(s) ? ssw.getVarTRUE() : kurs.gibIstSchieneGesperrt(s) ? ssw.getVarFALSE() : ssw.createNewVar();
			}
			mapKursSchiene.put(kurs, schienen);
		}
		let mapSchuelerFachartNichtwahl : HashMap<KursblockungDynSchueler, HashMap<KursblockungDynFachart, number>> = new HashMap();
		let mapSchuelerIstInKurs : HashMap<KursblockungDynSchueler, HashMap<KursblockungDynKurs, number>> = new HashMap();
		let listNichtwahlen : LinkedCollection<number> = new LinkedCollection();
		for (let schueler of this.schuelerAlle) {
			mapSchuelerFachartNichtwahl.put(schueler, new HashMap());
			mapSchuelerIstInKurs.put(schueler, new HashMap());
			for (let fachart of schueler.gibFacharten()) {
				let varNichtwahlen : number = ssw.createNewVar();
				let mapFachartNichtwahl : HashMap<KursblockungDynFachart, number> | null = mapSchuelerFachartNichtwahl.get(schueler);
				if (mapFachartNichtwahl === null)
					throw new NullPointerException()
				mapFachartNichtwahl.put(fachart, varNichtwahlen);
				listNichtwahlen.add(varNichtwahlen);
				for (let kurs of fachart.gibKurse()) {
					let varKurs : number = ssw.createNewVar();
					let mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
					if (mapIstInKurs === null)
						throw new NullPointerException()
					mapIstInKurs.put(kurs, varKurs);
				}
			}
		}
		for (let kurs of this.kurseAlle) {
			let list : LinkedCollection<number> = new LinkedCollection();
			let schienen : Array<number> | null = mapKursSchiene.get(kurs);
			if (schienen === null)
				throw new NullPointerException()
			for (let s : number = 0; s < nSchienen; s++) {
				list.add(schienen[s]);
			}
			let amount : number = kurs.gibSchienenAnzahl();
			ssw.c_exactly_GENERIC(list, amount);
		}
		for (let schueler of this.schuelerAlle) {
			for (let fachart of schueler.gibFacharten()) {
				let list : LinkedCollection<number> = new LinkedCollection();
				for (let kurs of fachart.gibKurse()) {
					let mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
					if (mapIstInKurs === null)
						throw new NullPointerException()
					let varKurs : number | null = mapIstInKurs.get(kurs);
					if (varKurs === null)
						throw new NullPointerException()
					list.add(varKurs);
				}
				ssw.c_exactly_GENERIC(list, 1);
			}
		}
		for (let schueler of this.schuelerAlle) {
			let mapIstInKurs : HashMap<KursblockungDynKurs, number> | null = mapSchuelerIstInKurs.get(schueler);
			if (mapIstInKurs === null)
				throw new NullPointerException()
			for (let fachart1 of schueler.gibFacharten()) {
				for (let fachart2 of schueler.gibFacharten()) {
					if (fachart1.gibNr() < fachart2.gibNr()) {
						for (let kurs1 of fachart1.gibKurse()) {
							for (let kurs2 of fachart2.gibKurse()) {
								let var1 : number | null = mapIstInKurs.get(kurs1);
								let var2 : number | null = mapIstInKurs.get(kurs2);
								if ((var1 === null) || (var2 === null))
									throw new NullPointerException()
								let x : number = ssw.c_new_var_AND(var1!, var2!);
								for (let s : number = 0; s < nSchienen; s++) {
									let schienenKurs1 : Array<number> | null = mapKursSchiene.get(kurs1);
									let schienenKurs2 : Array<number> | null = mapKursSchiene.get(kurs2);
									if ((schienenKurs1 === null) || (schienenKurs2 === null))
										throw new NullPointerException()
									let var3 : number = schienenKurs1[s];
									let var4 : number = schienenKurs2[s];
									ssw.c_3(-x, -var3, -var4);
								}
							}
						}
					}
				}
			}
		}
		console.log(JSON.stringify("V=" + ssw.getVarCount() + ", C=" + ssw.getClauseCount()));
		let satresult : number = ssw.solve(pMaxTimeMillis);
		if (satresult !== SatSolverA.RESULT_SATISFIABLE) {
			return satresult;
		}
		this.dynDaten.aktionSchuelerAusAllenKursenEntfernen();
		for (let kurs of this.kurseAlle) {
			let schienen : LinkedCollection<number> = new LinkedCollection();
			for (let s : number = 0; s < nSchienen; s++) {
				let schienenKurs : Array<number> | null = mapKursSchiene.get(kurs);
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
		return ['de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusKSatSolver', 'de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmusK'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_KursblockungAlgorithmusKSatSolver(obj : unknown) : KursblockungAlgorithmusKSatSolver {
	return obj as KursblockungAlgorithmusKSatSolver;
}
