import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import type { SchuleReportingKonfigurationProps } from "~/components/schule/reporting/SchuleReportingKonfigurationProps";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";

import { RouteDataSchuleReporting } from "./RouteDataSchuleReporting";

const SchuleReportingKonfiguration = () => import("~/components/schule/reporting/SchuleReportingKonfiguration.vue");

export class RouteSchuleReportingKonfiguration extends RouteNode<RouteDataSchuleReporting, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN], "schule.reporting.konfiguration", "konfiguration", SchuleReportingKonfiguration, new RouteDataSchuleReporting());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einstellungen";
		super.menugroup = RouteSchuleMenuGroup.REPORTING;
	}

	public getProps(to: RouteLocationNormalized): SchuleReportingKonfigurationProps {
		return {
		};
	}
}

export const routeSchuleReportingKonfiguration = new RouteSchuleReportingKonfiguration();
