import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { routeGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { routeApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";

export class RouteDataGostKlausurplanung  {

	child: ShallowRef<RouteRecordRaw> = shallowRef(routeGostKlausurplanungKlausurdaten.record);
	halbjahr: ShallowRef<GostHalbjahr> = shallowRef(GostHalbjahr.EF1);

	setHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKlausurplanung.getRoute(routeGost.data.item.value!.abiturjahr, value.id));
	}

	setChild = async (value: RouteRecordRaw) => {
		if (value.name === this.child.value.name)
			return;
		const abiturjahr = (routeGost.data.item.value === undefined) ? undefined : "" + routeGost.data.item.value.abiturjahr;
		await RouteManager.doRoute({ name: value.name, params: { abiturjahr: abiturjahr, halbjahr: this.halbjahr.value.id } });
	}

}

const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");
const SGostKlausurplanungAuswahl = () => import("~/components/gost/klausurplanung/SGostKlausurplanungAuswahl.vue");

export class RouteGostKlausurplanung extends RouteNode<RouteDataGostKlausurplanung, RouteGost> {

	public constructor() {
		super("gost.klausurplanung", "klausurplanung/:halbjahr([0-5])?", SGostKlausurplanung, new RouteDataGostKlausurplanung());
		super.propHandler = (route) => this.getProps(route);
		super.setView("gost_child_auswahl", SGostKlausurplanungAuswahl, (route) => this.getAuswahlProps(route));
		super.text = "Klausurplanung";
		super.children = [
			routeGostKlausurplanungKlausurdaten,
			routeGostKlausurplanungSchienen,
			routeGostKlausurplanungKalender,
			routeGostKlausurplanungPlanung,
			routeGostKlausurplanungKonflikte
		];
		super.defaultChild = routeGostKlausurplanungKlausurdaten;
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
			return routeGost.defaultChild!.getRoute(-1);
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
			let hj = GostHalbjahr.getPlanungshalbjahrFromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, routeApp.data.aktAbschnitt.value.schuljahr, routeApp.data.aktAbschnitt.value.abschnitt); // TODO getAktuellesHalbjahr in GostHalbjahr ergänzen
			if (hj === null) // In zwei Fällen existiert kein Planungshalbjahr, z.B. weil der Abiturjahrgang (fast) abgeschlossen ist oder noch in der Sek I ist.
				hj = (abiturjahr < routeApp.data.aktAbschnitt.value.schuljahr + routeApp.data.aktAbschnitt.value.abschnitt) ? GostHalbjahr.Q22 : GostHalbjahr.EF1;
			this.data.halbjahr.value = hj;
			return this.getRoute(abiturjahr, hj.id);
		}
		this.data.halbjahr.value = halbjahr;
		if (to.name === this.name) {
			const child = this.selectedChild === undefined ? this.defaultChild! : this.selectedChild;
			return child.getRoute(abiturjahr, halbjahr.id);
		}
	}

	public getRoute(abiturjahr: number, halbjahr?: number) : RouteLocationRaw {
		if (halbjahr === undefined)
			return { name: this.name, params: { abiturjahr: abiturjahr }};
		return { name: this.name, params: { abiturjahr: abiturjahr, halbjahr: halbjahr }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			setHalbjahr: this.data.setHalbjahr,
			item: routeGost.data.item,
			schule: routeGost.data.schule,
			jahrgangsdaten: routeGost.data.jahrgangsdaten,
			dataFaecher: routeGost.data.dataFaecher,
			listJahrgaenge: routeGost.data.listJahrgaenge,
			halbjahr: this.data.halbjahr
		}
	}
	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			setChild: this.data.setChild,
			child: this.data.child.value,
			children: this.children_records,
			hidden: this.children_hidden().value
		}
	}

	/**
     * TODO
     *
     * @returns
     */
	public getChildRouteSelector() {
		const router = useRouter();
		const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				const abiturjahr = (routeGost.data.item.value === undefined) ? undefined : "" + routeGost.data.item.value.abiturjahr;
				void router.push({ name: value.name, params: { abiturjahr: abiturjahr, halbjahr: this.data.halbjahr.value.id } });
			}
		});
		return selectedRoute;
	}

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
