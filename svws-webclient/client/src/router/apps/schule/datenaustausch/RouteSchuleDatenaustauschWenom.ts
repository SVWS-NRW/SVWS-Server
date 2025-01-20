import type { RouteLocationNormalized } from "vue-router";

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

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschWenomProps {
		return {
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

