import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ShallowRef, shallowRef } from "vue";
import { routeGostKursplanungHalbjahr } from "./kursplanung/RouteGostKursplanungHalbjahr";
import { routeApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";

export class RouteDataGostKursplanung  {
	halbjahr: ShallowRef<GostHalbjahr> = shallowRef(GostHalbjahr.EF1);

	setHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKursplanungHalbjahr.getRoute(routeGost.data.item.value!.abiturjahr, value.id, undefined));
	}
}

const SGostKursplanungEmpty = () => import("~/components/gost/kursplanung/SGostKursplanungEmpty.vue");
const SGostKursplanungAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungAuswahl.vue");

export class RouteGostKursplanung extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super("gost.kursplanung", "kursplanung/:halbjahr([0-5])?", SGostKursplanungEmpty, new RouteDataGostKursplanung());
		super.propHandler = (route) => this.getNoProps(route);
		super.setView("gost_child_auswahl", SGostKursplanungAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Kursplanung";
		super.children = [
			routeGostKursplanungHalbjahr
		];
		super.defaultChild = routeGostKursplanungHalbjahr;
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		const abiturjahr = params?.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr as string);
		if ((abiturjahr === undefined) || (routeGost.data.item.value !== undefined) && (abiturjahr !== routeGost.data.item.value.abiturjahr))
			return this.getRoute(-1);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		// Prüfe das Abiturjahr
		if (to_params.abiturjahr instanceof Array || to_params.halbjahr instanceof Array)
			throw new Error("Fehler: Die Parameter Abiturjahr und Halbjahr dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined)
			throw new Error("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
		// Aktualisiere das Halbjahr
		const halbjahr = (to_params.halbjahr === undefined) ? undefined : GostHalbjahr.fromID(parseInt(to_params.halbjahr)) || undefined;
		if (halbjahr === undefined) {
			let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt);
			if (hj === null) // In zwei Fällen existiert kein Planungshalbjahr, z.B. weil der Abiturjahrgang (fast) abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
			this.data.halbjahr.value = hj;
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, hj.id, undefined);
		}
		this.data.halbjahr.value = halbjahr;
		if (to.name === this.name)
			return routeGostKursplanungHalbjahr.getRoute(abiturjahr, halbjahr.id, undefined);
	}

	public getRoute(abiturjahr: number, halbjahr?: number) : RouteLocationRaw {
		if (halbjahr === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			setHalbjahr: this.data.setHalbjahr,
			jahrgangsdaten: routeGost.data.jahrgangsdaten.value,
			halbjahr: this.data.halbjahr.value
		}
	}

}

export const routeGostKursplanung = new RouteGostKursplanung();
