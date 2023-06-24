import { RouteNode } from "~/router/RouteNode";
import type { RouteGost} from "~/router/apps/RouteGost";
import { routeGost } from "~/router/apps/RouteGost";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { GostStammdatenProps } from "~/components/gost/stammdaten/SGostStammdatenProps";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.jahrgangsdaten", "daten", SGostStammdaten);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemein";
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostStammdatenProps {
		return {
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
		};
	}

}

export const routeGostJahrgangsdaten = new RouteGostJahrgangsdaten();
