import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { SchulformKatalogEintrag } from '../../../asd/data/schule/SchulformKatalogEintrag';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { Schulgliederung } from '../../../asd/types/schule/Schulgliederung';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { JahrgaengeUtils } from '../../../core/utils/kataloge/jahrgaenge/JahrgaengeUtils';
import { GostJahrgang } from '../../../core/data/gost/GostJahrgang';
import type { Comparator } from '../../../java/util/Comparator';

export class GostAbiturjahrUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Abiturjahrgängen in Abiturjahrgangslisten.
	 */
	public static readonly comparator: Comparator<GostJahrgang> = { compare: (a: GostJahrgang, b: GostJahrgang) => {
		const cmp: number = a.abiturjahr - b.abiturjahr;
		if (cmp !== 0) {
			return cmp;
		}
		if ((a.jahrgang === null) || (b.jahrgang === null)) {
			if ((a.jahrgang === null) && (b.jahrgang === null)) {
				return 0;
			}
			return (a.jahrgang === null) ? -1 : 1;
		}
		return JavaString.compareTo(a.jahrgang, b.jahrgang);
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Bestimmt für das übergebene Schuljahr eines Schülers das Kalenderjahr, in welchem ein Schüler
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
	public static getGostAbiturjahr(schulform: Schulform, gliederung: Schulgliederung, aktuellesSchuljahr: number, jahrgang: string): number | null {
		const sfke: SchulformKatalogEintrag | null = schulform.daten(aktuellesSchuljahr);
		if ((sfke === null) || (!sfke.hatGymOb)) {
			return null;
		}
		let restjahre: number | null = JahrgaengeUtils.getRestlicheJahre(schulform, gliederung, jahrgang);
		if (restjahre === null) {
			return null;
		}
		if ((schulform as unknown !== Schulform.GY as unknown) && (!JahrgaengeUtils.istGymOb(jahrgang))) {
			restjahre += 3;
		}
		return aktuellesSchuljahr + restjahre;
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
	public static getGostAbiturjahrJahrgang(schulform: Schulform, gliederung: Schulgliederung, schuljahr: number, abiturjahr: number): string | null {
		const sfke: SchulformKatalogEintrag | null = schulform.daten(schuljahr);
		if ((sfke === null) || (!sfke.hatGymOb)) {
			return null;
		}
		const restlicheJahre: number = abiturjahr - schuljahr;
		if (restlicheJahre <= 1) {
			return "Q2";
		}
		if (restlicheJahre === 2) {
			return "Q1";
		}
		if (restlicheJahre === 3) {
			return "EF";
		}
		const sekIJahre: number = (gliederung.istG8() || ((schulform as unknown === Schulform.GY as unknown) && (gliederung as unknown === Schulgliederung.DEFAULT as unknown))) ? 9 : 10;
		if (restlicheJahre >= sekIJahre) {
			return null;
		}
		let strJG: string | null = "" + (sekIJahre - (restlicheJahre - 4));
		if (strJG.length === 1) {
			strJG = "0" + strJG;
		}
		return strJG;
	}

	/**
	 * Bestimmt das Halbjahr, mit dem ein Schüler bei einer Wiederholung im neuen Jahrgang einsteigt. Das ist das Halbjahr,
	 * das auf den Schuljahresabschnitt folgt, in dem die Wiederholung angelegt wird.
	 *
	 * @param abiturjahr   das Abiturjahr des Jahrgangs der Wiederholung
	 * @param schuljahr    das Schuljahr des Schuljahresabschnitts, in dem die Wiederholung angelegt wird
	 * @param abschnitt    der Abschnitt (das Halbjahr) des Schuljahresabschnitts, in dem die Wiederholung angelegt wird
	 *
	 * @return das Halbjahr im neuen Jahrgang oder null, wenn dieser im Folgeabschnitt nicht in der gymnasialen Oberstufe ist
	 */
	public static getWiederholungHalbjahr(abiturjahr: number, schuljahr: number, abschnitt: number): GostHalbjahr | null {
		const neuesSchuljahr: boolean = (abschnitt >= 2);
		return GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, neuesSchuljahr ? (schuljahr + 1) : schuljahr, neuesSchuljahr ? 1 : 2);
	}

	/**
	 * Prüft, ob ein Schuljahresabschnitt vor dem Halbjahr liegt, mit dem ein Schüler bei einer Wiederholung im neuen Jahrgang einsteigt.
	 * Für solche Abschnitte gehört der Schüler noch zum Jahrgang seines Lernabschnitts, ab dem Einstieg zum neuen Jahrgang.
	 *
	 * @param wiederholungAbiturjahr   das Abiturjahr des Jahrgangs der Wiederholung
	 * @param wiederholungHalbjahr     das Halbjahr, mit dem der Schüler im neuen Jahrgang einsteigt
	 * @param schuljahr                das Schuljahr des zu prüfenden Abschnitts
	 * @param abschnitt                der zu prüfende Abschnitt (das Halbjahr) im Schuljahr
	 *
	 * @return true, wenn der Abschnitt vor dem Einstieg liegt
	 */
	public static liegtVorWiederholung(wiederholungAbiturjahr: number, wiederholungHalbjahr: GostHalbjahr, schuljahr: number, abschnitt: number): boolean {
		const schuljahrWiederholung: number = wiederholungHalbjahr.getSchuljahrFromAbiturjahr(wiederholungAbiturjahr);
		return (schuljahr < schuljahrWiederholung) || ((schuljahr === schuljahrWiederholung) && (abschnitt < wiederholungHalbjahr.halbjahr));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostAbiturjahrUtils';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostAbiturjahrUtils'].includes(name);
	}

	public static readonly class = new Class<GostAbiturjahrUtils>('de.svws_nrw.core.utils.gost.GostAbiturjahrUtils');

}

export function cast_de_svws_nrw_core_utils_gost_GostAbiturjahrUtils(obj: unknown): GostAbiturjahrUtils {
	return obj as GostAbiturjahrUtils;
}
