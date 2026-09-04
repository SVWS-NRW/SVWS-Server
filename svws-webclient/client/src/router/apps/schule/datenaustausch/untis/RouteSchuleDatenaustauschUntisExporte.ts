import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";
import type { SchuleDatenaustauschUntisExporteProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisExporteProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SSchuleDatenaustauschUntisExporte = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisExporte.vue");

export class RouteSchuleDatenaustauschUntisExporte extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.exporte", "exporte", SSchuleDatenaustauschUntisExporte);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Exporte";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisExporteProps {
		return {
			exportUntisKlassenGPU003: routeSchuleDatenaustauschUntis.data.exportUntisKlassenGPU003,
			exportUntisLehrerGPU004: routeSchuleDatenaustauschUntis.data.exportUntisLehrerGPU004,
			exportUntisFaecherGPU006: routeSchuleDatenaustauschUntis.data.exportUntisFaecherGPU006,
			exportUntisSchuelerGPU010: routeSchuleDatenaustauschUntis.data.exportUntisSchuelerGPU010,
			exportUntisFachwahlenGPU015: routeSchuleDatenaustauschUntis.data.exportUntisFachwahlenGPU015,
			exportUntisKlausurenGPU017: routeSchuleDatenaustauschUntis.data.exportUntisKlausurenGPU017,
			exportUntisSchienenGPU019: routeSchuleDatenaustauschUntis.data.exportUntisSchienenGPU019,
			ladeBlockungslisten: routeSchuleDatenaustauschUntis.data.ladeBlockungslisten,
			exportUntisBlockung: routeSchuleDatenaustauschUntis.data.exportUntisBlockung,
		};
	}

}

export const routeSchuleDatenaustauschUntisExporte = new RouteSchuleDatenaustauschUntisExporte();
