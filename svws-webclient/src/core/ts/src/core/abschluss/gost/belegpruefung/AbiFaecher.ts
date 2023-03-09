import { JavaObject, cast_java_lang_Object } from '../../../../java/lang/JavaObject';
import { GostFach, cast_de_nrw_schule_svws_core_data_gost_GostFach } from '../../../data/gost/GostFach';
import { GostAbiturFach, cast_de_nrw_schule_svws_core_types_gost_GostAbiturFach } from '../../../types/gost/GostAbiturFach';
import { HashMap, cast_java_util_HashMap } from '../../../../java/util/HashMap';
import { AbiturFachbelegung, cast_de_nrw_schule_svws_core_data_gost_AbiturFachbelegung } from '../../../data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt } from '../GostBelegpruefungsArt';
import { GostBelegpruefung, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefung } from '../GostBelegpruefung';
import { AbiturdatenManager, cast_de_nrw_schule_svws_core_abschluss_gost_AbiturdatenManager } from '../AbiturdatenManager';
import { GostFachbereich, cast_de_nrw_schule_svws_core_types_gost_GostFachbereich } from '../../../types/gost/GostFachbereich';
import { GostSchriftlichkeit, cast_de_nrw_schule_svws_core_types_gost_GostSchriftlichkeit } from '../../../types/gost/GostSchriftlichkeit';
import { GostHalbjahr, cast_de_nrw_schule_svws_core_types_gost_GostHalbjahr } from '../../../types/gost/GostHalbjahr';
import { List, cast_java_util_List } from '../../../../java/util/List';
import { GostBelegungsfehler, cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehler } from '../GostBelegungsfehler';
import { HashSet, cast_java_util_HashSet } from '../../../../java/util/HashSet';

export class AbiFaecher extends GostBelegpruefung {

	private mapAbiturFachbelegungen : HashMap<GostAbiturFach, AbiturFachbelegung> | null = null;

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
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungs_art : GostBelegpruefungsArt) {
		super(manager, pruefungs_art);
	}

	protected init() : void {
		this.mapAbiturFachbelegungen = new HashMap();
		this.anzahlAbiFaecher = 0;
		this.anzahlDeutschMatheFremdsprache = 0;
		this.anzahlFremdsprachen = 0;
		this.anzahlSportReligion = 0;
		this.hatAufgabenfeldI = false;
		this.hatAufgabenfeldII = false;
		this.hatAufgabenfeldIII = false;
		let alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen();
		for (let i : number = 0; i < alleFachbelegungen.size(); i++){
			let fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			let abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach === null) 
				continue;
			this.mapAbiturFachbelegungen.put(abiturFach, fachbelegung);
			this.anzahlAbiFaecher++;
			let fach : GostFach | null = this.manager.getFach(fachbelegung);
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
	}

	protected pruefeGesamt() : void {
		this.pruefeLK1();
		this.pruefeAnzahlUndAufgabenfelderAbiFaecher();
		this.pruefeMehrfacheAbiturfaecher();
		this.pruefeSchriftlichkeitAB3undAB4();
	}

	/**
	 * Gesamtprüfung Punkt 70:
	 * Prüfe, ob der erste LK eine fortgeführte Fremdsprache, eine klassische Naturwissenschaft, Mathematik oder Deutsch ist
	 */
	private pruefeLK1() : void {
		let lk1 : AbiturFachbelegung | null = this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		let lk1fach : GostFach | null = this.manager.getFach(lk1);
		if ((lk1 === null) || (lk1fach === null) || !((GostFachbereich.DEUTSCH.hat(lk1fach)) || (GostFachbereich.FREMDSPRACHE.hat(lk1fach) && !lk1.istFSNeu) || (GostFachbereich.MATHEMATIK.hat(lk1fach)) || (GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH.hat(lk1fach)))) 
			this.addFehler(GostBelegungsfehler.LK1_11);
	}

	/**
	 * Gesamtprüfung Punkt 71-74:
	 * Prüfe, ob die Zahl der Abiturfächer 4 ist und diese alle Aufgabenfelder abdecken
	 *    und ob mindestens 2 Fächer im Bereich Deutsch, Fremdsprache, Mathematik liegen
	 *    und ob maximale 1 Fach im Bereich Sport und Religion liegt
	 *    und ob Sport nicht als erstes oder drittes Abiturfach gewählt wurde 
	 */
	private pruefeAnzahlUndAufgabenfelderAbiFaecher() : void {
		if ((this.anzahlAbiFaecher !== 4) || (!this.hatAufgabenfeldI) || (!this.hatAufgabenfeldII) || (!this.hatAufgabenfeldIII)) 
			this.addFehler(GostBelegungsfehler.LK1_13);
		if (this.anzahlDeutschMatheFremdsprache < 2) 
			this.addFehler(GostBelegungsfehler.ABI_10);
		if ((this.anzahlDeutschMatheFremdsprache < 3) && (this.anzahlFremdsprachen > 1)) 
			this.addFehler(GostBelegungsfehler.ABI_19);
		if (this.anzahlSportReligion > 1) 
			this.addFehler(GostBelegungsfehler.ABI_11);
		let lk1 : AbiturFachbelegung | null = this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.LK1);
		let lk1fach : GostFach | null = this.manager.getFach(lk1);
		let ab3 : AbiturFachbelegung | null = this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		let ab3fach : GostFach | null = this.manager.getFach(ab3);
		if (((lk1fach !== null) && (GostFachbereich.SPORT.hat(lk1fach.kuerzel))) || ((ab3fach !== null) && (GostFachbereich.SPORT.hat(ab3fach.kuerzel)))) 
			this.addFehler(GostBelegungsfehler.ABI_15);
	}

	/**
	 * Gesamtprüfung: Prüfe, ob eines der Abiturfächer mehrfach belegt wurde. Es ist nicht zulässig
	 * Abiturfächer mehrfach belegt zu haben. 
	 */
	private pruefeMehrfacheAbiturfaecher() : void {
		let abiFaecher : HashSet<GostAbiturFach> = new HashSet();
		let alleFachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen();
		for (let i : number = 0; i < alleFachbelegungen.size(); i++){
			let fachbelegung : AbiturFachbelegung | null = alleFachbelegungen.get(i);
			let abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach === null) 
				continue;
			if (!abiFaecher.contains(abiturFach)) {
				abiFaecher.add(abiturFach);
				continue;
			}
			switch (abiturFach) {
				case GostAbiturFach.LK1: 
					this.addFehler(GostBelegungsfehler.ABI_21);
					break;
				case GostAbiturFach.LK2: 
					this.addFehler(GostBelegungsfehler.ABI_22);
					break;
				case GostAbiturFach.AB3: 
					this.addFehler(GostBelegungsfehler.ABI_23);
					break;
				case GostAbiturFach.AB4: 
					this.addFehler(GostBelegungsfehler.ABI_24);
					break;
			}
		}
	}

	/**
	 * Gesamtprüfung Punkte 76 und 77:
	 * Prüfe ob das 3. Abiturfach von Q1.1 bis Q2.2 schriftlich belegt wurde 
	 *   und on das 4. Abiturfach von Q1.1 bis Q2.1 schriftlich und in Q2.2 mündlich belegt wurde
	 * 
	 */
	private pruefeSchriftlichkeitAB3undAB4() : void {
		let ab3 : AbiturFachbelegung | null = this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB3);
		if (ab3 !== null) {
			if (!this.manager.pruefeBelegungMitSchriftlichkeit(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
				this.addFehler(GostBelegungsfehler.ABI_17);
			if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab3, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q22)) 
				this.addFehler(GostBelegungsfehler.ABI_12);
		}
		let ab4 : AbiturFachbelegung | null = this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(GostAbiturFach.AB4);
		if (ab4 !== null) {
			if (!this.manager.pruefeBelegungMitSchriftlichkeit(ab4, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) 
				this.addFehler(GostBelegungsfehler.ABI_18);
			if (!this.manager.pruefeBelegungMitSchriftlichkeitEinzeln(ab4, GostSchriftlichkeit.MUENDLICH, GostHalbjahr.Q22)) 
				this.addFehler(GostBelegungsfehler.ABI_13);
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
		return this.mapAbiturFachbelegungen === null ? null : this.mapAbiturFachbelegungen.get(abifach);
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.belegpruefung.AbiFaecher', 'de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_belegpruefung_AbiFaecher(obj : unknown) : AbiFaecher {
	return obj as AbiFaecher;
}
