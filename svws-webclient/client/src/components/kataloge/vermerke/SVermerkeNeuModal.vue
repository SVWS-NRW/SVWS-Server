<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Vermerkart Hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="vermerkart.bezeichnung" type="text" placeholder="Bezeichnung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!vermerkart.bezeichnung"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { VermerkartEintrag } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		addEintrag: (vermerkart: VermerkartEintrag) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const vermerkart = ref(new VermerkartEintrag());

	async function saveEntries() {
		await props.addEintrag(vermerkart.value);
		showModal().value = false;
	}

	const openModal = () => {
		vermerkart.value = new VermerkartEintrag();
		showModal().value = true;
	}

</script>
