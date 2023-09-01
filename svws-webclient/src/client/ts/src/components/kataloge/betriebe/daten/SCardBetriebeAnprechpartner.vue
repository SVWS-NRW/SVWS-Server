<template>
	<svws-ui-content-card title="Ansprechpartner">
		<svws-ui-data-table :columns="cols" :items="mapAnsprechpartner.values()" clickable v-model:clicked="select"
			selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
			<template #cell(anrede)="{ rowData }">
				<SvwsUiTextInput :model-value="rowData.anrede" @blur="anrede => patchBetriebAnpsrechpartner({anrede}, rowData.id)" headless required />
			</template>
			<template #cell(name)="{ rowData }">
				<SvwsUiTextInput :model-value="rowData.name" @blur="name=>patchBetriebAnpsrechpartner({name}, rowData.id)" headless required />
			</template>
			<template #cell(vorname)="{ rowData }">
				<SvwsUiTextInput :model-value="rowData.vorname" @blur="vorname=>patchBetriebAnpsrechpartner({vorname}, rowData.id)" headless required />
			</template>
			<template #cell(telefon)="{ rowData }">
				<SvwsUiTextInput :model-value="rowData.telefon" @blur="telefon=>patchBetriebAnpsrechpartner({telefon}, rowData.id)" headless required />
			</template>
			<template #cell(email)="{ rowData }">
				<SvwsUiTextInput :model-value="rowData.email" @blur="email=>patchBetriebAnpsrechpartner({email}, rowData.id)" headless required />
			</template>
			<template #footerActions>
				<s-card-betriebe-add-anprechpartner-modal v-slot="{ openModal }" :add-betrieb-ansprechpartner="addBetriebAnsprechpartner">
					<svws-ui-button @click="openModal()" type="secondary" title="Anpsrechpartner hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-betriebe-add-anprechpartner-modal>
				<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
					<svws-ui-button @click="removeBetriebAnsprechpartner(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebAnsprechpartner, BetriebStammdaten, KatalogEintrag, OrtKatalogEintrag } from "@core";
	import { ref, watch } from "vue";
	import { computed } from "vue";



	const props = defineProps<{
		daten: BetriebStammdaten;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
		patchBetriebAnpsrechpartner: (data : Partial<BetriebAnsprechpartner>, id: number) => Promise<void>;
		addBetriebAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
		removeBetriebAnsprechpartner: (data: BetriebAnsprechpartner[]) => Promise<void>;
	}>();
	const select = ref<BetriebAnsprechpartner | undefined>();
	const selected = ref<BetriebAnsprechpartner[]>([]);

	const cols = [
		{key: 'id', label: 'ID', span: 1}, {key: 'anrede', label: 'Anrede', span: 1}, {key: 'name', label: 'Name', span: 2},
		{key: 'vorname', label: 'Rufname', span: 2}, {key: 'telefon', label: 'Telefon', span: 3}, {key: 'email', label: 'Email', span: 2}
	]

	watch(() => props.daten, (first, second) => {
		selected.value = [];
	});

	watch(() => props.mapAnsprechpartner, (first, second) => {
		selected.value = [];
	});

</script>