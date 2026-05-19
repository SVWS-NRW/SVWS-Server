import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";

import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { RouteDataSchuleReporting } from "./RouteDataSchuleReporting";
import type { SchuleReportingProps } from "~/components/schule/reporting/SSchuleReportingProps";

const SSchuleReporting = () => import("~/components/schule/reporting/SSchuleReporting.vue");

export class RouteSchuleReporting extends RouteNode<RouteDataSchuleReporting, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN], "schule.reporting", "reporting", SSchuleReporting, new RouteDataSchuleReporting());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Reporting";
		super.menugroup = RouteSchuleMenuGroup.REPORTING;
	}

	public getProps(to: RouteLocationNormalized): SchuleReportingProps {
		return {
			createReport: this.data.createReport,
			createHtmlPreview: this.data.createHtmlPreview,
		};
	}
}

export const routeSchuleReporting = new RouteSchuleReporting();
