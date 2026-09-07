import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchuelerLernabschnittKonferenzProps } from "~/components/schueler/lernabschnitte/konferenz/SchuelerLernabschnittKonferenzProps";
import { type RouteSchuelerLernabschnitte, routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { RouteNode } from "~/router/RouteNode";

const SchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/konferenz/SchuelerLernabschnittKonferenz.vue");

export class RouteSchuelerLernabschnittKonferenz extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.konferenz", "konferenz", SchuelerLernabschnittAllgmein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konferenz";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittKonferenzProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen,
		};
	}

}

export const routeSchuelerLernabschnittKonferenz = new RouteSchuelerLernabschnittKonferenz();
