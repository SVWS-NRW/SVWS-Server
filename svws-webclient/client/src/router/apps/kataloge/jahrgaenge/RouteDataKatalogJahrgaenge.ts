import type { JahrgangsDaten, JahrgangsListeEintrag } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeKatalogJahrgaenge } from "./RouteKatalogJahrgaenge";
import { routeKatalogJahrgaengeDaten } from "./RouteKatalogJahrgaengeDaten";

interface RouteStateKatalogJahrgaenge {
	auswahl: JahrgangsListeEintrag | undefined;
	daten: JahrgangsDaten | undefined;
	mapKatalogeintraege: Map<number, JahrgangsListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogJahrgaenge {

	private static _defaultState: RouteStateKatalogJahrgaenge = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogJahrgaengeDaten,
	}
	private _state = shallowRef(RouteDataKatalogJahrgaenge._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogJahrgaenge>) {
		this._state.value = Object.assign({ ... RouteDataKatalogJahrgaenge._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogJahrgaenge>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogJahrgaenge.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): JahrgangsListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, JahrgangsListeEintrag> {
		return this._state.value.mapKatalogeintraege;
	}

	get daten(): JahrgangsDaten {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getJahrgaenge(api.schema);
		const mapKatalogeintraege = new Map<number, JahrgangsListeEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: JahrgangsListeEintrag) => {
		const daten = await api.server.getJahrgang(api.schema, auswahl.id)
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: JahrgangsListeEintrag) => {
		await RouteManager.doRoute(routeKatalogJahrgaenge.getRoute(eintrag.id));
	}

	patch = async (data : Partial<JahrgangsDaten>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchJahrgangDaten", data);
		//await api.server.patchJahrgangDaten(data, api.schema, this.item.id);
	}
}