<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{ 'bg-red-400': kollision, 'bg-blue-400': selected }"
	>
		<td class="px-2">{{ schueler.nachname }}, {{ schueler.vorname }}</td>
	</tr>
</template>

<script setup lang="ts">
	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";

	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		schueler: {
			type: SchuelerListeEintrag,
			required: true
		},
		kollision: { type: Boolean, required: true },
		selected: { type: Boolean, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;
</script>
