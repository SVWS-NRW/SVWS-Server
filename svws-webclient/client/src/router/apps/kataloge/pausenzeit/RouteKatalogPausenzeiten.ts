import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanPausenzeit } from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogPausenzeitDaten } from "~/router/apps/kataloge/pausenzeit/RouteKatalogPausenzeitDaten";

import type { PausenzeitenAuswahlProps } from "~/components/kataloge/pausenzeiten/SPausenzeitenAuswahlProps";
import type { PausenzeitenAppProps } from "~/components/kataloge/pausenzeiten/SPausenzeitenAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

interface RouteStateKatalogPausenzeiten {
	auswahl: StundenplanPausenzeit | undefined;
	daten: StundenplanPausenzeit | undefined;
	mapKatalogeintraege: Map<number, StundenplanPausenzeit>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogPausenzeiten {

	private static _defaultState: RouteStateKatalogPausenzeiten = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogPausenzeitDaten,
	}
	private _state = shallowRef(RouteDataKatalogPausenzeiten._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogPausenzeiten>) {
		this._state.value = Object.assign({ ... RouteDataKatalogPausenzeiten._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogPausenzeiten>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogPausenzeiten.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Pausenzeiten gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): StundenplanPausenzeit | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanPausenzeit> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): StundenplanPausenzeit {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Pausenzeitdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getPausenzeiten(api.schema);
		const mapKatalogeintraege = new Map<number, StundenplanPausenzeit>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: StundenplanPausenzeit) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: StundenplanPausenzeit) => {
		await RouteManager.doRoute(routeKatalogPausenzeiten.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<StundenplanPausenzeit>) => {
		delete eintrag.id;
		const pausenzeit = await api.server.addPausenzeit(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(pausenzeit.id, pausenzeit);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(pausenzeit);
	}

	deleteEintraege = async (eintraege: StundenplanPausenzeit[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		for (const eintrag of eintraege) {
			const pausenzeit = await api.server.deleteStundenplanPausenzeit(api.schema, eintrag.id);
			mapKatalogeintraege.delete(pausenzeit.id);
		}
		let auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<StundenplanPausenzeit>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchStundenplanPausenzeit(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}

const SPausenzeitenAuswahl = () => import("~/components/kataloge/pausenzeiten/SPausenzeitenAuswahl.vue")
const SPausenzeitenApp = () => import("~/components/kataloge/pausenzeiten/SPausenzeitenApp.vue")

export class RouteKatalogPausenzeiten extends RouteNode<RouteDataKatalogPausenzeiten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.pausenzeiten", "/kataloge/pausenzeiten/:id(\\d+)?", SPausenzeitenApp, new RouteDataKatalogPausenzeiten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausenzeiten";
		super.setView("liste", SPausenzeitenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogPausenzeitDaten
		];
		super.defaultChild = routeKatalogPausenzeitDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.mapKatalogeintraege.size < 1)
			return;
		let eintrag: StundenplanPausenzeit | undefined;
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

	public getAuswahlProps(to: RouteLocationNormalized): PausenzeitenAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () => this.data.mapKatalogeintraege,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): PausenzeitenAppProps {
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

export const routeKatalogPausenzeiten = new RouteKatalogPausenzeiten();
