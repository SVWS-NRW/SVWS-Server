import { useRoute, useRouter, Router, RouteLocationNormalizedLoaded } from 'vue-router'
// import slugify from "slugify";

import type { RouteItemTypesMap } from "./types";

/**
 * Mapt Routen-Namen zu Arrays mit properties, aus denen für die Route ein Slug gebaut werden kann
 */
const slugPropertiesMap = {
	schueler: ["nachname", "vorname"],
	lehrer: ["nachname", "vorname", "kuerzel"],
	klassen: ["kuerzel"],
	kurse: ["kuerzel"],
	jahrgaenge: ["kuerzel"],
	// gost: ['kuerzel'],
	faecher: ["kuerzel"]
} as const;

type RouteNames = keyof typeof slugPropertiesMap;

export function useRouterHelpers(): {
	route: RouteLocationNormalizedLoaded;
	router: Router;
	navigateTo: <T extends RouteNames>(
		routeName: T,
		item: RouteItemTypesMap[T]
	) => void;
} {
	const route = useRoute();
	const router = useRouter();

	/**
	 * router.push zur Route eines spezifischen items.
	 * Erzeugt aus dem Item neben der ID auch einen Sluf für eine besser lesbare URL
	 *
	 * @param name Name der App-Route, zu der navigiert werden soll
	 * @param item Item zu dem navigiert werden soll.
	 */
	function navigateTo<T extends RouteNames>(
		routeName: T,
		item: RouteItemTypesMap[T]
	) {
		// const slugProps = slugPropertiesMap[routeName];

		router.push({
			name: routeName,
			params: {
				id: item.id,
				// slug: slugify(slugProps.map(p => item[p] ?? "").join(" "), {
				// 	locale: "de"
				// })
			}
		});
	}

	return {
		route,
		router,
		navigateTo
	};
}
