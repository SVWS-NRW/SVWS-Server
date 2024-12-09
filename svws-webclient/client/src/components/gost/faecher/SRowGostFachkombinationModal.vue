<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>Hinweistext für die Fachkombination</template>
		<template #modalContent>
			<svws-ui-text-input placeholder="Eigener Hinweistext" v-model="value" type="text" @keyup.enter="patch()" />
			<p class="my-4">Standardtext: <strong>{{ hinweistext }}</strong></p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
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

	const show = ref<boolean>(false);

	const value = ref('');

	function openModal() {
		show.value = true;
	}

	async function patch() {
		const hinweistext = value.value.length !== 0 ? value.value : props.hinweistext;
		await props.patchFachkombination({ hinweistext }, props.kombination.id);
	}

</script>
