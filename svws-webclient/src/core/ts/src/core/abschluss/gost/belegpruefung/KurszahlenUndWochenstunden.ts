import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../../core/data/gost/GostFach';
import { HashMap, cast_java_util_HashMap } from '../../../../java/util/HashMap';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegungHalbjahr } from '../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart, cast_de_nrw_schule_svws_core_types_gost_GostKursart } from '../../../../core/types/gost/GostKursart';
import { Projektkurse, cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Projektkurse } from '../../../../core/abschluss/gost/belegpruefung/Projektkurse';
import { JavaInteger, cast_java_lang_Integer } from '../../../../java/lang/JavaInteger';
import { GostFachbereich, cast_de_nrw_schule_svws_core_types_gost_GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { NullPointerException, cast_java_lang_NullPointerException } from '../../../../java/lang/NullPointerException';
import { Note, cast_de_nrw_schule_svws_core_types_Note } from '../../../../core/types/Note';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class KurszahlenUndWochenstunden extends GostBelegpruefung {

	private kurszahlen : HashMap<GostHalbjahr, HashMap<GostKursart, number>> | null = null;

	private kurszahlenGrundkurse : HashMap<GostHalbjahr, number> | null = null;

	private kurszahlenLeistungskurse : HashMap<GostHalbjahr, number> | null = null;

	private kurszahlenAnrechenbar : HashMap<GostHalbjahr, number> | null = null;

	private kurszahlenEinfuehrungsphase : HashMap<GostKursart, number> | null = null;

	private kurszahlenQualifikationsphase : HashMap<GostKursart, number> | null = null;

	private blockIAnzahlGrundkurse : number = 0;

	private anzahlLKFaecher : number = 0;

	private blockIAnzahlLeistungskurse : number = 0;

	private blockIAnzahlAnrechenbar : number = 0;

	private wochenstunden : HashMap<GostHalbjahr, number> | null = null;

	private wochenstundenEinfuehrungsphase : number = 0;

	private wochenstundenQualifikationsphase : number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für die Kurszahlen.
	 *
	 * @param manager                 der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt, pruefungProjektkurse : GostBelegpruefung) {
		super(manager, pruefungs_art, pruefungProjektkurse);
	}

	protected init() : void {
		this.kurszahlen = new HashMap();
		this.kurszahlenGrundkurse = new HashMap();
		this.kurszahlenLeistungskurse = new HashMap();
		this.kurszahlenAnrechenbar = new HashMap();
		this.kurszahlenEinfuehrungsphase = new HashMap();
		this.kurszahlenQualifikationsphase = new HashMap();
		this.blockIAnzahlGrundkurse = 0;
		this.anzahlLKFaecher = 0;
		this.blockIAnzahlLeistungskurse = 0;
		this.blockIAnzahlAnrechenbar = 0;
		this.wochenstunden = new HashMap();
		this.wochenstundenEinfuehrungsphase = 0;
		this.wochenstundenQualifikationsphase = 0;
		let projektkurse : Projektkurse = (cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_Projektkurse(this.pruefungen_vorher[0]));
		let kursarten : Array<GostKursart> = GostKursart.values();
		for (let halbjahr of GostHalbjahr.values()) {
			let kurszahlenHalbjahr : HashMap<GostKursart, number> = new HashMap();
			this.kurszahlen.put(halbjahr, kurszahlenHalbjahr);
			for (let kursart of kursarten)
				kurszahlenHalbjahr.put(kursart, 0);
			this.kurszahlenGrundkurse.put(halbjahr, 0);
			this.kurszahlenLeistungskurse.put(halbjahr, 0);
			this.kurszahlenAnrechenbar.put(halbjahr, 0);
			this.wochenstunden.put(halbjahr, 0);
		}
		for (let kursart of kursarten) {
			this.kurszahlenEinfuehrungsphase.put(kursart, 0);
			this.kurszahlenQualifikationsphase.put(kursart, 0);
		}
		let alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen();
		for (let i : number = 0; i < alleFachbelegungen.size(); i++){
			let fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			let fach : GostFach | null = this.manager.getFach(fachbelegung);
			let istLKFach : boolean = false;
			for (let fachbelegungHalbjahr of fachbelegung.belegungen) {
				if (fachbelegungHalbjahr === null)
					continue;
				if (GostFachbereich.SPORT.hat(fach) && JavaObject.equalsTranspiler(Note.ATTEST, (Note.fromKuerzel(fachbelegungHalbjahr.notenkuerzel))))
					continue;
				let halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(fachbelegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				let kursart : GostKursart | null = GostKursart.fromKuerzel(fachbelegungHalbjahr.kursartKuerzel);
				if (kursart === null)
					continue;
				let kurszahlenHalbjahr : HashMap<GostKursart, number> | null = this.kurszahlen.get(halbjahr);
				if (kurszahlenHalbjahr === null)
					kurszahlenHalbjahr = new HashMap();
				let kurszahlAlt : number | null = kurszahlenHalbjahr.get(kursart);
				kurszahlenHalbjahr.put(kursart, kurszahlAlt === null ? 1 : kurszahlAlt! + 1);
				if ((kursart as unknown === GostKursart.GK as unknown) || (halbjahr.istQualifikationsphase() && ((kursart as unknown === GostKursart.ZK as unknown) || ((kursart as unknown === GostKursart.PJK as unknown) && (projektkurse.istAnrechenbar(fachbelegungHalbjahr)))))) {
					let kurszahlGK : number | null = this.kurszahlenGrundkurse.get(halbjahr);
					this.kurszahlenGrundkurse.put(halbjahr, kurszahlGK === null ? 1 : kurszahlGK! + 1);
					let kurszahlAnrechenbar : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
					this.kurszahlenAnrechenbar.put(halbjahr, kurszahlAnrechenbar === null ? 1 : kurszahlAnrechenbar! + 1);
					if (halbjahr.istQualifikationsphase()) {
						this.blockIAnzahlGrundkurse++;
						this.blockIAnzahlAnrechenbar++;
					}
				}
				if (halbjahr.istQualifikationsphase() && (kursart as unknown === GostKursart.LK as unknown)) {
					istLKFach = true;
					let kurszahlLK : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
					this.kurszahlenLeistungskurse.put(halbjahr, kurszahlLK === null ? 1 : kurszahlLK! + 1);
					let kurszahlAnrechenbar : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
					this.kurszahlenAnrechenbar.put(halbjahr, kurszahlAnrechenbar === null ? 1 : kurszahlAnrechenbar! + 1);
					this.blockIAnzahlLeistungskurse++;
					this.blockIAnzahlAnrechenbar++;
				}
				let stunden : number = 0;
				switch (kursart.kuerzel) {
					case "GK": 
						stunden = ((fach !== null) && fach.istFremdSpracheNeuEinsetzend) ? 4 : 3;
						break;
					case "LK": 
						stunden = 5;
						break;
					case "PJK": 
						stunden = (fachbelegungHalbjahr.wochenstunden === 3) ? 3 : 2;
						break;
					case "VTF": 
						stunden = 2;
						break;
					case "ZK": 
						stunden = 3;
						break;
				}
				let wochenstundenAlt : number | null = this.wochenstunden.get(halbjahr);
				this.wochenstunden.put(halbjahr, wochenstundenAlt === null ? stunden : wochenstundenAlt! + stunden);
				if (halbjahr.istEinfuehrungsphase()) {
					let kurszahlEF : number | null = this.kurszahlenEinfuehrungsphase.get(kursart);
					this.kurszahlenEinfuehrungsphase.put(kursart, kurszahlEF === null ? 1 : kurszahlEF! + 1);
					this.wochenstundenEinfuehrungsphase += stunden;
				} else {
					let kurszahlQ : number | null = this.kurszahlenQualifikationsphase.get(kursart);
					this.kurszahlenQualifikationsphase.put(kursart, kurszahlQ === null ? 1 : kurszahlQ! + 1);
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
		let kurszahlGK : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF1);
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
		let stunden : number | null = this.wochenstunden.get(GostHalbjahr.EF1);
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
		let kurszahlGK_EF1 : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF1);
		let kurszahlGK_EF2 : number | null = this.kurszahlenGrundkurse.get(GostHalbjahr.EF2);
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
		for (let halbjahr of GostHalbjahr.values()) {
			let stunden : number | null = this.wochenstunden.get(halbjahr);
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
		for (let halbjahr of GostHalbjahr.getQualifikationsphase()) {
			let kurszahlGK : number | null = this.kurszahlenGrundkurse.get(halbjahr);
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
		for (let halbjahr of GostHalbjahr.getQualifikationsphase()) {
			let kurszahlLK : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
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
		let kurszahlEF_VTF : number | null = this.kurszahlenEinfuehrungsphase.get(GostKursart.VTF);
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
		let kurszahlQ_VTF : number | null = this.kurszahlenQualifikationsphase.get(GostKursart.VTF);
		if ((kurszahlQ_VTF !== null) && (kurszahlQ_VTF > 2))
			this.addFehler(GostBelegungsfehler.VF_11);
	}

	/**
	 * Gesamtprüfung Punkt 69:
	 * Ist die Anzahl anrechenbarer Kurse für Block I des Abiturs (Qualifikationsphase) größer gleich 38?
	 */
	private pruefeAnrechenbareKurse() : void {
		if (this.blockIAnzahlAnrechenbar < 38)
			this.addFehler(GostBelegungsfehler.ANZ_12);
	}

	/**
	 * Gesamtprüfung Punkte 80-82:
	 * Prüfe, ob die Summe der durschnittlichen Kursstunden der 3 Jahre größer oder gleich 100 bzw. 102 ist
	 * und ob die durchschnittliche Summe der Kursstunden in der Einführungsphase under Qualifikationsphase
	 * größer oder gleich 34 ist.
	 */
	private pruefeKursstundenSummen() : void {
		if (this.wochenstundenEinfuehrungsphase / 2.0 < 34.0)
			this.addFehler(GostBelegungsfehler.WST_20);
		if (this.wochenstundenQualifikationsphase / 4.0 < 34.0)
			this.addFehler(GostBelegungsfehler.WST_21);
		let summeKursstundenDurchschnitte : number = (this.wochenstundenEinfuehrungsphase / 2.0) + (this.wochenstundenQualifikationsphase / 4.0) * 2.0;
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
		let kurszahlenHalbjahr : HashMap<GostKursart, number> | null = this.kurszahlen.get(halbjahr);
		if (kurszahlenHalbjahr === null)
			return 0;
		let kurszahl : number | null = kurszahlenHalbjahr.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		let kurszahl : number | null = this.kurszahlenGrundkurse.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		let kurszahl : number | null = this.kurszahlenLeistungskurse.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		let kurszahl : number | null = this.kurszahlenAnrechenbar.get(halbjahr);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		let kurszahl : number | null = this.kurszahlenEinfuehrungsphase.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		let kurszahl : number | null = this.kurszahlenQualifikationsphase.get(kursart);
		if (kurszahl === null)
			return 0;
		return kurszahl!;
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
		return stunden!;
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

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung', 'de.nrw.schule.svws.core.abschluss.gost.belegpruefung.KurszahlenUndWochenstunden'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_KurszahlenUndWochenstunden(obj : unknown) : KurszahlenUndWochenstunden {
	return obj as KurszahlenUndWochenstunden;
}
