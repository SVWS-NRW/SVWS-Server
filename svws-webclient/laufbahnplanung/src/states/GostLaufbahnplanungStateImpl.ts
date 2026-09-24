import { RouteManager } from "@lupo/router/RouteManager";

import type { ApiFile } from "@core/api/BaseApi";
import { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
import { GostBelegpruefungErgebnis } from "@core/core/abschluss/gost/GostBelegpruefungErgebnis";
import { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
import { AbiturFachbelegung } from "@core/core/data/gost/AbiturFachbelegung";
import { AbiturFachbelegungHalbjahr } from "@core/core/data/gost/AbiturFachbelegungHalbjahr";
import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import { GostLaufbahnplanungBeratungsdaten } from "@core/core/data/gost/GostLaufbahnplanungBeratungsdaten";
import type { GostSchuelerFachwahl } from "@core/core/data/gost/GostSchuelerFachwahl";
import { GostSchuelerGKLWahl } from "@core/core/data/gost/GostSchuelerGKLWahl";
import { GostLaufbahnplanungExportV1 } from "@core/core/data/gost/laufbahnplanung/v1/GostLaufbahnplanungExportV1";
import { GostLaufbahnplanungExportV2 } from "@core/core/data/gost/laufbahnplanung/v2/GostLaufbahnplanungExportV2";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import { GostKursart } from "@core/core/types/gost/GostKursart";
import type { GostFaecherManager } from "@core/core/utils/gost/GostFaecherManager";
import { GostLaufbahnplanungDataHandler } from "@core/core/utils/gost/GostLaufbahnplanungDataHandler";
import type { GostLaufbahnplanungGKLKlausurvorgabe } from "@core/core/utils/gost/GostLaufbahnplanungGKLKlausurvorgabe";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { GostBelegpruefungsModus, GostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
import { StateManager } from "@ui/ui/StateManager";
import { Config, ConfigElement } from "@ui/utils/Config";


interface GostLaufbahnplanungReactiveState {
	dataHandler: GostLaufbahnplanungDataHandler | null;
	dirty: boolean;
	config: Config;
	abiturdatenManager: AbiturdatenManager | undefined;
	gostBelegpruefungsArt: GostBelegpruefungsModus;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	zwischenspeicher: Abiturdaten | undefined;
};


/**
 * Der Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export class GostLaufbahnplanungStateImpl extends StateManager<GostLaufbahnplanungReactiveState> implements GostLaufbahnplanungState {

	public constructor() {
		super({
			dataHandler: null,
			dirty: false,
			config: new Config(async (key, value) => { }, async (key, value) => { }),
			abiturdatenManager: undefined,
			gostBelegpruefungsArt: 'gesamt',
			gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
			zwischenspeicher: undefined,
		});
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.modus", "user", "normal")]);
		this._state.value.config.addElements([new ConfigElement("app.schueler.laufbahnplanung.faecher.anzeigen", "user", "alle")]);
	}

	get valid(): boolean {
		return (this._state.value.dataHandler !== null) && (this._state.value.abiturdatenManager !== undefined);
	}

	public async clear() {
		this.setPatchedDefaultState({});
	}

	private get dataHandler(): GostLaufbahnplanungDataHandler {
		if (this._state.value.dataHandler === null) {
			throw new Error("Unerwarteter Fehler: Es ist keine Laufbahnplanungsdatei geladen.");
		}
		return this._state.value.dataHandler;
	}

	get modified(): boolean {
		return this._state.value.dirty;
	}

	get schueler(): SchuelerListeEintrag {
		return this.dataHandler.getSchueler();
	}

	get schuelerOrNull(): SchuelerListeEintrag | null {
		return this._state.value.dataHandler?.getSchueler() ?? null;
	}

	get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this.dataHandler.getGostJahrgangsdaten();
	}

	get beratungslehrer(): List<GostBeratungslehrer> {
		return new ArrayList(this.dataHandler.getGostJahrgangsdaten().beratungslehrer);
	}

	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
	}

	get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten {
		return new GostLaufbahnplanungBeratungsdaten();
	}

	get listeLehrer(): ArrayList<LehrerListeEintrag> {
		return new ArrayList();
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return new Map();
	}

	get faechermanager(): GostFaecherManager {
		return this.dataHandler.getFaecherManager();
	}

	get abiturdatenManager(): AbiturdatenManager {
		if (this._state.value.abiturdatenManager === undefined) {
			throw new Error("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		}
		return this._state.value.abiturdatenManager;
	}

	get id(): number | undefined {
		return undefined;
	}

	get zwischenspeicher(): Abiturdaten | undefined {
		return this._state.value.zwischenspeicher;
	}

	get hatZwischenspeicher(): boolean {
		return (this.zwischenspeicher !== undefined);
	}

	protected createAbiturdatenmanager(newDataHandler?: GostLaufbahnplanungDataHandler): AbiturdatenManager | undefined {
		const dataHandler = newDataHandler ?? this._state.value.dataHandler;
		if (dataHandler === null) {
			return undefined;
		}
		const abiturdaten = dataHandler.getAbiturdaten();
		const fachManager = dataHandler.getFaecherManager();
		const jahrgangsdaten = dataHandler.getGostJahrgangsdaten();
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1') {
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
		}
		if (art === 'gesamt') {
			return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		}
		const abiturdatenManager = new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen(), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			return abiturdatenManager;
		}
		return new AbiturdatenManager(abiturdaten, jahrgangsdaten, fachManager, GostBelegpruefungsArt.EF1);
	}

	setGostBelegpruefungErgebnis = async () => {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	};

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		if (this._state.value.dataHandler === null) {
			throw new DeveloperNotificationException("Die Laufbahnplanungsdaten stehen unerwartet nicht zur Verfügung.");
		}
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
	};


	public getKlausurvorgabe(id: number | null): GostLaufbahnplanungGKLKlausurvorgabe | null {
		if (id === null) {
			return null;
		}
		return this.dataHandler.getMapKlausurvorgaben().get(id);
	}


	public istGKLMoeglich(idFach: number, halbjahr: GostHalbjahr): List<GostLaufbahnplanungGKLKlausurvorgabe> {
		return this.dataHandler.getGklMoeglich().getOrException(idFach, halbjahr);
	}


	private pruefeGKLWahl(idVorgabe: number | null, idFach: number, halbjahr: GostHalbjahr): boolean {
		if (idVorgabe === null) {
			return false;
		}
		const vorgabe = this.getKlausurvorgabe(idVorgabe);
		if ((vorgabe !== null) && (vorgabe.getFach().id === idFach) && (vorgabe.getHalbjahr() === halbjahr)) {
			return true;
		}
		return false;
	}


	public istGKLGewaehlt(idFach: number, halbjahr: GostHalbjahr): boolean {
		const result = ((halbjahr.istEinfuehrungsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeEF_NW, idFach, halbjahr)))
			|| (halbjahr.istQualifikationsphase()
				&& (this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_Sprachen, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_GW, idFach, halbjahr)
					|| this.pruefeGKLWahl(this.gklWahlen.idKlausurvorgabeQ_NW, idFach, halbjahr))));
		return result;
	}


	public get gklWahlen(): GostSchuelerGKLWahl {
		return this._state.value.dataHandler?.getGklWahlen() ?? new GostSchuelerGKLWahl();
	}


	public async patchGKLWahlen(patch: Partial<GostSchuelerGKLWahl>) {
		const neu = Object.assign(new GostSchuelerGKLWahl(), this.dataHandler.getGklWahlen(), patch);
		this.dataHandler.replaceGKLWahlen(neu);
		this.commit();
	}


	exportLaufbahnplanung = async (): Promise<ApiFile> => {
		const json = GostLaufbahnplanungExportV2.transpilerToJSON(await this.schreibeV2Daten());
		const rawData = new Response(json).body;
		if (rawData === null) {
			throw new UserNotificationException("Unerwarteter Fehler beim Erstellen der Export-Daten aufgetreten.");
		}
		const compressedStream = rawData.pipeThrough(new CompressionStream('gzip'));
		const data = await new Response(compressedStream).blob();
		const name = `Laufbahnplanung_${this.gostJahrgangsdaten.abiturjahr}_${this.gostJahrgangsdaten.jahrgang}_${this.schueler.nachname}_${this.schueler.vorname}_${this.schueler.id}.lp`;
		this.setPatchedState({ dirty: false });
		return { data, name };
	};

	importLaufbahnplanung = async (formData: FormData): Promise<void> => {
		const gzData = formData.get("data");
		if (!(gzData instanceof File)) {
			throw new UserNotificationException("Es wurde keine gültige Datei angegeben");
		}
		const ds = new DecompressionStream("gzip");
		const rawData = await (new Response(gzData.stream().pipeThrough(ds))).text();
		const revision: number = JSON.parse(rawData).lpRevision;
		switch (revision) {
			case 1: {
				const laufbahnplanungsdaten = GostLaufbahnplanungExportV1.transpilerFromJSON(rawData);
				await this.ladeV1Daten(laufbahnplanungsdaten);
				break;
			}
			case 2: {
				const laufbahnplanungsdaten = GostLaufbahnplanungExportV2.transpilerFromJSON(rawData);
				await this.ladeV2Daten(laufbahnplanungsdaten);
				break;
			}
			default: {
				throw new UserNotificationException(`Die Revision der Laufbahnplanungsdatei (${revision}) entspricht nicht den unterstützen Revisionen 1 und 2`);
			}
		}
		await RouteManager.doRoute("laufbahnplanung");
	};

	patchBeratungsdaten = async (data: Partial<GostLaufbahnplanungBeratungsdaten>) => {
		throw new DeveloperNotificationException("Informationen zu Beratungsdaten sind in der Web-Laufbahnplanung nicht änderbar.");
	};

	saveLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.dataHandler === null) {
			return;
		}
		const zwischenspeicher = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(this._state.value.dataHandler.getAbiturdaten()));
		this.setPatchedState({ zwischenspeicher });
	};

	restoreLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.zwischenspeicher === undefined) {
			return;
		}
		const abiturdaten = this._state.value.zwischenspeicher;
		this.dataHandler.replaceAbiturdaten(abiturdaten);
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ zwischenspeicher: undefined, abiturdatenManager, gostBelegpruefungErgebnis, dirty: true });
	};

	get gostBelegpruefungsArt(): GostBelegpruefungsModus {
		return this._state.value.gostBelegpruefungsArt;
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsModus) => {
		this.setPatchedState({ gostBelegpruefungsArt });
		await this.setGostBelegpruefungErgebnis();
	};

	public async ladeV2Daten(daten: GostLaufbahnplanungExportV2) {
		const dataHandler = GostLaufbahnplanungDataHandler.importV2(daten);

		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(dataHandler);
		if (abiturdatenManager === undefined) {
			throw new UserNotificationException("Belegprüfungsergebnis konnte nicht berechnet werden.");
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedDefaultState({
			dataHandler,
			abiturdatenManager: abiturdatenManager,
			gostBelegpruefungErgebnis,
		});
	}

	public async ladeV1Daten(daten: GostLaufbahnplanungExportV1) {
		const dataHandler = GostLaufbahnplanungDataHandler.importV1(daten);

		// Erstelle den Abiturdaten-Manager
		const abiturdatenManager = this.createAbiturdatenmanager(dataHandler);
		if (abiturdatenManager === undefined) {
			throw new UserNotificationException("Belegprüfungsergebnis konnte nicht berechnet werden.");
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedDefaultState({
			dataHandler,
			abiturdatenManager: abiturdatenManager,
			gostBelegpruefungErgebnis,
		});
	}


	public async schreibeV2Daten(): Promise<GostLaufbahnplanungExportV2> {
		return this.dataHandler.exportV2((new Date()).toLocaleDateString("de-DE", { dateStyle: "short" }));
	}


	public async schreibeV1Daten(): Promise<GostLaufbahnplanungExportV1> {
		return this.dataHandler.exportV1((new Date()).toLocaleDateString("de-DE", { dateStyle: "short" }));
	}

	resetFachwahlen = async (forceDelete: boolean) => {
		const abiturdaten = this.dataHandler.getAbiturdaten();
		for (const fachbelegung of abiturdaten.fachbelegungen) {
			fachbelegung.abiturFach = null;
			for (let i = 0; i < this.gostJahrgangsdaten.istBlockungFestgelegt.length; i++) {
				if (this.gostJahrgangsdaten.istBlockungFestgelegt[i] === true) {
					continue;
				} else {
					fachbelegung.belegungen[i] = null;
				}
			}
		}
		const temp = Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(abiturdaten));
		this.dataHandler.replaceAbiturdaten(temp);
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	};

	gotoKursplanung = async (halbjahr: GostHalbjahr): Promise<void> => {
		throw new DeveloperNotificationException("Die Kursplanung steht in der Web-Laufbahnplanung nicht zur Verfügung.");
	};

	public async addBeratungslehrer(id: number) {
		throw new DeveloperNotificationException("Beratungslehrer können in der Web-Laufbahnplanung nicht hinzugefügt werden.");
	}

	public async removeBeratungslehrer(eintraege: GostBeratungslehrer[]) {
		throw new DeveloperNotificationException("Beratungslehrer können in der Web-Laufbahnplanung nicht entfernt werden.");
	}

	get hatAuswahl(): boolean {
		return (this._state.value.dataHandler !== null);
	}

	protected fachbelegungErstellen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const faecherManager = this.abiturdatenManager.faecher();
		const abidaten = this.dataHandler.getAbiturdaten();
		const belegung = new AbiturFachbelegung();
		const fach = faecherManager.get(fachID);
		if (fach === null) {
			throw new DeveloperNotificationException("Das Fach mit der ID " + fachID + " steht unerwartet nicht zur Verfügung.");
		}
		belegung.fachID = fachID;
		belegung.abiturFach = wahl.abiturFach;
		belegung.idReferenzfach = wahl.idReferenzfach;
		belegung.istFSNeu = fach.istFremdSpracheNeuEinsetzend;
		for (const hj of GostHalbjahr.values()) {
			const w = wahl.halbjahre[hj.id];
			if (w === null) {
				continue;
			}
			const hjBelegung = new AbiturFachbelegungHalbjahr();
			hjBelegung.halbjahrKuerzel = hj.kuerzel;
			if (w === "M") {
				if (fach.kuerzel === "PX") {
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				} else if (fach.kuerzel === "VX") {
					hjBelegung.kursartKuerzel = GostKursart.VTF.kuerzel;
				} else {
					hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				}
				hjBelegung.schriftlich = false;
			} else if (w === "ZK") {
				hjBelegung.kursartKuerzel = GostKursart.ZK.kuerzel;
				hjBelegung.schriftlich = false;
			} else if (w === "S") {
				hjBelegung.schriftlich = true;
				if (fach.kuerzel === "PX") {
					hjBelegung.kursartKuerzel = GostKursart.PJK.kuerzel;
					hjBelegung.wochenstunden = fach.wochenstundenQualifikationsphase;
				} else {
					hjBelegung.kursartKuerzel = GostKursart.GK.kuerzel;
				}
			} else if (w === "LK") {
				hjBelegung.kursartKuerzel = GostKursart.LK.kuerzel;
				hjBelegung.schriftlich = true;
			} else if (w === "AT") {
				hjBelegung.kursartKuerzel = w;
				hjBelegung.schriftlich = false;
				hjBelegung.notenkuerzel = w;
			}
			hjBelegung.biliSprache = fach.biliSprache;
			belegung.belegungen[hj.id] = hjBelegung;
			belegung.letzteKursart = hjBelegung.kursartKuerzel;
		}
		abidaten.fachbelegungen.add(belegung);
	}

	protected fachbelegungEntfernen(fachID: number, wahl: GostSchuelerFachwahl): void {
		const abidaten = this.dataHandler.getAbiturdaten();
		for (let i = 0; i < abidaten.fachbelegungen.size(); i++) {
			if (abidaten.fachbelegungen.get(i).fachID === fachID) {
				abidaten.fachbelegungen.removeElementAt(i);
			}
		}
	}

	public get config(): Config {
		return this._state.value.config;
	}

}


export const gostLaufbahnplanungStateImpl = new GostLaufbahnplanungStateImpl();
