
<template>
	<div v-if="app.dataKursblockung.daten" class="space-y-3">
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_1 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_6 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_2 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_3 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_7 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_8 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_4 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_5 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_9 />
		</div>
	</div>
</template>

<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;
</script>
