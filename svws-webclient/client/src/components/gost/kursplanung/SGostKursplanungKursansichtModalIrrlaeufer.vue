<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Ungültige Kurszuordnungen</template>
		<template #modalDescription>
			Sollen folgende fehlerhafte Kurs-Schüler-Zuordnungen entfernt werden?
			<svws-ui-table selectable v-model="selected" :items="zuordnungen" disable-footer :columns="[{key: 'idKurs', label: 'Kurs',}, {key: 'idSchueler', label: 'Schüler'}]">
				<template #cell(idSchueler)="{value: idSchueler}">
					{{ getErgebnismanager().getOfSchuelerNameVorname(idSchueler) }}
					<span v-if="getErgebnismanager().getParent().daten().abijahrgang !== getErgebnismanager().getParent().schuelerGet(idSchueler).abschlussjahrgang">
						(Abi {{ getErgebnismanager().getParent().schuelerGet(idSchueler).abschlussjahrgang }})
					</span>
				</template>
				<template #cell(idKurs)="{value: idKurs}">
					{{ getErgebnismanager().getOfKursName(idKurs) }}
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeZuordnung">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from 'vue';
	import type { GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungsergebnisManager , GostBlockungsergebnisKursSchuelerZuordnung} from '@core';
	import { DTOUtils, HashSet } from '@core';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	}>();

	const zuordnungen = computed<HashSet<GostBlockungsergebnisKursSchuelerZuordnung>>(() => {
		const zuordnungenSet = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const es of props.getErgebnismanager().getOfSchuelerMapIDzuUngueltigeKurse().entrySet())
			for (const kurs of es.getValue())
				zuordnungenSet.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs.id, es.getKey()));
		return zuordnungenSet;
	})

	const selected = shallowRef<GostBlockungsergebnisKursSchuelerZuordnung[]>(zuordnungen.value.toArray() as GostBlockungsergebnisKursSchuelerZuordnung[]);
	const _showModal = shallowRef<boolean>(false);
	const showModal = () => _showModal;

	async function removeZuordnung() {
		const set = new HashSet<GostBlockungsergebnisKursSchuelerZuordnung>();
		for (const z of selected.value)
			set.add(z);
		showModal().value = false;
		if (!set.isEmpty()) {
			const update = props.getErgebnismanager().kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(set);
			await props.updateKursSchuelerZuordnungen(update);
		}
		selected.value = [];
	}

	const openModal = () => {
		selected.value = zuordnungen.value.toArray() as GostBlockungsergebnisKursSchuelerZuordnung[];
		showModal().value = true;
	}

</script>
