<template>
	<div class="flex flex-col gap-2">
		<svws-ui-modal :show="showModal" size="small" class="hidden">
			<template #modalTitle>Zusatzkräfte für Kurs {{ kursbezeichnung }}</template>
			<template #modalContent>
				<s-gost-kursplanung-kursansicht-select-kurslehrer :kurs="kurs" :map-lehrer="mapLehrer" :get-datenmanager="getDatenmanager"
					:add-regel="addRegel" :add-kurs-lehrer="addKursLehrer" :remove-kurs-lehrer="removeKursLehrer" />
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="toggle_zusatzkraefte_modal">Schließen</svws-ui-button>
			</template>
		</svws-ui-modal>
		<div>
			<div class="text-sm font-bold">Zusatzkräfte<span v-if="anzahl_zusatzkraefte" class="font-normal">: {{ [...getDatenmanager().kursGetLehrkraefteSortiert(kurs.id)].map(lehrer => lehrer?.kuerzel).join(", ") }}</span></div>
		</div>
		<div>
			<svws-ui-button type="secondary" @click="toggle_zusatzkraefte_modal">
				<template v-if="anzahl_zusatzkraefte">
					Bearbeiten
				</template>
				<template v-else>
					Anlegen
				</template>
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungRegel, GostBlockungsdatenManager, LehrerListeEintrag } from "@core";
	import { computed, ref, type ComputedRef } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		kurs: GostBlockungKurs;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const kursbezeichnung: ComputedRef<string> = computed(() => props.getDatenmanager().kursGetName(props.kurs.id));

	function toggle_zusatzkraefte_modal() {
		showModal().value = !showModal().value;
	}

	const anzahl_zusatzkraefte = computed(() => {
		const nr = props.getDatenmanager().kursGetLehrkraefteSortiert(props.kurs.id).size();
		return nr ? `${nr}` : ""
	});

</script>
