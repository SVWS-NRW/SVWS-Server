<template>
	<div @dragover="checkDropZone($event)" @drop="onDrop(raum)" class="min-w-120 max-w-120 flex flex-col rounded-xl border bg-ui-contrast-0 h-fit"
		:class="{
			'shadow-lg shadow-ui-contrast-10 border-ui-contrast-10': dragData() === undefined,
			'border-dashed border-ui-brand dark:border-ui-brand ring-4 ring-ui-brand/25': dragData() !== undefined && dragData() instanceof GostKursklausur,
			'border-ui-danger': raumHatFehler(),
			'bg-ui-danger-secondary': raum.idTermin !== terminSelected.id, // TODO Priorität und warum überhaupt???
		}">
		<div class="flex h-full flex-col p-3">
			<div class="svws-raum-title flex justify-between">
				<svws-ui-select :title="raum.idStundenplanRaum ? 'Raum' : 'Raum auswählen...'"
					:model-value="raum.idStundenplanRaum === null ? undefined : kMan().stundenplanraumGetByKlausurraum(raum)"
					:disabled="!hatKompetenzUpdate"
					headless
					class="grow"
					@update:model-value="(value : StundenplanRaum | undefined) => void patchKlausurraum(raum.id, { idStundenplanRaum: value !== undefined ? value.id : null })"
					:item-text="(item: StundenplanRaum) => item !== null ? (item.kuerzel + ' (' + item.groesse+ ' Plätze, ' + item.beschreibung + ')') : ''"
					:items="raeumeVerfuegbar" />
				<span class="inline-flex items-center shrink-0  -my-1">
					<svws-ui-tooltip class="text-ui-danger font-bold text-headline-md" v-if="raumHatFehler()">
						<template #content>
							<template v-if="!raum.idStundenplanRaum">Keine Raumnummer zugeordnet</template>
							<template v-else-if="anzahlSuS() > kMan().stundenplanraumGetByKlausurraum(raum).groesse">Derzeitige Raumbelegung überschreitet die Raumkapazität</template>
						</template>
						<span class="icon icon-ui-danger i-ri-alert-fill" />
					</svws-ui-tooltip>
					<!--<span v-if="multijahrgang()" class="text-button">{{ GostHalbjahr.fromIDorException(kMan().terminGetByIdOrException(raum.idTermin).halbjahr).jahrgang }}</span>-->
					<template v-if="multijahrgang()">
						<span class="border rounded-md p-1 text-button" v-if="raum.idTermin === terminSelected.id">{{ GostHalbjahr.fromIDorException(termin().halbjahr).jahrgang }}</span>
						<svws-ui-button v-else type="secondary" class="p-1" @click="gotoTermin(termin().abijahr, GostHalbjahr.fromIDorException(termin().halbjahr), termin().id)" :title="`Zur Raumplanung des Jahrgangs`" size="small">{{ GostHalbjahr.fromIDorException(termin().halbjahr).jahrgang }}</svws-ui-button>
					</template>
				</span>
			</div>
			<svws-ui-table :items="[]" :columns="cols" :no-data="klausurenImRaum().size() === 0" no-data-text="Noch keine Klausuren zugewiesen." class="mt-4">
				<template #header><span /></template>
				<template #body>
					<div v-for="klausur of klausurenImRaum()" :key="klausur.id" class="svws-ui-tr cursor-grab" role="row" :data="klausur" :draggable="hatKompetenzUpdate" @dragstart="onDrag(klausur)"	@dragend="onDrag(undefined)">
						<div class="svws-ui-td" role="cell">
							<span v-if="hatKompetenzUpdate" class="icon i-ri-draggable -m-0.5 -ml-3" />
						</div>
						<div class="svws-ui-td" role="cell">
							{{ GostHalbjahr.fromIDorException(kMan().vorgabeByKursklausur(klausur).halbjahr).jahrgang }}
						</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-tooltip :hover="false" :indicator="false" autosize>
								<template #content>
									<s-gost-klausurplanung-kursliste :k-man :kursklausur="klausur" :termin="kMan().terminOrNullByKursklausur(klausur)!" :benutzer-kompetenzen />
								</template>
								<span class="svws-ui-badge hover:opacity-75" :style="`background-color: ${ kMan().fachHTMLFarbeRgbaByKursklausur(klausur) };`">{{ kMan().kursKurzbezeichnungByKursklausur(klausur) }}</span>
								<svws-ui-tooltip>
									<template #content>
										Bemerkung: {{ klausur.bemerkung }}
									</template>
									<span class="icon i-ri-edit-2-line icon-ui-brand" v-if="(klausur.bemerkung !== null) && (klausur.bemerkung.trim().length > 0)" />
								</svws-ui-tooltip>
							</svws-ui-tooltip>
						</div>
						<div class="svws-ui-td" role="cell">{{ kMan().kursLehrerKuerzelByKursklausur(klausur) }}</div>
						<div class="svws-ui-td flex" role="cell">
							<div>
								<span v-if="kMan().schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() !== kMan().kursAnzahlKlausurschreiberByKursklausur(klausur)" class="font-bold">{{ kMan().schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() }}/</span>
								<span :class="kMan().schuelerklausurterminGetMengeByRaumAndKursklausur(raum, klausur).size() !== kMan().kursAnzahlKlausurschreiberByKursklausur(klausur) ? 'line-through' : ''">{{ kMan().kursAnzahlKlausurschreiberByKursklausur(klausur) }}/</span>
								<span class="">{{ kMan().kursAnzahlSchuelerGesamtByKursklausur(klausur) }}</span>
							</div>
						</div>
						<div class="svws-ui-td" role="cell">{{ kMan().vorgabeByKursklausur(klausur).dauer }}</div>
						<div class="svws-ui-td" role="cell">
							<svws-ui-text-input :model-value="klausur.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(klausur.startzeit) : ''" headless :placeholder="klausur.startzeit === null ? (kMan().startzeitByKursklausurOrNull(klausur) !== null ? DateUtils.getStringOfUhrzeitFromMinuten(kMan().startzeitByKursklausurOrException(klausur)) + ' Uhr' || 'Startzeit' : '') : 'Individuelle Startzeit'" @change="zeit => patchKlausurbeginn(zeit, klausur)" />
						</div>
					</div>
				</template>
			</svws-ui-table>
			<div class="mt-3">
				<svws-ui-textarea-input class="text-sm" :headless="raum.bemerkung === null || raum.bemerkung.trim().length === 0" :rows="1" resizeable="none" autoresize placeholder="Bemerkungen zum Raum" :disabled="!hatKompetenzUpdate" :model-value="raum.bemerkung" @change="bemerkung => patchKlausurraum(raum.id, {bemerkung})" />
			</div>
			<span class="mt-auto -mb-3 flex w-full items-center justify-between gap-1 text-sm">
				<div class="py-3" :class="{'opacity-50': klausurenImRaum().size() === 0}">
					<span class="font-bold">
						<span v-if="raum.idStundenplanRaum !== null" :class="anzahlSuS() > kMan().stundenplanraumGetByKlausurraum(raum).groesse ? 'text-ui-danger' : ''">{{ anzahlSuS() }}/{{ kMan().stundenplanraumGetByKlausurraum(raum).groesse }} belegt, </span>
						<span v-else>{{ anzahlSuS() }} Plätze, </span>
					</span>
					<span>{{ anzahlRaumstunden }} Raumstunden benötigt</span>
				</div>
				<svws-ui-button type="icon" :disabled="!hatKompetenzUpdate" size="small" class="-mr-1" @click="loescheKlausurraum(raum.id)"><span class="icon i-ri-delete-bin-line" /></svws-ui-button>
			</span>
		</div>
	</div>
</template>


<script setup lang="ts">

	import type { StundenplanRaum, GostKlausurplanManager, GostKlausurenCollectionSkrsKrsData, GostKlausurraum, GostKlausurtermin} from '@core';
	import { BenutzerKompetenz } from '@core';
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from './SGostKlausurplanung';
	import type { DataTableColumn } from "@ui";
	import { GostKursklausur, GostHalbjahr } from '@core';
	import { computed } from 'vue';
	import { DateUtils} from "@core";

	const props = defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>,
		raum: GostKlausurraum;
		kMan: () => GostKlausurplanManager;
		patchKlausurraum: (id: number, raum: Partial<GostKlausurraum>) => Promise<boolean>;
		loescheKlausurraum: (id: number) => Promise<boolean>;
		patchKlausur: (klausur: GostKursklausur, patch: Partial<GostKursklausur>) => Promise<GostKlausurenCollectionSkrsKrsData>;
		dragData: () => GostKlausurplanungDragData;
		onDrag: (data: GostKlausurplanungDragData) => void;
		onDrop: (zone: GostKlausurplanungDropZone) => void;
		multijahrgang: () => boolean;
		terminSelected: GostKlausurtermin;
		gotoTermin: (abiturjahr: number, halbjahr: GostHalbjahr, value: number) => Promise<void>;
		// terminStartzeit?: string;
	}>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN));

	const raumHatFehler = () => (props.raum.idStundenplanRaum !== null && anzahlSuS() > props.kMan().stundenplanraumGetByKlausurraum(props.raum).groesse) || props.raum.idStundenplanRaum === null;

	const klausurenImRaum = () => props.kMan().kursklausurGetMengeByRaum(props.raum);

	const anzahlSuS = () => props.kMan().schuelerklausurterminGetMengeByRaum(props.raum).size();

	const termin = () => props.kMan().terminGetByIdOrException(props.raum.idTermin);

	const anzahlRaumstunden = computed(() => {
		return props.kMan().raumstundeGetMengeByRaum(props.raum).size();
	});

	const raeumeVerfuegbar = computed(() => {
		const raeume = props.kMan().stundenplanraumVerfuegbarGetMengeByTermin(termin(), props.multijahrgang());
		if (props.raum.idStundenplanRaum !== null)
			raeume.add(0, props.kMan().stundenplanraumGetByKlausurraum(props.raum));
		return raeume;
	});

	function isDropZone() : boolean {
		if ((props.dragData() === undefined) || ((props.dragData() instanceof GostKursklausur) && props.kMan().containsKlausurraumKursklausur(props.raum, props.dragData() as GostKursklausur)))
			return false;
		return true;
	}

	function checkDropZone(event: DragEvent) {
		if (isDropZone())
			event.preventDefault();
	}

	async function patchKlausurbeginn(event: string | null, klausur: GostKursklausur) {
		if (event === null)
			return;
		try {
			const startzeit = event.trim().length > 0 ? DateUtils.gibMinutenOfZeitAsString(event) : null;
			const result = await props.patchKlausur(klausur, {startzeit});
			props.kMan().setzeRaumZuSchuelerklausuren(result);
		} catch(e) {
			// Do nothing
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "dragHandle", label: " ", fixedWidth: 1 },
		{ key: "jgst", label: "Jgst.", fixedWidth: 2 },
		{ key: "kurs", label: "Kurs", span: 1.5 },
		{ key: "kuerzel", label: "Lehrkraft", span: 0.75 },
		{ key: "schriftlich", label: "Schriftlich", span: 0.5, minWidth: 4.75 },
		{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.25, minWidth: 4 },
		{ key: "startzeit", label: "Startzeit", span: 1.25, minWidth: 4 },
	];

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: 1rem 2rem minmax(4rem, 1.5fr) minmax(4rem, 0.75fr) minmax(4.75rem, 0.5fr) minmax(4rem, 0.25fr) minmax(4rem, 1.25fr) ;
	}

</style>