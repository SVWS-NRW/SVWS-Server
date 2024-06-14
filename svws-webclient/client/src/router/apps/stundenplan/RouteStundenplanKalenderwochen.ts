import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import { routeApp } from "../RouteApp";
import type { StundenplanKalenderwochenProps } from "~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochenProps";

const SStundenplanKalenderwochen = () => import("~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochen.vue");

export class RouteStundenplanKalenderwochen extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kalenderwochen", "kalenderwochen", SStundenplanKalenderwochen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalenderwochen";
		// this.isHidden = () => routeStundenplan.data.daten.wochenTypModell === 0 ? false : routeStundenplan.getRoute();
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanKalenderwochenProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patch: routeStundenplan.data.patch,
		};
	}

}

export const routeStundenplanKalenderwochen = new RouteStundenplanKalenderwochen();

