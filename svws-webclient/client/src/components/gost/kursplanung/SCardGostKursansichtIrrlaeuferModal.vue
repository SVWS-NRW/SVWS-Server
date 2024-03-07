<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Ungültige Kurszuordnungen</template>
		<template #modalDescription>
			Sollen folgende fehlerhafte Kurs-Schüler-Zuordnungen entfernt werden?
			<svws-ui-table selectable v-model="selected" :items="zuordnungen" disable-footer>
				<template #cell(name)="{value: id}"> {{ getErgebnismanager().getOfSchuelerNameVorname(id) }} </template>
				<template #cell(kurs)="{value: kurs}"> {{ getErgebnismanager().getOfKursName(kurs.id) }} </template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeZuordnung">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	type Item  = {
		name: number;
		kurs: GostBlockungsergebnisKurs;
	}

	import { computed, ref } from 'vue';
	import type { GostBlockungsergebnisKurs, GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungsergebnisManager , GostBlockungsergebnisKursSchuelerZuordnung} from '@core';
	import { DTOUtils, HashSet } from '@core';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	}>();

	const zuordnungen = computed<HashSet<GostBlockungsergebnisKursSchuelerZuordnung>>(()=>{
		const zuordnungenSet = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const es of props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().entrySet())
			for (const kurs of es.getValue())
				zuordnungenSet.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(es.getKey(), kurs.id));
		return zuordnungenSet;
	})

	const selected = ref<GostBlockungsergebnisKursSchuelerZuordnung[]>(zuordnungen.value.toArray() as GostBlockungsergebnisKursSchuelerZuordnung[]);
	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function removeZuordnung() {
		showModal().value = false;
		const set = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const z of selected.value)
			set.add(z);
		const update = props.getErgebnismanager().kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(set);
		await props.updateKursSchuelerZuordnungen(update);
		selected.value = [];
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
