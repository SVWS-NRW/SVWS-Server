<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<svws-ui-multi-select v-if="schule_abschnitte" v-model="akt_abschnitt" :items="schule_abschnitte" 
				:item-sort="item_sort" :item-text="item_text" />
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

	import { Schuljahresabschnitt, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeKlassen } from "~/router/apps/RouteKlassen";
	import { Schule } from "~/apps/schule/Schule";

	const props = defineProps<{ id?: number; item?: KlassenListeEintrag, routename: string }>();
	const selected = routeKlassen.auswahl;

	const main: Main = injectMainApp();
	const appSchule: ComputedRef<Schule> = computed(() => main.apps.schule);

	const cols = ref([
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	]);

	const rows: ComputedRef<KlassenListeEintrag[] | undefined> = computed(() => { return routeKlassen.data.auswahl.liste; });

	const schule_abschnitte: ComputedRef<Schuljahresabschnitt[] | undefined> = computed(() => 
		appSchule.value.schuleStammdaten.daten?.abschnitte?.toArray() as Schuljahresabschnitt[] || []
	);

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (abschnitt) => main.config.akt_abschnitt = abschnitt
	});

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);

	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";

</script>
