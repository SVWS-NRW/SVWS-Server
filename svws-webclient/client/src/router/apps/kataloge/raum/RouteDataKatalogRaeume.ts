import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { ArrayList, type Raum } from "@core";
import { routeKatalogRaeume } from "./RouteKatalogRaeume";
import { routeKatalogRaumDaten } from "./RouteKatalogRaumDaten";

interface RouteStateKatalogRaeume {
	auswahl: Raum | undefined;
	daten: Raum | undefined;
	mapKatalogeintraege: Map<number, Raum>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogRaeume {

	private static _defaultState: RouteStateKatalogRaeume = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogRaumDaten,
	}
	private _state = shallowRef(RouteDataKatalogRaeume._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogRaeume>) {
		this._state.value = Object.assign({ ... RouteDataKatalogRaeume._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogRaeume>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogRaeume.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): Raum | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, Raum> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	get daten(): Raum {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getRaeume(api.schema);
		const mapKatalogeintraege = new Map<number, Raum>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: Raum) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: Raum) => {
		await RouteManager.doRoute(routeKatalogRaeume.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<Raum>) => {
		delete eintrag.id;
		const raum = await api.server.addRaum(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: Raum[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const listID = new ArrayList<number>;
		for (const eintrag of eintraege) {
			listID.add(eintrag.id);
			mapKatalogeintraege.delete(eintrag.id);
		}
		let auswahl;
		const raeume = await api.server.deleteRaeume(listID, api.schema);
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<Raum>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchRaum(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}