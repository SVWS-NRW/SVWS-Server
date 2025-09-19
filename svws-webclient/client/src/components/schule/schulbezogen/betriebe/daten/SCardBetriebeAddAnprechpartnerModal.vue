<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Ansprechpartner hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-2">
				<svws-ui-text-input v-model="item.anrede" placeholder="Anrede" :readonly />
				<svws-ui-text-input v-model="item.name" placeholder="Name" required :readonly />
				<svws-ui-text-input v-model="item.vorname" placeholder="Vorname" :readonly />
				<svws-ui-text-input v-model="item.telefon" placeholder="Telefon" :max-len="20" :readonly />
				<svws-ui-text-input v-model="item.email" placeholder="E-Mail" :readonly />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.name"> Ansprechpartner Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { BenutzerKompetenz, BetriebAnsprechpartner } from "@core";

	const props = defineProps<{
		addBetriebAnsprechpartner: (ansprechpartner: BetriebAnsprechpartner) => Promise<void>;
		benutzerKompetenzen: Set<BenutzerKompetenz>;
	}>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

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
