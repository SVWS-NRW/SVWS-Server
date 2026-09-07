import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { LehrerUnterrichtsdatenProps } from "~/components/lehrer/unterrichtsdaten/LehrerUnterrichtsdatenProps";
import { type RouteLehrer, routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteNode } from "~/router/RouteNode";

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
