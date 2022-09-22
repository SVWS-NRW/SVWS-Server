<template>
	<div class="py-2 align-middle sm:px-6 lg:px-8">
		<div class="max-h-screen overflow-auto rounded-lg shadow">
			<table class="w-full border-collapse text-sm">
				<thead class="bg-slate-100">
					<tr>
						<td class="border text-center">Kursbezeichnung</td>
						<td class="border text-center">Koop</td>
						<td class="border text-center">Suffix</td>
						<td class="border text-center">Wochenstunden</td>
					</tr>
				</thead>
				<s-kurs
					v-for="(kurs, i) in kurse"
					:key="i"
					:kurs="kurs"
					:halbjahr="
						app?.blockungsauswahl.ausgewaehlt?.gostHalbjahr || 0
					"
				></s-kurs>
			</table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungKurs } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const bezeichnung: ComputedRef<string | undefined> = computed(() => {
		return app?.auswahl.ausgewaehlt?.bezeichnung?.toString();
	});

	const kurse: ComputedRef<Array<GostBlockungKurs> | undefined> = computed(
		() => {
			return (
				app?.dataKursblockung.daten?.kurse?.toArray(
					new Array<GostBlockungKurs>()
				) || new Array<GostBlockungKurs>()
			);
		}
	);
</script>
