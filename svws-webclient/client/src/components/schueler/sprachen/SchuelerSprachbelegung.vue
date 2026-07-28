<template>
	<svws-ui-content-card title="Sprachenfolge">
		<div v-if="!readonly && verfuegbareSprachen.length > 0" class="w-1/4 mb-4">
			<svws-ui-select ref="selectSprachen"
				title="Hinzufügen..."
				:model-value="undefined"
				@update:model-value="hinzufuegen"
				:items="verfuegbareSprachen"
				:item-text="getTextBySprache"
				removable autofocus focus-class-content />
		</div>
		<ui-table-grid v-if="!gridManager.daten.isEmpty()" name="Sprachbelegungen" :manager="() => gridManager" hide-selection>
			<template #header>
				<template v-for="col of gridManager.cols.values()" :key="col.name">
					<template v-if="col.kuerzel !== ''">
						<th v-if="col.kuerzel === 'Auswahl'" class="flex h-10 items-center justify-center">
							<svws-ui-checkbox :model-value="(auswahl.length === gridManager.daten.size()) && (auswahl.length > 0)"
								:indeterminate="(auswahl.length > 0) && (auswahl.length < gridManager.daten.size())"
								@update:model-value="value => auswahl = value ? [...gridManager.daten].map(d => d.data) : []" />
						</th>
						<th v-else-if="gridManager.isColVisible(col.kuerzel) ?? true" class="flex h-10" :class="{ 'text-left': ['Sprache'].includes(col.kuerzel) }">
							<div class="h-full content-center">
								<template v-if="col.kuerzel !== col.name">
									<svws-ui-tooltip>
										{{ col.kuerzel }}
										<template #content>{{ col.name }}</template>
									</svws-ui-tooltip>
								</template>
								<template v-else>{{ col.kuerzel }}</template>
							</div>
						</th>
					</template>
				</template>
			</template>
			<template #default="{ row }">
				<td class="flex items-center justify-center">
					<svws-ui-checkbox :model-value="auswahl.includes(row.data)" @update:model-value="toggleSelection(row.data)" />
				</td>
				<td class="text-left p-1"> {{ getTextBySprache(row.proxy.sprache) }} </td>
				<td v-if="hatSpalteNachweis">
					<svws-ui-checkbox v-model="row.proxy.istNachweis" headless :readonly />
				</td>
				<td class="ui-divider">
					<svws-ui-input-number v-if="!readonly" title="Reihenfolge" headless v-model="row.proxy.reihenfolge" @change="row.patch" :min="1" :max="8" />
					<span v-else> {{ row.proxy.reihenfolge ?? "-" }} </span>
				</td>
				<template v-if="hatSpaltenZeitraum">
					<td>
						<svws-ui-select v-if="!readonly" title="Von Jahrgang"
							v-model="row.belegungVonJahrgang.value"
							:items="sprachJahrgaenge"
							:item-text="jahrgangText"
							headless />
						<div v-else> {{ row.belegungVonJahrgang.value }} </div>
					</td>
					<td class="ui-divider">
						<div v-if="!readonly && alleVonAbschnitteErlaubt(row) && (row.belegungVonJahrgang.value !== null)"
							class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-100 w-fit m-auto p-[0.1rem] rounded-sm cursor-pointer"
							@click="toggleVonAbschnitt(row)">
							<span :class="{
								'opacity-100 font-bold': row.proxy.belegungVonAbschnitt === 1,
								'opacity-25 hover:opacity-100 font-medium': row.proxy.belegungVonAbschnitt === 2
							}">1</span>
							<span class="opacity-50">|</span>
							<span :class="{
								'opacity-100 font-bold': row.proxy.belegungVonAbschnitt === 2,
								'opacity-25 hover:opacity-100 font-medium': row.proxy.belegungVonAbschnitt === 1
							}">2</span>
						</div>
						<div v-else class="p-1"> {{ row.proxy.belegungVonAbschnitt ?? "" }} </div>
					</td>
					<td>
						<svws-ui-select v-if="!readonly"
							title="Bis Jahrgang"
							v-model="row.belegungBisJahrgang.value"
							:items="sprachJahrgaengeBis(row.belegungVonJahrgang.value)"
							:item-text="jahrgangText"
							headless use-null removable />
						<div v-else> {{ row.belegungBisJahrgang.value }} </div>
					</td>
					<td class="ui-divider">
						<div v-if="!readonly && alleBisAbschnitteErlaubt(row) && (row.belegungBisJahrgang.value !== null)"
							class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-100 w-fit m-auto p-[0.1rem] rounded-sm cursor-pointer"
							@click="toggleBisAbschnitt(row)">
							<span :class="{
								'opacity-100 font-bold': row.proxy.belegungBisAbschnitt === 1,
								'opacity-25 hover:opacity-100 font-medium': row.proxy.belegungBisAbschnitt === 2
							}">1</span>
							<span class="opacity-50">|</span>
							<span :class="{
								'opacity-100 font-bold': row.proxy.belegungBisAbschnitt === 2,
								'opacity-25 hover:opacity-100 font-medium': row.proxy.belegungBisAbschnitt === 1,
							}">2</span>
						</div>
						<div v-else class="p-1"> {{ row.proxy.belegungBisAbschnitt ?? "" }} </div>
					</td>
				</template>
				<td>
					<template v-if="row.proxy.sprache === 'G'">
						<svws-ui-checkbox v-if="!readonly" v-model="hatGraecum" headless>Graecum</svws-ui-checkbox>
						<div v-else-if="hatGraecum">Graecum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="row.proxy.sprache === 'H'">
						<svws-ui-checkbox v-if="!readonly" v-model="hatHebraicum" headless>Hebraicum</svws-ui-checkbox>
						<div v-else-if="hatHebraicum">Hebraicum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="row.proxy.sprache === 'L'">
						<svws-ui-select v-if="!readonly"
							:model-value="latinum"
							@update:model-value="val => patchLatinum(val ?? null, row)"
							:items="latein"
							:item-text="i => i.text"
							headless use-null removable />
						<div v-else> {{ latinum?.text ?? '-' }} </div>
					</template>
					<template v-else>
						<svws-ui-select v-if="!readonly"
							title="Referenzniveau"
							v-model="row.referenzniveau.value"
							:items="Sprachreferenzniveau.values()"
							:item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'"
							headless use-null removable />
						<div v-else> {{ row.referenzniveau }} </div>
					</template>
				</td>
			</template>
			<template #footer>
				<td class="col-span-2" />
				<td v-if="hatSpalteNachweis" />
				<td />
				<td v-if="hatSpaltenZeitraum" class="col-span-4" />
				<td class="flex flex-row justify-end">
					<svws-ui-button @click="remove" type="trash" :disabled="auswahl.length === 0" />
					<svws-ui-button v-if="serverState.hasDev" :disabled="auswahl.length === 0"
						@click="suchen" type="icon" size="small" title="Noch nicht implementiert: Diese Sprache in den Leistungsdaten suchen und Beginn und Ende aktualisieren">
						<span class="icon i-ri-search-line" />
					</svws-ui-button>
					<svws-ui-button v-if="serverState.hasDev" :disabled="auswahl.length === 0"
						@click="ermitteln" type="icon" size="small" title="Noch nicht implementiert: Das GER/Latinum anhand aller Daten ermitteln">
						<span class="icon i-ri-calculator-line" />
					</svws-ui-button>
				</td>
			</template>
		</ui-table-grid>
		<div v-else-if="readonly">
			keine Sprachenfolge vorhanden
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import type { List, Sprachbelegung } from '@core';
	import { ArrayList, Fach, Jahrgaenge, Schulform, Schulgliederung, Sprachreferenzniveau } from '@core';
	import { GridManager, useSchuleState, useServerState, type SvwsUiSelect } from '@ui';
	import { SchuelerSprachbelegungModelProxy } from './SchuelerSprachbelegungModelProxy';
	import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";

	const props = defineProps<{
		sprachbelegungen: () => List<Sprachbelegung>;
		patchSprachbelegung: (data: Partial<Sprachbelegung>, sprache: string) => Promise<void>;
		addSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<Sprachbelegung | null>;
		removeSprachbelegung: (data: Sprachbelegung) => Promise<Sprachbelegung>;
		schuelerListeManager: () => SchuelerListeManager;
		readonly: boolean;
	}>();
	const serverState = useServerState();
	const schuleState = useSchuleState();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());
	const auswahl = ref(new Array<Sprachbelegung>());
	const selectSprachen = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();
	const schulgliederung = computed<Schulgliederung | null>(() => Schulgliederung.data().getWertByIDOrNull(props.schuelerListeManager().auswahl().idSchulgliederung));
	const hatSpalteNachweis = computed<boolean>(() => schuleState.schulform === Schulform.WB);
	const hatSpaltenZeitraum = computed(() => {
		const istBKoderSB = [Schulform.BK, Schulform.SB].includes(schuleState.schulform);
		const istSpezielleGliederung = (schulgliederung.value !== null) && [Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung.value);
		return !(istBKoderSB && !istSpezielleGliederung);
	});

	function alleVonAbschnitteErlaubt(rowModel: SchuelerSprachbelegungModelProxy) {
		return !((rowModel.belegungVonJahrgang.value === rowModel.belegungBisJahrgang.value) && (rowModel.proxy.belegungBisAbschnitt === 1));
	}

	function alleBisAbschnitteErlaubt(rowModel: SchuelerSprachbelegungModelProxy) {
		return !((rowModel.belegungVonJahrgang.value === rowModel.belegungBisJahrgang.value) && (rowModel.proxy.belegungVonAbschnitt === 2));
	}

	function createList(sprachbelegungen: List<Sprachbelegung>) {
		const list = new ArrayList<SchuelerSprachbelegungModelProxy>();
		for (const sprachbelegung of sprachbelegungen) {
			const patchMethod = async (proxy: Partial<Sprachbelegung>) => {
				await props.patchSprachbelegung(proxy, sprachbelegung.sprache);
				return true;
			};
			const modelProxy = new SchuelerSprachbelegungModelProxy(() => sprachbelegung, props.schuelerListeManager, patchMethod);
			list.add(modelProxy);
		}
		return list;
	}
	const gridList = shallowRef<List<SchuelerSprachbelegungModelProxy>>(new ArrayList());
	watch(() => props.sprachbelegungen(), neu => gridList.value = createList(neu), { immediate: true });

	const gridManager = new GridManager<string, SchuelerSprachbelegungModelProxy, List<SchuelerSprachbelegungModelProxy>>({
		daten: computed(() => gridList.value),
		getRowKey: belegung => belegung.data.sprache,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Sprache", name: "Sprache", width: "16rem", hideable: false },
			{ kuerzel: "Nachweis", name: "Sprachbelegung einer zweiten Fremdsprache durch Nachweis erfolgt (siehe §34 Abst 3,4 APO-WbK)", width: "4rem", hideable: false },
			{ kuerzel: "Reihenfolge", name: "Reihenfolge", width: "6rem", hideable: false },
			{ kuerzel: "ab Jg", name: "belegt ab Jahrgang", width: "5rem", hideable: false },
			{ kuerzel: "ab Hj", name: "belegt ab Halbjahr", width: "5rem", hideable: false },
			{ kuerzel: "bis Jg", name: "belegt bis Jahrgang", width: "5rem", hideable: false },
			{ kuerzel: "bis Hj", name: "belegt bis Halbjahr", width: "5rem", hideable: false },
			{ kuerzel: "Referenzniveau", name: "das erreichte Referenzniveau nach dem gemeinsamen europäischen Referenznahmen", width: "8rem", hideable: false },
			{ kuerzel: "", name: "", width: "1.25rem", hideable: false },
		],
		colsVisible: computed({
			get: () => new Map(Object.entries({
				Auswahl: true,
				Sprache: true,
				Nachweis: hatSpalteNachweis.value,
				Reihenfolge: true,
				"ab Jg": hatSpaltenZeitraum.value,
				"ab Hj": hatSpaltenZeitraum.value,
				"bis Jg": hatSpaltenZeitraum.value,
				"bis Hj": hatSpaltenZeitraum.value,
				Referenzniveau: true,
				"": true,
			})),
			set: (_value) => {},
		}),
	});


	const verfuegbareSprachen = computed(() => {
		const belegungen = new Set();
		const sprachen = [];
		for (const b of gridManager.daten) {
			belegungen.add(b.data.sprache);
		}
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && !spracheEintrag.istErsatzPflichtFS && !spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach && !belegungen.has(k)) {
				sprachen.push(k);
			}
		}
		return sprachen;
	});

	const sprachJahrgaenge = computed(() => {
		const schulform = schuleState.schulform;
		if ((schulform === Schulform.BK) || (schulform === Schulform.SB)) {
			return Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, Schulform.GE);
		}
		if (schulform !== Schulform.WB) {
			return Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, schulform);
		}
		const jahrgaenge = new ArrayList<Jahrgaenge>(Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, schulform));
		jahrgaenge.addAll(Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, Schulform.R));
		jahrgaenge.sort({ compare(a, b) {
			return a.ordinal() - b.ordinal();
		} });
		return jahrgaenge;
	});

	function sprachJahrgaengeBis(jahrgangVon: Jahrgaenge | null) {
		const jahrgaenge_list = sprachJahrgaenge.value;
		const jahrgaenge = [];
		for (const jahrgang of jahrgaenge_list) {
			if ((jahrgangVon !== null) && (jahrgang.ordinal() >= jahrgangVon.ordinal())) {
				jahrgaenge.push(jahrgang);
			}
		}
		return jahrgaenge;
	}

	function getTextBySprache(kuerzel: string) {
		const fachEintrag = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(kuerzel)?.daten(schuljahr.value) ?? null;
		if (fachEintrag === null) {
			return '—';
		}
		if (fachEintrag.istHKFS) {
			return fachEintrag.text.replace(/^.*-/, "").trim();
		}
		return fachEintrag.text;
	}

	async function hinzufuegen(sprache: undefined | null | string) {
		if ((verfuegbareSprachen.value.length === 0) || (selectSprachen.value === undefined) || (sprache === null) || (sprache === undefined)) {
			selectSprachen.value?.reset();
			return;
		}
		const data: Partial<Sprachbelegung> = {};
		data.sprache = sprache;
		data.reihenfolge = gridManager.daten.size() + 1;
		const schulform = schuleState.schulform;
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB)) {
			data.belegungVonJahrgang = Jahrgaenge.data().getEintragByID(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzel;
		}
		await props.addSprachbelegung(data);
		selectSprachen.value.reset();
	}

	function jahrgangText(jg: Jahrgaenge | null) {
		if (jg === null) {
			return '—';
		}
		const jgDaten = jg.daten(schuljahr.value);
		if (jgDaten === null) {
			return '—';
		}
		return jgDaten.kuerzel;
	}

	const latein = [{ text: 'Kleines Latinum' }, { text: 'Latinum' }] as const;
	const latinum = computed(() => {
		if (hatKleinesLatinum.value) {
			return latein[0];
		}
		if (hatLatinum.value) {
			return latein[1];
		}
		return null;
	});

	async function patchLatinum(item: { text: 'Kleines Latinum' } | { text: 'Latinum' } | null, row: SchuelerSprachbelegungModelProxy) {
		if (item === null) {
			row.proxy.hatKleinesLatinum = false;
			row.proxy.hatLatinum = false;
		}
		if (item === latein[0]) {
			row.proxy.hatKleinesLatinum = true;
			row.proxy.hatLatinum = false;
		}
		if (item === latein[1]) {
			row.proxy.hatKleinesLatinum = false;
			row.proxy.hatLatinum = true;
		}
		await row.patch();
	}

	const hatKleinesLatinum = computed<boolean>(() => {
		for (const sprache of gridManager.daten) {
			if (sprache.proxy.sprache === 'L') {
				return sprache.proxy.hatKleinesLatinum;
			}
		}
		return false;
	});

	const hatLatinum = computed<boolean>(() => {
		for (const sprache of gridManager.daten) {
			if (sprache.proxy.sprache === 'L') {
				return sprache.proxy.hatLatinum;
			}
		}
		return false;
	});

	const hatGraecum = computed<boolean>({
		get: () => {
			for (const sprache of gridManager.daten) {
				if (sprache.proxy.sprache === 'G') {
					return sprache.proxy.hatGraecum;
				}
			}
			return false;
		},
		set: (hatGraecum) => {
			for (const sprache of gridManager.daten) {
				if (sprache.proxy.sprache === 'G') {
					sprache.proxy.hatGraecum = hatGraecum;
				}
			}
		},
	});

	const hatHebraicum = computed<boolean>({
		get: () => {
			for (const sprache of gridManager.daten) {
				if (sprache.proxy.sprache === 'H') {
					return sprache.proxy.hatHebraicum;
				}
			}
			return false;
		},
		set: (hatHebraicum) => {
			for (const sprache of gridManager.daten) {
				if (sprache.proxy.sprache === 'H') {
					sprache.proxy.hatHebraicum = hatHebraicum;
				}
			}
		},
	});

	async function remove() {
		for (const sprache of auswahl.value) {
			await props.removeSprachbelegung(sprache);
		}
		auswahl.value = [];
	}
	async function suchen() {
		// suche Sprache
	}
	async function ermitteln() {
		// ermittel Sprache
	}


	function toggleSelection(row: Sprachbelegung) {
		const idx = auswahl.value.indexOf(row);
		if (idx === -1) {
			auswahl.value.push(row);
		} else {
			auswahl.value.splice(idx, 1);
		}
	}

	async function toggleBisAbschnitt(rowModel: SchuelerSprachbelegungModelProxy) {
		if (!alleBisAbschnitteErlaubt(rowModel)) {
			return;
		}

		if (rowModel.proxy.belegungBisAbschnitt === 1) {
			rowModel.proxy.belegungBisAbschnitt = 2;
		} else {
			rowModel.proxy.belegungBisAbschnitt = 1;
		}
		await rowModel.patch();
	}

	async function toggleVonAbschnitt(rowModel: SchuelerSprachbelegungModelProxy) {
		if (!alleVonAbschnitteErlaubt(rowModel)) {
			return;
		}

		if (rowModel.proxy.belegungVonAbschnitt === 1) {
			rowModel.proxy.belegungVonAbschnitt = 2;
		} else {
			rowModel.proxy.belegungVonAbschnitt = 1;
		}
		await rowModel.patch();
	}

</script>
