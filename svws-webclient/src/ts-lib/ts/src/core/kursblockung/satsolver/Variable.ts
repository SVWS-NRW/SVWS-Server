import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { LinkedCollection, cast_de_nrw_schule_svws_core_adt_collection_LinkedCollection } from '../../../core/adt/collection/LinkedCollection';
import { Clause, cast_de_nrw_schule_svws_core_kursblockung_satsolver_Clause } from '../../../core/kursblockung/satsolver/Clause';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { System, cast_java_lang_System } from '../../../java/lang/System';

export class Variable extends JavaObject {

	/**
	 *  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 *  Die Nummer der Variablen.
	 */
	readonly nr : number;

	/**
	 *  Eine Statistik [sat][free] über Klauseln die nicht definierte Variablen haben und die schon erfüllte Variablen
	 *  haben.
	 */
	readonly statSatFree : Array<Array<number>>;

	/**
	 *  Liste aller Klauseln in denen diese Variable vorkommt.
	 */
	readonly clauses : LinkedCollection<Clause>;

	/**
	 *  Liste aller Variablen, die mit dieser Variable in einer Klausel vorkommen.
	 */
	readonly neighbours : LinkedCollection<Variable>;

	/**
	 *  Der Index der Variablen im Heap. Wenn der Index negativ ist, dann ist die Variable nicht mehr im Heap. Zudem
	 *  bedeutet der Wert -1, dass die Variable SAT ist und der Wert -2 UNSAT. Ein Wert kleiner als -2 ist nur ein
	 *  Dummy-Wert.
	 */
	index : number = 0;

	/**
	 *  Eine Referenz zur negierten Variable.
	 */
	negation : Variable | null = null;


	/**
	 * Konstruktor. Erzeugt eine neue Variable mit einer bestimmten Variablen-Nummer (ungleich 0).
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pNr     Die Nummer der Variablen (ungleich 0).
	 */
	public constructor(pRandom : Random, pNr : number) {
		super();
		this._random = pRandom;
		this.nr = pNr;
		this.statSatFree = [...Array(4)].map(e => Array(4).fill(0));
		this.clauses = new LinkedCollection();
		this.neighbours = new LinkedCollection();
		this.index = -3;
		this.negation = null;
	}

	public toString() : string {
		return "" + this.nr;
	}

	/**
	 * Überprüft, ob diese Variable noch auf TRUE gesetzt werden kann.
	 * 
	 * @return TRUE, falls man diese Variable und einen logischen Widerspruch erfüllen kann.
	 */
	public isUnsat() : boolean {
		if (this.statSatFree[0][0] > 0) {
			console.log(JSON.stringify("FEHLER: Dieser Fall darf gar nicht passieren."));
			return true;
		}
		if ((this.negation !== null) && (this.negation.statSatFree[0][1] > 0)) {
			return true;
		}
		return false;
	}

	/**
	 * Vergleicht die Statistik zweier Variablen und bestimmt, für welche man sich entscheiden sollte.
	 * 
	 * @param b Die Variable, mit der verglichen werden soll.
	 * 
	 * @return TRUE, wenn diese Instanz besser als "b" ist.
	 */
	public isBetterThan(b : Variable) : boolean {
		let statB : Array<Array<number>> = b.statSatFree;
		if (this.statSatFree[0][0] > statB[0][0]) 
			return true;
		if (this.statSatFree[0][0] < statB[0][0]) 
			return false;
		if (this.statSatFree[0][1] > statB[0][1]) 
			return true;
		if (this.statSatFree[0][1] < statB[0][1]) 
			return false;
		if (this.statSatFree[0][2] > statB[0][2]) 
			return true;
		if (this.statSatFree[0][2] < statB[0][2]) 
			return false;
		if (this.statSatFree[0][3] > statB[0][3]) 
			return true;
		if (this.statSatFree[0][3] < statB[0][3]) 
			return false;
		return this._random.nextBoolean();
	}

	/**
	 * Debug-Ausgabe. Nur für Testzwecke.
	 */
	public debug() : void {
		console.log(JSON.stringify("DEBUGGING VAR " + this.nr));
		for (let r : number = 0; r < this.statSatFree.length; r++){
			for (let c : number = 0; c < this.statSatFree[r].length; c++){
				console.log(JSON.stringify(" " + this.statSatFree[r][c]));
			}
			console.log(JSON.stringify("    "));
			if (this.negation !== null) {
				for (let c : number = 0; c < this.negation.statSatFree[r].length; c++){
					console.log(JSON.stringify(" " + this.negation.statSatFree[r][c]));
				}
			}
			console.log();
		}
	}

	/**
	 * Liefert die Anzahl an noch nicht erfüllten Klauseln.
	 * 
	 * @return Die Anzahl an noch nicht erfüllten Klauseln.
	 */
	getClauseOccurences() : number {
		let sum : number = 0;
		for (let free : number = 0; free < 4; free++){
			sum += this.statSatFree[0][free];
		}
		return sum;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.satsolver.Variable'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable(obj : unknown) : Variable {
	return obj as Variable;
}
