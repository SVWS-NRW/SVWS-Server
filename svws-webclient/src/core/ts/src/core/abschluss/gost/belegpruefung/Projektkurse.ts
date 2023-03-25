import { JavaObject } from '../../../../java/lang/JavaObject';
import { GostFach } from '../../../../core/data/gost/GostFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr } from '../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostFachManager } from '../../../../core/abschluss/gost/GostFachManager';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { List } from '../../../../java/util/List';
import { Vector } from '../../../../java/util/Vector';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';
import { HashSet } from '../../../../java/util/HashSet';

export class Projektkurse extends GostBelegpruefung {

	private projektkursBelegung : Vector<AbiturFachbelegung> | null = null;

	private projektkurs : AbiturFachbelegung | null = null;

	private projektkursHalbjahre : Vector<GostHalbjahr> | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.projektkurs = null;
		this.projektkursBelegung = new Vector();
		this.projektkursHalbjahre = new Vector();
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen();
		for (let i : number = 0; i < alleFachbelegungen.size(); i++) {
			const fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			if (this.manager.zaehleBelegung(fachbelegung) <= 0)
				continue;
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if ((fach !== null) && GostFachManager.istProjektkurs(fach)) {
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
		if (this.projektkursBelegung === null)
			return;
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
		if (this.projektkursBelegung === null)
			return;
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
				if (this.projektkursHalbjahre === null)
					this.projektkursHalbjahre = new Vector();
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
		if (this.projektkursBelegung === null)
			return;
		const pjkHalbjahre : HashSet<GostHalbjahr> = new HashSet();
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
				if ((nextHalbjahr !== null) && (GostFachManager.istWaehlbar(this.manager.getFach(fachbelegung), nextHalbjahr)) && ((this.projektkurs === null) || (this.projektkursHalbjahre === null) || (halbjahr.compareTo(this.projektkursHalbjahre.get(0)) < 0)))
					continue;
				this.addFehler(GostBelegungsfehler.PF_14);
			}
		}
	}

	/**
	 * Prüft die Belegung der Leitfächer
	 */
	private pruefeBelegungLeitfaecher() : void {
		if (this.projektkursBelegung === null)
			return;
		for (const fachbelegung of this.projektkursBelegung) {
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if (fach === null)
				continue;
			const leitfach1 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			const leitfach2 : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
			if ((leitfach1 !== null) && this.pruefeBelegungLeitfachbelegung(fachbelegung, leitfach1))
				continue;
			if ((leitfach2 !== null) && this.pruefeBelegungLeitfachbelegung(fachbelegung, leitfach2))
				continue;
			this.addFehler(GostBelegungsfehler.PF_13);
		}
	}

	/**
	 * Prüft, ob das Leitfach in Bezug auf die Belegung des Projektfaches die korrekten Halbjahresbelegungen hat.
	 *
	 * @param projektkurs   die Fachbelegungen des Projektfaches
	 * @param leitfach      die Fachbelegungen des Leitfaches
	 *
	 * @return true, falls das Leitfach eine geeigneten Belegung aufweist, sonst false
	 */
	private pruefeBelegungLeitfachbelegung(projektkurs : AbiturFachbelegung | null, leitfach : AbiturFachbelegung | null) : boolean {
		if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
			if ((leitfach !== null) && this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return true;
		} else
			if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q12, GostHalbjahr.Q21)) {
				if ((leitfach !== null) && (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12) || this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21)))
					return true;
			} else
				if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
					if ((leitfach !== null) && ((this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q21, GostHalbjahr.Q22))))
						return true;
				} else
					if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q11)) {
						if ((leitfach !== null) && this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11))
							return true;
					} else
						if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q12)) {
							if ((leitfach !== null) && (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12) || this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q12)))
								return true;
						} else
							if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q21)) {
								if ((leitfach !== null) && ((this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q21))))
									return true;
							} else
								if (this.manager.pruefeBelegung(projektkurs, GostHalbjahr.Q22)) {
									if ((leitfach !== null) && ((this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q11, GostHalbjahr.Q12)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q12, GostHalbjahr.Q21)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q21, GostHalbjahr.Q22)) || (this.manager.pruefeBelegung(leitfach, GostHalbjahr.Q22))))
										return true;
								}
		return false;
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse(obj : unknown) : Projektkurse {
	return obj as Projektkurse;
}
