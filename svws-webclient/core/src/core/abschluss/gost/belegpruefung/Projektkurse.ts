import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { Fach } from '../../../../asd/types/fach/Fach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostFachUtils } from '../../../../core/utils/gost/GostFachUtils';
import { ArrayList } from '../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr } from '../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { DeveloperNotificationException } from '../../../../core/exceptions/DeveloperNotificationException';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { Class } from '../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';
import { HashSet } from '../../../../java/util/HashSet';

export class Projektkurse extends GostBelegpruefung {

	private readonly projektkursBelegung : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	private projektkurs : AbiturFachbelegung | null = null;

	private readonly projektkursHalbjahre : ArrayList<GostHalbjahr> = new ArrayList<GostHalbjahr>();


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this.projektkurs = null;
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			if (this.manager.zaehleBelegung(fachbelegung) <= 0)
				continue;
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if ((fach !== null) && GostFachUtils.istProjektkurs(fach)) {
				this.projektkursBelegung.add(fachbelegung);
			}
		}
	}

	protected pruefeEF1() : void {
		this.pruefeBelegungEF();
	}

	protected pruefeGesamt() : void {
		this.pruefeBelegungEF();
		this.pruefeAufAnrechenbarenProjektkurs();
		this.pruefeBelegungHalbjahre();
		this.pruefeBelegungLeitfaecher();
		if (this.manager.istProjektKursBesondereLernleistung())
			this.addFehler((this.projektkurs !== null) ? GostBelegungsfehler.PF_16_INFO : GostBelegungsfehler.PF_15);
	}

	/**
	 * Prüft, ob ein Projektfach in der EF belegt wurde. Eine solche Belegung ist nicht zulässig.
	 */
	private pruefeBelegungEF() : void {
		for (const fachbelegung of this.projektkursBelegung) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if (belegungHalbjahr === null)
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if ((halbjahr as unknown === GostHalbjahr.EF1 as unknown) || (halbjahr as unknown === GostHalbjahr.EF2 as unknown))
					this.addFehler(GostBelegungsfehler.PF_10);
			}
		}
	}

	/**
	 * Prüft, ob ein anrechenbarer Projektkurs unter den belegten Projektfächern existiert. Es darf aber
	 * auch nur genau ein anrechenbarer Projektkurs existieren!
	 */
	private pruefeAufAnrechenbarenProjektkurs() : void {
		for (const fachbelegung of this.projektkursBelegung) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if (belegungHalbjahr === null)
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				if ((halbjahr as unknown === GostHalbjahr.EF1 as unknown) || (halbjahr as unknown === GostHalbjahr.EF2 as unknown))
					continue;
				const nextHalbjahr : GostHalbjahr | null = halbjahr.next();
				if (nextHalbjahr === null) {
					this.addFehler(GostBelegungsfehler.PF_18);
					continue;
				} else
					if (!this.manager.pruefeBelegung(fachbelegung, nextHalbjahr)) {
						this.addFehler(GostBelegungsfehler.PF_17_INFO);
						continue;
					}
				if (this.projektkurs !== null) {
					this.addFehler(GostBelegungsfehler.PF_14);
					break;
				}
				this.projektkurs = fachbelegung;
				this.projektkursHalbjahre.add(halbjahr);
				this.projektkursHalbjahre.add(nextHalbjahr);
				break;
			}
		}
	}

	/**
	 * Prüfe die Halbjahresbelegungen der belegten Projektfächer. Hierbei Darf
	 * es zu Einzelbelegungen neben dem anrechenbaren Projektkurs kommen. Diese dürfen
	 * aber nur vor der Belegung des anrechenbaren Kurses liegen. Außerdem dürfen in
	 * einem Halbjahr nicht mehrere Projektfächer belegt sein.
	 */
	private pruefeBelegungHalbjahre() : void {
		const pjkHalbjahre : HashSet<GostHalbjahr> = new HashSet<GostHalbjahr>();
		for (const fachbelegung of this.projektkursBelegung) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if (belegungHalbjahr === null)
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				if ((halbjahr as unknown === GostHalbjahr.EF1 as unknown) || (halbjahr as unknown === GostHalbjahr.EF2 as unknown))
					continue;
				if (!pjkHalbjahre.add(halbjahr)) {
					this.addFehler(GostBelegungsfehler.PF_14);
					continue;
				}
				if ((this.projektkurs !== null) && JavaObject.equalsTranspiler(this.projektkurs, (fachbelegung)) && (this.projektkursHalbjahre !== null) && this.projektkursHalbjahre.contains(halbjahr))
					continue;
				const nextHalbjahr : GostHalbjahr | null = halbjahr.next();
				if ((nextHalbjahr !== null) && (GostFachUtils.istWaehlbar(this.manager.getFach(fachbelegung), nextHalbjahr)) && ((this.projektkurs === null) || (this.projektkursHalbjahre === null) || (halbjahr.compareTo(this.projektkursHalbjahre.get(0)) < 0)))
					continue;
				this.addFehler(GostBelegungsfehler.PF_14);
			}
		}
	}

	/**
	 * Prüft die Belegung der Leitfächer
	 */
	private pruefeBelegungLeitfaecher() : void {
		for (const fachbelegung of this.projektkursBelegung) {
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if (fach === null)
				continue;
			const leitfach1 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			const leitfach2 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if ((leitfach1 !== null) && this.pruefeBelegungLeitfachbelegung(fachbelegung, leitfach1)) {
				const lf : GostFach | null = this.manager.getFach(leitfach1);
				if (lf === null)
					throw new DeveloperNotificationException("Interner Fehler: Das Leitfach mit der angegebenen ID existiert nicht als Fach der gymnasialen Oberstufe in diesem Jahrgang.")
				const zf : Fach | null = Fach.getBySchluesselOrDefault(lf.kuerzel);
				if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown)))
					this.addFehler(GostBelegungsfehler.PF_19);
				continue;
			}
			if ((leitfach2 !== null) && this.pruefeBelegungLeitfachbelegung(fachbelegung, leitfach2)) {
				const lf : GostFach | null = this.manager.getFach(leitfach2);
				if (lf === null) {
					this.addFehler(GostBelegungsfehler.PF_25);
					continue;
				}
				const zf : Fach | null = Fach.getBySchluesselOrDefault(lf.kuerzel);
				if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown)))
					this.addFehler(GostBelegungsfehler.PF_19);
				continue;
			}
			this.addFehler(GostBelegungsfehler.PF_13);
		}
	}

	/**
	 * Prüft, ob das Leitfach in Bezug auf die Belegung des Projektfaches die korrekten Halbjahresbelegungen hat.
	 *
	 * @param projektkurs   die Fachbelegungen des Projektfaches
	 * @param leitfach      die Fachbelegungen des Leitfaches
	 * @param halbjahr1     das erste Halbjahr der Belegung des Projektfaches (das zweite ist das nachfolgende)
	 *
	 * @return true, falls das Leitfach eine geeigneten Belegung aufweist, sonst false
	 */
	private pruefeBelegungLeitfachbelegungNormal(projektkurs : AbiturFachbelegung, leitfach : AbiturFachbelegung, halbjahr1 : GostHalbjahr) : boolean {
		if (halbjahr1 as unknown === GostHalbjahr.Q22 as unknown)
			return false;
		if (!this.manager.pruefeBelegung(projektkurs, halbjahr1, halbjahr1.nextOrException()))
			return false;
		for (let hj : GostHalbjahr = halbjahr1; hj.istQualifikationsphase(); hj = hj.previousOrException())
			if (this.manager.pruefeBelegung(leitfach, hj, hj.nextOrException()))
				return true;
		return false;
	}

	/**
	 * Prüft, ob das Leitfach in Bezug auf die Belegung des Projektfaches die korrekten Halbjahresbelegungen hat.
	 * In dieser Variante wird nur die Belegung eines Projektfaches für ein Halbjahr geprüft. Dies kann z.B.
	 * der Fall sein, wenn ein Projektkurs nach einem Halbjahr abgebrochen wurde.
	 *
	 * @param projektkurs   die Fachbelegungen des Projektfaches
	 * @param leitfach      die Fachbelegungen des Leitfaches
	 * @param halbjahr1     das erste Halbjahr der Belegung des Projektfaches (das zweite ist das nachfolgende)
	 *
	 * @return true, falls das Leitfach eine geeigneten Belegung aufweist, sonst false
	 */
	private pruefeBelegungLeitfachbelegungEinzel(projektkurs : AbiturFachbelegung, leitfach : AbiturFachbelegung, halbjahr1 : GostHalbjahr) : boolean {
		if (!this.manager.pruefeBelegung(projektkurs, halbjahr1))
			return false;
		if (this.manager.pruefeBelegung(leitfach, halbjahr1))
			return true;
		for (let hj : GostHalbjahr = halbjahr1.previousOrException(); hj.istQualifikationsphase(); hj = hj.previousOrException())
			if (this.manager.pruefeBelegung(leitfach, hj, hj.nextOrException()))
				return true;
		return false;
	}

	/**
	 * Prüft, ob das Leitfach in Bezug auf die Belegung des Projektfaches die korrekten Halbjahresbelegungen hat.
	 *
	 * @param projektkurs   die Fachbelegungen des Projektfaches
	 * @param leitfach      die Fachbelegungen des Leitfaches
	 *
	 * @return true, falls das Leitfach eine geeigneten Belegung aufweist, sonst false
	 */
	private pruefeBelegungLeitfachbelegung(projektkurs : AbiturFachbelegung, leitfach : AbiturFachbelegung) : boolean {
		return (this.pruefeBelegungLeitfachbelegungNormal(projektkurs, leitfach, GostHalbjahr.Q11) || this.pruefeBelegungLeitfachbelegungNormal(projektkurs, leitfach, GostHalbjahr.Q12) || this.pruefeBelegungLeitfachbelegungNormal(projektkurs, leitfach, GostHalbjahr.Q21) || this.pruefeBelegungLeitfachbelegungEinzel(projektkurs, leitfach, GostHalbjahr.Q11) || this.pruefeBelegungLeitfachbelegungEinzel(projektkurs, leitfach, GostHalbjahr.Q12) || this.pruefeBelegungLeitfachbelegungEinzel(projektkurs, leitfach, GostHalbjahr.Q21) || this.pruefeBelegungLeitfachbelegungEinzel(projektkurs, leitfach, GostHalbjahr.Q22));
	}

	/**
	 * Gibt den belegten Projektkurs zurück, fall ein Kurs gültig belegt wurde.
	 *
	 * @return die Fachbelegung des Projektkurses oder null
	 */
	public getProjektkurs() : AbiturFachbelegung | null {
		return this.projektkurs;
	}

	/**
	 * Gibt zurück, ob die angegebene Fachbelegung des Halbjahres eine Fachbelegung des
	 * angewählten Projektkurses ist und anrechenbar ist. Sollte sie Teil des Projektkurses
	 * sein, aber auch zu einer besonderen Lernleistung gehören, so ist sie nicht anrechenbar.
	 *
	 * @param fachbelegungHalbjahr   die Fachbelegung des Halbjahres
	 *
	 * @return true, wenn die Fachbelegung anrechenbar ist.
	 */
	public istAnrechenbar(fachbelegungHalbjahr : AbiturFachbelegungHalbjahr | null) : boolean {
		if (fachbelegungHalbjahr === null)
			return false;
		if (GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel) as unknown !== GostKursart.PJK as unknown)
			return false;
		const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
		if ((this.projektkurs === null) || (this.projektkursHalbjahre === null) || (this.manager.istProjektKursBesondereLernleistung()))
			return false;
		return (halbjahr as unknown === this.projektkursHalbjahre.get(0) as unknown) || (halbjahr as unknown === this.projektkursHalbjahre.get(1) as unknown);
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück
	 *
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public getAnrechenbareKurse() : number {
		if ((this.projektkurs === null) || (this.manager.istProjektKursBesondereLernleistung()))
			return 0;
		return 2;
	}

	/**
	 * Gibt die beiden anrechenbaren Halbjahre der Projektkursbelegung zurück, sofern
	 * der Kurs anrechenbar ist.
	 *
	 * @return die beiden Halbjahre der Projektkursbelegung
	 */
	public getAnrechenbareHalbjahre() : List<GostHalbjahr> {
		return this.projektkursHalbjahre;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse'].includes(name);
	}

	public static class = new Class<Projektkurse>('de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse(obj : unknown) : Projektkurse {
	return obj as Projektkurse;
}
