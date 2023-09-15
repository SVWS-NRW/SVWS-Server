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
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoEintrag" :items="rowsFiltered" :columns="cols" clickable scroll-into-view selectable :model-value="selected" @update:model-value="selected=$event" count>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
					<s-betriebe-neu-modal v-slot="{ openModal }" :add-eintrag="addEintrag" :delete-eintraege="doDeleteEintraege" :map-beschaeftigungsarten="mapBeschaeftigungsarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile">
						<svws-ui-button type="icon" @click="openModal()">
							<i-ri-add-line />
						</svws-ui-button>
					</s-betriebe-neu-modal>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>


<script setup lang="ts">

	import type { ComputedRef, Ref} from "vue";
	import type { BetriebListeEintrag } from "@core";
	import type { BetriebeAuswahlProps } from "./SBetriebeAuswahlProps";
	import { computed, ref} from "vue";

	const props = defineProps<BetriebeAuswahlProps>();
	const search: Ref<string> = ref("");
	const selected = ref<BetriebListeEintrag[]>([]);

	const cols = [
		{ key: "name1", label: "Name", sortable: true, span: 2 },
		{ key: "id", label: "ID", sortable: true, span: 0.5 },
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
