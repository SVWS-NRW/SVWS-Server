<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Fächer</span>
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

	import { FaecherListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { router } from "~/router/RouteManager";
	import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		item: ShallowRef<FaecherListeEintrag | undefined>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	const selected = routeKatalogFaecher.auswahl;

	const cols: DataTableColumn[]= [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const rows: ComputedRef<FaecherListeEintrag[]> = computed(() => routeKatalogFaecher.liste.liste || []);
</script>
