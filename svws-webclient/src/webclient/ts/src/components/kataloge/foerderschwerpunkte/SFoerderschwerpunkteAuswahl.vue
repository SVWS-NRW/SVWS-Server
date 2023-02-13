<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Kataloge" @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span title="Förderschwerpunkte">Förderschwerpunkte</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import {FoerderschwerpunktEintrag, List, Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import {computed, ComputedRef, ShallowRef} from "vue";
	import { router } from "~/router";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";

	const props = defineProps<{
		item: ShallowRef<FoerderschwerpunktEintrag | undefined>;
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	const selected = routeKatalogFoerderschwerpunkte.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kuerzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];

	const rows: ComputedRef<FoerderschwerpunktEintrag[]> = computed(() => routeKatalogFoerderschwerpunkte.liste.liste || []);
</script>
