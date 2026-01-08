<template>
	<div class="w-full">
		<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="small">
			<template #modalTitle>
				Grund für Fehlen angeben
			</template>
			<template #modalContent>
				<svws-ui-text-input focus placeholder="z.B. Krankheit" @update:model-value="bemerkung => terminSelected.bemerkung = bemerkung" @keyup.enter="createTermin(true)" />
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="createTermin(false)"> Abbrechen </svws-ui-button>
				<svws-ui-button type="primary" @click="createTermin(true)"> Nachschreibtermin erstellen </svws-ui-button>
			</template>
		</svws-ui-modal>
		<svws-ui-content-card v-if="hatKlausurManager()">
			<svws-ui-table :items="kMan().schuelerklausurGetMengeAsListSortedByDatumHT()" :columns="colsKlausuren">
				<!-- Quartal -->
				<template #cell(quartal)="{ rowData }">
					<div class="flex items-center min-h-12 font-medium">
						{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}
					</div>
				</template>
				<!-- Termin(e) -->
				<template #cell(termin)="{ rowData }">
					<div v-if="kMan().terminKursklausurBySchuelerklausur(rowData) !== null && kMan().terminKursklausurBySchuelerklausur(rowData)!.datum !== null" class="pl-4 border-l border-slate-300 space-y-2">
						<svws-ui-table :items="kMan().schuelerklausurterminGetMengeBySchuelerklausur(rowData)"
							:columns="colsTermine"
							disable-header
							class="bg-transparent [&_.svws-ui-td]:items-center! [&_.svws-ui-td]:py-1!">
							<!-- Datum -->
							<template #cell(datum)="{ rowData: termin }">
								<span class="text-sm font-mono text-slate-600">
									{{ kMan().terminOrNullBySchuelerklausurTermin(termin) !== null
										? (
											kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum !== null
												? DateUtils.gibDatumGermanFormat(
													kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum!
												)
												: 'N.N.'
										)
										: 'N.N.' }}
								</span>
							</template>
							<!-- Aktionen / Bemerkung -->
							<template #cell(button)="{ rowData: termin }">
								<div v-if="kMan().istSchuelerklausurterminAktuell(termin)" class="flex gap-1">
									<svws-ui-button v-if="kMan().terminOrNullBySchuelerklausurTermin(termin) !== null && kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum !== null" @click="terminSelected = termin; showModalTerminGrund = true">
										<svws-ui-tooltip>
											<template #content>
												Klausur nicht mitgeschrieben
											</template>
											<span class="icon i-ri-user-forbid-line" />
										</svws-ui-tooltip>
									</svws-ui-button>

									<svws-ui-button type="danger" v-if="kMan().schuelerklausurterminGetMengeBySchuelerklausur(rowData).size() > 1" @click="deleteSchuelerklausurTermin(termin)">
										<svws-ui-tooltip>
											<template #content>
												Nachschreibtermin löschen
											</template>
											<span class="icon i-ri-delete-bin-line" />
										</svws-ui-tooltip>
									</svws-ui-button>
								</div>
								<div v-else class="max-w-sm">
									<svws-ui-textarea-input class="-mt-1 -mb-1" size="small" :disabled="!patchSchuelerklausurTermin" :rows="1" resizeable="none" autoresize :placeholder=" (termin.bemerkung === null || termin.bemerkung!.trim().length === 0) ? 'Kein Grund angegeben' : ''" :model-value="termin.bemerkung" @change="bemerkung => patchSchuelerklausurTermin(termin.id, { bemerkung })" />
								</div>
							</template>
						</svws-ui-table>
					</div>
					<div v-else class="flex items-center min-h-12 text-slate-400 italic">
						Noch kein Termin gesetzt
					</div>
				</template>
				<!-- Kurs -->
				<template #cell(kurs)="{ rowData }">
					<div class="flex items-center min-h-12">
						{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausur(rowData)) }}
					</div>
				</template>
				<!-- Lehrer -->
				<template #cell(lehrer)="{ rowData }">
					<div class="flex items-center min-h-12 text-slate-600">
						{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausur(rowData)) }}
					</div>
				</template>
			</svws-ui-table>
			<div v-if="GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(manager().schuelerGet().abiturjahrgang!, manager().schuljahresabschnittGet().schuljahr, manager().schuljahresabschnittGet().abschnitt) !== null" class="mt-3 flex">
				<svws-ui-button type="transparent" size="small" @click="gotoPlanung">
					<span class="icon i-ri-link" />
					Planung öffnen
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<div v-else>
			Es ist kein Lernabschnitt der gymnasialen Oberstufe ausgewählt.
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { SchuelerLernabschnittGostKlausurenProps } from "./SSchuelerLernabschnittGostKlausurenProps";
	import { GostHalbjahr, GostSchuelerklausurTermin, DateUtils } from "@core";

	const props = defineProps<SchuelerLernabschnittGostKlausurenProps>();

	const showModalTerminGrund = ref<boolean>(false);

	const terminSelected = ref<GostSchuelerklausurTermin>(new GostSchuelerklausurTermin());

	const createTermin = async (create: boolean) => {
		if (create) {
			await props.patchSchuelerklausurTermin(terminSelected.value.id, { bemerkung: terminSelected.value.bemerkung });
			const sktNeu = new GostSchuelerklausurTermin();
			sktNeu.idSchuelerklausur = terminSelected.value.idSchuelerklausur;
			await props.createSchuelerklausurTermin(sktNeu);
		}
		showModalTerminGrund.value = false;
		terminSelected.value = new GostSchuelerklausurTermin();
	};

	const colsKlausuren: Array<DataTableColumn> = [
		{ key: "quartal", label: "Quartal", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 5 },
		{ key: "kurs", label: "Kurs", tooltip: "Kurs", fixedWidth: 7 },
		{ key: "lehrer", label: "Fachlehrer", tooltip: "Fachlehrer", fixedWidth: 7 },
		{ key: "termin", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", minWidth: 6 },
	];

	const colsTermine: Array<DataTableColumn> = [
		{ key: "datum", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 8 },
		{ key: "button", label: " ", tooltip: "" },
	];

</script>
