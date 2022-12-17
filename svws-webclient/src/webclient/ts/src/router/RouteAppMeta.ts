import { WritableComputedRef } from "vue";
import { RouteMeta, RouteRecordRaw } from "vue-router";

export interface RouteAppMeta<T> extends RouteMeta {
	auswahl: () => WritableComputedRef<T>
}

function routeAppMeta<T extends RouteRecordRaw, Item>(route : T) : RouteAppMeta<Item> {
	if (route.meta === undefined)
		throw new Error("Meta-Informationen für die Route '" + route.name?.toString() + "' nicht definiert.");
	return route.meta as RouteAppMeta<Item>;
}

export function routeAppAuswahl<T extends RouteRecordRaw, Item>(route : T) : WritableComputedRef<Item> {
	return routeAppMeta<T, Item>(route).auswahl();
}

