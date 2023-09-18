<template>
	<svws-ui-modal ref="modal" class="hidden">
		<template #modalTitle>Ansprechpartner hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" v-model="ansprechpartner.name" type="text" span="full" />
				<svws-ui-text-input placeholder="Vorname" v-model="ansprechpartner.vorname" type="text" span="full" />
				<svws-ui-text-input placeholder="Anrede" v-model="ansprechpartner.anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="ansprechpartner.titel" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Abteilung" v-model="ansprechpartner.abteilung" type="text" span="full" />
				<svws-ui-text-input placeholder="Telefon" v-model="ansprechpartner.telefon" type="tel" span="full" />
				<svws-ui-text-input placeholder="E-Mail" v-model="ansprechpartner.email" type="email" verify-email span="full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="save()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button type="icon" @click="modal.openModal()" title="Ansprechpartner hinzufügen">
		<i-ri-user-add-line />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import type { BetriebStammdaten } from "@core";
	import { BetriebAnsprechpartner } from "@core";
	import type { Ref} from 'vue';
	import { ref } from 'vue';

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
