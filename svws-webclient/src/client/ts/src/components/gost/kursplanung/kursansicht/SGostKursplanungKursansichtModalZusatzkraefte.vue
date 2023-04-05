<template>
	<div>
		<svws-ui-modal ref="zusatzkraefte_modal" size="small">
			<template #modalTitle>Zusatzkräfte für Kurs {{ kursbezeichnung }}</template>
			<template #modalContent>
				<s-gost-kursplanung-kursansicht-select-kurslehrer :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
					:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_zusatzkraefte_modal">Fertig</svws-ui-button>
			</template>
		</svws-ui-modal>
		<svws-ui-button size="small" type="secondary" @click="toggle_zusatzkraefte_modal">Zusatzkräfte anlegen {{ anzahl_zusatzkraefte }}<i-ri-briefcase-line class="ml-1 my-0.5" /></svws-ui-button>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, LehrerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, ref, Ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		kurs: GostBlockungKurs;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const kursbezeichnung: ComputedRef<string> = computed(() => props.getDatenmanager().getNameOfKurs(props.kurs.id));

	const zusatzkraefte_modal: Ref<any> = ref(null);
	function toggle_zusatzkraefte_modal() {
		zusatzkraefte_modal.value.isOpen ? zusatzkraefte_modal.value.closeModal() : zusatzkraefte_modal.value.openModal();
	}

	const anzahl_zusatzkraefte = computed(()=>{
		const nr = props.getDatenmanager().getOfKursLehrkraefteSortiert(props.kurs.id).size();
		return nr ? `(${nr})` : ""
	})

</script>