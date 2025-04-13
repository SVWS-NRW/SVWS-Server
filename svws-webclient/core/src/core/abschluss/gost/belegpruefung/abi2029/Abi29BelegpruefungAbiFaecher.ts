import { GostFach } from '../../../../../core/data/gost/GostFach';
import { Abi29BelegpruefungProjektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungProjektkurse } from '../../../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungProjektkurse';
import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { ArrayMap } from '../../../../../core/adt/map/ArrayMap';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart } from '../../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { GostSchriftlichkeit } from '../../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';
import { HashSet } from '../../../../../java/util/HashSet';

export class Abi29BelegpruefungAbiFaecher extends GostBelegpruefung {

	private mapAbiturFachbelegungen : ArrayMap<GostAbiturFach, AbiturFachbelegung> | null = null;

	private anzahlAbiFaecher : number = 0;

	private anzahlDeutschMatheFremdsprache : number = 0;

	private anzahlFremdsprachen : number = 0;

	private anzahlSportReligion : number = 0;

	private hatAufgabenfeldI : boolean = false;

	private hatAufgabenfeldII : boolean = false;

	private hatAufgabenfeldIII : boolean = false;


	/**
	 * Erstellt eine neue Belegprüfung für die Projektkurse.
	 *
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt, pruefungProjektkurse : Abi29BelegpruefungProjektkurse) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}

	protected init() : void {
		this.mapAbiturFachbelegungen = new ArrayMap(GostAbiturFach.values());
		this.anzahlAbiFaecher = 0;
		this.anzahlDeutschMatheFremdsprache = 0;
		this.anzahlFremdsprachen = 0;
		this.anzahlSportReligion = 0;
		this.hatAufgabenfeldI = false;
		this.hatAufgabenfeldII = false;
		this.hatAufgabenfeldIII = false;
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach === null)
				continue;
			this.mapAbiturFachbelegungen.put(abiturFach, fachbelegung);
			this.anzahlAbiFaecher++;
			const fach : GostFach | null = this.manager.getFach(fachbelegung);
			if (fach === null)
				continue;
			if (GostFachbereich.FREMDSPRACHE.hat(fach) || GostFachbereich.DEUTSCH.hat(fach))
				this.hatAufgabenfeldI = true;
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION.hat(fach))
				this.hatAufgabenfeldII = true;
			if (GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH.hat(fach))
				this.hatAufgabenfeldIII = true;
			if (GostFachbereich.FREMDSPRACHE.hat(fach) || GostFachbereich.DEUTSCH.hat(fach) || GostFachbereich.MATHEMATIK.hat(fach))
				this.anzahlDeutschMatheFremdsprache++;
			if (GostFachbereich.FREMDSPRACHE.hat(fach))
				this.anzahlFremdsprachen++;
			if (GostFachbereich.SPORT.hat(fach) || GostFachbereich.RELIGION.hat(fach))
				this.anzahlSportReligion++;
		}
	}

	protected pruefeEF1() : void {
		// empty block
	}

	protected pruefeGesamt() : void {
		this.pruefeLK1();
		this.pruefeAnzahlUndAufgabenfelderAbiFaecher();
		this.pruefeMehrfacheAbiturfaecher();
		this.pruefeSchriftlichkeitAB3undAB4();
		this.pruefeSchriftlichkeitAB5();
	}

	/**
	 * Gesamtprüfung Punkt 70:
	 * Prüfe, ob der erste LK eine fortgeführte Fremdsprache, eine klassische Naturwissenschaft, Mathematik oder Deutsch ist
	 */
	private pruefeLK1() : void {
		const lk1 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		const lk1fach : GostFach | null = this.manager.getFach(lk1);
		if ((lk1 === null) || (lk1fach === null) || !((GostFachbereich.DEUTSCH.hat(lk1fach)) || (GostFachbereich.FREMDSPRACHE.hat(lk1fach) && !lk1.istFSNeu) || (GostFachbereich.MATHEMATIK.hat(lk1fach)) || (GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(lk1fach))))
			this.addFehler(GostBelegungsfehler.LK1_11);
	}

	/**
	 * Gesamtprüfung Punkt 71-74:
	 * Prüfe, ob die Zahl der Abiturfächer 5 ist und diese alle Aufgabenfelder abdecken
	 *    und ob mindestens 2 Fächer im Bereich Deutsch, Fremdsprache, Mathematik liegen
	 *    und ob maximale 1 Fach im Bereich Sport und Religion liegt
	 *    und ob Sport nicht als erstes oder drittes Abiturfach gewählt wurde
	 */
	private pruefeAnzahlUndAufgabenfelderAbiFaecher() : void {
		if ((this.anzahlAbiFaecher !== 5) || (!this.hatAufgabenfeldI) || (!this.hatAufgabenfeldII) || (!this.hatAufgabenfeldIII))
			this.addFehler(GostBelegungsfehler.LK1_13_2);
		if (this.anzahlDeutschMatheFremdsprache < 2)
			this.addFehler(GostBelegungsfehler.ABI_10_2);
		if ((this.anzahlDeutschMatheFremdsprache < 3) && (this.anzahlFremdsprachen > 1))
			this.addFehler(GostBelegungsfehler.ABI_19_2);
		if (this.anzahlSportReligion > 1)
			this.addFehler(GostBelegungsfehler.ABI_11);
		const lk1 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		const lk1fach : GostFach | null = this.manager.getFach(lk1);
		const ab3 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		const ab3fach : GostFach | null = this.manager.getFach(ab3);
		if (((lk1fach !== null) && (GostFachbereich.SPORT.hatKuerzel(lk1fach.kuerzel))) || ((ab3fach !== null) && (GostFachbereich.SPORT.hatKuerzel(ab3fach.kuerzel))))
			this.addFehler(GostBelegungsfehler.ABI_15_2);
	}

	/**
	 * Gesamtprüfung: Prüfe, ob eines der Abiturfächer mehrfach belegt wurde. Es ist nicht zulässig
	 * Abiturfächer mehrfach belegt zu haben.
	 */
	private pruefeMehrfacheAbiturfaecher() : void {
		const abiFaecher : HashSet<GostAbiturFach> = new HashSet<GostAbiturFach>();
		const alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen();
		for (const fachbelegung of alleFachbelegungen) {
			const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach === null)
				continue;
			if (!abiFaecher.contains(abiturFach)) {
				abiFaecher.add(abiturFach);
				continue;
			}
			switch (abiturFach) {
				case GostAbiturFach.LK1: {
					this.addFehler(GostBelegungsfehler.ABI_21);
					break;
				}
				case GostAbiturFach.LK2: {
					this.addFehler(GostBelegungsfehler.ABI_22);
					break;
				}
				case GostAbiturFach.AB3: {
					this.addFehler(GostBelegungsfehler.ABI_23);
					break;
				}
				case GostAbiturFach.AB4: {
					this.addFehler(GostBelegungsfehler.ABI_24);
					break;
				}
				case GostAbiturFach.AB5: {
					this.addFehler(GostBelegungsfehler.ABI_25);
					break;
				}
			}
		}
	}

	private pruefeSchriftlichkeitVorQ22(belegung : AbiturFachbelegung | null) : boolean {
		if (this.manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			return true;
		const fach : GostFach | null = this.manager.getFach(belegung);
		if (fach !== null) {
			let belegungen : List<AbiturFachbelegung>;
			belegungen = this.manager.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11)) && (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12)) && (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21)))
				return true;
			if (GostFachbereich.RELIGION.hat(fach)) {
				belegungen = this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION);
				if ((this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11)) && (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12)) && (this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(belegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Gesamtprüfung Punkte 76 und 77:
	 * Prüfe ob das 3. Abiturfach von Q1.1 bis Q2.2 schriftlich belegt wurde
	 *   und on das 4. Abiturfach von Q1.1 bis Q2.1 schriftlich und in Q2.2 mündlich belegt wurde
	 */
	private pruefeSchriftlichkeitAB3undAB4() : void {
		const ab3 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		if (ab3 !== null) {
			if (!this.pruefeSchriftlichkeitVorQ22(ab3))
				this.addFehler(GostBelegungsfehler.ABI_17);
			if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22))
				this.addFehler(GostBelegungsfehler.ABI_12);
		}
		const ab4 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB4);
		if (ab4 !== null) {
			if (!this.pruefeSchriftlichkeitVorQ22(ab4))
				this.addFehler(GostBelegungsfehler.ABI_18);
			if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab4, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22))
				this.addFehler(GostBelegungsfehler.ABI_13);
		}
	}

	/**
	 * Gesamtprüfung:
	 * Prüfe, on das 5. Abiturfach ...
	 */
	private pruefeSchriftlichkeitAB5() : void {
		const ab5 : AbiturFachbelegung | null = (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB5);
		if (ab5 === null)
			return;
		if (this.manager.pruefeBelegungMitKursart(ab5, GostKursart.GK, GostHalbjahr.Q22)) {
			if (!this.pruefeSchriftlichkeitVorQ22(ab5))
				this.addFehler(GostBelegungsfehler.ABI_29_2);
			if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab5, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22))
				this.addFehler(GostBelegungsfehler.ABI_30_2);
			return;
		}
		if (this.manager.pruefeBelegungMitKursart(ab5, GostKursart.PJK, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			const pruefungProjektkurse : Abi29BelegpruefungProjektkurse = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungProjektkurse(this.pruefungen_vorher[0]));
			if (pruefungProjektkurse.getProjektkurs() === null)
				return;
			const fach : GostFach | null = this.manager.getFach(pruefungProjektkurse.getProjektkurs());
			if (fach === null)
				return;
			const leitfach : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			if (leitfach === null)
				return;
			if (!this.manager.pruefeBelegung(leitfach, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
				this.addFehler(GostBelegungsfehler.ABI_26_2);
				return;
			}
			if (!this.manager.pruefeBelegungMitSchriftlichkeit(leitfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
				this.addFehler(GostBelegungsfehler.ABI_27_2);
				return;
			}
			if (GostAbiturFach.fromID(leitfach.abiturFach) !== null) {
				this.addFehler(GostBelegungsfehler.ABI_28_2);
			}
		}
	}

	/**
	 * Liefert die zugehörige Abitur-Fachbelegung zurück.
	 *
	 * @param abifach  die Art des Abifachs (1., 2., 3. oder 4. Fach)
	 *
	 * @return die Abitur-Fachbelegung oder null, falls es (noch) nicht festgelegt wurde
	 */
	public getAbiturfach(abifach : GostAbiturFach | null) : AbiturFachbelegung | null {
		return (this.mapAbiturFachbelegungen === null) ? null : this.mapAbiturFachbelegungen.get(abifach);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungAbiFaecher';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungAbiFaecher'].includes(name);
	}

	public static class = new Class<Abi29BelegpruefungAbiFaecher>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029.Abi29BelegpruefungAbiFaecher');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2029_Abi29BelegpruefungAbiFaecher(obj : unknown) : Abi29BelegpruefungAbiFaecher {
	return obj as Abi29BelegpruefungAbiFaecher;
}
