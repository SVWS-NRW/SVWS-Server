<template>
	<svws-ui-content-card title="Räume">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="stundenplanManager().getMapRaeume().values()" clickable v-model:clicked="raum">
					<template #cell(kuerzel)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.kuerzel" @update:model-value="doPatch({kuerzel: String($event)}, rowData.id)" headless />
					</template>
					<template #cell(groesse)="{ rowData }">
						<SvwsUiTextInput type="number" :model-value="rowData.groesse" @update:model-value="doPatch({groesse: Number($event)}, rowData.id)" headless />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.beschreibung" @update:model-value="doPatch({beschreibung: String($event)}, rowData.id)" headless />
					</template>
				</svws-ui-data-table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanManager, StundenplanRaum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const emit = defineEmits<{
		// (e: 'patch', data: Partial<Stundenplan>): void;
		patchRaum: [data: Partial<StundenplanRaum>, id: number];
	}>()

	async function doPatch(data: Partial<StundenplanRaum>, id: number) {
		emit('patchRaum', data, id);
	}

	const raum = ref<StundenplanRaum | undefined>();

	const cols = [
		{key: 'kuerzel', label: 'Kürzel', span: 1}, {key: 'groesse', label: 'Größe', span: 1}, {key: 'beschreibung', label: 'Beschreibung', span: 3}
	]
</script>
