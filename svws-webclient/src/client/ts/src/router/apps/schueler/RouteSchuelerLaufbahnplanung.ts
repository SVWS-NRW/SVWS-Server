import type { Abiturdaten, GostFach, GostJahrgangFachkombination, GostLaufbahnplanungDaten, GostSchuelerFachwahl, LehrerListeEintrag, List, SchuelerListeEintrag } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteSchueler} from "~/router/apps/RouteSchueler";
import type { SchuelerLaufbahnplanungProps } from "@comp";
import type { WritableComputedRef} from "vue";
import { AbiturdatenManager, BenutzerKompetenz, BenutzerTyp, GostBelegpruefungErgebnis, GostBelegpruefungsArt,
	GostFaecherManager, GostJahrgang, GostJahrgangsdaten, GostLaufbahnplanungBeratungsdaten, Schulform, ArrayList
} from "@svws-nrw/svws-core";
import { computed, shallowRef } from "vue";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { RouteNode } from "~/router/RouteNode";
import { SSchuelerLaufbahnplanung } from "@comp";
import { ConfigElement } from "~/components/Config";

interface RouteStateSchuelerLaufbahnplanung {
	auswahl: SchuelerListeEintrag | undefined;
	abiturdaten: Abiturdaten | undefined;
	abiturdatenManager: AbiturdatenManager | undefined;
	faecherManager: GostFaecherManager | undefined;
	gostBelegpruefungErgebnis: GostBelegpruefungErgebnis;
	gostJahrgang: GostJahrgang;
	gostJahrgangsdaten: GostJahrgangsdaten;
	gostLaufbahnBeratungsdaten: GostLaufbahnplanungBeratungsdaten;
	listGostFaecher: List<GostFach>;
	mapFachkombinationen: Map<number, GostJahrgangFachkombination>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	zwischenspeicher: GostLaufbahnplanungDaten | undefined;
}
export class RouteDataSchuelerLaufbahnplanung {

	private static _defaultState : RouteStateSchuelerLaufbahnplanung = {
		auswahl: undefined,
		abiturdaten: undefined,
		abiturdatenManager: undefined,
		faecherManager: undefined,
		gostBelegpruefungErgebnis: new GostBelegpruefungErgebnis(),
		gostJahrgang: new GostJahrgang(),
		gostJahrgangsdaten: new GostJahrgangsdaten(),
		gostLaufbahnBeratungsdaten: new GostLaufbahnplanungBeratungsdaten(),
		listGostFaecher: new ArrayList(),
		mapFachkombinationen: new Map(),
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

	get mapFachkombinationen(): Map<number, GostJahrgangFachkombination> {
		return this._state.value.mapFachkombinationen;
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

	createAbiturdatenmanager = (): AbiturdatenManager | undefined => {
		if (this._state.value.abiturdaten === undefined)
			return;
		let abiturdatenManager;
		const art = routeSchuelerLaufbahnplanung.gostBelegpruefungsArt.value;
		if (art === 'ef1')
			abiturdatenManager = new AbiturdatenManager(this._state.value.abiturdaten, this._state.value.listGostFaecher, GostBelegpruefungsArt.EF1);
		else
			abiturdatenManager = new AbiturdatenManager(this._state.value.abiturdaten, this._state.value.listGostFaecher, GostBelegpruefungsArt.GESAMT);
		if (art === 'auto') {
			for (const fachwahl of abiturdatenManager.getSchuelerFachwahlen().values())
				if (fachwahl.halbjahre.some((w, i) => i > 0 && w !== null))
					return abiturdatenManager;
		}
		else return abiturdatenManager;
		return new AbiturdatenManager(this._state.value.abiturdaten, this._state.value.listGostFaecher, GostBelegpruefungsArt.EF1);
	}

	setGostBelegpruefungErgebnis = () => {
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined)
			return;
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
		const abiturdaten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.auswahl.id);
		const abiturdatenManager = this.createAbiturdatenmanager();
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
		const abiturdatenManager = this.createAbiturdatenmanager();
		if (abiturdatenManager === undefined)
			return;
		const gostBelegpruefungErgebnis = abiturdatenManager.getBelegpruefungErgebnis();
		this.setPatchedState({zwischenspeicher: undefined, abiturdaten, abiturdatenManager, gostBelegpruefungErgebnis});
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
				const listLehrer = await api.server.getLehrer(api.schema);
				const mapLehrer = new Map<number, LehrerListeEintrag>();
				for (const l of listLehrer)
					mapLehrer.set(l.id, l);
				this.setPatchedState({ auswahl, abiturdaten, gostJahrgang, gostJahrgangsdaten, gostLaufbahnBeratungsdaten,
					listGostFaecher, faecherManager, mapFachkombinationen, mapLehrer })
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
		api.config.addElements([new ConfigElement("app.gost.belegpruefungsart", "user", "gesamt")]);
	}

	gostBelegpruefungsArt: WritableComputedRef<'ef1'|'gesamt'|'auto'> = computed({
		get: () => {
			let belegpruefungsart = api.config.getValue("app.gost.belegpruefungsart") as 'ef1'|'gesamt'|'auto';
			if (belegpruefungsart === null) {
				void api.config.setValue("app.gost.belegpruefungsart", 'gesamt');
				belegpruefungsart = 'gesamt';
			}
			return belegpruefungsart;
		},
		set: (belegpruefungsart) =>
			void api.config.setValue("app.gost.belegpruefungsart", belegpruefungsart)
	})

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: 'ef1'|'gesamt'|'auto') => {
		this.gostBelegpruefungsArt.value = gostBelegpruefungsArt;
		this.data.setGostBelegpruefungErgebnis();
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
			setGostBelegpruefungsArt: this.setGostBelegpruefungsArt,
			getPdfWahlbogen: this.data.getPdfWahlbogen,
			exportLaufbahnplanung: this.data.exportLaufbahnplanung,
			importLaufbahnplanung: this.data.importLaufbahnplanung,
			schueler: this.data.auswahl,
			gostJahrgangsdaten: this.data.gostJahrgangsdaten,
			gostLaufbahnBeratungsdaten: () => this.data.gostLaufbahnBeratungsdaten,
			patchBeratungsdaten: this.data.patchBeratungsdaten,
			gostBelegpruefungsArt: this.gostBelegpruefungsArt.value,
			gostBelegpruefungErgebnis: this.data.gostBelegpruefungErgebnis,
			abiturdatenManager: this.data.abiturdatenManager,
			faechermanager: this.data.faechermanager,
			mapFachkombinationen: this.data.mapFachkombinationen,
			mapLehrer: this.data.mapLehrer,
			id: this.data.id,
			zwischenspeicher: this.data.zwischenspeicher,
			saveLaufbahnplanung: this.data.saveLaufbahnplanung,
			restoreLaufbahnplanung: this.data.restoreLaufbahnplanung,
		};
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

