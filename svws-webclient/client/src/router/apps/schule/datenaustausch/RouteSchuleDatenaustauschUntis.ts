import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustausch, type RouteSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";

import type { SchuleDatenaustauschUntisProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisProps";
import { api } from "~/router/Api";
import { routeApp } from "../../RouteApp";

const SSchuleDatenaustauschUntis = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntis.vue");

export class RouteSchuleDatenaustauschUntis extends RouteNode<unknown, RouteSchuleDatenaustausch> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch.untis", "untis", SSchuleDatenaustauschUntis);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Untis";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisProps {
		return {
			setUntisImportGPU: routeSchuleDatenaustausch.data.setUntisImportGPU,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
		};
	}
}

export const routeSchuleDatenaustauschUntis = new RouteSchuleDatenaustauschUntis();

