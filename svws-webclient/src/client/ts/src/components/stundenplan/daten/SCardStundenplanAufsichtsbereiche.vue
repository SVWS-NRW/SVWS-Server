<template>
	<svws-ui-content-card title="Aufsichtsbereiche">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="stundenplanManager().getMapAufsichtsbereich().values()" clickable v-model:clicked="bereich">
					<template #cell(kuerzel)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.kuerzel" @update:model-value="doPatch({kuerzel: String($event)}, rowData.id)" headless />
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

	import type { StundenplanAufsichtsbereich, StundenplanManager } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const emit = defineEmits<{
		patchAufsichtsbereich: [data: Partial<StundenplanAufsichtsbereich>, id: number];
	}>()

	async function doPatch(data: Partial<StundenplanAufsichtsbereich>, id: number) {
		emit('patchAufsichtsbereich', data, id);
	}

	const bereich = ref<StundenplanAufsichtsbereich | undefined>();

	const cols = [
		{key: 'kuerzel', label: 'Kürzel', span: 1}, {key: 'beschreibung', label: 'Beschreibung', span: 3}
	]
</script>
