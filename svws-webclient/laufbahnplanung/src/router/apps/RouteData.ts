import { shallowRef } from "vue";

import type { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLadeDaten } from "~/router/apps/RouteLadeDaten";

import type { GostJahrgangFachkombination, GostSchuelerFachwahl} from "@core";
import { AbiturdatenManager, Abiturdaten, GostBelegpruefungErgebnis, GostBelegpruefungsArt, GostFaecherManager, GostJahrgang, GostJahrgangsdaten,
	GostLaufbahnplanungBeratungsdaten, GostLaufbahnplanungDaten, SchuelerListeEintrag, UserNotificationException, AbiturFachbelegung, GostHalbjahr,
	LehrerListeEintrag,
	AbiturFachbelegungHalbjahr,
	DeveloperNotificationException,
	GostKursart} from "@core";


interface RouteState {
	auswahl: SchuelerListeEintrag | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager | undefined;
	gostBelegpruefungsArt: 'ef1' | 'gesamt' | 'auto';
	modus: 'manuell' | 'normal' | 'hochschreiben';
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	zwischenspeicher: Abiturdaten | undefined;
	view: RouteNode<any, any>;
}

export class RouteData {

	private static _defaultState : RouteState = {
		auswahl: undefined,
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: undefined,
		gostBelegpruefungsArt: 'gesamt',
		modus: 'normal',
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
		mapFachkombinationen: new Map(),
		mapLehrer: new Map(),
		zwischenspeicher: undefined,
		view: routeLadeDaten
	}

	private _state = shallowRef<RouteState>(RouteData._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... RouteData._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteState>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
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

	protected createAbiturdatenmanager(faecherManager?: GostFaecherManager | undefined, daten?: Abiturdaten): AbiturdatenManager | undefined {
		const abiturdaten = (daten === undefined) ? this._state.value.abiturdaten : daten;
		const fachManager = (faecherManager === undefined) ? this._state.value.faecherManager : faecherManager;
		if ((abiturdaten === undefined) || (fachManager === undefined))
			return undefined;
		const jahrgangsdaten = this._state.value.gostJahrgangsdaten;
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1')
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
		if (art === 'gesamt')
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		const abiturdatenManager = new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen()), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
			return abiturdatenManager;
		return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
	}

	public async ladeDaten(daten: GostLaufbahnplanungDaten) {
		// Lade die Fachkombinationen
		const mapFachkombinationen = new Map();
		for (const fk of daten.fachkombinationen)
			mapFachkombinationen.set(fk.id, fk);
		// Lade die Jahrgangsinformationen
		const gostJahrgang = new GostJahrgang();
		gostJahrgang.abiturjahr = daten.abiturjahr;
		gostJahrgang.jahrgang = daten.jahrgang;
		// Initialisiere die Map für die Beratungslehrer
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const bl of daten.beratungslehrer) {
			const l = new LehrerListeEintrag();
			l.id = bl.id;
			l.kuerzel = bl.kuerzel === null ? "???" : bl.kuerzel;
			l.nachname = bl.nachname === null ? "???" : bl.nachname;
			l.vorname = bl.vorname === null ? "???" : bl.vorname;
			mapLehrer.set(l.id, l);
		}
		// Initialisiere den Fächer-Manager mit den Fächerdaten
		const faecherManager = new GostFaecherManager(daten.faecher);
		// Initialisiere die Beratungsdaten für den Schüler
		const gostLaufbahnBeratungsdaten = new GostLaufbahnplanungBeratungsdaten();
		gostLaufbahnBeratungsdaten.kommentar = daten.anmerkungen;
		// Bestimme die importierten Laufbahnplanungsdaten für den Schüler
		const planungsdaten = daten.schueler.get(0);
		// Erstelle das Schüler-Objekt für die Anzeige
		const schueler = new SchuelerListeEintrag();
		schueler.id = planungsdaten.id;
		schueler.vorname = planungsdaten.vorname;
		schueler.nachname = planungsdaten.nachname;
		schueler.abiturjahrgang = gostJahrgang.abiturjahr;
		// Erstelle das Abiturdaten-Objekt mit den Fachbelegungen
		const abiturdaten = new Abiturdaten();
		abiturdaten.abiturjahr = daten.abiturjahr;
		abiturdaten.sprachendaten = planungsdaten.sprachendaten;
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
				belegung.belegungen[hj.id] = hjBelegung;
				belegung.letzteKursart = kursart;
			}
			abiturdaten.fachbelegungen.add(belegung);
		}
		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(faecherManager, abiturdaten);
		this.setPatchedDefaultState({
			mapFachkombinationen,
			gostJahrgang,
			mapLehrer,
			faecherManager,
			auswahl: schueler,
			abiturdaten,
			abiturdatenManager: abiturdatenManager,
		})
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

	get mapFachkombinationen(): Map<number, GostJahrgangFachkombination> {
		return this._state.value.mapFachkombinationen;
	}

	get gostBelegpruefungsArt(): 'ef1' | 'gesamt' | 'auto' {
		return this._state.value.gostBelegpruefungsArt;
	}

	get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten {
		return this._state.value.gostLaufbahnBeratungsdaten;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
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
				if (fach.kuerzel === "PX")
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
				else if (fach.kuerzel === "VX")
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
	}

	exportLaufbahnplanung = async (): Promise<Blob> => {
		const b = new Blob();
		// TODO Erstellen der GostLaufbahnplanungDaten aus den Abiturdaten, serialisieren als JSON und als GZIP-Blob zurückgeben
		return b;
	}

	importLaufbahnplanung = async (formData: FormData): Promise<boolean> => {
		try {
			const gzData = formData.get("data");
			if (!(gzData instanceof File))
				return false;
			const ds = new DecompressionStream("gzip");
			const rawData = await (new Response(gzData.stream().pipeThrough(ds))).text();
			const laufbahnplanungsdaten = GostLaufbahnplanungDaten.transpilerFromJSON(rawData);
			await this.ladeDaten(laufbahnplanungsdaten);
			return true;
		} catch (e) {
			throw new UserNotificationException(e instanceof Error ? e.message : "Unbekannter Fehler aufgetreten.");
		}
	}

	patchBeratungsdaten = async (data : Partial<GostLaufbahnplanungBeratungsdaten>) => {
		// Hier ist nichts zu tun, da nicht mit der API des SVWS-Server kommuniziert wird
	}


	get zwischenspeicher(): Abiturdaten | undefined {
		return this._state.value.zwischenspeicher;
	}

	get modus(): 'manuell'|'normal'|'hochschreiben' {
		return this._state.value.modus;
	}

	setModus = async (modus: 'manuell'|'normal'|'hochschreiben') => {
		this._state.value.modus = modus;
		this.commit();
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
		this.setPatchedState({ zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
	}

	resetFachwahlen = async () => {
		// TODO Leere die Belegungen in den Abiturdaten
		const abiturdaten = this._state.value.abiturdaten;
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	}

}

