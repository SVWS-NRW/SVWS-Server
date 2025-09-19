import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { SchuleDatenaustauschWenomProps } from "~/components/schule/datenaustausch/webnotenmanager/SSchuleDatenaustauschWenomProps";
import type { RouteApp } from "../../../RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteDataSchuleDatenaustauschWenom } from "./RouteDataSchuleDatenaustauschWenom";

const SSchuleDatenaustauschWenom = () => import("~/components/schule/datenaustausch/webnotenmanager/SSchuleDatenaustauschWenom.vue");

export class RouteSchuleDatenaustauschWenom extends RouteNode<RouteDataSchuleDatenaustauschWenom, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.NOTENMODUL_ADMINISTRATION ], "schule.datenaustausch.wenom", "wenom", SSchuleDatenaustauschWenom, new RouteDataSchuleDatenaustauschWenom());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Webnotenmanager";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.init();
	}

	public async leave(): Promise<void> {
		this.data.reset();
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschWenomProps {
		return {
			connected: this.data.connected,
			connectionInfo: () => this.data.connectionInfo,
			enmDaten: () => this.data.enmDaten,
			mapEnmInitialKennwoerter: () => this.data.mapEnmInitialKennwoerter,
			connect: this.data.connect,
			trustCertificate: this.data.trustCertificate,
			serverConfig: () => this.data.mapEnmServerConfigServer,
			setServerConfigElement: this.data.wenomSetServerConfigElement,
			setCredentials: this.data.wenomSetCredentials,
			removeCredentials: this.data.wenomRemoveCredential,
			synchronize: this.data.wenomSynchronize,
			download: this.data.wenomDownload,
			upload: this.data.wenomUpload,
			truncate: this.data.wenomTruncate,
			reset: this.data.wenomReset,
		};
	}
}

export const routeSchuleDatenaustauschWenom = new RouteSchuleDatenaustauschWenom();

