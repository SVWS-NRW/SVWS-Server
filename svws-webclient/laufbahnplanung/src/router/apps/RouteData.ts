import { shallowRef } from "vue";

import type { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLadeDaten } from "~/router/apps/RouteLadeDaten";

import { RouteManager } from "../RouteManager";
import { routeLaufbahnplanung } from "./RouteLaufbahnplanung";
import type { ApiFile } from "@core/api/BaseApi";
import { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import { JsonCoreTypeReaderStatic } from "@core/asd/utils/JsonCoreTypeReaderStatic";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
import { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostLaufbahnplanungDaten } from "@core/core/data/gost/GostLaufbahnplanungDaten";
import { GostLaufbahnplanungDatenFachbelegung } from "@core/core/data/gost/GostLaufbahnplanungDatenFachbelegung";
import { GostLaufbahnplanungDatenSchueler } from "@core/core/data/gost/GostLaufbahnplanungDatenSchueler";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostKursart } from "@core/core/types/gost/GostKursart";
import { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { Config, ConfigElement } from "@ui/utils/Config";
import { ServerMode } from "@core/core/types/ServerMode";


interface RouteState {
	serverMode: ServerMode,
	schuleStammdaten: SchuleStammdaten;
	auswahl: SchuelerListeEintrag | undefined;
	schuelerIDEncrypted: string;
	beratungslehrer : List<GostBeratungslehrer>;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager | undefined;
	config: Config;
	gostBelegpruefungsArt: 'ef1' | 'gesamt' | 'auto';
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	zwischenspeicher: Abiturdaten | undefined;
	dirty: boolean;
	view: RouteNode<any, any>;
}

export class RouteData {

	private static _defaultState : RouteState = {
		serverMode: ServerMode.STABLE,
		schuleStammdaten: new SchuleStammdaten(),
		auswahl: undefined,
		schuelerIDEncrypted: '',
		beratungslehrer: new ArrayList(),
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: undefined,
		config: new Config(async (key, value) => { }, async (key, value) => { }),
		gostBelegpruefungsArt: 'gesamt',
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		zwischenspeicher: undefined,
		dirty: false,
		view: routeLadeDaten,
	}

	private _state = shallowRef<RouteState>(RouteData._defaultState);
	private reader = new JsonCoreTypeReaderStatic();

	private setPatchedDefaultState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... RouteData._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public constructor() {
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.modus", "user", "normal")]);
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.faecher.anzeigen", "user", "alle")]);
	}

	public get serverMode(): ServerMode {
		return this._state.value.serverMode;
	}

	public get config(): Config {
		return this._state.value.config;
	}

	public get dirty(): boolean {
		return this._state.value.dirty;
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeApp.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	protected createAbiturdatenmanager(faecherManager?: GostFaecherManager, daten?: Abiturdaten): AbiturdatenManager | undefined {
		const abiturdaten = (daten === undefined) ? this._state.value.abiturdaten : daten;
		const fachManager = (faecherManager === undefined) ? this._state.value.faecherManager : faecherManager;
		if ((abiturdaten === undefined) || (fachManager === undefined))
			return undefined;
		const jahrgangsdaten = this._state.value.gostJahrgangsdaten;
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1')
			return new AbiturdatenManager(this.serverMode, abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
		if (art === 'gesamt')
			return new AbiturdatenManager(this.serverMode, abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		const abiturdatenManager = new AbiturdatenManager(this.serverMode, abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen(), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			return abiturdatenManager;
		return new AbiturdatenManager(this.serverMode, abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
	}

	public async ladeDaten(daten: GostLaufbahnplanungDaten) {
		// Lade die Informationen zur Schule
		const schuleStammdaten = new SchuleStammdaten();
		schuleStammdaten.schulNr = daten.schulNr;
		schuleStammdaten.bezeichnung1 = daten.schulBezeichnung1;
		schuleStammdaten.bezeichnung2 = daten.schulBezeichnung2;
		schuleStammdaten.bezeichnung3 = daten.schulBezeichnung3;
		// Lade die Jahrgangsinformationen
		const gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		gostJahrgang.bezeichnung = "Abiturjahr " + daten.abiturjahr;
		gostJahrgang.istAbgeschlossen = false;
		const gostJahrgangsdaten = new GostJahrgangsdaten();
		gostJahrgangsdaten.abiturjahr = gostJahrgang.abiturjahr;
		gostJahrgangsdaten.jahrgang = gostJahrgang.jahrgang;
		gostJahrgangsdaten.bezeichnung = gostJahrgang.bezeichnung;
		gostJahrgangsdaten.istAbgeschlossen = gostJahrgang.istAbgeschlossen;
		gostJahrgangsdaten.hatZusatzkursGE = daten.hatZusatzkursGE;
		gostJahrgangsdaten.beginnZusatzkursGE = daten.beginnZusatzkursGE;
		gostJahrgangsdaten.hatZusatzkursSW = daten.hatZusatzkursSW;
		gostJahrgangsdaten.beginnZusatzkursSW = daten.beginnZusatzkursSW;
		gostJahrgangsdaten.textBeratungsbogen = daten.textBeratungsbogen;
		gostJahrgangsdaten.textMailversand = null;
		// Initialisiere den Fächer-Manager mit den Fächerdaten
		const faecherManager = new GostFaecherManager(daten.abiturjahr - 1, daten.faecher);
		faecherManager.addFachkombinationenAll(daten.fachkombinationen);
		// Bestimme die importierten Laufbahnplanungsdaten für den Schüler
		const planungsdaten = daten.schueler.get(0);
		// Erstelle das Schüler-Objekt für die Anzeige
		const schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.geschlecht = planungsdaten.geschlecht;
		schueler.abiturjahrgang = gostJahrgang.abiturjahr;
		// Erstelle das Abiturdaten-Objekt mit den Fachbelegungen
		const abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		abiturdaten.sprachendaten = planungsdaten.sprachendaten;
		abiturdaten.bilingualeSprache = planungsdaten.bilingualeSprache;
		for (const hj of GostHalbjahr.values())
			abiturdaten.bewertetesHalbjahr[hj.id] = planungsdaten.bewertetesHalbjahr[hj.id];
		for (let i = 0; i < planungsdaten.fachbelegungen.size() ; i++) {
			const belegung = new AbiturFachbelegung();
			const fb = planungsdaten.fachbelegungen.get(i);
			const fach = faecherManager.get(fb.fachID);
			if (fach === null)
				continue;
			belegung.fachID = fb.fachID;
			belegung.abiturFach = fb.abiturFach;
			belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
			for (const hj of GostHalbjahr.values()) {
				const kursart = fb.kursart[hj.id];
				if (kursart === null)
					continue;
				const hjBelegung = new AbiturFachbelegungHalbjahr();
				hjBelegung.halbjahrKuerzel = hj.kuerzel;
				hjBelegung.kursartKuerzel = kursart;
				hjBelegung.schriftlich = fb.schriftlich[hj.id];
				hjBelegung.biliSprache = fach.biliSprache;
				if (fach.kuerzel === "PX")
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(faecherManager, abiturdaten);
		if (abiturdatenManager === undefined)
			throw new UserNotificationException("Belegprüfungsergebnis konnte nicht berechnet werden.");
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		gostJahrgangsdaten.istBlockungFestgelegt = abiturdaten.bewertetesHalbjahr;
		this.setPatchedDefaultState({
			schuleStammdaten,
			auswahl: schueler,
			schuelerIDEncrypted: planungsdaten.idEnc,
			beratungslehrer: daten.beratungslehrer,
			gostJahrgang,
			gostJahrgangsdaten,
			faecherManager,
			abiturdaten,
			abiturdatenManager: abiturdatenManager,
			gostBelegpruefungErgebnis,
		})
	}

	public async schreibeDaten() : Promise<GostLaufbahnplanungDaten> {
		if ((this._state.value.faecherManager === undefined) || (this._state.value.abiturdaten === undefined) || (this._state.value.auswahl === undefined))
			throw new UserNotificationException("Es müssen Abiturdaten geladen sein.");
		const daten = new GostLaufbahnplanungDaten();
		daten.schulNr = this._state.value.schuleStammdaten.schulNr;
		daten.schulBezeichnung1 = this._state.value.schuleStammdaten.bezeichnung1;
		daten.schulBezeichnung2 = (this._state.value.schuleStammdaten.bezeichnung2 === null) ? "" : this._state.value.schuleStammdaten.bezeichnung2;
		daten.schulBezeichnung3 = (this._state.value.schuleStammdaten.bezeichnung3 === null) ? "" : this._state.value.schuleStammdaten.bezeichnung3;
		daten.anmerkungen = "Letzte Änderung am " + (new Date()).toLocaleDateString("de-DE", { dateStyle: "short" });
		daten.abiturjahr = this._state.value.abiturdaten.abiturjahr;
		daten.jahrgang = this._state.value.gostJahrgang.jahrgang;
		daten.hatZusatzkursGE = this._state.value.gostJahrgangsdaten.hatZusatzkursGE;
		daten.beginnZusatzkursGE = this._state.value.gostJahrgangsdaten.beginnZusatzkursGE;
		daten.hatZusatzkursSW = this._state.value.gostJahrgangsdaten.hatZusatzkursSW;
		daten.beginnZusatzkursSW = this._state.value.gostJahrgangsdaten.beginnZusatzkursSW;
		daten.beratungslehrer.addAll(this._state.value.beratungslehrer);
		daten.textBeratungsbogen = this._state.value.gostJahrgangsdaten.textBeratungsbogen;
		for (const fk of this._state.value.faecherManager.getFachkombinationen())
			daten.fachkombinationen.add(fk);
		daten.faecher.addAll(this._state.value.faecherManager.faecher());
		const s = new GostLaufbahnplanungDatenSchueler();
		s.id = this._state.value.auswahl.id;
		s.idEnc = this._state.value.schuelerIDEncrypted;
		s.vorname = this._state.value.auswahl.vorname;
		s.nachname = this._state.value.auswahl.nachname;
		s.geschlecht = this._state.value.auswahl.geschlecht;
		s.bilingualeSprache = this._state.value.abiturdaten.bilingualeSprache;
		s.sprachendaten = this._state.value.abiturdaten.sprachendaten;
		for (const hj of GostHalbjahr.values())
			s.bewertetesHalbjahr[hj.id] = this._state.value.abiturdaten.bewertetesHalbjahr[hj.id];
		for (let i = 0; i < this._state.value.abiturdaten.fachbelegungen.size() ; i++) {
			const belegung = this._state.value.abiturdaten.fachbelegungen.get(i);
			const fb = new GostLaufbahnplanungDatenFachbelegung();
			fb.fachID = belegung.fachID;
			fb.abiturFach = belegung.abiturFach;
			for (const hj of GostHalbjahr.values()) {
				const hjBelegung = belegung.belegungen[hj.id];
				if (hjBelegung === null)
					continue;
				fb.kursart[hj.id] = hjBelegung.kursartKuerzel;
				fb.schriftlich[hj.id] = hjBelegung.schriftlich;
			}
			s.fachbelegungen.add(fb);
		}
		daten.schueler.add(s);
		return daten;
	}

	get hatAuswahl(): boolean {
		return (this._state.value.auswahl !== undefined);
	}

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		return this._state.value.auswahl;
	}

	get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this._state.value.gostJahrgangsdaten;
	}

	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
	}

	get gostBelegpruefungsArt(): 'ef1' | 'gesamt' | 'auto' {
		return this._state.value.gostBelegpruefungsArt;
	}

	get faechermanager(): GostFaecherManager {
		if (this._state.value.faecherManager === undefined)
			throw new Error("Unerwarteter Fehler: Fächer-Manager nicht initialisiert");
		return this._state.value.faecherManager;
	}

	set faecherManager(faecherManager: GostFaecherManager | undefined) {
		this.setPatchedState({ faecherManager })
	}

	get abiturdatenManager(): AbiturdatenManager {
		if (this._state.value.abiturdatenManager === undefined)
			throw new Error("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		return this._state.value.abiturdatenManager;
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: 'ef1' | 'gesamt' | 'auto') => {
		this.setPatchedState({ gostBelegpruefungsArt });
		await this.setGostBelegpruefungErgebnis();
	}

	setGostBelegpruefungErgebnis = async () => {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	}

	protected fachbelegungErstellen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const faecherManager = this.abiturdatenManager.faecher();
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined)
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		const belegung = new AbiturFachbelegung();
		const fach = faecherManager.get(fachID);
		if (fach === null)
			throw new DeveloperNotificationException("Das Fach mit der ID " + fachID + " steht unerwartet nicht zur Verfügung.");
		belegung.fachID = fachID;
		belegung.abiturFach = wahl.abiturFach;
		belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
		for (const hj of GostHalbjahr.values()) {
			const w = wahl.halbjahre[hj.id];
			if (w === null)
				continue;
			const hjBelegung = new AbiturFachbelegungHalbjahr();
			hjBelegung.halbjahrKuerzel = hj.kuerzel;
			if (w === "M") {
				if (fach.kuerzel === "PX") {
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				} else if (fach.kuerzel === "VX")
					hjBelegung.kursartKuerzel = GostKursart.VTF.kuerzel;
				else
					hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				hjBelegung.schriftlich = false;
			} else if (w === "ZK") {
				hjBelegung.kursartKuerzel = GostKursart.ZK.kuerzel;
				hjBelegung.schriftlich = false;
			} else if (w === "S") {
				hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				hjBelegung.schriftlich = true;
			} else if (w === "LK") {
				hjBelegung.kursartKuerzel = GostKursart.LK.kuerzel;
				hjBelegung.schriftlich = true;
			}
			hjBelegung.biliSprache = fach.biliSprache;
			belegung.belegungen[hj.id] = hjBelegung;
			belegung.letzteKursart = hjBelegung.kursartKuerzel;
		}
		abidaten.fachbelegungen.add(belegung);
	}

	protected fachbelegungEntfernen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined)
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		for (let i = 0; i < abidaten.fachbelegungen.size(); i++)
			if (abidaten.fachbelegungen.get(i).fachID === fachID)
				abidaten.fachbelegungen.removeElementAt(i);
	}

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		const abidaten = this._state.value.abiturdaten;
		if (abidaten === undefined)
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		const leereWahl = (wahl.halbjahre[0] === null) && (wahl.halbjahre[1] === null) && (wahl.halbjahre[2] === null) &&
			(wahl.halbjahre[3] === null) && (wahl.halbjahre[4] === null) && (wahl.halbjahre[5] === null);
		const belegung = this.abiturdatenManager.getFachbelegungByID(fachID);
		if (belegung === null) {
			this.fachbelegungErstellen(fachID, wahl);
		} else if (leereWahl) {
			this.fachbelegungEntfernen(fachID, wahl);
		} else {
			this.fachbelegungEntfernen(fachID, wahl);
			this.fachbelegungErstellen(fachID, wahl);
		}
		await this.setGostBelegpruefungErgebnis();
		this.setPatchedState({ dirty: true });
	}

	exportLaufbahnplanung = async (): Promise<ApiFile> => {
		const json = GostLaufbahnplanungDaten.transpilerToJSON(await this.schreibeDaten());
		const rawData = new Response(json).body;
		if (rawData === null)
			throw new UserNotificationException("Unerwarteter Fehler beim Erstellen der Export-Daten aufgetreten.");
		const compressedStream = rawData.pipeThrough(new CompressionStream('gzip'))
		const data = await new Response(compressedStream).blob();
		const name = `Laufbahnplanung_${this.gostJahrgangsdaten.abiturjahr}_${this.gostJahrgangsdaten.jahrgang}_${this.auswahl.nachname}_${this.auswahl.vorname}_${this.auswahl.id}.lp`;
		this.setPatchedState({ dirty: false });
		return { data, name };
	}

	importLaufbahnplanung = async (formData: FormData): Promise<void> => {
		this.reader.readAll();
		const gzData = formData.get("data");
		if (!(gzData instanceof File))
			throw new UserNotificationException("Es wurde keine gültige Datei angegeben");
		const ds = new DecompressionStream("gzip");
		const rawData = await (new Response(gzData.stream().pipeThrough(ds))).text();
		const laufbahnplanungsdaten = GostLaufbahnplanungDaten.transpilerFromJSON(rawData);
		const revRequired = 1;
		if (laufbahnplanungsdaten.lpRevision !== revRequired)
			throw new UserNotificationException("Die Revision der Laufbahnplanungsdatei (" + laufbahnplanungsdaten.lpRevision + ") entspricht nicht der unterstützen Revision " + revRequired);
		await this.ladeDaten(laufbahnplanungsdaten);
		await RouteManager.doRoute(routeLaufbahnplanung.name);
	}

	get zwischenspeicher(): Abiturdaten | undefined {
		return this._state.value.zwischenspeicher;
	}

	saveLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.abiturdaten === undefined)
			return;
		const zwischenspeicher = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(this._state.value.abiturdaten));
		this.setPatchedState({ zwischenspeicher });
	}

	restoreLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.zwischenspeicher === undefined)
			return;
		const abiturdaten = this._state.value.zwischenspeicher;
		const abiturdatenManager = this.createAbiturdatenmanager(this._state.value.faecherManager, abiturdaten);
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis, dirty: true });
	}

	resetFachwahlen = async (forceDelete: boolean) => {
		const abiturdaten = this._state.value.abiturdaten;
		if (abiturdaten === undefined)
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		for (const fachbelegung of abiturdaten.fachbelegungen) {
			fachbelegung.abiturFach = null;
			for (let i = 0; i < this.gostJahrgangsdaten.istBlockungFestgelegt.length; i++)
				if (this.gostJahrgangsdaten.istBlockungFestgelegt[i] === true)
					continue;
				else
					fachbelegung.belegungen[i] = null;
		}
		const temp = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(abiturdaten));
		const abiturdatenManager = this.createAbiturdatenmanager(this._state.value.faecherManager, temp);
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
	}

	reset = () => {
		this.setPatchedDefaultState({});
	}

	exitLaufbahnplanung = async () => {
		await RouteManager.doRoute(routeApp.getRoute());
	}

}
