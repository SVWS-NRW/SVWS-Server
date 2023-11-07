<template>
	<svws-ui-content-card title="Pausenzeiten">
		<svws-ui-table :columns="cols" :items="stundenplanManager().pausenzeitGetMengeAsList()" v-model:clicked="zeit" selectable v-model="selected" count>
			<template #cell(wochentag)="{ rowData }">
				<svws-ui-select :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="wochentag => patchPausenzeit({wochentag: Number(wochentag?.id || -1)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
			</template>
			<template #cell(beginn)="{ rowData }">
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.beginn ?? 0)" @change="beginn => patchBeginn(beginn, rowData.id)" headless />
			</template>
			<template #cell(ende)="{ rowData }">
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.ende ?? 0)" @change="ende => patchEnde(ende, rowData.id)" headless />
			</template>
			<template #actions>
				<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :import-pausenzeiten="importPausenzeiten" :list-pausenzeiten="listPausenzeiten">
					<svws-ui-button @click="openModal()" type="transparent" title="Pausenzeiten importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-pausenzeiten-modal>
				<svws-ui-button @click="removePausenzeiten(selected)" type="trash" :disabled="!selected.length" />
				<s-card-stundenplan-add-pausenzeit-modal v-slot="{ openModal }" :add-pausenzeit="addPausenzeit">
					<svws-ui-button @click="openModal()" type="icon" title="Pausenzeit hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-pausenzeit-modal>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanManager, StundenplanPausenzeit} from "@core";
	import { Wochentag, DateUtils } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (daten: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		addPausenzeit: (pausenzeit: StundenplanPausenzeit) => Promise<void>;
		removePausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		importPausenzeiten: (pausenzeiten: StundenplanPausenzeit[]) => Promise<void>;
		listPausenzeiten: () => List<StundenplanPausenzeit>;
	}>();

	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selected = ref<StundenplanPausenzeit[]>([]);

	const cols = [
		{key: 'wochentag', label: 'Wochentag', span: 1}, {key: 'beginn', label: 'Beginn', span: 1}, {key: 'ende', label: 'Ende', span: 1}
	]

	async function patchBeginn(minuten: string, id: number) {
		const beginn = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({beginn}, id);
	}

	async function patchEnde(minuten: string, id: number) {
		const ende = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({ende}, id);
	}
</script>
