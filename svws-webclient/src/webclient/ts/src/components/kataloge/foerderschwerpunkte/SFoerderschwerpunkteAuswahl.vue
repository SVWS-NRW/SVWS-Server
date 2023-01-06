<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Förderschwerpunktauswahl</span>
			</nav>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef } from "vue";
	import { router } from "~/router";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const props = defineProps<{ id?: number; item?: FoerderschwerpunktEintrag, routename: string }>();
	const selected = routeKatalogFoerderschwerpunkte.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kuerzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];

	const rows: ComputedRef<FoerderschwerpunktEintrag[]> =
	 	computed(() => routeKatalogFoerderschwerpunkte.data.auswahl.liste || []);
</script>
