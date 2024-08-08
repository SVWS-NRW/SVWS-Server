<template>
	<svws-ui-modal v-if="showModalTerminGrund" :show="showModalTerminGrund" size="small">
		<template #modalTitle>
			Grund für Fehlen angeben
		</template>
		<template #modalContent>
			<svws-ui-text-input focus placeholder="z.B. Krankheit" @change="bemerkung => terminSelected.bemerkung = bemerkung" @keyup.enter="createTermin(true)" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="createTermin(false)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="createTermin(true)"> Nachschreibtermin erstellen </svws-ui-button>
		</template>
	</svws-ui-modal>


	<h3 class="border-b text-headline-md">Klausurschreiber im Kurs {{ kMan().kursKurzbezeichnungByKursklausur(kursklausur) }}</h3>
	<table>
		<tr v-for="s in kMan().schuelerklausurterminGetMengeByKursklausur(kursklausur)" :key="s.id">
			<td>
				<template v-if="termin !== undefined && !kMan().schuelerSchreibtKlausurtermin(kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).id, termin)">
					<span class="line-through text-red-500">
						{{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).vorname }}
					</span>
					<span class="italic"> ({{ (s.bemerkung !== null && s.bemerkung.trim().length > 0) ? s.bemerkung : "kein Grund" }})</span>
				</template>
				<span v-else>
					{{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).vorname }}
				</span>
			</td>
			<td v-if="patchKlausur && createSchuelerklausurTermin">
				<svws-ui-button v-if="termin !== undefined && kMan().schuelerSchreibtKlausurtermin(kMan().schuelerlisteeintragGetBySchuelerklausurtermin(s).id, termin)" @click="terminSelected = s; showModalTerminGrund().value = true">
					<svws-ui-tooltip>
						<template #content>
							Klausur nicht mitgeschrieben
						</template>
						<span class="icon i-ri-user-forbid-line" />
					</svws-ui-tooltip>
				</svws-ui-button>
			</td>
		</tr>
	</table>
</template>

<script setup lang="ts">
	import type { GostKlausurplanManager, GostKursklausur, GostKlausurtermin, GostKlausurenCollectionSkrsKrsData} from '@core';
	import { GostSchuelerklausurTermin } from '@core';
	import type { Ref} from 'vue';
	import { ref } from 'vue';

	const props = withDefaults(defineProps<{
		kMan: () => GostKlausurplanManager;
		kursklausur: GostKursklausur;
		termin?: GostKlausurtermin;
		createSchuelerklausurTermin?: (id: number) => Promise<void>;
		patchKlausur?: (klausur: GostKursklausur | GostSchuelerklausurTermin, patch: Partial<GostKursklausur | GostSchuelerklausurTermin>) => Promise<GostKlausurenCollectionSkrsKrsData>;
	}>(), {
		termin: undefined,
		createSchuelerklausurTermin: undefined,
		patchKlausur: undefined,
	});

	const _showModalTerminGrund = ref<boolean>(false);
	const showModalTerminGrund = () => _showModalTerminGrund;

	let terminSelected: Ref<GostSchuelerklausurTermin> = ref(new GostSchuelerklausurTermin());

	const createTermin = async (create: boolean) => {
		if (props.patchKlausur && props.createSchuelerklausurTermin && create && terminSelected.value !== undefined) {
			await props.patchKlausur(terminSelected.value, { bemerkung: terminSelected.value.bemerkung } );
			await props.createSchuelerklausurTermin(terminSelected.value.idSchuelerklausur);
		}
		showModalTerminGrund().value = false;
		terminSelected.value = new GostSchuelerklausurTermin();
	};


</script>