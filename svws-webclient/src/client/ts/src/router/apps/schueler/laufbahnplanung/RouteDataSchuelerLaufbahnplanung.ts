import { shallowRef } from "vue";

import type { Abiturdaten, GostJahrgangFachkombination, GostLaufbahnplanungDaten, GostSchuelerFachwahl, LehrerListeEintrag,
	SchuelerListeEintrag } from "@core";
import { AbiturdatenManager, BenutzerTyp, GostBelegpruefungErgebnis, GostBelegpruefungsArt, GostFaecherManager, GostJahrgang,
	GostJahrgangsdaten, GostLaufbahnplanungBeratungsdaten, GostHalbjahr } from "@core";

import { api } from "~/router/Api";


interface RouteStateSchuelerLaufbahnplanung {
	auswahl: SchuelerListeEintrag | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	mapLehrer: Map<number, LehrerListeEintrag>;
	zwischenspeicher: GostLaufbahnplanungDaten | undefined;
}

export class RouteDataSchuelerLaufbahnplanung {

	private static _defaultState : RouteStateSchuelerLaufbahnplanung = {
		auswahl: undefined,
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: new GostFaecherManager(),
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
		mapLehrer: new Map(),
		zwischenspeicher: undefined,
	}

	private _state = shallowRef<RouteStateSchuelerLaufbahnplanung>(RouteDataSchuelerLaufbahnplanung._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchuelerLaufbahnplanung>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerLaufbahnplanung._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchuelerLaufbahnplanung>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}
	public async ladeFachkombinationen() {
		if (this._state.value.gostJahrgang === undefined)
			return;
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
	set abiturdatenManager(abiturdatenManager: AbiturdatenManager | undefined) {
		this.setPatchedState({ abiturdatenManager });
	}

	get id(): number | undefined {
		const { typ, typID } = api.benutzerdaten;
		return BenutzerTyp.getByID(typ) === BenutzerTyp.LEHRER ? typID : undefined;
	}

	get zwischenspeicher(): GostLaufbahnplanungDaten | undefined {
		return this._state.value.zwischenspeicher;
	}

	createAbiturdatenmanager = async (daten?: Abiturdaten): Promise<AbiturdatenManager | undefined> => {
		const abiturdaten = daten || this._state.value.abiturdaten;
		if (abiturdaten === undefined)
			return;
		const art = this.gostBelegpruefungsArt;
		if (art === 'ef1')
			return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
		if (art === 'gesamt')
			return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		const abiturdatenManager = new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.GESAMT);
		if (abiturdatenManager.pruefeBelegungExistiert(abiturdatenManager.getFachbelegungen()), GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)
			return abiturdatenManager;
		return new AbiturdatenManager(abiturdaten, this._state.value.gostJahrgangsdaten, this._state.value.faecherManager, GostBelegpruefungsArt.EF1);
	}

	setGostBelegpruefungErgebnis = async () => {
		const abiturdatenManager = await this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	}

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		await api.server.patchGostSchuelerFachwahl(wahl, api.schema, this.auswahl.id, fachID);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	}

	getPdfWahlbogen = async() => {
		return await api.server.getGostSchuelerPDFWahlbogen(api.schema, this.auswahl.id);
	}

	exportLaufbahnplanung = async (): Promise<Blob> => {
		return await api.server.exportGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
	}

	importLaufbahnplanung = async (data: FormData): Promise<boolean> => {
		const res = await api.server.importGostSchuelerLaufbahnplanung(data, api.schema, this.auswahl.id);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
		const abiturdatenManager = await this.createAbiturdatenmanager(abiturdaten);
		if (abiturdatenManager === undefined)
			return false;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis});
		return res.success;
	}

	patchBeratungsdaten = async (data : Partial<GostLaufbahnplanungBeratungsdaten>) => {
		await api.server.patchGostSchuelerLaufbahnplanungBeratungsdaten(data, api.schema, this.auswahl.id);
		const gostLaufbahnBeratungsdaten = this.gostLaufbahnBeratungsdaten;
		this.setPatchedState({gostLaufbahnBeratungsdaten: Object.assign(gostLaufbahnBeratungsdaten, data)});
	}

	saveLaufbahnplanung = async (): Promise<void> => {
		const zwischenspeicher = await api.server.exportGostSchuelerLaufbahnplanungsdaten(api.schema, this.auswahl.id);
		this.setPatchedState({zwischenspeicher});
	}

	restoreLaufbahnplanung = async (): Promise<void> => {
		if (this._state.value.zwischenspeicher === undefined)
			return;
		await api.server.importGostSchuelerLaufbahnplanungsdaten(this._state.value.zwischenspeicher, api.schema, this.auswahl.id);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
		const abiturdatenManager = await this.createAbiturdatenmanager(abiturdaten);
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis});
	}

	get gostBelegpruefungsArt(): 'ef1'|'gesamt'|'auto' {
		return api.config.getValue("app.gost.belegpruefungsart") as 'ef1'|'gesamt'|'auto';
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: 'ef1'|'gesamt'|'auto') => {
		await api.config.setValue("app.gost.belegpruefungsart", gostBelegpruefungsArt);
		await this.setGostBelegpruefungErgebnis();
	}

	public async ladeDaten(auswahl?: SchuelerListeEintrag) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === undefined)
			this.setPatchedDefaultState({});
		else {
			const gostJahrgang = new GostJahrgang();
			if (auswahl.abiturjahrgang !== null)
				gostJahrgang.abiturjahr = auswahl.abiturjahrgang;
			gostJahrgang.jahrgang = auswahl.jahrgang;
			try {
				const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, auswahl.id);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const gostLaufbahnBeratungsdaten = await api.server.getGostSchuelerLaufbahnplanungBeratungsdaten(api.schema, auswahl.id);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const listLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listLehrer)
					mapLehrer.set(l.id, l);
				this.setPatchedState({ auswahl, abiturdaten, gostJahrgang, gostJahrgangsdaten, gostLaufbahnBeratungsdaten, faecherManager, mapLehrer })
				await this.setGostBelegpruefungErgebnis();
			} catch(error) {
				throw new Error("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Schüler Laufbahndaten möglich?")
			}
		}
	}
}

