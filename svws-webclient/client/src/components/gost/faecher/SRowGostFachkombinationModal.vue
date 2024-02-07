<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>Hinweistext für die Fachkombination</template>
		<template #modalContent>
			<svws-ui-text-input placeholder="Eigener Hinweistext" v-model="value" type="text" @keyup.enter="patch()" />
			<p class="my-4">Standardtext: <strong>{{ hinweistext }}</strong></p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="patch()">{{ value ? 'Text speichern' : 'Standard verwenden' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostJahrgangFachkombination } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		hinweistext: string;
		kombination: GostJahrgangFachkombination;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const value = ref('');

	const openModal = () => {
		showModal().value = true;
	}

	async function patch() {
		const hinweistext = value.value ? value.value : props.hinweistext;
		await props.patchFachkombination({ hinweistext }, props.kombination.id);
	}

</script>
