import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { SchuleDatenaustauschSchulbewerbungProps } from "~/components/schule/datenaustausch/schulbewerbung/SSchuleDatenaustauschSchulbewerbungProps";
import type { RouteApp} from "../../RouteApp";
import { routeApp } from "../../RouteApp";

const SSchuleDatenaustauschSchulbewerbung = () => import("~/components/schule/datenaustausch/schulbewerbung/SSchuleDatenaustauschSchulbewerbung.vue");

export class RouteSchuleDatenaustauschSchulbewerbung extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE ], "schule.datenaustausch.schulbewerbung", "schulbewerbung", SSchuleDatenaustauschSchulbewerbung);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbewerbung.de";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschSchulbewerbungProps {
		return {
		};
	}
}

export const routeSchuleDatenaustauschSchulbewerbung = new RouteSchuleDatenaustauschSchulbewerbung();

