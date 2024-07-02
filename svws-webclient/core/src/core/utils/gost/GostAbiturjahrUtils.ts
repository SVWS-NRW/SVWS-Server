import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { JahrgangsUtils } from '../../../core/utils/jahrgang/JahrgangsUtils';
import { Schulform } from '../../../core/types/schule/Schulform';
import { Schulgliederung } from '../../../core/types/schule/Schulgliederung';
import { JavaString } from '../../../java/lang/JavaString';
import { GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import type { Comparator } from '../../../java/util/Comparator';

export class GostAbiturjahrUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Abiturjahrgängen in Abiturjahrgangslisten.
	 */
	public static readonly comparator : Comparator<GostJahrgang> = { compare : (a: GostJahrgang, b: GostJahrgang) => {
		const cmp : number = a.abiturjahr - b.abiturjahr;
		if (cmp !== 0)
			return cmp;
		if ((a.jahrgang === null) || (b.jahrgang === null)) {
			if ((a.jahrgang === null) && (b.jahrgang === null))
				return 0;
			return (a.jahrgang === null) ? -1 : 1;
		}
		return JavaString.compareTo(a.jahrgang, b.jahrgang);
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Bestimmt für das übergegebene Schuljahr eines Schülers das Kalenderjahr, in welchem ein Schüler
	 * der gymnasialen Oberstufe Abitur gemacht hat, macht oder machen wird. Hierbei wird für die
	 * Bestimmung die Schulgliederung und Schulform bei dem Schüler sowie das aktuelle Schuljahr und der
	 * Statistik-Jahrgang, in welchem sich der Schüler befindet, benötigt.
	 *
	 * @param schulform            die Schulform
	 * @param gliederung           die Schulgliederung des Schülers
	 * @param aktuellesSchuljahr   das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 * @param jahrgang             der Statistik-Jahrgang des Schülers
	 *
	 * @return das Kalenderjahr des Abiturs oder null, falls das Jahr des Abiturs nicht bestimmt werden kann.
	 */
	public static getGostAbiturjahr(schulform : Schulform, gliederung : Schulgliederung, aktuellesSchuljahr : number, jahrgang : string) : number | null {
		if ((schulform.daten === null) || (!schulform.daten.hatGymOb))
			return null;
		let restjahre : number | null = JahrgangsUtils.getRestlicheJahre(schulform, gliederung, jahrgang);
		if (restjahre === null)
			return null;
		if ((schulform as unknown !== Schulform.GY as unknown) && (!JahrgangsUtils.istGymOb(jahrgang)))
			restjahre += 3;
		return aktuellesSchuljahr + restjahre!;
	}

	/**
	 * Bestimmt für das angegebene Abiturjahr, das angegebene aktuelles Schuljahr, in dem sich die
	 * Schule befindet, und der angegebenen Schulform und Schulgliederung den zugrhörigen Statistik-Jahrgang
	 * einer Schule mit gymnasialer Oberstufe.
	 *
	 * @param schulform     die Schulform
	 * @param gliederung    die Schulgliederung
	 * @param schuljahr     das Schuljahr, in dem sich die Schule befindet
	 * @param abiturjahr    das Abiturjahr, für welches der Statistik-Jahrgang ermittelt werden soll.
	 *
	 * @return der Statistik-Jahrgang zu dem angegeben Abiturjahrgang
	 */
	public static getGostAbiturjahrJahrgang(schulform : Schulform, gliederung : Schulgliederung, schuljahr : number, abiturjahr : number) : string | null {
		if ((schulform.daten === null) || (!schulform.daten.hatGymOb))
			return null;
		const restlicheJahre : number = abiturjahr - schuljahr;
		if (restlicheJahre <= 1)
			return "Q2";
		if (restlicheJahre === 2)
			return "Q1";
		if (restlicheJahre === 3)
			return "EF";
		const sekIJahre : number = (gliederung.istG8() || ((schulform as unknown === Schulform.GY as unknown) && (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 9 : 10;
		if (restlicheJahre >= sekIJahre)
			return null;
		let strJG : string | null = "" + (sekIJahre - (restlicheJahre - 4));
		if (strJG.length === 1)
			strJG = "0" + strJG!;
		return strJG;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostAbiturjahrUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostAbiturjahrUtils'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_GostAbiturjahrUtils(obj : unknown) : GostAbiturjahrUtils {
	return obj as GostAbiturjahrUtils;
}
