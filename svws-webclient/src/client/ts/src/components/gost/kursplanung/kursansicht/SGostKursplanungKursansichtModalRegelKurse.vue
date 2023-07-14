<template>
	<svws-ui-modal ref="modal" size="small" v-once>
		<template #modalTitle>Regel erstellen für Kurse</template>
		<template #modalDescription>
			<div class="">
				Sollen die Kurse {{ getDatenmanager().kursGetName(kurs1Id) }} und {{ getDatenmanager().kursGetName(kurs2Id) }} immer oder nie zusammen auf einer Schiene liegen?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="regelImmerZusammen">Immer</svws-ui-button>
				<svws-ui-button @click="regelNieZusammen">Nie</svws-ui-button>
			</div>
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
		kurs1Id: number;
		kurs2Id: number;
	}>();

	const modal = ref();
	const openModal = () => modal.value.openModal();
	defineExpose({ openModal });

	async function regelImmerZusammen() {
		if (props.kurs1Id === 0)
			return;
		modal.value.closeModal();
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(props.kurs1Id)
		regel.parameter.add(props.kurs2Id);
		await props.addRegel(regel);
	}

	async function regelNieZusammen() {
		if (props.kurs1Id === 0)
			return;
		modal.value.closeModal();
		const regel = new GostBlockungRegel();
		regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel.parameter.add(props.kurs1Id)
		regel.parameter.add(props.kurs2Id);
		await props.addRegel(regel);
	}

</script>
