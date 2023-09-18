<template>
	<svws-ui-modal ref="modal" class="hidden">
		<template #modalTitle>Ansprechpartner bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" :model-value="ansprechpartner.name" @change="name=>patchAnsprechpartner({name}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Vorname" :model-value="ansprechpartner.vorname" @change="vorname=>patchAnsprechpartner({vorname}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Anrede" :model-value="ansprechpartner.anrede" @change="anrede=>patchAnsprechpartner({anrede}, ansprechpartner.id)" type="text" />
				<svws-ui-text-input placeholder="Titel" :model-value="ansprechpartner.titel" @change="titel=>patchAnsprechpartner({titel}, ansprechpartner.id)" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Abteilung" :model-value="ansprechpartner.abteilung" @change="abteilung=>patchAnsprechpartner({abteilung}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Telefon" :model-value="ansprechpartner.telefon" @change="telefon=>patchAnsprechpartner({telefon}, ansprechpartner.id)" type="tel" span="full" />
				<svws-ui-text-input placeholder="E-Mail" :model-value="ansprechpartner.email" @change="email=>patchAnsprechpartner({email}, ansprechpartner.id)" type="email" verify-email span="full" />
				<span class="opacity-50 col-span-full text-sm mt-3">Die Daten werden automatisch gespeichert.</span>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<!-- <svws-ui-button type="secondary" @click="$refs.modalEdit.closeModal"> Abbrechen </svws-ui-button> -->
			<svws-ui-button type="primary" @click="modal.closeModal"> Fertig </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button type="icon" @click="modal.openModal()" title="Ansprechpartner bearbeiten">
		<i-ri-edit-2-line />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner } from "@core";
	import { ref } from 'vue';

	const props = defineProps<{
		ansprechpartner: BetriebAnsprechpartner;
		patchAnsprechpartner: (data : Partial<BetriebAnsprechpartner>, id : number) => Promise<void>;
	}>();

	const modal = ref();

</script>
