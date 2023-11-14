import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";

import type { SchuleDatenaustauschSchuelerOnlineProps } from "~/components/schule/datenaustausch/schueleronline/SSchuleDatenaustauschSchuelerOnlineProps";

const SSchuleDatenaustauschSchuelerOnline = () => import("~/components/schule/datenaustausch/schueleronline/SSchuleDatenaustauschSchuelerOnline.vue");

export class RouteSchuleDatenaustauschSchuelerOnline extends RouteNode<unknown, RouteSchuleDatenaustausch> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.IMPORT_EXPORT_SCHUELER_ONLINE ], "schule.datenaustausch.schueleronline", "schueleronline", SSchuleDatenaustauschSchuelerOnline);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler-Online";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschSchuelerOnlineProps {
		return {
		};
	}
}

export const routeSchuleDatenaustauschSchuelerOnline = new RouteSchuleDatenaustauschSchuelerOnline();

