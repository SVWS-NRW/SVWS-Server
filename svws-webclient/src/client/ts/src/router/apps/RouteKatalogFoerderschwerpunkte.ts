import type { FoerderschwerpunktEintrag} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { FoerderschwerpunkteAppProps } from "~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAppProps";
import type { FoerderschwerpunkteAuswahlProps } from "~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahlProps";
import { routeKatalogFoerderschwerpunktDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunktDaten";
import type { RouteApp } from "~/router/RouteApp";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeKataloge } from "./RouteKataloge";


interface RouteStateKatalogFoerderschwerpunkte {
	auswahl: FoerderschwerpunktEintrag | undefined;
	daten: FoerderschwerpunktEintrag | undefined;
	mapKatalogeintraege: Map<number, FoerderschwerpunktEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogFoerderschwerpunkte {

	private static _defaultState: RouteStateKatalogFoerderschwerpunkte = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogFoerderschwerpunktDaten,
	}
	private _state = shallowRef(RouteDataKatalogFoerderschwerpunkte._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogFoerderschwerpunkte>) {
		this._state.value = Object.assign({ ... RouteDataKatalogFoerderschwerpunkte._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogFoerderschwerpunkte>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogFoerderschwerpunkte.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): FoerderschwerpunktEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, FoerderschwerpunktEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): FoerderschwerpunktEintrag {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getSchuelerFoerderschwerpunkte(api.schema);
		const mapKatalogeintraege = new Map<number, FoerderschwerpunktEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: FoerderschwerpunktEintrag) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: FoerderschwerpunktEintrag) => {
		await RouteManager.doRoute(routeKatalogFoerderschwerpunkte.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FoerderschwerpunktEintrag>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patch...Daten", data);
		//await api.server.patch...Daten(data, api.schema, this.item.id);
	}
}

const SFoerderschwerpunkteAuswahl = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue")
const SFoerderschwerpunkteApp = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue")

export class RouteKatalogFoerderschwerpunkte extends RouteNode<RouteDataKatalogFoerderschwerpunkte, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.foerderschwerpunkte", "/kataloge/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteApp, new RouteDataKatalogFoerderschwerpunkte());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFoerderschwerpunktDaten
		];
		super.defaultChild = routeKatalogFoerderschwerpunktDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: FoerderschwerpunktEintrag | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): FoerderschwerpunkteAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): FoerderschwerpunkteAppProps {
		return {
			auswahl: this.data.auswahl,
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

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
