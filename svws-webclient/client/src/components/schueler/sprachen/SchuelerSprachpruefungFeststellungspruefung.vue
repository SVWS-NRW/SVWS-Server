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
								@update:model-value="value => auswahl = value ? [...gridManager.daten] : []" />
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
				<td class="ui-divider">
					<svws-ui-text-input v-if="!readonly" title="Zeugnisbezeichnung" headless :model-value="row.zeugnisbezeichnung"
						@change="value => patchSprachpruefung({ zeugnisbezeichnung: value ?? '' }, row.sprache)" />
					<div v-else>{{ row.zeugnisbezeichnung }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Ersetzt" headless :items="ersetzt" :item-text="i=> i.key" removable
						:model-value="row.kannErstePflichtfremdspracheErsetzen ? ersetzt[0] : row.kannZweitePflichtfremdspracheErsetzen ? ersetzt[1] : row.kannWahlpflichtfremdspracheErsetzen ? ersetzt[2] : undefined"
						@update:model-value="o => patchSprachpruefung({kannErstePflichtfremdspracheErsetzen: o?.key === '1. Pflichtfremdsprache', kannZweitePflichtfremdspracheErsetzen: o?.key === '2. Pflichtfremdsprache', kannWahlpflichtfremdspracheErsetzen: o?.key === 'Wahlpflichtfremdsprache'}, row.sprache)" />
					<div v-else class="text-ellipsis text-nowrap"> {{ (row.kannErstePflichtfremdspracheErsetzen ? ersetzt[0] : row.kannZweitePflichtfremdspracheErsetzen ? ersetzt[1] : row.kannWahlpflichtfremdspracheErsetzen ? ersetzt[2] : undefined)?.key ?? '-' }} </div>
				</td>
				<td>
					<svws-ui-checkbox :disabled="readonly" :model-value="row.kannBelegungAlsFortgefuehrteSpracheErlauben"
						@update:model-value="kannBelegungAlsFortgefuehrteSpracheErlauben => patchSprachpruefung({kannBelegungAlsFortgefuehrteSpracheErlauben}, row.sprache)" headless />
				</td>
				<td v-if="hatSpaltenJahrgang">
					<svws-ui-select v-if="!readonly" title="Jahrgang" headless removable
						:model-value="(row.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.jahrgang)"
						@update:model-value="jahrgang => patchSprachpruefung({jahrgang: jahrgang?.daten(schuljahr)?.kuerzel ?? null}, row.sprache)" :items="sprachJahrgaenge"
						:item-text="i => i?.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ (row.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(row.jahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Sprachpruefungniveau" headless removable :model-value="Sprachpruefungniveau.getByID(row.anspruchsniveauId)"
						@update:model-value="anspruchsniveau => patchSprachpruefung({anspruchsniveauId: anspruchsniveau?.daten.id || null}, row.sprache)"
						:items="Sprachpruefungniveau.values()" :item-text="i => i.daten.beschreibung" />
					<div v-else>{{ Sprachpruefungniveau.getByID(row.anspruchsniveauId)?.daten.kuerzel ?? '-' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" :items="Note.getNotenOhneTendenz()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'"
						:model-value="Note.fromNoteSekI(row.note)"
						@update:model-value="note => patchSprachpruefung({ note: ((note === null) || (note === undefined)) ? null : note.getNoteSekI(schuljahr) }, row.sprache)"
						headless removable />
					<div v-else>{{ Note.fromNoteSekI(row.note)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</td>
				<td>
					<svws-ui-select v-if="!readonly" title="Referenzniveau" headless removable
						:model-value="(row.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertBySchluessel(row.referenzniveau)"
						@update:model-value="referenzniveau => patchSprachpruefung({referenzniveau: referenzniveau?.daten(schuljahr)?.schluessel ?? null}, row.sprache)"
						:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ (row.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(row.referenzniveau)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</td>
				<td>
					<svws-ui-text-input :disabled="readonly" placeholder="Prüfungsdatum" :model-value="row.pruefungsdatum"
						@change="pruefungsdatum => pruefungsdatum && patchSprachpruefung({pruefungsdatum}, row.sprache)" type="date" headless />
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

	import { computed, ref } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import { ArrayList, Fach, Jahrgaenge, Schulform, Schulgliederung, Sprachpruefungniveau, Sprachreferenzniveau, Note } from '@core';
	import type { List, ServerMode, Sprachpruefung } from '@core';
	import { GridManager, type SchuelerListeManager, type SvwsUiSelect } from '@ui';

	const props = defineProps<{
		sprachpruefungen: () => List<Sprachpruefung>;
		patchSprachpruefung: (data: Partial<Sprachpruefung>, sprache: string) => Promise<void>;
		addSprachpruefung: (data: Partial<Sprachpruefung>) => Promise<Sprachpruefung | null>;
		removeSprachpruefung: (data: Sprachpruefung) => Promise<Sprachpruefung>;
		schuelerListeManager: () => SchuelerListeManager;
		schulform: Schulform;
		serverMode: ServerMode;
		readonly: boolean;
	}>();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());
	const auswahl = ref<Sprachpruefung[]>([]);
	const selectSprachpruefung = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();

	const ersetzt = [{ key: '1. Pflichtfremdsprache' }, { key: '2. Pflichtfremdsprache' }, { key: 'Wahlpflichtfremdsprache' }];
	const schulgliederung = computed<Schulgliederung | null>(() => Schulgliederung.data().getWertByKuerzel(props.schuelerListeManager().auswahl().schulgliederung));
	const hatSpaltenJahrgang = computed(() => {
		const istBKoderSB = [Schulform.BK, Schulform.SB].includes(props.schulform);
		const istSpezielleGliederung = (schulgliederung.value !== null) && [Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung.value);
		return !(istBKoderSB && !istSpezielleGliederung);
	});

	const gridManager = new GridManager<string, Sprachpruefung, List<Sprachpruefung>>({
		daten: computed<List<Sprachpruefung>>(() => {
			const list = new ArrayList<Sprachpruefung>();
			for (const s of props.sprachpruefungen()) {
				if (!s.istHSUPruefung) {
					list.add(s);
				}
			}
			return list;
		}),
		getRowKey: belegung => belegung.sprache,
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
		const pruefungenFeststellung = new Set();
		const sprachen = [];
		for (const p of props.sprachpruefungen()) {
			pruefungenFeststellung.add(p.sprache);
		}
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach && !pruefungenFeststellung.has(k)) {
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
			data.jahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
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
