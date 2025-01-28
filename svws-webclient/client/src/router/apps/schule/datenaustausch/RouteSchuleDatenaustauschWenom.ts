import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { SchuleDatenaustauschWenomProps } from "~/components/schule/datenaustausch/wenom/SSchuleDatenaustauschWenomProps";
import type { RouteApp } from "../../RouteApp";
import { routeSchule } from "../RouteSchule";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";

const SSchuleDatenaustauschWenom = () => import("~/components/schule/datenaustausch/wenom/SSchuleDatenaustauschWenom.vue");

export class RouteSchuleDatenaustauschWenom extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.NOTENMODUL_ADMINISTRATION ], "schule.datenaustausch.wenom", "wenom", SSchuleDatenaustauschWenom);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Webnotenmanager";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			routeSchule.data.mapInitialKennwoerter = await routeSchule.data.wenomGetEnmCredentials();
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschWenomProps {
		return {
			mapInitialKennwoerter: () => routeSchule.data.mapInitialKennwoerter,
			getEnmDaten: routeSchule.data.wenomGetEnmDaten,
			getEnmCredentials: routeSchule.data.wenomGetEnmCredentials,
			getCredentials: routeSchule.data.wenomGetCredentials,
			setCredentials: routeSchule.data.wenomSetCredentials,
			removeCredentials: routeSchule.data.wenomRemoveCredential,
			synchronize: routeSchule.data.wenomSynchronize,
			download: routeSchule.data.wenomDownload,
			upload: routeSchule.data.wenomUpload,
			truncate: routeSchule.data.wenomTruncate,
			reset: routeSchule.data.wenomReset,
			check: routeSchule.data.wenomCheck,
			setup: routeSchule.data.wenomSetup,
		};
	}
}

export const routeSchuleDatenaustauschWenom = new RouteSchuleDatenaustauschWenom();

