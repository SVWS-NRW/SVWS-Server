import { RouteNode } from "~/router/RouteNode";
import type { RouteLehrer} from "~/router/apps/RouteLehrer";
import { routeLehrer } from "~/router/apps/RouteLehrer";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { LehrerUnterrichtsdatenProps } from "~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdatenProps";
import { BenutzerKompetenz, Schulform } from "@core";

const SLehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/SLehrerUnterrichtsdaten.vue");

export class RouteLehrerUnterrichtsdaten extends RouteNode<unknown, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "lehrer.unterrichtsdaten", "unterrichtsdaten", SLehrerUnterrichtsdaten);
		super.propHandler = (route) => routeLehrer.getProps(route);
		super.text = "Unterricht";
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
