import { ArrayList, DeveloperNotificationException, type Einwilligungsart } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogEinwilligungsartenDaten } from "./RouteKatalogEinwilligungsartenDaten";
import { routeKatalogEinwilligungsarten } from "./RouteKatalogEinwilligungsarten";

interface RouteStateKatalogeEinwilligungsarten extends RouteStateInterface {
	auswahl: Einwilligungsart | undefined;
	mapKatalogeintraege: Map<number, Einwilligungsart>;
}

const defaultState = <RouteStateKatalogeEinwilligungsarten>{
	auswahl: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogEinwilligungsartenDaten,
};

export class RouteDataKatalogEinwilligungsarten extends RouteData<RouteStateKatalogeEinwilligungsarten> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): Einwilligungsart | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, Einwilligungsart> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getEinwilligungsarten(api.schema);
		const mapKatalogeintraege = new Map<number, Einwilligungsart>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = (eintrag: Einwilligungsart) => {
		this.setPatchedState({
			auswahl: this.mapKatalogeintraege.get(eintrag.id)
		})
	}

	gotoEintrag = async (eintrag: Einwilligungsart) => {
		await RouteManager.doRoute(routeKatalogEinwilligungsarten.getRoute({ id: eintrag.id }));
	}

	addEintrag = async (eintrag: Partial<Einwilligungsart>) => {
		delete eintrag.id;
		const res = await api.server.createEinwilligungsart(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(res.id, res);
		this.setPatchedState({ mapKatalogeintraege });
		await RouteManager.doRoute(routeKatalogEinwilligungsarten.getRoute({ id: res.id }));
	}

	patch = async (data: Partial<Einwilligungsart>) => {
		const auswahl = this.auswahl;
		if (auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const mapKatalogeintraege = this._state.value.mapKatalogeintraege;
		await api.server.patchEinwilligungsart(data, api.schema, auswahl.id)
		Object.assign(auswahl, data);
		this.setPatchedState({ auswahl, mapKatalogeintraege });
	}

	deleteEintraege = async (eintraege: Iterable<Einwilligungsart>) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const listID = new ArrayList<number>;
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const einwilligungen = await api.server.deleteEinwilligungsarten(listID, api.schema);
		for (const eintrag of einwilligungen)
			mapKatalogeintraege.delete(eintrag.id);
		let auswahl = this.auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({ mapKatalogeintraege, auswahl });
	}
}
