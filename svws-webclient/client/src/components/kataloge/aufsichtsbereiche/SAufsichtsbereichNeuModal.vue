<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Aufsichtsbereich hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel"> Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Aufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addAufsichtsbereich: (raum: Aufsichtsbereich) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<Aufsichtsbereich>(new Aufsichtsbereich());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addAufsichtsbereich(item.value);
		item.value = new Aufsichtsbereich();
		showModal().value = false;
	}

</script>
