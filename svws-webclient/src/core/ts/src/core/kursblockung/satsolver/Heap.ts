import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { Variable, cast_de_nrw_schule_svws_core_kursblockung_satsolver_Variable } from '../../../core/kursblockung/satsolver/Variable';
import { Random, cast_java_util_Random } from '../../../java/util/Random';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Arrays, cast_java_util_Arrays } from '../../../java/util/Arrays';
import { System, cast_java_lang_System } from '../../../java/lang/System';

export class Heap extends JavaObject {

	/**
	 *  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected readonly _random : Random;

	/**
	 *  Das Array beinhaltet die Daten und wird als binärer Baum interpretiert. Das linke Kind von Index i ist der Index
	 *  2*i+1 und das rechte Kind ist der Index 2*i+2.
	 */
	private _data : Array<Variable>;

	/**
	 *  Die wirkliche Anzahl an Elementen im Array {@link #_data}.
	 */
	private _size : number = 0;


	/**
	 * Konstruktor.
	 * 
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	constructor(pRandom : Random) {
		super();
		this._random = pRandom;
		this._data = Array(1).fill(null);
		this._size = 0;
	}

	public toString() : string {
		return "Heap: " + Arrays.toString(Arrays.copyOf(this._data, this._size))!;
	}

	/**
	 * Überprüft, ob der Heap leer ist.
	 * 
	 * @return TRUE, wenn der Heap leer ist.
	 */
	isEmpty() : boolean {
		return this._size === 0;
	}

	/**
	 * Liefert das oberste (beste) Element des Heaps mit hoher Wahrscheinlichkeit.
	 * 
	 * @return Liefert das oberste (beste) Element des Heaps mit hoher Wahrscheinlichkeit.
	 */
	top() : Variable {
		let index : number = 0;
		while ((this._random.nextDouble() < 0.1) && (index + 1 < this._size)) {
			index++;
		}
		return this._data[index];
	}

	/**
	 * Liefert die Anzahl an Elementen im Heap.
	 * 
	 * @return Die Anzahl an Elementen im Heap.
	 */
	size() : number {
		return this._size;
	}

	/**
	 * Fügt die Variable "var" dem Heap hinzu. Nach dem Einfügen kennt die Variable mit {@link Variable#index} ihre
	 * eigene Position in diesem Heap.
	 * 
	 * @param pVar Die einzufügende Variable.
	 */
	insert(pVar : Variable) : void {
		if (this._size === this._data.length) {
			this._data = Arrays.copyOf(this._data, 2 * this._size);
		}
		let insertI : number = this._size;
		while (insertI > 0) {
			let parentI : number = Math.trunc((insertI - 1) / 2);
			let parentV : Variable = this._data[parentI];
			if (pVar.isBetterThan(parentV)) {
				this._data[insertI] = parentV;
				parentV.index = insertI;
				insertI = parentI;
			} else {
				break;
			}
		}
		this._data[insertI] = pVar;
		pVar.index = insertI;
		this._size++;
	}

	/**
	 * Entfernt die Variable pVar vom Heap. Dabei wird zunächst mit Hilfe von {@link Variable#index} die Position
	 * bestimmt. Anschließend wird der Heap so transformiert, dass die Variable entfernt werden kann.
	 * 
	 * @param pVar Die zu entfernende Variable.
	 */
	remove(pVar : Variable) : void {
		this._size--;
		let lastV : Variable = this._data[this._size];
		if (lastV as unknown === pVar as unknown) {
			return;
		}
		let currentI : number = pVar.index;
		if (this._data[pVar.index] as unknown !== pVar as unknown) {
			console.log(JSON.stringify("FEHLER: Die Variable " + pVar + " ist nicht beim Index " + pVar.index + "!"));
		}
		while (currentI > 0) {
			let parentI : number = Math.trunc((currentI - 1) / 2);
			this._data[currentI] = this._data[parentI];
			this._data[currentI].index = currentI;
			currentI = parentI;
		}
		let parentI : number = 0;
		let childI : number = 1;
		while (childI < this._size) {
			if (childI + 1 < this._size) {
				if (this._data[childI + 1].isBetterThan(this._data[childI])) {
					childI = childI + 1;
				}
			}
			let child : Variable = this._data[childI];
			if (child.isBetterThan(lastV)) {
				this._data[parentI] = child;
				child.index = parentI;
				parentI = childI;
				childI = parentI * 2 + 1;
			} else {
				break;
			}
		}
		this._data[parentI] = lastV;
		lastV.index = parentI;
	}

	/**
	 * Falls sich die Variable pVar im Heap befindet, wird sie entfernt und direkt wieder hinzugefügt. Diese Methode
	 * muss aufgerufen werden, sobald die Variable eine neue Bewertung erhalten hat.
	 * 
	 * @param pVar Die Variable, deren Bewertung sich geändert hat.
	 */
	public update(pVar : Variable) : void {
		if (pVar.index >= 0) {
			this.remove(pVar);
			if (pVar.negation !== null) 
				this.remove(pVar.negation);
			this.insert(pVar);
			if (pVar.negation !== null) 
				this.insert(pVar.negation);
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.kursblockung.satsolver.Heap'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_kursblockung_satsolver_Heap(obj : unknown) : Heap {
	return obj as Heap;
}
