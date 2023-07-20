<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span>Betriebe</span>
			</nav>
		</template>
		<template #header />
		<template #content>
			<svws-ui-data-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="rowsFiltered"
				:columns="cols" clickable :footer="true" scroll-into-view selectable :model-value="selected"
				@update:model-value="selected=$event" :count="selected.length > 0">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Betrieb" />
				</template>
				<template #footerActions>
					<s-betriebe-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag" :delete-eintraege="doDeleteEintraege" :map-beschaeftigungsarten="mapBeschaeftigungsarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile">
						<svws-ui-button type="icon" @click="openModal()">
							<i-ri-add-line />
						</svws-ui-button>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer"
								:disabled="selected.length === 0" />
						</div>
					</s-betriebe-neu-modal>
				</template>
			</svws-ui-data-table>
		</template>
	</svws-ui-secondary-menu>
</template>


<script setup lang="ts">
	import type { DataTableColumn } from "@ui";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref} from "vue";
	import type { BetriebListeEintrag } from "@core";
	import type { BetriebeAuswahlProps } from "./SBetriebeAuswahlProps";

	const props = defineProps<BetriebeAuswahlProps>();
	const search: Ref<string> = ref("");
	const selected = ref<BetriebListeEintrag[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "id", label: "ID", sortable: true},
		{ key: "name1", label: "Name", sortable: true, span: 2 },
	]

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

	const rowsFiltered: ComputedRef<BetriebListeEintrag[]> = computed(() => {
		const res = [];
		for(const k of props.mapKatalogeintraege.values())
			if(k.name1?.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				res.push(k);
		return res;
	})
</script>
