import { JavaObject } from '../../../java/lang/JavaObject';
import { Naturwissenschaften } from '../../../core/abschluss/gost/belegpruefung/Naturwissenschaften';
import { Schwerpunkt } from '../../../core/abschluss/gost/belegpruefung/Schwerpunkt';
import { HashMap } from '../../../java/util/HashMap';
import { KurszahlenUndWochenstunden } from '../../../core/abschluss/gost/belegpruefung/KurszahlenUndWochenstunden';
import { GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { ArrayList } from '../../../java/util/ArrayList';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import { GostBelegpruefungErgebnis } from '../../../core/abschluss/gost/GostBelegpruefungErgebnis';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { Latinum } from '../../../core/abschluss/gost/belegpruefung/Latinum';
import { Sprachendaten } from '../../../core/data/schueler/Sprachendaten';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { Allgemeines } from '../../../core/abschluss/gost/belegpruefung/Allgemeines';
import { GostSchuelerFachwahl } from '../../../core/data/gost/GostSchuelerFachwahl';
import { Sport } from '../../../core/abschluss/gost/belegpruefung/Sport';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import { ZulaessigesFach } from '../../../core/types/fach/ZulaessigesFach';
import { List } from '../../../java/util/List';
import { Collections } from '../../../java/util/Collections';
import { GesellschaftswissenschaftenUndReligion } from '../../../core/abschluss/gost/belegpruefung/GesellschaftswissenschaftenUndReligion';
import { GostBelegungsfehler } from '../../../core/abschluss/gost/GostBelegungsfehler';
import { AbiFaecher } from '../../../core/abschluss/gost/belegpruefung/AbiFaecher';
import { HashSet } from '../../../java/util/HashSet';
import { GostFach } from '../../../core/data/gost/GostFach';
import { LiterarischKuenstlerisch } from '../../../core/abschluss/gost/belegpruefung/LiterarischKuenstlerisch';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { GostFachManager } from '../../../core/abschluss/gost/GostFachManager';
import { Abiturdaten } from '../../../core/data/gost/Abiturdaten';
import { Projektkurse } from '../../../core/abschluss/gost/belegpruefung/Projektkurse';
import { SprachendatenUtils } from '../../../core/utils/schueler/SprachendatenUtils';
import { Deutsch } from '../../../core/abschluss/gost/belegpruefung/Deutsch';
import { Fremdsprachen } from '../../../core/abschluss/gost/belegpruefung/Fremdsprachen';
import { GostBelegpruefungErgebnisFehler } from '../../../core/abschluss/gost/GostBelegpruefungErgebnisFehler';
import { Mathematik } from '../../../core/abschluss/gost/belegpruefung/Mathematik';

export class AbiturdatenManager extends JavaObject {

	/**
	 * Das Abiturdaten-Objekt, welches mithilfe dieses Managers bearbeitet wird
	 */
	private readonly abidaten : Abiturdaten;

	/**
	 * Eine Map mit der Zuordnung der zulässigen Fächer der gymnasialen Oberstufe für diesen Abiturjahrgang
	 */
	private readonly gostFaecher : HashMap<number, GostFach>;

	/**
	 * Die Art der durchzuführenden Belegprüfung
	 */
	private readonly pruefungsArt : GostBelegpruefungsArt;

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen über den Fachbereich ermöglicht
	 */
	private readonly mapFachbereiche : ArrayMap<GostFachbereich, ArrayList<AbiturFachbelegung>> = new ArrayMap(GostFachbereich.values());

	/**
	 * Die Prüfungsergebnisse der einzelnen Teilprüfungen der Belegprüfung
	 */
	private belegpruefungen : ArrayList<GostBelegpruefung> = new ArrayList();

	/**
	 * Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind.
	 */
	private belegpruefungsfehler : ArrayList<GostBelegungsfehler> = new ArrayList();

	/**
	 * Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht.
	 */
	private belegpruefungErfolgreich : boolean = false;


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param abidaten       die Abiturdaten
	 * @param gostFaecher    die Fächer der Gymnasialen Oberstufe, die bei dem Abiturjahrgang zur Verfügung stehen.
	 * @param pruefungsArt   die Art der Belegpruefung (z.B. EF1 oder GESAMT)
	 */
	public constructor(abidaten : Abiturdaten, gostFaecher : List<GostFach>, pruefungsArt : GostBelegpruefungsArt) {
		super();
		this.abidaten = abidaten;
		this.gostFaecher = new HashMap();
		for (let i : number = 0; i < gostFaecher.size(); i++) {
			const fach : GostFach | null = gostFaecher.get(i);
			if (fach !== null)
				this.gostFaecher.put(fach.id, fach);
		}
		this.pruefungsArt = pruefungsArt;
		this.init();
	}

	/**
	 * Führt die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	public getPruefungen(pruefungsArt : GostBelegpruefungsArt) : ArrayList<GostBelegpruefung> {
		const pruefungen : ArrayList<GostBelegpruefung> = new ArrayList();
		pruefungen.add(new Deutsch(this, pruefungsArt));
		const pruefungFremdsprachen : Fremdsprachen = new Fremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Latinum(this, pruefungsArt));
		pruefungen.add(new LiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new GesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Mathematik(this, pruefungsArt));
		const pruefungNaturwissenschaften : Naturwissenschaften = new Naturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Sport(this, pruefungsArt));
		const pruefungProjektkurse : Projektkurse = new Projektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		pruefungen.add(new Schwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new AbiFaecher(this, pruefungsArt));
		pruefungen.add(new KurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse));
		pruefungen.add(new Allgemeines(this, pruefungsArt));
		return pruefungen;
	}

	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public init() : void {
		if (this.abidaten === null)
			return;
		this.initMapFachbereiche();
		this.belegpruefungen = this.getPruefungen(this.pruefungsArt);
		for (let i : number = 0; i < this.belegpruefungen.size(); i++) {
			const belegpruefung : GostBelegpruefung = this.belegpruefungen.get(i);
			belegpruefung.pruefe();
		}
		this.belegpruefungsfehler = GostBelegpruefung.getBelegungsfehlerAlle(this.belegpruefungen);
		this.belegpruefungErfolgreich = GostBelegpruefung.istErfolgreich(this.belegpruefungsfehler);
	}

	/**
	 * Initialisiert bzw. reinitialisiert die Map für den schnellen Zugriff auf Fachbelegungen
	 * anhand des Fachbereichs.
	 */
	private initMapFachbereiche() : void {
		this.mapFachbereiche.clear();
		for (const fachbereich of GostFachbereich.values())
			this.mapFachbereiche.put(fachbereich, new ArrayList());
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			if (this.zaehleBelegung(fachbelegung) > 0) {
				const fach : GostFach | null = this.getFach(fachbelegung);
				const fachbereiche : List<GostFachbereich> = GostFachbereich.getBereiche(fach);
				for (const fachbereich of fachbereiche) {
					const listFachbelegungen : ArrayList<AbiturFachbelegung> | null = this.mapFachbereiche.get(fachbereich);
					if (listFachbelegungen === null)
						continue;
					listFachbelegungen.add(fachbelegung);
				}
			}
		}
	}

	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public istBewertet(halbjahr : GostHalbjahr) : boolean {
		return this.abidaten.bewertetesHalbjahr[halbjahr.id];
	}

	/**
	 * Liefert die in den Abiturdaten enthaltenen Sprachendaten.
	 *
	 * @return Die Sprachendaten (siehe {@link Sprachendaten})
	 */
	public getSprachendaten() : Sprachendaten {
		return this.abidaten.sprachendaten;
	}

	/**
	 * Berechnet die Wochenstunden, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 *
	 * @return ein Array mit den Wochenstunden für die sechs Halbjahre
	 */
	public getWochenstunden() : Array<number> {
		const stunden : Array<number> = [0, 0, 0, 0, 0, 0];
		for (let i : number = 0; i < 6; i++) {
			for (const fb of this.abidaten.fachbelegungen) {
				const hjb : AbiturFachbelegungHalbjahr | null = fb.belegungen[i];
				if ((hjb === null) || (JavaObject.equalsTranspiler("AT", (hjb.kursartKuerzel))))
					continue;
				stunden[i] += hjb.wochenstunden;
			}
		}
		return stunden;
	}

	/**
	 * Berechnet die Anzahl der anrechenbaren Kurse, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur belegt wurden.
	 *
	 * @return ein Array mit den anrechenbaren Kursen für die sechs Halbjahre
	 */
	public getAnrechenbareKurse() : Array<number> {
		const anzahl : Array<number> = [0, 0, 0, 0, 0, 0];
		const bll : GostBesondereLernleistung | null = GostBesondereLernleistung.fromKuerzel(this.abidaten.besondereLernleistung);
		for (let i : number = 0; i < 6; i++) {
			for (const fb of this.abidaten.fachbelegungen) {
				const hjb : AbiturFachbelegungHalbjahr | null = fb.belegungen[i];
				if ((hjb === null) || (JavaObject.equalsTranspiler("AT", (hjb.kursartKuerzel))))
					continue;
				const kursart : GostKursart | null = GostKursart.fromKuerzel(hjb.kursartKuerzel);
				if ((kursart as unknown !== GostKursart.VTF as unknown) && (!((kursart as unknown === GostKursart.PJK as unknown) && (bll as unknown === GostBesondereLernleistung.PROJEKTKURS as unknown))))
					anzahl[i]++;
			}
		}
		return anzahl;
	}

	private static getSchuelerFachwahlFromBelegung(belegung : AbiturFachbelegung, halbjahr : GostHalbjahr) : string | null {
		let halbjahresbelegung : AbiturFachbelegungHalbjahr | null = belegung.belegungen[halbjahr.id];
		if (halbjahresbelegung === null) {
			halbjahresbelegung = new AbiturFachbelegungHalbjahr();
			halbjahresbelegung.halbjahrKuerzel = halbjahr.kuerzel;
			belegung.belegungen[halbjahr.id] = halbjahresbelegung;
		}
		const kursart : GostKursart | null = GostKursart.fromKuerzel(halbjahresbelegung.kursartKuerzel);
		if (kursart === null)
			return (JavaObject.equalsTranspiler("", (halbjahresbelegung.kursartKuerzel)) ? null : halbjahresbelegung.kursartKuerzel);
		if ((kursart as unknown === GostKursart.ZK as unknown) || (kursart as unknown === GostKursart.LK as unknown))
			return kursart.kuerzel;
		return halbjahresbelegung.schriftlich ? "S" : "M";
	}

	/**
	 * Bestimmt die Schüler-Fachwahl für das Fach mit der übegebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Schüler-Fachwahl
	 */
	public getSchuelerFachwahl(fachID : number) : GostSchuelerFachwahl {
		const belegung : AbiturFachbelegung | null = this.getFachbelegungByID(fachID);
		if (belegung === null)
			return new GostSchuelerFachwahl();
		const wahl : GostSchuelerFachwahl = new GostSchuelerFachwahl();
		wahl.halbjahre[0] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF1);
		wahl.halbjahre[1] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF2);
		wahl.halbjahre[2] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q11);
		wahl.halbjahre[3] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q12);
		wahl.halbjahre[4] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q21);
		wahl.halbjahre[5] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q22);
		wahl.abiturFach = belegung.abiturFach;
		return wahl;
	}

	/**
	 * Liefert das Fach der gymnasialen Oberstufe für die angegeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung (siehe {@link AbiturFachbelegung})
	 *
	 * @return das Fach der gymnasialen Oberstufe (siehe {@link GostFach})
	 */
	public getFach(belegung : AbiturFachbelegung | null) : GostFach | null {
		if (belegung === null)
			return null;
		return this.gostFaecher.get(belegung.fachID);
	}

	/**
	 * Prüft, ob das Faches in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public pruefeBelegung(fachbelegung : AbiturFachbelegung | null, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (belegungHalbjahr.kursartKuerzel === null))
				return false;
		}
		return true;
	}

	/**
	 * Bestimmt die Anzahl der Fachbelegungen, die dem Fach zugeordnet sind.
	 * Wird keine gültige Fachbelegung übergeben, so wird 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 *
	 * @return die Anzahl der Belegungen des Faches
	 */
	public zaehleBelegung(fachbelegung : AbiturFachbelegung | null) : number {
		if (fachbelegung === null)
			return 0;
		let anzahl : number = 0;
		for (let i : number = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			if (fachbelegung.belegungen[i] !== null)
				anzahl++;
		}
		return anzahl;
	}

	/**
	 * Zählt die Anzahl der Belegungen für die angegebenen Fachbelegungen in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegungen      die Fachbelegungen
	 * @param halbjahre           die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren und den Fächern
	 */
	public zaehleBelegungInHalbjahren(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : number {
		if (fachbelegungen === null)
			return 0;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return 0;
		let anzahl : number = 0;
		for (const fachbelegung of fachbelegungen)
			for (const halbjahr of halbjahre)
				if (fachbelegung.belegungen[halbjahr.id] !== null)
					anzahl++;
		return anzahl;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Kursart entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungMitKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (kursart as unknown !== GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel) as unknown))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren der angegebenen Kursart
	 * entspricht.
	 * Ist keine Fachbelegung angegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen
	 * Fachbelegung kein Halbjahr angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitKursart(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitKursart(fachbelegung, kursart, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebenen Kursart hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Kursart nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungHatMindestensEinmalKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return false;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr === null)
				continue;
			if (kursart as unknown === GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel) as unknown)
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegung === null)
			return false;
		const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr === null) || (belegungHalbjahr.schriftlich === null))
			return false;
		switch (schriftlichkeit) {
			case GostSchriftlichkeit.BELIEBIG: {
				return true;
			}
			case GostSchriftlichkeit.SCHRIFTLICH: {
				return belegungHalbjahr.schriftlich!;
			}
			case GostSchriftlichkeit.MUENDLICH: {
				return !belegungHalbjahr.schriftlich!;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungMitSchriftlichkeit(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const halbjahr of halbjahre)
			if (!this.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return false;
		return true;
	}

	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public pruefeBelegungErfuelltNicht(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || ((schriftlichkeit as unknown !== GostSchriftlichkeit.BELIEBIG as unknown) && (((schriftlichkeit as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (!belegungHalbjahr.schriftlich)) || ((schriftlichkeit as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht,
	 * sofern es in dem Halbjahr überhaupt belegt wurde..
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr === null)
				continue;
			const schriftlich : boolean = (belegungHalbjahr.schriftlich !== null) && belegungHalbjahr.schriftlich;
			if (((schriftlichkeit as unknown !== GostSchriftlichkeit.BELIEBIG as unknown) && (((schriftlichkeit as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (!schriftlich)) || ((schriftlichkeit as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (schriftlich)))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebene Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return false;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr === null)
				continue;
			if ((schriftlichkeit.istSchriftlich === null) || (schriftlichkeit.istSchriftlich as unknown === belegungHalbjahr.schriftlich as unknown))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren mindestens einmal die angegebene
	 * Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return false;
		for (const fachbelegung of fachbelegungen)
			if (this.pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine Fachbelegung mit den Halbjahren existiert, sonst false
	 */
	public pruefeBelegungExistiert(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			for (const halbjahr of halbjahre) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] !== null) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit dem angegebenen Halbjahr existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls eine Fachbelegung mit dem Halbjahr existiert, sonst false
	 */
	public pruefeBelegungExistiertEinzeln(fachbelegungen : List<AbiturFachbelegung> | null, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			for (const aktFachbelegung of alleBelegungen)
				if (aktFachbelegung.belegungen[halbjahr.id] !== null)
					return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit einer durchgängigen Belegung existiert,
	 * die zumindest in der Q1 und der Q2.1 schriftlich ist.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 *
	 * @return true, falls eine durchgehend schriftliche Fachbelegung existiert, sonst false
	 */
	public pruefeBelegungExistiertDurchgehendSchriftlich(fachbelegungen : List<AbiturFachbelegung> | null) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] !== null) {
						const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = aktFachbelegung.belegungen[halbjahr.id];
						if (((halbjahr as unknown !== GostHalbjahr.Q11 as unknown) && (halbjahr as unknown !== GostHalbjahr.Q12 as unknown) && (halbjahr as unknown !== GostHalbjahr.Q21 as unknown)) || ((belegungHalbjahr !== null) && (belegungHalbjahr.schriftlich !== null) && (belegungHalbjahr.schriftlich)))
							hatHalbjahresBelegung = true;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitSchriftlichkeitEinzeln(fachbelegungen : List<AbiturFachbelegung | null> | null, schriftlichkeit : GostSchriftlichkeit, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren mindestens einmal die angegebene Kursart
	 * hat. Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertHatMindestensEinmalKursart(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungHatMindestensEinmalKursart(fachbelegung, kursart, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public pruefeBelegungExistiertErfuelltNicht(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungErfuelltNicht(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist, sofern
	 * das Fach überhaupt belegt wurde.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public pruefeBelegungExistiertErfuelltNichtFallsBelegt(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgehend belegbar ist.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegbar(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (!GostFachManager.istDurchgehendBelegbarBisQ22(this.getFach(fachbelegung)))
			return false;
		return this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre);
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegbar ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegbarExistiert(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungDurchgehendBelegbar(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegt ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich sofern das Fach durchgängig belegt wurde, da kein Halbjahr auf die
	 * Schriftlichkeit geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei eine Fachbelegung durchgängig belegt wurde und die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegtExistiert(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22) && this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Fachbelegung in mindestens einem der Halbjahre die angegebene Kursart aufweist.
	 * Existiert die Fachbelegung nicht (null), so kommt die Kursart auch nicht vor.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursart        die Kursart
	 *
	 * @return true, falls mindestens einmal die Kursart belegt wurde, sonst false
	 */
	public pruefeAufKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart) : boolean {
		if (fachbelegung === null)
			return false;
		for (const belegunghalbjahr of fachbelegung.belegungen) {
			if ((belegunghalbjahr !== null) && GostKursart.fromKuerzel(belegunghalbjahr.kursartKuerzel) as unknown === kursart as unknown)
				return true;
		}
		return false;
	}

	/**
	 * Filtert die Fachbelegungen und gibt nur die Fachbelegungen zurück, bei denen die
	 * Kursart existiert.
	 * Wird keine Fachbelegung übergeben (null oder leere Liste), so kommt auch keine
	 * Belegung mit der Kursart vor.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param kursart          die Kursart
	 *
	 * @return eine Liste mit den Fachbelegungen, welche die kursart haben
	 */
	public filterBelegungKursartExistiert(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart) : List<AbiturFachbelegung> {
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return result;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeAufKursart(fachbelegung, kursart))
				result.add(fachbelegung);
		}
		return result;
	}

	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat. Zusatzkurse können nicht für eine
	 * durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist.
	 */
	public pruefeDurchgaengigkeit(fachbelegung : AbiturFachbelegung | null) : boolean {
		if ((fachbelegung === null) || (this.pruefeAufKursart(fachbelegung, GostKursart.ZK)))
			return false;
		return this.pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
	}

	/**
	 * Zählt die Fachbelegungen, welche eine durchgängige Belegung aufweisen. Zusatzkurse zählen
	 * nicht für eine durchgängige Belegung.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 *
	 * @param fachbelegungen   die zu überprüfenden Fachbelegungen
	 *
	 * @return die Anzahl der durchgängigen Belegungen
	 */
	public zaehleDurchgaengigeBelegungen(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		let anzahl : number = 0;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			if (fachbelegung.belegungen[GostHalbjahr.EF1.id] === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			const halbjahre : Array<GostHalbjahr> = [GostHalbjahr.EF1, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22];
			for (const halbjahr of halbjahre) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] !== null) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				anzahl++;
		}
		return anzahl;
	}

	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat und prüft die Schriftlichkeit
	 * in der Qualifikationsphase. Ein Fach in der Qualifikationsphase gilt als Schriftlich belegt,
	 * sofern die ersten 3 Halbjahre der Qualifikationsphase schriftlich belegt wurden.
	 * - Zusatzkurse können nicht für eine durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende die zu überprüfenden Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist und die Schriftlichkeit den Anforderungen genügt.
	 */
	public pruefeDurchgaengigkeitSchriftlich(fachbelegung : AbiturFachbelegung | null) : boolean {
		if (!this.pruefeDurchgaengigkeit(fachbelegung))
			return false;
		return this.pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
	}

	/**
	 * Prüft, ob unter den angegebenen Fachbelegungen ein Fach als Abiturfach von einem der angegebenen Arten
	 * gewählt wurde. Wird keine Art angebeben, so wird jede Fachbelegung akzeptiert und true zurückgegeben.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param arten            die Arten der Abiturfächer
	 *
	 * @return true, falls unter den Fachbelegungen mindestens ein Fach als Abiturfach von einem der
	 *         angegebenen Arten gewählt wurde und false sonst
	 */
	public pruefeExistiertAbiFach(fachbelegungen : List<AbiturFachbelegung> | null, ...arten : Array<GostAbiturFach>) : boolean {
		if ((arten === null) || (arten.length === 0))
			return true;
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen)
			for (const art of arten) {
				const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
				if (abiturFach as unknown === art as unknown)
					return true;
			}
		return false;
	}

	/**
	 * Prüft anhand des Statistik-Kürzels, ob in dem angegebenen Halbjahr eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.
	 *
	 * @param halbjahr   das zu prüfende Halbjahr
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public hatDoppelteFachbelegungInHalbjahr(halbjahr : GostHalbjahr) : boolean {
		const set : HashSet<string> = new HashSet();
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if (fach === null)
				continue;
			const belegung : AbiturFachbelegungHalbjahr | null = this.getBelegungHalbjahr(fb, halbjahr, GostSchriftlichkeit.BELIEBIG);
			if (belegung === null)
				continue;
			let kuerzel : string | null = GostFachManager.getFremdsprache(fach);
			if (kuerzel === null)
				kuerzel = fach.kuerzel === null ? "" : fach.kuerzel;
			if (!set.add(kuerzel) && (!JavaObject.equalsTranspiler("VX", (kuerzel))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft anhand des Statistik-Kürzels, ob in einem der angegebenen Halbjahre eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.
	 *
	 * @param halbjahre   die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public hatDoppelteFachbelegung(...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((halbjahre === null) || (halbjahre.length === 0))
			return false;
		for (const halbjahr of halbjahre)
			if (this.hatDoppelteFachbelegungInHalbjahr(halbjahr))
				return true;
		return false;
	}

	/**
	 * Gibt zurück, ob der Projektkurs als besondere Lernleistung verwendet wird.
	 *
	 * @return true, falls der Projektkurs als besondere Lernleistung verwendet wird
	 */
	public istProjektKursBesondereLernleistung() : boolean {
		return (GostBesondereLernleistung.PROJEKTKURS.is(this.abidaten.besondereLernleistung));
	}

	/**
	 * Bestimmt die Fachbelegung des Faches mit der angegebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public getFachbelegungByID(fachID : number) : AbiturFachbelegung | null {
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && (fachID === fach.id))
				return fb;
		}
		return null;
	}

	/**
	 * Bestimmt die erste Fachbelegung mit dem angegebenen Statistik-Kürzel
	 *
	 * @param kuerzel          das Kürzel des Faches, kann null sein (dann wird auch null zurückgegeben)
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public getFachbelegungByKuerzel(kuerzel : string | null) : AbiturFachbelegung | null {
		if ((kuerzel === null) || (JavaObject.equalsTranspiler("", (kuerzel))))
			return null;
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && (JavaObject.equalsTranspiler(kuerzel, (fach.kuerzel))))
				return fb;
		}
		return null;
	}

	/**
	 * Liefert alle Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public getFachbelegungen(...fachbereiche : Array<GostFachbereich>) : List<AbiturFachbelegung> {
		if ((fachbereiche === null) || (fachbereiche.length === 0))
			return this.abidaten.fachbelegungen;
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		for (const fachbereich of fachbereiche) {
			const fachbelegungen : List<AbiturFachbelegung> | null = this.mapFachbereiche.get(fachbereich);
			if (fachbelegungen === null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}

	/**
	 * Liefert alle Fachbelegungen, die bilingual unterrichtet wurden.
	 *
	 * @return eine Liste der Fachbelegungen
	 */
	public getFachbelegungenBilingual() : List<AbiturFachbelegung> {
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			if (this.zaehleBelegung(fb) <= 0)
				continue;
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && (!GostFachbereich.FREMDSPRACHE.hat(fach)) && (!GostFachbereich.DEUTSCH.hat(fach)) && (fach.biliSprache !== null) && (!JavaObject.equalsTranspiler("D", (fach.biliSprache))))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen auf neu einsetzende Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterFremdspracheNeuEinsetzend(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen auf fortgeführte Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterFremdspracheFortgefuehrt(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && fach.istFremdsprache && !fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen danach, ob sie durchgehend belegbar sind
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterDurchgehendBelegbar(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		if (fachbelegungen === null)
			return result;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if (GostFachManager.istDurchgehendBelegbarBisQ22(fach))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen. Es werden nur Fachbelegungen behalten, die in den angegebenen Halbjahren eine Belegung aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterBelegungen(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		for (const fb of fachbelegungen) {
			if (this.pruefeBelegung(fb, ...halbjahre))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public zaehleBelegungenDurchgaengig(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		const faecher : HashSet<ZulaessigesFach | null> = new HashSet();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fb.fachID);
			if (fach === null)
				continue;
			const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			if (zulFach as unknown !== ZulaessigesFach.DEFAULT as unknown)
				faecher.add(zulFach);
		}
		let count : number = 0;
		for (const zulFach of faecher) {
			let vorhanden : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let belegung_vorhanden : boolean = false;
				for (const fb of fachbelegungen) {
					const fbFach : GostFach | null = this.gostFaecher.get(fb.fachID);
					if (fbFach === null)
						continue;
					const fbZulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					if ((zulFach as unknown === fbZulFach as unknown) && (fb.belegungen[halbjahr.id] !== null)) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}

	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public zaehleBelegungenDurchgaengigSchriftlichInQPhase(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		const faecher : HashSet<ZulaessigesFach | null> = new HashSet();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fb.fachID);
			if (fach === null)
				continue;
			const zulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			if (zulFach as unknown !== ZulaessigesFach.DEFAULT as unknown)
				faecher.add(zulFach);
		}
		let count : number = 0;
		for (const zulFach of faecher) {
			let vorhanden : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let belegung_vorhanden : boolean = false;
				for (const fb of fachbelegungen) {
					const fbFach : GostFach | null = this.gostFaecher.get(fb.fachID);
					if (fbFach === null)
						continue;
					const fbZulFach : ZulaessigesFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					if (zulFach as unknown === fbZulFach as unknown) {
						const belegung : AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
						if (belegung !== null) {
							let istSchriftlichkeitOK : boolean = true;
							if (((halbjahr as unknown === GostHalbjahr.Q11 as unknown) || (halbjahr as unknown === GostHalbjahr.Q12 as unknown) || (halbjahr as unknown === GostHalbjahr.Q21 as unknown)) && ((belegung.schriftlich === null) || (!belegung.schriftlich)))
								istSchriftlichkeitOK = false;
							if (istSchriftlichkeitOK) {
								belegung_vorhanden = true;
								break;
							}
						}
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}

	/**
	 * Filtert die Fachbelegungen. Es werden nur Belegungen behalten, die in den angegebenen Halbjahren die geforderte Schriftlichkeit aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param schriftlichkeit   die geforderte Schriftlichkeit
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterBelegungenMitSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList();
		for (const fb of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeit(fb, schriftlichkeit, ...halbjahre))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Liefert die erste Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public getFachbelegung(fachbereich : GostFachbereich) : AbiturFachbelegung | null {
		const faecher : ArrayList<AbiturFachbelegung | null> | null = this.mapFachbereiche.get(fachbereich);
		if ((faecher === null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}

	/**
	 * Liefert alle Fachbelegungen mit dem angegebenen Statistk-Kürzel des Faches
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return eine Liste mit den Fachbelegungen
	 */
	public getFachbelegungByFachkuerzel(kuerzel : string | null) : List<AbiturFachbelegung> {
		const fachbelegungen : ArrayList<AbiturFachbelegung> = new ArrayList();
		if (kuerzel === null)
			return fachbelegungen;
		const tmpFachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of tmpFachbelegungen) {
			const fach : GostFach | null = this.gostFaecher.get(fachbelegung.fachID);
			if ((fach === null) || (!JavaObject.equalsTranspiler(kuerzel, (fach.kuerzel))))
				continue;
			fachbelegungen.add(fachbelegung);
		}
		return fachbelegungen;
	}

	/**
	 * Prüft, ob der Kurs in dem angegebenen Halbjahr mit der angegebenen Schriftlichkeit belegt ist
	 * und gibt ggf. die Belegung zurück.
	 *
	 * @param fachbelegung   die Abiturfachbelegung aus welcher die Belegungsinformationen für das Halbjahr entnommen wird
	 * @param halbjahr       das Halbjahr, in welchem die Belegung gesucht wird.
	 * @param schriftlich    gibt an, ob das Fach schriftlich oder mündlich belegt sein muss
	 *
	 * @return die Belegungsinformationen zu dem Fach
	 */
	public getBelegungHalbjahr(fachbelegung : AbiturFachbelegung, halbjahr : GostHalbjahr, schriftlich : GostSchriftlichkeit) : AbiturFachbelegungHalbjahr | null {
		const belegung : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
		return ((belegung !== null) && ((schriftlich as unknown === GostSchriftlichkeit.BELIEBIG as unknown) || ((schriftlich as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (belegung.schriftlich)) || ((schriftlich as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (!belegung.schriftlich)))) ? belegung : null;
	}

	/**
	 * Liefert die erste Sprachbelegung - sofern eine existiert
	 *
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return die Fachbelegung für die Sprache
	 */
	public getSprachbelegung(sprache : string | null) : AbiturFachbelegung | null {
		if (sprache === null)
			return null;
		const fachbelegungen : ArrayList<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach === null) || (!GostFachManager.istFremdsprachenfach(fach, sprache)))
				continue;
			if (JavaObject.equalsTranspiler(sprache, (GostFachManager.getFremdsprache(fach))))
				return fb;
		}
		return null;
	}

	/**
	 * Liefert für die übergebene Fachbelegung die Halbjahre, in denen das Fach mit einer der angebenen
	 * Kursarten belegt wurde. Ist keine Kursart angegeben, so werden die Halbjahre aller Belegungen
	 * zurückgegeben. Ist keine Fachbelegung angegeben, so wird eine leere Liste zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursarten      die Kursarten
	 *
	 * @return eine Liste der Halbjahre in den das Fach mit einer der Kursarten belegt wurde
	 */
	public getHalbjahreKursart(fachbelegung : AbiturFachbelegung | null, ...kursarten : Array<GostKursart>) : ArrayList<GostHalbjahr> {
		const halbjahre : ArrayList<GostHalbjahr> = new ArrayList();
		if (fachbelegung !== null) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if (belegungHalbjahr === null)
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				if ((kursarten === null) || (kursarten.length === 0)) {
					halbjahre.add(halbjahr);
					continue;
				}
				for (const kursart of kursarten) {
					if (JavaObject.equalsTranspiler(kursart, (GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel)))) {
						halbjahre.add(halbjahr);
						break;
					}
				}
			}
		}
		return halbjahre;
	}

	/**
	 * Gibt die Sprache des bilingualen Bildungsgang zurück oder null, falls keiner gewählt wurde.
	 *
	 * @return die Sprache des bilingualen Bildungsgang oder null
	 */
	public getBiligualenBildungsgang() : string | null {
		return this.abidaten.bilingualeSprache;
	}

	/**
	 * Prüft bei der Sprachenfolge, ob eine laut Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine fortgeführte Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public hatFortgefuehrteFremdspracheInSprachendaten(fremdsprachen : List<AbiturFachbelegung> | null) : boolean {
		if (fremdsprachen === null)
			return false;
		if (this.abidaten.sprachendaten === null)
			return false;
		for (const fs of fremdsprachen) {
			const fach : GostFach | null = this.getFach(fs);
			if ((fach === null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.abidaten.sprachendaten, GostFachManager.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft bei der Sprachenfolge, ob für eine laut Sprachenfolge neu einsetzende
	 * Fremdsprache fehlerhafterweise ein Kurs in einer fortgeführten Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine neu einsetzende Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public hatNeuEinsetzendeFremdspracheInSprachendaten(fremdsprachen : List<AbiturFachbelegung> | null) : boolean {
		if (fremdsprachen === null)
			return false;
		if (this.abidaten.sprachendaten === null)
			return false;
		for (const fs of fremdsprachen) {
			const fach : GostFach | null = this.getFach(fs);
			if ((fach === null) || (!fach.istFremdsprache))
				continue;
			if (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.abidaten.sprachendaten, GostFachManager.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung seit der EF1 vorhanden ist. Hierbei werden
	 * Zusatz-, Vertiefungs- und Projektkurse auch als später einsetzend akzeptiert.
	 * Dies gilt auch für Literatur, instrumental- und vokalpraktische Kurse sowie
	 * für Religion und Philosophie.
	 *
	 * @param fachbelegung   die Abiturfachbelegungen, die geprüft werden
	 *
	 * @return true, falls das Fach seit EF1 durchgängig belegt wurde oder eine der Ausnahmen zutrifft, sonsta false
	 */
	public istBelegtSeitEF(fachbelegung : AbiturFachbelegung) : boolean {
		const fach : GostFach | null = this.getFach(fachbelegung);
		if (fach === null)
			return false;
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return true;
		if (GostFachbereich.RELIGION.hat(fach))
			return true;
		if (JavaObject.equalsTranspiler("PL", (fach.kuerzel)))
			return true;
		for (const belegung of fachbelegung.belegungen) {
			if (belegung === null)
				continue;
			const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel);
			const kursart : GostKursart | null = GostKursart.fromKuerzel(belegung.kursartKuerzel);
			if ((halbjahr === null) || (kursart === null))
				continue;
			if ((kursart as unknown === GostKursart.ZK as unknown) || (kursart as unknown === GostKursart.PJK as unknown) || (kursart as unknown === GostKursart.VTF as unknown))
				continue;
			const prevHalbjahr : GostHalbjahr | null = halbjahr.previous();
			if (prevHalbjahr === null)
				continue;
			if (fachbelegung.belegungen[prevHalbjahr.id] === null) {
				const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
				if ((alleBelegungen === null) || (alleBelegungen.size() <= 1))
					return false;
				if (!this.pruefeBelegungExistiert(alleBelegungen, prevHalbjahr))
					return false;
			}
		}
		return true;
	}

	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public getBelegpruefungErgebnis() : GostBelegpruefungErgebnis {
		const ergebnis : GostBelegpruefungErgebnis = new GostBelegpruefungErgebnis();
		ergebnis.erfolgreich = this.belegpruefungErfolgreich;
		for (let i : number = 0; i < this.belegpruefungsfehler.size(); i++) {
			const fehler : GostBelegungsfehler = this.belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new GostBelegpruefungErgebnisFehler(fehler, this.pruefungsArt));
		}
		return ergebnis;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.AbiturdatenManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_AbiturdatenManager(obj : unknown) : AbiturdatenManager {
	return obj as AbiturdatenManager;
}
