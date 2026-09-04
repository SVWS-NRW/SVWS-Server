import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { RouteDataSchuleReporting } from "./RouteDataSchuleReporting";
import type { SchuleReportingProps } from "~/components/schule/reporting/SchuleReportingProps";
import { routeSchuleReportingKonfiguration } from "./RouteSchuleReportingKonfiguration";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";
import { ConfigElement } from "@ui/utils/Config";

const SchuleReporting = () => import("~/components/schule/reporting/SchuleReporting.vue");

export class RouteSchuleReporting extends RouteNode<RouteDataSchuleReporting, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN], "schule.reporting", "reporting", SchuleReporting, new RouteDataSchuleReporting());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Reporting";
		super.menugroup = RouteSchuleMenuGroup.REPORTING;
		super.children = [routeSchuleReportingKonfiguration];

		for (const vorlage of ReportingReportvorlage.values()) {
			configStateImpl.config.addElement(new ConfigElement(vorlage.getConfigKeyBenutzerVorlage(), "user", ""));
		}
	}

	public getProps(to: RouteLocationNormalized): SchuleReportingProps {
		return {
		};
	}
}

export const routeSchuleReporting = new RouteSchuleReporting();
