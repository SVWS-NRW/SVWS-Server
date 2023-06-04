<template>
	<svws-ui-content-card title="Räume">
		<svws-ui-data-table :columns="cols" :items="stundenplanManager().getListRaum()" clickable v-model:clicked="raum" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
			<template #cell(kuerzel)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.kuerzel" @update:model-value="patchRaum({kuerzel: String($event)}, rowData.id)" headless required />
			</template>
			<template #cell(groesse)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.groesse" @update:model-value="patchRaum({groesse: Number($event)}, rowData.id)" headless required />
			</template>
			<template #cell(beschreibung)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.beschreibung" @update:model-value="patchRaum({beschreibung: String($event)}, rowData.id)" headless />
			</template>
			<template #footerActions>
				<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :import-raeume="importRaeume" :list-raeume="listRaeume">
					<svws-ui-button @click="openModal()" type="secondary" title="Räume importieren">Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-raeume-modal>
				<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
					<svws-ui-button @click="openModal()" type="secondary" title="Raum hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-raum-modal>
				<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
					<svws-ui-button @click="removeRaeume(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, Raum, StundenplanManager, StundenplanRaum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
		addRaum: (raum: StundenplanRaum) => Promise<void>;
		removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
		importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
		listRaeume: List<Raum>;
	}>();

	const raum = ref<StundenplanRaum | undefined>();
	const selected = ref<StundenplanRaum[]>([]);

	const cols = [
		{key: 'kuerzel', label: 'Kürzel', span: 1}, {key: 'groesse', label: 'Größe', span: 1}, {key: 'beschreibung', label: 'Beschreibung', span: 3}
	]

</script>
