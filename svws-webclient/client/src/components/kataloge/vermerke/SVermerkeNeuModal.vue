<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Vermerkart Hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input	:valid="validatorVermerkBezeichnung" v-model="vermerkart.bezeichnung" type="text" placeholder="Bezeichnung" />
				<div v-if="!validatorVermerkBezeichnung(vermerkart.bezeichnung) && (vermerkart.bezeichnung.length > 0)" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet </p>
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!validatorVermerkBezeichnung(vermerkart.bezeichnung)"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { VermerkartEintrag } from '@core';
	import { ref } from 'vue';
	import type { InputDataType } from '../../../../../ui/src/types';

	const props = defineProps<{
		addEintrag: (vermerkart: Partial<VermerkartEintrag>) => Promise<void>;
		mapKatalogeintraege: Map<number, VermerkartEintrag>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const vermerkart = ref(new VermerkartEintrag());

	const validatorVermerkBezeichnung = (value: InputDataType) : boolean => {
		return ((value !== undefined) && (value !== null)) && ((String(value).length === 0) || (![...props.mapKatalogeintraege.values()].some(vmE => vmE.bezeichnung === String(value).trim())))
	}

	async function saveEntries() {
		const postCandidate: Partial<VermerkartEintrag> =  {bezeichnung: vermerkart.value.bezeichnung};
		showModal().value = false;
		await props.addEintrag(postCandidate);
	}

	const openModal = () => {
		vermerkart.value = new VermerkartEintrag();
		showModal().value = true;
	}

</script>
