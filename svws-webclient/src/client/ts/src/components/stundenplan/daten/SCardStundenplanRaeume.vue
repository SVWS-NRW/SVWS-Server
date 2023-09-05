<template>
	<svws-ui-content-card title="Räume" class="lg:col-start-2">
		<svws-ui-data-table :columns="cols" :items="stundenplanManager().raumGetMengeAsList()" clickable v-model:clicked="raum" selectable :model-value="selected" @blur="selected=$event" :count="selected.length > 0">
			<template #cell(kuerzel)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.kuerzel" @change="patchRaum({kuerzel: String($event)}, rowData.id)" headless required />
			</template>
			<template #cell(groesse)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.groesse" @change="patchRaum({groesse: Number($event)}, rowData.id)" headless required />
			</template>
			<template #cell(beschreibung)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.beschreibung" @change="patchRaum({beschreibung: String($event)}, rowData.id)" headless />
			</template>
			<template #footerActions>
				<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :import-raeume="importRaeume" :list-raeume="listRaeume">
					<svws-ui-button @click="openModal()" type="transparent" title="Räume importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-raeume-modal>
				<svws-ui-button v-if="selected.length > 0" @click="removeRaeume(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
					<svws-ui-button @click="openModal()" type="icon" title="Raum hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-raum-modal>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, Raum, StundenplanManager, StundenplanRaum } from "@core";
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
