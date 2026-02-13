import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";

import type { LehrerUnterrichtsdatenProps } from "~/components/lehrer/unterrichtsdaten/LehrerUnterrichtsdatenProps";

const LehrerUnterrichtsdaten = () => import("~/components/lehrer/unterrichtsdaten/LehrerUnterrichtsdaten.vue");

export class RouteLehrerUnterrichtsdaten extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer.unterrichtsdaten", "unterrichtsdaten", LehrerUnterrichtsdaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Unterricht";
	}

	public getProps(to: RouteLocationNormalized): LehrerUnterrichtsdatenProps {
		return {
			lehrerListeManager: () => routeLehrer.data.manager,
		};
	}

}

export const routeLehrerUnterrichtsdaten = new RouteLehrerUnterrichtsdaten();
