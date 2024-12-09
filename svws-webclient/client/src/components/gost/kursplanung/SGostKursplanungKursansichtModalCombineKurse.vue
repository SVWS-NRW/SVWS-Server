<template>
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>Kurse zusammenfassen</template>
		<template #modalContent>
			<p>Sollen die Kurse {{ kursname1 }} und {{ kursname2 }} zu einem Kurs zusammengefasst werden?</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="clickNo"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="clickYes"> Ja </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostBlockungKurs, GostBlockungsdatenManager, GostBlockungsergebnisKurs } from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		combineKurs: (kurs1 : GostBlockungKurs, fach2: GostBlockungKurs | GostBlockungsergebnisKurs | undefined | null) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const kurs1 = ref<GostBlockungKurs | undefined>(undefined);
	const kurs2 = ref<GostBlockungKurs | undefined>(undefined);

	const kursname1 = computed<string>(() => {
		return (kurs1.value === undefined) ? "???" : props.getDatenmanager().kursGetName(kurs1.value.id);
	})

	const kursname2 = computed<string>(() => {
		return (kurs2.value === undefined || !show.value) ? "???" : props.getDatenmanager().kursGetName(kurs2.value.id);
	})

	const openModal = (k1 : GostBlockungKurs, k2 : GostBlockungKurs) => {
		kurs1.value = k1;
		kurs2.value = k2;
		show.value = true;
	}
	defineExpose({ openModal });

	async function clickYes() {
		if ((kurs1.value === undefined) || (kurs2.value === undefined))
			return;
		show.value = false;
		await props.combineKurs(kurs1.value, kurs2.value);
	}

	async function clickNo() {
		show.value = false;
	}

</script>
