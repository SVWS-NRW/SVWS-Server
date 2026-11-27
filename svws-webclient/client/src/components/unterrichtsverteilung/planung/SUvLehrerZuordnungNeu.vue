<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">{{ bestehendeAnzahl === 0 ? 'Lehrkraft zuordnen' : 'Weitere Lehrkraft zuordnen' }}</h3>
			<svws-ui-button type="secondary" :disabled="pending" @click="props.schliessen()">Abbrechen</svws-ui-button>
		</div>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<div v-if="lerngruppe !== null && lehrer !== null" class="flex flex-col gap-3">
				<p>
					<span class="inline-flex items-center gap-2 font-semibold">{{ presenter.lerngruppeBezeichnung(lerngruppe) }}<s-uv-fach-badge :id-fach="presenter.lerngruppeFachId(lerngruppe)" /></span>
					<span class="text-ui-secondary"> · {{ stunden(lerngruppe.wochenstunden) }} Wochenstunden</span>
				</p>
				<p>Lehrkraft: <span class="font-semibold">{{ lehrer.nachname }}, {{ lehrer.vorname }} ({{ lehrer.kuerzel }})</span></p>
				<p v-if="bestehendeAnzahl > 0" class="text-sm text-ui-secondary">{{ bestehendeAnzahl }} Lehrkraft/Lehrkräfte bereits zugeordnet · {{ stunden(bestehendeStunden) }} h eingetragen</p>
				<div class="grid grid-cols-2 gap-3">
					<svws-ui-input-number placeholder="Wochenstunden"
						:model-value="wochenstunden"
						:disabled="pending"
						:min="0"
						:max="lerngruppe.wochenstundenUnterrichtet"
						:decimal-places="2"
						:steps="false"
						@change="value => wochenstunden = value ?? Number.NaN" />
					<svws-ui-input-number placeholder="Davon angerechnet"
						:model-value="angerechnet"
						:disabled="pending"
						:min="0"
						:max="wochenstunden"
						:decimal-places="2"
						:steps="false"
						@change="value => angerechnet = value ?? Number.NaN" />
				</div>
				<dl class="grid grid-cols-2 gap-x-4 gap-y-1 rounded border border-ui-10 p-3 text-sm">
					<dt>Bereits auf Deputat angerechnet</dt><dd class="text-right">{{ stunden(deputat) }} h</dd>
					<dt>Pflichtstundensoll</dt><dd class="text-right">{{ soll === null ? 'Nicht hinterlegt' : `${stunden(soll)} h` }}</dd>
					<dt>Anrechnungsstunden</dt><dd class="text-right">{{ stunden(anrechnung) }} h</dd>
					<dt class="font-semibold">Rest nach Zuordnung</dt><dd class="text-right font-semibold" :class="rest !== null && rest < 0 ? 'text-ui-danger' : ''">{{ rest === null ? 'Nicht berechenbar' : `${stunden(rest)} h` }}</dd>
				</dl>
				<p v-if="rest !== null && rest < 0" class="text-sm text-ui-danger">Die Zuordnung überschreitet das verbleibende Pflichtstundensoll.</p>
				<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
			</div>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button :disabled="pending || !gueltig" @click="speichern">Zuordnen</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import { useUvState } from '@ui/states/UvState';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { createUvPresenter, formatUvStunden as stunden } from '../UvPresenter';

	import { useUvPlanungsManager } from './UvPlanungsKontext';

	const props = defineProps<{
		idLerngruppe: number;
		idLehrer: number;
		assignLehrer: (idLerngruppe: number, idLehrer: number, wochenstunden: number, angerechnet: number) => Promise<void>;
		schliessen: () => void;
	}>();
	const emit = defineEmits<{
		saved: [];
	}>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const pending = ref(false);
	const fehler = ref('');
	const lerngruppe = computed(() => state.uvManager.lerngruppeGetByIdOrNull(props.idLerngruppe));
	const lehrer = computed(() => state.uvManager.lehrerGetByIdOrNull(props.idLehrer));
	const planungsManager = useUvPlanungsManager();
	const bestehendeAnzahl = computed(() => planungsManager.value?.lerngruppeZuordnungsAnzahl(props.idLerngruppe) ?? 0);
	const bestehendeStunden = computed(() => planungsManager.value?.lerngruppeZuordnungsStunden(props.idLerngruppe) ?? 0);
	const wochenstunden = ref(lerngruppe.value === null ? 0 : Math.max(0, lerngruppe.value.wochenstundenUnterrichtet - bestehendeStunden.value));
	const angerechnet = ref(0);
	const gueltig = computed(() => lerngruppe.value !== null
		&& (planungsManager.value?.neueLehrerZuordnungGueltig(lerngruppe.value, props.idLehrer, wochenstunden.value, angerechnet.value) ?? false));
	const lehrerStunden = computed(() => planungsManager.value?.lehrerStunden(props.idLehrer) ?? { verplant: 0, deputat: 0, soll: null, anrechnung: 0 });
	const deputat = computed(() => lehrerStunden.value.deputat);
	const soll = computed(() => lehrerStunden.value.soll);
	const anrechnung = computed(() => lehrerStunden.value.anrechnung);
	const rest = computed(() => soll.value === null ? null : soll.value - anrechnung.value - deputat.value - angerechnet.value);

	async function speichern(): Promise<void> {
		if (pending.value || !gueltig.value) {
			return;
		}
		pending.value = true;
		fehler.value = '';
		try {
			await props.assignLehrer(props.idLerngruppe, props.idLehrer, wochenstunden.value, angerechnet.value);
			emit('saved');
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Lehrkraft konnte nicht zugeordnet werden.';
		} finally {
			pending.value = false;
		}
	}
</script>
