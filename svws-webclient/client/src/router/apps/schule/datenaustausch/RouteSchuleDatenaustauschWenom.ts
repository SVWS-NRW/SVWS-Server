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
			return routeSchule.data.ladeCredentials();
	}


	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschWenomProps {
		return {
			secretSet: () => routeSchule.data.secretSet,
			setWenomCredentials: routeSchule.data.setWenomCredentials,
			wenomSynchronize: routeSchule.data.wenomSynchronize,
			wenomTruncate: routeSchule.data.wenomTruncate,
			wenomRemoveCredentials: routeSchule.data.wenomRemoveCredential,
		};
	}
}

export const routeSchuleDatenaustauschWenom = new RouteSchuleDatenaustauschWenom();

