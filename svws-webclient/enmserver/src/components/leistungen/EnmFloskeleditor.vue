<template>
	<div class="border-l h-full flex flex-col pl-4 pr-1 gap-4">
		<div class="text-headline-md">{{ schueler.nachname }}, {{ schueler.vorname }}</div>
		<svws-ui-textarea-input placeholder="Floskeln auswählen oder manuell eingeben" :model-value="text ?? ''" @input="text = $event ?? ''" autoresize />
		<div class="flex justify-between gap-2 w-full">
			<div class="flex gap-2">
				<div class="w-20">
					<svws-ui-input-number :model-value="every" :min="1" :max="9" @update:model-value="value => every = value ?? 1" />
				</div>
				<span class="mt-2">Vorname jedes {{ every === 1 ? '':`${every}.` }} Mal</span>
			</div>
			<div v-if="(text !== undefined) && (text !== props.manager.auswahlLeistung.leistung?.fachbezogeneBemerkungen)" class="flex gap-2">
				<svws-ui-button @click="doPatchLeistung" :type="clean ? 'primary':'secondary'">{{ clean ? 'Speichern':'Anwenden' }}</svws-ui-button>
				<svws-ui-button @click="text = ''">Zurücksetzen</svws-ui-button>
			</div>
		</div>
		<div class="svws-ui-table svws-clickable overflow-hidden" role="table" aria-label="Tabelle">
			<div class="svws-ui-thead" role="rowgroup" aria-label="Tabellenkopf">
				<div class="svws-ui-tr" role="row">
					<div class="svws-ui-td" role="columnheader">Kürzel</div>
					<div class="svws-ui-td" role="columnheader">Text</div>
					<div class="svws-ui-td" role="columnheader">Niveau</div>
					<div class="svws-ui-td" role="columnheader">Jg</div>
				</div>
			</div>
			<div class="svws-ui-tbody overflow-y-scroll" role="rowgroup" aria-label="Tabelleninhalt">
				<template v-for="[gruppe, floskeln] of gruppenMap" :key="gruppe.kuerzel">
					<div class="svws-ui-thead cursor-pointer select-none" role="rowgroup">
						<div class="svws-ui-td col-span-4 flex items-center gap-1" role="cell" @click="collapsed.set(gruppe, collapsed.get(gruppe) ? false : true)">
							<span class="icon i-ri-arrow-right-s-line" v-if="collapsed.get(gruppe)" />
							<span class="icon i-ri-arrow-down-s-line" v-else />
							<span> {{ gruppe.bezeichnung }}</span>
						</div>
					</div>
					<div v-for="floskel of floskeln" :key="floskel.kuerzel ?? 1" class="svws-ui-tr" role="row" v-show="!collapsed.get(gruppe)" @click="ergaenzeFloskel(floskel)">
						<div class="svws-ui-td" role="cell"> {{ floskel.kuerzel }} </div>
						<div class="svws-ui-td" role="cell"> {{ floskel.text }} </div>
						<div class="svws-ui-td" role="cell"> {{ floskel.niveau }} </div>
						<div class="svws-ui-td" role="cell"> {{ floskel.jahrgangID }} </div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from 'vue';
	import type { ENMLeistung } from '@core/core/data/enm/ENMLeistung';
	import type { ENMFloskel } from '@core/core/data/enm/ENMFloskel';
	import type { ENMFloskelgruppe } from '@core/core/data/enm/ENMFloskelgruppe';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { EnmManager } from './EnmManager';

	type Hauptgruppe = 'ALLG'|'ASV'|'AUE'|'FACH'|'FÖRD'|'FSP'|'VERM'|'VERS'|'ZB';

	const props = defineProps<{
		manager: EnmManager;
		patchLeistung: (patch: Partial<ENMLeistung>) => Promise<boolean>;
		erlaubteHauptgruppen: Set<Hauptgruppe>;
	}>();

	const text = ref<string>();

	const clean = computed(() => (text.value !== undefined) && !templateRegex.test(text.value));

	const gruppenMap = computed(() => {
		const liste = new Map<ENMFloskelgruppe, ArrayList<ENMFloskel>>();
		for (const gruppe of props.manager.daten.floskelgruppen) {
			const floskeln = new ArrayList<ENMFloskel>();
			for (const floskel of gruppe.floskeln)
				if ((floskel.fachID === null)
					|| ((props.manager.lerngruppeByIDOrException(props.manager.auswahlLeistung.leistung?.lerngruppenID ?? 0).fachID === floskel.fachID)
						&& ((floskel.jahrgangID === null) || (floskel.jahrgangID === schueler.value.jahrgangID))))
					floskeln.add(floskel);
			if ((gruppe.hauptgruppe !== null) && (props.erlaubteHauptgruppen.has(gruppe.hauptgruppe as Hauptgruppe)) && !floskeln.isEmpty())
				liste.set(gruppe, floskeln);
		}
		return liste;
	});

	const floskelMap = computed(() => {
		const floskeln = new Map<string, ENMFloskel>();
		for (const gruppe of gruppenMap.value.values())
			for (const floskel of gruppe)
				if (floskel.kuerzel !== null)
					floskeln.set(floskel.kuerzel.toLocaleLowerCase(), floskel);
		return floskeln;
	});

	watch(() => props.manager.auswahlLeistung.leistung, (neu, alt) => {
		text.value = neu?.fachbezogeneBemerkungen ?? undefined;
	}, { immediate: true })

	const templateRegex = /(?:\$(?:(Vorname)|(Name|Nachname)|(weibl)|(ein)|(Anrede)|(\w*%.*))\$)|(#\S*)/gi;
	const every = ref(3);
	const kleinPronomenMap = computed(() => new Map([['m', 'er'], ['w', 'sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));
	const grossPronomenMap = computed(() => new Map([['m', 'Er'], ['w', 'Sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));
	const anredeMap = computed(() => new Map([['m', 'Herr'], ['w', 'Frau']]));

	function ergaenzeFloskel(floskel: ENMFloskel) {
		let tmp = text.value;
		if (tmp === undefined)
			tmp = "";
		else if (tmp.endsWith('.'))
			tmp += " ";
		tmp += floskel.text;
		text.value = tmp;
	}

	function ersetzeTemplates() {
		if (text.value === undefined)
			return;
		let counter = -1;
		let tmp = text.value;
		tmp = tmp.replaceAll(templateRegex, (match, vorname, nachname, weibl, ein, anrede, mwdx, kuerzel, _offset, fullString: string, _groups) => {
			if (vorname !== undefined) {
				counter++;
				if ((counter % every.value) === 0)
					return schueler.value.vorname ?? '???';
				return fullString.slice(0, _offset).trimEnd().endsWith('.')
					? grossPronomenMap.value.get(schueler.value.geschlecht ?? 'x') ?? schueler.value.vorname ?? '???'
					: kleinPronomenMap.value.get(schueler.value.geschlecht ?? 'x') ?? schueler.value.vorname ?? '???';
			} else if (nachname !== undefined) {
				return schueler.value.nachname ?? '???';
			} else if (weibl !== undefined) {
				return schueler.value.geschlecht === 'w' ? 'in':'';
			} else if (ein !== undefined) {
				return schueler.value.geschlecht === 'w' ? 'in':'e';
			} else if (anrede !== undefined) {
				return anredeMap.value.get(schueler.value.geschlecht ?? 'm') ?? '';
			} else if (mwdx !== undefined) {
				const arr = match.slice(1, -1).split('%')
				const mwdxMap = new Map([['m', arr[0] ?? ''], ['w', arr[1] ?? ''], ['d', arr[2] ?? ''], ['x', arr[3] ?? '']]);
				return mwdxMap.get((schueler.value.geschlecht ?? 'x') as 'm'|'w'|'d'|'x')!;
			} else if (kuerzel !== undefined) {
				return floskelMap.value.get(kuerzel.toLocaleLowerCase())?.text ?? '???';
			}
			return '???';
		});
		text.value = tmp;
	}

	const schueler = computed(() => {
		const index = props.manager.auswahlLeistung.indexSchueler;
		return props.manager.lerngruppenAuswahlGetSchueler().get(index);
	})

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const collapsed = ref(new Map<ENMFloskelgruppe, boolean>([...props.manager.daten.floskelgruppen].map(g => [g, false])));

	async function doPatchLeistung() {
		if (props.manager.auswahlLeistung.leistung === null)
			return;
		if (!clean.value)
			return ersetzeTemplates();
		const id = props.manager.auswahlLeistung.leistung.id
		const patch = { id, fachbezogeneBemerkungen: text.value };
		const success = await props.patchLeistung(patch);
		if (success)
			Object.assign(props.manager.auswahlLeistung.leistung, patch);
		props.manager.update();
	}

</script>

<style lang="postcss" scoped>

	.svws-ui-tr {
		grid-template-columns: 6em 1fr 4em 4em;
		min-height: auto;
		.svws-ui-td {
			@apply leading-5 align-middle;
		}
	}

</style>