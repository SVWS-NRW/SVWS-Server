<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header>
			<div>
				<div class="mt-6 mb-2 flex gap-2">
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel"><i-ri-search-line /></svws-ui-text-input>
					<svws-ui-toggle v-model="sichtbar">Sichtbar</svws-ui-toggle>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="gotoLehrer" v-model="selectedItems" :items="rowsFiltered.values()"
					:columns="cols" clickable selectable :footer="true" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { LehrerAuswahlProps } from "./SLehrerAuswahlProps";

	const props = defineProps<LehrerAuswahlProps>();

	const sichtbar: Ref<boolean> = ref(true);

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

	const rowsFiltered: ComputedRef<Map<number, LehrerListeEintrag>> = computed(() => {
		if (!search.value)
			return props.mapLehrer;
		const result = new Map<number, LehrerListeEintrag>();
		for (const l of props.mapLehrer.values()) {
			if (l.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
				l.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) && l.istSichtbar === sichtbar.value)
				result.set(l.id, l);
		}
		return result;
	});

	function onAction(action: string, item: LehrerListeEintrag) {
		console.log(action, item);
	}

</script>


<style lang="postcss">

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
