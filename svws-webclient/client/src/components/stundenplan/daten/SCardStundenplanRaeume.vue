<template>
	<svws-ui-content-card title="Räume">
		<svws-ui-table :columns="cols" :items="items" v-model:clicked="raum" selectable v-model="selected" count>
			<template #cell(kuerzel)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.kuerzel" @change="patchRaum({kuerzel: String($event)}, rowData.id)" headless required />
			</template>
			<template #cell(groesse)="{ rowData }">
				<svws-ui-text-input type="number" :model-value="rowData.groesse" @change="patchRaum({groesse: Number($event)}, rowData.id)" headless required />
			</template>
			<template #cell(beschreibung)="{ rowData }">
				<svws-ui-text-input :model-value="rowData.beschreibung" @change="patchRaum({beschreibung: String($event)}, rowData.id)" headless />
			</template>
			<template #actions>
				<svws-ui-button @click="gotoKatalog('raeume')" type="transparent" title="Aufsichtsbereiche importieren"><i-ri-link /> Katalog bearbeiten</svws-ui-button>
				<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :import-raeume="importRaeume" :list-raeume="listRaeume">
					<svws-ui-button @click="openModal()" type="transparent" title="Räume importieren"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
				</s-card-stundenplan-import-raeume-modal>
				<svws-ui-button @click="removeRaeume(selected)" type="trash" :disabled="!selected.length" />
				<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
					<svws-ui-button @click="openModal()" type="icon" title="Raum hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</s-card-stundenplan-add-raum-modal>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, Raum, StundenplanManager, StundenplanRaum } from "@core";
	import { ref, watch } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchRaum: (daten: Partial<StundenplanRaum>, id: number) => Promise<void>;
		addRaum: (raum: StundenplanRaum) => Promise<void>;
		removeRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
		importRaeume: (raeume: StundenplanRaum[]) => Promise<void>;
		listRaeume: () => List<Raum>;
		gotoKatalog: (katalog: 'raeume'|'aufsichtsbereiche'|'pausenzeiten') => Promise<void>;
	}>();

	watch(()=>props.stundenplanManager, neu => {
		items.value = [...neu().raumGetMengeAsList()]
	})

	const raum = ref<StundenplanRaum | undefined>();
	const selected = ref<StundenplanRaum[]>([]);
	const items = ref<StundenplanRaum[]>([])

	const cols = [
		{key: 'kuerzel', label: 'Kürzel', span: 1}, {key: 'groesse', label: 'Größe', span: 1}, {key: 'beschreibung', label: 'Beschreibung', span: 3}
	]

</script>
