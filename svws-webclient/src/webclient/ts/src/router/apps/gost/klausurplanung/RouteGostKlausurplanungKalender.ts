import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { Schulform } from "@svws-nrw/svws-core-ts";

const SGostKlausurplanungKalender = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKalender.vue");

export class RouteGostKlausurplanungKalender extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super("gost.klausurplanung.kalender", "kalender", SGostKlausurplanungKalender);
		super.setSchulformenErlaubt(Schulform.getMitGymOb());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kalender";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGost.data.jahrgangsdaten.value
		}
	}

}

export const routeGostKlausurplanungKalender = new RouteGostKlausurplanungKalender();

