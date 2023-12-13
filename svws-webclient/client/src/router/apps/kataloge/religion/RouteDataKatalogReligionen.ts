import type { ReligionEintrag } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligionDaten } from "./RouteKatalogReligionDaten";
import { routeKatalogReligion } from "./RouteKatalogReligionen";

interface RouteStateKatalogeReligionen {
	auswahl: ReligionEintrag | undefined;
	mapKatalogeintraege: Map<number, ReligionEintrag>;
	view: RouteNode<any, any>;
}
export class RouteDataKatalogReligionen {

	private static _defaultState: RouteStateKatalogeReligionen = {
		auswahl: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogReligionDaten,
	}
	private _state = shallowRef(RouteDataKatalogReligionen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogeReligionen>) {
		this._state.value = Object.assign({ ... RouteDataKatalogReligionen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogeReligionen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogReligion.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Religionen gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): ReligionEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, ReligionEintrag> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getReligionen(api.schema);
		const mapKatalogeintraege = new Map<number, ReligionEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = (eintrag: ReligionEintrag) => {
		this.setPatchedState({
			auswahl: this.mapKatalogeintraege.get(eintrag.id)
		})
	}

	gotoEintrag = async (eintrag: ReligionEintrag) => {
		await RouteManager.doRoute(routeKatalogReligion.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: ReligionEintrag) => {
		const res = await api.server.createReligion(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(res.id, res);
		this.setPatchedState({ mapKatalogeintraege });
		await RouteManager.doRoute({ name: routeKatalogReligionDaten.name, params: { id: res.id } });
	}

	patch = async (data : Partial<ReligionEintrag>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchReligion(data, api.schema, this.auswahl.id)
	}
}