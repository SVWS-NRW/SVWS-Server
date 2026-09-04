<template>
	<svws-ui-content-card title="Sprachprüfungen — Feststellungsprüfungen">
		<div v-if="!readonly && verfuegbareSprachpruefungen.length" class="w-1/4 mb-4">
			<svws-ui-select title="Hinzufügen..." removable :model-value="undefined" @update:model-value="sprache => hinzufuegen(sprache, false)"
				:items="verfuegbareSprachpruefungen" :item-text="getTextBySprache" ref="selectSprachpruefung" focus-class />
		</div>
		<ui-table-grid v-if="!gridManager.daten.isEmpty()" :manager="() => gridManager" name="Sprachprüfungen" hide-selection>
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
				<td class="ui-divider">
					<svws-ui-text-input v-if="!readonly" title="Zeugnisbezeichnung" headless v-model="row.proxy.zeugnisbezeichnung" @change="row.patch" />
					<div v-else>{{ row.proxy.zeugnisbezeichnung }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Ersetzt" headless :items="row.ersetzt" :item-text="i=> i.key" removable v-model="row.ersetztSprache.value" />
					<div v-else class="text-ellipsis text-nowrap"> {{ row.ersetztSprache.value?.key ?? '-' }} </div>
				</td>
				<td>
					<svws-ui-checkbox :disabled="readonly" v-model="row.proxy.kannBelegungAlsFortgefuehrteSpracheErlauben" headless />
				</td>
				<td v-if="hatSpaltenJahrgang">
					<svws-ui-select v-if="!readonly" title="Jahrgang" headless removable v-model="row.jahrgang.value" :items="sprachJahrgaenge"
						:item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ row.jahrgang.value?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Sprachpruefungniveau" headless removable v-model="row.anspruchsniveauId.value"
						:items="Sprachpruefungniveau.values()" :item-text="i => i.daten.kuerzel" />
					<div v-else>{{ Sprachpruefungniveau.getByID(row.anspruchsniveauId.value?.daten.id ?? null)?.daten.kuerzel ?? '-' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" :items="Note.getNotenOhneTendenz()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" v-model="row.note.value" headless removable />
					<div v-else>{{ Note.fromNoteSekI(row.proxy.note)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Referenzniveau" headless removable v-model="row.referenzniveau.value"
						:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else> {{ row.referenzniveau }} </div>
				</td>
				<td>
					<svws-ui-text-input :disabled="readonly" placeholder="Prüfungsdatum" v-model="row.proxy.pruefungsdatum" @change="row.patch" type="date" headless />
				</td>
			</template>
			<template #footer>
				<td class="col-span-9" />
				<td class="flex flex-row justify-end">
					<svws-ui-button @click="remove" type="trash" :disabled="auswahl.length === 0" />
				</td>
			</template>
		</ui-table-grid>
		<div v-else-if="readonly">
			keine Sprachprüfungen für Feststellungsprüfungen vorhanden
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import { SchuelerSprachpruefungModelProxy } from './SchuelerSprachpruefungModelProxy';
	import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
	import type { Sprachpruefung } from '@core/asd/data/schueler/Sprachpruefung';
	import { Fach } from '@core/asd/types/fach/Fach';
	import { Jahrgaenge } from '@core/asd/types/jahrgang/Jahrgaenge';
	import { Schulform } from '@core/asd/types/schule/Schulform';
	import { Schulgliederung } from '@core/asd/types/schule/Schulgliederung';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useSchuleState } from '@ui/states/SchuleState';
	import { GridManager } from '@ui/ui/controls/tablegrid/GridManager';
	import SvwsUiSelect from '@ui/ui/controls/SvwsUiSelect.vue';
	import { Sprachreferenzniveau } from '@core/asd/types/fach/Sprachreferenzniveau';
	import { Sprachpruefungniveau } from '@core/core/types/fach/Sprachpruefungniveau';
	import { Note } from '@core/asd/types/Note';

	const props = defineProps<{
		sprachpruefungen: () => List<Sprachpruefung>;
		patchSprachpruefung: (data: Partial<Sprachpruefung>, id: number) => Promise<void>;
		addSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<Sprachpruefung | null>;
		removeSprachpruefung: (data: Sprachpruefung) => Promise<Sprachpruefung>;
		schuelerListeManager: () => SchuelerListeManager;
		readonly: boolean;
	}>();
	const schuleState = useSchuleState();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());
	const auswahl = ref<Sprachpruefung[]>([]);
	const selectSprachpruefung = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();
	const schulgliederung = computed<Schulgliederung | null>(() => Schulgliederung.data().getWertByIDOrNull(props.schuelerListeManager().auswahl().idSchulgliederung));
	const hatSpaltenJahrgang = computed(() => {
		const istBKoderSB = [Schulform.BK, Schulform.SB].includes(schuleState.schulform);
		const istSpezielleGliederung = (schulgliederung.value !== null) && [Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung.value);
		return !(istBKoderSB && !istSpezielleGliederung);
	});

	function createList(sprachpruefungen: List<Sprachpruefung>) {
		const list = new ArrayList<SchuelerSprachpruefungModelProxy>();
		for (const sprachpruefung of sprachpruefungen) {
			if (!sprachpruefung.istHSUPruefung) {
				const patchMethod = async (proxy: Partial<Sprachpruefung>) => {
					await props.patchSprachpruefung(proxy, sprachpruefung.id);
					return true;
				};
				const modelProxy = new SchuelerSprachpruefungModelProxy(() => sprachpruefung, props.schuelerListeManager, patchMethod);
				list.add(modelProxy);
			}
		}
		return list;
	}
	const gridList = shallowRef<List<SchuelerSprachpruefungModelProxy>>(new ArrayList());
	watch(() => props.sprachpruefungen(), neu => gridList.value = createList(neu), { immediate: true });

	const gridManager = new GridManager<string, SchuelerSprachpruefungModelProxy, List<SchuelerSprachpruefungModelProxy>>({
		daten: computed(() => gridList.value),
		getRowKey: belegung => belegung.data.sprache,
		columns: [
			{ kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Sprache", name: "Kürzel der Sprache", width: "8rem", hideable: false },
			{ kuerzel: "Zeugnistext", name: "Zeugnistext", width: "8rem", hideable: false },
			{ kuerzel: "Ersetzt", name: "Ersetzt", width: "15rem", hideable: false },
			{ kuerzel: "Fortgef", name: "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", width: "5rem", hideable: false },
			{ kuerzel: "Jahrgang", name: "Im Jahrgang", width: "5rem", hideable: false },
			{ kuerzel: "Anspruchsniveau", name: "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", width: "9rem", hideable: false },
			{ kuerzel: "Note", name: "Prüfungsnote", width: "5rem", hideable: false },
			{ kuerzel: "Referenzniveau", name: "Das Kürzel des Referenzniveau nach dem gemeinsamen europäischen Referenznahmen, welches durch die Prüfung erreicht wurde", width: "9rem", hideable: false },
			{ kuerzel: "Prüfungsdatum", name: "Prüfungsdatum", width: "10rem", hideable: false },
		],
		colsVisible: computed<Map<string, boolean | null>>({
			get: () => new Map(Object.entries({
				Sprache: true,
				Zeugnistext: true,
				Ersetzt: true,
				"Fortgef. Fs. GOSt": true,
				"Jahrgang": hatSpaltenJahrgang.value,
				Anspruchsniveau: true,
				Note: true,
				Referenzniveau: true,
				"Prüfungsdatum": true,
				"": true,
			})),
			set: (_value) => {},
		}),
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

	const verfuegbareSprachpruefungen = computed(() => {
		const sprachen = [];
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach) {
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

	async function hinzufuegen(sprache: string | undefined | null, hsu: boolean) {
		if ((verfuegbareSprachpruefungen.value.length === 0) || (selectSprachpruefung.value === undefined)
			|| (sprache === null) || (sprache === undefined)) {
			selectSprachpruefung.value?.reset();
			return;
		}
		const data: Partial<Sprachpruefung> = {};
		data.sprache = sprache;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB)) {
			data.jahrgang = Jahrgaenge.data().getEintragByID(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzel;
		}
		data.istHSUPruefung = hsu;
		data.istFeststellungspruefung = !hsu;
		await props.addSprachpruefung(data);
		selectSprachpruefung.value.reset();
	}

	async function remove() {
		const list = auswahl.value;
		for (const pruefung of list) {
			await props.removeSprachpruefung(pruefung);
		}
		auswahl.value = [];
	}

	function toggleSelection(row: Sprachpruefung) {
		const idx = auswahl.value.indexOf(row);
		if (idx === -1) {
			auswahl.value.push(row);
		} else {
			auswahl.value.splice(idx, 1);
		}
	}

</script>
