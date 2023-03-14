import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuleDatenaustausch, routeSchuleDatenaustausch } from "../schule/RouteSchuleDatenaustausch";
import { SchuleDatenaustauschKurs42Props } from "~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42Props";

const SSchuleDatenaustauschKurs42 = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42.vue");

export class RouteSchuleDatenaustauschKurs42 extends RouteNode<unknown, RouteSchuleDatenaustausch> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.kurs42.daten", "schule/datenaustausch/kurs42", SSchuleDatenaustauschKurs42);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurs42";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschKurs42Props {
		return {
			setGostKurs42ImportZip: routeSchuleDatenaustausch.data.setGostKurs42ImportZip,
		};
	}
}

export const routeSchuleDatenaustauschKurs42 = new RouteSchuleDatenaustauschKurs42();

