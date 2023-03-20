import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { GostStammdatenProps } from "~/components/gost/stammdaten/SGostStammdatenProps";

const SGostStammdaten = () => import("~/components/gost/stammdaten/SGostStammdaten.vue");

export class RouteGostJahrgangsdaten extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.jahrgangsdaten", "daten", SGostStammdaten);
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
