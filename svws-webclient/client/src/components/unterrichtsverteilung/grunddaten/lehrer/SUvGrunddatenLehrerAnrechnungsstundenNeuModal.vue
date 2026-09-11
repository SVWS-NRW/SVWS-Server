<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="medium">
		<template #modalTitle>Anrechnungsstunden hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<div class="flex flex-col gap-4">
				<ui-select :label="selectLabel" v-model="auswahlNeu" :manager="selectManager" required :removable="false" />
				<svws-ui-input-wrapper :grid="2">
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig ab" v-model="item.gueltigVon" type="date" required />
						<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig bis" v-model="item.gueltigBis" type="date" />
					</div>
					<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" v-model="item.anzahlStunden" required placeholder="Stundenanzahl" :decimal-places="2" :steps="0.5" :min="0" />
				</svws-ui-input-wrapper>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="loading || !hatKompetenzAendern || false"> Anrechnung hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { CoreTypeDataNurSchulformen } from "@core/asd/data/CoreTypeDataNurSchulformen";
	import { LehrerAnrechnungsgrund } from "@core/asd/types/lehrer/LehrerAnrechnungsgrund";
	import { LehrerMehrleistungsarten } from "@core/asd/types/lehrer/LehrerMehrleistungsarten";
	import { LehrerMinderleistungsarten } from "@core/asd/types/lehrer/LehrerMinderleistungsarten";
	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	const props = defineProps<{
		lehrer: UvLehrer;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});

	const show = ref<boolean>(false);
	const abschnittState = useAbschnittState();
	const activeTyp = ref<number>(1);

	const item = ref<UvLehrerAnrechnungsstunden>(new UvLehrerAnrechnungsstunden());

	const auswahlNeu = ref<CoreTypeDataNurSchulformen | null>(null);

	const selectManager = computed(() => {
		let clazz = LehrerAnrechnungsgrund.class;
		if (activeTyp.value === 1) {
			clazz = LehrerMehrleistungsarten.class;
		} else if (activeTyp.value === 2) {
			clazz = LehrerMinderleistungsarten.class;
		}
		return new CoreTypeSelectManager({
			clazz, schuljahr: abschnittState.auswahl.schuljahr,
			selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
		});
	});

	const selectLabel = computed(() => {
		if (activeTyp.value === 1) {
			return "Mehrleistungsgrund";
		}
		if (activeTyp.value === 2) {
			return "Minderleistungsgrund";
		}
		return "Anrechnungsgrund";
	});

	const openModal = (typ: number) => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		activeTyp.value = typ;
		auswahlNeu.value = null;
		show.value = true;
	};

	async function importer() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			if (auswahlNeu.value !== null) {
				const newItem: Partial<UvLehrerAnrechnungsstunden> = {
					idLehrer: props.lehrer.id,
					anrechnungsgrundKrz: auswahlNeu.value.kuerzel,
					anzahlStunden: item.value.anzahlStunden,
					gueltigVon: item.value.gueltigVon,
					gueltigBis: item.value.gueltigBis,
				};
				await state.addAnrechnungsstunden(newItem);
			}
			item.value = new UvLehrerAnrechnungsstunden();
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
