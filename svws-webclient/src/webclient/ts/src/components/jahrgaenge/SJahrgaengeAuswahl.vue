<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Jahrgänge</span>
			</nav>
		</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" :item-sort="item_sort" :item-text="item_text"></svws-ui-multi-select>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
	import {Schuljahresabschnitt} from "@svws-nrw/svws-core-ts";
	import {Schule} from "~/apps/schule/Schule";

	const props = defineProps<{ id?: number; item?: JahrgangsListeEintrag, routename: string }>();

	const selected = routeKatalogJahrgaenge.auswahl;

	const cols = [
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const main: Main = injectMainApp();
	const app = main.apps.jahrgaenge;

	const rows: ComputedRef<JahrgangsListeEintrag[] | undefined> = computed(() => { 
		return app.auswahl.liste; 
	});

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
