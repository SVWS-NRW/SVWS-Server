import { shallowRef } from "vue";
import type { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { SchulEintrag } from "@core";
import { ArrayList } from "@core";
import { api } from "~/router/Api";
import { routeKatalogSchuleDaten } from "./RouteKatalogSchuleDaten";
import { routeKatalogSchulen } from "./RouteKatalogSchulen";

interface RouteStateKatalogSchulen {
	auswahl: SchulEintrag | undefined;
	mapKatalogeintraege: Map<number, SchulEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogSchulen {

	private static _defaultState: RouteStateKatalogSchulen = {
		auswahl: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogSchuleDaten,
	}
	private _state = shallowRef(RouteDataKatalogSchulen._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogSchulen>) {
		this._state.value = Object.assign({ ... RouteDataKatalogSchulen._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogSchulen>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogSchulen.children.includes(view))
			this.setPatchedState({ view });
		else
			throw new Error("Diese für die Fächer gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): SchulEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, SchulEintrag> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	public async ladeListe() {
		api.status.start();
		const listKatalogeintraege = await api.server.getSchulen(api.schema);
		const mapKatalogeintraege = new Map<number, SchulEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedState({ auswahl, mapKatalogeintraege })
		api.status.stop();
	}

	removeEintraege = async (liste: Iterable<SchulEintrag>) => {
		api.status.start();
		const list = new ArrayList<number>();
		let leave = false;
		for (const schule of liste) {
			list.add(schule.id);
			if (this.auswahl?.id === schule.id)
				leave = true;
		}
		const res = await api.server.deleteSchulenVonKatalog(list, api.schema);
		for (const s of res)
			this.mapKatalogeintraege.delete(s.id);
		this.setPatchedState({ mapKatalogeintraege: this.mapKatalogeintraege })
		if (leave === true) {
			this._state.value.auswahl = undefined;
			return RouteManager.doRoute(routeKatalogSchulen.getRoute(undefined));
		}
	}

	addEintrag = async (data: Partial<SchulEintrag>) => {
		api.status.start();
		const schule = await api.server.addSchuleZuKatalog(data, api.schema);
		this.mapKatalogeintraege.set(schule.id, schule);
		api.status.stop();
		await this.gotoEintrag(schule);
	}

	setEintrag = (auswahl: SchulEintrag) => {
		this.setPatchedState({ auswahl })
	}

	gotoEintrag = async (eintrag: SchulEintrag) => {
		await RouteManager.doRoute(routeKatalogSchulen.getRoute(eintrag.id));
	}

	patch = async (data : Partial<SchulEintrag>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		api.status.start();
		await api.server.patchSchuleAusKatalog(data, api.schema, this.auswahl.id);
		Object.assign(this.auswahl, data);
		this.setPatchedState({auswahl: this.auswahl});
		api.status.stop();
	}
}