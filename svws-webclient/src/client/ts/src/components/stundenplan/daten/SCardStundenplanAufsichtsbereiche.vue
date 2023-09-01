<template>
	<svws-ui-content-card title="Aufsichtsbereiche" class="h-full lg:col-start-2">
		<svws-ui-data-table :columns="cols" :items="stundenplanManager().aufsichtsbereichGetMengeAsList()" clickable v-model:clicked="bereich" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
			<template #cell(kuerzel)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.kuerzel" @blur="kuerzel=>patchAufsichtsbereich({kuerzel}, rowData.id)" headless />
			</template>
			<template #cell(beschreibung)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.beschreibung" @blur="beschreibung=>patchAufsichtsbereich({beschreibung}, rowData.id)" headless />
			</template>
			<template #footerActions>
				<s-card-stundenplan-import-aufsichtsbereiche-modal v-slot="{ openModal }" :list-aufsichtsbereiche="listAufsichtsbereiche" :import-aufsichtsbereiche="importAufsichtsbereiche">
					<svws-ui-button @click="openModal()" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-aufsichtsbereiche-modal>
				<svws-ui-button v-if="selected.length > 0" @click="removeAufsichtsbereiche(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
				<s-card-stundenplan-add-aufsichtsbereich-modal v-slot="{ openModal }" :add-aufsichtsbereich="addAufsichtsbereich">
					<svws-ui-button @click="openModal()" type="icon" title="Aufsichtsbereich hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-aufsichtsbereich-modal>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanAufsichtsbereich, StundenplanManager } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchAufsichtsbereich: (daten: Partial<StundenplanAufsichtsbereich>, id: number) => Promise<void>;
		addAufsichtsbereich: (aufsichtsbereich: StundenplanAufsichtsbereich) => Promise<void>;
		removeAufsichtsbereiche: (raeume: StundenplanAufsichtsbereich[]) => Promise<void>;
		importAufsichtsbereiche: (s: StundenplanAufsichtsbereich[]) => Promise<void>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	}>();

	const bereich = ref<StundenplanAufsichtsbereich | undefined>();
	const selected = ref<StundenplanAufsichtsbereich[]>([]);

	const cols = [
		{key: 'kuerzel', label: 'Kürzel', span: 1}, {key: 'beschreibung', label: 'Beschreibung', span: 3}
	]
</script>
