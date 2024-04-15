import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeSchuleDatenaustauschKurs42, type RouteSchuleDatenaustauschKurs42 } from "~/router/apps/schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";

import type { SchuleDatenaustauschKurs42RaeumeProps } from "~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42RaeumeProps";

const SSchuleDatenaustauschKurs42Raeume = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42Raeume.vue");

export class RouteSchuleDatenaustauschKurs42Raeume extends RouteNode<unknown, RouteSchuleDatenaustauschKurs42> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.kurs42.raeume", "raeume", SSchuleDatenaustauschKurs42Raeume);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Räume";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschKurs42RaeumeProps {
		return {
			setGostKurs42RaeumeTxt: routeSchuleDatenaustauschKurs42.data.setGostKurs42RaeumeTxt,
		};
	}

}

export const routeSchuleDatenaustauschKurs42Raeume = new RouteSchuleDatenaustauschKurs42Raeume();

