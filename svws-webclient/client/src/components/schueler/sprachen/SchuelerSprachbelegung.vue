<template>
	<svws-ui-content-card title="Sprachenfolge">
		<div v-if="!readonly && verfuegbareSprachen.length > 0" class="w-1/4 mb-4">
			<svws-ui-select title="Hinzufügen..." removable :model-value="undefined" @update:model-value="hinzufuegen"
				:items="verfuegbareSprachen" :item-text="getTextBySprache" ref="selectSprachen" autofocus focus-class-content />
		</div>
		<ui-table-grid v-if="!sprachbelegungen().isEmpty()" name="Sprachbelegungen" :manager="() => gridManager" hide-selection>
			<template #header>
				<template v-for="col of gridManager.cols.values()" :key="col.name">
					<template v-if="col.kuerzel !== ''">
						<th v-if="col.kuerzel === 'Auswahl'" class="flex h-10 items-center justify-center">
							<svws-ui-checkbox :model-value="(auswahl.length === sprachbelegungen().size()) && (auswahl.length > 0)"
								:indeterminate="(auswahl.length > 0) && (auswahl.length < sprachbelegungen().size())"
								@update:model-value="value => auswahl = value ? [...sprachbelegungen()] : []" />
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
					<svws-ui-checkbox :model-value="auswahl.includes(row)" @update:model-value="toggleSelection(row)" />
				</td>
				<td class="text-left p-1"> {{ getTextBySprache(row.sprache) }} </td>
				<td v-if="hatSpalteNachweis">
					<svws-ui-checkbox :model-value="row.istNachweis" @update:model-value="istNachweis => patchSprachbelegung({ istNachweis }, row.sprache)" headless :readonly />
				</td>
				<td class="ui-divider">
					<svws-ui-input-number v-if="!readonly" title="Reihenfolge" headless :model-value="row.reihenfolge"
						@update:model-value="reihenfolge=>reihenfolge && patchSprachbelegung({reihenfolge}, row.sprache)" :min="1" :max="8" />
					<span v-else> {{ row.reihenfolge ?? "-" }} </span>
				</td>
				<template v-if="hatSpaltenZeitraum">
					<td>
						<svws-ui-select v-if="!readonly" title="Von Jahrgang" headless :model-value="(row.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.belegungVonJahrgang)"
							@update:model-value="jahrgang => jahrgang?.daten(schuljahr)?.kuerzel && patchSprachbelegung({belegungVonJahrgang: jahrgang.daten(schuljahr)!.kuerzel, sprache: row.sprache}, row.sprache)"
							:items="sprachJahrgaenge" :item-text="jahrgangText" />
						<div v-else> {{ (row.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.belegungVonJahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }} </div>
					</td>
					<td class="ui-divider">
						<div v-if="!readonly" class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-100 w-fit m-auto p-[0.1rem] rounded-sm cursor-pointer"
							@click="patchSprachbelegung({belegungVonAbschnitt: row.belegungVonAbschnitt === 1 ? 2 : 1}, row.sprache)">
							<span :class="{ 'opacity-100 font-bold': row.belegungVonAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': row.belegungVonAbschnitt === 2}">1</span>
							<span class="opacity-50">|</span>
							<span :class="{ 'opacity-100 font-bold': row.belegungVonAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': row.belegungVonAbschnitt === 1}">2</span>
						</div>
						<div v-else> {{ row.belegungVonAbschnitt ?? "?" }} </div>
					</td>
					<td>
						<svws-ui-select v-if="!readonly" title="Bis Jahrgang" headless removable :model-value="(row.belegungBisJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.belegungBisJahrgang)"
							@update:model-value="jahrgang => patchSprachbelegung({belegungBisJahrgang: jahrgang?.daten(schuljahr)?.kuerzel ?? null}, row.sprache)"
							:items="sprachJahrgaengeBis(row).value" :item-text="jahrgangText" />
						<div v-else> {{ (row.belegungBisJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.belegungBisJahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }} </div>
					</td>
					<td class="ui-divider">
						<div v-if="!readonly" class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-100 w-fit m-auto p-[0.1rem] rounded-sm cursor-pointer"
							@click="patchSprachbelegung({belegungBisAbschnitt: row.belegungBisAbschnitt === 1 ? 2 : 1}, row.sprache)">
							<span :class="{ 'opacity-100 font-bold': row.belegungBisAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': row.belegungBisAbschnitt === 2}">1</span>
							<span class="opacity-50">|</span>
							<span :class="{ 'opacity-100 font-bold': row.belegungBisAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': row.belegungBisAbschnitt === 1}">2</span>
						</div>
						<div v-else> {{ row.belegungBisAbschnitt ?? "?" }} </div>
					</td>
				</template>
				<td>
					<template v-if="row.sprache === 'G'">
						<svws-ui-checkbox v-if="!readonly" v-model="hatGraecum" headless title="Graecum">Graecum</svws-ui-checkbox>
						<div v-else-if="hatGraecum">Graecum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="row.sprache === 'H'">
						<svws-ui-checkbox v-if="!readonly" v-model="hatHebraicum" headless title="Hebraicum">Hebraicum</svws-ui-checkbox>
						<div v-else-if="hatHebraicum">Hebraicum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="row.sprache === 'L'">
						<svws-ui-select v-if="!readonly" headless :items="latein" :model-value="latinum" :item-text="i => i.text" @update:model-value="patchLatinum" removable />
						<div v-else> {{ latinum?.text ?? '-' }} </div>
					</template>
					<template v-else>
						<svws-ui-select v-if="!readonly" title="Referenzniveau" headless removable
							:model-value="(row.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertBySchluessel(row.referenzniveau)"
							@update:model-value="referenzniveau => patchSprachbelegung({referenzniveau: referenzniveau?.daten(schuljahr)?.schluessel ?? null}, row.sprache)"
							:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
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
					<svws-ui-button v-if="serverMode === ServerMode.DEV" :disabled="auswahl.length === 0"
						@click="suchen" type="icon" size="small" title="Noch nicht implementiert: Diese Sprache in den Leistungsdaten suchen und Beginn und Ende aktualisieren">
						<span class="icon i-ri-search-line" />
					</svws-ui-button>
					<svws-ui-button v-if="serverMode === ServerMode.DEV" :disabled="auswahl.length === 0"
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

	import { computed, ref } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import type { List, Sprachbelegung } from '@core';
	import { ArrayList, Fach, Jahrgaenge, Schulform, Schulgliederung, ServerMode, Sprachreferenzniveau } from '@core';
	import { GridManager, type SchuelerListeManager, type SvwsUiSelect } from '@ui';

	const props = defineProps<{
		sprachbelegungen: () => List<Sprachbelegung>;
		patchSprachbelegung: (data: Partial<Sprachbelegung>, sprache: string) => Promise<void>;
		addSprachbelegung: (data: Partial<Sprachbelegung>) => Promise<Sprachbelegung | null>;
		removeSprachbelegung: (data: Sprachbelegung) => Promise<Sprachbelegung>;
		schuelerListeManager: () => SchuelerListeManager;
		schulform: Schulform;
		serverMode: ServerMode;
		readonly: boolean;
	}>();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());
	const auswahl = ref(new Array<Sprachbelegung>());
	const selectSprachen = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();

	const schulgliederung = computed<Schulgliederung | null>(() => Schulgliederung.data().getWertByKuerzel(props.schuelerListeManager().auswahl().schulgliederung));
	const hatSpalteNachweis = computed<boolean>(() => props.schulform === Schulform.WB);
	const hatSpaltenZeitraum = computed(() => {
		const istBKoderSB = [Schulform.BK, Schulform.SB].includes(props.schulform);
		const istSpezielleGliederung = (schulgliederung.value !== null) && [Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung.value);
		return !(istBKoderSB && !istSpezielleGliederung);
	});

	const gridManager = new GridManager<string, Sprachbelegung, List<Sprachbelegung>>({
		daten: computed<List<Sprachbelegung>>(() => props.sprachbelegungen()),
		getRowKey: belegung => belegung.sprache,
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
		for (const b of props.sprachbelegungen()) {
			belegungen.add(b.sprache);
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
		const schulform = props.schuelerListeManager().schulform();
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

	const sprachJahrgaengeBis = (sprachbelegung: Sprachbelegung) => computed(() => {
		const jahrgangVon = (sprachbelegung.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(sprachbelegung.belegungVonJahrgang);
		const jahrgaenge_list = sprachJahrgaenge.value;
		const jahrgaenge = [];
		for (const jahrgang of jahrgaenge_list) {
			if ((jahrgangVon !== null) && (jahrgang.ordinal() > jahrgangVon.ordinal())) {
				jahrgaenge.push(jahrgang);
			}
		}
		return jahrgaenge;
	});

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
		data.reihenfolge = props.sprachbelegungen().size() + 1;
		data.belegungVonAbschnitt = 1;
		data.belegungBisAbschnitt = 2;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB)) {
			data.belegungVonJahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		}
		await props.addSprachbelegung(data);
		selectSprachen.value.reset();
	}

	function jahrgangText(jg: Jahrgaenge | undefined) {
		if (jg === undefined) {
			return '—';
		}
		const jgDaten = jg.daten(schuljahr.value);
		if (jgDaten === null) {
			return '—';
		}
		return jgDaten.kuerzel;
	}

	const latein = [{ text: 'Kleines Latinum' }, { text: 'Latinum' }];
	const latinum = computed(() => {
		if (hatKleinesLatinum.value) {
			return latein[0];
		}
		if (hatLatinum.value) {
			return latein[1];
		}
		return undefined;
	});

	async function patchLatinum(item: any) {
		console.log(item);
		if (item === undefined) {
			await props.patchSprachbelegung({ hatKleinesLatinum: false, hatLatinum: false }, 'L');
		}
		if (item === latein[0]) {
			await props.patchSprachbelegung({ hatKleinesLatinum: true, hatLatinum: false }, 'L');
		}
		if (item === latein[1]) {
			await props.patchSprachbelegung({ hatKleinesLatinum: false, hatLatinum: true }, 'L');
		}
	}

	const hatKleinesLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen()) {
			if (sprache.sprache === 'L') {
				return sprache.hatKleinesLatinum;
			}
		}
		return false;
	});

	const hatLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen()) {
			if (sprache.sprache === 'L') {
				return sprache.hatLatinum;
			}
		}
		return false;
	});

	const hatGraecum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen()) {
				if (sprache.sprache === 'G') {
					return sprache.hatGraecum;
				}
			}
			return false;
		},
		set: (hatGraecum) => {
			props.patchSprachbelegung({ hatGraecum }, 'G').catch((err: unknown) => { /* nichts machen */ });
		},
	});

	const hatHebraicum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen()) {
				if (sprache.sprache === 'H') {
					return sprache.hatHebraicum;
				}
			}
			return false;
		},
		set: (hatHebraicum) => {
			props.patchSprachbelegung({ hatHebraicum }, 'H').catch((err: unknown) => { /* nichts machen */ });
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

</script>
