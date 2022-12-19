<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal">
				<span> {{ anzeigename }} </span>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="RoutesBenutzerverwaltungChildren" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRoute, useRouter } from "vue-router";

	import { injectMainApp, Main } from "~/apps/Main";
	import { RoutesBenutzerverwaltungChildren } from "~/router/apps/RouteSchuleBenutzerverwaltung"
	import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
	import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

	const main: Main = injectMainApp();

	const route = useRoute();
	const router = useRouter();

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag | BenutzergruppeListeEintrag }>();

	const isRouteBenutzer: ComputedRef<boolean> = computed(() => {
 		return route.name?.toString() === "benutzer";
	});

	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return isRouteBenutzer.value ? RouteSchuleBenutzerverwaltungBenutzer : RouteSchuleBenutzerverwaltungBenutzergruppe;
		},
		set(value: RouteRecordRaw) {
			router.push({ name: value.name, params: { id: props.id } });
		}
	});

	const anzeigename: ComputedRef<string> = computed(() => {
		if (props.item === undefined)
			return "";
		return isRouteBenutzer.value
			? ((props.item as BenutzerListeEintrag).anzeigename.toString() || "")
			: ((props.item as BenutzergruppeListeEintrag).bezeichnung.toString() || "");
	});

	const inputId: ComputedRef<string> = computed(() => {
		return "ID: " + props.id;
	});

</script>
