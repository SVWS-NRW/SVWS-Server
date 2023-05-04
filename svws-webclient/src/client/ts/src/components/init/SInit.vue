<template>
	<div class="modal--content">
		<div class="init-form-header mb-8 px-8 py-4 mt-6">
			<h1 class="leading-none text-center w-full">
				<span class="font-normal">Initialisierung der Datenbank</span>
			</h1>
		</div>
		<div class="w-full mt-1 flex flex-col gap-2 items-center px-8">
			Die Datenbank soll initialisiert werden mit einer Auswahl aus dem Schulkatalog, der Migration einer
			bestehenden Schild 2-Datenbank oder aus einem Backup einer SVWS-Datenbank.
			<svws-ui-button type="secondary" @click="goto('schulkatalog')" :class="{'opacity-25 hover:opacity-100': !inputFocus}">Schulkatalog</svws-ui-button>
			<svws-ui-button type="secondary" @click="goto('schild2')" :class="{'opacity-25 hover:opacity-100': !inputFocus}">Schild 2-Datenbank</svws-ui-button>
			<svws-ui-button type="secondary" @click="goto('backup')" :class="{'opacity-25 hover:opacity-100': !inputFocus}">SVWS-Datenbank</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { InitProps } from "./SInitProps";

	const inputFocus = ref(false);

	const props = defineProps<InitProps>()
</script>

<style lang="postcss" scoped>
.init-form-header {
	@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
	font-size: 2.618rem;
}
.init-form .modal--content {
	@apply p-0;
}
</style>
