<template>
	<svws-ui-modal ref="zusatzkraefte_modal" size="small">
		<template #modalTitle>Zusatzkräfte für Kurs {{ kursbezeichnung }}</template>
		<template #modalContent>
			<s-gost-kursplanung-kursansicht-select-kurslehrer :kurs="kurs" :map-lehrer="mapLehrer" :datenmanager="datenmanager"
				:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
		</template>
		<template #modalActions>
			<svws-ui-button @click="toggle_zusatzkraefte_modal">Fertig</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button size="small" type="secondary" @click="toggle_zusatzkraefte_modal">Zusatzkräfte anlegen<i-ri-briefcase-line class="ml-1 my-0.5" /></svws-ui-button>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, LehrerListeEintrag } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ref, Ref } from 'vue';

	const props = defineProps<{
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		kurs: GostBlockungKurs;
		datenmanager: GostBlockungsdatenManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const kursbezeichnung: ComputedRef<String> = computed(() => props.datenmanager.getNameOfKurs(props.kurs.id));

	const zusatzkraefte_modal: Ref<any> = ref(null);
	function toggle_zusatzkraefte_modal() {
		zusatzkraefte_modal.value.isOpen ? zusatzkraefte_modal.value.closeModal() : zusatzkraefte_modal.value.openModal();
	}

</script>