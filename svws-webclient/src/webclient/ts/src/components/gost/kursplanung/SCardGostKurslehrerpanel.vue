
<template>
	<svws-ui-content-card v-if="app.dataKursblockung.daten" class="">
				<div class="w-96 py-2 align-middle sm:px-6 lg:px-8">
						<svws-ui-table :data="kurse.toArray()" :columns="[{label: 'Kurs'}, {label: 'Kurslehrer'}, {label: 'Aktionen'}]">
							<template #body="{rows}">
								<template v-for="kurs in <GostBlockungKurs[]>rows" :key="kurs.id">
									<s-kurslehrer-select :kurs="kurs"/>
								</template>
							</template>
						</svws-ui-table>
				</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
import { List, GostBlockungKurs, Vector } from "@svws-nrw/svws-core-ts";
import { ComputedRef, computed } from "vue";
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;

const kurse: ComputedRef<List<GostBlockungKurs>> =
	computed(() => app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector())
</script>
