<template>
	<div class="flex flex-col">
		<template v-if="sperrenVorhanden.size > 0">
			<div class="p-4 mb-4 border rounded-md bg-ui-warning text-ui-onwarning border-ui-warning font-normal text-base">
				{{ sperrenVorhanden.size === 1 ? 'Klasse' : 'Klassen' }} {{ gesperrteKlassen }} gesperrt. Eine Eingabe ist nicht möglich.
			</div>
		</template>
		<div class="flex overflow-hidden gap-6">
			<div class="min-w-fit overflow-auto border rounded-md border-uistatic-50">
				<ui-table-grid name="Schüler" :manager="() => gridManagerSchueler">
					<template #default="{ row, index }">
						<td :ref="auswahlSchueler(index)" :class="[
							'cursor-pointer text-left text-ellipsis overflow-hidden whitespace-nowrap',
							gridManagerSchueler.focusRowLast === index ? 'bg-ui-selected modalFocusField':'',
						]">
							<div class="flex items-center gap-1"><span :class="{'icon icon-ui-danger i-ri-lock-2-line': sperrenVorhanden.has(row.a)}" /> {{ row.a.kuerzel }} {{ row.b.nachname }}, {{ row.b.vorname }}</div>
						</td>
					</template>
				</ui-table-grid>
			</div>
			<div class="overflow-hidden flex flex-col w-full">
				<div class="overflow-y-auto">
					<ui-table-grid v-if="!gridManager.daten.isEmpty()" :manager="() => gridManager" class="min-w-full">
						<template #header="params">
							<template v-if="params.i === 1">
								<th class="text-left">Fach</th>
								<th>Kompetenz</th>
								<th class="text-center">1</th>
								<th class="text-center">2</th>
								<th class="text-center">3</th>
								<th class="text-center">4</th>
								<th class="text-center">5</th>
							</template>
						</template>
						<template #default="{ row, index }">
							<template v-if="row.kompetenz instanceof ENMv2Leistung">
								<td class="text-left bg-ui-50">
									<svws-ui-tooltip class="w-full">
										{{ row.gruppe.kuerzelAnzeige }}
										<template #content> {{ row.gruppe.bezeichnung }} </template>
									</svws-ui-tooltip>
								</td>
								<td v-if="auswahlZelle?.b.klasseID && enmManager().sperrungen.istSpalteneingabeErlaubt(auswahlZelle.b.klasseID, 'FB')"
									:ref="inputBemerkung(mapLeistungen.get(row.gruppe.id), 1, index)" class="ui-table-grid-button col-span-6 text-left"
									:class="{
										'bg-ui-selected': ((gridManager.focusColumn === 1) && (gridManager.focusRow === index)),
										'contentFocusField': gridManager.isFocusLast(1, index),
									}">
									<svws-ui-tooltip v-if="(row.kompetenz.fachbezogeneBemerkungen !== null) && (row.kompetenz.fachbezogeneBemerkungen.length > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ row.kompetenz.fachbezogeneBemerkungen }}</span>
										<template #content>
											{{ row.kompetenz.fachbezogeneBemerkungen }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> Fachbemerkung </span>
								</td>
								<td v-else class="ui-table-grid-button col-span-6 text-left">
									<svws-ui-tooltip v-if="(row.kompetenz.fachbezogeneBemerkungen !== null) && (row.kompetenz.fachbezogeneBemerkungen.length > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ row.kompetenz.fachbezogeneBemerkungen }}</span>
										<template #content>
											{{ row.kompetenz.fachbezogeneBemerkungen }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> kein Fachbemerkung hinterlegt </span>
								</td>
							</template>
							<template v-else>
								<td />
								<td class="text-left"> {{ enmManager().mapAnkreuzkompetenzen.get(row.kompetenz.kompetenzID)?.text }} </td>
								<template v-for="stufe, col of row.kompetenz.stufen" :key="col+2">
									<td v-if="auswahlZelle?.b.klasseID && enmManager().sperrungen.istSpalteneingabeErlaubt(auswahlZelle.b.klasseID, 'Note')"
										:ref="inputStufe(row.kompetenz, col+2, index)" class="ui-table-grid-button"
										:class="{
											'bg-ui-selected': (gridManager.focusColumn === col+2),
											'contentFocusField': gridManager.isFocusLast(col+2, index),
										}">
										<span v-if="stufe" class="icon-sm align-middle i-ri-checkbox-line" />
										<span v-else class="icon-sm align-middle i-ri-checkbox-blank-line" />
									</td>
									<td v-else>
										<span v-if="stufe" class="icon-sm align-middle i-ri-checkbox-line" />
										<span v-else class="icon-sm align-middle i-ri-checkbox-blank-line" />
									</td>
								</template>
							</template>
						</template>
					</ui-table-grid>
					<div v-else>{{ auswahlZelle?.b.geschlecht === 'm' ? 'Dieser Schüler' : auswahlZelle?.b.geschlecht === 'w' ? 'Diese Schülerin' : `${auswahlZelle?.b.vorname}` }} hat keine Ankreuzkompetenzen</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ComponentPublicInstance } from 'vue';
	import { computed, ref, shallowRef, watch, watchEffect } from 'vue';
	import { ENMv2Leistung } from '../../../../core/src/core/data/enm/v2/ENMv2Leistung';
	import type { ENMv2Schueler } from '../../../../core/src/core/data/enm/v2/ENMv2Schueler';
	import type { ENMv2Fach } from '../../../../core/src/core/data/enm/v2/ENMv2Fach';
	import type { ENMv2SchuelerAnkreuzkompetenz } from '../../../../core/src/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz';
	import { PairNN } from '../../../../core/src/asd/adt/PairNN';
	import type { List } from '../../../../core/src/java/util/List';
	import { ArrayList } from '../../../../core/src/java/util/ArrayList';
	import { GridManager } from '../../ui/controls/tablegrid/GridManager';
	import type { EnmAnkreuzkompetenzenUebersichtProps } from './EnmAnkreuzkompetenzenUebersichtProps';
	import type { ENMv2Klasse } from '../../../../core/src/core/data/enm/v2/ENMv2Klasse';

	const props = defineProps<EnmAnkreuzkompetenzenUebersichtProps>();

	const gridManagerSchueler = new GridManager<string, PairNN<ENMv2Klasse, ENMv2Schueler>, List<PairNN<ENMv2Klasse, ENMv2Schueler>>>({
		daten: computed<List<PairNN<ENMv2Klasse, ENMv2Schueler>>>(() => {
			const result = new ArrayList<PairNN<ENMv2Klasse, ENMv2Schueler>>();
			for (const lerngruppenAuswahl of props.auswahl()) {
				const listSchueler = props.enmManager().mapKlassenSchueler.get(lerngruppenAuswahl.id);
				const klasse = props.enmManager().mapKlassen.get(lerngruppenAuswahl.id);
				if ((klasse === null) || (listSchueler === null)) {
					continue;
				}
				const list = new ArrayList<PairNN<ENMv2Klasse, ENMv2Schueler>>();
				for (const schueler of listSchueler) {
					const pair = new PairNN<ENMv2Klasse, ENMv2Schueler>(klasse, schueler);
					list.add(pair);
				}
				result.addAll(list);
			}
			return result;
		}),
		getRowKey: row => `${row.a.id}_${row.b.id}`,
		columns: [{ kuerzel: "Name", name: "Name, Vorname", width: '15rem' }],
	});

	const lastRow = ref<number | null>(null);
	const auswahlZelle = shallowRef<PairNN<ENMv2Klasse, ENMv2Schueler>>();

	watch(() => gridManagerSchueler.daten, (neu) => {
		if (neu.contains(auswahlZelle.value)) {
			return;
		}
		lastRow.value = 0;
		onUpdate(0, true);
	}, { immediate: true });

	const mapLeistungen = computed(() => {
		const leistungen = auswahlZelle.value?.b.leistungsdaten ?? new ArrayList<ENMv2Leistung>();
		const map = new Map<number, ENMv2Leistung>();
		for (const leistung of leistungen) {
			const fach = props.enmManager().lerngruppeGetFach(leistung.lerngruppenID);
			if (fach !== null) {
				map.set(fach.id, leistung);
			}
		}
		return map;
	});

	function onUpdate(row: number | null, focus: boolean) {
		if ((row === null)) {
			return;
		}
		gridManagerSchueler.focusRowLast = row;
		if (!gridManagerSchueler.daten.isEmpty()) {
			auswahlZelle.value = gridManagerSchueler.daten.get(row);
		}
	}

	function auswahlSchueler(index: number) {
		const key = `Schueler_${index}`;
		const setter = () => onUpdate(index, false);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManagerSchueler.applyInputToggle(key, 1, index, element, setter);
			if (input !== null) {
				gridManagerSchueler.update(key, false);
				gridManagerSchueler.setNavigationOnEnter(key, null);
				if (index === lastRow.value) {
					gridManagerSchueler.doFocusByKey(key);
				}
			}
		};
	}

	type RowType = { gruppe: ENMv2Fach, kompetenz: ENMv2SchuelerAnkreuzkompetenz | ENMv2Leistung };
	const gridManager = new GridManager<string, RowType, List<RowType>>({
		daten: computed<List<RowType>>(() => {
			const result = new ArrayList<RowType>();
			const kompetenzen = props.enmManager().schuelerGetAnkreuzkompetenzen(auswahlZelle.value?.b.id ?? -1);
			if (kompetenzen.isEmpty()) {
				return result;
			}
			const [first] = kompetenzen;
			let fach = props.enmManager().mapFaecher.get(props.enmManager().mapAnkreuzkompetenzen.get(first.kompetenzID)?.fachID ?? null);
			if (fach !== null) {
				const leistung = mapLeistungen.value.get(fach.id);
				if (leistung !== undefined) {
					result.add({ gruppe: fach, kompetenz: leistung });
				}
			}
			for (const kompetenz of kompetenzen) {
				const nextFach = props.enmManager().mapFaecher.get(props.enmManager().mapAnkreuzkompetenzen.get(kompetenz.kompetenzID)?.fachID ?? null);
				if (nextFach === null) {
					continue;
				}
				if (nextFach !== fach) {
					const leistung = mapLeistungen.value.get(nextFach.id);
					if (leistung !== undefined) {
						result.add({ gruppe: nextFach, kompetenz: leistung });
					}
					fach = nextFach;
				}
				result.add({ gruppe: nextFach, kompetenz });
			}
			return result;
		}),
		getRowKey: row => `${row.gruppe.id}__${row.kompetenz.id}`,
		columns: [
			{ kuerzel: "Fach", name: "Fach", width: "6rem", hideable: false },
			{ kuerzel: "Kürzel", name: "Kürzel", width: "60rem", hideable: false },
			{ kuerzel: "Stufe1", name: "Stufe 1", width: "2rem", hideable: false },
			{ kuerzel: "Stufe2", name: "Stufe 2", width: "2rem", hideable: false },
			{ kuerzel: "Stufe3", name: "Stufe 3", width: "2rem", hideable: false },
			{ kuerzel: "Stufe4", name: "Stufe 4", width: "2rem", hideable: false },
			{ kuerzel: "Stufe5", name: "Stufe 5", width: "2rem", hideable: false },
		],
	});
	defineExpose({ gridManager, gridManagerSchueler });

	function inputStufe(kompetenz: ENMv2SchuelerAnkreuzkompetenz, col: number, index: number) {
		const key = kompetenz.id + '_Stufe_' + index + "_" + col;
		const stufen = kompetenz.stufen;
		const setter = (value: boolean) => {
			stufen[col - 2] = value;
			void props.patchAnkreuzkompetenz(kompetenz, { stufen });
		};
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				watchEffect(() => gridManager.update(key, kompetenz.stufen[col - 2] ?? false));
			}
		};
	}

	function inputBemerkung(leistung: ENMv2Leistung | undefined, col: number, index: number) {
		if (leistung === undefined) {
			return;
		}
		const key = 'FB_' + leistung.id;
		const indexSchueler = gridManagerSchueler.daten.indexOf(auswahlZelle.value);
		const setter = (_value: boolean) => void props.focusFloskelEditor(auswahlZelle.value?.b ?? null, leistung, indexSchueler, true);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			const input = gridManager.applyInputToggle(key, col, index, element, setter);
			if (input !== null) {
				gridManager.update(key, false);
				gridManager.setNavigationOnEnter(key, null);
			}
		};
	}

	const sperrenVorhanden = computed(() => {
		const set = new Set<ENMv2Klasse>();
		for (const klasse of props.auswahl()) {
			if (!props.enmManager().sperrungen.istEingabeErlaubt(klasse.id)) {
				set.add(klasse);
			}
		}
		return set;
	});

	const gesperrteKlassen = computed(() => {
		let str = "";
		for (const k of sperrenVorhanden.value) {
			str += `${k.kuerzel}, `;
		}
		return str.slice(0, -2);
	});

</script>
