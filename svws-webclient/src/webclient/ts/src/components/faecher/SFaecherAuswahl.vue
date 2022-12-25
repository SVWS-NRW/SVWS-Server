<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Fächer</span>
			</nav>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
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

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";
	import {Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import {Schule} from "~/apps/schule/Schule";

	const props = defineProps<{ id?: number; item?: FaecherListeEintrag, routename: string }>();
	
	const selected = routeKatalogFaecher.auswahl;

	const main: Main = injectMainApp();
	const app = main.apps.faecher;

	const cols = ref([
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	]);

	const rows: ComputedRef<FaecherListeEintrag[] | undefined> = computed(() => app.auswahl.liste);

	const schule_abschnitte: ComputedRef<
		Array<Schuljahresabschnitt> | undefined
	> = computed(() => {
		const liste = appSchule.value.schuleStammdaten.daten?.abschnitte;
		return liste?.toArray(new Array<Schuljahresabschnitt>()) || [];
	});

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get(): Schuljahresabschnitt {
			return main.config.akt_abschnitt;
		},
		set(abschnitt: Schuljahresabschnitt) {
			main.config.akt_abschnitt = abschnitt;
		}
	});

	const appSchule: ComputedRef<Schule> = computed(() => {
		return main.apps.schule;
	});
	function item_sort(a: Schuljahresabschnitt, b: Schuljahresabschnitt) {
		return (
			b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1)
		);
	}

	function item_text(item: Schuljahresabschnitt) {
		return item.schuljahr
			? `${item.schuljahr}, ${item.abschnitt}. HJ`
			: "Abschnitt";
	}
</script>
