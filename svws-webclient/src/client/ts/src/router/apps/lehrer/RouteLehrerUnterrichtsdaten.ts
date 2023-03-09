import { RouteNode } from "~/router/RouteNode";
import { RouteLehrer, routeLehrer } from "~/router/apps/RouteLehrer";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { LehrerUnterrichtsdatenProps } from "~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdatenProps";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";

const SLehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdaten.vue");

export class RouteLehrerUnterrichtsdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer_unterrichtsdaten", "unterrichtsdaten", SLehrerUnterrichtsdaten);
		super.propHandler = (route) => routeLehrer.getProps(route);
		super.text = "Unterrichtsdaten";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): LehrerUnterrichtsdatenProps {
		return {
			stammdaten: routeLehrer.data.stammdaten
		};
	}

}

export const routeLehrerUnterrichtsdaten = new RouteLehrerUnterrichtsdaten();
