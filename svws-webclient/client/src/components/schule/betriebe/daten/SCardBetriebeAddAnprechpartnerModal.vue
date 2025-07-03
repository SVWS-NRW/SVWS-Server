<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Ansprechpartner hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-2">
				<svws-ui-text-input v-model="item.anrede" placeholder="Anrede" />
				<svws-ui-text-input v-model="item.name" placeholder="Name" required />
				<svws-ui-text-input v-model="item.vorname" placeholder="Vorname" />
				<svws-ui-text-input v-model="item.telefon" placeholder="Telefon" :max-len="20" />
				<svws-ui-text-input v-model="item.email" placeholder="E-Mail" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.name"> Ansprechpartner Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { BetriebAnsprechpartner } from "@core";

	const props = defineProps<{
		addBetriebAnsprechpartner: (ansprechpartner: BetriebAnsprechpartner) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const item = ref<BetriebAnsprechpartner>(new BetriebAnsprechpartner());

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addBetriebAnsprechpartner(item.value);
		item.value = new BetriebAnsprechpartner();
		show.value = false;
	}

</script>
