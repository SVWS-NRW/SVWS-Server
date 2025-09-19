import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { SchuleDatenaustauschENMProps } from "~/components/schule/datenaustausch/enmNotenmanager/SSchuleDatenaustauschENMProps";
import type { RouteApp } from "../../../RouteApp";
import { routeSchule } from "../../RouteSchule";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

const SSchuleDatenaustauschENM = () => import("~/components/schule/datenaustausch/enmNotenmanager/SSchuleDatenaustauschENM.vue");

export class RouteSchuleDatenaustauschENM extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.NOTENMODUL_ADMINISTRATION ], "schule.datenaustausch.enm", "enm", SSchuleDatenaustauschENM);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "ENM Notenmanager";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschENMProps {
		return {
			setImportENM: routeSchule.data.setImportENM,
		};
	}
}

export const routeSchuleDatenaustauschENM = new RouteSchuleDatenaustauschENM();

