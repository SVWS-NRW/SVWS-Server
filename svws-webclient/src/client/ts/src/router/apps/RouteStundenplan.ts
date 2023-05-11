import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";
import type { RouteApp } from "~/router/RouteApp";
import { shallowRef } from "vue";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { routeApp } from "~/router/RouteApp";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { RouteNode } from "../RouteNode";
import { routeStundenplanDaten } from "./stundenplan/RouteStundenplanDaten";
import { routeStundenplanUnterricht } from "./stundenplan/RouteStundenplanUnterricht";
import { routeStundenplanPausenaufsicht } from "./stundenplan/RouteStundenplanPausenaufsicht";
import type { StundenplanAppProps } from "~/components/stundenplan/SStundenplanAppProps";

interface RouteStateStundenplan {
	view: RouteNode<any, any>;
}
export class RouteDataStundenplan {

	private static _defaultState: RouteStateStundenplan = {
		view: routeStundenplanDaten,
	}
	private _state = shallowRef(RouteDataStundenplan._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateStundenplan>) {
		this._state.value = Object.assign({ ... RouteDataStundenplan._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateStundenplan>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeStundenplan.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Stundenpläne gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}
}
const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue")

export class RouteStundenplan extends RouteNode<RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan", "/stundenplan", SStundenplanApp, new RouteDataStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("liste", SStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeStundenplanDaten,
			routeStundenplanUnterricht,
			routeStundenplanPausenaufsicht,
		];
		super.defaultChild = routeStundenplanDaten;
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAppProps {
		return {
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
		for (const c of this.children)
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
		await RouteManager.doRoute({ name: value.name, params: { } });
		await this.data.setView(node);
	}
}

export const routeStundenplan = new RouteStundenplan();
