<template>
	<svws-ui-modal ref="modal" class="hidden">
		<template #modalTitle>Ansprechpartner bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name" v-model="name" type="text" span="full" />
				<svws-ui-text-input placeholder="Vorname" v-model="vorname" type="text" span="full" />
				<svws-ui-text-input placeholder="Anrede" v-model="anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="titel" type="text" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Abteilung" v-model="abteilung" type="text" span="full" />
				<svws-ui-text-input placeholder="Telefon" v-model="telefonnr" type="tel" span="full" />
				<svws-ui-text-input placeholder="E-Mail" v-model="email" type="email" verify-email span="full" />
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
	import type { WritableComputedRef } from 'vue';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		ansprechpartner: BetriebAnsprechpartner;
		patchAnsprechpartner: (data : Partial<BetriebAnsprechpartner>, id : number) => Promise<void>;
	}>();

	const modal = ref();

	const name : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.name === null ? undefined : props.ansprechpartner.name,
		set: (value) => void props.patchAnsprechpartner({ name: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const vorname : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.vorname === null ? undefined : props.ansprechpartner.vorname,
		set: (value) => void props.patchAnsprechpartner({ vorname: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const anrede : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.anrede === null ? undefined : props.ansprechpartner.anrede,
		set: (value) => void props.patchAnsprechpartner({ anrede: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const telefonnr : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.telefon === null ? undefined : props.ansprechpartner.telefon,
		set: (value) => void props.patchAnsprechpartner({ telefon: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const email : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.email === null ? undefined : props.ansprechpartner.email,
		set: (value) => void props.patchAnsprechpartner({ email: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const abteilung : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.abteilung === null ? undefined : props.ansprechpartner.abteilung,
		set: (value) => void props.patchAnsprechpartner({ abteilung: value === undefined ? null : value }, props.ansprechpartner.id)
	})

	const titel : WritableComputedRef<string | undefined> = computed({
		get: () => props.ansprechpartner.titel === null ? undefined : props.ansprechpartner.titel,
		set: (value) => void props.patchAnsprechpartner({ titel: value === undefined ? null : value }, props.ansprechpartner.id)
	})

</script>
