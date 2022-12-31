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
	import { computed, ComputedRef, ref } from "vue";
	import { router } from "~/router";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const props = defineProps<{ id?: number; item?: FoerderschwerpunktEintrag, routename: string }>();
	const selected = routeKatalogFoerderschwerpunkte.auswahl;

	const cols = ref([
		{ key: "kuerzel", label: "Kuerzel", width: "6em", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	]);

	const rows: ComputedRef<FoerderschwerpunktEintrag[] | undefined> = computed(() => {
		return routeKatalogFoerderschwerpunkte.data.auswahl.liste;
	});

</script>
