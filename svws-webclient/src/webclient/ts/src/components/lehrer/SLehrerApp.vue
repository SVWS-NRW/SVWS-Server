<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + inputVorname + ' ' + inputNachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3">{{ inputTitel }} {{ inputVorname }} {{ inputNachname }}</span>
					<svws-ui-badge type="light" title="ID">
						<i-ri-fingerprint-line/>
						{{ item.value?.id }}
					</svws-ui-badge>
					<br>
					<span class="opacity-50">{{ inputKuerzel }}</span>
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

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeLehrer } from "~/router/apps/RouteLehrer";

	const props = defineProps<{
		item: ShallowRef<LehrerListeEintrag | undefined>;
		stammdaten: DataLehrerStammdaten;
		schule: DataSchuleStammdaten;
	}>();

	const selectedRoute = routeLehrer.childRouteSelector;
	const children_hidden = routeLehrer.children_hidden();

	const foto: ComputedRef<String | undefined> = computed(() => props.stammdaten.daten?.foto ?? undefined);

	const inputNachname: ComputedRef<string | undefined> = computed(() => props.item.value?.nachname);
	const inputVorname: ComputedRef<string | undefined> = computed(() => props.item.value?.vorname);
	const inputKuerzel: ComputedRef<string | undefined> = computed(() => props.item.value?.kuerzel);
	const inputTitel: ComputedRef<string | undefined> = computed(() => props.item.value?.titel ?? undefined);

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.item.value !== undefined) && (!routeLehrer.hidden());
	});

</script>
