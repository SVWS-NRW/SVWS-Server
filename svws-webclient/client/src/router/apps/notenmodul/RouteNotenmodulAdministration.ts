import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { ENMLehrerFunktion } from "@core/core/data/enm/ENMLehrerFunktion";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { WenomAuswahlListeManager } from "@ui/components/enm/WenomAuswahlListeManager";
import { useAbteilungenState } from "@ui/states/kataloge/AbteilungenState";
import { useNotenmodulState } from "@ui/states/NotenmodulState";

import type { RouteApp } from "../RouteApp";
import { routeError } from "~/router/error/RouteError";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";

import { RouteDataNotenmodulAdministration } from "./RouteDataNotenmodulAdministration";
import { routeNotenmodulVerbindungGruppenprozesse } from "./RouteNotenmodulGruppenprozesse";
import { routeNotenmodulKonfiguration } from "./RouteNotenmodulKonfiguration";
import { routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import { routeNotenmodulMail } from "./RouteNotenmodulMail";
import { RouteNotenmodulMenuGroup } from "./RouteNotenmodulMenuGroup";
import { routeNotenmodulSynchronisation } from "./RouteNotenmodulSynchronisation";
import { routeNotenmodulVerbindung } from "./RouteNotenmodulVerbindung";
import { routeNotenmodulVerbindungNeu } from "./RouteNotenmodulVerbindungNeu";

const NotenmodulAdministrationApp = () => import("~/components/notenmodul/NotenmodulAdministrationApp.vue");
const NotenmodulAdministrationAuswahl = () => import("~/components/notenmodul/NotenmodulAdministrationAuswahl.vue");

export class RouteNotenmodulAdministration extends RouteAuswahlNode<WenomAuswahlListeManager, RouteDataNotenmodulAdministration, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration", String.raw`notenmodul/administration/:id(-?\d+)?`, NotenmodulAdministrationApp, NotenmodulAdministrationAuswahl, new RouteDataNotenmodulAdministration());
		super.mode = ServerMode.STABLE;
		super.getAuswahlListProps = (props) => ({
			...props,
			manager: () => routeNotenmodulAdministration.data.manager,
		});
		super.text = "Serververbindungen";
		super.children = [
			routeNotenmodulVerbindung,
			routeNotenmodulKonfiguration,
			routeNotenmodulMail,
			routeNotenmodulSynchronisation,
			routeNotenmodulVerbindungNeu,
			routeNotenmodulVerbindungGruppenprozesse,
		];
		super.defaultChild = routeNotenmodulVerbindung;
		super.menugroup = RouteNotenmodulMenuGroup.ADMINISTRATION;
		super.updateIfTarget = this.doUpdateIfTarget;
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

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering && !(from?.name.startsWith("notenmodul.") ?? false)) {
			const abteilungenState = useAbteilungenState();
			await abteilungenState.init();
			const notenmodulState = useNotenmodulState();
			await notenmodulState.ladeDaten();
		}
		return await super.update(to, to_params, from, from_params, isEntering, redirected);
	}

	protected doUpdateIfTarget = async () => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeNotenmodulAdministration = new RouteNotenmodulAdministration();
