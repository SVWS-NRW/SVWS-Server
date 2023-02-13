<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table v-model:clicked="selected" clickable :columns="cols" :items="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { KlassenListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { routeKlassen } from "~/router/apps/RouteKlassen";
	import { DataTableColumn } from "@svws-nrw/svws-ui";

	const props = defineProps<{
		item: ShallowRef<KlassenListeEintrag | undefined>;
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();
	const selected = routeKlassen.auswahl;

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	];

	const rows: ComputedRef<KlassenListeEintrag[]> = computed(() => routeKlassen.liste.liste);
</script>
