<template>
	<div v-if="visible" class="flex h-full flex-row" >
		<div class="flex w-full flex-col">
			<svws-ui-router-vertical-tab-bar :routes="RouteGostKlausurplanungChildren" :hidden="routeAppAreHidden(RouteGostKlausurplanungChildren)" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-vertical-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onMounted, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRoute, useRouter } from "vue-router";
	import { injectMainApp } from "~/apps/Main";
	import { RouteGostKlausurplanung, routeGostKlausurplanungSetRedirect, RouteGostKlausurplanungChildren } from "~/router/apps/gost/RouteGostKlausurplanung";
	import { routeAppMeta, routeAppAreHidden } from "~/router/RouteUtils";

	const app = injectMainApp().apps.gost;
	
	const props = defineProps<{ id?: number; item?: GostJahrgang, routename: string }>();

	// Initialisiere die Sub-Routen
	const router = useRouter();
	const route = useRoute();
	const redirect = routeAppMeta(RouteGostKlausurplanung).redirect;
	routeGostKlausurplanungSetRedirect(route);
	
	onMounted(() => {
		if (((route.params.id === undefined) || (route.params.id === "")) && (app.auswahl.liste.length > 0))
			router.push({ name: redirect.value.name?.toString(), params: { abiturjahr: app.auswahl.liste[0].abiturjahr } });
	});

	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return redirect.value;
		},
		set(value: RouteRecordRaw) {
			redirect.value = value;
			router.push({ name: value.name, params: { abiturjahr: props.id } });
		}
	});


	const visible: ComputedRef<boolean> = computed(() => {
		//TODO: richtige Bedingung einpflegen
		return true;
	});

</script>
