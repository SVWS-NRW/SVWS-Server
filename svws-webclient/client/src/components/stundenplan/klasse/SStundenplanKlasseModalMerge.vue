<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium" class="hidden">
		<template #modalTitle>Unterricht zusammenlegen</template>
		<template #modalDescription>
			Sollen die unten angezeigten Unterrichtsgruppen zu einem einzigen Unterricht des Wochentyps 0 (jede Woche) zusammengeführt werden?
			<svws-ui-table selectable v-model="selected" :items="stundenplanManager().unterrichtsgruppenMergeableGet()" :columns disable-footer>
				<template #cell(id)="{rowData: list}">{{ kuerzel(list) }}</template>
				<template #cell(idZeitraster)="{rowData: list}">{{ zeitraster(list) }}</template>
				<template #cell(klassen)="{rowData: list}">{{ klassen(list) }}</template>
				<template #cell(lehrer)="{rowData: list}">{{ lehrer(list) }}</template>
				<template #cell(raeume)="{rowData: list}">{{ raeume(list) }}</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="mergeUnterrichte(selected)" :disabled="selected.length === 0">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { List, StundenplanManager, StundenplanUnterricht } from '@core';
	import { Wochentag } from '@core';

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		mergeUnterrichte: (list: Array<List<StundenplanUnterricht>>) => Promise<void>;
	}>();

	const columns = [
		{key: 'id', label: 'Unterricht'},
		{key: 'idZeitraster', label: 'Stunde'},
		{key: 'klassen', label: 'Klassen'},
		{key: 'lehrer', label: 'Lehrer'},
		{key: 'raeume', label: 'Räume'},
	];

	const selected = ref([]);

	function kuerzel(list: List<StundenplanUnterricht>) {
		const [item] = list;
		return props.stundenplanManager().unterrichtGetByIDStringOfFachOderKurs(item.id, true)
	}

	function klassen(list: List<StundenplanUnterricht>) {
		const [item] = list;
		const arr = [];
		for (const id of item.klassen)
			arr.push(props.stundenplanManager().klasseGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function lehrer(list: List<StundenplanUnterricht>) {
		const [item] = list;
		const arr = [];
		for (const id of item.lehrer)
			arr.push(props.stundenplanManager().lehrerGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function raeume(list: List<StundenplanUnterricht>) {
		const [item] = list;
		const arr = [];
		for (const id of item.raeume)
			arr.push(props.stundenplanManager().raumGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function zeitraster(list: List<StundenplanUnterricht>) {
		const [item] = list;
		return `${Wochentag.fromIDorException(props.stundenplanManager().zeitrasterGetByIdOrException(item.idZeitraster).wochentag).beschreibung}: ${props.stundenplanManager().zeitrasterGetByIdOrException(item.idZeitraster).unterrichtstunde}. Stunde`;
	}

	const show = ref<boolean>(false);

	const openModal = () => {
		show.value = true;
	}

</script>
