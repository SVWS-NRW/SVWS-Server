import { BKGymAufgabenfeld } from '../../../../../core/types/bk/BKGymAufgabenfeld';
import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelAufgabenfeld extends BKGymAbiturMarkierungsregel {

	/**
	 * das Aufgabenfeld I, II, III
	 */
	readonly aufgabenfeld: string;

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse eines Aufgabenfeldes
	 *
	 * @param aufgabenfeld   die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(aufgabenfeld: string, anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.aufgabenfeld = aufgabenfeld;
		this.anzahl = anzahl;
	}

	/**
	 * Markiert Kurse entsprechend der geforderten Anzahl Kurse für ein Aufgabenfeld
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const feld: BKGymAufgabenfeld | null = BKGymAufgabenfeld.getAufgabenfeldFromKuerzel(this.aufgabenfeld);
		if (feld === null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + this.kuerzel + "spezifiziert ein nicht vorhandenes Aufgabenfeld.")
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && feld.hatFachbezeichnung(variante.varianten.abiturdatenManager.getFaecherManager().getBezeichnungByFachID(markierung.fachID)) };
		const anzBereitsMarkiert: number = variante.zaehleMarkierte(bedingung);
		let verbleibend: number = this.anzahl;
		if (anzBereitsMarkiert > 0) {
			if (anzBereitsMarkiert >= verbleibend) {
				variante.addLogEintrag(1, "Die erforderliche Anzahl an Kursen ist bereits markiert.");
				return;
			}
			variante.addLogEintrag(1, "" + anzBereitsMarkiert + " Kurse sind bereits markiert.");
			verbleibend -= anzBereitsMarkiert;
		}
		verbleibend = variante.markiereKursanzahl(verbleibend, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, this.anzahl, 1);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelAufgabenfeld';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelAufgabenfeld'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelAufgabenfeld>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelAufgabenfeld');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelAufgabenfeld(obj: unknown): BKGymAbiturMarkierungsregelAufgabenfeld {
	return obj as BKGymAbiturMarkierungsregelAufgabenfeld;
}
