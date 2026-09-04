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
								<th v-for="column of columns" :key="column.kuerzel" class="text-center">
									<svws-ui-tooltip>
										{{ column.kuerzel }}
										<template #content>{{ column.name }}</template>
									</svws-ui-tooltip>
								</th>
							</template>
						</template>
						<template #default="{ row, index }">
							<template v-if="row.gruppe instanceof ENMv2Fach && row.kompetenz === null">
								<td class="text-left bg-ui-50">
									<svws-ui-tooltip class="w-full">
										{{ row.gruppe?.kuerzelAnzeige ?? '' }}
										<template #content> {{ row.gruppe?.bezeichnung ?? '' }} </template>
									</svws-ui-tooltip>
								</td>
								<td v-if="auswahlZelle?.b.klasseID && enmManager().sperrungen.istSpalteneingabeErlaubt(auswahlZelle.b.klasseID, 'FB')"
									:ref="inputBemerkung(mapLeistungen.get(row.gruppe.id), 1, index)" class="ui-table-grid-button text-left" :style="`grid-column: span ${columns.length + 1} / span ${columns.length + 1}`"
									:class="{
										'bg-ui-selected': ((gridManager.focusColumn === 1) && (gridManager.focusRow === index)),
										'contentFocusField': gridManager.isFocusLast(1, index),
									}">
									<svws-ui-tooltip v-if="(mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen ?? null !== null) && (mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen?.length ?? 0 > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen }}</span>
										<template #content>
											{{ mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> Fachbemerkung </span>
								</td>
								<td v-else class="ui-table-grid-button text-left" :style="`grid-column: span ${columns.length + 1} / span ${columns.length + 1}`">
									<svws-ui-tooltip v-if="(mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen ?? null !== null) && (mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen?.length ?? 0 > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen }}</span>
										<template #content>
											{{ mapLeistungen.get(row.gruppe.id)?.fachbezogeneBemerkungen }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> kein Fachbemerkung hinterlegt </span>
								</td>
							</template>
							<template v-else-if="row.gruppe === null && row.kompetenz === null">
								<td class="text-left bg-ui-50">
									<svws-ui-tooltip class="w-full">
										ASV
										<template #content> Arbeits- und Sozialverhalten </template>
									</svws-ui-tooltip>
								</td>
								<td v-if="auswahlZelle?.b.klasseID && enmManager().sperrungen.istSpalteneingabeErlaubt(auswahlZelle.b.klasseID, 'ASV')"
									:ref="inputASVBemerkung(1, index)" class="ui-table-grid-button text-left" :style="`grid-column: span ${columns.length + 1} / span ${columns.length + 1}`"
									:class="{
										'bg-ui-selected': ((gridManager.focusColumn === 1) && (gridManager.focusRow === index)),
										'contentFocusField': gridManager.isFocusLast(1, index),
									}">
									<svws-ui-tooltip v-if="(asvBemerkung !== null) && (asvBemerkung.length > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ asvBemerkung }}</span>
										<template #content>
											{{ asvBemerkung }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> ASV-Bemerkung </span>
								</td>
								<td v-else class="ui-table-grid-button text-left" :style="`grid-column: span ${columns.length + 1} / span ${columns.length + 1}`">
									<svws-ui-tooltip v-if="(asvBemerkung !== null) && (asvBemerkung.length > 20)" class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full">{{ asvBemerkung }}</span>
										<template #content>
											{{ asvBemerkung }}
										</template>
									</svws-ui-tooltip>
									<span v-else class="text-ui-50"> kein ASV-Bemerkung hinterlegt </span>
								</td>
							</template>
							<template v-else-if="typeof row.gruppe === 'string' && row.kompetenz === null">
								<td v-if="row.gruppe.length < 10" class="text-left bg-ui-50">
									{{ row.gruppe }}
								</td>
								<td v-else class="bg-ui-50">
									<svws-ui-tooltip class="h-full w-full">
										<span class="text-ellipsis overflow-hidden whitespace-nowrap w-full text-left">{{ row.gruppe }}</span>
										<template #content>
											{{ row.gruppe }}
										</template>
									</svws-ui-tooltip>
								</td>
								<td :style="`grid-column: span ${columns.length + 1} / span ${columns.length + 1}`" />
							</template>
							<template v-else-if="row.kompetenz !== null">
								<td />
								<td class="text-left"> {{ enmManager().mapAnkreuzkompetenzen.get(row.kompetenz.kompetenzID)?.text }} </td>
								<template v-for="stufe, col of row.kompetenz.stufen" :key="col+2">
									<template v-if="columns.at(col) !== undefined">
										<td v-if="auswahlZelle?.b.klasseID && enmManager().sperrungen.istSpalteneingabeErlaubt(auswahlZelle.b.klasseID, (row.gruppe === null) ? 'ASV' : 'Note')"
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
						</template>
					</ui-table-grid>
					<div v-else>{{ auswahlZelle?.b.geschlecht === 'm' ? 'Dieser Schüler' : auswahlZelle?.b.geschlecht === 'w' ? 'Diese Schülerin' : `${auswahlZelle?.b.vorname}` }} hat keine Ankreuzkompetenzen</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { PairNN } from '@core/asd/adt/PairNN';
	import { ENMv2Fach } from '@core/core/data/enm/v2/ENMv2Fach';
	import type { ENMv2Klasse } from '@core/core/data/enm/v2/ENMv2Klasse';
	import type { ENMv2Leistung } from '@core/core/data/enm/v2/ENMv2Leistung';
	import type { ENMv2Schueler } from '@core/core/data/enm/v2/ENMv2Schueler';
	import type { ENMv2SchuelerAnkreuzkompetenz } from '@core/core/data/enm/v2/ENMv2SchuelerAnkreuzkompetenz';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';
	import type { ComponentPublicInstance } from 'vue';
	import { computed, ref, shallowRef, watch, watchEffect } from 'vue';
	import type { EnmAnkreuzkompetenzenUebersichtProps } from './EnmAnkreuzkompetenzenUebersichtProps';

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

	const columns = computed(() => {
		const arr = [];
		for (let index = 1; index <= props.enmManager().daten.ankreuzkompetenzen.textStufen.length; index++) {
			const stufe = props.enmManager().daten.ankreuzkompetenzen.textStufen[index - 1];
			if (stufe === null) {
				return arr;
			}
			arr.push({ kuerzel: index.toString(), name: stufe, width: "2rem", hideable: false });
		}
		return arr;
	});

	type RowType = { gruppe: ENMv2Fach | string | null, kompetenz: ENMv2SchuelerAnkreuzkompetenz | null };
	const gridManager = new GridManager<string, RowType, List<RowType>>({
		daten: computed<List<RowType>>(() => {
			const result = new ArrayList<RowType>();
			const kompetenzen = props.enmManager().schuelerGetAnkreuzkompetenzen(auswahlZelle.value?.b.id ?? -1);
			if (kompetenzen.isEmpty()) {
				return result;
			}
			const katalog = props.enmManager().daten.ankreuzkompetenzen;
			let fach: ENMv2Fach | string | null;
			let letztesFach: ENMv2Fach | string | null = "";
			for (const kompetenz of kompetenzen) {
				const kompetenzFachOrNull = props.enmManager().mapFaecher.get(props.enmManager().mapAnkreuzkompetenzen.get(kompetenz.kompetenzID)?.fachID ?? null);
				if (kompetenzFachOrNull === null) {
					// Nur einfügen, wenn Benutzer die Klassenleitung ist
					if ((auswahlZelle.value === undefined) || !props.enmManager().listKlassenKlassenlehrer.contains(auswahlZelle.value.a)) {
						continue;
					}
					const ankreuzkompetenz = props.enmManager().mapAnkreuzkompetenzen.get(kompetenz.kompetenzID);
					if (ankreuzkompetenz === null) {
						continue;
					} else if (ankreuzkompetenz.istFachkompetenz) {
						fach = ((katalog.textSonstiges === null) || (katalog.textSonstiges.length === 0)) ? 'Sonstiges' : katalog.textSonstiges;
					} else {
						fach = null;
					}
				} else {
					fach = kompetenzFachOrNull;
				}
				if (letztesFach !== fach) {
					result.add({ gruppe: fach, kompetenz: null });
				}
				result.add({ gruppe: fach, kompetenz });
				letztesFach = fach;
			}
			return result;
		}),
		getRowKey: row => {
			if (row.gruppe === null) {
				return (row.kompetenz === null) ? "ASV" : `ASV__${row.kompetenz.id}`;
			} else if (typeof row.gruppe === 'string') {
				return (row.kompetenz === null) ? "Sonstige" : `Sonstige__${row.kompetenz.id}`;
			}
			return `${row.gruppe.id}__${row.kompetenz?.id}`;
		},
		columns: [
			{ kuerzel: "Fach", name: "Fach", width: "6rem", hideable: false },
			{ kuerzel: "Kürzel", name: "Kürzel", width: "60rem", hideable: false },
		].concat(columns.value),
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

	function inputASVBemerkung(col: number, index: number) {
		const schueler = auswahlZelle.value?.b;
		if (schueler === undefined) {
			return;
		}
		const key = 'ASV_' + schueler.id;
		const indexSchueler = gridManagerSchueler.daten.indexOf(auswahlZelle.value);
		const setter = (_value: boolean) => void props.focusFloskelEditor(auswahlZelle.value?.b ?? null, null, indexSchueler, true);
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
			str += `${k.kuerzelAnzeige}, `;
		}
		return str.slice(0, -2);
	});

	const asvBemerkung = computed(() => {
		// TODO Aufruf entfernen, Reaktivität funktioniert sonst nicht. Sollte über State laufen
		void props.auswahl();
		return auswahlZelle.value?.b.bemerkungen.ASV ?? null;
	});

</script>
