<template>
	<svws-ui-content-card title="Ansprechpartner">
		<svws-ui-table :columns="cols" :items="mapAnsprechpartner.values()" v-model:clicked="select" selectable :model-value="selected" @update:model-value="selected=$event" count>
			<template #cell(anrede)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.anrede" @change="anrede => patchBetriebAnpsrechpartner({anrede}, rowData.id)" headless required />
			</template>
			<template #cell(name)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.name" @change="name=>patchBetriebAnpsrechpartner({name}, rowData.id)" headless required />
			</template>
			<template #cell(vorname)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.vorname" @change="vorname=>patchBetriebAnpsrechpartner({vorname}, rowData.id)" headless required />
			</template>
			<template #cell(telefon)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.telefon" @change="telefon=>patchBetriebAnpsrechpartner({telefon}, rowData.id)" headless required />
			</template>
			<template #cell(email)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.email" @change="email=>patchBetriebAnpsrechpartner({email}, rowData.id)" headless required />
			</template>
			<template #actions>
				<svws-ui-button @click="removeBetriebAnsprechpartner(selected)" type="trash" :disabled="!selected.length" />
				<s-card-betriebe-add-anprechpartner-modal v-slot="{ openModal }" :add-betrieb-ansprechpartner="addBetriebAnsprechpartner">
					<svws-ui-button @click="openModal()" type="icon" title="Anpsrechpartner hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-betriebe-add-anprechpartner-modal>
			</template>
		</svws-ui-table>
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
		{key: 'anrede', label: 'Anrede', span: 1},
		{key: 'name', label: 'Name', span: 2},
		{key: 'vorname', label: 'Rufname', span: 2},
		{key: 'telefon', label: 'Telefon', span: 2},
		{key: 'email', label: 'Email', span: 3},
		{key: 'id', label: 'ID', span: 0.5}
	]

	watch(() => props.daten, (first, second) => {
		selected.value = [];
	});

	watch(() => props.mapAnsprechpartner, (first, second) => {
		selected.value = [];
	});

</script>
