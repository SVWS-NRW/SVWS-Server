<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Unterricht zusammenlegen</template>
		<template #modalDescription>
			Sollen die unten angezeigten Unterrichtsgruppen zu einem einzigen Unterricht des Wochentyps 0 (jede Woche) zusammengeführt werden?
			<svws-ui-table selectable v-model="selected" :items :columns disable-footer>
				<template #cell(id)="{value}">{{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(value) }}</template>
				<template #cell(idZeitraster)="{value}">{{ Wochentag.fromIDorException(stundenplanManager().zeitrasterGetByIdOrException(value).wochentag).beschreibung }}: {{ stundenplanManager().zeitrasterGetByIdOrException(value).unterrichtstunde }}. Stunde</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="()=>{}">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { StundenplanManager} from '@core';
	import { Wochentag } from '@core';

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const columns = [{key: 'id', label: 'Untericht'}, {key: 'idZeitraster', label: 'Stunde'}];

	const selected = ref([]);
	const items = computed(() => [...props.stundenplanManager().unterrichtsgruppenMergeableGet()].map(e => e.getFirst()));

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

</script>
