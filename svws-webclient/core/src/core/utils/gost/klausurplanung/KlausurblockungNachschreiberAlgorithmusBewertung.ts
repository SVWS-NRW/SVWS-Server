import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';

export class KlausurblockungNachschreiberAlgorithmusBewertung extends JavaObject {

	/**
	 *  Die Anzahl an Terminen, auf die Nachschreib-Klausuren gelegt wurden.
	 */
	anzahl_termine : number = 0;

	/**
	 *  Die Anzahl an neuen zusätzlichen Terminen, auf die Nachschreib-Klausuren gelegt wurden.
	 */
	anzahl_zusatztermine : number = 0;


	/**
	 * Default-Konstruktor
	 */
	public constructor() {
		super();
	}

	/**
	 * Liefert -1, 0 und +1, wenn dieses Objekt besser, gleich und schlechter als das übergebene Objekt ist.
	 *
	 * @param b  Das übergebene Objekt.
	 *
	 * @return -1, 0 und +1, wenn dieses Objekt besser, gleich und schlechter als das übergebene Objekt ist.
	 */
	public compare(b : KlausurblockungNachschreiberAlgorithmusBewertung) : number {
		if (this.anzahl_zusatztermine < b.anzahl_zusatztermine)
			return -1;
		if (this.anzahl_zusatztermine > b.anzahl_zusatztermine)
			return +1;
		if (this.anzahl_termine < b.anzahl_termine)
			return -1;
		if (this.anzahl_termine > b.anzahl_termine)
			return +1;
		return 0;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmusBewertung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmusBewertung'].includes(name);
	}

	public static class = new Class<KlausurblockungNachschreiberAlgorithmusBewertung>('de.svws_nrw.core.utils.gost.klausurplanung.KlausurblockungNachschreiberAlgorithmusBewertung');

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurblockungNachschreiberAlgorithmusBewertung(obj : unknown) : KlausurblockungNachschreiberAlgorithmusBewertung {
	return obj as KlausurblockungNachschreiberAlgorithmusBewertung;
}
