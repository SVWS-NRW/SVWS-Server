<template>
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>Regel erstellen</template>
		<template #modalContent>
			<p>
				Sollen die Kurse {{ getErgebnisManager().getOfKursName(id1) }} und {{ getErgebnisManager().getOfKursName(id2) }} immer oder nie zusammen auf einer Schiene liegen?
			</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="regelZusammen(true)"> Immer </svws-ui-button>
			<svws-ui-button type="secondary" @click="regelZusammen(false)"> Nie </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungRegelUpdate, GostBlockungsergebnisManager} from "@core";
	import { SetUtils } from "@core";
	import { ref } from 'vue';

	const props = defineProps<{
		getErgebnisManager: () => GostBlockungsergebnisManager;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const id1 = ref<number>(-1);
	const id2 = ref<number>(-1);

	const openModal = (idKurs1 : number, idKurs2 : number) => {
		id1.value = idKurs1;
		id2.value = idKurs2;
		showModal().value = true;
	}

	defineExpose({ openModal });

	async function regelZusammen(value: boolean) {
		if (id1.value === 0)
			return;
		showModal().value = false;
		const set = SetUtils.create2(id1.value, id2.value);
		const update = value === true
			? props.getErgebnisManager().regelupdateCreate_08_KURS_ZUSAMMEN_MIT_KURS(set)
			: props.getErgebnisManager().regelupdateCreate_07_KURS_VERBIETEN_MIT_KURS(set)
		await props.regelnUpdate(update);
	}

</script>
