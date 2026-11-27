<template>
	<div class="flex flex-col gap-3">
		<svws-ui-select :model-value="definition"
			:items="uvRegelDefinitionen"
			:item-text="item => `${item.kategorie} · ${item.typ.bezeichnung}`"
			:item-filter="definitionenFiltern"
			title="Regeltyp"
			autocomplete
			:disabled="pending"
			@update:model-value="item => typWaehlen(item?.typ.nr ?? 0)" />
		<template v-if="definition">
			<div v-for="(feld, index) in definition.felder" :key="index">
				<svws-ui-select v-if="istAuswahl(feld.art)" :model-value="optionen(feld.art).find(item => item.id === werte[index]) ?? null" autocomplete
					:items="optionen(feld.art)" :item-text="item => item.text" :item-filter="optionenFiltern" :title="feld.name" :disabled="pending"
					@update:model-value="item => wertSetzen(index, item?.id ?? null)" />
				<svws-ui-input-number v-else
					:model-value="werte[index] ?? null"
					:placeholder="feld.name"
					:min="0"
					:step="1"
					:valid="value => value !== null && Number.isInteger(value)"
					:disabled="pending"
					@change="value => wertSetzen(index, value)" />
			</div>
			<div v-if="definition.liste" class="rounded border border-ui-25 p-2">
				<p class="mb-2 text-sm font-semibold">{{ definition.liste === 'jahrgang' ? 'Jahrgänge' : 'Lerngruppen' }} auswählen</p>
				<div class="grid max-h-48 grid-cols-1 gap-1 overflow-auto sm:grid-cols-2">
					<label v-for="option in optionen(definition.liste)" :key="option.id" class="flex items-center gap-2 rounded p-1 hover:bg-ui-selected">
						<input type="checkbox"
							:checked="listenwerte.includes(option.id)"
							:disabled="pending"
							@change="listeUmschalten(option.id)"><span class="truncate text-sm">{{ option.text }}</span>
					</label>
				</div>
			</div>
			<svws-ui-select :model-value="prioritaeten.find(item => item.id === entwurf.prioritaet)"
				:items="prioritaeten"
				:item-text="item => item.text"
				title="Priorität"
				:disabled="pending"
				@update:model-value="item => prioritaetSetzen(item?.id ?? 2)" />
		</template>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	import { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
	import { useUvState } from '@ui/states/UvState';

	import { createUvPresenter, uvRegelPrioritaeten as prioritaeten } from '../UvPresenter';

	import { type UvRegelDefinition, uvRegelDefinitionen, type UvRegelFeldArt } from './UvRegelFelder';

	const props = defineProps<{
		entwurf: UvBlockungRegel;
		werte: (number | null)[];
		listenwerte: number[];
		pending: boolean;
	}>();
	const emit = defineEmits<{ 'update:entwurf': [value: UvBlockungRegel]; 'update:werte': [value: (number | null)[]]; 'update:listenwerte': [value: number[]] }>();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));
	const definition = computed(() => uvRegelDefinitionen.find(item => item.typ.nr === props.entwurf.typ));

	const optionenNachArt = computed(() => {
		const plan = state.planungsabschnitt;
		const result = new Map<UvRegelFeldArt, { id: number; text: string }[]>();
		if (plan !== null) {
			for (const art of ['lehrer', 'klasse', 'lerngruppe', 'fach', 'jahrgang', 'aktivierung'] as const) {
				result.set(art, presenter.value.regelOptionen(art, plan));
			}
		}
		return result;
	});

	function istAuswahl(art: UvRegelFeldArt): boolean {
		return ['lehrer', 'klasse', 'lerngruppe', 'fach', 'jahrgang', 'aktivierung'].includes(art);
	}

	function optionen(art: UvRegelFeldArt): { id: number; text: string }[] {
		return optionenNachArt.value.get(art) ?? [];
	}

	function optionenFiltern(items: { id: number; text: string }[], searchText: string): { id: number; text: string }[] {
		const suche = searchText.trim().toLocaleLowerCase('de');
		return items.filter(item => item.text.toLocaleLowerCase('de').includes(suche));
	}

	function definitionenFiltern(items: UvRegelDefinition[], searchText: string): UvRegelDefinition[] {
		const suche = searchText.trim().toLocaleLowerCase('de');
		return items.filter(item => `${item.kategorie} · ${item.typ.bezeichnung}`.toLocaleLowerCase('de').includes(suche));
	}

	function typWaehlen(typ: number): void {
		emit('update:entwurf', Object.assign(new UvBlockungRegel(), props.entwurf, { typ }));
		emit('update:werte', []);
		emit('update:listenwerte', []);
	}

	function prioritaetSetzen(prioritaet: number): void {
		emit('update:entwurf', Object.assign(new UvBlockungRegel(), props.entwurf, { prioritaet }));
	}

	function wertSetzen(index: number, wert: number | null): void {
		const werte = [...props.werte];
		werte[index] = wert;
		emit('update:werte', werte);
	}

	function listeUmschalten(id: number): void {
		emit('update:listenwerte', props.listenwerte.includes(id) ? props.listenwerte.filter(item => item !== id) : [...props.listenwerte, id]);
	}
</script>
