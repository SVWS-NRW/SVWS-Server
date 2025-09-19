<template>
	<svws-ui-modal :show type="default" size="big" @update:show="show = $event">
		<template #modalTitle>
			<div class="text-right w-full">{{ schueler?.nachname }}, {{ schueler?.vorname }}<br>{{ hauptgruppenBezeichnung[erlaubteHauptgruppe] }}</div>
		</template>
		<template #modalContent>
			<div class="flex overflow-hidden gap-6">
				<div class="min-w-fit overflow-auto border rounded-md border-uistatic-50">
					<ui-table-grid name="Schüler" :manager="() => gridManagerSchueler">
						<template #default="{ row, index }">
							<td :ref="auswahlSchueler(index)" :class="['cursor-pointer text-left', gridManagerSchueler.focusRowLast === index ? 'bg-ui-selected':'']">
								{{ row.a }} {{ row.b.nachname }}, {{ row.b.vorname }}
							</td>
						</template>
					</ui-table-grid>
				</div>
				<div class="overflow-hidden flex flex-col">
					<div>
						<div class="pb-4">
							<svws-ui-textarea-input class="floskel-input" placeholder="Floskeln auswählen oder manuell eingeben"
								:model-value="text" @input="onInput" autoresize is-content-focus-field />
						</div>
						<div class="flex justify-between gap-2 w-full flex-row-reverse">
							<div v-if="showButtons" class="flex gap-2">
								<svws-ui-button @click="doPatchLeistung" :type="clean ? 'primary':'secondary'">
									{{ clean ? 'Speichern':'Anwenden' }}
								</svws-ui-button>
								<svws-ui-button @click="text = bemerkung">Zurücksetzen</svws-ui-button>
							</div>
							<div v-if="(text !== null) && /$Vorname$/i.exec(text)" class="flex gap-2">
								<div class="w-20">
									<svws-ui-input-number :model-value="every" :min="1" :max="9"
										@update:model-value="value => every = value ?? 1" />
								</div>
								<span class="mt-2">Vorname jedes {{ every === 1 ? '':`${every}.` }} Mal</span>
							</div>
						</div>
					</div>
					<div class="overflow-y-auto">
						<ui-table-grid :footer-count="0" :manager="() => gridManager">
							<template #header>
								<th>Kürzel</th>
								<th>Text</th>
								<th>Niveau</th>
								<th>Jg</th>
							</template>
							<template #default="{ row: data, index }">
								<template v-if="data.floskel === null">
									<td class="col-span-4 text-left bg-ui-50">{{ data.gruppe.bezeichnung }}</td>
								</template>
								<template v-else>
									<td :ref="inputBemerkung(data.floskel, 1, index)" class="cursor-pointer"> {{ data.floskel.kuerzel }} </td>
									<td class="text-left"> {{ data.floskel.text }} </td>
									<td> {{ data.floskel.niveau }} </td>
									<td> {{ data.floskel.jahrgangID }} </td>
								</template>
							</template>
						</ui-table-grid>
					</div>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance } from 'vue';
	import { computed, onMounted, onBeforeUnmount, onUnmounted, ref, watch, watchEffect } from 'vue';
	import type { ENMFloskel } from '../../../../core/src/core/data/enm/ENMFloskel';
	import type { ENMFloskelgruppe } from '../../../../core/src/core/data/enm/ENMFloskelgruppe';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import type { ENMSchueler } from '../../../../core/src/core/data/enm/ENMSchueler';
	import type { ENMLeistung } from '../../../../core/src/core/data/enm/ENMLeistung';
	import { ENMKlasse } from '../../../../core/src/core/data/enm/ENMKlasse';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';
	import type { List } from '../../../../core/src/java/util/List';
	import type { EnmManager, BemerkungenHauptgruppe, EnmLerngruppenAuswahlEintrag } from './EnmManager';
	import { PairNN } from '../../../../core/src/asd/adt/PairNN';

	type RowType = { gruppe: ENMFloskelgruppe, floskel: ENMFloskel | null };
	type StrOrUndef = string|undefined;

	const props = defineProps<{
		enmManager: () => EnmManager;
		lerngruppenAuswahl: () => Array<EnmLerngruppenAuswahlEintrag|ENMKlasse>;
		auswahl: { klasse: ENMKlasse | null, schueler: ENMSchueler | null, leistung: ENMLeistung | null };
		patch: (value: string|null) => Promise<void>;
		erlaubteHauptgruppe: BemerkungenHauptgruppe;
		initialRow: number | null;
		onUpdate: (row: number | null, focus: boolean) => void;
	}>();

	const show = defineModel<boolean>({default: true});

	const lastRow = ref<number | null>(null);

	/** Die ausgewählte Zeile aus der Liste merken und in das Modal übernehmen */
	onMounted(() =>	lastRow.value = props.initialRow);
	/** Ebenso die zuletzt ausgewählte Zeile in die Übersicht übernehmen */
	onBeforeUnmount(() => lastRow.value = gridManagerSchueler.focusRowLast);
	onUnmounted(() => props.onUpdate(lastRow.value, true));

	const hauptgruppenBezeichnung = {
		'ASV': 'Arbeits- und Sozialverhalten',
		'AUE': 'Außerunterrichtliches Engagement',
		'FACH': 'Fachbezogene Bemerkungen',
		'FÖRD': 'Fördermaßnahmen',
		'FSP': 'Förderschwerpunkt',
		'VERM': 'Vermerke',
		'VERS': 'Versetzung',
		'ZB': 'Zeugnis-Bemerkungen',
	} as const;

	const gridManagerSchueler = new GridManager<string, PairNN<string, ENMSchueler>, List<PairNN<string, ENMSchueler>>>({
		daten: computed<List<PairNN<string, ENMSchueler>>>(() => {
			const result = new ArrayList<PairNN<string, ENMSchueler>>();
			for (const lerngruppenAuswahl of props.lerngruppenAuswahl())
				if (lerngruppenAuswahl instanceof ENMKlasse) {
					const listSchueler = props.enmManager().mapKlassenSchueler.get(lerngruppenAuswahl.id);
					const klasse = props.enmManager().mapKlassen.get(lerngruppenAuswahl.id)
					if ((klasse === null) || (listSchueler === null))
						continue;
					const list = new ArrayList<PairNN<string, ENMSchueler>>();
					for (const schueler of listSchueler) {
						const pair = new PairNN<string, ENMSchueler>(klasse.kuerzel ?? '???', schueler);
						list.add(pair);
					}
					result.addAll(list);
				} else {
					const leistungen = props.enmManager().mapLerngruppeLeistungen.get(lerngruppenAuswahl.id);
					const fach = props.enmManager().lerngruppeByIDOrException(lerngruppenAuswahl.id);
					if (leistungen === null)
						continue;
					for (const pairLeistung of leistungen) {
						const pair = new PairNN<string, ENMSchueler>(fach.bezeichnung ?? '???', pairLeistung.b);
						result.add(pair);
					}
				}
			return result;
		}),
		getRowKey: row => `${row.a}_${row.b.id}`,
		columns: [ { kuerzel: "Name", name: "Name, Vorname", width: '15rem' } ],
	})

	const gridManager = new GridManager<string, RowType, List<RowType>>({
		daten: computed<List<RowType>>(() => {
			const result = new ArrayList<RowType>();
			const auswahl = props.auswahl;
			if ((auswahl.schueler === null) || (auswahl.leistung === null))
				return result;
			let floskelnHauptgruppe: ENMFloskelgruppe | null = null;
			let floskelnAllgemein: ENMFloskelgruppe | null = null;
			for (const gruppe of props.enmManager().listFloskelgruppen) {
				if (gruppe.floskeln.isEmpty())
					continue;
				if (gruppe.hauptgruppe === props.erlaubteHauptgruppe)
					floskelnHauptgruppe = gruppe;
				else if (gruppe.hauptgruppe === 'ALLG')
					floskelnAllgemein = gruppe;
			}
			if ((floskelnHauptgruppe === null) && (floskelnAllgemein === null))
				return result;
			const temp = [];
			if (floskelnHauptgruppe !== null)
				temp.push(floskelnHauptgruppe);
			if (floskelnAllgemein !== null)
				temp.push(floskelnAllgemein);
			for (const gruppe of temp) {
				result.add({ gruppe, floskel: null });
				for (const floskel of gruppe.floskeln)
					if ((floskel.fachID === null) || ((props.enmManager().lerngruppeByIDOrException(auswahl.leistung.lerngruppenID).fachID === floskel.fachID)
						&& ((floskel.jahrgangID === null) || (floskel.jahrgangID === auswahl.schueler.jahrgangID))))
						result.add({ gruppe, floskel });
			}
			return result;
		}),
		getRowKey: row => `${row.gruppe.hauptgruppe}_${row.gruppe.kuerzel}_${row.floskel?.kuerzel}`,
		columns: [
			{ kuerzel: "Kürzel", name: "Kürzel", width: "4rem", hideable: false },
			{ kuerzel: "Text", name: "Text", width: "1fr", hideable: false },
			{ kuerzel: "Niveau", name: "Niveau", width: "6rem", hideable: false },
			{ kuerzel: "Jg", name: "Jahrgang", width: "4rem", hideable: false },
		],
	});

	function inputBemerkung(floskel: ENMFloskel, col: number, index: number) {
		const key = `Floskel_${floskel.kuerzel}`;
		const setter = () => ergaenzeFloskel(floskel);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

	function auswahlSchueler(index: number) {
		const key = `Schueler_${index}`;
		const setter = () => props.onUpdate(index, false);
		return (element : Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManagerSchueler.applyInputToggle(key, 1, index, element, setter);
			if (input !== null) {
				gridManagerSchueler.update(key, false);
				gridManagerSchueler.setNavigationOnEnter(key, null);
				if (index === lastRow.value)
					gridManagerSchueler.doFocusByKey(key);
			}
		};
	}

	const bemerkung = computed<string|null>(() => {
		const auswahl = props.auswahl;
		if (auswahl.schueler === null)
			return null;
		if (auswahl.leistung !== null)
			return auswahl.leistung.fachbezogeneBemerkungen;
		switch (props.erlaubteHauptgruppe) {
			case 'ASV':
				return auswahl.schueler.bemerkungen.ASV;
			case 'AUE':
				return auswahl.schueler.bemerkungen.AUE;
			case 'ZB':
				return auswahl.schueler.bemerkungen.ZB;
			default:
				return null;
		}
	});

	const text = ref<string|null>(null);
	const showButtons = computed(() => text.value !== bemerkung.value);

	watch(bemerkung, () => text.value = bemerkung.value, { immediate: true });

	const schueler = computed<ENMSchueler | null>(() => props.auswahl.schueler);
	const clean = computed(() => (text.value === null) || !templateRegex.exec(text.value));

	function onInput(value: string) {
		if (value.length > 1)
			text.value = value;
		else text.value = null;
	}

	const gruppenMap = computed(() => {
		const map = new Map<ENMFloskelgruppe, ArrayList<ENMFloskel>>();
		const auswahl = props.auswahl;
		if ((auswahl.schueler === null) || (auswahl.leistung === null))
			return map;
		for (const gruppe of props.enmManager().listFloskelgruppen) {
			if ((gruppe.hauptgruppe !== props.erlaubteHauptgruppe) && (gruppe.hauptgruppe !== 'ALLG'))
				continue;
			const floskeln = new ArrayList<ENMFloskel>();
			for (const floskel of gruppe.floskeln)
				if ((floskel.fachID === null) || ((props.enmManager().lerngruppeByIDOrException(auswahl.leistung.lerngruppenID).fachID === floskel.fachID)
					&& ((floskel.jahrgangID === null) || (floskel.jahrgangID === auswahl.schueler.jahrgangID))))
					floskeln.add(floskel);
			if (!floskeln.isEmpty())
				map.set(gruppe, floskeln);
		}
		return map;
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
	const kleinPronomenMap = computed(() => new Map([['m', 'er'], ['w', 'sie'], ['d', props.auswahl.schueler?.vorname ?? '???'], ['x', props.auswahl.schueler?.vorname ?? '???']]));
	const grossPronomenMap = computed(() => new Map([['m', 'Er'], ['w', 'Sie'], ['d', props.auswahl.schueler?.vorname ?? '???'], ['x', props.auswahl.schueler?.vorname ?? '???']]));
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
		const schueler = props.auswahl.schueler;
		if ((schueler === null) || (text.value === null))
			return;
		let counter = -1;
		text.value = text.value.replaceAll(templateRegexGlobal, (match, vorname: StrOrUndef, nachname: StrOrUndef, weibl: StrOrUndef, ein: StrOrUndef, anrede: StrOrUndef, mwdx: StrOrUndef, kuerzel: StrOrUndef, _offset, fullString: string, _groups: string[]) => {
			if (vorname !== undefined) {
				counter++;
				if ((counter % every.value) === 0)
					return schueler.vorname ?? '???';
				return fullString.slice(0, _offset).trimEnd().endsWith('.')
					? grossPronomenMap.value.get(schueler.geschlecht ?? 'x') ?? schueler.vorname ?? '???'
					: kleinPronomenMap.value.get(schueler.geschlecht ?? 'x') ?? schueler.vorname ?? '???';
			} else if (nachname !== undefined) {
				return schueler.nachname ?? '???';
			} else if (weibl !== undefined) {
				return schueler.geschlecht === 'w' ? 'in':'';
			} else if (ein !== undefined) {
				return schueler.geschlecht === 'w' ? 'in':'e';
			} else if (anrede !== undefined) {
				return anredeMap.value.get(schueler.geschlecht ?? 'm') ?? '';
			} else if (mwdx !== undefined) {
				const arr = match.slice(1, -1).split('%');
				const mwdxMap = new Map([['m', arr[0] ?? ''], ['w', arr[1] ?? ''], ['d', arr[2] ?? ''], ['x', arr[3] ?? '']]);
				return mwdxMap.get((schueler.geschlecht ?? 'x') as 'm'|'w'|'d'|'x')!;
			} else if (kuerzel !== undefined) {
				return floskelMap.value.get(kuerzel.toLocaleLowerCase())?.text ?? '???';
			}
			return '???';
		});
	}

	async function doPatchLeistung() {
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
