import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { ViewType } from "@ui";
import type { RouteOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import type { OrtsteileGruppenprozesseProps } from "~/components/schule/kataloge/ortsteile/gruppenprozesse/OrtsteileGruppenprozesseProps";

const OrtsteileGruppenprozesse = () => import("~/components/schule/kataloge/ortsteile/gruppenprozesse/OrtsteileGruppenprozesse.vue");

export class RouteOrtsteileGruppenprozesse extends RouteNode<any, RouteOrtsteile> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.ortsteile.gruppenprozesse", "gruppenprozesse", OrtsteileGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): OrtsteileGruppenprozesseProps {
		return {
			serverMode: api.mode,
			manager: () => routeOrtsteile.data.manager,
			delete: routeOrtsteile.data.delete,
			deleteCheck: routeOrtsteile.data.deleteCheck,
			benutzerKompetenzen: api.benutzerKompetenzen,
			goToDefaultView: routeOrtsteile.data.gotoDefaultView,
		};
	}

}

export const routeOrtsteileGruppenprozesse = new RouteOrtsteileGruppenprozesse();

