<template>
	<div class="flex min-h-0 flex-1 flex-col">
		<div class="flex flex-wrap items-center gap-2 border-b border-ui-10 p-3">
			<div class="min-w-0 flex-1"><h3 class="font-semibold">Regeln</h3><p class="text-xs text-ui-secondary">Vorgaben und Wünsche für die Blockung des Planungsabschnitts</p></div>
			<svws-ui-button v-if="state.hatKompetenzAendern" type="primary" @click="erstellen()">Regel erstellen</svws-ui-button>
		</div>
		<div class="grid grid-cols-1 gap-2 border-b border-ui-10 p-3 lg:grid-cols-2">
			<svws-ui-text-input v-model="suche" type="search" placeholder="Regeln suchen" removable />
			<svws-ui-select v-model="filter" :items="kategorien" :item-text="item => item" title="Kategorie" removable />
		</div>
		<svws-ui-notification v-if="status.konflikt" type="error" class="m-3">{{ status.konflikt }}</svws-ui-notification>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<div v-for="gruppe in gruppen" :key="gruppe.name" class="mb-4">
				<h4 class="mb-2 border-b border-ui-25 pb-1 text-xs font-semibold text-ui-secondary">{{ gruppe.name }} · {{ gruppe.regeln.length }}</h4>
				<div class="flex flex-col gap-2">
					<div v-for="regel in gruppe.regeln"
						:key="regel.id"
						class="group relative flex items-start gap-2 rounded border border-ui-25 bg-ui p-2"
						:class="uvAuswahlKlasse(auswahlManager?.auswahlStatus(auswahl, 'regeln', regel.id) ?? 'neutral')">
						<button type="button"
							class="min-w-0 flex-1 text-left focus-visible:outline-2 focus-visible:outline-ui-brand"
							@click="auswaehlen(regel.id)">
							<span class="block text-sm font-semibold">{{ beschreibung(regel) }}</span><span class="mt-1 block text-xs" :class="status.fehlerhafteIds.has(regel.id) ? 'text-ui-danger' : 'text-ui-secondary'">{{ status.fehlerhafteIds.has(regel.id) ? 'Ungültige Parameter · ' : '' }}{{ formatUvRegelPrioritaet(regel.prioritaet) }}</span>
						</button>
						<svws-ui-button v-if="state.hatKompetenzAendern"
							type="icon"
							title="Regel bearbeiten"
							@click="bearbeiten(regel)">
							<span class="icon i-ri-edit-2-line" />
						</svws-ui-button>
						<svws-ui-button v-if="state.hatKompetenzAendern" type="trash" title="Regel löschen" @click="deleteKandidat = regel" />
						<s-uv-details-oeffnen v-if="auswahlManager?.auswahlStatus(auswahl, 'regeln', regel.id) === 'direkt'" :oeffnen="() => detailsAnzeigen(regel.id)" />
					</div>
				</div>
			</div>
			<p v-if="gruppen.length === 0" class="text-sm text-ui-secondary">{{ suche || filter ? 'Keine passenden Regeln.' : 'Noch keine Regeln vorhanden.' }}</p>
		</div>
		<svws-ui-notification v-if="fehler && !dialog" type="error" class="m-3">{{ fehler }}</svws-ui-notification>
		<svws-ui-modal v-model:show="dialog" class="hidden" size="medium" :auto-close="!pending" :close-in-title="!pending">
			<template #modalTitle>Regel bearbeiten</template>
			<template #modalContent>
				<div class="flex max-h-[65vh] flex-col gap-3 overflow-auto p-1">
					<s-uv-regel-formular v-model:entwurf="entwurf" v-model:werte="werte" v-model:listenwerte="listenwerte" :pending />
					<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
				</div>
			</template>
			<template #modalActions><svws-ui-button type="secondary" :disabled="pending" @click="dialog = false">Abbrechen</svws-ui-button><svws-ui-button :disabled="pending || !definition" @click="speichern">Speichern</svws-ui-button></template>
		</svws-ui-modal>
		<svws-ui-modal :show="deleteKandidat !== null" class="hidden" size="small" :auto-close="!pending" :close-in-title="!pending"
			@update:show="schliessenLoeschdialog">
			<template #modalTitle>Regel löschen</template>
			<template #modalContent><p>Diese Regel dauerhaft löschen?</p><svws-ui-notification v-if="fehler" type="error" class="mt-2">{{ fehler }}</svws-ui-notification></template>
			<template #modalActions>
				<svws-ui-button type="secondary" :disabled="pending" @click="deleteKandidat = null">Abbrechen</svws-ui-button>
				<svws-ui-button type="trash" title="Regel endgültig löschen" :disabled="pending" @click="loeschen" />
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
	import { useUvState } from '@ui/states/UvState';

	import { createUvPresenter, formatUvRegelPrioritaet } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import SUvRegelFormular from './SUvRegelFormular.vue';
	import { uvAuswahlKlasse } from './UvAuswahlDarstellung';
	import type { UvPlanungsAuswahl } from './UvPlanung';
	import type { UvPlanungsManager } from './UvPlanungsManager';
	import { uvRegelDefinitionen } from './UvRegelFelder';

	const { auswahl, sortierAuswahl, auswahlManager, auswaehlen, detailsAnzeigen, erstellen } = defineProps<{
		auswahl: UvPlanungsAuswahl | null;
		sortierAuswahl: UvPlanungsAuswahl | null;
		auswahlManager: UvPlanungsManager | null;
		auswaehlen: (id: number) => void;
		detailsAnzeigen: (id: number) => void;
		erstellen: () => void;
	}>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const suche = ref('');
	const filter = ref<string | null>(null);
	const dialog = ref(false);
	const pending = ref(false);
	const fehler = ref('');
	const entwurf = ref(new UvBlockungRegel());
	const deleteKandidat = ref<UvBlockungRegel | null>(null);
	const werte = ref<(number | null)[]>([]);
	const listenwerte = ref<number[]>([]);
	const definitionen = uvRegelDefinitionen;
	const kategorien = [...new Set(definitionen.map(item => item.kategorie))];

	const definition = computed(() => definitionen.find(item => item.typ.nr === entwurf.value.typ));
	// Regelprüfung und gruppierte Anzeige

	const status = computed(() => auswahlManager?.regelStatus() ?? { fehlerhafteIds: new Set<number>(), konflikt: '' });
	const gruppen = computed(() => auswahlManager?.regelGruppen(filter.value, suche.value, sortierAuswahl) ?? []);

	function beschreibung(regel: UvBlockungRegel): string {
		const plan = state.planungsabschnitt;
		return plan === null ? `Regeltyp ${regel.typ}` : presenter.value.regelBeschreibung(regel, plan);
	}

	function bearbeiten(regel: UvBlockungRegel): void {
		entwurf.value = Object.assign(new UvBlockungRegel(), regel);
		const count = definitionen.find(item => item.typ.nr === regel.typ)?.felder.length ?? 0;
		werte.value = [...regel.parameter].slice(0, count);
		listenwerte.value = [...regel.parameter].slice(count);
		fehler.value = '';
		dialog.value = true;
	}

	function schliessenLoeschdialog(geoeffnet: boolean): void {
		if (!geoeffnet) {
			deleteKandidat.value = null;
		}
	}

	async function speichern(): Promise<void> {
		if (pending.value || auswahlManager === null) {
			return;
		}
		try {
			const regel = auswahlManager.regelEntwurf(entwurf.value, werte.value, listenwerte.value);
			pending.value = true;
			await state.regeln.patch(regel);
			dialog.value = false;
			fehler.value = '';
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Regel konnte nicht gespeichert werden.';
		} finally {
			pending.value = false;
		}
	}

	async function loeschen(): Promise<void> {
		const regel = deleteKandidat.value;
		if (pending.value || regel === null) {
			return;
		}
		pending.value = true;
		fehler.value = '';
		try {
			await state.regeln.loeschen(regel.id);
			deleteKandidat.value = null;
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Regel konnte nicht gelöscht werden.';
		} finally {
			pending.value = false;
		}
	}
</script>
