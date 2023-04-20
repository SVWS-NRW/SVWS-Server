import type { Abiturdaten, GostFach, GostJahrgangFachkombination,
	GostSchuelerFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag} from "@svws-nrw/svws-core";
import { AbiturdatenManager, BenutzerKompetenz, GostBelegpruefungErgebnis, GostBelegpruefungsArt,
	GostFaecherManager, GostJahrgang, GostJahrgangsdaten, GostLaufbahnplanungBeratungsdaten, Schulform, ArrayList
} from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import type { RouteSchueler} from "~/router/apps/RouteSchueler";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { RouteNode } from "~/router/RouteNode";
import type { SchuelerLaufbahnplanungProps } from "@comp";
import { SSchuelerLaufbahnplanung } from "@comp";

interface RouteStateSchuelerLaufbahnplanung {
	auswahl: SchuelerListeEintrag | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager | undefined;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	listGostFaecher: List<GostFach>;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	mapLehrer: Map<number, LehrerListeEintrag>;
}
export class RouteDataSchuelerLaufbahnplanung {

	private static _defaultState : RouteStateSchuelerLaufbahnplanung = {
		auswahl: undefined,
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: undefined,
		gostBelegpruefungsArt: GostBelegpruefungsArt.GESAMT,
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
		listGostFaecher: new ArrayList(),
		mapFachkombinationen: new Map(),
		mapLehrer: new Map(),
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

	get mapFachkombinationen(): Map<number, GostJahrgangFachkombination> {
		return this._state.value.mapFachkombinationen;
	}

	get gostBelegpruefungsArt(): GostBelegpruefungsArt {
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
	set abiturdatenManager(abiturdatenManager: AbiturdatenManager | undefined) {
		this.setPatchedState({ abiturdatenManager });
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsArt) => {
		this.setPatchedState({ gostBelegpruefungsArt });
		this.setGostBelegpruefungErgebnis();
	}

	setGostBelegpruefungErgebnis = () => {
		if (this._state.value.abiturdaten === undefined)
			return;
		const abiturdatenManager = new AbiturdatenManager(this._state.value.abiturdaten, this._state.value.listGostFaecher, this._state.value.gostBelegpruefungsArt);
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({ abiturdatenManager, gostBelegpruefungErgebnis });
	}

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		await api.server.patchGostSchuelerFachwahl(wahl, api.schema, this.auswahl.id, fachID);
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
		this._state.value.abiturdaten = abiturdaten;
		this.setGostBelegpruefungErgebnis();
	}

	getPdfWahlbogen = async() => {
		return await api.server.getGostSchuelerPDFWahlbogen(api.schema, this.auswahl.id);
	}

	exportLaufbahnplanung = async (): Promise<Blob> => {
		return await api.server.exportGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
	}

	importLaufbahnplanung = async (data: FormData): Promise<boolean> => {
		const res = await api.server.importGostSchuelerLaufbahnplanung(data, api.schema, this.auswahl.id);
		return res.success;
	}

	patchBeratungsdaten = async (data : Partial<GostLaufbahnplanungBeratungsdaten>) => {
		await api.server.patchGostSchuelerLaufbahnplanungBeratungsdaten(data, api.schema, this.auswahl.id);
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
				const listfachkombinationen	= await api.server.getGostAbiturjahrgangFachkombinationen(api.schema, gostJahrgang.abiturjahr);
				const mapFachkombinationen = new Map<number, GostJahrgangFachkombination>();
				for (const fk of listfachkombinationen)
					mapFachkombinationen.set(fk.id, fk);
				this.setPatchedState({ auswahl, abiturdaten, gostJahrgang, gostJahrgangsdaten, gostLaufbahnBeratungsdaten,
					listGostFaecher, faecherManager, mapFachkombinationen })
				this.setGostBelegpruefungErgebnis();
			} catch(error) {
				throw new Error("Die Laufbahndaten konnten nicht eingeholt werden, sind für diesen Schüler Laufbahndaten möglich?")
			}
		}
	}
}

export class RouteSchuelerLaufbahnplanung extends RouteNode<RouteDataSchuelerLaufbahnplanung, RouteSchueler> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "schueler.laufbahnplanung", "laufbahnplanung", SSchuelerLaufbahnplanung, new RouteDataSchuelerLaufbahnplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		super.isHidden = (params?: RouteParams) => {
			if (routeSchueler.data.auswahl === undefined)
				return false;
			const abiturjahr = routeSchueler.data.auswahl?.abiturjahrgang;
			return !(abiturjahr && routeSchueler.data.mapAbiturjahrgaenge.get(abiturjahr));
		}
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.data.ladeDaten();
		} else {
			const id = parseInt(to_params.id);
			try {
				await this.data.ladeDaten(this.parent.data.mapSchueler.get(id));
			} catch(error) {
				return routeSchueler.getRoute(id);
			}
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLaufbahnplanungProps {
		return {
			setWahl: this.data.setWahl,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			getPdfWahlbogen: this.data.getPdfWahlbogen,
			exportLaufbahnplanung: this.data.exportLaufbahnplanung,
			importLaufbahnplanung: this.data.importLaufbahnplanung,
			schueler: this.data.auswahl,
			gostJahrgangsdaten: this.data.gostJahrgangsdaten,
			gostLaufbahnBeratungsdaten: () => this.data.gostLaufbahnBeratungsdaten,
			patchBeratungsdaten: this.data.patchBeratungsdaten,
			gostBelegpruefungsArt: this.data.gostBelegpruefungsArt,
			gostBelegpruefungErgebnis: this.data.gostBelegpruefungErgebnis,
			abiturdatenManager: this.data.abiturdatenManager,
			faechermanager: this.data.faechermanager,
			mapFachkombinationen: this.data.mapFachkombinationen,
			mapLehrer: this.data.mapLehrer,
		};
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

