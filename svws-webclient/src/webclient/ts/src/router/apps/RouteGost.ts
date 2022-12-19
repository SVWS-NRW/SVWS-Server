import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";

const ROUTE_NAME: string = "gost";

export const RouteGost : RouteRecordRaw = {
	name: "gost",
	path: "/gost/:abiturjahr(-?\\d+)?",
	components: {
		default: () => import("~/components/gost/SGostApp.vue"),
		liste: () => import("~/components/gost/SGostAuswahl.vue")
	},
	props: {
		default: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl),
		liste: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl)
	},
	meta: <RouteAppMeta<GostJahrgang | undefined>> {
		auswahl: () => routeGostAuswahl(ROUTE_NAME, injectMainApp().apps.gost.auswahl)
	}
}


/**
 * Eine Methode zum Erzeugen der Properties "abiturjahr" und "item" zu einer Route bei 
 * der Gost-Auswahl-Liste, welche ein numerisches abiturjahr-Attribut hat.
 * 
 * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns das Objekt mit den Werten für die Properties
 */
function routePropsGostAuswahl<TAuswahl extends BaseList<{ abiturjahr: number }, unknown>>(route: RouteLocationNormalized, auswahl: TAuswahl) {
	if ((auswahl === undefined) || (route.params.abiturjahr === undefined))
		return { id: undefined, item: undefined };
	const abiturjahr = parseInt(route.params.abiturjahr as string);
	const item = auswahl.liste.find(s => s.abiturjahr === abiturjahr);
	return { id: abiturjahr, item: item };
}


/**
 * Eine Methode für Routen-Einträge zum Erzeugen der Computed-Property bei der
 * Gost-Auswahl einer Route eines Routen-Eintrags.
 * 
 * @param name      der Name des Routen-Eintrags, so dass dieser beim Setzen einer Route aktualisiert werden kann
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns die Computed-Property
 */
function routeGostAuswahl<TItem extends { abiturjahr: number }, TAuswahl extends BaseList<TItem, unknown>>(name: string, auswahl: TAuswahl) : WritableComputedRef<TItem | undefined> {
	const router = useRouter();
	const route = useRoute();

	const selected = computed({
		get(): TItem | undefined {
			if (route.params.abiturjahr === undefined)
				return undefined;
			// if ((auswahl.ausgewaehlt === undefined) || (auswahl.ausgewaehlt.abiturjahr.toString() !== route.params.abiturjahr))
			// 	auswahl.ausgewaehlt = auswahl.liste.find(s => s.abiturjahr.toString() === route.params.abiturjahr);
			return auswahl.ausgewaehlt;
		},
		set(value: TItem | undefined) {
			auswahl.ausgewaehlt = value;
			router.push({ name: name, params: { abiturjahr: value?.abiturjahr } });
		}
	});
	return selected;
}
