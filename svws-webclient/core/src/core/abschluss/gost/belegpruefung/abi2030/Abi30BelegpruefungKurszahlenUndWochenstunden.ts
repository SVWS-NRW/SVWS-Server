import { JavaObject } from '../../../../../java/lang/JavaObject';
import { Fach } from '../../../../../asd/types/fach/Fach';
import { GostFach } from '../../../../../core/data/gost/GostFach';
import { Abi30BelegpruefungProjektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungProjektkurse';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { ArrayMap } from '../../../../../core/adt/map/ArrayMap';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart } from '../../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { NullPointerException } from '../../../../../java/lang/NullPointerException';
import { Note } from '../../../../../asd/types/Note';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungKurszahlenUndWochenstunden extends GostBelegpruefung {

	private kurszahlen : ArrayMap<GostHalbjahr, ArrayMap<GostKursart, number>> | null = null;

	private kurszahlenGrundkurse : ArrayMap<GostHalbjahr, number> | null = null;

	private kurszahlenLeistungskurse : ArrayMap<GostHalbjahr, number> | null = null;

	private kurszahlenAnrechenbar : ArrayMap<GostHalbjahr, number> | null = null;

	private kurszahlenEinfuehrungsphase : ArrayMap<GostKursart, number> | null = null;

	private kurszahlenQualifikationsphase : ArrayMap<GostKursart, number> | null = null;

	private blockIAnzahlGrundkurse : number = 0;

	private anzahlLKFaecher : number = 0;

	private blockIAnzahlLeistungskurse : number = 0;

	private blockIAnzahlAnrechenbar : number = 0;

	private wochenstunden : ArrayMap<GostHalbjahr, number> | null = null;

	private wochenstundenEinfuehrungsphase : number = 0;

	private wochenstundenQualifikationsphase : number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für die Kurszahlen.
	 *
	 * @param manager                 der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt, pruefungProjektkurse : GostBelegpruefung) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}

	protected init() : void {
		this.kurszahlen = new ArrayMap(GostHalbjahr.values());
		this.kurszahlenGrundkurse = new ArrayMap(GostHalbjahr.values());
		this.kurszahlenLeistungskurse = new ArrayMap(GostHalbjahr.values());
		this.kurszahlenAnrechenbar = new ArrayMap(GostHalbjahr.values());
		this.kurszahlenEinfuehrungsphase = new ArrayMap(GostKursart.values());
		this.kurszahlenQualifikationsphase = new ArrayMap(GostKursart.values());
		this.blockIAnzahlGrundkurse = 0;
		this.anzahlLKFaecher = 0;
		this.blockIAnzahlLeistungskurse = 0;
		this.blockIAnzahlAnrechenbar = 0;
		this.wochenstunden = new ArrayMap(GostHalbjahr.values());
		this.wochenstundenEinfuehrungsphase = 0;
		this.wochenstundenQualifikationsphase = 0;
		const projektkurse : Abi30BelegpruefungProjektkurse = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse(this.pruefungen_vorher[0]));
		const musik : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(Fach.MU.name());
		const blockIHatMusikLK : boolean = this.manager.pruefeBelegungHatMindestensEinmalKursart(musik, GostKursart.LK, ...GostHalbjahr.getQualifikationsphase());
		const blockIHatMusikGKAbitur : boolean = this.manager.pruefeBelegungHatMindestensEinmalKursart(musik, GostKursart.GK, ...GostHalbjahr.getQualifikationsphase()) && (musik !== null) && ((musik.abiturFach !== null) && ((musik.abiturFach === 3) || (musik.abiturFach === 4)));
		let blockIAnzahlMusik : number = 0;
		let blockIAnzahlErsatzfach : number = 0;
		const kursarten : Array<GostKursart> = GostKursart.values();
		for (const halbjahr of GostHalbjahr.values()) {
			const kurszahlenHalbjahr : ArrayMap<GostKursart, number> = new ArrayMap<GostKursart, number>(GostKursart.values());
			this.kurszahlen.put(halbjahr, kurszahlenHalbjahr);
			for (const kursart of kursarten)
				kurszahlenHalbjahr.put(kursart, 0);
			this.kurszahlenGrundkurse.put(halbjahr, 0);
			this.kurszahlenLeistungskurse.put(halbjahr, 0);
			this.kurszahlenAnrechenbar.put(halbjahr, 0);
			this.wochenstunden.put(halbjahr, 0);
		}
		for (const kursart of kursarten) {
			this.kurszahlenEinfuehrungsphase.put(kursart, 0);
			this.kurszahlenQualifikationsphase.put(kursart, 0);
		}
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if ((fach === null) || (!fach.istPruefungsordnungsRelevant))
				continue;
			const zulFach : Fach | null = Fach.getBySchluesselOrDefault(fach.kuerzel);
			let istLKFach : boolean = false;
			for (const fachbelegungHalbjahr of fachbelegung.belegungen) {
				if (fachbelegungHalbjahr === null)
					continue;
				const note : Note | null = Note.fromKuerzel(fachbelegungHalbjahr.notenkuerzel);
				if (GostFachbereich.SPORT.hat(fach) && JavaObject.equalsTranspiler(Note.ATTEST, (note)))
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				const kursart : GostKursart | null = GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel);
				if (kursart === null)
					continue;
				let istAnrechenbar : boolean = true;
				let istAnrechenbarHalbjahr : boolean = true;
				let istNullPunkteBelegungInQPhase : boolean = false;
				const istMusik : boolean = (zulFach as unknown === Fach.MU as unknown);
				const istErsatzfach : boolean = GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach);
				const istMusikErsatzfach : boolean = istErsatzfach && ((zulFach as unknown === Fach.IN as unknown) || (zulFach as unknown === Fach.VO as unknown));
				if (halbjahr.istQualifikationsphase()) {
					if (istMusik || istMusikErsatzfach) {
						blockIAnzahlMusik++;
						if (blockIHatMusikLK) {
							istAnrechenbar = !istMusikErsatzfach;
							istAnrechenbarHalbjahr = !istMusikErsatzfach;
							if (!istAnrechenbar && (this.pruefungs_art as unknown === GostBelegpruefungsArt.GESAMT as unknown))
								this.addFehler(GostBelegungsfehler.ANZ_21_INFO);
						} else
							if (blockIHatMusikGKAbitur) {
								istAnrechenbar = (blockIAnzahlMusik <= 6);
								if (!istAnrechenbar && (this.pruefungs_art as unknown === GostBelegpruefungsArt.GESAMT as unknown))
									this.addFehler(GostBelegungsfehler.ANZ_22_INFO);
							} else {
								istAnrechenbar = (blockIAnzahlMusik <= 5);
								if (!istAnrechenbar && (this.pruefungs_art as unknown === GostBelegpruefungsArt.GESAMT as unknown))
									this.addFehler(GostBelegungsfehler.ANZ_23_INFO);
							}
					}
					if (istErsatzfach) {
						blockIAnzahlErsatzfach++;
						const istAnrechenbarErsatzfach : boolean = (blockIAnzahlErsatzfach <= 2);
						if (!istAnrechenbarErsatzfach && (this.pruefungs_art as unknown === GostBelegpruefungsArt.GESAMT as unknown))
							this.addFehler(GostBelegungsfehler.ANZ_20_INFO);
						istAnrechenbar = istAnrechenbar && istAnrechenbarErsatzfach;
					}
					istNullPunkteBelegungInQPhase = AbiturdatenManager.istNullPunkteBelegungInQPhase(fachbelegungHalbjahr);
				}
				if (istAnrechenbarHalbjahr && !istNullPunkteBelegungInQPhase) {
					let kurszahlenHalbjahr : ArrayMap<GostKursart, number> | null = this.kurszahlen.get(halbjahr);
					if (kurszahlenHalbjahr === null)
						kurszahlenHalbjahr = new ArrayMap(GostKursart.values());
					const kurszahlAlt : number | null = kurszahlenHalbjahr.get(kursart);
					kurszahlenHalbjahr.put(kursart, (kurszahlAlt === null) ? 1 : (kurszahlAlt + 1));
				}
				if (istAnrechenbarHalbjahr && !istNullPunkteBelegungInQPhase && ((kursart as unknown === GostKursart.GK as unknown) || (halbjahr.istQualifikationsphase() && ((kursart as unknown === GostKursart.ZK as unknown) || ((kursart as unknown === GostKursart.PJK as unknown) && (projektkurse.istAnrechenbar(fachbelegungHalbjahr))))))) {
					const kurszahlAnrechenbar : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
					this.kurszahlenAnrechenbar.put(halbjahr, (kurszahlAnrechenbar === null) ? 1 : (kurszahlAnrechenbar + 1));
					const kurszahlGK : number | null = this.kurszahlenGrundkurse.get(halbjahr);
					this.kurszahlenGrundkurse.put(halbjahr, (kurszahlGK === null) ? 1 : (kurszahlGK + 1));
					if (istAnrechenbar) {
						if (halbjahr.istQualifikationsphase()) {
							this.blockIAnzahlGrundkurse++;
							this.blockIAnzahlAnrechenbar++;
						}
					}
				}
				if (halbjahr.istQualifikationsphase() && !istNullPunkteBelegungInQPhase && (kursart as unknown === GostKursart.LK as unknown)) {
					istLKFach = true;
					const kurszahlLK : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
					this.kurszahlenLeistungskurse.put(halbjahr, (kurszahlLK === null) ? 1 : (kurszahlLK + 1));
					const kurszahlAnrechenbar : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
					this.kurszahlenAnrechenbar.put(halbjahr, (kurszahlAnrechenbar === null) ? 1 : (kurszahlAnrechenbar + 1));
					this.blockIAnzahlLeistungskurse++;
					this.blockIAnzahlAnrechenbar++;
				}
				let stunden : number = 0;
				switch (kursart.kuerzel) {
					case "GK": {
						stunden = fach.istFremdSpracheNeuEinsetzend ? 4 : 3;
						break;
					}
					case "LK": {
						stunden = 5;
						break;
					}
					case "PJK": {
						stunden = (fachbelegungHalbjahr.wochenstunden === 3) ? 3 : 2;
						break;
					}
					case "VTF": {
						stunden = 2;
						break;
					}
					case "ZK": {
						stunden = 3;
						break;
					}
					default: {
						stunden = 3;
						break;
					}
				}
				const wochenstundenAlt : number | null = this.wochenstunden.get(halbjahr);
				this.wochenstunden.put(halbjahr, (wochenstundenAlt === null) ? stunden : (wochenstundenAlt + stunden));
				if (halbjahr.istEinfuehrungsphase()) {
					if (istAnrechenbar) {
						const kurszahlEF : number | null = this.kurszahlenEinfuehrungsphase.get(kursart);
						this.kurszahlenEinfuehrungsphase.put(kursart, (kurszahlEF === null) ? 1 : (kurszahlEF + 1));
					}
					this.wochenstundenEinfuehrungsphase += stunden;
				} else {
					if (istAnrechenbar && !istNullPunkteBelegungInQPhase) {
						const kurszahlQ : number | null = this.kurszahlenQualifikationsphase.get(kursart);
						this.kurszahlenQualifikationsphase.put(kursart, (kurszahlQ === null) ? 1 : (kurszahlQ + 1));
					}
					this.wochenstundenQualifikationsphase += stunden;
				}
			}
			if (istLKFach)
				this.anzahlLKFaecher++;
		}
	}

	protected pruefeEF1() : void {
		this.pruefeGrundkurseEF1();
		this.pruefeWochenstundenEF1();
	}

	/**
	 * EF1-Prüfung Punkt 21:
	 * Prüfe, ob zu wenige Grundkurse (ohne Vertiefungskurse) in der EF belegt wurden,
	 * dh. weniger als 10 Kurse
	 */
	private pruefeGrundkurseEF1() : void {
		if (this.kurszahlenGrundkurse === null)
			throw new NullPointerException()
		const kurszahlGK : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF1);
		if ((kurszahlGK === null) || (kurszahlGK < 10))
			this.addFehler(GostBelegungsfehler.ANZ_10);
	}

	/**
	 * EF1-Prüfung Punkt 22:
	 * Prüfe, ob die Summe der Kursstunden in der EF.1 größer oder gleich 32 und kleiner oder gleich 36 ist.
	 */
	private pruefeWochenstundenEF1() : void {
		if (this.wochenstunden === null)
			throw new NullPointerException()
		const stunden : number | null = this.wochenstunden.get(GostHalbjahr.EF1);
		if ((stunden === null) || (stunden < 32) || (stunden > 36))
			this.addFehler(GostBelegungsfehler.ANZ_11_INFO);
	}

	protected pruefeGesamt() : void {
		this.pruefeGrundkurseEF();
		this.pruefeGrundkurseQ();
		this.pruefeLeistungskurse();
		this.pruefeVertiefungskurseEF();
		this.pruefeWochenstunden();
		this.pruefeVertiefungskurseQ();
		this.pruefeAnrechenbareKurse();
		this.pruefeKursstundenSummen();
	}

	/**
	 * Gesamtprüfung Punkt 58:
	 * Prüfe, ob zu wenige Grundkurse (ohne Vertiefungskurse) in der EF belegt wurden,
	 * dh. weniger als 10 Kurse
	 */
	private pruefeGrundkurseEF() : void {
		if (this.kurszahlenGrundkurse === null)
			throw new NullPointerException()
		const kurszahlGK_EF1 : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF1);
		const kurszahlGK_EF2 : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF2);
		if ((kurszahlGK_EF1 === null) || (kurszahlGK_EF1 < 10) || (kurszahlGK_EF2 === null) || (kurszahlGK_EF2 < 10))
			this.addFehler(GostBelegungsfehler.ANZ_10);
	}

	/**
	 * Gesamtprüfung Punkt 59:
	 * Prüfe, ob in jedem Halbjahr die Summe der Kursstunden größer oder gleich 32 und kleiner oder gleich 36 ist.
	 */
	private pruefeWochenstunden() : void {
		if (this.wochenstunden === null)
			throw new NullPointerException()
		for (const halbjahr of GostHalbjahr.values()) {
			const stunden : number | null = this.wochenstunden.get(halbjahr);
			if ((stunden === null) || (stunden < 32) || (stunden > 36))
				this.addFehler(GostBelegungsfehler.ANZ_11_INFO);
		}
	}

	/**
	 * Gesamtprüfung Punkt 61:
	 * Prüfe, ob in den Halbjahren der Qualifikationsphase mindestens 7 Grundkurse belegt wurden.
	 * Dazu zählen auch Zusatzkurse sowie solche Projektkurse, die 2 Halbjahre belegt wurden
	 * und zu keiner besonderen Lernleistung zählen.
	 */
	private pruefeGrundkurseQ() : void {
		if (this.kurszahlenGrundkurse === null)
			throw new NullPointerException()
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			const kurszahlGK : number | null = this.kurszahlenGrundkurse.get(halbjahr);
			if ((kurszahlGK === null) || (kurszahlGK < 7))
				this.addFehler(GostBelegungsfehler.GKS_10);
		}
	}

	/**
	 * Gesamtprüfung Punkt 60 und 75:
	 * Wurden in der Qualifikationsphase in jedem Halbjahr zwei LKs belegt in insgesamt genau 2 Fächern.
	 */
	private pruefeLeistungskurse() : void {
		if (this.anzahlLKFaecher !== 2)
			this.addFehler(GostBelegungsfehler.LK_10);
		if (this.kurszahlenLeistungskurse === null)
			throw new NullPointerException()
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			const kurszahlLK : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
			if (kurszahlLK !== null) {
				if (kurszahlLK < 2)
					this.addFehler(GostBelegungsfehler.LK_10);
				else
					if (kurszahlLK > 2)
						this.addFehler(GostBelegungsfehler.LK_11);
			}
		}
	}

	/**
	 * Gesamtprüfung Punkt 62:
	 * Ist die Summe aller belegten Vertiefungskurse in der EF kleiner gleich 4?
	 */
	private pruefeVertiefungskurseEF() : void {
		if (this.kurszahlenEinfuehrungsphase === null)
			throw new NullPointerException()
		const kurszahlEF_VTF : number | null = this.kurszahlenEinfuehrungsphase.get(GostKursart.VTF);
		if ((kurszahlEF_VTF !== null) && (kurszahlEF_VTF > 4))
			this.addFehler(GostBelegungsfehler.VF_10);
	}

	/**
	 * Gesamtprüfung Punkt 63:
	 * Ist die Summe aller belegten Vertiefungskurse in der Qualifikationsphase kleiner gleich 2?
	 */
	private pruefeVertiefungskurseQ() : void {
		if (this.kurszahlenQualifikationsphase === null)
			throw new NullPointerException()
		const kurszahlQ_VTF : number | null = this.kurszahlenQualifikationsphase.get(GostKursart.VTF);
		if ((kurszahlQ_VTF !== null) && (kurszahlQ_VTF > 2))
			this.addFehler(GostBelegungsfehler.VF_11);
	}

	/**
	 * Gesamtprüfung Punkt 69:
	 * Ist die Anzahl anrechenbarer Kurse für Block I des Abiturs (Qualifikationsphase) größer gleich 36?
	 */
	private pruefeAnrechenbareKurse() : void {
		if (this.blockIAnzahlAnrechenbar < 36)
			this.addFehler(GostBelegungsfehler.ANZ_12);
	}

	/**
	 * Gesamtprüfung Punkte 80-82:
	 * Prüfe, ob die Summe der durschnittlichen Kursstunden der 3 Jahre größer oder gleich 100 bzw. 102 ist
	 * und ob die durchschnittliche Summe der Kursstunden in der Einführungsphase under Qualifikationsphase
	 * größer oder gleich 34 ist.
	 */
	private pruefeKursstundenSummen() : void {
		if ((this.wochenstundenEinfuehrungsphase / 2.0) < 34.0)
			this.addFehler(GostBelegungsfehler.WST_20);
		if ((this.wochenstundenQualifikationsphase / 4.0) < 34.0)
			this.addFehler(GostBelegungsfehler.WST_21);
		const summeKursstundenDurchschnitte : number = (this.wochenstundenEinfuehrungsphase / 2.0) + ((this.wochenstundenQualifikationsphase / 4.0) * 2.0);
		if (summeKursstundenDurchschnitte < 102) {
			if (summeKursstundenDurchschnitte < 100) {
				this.addFehler(GostBelegungsfehler.STD_10);
			} else {
				this.addFehler(GostBelegungsfehler.STD_11_INFO);
			}
		}
	}

	/**
	 * Gibt die Kurszahlen für das Halbjahr und die Kursart zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 * @param kursart    die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlen(halbjahr : GostHalbjahr, kursart : GostKursart) : number {
		if (this.kurszahlen === null)
			return 0;
		const kurszahlenHalbjahr : ArrayMap<GostKursart, number> | null = this.kurszahlen.get(halbjahr);
		if (kurszahlenHalbjahr === null)
			return 0;
		const kurszahl : number | null = kurszahlenHalbjahr.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Kurszahlen für die Grundkurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlenGrundkurse(halbjahr : GostHalbjahr) : number {
		if (this.kurszahlenGrundkurse === null)
			return 0;
		const kurszahl : number | null = this.kurszahlenGrundkurse.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Kurszahlen für die Leistungskurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlenLeistungskurse(halbjahr : GostHalbjahr) : number {
		if (this.kurszahlenLeistungskurse === null)
			return 0;
		const kurszahl : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Zahl der anrechenbaren Kurse für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlenAnrechenbar(halbjahr : GostHalbjahr) : number {
		if (this.kurszahlenAnrechenbar === null)
			return 0;
		const kurszahl : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Zahl der Kurse mit der angegebenen Kursart in der Einführungsphase zurück.
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlenEinfuehrungsphase(kursart : GostKursart) : number {
		if (this.kurszahlenEinfuehrungsphase === null)
			return 0;
		const kurszahl : number | null = this.kurszahlenEinfuehrungsphase.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Zahl der Kurse mit der angegebenen Kursart in der Qualifikationsphase zurück.
	 *
	 * @param kursart   die Kursart
	 *
	 * @return die Kurszahlen
	 */
	public getKurszahlenQualifikationsphase(kursart : GostKursart) : number {
		if (this.kurszahlenQualifikationsphase === null)
			return 0;
		const kurszahl : number | null = this.kurszahlenQualifikationsphase.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl;
	}

	/**
	 * Gibt die Anzahl der Grundkurse für Block I zurück.
	 *
	 * @return die Anzahl der Grundkurse
	 */
	public getBlockIAnzahlGrundkurse() : number {
		return this.blockIAnzahlGrundkurse;
	}

	/**
	 * Gibt die Anzahl der Leistungskurse für Block I zurück.
	 *
	 * @return die Anzahl der Leistungskurse
	 */
	public getBlockIAnzahlLeistungskurse() : number {
		return this.blockIAnzahlLeistungskurse;
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I zurück.
	 *
	 * @return die Anzahl der anrechenbaren Kurse
	 */
	public getBlockIAnzahlAnrechenbar() : number {
		return this.blockIAnzahlAnrechenbar;
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für das angegebene Halbjahr zurück.
	 *
	 * @param halbjahr  das Halbjahr
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstunden(halbjahr : GostHalbjahr) : number {
		if (this.wochenstunden === null)
			return 0;
		let stunden : number | null = this.wochenstunden.get(halbjahr);
		if (stunden === null)
			stunden = 0;
		return stunden;
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für die Einführungsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstundenEinfuehrungsphase() : number {
		return this.wochenstundenEinfuehrungsphase;
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für die Qualifikationsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstundenQualifikationsphase() : number {
		return this.wochenstundenQualifikationsphase;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungKurszahlenUndWochenstunden';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungKurszahlenUndWochenstunden', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

	public static class = new Class<Abi30BelegpruefungKurszahlenUndWochenstunden>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungKurszahlenUndWochenstunden');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungKurszahlenUndWochenstunden(obj : unknown) : Abi30BelegpruefungKurszahlenUndWochenstunden {
	return obj as Abi30BelegpruefungKurszahlenUndWochenstunden;
}
