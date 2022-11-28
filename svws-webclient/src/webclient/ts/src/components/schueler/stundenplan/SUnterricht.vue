<template>
    <td class="text-center">
        {{unterricht.fachKuerzel}}
    </td>
</template>

<script setup lang="ts">
	import { BenutzergruppenManager, BenutzerKompetenz, SchuelerStundenplanUnterricht } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

    const props = defineProps({
        unterricht: { type: Object as () => SchuelerStundenplanUnterricht, required: true }
    });

	const main: Main = injectMainApp();
	const app = main.apps.schueler;


</script>
