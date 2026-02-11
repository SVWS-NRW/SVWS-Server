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
import { GostSchriftlichkeit } from '../../../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungProjektkurse extends GostBelegpruefung {

	/**
	 * Eine Vektor mit den Projektfächern, die belegt wurden. Dies sollte im Regelfall nur ein Fach sein, können aber ggf. bei einer gültigen Belegung bis zu drei Fächer sein
	 */
	private projektkursBelegung: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * falls ein Projektkurs gültig gewählt wurde: Der Projektkurs, sonst: null
	 */
	private projektkurs: AbiturFachbelegung | null = null;

	/**
	 * ein Vektor, welcher die anrechenbaren Halbjahre eines gültig angewählten Projektkurses beinhaltet
	 */
	private projektkursHalbjahre: List<GostHalbjahr> = new ArrayList<GostHalbjahr>();


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init(): void {
		this.projektkurs = null;
		this.projektkursBelegung = new ArrayList();
		this.projektkursHalbjahre = new ArrayList();
		const alleFachbelegungen: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			if (this.manager.zaehleBelegung(fachbelegung) <= 0)
				continue;
			const fach: GostFach | null = this.manager.getFach(fachbelegung);
			if ((fach !== null) && GostFachUtils.istProjektkurs(fach))
				this.projektkursBelegung.add(fachbelegung);
		}
	}

	protected pruefeEF1(): void {
		this.pruefeBelegungEF();
	}

	protected pruefeGesamt(): void {
		this.pruefeBelegungEF();
		this.pruefeBelegung();
		if (this.projektkurs !== null)
			this.pruefeBelegungLeitfaecher();
		if (this.manager.istProjektKursBesondereLernleistung())
			this.addFehler((this.projektkurs !== null) ? GostBelegungsfehler.PF_16_INFO : GostBelegungsfehler.PF_15);
	}

	/**
	 * Prüft, ob ein Projektfach in der EF belegt wurde. Eine solche Belegung ist nicht zulässig.
	 */
	private pruefeBelegungEF(): void {
		for (const fachbelegung of this.projektkursBelegung) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if (belegungHalbjahr === null)
					continue;
				const halbjahr: GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if ((halbjahr as unknown === GostHalbjahr.EF1 as unknown) || (halbjahr as unknown === GostHalbjahr.EF2 as unknown))
					this.addFehler(GostBelegungsfehler.PF_10);
			}
		}
	}

	/**
	 * Prüft, ob genau eine Projektkurs belegt wurde.
	 */
	private pruefeBelegung(): void {
		if (this.projektkursBelegung.isEmpty()) {
			this.addFehler(GostBelegungsfehler.PF_21_2);
			return;
		}
		if (this.projektkursBelegung.size() > 1) {
			this.addFehler(GostBelegungsfehler.PF_14);
			return;
		}
		const fachbelegung: AbiturFachbelegung | null = this.projektkursBelegung.get(0);
		if (fachbelegung === null)
			return;
		if ((fachbelegung.belegungen[GostHalbjahr.Q11.id] !== null) || (fachbelegung.belegungen[GostHalbjahr.Q12.id] !== null)) {
			this.addFehler(GostBelegungsfehler.PF_20_2);
			return;
		}
		if ((fachbelegung.belegungen[GostHalbjahr.Q21.id] === null) || (fachbelegung.belegungen[GostHalbjahr.Q22.id] === null)) {
			this.addFehler(GostBelegungsfehler.PF_20_2);
			return;
		}
		this.projektkurs = fachbelegung;
		this.projektkursHalbjahre.add(GostHalbjahr.Q21);
		this.projektkursHalbjahre.add(GostHalbjahr.Q22);
	}

	/**
	 * Prüft die Belegung der Leitfächer
	 */
	private pruefeBelegungLeitfaecher(): void {
		if (this.projektkurs === null)
			return;
		const fach: GostFach | null = this.manager.getFach(this.projektkurs);
		if (fach === null)
			return;
		const leitfach1: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
		const leitfach2: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach2Kuerzel);
		if ((leitfach1 === null) && (leitfach2 === null)) {
			this.addFehler(GostBelegungsfehler.PF_22_2);
			return;
		}
		let hatReferenzfach1Belegung: boolean = false;
		let hatReferenzfach1BelegungSchriftlich: boolean = false;
		if (leitfach1 !== null) {
			const lf: GostFach | null = this.manager.getFach(leitfach1);
			if (lf === null) {
				this.addFehler(GostBelegungsfehler.PF_25);
				return;
			}
			const zf: Fach | null = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown)))
				this.addFehler(GostBelegungsfehler.PF_19);
			hatReferenzfach1Belegung = this.manager.pruefeBelegung(leitfach1, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
			hatReferenzfach1BelegungSchriftlich = this.manager.pruefeBelegungMitSchriftlichkeit(leitfach1, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12);
		}
		let hatReferenzfach2Belegung: boolean = false;
		let hatReferenzfach2BelegungSchriftlich: boolean = false;
		if (leitfach2 !== null) {
			const lf: GostFach | null = this.manager.getFach(leitfach2);
			if (lf === null) {
				this.addFehler(GostBelegungsfehler.PF_25);
				return;
			}
			const zf: Fach | null = Fach.getBySchluesselOrDefault(lf.kuerzel);
			if ((GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(lf) || (zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown)))
				this.addFehler(GostBelegungsfehler.PF_19);
			hatReferenzfach2Belegung = this.manager.pruefeBelegung(leitfach2, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12);
			hatReferenzfach2BelegungSchriftlich = this.manager.pruefeBelegungMitSchriftlichkeit(leitfach2, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12);
		}
		if (!hatReferenzfach1Belegung && !hatReferenzfach2Belegung) {
			this.addFehler(GostBelegungsfehler.PF_23_2);
			return;
		}
		if ((!hatReferenzfach1Belegung || !hatReferenzfach1BelegungSchriftlich) && (!hatReferenzfach2Belegung || !hatReferenzfach2BelegungSchriftlich))
			this.addFehler(GostBelegungsfehler.PF_24_2);
	}

	/**
	 * Gibt den belegten Projektkurs zurück, fall ein Kurs gültig belegt wurde.
	 *
	 * @return die Fachbelegung des Projektkurses oder null
	 */
	public getProjektkurs(): AbiturFachbelegung | null {
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
	public istAnrechenbar(fachbelegungHalbjahr: AbiturFachbelegungHalbjahr | null): boolean {
		if (fachbelegungHalbjahr === null)
			return false;
		if (GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel) as unknown !== GostKursart.PJK as unknown)
			return false;
		const halbjahr: GostHalbjahr | null = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
		if ((this.projektkurs === null) || (this.projektkursHalbjahre.size() !== 2) || (this.manager.istProjektKursBesondereLernleistung()))
			return false;
		return (halbjahr as unknown === this.projektkursHalbjahre.get(0) as unknown) || (halbjahr as unknown === this.projektkursHalbjahre.get(1) as unknown);
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück
	 *
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public getAnrechenbareKurse(): number {
		if ((this.projektkurs === null) || (this.manager.istProjektKursBesondereLernleistung()))
			return 0;
		return 2;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse'].includes(name);
	}

	public static readonly class = new Class<Abi30BelegpruefungProjektkurse>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse(obj: unknown): Abi30BelegpruefungProjektkurse {
	return obj as Abi30BelegpruefungProjektkurse;
}
