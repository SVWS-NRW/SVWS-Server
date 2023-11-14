import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";

import type { SchuleDatenaustauschSchulbewerbungProps } from "~/components/schule/datenaustausch/schulbewerbung/SSchuleDatenaustauschSchulbewerbungProps";

const SSchuleDatenaustauschSchulbewerbung = () => import("~/components/schule/datenaustausch/schulbewerbung/SSchuleDatenaustauschSchulbewerbung.vue");

export class RouteSchuleDatenaustauschSchulbewerbung extends RouteNode<unknown, RouteSchuleDatenaustausch> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE ], "schule.datenaustausch.schulbewerbung", "schulbewerbung", SSchuleDatenaustauschSchulbewerbung);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbewerbung.de";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschSchulbewerbungProps {
		return {
		};
	}
}

export const routeSchuleDatenaustauschSchulbewerbung = new RouteSchuleDatenaustauschSchulbewerbung();

