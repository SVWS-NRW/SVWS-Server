import { GostFach } from '../../../../../core/data/gost/GostFach';
import { Fach } from '../../../../../asd/types/fach/Fach';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostFachUtils } from '../../../../../core/utils/gost/GostFachUtils';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr } from '../../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart } from '../../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungProjektkurse extends GostBelegpruefung {

	private projektkursBelegung : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	private projektkurs : AbiturFachbelegung | null = null;

	private projektkursHalbjahre : List<GostHalbjahr> = new ArrayList<GostHalbjahr>();


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
		this.projektkursBelegung = new ArrayList();
		this.projektkursHalbjahre = new ArrayList();
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
		this.pruefeBelegung();
		this.pruefeAufAnrechenbarenProjektkurs();
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
	 * Prüft, ob genau eine Projektkurs belegt wurde.
	 */
	private pruefeBelegung() : void {
		if (this.projektkursBelegung.isEmpty())
			this.addFehler(GostBelegungsfehler.PF_21_2);
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
				if ((halbjahr as unknown !== GostHalbjahr.Q21 as unknown) && (halbjahr as unknown !== GostHalbjahr.Q22 as unknown)) {
					this.addFehler(GostBelegungsfehler.PF_20_2);
					continue;
				}
				const nextHalbjahr : GostHalbjahr | null = halbjahr.next();
				if (nextHalbjahr === null) {
					this.addFehler(GostBelegungsfehler.PF_18);
					continue;
				} else
					if (!this.manager.pruefeBelegung(fachbelegung, nextHalbjahr)) {
						this.addFehler(GostBelegungsfehler.PF_17_2);
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
	 * Prüft die Belegung der Leitfächer
	 */
	private pruefeBelegungLeitfaecher() : void {
		for (const fachbelegung of this.projektkursBelegung) {
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if (fach === null)
				continue;
			const leitfach1 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			const leitfach2 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if ((leitfach1 === null) || (leitfach2 !== null)) {
				this.addFehler(GostBelegungsfehler.PF_22_2);
				continue;
			}
			if (!this.manager.pruefeBelegung(leitfach1, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
				this.addFehler(GostBelegungsfehler.PF_23_2);
				continue;
			}
			const lf : GostFach | null = this.manager.getFach(leitfach1);
			if (lf === null) {
				this.addFehler(GostBelegungsfehler.PF_25);
				continue;
			}
			const zf : Fach | null = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown)))
				this.addFehler(GostBelegungsfehler.PF_19);
		}
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
		if ((this.projektkurs === null) || (this.projektkursHalbjahre.size() !== 2) || (this.manager.istProjektKursBesondereLernleistung()))
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

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungProjektkurse>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse(obj : unknown) : Abi30BelegpruefungProjektkurse {
	return obj as Abi30BelegpruefungProjektkurse;
}
