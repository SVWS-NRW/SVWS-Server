import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

import { routeSchuleAdressdaten } from "../adressdaten/RouteSchuleAdressdaten";
import type { SchuleMerkmaleProps } from "~/components/schule/stammdaten/merkmale/SchuleMerkmaleProps";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { RouteNode } from "~/router/RouteNode";

const SchuleMerkmale = () => import("~/components/schule/stammdaten/merkmale/SchuleMerkmale.vue");

export class RouteSchuleMerkmale extends RouteNode<any, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.merkmale",
			"merkmale",
			SchuleMerkmale
		);
		super.text = "Merkmale";
		super.propHandler = (route) => this.getProps(route);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await routeSchuleAdressdaten.data.ladeDaten();
		}
	}

	public getProps(to: RouteLocationNormalized): SchuleMerkmaleProps {
		return {
			patch: routeSchuleAdressdaten.data.patch,
		};
	}
}

export const routeSchuleMerkmale = new RouteSchuleMerkmale();
