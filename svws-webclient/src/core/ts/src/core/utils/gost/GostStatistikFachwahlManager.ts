import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { GostStatistikFachwahl } from '../../../core/data/gost/GostStatistikFachwahl';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostStatistikFachwahlHalbjahr } from '../../../core/data/gost/GostStatistikFachwahlHalbjahr';

export class GostStatistikFachwahlManager extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation of " + GostStatistikFachwahlManager.class.getName()! + " not allowed")
	}

	/**
	 * Aktualisiert die Informationen zu einer Fachwahl in dem angegebenen Halbjahr anhand der übergebenen Fachwahl
	 *
	 * @param statfw     die Statistik-Informationen zu den Fachwahlen, bei denen die Informationen hinzugefügt werden sollen
	 * @param halbjahr   das Halbjahr, zu der die Fachwahl gehört
	 * @param fachwahl   die Fachwahl
	 */
	public static setFachwahlHalbjahr(statfw : GostStatistikFachwahl, halbjahr : GostHalbjahr, fachwahl : string | null) : void {
		if (statfw.fachwahlen[halbjahr.id] === null)
			statfw.fachwahlen[halbjahr.id] = new GostStatistikFachwahlHalbjahr();
		if ((fachwahl === null) || !(JavaObject.equalsTranspiler("M", (fachwahl)) || JavaObject.equalsTranspiler("S", (fachwahl)) || JavaObject.equalsTranspiler("ZK", (fachwahl)) || JavaObject.equalsTranspiler("LK", (fachwahl))))
			return;
		statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich += JavaObject.equalsTranspiler("M", (fachwahl)) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenGKSchriftlich += JavaObject.equalsTranspiler("S", (fachwahl)) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenGK += (JavaObject.equalsTranspiler("M", (fachwahl)) || JavaObject.equalsTranspiler("S", (fachwahl))) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenZK += JavaObject.equalsTranspiler("ZK", (fachwahl)) ? 1 : 0;
		statfw.fachwahlen[halbjahr.id].wahlenLK += JavaObject.equalsTranspiler("LK", (fachwahl)) ? 1 : 0;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostStatistikFachwahlManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostStatistikFachwahlManager(obj : unknown) : GostStatistikFachwahlManager {
	return obj as GostStatistikFachwahlManager;
}
