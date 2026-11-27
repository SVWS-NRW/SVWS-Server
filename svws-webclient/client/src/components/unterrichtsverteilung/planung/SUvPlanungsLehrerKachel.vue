<template>
	<div class="group relative flex min-w-0 flex-col gap-2 rounded-lg border border-ui-25 bg-ui p-3 shadow-sm transition-colors hover:border-ui-brand"
		:class="uvAuswahlKlasse(auswahlStatus)" :draggable @dragstart="dragStart">
		<component :is="auswahlAngebot ? 'div' : 'button'" :type="auswahlAngebot ? undefined : 'button'"
			class="block w-full min-w-0 text-left focus-visible:outline-2 focus-visible:outline-ui-brand" :title="`${kuerzel} · ${name}`"
			:disabled="modus === 'abschnitt-entfernen' || auswahlGesperrt" @click="lehrkraftAuswaehlen">
			<div class="flex items-start gap-2">
				<input v-if="modus === 'abschnitt-hinzufuegen'" type="checkbox" class="mt-1 shrink-0" :checked="ausgewaehlt"
					:disabled="pending" :aria-label="`${kuerzel} auswählen`" @change="props.auswahlUmschalten?.()">
				<div class="min-w-0 flex-1">
					<div class="flex items-center justify-between gap-2">
						<span class="rounded bg-ui-brand-secondary px-2 py-0.5 text-xs font-bold text-ui-brand">{{ kuerzel }}</span>
						<slot name="rechts">
							<span v-if="zeigtStunden" class="whitespace-nowrap text-xs font-semibold"
								:class="rest === null ? 'text-ui-secondary' : rest < 0 ? 'text-ui-danger' : 'text-ui-success'">
								{{ rest === null ? 'ohne Soll' : `${stunden(rest)} h Rest` }}
							</span>
						</slot>
					</div>
					<div class="mt-2 truncate text-sm font-medium">{{ name }}</div>
					<div v-if="faecher.length > 0" class="mt-1 flex flex-wrap gap-1">
						<s-uv-fach-badge v-for="fach in faecher" :key="fach.id" :kuerzel="fach.kuerzel" :kuerzel-statistik="fach.kuerzelStatistik" />
					</div>
				</div>
			</div>
			<span v-if="zeigtStunden" class="mt-1 block text-xs text-ui-secondary">
				{{ stunden(verplant) }} h Unterricht · {{ stunden(anrechnung) }} h Anrechnung<span v-if="soll !== null"> · {{ stunden(soll) }} h Soll</span>
			</span>
			<span v-if="zugeordnet" class="mt-1 block text-xs font-semibold text-ui-brand">Bereits zugeordnet</span>
		</component>

		<slot name="details" />

		<div v-if="auslastung !== null || aktion !== null" class="mt-auto flex flex-col gap-2">
			<meter v-if="auslastung !== null" class="sr-only" :aria-label="`Unterricht und Anrechnung von ${kuerzel}`"
				:value="auslastung.wert" :min="0" :max="auslastung.maximum" />
			<div v-if="auslastung !== null" class="h-1 overflow-hidden rounded-full bg-ui-neutral" aria-hidden="true">
				<div class="h-full rounded-full"
					:class="rest !== null && rest < 0 ? 'bg-ui-danger' : 'bg-ui-success'"
					:style="{ width: `${auslastung.prozent}%` }" />
			</div>
			<div v-if="aktion !== null" class="flex items-center justify-end gap-1 border-t border-ui-10 pt-1">
				<svws-ui-button v-if="aktion === 'zuordnen'" type="secondary" title="Lehrkraft zuordnen" @click="props.zuordnen?.()">Zuordnen</svws-ui-button>
				<svws-ui-button v-else-if="aktion === 'entfernen'" type="icon" :disabled="pending || entfernenGesperrt"
					:title="entfernenGesperrt ? 'Lehrkraft wird in diesem Planungsabschnitt verwendet' : 'Lehrkraft aus dem Planungsabschnitt entfernen'"
					@click="props.entfernen?.()">
					<span class="icon i-ri-subtract-line" />
				</svws-ui-button>
				<svws-ui-button v-else
					type="icon"
					:disabled="pending"
					:title="modus === 'abschnitt-hinzufuegen' ? `${kuerzel} hinzufügen` : `${kuerzel} zuordnen`"
					@click="props.zuordnen?.()">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</div>
		</div>

		<slot name="aktionen" />
		<s-uv-details-oeffnen v-if="detailsSichtbar" variante="gross" :oeffnen="() => props.detailsAnzeigen?.()" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import SUvFachBadge from '../SUvFachBadge.vue';
	import { formatUvStunden as stunden, lehrerAuslastung, type UvFachAnzeige } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import { uvAuswahlKlasse } from './UvAuswahlDarstellung';
	import { uvDragDatenSchreiben } from './UvDragDaten';
	import type { UvAuswahlStatus } from './UvPlanung';

	const props = withDefaults(defineProps<{
		modus?: 'uebersicht' | 'abschnitt-hinzufuegen' | 'abschnitt-entfernen' | 'zuordnung-auswahl' | 'zuordnung-detail';
		kuerzel: string;
		name: string;
		faecher: UvFachAnzeige[];
		verplant?: number;
		anrechnung?: number;
		soll?: number | null;
		rest?: number | null;
		auswahlStatus?: UvAuswahlStatus;
		zugeordnet?: boolean;
		zuordnenMoeglich?: boolean;
		auswahlGesperrt?: boolean;
		ausgewaehlt?: boolean;
		entfernenGesperrt?: boolean;
		pending?: boolean;
		idLehrer?: number;
		auswaehlen?: () => void;
		zuordnen?: () => void;
		entfernen?: () => void;
		auswahlUmschalten?: () => void;
		detailsAnzeigen?: () => void;
	}>(), {
		modus: 'uebersicht',
		verplant: 0,
		anrechnung: 0,
		soll: null,
		rest: null,
		auswahlStatus: 'neutral',
		idLehrer: undefined,
		auswaehlen: undefined,
		zuordnen: undefined,
		entfernen: undefined,
		auswahlUmschalten: undefined,
		detailsAnzeigen: undefined,
	});

	const emit = defineEmits<{
		'drag-start': [event: DragEvent];
	}>();

	// Der Modus legt die Darstellung fest; einzelne Zustände wie Auswahl und Ladezustand bleiben unabhängig.
	const auswahlAngebot = computed(() => props.modus === 'abschnitt-hinzufuegen' || props.modus === 'zuordnung-auswahl');
	const zeigtStunden = computed(() => props.modus !== 'abschnitt-hinzufuegen' && props.modus !== 'zuordnung-detail');
	const draggable = computed(() => !props.pending && (auswahlAngebot.value || (props.modus === 'abschnitt-entfernen' && !props.entfernenGesperrt)));
	const detailsSichtbar = computed(() => props.auswahlStatus === 'direkt' && (props.modus === 'uebersicht' || props.modus === 'zuordnung-detail'));
	const auslastung = computed(() => !zeigtStunden.value || props.soll === null || props.soll <= 0 || props.rest === null
		? null : lehrerAuslastung(props.soll, props.rest));
	const aktion = computed(() => {
		if (props.modus === 'abschnitt-entfernen') {
			return 'entfernen';
		}
		if (auswahlAngebot.value) {
			return 'hinzufuegen';
		}

		return props.zuordnenMoeglich ? 'zuordnen' : null;
	});

	function lehrkraftAuswaehlen(): void {
		if (!auswahlAngebot.value) {
			props.auswaehlen?.();
		}
	}

	function dragStart(event: DragEvent): void {
		if (auswahlAngebot.value && !props.pending) {
			emit('drag-start', event);
			return;
		}
		if (props.modus !== 'abschnitt-entfernen' || props.pending || props.entfernenGesperrt || props.idLehrer === undefined || event.dataTransfer === null) {
			return;
		}

		uvDragDatenSchreiben(event.dataTransfer, { typ: 'lehrer-mitgliedschaft', aktion: 'entfernen', id: props.idLehrer });
	}

</script>
