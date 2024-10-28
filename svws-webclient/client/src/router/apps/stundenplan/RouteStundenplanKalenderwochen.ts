import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { StundenplanKalenderwochenProps } from "~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochenProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import { routeApp } from "../RouteApp";
import { api } from "~/router/Api";

const SStundenplanKalenderwochen = () => import("~/components/stundenplan/kalenderwochen/SStundenplanKalenderwochen.vue");

export class RouteStundenplanKalenderwochen extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.kalenderwochen", "kalenderwochen", SStundenplanKalenderwochen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalenderwochen";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanKalenderwochenProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patchKalenderwochenzuordnungen: routeStundenplan.data.patchKalenderwochenzuordnungen,
			deleteKalenderwochenzuordnungen: routeStundenplan.data.deleteKalenderwochenzuordnungen,
		};
	}

}

export const routeStundenplanKalenderwochen = new RouteStundenplanKalenderwochen();

