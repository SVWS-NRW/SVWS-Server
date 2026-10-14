import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { useAbteilungenState } from "@ui/states/kataloge/AbteilungenState";
import { useNotenmodulState } from "@ui/states/NotenmodulState";

import type { RouteApp } from "../RouteApp";
import type { NotenmodulZugangsdatenProps } from "~/components/notenmodul/NotenmodulZugangsdatenProps";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";

import { RouteDataNotenmodulZugangsdaten } from "./RouteDataNotenmodulZugangsdaten";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";

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
		const notenmodulState = useNotenmodulState();
		try {
			if (notenmodulState.auswahlAnsicht !== ENMLehrerFunktion.NOTENMODULADMIN) {
				return routeNotenmodulLeistungen.getRouteDefaultChild();
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		const notenmodulState = useNotenmodulState();
		if (isEntering && !(from?.name.startsWith("notenmodul.") ?? false)) {
			const abteilungenState = useAbteilungenState();
			await abteilungenState.init();
			await notenmodulState.ladeDaten();
		}
		await this.data.init(notenmodulState.idsLehrer);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
		if (!(to.name.startsWith("notenmodul"))) {
			const notenmodulState = useNotenmodulState();
			notenmodulState.reset();
		}
		await super.leave(from, from_params, to, to_params);
	}

	public getProps(to: RouteLocationNormalized): NotenmodulZugangsdatenProps {
		const notenmodulState = useNotenmodulState();
		return {
			open: this.data.open,
			manager: () => notenmodulState.manager,
			mapEnmInitialKennwoerter: () => this.data.mapEnmInitialKennwoerter,
			resetPassword: this.data.resetPassword,
			generateInitialPassword: this.data.generateInitialPassword,
			resetTotp: this.data.resetTotp,
			set2fa: this.data.set2fa,
		};
	}


}

export const routeNotenmodulZugangsdaten = new RouteNotenmodulZugangsdaten();
