<template>
	<div class="border-l h-full flex flex-col pl-4 pr-1 gap-4">
		<div class="text-headline-md">{{ schueler.nachname }}, {{ schueler.vorname }}</div>
		<svws-ui-textarea-input placeholder="Floskeln auswählen oder manuell eingeben"	:model-value="text" @change="doPatch" autoresize />
		<div class="flex justify-end gap-2 w-full">
			<svws-ui-button @click="ersetzeTemplates">Anwenden</svws-ui-button>
			<svws-ui-button @click="text = ''">Zurücksetzen</svws-ui-button>
		</div>
		<div class="svws-ui-table svws-clickable overflow-hidden" role="table" aria-label="Tabelle">
			<div class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
				<tr class="svws-ui-tr" role="row">
					<td class="svws-ui-td" role="columnheader">Kürzel</td>
					<td class="svws-ui-td" role="columnheader">Text</td>
					<td class="svws-ui-td" role="columnheader">Niveau</td>
					<td class="svws-ui-td" role="columnheader">Jg</td>
				</tr>
			</div>
			<div class="svws-ui-tbody overflow-y-scroll" role="rowgroup" aria-label="Tabelleninhalt">
				<template v-for="gruppe of manager.daten.floskelgruppen" :key="gruppe.kuerzel">
					<template v-if="(gruppe.hauptgruppe !== null) && (erlaubteHauptgruppen.has(gruppe.hauptgruppe as Hauptgruppen)) && !gruppe.floskeln.isEmpty()">
						<div class="svws-ui-thead cursor-pointer select-none" role="rowgroup">
							<div class="svws-ui-td col-span-4 flex items-center gap-1" role="cell" @click="collapsed.set(gruppe, collapsed.get(gruppe) ? false : true)">
								<span class="icon i-ri-arrow-right-s-line" v-if="collapsed.get(gruppe)" />
								<span class="icon i-ri-arrow-down-s-line" v-else />
								<span> {{ gruppe.bezeichnung }}</span>
							</div>
						</div>
						<div v-for="floskel of gruppe.floskeln" :key="floskel.kuerzel ?? 1" class="svws-ui-tr" role="row" v-show="!collapsed.get(gruppe)" @click="ergaenzeFloskel(floskel)">
							<div class="svws-ui-td" role="cell"> {{ floskel.kuerzel }} </div>
							<div class="svws-ui-td" role="cell"> {{ floskel.text }} </div>
							<div class="svws-ui-td" role="cell"> {{ floskel.niveau }} </div>
							<div class="svws-ui-td" role="cell"> {{ floskel.jahrgangID }} </div>
						</div>
					</template>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import type { EnmManager } from './EnmManager';
	import type { ENMFloskel } from '@core/core/data/enm/ENMFloskel';
	import type { ENMFloskelgruppe } from '@core/core/data/enm/ENMFloskelgruppe';

	type Hauptgruppen = 'ALLG'|'ASV'|'AUE'|'FACH'|'FÖRD'|'FSP'|'VERM'|'VERS'|'ZB';

	const props = defineProps<{
		manager: EnmManager;
		patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
		erlaubteHauptgruppen: Set<Hauptgruppen>;
	}>();

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const text = ref<string>(props.manager.auswahlLeistung.leistung?.fachbezogeneBemerkungen ?? "");

	const every = ref(3);

	const klein = computed(() => new Map([['m', 'er'], ['w', 'sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));
	const gross = computed(() => new Map([['m', 'Er'], ['w', 'Sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));

	function ergaenzeFloskel(floskel: ENMFloskel) {
		if (text.value.length > 0)
			text.value += " ";
		text.value += floskel.text;
	}

	function ersetzeTemplates() {
		const templates = /\$((Vorname)|(Name|Nachname)|(weibl)|(ein)|(\w*%.*))\$/gi;
		let counter = -1;
		text.value = text.value.replaceAll(templates, (match, _, vorname, nachname, weibl, ein, mwdx, _offset, fullString: string, _groups) => {
			if (vorname !== undefined) {
				counter++;
				if ((counter % every.value) === 0)
					return schueler.value.vorname ?? '???';
				return fullString.slice(0, _offset).trimEnd().endsWith('.')
					? gross.value.get(schueler.value.geschlecht ?? 'x') ?? schueler.value.vorname ?? '???'
					: klein.value.get(schueler.value.geschlecht ?? 'x') ?? schueler.value.vorname ?? '???';
			} else if (nachname !== undefined) {
				return schueler.value.nachname ?? '???';
			} else if (weibl !== undefined) {
				return schueler.value.geschlecht === 'w' ? 'in':'';
			} else if (ein !== undefined) {
				return schueler.value.geschlecht === 'w' ? 'in':'e';
			} else if (mwdx !== undefined) {
				const arr = match.slice(1, -1).split('%')
				const mwdxMap = new Map([['m', arr[0] ?? ''], ['w', arr[1] ?? ''], ['d', arr[2] ?? ''], ['x', arr[3] ?? '']]);
				return mwdxMap.get((schueler.value.geschlecht ?? 'x') as 'm'|'w'|'d'|'x')!;
			} else
				return '???';
		});
	}

	const schueler = computed(() => {
		const index = props.manager.auswahlLeistung.indexSchueler;
		return props.manager.lerngruppenAuswahlGetSchueler().get(index);
	})

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const collapsed = ref(new Map<ENMFloskelgruppe, boolean>([...props.manager.daten.floskelgruppen].map(g => [g, false])));

	async function doPatch(fachbezogeneBemerkungen: string | null) {
		text.value = fachbezogeneBemerkungen ?? '';
		await props.patchLeistung({ fachbezogeneBemerkungen });
	}

</script>

<style lang="postcss" scoped>

	.svws-ui-tr {
		grid-template-columns: 6em 1fr 4em 4em;
		min-height: auto;
	}

</style>