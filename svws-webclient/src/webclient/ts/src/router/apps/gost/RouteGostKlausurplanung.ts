import { RouteRecordRaw, useRouter } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { routeGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { computed, WritableComputedRef } from "vue";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");

export class RouteGostKlausurplanung extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super("gost_klausurplanung", "klausurplanung", SGostKlausurplanung);
		super.propHandler = (route) => routeGost.getProps(route);
		super.text = "Klausurplanung";
		this.isHidden = () => {
			return (routeGost.data.item.value === undefined) || (routeGost.data.item.value.abiturjahr === -1);
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
