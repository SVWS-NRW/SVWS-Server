<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Aufsichtsbereich hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel">Aufsichtsbereich hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { StundenplanAufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addAufsichtsbereich: (raum: StundenplanAufsichtsbereich) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<StundenplanAufsichtsbereich>(new StundenplanAufsichtsbereich());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addAufsichtsbereich(item.value);
		console.log("Fertig")
		showModal().value = false;
	}

</script>
