import { JavaObject } from '../../../java/lang/JavaObject';
import { UvAlgorithmusDynDatenRegel29 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel29';
import { UvAlgorithmusDynDatenRegel28 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel28';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import { UvAlgorithmusDynDatenRegel27 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel27';
import { UvAlgorithmusDynDatenRegel26 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel26';
import { UvBlockungRegelTyp } from '../../../core/data/uv/regel/UvBlockungRegelTyp';
import { HashMap } from '../../../java/util/HashMap';
import { UvAlgorithmusDynDatenRegel23 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel23';
import { UvAlgorithmusDynDatenRegel22 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel22';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvAlgorithmusDynDatenRegel21 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel21';
import { UvAlgorithmusDynDatenRegel20 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel20';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaString } from '../../../java/lang/JavaString';
import { LogLevel } from '../../../core/logger/LogLevel';
import type { Comparator } from '../../../java/util/Comparator';
import { Random } from '../../../java/util/Random';
import type { List } from '../../../java/util/List';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import { UvAlgorithmusDynDatenRegel39 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel39';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDatenRegel38 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel38';
import { UvAlgorithmusDynDatenRegel37 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel37';
import { UvAlgorithmusDynDatenRegel36 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel36';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { UvAlgorithmusDynDatenRegel30 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel30';
import { UvAlgorithmusDynDatenFach } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenFach';
import { UvLerngruppe } from '../../../core/data/uv/UvLerngruppe';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';
import { Arrays } from '../../../java/util/Arrays';
import type { JavaMap } from '../../../java/util/JavaMap';
import { UvAlgorithmusDynDatenUndo } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenUndo';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { UvAlgorithmusDynDatenRegel06 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel06';
import { UvAlgorithmusDynDatenRegel04 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel04';
import { UvAlgorithmusDynDatenRegel02 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel02';
import { UvAlgorithmusDynDatenRegel45 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel45';
import { UvAlgorithmusDynDatenRegel43 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel43';
import { UvAlgorithmusDynDatenRegel42 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel42';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { UvAlgorithmusDynDatenRegel41 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel41';
import { UvAlgorithmusDynDatenRegel40 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel40';
import { Logger } from '../../../core/logger/Logger';
import { System } from '../../../java/lang/System';
import { UvAlgorithmusDynDatenJahrgang } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenJahrgang';
import type { JavaIterator } from '../../../java/util/JavaIterator';
import { UvAlgorithmusDynDatenRegel08 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel08';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvLerngruppenLehrer } from '../../../core/data/uv/UvLerngruppenLehrer';
import { UvAlgorithmusDynDatenRegel17 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel17';
import { UvAlgorithmusDynDatenRegel16 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel16';
import { UvAlgorithmusDynDatenRegel15 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel15';
import { UvRegelManager } from '../../../core/utils/uv/UvRegelManager';
import { UvAlgorithmusDynDatenRegel12 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel12';
import { UvAlgorithmusDynDatenRegel10 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel10';
import { UvFach } from '../../../core/data/uv/UvFach';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvBlockungRegelPrioritaet } from '../../../core/data/uv/regel/UvBlockungRegelPrioritaet';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import { UvKlassenLehrer } from '../../../core/data/uv/UvKlassenLehrer';
import { UvBlockungRegel } from '../../../core/data/uv/regel/UvBlockungRegel';

export class UvAlgorithmusDynDaten extends JavaObject {

	/**
	 * Debug-Ausgaben.
	 */
	private static readonly LOG_ADDITIONAL: boolean = false;

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	private readonly log: Logger;

	/**
	 * Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung.
	 */
	private readonly rnd: Random;

	/**
	 * Die Eingabedaten von der GUI (UV).
	 */
	private readonly man: UvManager;

	/**
	 * Die Eingabedaten von der GUI (UV-Regeln).
	 */
	private readonly manRegeln: UvRegelManager;

	/**
	 * Der Planungsabschnitt auf den sich diese Berechnung bezieht.
	 */
	private readonly planungsabschnitt: UvPlanungsabschnitt;

	/**
	 * Die Menge aller Lehrer (dynamisch).
	 */
	private readonly aLehrkraft: Array<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Menge aller Lerngruppen (dynamisch).
	 */
	private readonly aLerngruppe: Array<UvAlgorithmusDynDatenLerngruppe>;

	/**
	 * Die Menge aller Klassen (dynamisch).
	 */
	private readonly aKlasse: Array<UvAlgorithmusDynDatenKlasse>;

	/**
	 * Die Menge aller Jahrgänge (dynamisch).
	 */
	private readonly aJahrgaenge: Array<UvAlgorithmusDynDatenJahrgang>;

	/**
	 * Die Menge aller Fächer (dynamisch).
	 */
	private readonly aFaecher: Array<UvAlgorithmusDynDatenFach>;

	/**
	 * Mapping von der externen Lehrkraft-ID zum internen Objekt.
	 */
	private readonly mapIDzuDynLehrkraft: JavaMap<number, UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Mapping von der externen Lerngruppen-ID zum internen Objekt.
	 */
	private readonly mapIDzuDynLerngruppe: JavaMap<number, UvAlgorithmusDynDatenLerngruppe>;

	/**
	 * Mapping von der externen Klassen-ID zum internen Objekt.
	 */
	private readonly mapIDzuDynKlasse: JavaMap<number, UvAlgorithmusDynDatenKlasse>;

	/**
	 * Mapping von der externen Jahrgang-ID zum internen Objekt.
	 */
	private readonly mapIDzuDynJahrgang: JavaMap<number, UvAlgorithmusDynDatenJahrgang>;

	/**
	 * Mapping von der externen Fach-ID zum internen Objekt.
	 */
	private readonly mapIDzuDynFach: JavaMap<number, UvAlgorithmusDynDatenFach>;

	/**
	 * Die Menge aller Undo-Objekte um Manipulationen schnell rückgängig zu machen.
	 */
	private readonly undos: List<UvAlgorithmusDynDatenUndo>;

	/**
	 * Bewertung des Malus.
	 */
	private readonly malus: Array<number>;

	/**
	 * Zur Speicherung der Bewertung des Malus.
	 */
	private readonly malusSave: Array<number>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf (Lerngruppe, Lehrkraft) beziehen.
	 */
	private readonly mapLerngruppeLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf (Klasse, Lehrkraft) beziehen.
	 */
	private readonly mapKlasseLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf (Fach, Lehrkraft) beziehen.
	 */
	private readonly mapFachLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;


	/**
	 * Der Konstruktor.
	 *
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param rnd                 Ein Random-Objekt.
	 * @param man                 Ein {@link UvManager}-Objekt, der alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, der alle Regeln hat.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt} auf den sich die Manager-Daten beziehen.
	 */
	public constructor(log: Logger, rnd: Random, man: UvManager, manRegeln: UvRegelManager, planungsabschnitt: UvPlanungsabschnitt) {
		super();
		this.log = log;
		this.rnd = rnd;
		this.man = man;
		this.planungsabschnitt = planungsabschnitt;
		this.manRegeln = manRegeln;
		this.undos = new ArrayList();
		this.mapIDzuDynLehrkraft = new HashMap();
		this.mapIDzuDynKlasse = new HashMap();
		this.mapIDzuDynLerngruppe = new HashMap();
		this.mapIDzuDynJahrgang = new HashMap();
		this.mapIDzuDynFach = new HashMap();
		this.malus = Array(UvBlockungRegelPrioritaet.values().length).fill(0);
		this.malusSave = Array(this.malus.length).fill(0);
		for (let i: number = 0; i < this.malus.length; i++) {
			this.malus[i] = 0;
			this.malusSave[i] = 0;
		}
		this.aFaecher = this.createFaecher();
		this.aJahrgaenge = this.createJahrgaenge();
		this.aLehrkraft = this.createLehrkraefte();
		this.aKlasse = this.createKlassen();
		this.aLerngruppe = this.createLerngruppen();
		this.initLerngruppenLehrkraftPotentielle();
		this.mapLerngruppeLehrkraftZuRegeln = new ArrayList();
		for (let i: number = 0; i < this.aLerngruppe.length; i++) {
			this.mapLerngruppeLehrkraftZuRegeln.add(new ArrayList());
			for (let j: number = 0; j < this.aLehrkraft.length; j++) {
				this.mapLerngruppeLehrkraftZuRegeln.get(i).add(new ArrayList());
			}
		}
		this.mapKlasseLehrkraftZuRegeln = new ArrayList();
		for (let i: number = 0; i < this.aKlasse.length; i++) {
			this.mapKlasseLehrkraftZuRegeln.add(new ArrayList());
			for (let j: number = 0; j < this.aLehrkraft.length; j++) {
				this.mapKlasseLehrkraftZuRegeln.get(i).add(new ArrayList());
			}
		}
		this.mapFachLehrkraftZuRegeln = new ArrayList();
		for (let i: number = 0; i < this.aFaecher.length; i++) {
			this.mapFachLehrkraftZuRegeln.add(new ArrayList());
			for (let j: number = 0; j < this.aLehrkraft.length; j++) {
				this.mapFachLehrkraftZuRegeln.get(i).add(new ArrayList());
			}
		}
		for (let i: number = 0; i < this.aLerngruppe.length; i++) {
			const sollLehrkraftAnzahl: number = 1;
			const prioritaet: number = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			const r28: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel28(this.malus, this.aLerngruppe[i], sollLehrkraftAnzahl, prioritaet);
			this.aLerngruppe[i].regeln.add(r28);
		}
		for (let i: number = 0; i < this.aLehrkraft.length; i++) {
			const aktiviert: boolean = true;
			const prioritaet: number = UvBlockungRegelPrioritaet.MITTEL.nr;
			const r45: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel45(this.malus, this.aLehrkraft[i], aktiviert, prioritaet);
			this.aLehrkraft[i].regeln.add(r45);
		}
		for (let i: number = 0; i < this.aKlasse.length; i++) {
			const sollLeitung1: number = 1;
			const prioritaet1: number = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			const r02: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel02(this.malus, this.aKlasse[i], sollLeitung1, prioritaet1);
			this.aKlasse[i].regeln.add(r02);
			const sollLeitung2: number = 1;
			const prioritaet2: number = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			const r04: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel04(this.malus, this.aKlasse[i], sollLeitung2, prioritaet2);
			this.aKlasse[i].regeln.add(r04);
		}
		this.initRegeln();
		if (UvAlgorithmusDynDaten.LOG_ADDITIONAL) {
			this.logging();
		}
	}

	private createFaecher(): Array<UvAlgorithmusDynDatenFach> {
		const uvFachMenge: List<UvFach> = new ArrayList<UvFach>(this.man.fachGetMengeAsList());
		const temp: Array<UvAlgorithmusDynDatenFach> = Array(uvFachMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvFach: UvFach = uvFachMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenFach(i, uvFach);
			if (this.mapIDzuDynFach.put(uvFach.id, temp[i]) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Bei der Initialisierung der Fächer gab es die ID=%d doppelt!", uvFach.id));
			}
		}
		return temp;
	}

	private createJahrgaenge(): Array<UvAlgorithmusDynDatenJahrgang> {
		const uvJahrgangMenge: List<JahrgangsDaten> = new ArrayList<JahrgangsDaten>(this.man.jahrgangsdatenGetMenge());
		const temp: Array<UvAlgorithmusDynDatenJahrgang> = Array(uvJahrgangMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvJahrgang: JahrgangsDaten = uvJahrgangMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenJahrgang(i, uvJahrgang);
			if (this.mapIDzuDynJahrgang.put(uvJahrgang.id, temp[i]) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Bei der Initialisierung der Jahrgänge gab es die ID=%d doppelt!", uvJahrgang.id));
			}
		}
		return temp;
	}

	private createLehrkraefte(): Array<UvAlgorithmusDynDatenLehrkraft> {
		const uvLehrerMenge: List<UvLehrer> = this.man.lehrerGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		const temp: Array<UvAlgorithmusDynDatenLehrkraft> = Array(uvLehrerMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvLehrer: UvLehrer = uvLehrerMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenLehrkraft(i, this.log, this.man, this.planungsabschnitt, uvLehrer);
			if (this.mapIDzuDynLehrkraft.put(uvLehrer.id, temp[i]) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Bei der Initialisierung der Lehrkräfte gab es die ID=%d doppelt!", uvLehrer.id));
			}
		}
		return temp;
	}

	private createKlassen(): Array<UvAlgorithmusDynDatenKlasse> {
		const uvKlassenMenge: List<UvKlasse> = this.man.klasseGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		const temp: Array<UvAlgorithmusDynDatenKlasse> = Array(uvKlassenMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvKlasse: UvKlasse = uvKlassenMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenKlasse(this.log, this.rnd, i, uvKlasse, this.aLehrkraft);
			if (this.mapIDzuDynKlasse.put(uvKlasse.id, temp[i]) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Bei der Initialisierung der Klassen gab es die ID=%d doppelt!", uvKlasse.id));
			}
		}
		return temp;
	}

	private createLerngruppen(): Array<UvAlgorithmusDynDatenLerngruppe> {
		const uvLerngruppenMenge: List<UvLerngruppe> = this.man.lerngruppeGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		const temp: Array<UvAlgorithmusDynDatenLerngruppe> = Array(uvLerngruppenMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvLerngruppe: UvLerngruppe = uvLerngruppenMenge.get(i);
			const klassenmenge: List<UvAlgorithmusDynDatenKlasse> = this.createKlassenOfLerngruppe(uvLerngruppe);
			const jahrgangmenge: List<UvAlgorithmusDynDatenJahrgang> = this.createJahrgaengeOfLerngruppe(uvLerngruppe);
			temp[i] = new UvAlgorithmusDynDatenLerngruppe(i, this, this.man, this.planungsabschnitt, klassenmenge, jahrgangmenge);
			if (this.mapIDzuDynLerngruppe.put(uvLerngruppe.id, temp[i]) !== null) {
				throw new DeveloperNotificationException(JavaString.format("Bei der Initialisierung der Lerngruppen gab es die ID=%d doppelt!", uvLerngruppe.id));
			}
		}
		return temp;
	}

	private createJahrgaengeOfLerngruppe(uvLerngruppe: UvLerngruppe): List<UvAlgorithmusDynDatenJahrgang> {
		const list: List<JahrgangsDaten> = this.man.jahrgangsdatenGetMengeByLerngruppe(uvLerngruppe);
		const jahrgangmenge: List<UvAlgorithmusDynDatenJahrgang> = new ArrayList<UvAlgorithmusDynDatenJahrgang>();
		for (let i: number = 0; i < list.size(); i++) {
			const uvJahrgang: JahrgangsDaten = list.get(i);
			const dynJahrgang: UvAlgorithmusDynDatenJahrgang | null = this.mapIDzuDynJahrgang.get(uvJahrgang.id);
			if (dynJahrgang === null) {
				throw new DeveloperNotificationException(JavaString.format("Das Mapping des Jahrgangs UV-ID=%d zu DYN würde nicht gefunden!", uvJahrgang.id));
			}
			jahrgangmenge.add(dynJahrgang);
		}
		return jahrgangmenge;
	}

	private createKlassenOfLerngruppe(uvLerngruppe: UvLerngruppe): List<UvAlgorithmusDynDatenKlasse> {
		const list: List<UvKlasse> = this.man.klasseGetMengeByLerngruppe(uvLerngruppe);
		const klassenmenge: List<UvAlgorithmusDynDatenKlasse> = new ArrayList<UvAlgorithmusDynDatenKlasse>();
		for (let i: number = 0; i < list.size(); i++) {
			const uvKlasse: UvKlasse = list.get(i);
			const dynKlasse: UvAlgorithmusDynDatenKlasse | null = this.mapIDzuDynKlasse.get(uvKlasse.id);
			if (dynKlasse === null) {
				throw new DeveloperNotificationException(JavaString.format("Das Mapping der Klasse UV-ID=%d zu DYN würde nicht gefunden!", uvKlasse.id));
			}
			klassenmenge.add(dynKlasse);
		}
		return klassenmenge;
	}

	/**
	 * Liefert das {@link UvAlgorithmusDynDatenFach} der Lerngruppe.
	 *
	 * @param uvLerngruppe   Die angefragte Lerngruppe.
	 *
	 * @return das {@link UvAlgorithmusDynDatenFach} der Lerngruppe.
	 */
	public gibDynFachByLerngruppe(uvLerngruppe: UvLerngruppe): UvAlgorithmusDynDatenFach {
		const dynFach: UvAlgorithmusDynDatenFach | null = this.mapIDzuDynFach.get(uvLerngruppe.idFach);
		if (dynFach === null) {
			throw new DeveloperNotificationException(JavaString.format("Das Mapping des Faches UV-ID=%d zu DYN würde nicht gefunden!", uvLerngruppe.id));
		}
		return dynFach;
	}

	/**
	 * Liefert den {@link Logger}.
	 *
	 * @return den {@link Logger}.
	 */
	public gibLogger(): Logger {
		return this.log;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	public gibRandom(): Random {
		return this.rnd;
	}

	private gibLerngruppeZufaellig(): UvAlgorithmusDynDatenLerngruppe | null {
		if (this.aLerngruppe.length === 0) {
			return null;
		}
		return this.aLerngruppe[this.rnd.nextInt(this.aLerngruppe.length)];
	}

	private gibKlasseZufaellig(): UvAlgorithmusDynDatenKlasse | null {
		if (this.aKlasse.length === 0) {
			return null;
		}
		return this.aKlasse[this.rnd.nextInt(this.aKlasse.length)];
	}

	/**
	 * Liefert die aktuelle Lehrkraft-Lerngruppen-Zuordnung.
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuelle Lehrkraft-Lerngruppen-Zuordnung.
	 */
	public gibAktuelleZuordnung(): List<UvLerngruppenLehrer> {
		const list: List<UvLerngruppenLehrer> = new ArrayList<UvLerngruppenLehrer>();
		for (const lerngruppe of this.aLerngruppe) {
			const listLehrkraefte: List<UvAlgorithmusDynDatenLehrkraft> = lerngruppe.lehrkraefteZugeordnetAlle;
			for (let i: number = 0; i < listLehrkraefte.size(); i++) {
				const le: UvLerngruppenLehrer = new UvLerngruppenLehrer();
				le.id = -1;
				le.idLehrer = listLehrkraefte.get(i).uvID;
				le.idLerngruppe = lerngruppe.uvID;
				le.idPlanungsabschnitt = this.planungsabschnitt.id;
				le.reihenfolge = i + 1;
				le.wochenstunden = lerngruppe.wochenstundenVorgesehenGekuerzt;
				le.wochenstundenAngerechnet = lerngruppe.wochenstundenVorgesehenGekuerzt;
				list.add(le);
			}
		}
		return list;
	}

	/**
	 * Liefert die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuellen Klasse-Lehrkraft-Zuordnungen (Klassenleitungen).
	 */
	public gibAktuelleKlassenleitungen(): List<UvKlassenLehrer> {
		const list: List<UvKlassenLehrer> = new ArrayList<UvKlassenLehrer>();
		for (const klasse of this.aKlasse) {
			let reihenfolge: number = 1;
			for (const leitung1und2 of klasse.gibAktuelleMengeLeitung1und2()) {
				const kl: UvKlassenLehrer = new UvKlassenLehrer();
				kl.id = -1;
				kl.idPlanungsabschnitt = this.planungsabschnitt.id;
				kl.idKlasse = klasse.uvID;
				kl.idLehrer = leitung1und2.uvID;
				kl.reihenfolge = reihenfolge++;
				list.add(kl);
			}
		}
		return list;
	}

	/**
	 * Liefert den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 *
	 * @param prioritaet   Das {@link UvBlockungRegelPrioritaet}-Objekt.
	 *
	 * @return den aktuellen Malus der bestimmten {@link UvBlockungRegelPrioritaet}.
	 */
	public gibMalusDerPrioritaet(prioritaet: UvBlockungRegelPrioritaet): number {
		return this.malus[prioritaet.nr];
	}

	/**
	 * Liefert eine Kopie des aktuellen Malus-Arrays.
	 *
	 * @return eine Kopie des aktuellen Malus-Arrays.
	 */
	public gibMalusKopie(): Array<number> {
		const c: Array<number> = Array(this.malus.length).fill(0);
		System.arraycopy(this.malus, 0, c, 0, c.length);
		return c;
	}

	/**
	 * Liefert eine Beschreibung des aktuellen Malus-Zustandes.
	 *
	 * @return eine Beschreibung des aktuellen Malus-Zustandes.
	 */
	public gibMalusBeschreibung(): string | null {
		return Arrays.toString(this.malus);
	}

	/**
	 * Prüft, ob die übergebene Regel in der Menge fehlerhafter Regeln enthalten ist.
	 *
	 * @param regel   Die zu prüfende Regel.
	 *
	 * @return {@code true}, wenn die Regel als fehlerhaft erkannt wurde, sonst {@code false}
	 */
	public gibIstRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		return this.manRegeln.regelnGetMengeFehlerhaftAsList().contains(regel);
	}

	private gibKlasseOrException(uvKlasseID: number): UvAlgorithmusDynDatenKlasse {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.mapIDzuDynKlasse.get(uvKlasseID);
		if (klasse === null) {
			throw new DeveloperNotificationException(JavaString.format("Ungültige Klasse-ID=%d!", uvKlasseID));
		}
		return klasse;
	}

	private gibLehrkraftOrException(uvLehrkraftID: number): UvAlgorithmusDynDatenLehrkraft {
		const lehrer: UvAlgorithmusDynDatenLehrkraft | null = this.mapIDzuDynLehrkraft.get(uvLehrkraftID);
		if (lehrer === null) {
			throw new DeveloperNotificationException(JavaString.format("Ungültige Lehrkraft-ID=%d!", uvLehrkraftID));
		}
		return lehrer;
	}

	private gibLerngruppeOrException(uvLerngruppeID: number): UvAlgorithmusDynDatenLerngruppe {
		const lehrer: UvAlgorithmusDynDatenLerngruppe | null = this.mapIDzuDynLerngruppe.get(uvLerngruppeID);
		if (lehrer === null) {
			throw new DeveloperNotificationException(JavaString.format("Ungültige Lerngruppe-ID=%d!", uvLerngruppeID));
		}
		return lehrer;
	}

	private gibJahrgangOrException(uvJahrgangID: number | null): UvAlgorithmusDynDatenJahrgang {
		const jahrgang: UvAlgorithmusDynDatenJahrgang | null = this.mapIDzuDynJahrgang.get(uvJahrgangID);
		if (jahrgang === null) {
			throw new DeveloperNotificationException(JavaString.format("Ungültige Jahrgang-ID=%d!", uvJahrgangID));
		}
		return jahrgang;
	}

	private gibFachOrException(uvFachID: number | null): UvAlgorithmusDynDatenFach {
		const fach: UvAlgorithmusDynDatenFach | null = this.mapIDzuDynFach.get(uvFachID);
		if (fach === null) {
			throw new DeveloperNotificationException(JavaString.format("Ungültige Fach-ID=%d!", uvFachID));
		}
		return fach;
	}

	private initLerngruppenLehrkraftPotentielle(): void {
		const uvLerngruppen: List<UvLerngruppe> = this.man.lerngruppeGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		const uvLehrer: List<UvLehrer> = this.man.lehrerGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		for (let iLerngruppe: number = 0; iLerngruppe < this.aLerngruppe.length; iLerngruppe++) {
			const lerngruppe: UvLerngruppe = uvLerngruppen.get(iLerngruppe);
			for (let iLehrer: number = 0; iLehrer < this.aLehrkraft.length; iLehrer++) {
				const lehrer: UvLehrer = uvLehrer.get(iLehrer);
				if (this.man.lehrerHatLehrbefaehigungLerngruppe(lehrer, lerngruppe)) {
					this.aLerngruppe[iLerngruppe].fuegeLehrkraftHinzuf(this.aLehrkraft[iLehrer]);
				}
			}
		}
	}

	private initRegeln(): void {
		const sortierungProRegeltyp: Array<number> = Array(UvBlockungRegelTyp.values().length).fill(0);
		Arrays.fill(sortierungProRegeltyp, 10);
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN.nr] = 0;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 20;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;
		sortierungProRegeltyp[UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr] = 30;
		const compRegeln: Comparator<UvBlockungRegel> = { compare: (a: UvBlockungRegel, b: UvBlockungRegel) => {
			const sa: number = sortierungProRegeltyp[a.typ];
			const sb: number = sortierungProRegeltyp[b.typ];
			if (sa !== sb) {
				return JavaInteger.compare(sa, sb);
			}
			return JavaInteger.compare(a.typ, b.typ);
		} };
		const regeln: List<UvBlockungRegel> = this.manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList();
		regeln.sort(compRegeln);
		for (const regel of regeln) {
			this.initRegel(regel);
		}
	}

	private initRegel(regel: UvBlockungRegel): void {
		const typ: UvBlockungRegelTyp = UvBlockungRegelTyp.ofNr(regel.typ);
		const parameter: List<number> = regel.parameter;
		const prioritaet: number = regel.prioritaet;
		switch (typ) {
			case UvBlockungRegelTyp.UNDEFINIERT: {
				// empty block
				break;
			}
			case UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_KLASSENLEHRER: {
				this.initRegel01KlassenlehrerDefault(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_KLASSENLEHRER: {
				this.initRegel02KlasseBenoetigtKlassenlehrer(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.KLASSE_DEFAULT_BENOETIGT_A_STELLV_KLASSENLEHRER: {
				this.initRegel03KlassenlehrerDefault(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.KLASSE_A_BENOETIGT_B_STELLV_KLASSENLEHRER: {
				this.initRegel04KlasseBenoetigtStellvKlassenlehrer(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE: {
				this.initRegel05WennLeitung1Dann(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_WENN_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE: {
				this.initRegel06WennLehrkraftLeitung1Dann(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_A_STUNDEN_IN_KLASSE: {
				this.initRegel07WennLeitung2Dann(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_WENN_STELLV_KLASSENLEHRER_DANN_MINDESTENS_B_STUNDEN_IN_KLASSE: {
				this.initRegel08WennLehrkraftLeitung2Dann(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_KLASSENLEHRER: {
				this.initRegel09LehrerMaxLeitung1Default(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER: {
				this.initRegel10LehrerMaxLeitung1(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER: {
				this.initRegel11LehrerMaxLeitung2Default(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER: {
				this.initRegel12LehrerMaxLeitung2(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B: {
				this.initRegel13LehrkraftVerbotenInLerngruppe(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B: {
				this.initRegel14LehrkraftFixiertInLerngruppe(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_IN_LERNGRUPPE_B: {
				this.initRegel15LehrkraftGerneInLerngruppe(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_IN_LERNGRUPPE_B: {
				this.initRegel16LehrkraftUngernInLerngruppe(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LERNGRUPPE_A_LIEGT_AM_WOCHENTAG_B_STUNDE_C_WOCHENTYP_D: {
				this.initRegel17LerngruppeHatWochentagStundeWochentyp(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B: {
				this.initRegel18LehrkraftVerbotenInKlasse(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B: {
				this.initRegel19LehrkraftVerbotenInStufe(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_MAL_FACH_C: {
				this.initRegel20LehrkraftHatMindestensFach(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_MAL_FACH_C: {
				this.initRegel21LehrkraftHatMaximalFach(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_KORREKTUREN: {
				this.initRegel22LehrkraftHatKorrekturenMin(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_KORREKTUREN: {
				this.initRegel23LehrkraftHatKorrekturenMax(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN_IN_DEN_JAHRGAENGEN: {
				this.initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_DARF_FACH_B_NICHT_UNTERRICHTEN: {
				this.initRegel25LehrkraftDarfNichtFachUnterrichten(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_VERSCHIEDENE_JAHRGAENGE: {
				this.initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAEFTE_A_UND_B_NICHT_IN_DER_SELBEN_KLASSE: {
				this.initRegel27LehrkraftNichtInSelberKlasse(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LERNGRUPPE_A_BENOETIGT_B_LEHRKRAEFTE: {
				this.initRegel28LerngruppeBenoetigtBLehrkraefte(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_SELBE_LEHRKRAFT: {
				this.initRegel29LerngruppenHabenSelbeLehrkraft(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LERNGRUPPEN_ERHALTEN_VERSCHIEDENE_LEHRKRAEFTE: {
				this.initRegel30LerngruppenHabenVerschiedeneLehrkraefte(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LERNGRUPPEN_GEMEINSAM_KUERZEN_AUF_A_STUNDEN: {
				this.initRegel31LerngruppenGemeinsamKuerzenAuf(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel32LehrkraftFixiertAlsLeitung1(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel33LehrkraftFixiertAlsLeitung2(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel34LehrkraftVerbotenAlsLeitung1(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel35LehrkraftVerbotenAlsLeitung2(parameter)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel36LehrkraftGerneAlsLeitung1(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_GERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel37LehrkraftGerneAlsLeitung2(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel38LehrkraftUngerneAlsLeitung1(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_UNGERNE_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B: {
				this.initRegel39LehrkraftUngerneAlsLeitung2(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MAXIMAL_B_LERNGRUPPEN: {
				this.initRegel40LehrkraftMaxAnzahlLerngruppen(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_HAT_MINDESTENS_B_LERNGRUPPEN: {
				this.initRegel41LehrkraftMinAnzahlLerngruppen(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_MINDESTENS_B_MAL_IN_LERNGRUPPEN: {
				this.initRegel42LehrkraftMinOftInLerngruppen(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_MAXIMAL_B_MAL_IN_LERNGRUPPEN: {
				this.initRegel43LehrkraftMaxOftInLerngruppen(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_SOLL_IST_AKTIVIERUNG_A: {
				this.initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_SOLL_IST_AKTIVIERUNG_B: {
				this.initRegel45LehrkraftSollIstAbweichungAktivierung(parameter, prioritaet)
				break;
			}
			default: {
				throw new IllegalStateException("Unbekannter Regeltyp: " + regel.typ);
				break;
			}
		}
	}

	private initRegel01KlassenlehrerDefault(parameter: List<number>, prioritaet: number): void {
		const neuLeitung1Anzahl: number = parameter.get(0);
		for (const klasse of this.aKlasse) {
			for (const r of klasse.regeln) {
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02')))) {
					(r as unknown as UvAlgorithmusDynDatenRegel02).setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
				}
			}
		}
	}

	private initRegel02KlasseBenoetigtKlassenlehrer(parameter: List<number>, prioritaet: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(0));
		const neuLeitung1Anzahl: number = parameter.get(1);
		for (const r of klasse.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel02).setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
			}
		}
	}

	private initRegel03KlassenlehrerDefault(parameter: List<number>, prioritaet: number): void {
		const neuLeitung2Anzahl: number = parameter.get(0);
		for (const klasse of this.aKlasse) {
			for (const r of klasse.regeln) {
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel04')))) {
					(r as unknown as UvAlgorithmusDynDatenRegel04).setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
				}
			}
		}
	}

	private initRegel04KlasseBenoetigtStellvKlassenlehrer(parameter: List<number>, prioritaet: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(0));
		const neuLeitung2Anzahl: number = parameter.get(1);
		for (const r of klasse.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel04')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel04).setzeLeitung2AnzahlUndPrioritaet(neuLeitung2Anzahl, prioritaet);
			}
		}
	}

	private initRegel05WennLeitung1Dann(parameter: List<number>, prioritaet: number): void {
		const minKlassenstundenBeiLeitung1: number = parameter.get(0);
		for (const klasse of this.aKlasse) {
			for (const lehrkraft of this.aLehrkraft) {
				const r06: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel06(this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
			}
		}
	}

	private initRegel06WennLehrkraftLeitung1Dann(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const minKlassenstundenBeiLeitung1: number = parameter.get(1);
		for (const klasse of this.aKlasse) {
			const regeln: List<UvAlgorithmusDynDatenRegel> = this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (let iterator: JavaIterator<UvAlgorithmusDynDatenRegel> = regeln.iterator(); iterator.hasNext(); ) {
				const r: UvAlgorithmusDynDatenRegel = iterator.next();
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel06')))) {
					iterator.remove();
				}
			}
		}
		for (const klasse of this.aKlasse) {
			const r06: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel06(this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
		}
	}

	private initRegel07WennLeitung2Dann(parameter: List<number>, prioritaet: number): void {
		const minKlassenstundenBeiLeitung2: number = parameter.get(0);
		for (const klasse of this.aKlasse) {
			for (const lehrkraft of this.aLehrkraft) {
				const r08: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel08(this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
			}
		}
	}

	private initRegel08WennLehrkraftLeitung2Dann(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const minKlassenstundenBeiLeitung2: number = parameter.get(1);
		for (const klasse of this.aKlasse) {
			const regeln: List<UvAlgorithmusDynDatenRegel> = this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (let iterator: JavaIterator<UvAlgorithmusDynDatenRegel> = regeln.iterator(); iterator.hasNext(); ) {
				const r: UvAlgorithmusDynDatenRegel = iterator.next();
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel08')))) {
					iterator.remove();
				}
			}
		}
		for (const klasse of this.aKlasse) {
			const r08: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel08(this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
		}
	}

	private initRegel09LehrerMaxLeitung1Default(parameter: List<number>, prioritaet: number): void {
		const maxLeitung1: number = parameter.get(0);
		for (const lehrkraft of this.aLehrkraft) {
			const r10: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel10(this.malus, lehrkraft, maxLeitung1, prioritaet);
			for (const klasse of this.aKlasse) {
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
			}
		}
	}

	private initRegel10LehrerMaxLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const maxLeitung1: number = parameter.get(1);
		for (const klasse of this.aKlasse) {
			const regeln: List<UvAlgorithmusDynDatenRegel> = this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (let iterator: JavaIterator<UvAlgorithmusDynDatenRegel> = regeln.iterator(); iterator.hasNext(); ) {
				const r: UvAlgorithmusDynDatenRegel = iterator.next();
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel10')))) {
					iterator.remove();
				}
			}
		}
		const r10: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel10(this.malus, lehrkraft, maxLeitung1, prioritaet);
		for (const klasse of this.aKlasse) {
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
		}
	}

	private initRegel11LehrerMaxLeitung2Default(parameter: List<number>, prioritaet: number): void {
		const maxLeitung2: number = parameter.get(0);
		for (const lehrkraft of this.aLehrkraft) {
			const r12: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel12(this.malus, lehrkraft, maxLeitung2, prioritaet);
			for (const klasse of this.aKlasse) {
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
			}
		}
	}

	private initRegel12LehrerMaxLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const maxLeitung2: number = parameter.get(1);
		for (const klasse of this.aKlasse) {
			const regeln: List<UvAlgorithmusDynDatenRegel> = this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID);
			for (let iterator: JavaIterator<UvAlgorithmusDynDatenRegel> = regeln.iterator(); iterator.hasNext(); ) {
				const r: UvAlgorithmusDynDatenRegel = iterator.next();
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel12')))) {
					iterator.remove();
				}
			}
		}
		const r12: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel12(this.malus, lehrkraft, maxLeitung2, prioritaet);
		for (const klasse of this.aKlasse) {
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
		}
	}

	private initRegel13LehrkraftVerbotenInLerngruppe(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(1));
		lerngruppe.entferneLehrkraft(lehrkraft);
	}

	private initRegel14LehrkraftFixiertInLerngruppe(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(1));
		this.stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, true);
	}

	private initRegel15LehrkraftGerneInLerngruppe(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(1));
		const r15: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel15(this.malus, lerngruppe, lehrkraft, prioritaet);
		this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r15);
	}

	private initRegel16LehrkraftUngernInLerngruppe(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(1));
		const r16: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel16(this.malus, lerngruppe, lehrkraft, prioritaet);
		this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r16);
	}

	private initRegel17LerngruppeHatWochentagStundeWochentyp(parameter: List<number>, prioritaet: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(0));
		const wochentag: number = parameter.get(1);
		const stunde: number = parameter.get(2);
		const wochentyp: number = parameter.get(3);
		const r17: UvAlgorithmusDynDatenRegel17 = new UvAlgorithmusDynDatenRegel17(this.malus, lerngruppe, wochentag, stunde, wochentyp, prioritaet);
		lerngruppe.regeln.add(r17);
		lerngruppe.zeitslots.add(r17);
	}

	private initRegel18LehrkraftVerbotenInKlasse(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennKlasseUebereinstimmt(lehrkraft, klasse);
		}
	}

	private initRegel19LehrkraftVerbotenInStufe(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const jahrgang: UvAlgorithmusDynDatenJahrgang = this.gibJahrgangOrException(parameter.get(1));
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennStufeUebereinstimmt(lehrkraft, jahrgang);
		}
	}

	private initRegel20LehrkraftHatMindestensFach(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const minimum: number = parameter.get(1);
		const fach: UvAlgorithmusDynDatenFach = this.gibFachOrException(parameter.get(2));
		const r20: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel20(this.malus, fach, lehrkraft, minimum, prioritaet);
		this.mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r20);
	}

	private initRegel21LehrkraftHatMaximalFach(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const maximum: number = parameter.get(1);
		const fach: UvAlgorithmusDynDatenFach = this.gibFachOrException(parameter.get(2));
		const r21: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel21(this.malus, fach, lehrkraft, maximum, prioritaet);
		this.mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r21);
	}

	private initRegel22LehrkraftHatKorrekturenMin(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const minimum: number = parameter.get(1);
		const r22: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel22(this.malus, lehrkraft, minimum, prioritaet);
		lehrkraft.regeln.add(r22);
	}

	private initRegel23LehrkraftHatKorrekturenMax(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const maximum: number = parameter.get(1);
		const r23: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel23(this.malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r23);
	}

	private initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const fach: UvAlgorithmusDynDatenFach = this.gibFachOrException(parameter.get(1));
		const jahrgaenge: List<UvAlgorithmusDynDatenJahrgang> = new ArrayList<UvAlgorithmusDynDatenJahrgang>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const jahrgang: UvAlgorithmusDynDatenJahrgang = this.gibJahrgangOrException(parameter.get(i));
			jahrgaenge.add(jahrgang);
		}
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUndFachUebereinstimmt(lehrkraft, fach, jahrgaenge);
		}
	}

	private initRegel25LehrkraftDarfNichtFachUnterrichten(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const fach: UvAlgorithmusDynDatenFach = this.gibFachOrException(parameter.get(1));
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennFachUebereinstimmt(lehrkraft, fach);
		}
	}

	private initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const maximum: number = parameter.get(1);
		const r26: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel26(this.malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r26);
	}

	private initRegel27LehrkraftNichtInSelberKlasse(parameter: List<number>, prioritaet: number): void {
		const lehrkraft1: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const lehrkraft2: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(1));
		for (const klasse of this.aKlasse) {
			const r27: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel27(this.malus, klasse, lehrkraft1, lehrkraft2, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft1.interneID).add(r27);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft2.interneID).add(r27);
		}
	}

	private initRegel28LerngruppeBenoetigtBLehrkraefte(parameter: List<number>, prioritaet: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(0));
		const neuesSoll: number = parameter.get(1);
		for (const r of lerngruppe.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel28')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel28).setzeAuf(neuesSoll, prioritaet);
			}
		}
	}

	private initRegel29LerngruppenHabenSelbeLehrkraft(parameter: List<number>, prioritaet: number): void {
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 0; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}
		const r29: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel29(this.malus, lerngruppenMenge, this.aLehrkraft.length, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			lerngruppe.regeln.add(r29);
		}
	}

	private initRegel30LerngruppenHabenVerschiedeneLehrkraefte(parameter: List<number>, prioritaet: number): void {
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 0; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}
		const r30: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel30(this.malus, lerngruppenMenge, this.aLehrkraft.length, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			lerngruppe.regeln.add(r30);
		}
	}

	private initRegel31LerngruppenGemeinsamKuerzenAuf(parameter: List<number>): void {
		const neuerStundenSoll: number = parameter.get(0);
		for (let i: number = 1; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(i));
			lerngruppe.setzeStundenSollAuf(neuerStundenSoll);
		}
	}

	private initRegel32LehrkraftFixiertAlsLeitung1(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		this.stateKlassenLeitung1Add(klasse, lehrkraft, true);
	}

	private initRegel33LehrkraftFixiertAlsLeitung2(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		this.stateKlassenLeitung2Add(klasse, lehrkraft, true);
	}

	private initRegel34LehrkraftVerbotenAlsLeitung1(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		klasse.entferneLeitung1(lehrkraft);
	}

	private initRegel35LehrkraftVerbotenAlsLeitung2(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		klasse.entferneLeitung2(lehrkraft);
	}

	private initRegel36LehrkraftGerneAlsLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		const r36: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel36(this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r36);
	}

	private initRegel37LehrkraftGerneAlsLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		const r37: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel37(this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r37);
	}

	private initRegel38LehrkraftUngerneAlsLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		const r38: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel38(this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r38);
	}

	private initRegel39LehrkraftUngerneAlsLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(parameter.get(1));
		const r39: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel39(this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r39);
	}

	private initRegel40LehrkraftMaxAnzahlLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const max: number = parameter.get(1);
		const r40: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel40(this.malus, lehrkraft, max, prioritaet);
		lehrkraft.regeln.add(r40);
	}

	private initRegel41LehrkraftMinAnzahlLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const min: number = parameter.get(1);
		const r41: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel41(this.malus, lehrkraft, min, prioritaet);
		lehrkraft.regeln.add(r41);
	}

	private initRegel42LehrkraftMinOftInLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const min: number = parameter.get(1);
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}
		const r42: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel42(this.malus, lehrkraft, min, lerngruppenMenge, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r42);
		}
	}

	private initRegel43LehrkraftMaxOftInLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const max: number = parameter.get(1);
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(parameter.get(i));
			lerngruppenMenge.add(lerngruppe);
		}
		const r43: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel43(this.malus, lehrkraft, max, lerngruppenMenge, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r43);
		}
	}

	private initRegel44LehrkraftDefaultSollIstAbweichungAktivierung(parameter: List<number>, prioritaet: number): void {
		const aktiviert: boolean = (parameter.get(0) === 1);
		for (const lehrkraft of this.aLehrkraft) {
			for (const r of lehrkraft.regeln) {
				if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45')))) {
					(r as unknown as UvAlgorithmusDynDatenRegel45).setzeAuf(aktiviert, prioritaet);
				}
			}
		}
	}

	private initRegel45LehrkraftSollIstAbweichungAktivierung(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(parameter.get(0));
		const aktiviert: boolean = (parameter.get(1) === 1);
		for (const r of lehrkraft.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel45).setzeAuf(aktiviert, prioritaet);
			}
		}
	}

	private logging(): void {
		for (let i: number = 0; i < this.aLehrkraft.length; i++) {
			this.log.logLn(LogLevel.INFO, "Dyn-Lehrer: " + this.aLehrkraft[i].toString());
		}
		for (let i: number = 0; i < this.aLerngruppe.length; i++) {
			this.log.logLn(LogLevel.INFO, "Dyn-Lerngruppe: " + this.aLerngruppe[i].toString());
		}
		this.log.logLn(LogLevel.INFO, "Malus = " + this.gibMalusBeschreibung());
	}

	private bewertungSpeichern(): void {
		System.arraycopy(this.malus, 0, this.malusSave, 0, this.malus.length);
	}

	private bewertungIstSchlechterAlsGespeichert(): boolean {
		for (let i: number = 0; i < this.malus.length; i++) {
			if (this.malus[i] !== this.malusSave[i]) {
				return this.malus[i] > this.malusSave[i];
			}
		}
		return false;
	}

	private undoClear(): void {
		this.undos.clear();
	}

	/**
	 * Macht alle gespeicherten Undo-Schritte rückgängig.
	 */
	public undoAll(): void {
		while (!this.undos.isEmpty()) {
			const undo: UvAlgorithmusDynDatenUndo = this.undos.removeLast();
			const typ: number = undo.gibTyp();
			const data: Array<number> = undo.gibData();
			switch (typ) {
				case 1: {
					this.stateLerngruppeLehrkraftDel(this.aLerngruppe[data[0]], this.aLehrkraft[data[1]])
					break;
				}
				case 2: {
					this.stateLerngruppeLehrkraftAdd(this.aLerngruppe[data[0]], this.aLehrkraft[data[1]], false)
					break;
				}
				case 3: {
					this.stateKlassenLeitung1Del(this.aKlasse[data[0]], this.aLehrkraft[data[1]])
					break;
				}
				case 4: {
					this.stateKlassenLeitung1Add(this.aKlasse[data[0]], this.aLehrkraft[data[1]], false)
					break;
				}
				case 5: {
					this.stateKlassenLeitung2Del(this.aKlasse[data[0]], this.aLehrkraft[data[1]])
					break;
				}
				case 6: {
					this.stateKlassenLeitung2Add(this.aKlasse[data[0]], this.aLehrkraft[data[1]], false)
					break;
				}
				default: {
					throw new IllegalArgumentException("Undo-Typ unbekannt: " + typ);
					break;
				}
			}
		}
	}

	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige Lehrkraft hinzu.
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieLerngruppeLehrkraftHinzufuegen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationLerngruppeLehrkraftAdd();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete Lehrkraft (falls vorhanden).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieLerngruppeLehrkraftEntfernen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationLerngruppeLehrkraftDel();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Klasse erhält eine zufällige Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieKlassenleitung1Hinzufuegen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationKlassenleitung1Add();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete Klassenleitung (Leitung 1).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieKlassenleitung1Entfernen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationKlassenleitung1Del();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Klasse erhält eine zufällige stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieKlassenleitung2Hinzufuegen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationKlassenleitung2Add();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete stellv. Klassenleitung (Leitung 2).
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	public strategieKlassenleitung2Entfernen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationKlassenleitung2Del();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige potentielle Lehrkraft hinzu.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationLerngruppeLehrkraftAdd(): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeZufaellig();
		if (lerngruppe === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = lerngruppe.gibLehrkraftPotentiellZufaelligOderNull();
		if (lehrkraft === null) {
			return;
		}
		this.stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe.interneID, lehrkraft.interneID));
	}

	/**
	 * Eine zufällige Lerngruppe entfernt eine zufällige zugeordnete, nicht fixierte Lehrkraft (falls vorhanden).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationLerngruppeLehrkraftDel(): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeZufaellig();
		if (lerngruppe === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = lerngruppe.gibLehrkraftZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft === null) {
			return;
		}
		this.stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
		this.undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe.interneID, lehrkraft.interneID));
	}

	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationKlassenleitung1Add(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaellig();
		if (klasse === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = klasse.gibLeitung1PotentiellZufaelligOderNull();
		if (lehrkraft === null) {
			return;
		}
		this.stateKlassenLeitung1Add(klasse, lehrkraft, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(3, klasse.interneID, lehrkraft.interneID));
	}

	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte Klassenleitung (Leitung 1).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationKlassenleitung1Del(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaellig();
		if (klasse === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = klasse.gibLeitung1ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft === null) {
			return;
		}
		this.stateKlassenLeitung1Del(klasse, lehrkraft);
		this.undos.add(new UvAlgorithmusDynDatenUndo(4, klasse.interneID, lehrkraft.interneID));
	}

	/**
	 * Eine zufällige Klasse erhält eine zufällige potentielle stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationKlassenleitung2Add(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaellig();
		if (klasse === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = klasse.gibLeitung2PotentiellZufaelligOderNull();
		if (lehrkraft === null) {
			return;
		}
		this.stateKlassenLeitung2Add(klasse, lehrkraft, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(5, klasse.interneID, lehrkraft.interneID));
	}

	/**
	 * Eine zufällige Klasse entfernt eine zufällige aktuell zugeordnete, nicht fixierte stellv. Klassenleitung (Leitung 2).
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	public manipulationKlassenleitung2Del(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaellig();
		if (klasse === null) {
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = klasse.gibLeitung2ZugeordnetAberNichtFixiertZufaellig();
		if (lehrkraft === null) {
			return;
		}
		this.stateKlassenLeitung2Del(klasse, lehrkraft);
		this.undos.add(new UvAlgorithmusDynDatenUndo(6, klasse.interneID, lehrkraft.interneID));
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert          Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public setzeLerngruppeLehrkraftAdd(uvLerngruppeID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(uvLerngruppeID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, fixiert);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param uvLehrkraftID    Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public setzeLerngruppeLehrkraftDel(uvLerngruppeID: number, uvLehrkraftID: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe = this.gibLerngruppeOrException(uvLerngruppeID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateLerngruppeLehrkraftDel(lerngruppe, lehrkraft);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public setzeKlassenLeitung1Add(uvKlasseID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(uvKlasseID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateKlassenLeitung1Add(klasse, lehrkraft, fixiert);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public setzeKlassenLeitung1Del(uvKlasseID: number, uvLehrkraftID: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(uvKlasseID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateKlassenLeitung1Del(klasse, lehrkraft);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellvertretenden Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	public setzeKlassenLeitung2Add(uvKlasseID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(uvKlasseID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateKlassenLeitung2Add(klasse, lehrkraft, fixiert);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle stellvertretende Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	public setzeKlassenLeitung2Del(uvKlasseID: number, uvLehrkraftID: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse = this.gibKlasseOrException(uvKlasseID);
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.gibLehrkraftOrException(uvLehrkraftID);
		this.stateKlassenLeitung2Del(klasse, lehrkraft);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert      Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateLerngruppeLehrkraftAdd(lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (!lerngruppe.gibIstLehrkraftPotentiell(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lerngruppe %sd will Lehrkraft %sd hinzufügen, Lehrkraft ist aber keine potentielle!", lerngruppe.toString(), lehrkraft.toString()));
		}
		this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);
		lerngruppe.stateLehrkraftZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLerngruppeZugeordnetAdd(lerngruppe);
		this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt eine Lerngruppen-Lehrkraft-Zuordnung.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private stateLerngruppeLehrkraftDel(lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (lerngruppe.gibIstLehrkraftFixiert(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lerngruppe %s will Lehrkraft %s entfernen, Lehrkraft ist aber fixiert!", lerngruppe.toString(), lehrkraft.toString()));
		}
		this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);
		lerngruppe.stateLehrkraftZugeordnetDel(lehrkraft);
		lehrkraft.stateLerngruppeZugeordnetDel(lerngruppe);
		this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateKlassenLeitung1Add(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (!klasse.gibIstLeitung1Potentiell(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Klasse %s will Lehrkraft %s als Leitung 1 hinzufügen, Lehrkraft ist aber keine potentielle!", klasse.toString(), lehrkraft.toString()));
		}
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
		klasse.stateLeitung1ZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLeitung1Inc(klasse);
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle Klassenleitung (Leitung 1).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private stateKlassenLeitung1Del(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (klasse.gibIstLeitung1Fixiert(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Klasse %s will Lehrkraft %s als Leitung 1 entfernen, Lehrkraft ist aber fixiert!", klasse.toString(), lehrkraft.toString()));
		}
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
		klasse.stateLeitung1ZugeordnetDel(lehrkraft);
		lehrkraft.stateLeitung1Dec(klasse);
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateKlassenLeitung2Add(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (!klasse.gibIstLeitung2Potentiell(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Klasse %s will Lehrkraft %s als Leitung 2 hinzufügen, Lehrkraft ist aber keine potentielle!", klasse.toString(), lehrkraft.toString()));
		}
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
		klasse.stateLeitung2ZugeordnetAdd(lehrkraft, fixiert);
		lehrkraft.stateLeitung2Inc(klasse);
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Entfernt aus der Klasse die aktuelle stellv. Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private stateKlassenLeitung2Del(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (klasse.gibIstLeitung2Fixiert(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Klasse %s will Lehrkraft %s als Leitung 2 entfernen, Lehrkraft ist aber fixiert!", klasse.toString(), lehrkraft.toString()));
		}
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
		klasse.stateLeitung2ZugeordnetDel(lehrkraft);
		lehrkraft.stateLeitung2Dec(klasse);
		this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
	}

	private changeMalusLerngruppeLehrkraft(lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft, faktor: number): void {
		for (const regel of lerngruppe.regeln) {
			regel.changeMalus(faktor);
		}
		for (const regel of lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}
		for (const klasse of lerngruppe.klassenmenge) {
			for (const regel of this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
				regel.changeMalus(faktor);
			}
		}
		for (const regel of this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}
		for (const regel of this.mapFachLehrkraftZuRegeln.get(lerngruppe.fach.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}
	}

	private changeMalusKlasseLehrkraft(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, faktor: number): void {
		for (const regel of klasse.regeln) {
			regel.changeMalus(faktor);
		}
		for (const regel of lehrkraft.regeln) {
			regel.changeMalus(faktor);
		}
		for (const regel of this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID)) {
			regel.changeMalus(faktor);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDaten'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDaten>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDaten');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDaten(obj: unknown): UvAlgorithmusDynDaten {
	return obj as UvAlgorithmusDynDaten;
}
