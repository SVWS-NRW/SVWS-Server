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

	import { Schuljahresabschnitt, KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, ShallowRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeKlassen } from "~/router/apps/RouteKlassen";
	import { ListLehrer } from "~/apps/lehrer/ListLehrer";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { DataTableColumn } from "@svws-nrw/svws-ui/dist/components/Layout/Table/types";

	const { schule } = defineProps<{ 
		item: ShallowRef<KlassenListeEintrag | undefined>;
		schule: DataSchuleStammdaten;
		listLehrer: ListLehrer;
		mapLehrer: Map<Number, LehrerListeEintrag>;
	}>();
	const selected = routeKlassen.auswahl;

	const main: Main = injectMainApp();

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const rows: ComputedRef<KlassenListeEintrag[]> = computed(() => routeKlassen.data.auswahl.liste);

	const schule_abschnitte: ComputedRef<Schuljahresabschnitt[] | undefined> = computed(() => 
		schule.daten?.abschnitte?.toArray() as Schuljahresabschnitt[] || []
	);

	const akt_abschnitt: WritableComputedRef<Schuljahresabschnitt> = computed({
		get: () => main.config.akt_abschnitt,
		set: (abschnitt) => main.config.akt_abschnitt = abschnitt
	});

	const item_sort = (a: Schuljahresabschnitt, b: Schuljahresabschnitt) => b.schuljahr + b.abschnitt * 0.1 - (a.schuljahr + a.abschnitt * 0.1);

	const item_text = (item: Schuljahresabschnitt) => item.schuljahr ? `${item.schuljahr}, ${item.abschnitt}. HJ` : "Abschnitt";

</script>
