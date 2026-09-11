import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenUndo extends JavaObject {

	/**
	 * Die Art des Undos.
	 */
	private readonly typ: number;

	/**
	 * Undo-Informationen (abhängig vom Typ).
	 */
	private readonly data: Array<number>;


	/**
	 * Der Konstruktor.
	 *
	 * @param typ    Die Art des Undos.
	 * @param ref1   Eine Referenz (Nr. 1) auf eine Array-Position.
	 * @param ref2   Eine Referenz (Nr. 2) auf eine Array-Position.
	 */
	public constructor(typ: number, ref1: number, ref2: number) {
		super();
		this.typ = typ;
		this.data = [ref1, ref2];
	}

	/**
	 * Liefert den Typ des Undos.
	 *
	 * @return den Typ des Undos.
	 */
	public gibTyp(): number {
		return this.typ;
	}

	/**
	 * Liefert die Referenzen in Form eines Arrays.
	 *
	 * @return die Referenzen in Form eines Arrays.
	 */
	public gibData(): Array<number> {
		return this.data;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenUndo';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenUndo'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenUndo>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenUndo');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenUndo(obj: unknown): UvAlgorithmusDynDatenUndo {
	return obj as UvAlgorithmusDynDatenUndo;
}
