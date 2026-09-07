import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { EmailServerProps } from "~/components/einstellungen/emailserver/EmailServerProps";
import { RouteDataEmailServer } from "~/router/apps/einstellungen/emailserver/RouteDataEmailServer";
import { RouteEinstellungenMenuGroup } from "~/router/apps/einstellungen/RouteEinstellungenMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";

export const EmailServer = () => import("~/components/einstellungen/emailserver/EmailServer.vue");

export class RouteEmailServer extends RouteNode<RouteDataEmailServer, RouteApp> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.ADMIN],
			"einstellungen.emailserver",
			"einstellungen/emailserver",
			EmailServer,
			new RouteDataEmailServer());
		super.propHandler = (route) => this.getProps(route);
		super.mode = ServerMode.STABLE;
		super.text = "E-Mail-Server";
		super.children = [];
		super.menugroup = RouteEinstellungenMenuGroup.ALLGEMEIN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await routeEmailServer.data.ladeDaten();
		}
		await this.data.ladeDaten();
	}

	public getProps(_: RouteLocationNormalized): EmailServerProps {
		return {
			smptServerKonfiguration: () => routeEmailServer.data.smtpServerKonfiguration,
			patch: routeEmailServer.data.patchSMTServerKonfiguration,
		};
	}
}

export const routeEmailServer = new RouteEmailServer();
