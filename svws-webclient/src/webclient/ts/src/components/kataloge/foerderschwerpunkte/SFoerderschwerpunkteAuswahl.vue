<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Kataloge" @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span title="Förderschwerpunkte">Förderschwerpunkte</span>
			</nav>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text" />
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

	import {FoerderschwerpunktEintrag, Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import {computed, ComputedRef, ShallowRef, WritableComputedRef} from "vue";
	import { router } from "~/router";
	import { routeKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";
	import {injectMainApp, Main} from "~/apps/Main";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

	const main: Main = injectMainApp();

	const props = defineProps<{
		item: ShallowRef<FoerderschwerpunktEintrag | undefined>;
		schule: DataSchuleStammdaten;
	}>();

	const selected = routeKatalogFoerderschwerpunkte.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kuerzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Bezeichnung", sortable: true }
	];

	const rows: ComputedRef<FoerderschwerpunktEintrag[]> = computed(() => routeKatalogFoerderschwerpunkte.liste.liste || []);

	const schule_abschnitte: ComputedRef<Array<Schuljahresabschnitt> | undefined> = computed(() => {
		const liste = props.schule.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (abschnitt) => main.config.akt_abschnitt = abschnitt
	});

	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr
			? `${item.schuljahr}, ${item.abschnitt}. HJ`
			: "Abschnitt";
	}

</script>
