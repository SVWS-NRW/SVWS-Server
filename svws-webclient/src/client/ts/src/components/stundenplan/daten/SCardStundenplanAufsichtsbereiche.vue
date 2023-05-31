<template>
	<svws-ui-content-card title="Aufsichtsbereiche">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="stundenplanManager().getListAufsichtbereich()" clickable v-model:clicked="bereich" selectable :model-value="selected" @update:model-value="selected=$event" :count="selected.length > 0">
					<template #cell(kuerzel)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.kuerzel" @update:model-value="patchAufsichtsbereich({kuerzel: String($event)}, rowData.id)" headless />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<SvwsUiTextInput :model-value="rowData.beschreibung" @update:model-value="patchAufsichtsbereich({beschreibung: String($event)}, rowData.id)" headless />
					</template>
					<template #footerActions>
						<s-card-stundenplan-import-aufsichtsbereiche-modal v-slot="{ openModal }" :list-aufsichtsbereiche="listAufsichtsbereiche" :import-aufsichtsbereiche="importAufsichtsbereiche">
							<svws-ui-button @click="openModal()" type="secondary" title="Aufsichtsbereiche importieren">Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-aufsichtsbereiche-modal>
						<s-card-stundenplan-add-aufsichtsbereich-modal v-slot="{ openModal }" :add-aufsichtsbereich="addAufsichtsbereich">
							<svws-ui-button @click="openModal()" type="secondary" title="Aufsichtsbereich hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-card-stundenplan-add-aufsichtsbereich-modal>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="removeAufsichtsbereiche(selected)" type="trash" class="cursor-pointer" :disabled="!selected.length" />
						</div>
					</template>
				</svws-ui-data-table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanAufsichtsbereich, StundenplanManager } from "@svws-nrw/svws-core";
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
