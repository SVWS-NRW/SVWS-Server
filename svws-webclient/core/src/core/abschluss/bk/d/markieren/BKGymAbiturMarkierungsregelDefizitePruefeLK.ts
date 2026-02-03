import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelDefizitePruefeLK extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl
	 */
	readonly erlaubteDefizite: number;


	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param anzahl         die erlaubte Anzahl an Defiziten
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.erlaubteDefizite = anzahl;
	}

	/**
	 * Prüft, dass nicht mehr als die erlaubte Anzahl an Defiziten im LK-Bereich vorliegen
	 *
	 * Gegebenenfalls wird der Erfolg der Variante auf false gesetzt.
	 * § 15 Abs. 2 Nr. 4 : Schülerinnen und Schüler, die in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben, dürfen zum Erwerb der allgemeinen Hochschulreife in keinem der vier in der Qualifikationsphase
	 * belegten Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache mit null Punkten bewertet worden sein.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const anzahlLKDefizite: number = this.anzahlDefizite("LK1", variante) + this.anzahlDefizite("LK2", variante);
		if (anzahlLKDefizite <= this.erlaubteDefizite)
			variante.addLogEintrag(1, "Defizite im LK-Bereich: Erlaubt=" + this.erlaubteDefizite + " Ist=" + anzahlLKDefizite + ".");
		else {
			variante.addLogEintrag(1, "Fehler: Es sind mehr als die " + this.erlaubteDefizite + " erlaubten Defizite im LK-Bereich vorhanden.");
			variante.setHatZulassung(false);
		}
	}

	/**
	 * Diese Methode zählt die Anzahl der Defizite für die angegebene Kursart von markierten Kursen
	 *
	 * @param kursart    die Kursart z.B. LK1
	 * @param variante   die Variante
	 *
	 * @return die Anzahl der Defizite
	 */
	private anzahlDefizite(kursart: string, variante: BKGymAbiturMarkierungsVariante): number {
		const abifach: GostAbiturFach | null = GostAbiturFach.fromKuerzel(kursart);
		if (abifach === null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + this.kuerzel + " enthält die unzulässige Kursart '" + kursart + "'.")
		const abiFachID: number | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(abifach);
		if (abiFachID === null)
			return 0;
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => ((markierung !== null) && (markierung.fachID === abiFachID) && (markierung.punkte !== null) && (markierung.punkte < 5)) };
		return variante.zaehleMarkierte(bedingung);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizitePruefeLK';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizitePruefeLK', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelDefizitePruefeLK>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelDefizitePruefeLK');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelDefizitePruefeLK(obj: unknown): BKGymAbiturMarkierungsregelDefizitePruefeLK {
	return obj as BKGymAbiturMarkierungsregelDefizitePruefeLK;
}
