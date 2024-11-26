import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";

import type { SchuleDatenaustauschUntisExporteProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisExporteProps";

const SSchuleDatenaustauschUntisExporte = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisExporte.vue");

export class RouteSchuleDatenaustauschUntisExporte extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.exporte", "exporte", SSchuleDatenaustauschUntisExporte);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Weitere Exporte";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisExporteProps {
		return {
			exportUntisKlassenGPU003: routeSchuleDatenaustauschUntis.data.exportUntisKlassenGPU003,
			exportUntisLehrerGPU004: routeSchuleDatenaustauschUntis.data.exportUntisLehrerGPU004,
			exportUntisFaecherGPU006: routeSchuleDatenaustauschUntis.data.exportUntisFaecherGPU006,
			exportUntisSchuelerGPU010: routeSchuleDatenaustauschUntis.data.exportUntisSchuelerGPU010,
			exportUntisKlausurenGPU017: routeSchuleDatenaustauschUntis.data.exportUntisKlausurenGPU017,
		};
	}

}

export const routeSchuleDatenaustauschUntisExporte = new RouteSchuleDatenaustauschUntisExporte();

