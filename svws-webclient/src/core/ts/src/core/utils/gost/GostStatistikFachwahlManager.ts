import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { GostStatistikFachwahl, cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahl } from '../../../core/data/gost/GostStatistikFachwahl';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { GostStatistikFachwahlHalbjahr, cast_de_nrw_schule_svws_core_data_gost_GostStatistikFachwahlHalbjahr } from '../../../core/data/gost/GostStatistikFachwahlHalbjahr';

export class GostStatistikFachwahlManager extends JavaObject {


	public constructor() {
		super();
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
		return ['de.nrw.schule.svws.core.utils.gost.GostStatistikFachwahlManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_gost_GostStatistikFachwahlManager(obj : unknown) : GostStatistikFachwahlManager {
	return obj as GostStatistikFachwahlManager;
}
