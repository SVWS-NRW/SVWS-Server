import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { OrtsteileGruppenprozesseProps } from "~/components/schule/kataloge/ortsteile/gruppenprozesse/OrtsteileGruppenprozesseProps";
import type { RouteOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

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
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): OrtsteileGruppenprozesseProps {
		return {
			manager: () => routeOrtsteile.data.manager,
			delete: routeOrtsteile.data.delete,
			deleteCheck: routeOrtsteile.data.deleteCheck,
			goToDefaultView: routeOrtsteile.data.gotoDefaultView,
		};
	}

}

export const routeOrtsteileGruppenprozesse = new RouteOrtsteileGruppenprozesse();

