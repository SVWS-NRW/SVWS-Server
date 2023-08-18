<template>
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Kurse zusammenfassen</template>
		<template #modalDescription>
			<div class="">
				Sollen die Kurse {{ kursname1 }} und {{ kursname2 }} zu einem Kurs zusammengefasst werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="clickNo"> Abbrechen </svws-ui-button>
				<svws-ui-button @click="clickYes"> Ja </svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisKurs} from "@core";
	import { computed, ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs) => Promise<void>;
	}>();

	const kurs1 = ref<GostBlockungKurs | undefined>(undefined);
	const kurs2 = ref<GostBlockungKurs | undefined>(undefined);

	const kursname1 = computed<string>(() => {
		return (kurs1.value === undefined) ? "???" : props.getDatenmanager().kursGetName(kurs1.value.id);
	})

	const kursname2 = computed<string>(() => {
		return (kurs2.value === undefined || !modal.value.isOpen) ? "???" : props.getDatenmanager().kursGetName(kurs2.value.id);
	})

	const modal = ref();
	const openModal = (k1 : GostBlockungKurs, k2 : GostBlockungKurs) => {
		kurs1.value = k1;
		kurs2.value = k2;
		modal.value.openModal();
	}
	defineExpose({ openModal });

	async function clickYes() {
		if ((kurs1.value === undefined) || (kurs2.value === undefined))
			return;
		modal.value.closeModal();
		await props.combineKurs(kurs1.value, kurs2.value);
	}

	async function clickNo() {
		modal.value.closeModal();
	}

</script>
