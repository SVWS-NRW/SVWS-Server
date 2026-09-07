import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchuelerLernabschnittZeugnisdruckProps } from "~/components/schueler/lernabschnitte/zeugnisdruck/SchuelerLernabschnittZeugnisdruckProps";
import { type RouteSchuelerLernabschnitte, routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { RouteNode } from "~/router/RouteNode";

const SchuelerLernabschnittAllgemein = () => import("~/components/schueler/lernabschnitte/zeugnisdruck/SchuelerLernabschnittZeugnisdruck.vue");

export class RouteSchuelerLernabschnittZeugnisdruck extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.zeugnisdruck", "zeugnisdruck", SchuelerLernabschnittAllgemein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeugnisdruck";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittZeugnisdruckProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen,
		};
	}

}

export const routeSchuelerLernabschnittZeugnisdruck = new RouteSchuelerLernabschnittZeugnisdruck();

