import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleDatenaustauschUntis, type RouteSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";
import type { SchuleDatenaustauschUntisImporteProps } from "~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisImporteProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SSchuleDatenaustauschUntisImporte = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntisImporte.vue");

export class RouteSchuleDatenaustauschUntisImporte extends RouteNode<any, RouteSchuleDatenaustauschUntis> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis.importe", "importe", SSchuleDatenaustauschUntisImporte);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Importe";
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschUntisImporteProps {
		return {
			importUntisStundenplanGPU001: routeSchuleDatenaustauschUntis.data.importUntisStundenplanGPU001,
			importUntisRaeumeGPU005: routeSchuleDatenaustauschUntis.data.importUntisRaeumeGPU005,
		};
	}

}

export const routeSchuleDatenaustauschUntisImporte = new RouteSchuleDatenaustauschUntisImporte();
