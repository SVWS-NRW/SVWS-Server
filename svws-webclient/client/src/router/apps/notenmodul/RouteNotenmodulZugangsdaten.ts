import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import type { RouteApp } from "../RouteApp";
import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { NotenmodulZugangsdatenProps } from "~/components/notenmodul/NotenmodulZugangsdatenProps";
import { RouteDataNotenmodulZugangsdaten } from "./RouteDataNotenmodulZugangsdaten";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { routeError } from "~/router/error/RouteError";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const NotenmodulZugangsdaten = () => import("~/components/notenmodul/NotenmodulZugangsdaten.vue");

export class RouteNotenmodulZugangsdaten extends RouteNode<RouteDataNotenmodulZugangsdaten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.zugangsdaten", "notenmodul/zugangsdaten", NotenmodulZugangsdaten, new RouteDataNotenmodulZugangsdaten());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zugangsdaten";
		super.children = [];
		super.menugroup = RouteNotenmodulMenuGroup.ADMINISTRATION;
		this.isHidden = () => this.checkHidden();
	}

	protected checkHidden() {
		try {
			if (notenmodulStateImpl.istAdminLehrer === false) {
				return routeNotenmodulLeistungen.getRouteDefaultChild();
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await notenmodulStateImpl.ladeDaten();
		}
		await this.data.init(notenmodulStateImpl.idsLehrer);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul"))) {
			notenmodulStateImpl.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

	public getProps(to: RouteLocationNormalized): NotenmodulZugangsdatenProps {
		return {
			open: this.data.open,
			manager: () => notenmodulStateImpl.manager,
			mapEnmInitialKennwoerter: () => this.data.mapEnmInitialKennwoerter,
			resetPassword: this.data.resetPassword,
			generateInitialPassword: this.data.generateInitialPassword,
			resetTotp: this.data.resetTotp,
			set2fa: this.data.set2fa,
		};
	}


}

export const routeNotenmodulZugangsdaten = new RouteNotenmodulZugangsdaten();
