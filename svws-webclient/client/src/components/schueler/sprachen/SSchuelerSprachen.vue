<template>
	<div class="page page-flex-col min-w-196 max-w-354">
		<svws-ui-content-card title="Sprachenfolge">
			<div v-if="hatUpdateKompetenz && verfuegbareSprachen.length" class="w-1/4 mb-4">
				<svws-ui-select title="Hinzufügen..." removable :model-value="undefined" @update:model-value="sprache=> hinzufuegen(sprache)"
					:items="verfuegbareSprachen" :item-text="i => `${i} - ${Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(i)?.daten(schuljahr)?.text ?? '—'}`"
					ref="selectSprachen" autofocus focus-class-content />
			</div>
			<svws-ui-table v-if="sprachbelegungen().size()" :items="sprachbelegungen()" :columns="colsSprachenfolge" :selectable="hatUpdateKompetenz" v-model="auswahl">
				<template #cell(sprache)="{ value: kuerzel }">{{ Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(kuerzel)?.daten(schuljahr)?.text ?? '—' }} </template>
				<template #cell(reihenfolge)="{ rowData }">
					<svws-ui-input-number v-if="hatUpdateKompetenz" title="Reihenfolge" headless :model-value="rowData.reihenfolge"
						@update:model-value="reihenfolge=>reihenfolge && patchSprachbelegung({reihenfolge}, rowData.sprache)" :min="1" :max="9" />
					<span v-else> {{ rowData.reihenfolge ?? "-" }} </span>
				</template>
				<template #cell(belegungVonAbschnitt)="{ rowData }">
					<div v-if="hatUpdateKompetenz" class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-0 my-auto p-[0.1rem] rounded-sm cursor-pointer"
						@click="patchSprachbelegung({belegungVonAbschnitt: rowData.belegungVonAbschnitt === 1 ? 2 : 1}, rowData.sprache)">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 2}">1</span>
						<span class="opacity-50">|</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungVonAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungVonAbschnitt === 1}">2</span>
					</div>
					<div v-else> {{ rowData.belegungVonAbschnitt ?? "?" }} </div>
				</template>
				<template #cell(belegungVonJahrgang)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Von Jahrgang" headless :model-value="(rowData.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.belegungVonJahrgang)"
						@update:model-value="jahrgang => jahrgang?.daten(schuljahr)?.kuerzel && patchSprachbelegung({belegungVonJahrgang: jahrgang.daten(schuljahr)!.kuerzel, sprache: rowData.sprache}, rowData.sprache)"
						:items="sprachJahrgaenge" :item-text="jahrgangText" />
					<div v-else> {{ (rowData.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.belegungVonJahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }} </div>
				</template>
				<template #cell(belegungBisAbschnitt)="{ rowData }">
					<div v-if="hatUpdateKompetenz" class="flex items-center gap-0.5 border border-ui-25 border-dashed hover:border-ui-50 hover:border-solid hover:bg-ui-0 my-auto p-[0.1rem] rounded-sm cursor-pointer"
						@click="patchSprachbelegung({belegungBisAbschnitt: rowData.belegungBisAbschnitt === 1 ? 2 : 1}, rowData.sprache)">
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 1, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 2}">1</span>
						<span class="opacity-50">|</span>
						<span :class="{ 'opacity-100 font-bold': rowData.belegungBisAbschnitt === 2, 'opacity-25 hover:opacity-100 font-medium': rowData.belegungBisAbschnitt === 1}">2</span>
					</div>
					<div v-else> {{ rowData.belegungBisAbschnitt ?? "?" }} </div>
				</template>
				<template #cell(belegungBisJahrgang)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Bis Jahrgang" headless removable :model-value="(rowData.belegungBisJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.belegungBisJahrgang)"
						@update:model-value="jahrgang => patchSprachbelegung({belegungBisJahrgang: jahrgang?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)"
						:items="sprachJahrgaengeBis(rowData).value" :item-text="jahrgangText" />
					<div v-else> {{ (rowData.belegungBisJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.belegungBisJahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }} </div>
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<template v-if="rowData.sprache === 'G'">
						<svws-ui-checkbox v-if="hatUpdateKompetenz" v-model="hatGraecum" headless title="Graecum">Graecum</svws-ui-checkbox>
						<div v-else-if="hatGraecum">Graecum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="rowData.sprache === 'H'">
						<svws-ui-checkbox v-if="hatUpdateKompetenz" v-model="hatHebraicum" headless title="Hebraicum">Hebraicum</svws-ui-checkbox>
						<div v-else-if="hatHebraicum">Hebraicum</div>
						<div v-else>-</div>
					</template>
					<template v-else-if="rowData.sprache === 'L'">
						<svws-ui-select v-if="hatUpdateKompetenz" headless :items="latein" :model-value="latinum" :item-text="i => i.text" @update:model-value="patchLatinum" removable />
						<div v-else> {{ latinum?.text ?? '-' }} </div>
					</template>
					<template v-else>
						<svws-ui-select v-if="hatUpdateKompetenz" title="Referenzniveau" headless removable
							:model-value="(rowData.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(rowData.referenzniveau)"
							@update:model-value="referenzniveau => patchSprachbelegung({referenzniveau: referenzniveau?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)"
							:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
						<div v-else> {{ rowData.referenzniveau }} </div>
					</template>
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-button @click="remove" type="trash" :disabled="auswahl.length === 0" />
					<svws-ui-button v-if="serverMode === ServerMode.DEV" @click="suchen" type="icon" title="Noch nicht implementiert: Diese Sprache in den Leistungsdaten suchen und Beginn und Ende aktualisieren" :disabled="auswahl.length === 0"> <span class="icon i-ri-search-line" /></svws-ui-button>
					<svws-ui-button v-if="serverMode === ServerMode.DEV" @click="ermitteln" type="icon" title="Noch nicht implementiert: Das GER/Latinum anhand aller Daten ermitteln" :disabled="auswahl.length === 0"><span class="icon i-ri-calculator-line" /></svws-ui-button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen – Herkunftsprachlicher Unterricht">
			<div v-if="hatUpdateKompetenz && verfuegbareSprachenPruefungenHerkunftsprachlich.length" class="w-1/4 mb-4">
				<svws-ui-select title="Hinzufügen..." removable :model-value="undefined" @update:model-value="sprache=> hinzufuegenPruefungHerkunftssprachlich(sprache, true)"
					:items="verfuegbareSprachenPruefungenHerkunftsprachlich" :item-text="i=> `${i} - ${ Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(i)?.daten(schuljahr)?.text ?? '—' }`"
					ref="selectSprachenPruefungHerkunftsprachlich" focus-class-content />
			</div>
			<svws-ui-table v-if="sprachpruefungenHSU.length" :items="sprachpruefungenHSU" :columns="colsSprachpruefungenHSU" :selectable="hatUpdateKompetenz" v-model="auswahlPrHSU">
				<template #cell(sprache)="{ value: kuerzel }">{{ Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(kuerzel)?.daten(schuljahr)?.text }} </template>
				<template #cell(kannBelegungAlsFortgefuehrteSpracheErlauben)="{ rowData }">
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" :model-value="rowData.kannBelegungAlsFortgefuehrteSpracheErlauben"
						@update:model-value="kannBelegungAlsFortgefuehrteSpracheErlauben => patchSprachpruefung({kannBelegungAlsFortgefuehrteSpracheErlauben}, rowData.sprache)"
						headless />
				</template>
				<template #cell(jahrgang)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Jahrgang" headless removable
						:model-value="(rowData.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.jahrgang)"
						@update:model-value="jahrgang => patchSprachpruefung({jahrgang: jahrgang?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)"
						:items="sprachJahrgaenge" :item-text="jahrgangText" />
					<div v-else>{{ (rowData.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.jahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(anspruchsniveauId)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Sprachpruefungniveau" headless removable :model-value="Sprachpruefungniveau.getByID(rowData.anspruchsniveauId)"
						@update:model-value="anspruchsniveau => patchSprachpruefung({anspruchsniveauId: anspruchsniveau?.daten.id || null}, rowData.sprache)"
						:items="Sprachpruefungniveau.values()" :item-text="i => i.daten.kuerzel" />
					<div v-else>{{ Sprachpruefungniveau.getByID(rowData.anspruchsniveauId)?.daten.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(note)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" :items="Note.getNotenOhneTendenz()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'"
						:model-value="Note.fromNoteSekI(rowData.note)"
						@update:model-value="note => patchSprachpruefung({ note: ((note === null) || (note === undefined)) ? null : note.getNoteSekI(schuljahr) }, rowData.sprache)"
						headless removable />
					<div v-else>{{ Note.fromNoteSekI(rowData.note)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Referenzniveau" headless removable
						:model-value="(rowData.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(rowData.referenzniveau)"
						@update:model-value="referenzniveau => patchSprachpruefung({referenzniveau: referenzniveau?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)"
						:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ (rowData.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(rowData.referenzniveau)?.daten(schuljahr)?.kuerzel ?? '-' }}</div>
				</template>
				<template #cell(pruefungsdatum)="{ rowData }">
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Prüfungsdatum" :model-value="rowData.pruefungsdatum"
						@change="pruefungsdatum => pruefungsdatum && patchSprachpruefung({pruefungsdatum}, rowData.sprache)" type="date" headless />
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-button @click="removePruefungen(true)" type="trash" :disabled="auswahlPrHSU.length === 0" />
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen – Feststellungsprüfungen">
			<div v-if="hatUpdateKompetenz && verfuegbareSprachenPruefungenFeststellungErsatz.length" class="w-1/4 mb-4">
				<svws-ui-select title="Hinzufügen..." removable :model-value="undefined" @update:model-value="sprache => hinzufuegenPruefungFeststellung(sprache, false)" :items="verfuegbareSprachenPruefungenFeststellungErsatz"
					:item-text="i => `${i} - ${Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(i)?.daten(schuljahr)?.text ?? '—'}`" ref="selectSprachenPruefungFeststellungErsatz" focus-class />
			</div>
			<svws-ui-table v-if="sprachpruefungenFP.length" :items="sprachpruefungenFP" :columns="colsSprachpruefungenFP" :selectable="hatUpdateKompetenz" v-model="auswahlPrFP">
				<template #cell(sprache)="{ value: kuerzel }">{{ Fach.getMapFremdsprachenKuerzelAtomar(schuljahr).get(kuerzel)?.daten(schuljahr)?.text ?? '—' }} </template>
				<template #cell(ersetzt)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Ersetzt" headless :items="ersetzt" :item-text="i=> i.key" removable
						:model-value="rowData.kannErstePflichtfremdspracheErsetzen ? ersetzt[0] : rowData.kannZweitePflichtfremdspracheErsetzen ? ersetzt[1] : rowData.kannWahlpflichtfremdspracheErsetzen ? ersetzt[2] : undefined"
						@update:model-value="o => patchSprachpruefung({kannErstePflichtfremdspracheErsetzen: o?.key === '1. Pflichtfremdsprache', kannZweitePflichtfremdspracheErsetzen: o?.key === '2. Pflichtfremdsprache', kannWahlpflichtfremdspracheErsetzen: o?.key === 'Wahlpflichtfremdsprache'}, rowData.sprache)" />
					<div v-else class="text-ellipsis text-nowrap"> {{ (rowData.kannErstePflichtfremdspracheErsetzen ? ersetzt[0] : rowData.kannZweitePflichtfremdspracheErsetzen ? ersetzt[1] : rowData.kannWahlpflichtfremdspracheErsetzen ? ersetzt[2] : undefined)?.key ?? '-' }} </div>
				</template>
				<template #cell(kannBelegungAlsFortgefuehrteSpracheErlauben)="{ rowData }">
					<svws-ui-checkbox :disabled="!hatUpdateKompetenz" :model-value="rowData.kannBelegungAlsFortgefuehrteSpracheErlauben"
						@update:model-value="kannBelegungAlsFortgefuehrteSpracheErlauben => patchSprachpruefung({kannBelegungAlsFortgefuehrteSpracheErlauben}, rowData.sprache)" headless />
				</template>
				<template #cell(jahrgang)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Jahrgang" headless removable
						:model-value="(rowData.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.jahrgang)"
						@update:model-value="jahrgang => patchSprachpruefung({jahrgang: jahrgang?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)" :items="sprachJahrgaenge"
						:item-text="i => i?.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ (rowData.jahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(rowData.jahrgang)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(anspruchsniveauId)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Sprachpruefungniveau" headless removable :model-value="Sprachpruefungniveau.getByID(rowData.anspruchsniveauId)"
						@update:model-value="anspruchsniveau => patchSprachpruefung({anspruchsniveauId: anspruchsniveau?.daten.id || null}, rowData.sprache)"
						:items="Sprachpruefungniveau.values()" :item-text="i => i.daten.kuerzel" />
					<div v-else>{{ Sprachpruefungniveau.getByID(rowData.anspruchsniveauId)?.daten.kuerzel ?? '-' }}</div>
				</template>
				<template #cell(note)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" :items="Note.getNotenOhneTendenz()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'"
						:model-value="Note.fromNoteSekI(rowData.note)"
						@update:model-value="note => patchSprachpruefung({ note: ((note === null) || (note === undefined)) ? null : note.getNoteSekI(schuljahr) }, rowData.sprache)"
						headless removable />
					<div v-else>{{ Note.fromNoteSekI(rowData.note)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(referenzniveau)="{ rowData }">
					<svws-ui-select v-if="hatUpdateKompetenz" title="Referenzniveau" headless removable
						:model-value="(rowData.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(rowData.referenzniveau)"
						@update:model-value="referenzniveau => patchSprachpruefung({referenzniveau: referenzniveau?.daten(schuljahr)?.kuerzel ?? null}, rowData.sprache)"
						:items="Sprachreferenzniveau.values()" :item-text="i => i.daten(schuljahr)?.kuerzel ?? '—'" />
					<div v-else>{{ (rowData.referenzniveau === null) ? null : Sprachreferenzniveau.data().getWertByKuerzel(rowData.referenzniveau)?.daten(schuljahr)?.kuerzel ?? '—' }}</div>
				</template>
				<template #cell(pruefungsdatum)="{ rowData }">
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Prüfungsdatum" :model-value="rowData.pruefungsdatum"
						@change="pruefungsdatum => pruefungsdatum && patchSprachpruefung({pruefungsdatum}, rowData.sprache)" type="date" headless />
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-button @click="removePruefungen(false)" type="trash" :disabled="auswahlPrFP.length === 0" />
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { ComponentExposed } from 'vue-component-type-helpers';
	import type { SchuelerSprachenProps } from './SSchuelerSprachenProps';
	import type { DataTableColumn, SvwsUiSelect } from "@ui";
	import type { Sprachbelegung , Sprachpruefung} from '@core';
	import { Schulform, Sprachreferenzniveau, Fach, Jahrgaenge, Note, Schulgliederung, Sprachpruefungniveau, ServerMode, BenutzerKompetenz, ArrayList } from '@core';

	const props = defineProps<SchuelerSprachenProps>();

	const hatUpdateKompetenz = computed<boolean>(() => {
		return (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))
			|| ((props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN))
				&& props.benutzerKompetenzenKlassen.has(props.schuelerListeManager().auswahl().idKlasse));
	});

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());

	const auswahl = ref([]);
	const auswahlPrHSU = ref([]);
	const auswahlPrFP = ref([]);
	const selectSprachen = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();
	const selectSprachenPruefungHerkunftsprachlich = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();
	const selectSprachenPruefungFeststellungErsatz = ref<ComponentExposed<typeof SvwsUiSelect<string[]>>>();

	const colsSprachenfolge = computed<DataTableColumn[]>(() => {
		const schulgliederung = Schulgliederung.data().getWertByKuerzel(props.schuelerListeManager().auswahl().schulgliederung);
		return [{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", span: 2 },
			{ key: "reihenfolge", label: "Reihenfolge", tooltip: "Reihenfolge", divider: true },
			...(([Schulform.BK, Schulform.SB].includes(props.schuelerListeManager().schulform()) && !(schulgliederung && ([Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung))))
				? []
				: [{ key: "belegungVonJahrgang", label: "ab Jg", tooltip: "belegt ab Jahrgang" },
					{ key: "belegungVonAbschnitt", label: "Halbjahr", tooltip: "belegt ab Abschnitt", divider: true },
					{ key: "belegungBisJahrgang", label: "bis Jg", tooltip: "belegt bis Jahrgang" },
					{ key: "belegungBisAbschnitt", label: "Halbjahr", tooltip: "belegt bis Abschnitt", divider: true }]),
			{ key: "referenzniveau", label: "Referenzniveau", tooltip: "das erreichte Referenzniveau nach dem gemeinsamen europäischen Referenznahmen", span: 2 },
		]}
	);

	const colsSprachpruefungenHSU = computed<DataTableColumn[]>(() => {
		const schulgliederung = Schulgliederung.data().getWertByKuerzel(props.schuelerListeManager().auswahl().schulgliederung);
		return [{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", minWidth: 4 },
			{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgef. Fs. GOSt", tooltip: "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", align: 'center', minWidth: 4 },
			...([Schulform.BK, Schulform.SB].includes(props.schuelerListeManager().schulform()) && !(schulgliederung && ([Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung)))
				? [] : [{ key: "jahrgang", label: "Jahrgang", tooltip: "Im Jahrgang", minWidth: 4 }]),
			{ key: "anspruchsniveauId", label: "Anspruchsniveau", tooltip: "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", minWidth: 4 },
			{ key: "note", label: "Note", tooltip: "Prüfungsnote", minWidth: 2 },
			{ key: "referenzniveau", label: "Referenzniveau", tooltip: "Das Kürzel des Referenzniveau nach dem gemeinsamen europäischen Referenznahmen, welches durch die Prüfung erreicht wurde", minWidth: 3 },
			{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", minWidth: 3 },
		]
	});
	const colsSprachpruefungenFP = computed<DataTableColumn[]>(() => {
		const schulgliederung = Schulgliederung.data().getWertByKuerzel(props.schuelerListeManager().auswahl().schulgliederung);
		return [{ key: "sprache", label: "Sprache", tooltip: "Kürzel der Sprache", minWidth: 4 },
			{ key: "ersetzt", label: "Ersetzt", minWidth: 4 },
			{ key: "kannBelegungAlsFortgefuehrteSpracheErlauben", label: "Fortgef. Fs. GOSt", tooltip: "Durch die Prüfung kann die Sprache als fortgeführte Fremdsprache in der GOSt belegt werden", align: 'center', minWidth: 4 },
			...([Schulform.BK, Schulform.SB].includes(props.schuelerListeManager().schulform()) && !(schulgliederung && ([Schulgliederung.D01, Schulgliederung.D02].includes(schulgliederung)))
				? [] : [{ key: "jahrgang", label: "Jahrgang", tooltip: "Im Jahrgang", minWidth: 4 }]),
			{ key: "anspruchsniveauId", label: "Anspruchsniveau", tooltip: "Bezeichnung des am Schulabschluss orientierte Anspruchsniveau der Sprachprüfung", minWidth: 4 },
			{ key: "note", label: "Note", tooltip: "Prüfungsnote", minWidth: 2 },
			{ key: "referenzniveau", label: "Referenzniveau", tooltip: "Das Kürzel des Referenzniveau nach dem gemeinsamen europäischen Referenznahmen, welches durch die Prüfung erreicht wurde", minWidth: 3 },
			{ key: "pruefungsdatum", label: "Prüfungsdatum", tooltip: "Prüfungsdatum", minWidth: 3 },
		]
	});

	const verfuegbareSprachen = computed(() => {
		const belegungen = new Set();
		const sprachen = [];
		for (const b of props.sprachbelegungen())
			belegungen.add(b.sprache);
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && !spracheEintrag.istErsatzPflichtFS && !spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach && !belegungen.has(k))
				sprachen.push(k);
		}
		return sprachen;
	})

	const verfuegbareSprachenPruefungenHerkunftsprachlich = computed(() => {
		const pruefungenHKFS = new Set();
		const sprachen = [];
		for (const p of props.sprachpruefungen())
			pruefungenHKFS.add(p.sprache);
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && !spracheEintrag.istErsatzPflichtFS && spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach && !pruefungenHKFS.has(k))
				sprachen.push(k);
		}
		return sprachen;
	})

	const verfuegbareSprachenPruefungenFeststellungErsatz = computed(() => {
		const pruefungenFeststellung = new Set();
		const sprachen = [];
		for (const p of props.sprachpruefungen())
			pruefungenFeststellung.add(p.sprache);
		for (const k of Fach.getListFremdsprachenKuerzelAtomar(schuljahr.value)) {
			const sprache = Fach.getMapFremdsprachenKuerzelAtomar(schuljahr.value).get(k);
			const spracheEintrag = sprache?.daten(schuljahr.value) ?? null;
			if ((spracheEintrag !== null) && spracheEintrag.istErsatzPflichtFS && spracheEintrag.istHKFS && !spracheEintrag.istAusRegUFach && !pruefungenFeststellung.has(k))
				sprachen.push(k);
		}
		return sprachen;
	})

	const sprachpruefungenHSU = computed(() => {
		const list = [];
		for (const s of props.sprachpruefungen())
			if (s.istHSUPruefung)
				list.push(s);
		return list;
	})

	const sprachpruefungenFP = computed(() => {
		const list = [];
		for (const s of props.sprachpruefungen())
			if (!s.istHSUPruefung)
				list.push(s);
		return list;
	})

	const sprachJahrgaenge = computed(() => {
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform === Schulform.BK) || (schulform === Schulform.SB))
			return Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, Schulform.GE);
		if (schulform !== Schulform.WB)
			return Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, schulform);
		const jahrgaenge = new ArrayList<Jahrgaenge>(Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, schulform));
		jahrgaenge.addAll(Jahrgaenge.getListBySchuljahrAndSchulform(schuljahr.value, Schulform.R));
		jahrgaenge.sort({ compare(a, b) { return a.ordinal() - b.ordinal() } });
		return jahrgaenge;
	});

	const sprachJahrgaengeBis = (sprachbelegung: Sprachbelegung) => computed(() => {
		const jahrgangVon = (sprachbelegung.belegungVonJahrgang === null) ? null : Jahrgaenge.data().getWertByKuerzel(sprachbelegung.belegungVonJahrgang);
		const jahrgaenge_list = sprachJahrgaenge.value;
		const jahrgaenge = [];
		for (const jahrgang of jahrgaenge_list)
			if ((jahrgangVon !== null) && (jahrgang.ordinal() > jahrgangVon.ordinal()))
				jahrgaenge.push(jahrgang)
		return jahrgaenge;
	});

	function jahrgangText(jg: Jahrgaenge | undefined) {
		if (jg === undefined)
			return '—';
		const jgDaten = jg.daten(schuljahr.value);
		if (jgDaten === null)
			return '—';
		return jgDaten.kuerzel;
	}

	const latein = [{text: 'Kleines Latinum'}, {text: 'Latinum'}];
	const latinum = computed(() => {
		if (hatKleinesLatinum.value)
			return latein[0];
		if (hatLatinum.value)
			return latein[1];
		return
	})

	async function patchLatinum(item:any) {
		console.log(item)
		if (item === undefined)
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: false}, 'L');
		if (item === latein[0])
			await props.patchSprachbelegung({hatKleinesLatinum: true, hatLatinum: false}, 'L');
		if (item === latein[1])
			await props.patchSprachbelegung({hatKleinesLatinum: false, hatLatinum: true}, 'L');
	}

	const hatKleinesLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatKleinesLatinum;
		return false;
	})

	const hatLatinum = computed<boolean>(() => {
		for (const sprache of props.sprachbelegungen())
			if (sprache.sprache === 'L')
				return sprache.hatLatinum;
		return false;
	});

	const hatGraecum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'G')
					return sprache.hatGraecum;
			return false;
		},
		set: (hatGraecum) => void props.patchSprachbelegung({hatGraecum}, 'G'),
	});

	const hatHebraicum = computed<boolean>({
		get: () => {
			for (const sprache of props.sprachbelegungen())
				if (sprache.sprache === 'H')
					return sprache.hatHebraicum;
			return false;
		},
		set: (hatHebraicum) => void props.patchSprachbelegung({hatHebraicum}, 'H'),
	});

	async function remove() {
		for (const sprache of auswahl.value)
			await props.removeSprachbelegung(sprache);
		auswahl.value = [];
	}
	async function suchen() {
		//suche Sprache
	}
	async function ermitteln() {
		//ermittel Sprache
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
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB))
			data.belegungVonJahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		await props.addSprachbelegung(data);
		selectSprachen.value.reset();
	}

	async function hinzufuegenPruefungHerkunftssprachlich(sprache: string | undefined | null, hsu: boolean) {
		if ((verfuegbareSprachenPruefungenHerkunftsprachlich.value.length === 0) || (selectSprachenPruefungHerkunftsprachlich.value === undefined)
			|| (sprache === null) || (sprache === undefined)) {
			selectSprachenPruefungHerkunftsprachlich.value?.reset();
			return;
		}
		const data: Partial<Sprachpruefung> = {};
		data.sprache = sprache;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB))
			data.jahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		data.istHSUPruefung = hsu;
		data.istFeststellungspruefung = !hsu;
		await props.addSprachpruefung(data);
		selectSprachenPruefungHerkunftsprachlich.value.reset();
	}

	async function hinzufuegenPruefungFeststellung(sprache: string | undefined | null, hsu: boolean) {
		if ((verfuegbareSprachenPruefungenFeststellungErsatz.value.length === 0) || (selectSprachenPruefungFeststellungErsatz.value === undefined)
			|| (sprache === null) || (sprache === undefined)) {
			selectSprachenPruefungFeststellungErsatz.value?.reset();
			return;
		}
		const data: Partial<Sprachpruefung> = {};
		data.sprache = sprache;
		const schulform = props.schuelerListeManager().schulform();
		if ((schulform !== Schulform.BK) && (schulform !== Schulform.SB))
			data.jahrgang = props.schuelerListeManager().jahrgaenge.get(props.schuelerListeManager().auswahl().idJahrgang)?.kuerzelStatistik;
		data.istHSUPruefung = hsu;
		data.istFeststellungspruefung = !hsu;
		await props.addSprachpruefung(data);
		selectSprachenPruefungFeststellungErsatz.value.reset();
	}

	async function removePruefungen(hsu: boolean) {
		const list = hsu ? auswahlPrHSU.value : auswahlPrFP.value;
		for (const pruefung of list)
			await props.removeSprachpruefung(pruefung);
		if (hsu)
			auswahlPrHSU.value = []
		else
			auswahlPrFP.value = [];
	}

	const ersetzt = [{key: '1. Pflichtfremdsprache'}, {key: '2. Pflichtfremdsprache'}, {key: 'Wahlpflichtfremdsprache'}];

</script>
