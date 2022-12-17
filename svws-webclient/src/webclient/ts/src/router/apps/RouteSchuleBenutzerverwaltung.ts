import { BenutzergruppeListeEintrag, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "../RouteAppMeta";

export const RouteSchuleBenutzerverwaltungBenutzer : RouteRecordRaw = {
	name: "benutzer",
	path: "/schule/benutzerverwaltung/benutzer/:id(\\d+)?",
	components: {
	},
	props: {
		default: getRouteSchuleBenutzerverwaltungBenutzerProps,
		liste: getRouteSchuleBenutzerverwaltungBenutzerProps
	},
	meta: <RouteAppMeta<BenutzerListeEintrag  | undefined>> {
		auswahl: routeBenutzerverwaltungBenutzerAuswahl
	}
}


function getRouteSchuleBenutzerverwaltungBenutzerProps(route: RouteLocationNormalized) {
	if ((route.name !== "benutzer") || (route.params.id === undefined))
		return { id: undefined, item: undefined };
	const id = parseInt(route.params.id as string);
	const app = injectMainApp().apps.benutzer;
	const item = app.auswahl.liste.find(s => s.id === id);
	return { id: id, item: item };
}


function routeBenutzerverwaltungBenutzerAuswahl(): WritableComputedRef<BenutzerListeEintrag | undefined> {
	const router = useRouter();
	const route = useRoute();
	const app = injectMainApp().apps.benutzer;

	const selected = computed({
		get(): BenutzerListeEintrag | undefined {
			if (route.params.id === undefined)
				return undefined;
			return app.auswahl.liste.find(s => s.id.toString() === route.params.id);
		},
		set(value: BenutzerListeEintrag | undefined) {
			app.auswahl.ausgewaehlt = value;
			router.push({ name: "benutzer", params: { id: value?.id } });
		}
	});
	return selected;
}


export const RouteSchuleBenutzerverwaltungBenutzergruppe : RouteRecordRaw = {
	name: "benutzergruppe",
	path: "/schule/benutzerverwaltung/benutzergruppe/:id(\\d+)?",
	components: {
	},
	props: {
		default: getRouteSchuleBenutzerverwaltungBenutzergruppeProps,
		liste: getRouteSchuleBenutzerverwaltungBenutzergruppeProps
	},
	meta: <RouteAppMeta<BenutzergruppeListeEintrag  | undefined>> {
		auswahl: routeBenutzerverwaltungBenutzergruppenAuswahl
	}
}


function getRouteSchuleBenutzerverwaltungBenutzergruppeProps(route: RouteLocationNormalized) {
	if ((route.name !== "benutzergruppe") || (route.params.id === undefined))
		return { id: undefined, item: undefined };
	const id = parseInt(route.params.id as string);
	const app = injectMainApp().apps.benutzergruppe;
	const item = app.auswahl.liste.find(s => s.id === id);
	return { id: id, item: item };
}




function routeBenutzerverwaltungBenutzergruppenAuswahl(): WritableComputedRef<BenutzergruppeListeEintrag | undefined> {
	const router = useRouter();
	const route = useRoute();
	const app = injectMainApp().apps.benutzergruppe;

	const selected = computed({
		get(): BenutzergruppeListeEintrag | undefined {
			if (route.params.id === undefined)
				return undefined;
			return app.auswahl.liste.find(s => s.id.toString() === route.params.id);
		},
		set(value: BenutzergruppeListeEintrag | undefined) {
			app.auswahl.ausgewaehlt = value;
			router.push({ name: "benutzergruppe", params: { id: value?.id } });
		}
	});
	return selected;
}


export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: "benutzerverwaltung",
	path: "/schule/benutzerverwaltung",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	children: [ RouteSchuleBenutzerverwaltungBenutzer, RouteSchuleBenutzerverwaltungBenutzergruppe ]
}
