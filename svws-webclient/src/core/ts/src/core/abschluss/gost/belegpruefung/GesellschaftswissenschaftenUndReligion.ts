import { GostFach } from '../../../../core/data/gost/GostFach';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostFachManager } from '../../../../core/abschluss/gost/GostFachManager';
import { GostKursart } from '../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { NullPointerException } from '../../../../java/lang/NullPointerException';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { ZulaessigesFach } from '../../../../core/types/fach/ZulaessigesFach';
import type { List } from '../../../../java/util/List';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class GesellschaftswissenschaftenUndReligion extends GostBelegpruefung {

	private gesellschaftswissenschaften : List<AbiturFachbelegung> | null = null;

	private geschichte : List<AbiturFachbelegung> | null = null;

	private sozialwissenschaften : List<AbiturFachbelegung> | null = null;

	private philosophie : AbiturFachbelegung | null = null;

	private sonstige_gesellschaftswissenschaften : List<AbiturFachbelegung> | null = null;

	private religion : List<AbiturFachbelegung> | null = null;

	private zusatzkursFachbelegungen : ArrayList<AbiturFachbelegung> | null = null;


	/**
	 * Erstellt eine neue Belegprüfung für den Bereich der Gesellschaftswissenschaften und Religion.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this.gesellschaftswissenschaften = this.manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH);
		this.geschichte = this.manager.getFachbelegungen(GostFachbereich.GESCHICHTE);
		this.sozialwissenschaften = this.manager.getFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		this.philosophie = this.manager.getFachbelegung(GostFachbereich.PHILOSOPHIE);
		this.sonstige_gesellschaftswissenschaften = this.manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		this.religion = this.manager.getFachbelegungen(GostFachbereich.RELIGION);
		this.zusatzkursFachbelegungen = new ArrayList();
	}

	protected pruefeEF1() : void {
		this.pruefeGesellschaftswissenschaftenEF1();
		this.pruefeReligionEF1();
	}

	/**
	 * EF1-Prüfung Punkte 8-10:
	 * Prüfe, ob eine Gesellschaftswissenschaft in EF.1 schriftlich belegt wurde und durchgängig belegbar ist
	 *    und ob Geschichte belegt wurde
	 *    und ob Sozialwissenschaften belegt wurde
	 */
	private pruefeGesellschaftswissenschaftenEF1() : void {
		if (!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this.gesellschaftswissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.GW_10);
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.GW_11);
		if (this.manager.zaehleBelegungInHalbjahren(this.geschichte, GostHalbjahr.EF1) <= 0)
			this.addFehler(GostBelegungsfehler.GE_1_INFO);
		if (this.manager.zaehleBelegungInHalbjahren(this.sozialwissenschaften, GostHalbjahr.EF1) <= 0)
			this.addFehler(GostBelegungsfehler.SW_1_INFO);
	}

	/**
	 * EF1-Prüfung Punkt 11:
	 * Prüfe, ob Religion in EF.1 belegt wurde oder ob Philosophie und eine weitere durchgehend belegbare Gesellschaftswissenschaft belegt wurde.
	 * Falls Philosophie als Ersatz für Religion gewählt wurde, zählt es nicht als durchgehend belegte Gesellschaftswissenschaft.
	 */
	private pruefeReligionEF1() : void {
		if (this.manager.pruefeBelegungExistiert(this.religion, GostHalbjahr.EF1))
			return;
		if ((!this.manager.pruefeBelegung(this.philosophie, GostHalbjahr.EF1)) || ((!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this.geschichte, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) && (!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this.sozialwissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) && (!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this.sonstige_gesellschaftswissenschaften, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)))) {
			this.addFehler(GostBelegungsfehler.RE_10);
		}
	}

	protected pruefeGesamt() : void {
		this.pruefeSchriftlichkeitEF();
		this.pruefeDurchgaengigeBelegung();
		this.pruefeDurchgaengigeBelegungUndSchriftlich();
		this.pruefeZusatzkurs(this.geschichte);
		this.pruefeBelegungGeschichte();
		this.pruefeZusatzkurs(this.sozialwissenschaften);
		this.pruefeBelegungSozialwissenschaften();
		this.pruefeReligionEF();
		this.pruefeReligionQ1();
		this.pruefeReligionKontinuitaet();
	}

	/**
	 * Gesamtprüfung Punkt 39:
	 * Prüfe, on in EF.1 und EF.2 jeweils ein Fach der Gesellschaftswissenschaften schriftlich belegt wurde
	 */
	private pruefeSchriftlichkeitEF() : void {
		if ((!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) || (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.gesellschaftswissenschaften, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			this.addFehler(GostBelegungsfehler.GW_11);
	}

	/**
	 * Gesamtprüfung Punkt 38:
	 * Prüfe, ob ein Fach der Gesellschaftswissenschaften von EF.1 bis Q2.2 durchgängig belegt wurde
	 * - Zusatzkurse zählen hier nicht als Belegung
	 */
	private pruefeDurchgaengigeBelegung() : void {
		if (!this.manager.pruefeBelegungExistiert(this.gesellschaftswissenschaften, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.GW_10);
	}

	/**
	 * Gesamtprüfung Punkt 40:
	 * Prüfe, ob ein Fach der Gesellschaftswissenschaften oder Religionslehre von EF.1 bis Q2.2 belegt
	 * und von Q1.1 bis Q2.1 schriftlich belegt wurde, damit es als potentielles Abiturfach zur Verfügung steht.
	 * - Zusatzkurse zählen hier nicht als Belegung
	 */
	private pruefeDurchgaengigeBelegungUndSchriftlich() : void {
		if (this.manager.pruefeBelegungExistiertDurchgehendSchriftlich(this.gesellschaftswissenschaften))
			return;
		if (this.manager.pruefeBelegungExistiertDurchgehendSchriftlich(this.religion))
			return;
		this.addFehler(GostBelegungsfehler.GW_12);
	}

	/**
	 * Gesamtprüfung Punkte 27, 29 und 30:
	 * Prüft, ob der Zusatzkurs genau zwei mal belegt wurde
	 *    und ob ein Zusatzkurs belegt wurde, obwohl im Halbjahr zuvor ein Geschichtskurs belegt wurde.
	 *
	 * @param fachbelegungen   die Fachbelegung für Geschichte oder Sozialwissenschaften
	 */
	private pruefeZusatzkurs(fachbelegungen : List<AbiturFachbelegung> | null) : void {
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return;
		const fachbelegungenZK : List<AbiturFachbelegung> | null = this.manager.filterBelegungKursartExistiert(fachbelegungen, GostKursart.ZK);
		if (fachbelegungenZK.isEmpty())
			return;
		if (fachbelegungenZK.size() > 1)
			this.addFehler(GostBelegungsfehler.ZK_13);
		const fachbelegung : AbiturFachbelegung | null = fachbelegungenZK.get(0);
		const fach : GostFach | null = this.manager.getFach(fachbelegung);
		if (fach === null)
			throw new NullPointerException()
		if (GostFachManager.istBilingual(fach))
			this.addFehler(GostBelegungsfehler.ZK_13);
		const zFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
		if ((zFach as unknown === ZulaessigesFach.GE as unknown) && (!this.manager.istErlaubtZusatzkursGE()))
			this.addFehler(GostBelegungsfehler.ZK_14);
		if ((zFach as unknown === ZulaessigesFach.SW as unknown) && (!this.manager.istErlaubtZusatzkursSW()))
			this.addFehler(GostBelegungsfehler.ZK_15);
		const halbjahre : List<GostHalbjahr> = this.manager.getHalbjahreKursart(fachbelegung, GostKursart.ZK);
		if (halbjahre.size() === 2) {
			const belegungQ11 : boolean = this.manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q11, GostHalbjahr.Q12);
			const belegungQ12 : boolean = this.manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q12, GostHalbjahr.Q21);
			const belegungQ21 : boolean = this.manager.pruefeBelegungMitKursart(fachbelegung, GostKursart.ZK, GostHalbjahr.Q21, GostHalbjahr.Q22);
			if ((this.zusatzkursFachbelegungen !== null) && (belegungQ11 || belegungQ12 || belegungQ21))
				this.zusatzkursFachbelegungen.add(fachbelegung);
			if ((belegungQ11 && this.manager.zaehleBelegungInHalbjahren(fachbelegungenZK, GostHalbjahr.Q21, GostHalbjahr.Q22) > 0) || (belegungQ12 && this.manager.zaehleBelegungInHalbjahren(fachbelegungenZK, GostHalbjahr.Q22) > 0))
				this.addFehler(GostBelegungsfehler.ZK_18);
		} else
			if (halbjahre.size() > 1) {
				this.addFehler(GostBelegungsfehler.ZK_12);
			}
		if (!halbjahre.isEmpty()) {
			const prevHalbjahr : GostHalbjahr | null = halbjahre.get(0).previous();
			if ((prevHalbjahr !== null) && (this.manager.pruefeBelegung(fachbelegung, prevHalbjahr)))
				this.addFehler(GostBelegungsfehler.ZK_10);
		}
		if (!halbjahre.isEmpty()) {
			if ((zFach as unknown === ZulaessigesFach.GE as unknown) && (this.manager.getBeginnZusatzkursGE() as unknown !== halbjahre.get(0) as unknown))
				this.addFehler(GostBelegungsfehler.ZK_16);
			if ((zFach as unknown === ZulaessigesFach.SW as unknown) && (this.manager.getBeginnZusatzkursSW() as unknown !== halbjahre.get(0) as unknown))
				this.addFehler(GostBelegungsfehler.ZK_17);
		}
	}

	/**
	 * Gesamtprüfung Punkt 41:
	 * Prüft, ob Geschichte korrekt belegt wurde (mind. von EF.1 bis Q1.2 oder als Zusatzkurs)
	 */
	private pruefeBelegungGeschichte() : void {
		if ((this.geschichte === null) || (this.geschichte.isEmpty())) {
			this.addFehler(GostBelegungsfehler.GE_10);
			return;
		}
		if (this.manager.pruefeBelegungExistiert(this.geschichte, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
			return;
		if (this.zusatzkursFachbelegungen !== null)
			for (const zkBelegung of this.zusatzkursFachbelegungen)
				if (this.geschichte.contains(zkBelegung))
					return;
		this.addFehler(GostBelegungsfehler.GE_10);
	}

	/**
	 * Gesamtprüfung Punkt 42:
	 * Prüft, ob Sozialwissenschaften korrekt belegt wurde (mind. von EF.1 bis Q1.2 oder als Zusatzkurs)
	 */
	private pruefeBelegungSozialwissenschaften() : void {
		if ((this.sozialwissenschaften === null) || (this.sozialwissenschaften.isEmpty())) {
			this.addFehler(GostBelegungsfehler.SW_10);
			return;
		}
		if (this.manager.pruefeBelegungExistiert(this.sozialwissenschaften, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
			return;
		if (this.zusatzkursFachbelegungen !== null)
			for (const zkBelegung of this.zusatzkursFachbelegungen)
				if (this.sozialwissenschaften.contains(zkBelegung))
					return;
		this.addFehler(GostBelegungsfehler.SW_10);
	}

	/**
	 * Gesamtprüfung Punkte 43:
	 * Prüft die Belegung von Religion und Philosophie in der EF. Wird Philosophie als Ersatz belegt, so wird auch geprüft,
	 * ob eine weitere Gesellschaftswissenschaft belegt wurde.
	 */
	private pruefeReligionEF() : void {
		for (const halbjahr of GostHalbjahr.getEinfuehrungsphase()) {
			if (this.manager.pruefeBelegungExistiertEinzeln(this.religion, halbjahr))
				continue;
			if ((!this.manager.pruefeBelegung(this.philosophie, halbjahr)) || (this.manager.pruefeDurchgaengigkeit(this.philosophie) && (this.manager.zaehleBelegungInHalbjahren(this.gesellschaftswissenschaften, halbjahr) <= 1))) {
				this.addFehler(GostBelegungsfehler.RE_10);
				break;
			}
		}
	}

	/**
	 * Gesamtprüfung Punkt 44:
	 * Prüft die Belegung von Religion und Philosophie. Wird Philosophie als Ersatz belegt, so wird auch geprüft, ob eine weitere
	 * Gesellschaftswissenschaft belegt wurde.
	 */
	private pruefeReligionQ1() : void {
		for (const halbjahr of GostHalbjahr.getHalbjahreFromJahrgang("Q1")) {
			if (this.manager.pruefeBelegungExistiertEinzeln(this.religion, halbjahr))
				continue;
			if (!this.manager.pruefeBelegung(this.philosophie, halbjahr)) {
				this.addFehler(GostBelegungsfehler.RE_10);
				return;
			}
			if (this.manager.pruefeDurchgaengigkeit(this.philosophie) && (this.manager.zaehleDurchgaengigeBelegungen(this.gesellschaftswissenschaften) > 1))
				continue;
			if (!this.manager.pruefeDurchgaengigkeit(this.philosophie) && (this.manager.zaehleDurchgaengigeBelegungen(this.gesellschaftswissenschaften) > 0))
				continue;
			if (this.manager.zaehleBelegungInHalbjahren(this.sonstige_gesellschaftswissenschaften, halbjahr) > 0)
				continue;
			if ((halbjahr as unknown === GostHalbjahr.Q11 as unknown) && (this.manager.pruefeBelegungExistiertEinzeln(this.geschichte, GostHalbjahr.Q11) || this.manager.pruefeBelegungExistiertEinzeln(this.sozialwissenschaften, GostHalbjahr.Q11)))
				continue;
			this.addFehler(GostBelegungsfehler.RE_10);
			break;
		}
	}

	/**
	 * Gesamtprüfung Punkt 55:
	 * Prüft, ob Fehler bei der Kontinuität bei Philosophie und Religion nur durch die Ersatzfachregelung bei Religion
	 * zustandekommen und damit zulässig sind.
	 */
	private pruefeReligionKontinuitaet() : void {
		if (this.philosophie === null)
			return;
		for (const belegung of this.philosophie.belegungen) {
			if (belegung === null)
				continue;
			const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel);
			if (halbjahr === null)
				continue;
			const prevHalbjahr : GostHalbjahr | null = halbjahr.previous();
			if (prevHalbjahr === null)
				continue;
			if (this.manager.pruefeBelegung(this.philosophie, prevHalbjahr))
				continue;
			if (this.manager.pruefeBelegungExistiertEinzeln(this.religion, halbjahr))
				this.addFehler(GostBelegungsfehler.E1BEL_10);
			if (!this.manager.pruefeBelegungExistiertEinzeln(this.religion, prevHalbjahr))
				this.addFehler(GostBelegungsfehler.E1BEL_10);
		}
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.GesellschaftswissenschaftenUndReligion'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_GesellschaftswissenschaftenUndReligion(obj : unknown) : GesellschaftswissenschaftenUndReligion {
	return obj as GesellschaftswissenschaftenUndReligion;
}
