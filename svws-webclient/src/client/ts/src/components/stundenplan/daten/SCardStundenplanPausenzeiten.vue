<template>
	<svws-ui-content-card title="Pausenzeiten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="stundenplanManager().getMapPausenzeiten().values()" clickable v-model:clicked="zeit">
					<template #cell(wochentag)="{ rowData }">
						<SvwsUiTextInput type="number" :model-value="rowData.wochentag" @update:model-value="doPatch({wochentag: Number($event)}, rowData.id)" headless />
					</template>
					<template #cell(beginn)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.beginn" @update:model-value="doPatch({beginn: String($event)}, rowData.id)" headless />
					</template>
					<template #cell(ende)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.ende" @update:model-value="doPatch({ende: String($event)}, rowData.id)" headless />
					</template>
				</svws-ui-data-table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit, StundenplanRaum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const emit = defineEmits<{
		// (e: 'patch', data: Partial<Stundenplan>): void;
		patchPausenzeit: [data: Partial<StundenplanPausenzeit>, id: number];
	}>()

	async function doPatch(data: Partial<StundenplanPausenzeit>, id: number) {
		emit('patchPausenzeit', data, id);
	}

	const zeit = ref<StundenplanPausenzeit | undefined>();

	const cols = [
		{key: 'wochentag', label: 'Wochentag', span: 1}, {key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]
</script>
