import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams,  RouteParamValue,  useRouter } from "vue-router";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { routeGostKursplanungHalbjahr } from "./kursplanung/RouteGostKursplanungHalbjahr";
import { App } from "~/apps/BaseApp";

export class RouteDataGostKursplanung  {
	halbjahr: ShallowRef<GostHalbjahr> = shallowRef(GostHalbjahr.EF1);
}

const SGostKursplanungEmpty = () => import("~/components/gost/kursplanung/SGostKursplanungEmpty.vue");
const SGostKursplanungAuswahl = () => import("~/components/gost/kursplanung/SGostKursplanungAuswahl.vue");

export class RouteGostKursplanung extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super("gost_kursplanung", "kursplanung/:halbjahr([0-5])?", SGostKursplanungEmpty, new RouteDataGostKursplanung());
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
			let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, App.akt_abschnitt.schuljahr, App.akt_abschnitt.abschnitt);
			if (hj === null) // In zwei Fällen existiert kein Planungshalbjahr, z.B. weil der Abiturjahrgang (fast) abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < App.akt_abschnitt.schuljahr + App.akt_abschnitt.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
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
			jahrgangsdaten: routeGost.data.jahrgangsdaten,
			halbjahr: this.data.halbjahr.value
		}
	}

	public getSelector() : WritableComputedRef<GostHalbjahr> {
		const router = useRouter();
		return computed({
			get: () => {
				return this.data.halbjahr.value;
			},
			set: (value) => {
				this.data.halbjahr.value = value;
				const id_value = "" + value.id;
				const params = { abiturjahr: routeGost.data.item.value!.abiturjahr, halbjahr: id_value };
				void router.push({ name: this.defaultChild!.name, params: params });
			}
		});
	}

}

export const routeGostKursplanung = new RouteGostKursplanung();
