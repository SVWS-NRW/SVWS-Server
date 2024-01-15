<template>
	<div :id="'gost-klausurtermin-' + termin.id" DELETE_ME_dragover="if (onDropTermin !== undefined) $event.preventDefault();" DELETE_ME_drop="if (onDropTermin !== undefined) onDropTermin(termin);" class="svws-ui-termin h-full flex flex-col group">
		<slot name="header">
			<section class="text-headline-md leading-none px-3 pt-3" :class="{'pb-2': !$slots.tableTitle, 'text-svws': terminSelected}">
				<template v-if="!$slots.tableTitle">
					<slot name="title">
						<span class="leading-tight inline-flex gap-0.5" :class="{'text-base': compact || compactWithDate}">
							<span v-if="dragIcon && !compact" class="group-hover:bg-black/10 dark:group-hover:bg-white/10 -ml-1 mr-0.5 rounded">
								<i-ri-draggable :class="{'text-sm': compact, '-mx-0.5': !compact}" />
							</span>
							<span class="line-clamp-1 break-all">{{ termin.bezeichnung === null ? (kursklausuren().size() ? [...kursklausurmanager().kursklausurGetMengeByTerminid(termin.id)].map(k => props.kursmanager.get(k.idKurs)!.kuerzel!).join(", ") : 'Neuer Termin') : termin.bezeichnung || 'Klausurtermin' }}</span>
						</span>
						<div v-if="compactWithDate && termin.datum" class="mb-1 -mt-0.5 opacity-50 text-base">{{ DateUtils.gibDatumGermanFormat(termin.datum) }}</div>
						<div v-if="compact || compactWithDate" class="svws-compact-data text-sm font-medium flex flex-wrap mt-0.5">
							<span>{{ kursklausurmanager().schuelerklausurAnzahlGetByTerminid(termin.id) }} Schüler<slot name="compactMaximaleDauer">, bis {{ maximaleDauer }} Minuten</slot></span>
							<span v-if="quartalsauswahl && quartalsauswahl.value === 0">, {{ termin.quartal ? termin.quartal + ' . Quartal' : 'Beide Quartale' }}</span>
						</div>
					</slot>
				</template>
				<div class="flex justify-between w-full gap-1 items-center">
					<div v-if="!compact && !compactWithDate">
						<slot name="datum">
							<template v-if="termin.datum === null">
								<span class="opacity-25 inline-flex items-center gap-1">
									<i-ri-calendar-2-line />
									<span>...</span>
								</span>
							</template>
							<span v-else class="opacity-50">{{ DateUtils.gibDatumGermanFormat(termin.datum) }}</span>
						</slot>
					</div>
					<div v-if="$slots.actions" class="flex gap-0.5 items-center -mr-2 -my-1">
						<slot name="actions" />
					</div>
				</div>
			</section>
		</slot>
		<slot name="main" v-if="!compact">
			<section class="px-3 flex flex-col flex-grow" :class="{'mt-2': !$slots.tableTitle}">
				<slot name="klausuren">
					<div v-if="kursklausuren().size() === 0 && (!showSchuelerklausuren || schuelerklausurtermine().size() === 0)">
						Keine Klausuren
					</div>
					<slot name="kursklausuren" v-if="kursklausuren().size()">
						<svws-ui-table :columns="cols" :disable-header="!$slots.tableTitle" :class="{'border-t border-black/25 dark:border-white/25': !$slots.tableTitle}">
							<template #header>
								<div class="svws-ui-tr" role="row">
									<div class="svws-ui-td col-span-full" role="columnheader">
										<slot name="tableTitle" />
									</div>
								</div>
							</template>
							<template #body>
								<div v-for="klausur in kursklausuren()"
									:key="klausur.id"
									:data="klausur"
									:draggable="onDrag !== undefined && draggable(klausur)"
									@dragstart="onDrag && onDrag(klausur);$event.stopPropagation()"
									@dragend="onDrag && onDrag(undefined);$event.stopPropagation()"
									class="svws-ui-tr" role="row" :title="cols.map(c => c.tooltip !== undefined ? c.tooltip : c.label).join(', ')"
									:class="[
										props.klausurCssClasses === undefined ? '' : props.klausurCssClasses(klausur, termin),
										{
											'cursor-grab active:cursor-grabbing group': onDrag !== undefined && (draggable === undefined || draggable(klausur))
										}
									]">
									<div class="svws-ui-td" role="cell">
										<i-ri-draggable v-if="onDrag !== undefined && (draggable === undefined || draggable(klausur))" class="i-ri-draggable -m-0.5 -ml-3" />
										<span class="svws-ui-badge" :class="{'!ml-2': draggable !== undefined && !draggable(klausur)}" :style="`--background-color: ${getBgColor(props.kursmanager.get(klausur.idKurs)!.kuerzel.split('-')[0])};`">{{ props.kursmanager.get(klausur.idKurs)!.kuerzel }}</span>
									</div>
									<div class="svws-ui-td" role="cell">{{ getLehrerKuerzel(klausur.idKurs) }}</div>
									<div class="svws-ui-td svws-align-right" role="cell">{{ klausur.schuelerIds.size() + "/" + props.kursmanager.get(klausur.idKurs)?.schueler.size() || 0 }}</div>
									<div class="svws-ui-td svws-align-right" role="cell">{{ kursklausurmanager().vorgabeByKursklausur(klausur).dauer }}</div>
									<div v-if="showKursschiene === true" class="svws-ui-td svws-align-right"><span class="opacity-50">{{ klausur.kursSchiene.toString() }}</span></div>
									<div v-if="kursklausurmanager().quartalGetByTerminid(termin.id) === -1" class="svws-ui-td svws-align-right" role="cell"><span class="opacity-50">{{ klausur.quartal }}.</span></div>
									<div v-if="showLastKlausurtermin === true" class="svws-ui-td svws-align-right" role="cell"><span class="opacity-50">{{ datumVorklausur(klausur) }}</span></div>
								</div>
							</template>
						</svws-ui-table>
					</slot>
					<slot name="schuelerklausuren" v-if="showSchuelerklausuren && schuelerklausurtermine().size()">
						<s-gost-klausurplanung-schuelerklausur-table :schuelerklausuren="kursklausurmanager().schuelerklausurterminGetMengeByTerminid(termin.id)"
							:kursmanager="kursmanager"
							:kursklausurmanager="kursklausurmanager"
							:on-drag="onDrag"
							:map-lehrer="mapLehrer"
							:map-schueler="mapSchueler"
							:draggable="draggable" />
					</slot>
					<span class="flex w-full justify-between items-center gap-1 text-sm mt-auto">
						<div class="py-3" :class="{'opacity-50': !kursklausuren().size()}">
							<span class="font-bold">{{ kursklausurmanager().schuelerklausurAnzahlGetByTerminid(termin.id) }} Schüler, </span>
							<span>bis {{ maximaleDauer }} Min</span>
						</div>
						<slot name="loeschen" />
					</span>
				</slot>
			</section>
		</slot>
	</div>
</template>

<script setup lang="ts">

	import type { GostKursklausurManager, GostKursklausur, GostKlausurtermin, LehrerListeEintrag, SchuelerListeEintrag, KursManager} from "@core";
	import type { GostKlausurplanungDragData, GostKlausurplanungDropZone } from "./SGostKlausurplanung";
	import type {DataTableColumn} from "@ui";
	import {computed} from "vue";
	import {ZulaessigesFach, DateUtils } from "@core";

	const props = withDefaults(defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
		kursmanager: KursManager;
		klausurCssClasses?: (klausur: GostKursklausur, termin: GostKlausurtermin | undefined) => void;
		onDrag?: (data: GostKlausurplanungDragData) => void;
		draggable?: (data: GostKlausurplanungDragData) => boolean;
		//onDrop?: (zone: GostKlausurplanungDropZone) => void;
		compact?: boolean;
		compactWithDate?: boolean;
		quartalsauswahl?: {value: number};
		dragIcon?: boolean;
		terminSelected?: boolean;
		showKursschiene? : boolean;
		showLastKlausurtermin? : boolean;
		showSchuelerklausuren?: boolean;
	}>(), {
		klausurCssClasses: undefined,
		onDrag: undefined,
		draggable: () => false,
		//onDrop: undefined,
		quartalsauswahl: undefined,
		showSchuelerklausuren: false,
	});

	const kursklausuren = () => props.kursklausurmanager().kursklausurGetMengeByTerminid(props.termin.id);
	const schuelerklausurtermine = () => props.kursklausurmanager().schuelerklausurterminGetMengeByTerminid(props.termin.id);

	const datumVorklausur = (klausur: GostKursklausur) => {
		const vorklausur = props.kursklausurmanager().kursklausurVorterminByKursklausur(klausur);
		if (vorklausur === null)
			return "-";
		const termin = props.kursklausurmanager().terminByKursklausur(vorklausur);
		return termin === null || termin.datum === null ? "-" : DateUtils.gibDatumGermanFormat(termin.datum).substring(0,6);
	};

	function calculateColumns() {
		const cols: DataTableColumn[] = [
			{ key: "kurs", label: "Kurs", span: 1.25 },
			{ key: "kuerzel", label: "Lehrkraft" },
			{ key: "schriftlich", label: "Schriftlich", span: 0.5, align: "right", minWidth: 3.25 },
			{ key: "dauer", label: "Dauer", tooltip: "Dauer in Minuten", span: 0.5, align: "right", minWidth: 3.25 },
		];

		if (props.showKursschiene === true) {
			cols.push({ key: "kursSchiene", label: "S", tooltip: "Schiene", span: 0.25, align: "right", minWidth: 1.75 })
		}

		if (props.kursklausurmanager().quartalGetByTerminid(props.termin.id) === -1) {
			cols.push({ key: "quartal", label: "Q", tooltip: "Quartal", span: 0.25, align: "center", minWidth: 1.75 })
		}

		if (props.showLastKlausurtermin === true) {
			cols.push({ key: "lastDate", label: "Vordatum", tooltip: "Datum der letzten Klausur", span: 0.25, align: "center", minWidth: 4.75 })
		}

		return cols;
	}

	const cols = computed(() => calculateColumns());

	const maximaleDauer = computed(() => {
		let dauer = 0;
		for (const klausur of kursklausuren()) {
			const vorgabe = props.kursklausurmanager().vorgabeByKursklausur(klausur);
			dauer < vorgabe.dauer ? dauer = vorgabe.dauer : dauer;
		}
		return dauer;
	});

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0); // TODO: Fachkuerzel für Kursklausur

	function getLehrerKuerzel(kursid: number) {
		const kurs = props.kursmanager.get(kursid);
		const lehrerid = kurs?.lehrer;
		if (typeof lehrerid === 'number')
			return props.mapLehrer.get(lehrerid)?.kuerzel || ''
		return ''
	}

</script>

<style lang="postcss" scoped>
.svws-warning {
  .i-ri-draggable {
    @apply opacity-10;
  }

  &:hover {
    .i-ri-draggable {
      @apply opacity-100 text-black dark:text-white;
    }
  }
}
</style>

<style lang="postcss">
.svws-ui-termin {
  .text-input--headless {
    @apply text-headline-md;

    &:not(:focus) {
      &::placeholder {
        @apply text-black dark:text-white;
      }
    }

    &::placeholder {
      @apply font-bold;
    }
  }

  .svws-selected & {
    .text-input--headless {
      &:not(:focus) {
        &::placeholder {
          @apply text-svws dark:text-svws;
        }
      }

      &:focus {
        &::placeholder {
          @apply text-svws/50 dark:text-svws/50;
        }
      }
    }
  }

}

.svws-ui-stundenplan--unterricht .svws-ui-termin {
  @apply z-10;

  .px-3 {
    @apply my-auto;
    padding: 0 0.25rem;
  }

  .svws-compact-data {
    @apply justify-center;
  }
}
</style>
