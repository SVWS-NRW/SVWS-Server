<template>
	<div class="border-l h-full flex flex-auto flex-col pl-4 pr-1 gap-4">
		<div class="text-headline-md flex justify-between"><span>{{ schueler.nachname }}, {{ schueler.vorname }}</span><span>{{ hauptgruppenBezeichnung[erlaubteHauptgruppe] }}</span></div>
		<svws-ui-textarea-input placeholder="Floskeln auswählen oder manuell eingeben" :model-value="text" @input="onInput" autoresize />
		<div class="flex justify-between gap-2 w-full flex-row-reverse">
			<div v-if="showButtons" class="flex gap-2">
				<svws-ui-button @click="doPatchLeistung" :type="clean ? 'primary':'secondary'">{{ clean ? 'Speichern':'Anwenden' }}</svws-ui-button>
				<svws-ui-button @click="text = bemerkung">Zurücksetzen</svws-ui-button>
			</div>
			<div v-if="(text !== null) && /\$Vorname\$/i.exec(text)" class="flex gap-2">
				<div class="w-20">
					<svws-ui-input-number :model-value="every" :min="1" :max="9" @update:model-value="value => every = value ?? 1" />
				</div>
				<span class="mt-2">Vorname jedes {{ every === 1 ? '':`${every}.` }} Mal</span>
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
	import type { ENMFloskel } from '@core/core/data/enm/ENMFloskel';
	import type { ENMFloskelgruppe } from '@core/core/data/enm/ENMFloskelgruppe';
	import type { EnmManager, BemerkungenHauptgruppe } from './EnmManager';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { ENMSchueler } from '@core/core/data/enm/ENMSchueler';


	const props = defineProps<{
		manager: EnmManager;
		patch: (value: string|null) => Promise<void>;
		erlaubteHauptgruppe: BemerkungenHauptgruppe;
	}>();

	const hauptgruppenBezeichnung: Record<BemerkungenHauptgruppe, string> = {
		'ASV': 'Arbeits- und Sozialverhalten',
		'AUE': 'Außerunterrichtliches Engagement',
		'FACH': 'Fachbezogene Bemerkungen',
		'FÖRD': 'Fördermaßnahmen',
		'FSP': 'Förderschwerpunkt',
		'VERM': 'Vermerke',
		'VERS': 'Versetzung',
		'ZB': 'Zeugnis-Bemerkungen',
	};

	const showButtons = computed(() => text.value !== bemerkung.value);

	const text = ref<string|null>(null);

	const bemerkung = computed<string|null>(() => {
		switch (props.erlaubteHauptgruppe) {
			case 'FACH':
				return props.manager.auswahlLeistung.leistung?.fachbezogeneBemerkungen ?? null;
			case 'ASV':
				return props.manager.auswahlSchueler?.bemerkungen.ASV ?? null;
			case 'AUE':
				return props.manager.auswahlSchueler?.bemerkungen.AUE ?? null;
			case 'ZB':
				return props.manager.auswahlSchueler?.bemerkungen.ZB ?? null;
			default:
				return null;
		}
	});

	watch([bemerkung, () => props.manager.auswahlLeistung.leistung, () => props.manager.auswahlSchueler, () => props.erlaubteHauptgruppe],
		([neuBemerkung]) => text.value = neuBemerkung);

	const schueler = computed<ENMSchueler>(() => {
		const index = props.manager.auswahlLeistung.indexSchueler;
		if (props.erlaubteHauptgruppe === 'FACH')
			return props.manager.lerngruppenAuswahlGetSchueler().get(index);
		else
			return props.manager.auswahlSchueler ?? new ENMSchueler();
	})

	const clean = computed(() => (text.value === null) || !templateRegex.exec(text.value));

	function onInput(value: string) {
		if (value.length > 1)
			text.value = value
		else text.value = null;
	}

	const gruppenMap = computed(() => {
		const liste = new Map<ENMFloskelgruppe, ArrayList<ENMFloskel>>();
		for (const gruppe of props.manager.daten.floskelgruppen) {
			if ((gruppe.hauptgruppe !== props.erlaubteHauptgruppe) && (gruppe.hauptgruppe !== 'ALLG'))
				continue;
			const floskeln = new ArrayList<ENMFloskel>();
			for (const floskel of gruppe.floskeln)
				if ((floskel.fachID === null)
					|| ((props.manager.lerngruppeByIDOrException(props.manager.auswahlLeistung.leistung?.lerngruppenID ?? 0).fachID === floskel.fachID)
						&& ((floskel.jahrgangID === null) || (floskel.jahrgangID === schueler.value.jahrgangID))))
					floskeln.add(floskel);
			if (!floskeln.isEmpty())
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

	/**
	 * Dieses Regex hat zwei Gruppen mit Untergruppen:
	 * Die erste sucht Templates mit dem Muster $xx$ und $xx%yy%zz$
	 * die zweite sucht nach dem Muster #tag
	 * ?: verhindert das Erzeugen von Gruppen, die erste RegexGruppe wäre also Vorname
	 *  */
	const query = /(?:\$(?:(Vorname)|(Name|Nachname)|(weibl)|(ein)|(Anrede)|(\S+%\S+))\$)|(#\S+)/;
	const templateRegexGlobal = new RegExp(query, 'gi');
	const templateRegex = new RegExp(query, 'i');
	const every = ref(3);
	const kleinPronomenMap = computed(() => new Map([['m', 'er'], ['w', 'sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));
	const grossPronomenMap = computed(() => new Map([['m', 'Er'], ['w', 'Sie'], ['d', schueler.value.vorname ?? '???'], ['x', schueler.value.vorname ?? '???']]));
	const anredeMap = computed(() => new Map([['m', 'Herr'], ['w', 'Frau']]));

	function ergaenzeFloskel(floskel: ENMFloskel) {
		let tmp = text.value;
		if (tmp === null)
			tmp = "";
		else if (tmp.endsWith('.'))
			tmp += " ";
		tmp += floskel.text;
		text.value = tmp;
	}

	function ersetzeTemplates() {
		if (text.value === null)
			return;
		let counter = -1;
		let tmp = text.value;
		tmp = tmp.replaceAll(templateRegexGlobal, (match, vorname, nachname, weibl, ein, anrede, mwdx, kuerzel, _offset, fullString: string, _groups) => {
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

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const collapsed = ref(new Map<ENMFloskelgruppe, boolean>([...props.manager.daten.floskelgruppen].map(g => [g, false])));

	async function doPatchLeistung() {
		if (props.manager.auswahlLeistung.leistung === null)
			return;
		if (!clean.value)
			return ersetzeTemplates();
		await props.patch(text.value);
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: 6em 1fr 4em 4em;
		min-height: auto;
		.svws-ui-td {
			line-height: 1.25rem;
			vertical-align: middle;
		}
	}

</style>
