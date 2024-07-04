import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import { routeApp } from "../RouteApp";
import type { StundenplanUnterrichteProps } from "~/components/stundenplan/unterrichte/SStundenplanUnterrichteProps";

const SStundenplanUnterrichte = () => import("~/components/stundenplan/unterrichte/SStundenplanUnterrichte.vue");

export class RouteStundenplanUnterrichte extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.unterrichte", "unterrichte", SStundenplanUnterrichte);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Unterrichte";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanUnterrichteProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			stundenplanUnterrichtListeManager: () => routeStundenplan.data.stundenplanUnterrichtListeManager,
			setFilter: routeStundenplan.data.setFilter,
			patchUnterricht: routeStundenplan.data.patchUnterricht,
		};
	}

}

export const routeStundenplanUnterrichte = new RouteStundenplanUnterrichte();

