import type { StundenplanAufsichtsbereich } from "@core";
import { ArrayList } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeKatalogAufsichtsbereichDaten } from "./RouteKatalogAufsichtsbereichDaten";
import { routeKatalogAufsichtsbereiche } from "./RouteKatalogAufsichtsbereiche";

interface RouteStateKatalogAufsichtsbereiche {
	auswahl: StundenplanAufsichtsbereich | undefined;
	daten: StundenplanAufsichtsbereich | undefined;
	mapKatalogeintraege: Map<number, StundenplanAufsichtsbereich>;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogAufsichtsbereiche {

	private static _defaultState: RouteStateKatalogAufsichtsbereiche = {
		auswahl: undefined,
		daten: undefined,
		mapKatalogeintraege: new Map(),
		view: routeKatalogAufsichtsbereichDaten,
	}
	private _state = shallowRef(RouteDataKatalogAufsichtsbereiche._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogAufsichtsbereiche>) {
		this._state.value = Object.assign({ ... RouteDataKatalogAufsichtsbereiche._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogAufsichtsbereiche>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogAufsichtsbereiche.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): StundenplanAufsichtsbereich | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanAufsichtsbereich> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	get daten(): StundenplanAufsichtsbereich {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getAufsichtsbereiche(api.schema);
		const mapKatalogeintraege = new Map<number, StundenplanAufsichtsbereich>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = async (auswahl: StundenplanAufsichtsbereich) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: StundenplanAufsichtsbereich) => {
		await RouteManager.doRoute(routeKatalogAufsichtsbereiche.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<StundenplanAufsichtsbereich>) => {
		delete eintrag.id;
		const raum = await api.server.addAufsichtsbereich(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: StundenplanAufsichtsbereich[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const list = new ArrayList<number>();
		for (const eintrag of eintraege) {
			mapKatalogeintraege.delete(eintrag.id);
			list.add(eintrag.id);
		}
		let auswahl;
		await api.server.deleteAufsichtsbereiche(list, api.schema);
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<StundenplanAufsichtsbereich>) => {
		if (this.auswahl === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchAufsichtsbereich(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		this.setPatchedState({auswahl: Object.assign(auswahl, eintrag)});
	}
}