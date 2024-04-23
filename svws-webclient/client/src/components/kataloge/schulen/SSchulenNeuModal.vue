<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Schule hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.name" required placeholder="Name" />
				<svws-ui-text-input v-model="item.schulnummer" required placeholder="Schulnummer" :valid />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.name || !valid(item.schulnummer)"> Schule hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { InputDataType } from "@ui";
	import { SchulEintrag } from "@core";

	const props = defineProps<{
		addEintrag: (schule: Partial<SchulEintrag>) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<SchulEintrag>(new SchulEintrag());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addEintrag({name: item.value.name, schulnummer: item.value.schulnummer});
		item.value = new SchulEintrag();
		showModal().value = false;
	}

	function valid(schulnummer: InputDataType) {
		return typeof schulnummer === 'string' && schulnummer.length === 6;
	}
</script>
