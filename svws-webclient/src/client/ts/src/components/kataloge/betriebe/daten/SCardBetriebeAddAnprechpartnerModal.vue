<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Ansprechpartner hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-2">
				<svws-ui-text-input v-model="item.anrede" placeholder="Anrede" />
				<svws-ui-text-input v-model="item.name" placeholder="Name" required />
				<svws-ui-text-input v-model="item.vorname" placeholder="Vorname" />
				<svws-ui-text-input v-model="item.telefon" placeholder="Telefon" />
				<svws-ui-text-input v-model="item.email" placeholder="EMail" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.name"> Ansprechpartner Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { BetriebAnsprechpartner } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addBetriebAnsprechpartner: (ansprechpartner: BetriebAnsprechpartner) => Promise<void>;
	}>();


	const modal = ref();
	const item = ref<BetriebAnsprechpartner>(new BetriebAnsprechpartner());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addBetriebAnsprechpartner(item.value);
		item.value = new BetriebAnsprechpartner();
		modal.value.closeModal();
	}
</script>