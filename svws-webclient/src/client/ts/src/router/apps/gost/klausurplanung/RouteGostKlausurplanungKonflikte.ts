import { RouteNode } from "~/router/RouteNode";
import { routeGost } from "~/router/apps/RouteGost";
import type { RouteGostKlausurplanung } from "../RouteGostKlausurplanung";
import { routeGostKlausurplanung } from "../RouteGostKlausurplanung";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

const SGostKlausurplanungKonflikte = () => import("~/components/gost/klausurplanung/SGostKlausurplanungKonflikte.vue");

export class RouteGostKlausurplanungKonflikte extends RouteNode<unknown, RouteGostKlausurplanung> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.klausurplanung.konflikte", "konflikte", SGostKlausurplanungKonflikte);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konflikte";
	}

	public getRoute(abiturjahr: number, halbjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgangsdaten: routeGostKlausurplanung.data.jahrgangsdaten
		}
	}
}

export const routeGostKlausurplanungKonflikte = new RouteGostKlausurplanungKonflikte();
