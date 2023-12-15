import type { ReligionEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogReligionDaten } from "./RouteKatalogReligionDaten";
import { routeKatalogReligion } from "./RouteKatalogReligionen";

interface RouteStateKatalogeReligionen extends RouteStateInterface {
	auswahl: ReligionEintrag | undefined;
	mapKatalogeintraege: Map<number, ReligionEintrag>;
}

const defaultState = <RouteStateKatalogeReligionen> {
	auswahl: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogReligionDaten,
};

export class RouteDataKatalogReligionen extends RouteData<RouteStateKatalogeReligionen> {

	public constructor() {
		super(defaultState);
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