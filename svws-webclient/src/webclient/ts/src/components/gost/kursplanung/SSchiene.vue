<template>
	<tr class="border border-[#7f7f7f]/20 px-2 text-left">
		<!-- Bezeichnung weicht hier ab z.B. S/S statt S/S0 -->
		<td class="px-2">{{ number }}</td>
		<td class="px-2">{{ schiene.id }}</td>
		<td class="w-36 text-center">
			<svws-ui-text-input v-model="bezeichnung"></svws-ui-text-input>
		</td>
		<td class="w-12 px-2 text-center">
			<svws-ui-text-input
				v-model="wochenstunden"
				type="number"
			></svws-ui-text-input>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungSchiene } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		schiene: {
			type: GostBlockungSchiene,
			required: true
		},
		number: {
			type: Number,
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const wochenstunden: WritableComputedRef<number> = computed({
		get(): number {
			return props.schiene.wochenstunden?.valueOf() || 0;
		},
		set(value: number) {
			const schienen = app.dataKursblockung.daten?.schienen;
			if (!schienen) return;
			const index = schienen.indexOf(props.schiene);
			const schiene = schienen.get(index);
			schiene.wochenstunden = Number(value);
			app.dataKursblockung.patch_schiene(schiene);
		}
	});

	const bezeichnung: WritableComputedRef<string> = computed({
		get(): string {
			return props.schiene.bezeichnung.toString();
		},
		set(value: string) {
			const schienen = app.dataKursblockung.daten?.schienen;
			if (!schienen) return;
			const index = schienen.indexOf(props.schiene);
			const schiene = schienen.get(index);
			schiene.bezeichnung = String(value);
			app.dataKursblockung.patch_schiene(schiene);
		}
	});
</script>
