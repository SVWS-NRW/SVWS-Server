import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { ArrayList, type StundenplanPausenzeit } from "@core";
import { routeKatalogPausenzeitDaten } from "./RouteKatalogPausenzeitDaten";
import { routeKatalogPausenzeiten } from "./RouteKatalogPausenzeiten";

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
		return new Map(this._state.value.mapKatalogeintraege);
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
		const list = new ArrayList<number>();
		for (const eintrag of eintraege) {
			list.add(eintrag.id)
			mapKatalogeintraege.delete(eintrag.id);
		}
		await api.server.deletePausenzeiten(list, api.schema);
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