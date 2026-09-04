import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchulen, type RouteSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import type { SchulenDatenProps } from "~/components/schule/kataloge/schulen/daten/SchulenDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchulenDaten = () => import("~/components/schule/kataloge/schulen/daten/SchulenDaten.vue");

export class RouteSchulenDaten extends RouteNode<any, RouteSchulen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen.daten", "daten", SchulenDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
	}

	public getProps(to: RouteLocationNormalized): SchulenDatenProps {
		return {
			manager: () => routeSchulen.data.manager,
			patch: routeSchulen.data.patch,
		};
	}

}

export const routeSchulenDaten = new RouteSchulenDaten();

