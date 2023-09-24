<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Ansprechpartner hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-2">
				<svws-ui-text-input v-model="item.anrede" placeholder="Anrede" />
				<svws-ui-text-input v-model="item.name" placeholder="Name" required />
				<svws-ui-text-input v-model="item.vorname" placeholder="Rufname" />
				<svws-ui-text-input v-model="item.telefon" placeholder="Telefon" />
				<svws-ui-text-input v-model="item.email" placeholder="EMail" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
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

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<BetriebAnsprechpartner>(new BetriebAnsprechpartner());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addBetriebAnsprechpartner(item.value);
		item.value = new BetriebAnsprechpartner();
		showModal().value = false;
	}

</script>
