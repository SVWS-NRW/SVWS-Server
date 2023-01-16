import { RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { routeGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { computed, WritableComputedRef } from "vue";
import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { mainApp } from "~/apps/Main";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");

export class RouteGostKlausurplanung extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_klausurplanung", "klausurplanung", SGostKlausurplanung);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Klausurplanung";
		this.isHidden = (params: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
			routeGostKlausurplanungKlausurdaten,
			routeGostKlausurplanungSchienen,
			routeGostKlausurplanungKalender,
			routeGostKlausurplanungPlanung,
			routeGostKlausurplanungKonflikte
		];
		super.defaultChild = routeGostKlausurplanungKlausurdaten;
	}

	public checkHidden(params: RouteParams) {
		const abiturjahr = params.abiturjahr === undefined ? undefined : parseInt(params.abiturjahr as string);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

    public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to.name === this.name) {
			if (to_params.abiturjahr === undefined)
				return false;
			const jahrgang = routeGost.liste.liste.find(elem => elem.abiturjahr.toString() === to_params.abiturjahr);
			if (this.checkHidden(jahrgang))
				return { name: this.parent!.defaultChild!.name, params: { abiturjahr: routeGost.liste.liste.at(0)?.abiturjahr }};
		}
        return true;
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
                router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
            }
        });
        return selectedRoute;
    }

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
