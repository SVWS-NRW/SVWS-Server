<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Impressum</template>
		<template #modalDescription>
			<div v-if="auskunftState.impressum !== null" class="text-left">
				<mark-down :text="auskunftState.impressum" />
			</div>
			<div v-else class="space-y-2 text-left">
				Das Impressum für diese Seite wird nachgereicht. Bitte fragen Sie bei Ihrer Schule nach Details.
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button @click="closeModal">Ok</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { useAuskunftState } from '@ui/states/AuskunftState';
	import { ref } from 'vue';

	const auskunftState = useAuskunftState();

	const show = ref<boolean>(false);

	async function closeModal() {
		show.value = false;
	}

	const openModal = () => {
		show.value = true;
	};

</script>
