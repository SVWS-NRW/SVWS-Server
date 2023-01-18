<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Fächer</span>
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

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef, WritableComputedRef } from "vue";
	import { router } from "~/router"
	import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
	import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		item: ShallowRef<FaecherListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
	}>();

	const selected = routeKatalogFaecher.auswahl;

	const cols: DataTableColumn[]= [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const main: Main = injectMainApp();

	const rows: ComputedRef<FaecherListeEintrag[]> = computed(() => routeKatalogFaecher.liste.liste || []);

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
		return item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";
	}

</script>
