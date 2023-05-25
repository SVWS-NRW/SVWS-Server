<template>
	<svws-ui-content-card title="Pausenzeiten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="stundenplanManager().getListPausenzeit()" clickable v-model:clicked="zeit" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
					<template #cell(wochentag)="{ rowData }">
						<SvwsUiTextInput type="number" :model-value="rowData.wochentag" @update:model-value="patchPausenzeit({wochentag: Number($event)}, rowData.id)" headless />
					</template>
					<template #cell(beginn)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.beginn" @update:model-value="patchPausenzeit({beginn: String($event)}, rowData.id)" headless />
					</template>
					<template #cell(ende)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.ende" @update:model-value="patchPausenzeit({ende: String($event)}, rowData.id)" headless />
					</template>
					<template #footerActions>
						<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
							<svws-ui-button @click="openModal()" type="secondary" title="Pausenzeit hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-card-stundenplan-add-pausenzeit-modal>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="removePausenzeiten(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
						</div>
					</template>
				</svws-ui-data-table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanManager, StundenplanPausenzeit } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
		removePausenzeiten: (raeume: StundenplanPausenzeit[]) => Promise<void>;
	}>();

	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selected = ref<StundenplanPausenzeit[]>([]);

	const cols = [
		{key: 'wochentag', label: 'Wochentag', span: 1}, {key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]
</script>
