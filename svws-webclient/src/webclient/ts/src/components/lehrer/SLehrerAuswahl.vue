<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" />
		</template>
		<template #header>
			<div>
				<div class="mb-2">
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel"><i-ri-search-line /></svws-ui-text-input>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-data-table v-model:clicked="selected" v-model="selectedItems" :items="rowsFiltered" :columns="cols" clickable selectable :footer="true" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag, List, Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, ShallowRef } from "vue";
	import { routeLehrer } from "~/router/apps/RouteLehrer";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc" },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 }
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");
	const selectedItems = ref([]);

	const props = defineProps<{
		item: ShallowRef<LehrerListeEintrag | undefined>;
		abschnitte: List<Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	}>();

	const rows: ComputedRef<LehrerListeEintrag[] | undefined> = computed(() => routeLehrer.liste.liste);

	const rowsFiltered: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		if (rows.value === undefined)
			return [];
		if (search.value) {
			return rows.value.filter(
				(e: LehrerListeEintrag) =>
					e.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
					e.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
			);
		}
		return rows.value;
	});

	const selected = routeLehrer.auswahl;

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}
</script>


<style>
	.action-button {
		@apply h-6 w-6;
	}

	.action-items {
		@apply bg-white;
		@apply flex flex-col;
		@apply px-2 py-1;
		@apply ring-1;
		@apply ring-black ring-opacity-5;
		@apply rounded-md;
		@apply shadow-lg;
		@apply w-48;
	}
</style>
