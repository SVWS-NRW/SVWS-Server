import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { RouteApp } from "../RouteApp";

import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { NotenmodulZugangsdatenProps } from "~/components/notenmodul/NotenmodulZugangsdatenProps";
import { RouteDataNotenmodulZugangsdaten } from "./RouteDataNotenmodulZugangsdaten";
import { routeNotenmodul } from "./RouteNotenmodul";

const NotenmodulZugangsdaten = () => import("~/components/notenmodul/NotenmodulZugangsdaten.vue");

export class RouteNotenmodulZugangsdaten extends RouteNode<RouteDataNotenmodulZugangsdaten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.zugangsdaten", "notenmodul/zugangsdaten", NotenmodulZugangsdaten, new RouteDataNotenmodulZugangsdaten());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zugangsdaten";
		super.children = [];
		super.menugroup = RouteNotenmodulMenuGroup.ADMINISTRATION;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await routeNotenmodul.data.ladeDaten();
		}
		await this.data.init();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul"))) {
			await routeNotenmodul.data.entferneDaten();
		}
		await super.leave(from, from_params, to, to_params);
	}

	public getProps(to: RouteLocationNormalized): NotenmodulZugangsdatenProps {
		return {
			manager: () => routeNotenmodul.data.manager,
			mapEnmInitialKennwoerter: () => this.data.mapEnmInitialKennwoerter,
		};
	}


}

export const routeNotenmodulZugangsdaten = new RouteNotenmodulZugangsdaten();
