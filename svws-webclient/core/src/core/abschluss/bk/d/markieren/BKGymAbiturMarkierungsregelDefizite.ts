import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelDefizite extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl der Kurse
	 */
	readonly anzahlKurse: number;

	/**
	 * die erlaubte Anzahl an Defiziten
	 */
	readonly erlaubteDefizite: number;


	/**
	 * erstellt eine Regel zur Markierung weiterer Kurse um die Defizitregeln zu erfüllen
	 *
	 * @param anzahlKurse        die Art des Kurses
	 * @param erlaubteDefizite   die geforderte Anzahl an Kursen
	 * @param regelkuerzel       das eindeutige Kürzel dieser Regel
	 * @param hinweis            Hinweis für das log
	 * @param bezugAPOBK         Referenz in den APO-BK
	 */
	public constructor(anzahlKurse: number, erlaubteDefizite: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahlKurse = anzahlKurse;
		this.erlaubteDefizite = erlaubteDefizite;
	}

	/**
	 * Markiert weitere Kurse wenn zu viele Defizite vorhanden sind
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.sindDefizitregelnAbgeschlossen()) {
			variante.addLogEintrag(1, "Die Defizitregel wurde bereits erfüllt.");
			return;
		}
		if (variante.getDefizite() < this.erlaubteDefizite) {
			variante.addLogEintrag(1, "Die erlaubte Anzahl von " + this.erlaubteDefizite + " Defiziten wurde nicht überschritten.");
			return;
		}
		if (variante.getDefizite() === this.erlaubteDefizite) {
			variante.setDefizitregelnAbgeschlossen(true);
			if (variante.anzahlEingebrachteKurse() >= this.anzahlKurse) {
				variante.addLogEintrag(1, "Die erlaubte Anzahl von " + this.erlaubteDefizite + " Defiziten wurde nicht überschritten.");
				return;
			}
			const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => true };
			variante.markiereKursanzahl(this.anzahlKurse - variante.anzahlEingebrachteKurse(), bedingung);
		}
		if (variante.getDefizite() > this.erlaubteDefizite) {
			variante.addLogEintrag(1, "Fehler: Die Defizitregel konnte auch nicht durch Markieren weiterer Kurse erfüllt werden.");
			variante.setHatZulassung(false);
		} else {
			variante.addLogEintrag(1, "Die Defizitregel konnte durch Markieren weiterer Kurse erfüllt werden.");
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizite';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizite'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelDefizite>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizite');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelDefizite(obj: unknown): BKGymAbiturMarkierungsregelDefizite {
	return obj as BKGymAbiturMarkierungsregelDefizite;
}
