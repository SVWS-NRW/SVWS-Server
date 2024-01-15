<template>
	<div class="content">
		<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="small">
			<template #modalTitle>
				Grund für Fehlen angeben
			</template>
			<template #modalContent>
				<svws-ui-textarea-input placeholder="z.B. Krankheit" @change="bemerkung => terminSelected.bemerkung = bemerkung" />
			</template>
			<template #modalActions>
				<svws-ui-button type="secondary" @click="createTermin(false)"> Abbrechen </svws-ui-button>
				<svws-ui-button type="primary" @click="createTermin(true)"> Nachschreibtermin erstellen </svws-ui-button>
			</template>
		</svws-ui-modal>
		<svws-ui-content-card title="Klausuren" v-if="hatKlausurManager()">
			<svws-ui-table :items="klausurManager().schuelerklausurGetMengeAsList()" :columns="colsKlausuren">
				<template #cell(quartal)="{ rowData }">
					{{ klausurManager().vorgabeBySchuelerklausur(rowData).quartal }}
				</template>
				<template #cell(termin)="{ rowData }">
					<svws-ui-table v-if="klausurManager().terminKursklausurBySchuelerklausur(rowData) !== null && klausurManager().terminKursklausurBySchuelerklausur(rowData)!.datum !== null" :items="klausurManager().schuelerklausurterminGetMengeBySchuelerklausur(rowData)" :columns="colsTermine" disable-header>
						<template #cell(datum)="{ rowData: termin }">
							{{ klausurManager().terminBySchuelerklausurTermin(termin) !== null ? (klausurManager().terminBySchuelerklausurTermin(termin)!.datum !== null ? DateUtils.gibDatumGermanFormat(klausurManager().terminBySchuelerklausurTermin(termin)!.datum!) : "N.N.") : "N.N." }}
						</template>
						<template #cell(button)="{ rowData: termin }">
							<div class="flex space-x-1" v-if="klausurManager().istAktuellerSchuelerklausurtermin(termin)">
								<svws-ui-button class="mt-4" v-if="klausurManager().terminBySchuelerklausurTermin(termin) !== null && klausurManager().terminBySchuelerklausurTermin(termin)!.datum !== null" @click="terminSelected = termin; showModalTerminGrund().value = true">
									<svws-ui-tooltip>
										<template #content>
											Klausur nicht mitgeschrieben
										</template>
										<i-ri-user-forbid-line />
									</svws-ui-tooltip>
								</svws-ui-button>
								<svws-ui-button type="danger" v-if="klausurManager().schuelerklausurterminGetMengeBySchuelerklausur(rowData).size() > 1" class="mt-4" @click="deleteSchuelerklausurTermin(termin)">
									<svws-ui-tooltip>
										<template #content>
											Nachschreibtermin löschen
										</template>
										<i-ri-delete-bin-line />
									</svws-ui-tooltip>
								</svws-ui-button>
							</div>
							<div v-else>
								{{ termin.bemerkung }}
							</div>
						</template>
					</svws-ui-table>
					<div v-else>Noch kein Termin gesetzt</div>
				</template>
				<template #cell(kurs)="{ rowData }">
					{{ manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).kuerzel }}
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).lehrer !== null ? manager().lehrerGetByIdOrException(manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).lehrer!).kuerzel : "N.N." }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<div v-else>
			Kein Gost-Lernabschnitt ausgewählt.
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import { GostSchuelerklausurTermin } from "@core";
	import { DateUtils  } from "@core";
	import type { SchuelerLernabschnittGostKlausurenProps } from "./SSchuelerLernabschnittGostKlausurenProps";
	import type { Ref} from "vue";
	import { ref } from "vue";

	const props = defineProps<SchuelerLernabschnittGostKlausurenProps>();

	const _showModalTerminGrund = ref<boolean>(false);
	const showModalTerminGrund = () => _showModalTerminGrund;

	let terminSelected: Ref<GostSchuelerklausurTermin> = ref(new GostSchuelerklausurTermin());

	const createTermin = async (create: boolean) => {
		if (create && terminSelected.value !== undefined) {
			await props.patchSchuelerklausurTermin(terminSelected.value.id, { bemerkung: terminSelected.value.bemerkung } );
			await props.createSchuelerklausurTermin(terminSelected.value.idSchuelerklausur);
		}
		showModalTerminGrund().value = false;
		terminSelected.value = new GostSchuelerklausurTermin();
	};

	const colsKlausuren: Array<DataTableColumn> = [
		{ key: "quartal", label: "Quartal", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 5},
		{ key: "kurs", label: "Kurs", tooltip: "Kurs", fixedWidth: 5 },
		{ key: "lehrer", label: "Fachlehrer", tooltip: "Fachlehrer", fixedWidth: 5 },
		{ key: "termin", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", minWidth: 6 },
	];

	const colsTermine: Array<DataTableColumn> = [
		{ key: "datum", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", fixedWidth: 8 },
		{ key: "button", label: " ", tooltip: "" },
	];

</script>


<style lang="postcss" scoped>

	.content {
		@apply w-full h-full grid grid-cols-2;
	}

</style>
