import { shallowRef } from "vue";
import type { FachDaten, FaecherListeEintrag } from "@core";
import { api } from "~/router/Api";
import type { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeKatalogFachDaten } from "./RouteKatalogFachDaten";
import { routeKatalogFaecher } from "./RouteKatalogFaecher";

interface RouteStateKatalogFaecher {
	auswahl: FaecherListeEintrag | undefined;
	daten: FachDaten | undefined;
	mapKatalogeintraege: Map<number, FaecherListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogFaecher {

	private static _defaultState: RouteStateKatalogFaecher = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogFachDaten,
	}
	private _state = shallowRef(RouteDataKatalogFaecher._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogFaecher>) {
		this._state.value = Object.assign({ ... RouteDataKatalogFaecher._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogFaecher>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogFaecher.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Fächer gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): FaecherListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, FaecherListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): FachDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getFaecher(api.schema);
		const mapKatalogeintraege = new Map<number, FaecherListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: FaecherListeEintrag) => {
		const daten = await api.server.getFach(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: FaecherListeEintrag) => {
		await RouteManager.doRoute(routeKatalogFaecher.getRoute(eintrag.id));
	}

	patch = async (data : Partial<FachDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchFach(data, api.schema, this.daten.id);
		Object.assign(this.daten, data);
	}

}
