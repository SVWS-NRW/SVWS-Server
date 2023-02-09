<template>
	<svws-ui-modal ref="zusatzkraefte_modal" size="small">
		<template #modalTitle>Zusatzkräfte für Kurs {{ kursbezeichnung }}</template>
		<template #modalContent>
			<s-gost-kursplanung-kursansicht-select-kurslehrer :kurs="kurs" :map-lehrer="mapLehrer" :manager="manager" :blockung="blockung" />
		</template>
		<template #modalActions>
			<svws-ui-button @click="toggle_zusatzkraefte_modal">Fertig</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button size="small" type="secondary" @click="toggle_zusatzkraefte_modal">Zusatzkräfte anlegen<i-ri-briefcase-line class="ml-1 my-0.5" /></svws-ui-button>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsdatenManager, LehrerListeEintrag } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref } from 'vue';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';

	const props = defineProps<{
		kurs: GostBlockungKurs
		manager: GostBlockungsdatenManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		blockung: DataGostKursblockung;
	}>();

	const kursbezeichnung: ComputedRef<String> = computed(() => props.manager.getNameOfKurs(props.kurs.id));

	const zusatzkraefte_modal: Ref<any> = ref(null);
	function toggle_zusatzkraefte_modal() {
		zusatzkraefte_modal.value.isOpen ? zusatzkraefte_modal.value.closeModal() : zusatzkraefte_modal.value.openModal();
	}

</script>