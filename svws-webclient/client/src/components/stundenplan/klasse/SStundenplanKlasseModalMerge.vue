<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
		<template #modalTitle>Unterricht zusammenlegen</template>
		<template #modalDescription>
			Sollen die unten angezeigten Unterrichtsgruppen zu einem einzigen Unterricht des Wochentyps 0 (jede Woche) zusammengeführt werden?
			<svws-ui-table selectable v-model="selected" :items="stundenplanManager().unterrichtsgruppenMergeableGet()" :columns disable-footer>
				<template #cell(id)="{rowData: list}">{{ kuerzel(list) }}</template>
				<template #cell(idZeitraster)="{rowData: list}">{{ zeitraster(list) }}</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="mergeUnterrichte(selected)">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { List, StundenplanManager, StundenplanUnterricht} from '@core';
	import { Wochentag } from '@core';

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		mergeUnterrichte: (list: Array<List<StundenplanUnterricht>>) => Promise<void>;
	}>();

	const columns = [{key: 'id', label: 'Unterricht'}, {key: 'idZeitraster', label: 'Stunde'}];

	const selected = ref([]);

	function kuerzel(list: List<StundenplanUnterricht>) {
		const [item] = list;
		return props.stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(item.id)
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
