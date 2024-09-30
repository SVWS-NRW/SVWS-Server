import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { SchuleDatenaustauschLaufbahnplanungProps } from "~/components/schule/datenaustausch/laufbahnplanung/SSchuleDatenaustauschLaufbahnplanungProps";
import type { RouteApp} from "../../RouteApp";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeSchule } from "../RouteSchule";

const SSchuleDatenaustauschLaufbahnplanung = () => import("~/components/schule/datenaustausch/laufbahnplanung/SSchuleDatenaustauschLaufbahnplanung.vue");

export class RouteSchuleDatenaustauschLaufbahnplanung extends RouteNode<any, RouteApp> {

	public constructor() {
		super(schulformenGymOb, [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.laufbahnplanung", "laufbahnplanung", SSchuleDatenaustauschLaufbahnplanung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "LuPO Laufbahnplanung";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschLaufbahnplanungProps {
		return {
			setGostLupoImportMDBFuerJahrgang: routeSchule.data.setGostLupoImportMDBFuerJahrgang,
		};
	}
}

export const routeSchuleDatenaustauschLaufbahnplanung = new RouteSchuleDatenaustauschLaufbahnplanung();

