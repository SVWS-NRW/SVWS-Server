import { BKGymAufgabenfeld } from '../../../../../core/types/bk/BKGymAufgabenfeld';
import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { BKGymFachbelegungManager } from '../../../../../core/abschluss/bk/d/BKGymFachbelegungManager';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import { Class } from '../../../../../java/lang/Class';
import { JavaString } from '../../../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelFachgruppe extends BKGymAbiturMarkierungsregel {

	/**
	 * die Bezeichnung der Fachgruppe
	 */
	readonly fachgruppe: string;

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;

	/**
	 * belegt seit
	 */
	readonly belegtSeit: string | null;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl von Kursen einer Fachgruppe
	 *
	 * @param fachgruppe     die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param belegtSeit     seit welchem GostHalbjahr der Kurs belegt sein muss
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(fachgruppe: string, anzahl: number, belegtSeit: string | null, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.fachgruppe = fachgruppe;
		this.anzahl = anzahl;
		this.belegtSeit = belegtSeit;
	}

	/**
	 * Führt die Markierung entsprechend der Fachgruppe durch.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const gruppe: BKGymAufgabenfeld | null = BKGymAufgabenfeld.getAufgabenfeldFromKuerzel(this.fachgruppe);
		if (gruppe === null)
			throw new DeveloperNotificationException("Die Prüfbedingung spezifiziert eine nicht vorhandene Fachgruppe.")
		const faecher: ArrayList<number> = BKGymAbiturMarkierungsregelFachgruppe.moeglicheFaecherAusFachgruppe(gruppe, this.belegtSeit, variante);
		if (this.pruefeBereitsMarkiert(faecher, variante)) {
			variante.addLogEintrag(1, "Die erforderliche Anzahl an Kursen ist bereits markiert.");
			return;
		}
		const bestesFachID: number | null = this.ermittleBesteFachID(faecher, variante);
		if (bestesFachID === null) {
			variante.addLogAnzahlMarkierungen(this.anzahl, this.anzahl, 1);
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (bestesFachID === markierung.fachID) };
		variante.markiereKursanzahl(this.anzahl, bedingung);
		variante.addLogAnzahlMarkierungen((bestesFachID === -1 ? this.anzahl : 0), this.anzahl, 1);
	}

	/**
	 * Ermittelt aus der Liste der Fächer das Fach, das die meisten Punkte liefert und
	 * gibt dessen ID zurück.
	 *
	 * @param faecher    die Fachbezeichnungen der zu untersuchenden Fächer
	 * @param variante   die Variante
	 *
	 * @return die ID des besten Fachs oder null wenn es keins gibt.
	 */
	private ermittleBesteFachID(faecher: ArrayList<number>, variante: BKGymAbiturMarkierungsVariante): number | null {
		let beste: number | null = null;
		let maxPunktanzahl: number = 0;
		for (const fachID of faecher) {
			const punkte: number = variante.punktsummeFuerFach(fachID, this.anzahl);
			if (punkte > maxPunktanzahl) {
				beste = fachID;
				maxPunktanzahl = punkte;
			}
		}
		return beste;
	}

	/**
	 * Prueft ob die geforderte Anzahl von Markierungen schon vorhanden sind
	 *
	 * @param faecher    die FachIDs, der in Frage kommenden Fächer
	 * @param variante   die Variante
	 *
	 * @return true, wenn Markierungen vorhanden sind, sonst false
	 */
	private pruefeBereitsMarkiert(faecher: ArrayList<number>, variante: BKGymAbiturMarkierungsVariante): boolean {
		for (const fachID of faecher) {
			const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (fachID === markierung.fachID) };
			if (variante.zaehleMarkierte(bedingung) >= this.anzahl)
				return true;
		}
		return false;
	}

	/**
	 * ermittelt die Fachbezeichnungen des Aufgabenfeldes/der Fachgruppe, die ab dem gegebenen Halbjahr in der
	 * Einführungsphase belegt sind oder alle Fächer des Aufgabenfeldes/der Fachgruppe, wenn seitHalbjahr leer ist.
	 *
	 * @param gruppe         das Aufgabenfeld/die Fachgruppe
	 * @param seitHalbjahr   ggfs. das Halbjahr, ab dem belegt sein muss in EF
	 * @param variante       eine Markierungsvariante, auf der gearbeitet wird
	 *
	 * @return die Liste der Fachbezeichnungen
	 */
	private static moeglicheFaecherAusFachgruppe(gruppe: BKGymAufgabenfeld, seitHalbjahr: string | null, variante: BKGymAbiturMarkierungsVariante): ArrayList<number> {
		const fachbelegungManager: BKGymFachbelegungManager = variante.varianten.abiturdatenManager.getFachbelegungManager();
		const result: ArrayList<number> = new ArrayList<number>();
		if ((seitHalbjahr === null) || JavaString.isEmpty(seitHalbjahr)) {
			for (const fach of gruppe.getFaecher()) {
				result.add(fachbelegungManager.getFachIDByBezeichnung(fach));
			}
			return result;
		}
		const hj: GostHalbjahr | null = GostHalbjahr.fromKuerzel(seitHalbjahr);
		if (hj === null)
			throw new DeveloperNotificationException("Die Prüfbedingung enthält ein ungültiges GostHalbjahr '" + seitHalbjahr + "'.")
		const hje: Array<GostHalbjahr> = GostHalbjahr.getHalbjahreAbHalbjahr(hj);
		const leereHje: ArrayList<GostHalbjahr> = new ArrayList<GostHalbjahr>();
		for (const h of hje)
			if (!variante.varianten.abiturdatenManager.istBewertet(h))
				leereHje.add(h);
		if (!leereHje.isEmpty())
			variante.addLogEintrag(1, "HINWEIS: Nicht alle Halbjahre bewertet. Bitte die erforderliche Belegung der Fachgruppe '" + gruppe.name() + "' prüfen!");
		for (const fach of gruppe.getFaecher())
			if (fachbelegungManager.pruefeBelegung(fachbelegungManager.getFachbelegungByBezeichnung(fach), leereHje, ...hje))
				result.add(fachbelegungManager.getFachIDByBezeichnung(fach));
		return result;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFachgruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFachgruppe'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFachgruppe>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFachgruppe');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFachgruppe(obj: unknown): BKGymAbiturMarkierungsregelFachgruppe {
	return obj as BKGymAbiturMarkierungsregelFachgruppe;
}
