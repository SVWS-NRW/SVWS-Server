<template>
	<div class="py-2 align-middle sm:px-6 lg:px-8">
		<div class="overflow-hidden rounded-lg shadow">
			<table class="w-full border-collapse text-sm">
				<thead class="bg-slate-100">
					<tr>
						<td class="border text-center">Nr</td>
						<td class="border text-center">ID</td>
						<td class="border text-center">Bezeichnung</td>
						<td class="border text-center">Wochenstunden</td>
					</tr>
				</thead>
				<s-schiene v-for="(schiene, i) in schienen" :key="schiene.id" :number="i + 1" :schiene="schiene"></s-schiene>
			</table>
			<div class="flex justify-between">
				<div class="flex gap-4">
					<svws-ui-button type="primary" class="my-4" @click="add_schiene">Schiene hinzufügen</svws-ui-button>
					<svws-ui-button type="danger" class="my-4" @click="del_schiene">Schiene löschen</svws-ui-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { GostBlockungSchiene, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;

const schienen: ComputedRef<Vector<GostBlockungSchiene>> = computed(() => {
	return (
		app.dataKursblockung.daten?.schienen || new Vector<GostBlockungSchiene>()
	);
});

async function add_schiene() {
	const schiene = await app.dataKursblockung.add_blockung_schiene()
	if (!schiene) {
		console.log("Fehler beim Hinzufügen einer Schiene")
		return
	}
}
async function del_schiene() {
	if (!schienen.value.size) return
	const schiene = await app.dataKursblockung.del_blockung_schiene()
	if (!schiene) {
		console.log("Fehler beim Entfernen einer Schiene")
		return
	}
}
</script>
