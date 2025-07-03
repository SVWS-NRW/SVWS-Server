<template>
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Ansprechpartner bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" :model-value="ansprechpartner.name" @change="name=>patchAnsprechpartner({name}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Vorname" :model-value="ansprechpartner.vorname" @change="vorname=>patchAnsprechpartner({vorname}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Anrede" :model-value="ansprechpartner.anrede" @change="anrede=>patchAnsprechpartner({anrede}, ansprechpartner.id)" type="text" />
				<svws-ui-text-input placeholder="Titel" :model-value="ansprechpartner.titel" @change="titel=>patchAnsprechpartner({titel}, ansprechpartner.id)" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Abteilung" :model-value="ansprechpartner.abteilung" @change="abteilung=>patchAnsprechpartner({abteilung}, ansprechpartner.id)" type="text" span="full" />
				<svws-ui-text-input placeholder="Telefon" :model-value="ansprechpartner.telefon" @change="telefon=>patchAnsprechpartner({telefon}, ansprechpartner.id)" type="tel" span="full" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" :model-value="ansprechpartner.email" @change="email=>patchAnsprechpartner({email}, ansprechpartner.id)" type="email" verify-email span="full" />
				<span class="opacity-50 col-span-full text-sm mt-3">Die Daten werden automatisch gespeichert.</span>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<!-- <svws-ui-button type="secondary" @click="$refs.modalEdit.closeModal"> Abbrechen </svws-ui-button> -->
			<svws-ui-button type="primary" @click="show = false"> Fertig </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button type="icon" @click="show = true" title="Ansprechpartner bearbeiten">
		<span class="icon i-ri-edit-2-line" />
	</svws-ui-button>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { BetriebAnsprechpartner } from "@core";

	const props = defineProps<{
		ansprechpartner: BetriebAnsprechpartner;
		patchAnsprechpartner: (data : Partial<BetriebAnsprechpartner>, id : number) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

</script>
