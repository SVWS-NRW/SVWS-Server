import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";

import { RouteSchuleMenuGroup } from "../RouteSchuleMenuGroup";
import { RouteDataSchuleReporting } from "./RouteDataSchuleReporting";
import type { SchuleReportingProps } from "~/components/schule/reporting/SSchuleReportingProps";
import { api } from "~/router/Api";

const SSchuleReporting = () => import("~/components/schule/reporting/SSchuleReporting.vue")

export class RouteSchuleReporting extends RouteNode<RouteDataSchuleReporting, RouteApp>{

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.reporting", "reporting", SSchuleReporting, new RouteDataSchuleReporting());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Reporting";
		super.menugroup = RouteSchuleMenuGroup.REPORTING;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		// if (isEntering)
		// 	await routeSchuleReporting.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuleReportingProps {
		return {
			createReport: this.data.createReport,
			idAbschnitt: api.abschnitt.id,
		};
	}
}

export const routeSchuleReporting = new RouteSchuleReporting();
