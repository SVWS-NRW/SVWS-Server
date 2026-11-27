<template>
	<svws-ui-modal v-model:show="show" size="small" :auto-close="!busy" :close-in-title="!busy">
		<template #modalTitle>Lehrkraft-Zuordnung bearbeiten</template>
		<template #modalContent>
			<div v-if="zuordnung !== undefined && lerngruppe !== null" class="flex flex-col gap-3">
				<p class="flex items-center gap-2 font-semibold">{{ presenter.lerngruppeBezeichnung(lerngruppe) }}<s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(lerngruppe)" /></p>
				<svws-ui-select title="Lehrkraft" :items="verfuegbareLehrer" :item-text="item => `${item.nachname}, ${item.vorname} (${item.kuerzel})`"
					:model-value="gewaehlterLehrer" :disabled="busy || fixiert" @update:model-value="item => idLehrer = item?.id ?? null" />
				<s-uv-lehrer-fixierung :id-lerngruppe="lerngruppe.id"
					:id-lehrer="zuordnung.idLehrer"
					:disabled="pending || idLehrer !== zuordnung.idLehrer"
					@busy="fixierungPending = $event" />
				<div class="grid grid-cols-2 gap-3">
					<svws-ui-input-number placeholder="Reihenfolge" :model-value="reihenfolge" :disabled="busy" :min="1" :steps="false"
						@change="value => reihenfolge = value ?? Number.NaN" />
					<svws-ui-input-number placeholder="Wochenstunden"
						:model-value="wochenstunden"
						:disabled="busy"
						:min="0"
						:max="lerngruppe.wochenstundenUnterrichtet"
						:decimal-places="2"
						:steps="false"
						@change="value => wochenstunden = value ?? Number.NaN" />
				</div>
				<svws-ui-input-number placeholder="Davon angerechnet"
					:model-value="wochenstundenAngerechnet"
					:disabled="busy"
					:min="0"
					:max="wochenstunden"
					:decimal-places="2"
					:steps="false"
					@change="value => wochenstundenAngerechnet = value ?? Number.NaN" />
				<p class="text-sm" :class="restNachAenderung !== null && restNachAenderung < 0 ? 'text-ui-danger' : 'text-ui-secondary'">
					Reststunden nach Änderung: {{ restNachAenderung === null ? 'nicht berechenbar' : `${stunden(restNachAenderung)} h` }}
				</p>
				<p v-if="loeschenBestaetigen" class="text-sm text-ui-danger">Die Zuordnung dieser Lehrkraft zur Lerngruppe wirklich entfernen?</p>
				<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" :disabled="busy" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button v-if="!loeschenBestaetigen"
				type="secondary"
				:disabled="busy || fixiert"
				:title="fixiert ? 'Zuerst die Fixierung lösen' : undefined"
				@click="loeschenBestaetigen = true">
				Entfernen
			</svws-ui-button>
			<svws-ui-button v-if="loeschenBestaetigen" type="secondary" :disabled="busy || fixiert" @click="entfernen">Entfernen bestätigen</svws-ui-button>
			<svws-ui-button v-else :disabled="busy || !gueltig" @click="speichern">Speichern</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import type { UvLerngruppenLehrer } from '@core/core/data/uv/UvLerngruppenLehrer';
	import { useUvState } from '@ui/states/UvState';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { createUvPresenter, formatUvStunden as stunden } from '../UvPresenter';

	import SUvLehrerFixierung from './SUvLehrerFixierung.vue';
	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		idZuordnung: number;
		patchZuordnung: (id: number, data: Partial<UvLerngruppenLehrer>) => Promise<void>;
		removeZuordnung: (id: number) => Promise<void>;
	}>();
	const emit = defineEmits<{ closed: []; saved: [aktion: 'geändert' | 'entfernt'] }>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const planungsManager = useUvPlanungsManager();
	const show = ref(true);
	const pending = ref(false);
	const fixierungPending = ref(false);
	const busy = computed(() => pending.value || fixierungPending.value);
	const fehler = ref('');
	const loeschenBestaetigen = ref(false);
	const zuordnung = computed(() => {
		const plan = state.planungsabschnitt;
		return plan === null ? undefined : [...state.uvManager.lerngruppenLehrerGetMengeByPlanungsabschnitt(plan)].find(item => item.id === props.idZuordnung);
	});
	const lerngruppe = computed(() => zuordnung.value === undefined ? null : state.uvManager.lerngruppeGetByIdOrNull(zuordnung.value.idLerngruppe));
	const fixiert = computed(() => zuordnung.value === undefined ? false
		: planungsManager.value?.lehrerIstFixiert(zuordnung.value.idLerngruppe, zuordnung.value.idLehrer) ?? false);
	const idLehrer = ref<number | null>(zuordnung.value?.idLehrer ?? null);
	const reihenfolge = ref(zuordnung.value?.reihenfolge ?? 1);
	const wochenstunden = ref(zuordnung.value?.wochenstunden ?? 0);
	const wochenstundenAngerechnet = ref(zuordnung.value?.wochenstundenAngerechnet ?? 0);
	const verfuegbareLehrer = computed(() => lerngruppe.value === null || zuordnung.value === undefined ? []
		: planungsManager.value?.lehrerFuerZuordnung(lerngruppe.value, props.idZuordnung) ?? []);
	const gewaehlterLehrer = computed(() => verfuegbareLehrer.value.find(item => item.id === idLehrer.value));
	const gueltig = computed(() => lerngruppe.value !== null && idLehrer.value !== null
		&& (planungsManager.value?.lehrerZuordnungGueltig(lerngruppe.value, idLehrer.value, props.idZuordnung,
			reihenfolge.value, wochenstunden.value, wochenstundenAngerechnet.value) ?? false));
	const restNachAenderung = computed(() => idLehrer.value === null ? null
		: planungsManager.value?.lehrerRestNachZuordnung(idLehrer.value, props.idZuordnung, wochenstundenAngerechnet.value) ?? null);

	watch(show, value => {
		if (!value) {
			emit('closed');
		}
	});


	async function speichern(): Promise<void> {
		if (busy.value || !gueltig.value || idLehrer.value === null) {
			return;
		}
		if (fixiert.value && idLehrer.value !== zuordnung.value?.idLehrer) {
			return;
		}
		pending.value = true;
		fehler.value = '';
		try {
			await props.patchZuordnung(props.idZuordnung, { idLehrer: idLehrer.value, reihenfolge: reihenfolge.value,
				wochenstunden: wochenstunden.value, wochenstundenAngerechnet: wochenstundenAngerechnet.value });
			emit('saved', 'geändert');
			show.value = false;
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Zuordnung konnte nicht gespeichert werden.';
		} finally {
			pending.value = false;
		}
	}

	async function entfernen(): Promise<void> {
		if (busy.value || fixiert.value) {
			return;
		}
		pending.value = true;
		fehler.value = '';
		try {
			await props.removeZuordnung(props.idZuordnung);
			emit('saved', 'entfernt');
			show.value = false;
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Zuordnung konnte nicht entfernt werden.';
		} finally {
			pending.value = false;
		}
	}
</script>
