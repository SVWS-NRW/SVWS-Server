import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import type { OrteDatenProps } from "~/components/schule/kataloge/orte/daten/OrteDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const OrteDaten = () => import("~/components/schule/kataloge/orte/daten/OrteDaten.vue");

export class RouteOrteDaten extends RouteNode<any, RouteOrte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.orte.daten", "daten", OrteDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ort";
	}

	public getProps(to: RouteLocationNormalized): OrteDatenProps {
		return {
			manager: () => routeOrte.data.manager,
			patch: routeOrte.data.patch,
		};
	}

}

export const routeOrteDaten = new RouteOrteDaten();

