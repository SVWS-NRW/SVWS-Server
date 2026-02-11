import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelMaxAnzahlkurse extends BKGymAbiturMarkierungsregel {

	/**
	 * die maximal erlaubte Anzahl an Kursen
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse, wenn sich das Ergebnis in Block I verbessert.
	 *
	 * @param anzahl         die maximale Anzahl an Kursen
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, varKennung: string | null, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}

	/**
	 * Markiert bis zur angegebenen Anzahl Kurse, solange sich das Ergebnis verbessert.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const vorherMarkiert: number = variante.anzahlEingebrachteKurse();
		if (vorherMarkiert > this.anzahl)
			throw new DeveloperNotificationException("Es wurden mehr Kurse markiert als maximal erlaubt ist.")
		if (vorherMarkiert === this.anzahl) {
			variante.addLogEintrag(1, "Es sind bereits " + vorherMarkiert + " Kurse durch die vorherigen Bedingungen markiert.");
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (markierung.punkte !== null) && (variante.getDurchschnitt() < markierung.punkte) };
		variante.markiereKursanzahl(this.anzahl - vorherMarkiert, bedingung);
		const jetztMarkiert: number = variante.anzahlEingebrachteKurse();
		if (vorherMarkiert < jetztMarkiert)
			variante.addLogEintrag(1, "Es konnten " + (jetztMarkiert - vorherMarkiert) + " weitere Kurse zur Verbesserung markiert werden.");
		else
			variante.addLogEintrag(1, "Weitere Kurse wurden nicht markiert, da dadurch keine Verbesserung erreicht wird.");
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMaxAnzahlkurse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMaxAnzahlkurse', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelMaxAnzahlkurse>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMaxAnzahlkurse');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelMaxAnzahlkurse(obj: unknown): BKGymAbiturMarkierungsregelMaxAnzahlkurse {
	return obj as BKGymAbiturMarkierungsregelMaxAnzahlkurse;
}
