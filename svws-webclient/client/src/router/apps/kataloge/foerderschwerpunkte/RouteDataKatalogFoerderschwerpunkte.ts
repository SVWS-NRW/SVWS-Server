import type { FoerderschwerpunktEintrag } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeKatalogFoerderschwerpunktDaten } from "./RouteKatalogFoerderschwerpunktDaten";
import { routeKatalogFoerderschwerpunkte } from "./RouteKatalogFoerderschwerpunkte";

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