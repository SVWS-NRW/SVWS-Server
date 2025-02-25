import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { StundenplanRaumProps } from "~/components/stundenplan/raum/SStundenplanRaumProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeStundenplan, type RouteStundenplan } from "./RouteStundenplan";
import { api } from "~/router/Api";
import { ConfigElement } from "~/components/Config";

const SStundenplanRaum = () => import("~/components/stundenplan/raum/SStundenplanRaum.vue");

export class RouteStundenplanRaum extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.raum", "raum", SStundenplanRaum);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
		api.config.addElements([
			new ConfigElement("stundenplan.raeume.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public getProps(to: RouteLocationNormalized): StundenplanRaumProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			ganzerStundenplanRaeume: () => routeStundenplan.data.ganzerStundenplanRaum,
			setGanzerStundenplanRaeume: routeStundenplan.data.setGanzerStundenplanRaum,
		};
	}

}

export const routeStundenplanRaum = new RouteStundenplanRaum();
