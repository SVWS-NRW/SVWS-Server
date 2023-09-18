<template>
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Regel erstellen</template>
		<template #modalContent>
			<p>
				Sollen die Kurse {{ getDatenmanager().kursGetName(id1) }} und {{ getDatenmanager().kursGetName(id2) }} immer oder nie zusammen auf einer Schiene liegen?
			</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="regelImmerZusammen"> Immer </svws-ui-button>
			<svws-ui-button type="secondary" @click="regelNieZusammen"> Nie </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager} from "@core";
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@core";
	import { ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	}>();

	const id1 = ref<number>(-1);
	const id2 = ref<number>(-1);

	const modal = ref();
	const openModal = (idKurs1 : number, idKurs2 : number) => {
		id1.value = idKurs1;
		id2.value = idKurs2;
		modal.value.openModal();
	}
	defineExpose({ openModal });

	async function regelImmerZusammen() {
		if (id1.value === 0)
			return;
		modal.value.closeModal();
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(id1.value)
		regel.parameter.add(id2.value);
		await props.addRegel(regel);
	}

	async function regelNieZusammen() {
		if (id1.value === 0)
			return;
		modal.value.closeModal();
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel.parameter.add(id1.value)
		regel.parameter.add(id2.value);
		await props.addRegel(regel);
	}

</script>
