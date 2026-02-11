import { RouteNode } from "~/router/RouteNode";
import { RouteDataEmailServer } from "~/router/apps/einstellungen/emailserver/RouteDataEmailServer";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteEinstellungenMenuGroup } from "~/router/apps/einstellungen/RouteEinstellungenMenuGroup";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import type { EmailServerProps } from "~/components/einstellungen/emailserver/EmailServerProps";

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
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeEmailServer = new RouteEmailServer();
