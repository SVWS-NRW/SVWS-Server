<template>
	<svws-ui-content-card title="Pausenzeiten" class="lg:col-start-2">
		<svws-ui-data-table :columns="cols" :items="stundenplanManager().pausenzeitGetMengeAsList()" clickable v-model:clicked="zeit" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
			<template #cell(wochentag)="{ rowData }">
				<svws-ui-multi-select :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="patchPausenzeit({wochentag: Number($event.id)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
			</template>
			<template #cell(beginn)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.beginn" @blur="beginn => patchPausenzeit({beginn: Number(beginn)}, rowData.id)" headless />
			</template>
			<template #cell(ende)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.ende" @blur="ende => patchPausenzeit({ende: Number(ende)}, rowData.id)" headless />
			</template>
			<template #footerActions>
				<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :import-pausenzeiten="importPausenzeiten" :list-pausenzeiten="listPausenzeiten">
					<svws-ui-button @click="openModal()" type="transparent" title="Pausenzeiten importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-pausenzeiten-modal>
				<svws-ui-button v-if="selected.length > 0" @click="removePausenzeiten(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
					<svws-ui-button @click="openModal()" type="icon" title="Pausenzeit hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-pausenzeit-modal>
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
