import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittZeugnisdruckProps } from "~/components/schueler/lernabschnitte/zeugnisdruck/SchuelerLernabschnittZeugnisdruckProps";

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

