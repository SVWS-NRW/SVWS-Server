import { JavaObject } from '../../../java/lang/JavaObject';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';

export class UvAlgorithmusDynDatenJahrgang extends JavaObject {

	/**
	 * Der Index im internen Array.
	 */
	readonly interneID: number;

	/**
	 * Die externe ID der DB/GUI.
	 */
	readonly uvID: number;


	/**
	 * Der Konstruktor.
	 *
	 * @param index        Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvJahrgang   Die {@link JahrgangsDaten}.
	 */
	public constructor(index: number, uvJahrgang: JahrgangsDaten) {
		super();
		this.interneID = index;
		this.uvID = uvJahrgang.id;
	}

	public toString(): string {
		return JavaString.format("UvAlgorithmusDynJahrgang[interneID=%d, uvID=%d]", this.interneID, this.uvID);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenJahrgang';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenJahrgang'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenJahrgang>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenJahrgang');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenJahrgang(obj: unknown): UvAlgorithmusDynDatenJahrgang {
	return obj as UvAlgorithmusDynDatenJahrgang;
}
