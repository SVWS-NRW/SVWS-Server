import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { ZeitrasterAuswahlProps } from "~/components/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { ZeitrasterAppProps } from "~/components/kataloge/zeitraster/SZeitrasterAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { List, StundenplanZeitraster} from "@core";
import { StundenplanManager, ArrayList, BenutzerKompetenz, Schulform, ServerMode, Stundenplan } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogZeitrasterDaten } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitrasterDaten";

interface RouteStateKatalogZeitraster {
	listKatalogeintraege: List<StundenplanZeitraster>;
	stundenplanManager: StundenplanManager | undefined;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogZeitraster {

	private static _defaultState: RouteStateKatalogZeitraster = {
		listKatalogeintraege: new ArrayList(),
		stundenplanManager: undefined,
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
		const list = new ArrayList<StundenplanZeitraster>();
		for (const z of zeitraster) {
			delete z.id;
			const item = await api.server.addZeitrasterEintrag(z, api.schema);
			list.add(item);
		}
		this.stundenplanManager.zeitrasterAddAll(list);
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (zeitraster: Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list = new ArrayList<StundenplanZeitraster>();
		for (const z of zeitraster) {
			const item = await api.server.deleteZeitrasterEintrag(api.schema, z.id);
			list.add(item);
		}
		this.stundenplanManager.zeitrasterRemoveAll(list);
		this.commit();
		api.status.stop();
	}

	patchZeitraster = async (zeitraster : Iterable<StundenplanZeitraster>) => {
		api.status.start();
		const list = new ArrayList<StundenplanZeitraster>();
		for (const z of zeitraster) {
			await api.server.patchZeitrasterEintrag(z, api.schema, z.id);
			this.stundenplanManager.zeitrasterPatchAttributes(z);
			list.add(z);
		}
		// this.stundenplanManager.zeitrasterPatchAttributesAll(list);
		this.commit();
		api.status.stop();
	}
}

const SZeitrasterAuswahl = () => import("~/components/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster", "/kataloge/zeitraster", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterAppProps {
		return {
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name });
		await this.data.setView(node);
	}
}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
