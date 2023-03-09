<template>
	<td class="text-center"> {{ unterricht.fachKuerzel }} </td>
</template>

<script setup lang="ts">

	import { SchuelerStundenplanUnterricht } from "@svws-nrw/svws-core";

	const props = defineProps({
		unterricht: { type: Object as () => SchuelerStundenplanUnterricht, required: true }
	});

</script>
