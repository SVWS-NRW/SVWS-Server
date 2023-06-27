import type { JahrgangsListeEintrag, KlassenDaten, KlassenListeEintrag, LehrerListeEintrag, Schueler} from "@core";
import { BenutzerKompetenz, List, Schulform, ServerMode } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { KlassenAppProps } from "~/components/klassen/SKlassenAppProps";
import type { KlassenAuswahlProps } from "~/components/klassen/SKlassenAuswahlProps";
import { routeKlasseDaten } from "~/router/apps/klassen/RouteKlasseDaten";
import type { RouteApp } from "~/router/RouteApp";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeSchueler } from "./RouteSchueler";


interface RouteStateKlassen {
	auswahl: KlassenListeEintrag | undefined;
	daten: KlassenDaten | undefined;
	mapKatalogeintraege: Map<number, KlassenListeEintrag>;
	mapLehrer: Map<number, LehrerListeEintrag>;
	mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKlassen {

	private static _defaultState: RouteStateKlassen = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		mapLehrer: new Map(),
		mapJahrgaenge: new Map(),
		view: routeKlasseDaten,
	}
	private _state = shallowRef(RouteDataKlassen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKlassen>) {
		this._state.value = Object.assign({ ...RouteDataKlassen._defaultState }, patch);
	}


	private setPatchedState(patch: Partial<RouteStateKlassen>) {
		this._state.value = Object.assign({ ...this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ...this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKlassen.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): KlassenListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, KlassenListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}

	get mapJahrgaenge(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapJahrgaenge;
	}

	get daten(): KlassenDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getKlassenFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKatalogeintraege = new Map<number, KlassenListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		// Laden der Jahrgänge
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		// Laden des Lehrer-Katalogs
		const listLehrer = await api.server.getLehrer(api.schema);
		const mapLehrer = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, mapLehrer, mapJahrgaenge })
	}

	setEintrag = async (auswahl: KlassenListeEintrag) => {
		const daten = await api.server.getKlasse(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: KlassenListeEintrag) => {
		await RouteManager.doRoute(routeKlassen.getRoute(eintrag.id));
	}

	gotoSchueler = async (eintrag: Schueler) => {
		await RouteManager.doRoute(routeSchueler.getRoute(eintrag.id));
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKlassenDaten", data);
		//await api.server.patchKursDaten(data, api.schema, this.item.id);
	}
}

const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "klassen", "/klassen/:id(\\d+)?", SKlassenApp, new RouteDataKlassen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlasseDaten
		];
		super.defaultChild = routeKlasseDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: KlassenListeEintrag | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.mapKatalogeintraege.get(0);
			return this.getRoute(eintrag?.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined) {
				return;
			}
		}
		if (eintrag !== undefined)
			await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): KlassenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
		};
	}

	public getProps(to: RouteLocationNormalized): KlassenAppProps {
		return {
			auswahl: this.data.auswahl,
			mapLehrer: this.data.mapLehrer,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node);
	}

}

export const routeKlassen = new RouteKlassen();
