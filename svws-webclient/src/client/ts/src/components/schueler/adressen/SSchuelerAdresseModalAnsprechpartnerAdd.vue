<template>
	<svws-ui-modal ref="modal" size="medium">
		<template #modalTitle>Ansprechpartner Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Name" v-model="ansprechpartner.name" type="text" />
				<svws-ui-text-input placeholder="Vorname" v-model="ansprechpartner.vorname" type="text" />
				<svws-ui-text-input placeholder="Anrede" v-model="ansprechpartner.anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="ansprechpartner.titel" type="text" />
				<svws-ui-text-input placeholder="Abteilung" v-model="ansprechpartner.abteilung" type="text" />
				<svws-ui-text-input placeholder="Telefon-Nr." v-model="ansprechpartner.telefon" type="tel" />
				<svws-ui-text-input placeholder="E-Mail Adresse" v-model="ansprechpartner.email" type="email" verify-email />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="save()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button type="secondary" @click="modal.openModal()">
		<svws-ui-icon> <i-ri-user-add-line /> </svws-ui-icon>
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { BetriebAnsprechpartner, BetriebStammdaten } from "@svws-nrw/svws-core";
	import { Ref, ref } from 'vue';

	const props = defineProps<{
		betriebsStammdaten: BetriebStammdaten;
		createAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
	}>();

	const modal = ref();

	const ansprechpartner : Ref<BetriebAnsprechpartner> = ref(new BetriebAnsprechpartner())

	async function save() {
		ansprechpartner.value.betrieb_id = props.betriebsStammdaten.id;
		await props.createAnsprechpartner(ansprechpartner.value);
		modal.value.closeModal();
	}

</script>
