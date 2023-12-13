import { shallowRef } from "vue";
import type { List, StundenplanPausenzeit, StundenplanZeitraster, Wochentag } from "@core";
import { ArrayList, Stundenplan, StundenplanManager } from "@core";
import { api } from "~/router/Api";
import type { RouteNode } from "~/router/RouteNode";
import { routeKatalogZeitrasterDaten } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitrasterDaten";
import { routeKatalogZeitraster } from "./RouteKatalogZeitraster";

interface RouteStateKatalogZeitraster {
	listKatalogeintraege: List<StundenplanZeitraster>;
	stundenplanManager: StundenplanManager | undefined;
	selected: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogZeitraster {

	private static _defaultState: RouteStateKatalogZeitraster = {
		listKatalogeintraege: new ArrayList(),
		stundenplanManager: undefined,
		selected: undefined,
		view: routeKatalogZeitrasterDaten,
	}
	private _state = shallowRef(RouteDataKatalogZeitraster._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... RouteDataKatalogZeitraster._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogZeitraster.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	get selected(): Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined {
		return this._state.value.selected;
	}

	public setSelection = (value: Wochentag | number | StundenplanZeitraster | StundenplanPausenzeit | undefined) => {
		this.setPatchedState({selected: value});
	}

	public async ladeListe() {
		api.status.start();
		const listZeitraster = await api.server.getZeitraster(api.schema);
		const stundenplan = new Stundenplan();
		stundenplan.zeitraster = listZeitraster;
		stundenplan.gueltigAb = "1979-07-10";
		stundenplan.gueltigBis = "2050-09-29";
		const stundenplanManager = new StundenplanManager(stundenplan, new ArrayList(), new ArrayList(), null);
		this.setPatchedDefaultState({ stundenplanManager });
		api.status.stop();
	}

	addZeitraster = async (zeitraster: Iterable<Partial<StundenplanZeitraster>>) => {
		api.status.start();
		const list = new ArrayList<Partial<StundenplanZeitraster>>();
		for (const z of zeitraster) {
			delete z.id;
			list.add(z);
		}
		const items = await api.server.addZeitrasterEintraege(list, api.schema);
		this.stundenplanManager.zeitrasterAddAll(items);
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (zeitraster: Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list = new ArrayList<number>();
		for (const z of zeitraster)
			list.add(z.id);
		const items = await api.server.deleteZeitrasterEintraege(list, api.schema);
		this.stundenplanManager.zeitrasterRemoveAll(items);
		this._state.value.selected = undefined;
		this.commit();
		api.status.stop();
	}

	patchZeitraster = async (zeitraster : Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list: List<StundenplanZeitraster> = new ArrayList();
		for (const z of zeitraster)
			list.add(z);
		await api.server.patchZeitrasterEintraege(list, api.schema);
		this.stundenplanManager.zeitrasterPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}
}

