import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustausch, type RouteSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";

import type { SchuleDatenaustauschWenomProps } from "~/components/schule/datenaustausch/Wenom/SSchuleDatenaustauschWenomProps";

const SSchuleDatenaustauschWenom = () => import("~/components/schule/datenaustausch/wenom/SSchuleDatenaustauschWenom.vue");

export class RouteSchuleDatenaustauschWenom extends RouteNode<unknown, RouteSchuleDatenaustausch> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.wenom.daten", "Webnotenmanager", SSchuleDatenaustauschWenom);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Webnotenmanager";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschWenomProps {
		return {
		};
	}
}

export const routeSchuleDatenaustauschWenom = new RouteSchuleDatenaustauschWenom();

