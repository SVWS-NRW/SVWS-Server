import type { Abiturdaten, ApiFile, GostBeratungslehrer, GostBlockungListeneintrag, GostBlockungsergebnis, GostLaufbahnplanungExportV1,
	GostSchuelerFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag } from "@core";
import { AbiturdatenManager, ArrayList, BenutzerTyp, DeveloperNotificationException, GostBelegpruefungErgebnis,
	GostBelegpruefungsArt, GostFaecherManager, GostHalbjahr, GostJahrgang, GostJahrgangsdaten,
	GostLaufbahnplanungBeratungsdaten } from "@core";
import type { GostLaufbahnplanungState, GostBelegpruefungsModus } from "@ui";
import { StateManager } from "@ui";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { serverStateImpl } from "./ServerStateImpl";

interface GostLaufbahnplanungReactiveState {
	mode: 'schueler' | 'abiturjahrgang' | undefined;
	auswahlSchueler: SchuelerListeEintrag | undefined;
	auswahlAbiturjahrgang: number | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	listeLehrer: List<LehrerListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	zwischenspeicher: GostLaufbahnplanungExportV1 | undefined;
};

/**
 * Der Zustand der Laufbahnplanung der Gymnasialen Oberstufe
 */
export class GostLaufbahnplanungStateImpl extends StateManager<GostLaufbahnplanungReactiveState> implements GostLaufbahnplanungState {

	// TODO evtl später noch auf userServerState umstellen?
	private readonly serverState = serverStateImpl;

	public constructor() {
		super({
			mode: undefined,
			auswahlSchueler: undefined,
			auswahlAbiturjahrgang: undefined,
			abiturdaten: undefined,
			abiturdatenManager: undefined,
			faecherManager: new GostFaecherManager(-1),
			gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
			gostJahrgang: new GostJahrgang(),
			gostJahrgangsdaten: new GostJahrgangsdaten(),
			gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
			listeLehrer: new ArrayList<LehrerListeEintrag>(),
			mapLehrer: new Map<number, LehrerListeEintrag>(),
			zwischenspeicher: undefined,
		});
	}


	public async clear() {
		this.setPatchedDefaultState({});
	}

	get valid(): boolean {
		return (this.mode !== undefined) && (this._state.value.abiturdatenManager !== undefined)
			&& (((this.mode === 'schueler') && (this._state.value.auswahlSchueler !== undefined))
				|| ((this.mode === 'abiturjahrgang') && (this._state.value.auswahlAbiturjahrgang !== undefined)));
	}

	get mode(): 'schueler' | 'abiturjahrgang' | undefined {
		return this._state.value.mode;
	}

	get schueler(): SchuelerListeEintrag {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Die Laufbahnplanung wurde für die Vorlagen bezüglich der Abiturjahrgänge initialisiert. Es stehen keine Schüler-spezifischen Informationen zur Verfügung.");
		}
		if (this._state.value.auswahlSchueler === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zur Laufbahnplanung abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahlSchueler;
	}

	get schuelerOrNull(): SchuelerListeEintrag | null {
		return this._state.value.auswahlSchueler ?? null;
	}

	private get auswahlAbiturjahrgang(): number {
		if (this._state.value.auswahlAbiturjahrgang === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		}
		return this._state.value.auswahlAbiturjahrgang;
	}

	get id(): number | undefined {
		const { typ, typID } = api.benutzerdaten;
		return BenutzerTyp.getByID(typ) === BenutzerTyp.LEHRER ? typID : undefined;
	}

	get listeLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listeLehrer;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this._state.value.gostJahrgangsdaten;
	}

	get beratungslehrer(): List<GostBeratungslehrer> {
		return new ArrayList(this._state.value.gostJahrgangsdaten.beratungslehrer);
	}

	get gostLaufbahnBeratungsdaten(): GostLaufbahnplanungBeratungsdaten {
		return this._state.value.gostLaufbahnBeratungsdaten;
	}

	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
	}

	get abiturdatenManager(): AbiturdatenManager {
		if (this._state.value.abiturdatenManager === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		}
		return this._state.value.abiturdatenManager;
	}

	get gostBelegpruefungsArt(): GostBelegpruefungsModus {
		const s = api.config.getValue("app.gost.belegpruefungsart");
		if ((s === 'ef1') || (s === 'gesamt') || (s === 'auto')) {
			return s;
		}
		void api.config.setValue("app.gost.belegpruefungsart", 'auto');
		throw new DeveloperNotificationException("Es wurde eine fehlerhafte Belegpruefungsart als Standardauswahl hinterlegt");
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsModus) => {
		await api.config.setValue("app.gost.belegpruefungsart", gostBelegpruefungsArt);
		await this.setGostBelegpruefungErgebnis();
	};


	exportLaufbahnplanung = api.call(async (): Promise<ApiFile> => {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Der Export von Schüler-Laufbahnplanungen steht nur in der Schüleransicht zur Verfügung.");
		}
		return await api.server.exportGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
	});


	importLaufbahnplanung = api.call(async (data: FormData): Promise<void> => {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Der Import von Schüler-Laufbahnplanungen steht nur in der Schüleransicht zur Verfügung.");
		}
		await api.server.importGostSchuelerLaufbahnplanung(data, api.schema, this.schueler.id);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		const abiturdatenManager = this.createAbiturdatenmanager(abiturdaten);
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
	});


	setWahl = api.call(async (idFach: number, wahl: GostSchuelerFachwahl) => {
		let abiturdaten;
		if (this.mode === 'schueler') {
			await api.server.patchGostSchuelerFachwahl(wahl, api.schema, this.schueler.id, idFach);
			abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		} else if (this.mode === 'abiturjahrgang') {
			await api.server.patchGostAbiturjahrgangFachwahl(wahl, api.schema, this.auswahlAbiturjahrgang, idFach);
			abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahlAbiturjahrgang);
		}
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	});

	patchBeratungsdaten = api.call(async (data: Partial<GostLaufbahnplanungBeratungsdaten>) => {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Anpassungen an Beratungsdaten sind nur in der Schüleransicht möglich.");
		}
		await api.server.patchGostSchuelerLaufbahnplanungBeratungsdaten(data, api.schema, this.schueler.id);
		const gostLaufbahnBeratungsdaten = this.gostLaufbahnBeratungsdaten;
		this.setPatchedState({ gostLaufbahnBeratungsdaten: Object.assign(gostLaufbahnBeratungsdaten, data) });
	});


	get hatZwischenspeicher(): boolean {
		return (this.zwischenspeicher !== undefined);
	}


	saveLaufbahnplanung = api.call(async (): Promise<void> => {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Das Zwischenspeichern der aktuellen Planungsdaten ist nur in der Schüleransicht möglich.");
		}
		const zwischenspeicher = await api.server.exportGostSchuelerLaufbahnplanungsdaten(api.schema, this.schueler.id);
		this.setPatchedState({ zwischenspeicher });
	});

	restoreLaufbahnplanung = api.call(async (): Promise<void> => {
		if (this.mode !== 'schueler') {
			throw new DeveloperNotificationException("Das Zwischenspeichern der aktuellen Planungsdaten ist nur in der Schüleransicht möglich.");
		}
		if (this._state.value.zwischenspeicher === undefined) {
			return;
		}
		await api.server.importGostSchuelerLaufbahnplanungsdaten(this._state.value.zwischenspeicher, api.schema, this.schueler.id);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		const abiturdatenManager = this.createAbiturdatenmanager(abiturdaten);
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis });
	});


	resetFachwahlen = api.call(async (forceDelete: boolean) => {
		if (this.mode === 'schueler') {
			if (forceDelete) {
				await api.server.deleteGostSchuelerFachwahlen(api.schema, this.schueler.id);
			} else {
				await api.server.resetGostSchuelerFachwahlen(api.schema, this.schueler.id);
			}
			const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
			this._state.value.abiturdaten = abiturdaten;
			await this.setGostBelegpruefungErgebnis();
		} else if (this.mode === 'abiturjahrgang') {
			await api.server.resetGostAbiturjahrgangFachwahlen(api.schema, this.auswahlAbiturjahrgang);
			const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahlAbiturjahrgang);
			this._state.value.abiturdaten = abiturdaten;
			await this.setGostBelegpruefungErgebnis();
		}
	});

	private async gotoKursplanungMode(halbjahr: GostHalbjahr, idschueler: number | null): Promise<void> {
		// Bestimme die Liste der Blockungen
		const blockungsliste = await api.server.getGostAbiturjahrgangBlockungsliste(api.schema, this.gostJahrgangsdaten.abiturjahr, halbjahr.id);
		if (blockungsliste.isEmpty()) {
			return;
		}
		// Bestimme die aktive Blockung, falls gesetzt, sonst nehme das erste in der Liste
		let blockungseintrag: GostBlockungListeneintrag | undefined = undefined;
		for (const e of blockungsliste) {
			if (e.istAktiv) {
				blockungseintrag = e;
				break;
			}
		}
		blockungseintrag ??= blockungsliste.get(0);
		// Bestimme die Daten der Blockung mit der Ergebnisliste
		const blockungsdaten = await api.server.getGostBlockung(api.schema, blockungseintrag.id);
		if (blockungsdaten.ergebnisse.isEmpty()) {
			return;
		}
		// Bestimme das aktive Ergebnis, falls gesetzt, sonst nehme das erste in der Liste
		let ergebnis: GostBlockungsergebnis | undefined = undefined;
		for (const e of blockungsdaten.ergebnisse) {
			if (e.istAktiv) {
				ergebnis = e;
				break;
			}
		}
		ergebnis ??= blockungsdaten.ergebnisse.get(0);
		let route;
		if (idschueler === null) {
			route = RouteNode.getNodeByName("gost.kursplanung")!.getRoute({
				abiturjahr: this.gostJahrgangsdaten.abiturjahr,
				halbjahr: halbjahr.id,
				idblockung: blockungsdaten.id,
				idergebnis: ergebnis.id,
			});
		} else {
			route = RouteNode.getNodeByName("gost.kursplanung.schueler")!.getRoute({
				abiturjahr: this.gostJahrgangsdaten.abiturjahr,
				halbjahr: halbjahr.id,
				idblockung: blockungsdaten.id,
				idergebnis: ergebnis.id,
				idschueler,
			});
		}
		await RouteManager.doRoute(route);
	}

	gotoKursplanung = api.call(async (halbjahr: GostHalbjahr): Promise<void> => {
		if (this.mode === 'schueler') {
			await this.gotoKursplanungMode(halbjahr, this.schueler.id);
		} else if (this.mode === 'abiturjahrgang') {
			await this.gotoKursplanungMode(halbjahr, null);
		}
	});


	createAbiturdatenmanager = (daten?: Abiturdaten): AbiturdatenManager | undefined => {
		const abiturdaten = daten || this._state.value.abiturdaten;
		if (abiturdaten === undefined) {
			return;
		}
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1') {
			return new AbiturdatenManager(this.serverState.mode, abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
		}
		if (art === 'gesamt') {
			return new AbiturdatenManager(this.serverState.mode, abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		}
		const abiturdatenManager = new AbiturdatenManager(this.serverState.mode, abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen(), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)) {
			return abiturdatenManager;
		}
		return new AbiturdatenManager(this.serverState.mode, abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
	};

	setGostBelegpruefungErgebnis = async () => {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined) {
			return;
		}
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	};

	get zwischenspeicher(): GostLaufbahnplanungExportV1 | undefined {
		return this._state.value.zwischenspeicher;
	}

	get faechermanager(): GostFaecherManager {
		return this._state.value.faecherManager;
	}

	set faecherManager(faecherManager: GostFaecherManager | undefined) {
		this.setPatchedState({ faecherManager });
	}

	public async addBeratungslehrer(id: number) {
		if (this.mode !== 'abiturjahrgang') {
			throw new DeveloperNotificationException("Das Hinzufügen eines Beratungslehrers ist nur in der Ansicht der Laufbahnplanung für den Abiturjahrgang möglich.");
		}
		api.status.start();
		const lehrer = await api.server.addGostAbiturjahrgangBeratungslehrer(id, api.schema, this.gostJahrgangsdaten.abiturjahr);
		this._state.value.gostJahrgangsdaten.beratungslehrer.add(lehrer);
		this.setPatchedState({ gostJahrgangsdaten: this._state.value.gostJahrgangsdaten });
		api.status.stop();
	}

	public async removeBeratungslehrer(eintraege: GostBeratungslehrer[]) {
		if (this.mode !== 'abiturjahrgang') {
			throw new DeveloperNotificationException("Das Erntfernen eines Beratungslehrers ist nur in der Ansicht der Laufbahnplanung für den Abiturjahrgang möglich.");
		}
		api.status.start();
		for (const eintrag of eintraege) {
			await api.server.removeGostAbiturjahrgangBeratungslehrer(eintrag.id, api.schema, this.gostJahrgangsdaten.abiturjahr);
			for (let i = 0; i < this.gostJahrgangsdaten.beratungslehrer.size() ; i++) {
				const b = this.gostJahrgangsdaten.beratungslehrer.get(i);
				if (b.id === eintrag.id) {
					this.gostJahrgangsdaten.beratungslehrer.removeElementAt(i);
				}
			}
		}
		this.setPatchedState({ gostJahrgangsdaten: this._state.value.gostJahrgangsdaten });
		api.status.stop();
	}

	public async ladeSchuelerDaten(auswahlSchueler: SchuelerListeEintrag | null) {
		if ((this._state.value.mode === 'schueler') && (auswahlSchueler === this._state.value.auswahlSchueler)) {
			return;
		}
		if (auswahlSchueler === null) {
			this.setPatchedDefaultState({});
		} else {
			const gostJahrgang = new GostJahrgang();
			if (auswahlSchueler.abiturjahrgang !== null) {
				gostJahrgang.abiturjahr = auswahlSchueler.abiturjahrgang;
			}
			gostJahrgang.jahrgang = auswahlSchueler.jahrgang;
			try {
				const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, auswahlSchueler.id);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const gostLaufbahnBeratungsdaten = await api.server.getGostSchuelerLaufbahnplanungBeratungsdaten(api.schema, auswahlSchueler.id);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(abiturdaten.schuljahrAbitur, listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const listeLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listeLehrer) {
					mapLehrer.set(l.id, l);
				}
				const mode = 'schueler';
				this.setPatchedState({ mode, auswahlSchueler, abiturdaten, gostJahrgang, gostJahrgangsdaten, gostLaufbahnBeratungsdaten, faecherManager, listeLehrer, mapLehrer, zwischenspeicher: undefined });
				await this.setGostBelegpruefungErgebnis();
			} catch {
				throw new DeveloperNotificationException("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Schüler Laufbahndaten möglich?");
			}
		}
	}

	public async ladeAbijahrgangsDaten(auswahlAbiturjahrgang: number) {
		if ((this._state.value.mode === 'abiturjahrgang') && (auswahlAbiturjahrgang === this._state.value.auswahlAbiturjahrgang)) {
			return;
		} else {
			const gostJahrgang = new GostJahrgang();
			gostJahrgang.abiturjahr = auswahlAbiturjahrgang;
			gostJahrgang.jahrgang = GostHalbjahr.EF1.jahrgang; // Gehe bei der Vorlage von einer Planung ab EF.1 ohne vorhandene/vergangene Daten aus
			try {
				const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, auswahlAbiturjahrgang);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(gostJahrgang.abiturjahr - 1, listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const listeLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listeLehrer) {
					mapLehrer.set(l.id, l);
				}
				const mode = 'abiturjahrgang';
				this.setPatchedState({ mode, auswahlAbiturjahrgang, abiturdaten, gostJahrgang, gostJahrgangsdaten, faecherManager, listeLehrer, mapLehrer });
				await this.setGostBelegpruefungErgebnis();
			} catch {
				throw new DeveloperNotificationException("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Abiturjahrgang Laufbahndaten möglich?");
			}
		}
	}

}

export const gostLaufbahnplanungStateImpl = new GostLaufbahnplanungStateImpl();
