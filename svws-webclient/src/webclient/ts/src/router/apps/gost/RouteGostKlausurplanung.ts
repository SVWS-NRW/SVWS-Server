import { RouteRecordRaw, useRouter } from "vue-router";
import { mainApp } from "~/apps/Main";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, RouteGost } from "~/router/apps/RouteGost";
import { routeGostKlausurplanungKlausurdaten } from "./klausurplanung/RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungSchienen } from "./klausurplanung/RouteGostKlausurplanungSchienen";
import { routeGostKlausurplanungKalender } from "./klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungPlanung } from "./klausurplanung/RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungKonflikte } from "./klausurplanung/RouteGostKlausurplanungKonflikte";
import { computed, WritableComputedRef } from "vue";


const SGostKlausurplanung = () => import("~/components/gost/klausurplanung/SGostKlausurplanung.vue");

export class RouteGostKlausurplanung extends RouteNode<unknown> {

	public constructor() {
		super("gost_klausurplanung", "klausurplanung", SGostKlausurplanung);
		super.propHandler = (route) => RouteGost.getPropsByAuswahlAbiturjahr(route, mainApp.apps.gost.auswahl);
		super.text = "Klausurplanung";
		this.isHidden = () => {
			return (routeGost.data.item === undefined) || (routeGost.data.item.abiturjahr === -1);
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
				const abiturjahr = (routeGost.data.item === undefined) ? undefined : "" + routeGost.data.item.abiturjahr;
                router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
            }
        });
        return selectedRoute;
    }

}

export const routeGostKlausurplanung = new RouteGostKlausurplanung();
