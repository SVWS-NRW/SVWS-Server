<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium" class="hidden">
		<template #modalTitle>Ungültige Unterrichte entfernen</template>
		<template #modalDescription>
			Sollen die unten angezeigten Unterrichte entfernt werden? Diese Unterrichte sind im Stundenplan eingetragen, haben aber keinen Bezug zum Klassen- oder Kursunterricht mehr.
			<svws-ui-table selectable v-model="selected" :items="stundenplanManager().unterrichtGetMengeUngueltigAsList()" :columns disable-footer>
				<template #cell(idZeitraster)="{rowData: unterricht}">{{ zeitraster(unterricht) }}</template>
				<template #cell(klassen)="{rowData: unterricht}">{{ klassen(unterricht) }}</template>
				<template #cell(lehrer)="{rowData: unterricht}">{{ lehrer(unterricht) }}</template>
				<template #cell(raeume)="{rowData: unterricht}">{{ raeume(unterricht) }}</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="remove" :disabled="selected.length === 0">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from 'vue';
	import type { StundenplanManager, StundenplanUnterricht } from '@core';
	import { Wochentag } from '@core';

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		removeUnterrichte: (list: Iterable<StundenplanUnterricht>) => Promise<void>;
	}>();

	const columns = [
		{key: 'idZeitraster', label: 'Stunde'},
		{key: 'klassen', label: 'Klassen'},
		{key: 'lehrer', label: 'Lehrer'},
		{key: 'raeume', label: 'Räume'},
	];

	const selected = shallowRef([]);

	async function remove() {
		await props.removeUnterrichte(selected.value);
		selected.value = [];
		if (props.stundenplanManager().unterrichtGetMengeUngueltigAsList().isEmpty())
			show.value = false;
	}

	function klassen(unterricht: StundenplanUnterricht) {
		const arr = [];
		for (const id of unterricht.klassen)
			arr.push(props.stundenplanManager().klasseGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function lehrer(unterricht: StundenplanUnterricht) {
		const arr = [];
		for (const id of unterricht.lehrer)
			arr.push(props.stundenplanManager().lehrerGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function raeume(unterricht: StundenplanUnterricht) {
		const arr = [];
		for (const id of unterricht.raeume)
			arr.push(props.stundenplanManager().raumGetByIdOrException(id).kuerzel);
		return arr.join(', ');
	}

	function zeitraster(unterricht: StundenplanUnterricht) {
		return `${Wochentag.fromIDorException(props.stundenplanManager().zeitrasterGetByIdOrException(unterricht.idZeitraster).wochentag).beschreibung}: ${props.stundenplanManager().zeitrasterGetByIdOrException(unterricht.idZeitraster).unterrichtstunde}. Stunde`;
	}

	const show = ref<boolean>(false);

	const openModal = () => show.value = true;

</script>
