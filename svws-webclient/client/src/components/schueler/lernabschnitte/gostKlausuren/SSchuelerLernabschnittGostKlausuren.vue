<template>
	<div class="w-140">
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
			<svws-ui-table :items="kMan().schuelerklausurGetMengeAsList()" :columns="colsKlausuren">
				<template #cell(quartal)="{ rowData }">
					<span class="mt-3 mb-3">{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}</span>
				</template>
				<template #cell(termin)="{ rowData }">
					<svws-ui-table class="-mb-5" v-if="kMan().terminKursklausurBySchuelerklausur(rowData) !== null && kMan().terminKursklausurBySchuelerklausur(rowData)!.datum !== null" :items="kMan().schuelerklausurterminGetMengeBySchuelerklausur(rowData)" :columns="colsTermine" disable-header>
						<template #cell(datum)="{ rowData: termin }">
							<span class="h-full flex justify-center items-center">{{ kMan().terminOrNullBySchuelerklausurTermin(termin) !== null ? (kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum !== null ? DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum!) : "N.N.") : "N.N." }}</span>
						</template>
						<template #cell(button)="{ rowData: termin }">
							<div class="flex space-x-1 pl-1" v-if="kMan().istSchuelerklausurterminAktuell(termin)">
								<svws-ui-button class="" v-if="kMan().terminOrNullBySchuelerklausurTermin(termin) !== null && kMan().terminOrExceptionBySchuelerklausurTermin(termin).datum !== null" @click="terminSelected = termin; showModalTerminGrund = true">
									<svws-ui-tooltip>
										<template #content>
											Klausur nicht mitgeschrieben
										</template>
										<span class="icon i-ri-user-forbid-line" />
									</svws-ui-tooltip>
								</svws-ui-button>
								<svws-ui-button type="danger" v-if="kMan().schuelerklausurterminGetMengeBySchuelerklausur(rowData).size() > 1" class="mt-4" @click="deleteSchuelerklausurTermin(termin)">
									<svws-ui-tooltip>
										<template #content>
											Nachschreibtermin löschen
										</template>
										<span class="icon i-ri-delete-bin-line" />
									</svws-ui-tooltip>
								</svws-ui-button>
							</div>
							<div v-else>
								<svws-ui-textarea-input :disabled="!patchSchuelerklausurTermin" :rows="1" resizeable="none" autoresize :placeholder="(termin.bemerkung === null || termin.bemerkung!.trim().length === 0) ? 'Kein Grund angegeben' : ''" :model-value="termin.bemerkung" @change="bemerkung => patchSchuelerklausurTermin(termin.id, {bemerkung})" />
							</div>
						</template>
					</svws-ui-table>
					<div v-else>Noch kein Termin gesetzt</div>
				</template>
				<template #cell(kurs)="{ rowData }">
					<span class="mt-3">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausur(rowData)) }}</span>
				</template>
				<template #cell(lehrer)="{ rowData }">
					<span class="mt-3">{{ kMan().kursLehrerKuerzelByKursklausur(kMan().kursklausurBySchuelerklausur(rowData)) }}</span>
				</template>
			</svws-ui-table>
			<div v-if="GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(manager().schuelerGet().abiturjahrgang!, manager().schuljahresabschnittGet().schuljahr, manager().schuljahresabschnittGet().abschnitt) !== null"
				class="mt-2 flex">
				<svws-ui-button type="transparent" size="small" @click="gotoPlanung">
					<span class="icon i-ri-link" /> Planung öffnen
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<div v-else>
			Es ist kein Lernabschnitt der gymnasialen Oberstufe ausgewält.
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { SchuelerLernabschnittGostKlausurenProps } from "./SSchuelerLernabschnittGostKlausurenProps";
	import { GostHalbjahr, GostSchuelerklausurTermin } from "@core";
	import { DateUtils } from "@core";

	const props = defineProps<SchuelerLernabschnittGostKlausurenProps>();

	const showModalTerminGrund = ref<boolean>(false);

	const terminSelected = ref<GostSchuelerklausurTermin>(new GostSchuelerklausurTermin());

	const createTermin = async (create: boolean) => {
		if (create) {
			await props.patchSchuelerklausurTermin(terminSelected.value.id, { bemerkung: terminSelected.value.bemerkung } );
			await props.createSchuelerklausurTermin(terminSelected.value.idSchuelerklausur);
		}
		showModalTerminGrund.value = false;
		terminSelected.value = new GostSchuelerklausurTermin();
	};

	const colsKlausuren: Array<DataTableColumn> = [
		{ key: "quartal", label: "Quartal", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 5},
		{ key: "kurs", label: "Kurs", tooltip: "Kurs" },
		{ key: "lehrer", label: "Fachlehrer", tooltip: "Fachlehrer", fixedWidth: 7 },
		{ key: "termin", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", minWidth: 6 },
	];

	const colsTermine: Array<DataTableColumn> = [
		{ key: "datum", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 8 },
		{ key: "button", label: " ", tooltip: "" },
	];

</script>
