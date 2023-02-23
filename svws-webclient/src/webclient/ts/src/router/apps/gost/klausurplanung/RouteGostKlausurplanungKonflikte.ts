import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { Schulform } from "@svws-nrw/svws-core-ts";

const SGostKlausurplanungKonflikte = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKonflikte.vue");

export class RouteGostKlausurplanungKonflikte extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), "gost.klausurplanung.konflikte", "konflikte", SGostKlausurplanungKonflikte);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konflikte";
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

export const routeGostKlausurplanungKonflikte = new RouteGostKlausurplanungKonflikte();
