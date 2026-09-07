import type { RouteLocationNormalized, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ConfigElement } from "@ui/utils/Config";

import type { StundenplanRaumProps } from "~/components/stundenplan/raum/SStundenplanRaumProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { configStateImpl } from "~/states/ConfigStateImpl";

import { RouteStundenplan, routeStundenplan } from "./RouteStundenplan";

const SStundenplanRaum = () => import("~/components/stundenplan/raum/SStundenplanRaum.vue");

export class RouteStundenplanRaum extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.raum", "raum", SStundenplanRaum);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
		configStateImpl.config.addElements([
			new ConfigElement("stundenplan.raeume.ganzerStundenplan", "user", "true"),
		]);
	}

	public getProps(to: RouteLocationNormalized): StundenplanRaumProps {
		return {
			apiStatus: api.status,
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			ganzerStundenplanRaeume: () => routeStundenplan.data.ganzerStundenplanRaum,
			setGanzerStundenplanRaeume: routeStundenplan.data.setGanzerStundenplanRaum,
		};
	}

}

export const routeStundenplanRaum = new RouteStundenplanRaum();
