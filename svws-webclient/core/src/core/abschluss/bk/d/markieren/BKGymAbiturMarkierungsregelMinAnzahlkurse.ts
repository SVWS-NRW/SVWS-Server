import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelMinAnzahlkurse extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse bis die geforderte Anzahl Kurse insgesamt markiert ist.
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}

	/**
	 * Es werden zusätzlich soviel Kurse markiert, so dass die angegeben Zahl an Kursen erreicht wird.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const anzMarkiert: number = variante.anzahlEingebrachteKurse();
		if (anzMarkiert >= this.anzahl) {
			variante.addLogEintrag(1, "Es sind bereits " + anzMarkiert + " Kurse durch die vorherigen Bedingungen markiert.");
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => true };
		variante.markiereKursanzahl(this.anzahl - anzMarkiert, bedingung);
		variante.addLogAnzahlMarkierungen(this.anzahl - variante.anzahlEingebrachteKurse(), this.anzahl, 1);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlkurse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlkurse'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelMinAnzahlkurse>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlkurse');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelMinAnzahlkurse(obj: unknown): BKGymAbiturMarkierungsregelMinAnzahlkurse {
	return obj as BKGymAbiturMarkierungsregelMinAnzahlkurse;
}
