import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import type { OrtsteileDatenProps } from "~/components/schule/kataloge/ortsteile/daten/OrtsteileDatenProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const OrtsteileDaten = () => import("~/components/schule/kataloge/ortsteile/daten/OrtsteileDaten.vue");

export class RouteOrtsteileDaten extends RouteNode<any, RouteOrtsteile> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.ortsteile.daten", "daten", OrtsteileDaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ortsteil";
	}

	public getProps(to: RouteLocationNormalized): OrtsteileDatenProps {
		return {
			manager: () => routeOrtsteile.data.manager,
			patch: routeOrtsteile.data.patch,
		};
	}

}

export const routeOrtsteileDaten = new RouteOrtsteileDaten();

