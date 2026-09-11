import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { UvFach } from '../../../core/data/uv/UvFach';

export class UvAlgorithmusDynDatenFach extends JavaObject {

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
	 * @param index    Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param uvFach   Das {@link UvFach}.
	 */
	public constructor(index: number, uvFach: UvFach) {
		super();
		this.interneID = index;
		this.uvID = uvFach.id;
	}

	public toString(): string {
		return JavaString.format("UvAlgorithmusDynDatenFach[interneID=%d, uvID=%d]", this.interneID, this.uvID);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenFach'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenFach>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenFach');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenFach(obj: unknown): UvAlgorithmusDynDatenFach {
	return obj as UvAlgorithmusDynDatenFach;
}
