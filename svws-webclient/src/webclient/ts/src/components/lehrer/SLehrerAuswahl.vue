<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Lehrkräfte</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" @update:clicked="gotoLehrer" v-model="selectedItems" :items="rowsFiltered.values()"
					:columns="cols" clickable selectable :footer="true" filter :filter-open="false">
					<template #search>
						<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel"><i-ri-search-line /></svws-ui-text-input>
					</template>
					<template #filter>
						<svws-ui-multi-select v-model="personaltyp" :items="PersonalTyp.values()" :item-text="p => p.bezeichnung" title="Personaltyp" removable />
						<svws-ui-toggle v-model="sichtbar">Sichtbar</svws-ui-toggle>
						<svws-ui-toggle v-model="statistikrelevant">Statistikrelevant</svws-ui-toggle>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { LehrerAuswahlProps } from "./SLehrerAuswahlProps";

	const props = defineProps<LehrerAuswahlProps>();

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
	const sichtbar: Ref<boolean> = ref(true);
	const statistikrelevant: Ref<boolean> = ref(true);
	const personaltyp: Ref<PersonalTyp | undefined> = ref(undefined);

	const rowsFiltered: ComputedRef<Map<number, LehrerListeEintrag>> = computed(() => {
		const result = new Map<number, LehrerListeEintrag>();
		for (const l of props.mapLehrer.values()) {
			let pt = true;
			console.log(l.personTyp, personaltyp.value?.kuerzel, l.personTyp === personaltyp.value?.kuerzel)
			if (personaltyp.value)
				pt = personaltyp.value.kuerzel === l.personTyp;
			if ((l.nachname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| l.vorname.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())
				|| l.kuerzel.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				&& l.istSichtbar === sichtbar.value
				&& l.istRelevantFuerStatistik === statistikrelevant.value
				&& pt)
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
