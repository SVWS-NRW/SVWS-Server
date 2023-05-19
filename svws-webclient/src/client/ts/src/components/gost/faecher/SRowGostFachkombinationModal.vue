<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Hinweistext für die Fachkombination</template>
		<template #modalContent>
			<svws-ui-text-input placeholder="Eigener Hinweistext" v-model="value" type="text" @keyup.enter="patch()" />
			<p class="my-4">Standardtext: <strong>{{ hinweistext }}</strong></p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="patch()">{{ value ? 'Text speichern' : 'Standard verwenden' }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { GostJahrgangFachkombination } from '@svws-nrw/svws-core';
	import { ref } from 'vue';

	const props = defineProps<{
		hinweistext: string;
		kombination: GostJahrgangFachkombination;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
	}>();

	const modal = ref();
	const value = ref('');

	const openModal = () => {
		modal.value.openModal();
	}

	async function patch() {
		const hinweistext = value.value ? value.value : props.hinweistext;
		await props.patchFachkombination({ hinweistext }, props.kombination.id);
	}

</script>
