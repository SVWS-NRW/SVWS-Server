import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelMinAnzahlpunkte extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Punktzahl
	 */
	readonly minPunkte: number;

	/**
	 * die maximale Anzahl von Kursen
	 */
	readonly maxKurse: number;


	/**
	 * erstellt eine Regel zur Markierung weitere Kurse zur Erreichung der geforderten
	 * Anzahl an Punkten in Block I.
	 *
	 * @param minPunkte      die geforderte Anzahl an Kursen
	 * @param maxKurse       die maximale Anzahl von Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(minPunkte: number, maxKurse: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.minPunkte = minPunkte;
		this.maxKurse = maxKurse;
	}

	/**
	 * Markiert möglichst so viele Kurse, dass die Mindestpunktzahl für Block I erreicht wird.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.getPunktzahlBlockI() >= this.minPunkte) {
			variante.addLogEintrag(1, "Die Mindestpunktzahl ist bereits erreicht.");
			return;
		}
		const vorherMarkiert: number = variante.anzahlEingebrachteKurse();
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (this.minPunkte < variante.getPunktzahlBlockI()) };
		variante.markiereKursanzahl(this.maxKurse - vorherMarkiert, bedingung);
		if (variante.getPunktzahlBlockI() < this.minPunkte) {
			variante.addLogEintrag(1, "Fehler: Die Mindestpunktzahl konnte auch nicht durch Markieren weiterer Kurse erreicht werden.");
			variante.setHatZulassung(false);
			return;
		}
		variante.addLogEintrag(1, "Durch das Markieren von " + (variante.anzahlEingebrachteKurse() - vorherMarkiert) + "weiteren Kursen konnte die Mindestpunktzahl erreicht werden.");
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlpunkte';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlpunkte'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelMinAnzahlpunkte>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelMinAnzahlpunkte');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelMinAnzahlpunkte(obj: unknown): BKGymAbiturMarkierungsregelMinAnzahlpunkte {
	return obj as BKGymAbiturMarkierungsregelMinAnzahlpunkte;
}
