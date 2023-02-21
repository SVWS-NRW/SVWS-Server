<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4">
					<svws-ui-avatar :src="'data:image/png;base64, ' + stammdaten.foto" :alt="stammdaten.foto ? 'Foto ' + stammdaten.vorname + ' ' + stammdaten.nachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3">{{ stammdaten.titel }} {{ stammdaten.vorname }} {{ stammdaten.nachname }}</span>
					<svws-ui-badge type="light" title="ID">
						<i-ri-fingerprint-line />
						{{ stammdaten.id }}
					</svws-ui-badge>
					<br>
					<span class="opacity-50">{{ stammdaten.kuerzel }}</span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeLehrer.children_records" :hidden="children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { routeLehrer } from "~/router/apps/RouteLehrer";
	import { LehrerAppProps } from "./SLehrerAppProps";

	const props = defineProps<LehrerAppProps>();

	const selectedRoute = routeLehrer.childRouteSelector;
	const children_hidden = routeLehrer.children_hidden();

	const visible: ComputedRef<boolean> = computed(() => {
		return !routeLehrer.hidden();
	});

</script>
