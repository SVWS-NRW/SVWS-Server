
<template>
	<svws-ui-content-card v-if="app.dataKursblockung.daten" class="">
		<div class="flex flex-row">
			<div class="w-full flex-none">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
							<Blockungsregel_9 />
						</div>
						<span class="font-bold">Kurslehrerzuordnung</span>
						<div v-for="kurs in kurse" :key="kurs.hashCode()" class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg mb-1">
							<s-kurslehrer-select :kurs="kurs"/>
						</div>
					</div>
				</div>
			</div>
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
