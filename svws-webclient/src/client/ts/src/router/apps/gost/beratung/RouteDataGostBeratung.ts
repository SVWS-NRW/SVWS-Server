import { shallowRef } from "vue";

import type { Abiturdaten, GostSchuelerFachwahl, LehrerListeEintrag, Sprachbelegung} from "@core";
import { GostBelegpruefungErgebnis, GostFaecherManager, GostJahrgang, GostJahrgangsdaten,
	AbiturdatenManager, GostBelegpruefungsArt, BenutzerTyp, GostHalbjahr} from "@core";

import { api } from "~/router/Api";


interface RouteStateDataGostBeratung {
	auswahl: number | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	mapLehrer: Map<number, LehrerListeEintrag>;
}

export class RouteDataGostBeratung  {

	private static _defaultState: RouteStateDataGostBeratung = {
		auswahl: undefined,
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: new GostFaecherManager(),
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		mapLehrer: new Map(),
	}

	private _state = shallowRef(RouteDataGostBeratung._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostBeratung>) {
		this._state.value = Object.assign({ ... RouteDataGostBeratung._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostBeratung>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async ladeFachkombinationen() {
		if (this._state.value.gostJahrgang === undefined)
			return;
	}

	get auswahl(): number {
		if (this._state.value.auswahl === undefined)
			throw new Error("Unerwarteter Fehler: Abiturjahrgang nicht initialisiert");
		return this._state.value.auswahl;
	}

	get gostJahrgangsdaten(): GostJahrgangsdaten {
		return this._state.value.gostJahrgangsdaten;
	}

	get gostBelegpruefungErgebnis(): GostBelegpruefungErgebnis {
		return this._state.value.gostBelegpruefungErgebnis;
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
		await api.server.patchGostAbiturjahrgangFachwahl(wahl, api.schema, this.auswahl, fachID);
		const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahl);
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	}

	get gostBelegpruefungsArt(): 'ef1'|'gesamt'|'auto' {
		return api.config.getValue("app.gost.belegpruefungsart") as 'ef1'|'gesamt'|'auto';
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: 'ef1'|'gesamt'|'auto') => {
		await api.config.setValue("app.gost.belegpruefungsart", gostBelegpruefungsArt);
		await this.setGostBelegpruefungErgebnis();
	}

	public async ladeDaten(auswahl: number) {
		if (auswahl === this._state.value.auswahl)
			return;
		if (auswahl === undefined)
			this.setPatchedDefaultState({});
		else {
			const gostJahrgang = new GostJahrgang();
			gostJahrgang.abiturjahr = auswahl;
			gostJahrgang.jahrgang = GostHalbjahr.EF1.jahrgang; // Gehe bei der Vorlage von einer Planung ab EF.1 ohne vorhandene/vergangene Daten aus
			try {
				const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, auswahl);
				const gostJahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, gostJahrgang.abiturjahr);
				const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, gostJahrgang.abiturjahr);
				const faecherManager = new GostFaecherManager(listGostFaecher);
				const listFachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				faecherManager.addFachkombinationenAll(listFachkombinationen);
				const listLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listLehrer)
					mapLehrer.set(l.id, l);
				this.setPatchedState({ auswahl, abiturdaten, gostJahrgang, gostJahrgangsdaten, faecherManager, mapLehrer })
				await this.setGostBelegpruefungErgebnis();
			} catch(error) {
				throw new Error("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Abiturjahrgang Laufbahndaten möglich?");
			}
		}
	}

	resetFachwahlen = async () => {
		await api.server.resetGostAbiturjahrgangFachwahlen(api.schema, this.auswahl);
		const abiturdaten = await api.server.getGostAbiturjahrgangLaufbahnplanung(api.schema, this.auswahl);
		this._state.value.abiturdaten = abiturdaten;
		await this.setGostBelegpruefungErgebnis();
	}

}

