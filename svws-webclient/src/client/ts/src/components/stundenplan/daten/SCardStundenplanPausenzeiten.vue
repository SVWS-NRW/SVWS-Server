<template>
	<svws-ui-content-card title="Pausenzeiten">
		<svws-ui-data-table :columns="cols" :items="stundenplanManager().getListPausenzeit()" clickable v-model:clicked="zeit" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
			<template #cell(wochentag)="{ rowData }">
				<svws-ui-multi-select :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="patchPausenzeit({wochentag: Number($event.id)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
			</template>
			<template #cell(beginn)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.beginn" @update:model-value="patchPausenzeit({beginn: Number($event)}, rowData.id)" headless />
			</template>
			<template #cell(ende)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.ende" @update:model-value="patchPausenzeit({ende: Number($event)}, rowData.id)" headless />
			</template>
			<template #footerActions>
				<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :import-pausenzeiten="importPausenzeiten" :list-pausenzeiten="listPausenzeiten">
					<svws-ui-button @click="openModal()" type="secondary" title="Pausenzeiten importieren">Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-pausenzeiten-modal>
				<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
					<svws-ui-button @click="openModal()" type="secondary" title="Pausenzeit hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-pausenzeit-modal>
				<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
					<svws-ui-button @click="removePausenzeiten(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanManager, StundenplanPausenzeit} from "@core";
	import { Wochentag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
		removePausenzeiten: (raeume: StundenplanPausenzeit[]) => Promise<void>;
		importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: List<StundenplanPausenzeit>;
	}>();

	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selected = ref<StundenplanPausenzeit[]>([]);

	const cols = [
		{key: 'wochentag', label: 'Wochentag', span: 1}, {key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]
</script>
