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
import { JavaString } from '../../../java/lang/JavaString';
import { LogLevel } from '../../../core/logger/LogLevel';
import type { Comparator } from '../../../java/util/Comparator';
import { Random } from '../../../java/util/Random';
import type { List } from '../../../java/util/List';
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
import { UvAlgorithmusDynDatenRegel17 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel17';
import { UvLerngruppenLehrer } from '../../../core/data/uv/UvLerngruppenLehrer';
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
	 * Der Planungsabschnitt, auf den sich diese Berechnung bezieht.
	 */
	private readonly planungsabschnitt: UvPlanungsabschnitt;

	/**
	 * Die Menge aller Lehrkräfte (dynamisch).
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
	 * Die Menge aller Undo-Objekte, um Manipulationen schnell rückgängig zu machen.
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
	 * Die Menge aller Regel-Objekte, die sich auf (Lerngruppe, Lehrkraft) beziehen.
	 */
	private readonly mapLerngruppeLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;

	/**
	 * Die Menge aller Regel-Objekte, die sich auf (Klasse, Lehrkraft) beziehen.
	 */
	private readonly mapKlasseLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;

	/**
	 * Die Menge aller Regel-Objekte, die sich auf (Fach, Lehrkraft) beziehen.
	 */
	private readonly mapFachLehrkraftZuRegeln: List<List<List<UvAlgorithmusDynDatenRegel>>>;


	/**
	 * Der Konstruktor.
	 *
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param rnd                 Ein Random-Objekt.
	 * @param man                 Ein {@link UvManager}-Objekt, das alle Daten hat.
	 * @param manRegeln           Ein {@link UvRegelManager}-Objekt, das alle Regeln hat.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt}, auf den sich die Manager-Daten beziehen.
	 */
	constructor(log: Logger, rnd: Random, man: UvManager, manRegeln: UvRegelManager, planungsabschnitt: UvPlanungsabschnitt) {
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
			const r28: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel28(log, this.malus, this.aLerngruppe[i], sollLehrkraftAnzahl, prioritaet);
			this.aLerngruppe[i].regeln.add(r28);
		}
		for (let i: number = 0; i < this.aLehrkraft.length; i++) {
			const aktiviert: boolean = true;
			const prioritaet: number = UvBlockungRegelPrioritaet.MITTEL.nr;
			const r45: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel45(log, this.malus, this.aLehrkraft[i], aktiviert, prioritaet);
			this.aLehrkraft[i].regeln.add(r45);
		}
		for (let i: number = 0; i < this.aKlasse.length; i++) {
			const sollLeitung1: number = 1;
			const prioritaet1: number = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			const r02: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel02(log, this.malus, this.aKlasse[i], sollLeitung1, prioritaet1);
			this.aKlasse[i].regeln.add(r02);
			const sollLeitung2: number = 1;
			const prioritaet2: number = UvBlockungRegelPrioritaet.SEHR_HOCH.nr;
			const r04: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel04(log, this.malus, this.aKlasse[i], sollLeitung2, prioritaet2);
			this.aKlasse[i].regeln.add(r04);
		}
		this.initRegeln();
		if (UvAlgorithmusDynDaten.LOG_ADDITIONAL) {
			this.logging();
		}
	}

	private logging(): void {
		for (let i: number = 0; i < this.aLehrkraft.length; i++) {
			this.log.logLn(LogLevel.INFO, "Dyn-Lehrkraft: " + this.aLehrkraft[i].toString());
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

	private bewertungIstBesserAlsGespeichert(): boolean {
		for (let i: number = 0; i < this.malus.length; i++) {
			if (this.malus[i] !== this.malusSave[i]) {
				return this.malus[i] < this.malusSave[i];
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
	undoAll(): void {
		this.undoBis(0);
	}

	/**
	 * Macht die Undo-Schritte bis zur angegebenen Stapelgröße rückgängig.
	 *
	 * @param zielGroesse   Die angestrebte Größe des Undo-Stapels.
	 */
	private undoBis(zielGroesse: number): void {
		while (this.undos.size() > zielGroesse) {
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
					this.log.logLn(LogLevel.ERROR, JavaString.format("Undo-Typ unbekannt: %d - Undo wird übersprungen.", typ))
					break;
				}
			}
		}
	}

	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige Lehrkraft hinzu.
	 * Falls die Bewertung sich verschlechtert, wird die Veränderung rückgängig gemacht.
	 */
	strategieLerngruppeLehrkraftHinzufuegen(): void {
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
	strategieLerngruppeLehrkraftEntfernen(): void {
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
	strategieKlassenleitung1Hinzufuegen(): void {
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
	strategieKlassenleitung1Entfernen(): void {
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
	strategieKlassenleitung2Hinzufuegen(): void {
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
	strategieKlassenleitung2Entfernen(): void {
		this.bewertungSpeichern();
		this.undoClear();
		this.manipulationKlassenleitung2Del();
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Ein generischer Ring-Ketten-Übernahme-Mix (Ringe zuerst) mit einstellbaren Anzahlen, Runden und
	 * Ringgröße. Bei Verschlechterung wird alles rückgängig gemacht.
	 *
	 * @param ketten        Die Anzahl der Ketten je Runde.
	 * @param ringe         Die Anzahl der Ring-Züge je Runde.
	 * @param uebernahmen   Die Anzahl der Übernahmen je Runde.
	 * @param runden        Die Anzahl der Runden.
	 * @param ringGroesse   Die feste Ringgröße (0 = zufällig).
	 */
	strategieMixRingZuerst(ketten: number, ringe: number, uebernahmen: number, runden: number, ringGroesse: number): void {
		this.bewertungSpeichern();
		this.undoClear();
		for (let i: number = 0; i < runden; i++) {
			if (this.fuehreRingeAus(ringe, ringGroesse) || this.fuehreKettenAus(ketten) || this.fuehreUebernahmenAus(uebernahmen)) {
				return;
			}
		}
		if (this.bewertungIstSchlechterAlsGespeichert()) {
			this.undoAll();
		}
	}

	/**
	 * Führt Ring-Züge aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param ringe         Die Anzahl der Ring-Züge.
	 * @param ringGroesse   Die feste Ringgröße (0 = zufällig).
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private fuehreRingeAus(ringe: number, ringGroesse: number): boolean {
		for (let j: number = 0; j < ringe; j++) {
			if (ringGroesse > 0) {
				this.manipulationLerngruppeLehrkraftRing(ringGroesse);
			} else {
				this.manipulationLerngruppeLehrkraftRing();
			}
			if (this.bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Führt Abweichungs-Ketten aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param ketten   Die Anzahl der Ketten.
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private fuehreKettenAus(ketten: number): boolean {
		for (let j: number = 0; j < ketten; j++) {
			this.manipulationLerngruppeLehrkraftAbweichungKette();
			if (this.bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Führt Übernahmen aus und meldet, ob dabei eine Verbesserung erzielt wurde.
	 *
	 * @param uebernahmen   Die Anzahl der Übernahmen.
	 *
	 * @return TRUE, falls eine Verbesserung erzielt wurde.
	 */
	private fuehreUebernahmenAus(uebernahmen: number): boolean {
		for (let j: number = 0; j < uebernahmen; j++) {
			this.manipulationLerngruppeLehrkraftRing(1);
			if (this.bewertungIstBesserAlsGespeichert()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Führt eine zufällige der sechs Basis-Strategien aus.
	 */
	basisStrategieZufaellig(): void {
		switch (this.rnd.nextInt(6)) {
			case 0: {
				this.strategieLerngruppeLehrkraftHinzufuegen()
				break;
			}
			case 1: {
				this.strategieLerngruppeLehrkraftEntfernen()
				break;
			}
			case 2: {
				this.strategieKlassenleitung1Hinzufuegen()
				break;
			}
			case 3: {
				this.strategieKlassenleitung1Entfernen()
				break;
			}
			case 4: {
				this.strategieKlassenleitung2Hinzufuegen()
				break;
			}
			case 5: {
				this.strategieKlassenleitung2Entfernen()
				break;
			}
			default: {
				// empty block
				break;
			}
		}
	}

	/**
	 * Eine Abweichungs-Kette: Eine Über-Soll-Lehrkraft gibt eine Lerngruppe an eine Zwischenlehrkraft
	 * ab, welche dafür eine Lerngruppe an eine Unter-Soll-Lehrkraft abgibt.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	manipulationLerngruppeLehrkraftAbweichungKette(): void {
		this.manipulationLerngruppeLehrkraftAbweichungKetteVon(this.gibLehrkraftUeberSollZufaelligOderNull());
	}

	/**
	 * Eine Abweichungs-Kette mit vorgegebener Über-Soll-Lehrkraft (ohne Bewertungsprüfung).
	 *
	 * @param lehrkraftUeber   Die abgebende Über-Soll-Lehrkraft (oder null).
	 */
	manipulationLerngruppeLehrkraftAbweichungKetteVon(lehrkraftUeber: UvAlgorithmusDynDatenLehrkraft | null): void {
		if (lehrkraftUeber === null) {
			return;
		}
		const lernGruppenUeber: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteLerngruppen(lehrkraftUeber);
		if (lernGruppenUeber.isEmpty()) {
			return;
		}
		const lerngruppe1: UvAlgorithmusDynDatenLerngruppe | null = lernGruppenUeber.get(this.rnd.nextInt(lernGruppenUeber.size()));
		const kandidatenZwischen: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		for (const kandidat of this.aLehrkraft) {
			if ((kandidat as unknown !== lehrkraftUeber as unknown) && lerngruppe1.gibIstLehrkraftPotentiell(kandidat) && !lerngruppe1.gibIstLehrkraftZugeordnet(kandidat) && !UvAlgorithmusDynDaten.gibNichtFixierteLerngruppen(kandidat).isEmpty()) {
				kandidatenZwischen.add(kandidat);
			}
		}
		if (kandidatenZwischen.isEmpty()) {
			return;
		}
		const lehrkraftZwischen: UvAlgorithmusDynDatenLehrkraft | null = kandidatenZwischen.get(this.rnd.nextInt(kandidatenZwischen.size()));
		const lernGruppenZwischen: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteLerngruppen(lehrkraftZwischen);
		const lerngruppe2: UvAlgorithmusDynDatenLerngruppe | null = lernGruppenZwischen.get(this.rnd.nextInt(lernGruppenZwischen.size()));
		const kandidatenUnter: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		for (const kandidat of this.aLehrkraft) {
			if ((kandidat as unknown !== lehrkraftUeber as unknown) && (kandidat as unknown !== lehrkraftZwischen as unknown) && lerngruppe2.gibIstLehrkraftPotentiell(kandidat) && !lerngruppe2.gibIstLehrkraftZugeordnet(kandidat) && ((kandidat.stundenSoll - kandidat.stundenAnrechnung - kandidat.istStundensummeLerngruppen) > 0)) {
				kandidatenUnter.add(kandidat);
			}
		}
		if (kandidatenUnter.isEmpty()) {
			return;
		}
		const lehrkraftUnter: UvAlgorithmusDynDatenLehrkraft | null = kandidatenUnter.get(this.rnd.nextInt(kandidatenUnter.size()));
		this.stateLerngruppeLehrkraftDel(lerngruppe1, lehrkraftUeber);
		this.undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe1.interneID, lehrkraftUeber.interneID));
		this.stateLerngruppeLehrkraftAdd(lerngruppe1, lehrkraftZwischen, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe1.interneID, lehrkraftZwischen.interneID));
		this.stateLerngruppeLehrkraftDel(lerngruppe2, lehrkraftZwischen);
		this.undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe2.interneID, lehrkraftZwischen.interneID));
		this.stateLerngruppeLehrkraftAdd(lerngruppe2, lehrkraftUnter, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe2.interneID, lehrkraftUnter.interneID));
	}

	/**
	 * Liefert eine zufällige Lehrkraft oder null, falls keine Lehrkraft existiert.
	 *
	 * @return eine zufällige Lehrkraft oder null, falls keine existiert.
	 */
	private gibLehrkraftZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		if (this.aLehrkraft.length === 0) {
			return null;
		}
		return this.aLehrkraft[this.rnd.nextInt(this.aLehrkraft.length)];
	}

	/**
	 * Liefert eine zufällige Lehrkraft mit Unter-Soll (Soll − Anrechnung − Ist > 0) oder null,
	 * falls keine Lehrkraft ein Unter-Soll hat.
	 *
	 * @return eine zufällige Lehrkraft mit Unter-Soll oder null.
	 */
	private gibLehrkraftUnterSollZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		const kandidaten: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		for (const lehrkraft of this.aLehrkraft) {
			if ((lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen) > 0) {
				kandidaten.add(lehrkraft);
			}
		}
		if (kandidaten.isEmpty()) {
			return null;
		}
		return kandidaten.get(this.rnd.nextInt(kandidaten.size()));
	}

	/**
	 * Liefert mit 50 %-iger Wahrscheinlichkeit eine zufällige Lehrkraft mit Unter-Soll,
	 * ansonsten eine vollständig zufällige Lehrkraft (oder null, falls keine existiert).
	 *
	 * @return eine Lehrkraft mit bevorzugtem Unter-Soll oder null.
	 */
	private gibLehrkraftUnterSollBevorzugtZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		if (this.rnd.nextBoolean()) {
			const kandidat: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftUnterSollZufaelligOderNull();
			if (kandidat !== null) {
				return kandidat;
			}
		}
		return this.gibLehrkraftZufaelligOderNull();
	}

	/**
	 * Liefert alle nicht fixierten Lerngruppen der Lehrkraft, welche überhaupt an eine andere
	 * Lehrkraft übergeben werden können (dafür sind mindestens zwei potentielle Lehrkräfte nötig).
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return alle nicht fixierten, übertragbaren Lerngruppen der Lehrkraft.
	 */
	private static gibNichtFixierteUebertragbareLerngruppen(lehrkraft: UvAlgorithmusDynDatenLehrkraft): List<UvAlgorithmusDynDatenLerngruppe> {
		const result: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (const lerngruppe of lehrkraft.istLerngruppenZugeordnet) {
			if (!lerngruppe.gibIstLehrkraftFixiert(lehrkraft) && (lerngruppe.lehrkraeftePotentiell.size() > 1)) {
				result.add(lerngruppe);
			}
		}
		return result;
	}

	/**
	 * Prüft, ob die Lehrkraft die Lerngruppe übernehmen könnte
	 * (identisch zur Guard-Prüfung des Zustandswechsels beim Hinzufügen).
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param nehmer       Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe übernehmen soll.
	 *
	 * @return TRUE, falls die Übernahme möglich ist.
	 */
	private static istUebernahmeMoeglich(lerngruppe: UvAlgorithmusDynDatenLerngruppe, nehmer: UvAlgorithmusDynDatenLehrkraft): boolean {
		return lerngruppe.gibIstLehrkraftZugeordnetAddMoeglich(nehmer);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Die Lehrkraft "abgeber" verliert die Lerngruppe, die Lehrkraft "nehmer" erhält sie.
	 * <br> Aktualisiert die Bewertung und erzeugt für beide Schritte die passenden Undo-Objekte.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}, welche den Halter wechselt.
	 * @param abgeber      Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe abgibt.
	 * @param nehmer       Die {@link UvAlgorithmusDynDatenLehrkraft}, welche die Lerngruppe übernimmt.
	 */
	private stateLerngruppeLehrkraftWechsel(lerngruppe: UvAlgorithmusDynDatenLerngruppe, abgeber: UvAlgorithmusDynDatenLehrkraft, nehmer: UvAlgorithmusDynDatenLehrkraft): void {
		this.stateLerngruppeLehrkraftDel(lerngruppe, abgeber);
		this.undos.add(new UvAlgorithmusDynDatenUndo(2, lerngruppe.interneID, abgeber.interneID));
		this.stateLerngruppeLehrkraftAdd(lerngruppe, nehmer, false);
		this.undos.add(new UvAlgorithmusDynDatenUndo(1, lerngruppe.interneID, nehmer.interneID));
	}

	/**
	 * Ein Ring-Zug: Ein bis drei zufällige Lehrkräfte geben jeweils genau eine nicht fixierte,
	 * übertragbare Lerngruppe im Kreis an die nächste Lehrkraft weiter (Ring-Größe 1 = Übernahme).
	 * Die Startlehrkraft wird zur Hälfte bevorzugt unter den Über-Soll-Lehrkräften gezogen.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	manipulationLerngruppeLehrkraftRing(): void;

	/**
	 * Ein Ring-Zug mit fester Ring-Größe (ohne Bewertungsprüfung).
	 *
	 * @param k   Die Ring-Größe: 1 (Übernahme), 2 (Tausch) oder 3 (Rotation).
	 */
	manipulationLerngruppeLehrkraftRing(k: number): void;

	/**
	 * Implementation for method overloads of 'manipulationLerngruppeLehrkraftRing'
	 */
	public manipulationLerngruppeLehrkraftRing(__param0?: number): void {
		if ((__param0 === undefined)) {
			const rndK: number = this.rnd.nextInt(10);
			let k: number = 1;
			if (rndK < 6) {
				k = 2;
			} else
				if (rndK < 9) {
					k = 3;
				}
			this.manipulationLerngruppeLehrkraftRing(k);
		} else if (((__param0 !== undefined) && typeof __param0 === "number")) {
			const k: number = __param0 as number;
			let lehrkraft1: UvAlgorithmusDynDatenLehrkraft | null = null;
			if (this.rnd.nextBoolean()) {
				lehrkraft1 = this.gibLehrkraftUeberSollZufaelligOderNull();
			}
			if (lehrkraft1 === null) {
				lehrkraft1 = this.gibLehrkraftZufaelligOderNull();
			}
			this.manipulationLerngruppeLehrkraftRingVon(k, lehrkraft1);
		} else {
			throw new Error('invalid method overload');
		}
	}

	/**
	 * Ein Ring-Zug mit fester Ring-Größe und vorgegebener Startlehrkraft (ohne Bewertungsprüfung).
	 *
	 * @param k            Die Ring-Größe (1–4).
	 * @param lehrkraft1   Die Startlehrkraft (oder null).
	 */
	private manipulationLerngruppeLehrkraftRingVon(k: number, lehrkraft1: UvAlgorithmusDynDatenLehrkraft | null): void {
		if (lehrkraft1 === null) {
			return;
		}
		const lernGruppen1: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft1);
		if (lernGruppen1.isEmpty()) {
			return;
		}
		const lerngruppe1: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen1.get(this.rnd.nextInt(lernGruppen1.size()));
		if (k === 1) {
			this.manipulationLerngruppeLehrkraftRingUebernahme(lerngruppe1, lehrkraft1);
			return;
		}
		if (k === 2) {
			this.manipulationLerngruppeLehrkraftRingZwei(lerngruppe1, lehrkraft1);
			return;
		}
		if (k === 3) {
			this.manipulationLerngruppeLehrkraftRingDrei(lerngruppe1, lehrkraft1);
			return;
		}
		this.manipulationLerngruppeLehrkraftRingVier(lerngruppe1, lehrkraft1);
	}

	/**
	 * Ein Ring-Zug der Größe 1: Die abgebende Lehrkraft übergibt eine Lerngruppe an eine
	 * (bevorzugt Unter-Soll-)Lehrkraft. Es wird bis zu dreimal ein Nehmer gezogen.
	 *
	 * @param lerngruppe   Die zu übergebende {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param abgeber      Die abgebende {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private manipulationLerngruppeLehrkraftRingUebernahme(lerngruppe: UvAlgorithmusDynDatenLerngruppe, abgeber: UvAlgorithmusDynDatenLehrkraft): void {
		for (let versuch: number = 0; versuch < 3; versuch++) {
			const nehmer: UvAlgorithmusDynDatenLehrkraft | null = (versuch === 0) ? this.gibLehrkraftUnterSollBevorzugtZufaelligOderNull() : this.gibLehrkraftZufaelligOderNull();
			if (nehmer === null) {
				return;
			}
			if ((nehmer as unknown !== abgeber as unknown) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe, nehmer)) {
				this.stateLerngruppeLehrkraftWechsel(lerngruppe, abgeber, nehmer);
				return;
			}
		}
	}

	/**
	 * Ein Ring-Zug der Größe 2: Zwei Lehrkräfte tauschen je eine übertragbare Lerngruppe.
	 * Die zweite Lehrkraft wird bevorzugt unter den Unter-Soll-Lehrkräften gezogen.
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private manipulationLerngruppeLehrkraftRingZwei(lerngruppe1: UvAlgorithmusDynDatenLerngruppe, lehrkraft1: UvAlgorithmusDynDatenLehrkraft): void {
		for (let versuch: number = 0; versuch < 3; versuch++) {
			const lehrkraft2: UvAlgorithmusDynDatenLehrkraft | null = (versuch === 0) ? this.gibLehrkraftUnterSollBevorzugtZufaelligOderNull() : this.gibLehrkraftZufaelligOderNull();
			if ((lehrkraft2 !== null) && (lehrkraft2 as unknown !== lehrkraft1 as unknown)) {
				const lernGruppen2: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
				if (!lernGruppen2.isEmpty()) {
					const lerngruppe2: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen2.get(this.rnd.nextInt(lernGruppen2.size()));
					if ((lerngruppe2 as unknown !== lerngruppe1 as unknown) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe1, lehrkraft2) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe2, lehrkraft1)) {
						this.stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
						this.stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft1);
						return;
					}
				}
			}
		}
	}

	/**
	 * Ein Ring-Zug der Größe 3: Drei Lehrkräfte rotieren ihre Lerngruppen im Kreis
	 * (Lehrkraft 1 gibt an 2, 2 an 3, 3 an 1).
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private manipulationLerngruppeLehrkraftRingDrei(lerngruppe1: UvAlgorithmusDynDatenLerngruppe, lehrkraft1: UvAlgorithmusDynDatenLehrkraft): void {
		for (let versuch: number = 0; versuch < 2; versuch++) {
			const lehrkraft2: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftZufaelligOderNull();
			const lehrkraft3: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftZufaelligOderNull();
			if ((lehrkraft2 !== null) && (lehrkraft3 !== null) && (lehrkraft2 as unknown !== lehrkraft1 as unknown) && (lehrkraft3 as unknown !== lehrkraft1 as unknown) && (lehrkraft2 as unknown !== lehrkraft3 as unknown)) {
				const lernGruppen2: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
				const lernGruppen3: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft3);
				if (!lernGruppen2.isEmpty() && !lernGruppen3.isEmpty()) {
					const lerngruppe2: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen2.get(this.rnd.nextInt(lernGruppen2.size()));
					const lerngruppe3: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen3.get(this.rnd.nextInt(lernGruppen3.size()));
					if ((lerngruppe2 as unknown !== lerngruppe1 as unknown) && (lerngruppe3 as unknown !== lerngruppe1 as unknown) && (lerngruppe3 as unknown !== lerngruppe2 as unknown) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe1, lehrkraft2) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe2, lehrkraft3) && UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe3, lehrkraft1)) {
						this.stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
						this.stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft3);
						this.stateLerngruppeLehrkraftWechsel(lerngruppe3, lehrkraft3, lehrkraft1);
						return;
					}
				}
			}
		}
	}

	/**
	 * Ein Ring-Zug der Größe 4: Vier Lehrkräfte rotieren ihre Lerngruppen im Kreis
	 * (Lehrkraft 1 gibt an 2, 2 an 3, 3 an 4, 4 an 1).
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	private manipulationLerngruppeLehrkraftRingVier(lerngruppe1: UvAlgorithmusDynDatenLerngruppe, lehrkraft1: UvAlgorithmusDynDatenLehrkraft): void {
		for (let versuch: number = 0; versuch < 2; versuch++) {
			if (this.versucheRingVier(lerngruppe1, lehrkraft1)) {
				return;
			}
		}
	}

	/**
	 * Führt einen Versuch für einen Ring-Zug der Größe 4 aus.
	 *
	 * @param lerngruppe1   Die Lerngruppe der ersten {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param lehrkraft1    Die erste {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls der Ring-Zug ausgeführt wurde.
	 */
	private versucheRingVier(lerngruppe1: UvAlgorithmusDynDatenLerngruppe, lehrkraft1: UvAlgorithmusDynDatenLehrkraft): boolean {
		const lehrkraft2: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftZufaelligOderNull();
		const lehrkraft3: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftZufaelligOderNull();
		const lehrkraft4: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftZufaelligOderNull();
		if ((lehrkraft2 === null) || (lehrkraft3 === null) || (lehrkraft4 === null) || (lehrkraft2 as unknown === lehrkraft1 as unknown) || (lehrkraft3 as unknown === lehrkraft1 as unknown) || (lehrkraft4 as unknown === lehrkraft1 as unknown) || (lehrkraft2 as unknown === lehrkraft3 as unknown) || (lehrkraft2 as unknown === lehrkraft4 as unknown) || (lehrkraft3 as unknown === lehrkraft4 as unknown)) {
			return false;
		}
		const lernGruppen2: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft2);
		const lernGruppen3: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft3);
		const lernGruppen4: List<UvAlgorithmusDynDatenLerngruppe> = UvAlgorithmusDynDaten.gibNichtFixierteUebertragbareLerngruppen(lehrkraft4);
		if (lernGruppen2.isEmpty() || lernGruppen3.isEmpty() || lernGruppen4.isEmpty()) {
			return false;
		}
		const lerngruppe2: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen2.get(this.rnd.nextInt(lernGruppen2.size()));
		const lerngruppe3: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen3.get(this.rnd.nextInt(lernGruppen3.size()));
		const lerngruppe4: UvAlgorithmusDynDatenLerngruppe | null = lernGruppen4.get(this.rnd.nextInt(lernGruppen4.size()));
		if ((lerngruppe2 as unknown === lerngruppe1 as unknown) || (lerngruppe3 as unknown === lerngruppe1 as unknown) || (lerngruppe4 as unknown === lerngruppe1 as unknown) || (lerngruppe2 as unknown === lerngruppe3 as unknown) || (lerngruppe2 as unknown === lerngruppe4 as unknown) || (lerngruppe3 as unknown === lerngruppe4 as unknown) || !UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe1, lehrkraft2) || !UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe2, lehrkraft3) || !UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe3, lehrkraft4) || !UvAlgorithmusDynDaten.istUebernahmeMoeglich(lerngruppe4, lehrkraft1)) {
			return false;
		}
		this.stateLerngruppeLehrkraftWechsel(lerngruppe1, lehrkraft1, lehrkraft2);
		this.stateLerngruppeLehrkraftWechsel(lerngruppe2, lehrkraft2, lehrkraft3);
		this.stateLerngruppeLehrkraftWechsel(lerngruppe3, lehrkraft3, lehrkraft4);
		this.stateLerngruppeLehrkraftWechsel(lerngruppe4, lehrkraft4, lehrkraft1);
		return true;
	}

	/**
	 * Eine zufällige Lerngruppe fügt eine zufällige potentielle Lehrkraft hinzu.
	 * Im Gegensatz zur Strategie wird keine Bewertung geprüft – der Zustand kann sich verschlechtern.
	 */
	manipulationLerngruppeLehrkraftAdd(): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeZufaelligOderNull();
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
	manipulationLerngruppeLehrkraftDel(): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeZufaelligOderNull();
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
	manipulationKlassenleitung1Add(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaelligOderNull();
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
	manipulationKlassenleitung1Del(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaelligOderNull();
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
	manipulationKlassenleitung2Add(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaelligOderNull();
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
	manipulationKlassenleitung2Del(): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseZufaelligOderNull();
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
	setzeLerngruppeLehrkraftAdd(uvLerngruppeID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(uvLerngruppeID);
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeLerngruppeLehrkraftAdd: Unbekannte Lerngruppen-ID=%d - Aufruf wird ignoriert.", uvLerngruppeID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeLerngruppeLehrkraftAdd: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
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
	setzeLerngruppeLehrkraftDel(uvLerngruppeID: number, uvLehrkraftID: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(uvLerngruppeID);
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeLerngruppeLehrkraftDel: Unbekannte Lerngruppen-ID=%d - Aufruf wird ignoriert.", uvLerngruppeID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeLerngruppeLehrkraftDel: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
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
	setzeKlassenLeitung1Add(uvKlasseID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(uvKlasseID);
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung1Add: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.", uvKlasseID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung1Add: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
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
	setzeKlassenLeitung1Del(uvKlasseID: number, uvLehrkraftID: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(uvKlasseID);
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung1Del: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.", uvKlasseID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung1Del: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
		this.stateKlassenLeitung1Del(klasse, lehrkraft);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert         Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	setzeKlassenLeitung2Add(uvKlasseID: number, uvLehrkraftID: number, fixiert: boolean): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(uvKlasseID);
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung2Add: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.", uvKlasseID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung2Add: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
		this.stateKlassenLeitung2Add(klasse, lehrkraft, fixiert);
	}

	/**
	 * Öffentliche Methode zum Verändern des Zustandes:
	 * <br> Entfernt aus einer Klasse die aktuelle stellv. Klassenleitung (Leitung 2).
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param uvKlasseID      Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	setzeKlassenLeitung2Del(uvKlasseID: number, uvLehrkraftID: number): void {
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(uvKlasseID);
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung2Del: Unbekannte Klassen-ID=%d - Aufruf wird ignoriert.", uvKlasseID));
			return;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(uvLehrkraftID);
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("setzeKlassenLeitung2Del: Unbekannte Lehrkraft-ID=%d - Aufruf wird ignoriert.", uvLehrkraftID));
			return;
		}
		this.stateKlassenLeitung2Del(klasse, lehrkraft);
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt eine Lerngruppen-Lehrkraft-Zuordnung hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert      Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateLerngruppeLehrkraftAdd(lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (lerngruppe.gibIstLehrkraftZugeordnetAddMoeglich(lehrkraft)) {
			this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);
			lerngruppe.stateLehrkraftZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLerngruppeZugeordnetAdd(lerngruppe);
			this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Lerngruppe %s und Lehrkraft %s: Hinzufügen der Zuordnung ist nicht möglich - Aufruf wird ignoriert.", lerngruppe.toString(), lehrkraft.toString()));
		}
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
		if (lerngruppe.gibIstLehrkraftZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLerngruppeZugeordnetDelMoeglich(lerngruppe)) {
			this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, -1);
			lerngruppe.stateLehrkraftZugeordnetDel(lehrkraft);
			lehrkraft.stateLerngruppeZugeordnetDel(lerngruppe);
			this.changeMalusLerngruppeLehrkraft(lerngruppe, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Lerngruppe %s und Lehrkraft %s: Entfernen der Zuordnung ist nicht möglich - Aufruf wird ignoriert.", lerngruppe.toString(), lehrkraft.toString()));
		}
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen Klassenlehrer (Leitung 1) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateKlassenLeitung1Add(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (klasse.gibIstLeitung1ZugeordnetAddMoeglich(lehrkraft) && lehrkraft.gibIstLeitung1IncMoeglich(klasse)) {
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
			klasse.stateLeitung1ZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLeitung1Inc(klasse);
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Klasse %s und Lehrkraft %s: Hinzufügen von Leitung 1 ist nicht möglich - Aufruf wird ignoriert.", klasse.toString(), lehrkraft.toString()));
		}
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
		if (klasse.gibIstLeitung1ZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLeitung1DecMoeglich(klasse)) {
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
			klasse.stateLeitung1ZugeordnetDel(lehrkraft);
			lehrkraft.stateLeitung1Dec(klasse);
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Klasse %s und Lehrkraft %s: Entfernen von Leitung 1 ist nicht möglich - Aufruf wird ignoriert.", klasse.toString(), lehrkraft.toString()));
		}
	}

	/**
	 * Hauptmethode zum Verändern des Zustandes:
	 * <br> Fügt einer Klasse einen stellv. Klassenlehrer (Leitung 2) hinzu.
	 * <br> Aktualisiert die Bewertung.
	 *
	 * @param klasse      Die {@link UvAlgorithmusDynDatenKlasse}.
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Gibt an, ob die Lehrkraft zusätzlich fixiert werden soll.
	 */
	private stateKlassenLeitung2Add(klasse: UvAlgorithmusDynDatenKlasse, lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (klasse.gibIstLeitung2ZugeordnetAddMoeglich(lehrkraft) && lehrkraft.gibIstLeitung2IncMoeglich(klasse)) {
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
			klasse.stateLeitung2ZugeordnetAdd(lehrkraft, fixiert);
			lehrkraft.stateLeitung2Inc(klasse);
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Klasse %s und Lehrkraft %s: Hinzufügen von Leitung 2 ist nicht möglich - Aufruf wird ignoriert.", klasse.toString(), lehrkraft.toString()));
		}
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
		if (klasse.gibIstLeitung2ZugeordnetDelMoeglich(lehrkraft) && lehrkraft.gibIstLeitung2DecMoeglich(klasse)) {
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, -1);
			klasse.stateLeitung2ZugeordnetDel(lehrkraft);
			lehrkraft.stateLeitung2Dec(klasse);
			this.changeMalusKlasseLehrkraft(klasse, lehrkraft, 1);
		} else {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Klasse %s und Lehrkraft %s: Entfernen von Leitung 2 ist nicht möglich - Aufruf wird ignoriert.", klasse.toString(), lehrkraft.toString()));
		}
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
		if (lerngruppe.fach !== null) {
			for (const regel of this.mapFachLehrkraftZuRegeln.get(lerngruppe.fach.interneID).get(lehrkraft.interneID)) {
				regel.changeMalus(faktor);
			}
		}
		for (const r17 of UvAlgorithmusDynDaten.sammleBetroffeneRegel17(lerngruppe, lehrkraft)) {
			r17.changeMalus(faktor);
		}
	}

	/**
	 * Sammelt alle Zeitslot-Regeln (Regel 17), deren Malus sich durch eine Änderung der Zuordnung
	 * der Lehrkraft zur Lerngruppe ändern kann. TODO BAR Umsetzung von Regel 17 nioch unschön
	 *
	 * @param lerngruppe   Die {@link UvAlgorithmusDynDatenLerngruppe}, deren Zuordnung sich ändert.
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}, deren Zuordnung sich ändert.
	 *
	 * @return alle betroffenen Zeitslot-Regeln (Regel 17).
	 */
	private static sammleBetroffeneRegel17(lerngruppe: UvAlgorithmusDynDatenLerngruppe, lehrkraft: UvAlgorithmusDynDatenLehrkraft): List<UvAlgorithmusDynDatenRegel17> {
		const ergebnis: List<UvAlgorithmusDynDatenRegel17> = new ArrayList<UvAlgorithmusDynDatenRegel17>();
		for (const slot of lerngruppe.zeitslots) {
			UvAlgorithmusDynDaten.sammleZeitslotRegeln(lerngruppe.zeitslots, slot, ergebnis);
			for (const andere of lehrkraft.istLerngruppenZugeordnet) {
				UvAlgorithmusDynDaten.sammleZeitslotRegeln(andere.zeitslots, slot, ergebnis);
			}
		}
		return ergebnis;
	}

	/**
	 * Sammelt alle Zeitslot-Regeln der Quelle, die zum selben (Wochentag, Stunde) wie der Referenz-Slot gehören.
	 *
	 * @param quelle    Die zu durchsuchenden Zeitslot-Regeln.
	 * @param slot      Der Referenz-Slot (Wochentag und Stunde).
	 * @param ergebnis  Die Ergebnisliste, in die neue Regeln aufgenommen werden.
	 */
	private static sammleZeitslotRegeln(quelle: List<UvAlgorithmusDynDatenRegel17>, slot: UvAlgorithmusDynDatenRegel17, ergebnis: List<UvAlgorithmusDynDatenRegel17>): void {
		for (const r17 of quelle) {
			if ((r17.wochentag === slot.wochentag) && (r17.stunde === slot.stunde) && !ergebnis.contains(r17)) {
				ergebnis.add(r17);
			}
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

	private gibUvLehrkraefteOderLeer(): List<UvLehrer> {
		try {
			return this.man.lehrerGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		} catch (ex: any) {
			this.log.logLn(LogLevel.ERROR, "Die Lehrkräfte konnten nicht ermittelt werden - sie werden übersprungen.");
			return new ArrayList();
		}
	}

	private gibUvJahrgaengeDerLerngruppeOderLeer(uvLerngruppe: UvLerngruppe): List<JahrgangsDaten> {
		try {
			return this.man.jahrgangsdatenGetMengeByLerngruppe(uvLerngruppe);
		} catch (ex: any) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Jahrgänge der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.", uvLerngruppe.id));
			return new ArrayList();
		}
	}

	private gibUvKlassenDerLerngruppeOderLeer(uvLerngruppe: UvLerngruppe): List<UvKlasse> {
		try {
			return this.man.klasseGetMengeByLerngruppe(uvLerngruppe);
		} catch (ex: any) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Klassen der Lerngruppe UV-ID=%d konnten nicht ermittelt werden - leere Liste wird verwendet.", uvLerngruppe.id));
			return new ArrayList();
		}
	}

	private createFaecher(): Array<UvAlgorithmusDynDatenFach> {
		const uvFachMenge: List<UvFach> = new ArrayList<UvFach>(this.man.fachGetMengeAsList());
		const temp: Array<UvAlgorithmusDynDatenFach> = Array(uvFachMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvFach: UvFach = uvFachMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenFach(i, uvFach);
			if (this.mapIDzuDynFach.get(uvFach.id) !== null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Bei der Initialisierung der Fächer gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.", uvFach.id));
			} else {
				this.mapIDzuDynFach.put(uvFach.id, temp[i]);
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
			if (this.mapIDzuDynJahrgang.get(uvJahrgang.id) !== null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Bei der Initialisierung der Jahrgänge gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.", uvJahrgang.id));
			} else {
				this.mapIDzuDynJahrgang.put(uvJahrgang.id, temp[i]);
			}
		}
		return temp;
	}

	private createLehrkraefte(): Array<UvAlgorithmusDynDatenLehrkraft> {
		const uvLehrkraftMenge: List<UvLehrer> = this.gibUvLehrkraefteOderLeer();
		if (uvLehrkraftMenge.isEmpty()) {
			return Array(0).fill(null);
		}
		const temp: Array<UvAlgorithmusDynDatenLehrkraft> = Array(uvLehrkraftMenge.size()).fill(null);
		for (let i: number = 0; i < temp.length; i++) {
			const uvLehrkraft: UvLehrer = uvLehrkraftMenge.get(i);
			temp[i] = new UvAlgorithmusDynDatenLehrkraft(i, this.log, this.man, this.planungsabschnitt, uvLehrkraft);
			if (this.mapIDzuDynLehrkraft.get(uvLehrkraft.id) !== null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Bei der Initialisierung der Lehrkräfte gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.", uvLehrkraft.id));
			} else {
				this.mapIDzuDynLehrkraft.put(uvLehrkraft.id, temp[i]);
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
			if (this.mapIDzuDynKlasse.get(uvKlasse.id) !== null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Bei der Initialisierung der Klassen gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.", uvKlasse.id));
			} else {
				this.mapIDzuDynKlasse.put(uvKlasse.id, temp[i]);
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
			if (this.mapIDzuDynLerngruppe.get(uvLerngruppe.id) !== null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Bei der Initialisierung der Lerngruppen gab es die ID=%d doppelt! - Der doppelte Eintrag wird ignoriert.", uvLerngruppe.id));
			} else {
				this.mapIDzuDynLerngruppe.put(uvLerngruppe.id, temp[i]);
			}
		}
		return temp;
	}

	private createJahrgaengeOfLerngruppe(uvLerngruppe: UvLerngruppe): List<UvAlgorithmusDynDatenJahrgang> {
		const list: List<JahrgangsDaten> = this.gibUvJahrgaengeDerLerngruppeOderLeer(uvLerngruppe);
		const jahrgangmenge: List<UvAlgorithmusDynDatenJahrgang> = new ArrayList<UvAlgorithmusDynDatenJahrgang>();
		for (let i: number = 0; i < list.size(); i++) {
			const uvJahrgang: JahrgangsDaten = list.get(i);
			const dynJahrgang: UvAlgorithmusDynDatenJahrgang | null = this.mapIDzuDynJahrgang.get(uvJahrgang.id);
			if (dynJahrgang === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Das Mapping des Jahrgangs UV-ID=%d zu DYN wurde nicht gefunden! - Der Jahrgang wird ignoriert.", uvJahrgang.id));
				continue;
			}
			jahrgangmenge.add(dynJahrgang);
		}
		return jahrgangmenge;
	}

	private createKlassenOfLerngruppe(uvLerngruppe: UvLerngruppe): List<UvAlgorithmusDynDatenKlasse> {
		const list: List<UvKlasse> = this.gibUvKlassenDerLerngruppeOderLeer(uvLerngruppe);
		const klassenmenge: List<UvAlgorithmusDynDatenKlasse> = new ArrayList<UvAlgorithmusDynDatenKlasse>();
		for (let i: number = 0; i < list.size(); i++) {
			const uvKlasse: UvKlasse = list.get(i);
			const dynKlasse: UvAlgorithmusDynDatenKlasse | null = this.mapIDzuDynKlasse.get(uvKlasse.id);
			if (dynKlasse === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Das Mapping der Klasse UV-ID=%d zu DYN wurde nicht gefunden! - Die Klasse wird ignoriert.", uvKlasse.id));
				continue;
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
	 * @return das {@link UvAlgorithmusDynDatenFach} der Lerngruppe oder null, falls das Mapping nicht gefunden wurde.
	 */
	gibDynFachByLerngruppe(uvLerngruppe: UvLerngruppe): UvAlgorithmusDynDatenFach | null {
		let idFach: number | null = null;
		try {
			const fach: UvFach | null = this.man.fachGetByLerngruppe(uvLerngruppe);
			if (fach !== null) {
				idFach = fach.id;
			}
		} catch (ex: any) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Das Fach der Lerngruppe UV-ID=%d konnte nicht ermittelt werden - null wird verwendet.", uvLerngruppe.id));
		}
		const dynFach: UvAlgorithmusDynDatenFach | null = this.mapIDzuDynFach.get(idFach);
		if (dynFach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Das Mapping des Faches UV-ID=%d zu DYN wurde nicht gefunden! - null wird verwendet.", idFach));
		}
		return dynFach;
	}

	/**
	 * Liefert den {@link Logger}.
	 *
	 * @return den {@link Logger}.
	 */
	gibLogger(): Logger {
		return this.log;
	}

	/**
	 * Liefert das {@link Random}-Objekt.
	 *
	 * @return das {@link Random}-Objekt.
	 */
	gibRandom(): Random {
		return this.rnd;
	}

	private gibLerngruppeZufaelligOderNull(): UvAlgorithmusDynDatenLerngruppe | null {
		if (this.aLerngruppe.length === 0) {
			return null;
		}
		return this.aLerngruppe[this.rnd.nextInt(this.aLerngruppe.length)];
	}

	private static gibNichtFixierteLerngruppen(lehrkraft: UvAlgorithmusDynDatenLehrkraft): List<UvAlgorithmusDynDatenLerngruppe> {
		const result: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (const lerngruppe of lehrkraft.istLerngruppenZugeordnet) {
			if (!lerngruppe.gibIstLehrkraftFixiert(lehrkraft)) {
				result.add(lerngruppe);
			}
		}
		return result;
	}

	private gibLehrkraftUeberSollZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		const kandidaten: List<UvAlgorithmusDynDatenLehrkraft> = new ArrayList<UvAlgorithmusDynDatenLehrkraft>();
		for (const lehrkraft of this.aLehrkraft) {
			if ((lehrkraft.stundenSoll - lehrkraft.stundenAnrechnung - lehrkraft.istStundensummeLerngruppen) < 0) {
				kandidaten.add(lehrkraft);
			}
		}
		if (kandidaten.isEmpty()) {
			return null;
		}
		return kandidaten.get(this.rnd.nextInt(kandidaten.size()));
	}

	private gibKlasseZufaelligOderNull(): UvAlgorithmusDynDatenKlasse | null {
		if (this.aKlasse.length === 0) {
			return null;
		}
		return this.aKlasse[this.rnd.nextInt(this.aKlasse.length)];
	}

	/**
	 * Liefert die aktuelle Lerngruppen-Lehrkraft-Zuordnung.
	 * <br>Hinweis: Die Liste kann potentiell auch leer sein.
	 *
	 * @return die aktuelle Lerngruppen-Lehrkraft-Zuordnung.
	 */
	gibAktuelleZuordnung(): List<UvLerngruppenLehrer> {
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
	gibAktuelleKlassenleitungen(): List<UvKlassenLehrer> {
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
	gibMalusDerPrioritaet(prioritaet: UvBlockungRegelPrioritaet): number {
		return this.malus[prioritaet.nr];
	}

	/**
	 * Liefert eine Kopie des aktuellen Malus-Arrays.
	 *
	 * @return eine Kopie des aktuellen Malus-Arrays.
	 */
	gibMalusKopie(): Array<number> {
		const c: Array<number> = Array(this.malus.length).fill(0);
		System.arraycopy(this.malus, 0, c, 0, c.length);
		return c;
	}

	/**
	 * Liefert eine Beschreibung des aktuellen Malus-Zustandes.
	 *
	 * @return eine Beschreibung des aktuellen Malus-Zustandes.
	 */
	gibMalusBeschreibung(): string {
		return Arrays.toString(this.malus);
	}

	/**
	 * Prüft, ob die übergebene Regel in der Menge fehlerhafter Regeln enthalten ist.
	 *
	 * @param regel   Die zu prüfende Regel.
	 *
	 * @return {@code true}, wenn die Regel als fehlerhaft erkannt wurde, sonst {@code false}.
	 */
	gibIstRegelFehlerhaft(regel: UvBlockungRegel): boolean {
		return this.manRegeln.regelnGetMengeFehlerhaftAsList().contains(regel);
	}

	/**
	 * Liefert die {@link UvAlgorithmusDynDatenKlasse} zur übergebenen UV-ID oder null.
	 *
	 * @param uvKlasseID   Die UV-ID der {@link UvAlgorithmusDynDatenKlasse}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenKlasse} zur übergebenen UV-ID oder null.
	 */
	private gibKlasseOrNull(uvKlasseID: number): UvAlgorithmusDynDatenKlasse | null {
		return this.mapIDzuDynKlasse.get(uvKlasseID);
	}

	/**
	 * Liefert die {@link UvAlgorithmusDynDatenLehrkraft} zur übergebenen UV-ID oder null.
	 *
	 * @param uvLehrkraftID   Die UV-ID der {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenLehrkraft} zur übergebenen UV-ID oder null.
	 */
	private gibLehrkraftOrNull(uvLehrkraftID: number): UvAlgorithmusDynDatenLehrkraft | null {
		return this.mapIDzuDynLehrkraft.get(uvLehrkraftID);
	}

	/**
	 * Liefert die {@link UvAlgorithmusDynDatenLerngruppe} zur übergebenen UV-ID oder null.
	 *
	 * @param uvLerngruppeID   Die UV-ID der {@link UvAlgorithmusDynDatenLerngruppe}.
	 *
	 * @return die {@link UvAlgorithmusDynDatenLerngruppe} zur übergebenen UV-ID oder null.
	 */
	private gibLerngruppeOrNull(uvLerngruppeID: number): UvAlgorithmusDynDatenLerngruppe | null {
		return this.mapIDzuDynLerngruppe.get(uvLerngruppeID);
	}

	/**
	 * Liefert den {@link UvAlgorithmusDynDatenJahrgang} zur übergebenen UV-ID oder null.
	 *
	 * @param uvJahrgangID   Die UV-ID des {@link UvAlgorithmusDynDatenJahrgang}.
	 *
	 * @return den {@link UvAlgorithmusDynDatenJahrgang} zur übergebenen UV-ID oder null.
	 */
	private gibJahrgangOrNull(uvJahrgangID: number | null): UvAlgorithmusDynDatenJahrgang | null {
		return this.mapIDzuDynJahrgang.get(uvJahrgangID);
	}

	/**
	 * Liefert das {@link UvAlgorithmusDynDatenFach} zur übergebenen UV-ID oder null.
	 *
	 * @param uvFachID   Die UV-ID des {@link UvAlgorithmusDynDatenFach}.
	 *
	 * @return das {@link UvAlgorithmusDynDatenFach} zur übergebenen UV-ID oder null.
	 */
	private gibFachOrNull(uvFachID: number | null): UvAlgorithmusDynDatenFach | null {
		return this.mapIDzuDynFach.get(uvFachID);
	}

	private initLerngruppenLehrkraftPotentielle(): void {
		const uvLerngruppen: List<UvLerngruppe> = this.man.lerngruppeGetMengeByPlanungsabschnitt(this.planungsabschnitt);
		const uvLehrkraft: List<UvLehrer> = this.gibUvLehrkraefteOderLeer();
		if (uvLehrkraft.isEmpty()) {
			return;
		}
		for (let iLerngruppe: number = 0; iLerngruppe < this.aLerngruppe.length; iLerngruppe++) {
			const lerngruppe: UvLerngruppe = uvLerngruppen.get(iLerngruppe);
			for (let iLehrkraft: number = 0; iLehrkraft < this.aLehrkraft.length; iLehrkraft++) {
				const lehrkraft: UvLehrer = uvLehrkraft.get(iLehrkraft);
				let hatLehrbefaehigung: boolean;
				try {
					hatLehrbefaehigung = this.man.lehrerHatLehrbefaehigungLerngruppe(lehrkraft, lerngruppe);
				} catch (ex: any) {
					this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrbefähigung der Lehrkraft UV-ID=%d für die Lerngruppe UV-ID=%d konnte nicht ermittelt werden - sie wird übersprungen.", lehrkraft.id, lerngruppe.id));
					hatLehrbefaehigung = false;
				}
				if (hatLehrbefaehigung) {
					this.aLerngruppe[iLerngruppe].fuegeLehrkraftHinzu(this.aLehrkraft[iLehrkraft]);
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
				this.initRegel03StellvKlassenlehrerDefault(parameter, prioritaet)
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
				this.initRegel09LehrkraftMaxLeitung1Default(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_KLASSENLEHRER: {
				this.initRegel10LehrkraftMaxLeitung1(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_DEFAULT_IST_MAXIMAL_A_MAL_STELLV_KLASSENLEHRER: {
				this.initRegel11LehrkraftMaxLeitung2Default(parameter, prioritaet)
				break;
			}
			case UvBlockungRegelTyp.LEHRKRAFT_A_IST_MAXIMAL_B_MAL_STELLV_KLASSENLEHRER: {
				this.initRegel12LehrkraftMaxLeitung2(parameter, prioritaet)
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
				this.initRegel19LehrkraftVerbotenInJahrgang(parameter)
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
				this.initRegel27LehrkraftNichtInDerSelbenKlasse(parameter, prioritaet)
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
				this.log.logLn(LogLevel.ERROR, JavaString.format("Unbekannter Regeltyp: %d - Regel wird ignoriert.", regel.typ))
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
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(0));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 02: Unbekannte Klassen-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const neuLeitung1Anzahl: number = parameter.get(1);
		for (const r of klasse.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel02')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel02).setzeLeitung1AnzahlUndPrioritaet(neuLeitung1Anzahl, prioritaet);
			}
		}
	}

	private initRegel03StellvKlassenlehrerDefault(parameter: List<number>, prioritaet: number): void {
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
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(0));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 04: Unbekannte Klassen-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
				const r06: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel06(this.log, this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
			}
		}
	}

	private initRegel06WennLehrkraftLeitung1Dann(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 06: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
			const r06: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel06(this.log, this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung1, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r06);
		}
	}

	private initRegel07WennLeitung2Dann(parameter: List<number>, prioritaet: number): void {
		const minKlassenstundenBeiLeitung2: number = parameter.get(0);
		for (const klasse of this.aKlasse) {
			for (const lehrkraft of this.aLehrkraft) {
				const r08: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel08(this.log, this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
			}
		}
	}

	private initRegel08WennLehrkraftLeitung2Dann(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 08: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
			const r08: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel08(this.log, this.malus, klasse, lehrkraft, minKlassenstundenBeiLeitung2, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r08);
		}
	}

	private initRegel09LehrkraftMaxLeitung1Default(parameter: List<number>, prioritaet: number): void {
		const maxLeitung1: number = parameter.get(0);
		for (const lehrkraft of this.aLehrkraft) {
			const r10: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel10(this.log, this.malus, lehrkraft, maxLeitung1, prioritaet);
			for (const klasse of this.aKlasse) {
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
			}
		}
	}

	private initRegel10LehrkraftMaxLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 10: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
		const r10: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel10(this.log, this.malus, lehrkraft, maxLeitung1, prioritaet);
		for (const klasse of this.aKlasse) {
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r10);
		}
	}

	private initRegel11LehrkraftMaxLeitung2Default(parameter: List<number>, prioritaet: number): void {
		const maxLeitung2: number = parameter.get(0);
		for (const lehrkraft of this.aLehrkraft) {
			const r12: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel12(this.log, this.malus, lehrkraft, maxLeitung2, prioritaet);
			for (const klasse of this.aKlasse) {
				this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
			}
		}
	}

	private initRegel12LehrkraftMaxLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 12: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
		const r12: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel12(this.log, this.malus, lehrkraft, maxLeitung2, prioritaet);
		for (const klasse of this.aKlasse) {
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r12);
		}
	}

	private initRegel13LehrkraftVerbotenInLerngruppe(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 13: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 13: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		lerngruppe.entferneLehrkraft(lehrkraft);
	}

	private initRegel14LehrkraftFixiertInLerngruppe(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 14: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 14: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		this.stateLerngruppeLehrkraftAdd(lerngruppe, lehrkraft, true);
	}

	private initRegel15LehrkraftGerneInLerngruppe(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 15: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 15: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r15: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel15(this.log, this.malus, lerngruppe, lehrkraft, prioritaet);
		this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r15);
	}

	private initRegel16LehrkraftUngernInLerngruppe(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 16: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(1));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 16: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r16: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel16(this.log, this.malus, lerngruppe, lehrkraft, prioritaet);
		this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r16);
	}

	private initRegel17LerngruppeHatWochentagStundeWochentyp(parameter: List<number>, prioritaet: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(0));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 17: Unbekannte Lerngruppen-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const wochentag: number = parameter.get(1);
		const stunde: number = parameter.get(2);
		const wochentyp: number = parameter.get(3);
		const r17: UvAlgorithmusDynDatenRegel17 = new UvAlgorithmusDynDatenRegel17(this.log, this.malus, lerngruppe, wochentag, stunde, wochentyp, prioritaet);
		lerngruppe.zeitslots.add(r17);
	}

	private initRegel18LehrkraftVerbotenInKlasse(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 18: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 18: Unbekannte Klassen-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennKlasseUebereinstimmt(lehrkraft, klasse);
		}
	}

	private initRegel19LehrkraftVerbotenInJahrgang(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 19: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const jahrgang: UvAlgorithmusDynDatenJahrgang | null = this.gibJahrgangOrNull(parameter.get(1));
		if (jahrgang === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 19: Unbekannte Jahrgang-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUebereinstimmt(lehrkraft, jahrgang);
		}
	}

	private initRegel20LehrkraftHatMindestensFach(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 20: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const minimum: number = parameter.get(1);
		const fach: UvAlgorithmusDynDatenFach | null = this.gibFachOrNull(parameter.get(2));
		if (fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 20: Unbekannte Fach-ID=%d - Regel wird ignoriert.", parameter.get(2)));
			return;
		}
		const r20: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel20(this.log, this.malus, fach, lehrkraft, minimum, prioritaet);
		this.mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r20);
	}

	private initRegel21LehrkraftHatMaximalFach(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 21: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const maximum: number = parameter.get(1);
		const fach: UvAlgorithmusDynDatenFach | null = this.gibFachOrNull(parameter.get(2));
		if (fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 21: Unbekannte Fach-ID=%d - Regel wird ignoriert.", parameter.get(2)));
			return;
		}
		const r21: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel21(this.log, this.malus, fach, lehrkraft, maximum, prioritaet);
		this.mapFachLehrkraftZuRegeln.get(fach.interneID).get(lehrkraft.interneID).add(r21);
	}

	private initRegel22LehrkraftHatKorrekturenMin(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 22: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const minimum: number = parameter.get(1);
		const r22: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel22(this.log, this.malus, lehrkraft, minimum, prioritaet);
		lehrkraft.regeln.add(r22);
	}

	private initRegel23LehrkraftHatKorrekturenMax(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 23: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const maximum: number = parameter.get(1);
		const r23: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel23(this.log, this.malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r23);
	}

	private initRegel24LehrkraftDarfNichtFachUnterrichtenInDenJahrgaengen(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 24: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const fach: UvAlgorithmusDynDatenFach | null = this.gibFachOrNull(parameter.get(1));
		if (fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 24: Unbekannte Fach-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const jahrgaenge: List<UvAlgorithmusDynDatenJahrgang> = new ArrayList<UvAlgorithmusDynDatenJahrgang>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const jahrgang: UvAlgorithmusDynDatenJahrgang | null = this.gibJahrgangOrNull(parameter.get(i));
			if (jahrgang === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 24: Unbekannte Jahrgang-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			jahrgaenge.add(jahrgang);
		}
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennJahrgangUndFachUebereinstimmt(lehrkraft, fach, jahrgaenge);
		}
	}

	private initRegel25LehrkraftDarfNichtFachUnterrichten(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 25: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const fach: UvAlgorithmusDynDatenFach | null = this.gibFachOrNull(parameter.get(1));
		if (fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 25: Unbekannte Fach-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		for (const lerngruppe of this.aLerngruppe) {
			lerngruppe.entferneLehrkraftWennFachUebereinstimmt(lehrkraft, fach);
		}
	}

	private initRegel26LehrkraftHatMaximaleJahrgangsAnzahl(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 26: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const maximum: number = parameter.get(1);
		const r26: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel26(this.log, this.malus, lehrkraft, maximum, prioritaet);
		lehrkraft.regeln.add(r26);
	}

	private initRegel27LehrkraftNichtInDerSelbenKlasse(parameter: List<number>, prioritaet: number): void {
		const lehrkraft1: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft1 === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 27: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const lehrkraft2: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(1));
		if (lehrkraft2 === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 27: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		for (const klasse of this.aKlasse) {
			const r27: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel27(this.log, this.malus, klasse, lehrkraft1, lehrkraft2, prioritaet);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft1.interneID).add(r27);
			this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft2.interneID).add(r27);
		}
	}

	private initRegel28LerngruppeBenoetigtBLehrkraefte(parameter: List<number>, prioritaet: number): void {
		const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(0));
		if (lerngruppe === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 28: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
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
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 29: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}
		const r29: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel29(this.log, this.malus, lerngruppenMenge, this.aLehrkraft.length, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			lerngruppe.regeln.add(r29);
		}
	}

	private initRegel30LerngruppenHabenVerschiedeneLehrkraefte(parameter: List<number>, prioritaet: number): void {
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 0; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 30: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}
		const r30: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel30(this.log, this.malus, lerngruppenMenge, this.aLehrkraft.length, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			lerngruppe.regeln.add(r30);
		}
	}

	private initRegel31LerngruppenGemeinsamKuerzenAuf(parameter: List<number>): void {
		const neuerStundenSoll: number = parameter.get(0);
		for (let i: number = 1; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 31: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			lerngruppe.setzeStundenSollAuf(neuerStundenSoll);
		}
	}

	private initRegel32LehrkraftFixiertAlsLeitung1(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 32: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 32: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		this.stateKlassenLeitung1Add(klasse, lehrkraft, true);
	}

	private initRegel33LehrkraftFixiertAlsLeitung2(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 33: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 33: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		this.stateKlassenLeitung2Add(klasse, lehrkraft, true);
	}

	private initRegel34LehrkraftVerbotenAlsLeitung1(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 34: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 34: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		klasse.entferneLeitung1(lehrkraft);
	}

	private initRegel35LehrkraftVerbotenAlsLeitung2(parameter: List<number>): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 35: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 35: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		klasse.entferneLeitung2(lehrkraft);
	}

	private initRegel36LehrkraftGerneAlsLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 36: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 36: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r36: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel36(this.log, this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r36);
	}

	private initRegel37LehrkraftGerneAlsLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 37: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 37: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r37: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel37(this.log, this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r37);
	}

	private initRegel38LehrkraftUngerneAlsLeitung1(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 38: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 38: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r38: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel38(this.log, this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r38);
	}

	private initRegel39LehrkraftUngerneAlsLeitung2(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 39: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const klasse: UvAlgorithmusDynDatenKlasse | null = this.gibKlasseOrNull(parameter.get(1));
		if (klasse === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 39: Unbekannte Klasse-ID=%d - Regel wird ignoriert.", parameter.get(1)));
			return;
		}
		const r39: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel39(this.log, this.malus, klasse, lehrkraft, prioritaet);
		this.mapKlasseLehrkraftZuRegeln.get(klasse.interneID).get(lehrkraft.interneID).add(r39);
	}

	private initRegel40LehrkraftMaxAnzahlLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 40: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const max: number = parameter.get(1);
		const r40: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel40(this.log, this.malus, lehrkraft, max, prioritaet);
		lehrkraft.regeln.add(r40);
	}

	private initRegel41LehrkraftMinAnzahlLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 41: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const min: number = parameter.get(1);
		const r41: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel41(this.log, this.malus, lehrkraft, min, prioritaet);
		lehrkraft.regeln.add(r41);
	}

	private initRegel42LehrkraftMinOftInLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 42: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const min: number = parameter.get(1);
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 42: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}
		const r42: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel42(this.log, this.malus, lehrkraft, min, lerngruppenMenge, prioritaet);
		for (const lerngruppe of lerngruppenMenge) {
			this.mapLerngruppeLehrkraftZuRegeln.get(lerngruppe.interneID).get(lehrkraft.interneID).add(r42);
		}
	}

	private initRegel43LehrkraftMaxOftInLerngruppen(parameter: List<number>, prioritaet: number): void {
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 43: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const max: number = parameter.get(1);
		const lerngruppenMenge: List<UvAlgorithmusDynDatenLerngruppe> = new ArrayList<UvAlgorithmusDynDatenLerngruppe>();
		for (let i: number = 2; i < parameter.size(); i++) {
			const lerngruppe: UvAlgorithmusDynDatenLerngruppe | null = this.gibLerngruppeOrNull(parameter.get(i));
			if (lerngruppe === null) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 43: Unbekannte Lerngruppe-ID=%d - Regel wird ignoriert.", parameter.get(i)));
				return;
			}
			lerngruppenMenge.add(lerngruppe);
		}
		const r43: UvAlgorithmusDynDatenRegel = new UvAlgorithmusDynDatenRegel43(this.log, this.malus, lehrkraft, max, lerngruppenMenge, prioritaet);
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
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft | null = this.gibLehrkraftOrNull(parameter.get(0));
		if (lehrkraft === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Regel 45: Unbekannte Lehrkraft-ID=%d - Regel wird ignoriert.", parameter.get(0)));
			return;
		}
		const aktiviert: boolean = (parameter.get(1) === 1);
		for (const r of lehrkraft.regeln) {
			if (((r instanceof JavaObject) && (r.isTranspiledInstanceOf('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenRegel45')))) {
				(r as unknown as UvAlgorithmusDynDatenRegel45).setzeAuf(aktiviert, prioritaet);
			}
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
